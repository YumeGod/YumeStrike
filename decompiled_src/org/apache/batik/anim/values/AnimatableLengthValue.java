package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableLengthValue extends AnimatableValue {
   protected static final String[] UNITS = new String[]{"", "%", "em", "ex", "px", "cm", "mm", "in", "pt", "pc"};
   protected short lengthType;
   protected float lengthValue;
   protected short percentageInterpretation;

   protected AnimatableLengthValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableLengthValue(AnimationTarget var1, short var2, float var3, short var4) {
      super(var1);
      this.lengthType = var2;
      this.lengthValue = var3;
      this.percentageInterpretation = var4;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableLengthValue var6;
      if (var1 == null) {
         var6 = new AnimatableLengthValue(this.target);
      } else {
         var6 = (AnimatableLengthValue)var1;
      }

      short var7 = var6.lengthType;
      float var8 = var6.lengthValue;
      short var9 = var6.percentageInterpretation;
      var6.lengthType = this.lengthType;
      var6.lengthValue = this.lengthValue;
      var6.percentageInterpretation = this.percentageInterpretation;
      AnimatableLengthValue var10;
      float var11;
      if (var2 != null) {
         var10 = (AnimatableLengthValue)var2;
         if (!compatibleTypes(var6.lengthType, var6.percentageInterpretation, var10.lengthType, var10.percentageInterpretation)) {
            var6.lengthValue = this.target.svgToUserSpace(var6.lengthValue, var6.lengthType, var6.percentageInterpretation);
            var6.lengthType = 1;
            var11 = var10.target.svgToUserSpace(var10.lengthValue, var10.lengthType, var10.percentageInterpretation);
         } else {
            var11 = var10.lengthValue;
         }

         var6.lengthValue += var3 * (var11 - var6.lengthValue);
      }

      if (var4 != null) {
         var10 = (AnimatableLengthValue)var4;
         if (!compatibleTypes(var6.lengthType, var6.percentageInterpretation, var10.lengthType, var10.percentageInterpretation)) {
            var6.lengthValue = this.target.svgToUserSpace(var6.lengthValue, var6.lengthType, var6.percentageInterpretation);
            var6.lengthType = 1;
            var11 = var10.target.svgToUserSpace(var10.lengthValue, var10.lengthType, var10.percentageInterpretation);
         } else {
            var11 = var10.lengthValue;
         }

         var6.lengthValue += (float)var5 * var11;
      }

      if (var9 != var6.percentageInterpretation || var7 != var6.lengthType || var8 != var6.lengthValue) {
         var6.hasChanged = true;
      }

      return var6;
   }

   public static boolean compatibleTypes(short var0, short var1, short var2, short var3) {
      return var0 == var2 && (var0 != 2 || var1 == var3) || var0 == 1 && var2 == 5 || var0 == 5 && var2 == 1;
   }

   public int getLengthType() {
      return this.lengthType;
   }

   public float getLengthValue() {
      return this.lengthValue;
   }

   public boolean canPace() {
      return true;
   }

   public float distanceTo(AnimatableValue var1) {
      AnimatableLengthValue var2 = (AnimatableLengthValue)var1;
      float var3 = this.target.svgToUserSpace(this.lengthValue, this.lengthType, this.percentageInterpretation);
      float var4 = this.target.svgToUserSpace(var2.lengthValue, var2.lengthType, var2.percentageInterpretation);
      return Math.abs(var3 - var4);
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableLengthValue(this.target, (short)1, 0.0F, this.percentageInterpretation);
   }

   public String getCssText() {
      return formatNumber(this.lengthValue) + UNITS[this.lengthType - 1];
   }
}
