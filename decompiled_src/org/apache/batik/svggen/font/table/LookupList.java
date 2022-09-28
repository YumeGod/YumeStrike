package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class LookupList {
   private int lookupCount;
   private int[] lookupOffsets;
   private Lookup[] lookups;

   public LookupList(RandomAccessFile var1, int var2, LookupSubtableFactory var3) throws IOException {
      var1.seek((long)var2);
      this.lookupCount = var1.readUnsignedShort();
      this.lookupOffsets = new int[this.lookupCount];
      this.lookups = new Lookup[this.lookupCount];

      int var4;
      for(var4 = 0; var4 < this.lookupCount; ++var4) {
         this.lookupOffsets[var4] = var1.readUnsignedShort();
      }

      for(var4 = 0; var4 < this.lookupCount; ++var4) {
         this.lookups[var4] = new Lookup(var3, var1, var2 + this.lookupOffsets[var4]);
      }

   }

   public Lookup getLookup(Feature var1, int var2) {
      if (var1.getLookupCount() > var2) {
         int var3 = var1.getLookupListIndex(var2);
         return this.lookups[var3];
      } else {
         return null;
      }
   }
}
