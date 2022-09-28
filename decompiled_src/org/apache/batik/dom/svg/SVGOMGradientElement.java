package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGAnimatedTransformList;
import org.w3c.dom.svg.SVGGradientElement;

public abstract class SVGOMGradientElement extends SVGStylableElement implements SVGGradientElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final AttributeInitializer attributeInitializer;
   protected static final String[] UNITS_VALUES;
   protected static final String[] SPREAD_METHOD_VALUES;
   protected SVGOMAnimatedEnumeration gradientUnits;
   protected SVGOMAnimatedEnumeration spreadMethod;
   protected SVGOMAnimatedString href;
   protected SVGOMAnimatedBoolean externalResourcesRequired;

   protected SVGOMGradientElement() {
   }

   protected SVGOMGradientElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.gradientUnits = this.createLiveAnimatedEnumeration((String)null, "gradientUnits", UNITS_VALUES, (short)2);
      this.spreadMethod = this.createLiveAnimatedEnumeration((String)null, "spreadMethod", SPREAD_METHOD_VALUES, (short)1);
      this.href = this.createLiveAnimatedString("http://www.w3.org/1999/xlink", "href");
      this.externalResourcesRequired = this.createLiveAnimatedBoolean((String)null, "externalResourcesRequired", false);
   }

   public SVGAnimatedTransformList getGradientTransform() {
      throw new UnsupportedOperationException("SVGGradientElement.getGradientTransform is not implemented");
   }

   public SVGAnimatedEnumeration getGradientUnits() {
      return this.gradientUnits;
   }

   public SVGAnimatedEnumeration getSpreadMethod() {
      return this.spreadMethod;
   }

   public SVGAnimatedString getHref() {
      return this.href;
   }

   public SVGAnimatedBoolean getExternalResourcesRequired() {
      return this.externalResourcesRequired;
   }

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMAElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGStylableElement.xmlTraitInformation);
      var0.put((Object)null, "gradientUnits", new TraitInformation(true, 15));
      var0.put((Object)null, "spreadMethod", new TraitInformation(true, 15));
      var0.put((Object)null, "gradientTransform", new TraitInformation(true, 9));
      var0.put((Object)null, "externalResourcesRequired", new TraitInformation(true, 49));
      var0.put("http://www.w3.org/1999/xlink", "href", new TraitInformation(true, 10));
      xmlTraitInformation = var0;
      attributeInitializer = new AttributeInitializer(4);
      attributeInitializer.addAttribute("http://www.w3.org/2000/xmlns/", (String)null, "xmlns:xlink", "http://www.w3.org/1999/xlink");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "type", "simple");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "show", "other");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "actuate", "onLoad");
      UNITS_VALUES = new String[]{"", "userSpaceOnUse", "objectBoundingBox"};
      SPREAD_METHOD_VALUES = new String[]{"", "pad", "reflect", "repeat"};
   }
}
