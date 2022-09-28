package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Feature {
   private int featureParams;
   private int lookupCount;
   private int[] lookupListIndex;

   protected Feature(RandomAccessFile var1, int var2) throws IOException {
      var1.seek((long)var2);
      this.featureParams = var1.readUnsignedShort();
      this.lookupCount = var1.readUnsignedShort();
      this.lookupListIndex = new int[this.lookupCount];

      for(int var3 = 0; var3 < this.lookupCount; ++var3) {
         this.lookupListIndex[var3] = var1.readUnsignedShort();
      }

   }

   public int getLookupCount() {
      return this.lookupCount;
   }

   public int getLookupListIndex(int var1) {
      return this.lookupListIndex[var1];
   }
}
