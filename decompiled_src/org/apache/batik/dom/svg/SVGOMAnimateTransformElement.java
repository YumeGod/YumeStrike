package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimateTransformElement;

public class SVGOMAnimateTransformElement extends SVGOMAnimationElement implements SVGAnimateTransformElement {
   protected static final AttributeInitializer attributeInitializer = new AttributeInitializer(1);

   protected SVGOMAnimateTransformElement() {
   }

   public SVGOMAnimateTransformElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "animateTransform";
   }

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMAnimateTransformElement();
   }

   static {
      attributeInitializer.addAttribute((String)null, (String)null, "type", "translate");
   }
}
