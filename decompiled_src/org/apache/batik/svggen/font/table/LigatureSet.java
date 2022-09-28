package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class LigatureSet {
   private int ligatureCount;
   private int[] ligatureOffsets;
   private Ligature[] ligatures;

   public LigatureSet(RandomAccessFile var1, int var2) throws IOException {
      var1.seek((long)var2);
      this.ligatureCount = var1.readUnsignedShort();
      this.ligatureOffsets = new int[this.ligatureCount];
      this.ligatures = new Ligature[this.ligatureCount];

      int var3;
      for(var3 = 0; var3 < this.ligatureCount; ++var3) {
         this.ligatureOffsets[var3] = var1.readUnsignedShort();
      }

      for(var3 = 0; var3 < this.ligatureCount; ++var3) {
         var1.seek((long)(var2 + this.ligatureOffsets[var3]));
         this.ligatures[var3] = new Ligature(var1);
      }

   }
}
