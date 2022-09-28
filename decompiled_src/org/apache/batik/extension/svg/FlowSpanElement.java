package org.apache.batik.extension.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGOMTextPositioningElement;
import org.w3c.dom.Node;

public class FlowSpanElement extends SVGOMTextPositioningElement implements BatikExtConstants {
   protected FlowSpanElement() {
   }

   public FlowSpanElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "flowSpan";
   }

   public String getNamespaceURI() {
      return "http://xml.apache.org/batik/ext";
   }

   protected Node newNode() {
      return new FlowSpanElement();
   }
}
