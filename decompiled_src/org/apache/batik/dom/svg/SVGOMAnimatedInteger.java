package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatableIntegerValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedInteger;

public class SVGOMAnimatedInteger extends AbstractSVGAnimatedValue implements SVGAnimatedInteger {
   protected int defaultValue;
   protected boolean valid;
   protected int baseVal;
   protected int animVal;
   protected boolean changing;

   public SVGOMAnimatedInteger(AbstractElement var1, String var2, String var3, int var4) {
      super(var1, var2, var3);
      this.defaultValue = var4;
   }

   public int getBaseVal() {
      if (!this.valid) {
         this.update();
      }

      return this.baseVal;
   }

   protected void update() {
      Attr var1 = this.element.getAttributeNodeNS(this.namespaceURI, this.localName);
      if (var1 == null) {
         this.baseVal = this.defaultValue;
      } else {
         this.baseVal = Integer.parseInt(var1.getValue());
      }

      this.valid = true;
   }

   public void setBaseVal(int var1) throws DOMException {
      try {
         this.baseVal = var1;
         this.valid = true;
         this.changing = true;
         this.element.setAttributeNS(this.namespaceURI, this.localName, String.valueOf(var1));
      } finally {
         this.changing = false;
      }

   }

   public int getAnimVal() {
      if (this.hasAnimVal) {
         return this.animVal;
      } else {
         if (!this.valid) {
            this.update();
         }

         return this.baseVal;
      }
   }

   public AnimatableValue getUnderlyingValue(AnimationTarget var1) {
      return new AnimatableIntegerValue(var1, this.getBaseVal());
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         this.animVal = ((AnimatableIntegerValue)var1).getValue();
      }

      this.fireAnimatedAttributeListeners();
   }

   public void attrAdded(Attr var1, String var2) {
      if (!this.changing) {
         this.valid = false;
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public void attrModified(Attr var1, String var2, String var3) {
      if (!this.changing) {
         this.valid = false;
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public void attrRemoved(Attr var1, String var2) {
      if (!this.changing) {
         this.valid = false;
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }
}
