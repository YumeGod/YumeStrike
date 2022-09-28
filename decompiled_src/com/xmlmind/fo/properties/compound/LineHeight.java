package com.xmlmind.fo.properties.compound;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;

public class LineHeight extends Space {
   public LineHeight(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public Value compute(Value var1, Context var2) {
      double var3;
      switch (var1.type) {
         case 3:
            var3 = var1.number() * var2.fontSize();
            var1 = new Value((byte)4, 4, var3);
            break;
         case 13:
            var3 = var1.percentage() * var2.fontSize() / 100.0;
            var1 = new Value((byte)4, 4, var3);
            break;
         default:
            var1 = super.compute(var1, var2);
      }

      return var1;
   }
}
