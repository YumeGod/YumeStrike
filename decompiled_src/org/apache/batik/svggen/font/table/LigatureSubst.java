package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class LigatureSubst extends LookupSubtable {
   public static LigatureSubst read(RandomAccessFile var0, int var1) throws IOException {
      LigatureSubstFormat1 var2 = null;
      var0.seek((long)var1);
      int var3 = var0.readUnsignedShort();
      if (var3 == 1) {
         var2 = new LigatureSubstFormat1(var0, var1);
      }

      return var2;
   }
}
