package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FpgmTable extends Program implements Table {
   protected FpgmTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      this.readInstructions(var2, var1.getLength());
   }

   public int getType() {
      return 1718642541;
   }
}
