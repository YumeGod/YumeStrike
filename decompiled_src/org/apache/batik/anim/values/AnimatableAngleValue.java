package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableAngleValue extends AnimatableNumberValue {
   protected static final String[] UNITS = new String[]{"", "", "deg", "rad", "grad"};
   protected short unit;

   public AnimatableAngleValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableAngleValue(AnimationTarget var1, float var2, short var3) {
      super(var1, var2);
      this.unit = var3;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableAngleValue var6;
      if (var1 == null) {
         var6 = new AnimatableAngleValue(this.target);
      } else {
         var6 = (AnimatableAngleValue)var1;
      }

      float var7 = this.value;
      short var8 = this.unit;
      AnimatableAngleValue var9;
      if (var2 != null) {
         var9 = (AnimatableAngleValue)var2;
         if (var9.unit != var8) {
            var7 = rad(var7, var8);
            var7 += var3 * (rad(var9.value, var9.unit) - var7);
            var8 = 3;
         } else {
            var7 += var3 * (var9.value - var7);
         }
      }

      if (var4 != null) {
         var9 = (AnimatableAngleValue)var4;
         if (var9.unit != var8) {
            var7 += (float)var5 * rad(var9.value, var9.unit);
            var8 = 3;
         } else {
            var7 += (float)var5 * var9.value;
         }
      }

      if (var6.value != var7 || var6.unit != var8) {
         var6.value = var7;
         var6.unit = var8;
         var6.hasChanged = true;
      }

      return var6;
   }

   public short getUnit() {
      return this.unit;
   }

   public float distanceTo(AnimatableValue var1) {
      AnimatableAngleValue var2 = (AnimatableAngleValue)var1;
      return Math.abs(rad(this.value, this.unit) - rad(var2.value, var2.unit));
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableAngleValue(this.target, 0.0F, (short)1);
   }

   public String getCssText() {
      return super.getCssText() + UNITS[this.unit];
   }

   public static float rad(float var0, short var1) {
      switch (var1) {
         case 3:
            return var0;
         case 4:
            return 3.1415927F * var0 / 200.0F;
         default:
            return 3.1415927F * var0 / 180.0F;
      }
   }
}
