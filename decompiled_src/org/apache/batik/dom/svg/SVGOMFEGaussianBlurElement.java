package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFEGaussianBlurElement;

public class SVGOMFEGaussianBlurElement extends SVGOMFilterPrimitiveStandardAttributes implements SVGFEGaussianBlurElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedString in;

   protected SVGOMFEGaussianBlurElement() {
   }

   public SVGOMFEGaussianBlurElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.in = this.createLiveAnimatedString((String)null, "in");
   }

   public String getLocalName() {
      return "feGaussianBlur";
   }

   public SVGAnimatedString getIn1() {
      return this.in;
   }

   public SVGAnimatedNumber getStdDeviationX() {
      throw new UnsupportedOperationException("SVGFEGaussianBlurElement.getStdDeviationX is not implemented");
   }

   public SVGAnimatedNumber getStdDeviationY() {
      throw new UnsupportedOperationException("SVGFEGaussianBlurElement.getStdDeviationY is not implemented");
   }

   public void setStdDeviation(float var1, float var2) {
      this.setAttributeNS((String)null, "stdDeviation", Float.toString(var1) + " " + Float.toString(var2));
   }

   protected Node newNode() {
      return new SVGOMFEGaussianBlurElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMFilterPrimitiveStandardAttributes.xmlTraitInformation);
      var0.put((Object)null, "in", new TraitInformation(true, 16));
      var0.put((Object)null, "stdDeviation", new TraitInformation(true, 4));
      xmlTraitInformation = var0;
   }
}
