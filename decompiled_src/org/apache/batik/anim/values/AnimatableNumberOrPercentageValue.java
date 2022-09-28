package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableNumberOrPercentageValue extends AnimatableNumberValue {
   protected boolean isPercentage;

   protected AnimatableNumberOrPercentageValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableNumberOrPercentageValue(AnimationTarget var1, float var2) {
      super(var1, var2);
   }

   public AnimatableNumberOrPercentageValue(AnimationTarget var1, float var2, boolean var3) {
      super(var1, var2);
      this.isPercentage = var3;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableNumberOrPercentageValue var6;
      if (var1 == null) {
         var6 = new AnimatableNumberOrPercentageValue(this.target);
      } else {
         var6 = (AnimatableNumberOrPercentageValue)var1;
      }

      AnimatableNumberOrPercentageValue var9 = (AnimatableNumberOrPercentageValue)var2;
      AnimatableNumberOrPercentageValue var10 = (AnimatableNumberOrPercentageValue)var4;
      float var7;
      boolean var8;
      if (var2 != null) {
         if (var9.isPercentage == this.isPercentage) {
            var7 = this.value + var3 * (var9.value - this.value);
            var8 = this.isPercentage;
         } else if ((double)var3 >= 0.5) {
            var7 = var9.value;
            var8 = var9.isPercentage;
         } else {
            var7 = this.value;
            var8 = this.isPercentage;
         }
      } else {
         var7 = this.value;
         var8 = this.isPercentage;
      }

      if (var4 != null && var10.isPercentage == var8) {
         var7 += (float)var5 * var10.value;
      }

      if (var6.value != var7 || var6.isPercentage != var8) {
         var6.value = var7;
         var6.isPercentage = var8;
         var6.hasChanged = true;
      }

      return var6;
   }

   public boolean isPercentage() {
      return this.isPercentage;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableNumberOrPercentageValue(this.target, 0.0F, this.isPercentage);
   }

   public String getCssText() {
      StringBuffer var1 = new StringBuffer();
      var1.append(formatNumber(this.value));
      if (this.isPercentage) {
         var1.append('%');
      }

      return var1.toString();
   }
}
