package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class StackMap extends Attribute implements Node {
   private int map_length;
   private StackMapEntry[] map;

   public StackMap(int name_index, int length, StackMapEntry[] map, ConstantPool constant_pool) {
      super((byte)11, name_index, length, constant_pool);
      this.setStackMap(map);
   }

   StackMap(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
      this(name_index, length, (StackMapEntry[])null, constant_pool);
      this.map_length = file.readUnsignedShort();
      this.map = new StackMapEntry[this.map_length];

      for(int i = 0; i < this.map_length; ++i) {
         this.map[i] = new StackMapEntry(file, constant_pool);
      }

   }

   public final void dump(DataOutputStream file) throws IOException {
      super.dump(file);
      file.writeShort(this.map_length);

      for(int i = 0; i < this.map_length; ++i) {
         this.map[i].dump(file);
      }

   }

   public final StackMapEntry[] getStackMap() {
      return this.map;
   }

   public final void setStackMap(StackMapEntry[] map) {
      this.map = map;
      this.map_length = map == null ? 0 : map.length;
   }

   public final String toString() {
      StringBuffer buf = new StringBuffer("StackMap(");

      for(int i = 0; i < this.map_length; ++i) {
         buf.append(this.map[i].toString());
         if (i < this.map_length - 1) {
            buf.append(", ");
         }
      }

      buf.append(')');
      return buf.toString();
   }

   public Attribute copy(ConstantPool constant_pool) {
      StackMap c = (StackMap)this.clone();
      c.map = new StackMapEntry[this.map_length];

      for(int i = 0; i < this.map_length; ++i) {
         c.map[i] = this.map[i].copy();
      }

      c.constant_pool = constant_pool;
      return c;
   }

   public void accept(Visitor v) {
      v.visitStackMap(this);
   }

   public final int getMapLength() {
      return this.map_length;
   }
}
