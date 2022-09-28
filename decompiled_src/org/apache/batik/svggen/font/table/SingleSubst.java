package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class SingleSubst extends LookupSubtable {
   public abstract int getFormat();

   public abstract int substitute(int var1);

   public static SingleSubst read(RandomAccessFile var0, int var1) throws IOException {
      Object var2 = null;
      var0.seek((long)var1);
      int var3 = var0.readUnsignedShort();
      if (var3 == 1) {
         var2 = new SingleSubstFormat1(var0, var1);
      } else if (var3 == 2) {
         var2 = new SingleSubstFormat2(var0, var1);
      }

      return (SingleSubst)var2;
   }
}
