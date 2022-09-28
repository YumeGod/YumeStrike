package org.apache.batik.extension.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.extension.GraphicsExtensionElement;
import org.w3c.dom.Node;

public class BatikRegularPolygonElement extends GraphicsExtensionElement implements BatikExtConstants {
   protected BatikRegularPolygonElement() {
   }

   public BatikRegularPolygonElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "regularPolygon";
   }

   public String getNamespaceURI() {
      return "http://xml.apache.org/batik/ext";
   }

   protected Node newNode() {
      return new BatikRegularPolygonElement();
   }
}
