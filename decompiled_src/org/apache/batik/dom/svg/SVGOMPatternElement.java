package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGAnimatedTransformList;
import org.w3c.dom.svg.SVGPatternElement;
import org.w3c.dom.svg.SVGStringList;

public class SVGOMPatternElement extends SVGStylableElement implements SVGPatternElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final AttributeInitializer attributeInitializer;
   protected static final String[] UNITS_VALUES;
   protected SVGOMAnimatedLength x;
   protected SVGOMAnimatedLength y;
   protected SVGOMAnimatedLength width;
   protected SVGOMAnimatedLength height;
   protected SVGOMAnimatedEnumeration patternUnits;
   protected SVGOMAnimatedEnumeration patternContentUnits;
   protected SVGOMAnimatedString href;
   protected SVGOMAnimatedBoolean externalResourcesRequired;
   protected SVGOMAnimatedPreserveAspectRatio preserveAspectRatio;

   protected SVGOMPatternElement() {
   }

   public SVGOMPatternElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.x = this.createLiveAnimatedLength((String)null, "x", "0", (short)2, false);
      this.y = this.createLiveAnimatedLength((String)null, "y", "0", (short)1, false);
      this.width = this.createLiveAnimatedLength((String)null, "width", "0", (short)2, true);
      this.height = this.createLiveAnimatedLength((String)null, "height", "0", (short)1, true);
      this.patternUnits = this.createLiveAnimatedEnumeration((String)null, "patternUnits", UNITS_VALUES, (short)2);
      this.patternContentUnits = this.createLiveAnimatedEnumeration((String)null, "patternContentUnits", UNITS_VALUES, (short)1);
      this.href = this.createLiveAnimatedString("http://www.w3.org/1999/xlink", "href");
      this.externalResourcesRequired = this.createLiveAnimatedBoolean((String)null, "externalResourcesRequired", false);
      this.preserveAspectRatio = this.createLiveAnimatedPreserveAspectRatio();
   }

   public String getLocalName() {
      return "pattern";
   }

   public SVGAnimatedTransformList getPatternTransform() {
      throw new UnsupportedOperationException("SVGPatternElement.getPatternTransform is not implemented");
   }

   public SVGAnimatedEnumeration getPatternUnits() {
      return this.patternUnits;
   }

   public SVGAnimatedEnumeration getPatternContentUnits() {
      return this.patternContentUnits;
   }

   public SVGAnimatedLength getX() {
      return this.x;
   }

   public SVGAnimatedLength getY() {
      return this.y;
   }

   public SVGAnimatedLength getWidth() {
      return this.width;
   }

   public SVGAnimatedLength getHeight() {
      return this.height;
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   public SVGAnimatedString getHref() {
      return this.href;
   }

   public SVGAnimatedRect getViewBox() {
      throw new UnsupportedOperationException("SVGFitToViewBox.getViewBox is not implemented");
   }

   public SVGAnimatedPreserveAspectRatio getPreserveAspectRatio() {
      return this.preserveAspectRatio;
   }

   public SVGAnimatedBoolean getExternalResourcesRequired() {
      return this.externalResourcesRequired;
   }

   public String getXMLlang() {
      return XMLSupport.getXMLLang(this);
   }

   public void setXMLlang(String var1) {
      this.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", var1);
   }

   public String getXMLspace() {
      return XMLSupport.getXMLSpace(this);
   }

   public void setXMLspace(String var1) {
      this.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:space", var1);
   }

   public SVGStringList getRequiredFeatures() {
      return SVGTestsSupport.getRequiredFeatures(this);
   }

   public SVGStringList getRequiredExtensions() {
      return SVGTestsSupport.getRequiredExtensions(this);
   }

   public SVGStringList getSystemLanguage() {
      return SVGTestsSupport.getSystemLanguage(this);
   }

   public boolean hasExtension(String var1) {
      return SVGTestsSupport.hasExtension(this, var1);
   }

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMPatternElement();
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGStylableElement.xmlTraitInformation);
      var0.put((Object)null, "x", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "y", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "width", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "height", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "patternUnits", new TraitInformation(true, 15));
      var0.put((Object)null, "patternContentUnits", new TraitInformation(true, 15));
      var0.put((Object)null, "patternTransform", new TraitInformation(true, 9));
      var0.put((Object)null, "viewBox", new TraitInformation(true, 13));
      var0.put((Object)null, "preserveAspectRatio", new TraitInformation(true, 32));
      var0.put((Object)null, "externalResourcesRequired", new TraitInformation(true, 49));
      xmlTraitInformation = var0;
      attributeInitializer = new AttributeInitializer(5);
      attributeInitializer.addAttribute((String)null, (String)null, "preserveAspectRatio", "xMidYMid meet");
      attributeInitializer.addAttribute("http://www.w3.org/2000/xmlns/", (String)null, "xmlns:xlink", "http://www.w3.org/1999/xlink");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "type", "simple");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "show", "other");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "actuate", "onLoad");
      UNITS_VALUES = new String[]{"", "userSpaceOnUse", "objectBoundingBox"};
   }
}
