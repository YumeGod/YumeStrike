package com.xmlmind.fo.properties.compound;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class Space extends Compound {
   public Space(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Value space(String var1) {
      StringTokenizer var3 = new StringTokenizer(var1);
      if (var3.countTokens() != 5) {
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
               if (var6 == null) {
                  return null;
               } else {
                  var2 = this.name + ".conditionality";
                  Value var7 = this.evaluate(Property.list[Property.index(var2)], var3.nextToken());
                  if (var7 == null) {
                     return null;
                  } else {
                     var2 = this.name + ".precedence";
                     Value var8 = this.evaluate(Property.list[Property.index(var2)], var3.nextToken());
                     return var8 == null ? null : new Value((byte)9, new Value[]{var4, var5, var6, var7, var8});
                  }
               }
            }
         }
      }
   }

   public Value compute(Value var1, Context var2) {
      if (var1.type == 9) {
         Value[] var3 = var1.space();
         Value[] var4 = new Value[5];

         for(int var5 = 0; var5 < 5; ++var5) {
            var4[var5] = this.compute(var3[var5], var2);
         }

         var1 = new Value((byte)9, var4);
      } else {
         var1 = super.compute(var1, var2);
      }

      return var1;
   }

   public void expand(PropertyValues var1) {
      Value var8 = var1.values[this.index];
      int var2;
      Value var3;
      Value var4;
      Value var5;
      Value var6;
      Value var7;
      switch (var8.type) {
         case 4:
            var3 = var8;
            var4 = var8;
            var5 = var8;
            var2 = Property.index(this.name + ".conditionality");
            var6 = Property.list[var2].initialValue;
            var2 = Property.index(this.name + ".precedence");
            var7 = Property.list[var2].initialValue;
            break;
         case 9:
            Value[] var9 = var8.space();
            var3 = var9[0];
            var4 = var9[1];
            var5 = var9[2];
            var6 = var9[3];
            var7 = var9[4];
            break;
         default:
            return;
      }

      var2 = Property.index(this.name + ".minimum");
      var1.values[var2] = var3;
      var1.types[var2] = var1.types[this.index];
      var2 = Property.index(this.name + ".optimum");
      var1.values[var2] = var4;
      var1.types[var2] = var1.types[this.index];
      var2 = Property.index(this.name + ".maximum");
      var1.values[var2] = var5;
      var1.types[var2] = var1.types[this.index];
      var2 = Property.index(this.name + ".conditionality");
      var1.values[var2] = var6;
      var1.types[var2] = var1.types[this.index];
      var2 = Property.index(this.name + ".precedence");
      var1.values[var2] = var7;
      var1.types[var2] = var1.types[this.index];
   }

   public static Value space(Value var0) {
      Value var2;
      switch (var0.type) {
         case 4:
            var2 = var0;
            break;
         case 9:
            return var0;
         default:
            var2 = Value.LENGTH_ZERO;
      }

      Value var4 = Value.KEYWORD_RETAIN;
      Value var5 = Value.KEYWORD_FORCE;
      Value[] var6 = new Value[]{var2, var2, var2, var4, var5};
      return new Value((byte)9, var6);
   }

   public static Value length(Value var0) {
      Value var1 = Value.LENGTH_ZERO;
      switch (var0.type) {
         case 4:
            var1 = var0;
            break;
         case 9:
            Value[] var2 = var0.space();
            var1 = var2[1];
      }

      return var1;
   }
}
