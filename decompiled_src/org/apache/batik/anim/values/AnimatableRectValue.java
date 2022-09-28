package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableRectValue extends AnimatableValue {
   protected float x;
   protected float y;
   protected float width;
   protected float height;

   protected AnimatableRectValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableRectValue(AnimationTarget var1, float var2, float var3, float var4, float var5) {
      super(var1);
      this.x = var2;
      this.y = var3;
      this.width = var4;
      this.height = var5;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableRectValue var6;
      if (var1 == null) {
         var6 = new AnimatableRectValue(this.target);
      } else {
         var6 = (AnimatableRectValue)var1;
      }

      float var7 = this.x;
      float var8 = this.y;
      float var9 = this.width;
      float var10 = this.height;
      AnimatableRectValue var11;
      if (var2 != null) {
         var11 = (AnimatableRectValue)var2;
         var7 += var3 * (var11.x - this.x);
         var8 += var3 * (var11.y - this.y);
         var9 += var3 * (var11.width - this.width);
         var10 += var3 * (var11.height - this.height);
      }

      if (var4 != null && var5 != 0) {
         var11 = (AnimatableRectValue)var4;
         var7 += (float)var5 * var11.x;
         var8 += (float)var5 * var11.y;
         var9 += (float)var5 * var11.width;
         var10 += (float)var5 * var11.height;
      }

      if (var6.x != var7 || var6.y != var8 || var6.width != var9 || var6.height != var10) {
         var6.x = var7;
         var6.y = var8;
         var6.width = var9;
         var6.height = var10;
         var6.hasChanged = true;
      }

      return var6;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableRectValue(this.target, 0.0F, 0.0F, 0.0F, 0.0F);
   }

   public String toStringRep() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.x);
      var1.append(',');
      var1.append(this.y);
      var1.append(',');
      var1.append(this.width);
      var1.append(',');
      var1.append(this.height);
      return var1.toString();
   }
}
