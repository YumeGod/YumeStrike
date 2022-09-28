package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGTextPathElement;

public class SVGOMTextPathElement extends SVGOMTextContentElement implements SVGTextPathElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final AttributeInitializer attributeInitializer;
   protected static final String[] METHOD_VALUES;
   protected static final String[] SPACING_VALUES;
   protected SVGOMAnimatedEnumeration method;
   protected SVGOMAnimatedEnumeration spacing;
   protected SVGOMAnimatedLength startOffset;
   protected SVGOMAnimatedString href;

   protected SVGOMTextPathElement() {
   }

   public SVGOMTextPathElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.method = this.createLiveAnimatedEnumeration((String)null, "method", METHOD_VALUES, (short)1);
      this.spacing = this.createLiveAnimatedEnumeration((String)null, "spacing", SPACING_VALUES, (short)2);
      this.startOffset = this.createLiveAnimatedLength((String)null, "startOffset", "0", (short)0, true);
      this.href = this.createLiveAnimatedString("http://www.w3.org/1999/xlink", "href");
   }

   public String getLocalName() {
      return "textPath";
   }

   public SVGAnimatedLength getStartOffset() {
      return this.startOffset;
   }

   public SVGAnimatedEnumeration getMethod() {
      return this.method;
   }

   public SVGAnimatedEnumeration getSpacing() {
      return this.spacing;
   }

   public SVGAnimatedString getHref() {
      return this.href;
   }

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMTextPathElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMTextContentElement.xmlTraitInformation);
      var0.put((Object)null, "method", new TraitInformation(true, 15));
      var0.put((Object)null, "spacing", new TraitInformation(true, 15));
      var0.put((Object)null, "startOffset", new TraitInformation(true, 3));
      var0.put("http://www.w3.org/1999/xlink", "href", new TraitInformation(true, 10));
      xmlTraitInformation = var0;
      attributeInitializer = new AttributeInitializer(4);
      attributeInitializer.addAttribute("http://www.w3.org/2000/xmlns/", (String)null, "xmlns:xlink", "http://www.w3.org/1999/xlink");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "type", "simple");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "show", "other");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "actuate", "onLoad");
      METHOD_VALUES = new String[]{"", "align", "stretch"};
      SPACING_VALUES = new String[]{"", "auto", "exact"};
   }
}
