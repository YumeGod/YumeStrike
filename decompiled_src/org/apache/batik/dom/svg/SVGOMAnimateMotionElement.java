package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimateMotionElement;

public class SVGOMAnimateMotionElement extends SVGOMAnimationElement implements SVGAnimateMotionElement {
   protected static final AttributeInitializer attributeInitializer = new AttributeInitializer(1);

   protected SVGOMAnimateMotionElement() {
   }

   public SVGOMAnimateMotionElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "animateMotion";
   }

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMAnimateMotionElement();
   }

   static {
      attributeInitializer.addAttribute((String)null, (String)null, "calcMode", "paced");
   }
}
