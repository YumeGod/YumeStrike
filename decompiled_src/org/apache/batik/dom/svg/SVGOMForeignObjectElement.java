package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGForeignObjectElement;

public class SVGOMForeignObjectElement extends SVGGraphicsElement implements SVGForeignObjectElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedLength x;
   protected SVGOMAnimatedLength y;
   protected SVGOMAnimatedLength width;
   protected SVGOMAnimatedLength height;
   protected SVGOMAnimatedPreserveAspectRatio preserveAspectRatio;

   protected SVGOMForeignObjectElement() {
   }

   public SVGOMForeignObjectElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.x = this.createLiveAnimatedLength((String)null, "x", "0", (short)2, false);
      this.y = this.createLiveAnimatedLength((String)null, "y", "0", (short)1, false);
      this.width = this.createLiveAnimatedLength((String)null, "width", (String)null, (short)2, true);
      this.height = this.createLiveAnimatedLength((String)null, "height", (String)null, (short)1, true);
      this.preserveAspectRatio = this.createLiveAnimatedPreserveAspectRatio();
   }

   public String getLocalName() {
      return "foreignObject";
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

   protected Node newNode() {
      return new SVGOMForeignObjectElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGGraphicsElement.xmlTraitInformation);
      var0.put((Object)null, "x", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "y", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "width", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "height", new TraitInformation(true, 3, (short)2));
      xmlTraitInformation = var0;
   }
}
