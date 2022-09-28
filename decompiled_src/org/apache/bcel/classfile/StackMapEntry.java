package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class StackMapEntry implements Cloneable {
   private int byte_code_offset;
   private int number_of_locals;
   private StackMapType[] types_of_locals;
   private int number_of_stack_items;
   private StackMapType[] types_of_stack_items;
   private ConstantPool constant_pool;

   StackMapEntry(DataInputStream file, ConstantPool constant_pool) throws IOException {
      this(file.readShort(), file.readShort(), (StackMapType[])null, -1, (StackMapType[])null, constant_pool);
      this.types_of_locals = new StackMapType[this.number_of_locals];

      for(int i = 0; i < this.number_of_locals; ++i) {
         this.types_of_locals[i] = new StackMapType(file, constant_pool);
      }

      this.number_of_stack_items = file.readShort();
      this.types_of_stack_items = new StackMapType[this.number_of_stack_items];

      for(int i = 0; i < this.number_of_stack_items; ++i) {
         this.types_of_stack_items[i] = new StackMapType(file, constant_pool);
      }

   }

   public StackMapEntry(int byte_code_offset, int number_of_locals, StackMapType[] types_of_locals, int number_of_stack_items, StackMapType[] types_of_stack_items, ConstantPool constant_pool) {
      this.byte_code_offset = byte_code_offset;
      this.number_of_locals = number_of_locals;
      this.types_of_locals = types_of_locals;
      this.number_of_stack_items = number_of_stack_items;
      this.types_of_stack_items = types_of_stack_items;
      this.constant_pool = constant_pool;
   }

   public final void dump(DataOutputStream file) throws IOException {
      file.writeShort(this.byte_code_offset);
      file.writeShort(this.number_of_locals);

      for(int i = 0; i < this.number_of_locals; ++i) {
         this.types_of_locals[i].dump(file);
      }

      file.writeShort(this.number_of_stack_items);

      for(int i = 0; i < this.number_of_stack_items; ++i) {
         this.types_of_stack_items[i].dump(file);
      }

   }

   public final String toString() {
      StringBuffer buf = new StringBuffer("(offset=" + this.byte_code_offset);
      int i;
      if (this.number_of_locals > 0) {
         buf.append(", locals={");

         for(i = 0; i < this.number_of_locals; ++i) {
            buf.append(this.types_of_locals[i]);
            if (i < this.number_of_locals - 1) {
               buf.append(", ");
            }
         }

         buf.append("}");
      }

      if (this.number_of_stack_items > 0) {
         buf.append(", stack items={");

         for(i = 0; i < this.number_of_stack_items; ++i) {
            buf.append(this.types_of_stack_items[i]);
            if (i < this.number_of_stack_items - 1) {
               buf.append(", ");
            }
         }

         buf.append("}");
      }

      buf.append(")");
      return buf.toString();
   }

   public void setByteCodeOffset(int b) {
      this.byte_code_offset = b;
   }

   public int getByteCodeOffset() {
      return this.byte_code_offset;
   }

   public void setNumberOfLocals(int n) {
      this.number_of_locals = n;
   }

   public int getNumberOfLocals() {
      return this.number_of_locals;
   }

   public void setTypesOfLocals(StackMapType[] t) {
      this.types_of_locals = t;
   }

   public StackMapType[] getTypesOfLocals() {
      return this.types_of_locals;
   }

   public void setNumberOfStackItems(int n) {
      this.number_of_stack_items = n;
   }

   public int getNumberOfStackItems() {
      return this.number_of_stack_items;
   }

   public void setTypesOfStackItems(StackMapType[] t) {
      this.types_of_stack_items = t;
   }

   public StackMapType[] getTypesOfStackItems() {
      return this.types_of_stack_items;
   }

   public StackMapEntry copy() {
      try {
         return (StackMapEntry)this.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public void accept(Visitor v) {
      v.visitStackMapEntry(this);
   }

   public final ConstantPool getConstantPool() {
      return this.constant_pool;
   }

   public final void setConstantPool(ConstantPool constant_pool) {
      this.constant_pool = constant_pool;
   }
}
