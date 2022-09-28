package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatableMotionPointValue extends AnimatableValue {
   protected float x;
   protected float y;
   protected float angle;

   protected AnimatableMotionPointValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableMotionPointValue(AnimationTarget var1, float var2, float var3, float var4) {
      super(var1);
      this.x = var2;
      this.y = var3;
      this.angle = var4;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableMotionPointValue var6;
      if (var1 == null) {
         var6 = new AnimatableMotionPointValue(this.target);
      } else {
         var6 = (AnimatableMotionPointValue)var1;
      }

      float var7 = this.x;
      float var8 = this.y;
      float var9 = this.angle;
      int var10 = 1;
      AnimatableMotionPointValue var11;
      if (var2 != null) {
         var11 = (AnimatableMotionPointValue)var2;
         var7 += var3 * (var11.x - this.x);
         var8 += var3 * (var11.y - this.y);
         var9 += var11.angle;
         ++var10;
      }

      if (var4 != null && var5 != 0) {
         var11 = (AnimatableMotionPointValue)var4;
         var7 += (float)var5 * var11.x;
         var8 += (float)var5 * var11.y;
         var9 += var11.angle;
         ++var10;
      }

      var9 /= (float)var10;
      if (var6.x != var7 || var6.y != var8 || var6.angle != var9) {
         var6.x = var7;
         var6.y = var8;
         var6.angle = var9;
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

   public float getAngle() {
      return this.angle;
   }

   public boolean canPace() {
      return true;
   }

   public float distanceTo(AnimatableValue var1) {
      AnimatableMotionPointValue var2 = (AnimatableMotionPointValue)var1;
      float var3 = this.x - var2.x;
      float var4 = this.y - var2.y;
      return (float)Math.sqrt((double)(var3 * var3 + var4 * var4));
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableMotionPointValue(this.target, 0.0F, 0.0F, 0.0F);
   }

   public String toStringRep() {
      StringBuffer var1 = new StringBuffer();
      var1.append(formatNumber(this.x));
      var1.append(',');
      var1.append(formatNumber(this.y));
      var1.append(',');
      var1.append(formatNumber(this.angle));
      var1.append("rad");
      return var1.toString();
   }
}
