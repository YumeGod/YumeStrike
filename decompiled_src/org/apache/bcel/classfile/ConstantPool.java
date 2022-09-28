package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;

public class ConstantPool implements Cloneable, Node {
   private int constant_pool_count;
   private Constant[] constant_pool;

   public ConstantPool(Constant[] constant_pool) {
      this.setConstantPool(constant_pool);
   }

   ConstantPool(DataInputStream file) throws IOException, ClassFormatError {
      this.constant_pool_count = file.readUnsignedShort();
      this.constant_pool = new Constant[this.constant_pool_count];

      for(int i = 1; i < this.constant_pool_count; ++i) {
         this.constant_pool[i] = Constant.readConstant(file);
         byte tag = this.constant_pool[i].getTag();
         if (tag == 6 || tag == 5) {
            ++i;
         }
      }

   }

   public void accept(Visitor v) {
      v.visitConstantPool(this);
   }

   public String constantToString(Constant c) throws ClassFormatError {
      byte tag = c.getTag();
      String str;
      int i;
      switch (tag) {
         case 1:
            str = ((ConstantUtf8)c).getBytes();
            break;
         case 2:
         default:
            throw new RuntimeException("Unknown constant type " + tag);
         case 3:
            str = "" + ((ConstantInteger)c).getBytes();
            break;
         case 4:
            str = "" + ((ConstantFloat)c).getBytes();
            break;
         case 5:
            str = "" + ((ConstantLong)c).getBytes();
            break;
         case 6:
            str = "" + ((ConstantDouble)c).getBytes();
            break;
         case 7:
            i = ((ConstantClass)c).getNameIndex();
            c = this.getConstant(i, (byte)1);
            str = Utility.compactClassName(((ConstantUtf8)c).getBytes(), false);
            break;
         case 8:
            i = ((ConstantString)c).getStringIndex();
            c = this.getConstant(i, (byte)1);
            str = "\"" + escape(((ConstantUtf8)c).getBytes()) + "\"";
            break;
         case 9:
         case 10:
         case 11:
            str = this.constantToString(((ConstantCP)c).getClassIndex(), (byte)7) + "." + this.constantToString(((ConstantCP)c).getNameAndTypeIndex(), (byte)12);
            break;
         case 12:
            str = this.constantToString(((ConstantNameAndType)c).getNameIndex(), (byte)1) + " " + this.constantToString(((ConstantNameAndType)c).getSignatureIndex(), (byte)1);
      }

      return str;
   }

   private static final String escape(String str) {
      int len = str.length();
      StringBuffer buf = new StringBuffer(len + 5);
      char[] ch = str.toCharArray();

      for(int i = 0; i < len; ++i) {
         switch (ch[i]) {
            case '\b':
               buf.append("\\b");
               break;
            case '\t':
               buf.append("\\t");
               break;
            case '\n':
               buf.append("\\n");
               break;
            case '\r':
               buf.append("\\r");
               break;
            case '"':
               buf.append("\\\"");
               break;
            default:
               buf.append(ch[i]);
         }
      }

      return buf.toString();
   }

   public String constantToString(int index, byte tag) throws ClassFormatError {
      Constant c = this.getConstant(index, tag);
      return this.constantToString(c);
   }

   public void dump(DataOutputStream file) throws IOException {
      file.writeShort(this.constant_pool_count);

      for(int i = 1; i < this.constant_pool_count; ++i) {
         if (this.constant_pool[i] != null) {
            this.constant_pool[i].dump(file);
         }
      }

   }

   public Constant getConstant(int index) {
      if (index < this.constant_pool.length && index >= 0) {
         return this.constant_pool[index];
      } else {
         throw new ClassFormatError("Invalid constant pool reference: " + index + ". Constant pool size is: " + this.constant_pool.length);
      }
   }

   public Constant getConstant(int index, byte tag) throws ClassFormatError {
      Constant c = this.getConstant(index);
      if (c == null) {
         throw new ClassFormatError("Constant pool at index " + index + " is null.");
      } else if (c.getTag() == tag) {
         return c;
      } else {
         throw new ClassFormatError("Expected class `" + Constants.CONSTANT_NAMES[tag] + "' at index " + index + " and got " + c);
      }
   }

   public Constant[] getConstantPool() {
      return this.constant_pool;
   }

   public String getConstantString(int index, byte tag) throws ClassFormatError {
      Constant c = this.getConstant(index, tag);
      int i;
      switch (tag) {
         case 7:
            i = ((ConstantClass)c).getNameIndex();
            break;
         case 8:
            i = ((ConstantString)c).getStringIndex();
            break;
         default:
            throw new RuntimeException("getConstantString called with illegal tag " + tag);
      }

      c = this.getConstant(i, (byte)1);
      return ((ConstantUtf8)c).getBytes();
   }

   public int getLength() {
      return this.constant_pool_count;
   }

   public void setConstant(int index, Constant constant) {
      this.constant_pool[index] = constant;
   }

   public void setConstantPool(Constant[] constant_pool) {
      this.constant_pool = constant_pool;
      this.constant_pool_count = constant_pool == null ? 0 : constant_pool.length;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();

      for(int i = 1; i < this.constant_pool_count; ++i) {
         buf.append(i + ")" + this.constant_pool[i] + "\n");
      }

      return buf.toString();
   }

   public ConstantPool copy() {
      ConstantPool c = null;

      try {
         c = (ConstantPool)this.clone();
      } catch (CloneNotSupportedException var3) {
      }

      c.constant_pool = new Constant[this.constant_pool_count];

      for(int i = 1; i < this.constant_pool_count; ++i) {
         if (this.constant_pool[i] != null) {
            c.constant_pool[i] = this.constant_pool[i].copy();
         }
      }

      return c;
   }
}
