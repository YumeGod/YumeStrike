package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class PrepTable extends Program implements Table {
   public PrepTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      this.readInstructions(var2, var1.getLength());
   }

   public int getType() {
      return 1886545264;
   }
}
