package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFEImageElement;

public class SVGOMFEImageElement extends SVGOMFilterPrimitiveStandardAttributes implements SVGFEImageElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final AttributeInitializer attributeInitializer;
   protected SVGOMAnimatedString href;
   protected SVGOMAnimatedPreserveAspectRatio preserveAspectRatio;
   protected SVGOMAnimatedBoolean externalResourcesRequired;

   protected SVGOMFEImageElement() {
   }

   public SVGOMFEImageElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.href = this.createLiveAnimatedString("http://www.w3.org/1999/xlink", "href");
      this.preserveAspectRatio = this.createLiveAnimatedPreserveAspectRatio();
      this.externalResourcesRequired = this.createLiveAnimatedBoolean((String)null, "externalResourcesRequired", false);
   }

   public String getLocalName() {
      return "feImage";
   }

   public SVGAnimatedString getHref() {
      return this.href;
   }

   public SVGAnimatedPreserveAspectRatio getPreserveAspectRatio() {
      return this.preserveAspectRatio;
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

   public SVGAnimatedBoolean getExternalResourcesRequired() {
      return this.externalResourcesRequired;
   }

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMFEImageElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMFilterPrimitiveStandardAttributes.xmlTraitInformation);
      var0.put((Object)null, "externalResourcesRequired", new TraitInformation(true, 49));
      var0.put((Object)null, "preserveAspectRatio", new TraitInformation(true, 32));
      var0.put("http://www.w3.org/1999/xlink", "href", new TraitInformation(true, 10));
      xmlTraitInformation = var0;
      attributeInitializer = new AttributeInitializer(4);
      attributeInitializer.addAttribute("http://www.w3.org/2000/xmlns/", (String)null, "xmlns:xlink", "http://www.w3.org/1999/xlink");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "type", "simple");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "show", "embed");
      attributeInitializer.addAttribute("http://www.w3.org/1999/xlink", "xlink", "actuate", "onLoad");
   }
}
