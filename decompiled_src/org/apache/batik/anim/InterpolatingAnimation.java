package org.apache.batik.anim;

import org.apache.batik.anim.timing.TimedElement;
import org.apache.batik.dom.anim.AnimatableElement;
import org.apache.batik.ext.awt.geom.Cubic;

public abstract class InterpolatingAnimation extends AbstractAnimation {
   protected int calcMode;
   protected float[] keyTimes;
   protected float[] keySplines;
   protected Cubic[] keySplineCubics;
   protected boolean additive;
   protected boolean cumulative;

   public InterpolatingAnimation(TimedElement var1, AnimatableElement var2, int var3, float[] var4, float[] var5, boolean var6, boolean var7) {
      super(var1, var2);
      this.calcMode = var3;
      this.keyTimes = var4;
      this.keySplines = var5;
      this.additive = var6;
      this.cumulative = var7;
      if (var3 == 3) {
         if (var5 == null || var5.length % 4 != 0) {
            throw var1.createException("attribute.malformed", new Object[]{null, "keySplines"});
         }

         this.keySplineCubics = new Cubic[var5.length / 4];

         for(int var8 = 0; var8 < var5.length / 4; ++var8) {
            this.keySplineCubics[var8] = new Cubic(0.0, 0.0, (double)var5[var8 * 4], (double)var5[var8 * 4 + 1], (double)var5[var8 * 4 + 2], (double)var5[var8 * 4 + 3], 1.0, 1.0);
         }
      }

      if (var4 != null) {
         boolean var10 = false;
         if ((var3 == 1 || var3 == 3 || var3 == 2) && (var4.length < 2 || var4[0] != 0.0F || var4[var4.length - 1] != 1.0F) || var3 == 0 && (var4.length == 0 || var4[0] != 0.0F)) {
            var10 = true;
         }

         if (!var10) {
            for(int var9 = 1; var9 < var4.length; ++var9) {
               if (var4[var9] < 0.0F || var4[1] > 1.0F || var4[var9] < var4[var9 - 1]) {
                  var10 = true;
                  break;
               }
            }
         }

         if (var10) {
            throw var1.createException("attribute.malformed", new Object[]{null, "keyTimes"});
         }
      }

   }

   protected boolean willReplace() {
      return !this.additive;
   }

   protected void sampledLastValue(int var1) {
      this.sampledAtUnitTime(1.0F, var1);
   }

   protected void sampledAt(float var1, float var2, int var3) {
      float var4;
      if (var2 == Float.POSITIVE_INFINITY) {
         var4 = 0.0F;
      } else {
         var4 = var1 / var2;
      }

      this.sampledAtUnitTime(var4, var3);
   }

   protected abstract void sampledAtUnitTime(float var1, int var2);
}
