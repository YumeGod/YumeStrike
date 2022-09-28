package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableColorValue extends AnimatableValue {
   protected float red;
   protected float green;
   protected float blue;

   protected AnimatableColorValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableColorValue(AnimationTarget var1, float var2, float var3, float var4) {
      super(var1);
      this.red = var2;
      this.green = var3;
      this.blue = var4;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableColorValue var6;
      if (var1 == null) {
         var6 = new AnimatableColorValue(this.target);
      } else {
         var6 = (AnimatableColorValue)var1;
      }

      float var7 = var6.red;
      float var8 = var6.green;
      float var9 = var6.blue;
      var6.red = this.red;
      var6.green = this.green;
      var6.blue = this.blue;
      AnimatableColorValue var10 = (AnimatableColorValue)var2;
      AnimatableColorValue var11 = (AnimatableColorValue)var4;
      if (var2 != null) {
         var6.red += var3 * (var10.red - var6.red);
         var6.green += var3 * (var10.green - var6.green);
         var6.blue += var3 * (var10.blue - var6.blue);
      }

      if (var4 != null) {
         var6.red += (float)var5 * var11.red;
         var6.green += (float)var5 * var11.green;
         var6.blue += (float)var5 * var11.blue;
      }

      if (var6.red != var7 || var6.green != var8 || var6.blue != var9) {
         var6.hasChanged = true;
      }

      return var6;
   }

   public boolean canPace() {
      return true;
   }

   public float distanceTo(AnimatableValue var1) {
      AnimatableColorValue var2 = (AnimatableColorValue)var1;
      float var3 = this.red - var2.red;
      float var4 = this.green - var2.green;
      float var5 = this.blue - var2.blue;
      return (float)Math.sqrt((double)(var3 * var3 + var4 * var4 + var5 * var5));
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableColorValue(this.target, 0.0F, 0.0F, 0.0F);
   }

   public String getCssText() {
      return "rgb(" + Math.round(this.red * 255.0F) + ',' + Math.round(this.green * 255.0F) + ',' + Math.round(this.blue * 255.0F) + ')';
   }
}
