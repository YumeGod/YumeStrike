package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.svg.SVGAnimatedLengthList;
import org.w3c.dom.svg.SVGAnimatedNumberList;
import org.w3c.dom.svg.SVGTextPositioningElement;

public abstract class SVGOMTextPositioningElement extends SVGOMTextContentElement implements SVGTextPositioningElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedLengthList x;
   protected SVGOMAnimatedLengthList y;
   protected SVGOMAnimatedLengthList dx;
   protected SVGOMAnimatedLengthList dy;
   protected SVGOMAnimatedNumberList rotate;

   protected SVGOMTextPositioningElement() {
   }

   protected SVGOMTextPositioningElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.x = this.createLiveAnimatedLengthList((String)null, "x", this.getDefaultXValue(), true, (short)2);
      this.y = this.createLiveAnimatedLengthList((String)null, "y", this.getDefaultYValue(), true, (short)1);
      this.dx = this.createLiveAnimatedLengthList((String)null, "dx", "", true, (short)2);
      this.dy = this.createLiveAnimatedLengthList((String)null, "dy", "", true, (short)1);
      this.rotate = this.createLiveAnimatedNumberList((String)null, "rotate", "", true);
   }

   public SVGAnimatedLengthList getX() {
      return this.x;
   }

   public SVGAnimatedLengthList getY() {
      return this.y;
   }

   public SVGAnimatedLengthList getDx() {
      return this.dx;
   }

   public SVGAnimatedLengthList getDy() {
      return this.dy;
   }

   public SVGAnimatedNumberList getRotate() {
      return this.rotate;
   }

   protected String getDefaultXValue() {
      return "";
   }

   protected String getDefaultYValue() {
      return "";
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMTextContentElement.xmlTraitInformation);
      var0.put((Object)null, "x", new TraitInformation(true, 14, (short)1));
      var0.put((Object)null, "y", new TraitInformation(true, 14, (short)2));
      var0.put((Object)null, "dx", new TraitInformation(true, 14, (short)1));
      var0.put((Object)null, "dy", new TraitInformation(true, 14, (short)2));
      var0.put((Object)null, "rotate", new TraitInformation(true, 13));
      xmlTraitInformation = var0;
   }
}
