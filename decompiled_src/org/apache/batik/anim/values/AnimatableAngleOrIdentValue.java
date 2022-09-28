package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableAngleOrIdentValue extends AnimatableAngleValue {
   protected boolean isIdent;
   protected String ident;

   protected AnimatableAngleOrIdentValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableAngleOrIdentValue(AnimationTarget var1, float var2, short var3) {
      super(var1, var2, var3);
   }

   public AnimatableAngleOrIdentValue(AnimationTarget var1, String var2) {
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
      return new AnimatableAngleOrIdentValue(this.target, 0.0F, (short)1);
   }

   public String getCssText() {
      return this.isIdent ? this.ident : super.getCssText();
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableAngleOrIdentValue var6;
      if (var1 == null) {
         var6 = new AnimatableAngleOrIdentValue(this.target);
      } else {
         var6 = (AnimatableAngleOrIdentValue)var1;
      }

      if (var2 == null) {
         if (this.isIdent) {
            var6.hasChanged = !var6.isIdent || !var6.ident.equals(this.ident);
            var6.ident = this.ident;
            var6.isIdent = true;
         } else {
            short var7 = var6.unit;
            float var8 = var6.value;
            super.interpolate(var6, var2, var3, var4, var5);
            if (var6.unit != var7 || var6.value != var8) {
               var6.hasChanged = true;
            }
         }
      } else {
         AnimatableAngleOrIdentValue var9 = (AnimatableAngleOrIdentValue)var2;
         if (!this.isIdent && !var9.isIdent) {
            super.interpolate(var6, var2, var3, var4, var5);
         } else if ((double)var3 >= 0.5) {
            if (var6.isIdent != var9.isIdent || var6.unit != var9.unit || var6.value != var9.value || var6.isIdent && var9.isIdent && !var9.ident.equals(this.ident)) {
               var6.isIdent = var9.isIdent;
               var6.ident = var9.ident;
               var6.unit = var9.unit;
               var6.value = var9.value;
               var6.hasChanged = true;
            }
         } else if (var6.isIdent != this.isIdent || var6.unit != this.unit || var6.value != this.value || var6.isIdent && this.isIdent && !var6.ident.equals(this.ident)) {
            var6.isIdent = this.isIdent;
            var6.ident = this.ident;
            var6.unit = this.unit;
            var6.value = this.value;
            var6.hasChanged = true;
         }
      }

      return var6;
   }
}
