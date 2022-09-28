package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFESpecularLightingElement;

public class SVGOMFESpecularLightingElement extends SVGOMFilterPrimitiveStandardAttributes implements SVGFESpecularLightingElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedString in;
   protected SVGOMAnimatedNumber surfaceScale;
   protected SVGOMAnimatedNumber specularConstant;
   protected SVGOMAnimatedNumber specularExponent;

   protected SVGOMFESpecularLightingElement() {
   }

   public SVGOMFESpecularLightingElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.in = this.createLiveAnimatedString((String)null, "in");
      this.surfaceScale = this.createLiveAnimatedNumber((String)null, "surfaceScale", 1.0F);
      this.specularConstant = this.createLiveAnimatedNumber((String)null, "specularConstant", 1.0F);
      this.specularExponent = this.createLiveAnimatedNumber((String)null, "specularExponent", 1.0F);
   }

   public String getLocalName() {
      return "feSpecularLighting";
   }

   public SVGAnimatedString getIn1() {
      return this.in;
   }

   public SVGAnimatedNumber getSurfaceScale() {
      return this.surfaceScale;
   }

   public SVGAnimatedNumber getSpecularConstant() {
      return this.specularConstant;
   }

   public SVGAnimatedNumber getSpecularExponent() {
      return this.specularExponent;
   }

   protected Node newNode() {
      return new SVGOMFESpecularLightingElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMFilterPrimitiveStandardAttributes.xmlTraitInformation);
      var0.put((Object)null, "in", new TraitInformation(true, 16));
      var0.put((Object)null, "surfaceScale", new TraitInformation(true, 2));
      var0.put((Object)null, "specularConstant", new TraitInformation(true, 2));
      var0.put((Object)null, "specularExponent", new TraitInformation(true, 2));
      xmlTraitInformation = var0;
   }
}
