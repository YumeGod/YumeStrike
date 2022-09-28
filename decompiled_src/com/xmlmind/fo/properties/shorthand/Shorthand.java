package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;

public abstract class Shorthand extends Property {
   public static final int[] list = new int[]{6, 10, 20, 38, 63, 68, 33, 45, 49, 64, 56, 88, 103, 172, 192, 209, 210, 211, 215, 221, 244, 300, 304, 317};
   public int[] simpleProperties;

   public Shorthand(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public Value compute(Value var1, Context var2) {
      if (var1.type == 27) {
         Value[] var3 = var1.list();
         Value[] var4 = new Value[this.simpleProperties.length];

         for(int var5 = 0; var5 < this.simpleProperties.length; ++var5) {
            if (var3[var5] != null) {
               int var6 = this.simpleProperties[var5];
               if (var6 == 164) {
                  var4[var5] = var3[var5];
               } else {
                  Property var7 = Property.list[var6];
                  var4[var5] = var7.compute(var3[var5], var2);
               }
            }
         }

         var1 = new Value((byte)27, var4);
      } else {
         var1 = super.compute(var1, var2);
      }

      return var1;
   }

   public Value inherit(PropertyValues var1) {
      Value[] var2 = new Value[this.simpleProperties.length];

      for(int var3 = 0; var3 < this.simpleProperties.length; ++var3) {
         int var4 = this.simpleProperties[var3];
         var2[var3] = var1.values[var4];
      }

      return new Value((byte)27, var2);
   }

   public void expand(PropertyValues var1) {
      Value[] var2 = var1.values[this.index].list();

      for(int var3 = 0; var3 < this.simpleProperties.length; ++var3) {
         int var4 = this.simpleProperties[var3];
         var1.values[var4] = var2[var3];
         var1.types[var4] = var1.types[this.index];
      }

   }

   public Value fromNearestSpecifiedValue(String var1, Context var2) {
      Value[] var3 = new Value[this.simpleProperties.length];
      int var4;
      if (var1 != null) {
         var4 = index(var1);
         if (var4 != this.index) {
            return null;
         }
      }

      for(var4 = 0; var4 < this.simpleProperties.length; ++var4) {
         int var5 = this.simpleProperties[var4];
         var3[var4] = var2.nearestSpecifiedValue(var5);
         if (var3[var4] == null) {
            var3[var4] = Property.list[var5].initialValue;
         }
      }

      return new Value((byte)27, var3);
   }

   protected Value evaluate(Property var1, String var2) {
      Value var3 = var1.evaluate(var2);
      return var3 != null && var3.type != 28 ? var3 : null;
   }
}
