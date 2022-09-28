package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CoverageFormat1 extends Coverage {
   private int glyphCount;
   private int[] glyphIds;

   protected CoverageFormat1(RandomAccessFile var1) throws IOException {
      this.glyphCount = var1.readUnsignedShort();
      this.glyphIds = new int[this.glyphCount];

      for(int var2 = 0; var2 < this.glyphCount; ++var2) {
         this.glyphIds[var2] = var1.readUnsignedShort();
      }

   }

   public int getFormat() {
      return 1;
   }

   public int findGlyph(int var1) {
      for(int var2 = 0; var2 < this.glyphCount; ++var2) {
         if (this.glyphIds[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }
}
