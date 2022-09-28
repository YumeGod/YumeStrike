package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatablePreserveAspectRatioValue extends AnimatableValue {
   protected static final String[] ALIGN_VALUES = new String[]{null, "none", "xMinYMin", "xMidYMin", "xMaxYMin", "xMinYMid", "xMidYMid", "xMaxYMid", "xMinYMax", "xMidYMax", "xMaxYMax"};
   protected static final String[] MEET_OR_SLICE_VALUES = new String[]{null, "meet", "slice"};
   protected short align;
   protected short meetOrSlice;

   protected AnimatablePreserveAspectRatioValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatablePreserveAspectRatioValue(AnimationTarget var1, short var2, short var3) {
      super(var1);
      this.align = var2;
      this.meetOrSlice = var3;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatablePreserveAspectRatioValue var6;
      if (var1 == null) {
         var6 = new AnimatablePreserveAspectRatioValue(this.target);
      } else {
         var6 = (AnimatablePreserveAspectRatioValue)var1;
      }

      short var7;
      short var8;
      if (var2 != null && (double)var3 >= 0.5) {
         AnimatablePreserveAspectRatioValue var9 = (AnimatablePreserveAspectRatioValue)var2;
         var7 = var9.align;
         var8 = var9.meetOrSlice;
      } else {
         var7 = this.align;
         var8 = this.meetOrSlice;
      }

      if (var6.align != var7 || var6.meetOrSlice != var8) {
         var6.align = this.align;
         var6.meetOrSlice = this.meetOrSlice;
         var6.hasChanged = true;
      }

      return var6;
   }

   public short getAlign() {
      return this.align;
   }

   public short getMeetOrSlice() {
      return this.meetOrSlice;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      return new AnimatablePreserveAspectRatioValue(this.target, (short)1, (short)1);
   }

   public String toStringRep() {
      if (this.align >= 1 && this.align <= 10) {
         String var1 = ALIGN_VALUES[this.align];
         if (this.align == 1) {
            return var1;
         } else {
            return this.meetOrSlice >= 1 && this.meetOrSlice <= 2 ? var1 + ' ' + MEET_OR_SLICE_VALUES[this.meetOrSlice] : null;
         }
      } else {
         return null;
      }
   }
}
