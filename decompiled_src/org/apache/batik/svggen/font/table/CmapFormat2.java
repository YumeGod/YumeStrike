package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CmapFormat2 extends CmapFormat {
   private short[] subHeaderKeys = new short[256];
   private int[] subHeaders1;
   private int[] subHeaders2;
   private short[] glyphIndexArray;

   protected CmapFormat2(RandomAccessFile var1) throws IOException {
      super(var1);
      this.format = 2;
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
