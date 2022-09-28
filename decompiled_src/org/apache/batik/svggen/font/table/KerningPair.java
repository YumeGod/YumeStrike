package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class KerningPair {
   private int left;
   private int right;
   private short value;

   protected KerningPair(RandomAccessFile var1) throws IOException {
      this.left = var1.readUnsignedShort();
      this.right = var1.readUnsignedShort();
      this.value = var1.readShort();
   }

   public int getLeft() {
      return this.left;
   }

   public int getRight() {
      return this.right;
   }

   public short getValue() {
      return this.value;
   }
}
