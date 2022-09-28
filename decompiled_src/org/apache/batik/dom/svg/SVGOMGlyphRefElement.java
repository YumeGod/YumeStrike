package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGGlyphRefElement;

public class SVGOMGlyphRefElement extends SVGStylableElement implements SVGGlyphRefElement {
   protected static final AttributeInitializer attributeInitializer = new AttributeInitializer(4);
   protected SVGOMAnimatedString href;

   protected SVGOMGlyphRefElement() {
   }

   public SVGOMGlyphRefElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.href = this.createLiveAnimatedString("http://www.w3.org/1999/xlink", "href");
   }

   public String getLocalName() {
      return "glyphRef";
   }

   public SVGAnimatedString getHref() {
      return this.href;
   }

   public String getGlyphRef() {
      return this.getAttributeNS((String)null, "glyphRef");
   }

   public void setGlyphRef(String var1) throws DOMException {
      this.setAttributeNS((String)null, "glyphRef", var1);
   }

   public String getFormat() {
      return this.getAttributeNS((String)null, "format");
   }

   public void setFormat(String var1) throws DOMException {
      this.setAttributeNS((String)null, "format", var1);
   }

   public float getX() {
      return Float.parseFloat(this.getAttributeNS((String)null, "x"));
   }

   public void setX(float var1) throws DOMException {
      this.setAttributeNS((String)null, "x", String.valueOf(var1));
   }

   public float getY() {
      return Float.parseFloat(this.getAttributeNS((String)null, "y"));
   }

   public void setY(float var1) throws DOMException {
      this.setAttributeNS((String)null, "y", String.valueOf(var1));
   }

   public float getDx() {
      return Float.parseFloat(this.getAttributeNS((String)null, "dx"));
   }

   public void setDx(float var1) throws DOMException {
      this.setAttributeNS((String)null, "dx", String.valueOf(var1));
   }

   public float getDy() {
      return Float.parseFloat(this.getAttributeNS((String)null, "dy"));
   }

   public void setDy(float var1) throws DOMException {
      this.setAttributeNS((String)null, "dy", String.valueOf(var1));
   }

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMGlyphRefElement();
   }

   static {
      attributeInitializer.addAttribute("http://www.w3.org/2000/xmlns/", (String)null, "xmlns:xlink", "http://www.w3.org/1999/xlink");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "type", "simple");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "show", "other");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "actuate", "onLoad");
   }
}
