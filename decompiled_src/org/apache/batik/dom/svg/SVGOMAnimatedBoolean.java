package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatableBooleanValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedBoolean;

public class SVGOMAnimatedBoolean extends AbstractSVGAnimatedValue implements SVGAnimatedBoolean {
   protected boolean defaultValue;
   protected boolean valid;
   protected boolean baseVal;
   protected boolean animVal;
   protected boolean changing;

   public SVGOMAnimatedBoolean(AbstractElement var1, String var2, String var3, boolean var4) {
      super(var1, var2, var3);
      this.defaultValue = var4;
   }

   public boolean getBaseVal() {
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
         this.baseVal = var1.getValue().equals("true");
      }

      this.valid = true;
   }

   public void setBaseVal(boolean var1) throws DOMException {
      try {
         this.baseVal = var1;
         this.valid = true;
         this.changing = true;
         this.element.setAttributeNS(this.namespaceURI, this.localName, String.valueOf(var1));
      } finally {
         this.changing = false;
      }

   }

   public boolean getAnimVal() {
      if (this.hasAnimVal) {
         return this.animVal;
      } else {
         if (!this.valid) {
            this.update();
         }

         return this.baseVal;
      }
   }

   public void setAnimatedValue(boolean var1) {
      this.hasAnimVal = true;
      this.animVal = var1;
      this.fireAnimatedAttributeListeners();
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         this.animVal = ((AnimatableBooleanValue)var1).getValue();
      }

      this.fireAnimatedAttributeListeners();
   }

   public AnimatableValue getUnderlyingValue(AnimationTarget var1) {
      return new AnimatableBooleanValue(var1, this.getBaseVal());
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
