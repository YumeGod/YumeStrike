package com.xmlmind.fo.properties;

import com.xmlmind.fo.converter.Context;

public class Volume extends Property {
   public static final double MEDIUM = 50.0;

   public Volume(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Value number(String var1) {
      double var2;
      try {
         var2 = Double.valueOf(var1);
         if (var2 < 0.0) {
            var2 = 0.0;
         } else if (var2 > 100.0) {
            var2 = 100.0;
         }
      } catch (NumberFormatException var5) {
         return null;
      }

      return new Value((byte)3, var2);
   }

   public Value compute(Value var1, Context var2) {
      if (var1.type == 13) {
         double var3 = var1.percentage();
         Context var5 = var2.parent();
         if (var5 == null) {
            var1 = this.initialValue;
         } else {
            var1 = var5.properties.values[303];
         }

         if (var1.type == 3) {
            double var6 = var1.number() * var3 / 100.0;
            if (var6 > 100.0) {
               var6 = 100.0;
            }

            var1 = new Value((byte)3, var6);
         }
      } else if (var1.type == 1) {
         int var8 = var1.keyword();
         if (var8 == 88) {
            return super.compute(var1, var2);
         }

         if (var8 != 179) {
            double var4;
            switch (var8) {
               case 109:
                  var4 = 75.0;
                  break;
               case 118:
               default:
                  var4 = 50.0;
                  break;
               case 186:
                  var4 = 25.0;
                  break;
               case 240:
                  var4 = 100.0;
                  break;
               case 244:
                  var4 = 0.0;
            }

            var1 = new Value((byte)3, var4);
         }
      } else {
         var1 = super.compute(var1, var2);
      }

      return var1;
   }
}
