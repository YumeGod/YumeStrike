package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class KernTable implements Table {
   private int version;
   private int nTables;
   private KernSubtable[] tables;

   protected KernTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      this.version = var2.readUnsignedShort();
      this.nTables = var2.readUnsignedShort();
      this.tables = new KernSubtable[this.nTables];

      for(int var3 = 0; var3 < this.nTables; ++var3) {
         this.tables[var3] = KernSubtable.read(var2);
      }

   }

   public int getSubtableCount() {
      return this.nTables;
   }

   public KernSubtable getSubtable(int var1) {
      return this.tables[var1];
   }

   public int getType() {
      return 1801810542;
   }
}
