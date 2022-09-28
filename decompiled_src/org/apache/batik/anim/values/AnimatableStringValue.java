package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableStringValue extends AnimatableValue {
   protected String string;

   protected AnimatableStringValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableStringValue(AnimationTarget var1, String var2) {
      super(var1);
      this.string = var2;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableStringValue var6;
      if (var1 == null) {
         var6 = new AnimatableStringValue(this.target);
      } else {
         var6 = (AnimatableStringValue)var1;
      }

      String var7;
      if (var2 != null && (double)var3 >= 0.5) {
         AnimatableStringValue var8 = (AnimatableStringValue)var2;
         var7 = var8.string;
      } else {
         var7 = this.string;
      }

      if (var6.string == null || !var6.string.equals(var7)) {
         var6.string = var7;
         var6.hasChanged = true;
      }

      return var6;
   }

   public String getString() {
      return this.string;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableStringValue(this.target, "");
   }

   public String getCssText() {
      return this.string;
   }
}
