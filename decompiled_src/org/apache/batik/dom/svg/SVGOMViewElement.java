package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGStringList;
import org.w3c.dom.svg.SVGViewElement;

public class SVGOMViewElement extends SVGOMElement implements SVGViewElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final AttributeInitializer attributeInitializer;
   protected SVGOMAnimatedBoolean externalResourcesRequired;
   protected SVGOMAnimatedPreserveAspectRatio preserveAspectRatio;

   protected SVGOMViewElement() {
   }

   public SVGOMViewElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.externalResourcesRequired = this.createLiveAnimatedBoolean((String)null, "externalResourcesRequired", false);
      this.preserveAspectRatio = this.createLiveAnimatedPreserveAspectRatio();
   }

   public String getLocalName() {
      return "view";
   }

   public SVGStringList getViewTarget() {
      throw new UnsupportedOperationException("SVGViewElement.getViewTarget is not implemented");
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
      return new SVGOMViewElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMElement.xmlTraitInformation);
      var0.put((Object)null, "preserveAspectRatio", new TraitInformation(true, 32));
      var0.put((Object)null, "viewBox", new TraitInformation(true, 13));
      var0.put((Object)null, "externalResourcesRequired", new TraitInformation(true, 49));
      xmlTraitInformation = var0;
      attributeInitializer = new AttributeInitializer(2);
      attributeInitializer.addAttribute((String)null, (String)null, "preserveAspectRatio", "xMidYMid meet");
      attributeInitializer.addAttribute((String)null, (String)null, "zoomAndPan", "magnify");
   }
}
