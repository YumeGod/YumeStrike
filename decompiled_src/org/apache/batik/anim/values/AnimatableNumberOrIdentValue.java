package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableNumberOrIdentValue extends AnimatableNumberValue {
   protected boolean isIdent;
   protected String ident;
   protected boolean numericIdent;

   protected AnimatableNumberOrIdentValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableNumberOrIdentValue(AnimationTarget var1, float var2, boolean var3) {
      super(var1, var2);
      this.numericIdent = var3;
   }

   public AnimatableNumberOrIdentValue(AnimationTarget var1, String var2) {
      super(var1);
      this.ident = var2;
      this.isIdent = true;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableNumberOrIdentValue(this.target, 0.0F, this.numericIdent);
   }

   public String getCssText() {
      if (this.isIdent) {
         return this.ident;
      } else {
         return this.numericIdent ? Integer.toString((int)this.value) : super.getCssText();
      }
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableNumberOrIdentValue var6;
      if (var1 == null) {
         var6 = new AnimatableNumberOrIdentValue(this.target);
      } else {
         var6 = (AnimatableNumberOrIdentValue)var1;
      }

      if (var2 == null) {
         if (this.isIdent) {
            var6.hasChanged = !var6.isIdent || !var6.ident.equals(this.ident);
            var6.ident = this.ident;
            var6.isIdent = true;
         } else if (this.numericIdent) {
            var6.hasChanged = var6.value != this.value || var6.isIdent;
            var6.value = this.value;
            var6.isIdent = false;
            var6.hasChanged = true;
            var6.numericIdent = true;
         } else {
            float var7 = var6.value;
            super.interpolate(var6, var2, var3, var4, var5);
            var6.numericIdent = false;
            if (var6.value != var7) {
               var6.hasChanged = true;
            }
         }
      } else {
         AnimatableNumberOrIdentValue var8 = (AnimatableNumberOrIdentValue)var2;
         if (!this.isIdent && !var8.isIdent && !this.numericIdent) {
            super.interpolate(var6, var2, var3, var4, var5);
            var6.numericIdent = false;
         } else if ((double)var3 >= 0.5) {
            if (var6.isIdent != var8.isIdent || var6.value != var8.value || var6.isIdent && var8.isIdent && !var8.ident.equals(this.ident)) {
               var6.isIdent = var8.isIdent;
               var6.ident = var8.ident;
               var6.value = var8.value;
               var6.numericIdent = var8.numericIdent;
               var6.hasChanged = true;
            }
         } else if (var6.isIdent != this.isIdent || var6.value != this.value || var6.isIdent && this.isIdent && !var6.ident.equals(this.ident)) {
            var6.isIdent = this.isIdent;
            var6.ident = this.ident;
            var6.value = this.value;
            var6.numericIdent = this.numericIdent;
            var6.hasChanged = true;
         }
      }

      return var6;
   }
}
