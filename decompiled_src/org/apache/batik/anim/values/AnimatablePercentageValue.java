package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatablePercentageValue extends AnimatableNumberValue {
   protected AnimatablePercentageValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatablePercentageValue(AnimationTarget var1, float var2) {
      super(var1, var2);
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      if (var1 == null) {
         var1 = new AnimatablePercentageValue(this.target);
      }

      return super.interpolate((AnimatableValue)var1, var2, var3, var4, var5);
   }

   public AnimatableValue getZeroValue() {
      return new AnimatablePercentageValue(this.target, 0.0F);
   }

   public String getCssText() {
      return super.getCssText() + "%";
   }
}
