package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class LangSys {
   private int lookupOrder;
   private int reqFeatureIndex;
   private int featureCount;
   private int[] featureIndex;

   protected LangSys(RandomAccessFile var1) throws IOException {
      this.lookupOrder = var1.readUnsignedShort();
      this.reqFeatureIndex = var1.readUnsignedShort();
      this.featureCount = var1.readUnsignedShort();
      this.featureIndex = new int[this.featureCount];

      for(int var2 = 0; var2 < this.featureCount; ++var2) {
         this.featureIndex[var2] = var1.readUnsignedShort();
      }

   }

   protected boolean isFeatureIndexed(int var1) {
      for(int var2 = 0; var2 < this.featureCount; ++var2) {
         if (this.featureIndex[var2] == var1) {
            return true;
         }
      }

      return false;
   }
}
