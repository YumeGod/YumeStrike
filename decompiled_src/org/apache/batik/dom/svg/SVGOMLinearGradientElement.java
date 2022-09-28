package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGLinearGradientElement;

public class SVGOMLinearGradientElement extends SVGOMGradientElement implements SVGLinearGradientElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedLength x1;
   protected SVGOMAnimatedLength y1;
   protected SVGOMAnimatedLength x2;
   protected SVGOMAnimatedLength y2;

   protected SVGOMLinearGradientElement() {
   }

   public SVGOMLinearGradientElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.x1 = this.createLiveAnimatedLength((String)null, "x1", "0%", (short)2, false);
      this.y1 = this.createLiveAnimatedLength((String)null, "y1", "0%", (short)1, false);
      this.x2 = this.createLiveAnimatedLength((String)null, "x2", "100%", (short)2, false);
      this.y2 = this.createLiveAnimatedLength((String)null, "y2", "0%", (short)1, false);
   }

   public String getLocalName() {
      return "linearGradient";
   }

   public SVGAnimatedLength getX1() {
      return this.x1;
   }

   public SVGAnimatedLength getY1() {
      return this.y1;
   }

   public SVGAnimatedLength getX2() {
      return this.x2;
   }

   public SVGAnimatedLength getY2() {
      return this.y2;
   }

   protected Node newNode() {
      return new SVGOMLinearGradientElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMGradientElement.xmlTraitInformation);
      var0.put((Object)null, "x", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "y", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "width", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "height", new TraitInformation(true, 3, (short)2));
      xmlTraitInformation = var0;
   }
}
