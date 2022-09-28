package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class KernSubtableFormat2 extends KernSubtable {
   private int rowWidth;
   private int leftClassTable;
   private int rightClassTable;
   private int array;

   protected KernSubtableFormat2(RandomAccessFile var1) throws IOException {
      this.rowWidth = var1.readUnsignedShort();
      this.leftClassTable = var1.readUnsignedShort();
      this.rightClassTable = var1.readUnsignedShort();
      this.array = var1.readUnsignedShort();
   }

   public int getKerningPairCount() {
      return 0;
   }

   public KerningPair getKerningPair(int var1) {
      return null;
   }
}
