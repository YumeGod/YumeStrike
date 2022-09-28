package com.xmlmind.fo.properties;

import com.xmlmind.fo.converter.Context;

public class FontSize extends Property {
   public FontSize(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public Value compute(Value var1, Context var2) {
      Context var5 = var2.parent();
      double var3;
      if (var5.properties != null) {
         var3 = var5.properties.fontSize();
      } else {
         var3 = var2.translator.fontSize(118, 0.0);
      }

      if (var1.type == 1) {
         int var6 = var1.keyword();
         if (var6 == 88) {
            return super.compute(var1, var2);
         }

         var3 = var2.translator.fontSize(var1.keyword(), var3);
         var1 = new Value((byte)4, 4, var3);
      } else if (var1.type == 13) {
         var3 = var3 * var1.percentage() / 100.0;
         var1 = new Value((byte)4, 4, var3);
      } else {
         var1 = super.compute(var1, var2);
      }

      return var1;
   }

   protected Value length(Value var1, Context var2) {
      int var3 = var1.unit();
      if (var3 != 7 && var3 != 8) {
         var1 = super.length(var1, var2);
      } else {
         double var4 = var2.translator.fontSize(118, 0.0);
         Context var6 = var2.parent();
         if (var6.properties != null) {
            var4 = var6.properties.fontSize();
         }

         double var7 = var1.length() * var4;
         if (var3 == 8) {
            var7 /= 2.0;
         }

         var1 = new Value((byte)4, 4, var7);
      }

      return var1;
   }
}
