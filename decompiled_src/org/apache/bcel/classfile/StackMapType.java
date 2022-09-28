package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;

public final class StackMapType implements Cloneable {
   private byte type;
   private int index;
   private ConstantPool constant_pool;

   StackMapType(DataInputStream file, ConstantPool constant_pool) throws IOException {
      this(file.readByte(), -1, constant_pool);
      if (this.hasIndex()) {
         this.setIndex(file.readShort());
      }

      this.setConstantPool(constant_pool);
   }

   public StackMapType(byte type, int index, ConstantPool constant_pool) {
      this.index = -1;
      this.setType(type);
      this.setIndex(index);
      this.setConstantPool(constant_pool);
   }

   public void setType(byte t) {
      if (t >= 0 && t <= 8) {
         this.type = t;
      } else {
         throw new RuntimeException("Illegal type for StackMapType: " + t);
      }
   }

   public byte getType() {
      return this.type;
   }

   public void setIndex(int t) {
      this.index = t;
   }

   public int getIndex() {
      return this.index;
   }

   public final void dump(DataOutputStream file) throws IOException {
      file.writeByte(this.type);
      if (this.hasIndex()) {
         file.writeShort(this.getIndex());
      }

   }

   public final boolean hasIndex() {
      return this.type == 7 || this.type == 8;
   }

   private String printIndex() {
      if (this.type == 7) {
         return ", class=" + this.constant_pool.constantToString(this.index, (byte)7);
      } else {
         return this.type == 8 ? ", offset=" + this.index : "";
      }
   }

   public final String toString() {
      return "(type=" + Constants.ITEM_NAMES[this.type] + this.printIndex() + ")";
   }

   public StackMapType copy() {
      try {
         return (StackMapType)this.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public final ConstantPool getConstantPool() {
      return this.constant_pool;
   }

   public final void setConstantPool(ConstantPool constant_pool) {
      this.constant_pool = constant_pool;
   }
}
