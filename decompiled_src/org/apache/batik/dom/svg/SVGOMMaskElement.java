package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGMaskElement;

public class SVGOMMaskElement extends SVGGraphicsElement implements SVGMaskElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final String[] UNITS_VALUES;
   protected SVGOMAnimatedLength x;
   protected SVGOMAnimatedLength y;
   protected SVGOMAnimatedLength width;
   protected SVGOMAnimatedLength height;
   protected SVGOMAnimatedEnumeration maskUnits;
   protected SVGOMAnimatedEnumeration maskContentUnits;

   protected SVGOMMaskElement() {
   }

   public SVGOMMaskElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.x = this.createLiveAnimatedLength((String)null, "x", "-10%", (short)2, false);
      this.y = this.createLiveAnimatedLength((String)null, "y", "-10%", (short)1, false);
      this.width = this.createLiveAnimatedLength((String)null, "width", "120%", (short)2, true);
      this.height = this.createLiveAnimatedLength((String)null, "height", "120%", (short)1, true);
      this.maskUnits = this.createLiveAnimatedEnumeration((String)null, "maskUnits", UNITS_VALUES, (short)2);
      this.maskContentUnits = this.createLiveAnimatedEnumeration((String)null, "maskContentUnits", UNITS_VALUES, (short)1);
   }

   public String getLocalName() {
      return "mask";
   }

   public SVGAnimatedEnumeration getMaskUnits() {
      return this.maskUnits;
   }

   public SVGAnimatedEnumeration getMaskContentUnits() {
      return this.maskContentUnits;
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
      return new SVGOMMaskElement();
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
      var0.put((Object)null, "maskUnits", new TraitInformation(true, 15));
      var0.put((Object)null, "maskContentUnits", new TraitInformation(true, 15));
      xmlTraitInformation = var0;
      UNITS_VALUES = new String[]{"", "userSpaceOnUse", "objectBoundingBox"};
   }
}
