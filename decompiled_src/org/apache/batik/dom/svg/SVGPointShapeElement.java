package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.svg.SVGAnimatedPoints;
import org.w3c.dom.svg.SVGPointList;

public abstract class SVGPointShapeElement extends SVGGraphicsElement implements SVGAnimatedPoints {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedPoints points;

   protected SVGPointShapeElement() {
   }

   public SVGPointShapeElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.points = this.createLiveAnimatedPoints((String)null, "points", "");
   }

   public SVGOMAnimatedPoints getSVGOMAnimatedPoints() {
      return this.points;
   }

   public SVGPointList getPoints() {
      return this.points.getPoints();
   }

   public SVGPointList getAnimatedPoints() {
      return this.points.getAnimatedPoints();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGGraphicsElement.xmlTraitInformation);
      var0.put((Object)null, "points", new TraitInformation(true, 31));
      xmlTraitInformation = var0;
   }
}
