package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableLengthListValue extends AnimatableValue {
   protected short[] lengthTypes;
   protected float[] lengthValues;
   protected short percentageInterpretation;

   protected AnimatableLengthListValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableLengthListValue(AnimationTarget var1, short[] var2, float[] var3, short var4) {
      super(var1);
      this.lengthTypes = var2;
      this.lengthValues = var3;
      this.percentageInterpretation = var4;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableLengthListValue var6 = (AnimatableLengthListValue)var2;
      AnimatableLengthListValue var7 = (AnimatableLengthListValue)var4;
      boolean var8 = var2 != null;
      boolean var9 = var4 != null;
      boolean var10 = (!var8 || var6.lengthTypes.length == this.lengthTypes.length) && (!var9 || var7.lengthTypes.length == this.lengthTypes.length);
      short[] var11;
      float[] var12;
      if (!var10 && var8 && (double)var3 >= 0.5) {
         var11 = var6.lengthTypes;
         var12 = var6.lengthValues;
      } else {
         var11 = this.lengthTypes;
         var12 = this.lengthValues;
      }

      int var13 = var11.length;
      AnimatableLengthListValue var14;
      if (var1 == null) {
         var14 = new AnimatableLengthListValue(this.target);
         var14.lengthTypes = new short[var13];
         var14.lengthValues = new float[var13];
      } else {
         var14 = (AnimatableLengthListValue)var1;
         if (var14.lengthTypes == null || var14.lengthTypes.length != var13) {
            var14.lengthTypes = new short[var13];
            var14.lengthValues = new float[var13];
         }
      }

      var14.hasChanged = this.percentageInterpretation != var14.percentageInterpretation;
      var14.percentageInterpretation = this.percentageInterpretation;

      for(int var15 = 0; var15 < var13; ++var15) {
         float var16 = 0.0F;
         float var17 = 0.0F;
         short var18 = var11[var15];
         float var19 = var12[var15];
         if (var10) {
            if (var8 && !AnimatableLengthValue.compatibleTypes(var18, this.percentageInterpretation, var6.lengthTypes[var15], var6.percentageInterpretation) || var9 && !AnimatableLengthValue.compatibleTypes(var18, this.percentageInterpretation, var7.lengthTypes[var15], var7.percentageInterpretation)) {
               var19 = this.target.svgToUserSpace(var19, var18, this.percentageInterpretation);
               var18 = 1;
               if (var8) {
                  var16 = var2.target.svgToUserSpace(var6.lengthValues[var15], var6.lengthTypes[var15], var6.percentageInterpretation);
               }

               if (var9) {
                  var17 = var4.target.svgToUserSpace(var7.lengthValues[var15], var7.lengthTypes[var15], var7.percentageInterpretation);
               }
            } else {
               if (var8) {
                  var16 = var6.lengthValues[var15];
               }

               if (var9) {
                  var17 = var7.lengthValues[var15];
               }
            }

            var19 += var3 * (var16 - var19) + (float)var5 * var17;
         }

         if (!var14.hasChanged) {
            var14.hasChanged = var18 != var14.lengthTypes[var15] || var19 != var14.lengthValues[var15];
         }

         var14.lengthTypes[var15] = var18;
         var14.lengthValues[var15] = var19;
      }

      return var14;
   }

   public short[] getLengthTypes() {
      return this.lengthTypes;
   }

   public float[] getLengthValues() {
      return this.lengthValues;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      float[] var1 = new float[this.lengthValues.length];
      return new AnimatableLengthListValue(this.target, this.lengthTypes, var1, this.percentageInterpretation);
   }

   public String getCssText() {
      StringBuffer var1 = new StringBuffer();
      if (this.lengthValues.length > 0) {
         var1.append(formatNumber(this.lengthValues[0]));
         var1.append(AnimatableLengthValue.UNITS[this.lengthTypes[0] - 1]);
      }

      for(int var2 = 1; var2 < this.lengthValues.length; ++var2) {
         var1.append(',');
         var1.append(formatNumber(this.lengthValues[var2]));
         var1.append(AnimatableLengthValue.UNITS[this.lengthTypes[var2] - 1]);
      }

      return var1.toString();
   }
}
