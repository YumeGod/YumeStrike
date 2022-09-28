package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableNumberListValue extends AnimatableValue {
   protected float[] numbers;

   protected AnimatableNumberListValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableNumberListValue(AnimationTarget var1, float[] var2) {
      super(var1);
      this.numbers = var2;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableNumberListValue var6 = (AnimatableNumberListValue)var2;
      AnimatableNumberListValue var7 = (AnimatableNumberListValue)var4;
      boolean var8 = var2 != null;
      boolean var9 = var4 != null;
      boolean var10 = (!var8 || var6.numbers.length == this.numbers.length) && (!var9 || var7.numbers.length == this.numbers.length);
      float[] var11;
      if (!var10 && var8 && (double)var3 >= 0.5) {
         var11 = var6.numbers;
      } else {
         var11 = this.numbers;
      }

      int var12 = var11.length;
      AnimatableNumberListValue var13;
      if (var1 == null) {
         var13 = new AnimatableNumberListValue(this.target);
         var13.numbers = new float[var12];
      } else {
         var13 = (AnimatableNumberListValue)var1;
         if (var13.numbers == null || var13.numbers.length != var12) {
            var13.numbers = new float[var12];
         }
      }

      for(int var14 = 0; var14 < var12; ++var14) {
         float var15 = var11[var14];
         if (var10) {
            if (var8) {
               var15 += var3 * (var6.numbers[var14] - var15);
            }

            if (var9) {
               var15 += (float)var5 * var7.numbers[var14];
            }
         }

         if (var13.numbers[var14] != var15) {
            var13.numbers[var14] = var15;
            var13.hasChanged = true;
         }
      }

      return var13;
   }

   public float[] getNumbers() {
      return this.numbers;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      float[] var1 = new float[this.numbers.length];
      return new AnimatableNumberListValue(this.target, var1);
   }

   public String getCssText() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.numbers[0]);

      for(int var2 = 1; var2 < this.numbers.length; ++var2) {
         var1.append(' ');
         var1.append(this.numbers[var2]);
      }

      return var1.toString();
   }
}
