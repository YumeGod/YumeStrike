package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ClassDefFormat1 extends ClassDef {
   private int startGlyph;
   private int glyphCount;
   private int[] classValues;

   public ClassDefFormat1(RandomAccessFile var1) throws IOException {
      this.startGlyph = var1.readUnsignedShort();
      this.glyphCount = var1.readUnsignedShort();
      this.classValues = new int[this.glyphCount];

      for(int var2 = 0; var2 < this.glyphCount; ++var2) {
         this.classValues[var2] = var1.readUnsignedShort();
      }

   }

   public int getFormat() {
      return 1;
   }
}
