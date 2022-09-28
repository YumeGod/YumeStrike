package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class KernSubtable {
   protected KernSubtable() {
   }

   public abstract int getKerningPairCount();

   public abstract KerningPair getKerningPair(int var1);

   public static KernSubtable read(RandomAccessFile var0) throws IOException {
      Object var1 = null;
      var0.readUnsignedShort();
      var0.readUnsignedShort();
      int var2 = var0.readUnsignedShort();
      int var3 = var2 >> 8;
      switch (var3) {
         case 0:
            var1 = new KernSubtableFormat0(var0);
            break;
         case 2:
            var1 = new KernSubtableFormat2(var0);
      }

      return (KernSubtable)var1;
   }
}
