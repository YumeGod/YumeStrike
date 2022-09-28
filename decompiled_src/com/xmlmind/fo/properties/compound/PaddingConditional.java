package com.xmlmind.fo.properties.compound;

import com.xmlmind.fo.properties.Value;

public class PaddingConditional extends LengthConditional {
   public PaddingConditional(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public static double length(Value var0) {
      double var1 = 0.0;
      switch (var0.type) {
         case 4:
            var1 = var0.length();
            break;
         case 6:
            Value[] var3 = var0.lengthConditional();
            var1 = var3[0].length();
      }

      return var1;
   }
}
