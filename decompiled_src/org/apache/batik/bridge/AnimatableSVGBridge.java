package org.apache.batik.bridge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.dom.anim.AnimationTargetListener;
import org.apache.batik.dom.svg.SVGAnimationTargetContext;
import org.w3c.dom.Element;

public abstract class AnimatableSVGBridge extends AbstractSVGBridge implements SVGAnimationTargetContext {
   protected Element e;
   protected BridgeContext ctx;
   protected HashMap targetListeners;

   public void addTargetListener(String var1, AnimationTargetListener var2) {
      if (this.targetListeners == null) {
         this.targetListeners = new HashMap();
      }

      LinkedList var3 = (LinkedList)this.targetListeners.get(var1);
      if (var3 == null) {
         var3 = new LinkedList();
         this.targetListeners.put(var1, var3);
      }

      var3.add(var2);
   }

   public void removeTargetListener(String var1, AnimationTargetListener var2) {
      LinkedList var3 = (LinkedList)this.targetListeners.get(var1);
      var3.remove(var2);
   }

   protected void fireBaseAttributeListeners(String var1) {
      if (this.targetListeners != null) {
         LinkedList var2 = (LinkedList)this.targetListeners.get(var1);
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               AnimationTargetListener var4 = (AnimationTargetListener)var3.next();
               var4.baseValueChanged((AnimationTarget)this.e, (String)null, var1, true);
            }
         }
      }

   }
}
