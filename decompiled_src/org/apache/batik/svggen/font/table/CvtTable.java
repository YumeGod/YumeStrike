package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CvtTable implements Table {
   private short[] values;

   protected CvtTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      int var3 = var1.getLength() / 2;
      this.values = new short[var3];

      for(int var4 = 0; var4 < var3; ++var4) {
         this.values[var4] = var2.readShort();
      }

   }

   public int getType() {
      return 1668707360;
   }

   public short[] getValues() {
      return this.values;
   }
}
