package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class TableDirectory {
   private int version = 0;
   private short numTables = 0;
   private short searchRange = 0;
   private short entrySelector = 0;
   private short rangeShift = 0;
   private DirectoryEntry[] entries;

   public TableDirectory(RandomAccessFile var1) throws IOException {
      this.version = var1.readInt();
      this.numTables = var1.readShort();
      this.searchRange = var1.readShort();
      this.entrySelector = var1.readShort();
      this.rangeShift = var1.readShort();
      this.entries = new DirectoryEntry[this.numTables];

      for(int var2 = 0; var2 < this.numTables; ++var2) {
         this.entries[var2] = new DirectoryEntry(var1);
      }

      boolean var5 = true;

      while(var5) {
         var5 = false;

         for(int var3 = 0; var3 < this.numTables - 1; ++var3) {
            if (this.entries[var3].getOffset() > this.entries[var3 + 1].getOffset()) {
               DirectoryEntry var4 = this.entries[var3];
               this.entries[var3] = this.entries[var3 + 1];
               this.entries[var3 + 1] = var4;
               var5 = true;
            }
         }
      }

   }

   public DirectoryEntry getEntry(int var1) {
      return this.entries[var1];
   }

   public DirectoryEntry getEntryByTag(int var1) {
      for(int var2 = 0; var2 < this.numTables; ++var2) {
         if (this.entries[var2].getTag() == var1) {
            return this.entries[var2];
         }
      }

      return null;
   }

   public short getEntrySelector() {
      return this.entrySelector;
   }

   public short getNumTables() {
      return this.numTables;
   }

   public short getRangeShift() {
      return this.rangeShift;
   }

   public short getSearchRange() {
      return this.searchRange;
   }

   public int getVersion() {
      return this.version;
   }
}
