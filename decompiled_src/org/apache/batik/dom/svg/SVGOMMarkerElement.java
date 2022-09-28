package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAngle;
import org.w3c.dom.svg.SVGAnimatedAngle;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGMarkerElement;

public class SVGOMMarkerElement extends SVGStylableElement implements SVGMarkerElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final AttributeInitializer attributeInitializer;
   protected static final String[] UNITS_VALUES;
   protected static final String[] ORIENT_TYPE_VALUES;
   protected SVGOMAnimatedLength refX;
   protected SVGOMAnimatedLength refY;
   protected SVGOMAnimatedLength markerWidth;
   protected SVGOMAnimatedLength markerHeight;
   protected SVGOMAnimatedMarkerOrientValue orient;
   protected SVGOMAnimatedEnumeration markerUnits;
   protected SVGOMAnimatedPreserveAspectRatio preserveAspectRatio;
   protected SVGOMAnimatedRect viewBox;
   protected SVGOMAnimatedBoolean externalResourcesRequired;

   protected SVGOMMarkerElement() {
   }

   public SVGOMMarkerElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.refX = this.createLiveAnimatedLength((String)null, "refX", "0", (short)2, false);
      this.refY = this.createLiveAnimatedLength((String)null, "refY", "0", (short)1, false);
      this.markerWidth = this.createLiveAnimatedLength((String)null, "markerWidth", "3", (short)2, true);
      this.markerHeight = this.createLiveAnimatedLength((String)null, "markerHeight", "3", (short)1, true);
      this.orient = this.createLiveAnimatedMarkerOrientValue((String)null, "orient");
      this.markerUnits = this.createLiveAnimatedEnumeration((String)null, "markerUnits", UNITS_VALUES, (short)2);
      this.preserveAspectRatio = this.createLiveAnimatedPreserveAspectRatio();
      this.viewBox = this.createLiveAnimatedRect((String)null, "viewBox", (String)null);
      this.externalResourcesRequired = this.createLiveAnimatedBoolean((String)null, "externalResourcesRequired", false);
   }

   public String getLocalName() {
      return "marker";
   }

   public SVGAnimatedLength getRefX() {
      return this.refX;
   }

   public SVGAnimatedLength getRefY() {
      return this.refY;
   }

   public SVGAnimatedEnumeration getMarkerUnits() {
      return this.markerUnits;
   }

   public SVGAnimatedLength getMarkerWidth() {
      return this.markerWidth;
   }

   public SVGAnimatedLength getMarkerHeight() {
      return this.markerHeight;
   }

   public SVGAnimatedEnumeration getOrientType() {
      return this.orient.getAnimatedEnumeration();
   }

   public SVGAnimatedAngle getOrientAngle() {
      return this.orient.getAnimatedAngle();
   }

   public void setOrientToAuto() {
      this.setAttributeNS((String)null, "orient", "auto");
   }

   public void setOrientToAngle(SVGAngle var1) {
      this.setAttributeNS((String)null, "orient", var1.getValueAsString());
   }

   public SVGAnimatedRect getViewBox() {
      return this.viewBox;
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

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMMarkerElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGStylableElement.xmlTraitInformation);
      var0.put((Object)null, "refX", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "refY", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "markerWidth", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "markerHeight", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "markerUnits", new TraitInformation(true, 15));
      var0.put((Object)null, "orient", new TraitInformation(true, 15));
      var0.put((Object)null, "preserveAspectRatio", new TraitInformation(true, 32));
      var0.put((Object)null, "externalResourcesRequired", new TraitInformation(true, 49));
      xmlTraitInformation = var0;
      attributeInitializer = new AttributeInitializer(1);
      attributeInitializer.addAttribute((String)null, (String)null, "preserveAspectRatio", "xMidYMid meet");
      UNITS_VALUES = new String[]{"", "userSpaceOnUse", "stroke-width"};
      ORIENT_TYPE_VALUES = new String[]{"", "auto", ""};
   }
}
