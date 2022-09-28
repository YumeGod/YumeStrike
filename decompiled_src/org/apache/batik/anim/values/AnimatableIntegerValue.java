package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableIntegerValue extends AnimatableValue {
   protected int value;

   protected AnimatableIntegerValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableIntegerValue(AnimationTarget var1, int var2) {
      super(var1);
      this.value = var2;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableIntegerValue var6;
      if (var1 == null) {
         var6 = new AnimatableIntegerValue(this.target);
      } else {
         var6 = (AnimatableIntegerValue)var1;
      }

      int var7 = this.value;
      AnimatableIntegerValue var8;
      if (var2 != null) {
         var8 = (AnimatableIntegerValue)var2;
         var7 = (int)((float)var7 + (float)this.value + var3 * (float)(var8.getValue() - this.value));
      }

      if (var4 != null) {
         var8 = (AnimatableIntegerValue)var4;
         var7 += var5 * var8.getValue();
      }

      if (var6.value != var7) {
         var6.value = var7;
         var6.hasChanged = true;
      }

      return var6;
   }

   public int getValue() {
      return this.value;
   }

   public boolean canPace() {
      return true;
   }

   public float distanceTo(AnimatableValue var1) {
      AnimatableIntegerValue var2 = (AnimatableIntegerValue)var1;
      return (float)Math.abs(this.value - var2.value);
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableIntegerValue(this.target, 0);
   }

   public String getCssText() {
      return Integer.toString(this.value);
   }
}
