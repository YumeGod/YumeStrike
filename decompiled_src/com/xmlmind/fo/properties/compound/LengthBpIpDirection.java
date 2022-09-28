package com.xmlmind.fo.properties.compound;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class LengthBpIpDirection extends Compound {
   public LengthBpIpDirection(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Value lengthBpIpDirection(String var1) {
      StringTokenizer var3 = new StringTokenizer(var1);
      if (var3.countTokens() != 2) {
         return null;
      } else {
         String var2 = this.name + ".block-progression-direction";
         Value var4 = this.evaluate(Property.list[Property.index(var2)], var3.nextToken());
         if (var4 == null) {
            return null;
         } else {
            var2 = this.name + ".inline-progression-direction";
            Value var5 = this.evaluate(Property.list[Property.index(var2)], var3.nextToken());
            return var5 == null ? null : new Value((byte)7, new Value[]{var4, var5});
         }
      }
   }

   public Value compute(Value var1, Context var2) {
      if (var1.type == 7) {
         Value[] var3 = var1.lengthBpIpDirection();
         Value[] var4 = new Value[]{this.compute(var3[0], var2), this.compute(var3[1], var2)};
         var1 = new Value((byte)7, var4);
      } else {
         var1 = super.compute(var1, var2);
      }

      return var1;
   }

   public void expand(PropertyValues var1) {
      Value var5 = var1.values[this.index];
      Value var3;
      Value var4;
      if (var5.type == 7) {
         Value[] var6 = var5.lengthBpIpDirection();
         var3 = var6[0];
         var4 = var6[1];
      } else {
         var3 = var5;
         var4 = var5;
      }

      int var2 = Property.index(this.name + ".block-progression-direction");
      var1.values[var2] = var3;
      var1.types[var2] = var1.types[this.index];
      var2 = Property.index(this.name + ".inline-progression-direction");
      var1.values[var2] = var4;
      var1.types[var2] = var1.types[this.index];
   }
}
