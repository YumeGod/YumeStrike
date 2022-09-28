package org.apache.batik.bridge;

import java.util.Calendar;
import org.apache.batik.anim.timing.TimedElement;
import org.apache.batik.dom.events.DOMTimeEvent;
import org.apache.batik.dom.svg.IdContainer;
import org.apache.batik.dom.svg.SVGOMAnimationElement;
import org.apache.batik.dom.svg.SVGOMUseShadowRoot;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;

public abstract class AnimationSupport {
   public static void fireTimeEvent(EventTarget var0, String var1, Calendar var2, int var3) {
      DocumentEvent var4 = (DocumentEvent)((Node)var0).getOwnerDocument();
      DOMTimeEvent var5 = (DOMTimeEvent)var4.createEvent("TimeEvent");
      var5.initTimeEventNS("http://www.w3.org/2001/xml-events", var1, (AbstractView)null, var3);
      var5.setTimestamp(var2.getTime().getTime());
      var0.dispatchEvent(var5);
   }

   public static TimedElement getTimedElementById(String var0, Node var1) {
      Element var2 = getElementById(var0, var1);
      if (var2 instanceof SVGOMAnimationElement) {
         SVGAnimationElementBridge var3 = (SVGAnimationElementBridge)((SVGOMAnimationElement)var2).getSVGContext();
         return var3.getTimedElement();
      } else {
         return null;
      }
   }

   public static EventTarget getEventTargetById(String var0, Node var1) {
      return (EventTarget)getElementById(var0, var1);
   }

   protected static Element getElementById(String var0, Node var1) {
      Node var2 = var1.getParentNode();

      while(var2 != null) {
         var1 = var2;
         if (var2 instanceof SVGOMUseShadowRoot) {
            var2 = ((SVGOMUseShadowRoot)var2).getCSSParentNode();
         } else {
            var2 = var2.getParentNode();
         }
      }

      if (var1 instanceof IdContainer) {
         return ((IdContainer)var1).getElementById(var0);
      } else {
         return null;
      }
   }
}
