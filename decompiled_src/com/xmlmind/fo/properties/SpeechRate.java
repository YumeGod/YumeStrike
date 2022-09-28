package com.xmlmind.fo.properties;

import com.xmlmind.fo.converter.Context;

public class SpeechRate extends Property {
   public static final double MEDIUM = 180.0;

   public SpeechRate(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public Value compute(Value var1, Context var2) {
      if (var1.type == 1) {
         int var3 = var1.keyword();
         if (var3 == 88) {
            return super.compute(var1, var2);
         }

         double var4;
         switch (var3) {
            case 64:
               var4 = 300.0;
               break;
            case 65:
            case 181:
               Context var6 = var2.parent();
               if (var6 == null) {
                  var1 = this.initialValue;
               } else {
                  var1 = var6.properties.values[275];
               }

               var4 = var1.number();
               if (var3 == 65) {
                  var4 += 40.0;
               } else {
                  var4 -= 40.0;
               }
               break;
            case 118:
            default:
               var4 = 180.0;
               break;
            case 180:
               var4 = 120.0;
               break;
            case 237:
               var4 = 500.0;
               break;
            case 242:
               var4 = 80.0;
         }

         var1 = new Value((byte)3, var4);
      } else {
         var1 = super.compute(var1, var2);
      }

      return var1;
   }
}
