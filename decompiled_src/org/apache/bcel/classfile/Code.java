package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Code extends Attribute {
   private int max_stack;
   private int max_locals;
   private int code_length;
   private byte[] code;
   private int exception_table_length;
   private CodeException[] exception_table;
   private int attributes_count;
   private Attribute[] attributes;

   public Code(Code c) {
      this(c.getNameIndex(), c.getLength(), c.getMaxStack(), c.getMaxLocals(), c.getCode(), c.getExceptionTable(), c.getAttributes(), c.getConstantPool());
   }

   Code(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
      this(name_index, length, file.readUnsignedShort(), file.readUnsignedShort(), (byte[])null, (CodeException[])null, (Attribute[])null, constant_pool);
      this.code_length = file.readInt();
      this.code = new byte[this.code_length];
      file.readFully(this.code);
      this.exception_table_length = file.readUnsignedShort();
      this.exception_table = new CodeException[this.exception_table_length];

      for(int i = 0; i < this.exception_table_length; ++i) {
         this.exception_table[i] = new CodeException(file);
      }

      this.attributes_count = file.readUnsignedShort();
      this.attributes = new Attribute[this.attributes_count];

      for(int i = 0; i < this.attributes_count; ++i) {
         this.attributes[i] = Attribute.readAttribute(file, constant_pool);
      }

      super.length = length;
   }

   public Code(int name_index, int length, int max_stack, int max_locals, byte[] code, CodeException[] exception_table, Attribute[] attributes, ConstantPool constant_pool) {
      super((byte)2, name_index, length, constant_pool);
      this.max_stack = max_stack;
      this.max_locals = max_locals;
      this.setCode(code);
      this.setExceptionTable(exception_table);
      this.setAttributes(attributes);
   }

   public void accept(Visitor v) {
      v.visitCode(this);
   }

   public final void dump(DataOutputStream file) throws IOException {
      super.dump(file);
      file.writeShort(this.max_stack);
      file.writeShort(this.max_locals);
      file.writeInt(this.code_length);
      file.write(this.code, 0, this.code_length);
      file.writeShort(this.exception_table_length);

      for(int i = 0; i < this.exception_table_length; ++i) {
         this.exception_table[i].dump(file);
      }

      file.writeShort(this.attributes_count);

      for(int i = 0; i < this.attributes_count; ++i) {
         this.attributes[i].dump(file);
      }

   }

   public final Attribute[] getAttributes() {
      return this.attributes;
   }

   public LineNumberTable getLineNumberTable() {
      for(int i = 0; i < this.attributes_count; ++i) {
         if (this.attributes[i] instanceof LineNumberTable) {
            return (LineNumberTable)this.attributes[i];
         }
      }

      return null;
   }

   public LocalVariableTable getLocalVariableTable() {
      for(int i = 0; i < this.attributes_count; ++i) {
         if (this.attributes[i] instanceof LocalVariableTable) {
            return (LocalVariableTable)this.attributes[i];
         }
      }

      return null;
   }

   public final byte[] getCode() {
      return this.code;
   }

   public final CodeException[] getExceptionTable() {
      return this.exception_table;
   }

   public final int getMaxLocals() {
      return this.max_locals;
   }

   public final int getMaxStack() {
      return this.max_stack;
   }

   private final int getInternalLength() {
      return 8 + this.code_length + 2 + 8 * this.exception_table_length + 2;
   }

   private final int calculateLength() {
      int len = 0;

      for(int i = 0; i < this.attributes_count; ++i) {
         len += this.attributes[i].length + 6;
      }

      return len + this.getInternalLength();
   }

   public final void setAttributes(Attribute[] attributes) {
      this.attributes = attributes;
      this.attributes_count = attributes == null ? 0 : attributes.length;
      super.length = this.calculateLength();
   }

   public final void setCode(byte[] code) {
      this.code = code;
      this.code_length = code == null ? 0 : code.length;
   }

   public final void setExceptionTable(CodeException[] exception_table) {
      this.exception_table = exception_table;
      this.exception_table_length = exception_table == null ? 0 : exception_table.length;
   }

   public final void setMaxLocals(int max_locals) {
      this.max_locals = max_locals;
   }

   public final void setMaxStack(int max_stack) {
      this.max_stack = max_stack;
   }

   public final String toString(boolean verbose) {
      StringBuffer buf = new StringBuffer("Code(max_stack = " + this.max_stack + ", max_locals = " + this.max_locals + ", code_length = " + this.code_length + ")\n" + Utility.codeToString(this.code, super.constant_pool, 0, -1, verbose));
      int i;
      if (this.exception_table_length > 0) {
         buf.append("\nException handler(s) = \nFrom\tTo\tHandler\tType\n");

         for(i = 0; i < this.exception_table_length; ++i) {
            buf.append(this.exception_table[i].toString(super.constant_pool, verbose) + "\n");
         }
      }

      if (this.attributes_count > 0) {
         buf.append("\nAttribute(s) = \n");

         for(i = 0; i < this.attributes_count; ++i) {
            buf.append(this.attributes[i].toString() + "\n");
         }
      }

      return buf.toString();
   }

   public final String toString() {
      return this.toString(true);
   }

   public Attribute copy(ConstantPool constant_pool) {
      Code c = (Code)this.clone();
      c.code = (byte[])this.code.clone();
      c.constant_pool = constant_pool;
      c.exception_table = new CodeException[this.exception_table_length];

      for(int i = 0; i < this.exception_table_length; ++i) {
         c.exception_table[i] = this.exception_table[i].copy();
      }

      c.attributes = new Attribute[this.attributes_count];

      for(int i = 0; i < this.attributes_count; ++i) {
         c.attributes[i] = this.attributes[i].copy(constant_pool);
      }

      return c;
   }
}
