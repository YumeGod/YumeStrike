package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedInteger;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFilterElement;

public class SVGOMFilterElement extends SVGStylableElement implements SVGFilterElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final AttributeInitializer attributeInitializer;
   protected static final String[] UNITS_VALUES;
   protected SVGOMAnimatedEnumeration filterUnits;
   protected SVGOMAnimatedEnumeration primitiveUnits;
   protected SVGOMAnimatedLength x;
   protected SVGOMAnimatedLength y;
   protected SVGOMAnimatedLength width;
   protected SVGOMAnimatedLength height;
   protected SVGOMAnimatedString href;
   protected SVGOMAnimatedBoolean externalResourcesRequired;

   protected SVGOMFilterElement() {
   }

   public SVGOMFilterElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.filterUnits = this.createLiveAnimatedEnumeration((String)null, "filterUnits", UNITS_VALUES, (short)2);
      this.primitiveUnits = this.createLiveAnimatedEnumeration((String)null, "primitiveUnits", UNITS_VALUES, (short)1);
      this.x = this.createLiveAnimatedLength((String)null, "x", "-10%", (short)2, false);
      this.y = this.createLiveAnimatedLength((String)null, "y", "-10%", (short)1, false);
      this.width = this.createLiveAnimatedLength((String)null, "width", "120%", (short)2, true);
      this.height = this.createLiveAnimatedLength((String)null, "height", "120%", (short)1, true);
      this.href = this.createLiveAnimatedString("http://www.w3.org/1999/xlink", "href");
      this.externalResourcesRequired = this.createLiveAnimatedBoolean((String)null, "externalResourcesRequired", false);
   }

   public String getLocalName() {
      return "filter";
   }

   public SVGAnimatedEnumeration getFilterUnits() {
      return this.filterUnits;
   }

   public SVGAnimatedEnumeration getPrimitiveUnits() {
      return this.primitiveUnits;
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

   public SVGAnimatedInteger getFilterResX() {
      throw new UnsupportedOperationException("SVGFilterElement.getFilterResX is not implemented");
   }

   public SVGAnimatedInteger getFilterResY() {
      throw new UnsupportedOperationException("SVGFilterElement.getFilterResY is not implemented");
   }

   public void setFilterRes(int var1, int var2) {
      throw new UnsupportedOperationException("SVGFilterElement.setFilterRes is not implemented");
   }

   public SVGAnimatedString getHref() {
      return this.href;
   }

   public SVGAnimatedBoolean getExternalResourcesRequired() {
      return this.externalResourcesRequired;
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
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

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMFilterElement();
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGStylableElement.xmlTraitInformation);
      var0.put((Object)null, "filterUnits", new TraitInformation(true, 15));
      var0.put((Object)null, "primitiveUnits", new TraitInformation(true, 15));
      var0.put((Object)null, "x", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "y", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "width", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "height", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "filterRes", new TraitInformation(true, 4));
      xmlTraitInformation = var0;
      attributeInitializer = new AttributeInitializer(4);
      attributeInitializer.addAttribute("http://www.w3.org/2000/xmlns/", (String)null, "xmlns:xlink", "http://www.w3.org/1999/xlink");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "type", "simple");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "show", "other");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "actuate", "onLoad");
      UNITS_VALUES = new String[]{"", "userSpaceOnUse", "objectBoundingBox"};
   }
}
