package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;

public class SVGOMToBeImplementedElement extends SVGGraphicsElement {
   protected String localName;

   protected SVGOMToBeImplementedElement() {
   }

   public SVGOMToBeImplementedElement(String var1, AbstractDocument var2, String var3) {
      super(var1, var2);
      this.localName = var3;
   }

   public String getLocalName() {
      return this.localName;
   }

   protected Node newNode() {
      return new SVGOMToBeImplementedElement();
   }

   protected Node export(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      SVGOMToBeImplementedElement var3 = (SVGOMToBeImplementedElement)var1;
      var3.localName = this.localName;
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);
      SVGOMToBeImplementedElement var3 = (SVGOMToBeImplementedElement)var1;
      var3.localName = this.localName;
      return var1;
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      SVGOMToBeImplementedElement var2 = (SVGOMToBeImplementedElement)var1;
      var2.localName = this.localName;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      SVGOMToBeImplementedElement var2 = (SVGOMToBeImplementedElement)var1;
      var2.localName = this.localName;
      return var1;
   }
}
