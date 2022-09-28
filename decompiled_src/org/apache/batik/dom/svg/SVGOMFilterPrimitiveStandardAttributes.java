package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes;

public abstract class SVGOMFilterPrimitiveStandardAttributes extends SVGStylableElement implements SVGFilterPrimitiveStandardAttributes {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedLength x;
   protected SVGOMAnimatedLength y;
   protected SVGOMAnimatedLength width;
   protected SVGOMAnimatedLength height;
   protected SVGOMAnimatedString result;

   protected SVGOMFilterPrimitiveStandardAttributes() {
   }

   protected SVGOMFilterPrimitiveStandardAttributes(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.x = this.createLiveAnimatedLength((String)null, "x", "0%", (short)2, false);
      this.y = this.createLiveAnimatedLength((String)null, "y", "0%", (short)1, false);
      this.width = this.createLiveAnimatedLength((String)null, "width", "100%", (short)2, true);
      this.height = this.createLiveAnimatedLength((String)null, "height", "100%", (short)1, true);
      this.result = this.createLiveAnimatedString((String)null, "result");
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

   public SVGAnimatedString getResult() {
      return this.result;
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGStylableElement.xmlTraitInformation);
      var0.put((Object)null, "x", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "y", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "width", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "height", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "result", new TraitInformation(true, 16));
      xmlTraitInformation = var0;
   }
}
