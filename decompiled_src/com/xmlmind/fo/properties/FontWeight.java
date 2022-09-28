package com.xmlmind.fo.properties;

import com.xmlmind.fo.converter.Context;

public class FontWeight extends Property {
   public FontWeight(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Value integer(String var1) {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var4) {
         return null;
      }

      return var2 >= 100 && var2 <= 900 && var2 % 100 == 0 ? new Value((byte)2, var2) : null;
   }

   public Value compute(Value var1, Context var2) {
      if (var1.type == 1) {
         int var3 = var1.keyword();
         if (var3 == 88) {
            return super.compute(var1, var2);
         }

         int var4;
         switch (var3) {
            case 24:
               var4 = 700;
               break;
            case 25:
            case 104:
               Context var5 = var2.parent();
               if (var5 == null) {
                  var1 = this.initialValue;
               } else {
                  var1 = var5.properties.values[111];
               }

               var4 = var1.integer();
               if (var3 == 25) {
                  var4 = Math.min(var4 + 100, 900);
               } else {
                  var4 = Math.max(var4 - 100, 100);
               }
               break;
            case 127:
            default:
               var4 = 400;
         }

         var1 = new Value((byte)2, var4);
      } else {
         var1 = super.compute(var1, var2);
      }

      return var1;
   }
}
