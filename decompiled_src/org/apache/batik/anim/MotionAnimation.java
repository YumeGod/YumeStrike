package org.apache.batik.anim;

import java.awt.geom.Point2D;
import org.apache.batik.anim.timing.TimedElement;
import org.apache.batik.anim.values.AnimatableAngleValue;
import org.apache.batik.anim.values.AnimatableMotionPointValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimatableElement;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.ext.awt.geom.Cubic;
import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.apache.batik.ext.awt.geom.ExtendedPathIterator;
import org.apache.batik.ext.awt.geom.PathLength;

public class MotionAnimation extends InterpolatingAnimation {
   protected ExtendedGeneralPath path;
   protected PathLength pathLength;
   protected float[] keyPoints;
   protected boolean rotateAuto;
   protected boolean rotateAutoReverse;
   protected float rotateAngle;

   public MotionAnimation(TimedElement var1, AnimatableElement var2, int var3, float[] var4, float[] var5, boolean var6, boolean var7, AnimatableValue[] var8, AnimatableValue var9, AnimatableValue var10, AnimatableValue var11, ExtendedGeneralPath var12, float[] var13, boolean var14, boolean var15, float var16, short var17) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.rotateAuto = var14;
      this.rotateAutoReverse = var15;
      this.rotateAngle = AnimatableAngleValue.rad(var16, var17);
      if (var12 == null) {
         var12 = new ExtendedGeneralPath();
         AnimatableMotionPointValue var18;
         if (var8 != null && var8.length != 0) {
            var18 = (AnimatableMotionPointValue)var8[0];
            var12.moveTo(var18.getX(), var18.getY());

            for(int var26 = 1; var26 < var8.length; ++var26) {
               var18 = (AnimatableMotionPointValue)var8[var26];
               var12.lineTo(var18.getX(), var18.getY());
            }
         } else if (var9 != null) {
            var18 = (AnimatableMotionPointValue)var9;
            float var19 = var18.getX();
            float var20 = var18.getY();
            var12.moveTo(var19, var20);
            AnimatableMotionPointValue var21;
            if (var10 != null) {
               var21 = (AnimatableMotionPointValue)var10;
               var12.lineTo(var21.getX(), var21.getY());
            } else {
               if (var11 == null) {
                  throw var1.createException("values.to.by.path.missing", new Object[]{null});
               }

               var21 = (AnimatableMotionPointValue)var11;
               var12.lineTo(var19 + var21.getX(), var20 + var21.getY());
            }
         } else if (var10 != null) {
            var18 = (AnimatableMotionPointValue)var2.getUnderlyingValue();
            AnimatableMotionPointValue var24 = (AnimatableMotionPointValue)var10;
            var12.moveTo(var18.getX(), var18.getY());
            var12.lineTo(var24.getX(), var24.getY());
            this.cumulative = false;
         } else {
            if (var11 == null) {
               throw var1.createException("values.to.by.path.missing", new Object[]{null});
            }

            var18 = (AnimatableMotionPointValue)var11;
            var12.moveTo(0.0F, 0.0F);
            var12.lineTo(var18.getX(), var18.getY());
            this.additive = true;
         }
      }

      this.path = var12;
      this.pathLength = new PathLength(var12);
      int var25 = 0;

      ExtendedPathIterator var27;
      int var28;
      for(var27 = var12.getExtendedPathIterator(); !var27.isDone(); var27.next()) {
         var28 = var27.currentSegment();
         if (var28 != 0) {
            ++var25;
         }
      }

      var28 = var13 == null ? var25 + 1 : var13.length;
      float var29 = this.pathLength.lengthOfPath();
      int var22;
      int var23;
      if (this.keyTimes != null && var3 != 2) {
         if (this.keyTimes.length != var28) {
            throw var1.createException("attribute.malformed", new Object[]{null, "keyTimes"});
         }
      } else if (var3 != 1 && var3 != 3) {
         if (var3 == 0) {
            this.keyTimes = new float[var28];

            for(var22 = 0; var22 < var28; ++var22) {
               this.keyTimes[var22] = (float)var22 / (float)var28;
            }
         } else {
            var27 = var12.getExtendedPathIterator();
            this.keyTimes = new float[var28];
            var22 = 0;

            for(var23 = 0; var23 < var28 - 1; ++var23) {
               while(var27.currentSegment() == 0) {
                  ++var22;
                  var27.next();
               }

               this.keyTimes[var23] = this.pathLength.getLengthAtSegment(var22) / var29;
               ++var22;
               var27.next();
            }

            this.keyTimes[var28 - 1] = 1.0F;
         }
      } else {
         this.keyTimes = new float[var28];

         for(var22 = 0; var22 < var28; ++var22) {
            this.keyTimes[var22] = (float)var22 / (float)(var28 - 1);
         }
      }

