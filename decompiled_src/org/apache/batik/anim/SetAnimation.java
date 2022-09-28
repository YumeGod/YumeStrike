package org.apache.batik.anim;

import org.apache.batik.anim.timing.TimedElement;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimatableElement;

public class SetAnimation extends AbstractAnimation {
   protected AnimatableValue to;

   public SetAnimation(TimedElement var1, AnimatableElement var2, AnimatableValue var3) {
      super(var1, var2);
      this.to = var3;
   }

   protected void sampledAt(float var1, float var2, int var3) {
      if (this.value == null) {
         this.value = this.to;
         this.markDirty();
      }

   }

   protected void sampledLastValue(int var1) {
      if (this.value == null) {
         this.value = this.to;
         this.markDirty();
      }

   }
}
