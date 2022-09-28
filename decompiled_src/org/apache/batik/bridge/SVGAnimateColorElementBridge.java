package org.apache.batik.bridge;

import org.apache.batik.anim.AbstractAnimation;
import org.apache.batik.anim.ColorAnimation;
import org.apache.batik.anim.values.AnimatableColorValue;
import org.apache.batik.anim.values.AnimatablePaintValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;

public class SVGAnimateColorElementBridge extends SVGAnimateElementBridge {
   public String getLocalName() {
      return "animateColor";
   }

   public Bridge getInstance() {
      return new SVGAnimateColorElementBridge();
   }

   protected AbstractAnimation createAnimation(AnimationTarget var1) {
      AnimatableValue var2 = this.parseAnimatableValue("from");
      AnimatableValue var3 = this.parseAnimatableValue("to");
      AnimatableValue var4 = this.parseAnimatableValue("by");
      return new ColorAnimation(this.timedElement, this, this.parseCalcMode(), this.parseKeyTimes(), this.parseKeySplines(), this.parseAdditive(), this.parseAccumulate(), this.parseValues(), var2, var3, var4);
   }

   protected boolean canAnimateType(int var1) {
      return var1 == 6 || var1 == 7;
   }

   protected boolean checkValueType(AnimatableValue var1) {
      if (var1 instanceof AnimatablePaintValue) {
         return ((AnimatablePaintValue)var1).getPaintType() == 2;
      } else {
         return var1 instanceof AnimatableColorValue;
      }
   }
}
