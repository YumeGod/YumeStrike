package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CmapFormat6 extends CmapFormat {
   private short format = 6;
   private short length;
   private short version;
   private short firstCode;
   private short entryCount;
   private short[] glyphIdArray;

   protected CmapFormat6(RandomAccessFile var1) throws IOException {
      super(var1);
   }

   public int getFirst() {
      return 0;
   }

   public int getLast() {
      return 0;
   }

   public int mapCharCode(int var1) {
      return 0;
   }
}
