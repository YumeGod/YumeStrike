package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableLengthOrIdentValue extends AnimatableLengthValue {
   protected boolean isIdent;
   protected String ident;

   protected AnimatableLengthOrIdentValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableLengthOrIdentValue(AnimationTarget var1, short var2, float var3, short var4) {
      super(var1, var2, var3, var4);
   }

   public AnimatableLengthOrIdentValue(AnimationTarget var1, String var2) {
      super(var1);
      this.ident = var2;
      this.isIdent = true;
   }

   public boolean isIdent() {
      return this.isIdent;
   }

   public String getIdent() {
      return this.ident;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableLengthOrIdentValue(this.target, (short)1, 0.0F, this.percentageInterpretation);
   }

   public String getCssText() {
      return this.isIdent ? this.ident : super.getCssText();
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableLengthOrIdentValue var6;
      if (var1 == null) {
         var6 = new AnimatableLengthOrIdentValue(this.target);
      } else {
         var6 = (AnimatableLengthOrIdentValue)var1;
      }

      if (var2 == null) {
         if (this.isIdent) {
            var6.hasChanged = !var6.isIdent || !var6.ident.equals(this.ident);
            var6.ident = this.ident;
            var6.isIdent = true;
         } else {
            short var7 = var6.lengthType;
            float var8 = var6.lengthValue;
            short var9 = var6.percentageInterpretation;
            super.interpolate(var6, var2, var3, var4, var5);
            if (var6.lengthType != var7 || var6.lengthValue != var8 || var6.percentageInterpretation != var9) {
               var6.hasChanged = true;
            }
         }
      } else {
         AnimatableLengthOrIdentValue var10 = (AnimatableLengthOrIdentValue)var2;
         if (!this.isIdent && !var10.isIdent) {
            super.interpolate(var6, var2, var3, var4, var5);
         } else if ((double)var3 >= 0.5) {
            if (var6.isIdent != var10.isIdent || var6.lengthType != var10.lengthType || var6.lengthValue != var10.lengthValue || var6.isIdent && var10.isIdent && !var10.ident.equals(this.ident)) {
               var6.isIdent = var10.isIdent;
               var6.ident = var10.ident;
               var6.lengthType = var10.lengthType;
               var6.lengthValue = var10.lengthValue;
               var6.hasChanged = true;
            }
         } else if (var6.isIdent != this.isIdent || var6.lengthType != this.lengthType || var6.lengthValue != this.lengthValue || var6.isIdent && this.isIdent && !var6.ident.equals(this.ident)) {
            var6.isIdent = this.isIdent;
            var6.ident = this.ident;
            var6.ident = this.ident;
            var6.lengthType = this.lengthType;
            var6.hasChanged = true;
         }
      }

      return var6;
   }
}
