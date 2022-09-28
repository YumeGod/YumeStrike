package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGColorProfileElement;

public class SVGOMColorProfileElement extends SVGOMURIReferenceElement implements SVGColorProfileElement {
   protected static final AttributeInitializer attributeInitializer = new AttributeInitializer(5);

   protected SVGOMColorProfileElement() {
   }

   public SVGOMColorProfileElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "color-profile";
   }

   public String getLocal() {
      return this.getAttributeNS((String)null, "local");
   }

   public void setLocal(String var1) throws DOMException {
      this.setAttributeNS((String)null, "local", var1);
   }

   public String getName() {
      return this.getAttributeNS((String)null, "name");
   }

   public void setName(String var1) throws DOMException {
      this.setAttributeNS((String)null, "name", var1);
   }

   public short getRenderingIntent() {
      Attr var1 = this.getAttributeNodeNS((String)null, "rendering-intent");
      if (var1 == null) {
         return 1;
      } else {
         String var2 = var1.getValue();
         switch (var2.length()) {
            case 4:
               if (var2.equals("auto")) {
                  return 1;
               }
               break;
            case 10:
               if (var2.equals("perceptual")) {
                  return 2;
               }

               if (var2.equals("saturate")) {
                  return 4;
               }
               break;
            case 21:
               if (var2.equals("absolute-colorimetric")) {
                  return 5;
               }

               if (var2.equals("relative-colorimetric")) {
                  return 3;
               }
         }

         return 0;
      }
   }

   public void setRenderingIntent(short var1) throws DOMException {
      switch (var1) {
         case 1:
            this.setAttributeNS((String)null, "rendering-intent", "auto");
            break;
         case 2:
            this.setAttributeNS((String)null, "rendering-intent", "perceptual");
            break;
         case 3:
            this.setAttributeNS((String)null, "rendering-intent", "relative-colorimetric");
            break;
         case 4:
            this.setAttributeNS((String)null, "rendering-intent", "saturate");
            break;
         case 5:
            this.setAttributeNS((String)null, "rendering-intent", "absolute-colorimetric");
      }

   }

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMColorProfileElement();
   }

   static {
      attributeInitializer.addAttribute((String)null, (String)null, "rendering-intent", "auto");
      attributeInitializer.addAttribute("http://www.w3.org/2000/xmlns/", (String)null, "xmlns:xlink", "http://www.w3.org/1999/xlink");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "type", "simple");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "show", "other");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "actuate", "onLoad");
   }
}
