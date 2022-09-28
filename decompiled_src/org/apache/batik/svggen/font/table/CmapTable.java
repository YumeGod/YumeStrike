package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CmapTable implements Table {
   private int version;
   private int numTables;
   private CmapIndexEntry[] entries;
   private CmapFormat[] formats;

   protected CmapTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      long var3 = var2.getFilePointer();
      this.version = var2.readUnsignedShort();
      this.numTables = var2.readUnsignedShort();
      this.entries = new CmapIndexEntry[this.numTables];
      this.formats = new CmapFormat[this.numTables];

      int var5;
      for(var5 = 0; var5 < this.numTables; ++var5) {
         this.entries[var5] = new CmapIndexEntry(var2);
      }

      for(var5 = 0; var5 < this.numTables; ++var5) {
         var2.seek(var3 + (long)this.entries[var5].getOffset());
         int var6 = var2.readUnsignedShort();
         this.formats[var5] = CmapFormat.create(var6, var2);
      }

   }

   public CmapFormat getCmapFormat(short var1, short var2) {
      for(int var3 = 0; var3 < this.numTables; ++var3) {
         if (this.entries[var3].getPlatformId() == var1 && this.entries[var3].getEncodingId() == var2) {
            return this.formats[var3];
         }
      }

      return null;
   }

   public int getType() {
      return 1668112752;
   }

   public String toString() {
      StringBuffer var1 = (new StringBuffer(this.numTables * 8)).append("cmap\n");

      int var2;
      for(var2 = 0; var2 < this.numTables; ++var2) {
         var1.append('\t').append(this.entries[var2].toString()).append('\n');
      }

      for(var2 = 0; var2 < this.numTables; ++var2) {
         var1.append('\t').append(this.formats[var2].toString()).append('\n');
      }

      return var1.toString();
   }
}
