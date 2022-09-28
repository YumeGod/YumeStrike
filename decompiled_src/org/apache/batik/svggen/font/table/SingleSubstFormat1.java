package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class SingleSubstFormat1 extends SingleSubst {
   private int coverageOffset;
   private short deltaGlyphID;
   private Coverage coverage;

   protected SingleSubstFormat1(RandomAccessFile var1, int var2) throws IOException {
      this.coverageOffset = var1.readUnsignedShort();
      this.deltaGlyphID = var1.readShort();
      var1.seek((long)(var2 + this.coverageOffset));
      this.coverage = Coverage.read(var1);
   }

   public int getFormat() {
      return 1;
   }

   public int substitute(int var1) {
      int var2 = this.coverage.findGlyph(var1);
      return var2 > -1 ? var1 + this.deltaGlyphID : var1;
   }
}
