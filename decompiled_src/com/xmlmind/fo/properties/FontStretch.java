package com.xmlmind.fo.properties;

import com.xmlmind.fo.converter.Context;

public class FontStretch extends Property {
   private int[] scale = new int[]{210, 59, 39, 175, 127, 176, 58, 60, 211};

   public FontStretch(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public Value compute(Value var1, Context var2) {
      if (var1.type == 1) {
         int var3 = var1.keyword();
         if (var3 == 88) {
            return super.compute(var1, var2);
         }

         if (var3 == 223 || var3 == 123) {
            Context var5 = var2.parent();
            if (var5 == null) {
               var1 = this.initialValue;
            } else {
               var1 = var5.properties.values[108];
            }

            int var4;
            for(var4 = 0; var4 < this.scale.length && this.scale[var4] != var1.keyword(); ++var4) {
            }

            if (var3 == 223) {
               var4 = Math.min(var4 + 1, this.scale.length - 1);
            } else {
               var4 = Math.max(var4 - 1, 0);
            }

            var1 = new Value((byte)1, this.scale[var4]);
         }
      } else {
         var1 = super.compute(var1, var2);
      }

      return var1;
   }
}
