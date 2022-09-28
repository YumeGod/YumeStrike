package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FeatureList {
   private int featureCount;
   private FeatureRecord[] featureRecords;
   private Feature[] features;

   public FeatureList(RandomAccessFile var1, int var2) throws IOException {
      var1.seek((long)var2);
      this.featureCount = var1.readUnsignedShort();
      this.featureRecords = new FeatureRecord[this.featureCount];
      this.features = new Feature[this.featureCount];

      int var3;
      for(var3 = 0; var3 < this.featureCount; ++var3) {
         this.featureRecords[var3] = new FeatureRecord(var1);
      }

      for(var3 = 0; var3 < this.featureCount; ++var3) {
         this.features[var3] = new Feature(var1, var2 + this.featureRecords[var3].getOffset());
      }

   }

   public Feature findFeature(LangSys var1, String var2) {
      if (var2.length() != 4) {
         return null;
      } else {
         int var3 = var2.charAt(0) << 24 | var2.charAt(1) << 16 | var2.charAt(2) << 8 | var2.charAt(3);

         for(int var4 = 0; var4 < this.featureCount; ++var4) {
            if (this.featureRecords[var4].getTag() == var3 && var1.isFeatureIndexed(var4)) {
               return this.features[var4];
            }
         }

         return null;
      }
   }
}
