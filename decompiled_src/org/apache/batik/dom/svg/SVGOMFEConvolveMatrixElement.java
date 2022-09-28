package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedInteger;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGAnimatedNumberList;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFEConvolveMatrixElement;

public class SVGOMFEConvolveMatrixElement extends SVGOMFilterPrimitiveStandardAttributes implements SVGFEConvolveMatrixElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final String[] EDGE_MODE_VALUES;
   protected SVGOMAnimatedString in;
   protected SVGOMAnimatedEnumeration edgeMode;
   protected SVGOMAnimatedNumber bias;
   protected SVGOMAnimatedBoolean preserveAlpha;

   protected SVGOMFEConvolveMatrixElement() {
   }

   public SVGOMFEConvolveMatrixElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.in = this.createLiveAnimatedString((String)null, "in");
      this.edgeMode = this.createLiveAnimatedEnumeration((String)null, "edgeMode", EDGE_MODE_VALUES, (short)1);
      this.bias = this.createLiveAnimatedNumber((String)null, "bias", 0.0F);
      this.preserveAlpha = this.createLiveAnimatedBoolean((String)null, "preserveAlpha", false);
   }

   public String getLocalName() {
      return "feConvolveMatrix";
   }

   public SVGAnimatedString getIn1() {
      return this.in;
   }

   public SVGAnimatedEnumeration getEdgeMode() {
      return this.edgeMode;
   }

   public SVGAnimatedNumberList getKernelMatrix() {
      throw new UnsupportedOperationException("SVGFEConvolveMatrixElement.getKernelMatrix is not implemented");
   }

   public SVGAnimatedInteger getOrderX() {
      throw new UnsupportedOperationException("SVGFEConvolveMatrixElement.getOrderX is not implemented");
   }

   public SVGAnimatedInteger getOrderY() {
      throw new UnsupportedOperationException("SVGFEConvolveMatrixElement.getOrderY is not implemented");
   }

   public SVGAnimatedInteger getTargetX() {
      throw new UnsupportedOperationException("SVGFEConvolveMatrixElement.getTargetX is not implemented");
   }

   public SVGAnimatedInteger getTargetY() {
      throw new UnsupportedOperationException("SVGFEConvolveMatrixElement.getTargetY is not implemented");
   }

   public SVGAnimatedNumber getDivisor() {
      throw new UnsupportedOperationException("SVGFEConvolveMatrixElement.getDivisor is not implemented");
   }

   public SVGAnimatedNumber getBias() {
      return this.bias;
   }

   public SVGAnimatedNumber getKernelUnitLengthX() {
      throw new UnsupportedOperationException("SVGFEConvolveMatrixElement.getKernelUnitLengthX is not implemented");
   }

   public SVGAnimatedNumber getKernelUnitLengthY() {
      throw new UnsupportedOperationException("SVGFEConvolveMatrixElement.getKernelUnitLengthY is not implemented");
   }

   public SVGAnimatedBoolean getPreserveAlpha() {
      return this.preserveAlpha;
   }

   protected Node newNode() {
      return new SVGOMFEConvolveMatrixElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMFilterPrimitiveStandardAttributes.xmlTraitInformation);
      var0.put((Object)null, "in", new TraitInformation(true, 16));
      var0.put((Object)null, "order", new TraitInformation(true, 4));
      var0.put((Object)null, "kernelUnitLength", new TraitInformation(true, 4));
      var0.put((Object)null, "kernelMatrix", new TraitInformation(true, 13));
      var0.put((Object)null, "divisor", new TraitInformation(true, 2));
      var0.put((Object)null, "bias", new TraitInformation(true, 2));
      var0.put((Object)null, "targetX", new TraitInformation(true, 1));
      var0.put((Object)null, "targetY", new TraitInformation(true, 1));
      var0.put((Object)null, "edgeMode", new TraitInformation(true, 15));
      var0.put((Object)null, "preserveAlpha", new TraitInformation(true, 49));
      xmlTraitInformation = var0;
      EDGE_MODE_VALUES = new String[]{"", "duplicate", "wrap", "none"};
   }
}
