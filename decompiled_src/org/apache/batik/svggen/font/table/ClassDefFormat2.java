package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ClassDefFormat2 extends ClassDef {
   private int classRangeCount;
   private RangeRecord[] classRangeRecords;

   public ClassDefFormat2(RandomAccessFile var1) throws IOException {
      this.classRangeCount = var1.readUnsignedShort();
      this.classRangeRecords = new RangeRecord[this.classRangeCount];

      for(int var2 = 0; var2 < this.classRangeCount; ++var2) {
         this.classRangeRecords[var2] = new RangeRecord(var1);
      }

   }

   public int getFormat() {
      return 2;
   }
}
