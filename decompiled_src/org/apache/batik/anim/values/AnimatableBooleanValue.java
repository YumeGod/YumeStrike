package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableBooleanValue extends AnimatableValue {
   protected boolean value;

   protected AnimatableBooleanValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableBooleanValue(AnimationTarget var1, boolean var2) {
      super(var1);
      this.value = var2;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableBooleanValue var6;
      if (var1 == null) {
         var6 = new AnimatableBooleanValue(this.target);
      } else {
         var6 = (AnimatableBooleanValue)var1;
      }

      boolean var7;
      if (var2 != null && (double)var3 >= 0.5) {
         AnimatableBooleanValue var8 = (AnimatableBooleanValue)var2;
         var7 = var8.value;
      } else {
         var7 = this.value;
      }

      if (var6.value != var7) {
         var6.value = var7;
         var6.hasChanged = true;
      }

      return var6;
   }

   public boolean getValue() {
      return this.value;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableBooleanValue(this.target, false);
   }

   public String getCssText() {
      return this.value ? "true" : "false";
   }
}
