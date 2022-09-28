package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGAnimatedNumberList;
import org.w3c.dom.svg.SVGComponentTransferFunctionElement;

public abstract class SVGOMComponentTransferFunctionElement extends SVGOMElement implements SVGComponentTransferFunctionElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final String[] TYPE_VALUES;
   protected SVGOMAnimatedEnumeration type;
   protected SVGOMAnimatedNumberList tableValues;
   protected SVGOMAnimatedNumber slope;
   protected SVGOMAnimatedNumber intercept;
   protected SVGOMAnimatedNumber amplitude;
   protected SVGOMAnimatedNumber exponent;
   protected SVGOMAnimatedNumber offset;

   protected SVGOMComponentTransferFunctionElement() {
   }

   protected SVGOMComponentTransferFunctionElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.type = this.createLiveAnimatedEnumeration((String)null, "type", TYPE_VALUES, (short)1);
      this.tableValues = this.createLiveAnimatedNumberList((String)null, "tableValues", "", false);
      this.slope = this.createLiveAnimatedNumber((String)null, "slope", 1.0F);
      this.intercept = this.createLiveAnimatedNumber((String)null, "intercept", 0.0F);
      this.amplitude = this.createLiveAnimatedNumber((String)null, "amplitude", 1.0F);
      this.exponent = this.createLiveAnimatedNumber((String)null, "exponent", 1.0F);
      this.offset = this.createLiveAnimatedNumber((String)null, "exponent", 0.0F);
   }

   public SVGAnimatedEnumeration getType() {
      return this.type;
   }

   public SVGAnimatedNumberList getTableValues() {
      throw new UnsupportedOperationException("SVGComponentTransferFunctionElement.getTableValues is not implemented");
   }

   public SVGAnimatedNumber getSlope() {
      return this.slope;
   }

   public SVGAnimatedNumber getIntercept() {
      return this.intercept;
   }

   public SVGAnimatedNumber getAmplitude() {
      return this.amplitude;
   }

   public SVGAnimatedNumber getExponent() {
      return this.exponent;
   }

   public SVGAnimatedNumber getOffset() {
      return this.offset;
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMElement.xmlTraitInformation);
      var0.put((Object)null, "type", new TraitInformation(true, 15));
      var0.put((Object)null, "tableValues", new TraitInformation(true, 13));
      var0.put((Object)null, "slope", new TraitInformation(true, 2));
      var0.put((Object)null, "intercept", new TraitInformation(true, 2));
      var0.put((Object)null, "amplitude", new TraitInformation(true, 2));
      var0.put((Object)null, "exponent", new TraitInformation(true, 2));
      var0.put((Object)null, "offset", new TraitInformation(true, 2));
      xmlTraitInformation = var0;
      TYPE_VALUES = new String[]{"", "identity", "table", "discrete", "linear", "gamma"};
   }
}
