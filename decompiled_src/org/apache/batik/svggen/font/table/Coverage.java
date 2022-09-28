package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class Coverage {
   public abstract int getFormat();

   public abstract int findGlyph(int var1);

   protected static Coverage read(RandomAccessFile var0) throws IOException {
      Object var1 = null;
      int var2 = var0.readUnsignedShort();
      if (var2 == 1) {
         var1 = new CoverageFormat1(var0);
      } else if (var2 == 2) {
         var1 = new CoverageFormat2(var0);
      }

      return (Coverage)var1;
   }
}
