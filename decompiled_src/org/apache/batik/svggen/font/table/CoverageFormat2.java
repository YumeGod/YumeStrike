package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CoverageFormat2 extends Coverage {
   private int rangeCount;
   private RangeRecord[] rangeRecords;

   protected CoverageFormat2(RandomAccessFile var1) throws IOException {
      this.rangeCount = var1.readUnsignedShort();
      this.rangeRecords = new RangeRecord[this.rangeCount];

      for(int var2 = 0; var2 < this.rangeCount; ++var2) {
         this.rangeRecords[var2] = new RangeRecord(var1);
      }

   }

   public int getFormat() {
      return 2;
   }

   public int findGlyph(int var1) {
      for(int var2 = 0; var2 < this.rangeCount; ++var2) {
         int var3 = this.rangeRecords[var2].getCoverageIndex(var1);
         if (var3 > -1) {
            return var3;
         }
      }

      return -1;
   }
}
