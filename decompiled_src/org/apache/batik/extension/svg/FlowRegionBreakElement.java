package org.apache.batik.extension.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGOMTextPositioningElement;
import org.w3c.dom.Node;

public class FlowRegionBreakElement extends SVGOMTextPositioningElement implements BatikExtConstants {
   protected FlowRegionBreakElement() {
   }

   public FlowRegionBreakElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "flowRegionBreak";
   }

   public String getNamespaceURI() {
      return "http://xml.apache.org/batik/ext";
   }

   protected Node newNode() {
      return new FlowRegionBreakElement();
   }
}
