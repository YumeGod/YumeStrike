package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class ClassDef {
   public abstract int getFormat();

   protected static ClassDef read(RandomAccessFile var0) throws IOException {
      Object var1 = null;
      int var2 = var0.readUnsignedShort();
      if (var2 == 1) {
         var1 = new ClassDefFormat1(var0);
      } else if (var2 == 2) {
         var1 = new ClassDefFormat2(var0);
      }

      return (ClassDef)var1;
   }
}
