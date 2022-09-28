package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class LigatureSubstFormat1 extends LigatureSubst {
   private int coverageOffset;
   private int ligSetCount;
   private int[] ligatureSetOffsets;
   private Coverage coverage;
   private LigatureSet[] ligatureSets;

   protected LigatureSubstFormat1(RandomAccessFile var1, int var2) throws IOException {
      this.coverageOffset = var1.readUnsignedShort();
      this.ligSetCount = var1.readUnsignedShort();
      this.ligatureSetOffsets = new int[this.ligSetCount];
      this.ligatureSets = new LigatureSet[this.ligSetCount];

      int var3;
      for(var3 = 0; var3 < this.ligSetCount; ++var3) {
         this.ligatureSetOffsets[var3] = var1.readUnsignedShort();
      }

      var1.seek((long)(var2 + this.coverageOffset));
      this.coverage = Coverage.read(var1);

      for(var3 = 0; var3 < this.ligSetCount; ++var3) {
         this.ligatureSets[var3] = new LigatureSet(var1, var2 + this.ligatureSetOffsets[var3]);
      }

   }

   public int getFormat() {
      return 1;
   }
}
