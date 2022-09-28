package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Ligature {
   private int ligGlyph;
   private int compCount;
   private int[] components;

   public Ligature(RandomAccessFile var1) throws IOException {
      this.ligGlyph = var1.readUnsignedShort();
      this.compCount = var1.readUnsignedShort();
      this.components = new int[this.compCount - 1];

      for(int var2 = 0; var2 < this.compCount - 1; ++var2) {
         this.components[var2] = var1.readUnsignedShort();
      }

   }

   public int getGlyphCount() {
      return this.compCount;
   }

   public int getGlyphId(int var1) {
      return var1 == 0 ? this.ligGlyph : this.components[var1 - 1];
   }
}
