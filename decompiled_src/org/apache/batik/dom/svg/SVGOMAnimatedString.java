package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatableStringValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedString;

public class SVGOMAnimatedString extends AbstractSVGAnimatedValue implements SVGAnimatedString {
   protected String animVal;

   public SVGOMAnimatedString(AbstractElement var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   public String getBaseVal() {
      return this.element.getAttributeNS(this.namespaceURI, this.localName);
   }

   public void setBaseVal(String var1) throws DOMException {
      this.element.setAttributeNS(this.namespaceURI, this.localName, var1);
   }

   public String getAnimVal() {
      return this.hasAnimVal ? this.animVal : this.element.getAttributeNS(this.namespaceURI, this.localName);
   }

   public AnimatableValue getUnderlyingValue(AnimationTarget var1) {
      return new AnimatableStringValue(var1, this.getBaseVal());
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         this.animVal = ((AnimatableStringValue)var1).getString();
      }

      this.fireAnimatedAttributeListeners();
   }

   public void attrAdded(Attr var1, String var2) {
      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public void attrModified(Attr var1, String var2, String var3) {
      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public void attrRemoved(Attr var1, String var2) {
      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }
}
