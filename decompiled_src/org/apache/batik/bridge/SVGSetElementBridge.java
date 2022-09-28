package org.apache.batik.bridge;

import org.apache.batik.anim.AbstractAnimation;
import org.apache.batik.anim.SetAnimation;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;

public class SVGSetElementBridge extends SVGAnimationElementBridge {
   public String getLocalName() {
      return "set";
   }

   public Bridge getInstance() {
      return new SVGSetElementBridge();
   }

   protected AbstractAnimation createAnimation(AnimationTarget var1) {
      AnimatableValue var2 = this.parseAnimatableValue("to");
      return new SetAnimation(this.timedElement, this, var2);
   }

   protected boolean canAnimateType(int var1) {
      return true;
   }

   protected boolean isConstantAnimation() {
      return true;
   }
}
