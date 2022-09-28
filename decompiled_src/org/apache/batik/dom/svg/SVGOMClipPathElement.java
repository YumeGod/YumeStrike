package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGClipPathElement;

public class SVGOMClipPathElement extends SVGGraphicsElement implements SVGClipPathElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final String[] CLIP_PATH_UNITS_VALUES;
   protected SVGOMAnimatedEnumeration clipPathUnits;

   protected SVGOMClipPathElement() {
   }

   public SVGOMClipPathElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.clipPathUnits = this.createLiveAnimatedEnumeration((String)null, "clipPathUnits", CLIP_PATH_UNITS_VALUES, (short)1);
   }

   public String getLocalName() {
      return "clipPath";
   }

   public SVGAnimatedEnumeration getClipPathUnits() {
      return this.clipPathUnits;
   }

   protected Node newNode() {
      return new SVGOMClipPathElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGGraphicsElement.xmlTraitInformation);
      var0.put((Object)null, "clipPathUnits", new TraitInformation(true, 15));
      xmlTraitInformation = var0;
      CLIP_PATH_UNITS_VALUES = new String[]{"", "userSpaceOnUse", "objectBoundingBox"};
   }
}
