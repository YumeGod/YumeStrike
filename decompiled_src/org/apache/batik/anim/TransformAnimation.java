package org.apache.batik.anim;

import org.apache.batik.anim.timing.TimedElement;
import org.apache.batik.anim.values.AnimatableTransformListValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimatableElement;

public class TransformAnimation extends SimpleAnimation {
   protected short type;
   protected float[] keyTimes2;
   protected float[] keyTimes3;

   public TransformAnimation(TimedElement var1, AnimatableElement var2, int var3, float[] var4, float[] var5, boolean var6, boolean var7, AnimatableValue[] var8, AnimatableValue var9, AnimatableValue var10, AnimatableValue var11, short var12) {
      super(var1, var2, var3 == 2 ? 1 : var3, var3 == 2 ? null : var4, var5, var6, var7, var8, var9, var10, var11);
      this.calcMode = var3;
      this.type = var12;
      if (var3 == 2) {
         int var13 = this.values.length;
         float[] var15 = null;
         float[] var16 = null;
         switch (var12) {
            case 4:
               var16 = new float[var13];
               var16[0] = 0.0F;
            case 2:
            case 3:
               var15 = new float[var13];
               var15[0] = 0.0F;
            default:
               float[] var14 = new float[var13];
               var14[0] = 0.0F;
               int var17 = 1;

               while(var17 < this.values.length) {
                  switch (var12) {
                     case 4:
                        var16[var17] = var16[var17 - 1] + ((AnimatableTransformListValue)this.values[var17 - 1]).distanceTo3(this.values[var17]);
                     case 2:
                     case 3:
                        var15[var17] = var15[var17 - 1] + ((AnimatableTransformListValue)this.values[var17 - 1]).distanceTo2(this.values[var17]);
                     default:
                        var14[var17] = var14[var17 - 1] + ((AnimatableTransformListValue)this.values[var17 - 1]).distanceTo1(this.values[var17]);
                        ++var17;
                  }
               }

               int var18;
               float var19;
               switch (var12) {
                  case 4:
                     var19 = var16[var13 - 1];
                     this.keyTimes3 = new float[var13];
                     this.keyTimes3[0] = 0.0F;

                     for(var18 = 1; var18 < var13 - 1; ++var18) {
                        this.keyTimes3[var18] = var16[var18] / var19;
                     }

                     this.keyTimes3[var13 - 1] = 1.0F;
                  case 2:
                  case 3:
                     var19 = var15[var13 - 1];
                     this.keyTimes2 = new float[var13];
                     this.keyTimes2[0] = 0.0F;

                     for(var18 = 1; var18 < var13 - 1; ++var18) {
                        this.keyTimes2[var18] = var15[var18] / var19;
                     }

                     this.keyTimes2[var13 - 1] = 1.0F;
               }

               var19 = var14[var13 - 1];
               this.keyTimes = new float[var13];
               this.keyTimes[0] = 0.0F;

               for(var18 = 1; var18 < var13 - 1; ++var18) {
                  this.keyTimes[var18] = var14[var18] / var19;
               }

               this.keyTimes[var13 - 1] = 1.0F;
         }
      }
   }

   protected void sampledAtUnitTime(float var1, int var2) {
      if (this.calcMode == 2 && this.type != 5 && this.type != 6) {
         AnimatableTransformListValue var5 = null;
         AnimatableTransformListValue var8 = null;
         float var10 = 0.0F;
         float var11 = 0.0F;
         float var12 = 0.0F;
         AnimatableTransformListValue var3;
         AnimatableTransformListValue var4;
         AnimatableTransformListValue var6;
         AnimatableTransformListValue var7;
         if (var1 != 1.0F) {
            int var13;
            switch (this.type) {
               case 4:
                  for(var13 = 0; var13 < this.keyTimes3.length - 1 && var1 >= this.keyTimes3[var13 + 1]; ++var13) {
                  }

                  var5 = (AnimatableTransformListValue)this.values[var13];
                  var8 = (AnimatableTransformListValue)this.values[var13 + 1];
                  var12 = (var1 - this.keyTimes3[var13]) / (this.keyTimes3[var13 + 1] - this.keyTimes3[var13]);
            }

            for(var13 = 0; var13 < this.keyTimes2.length - 1 && var1 >= this.keyTimes2[var13 + 1]; ++var13) {
            }

            var4 = (AnimatableTransformListValue)this.values[var13];
            var7 = (AnimatableTransformListValue)this.values[var13 + 1];
            var11 = (var1 - this.keyTimes2[var13]) / (this.keyTimes2[var13 + 1] - this.keyTimes2[var13]);

            for(var13 = 0; var13 < this.keyTimes.length - 1 && var1 >= this.keyTimes[var13 + 1]; ++var13) {
            }

            var3 = (AnimatableTransformListValue)this.values[var13];
            var6 = (AnimatableTransformListValue)this.values[var13 + 1];
            var10 = (var1 - this.keyTimes[var13]) / (this.keyTimes[var13 + 1] - this.keyTimes[var13]);
         } else {
            var3 = var4 = var5 = (AnimatableTransformListValue)this.values[this.values.length - 1];
            var8 = null;
            var7 = null;
            var6 = null;
            var12 = 1.0F;
            var11 = 1.0F;
            var10 = 1.0F;
         }

         AnimatableTransformListValue var9;
         if (this.cumulative) {
            var9 = (AnimatableTransformListValue)this.values[this.values.length - 1];
         } else {
            var9 = null;
         }

         switch (this.type) {
            case 4:
               this.value = AnimatableTransformListValue.interpolate((AnimatableTransformListValue)this.value, var3, var4, var5, var6, var7, var8, var10, var11, var12, var9, var2);
               break;
            default:
               this.value = AnimatableTransformListValue.interpolate((AnimatableTransformListValue)this.value, var3, var4, var6, var7, var10, var11, var9, var2);
         }

         if (this.value.hasChanged()) {
            this.markDirty();
         }

      } else {
         super.sampledAtUnitTime(var1, var2);
      }
   }
}
