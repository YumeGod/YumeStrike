package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGFESpotLightElement;

public class SVGOMFESpotLightElement extends SVGOMElement implements SVGFESpotLightElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedNumber x;
   protected SVGOMAnimatedNumber y;
   protected SVGOMAnimatedNumber z;
   protected SVGOMAnimatedNumber pointsAtX;
   protected SVGOMAnimatedNumber pointsAtY;
   protected SVGOMAnimatedNumber pointsAtZ;
   protected SVGOMAnimatedNumber specularExponent;
   protected SVGOMAnimatedNumber limitingConeAngle;

   protected SVGOMFESpotLightElement() {
   }

   public SVGOMFESpotLightElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.x = this.createLiveAnimatedNumber((String)null, "x", 0.0F);
      this.y = this.createLiveAnimatedNumber((String)null, "y", 0.0F);
      this.z = this.createLiveAnimatedNumber((String)null, "z", 0.0F);
      this.pointsAtX = this.createLiveAnimatedNumber((String)null, "pointsAtX", 0.0F);
      this.pointsAtY = this.createLiveAnimatedNumber((String)null, "pointsAtY", 0.0F);
      this.pointsAtZ = this.createLiveAnimatedNumber((String)null, "pointsAtZ", 0.0F);
      this.specularExponent = this.createLiveAnimatedNumber((String)null, "specularExponent", 1.0F);
      this.limitingConeAngle = this.createLiveAnimatedNumber((String)null, "limitingConeAngle", 0.0F);
   }

   public String getLocalName() {
      return "feSpotLight";
   }

   public SVGAnimatedNumber getX() {
      return this.x;
   }

   public SVGAnimatedNumber getY() {
      return this.y;
   }

   public SVGAnimatedNumber getZ() {
      return this.z;
   }

   public SVGAnimatedNumber getPointsAtX() {
      return this.pointsAtX;
   }

   public SVGAnimatedNumber getPointsAtY() {
      return this.pointsAtY;
   }

   public SVGAnimatedNumber getPointsAtZ() {
      return this.pointsAtZ;
   }

   public SVGAnimatedNumber getSpecularExponent() {
      return this.specularExponent;
   }

   public SVGAnimatedNumber getLimitingConeAngle() {
      return this.limitingConeAngle;
   }

   protected Node newNode() {
      return new SVGOMFESpotLightElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMElement.xmlTraitInformation);
      var0.put((Object)null, "x", new TraitInformation(true, 2));
      var0.put((Object)null, "y", new TraitInformation(true, 2));
      var0.put((Object)null, "z", new TraitInformation(true, 2));
      var0.put((Object)null, "pointsAtX", new TraitInformation(true, 2));
      var0.put((Object)null, "pointsAtY", new TraitInformation(true, 2));
      var0.put((Object)null, "pointsAtZ", new TraitInformation(true, 2));
      var0.put((Object)null, "specularExponent", new TraitInformation(true, 2));
      var0.put((Object)null, "limitingConeAngle", new TraitInformation(true, 2));
      xmlTraitInformation = var0;
   }
}
