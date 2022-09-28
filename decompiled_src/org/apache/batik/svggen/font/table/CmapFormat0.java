package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CmapFormat0 extends CmapFormat {
   private int[] glyphIdArray = new int[256];
   private int first;
   private int last;

   protected CmapFormat0(RandomAccessFile var1) throws IOException {
      super(var1);
      this.format = 0;
      this.first = -1;

      for(int var2 = 0; var2 < 256; ++var2) {
         this.glyphIdArray[var2] = var1.readUnsignedByte();
         if (this.glyphIdArray[var2] > 0) {
            if (this.first == -1) {
               this.first = var2;
            }

            this.last = var2;
         }
      }

   }

   public int getFirst() {
      return this.first;
   }

   public int getLast() {
      return this.last;
   }

   public int mapCharCode(int var1) {
      return 0 <= var1 && var1 < 256 ? this.glyphIdArray[var1] : 0;
   }
}