      if (var13 != null) {
         if (var13.length != this.keyTimes.length) {
            throw var1.createException("attribute.malformed", new Object[]{null, "keyPoints"});
         }
      } else {
         var27 = var12.getExtendedPathIterator();
         var13 = new float[var28];
         var22 = 0;

         for(var23 = 0; var23 < var28 - 1; ++var23) {
            while(var27.currentSegment() == 0) {
               ++var22;
               var27.next();
            }

            var13[var23] = this.pathLength.getLengthAtSegment(var22) / var29;
            ++var22;
            var27.next();
         }

         var13[var28 - 1] = 1.0F;
      }

      this.keyPoints = var13;
   }

   protected void sampledAtUnitTime(float var1, int var2) {
      float var5 = 0.0F;
      AnimatableMotionPointValue var3;
      Point2D var15;
      float var16;
      if (var1 != 1.0F) {
         int var6;
         for(var6 = 0; var6 < this.keyTimes.length - 1 && var1 >= this.keyTimes[var6 + 1]; ++var6) {
         }

         float var9;
         if (var6 == this.keyTimes.length - 1 && this.calcMode == 0) {
            var6 = this.keyTimes.length - 2;
            var5 = 1.0F;
         } else if (this.calcMode == 1 || this.calcMode == 2 || this.calcMode == 3) {
            if (var1 == 0.0F) {
               var5 = 0.0F;
            } else {
               var5 = (var1 - this.keyTimes[var6]) / (this.keyTimes[var6 + 1] - this.keyTimes[var6]);
            }

            if (this.calcMode == 3 && var1 != 0.0F) {
               Cubic var7 = this.keySplineCubics[var6];
               float var8 = 0.001F;
               var9 = 0.0F;
               float var10 = 1.0F;

               while(true) {
                  float var12 = (var9 + var10) / 2.0F;
                  Point2D.Double var11 = var7.eval((double)var12);
                  double var13 = var11.getX();
                  if (Math.abs(var13 - (double)var5) < (double)var8) {
                     var5 = (float)var11.getY();
                     break;
                  }

                  if (var13 < (double)var5) {
                     var9 = var12;
                  } else {
                     var10 = var12;
                  }
               }
            }
         }

         var16 = this.keyPoints[var6];
         if (var5 != 0.0F) {
            var16 += var5 * (this.keyPoints[var6 + 1] - this.keyPoints[var6]);
         }

         var16 *= this.pathLength.lengthOfPath();
         Point2D var17 = this.pathLength.pointAtLength(var16);
         if (this.rotateAuto) {
            var9 = this.pathLength.angleAtLength(var16);
            if (this.rotateAutoReverse) {
               var9 = (float)((double)var9 + Math.PI);
            }
         } else {
            var9 = this.rotateAngle;
         }

         var3 = new AnimatableMotionPointValue((AnimationTarget)null, (float)var17.getX(), (float)var17.getY(), var9);
      } else {
         var15 = this.pathLength.pointAtLength(this.pathLength.lengthOfPath());
         if (this.rotateAuto) {
            var16 = this.pathLength.angleAtLength(this.pathLength.lengthOfPath());
            if (this.rotateAutoReverse) {
               var16 = (float)((double)var16 + Math.PI);
            }
         } else {
            var16 = this.rotateAngle;
         }

         var3 = new AnimatableMotionPointValue((AnimationTarget)null, (float)var15.getX(), (float)var15.getY(), var16);
      }

      AnimatableMotionPointValue var4;
      if (this.cumulative) {
         var15 = this.pathLength.pointAtLength(this.pathLength.lengthOfPath());
         if (this.rotateAuto) {
            var16 = this.pathLength.angleAtLength(this.pathLength.lengthOfPath());
            if (this.rotateAutoReverse) {
               var16 = (float)((double)var16 + Math.PI);
            }
         } else {
            var16 = this.rotateAngle;
         }

         var4 = new AnimatableMotionPointValue((AnimationTarget)null, (float)var15.getX(), (float)var15.getY(), var16);
      } else {
         var4 = null;
      }

      this.value = var3.interpolate(this.value, (AnimatableValue)null, var5, var4, var2);
      if (this.value.hasChanged()) {
         this.markDirty();
      }

   }
}
