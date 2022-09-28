package org.apache.bcel.classfile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Signature extends Attribute {
   private int signature_index;

   public Signature(Signature c) {
      this(c.getNameIndex(), c.getLength(), c.getSignatureIndex(), c.getConstantPool());
   }

   Signature(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
      this(name_index, length, file.readUnsignedShort(), constant_pool);
   }

   public Signature(int name_index, int length, int signature_index, ConstantPool constant_pool) {
      super((byte)10, name_index, length, constant_pool);
      this.signature_index = signature_index;
   }

   public void accept(Visitor v) {
      System.err.println("Visiting non-standard Signature object");
   }

   public final void dump(DataOutputStream file) throws IOException {
      super.dump(file);
      file.writeShort(this.signature_index);
   }

   public final int getSignatureIndex() {
      return this.signature_index;
   }

   public final void setSignatureIndex(int signature_index) {
      this.signature_index = signature_index;
   }

   public final String getSignature() {
      ConstantUtf8 c = (ConstantUtf8)super.constant_pool.getConstant(this.signature_index, (byte)1);
      return c.getBytes();
   }

   private static boolean identStart(int ch) {
      return ch == 84 || ch == 76;
   }

   private static boolean identPart(int ch) {
      return ch == 47 || ch == 59;
   }

   private static final void matchIdent(MyByteArrayInputStream in, StringBuffer buf) {
      int ch;
      if ((ch = in.read()) == -1) {
         throw new RuntimeException("Illegal signature: " + in.getData() + " no ident, reaching EOF");
      } else {
         StringBuffer buf2;
         if (!identStart(ch)) {
            buf2 = new StringBuffer();

            int count;
            for(count = 1; Character.isJavaIdentifierPart((char)ch); ch = in.read()) {
               buf2.append((char)ch);
               ++count;
            }

            if (ch == 58) {
               in.skip((long)"Ljava/lang/Object".length());
               buf.append(buf2);
               ch = in.read();
               in.unread();
            } else {
               for(int i = 0; i < count; ++i) {
                  in.unread();
               }
            }

         } else {
            buf2 = new StringBuffer();
            ch = in.read();

            do {
               buf2.append((char)ch);
               ch = in.read();
            } while(ch != -1 && (Character.isJavaIdentifierPart((char)ch) || ch == 47));

            buf.append(buf2.toString().replace('/', '.'));
            if (ch != -1) {
               in.unread();
            }

         }
      }
   }

   private static final void matchGJIdent(MyByteArrayInputStream in, StringBuffer buf) {
      matchIdent(in, buf);
      int ch = in.read();
      if (ch != 60 && ch != 40) {
         in.unread();
      } else {
         buf.append((char)ch);
         matchGJIdent(in, buf);

         while((ch = in.read()) != 62 && ch != 41) {
            if (ch == -1) {
               throw new RuntimeException("Illegal signature: " + in.getData() + " reaching EOF");
            }

            buf.append(", ");
            in.unread();
            matchGJIdent(in, buf);
         }

         buf.append((char)ch);
      }

      ch = in.read();
      if (identStart(ch)) {
         in.unread();
         matchGJIdent(in, buf);
      } else {
         if (ch == 41) {
            in.unread();
            return;
         }

         if (ch != 59) {
            throw new RuntimeException("Illegal signature: " + in.getData() + " read " + (char)ch);
         }
      }

   }

   public static String translate(String s) {
      StringBuffer buf = new StringBuffer();
      matchGJIdent(new MyByteArrayInputStream(s), buf);
      return buf.toString();
   }

   public static final boolean isFormalParameterList(String s) {
      return s.startsWith("<") && s.indexOf(58) > 0;
   }

   public static final boolean isActualParameterList(String s) {
      return s.startsWith("L") && s.endsWith(">;");
   }

   public final String toString() {
      String s = this.getSignature();
      return "Signature(" + s + ")";
   }

   public Attribute copy(ConstantPool constant_pool) {
      return (Signature)this.clone();
   }

   private static final class MyByteArrayInputStream extends ByteArrayInputStream {
      MyByteArrayInputStream(String data) {
         super(data.getBytes());
      }

      final int mark() {
         return super.pos;
      }

      final String getData() {
         return new String(super.buf);
      }

      final void reset(int p) {
         super.pos = p;
      }

      final void unread() {
         if (super.pos > 0) {
            --super.pos;
         }

      }
   }
}
