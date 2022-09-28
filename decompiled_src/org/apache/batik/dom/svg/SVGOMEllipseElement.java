package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGEllipseElement;

public class SVGOMEllipseElement extends SVGGraphicsElement implements SVGEllipseElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedLength cx;
   protected SVGOMAnimatedLength cy;
   protected SVGOMAnimatedLength rx;
   protected SVGOMAnimatedLength ry;

   protected SVGOMEllipseElement() {
   }

   public SVGOMEllipseElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.cx = this.createLiveAnimatedLength((String)null, "cx", "0", (short)2, false);
      this.cy = this.createLiveAnimatedLength((String)null, "cy", "0", (short)1, false);
      this.rx = this.createLiveAnimatedLength((String)null, "rx", (String)null, (short)2, true);
      this.ry = this.createLiveAnimatedLength((String)null, "ry", (String)null, (short)1, true);
   }

   public String getLocalName() {
      return "ellipse";
   }

   public SVGAnimatedLength getCx() {
      return this.cx;
   }

   public SVGAnimatedLength getCy() {
      return this.cy;
   }

   public SVGAnimatedLength getRx() {
      return this.rx;
   }

   public SVGAnimatedLength getRy() {
      return this.ry;
   }

   protected Node newNode() {
      return new SVGOMEllipseElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGGraphicsElement.xmlTraitInformation);
      var0.put((Object)null, "cx", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "cy", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "rx", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "ry", new TraitInformation(true, 3, (short)2));
      xmlTraitInformation = var0;
   }
}
