package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGURIReference;

public abstract class SVGURIReferenceTextPositioningElement extends SVGOMTextPositioningElement implements SVGURIReference {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedString href;

   protected SVGURIReferenceTextPositioningElement() {
   }

   protected SVGURIReferenceTextPositioningElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.href = this.createLiveAnimatedString("http://www.w3.org/1999/xlink", "href");
   }

   public SVGAnimatedString getHref() {
      return this.href;
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMTextPositioningElement.xmlTraitInformation);
      var0.put("http://www.w3.org/1999/xlink", "href", new TraitInformation(true, 10));
      xmlTraitInformation = var0;
   }
}
