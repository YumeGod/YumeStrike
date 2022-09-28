package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Device {
   private int startSize;
   private int endSize;
   private int deltaFormat;
   private int[] deltaValues;

   public Device(RandomAccessFile var1) throws IOException {
      this.startSize = var1.readUnsignedShort();
      this.endSize = var1.readUnsignedShort();
      this.deltaFormat = var1.readUnsignedShort();
      int var2 = this.startSize - this.endSize;
      switch (this.deltaFormat) {
         case 1:
            var2 = var2 % 8 == 0 ? var2 / 8 : var2 / 8 + 1;
            break;
         case 2:
            var2 = var2 % 4 == 0 ? var2 / 4 : var2 / 4 + 1;
            break;
         case 3:
            var2 = var2 % 2 == 0 ? var2 / 2 : var2 / 2 + 1;
      }

      this.deltaValues = new int[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.deltaValues[var3] = var1.readUnsignedShort();
      }

   }
}
