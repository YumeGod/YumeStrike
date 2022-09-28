package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class NameTable implements Table {
   private short formatSelector;
   private short numberOfNameRecords;
   private short stringStorageOffset;
   private NameRecord[] records;

   protected NameTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      this.formatSelector = var2.readShort();
      this.numberOfNameRecords = var2.readShort();
      this.stringStorageOffset = var2.readShort();
      this.records = new NameRecord[this.numberOfNameRecords];

      int var3;
      for(var3 = 0; var3 < this.numberOfNameRecords; ++var3) {
         this.records[var3] = new NameRecord(var2);
      }

      for(var3 = 0; var3 < this.numberOfNameRecords; ++var3) {
         this.records[var3].loadString(var2, var1.getOffset() + this.stringStorageOffset);
      }

   }

   public String getRecord(short var1) {
      for(int var2 = 0; var2 < this.numberOfNameRecords; ++var2) {
         if (this.records[var2].getNameId() == var1) {
            return this.records[var2].getRecordString();
         }
      }

      return "";
   }

   public int getType() {
      return 1851878757;
   }
}
