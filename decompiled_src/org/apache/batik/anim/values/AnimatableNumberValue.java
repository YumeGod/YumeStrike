package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableNumberValue extends AnimatableValue {
   protected float value;

   protected AnimatableNumberValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableNumberValue(AnimationTarget var1, float var2) {
      super(var1);
      this.value = var2;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableNumberValue var6;
      if (var1 == null) {
         var6 = new AnimatableNumberValue(this.target);
      } else {
         var6 = (AnimatableNumberValue)var1;
      }

      float var7 = this.value;
      AnimatableNumberValue var8;
      if (var2 != null) {
         var8 = (AnimatableNumberValue)var2;
         var7 += var3 * (var8.value - this.value);
      }

      if (var4 != null) {
         var8 = (AnimatableNumberValue)var4;
         var7 += (float)var5 * var8.value;
      }

      if (var6.value != var7) {
         var6.value = var7;
         var6.hasChanged = true;
      }

      return var6;
   }

   public float getValue() {
      return this.value;
   }

   public boolean canPace() {
      return true;
   }

   public float distanceTo(AnimatableValue var1) {
      AnimatableNumberValue var2 = (AnimatableNumberValue)var1;
      return Math.abs(this.value - var2.value);
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableNumberValue(this.target, 0.0F);
   }

   public String getCssText() {
      return formatNumber(this.value);
   }
}
