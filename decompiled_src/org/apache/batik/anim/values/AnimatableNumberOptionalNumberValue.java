package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableNumberOptionalNumberValue extends AnimatableValue {
   protected float number;
   protected boolean hasOptionalNumber;
   protected float optionalNumber;

   protected AnimatableNumberOptionalNumberValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableNumberOptionalNumberValue(AnimationTarget var1, float var2) {
      super(var1);
      this.number = var2;
   }

   public AnimatableNumberOptionalNumberValue(AnimationTarget var1, float var2, float var3) {
      super(var1);
      this.number = var2;
      this.optionalNumber = var3;
      this.hasOptionalNumber = true;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableNumberOptionalNumberValue var6;
      if (var1 == null) {
         var6 = new AnimatableNumberOptionalNumberValue(this.target);
      } else {
         var6 = (AnimatableNumberOptionalNumberValue)var1;
      }

      float var7;
      float var8;
      boolean var9;
      if (var2 != null && (double)var3 >= 0.5) {
         AnimatableNumberOptionalNumberValue var10 = (AnimatableNumberOptionalNumberValue)var2;
         var7 = var10.number;
         var8 = var10.optionalNumber;
         var9 = var10.hasOptionalNumber;
      } else {
         var7 = this.number;
         var8 = this.optionalNumber;
         var9 = this.hasOptionalNumber;
      }

      if (var6.number != var7 || var6.hasOptionalNumber != var9 || var6.optionalNumber != var8) {
         var6.number = this.number;
         var6.optionalNumber = this.optionalNumber;
         var6.hasOptionalNumber = this.hasOptionalNumber;
         var6.hasChanged = true;
      }

      return var6;
   }

   public float getNumber() {
      return this.number;
   }

   public boolean hasOptionalNumber() {
      return this.hasOptionalNumber;
   }

   public float getOptionalNumber() {
      return this.optionalNumber;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      return this.hasOptionalNumber ? new AnimatableNumberOptionalNumberValue(this.target, 0.0F, 0.0F) : new AnimatableNumberOptionalNumberValue(this.target, 0.0F);
   }

   public String getCssText() {
      StringBuffer var1 = new StringBuffer();
      var1.append(formatNumber(this.number));
      if (this.hasOptionalNumber) {
         var1.append(' ');
         var1.append(formatNumber(this.optionalNumber));
      }

      return var1.toString();
   }
}
