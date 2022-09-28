package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RangeRecord {
   private int start;
   private int end;
   private int startCoverageIndex;

   public RangeRecord(RandomAccessFile var1) throws IOException {
      this.start = var1.readUnsignedShort();
      this.end = var1.readUnsignedShort();
      this.startCoverageIndex = var1.readUnsignedShort();
   }

   public boolean isInRange(int var1) {
      return this.start <= var1 && var1 <= this.end;
   }

   public int getCoverageIndex(int var1) {
      return this.isInRange(var1) ? this.startCoverageIndex + var1 - this.start : -1;
   }
}
