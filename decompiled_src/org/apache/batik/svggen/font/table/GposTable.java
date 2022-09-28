package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class GposTable implements Table {
   protected GposTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      var2.readInt();
      var2.readInt();
      var2.readInt();
      var2.readInt();
   }

   public int getType() {
      return 1196445523;
   }

   public String toString() {
      return "GPOS";
   }
}
