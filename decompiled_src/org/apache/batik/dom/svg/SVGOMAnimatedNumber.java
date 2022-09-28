package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatableNumberValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedNumber;

public class SVGOMAnimatedNumber extends AbstractSVGAnimatedValue implements SVGAnimatedNumber {
   protected float defaultValue;
   protected boolean allowPercentage;
   protected boolean valid;
   protected float baseVal;
   protected float animVal;
   protected boolean changing;

   public SVGOMAnimatedNumber(AbstractElement var1, String var2, String var3, float var4) {
      this(var1, var2, var3, var4, false);
   }

   public SVGOMAnimatedNumber(AbstractElement var1, String var2, String var3, float var4, boolean var5) {
      super(var1, var2, var3);
      this.defaultValue = var4;
      this.allowPercentage = var5;
   }

   public float getBaseVal() {
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
         String var2 = var1.getValue();
         int var3 = var2.length();
         if (this.allowPercentage && var3 > 1 && var2.charAt(var3 - 1) == '%') {
            this.baseVal = 0.01F * Float.parseFloat(var2.substring(0, var3 - 1));
         } else {
            this.baseVal = Float.parseFloat(var2);
         }
      }

      this.valid = true;
   }

   public void setBaseVal(float var1) throws DOMException {
      try {
         this.baseVal = var1;
         this.valid = true;
         this.changing = true;
         this.element.setAttributeNS(this.namespaceURI, this.localName, String.valueOf(var1));
      } finally {
         this.changing = false;
      }

   }

   public float getAnimVal() {
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
      return new AnimatableNumberValue(var1, this.getBaseVal());
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         this.animVal = ((AnimatableNumberValue)var1).getValue();
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
