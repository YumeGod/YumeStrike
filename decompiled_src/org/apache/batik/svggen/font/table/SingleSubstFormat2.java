package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class SingleSubstFormat2 extends SingleSubst {
   private int coverageOffset;
   private int glyphCount;
   private int[] substitutes;
   private Coverage coverage;

   protected SingleSubstFormat2(RandomAccessFile var1, int var2) throws IOException {
      this.coverageOffset = var1.readUnsignedShort();
      this.glyphCount = var1.readUnsignedShort();
      this.substitutes = new int[this.glyphCount];

      for(int var3 = 0; var3 < this.glyphCount; ++var3) {
         this.substitutes[var3] = var1.readUnsignedShort();
      }

      var1.seek((long)(var2 + this.coverageOffset));
      this.coverage = Coverage.read(var1);
   }

   public int getFormat() {
      return 2;
   }

   public int substitute(int var1) {
      int var2 = this.coverage.findGlyph(var1);
      return var2 > -1 ? this.substitutes[var2] : var1;
   }
}
