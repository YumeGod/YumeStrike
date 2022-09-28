package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class KernSubtableFormat0 extends KernSubtable {
   private int nPairs;
   private int searchRange;
   private int entrySelector;
   private int rangeShift;
   private KerningPair[] kerningPairs;

   protected KernSubtableFormat0(RandomAccessFile var1) throws IOException {
      this.nPairs = var1.readUnsignedShort();
      this.searchRange = var1.readUnsignedShort();
      this.entrySelector = var1.readUnsignedShort();
      this.rangeShift = var1.readUnsignedShort();
      this.kerningPairs = new KerningPair[this.nPairs];

      for(int var2 = 0; var2 < this.nPairs; ++var2) {
         this.kerningPairs[var2] = new KerningPair(var1);
      }

   }

   public int getKerningPairCount() {
      return this.nPairs;
   }

   public KerningPair getKerningPair(int var1) {
      return this.kerningPairs[var1];
   }
}
