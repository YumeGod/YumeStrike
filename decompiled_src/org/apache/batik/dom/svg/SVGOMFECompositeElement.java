package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFECompositeElement;

public class SVGOMFECompositeElement extends SVGOMFilterPrimitiveStandardAttributes implements SVGFECompositeElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final String[] OPERATOR_VALUES;
   protected SVGOMAnimatedString in;
   protected SVGOMAnimatedString in2;
   protected SVGOMAnimatedEnumeration operator;
   protected SVGOMAnimatedNumber k1;
   protected SVGOMAnimatedNumber k2;
   protected SVGOMAnimatedNumber k3;
   protected SVGOMAnimatedNumber k4;

   protected SVGOMFECompositeElement() {
   }

   public SVGOMFECompositeElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.in = this.createLiveAnimatedString((String)null, "in");
      this.in2 = this.createLiveAnimatedString((String)null, "in2");
      this.operator = this.createLiveAnimatedEnumeration((String)null, "operator", OPERATOR_VALUES, (short)1);
      this.k1 = this.createLiveAnimatedNumber((String)null, "k1", 0.0F);
      this.k2 = this.createLiveAnimatedNumber((String)null, "k2", 0.0F);
      this.k3 = this.createLiveAnimatedNumber((String)null, "k3", 0.0F);
      this.k4 = this.createLiveAnimatedNumber((String)null, "k4", 0.0F);
   }

   public String getLocalName() {
      return "feComposite";
   }

   public SVGAnimatedString getIn1() {
      return this.in;
   }

   public SVGAnimatedString getIn2() {
      return this.in2;
   }

   public SVGAnimatedEnumeration getOperator() {
      return this.operator;
   }

   public SVGAnimatedNumber getK1() {
      return this.k1;
   }

   public SVGAnimatedNumber getK2() {
      return this.k2;
   }

   public SVGAnimatedNumber getK3() {
      return this.k3;
   }

   public SVGAnimatedNumber getK4() {
      return this.k4;
   }

   protected Node newNode() {
      return new SVGOMFECompositeElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMFilterPrimitiveStandardAttributes.xmlTraitInformation);
      var0.put((Object)null, "in", new TraitInformation(true, 16));
      var0.put((Object)null, "in2", new TraitInformation(true, 16));
      var0.put((Object)null, "operator", new TraitInformation(true, 15));
      var0.put((Object)null, "k1", new TraitInformation(true, 2));
      var0.put((Object)null, "k2", new TraitInformation(true, 2));
      var0.put((Object)null, "k3", new TraitInformation(true, 2));
      var0.put((Object)null, "k4", new TraitInformation(true, 2));
      xmlTraitInformation = var0;
      OPERATOR_VALUES = new String[]{"", "over", "in", "out", "atop", "xor", "arithmetic"};
   }
}
