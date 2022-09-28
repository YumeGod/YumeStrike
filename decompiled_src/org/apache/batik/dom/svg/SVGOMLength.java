package org.apache.batik.dom.svg;

public class SVGOMLength extends AbstractSVGLength {
   protected AbstractElement element;

   public SVGOMLength(AbstractElement var1) {
      super((short)0);
      this.element = var1;
   }

   protected SVGOMElement getAssociatedElement() {
      return (SVGOMElement)this.element;
   }
}
