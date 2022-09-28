package org.apache.batik.anim;

import org.apache.batik.anim.timing.TimedElement;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimatableElement;

public abstract class AbstractAnimation {
   public static final short CALC_MODE_DISCRETE = 0;
   public static final short CALC_MODE_LINEAR = 1;
   public static final short CALC_MODE_PACED = 2;
   public static final short CALC_MODE_SPLINE = 3;
   protected TimedElement timedElement;
   protected AnimatableElement animatableElement;
   protected AbstractAnimation lowerAnimation;
   protected AbstractAnimation higherAnimation;
   protected boolean isDirty;
   protected boolean isActive;
   protected boolean isFrozen;
   protected float beginTime;
   protected AnimatableValue value;
   protected AnimatableValue composedValue;
   protected boolean usesUnderlyingValue;
   protected boolean toAnimation;

   protected AbstractAnimation(TimedElement var1, AnimatableElement var2) {
      this.timedElement = var1;
      this.animatableElement = var2;
   }

   public TimedElement getTimedElement() {
      return this.timedElement;
   }

   public AnimatableValue getValue() {
      return !this.isActive && !this.isFrozen ? null : this.value;
   }

   public AnimatableValue getComposedValue() {
      if (!this.isActive && !this.isFrozen) {
         return null;
      } else {
         if (this.isDirty) {
            AnimatableValue var1 = null;
            if (!this.willReplace()) {
               if (this.lowerAnimation == null) {
                  var1 = this.animatableElement.getUnderlyingValue();
                  this.usesUnderlyingValue = true;
               } else {
                  var1 = this.lowerAnimation.getComposedValue();
                  this.usesUnderlyingValue = false;
               }
            }

            this.composedValue = this.value.interpolate(this.composedValue, (AnimatableValue)null, 0.0F, var1, 1);
            this.isDirty = false;
         }

         return this.composedValue;
      }
   }

   public String toString() {
      return this.timedElement.toString();
   }

   public boolean usesUnderlyingValue() {
      return this.usesUnderlyingValue || this.toAnimation;
   }

   protected boolean willReplace() {
      return true;
   }

   protected void markDirty() {
      this.isDirty = true;
      if (this.higherAnimation != null && !this.higherAnimation.willReplace() && !this.higherAnimation.isDirty) {
         this.higherAnimation.markDirty();
      }

   }

   protected void sampledLastValue(int var1) {
   }

   protected abstract void sampledAt(float var1, float var2, int var3);
}
