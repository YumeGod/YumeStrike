package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGStopElement;

public class SVGOMStopElement extends SVGStylableElement implements SVGStopElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedNumber offset;

   protected SVGOMStopElement() {
   }

   public SVGOMStopElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.offset = this.createLiveAnimatedNumber((String)null, "offset", 0.0F, true);
   }

   public String getLocalName() {
      return "stop";
   }

   public SVGAnimatedNumber getOffset() {
      return this.offset;
   }

   protected Node newNode() {
      return new SVGOMStopElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGStylableElement.xmlTraitInformation);
      var0.put((Object)null, "offset", new TraitInformation(true, 47));
      xmlTraitInformation = var0;
   }
}
