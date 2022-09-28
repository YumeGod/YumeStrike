package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatablePointListValue extends AnimatableNumberListValue {
   protected AnimatablePointListValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatablePointListValue(AnimationTarget var1, float[] var2) {
      super(var1, var2);
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      if (var1 == null) {
         var1 = new AnimatablePointListValue(this.target);
      }

      return super.interpolate((AnimatableValue)var1, var2, var3, var4, var5);
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      float[] var1 = new float[this.numbers.length];
      return new AnimatablePointListValue(this.target, var1);
   }
}
