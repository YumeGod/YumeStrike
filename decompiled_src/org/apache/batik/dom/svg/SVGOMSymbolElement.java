package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGSymbolElement;

public class SVGOMSymbolElement extends SVGStylableElement implements SVGSymbolElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedPreserveAspectRatio preserveAspectRatio;
   protected static final AttributeInitializer attributeInitializer;
   protected SVGOMAnimatedBoolean externalResourcesRequired;

   protected SVGOMSymbolElement() {
   }

   public SVGOMSymbolElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.preserveAspectRatio = this.createLiveAnimatedPreserveAspectRatio();
   }

   public String getLocalName() {
      return "symbol";
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

   public short getZoomAndPan() {
      return SVGZoomAndPanSupport.getZoomAndPan(this);
   }

   public void setZoomAndPan(short var1) {
      SVGZoomAndPanSupport.setZoomAndPan(this, var1);
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

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMSymbolElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGStylableElement.xmlTraitInformation);
      var0.put((Object)null, "externalResourcesRequired", new TraitInformation(true, 49));
      var0.put((Object)null, "preserveAspectRatio", new TraitInformation(true, 32));
      var0.put((Object)null, "viewBox", new TraitInformation(true, 13));
      xmlTraitInformation = var0;
      attributeInitializer = new AttributeInitializer(1);
      attributeInitializer.addAttribute((String)null, (String)null, "preserveAspectRatio", "xMidYMid meet");
   }
}
