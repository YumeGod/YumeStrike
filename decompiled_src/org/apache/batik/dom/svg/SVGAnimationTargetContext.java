package org.apache.batik.dom.svg;

import org.apache.batik.dom.anim.AnimationTargetListener;

public interface SVGAnimationTargetContext extends SVGContext {
   void addTargetListener(String var1, AnimationTargetListener var2);

   void removeTargetListener(String var1, AnimationTargetListener var2);
}
