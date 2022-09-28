package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;

public class Border extends Shorthand {
   public Border(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{36, 35, 34, 48, 47, 46, 52, 51, 50, 67, 66, 65};
   }

   protected Value list(String var1) {
      Value var2 = this.evaluate(Property.list[64], var1);
      Value[] var4 = new Value[12];
      if (var2 != null && var2.type == 27) {
         Value[] var3 = var2.list();

         for(int var5 = 0; var5 < 12; var5 += 3) {
            for(int var6 = 0; var6 < 3; ++var6) {
               var4[var5 + var6] = var3[var6];
            }
         }

         return new Value((byte)27, var4);
      } else {
         return null;
      }
   }

   public void expand(PropertyValues var1) {
      Value[] var2 = var1.values[this.index].list();

      for(int var3 = 0; var3 < 4; ++var3) {
         for(int var4 = 0; var4 < 3; ++var4) {
            int var5 = this.simpleProperties[3 * var3 + var4];
            var1.values[var5] = var2[var4];
            var1.types[var5] = var1.types[this.index];
         }
      }

   }
}
