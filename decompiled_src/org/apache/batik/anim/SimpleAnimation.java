package org.apache.batik.anim;

import java.awt.geom.Point2D;
import org.apache.batik.anim.timing.TimedElement;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimatableElement;
import org.apache.batik.ext.awt.geom.Cubic;

public class SimpleAnimation extends InterpolatingAnimation {
   protected AnimatableValue[] values;
   protected AnimatableValue from;
   protected AnimatableValue to;
   protected AnimatableValue by;

   public SimpleAnimation(TimedElement var1, AnimatableElement var2, int var3, float[] var4, float[] var5, boolean var6, boolean var7, AnimatableValue[] var8, AnimatableValue var9, AnimatableValue var10, AnimatableValue var11) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.from = var9;
      this.to = var10;
      this.by = var11;
      if (var8 == null) {
         if (var9 != null) {
            var8 = new AnimatableValue[]{var9, null};
            if (var10 != null) {
               var8[1] = var10;
            } else {
               if (var11 == null) {
                  throw var1.createException("values.to.by.missing", new Object[]{null});
               }

               var8[1] = var9.interpolate((AnimatableValue)null, (AnimatableValue)null, 0.0F, var11, 1);
            }
         } else if (var10 != null) {
            var8 = new AnimatableValue[]{var2.getUnderlyingValue(), var10};
            this.cumulative = false;
            this.toAnimation = true;
         } else {
            if (var11 == null) {
               throw var1.createException("values.to.by.missing", new Object[]{null});
            }

            this.additive = true;
            var8 = new AnimatableValue[]{var11.getZeroValue(), var11};
         }
      }

      this.values = var8;
      if (this.keyTimes != null && var3 != 2) {
         if (this.keyTimes.length != var8.length) {
            throw var1.createException("attribute.malformed", new Object[]{null, "keyTimes"});
         }
      } else {
         int var12;
         int var13;
         if (var3 != 1 && var3 != 3 && (var3 != 2 || var8[0].canPace())) {
            if (var3 == 0) {
               var12 = var8.length;
               this.keyTimes = new float[var12];

               for(var13 = 0; var13 < var12; ++var13) {
                  this.keyTimes[var13] = (float)var13 / (float)var12;
               }
            } else {
               var12 = var8.length;
               float[] var16 = new float[var12];
               var16[0] = 0.0F;

               for(int var14 = 1; var14 < var12; ++var14) {
                  var16[var14] = var16[var14 - 1] + var8[var14 - 1].distanceTo(var8[var14]);
               }

               float var17 = var16[var12 - 1];
               this.keyTimes = new float[var12];
               this.keyTimes[0] = 0.0F;

               for(int var15 = 1; var15 < var12 - 1; ++var15) {
                  this.keyTimes[var15] = var16[var15] / var17;
               }

               this.keyTimes[var12 - 1] = 1.0F;
            }
         } else {
            var12 = var8.length == 1 ? 2 : var8.length;
            this.keyTimes = new float[var12];

            for(var13 = 0; var13 < var12; ++var13) {
               this.keyTimes[var13] = (float)var13 / (float)(var12 - 1);
            }
         }
      }

      if (var3 == 3 && var5.length != (this.keyTimes.length - 1) * 4) {
         throw var1.createException("attribute.malformed", new Object[]{null, "keySplines"});
      }
   }

   protected void sampledAtUnitTime(float var1, int var2) {
      float var6 = 0.0F;
      AnimatableValue var3;
      AnimatableValue var5;
      if (var1 != 1.0F) {
         int var7;
         for(var7 = 0; var7 < this.keyTimes.length - 1 && var1 >= this.keyTimes[var7 + 1]; ++var7) {
         }

         var3 = this.values[var7];
         if (this.calcMode != 1 && this.calcMode != 2 && this.calcMode != 3) {
            var5 = null;
         } else {
            var5 = this.values[var7 + 1];
            var6 = (var1 - this.keyTimes[var7]) / (this.keyTimes[var7 + 1] - this.keyTimes[var7]);
            if (this.calcMode == 3 && var1 != 0.0F) {
               Cubic var8 = this.keySplineCubics[var7];
               float var9 = 0.001F;
               float var10 = 0.0F;
               float var11 = 1.0F;

               while(true) {
                  float var13 = (var10 + var11) / 2.0F;
                  Point2D.Double var12 = var8.eval((double)var13);
                  double var14 = var12.getX();
                  if (Math.abs(var14 - (double)var6) < (double)var9) {
                     var6 = (float)var12.getY();
                     break;
                  }

                  if (var14 < (double)var6) {
                     var10 = var13;
                  } else {
                     var11 = var13;
                  }
               }
            }
         }
      } else {
         var3 = this.values[this.values.length - 1];
         var5 = null;
      }

      AnimatableValue var4;
      if (this.cumulative) {
         var4 = this.values[this.values.length - 1];
      } else {
         var4 = null;
      }

      this.value = var3.interpolate(this.value, var5, var6, var4, var2);
      if (this.value.hasChanged()) {
         this.markDirty();
      }

   }
}
