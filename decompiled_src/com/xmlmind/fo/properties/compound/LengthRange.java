package com.xmlmind.fo.properties.compound;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class LengthRange extends Compound {
   public LengthRange(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Value lengthRange(String var1) {
      StringTokenizer var3 = new StringTokenizer(var1);
      if (var3.countTokens() != 3) {
         return null;
      } else {
         String var2 = this.name + ".minimum";
         Value var4 = this.evaluate(Property.list[Property.index(var2)], var3.nextToken());
         if (var4 == null) {
            return null;
         } else {
            var2 = this.name + ".optimum";
            Value var5 = this.evaluate(Property.list[Property.index(var2)], var3.nextToken());
            if (var5 == null) {
               return null;
            } else {
               var2 = this.name + ".maximum";
               Value var6 = this.evaluate(Property.list[Property.index(var2)], var3.nextToken());
               return var6 == null ? null : new Value((byte)5, new Value[]{var4, var5, var6});
            }
         }
      }
   }

   public Value compute(Value var1, Context var2) {
      if (var1.type == 5) {
         Value[] var3 = var1.lengthRange();
         Value[] var4 = new Value[3];

         for(int var5 = 0; var5 < 3; ++var5) {
            var4[var5] = this.compute(var3[var5], var2);
         }

         var1 = new Value((byte)5, var4);
      } else {
         var1 = super.compute(var1, var2);
      }

      return var1;
   }

   public void expand(PropertyValues var1) {
      Value var6 = var1.values[this.index];
      Value var3;
      Value var4;
      Value var5;
      if (var6.type == 5) {
         Value[] var7 = var6.lengthRange();
         var3 = var7[0];
         var4 = var7[1];
         var5 = var7[2];
      } else {
         var3 = var6;
         var4 = var6;
         var5 = var6;
      }

      int var2 = Property.index(this.name + ".minimum");
      var1.values[var2] = var3;
      var1.types[var2] = var1.types[this.index];
      var2 = Property.index(this.name + ".optimum");
      var1.values[var2] = var4;
      var1.types[var2] = var1.types[this.index];
      var2 = Property.index(this.name + ".maximum");
      var1.values[var2] = var5;
      var1.types[var2] = var1.types[this.index];
   }
}
