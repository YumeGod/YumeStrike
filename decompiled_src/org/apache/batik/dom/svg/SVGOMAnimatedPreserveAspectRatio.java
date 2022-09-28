package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatablePreserveAspectRatioValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGPreserveAspectRatio;

public class SVGOMAnimatedPreserveAspectRatio extends AbstractSVGAnimatedValue implements SVGAnimatedPreserveAspectRatio {
   protected BaseSVGPARValue baseVal;
   protected AnimSVGPARValue animVal;
   protected boolean changing;

   public SVGOMAnimatedPreserveAspectRatio(AbstractElement var1) {
      super(var1, (String)null, "preserveAspectRatio");
   }

   public SVGPreserveAspectRatio getBaseVal() {
      if (this.baseVal == null) {
         this.baseVal = new BaseSVGPARValue();
      }

      return this.baseVal;
   }

   public SVGPreserveAspectRatio getAnimVal() {
      if (this.animVal == null) {
         this.animVal = new AnimSVGPARValue();
      }

      return this.animVal;
   }

   public void check() {
      if (!this.hasAnimVal) {
         if (this.baseVal == null) {
            this.baseVal = new BaseSVGPARValue();
         }

         if (this.baseVal.malformed) {
            throw new LiveAttributeException(this.element, this.localName, (short)1, this.baseVal.getValueAsString());
         }
      }

   }

   public AnimatableValue getUnderlyingValue(AnimationTarget var1) {
      SVGPreserveAspectRatio var2 = this.getBaseVal();
      return new AnimatablePreserveAspectRatioValue(var1, var2.getAlign(), var2.getMeetOrSlice());
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         if (this.animVal == null) {
            this.animVal = new AnimSVGPARValue();
         }

         AnimatablePreserveAspectRatioValue var2 = (AnimatablePreserveAspectRatioValue)var1;
         this.animVal.setAnimatedValue(var2.getAlign(), var2.getMeetOrSlice());
      }

      this.fireAnimatedAttributeListeners();
   }

   public void attrAdded(Attr var1, String var2) {
      if (!this.changing && this.baseVal != null) {
         this.baseVal.invalidate();
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public void attrModified(Attr var1, String var2, String var3) {
      if (!this.changing && this.baseVal != null) {
         this.baseVal.invalidate();
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public void attrRemoved(Attr var1, String var2) {
      if (!this.changing && this.baseVal != null) {
         this.baseVal.invalidate();
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public class AnimSVGPARValue extends AbstractSVGPreserveAspectRatio {
      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedPreserveAspectRatio.this.element.createDOMException(var1, var2, var3);
      }

      protected void setAttributeValue(String var1) throws DOMException {
      }

      public short getAlign() {
         return SVGOMAnimatedPreserveAspectRatio.this.hasAnimVal ? super.getAlign() : SVGOMAnimatedPreserveAspectRatio.this.getBaseVal().getAlign();
      }

      public short getMeetOrSlice() {
         return SVGOMAnimatedPreserveAspectRatio.this.hasAnimVal ? super.getMeetOrSlice() : SVGOMAnimatedPreserveAspectRatio.this.getBaseVal().getMeetOrSlice();
      }

      public void setAlign(short var1) {
         throw SVGOMAnimatedPreserveAspectRatio.this.element.createDOMException((short)7, "readonly.preserve.aspect.ratio", (Object[])null);
      }

      public void setMeetOrSlice(short var1) {
         throw SVGOMAnimatedPreserveAspectRatio.this.element.createDOMException((short)7, "readonly.preserve.aspect.ratio", (Object[])null);
      }

      protected void setAnimatedValue(short var1, short var2) {
         this.align = var1;
         this.meetOrSlice = var2;
      }
   }

   public class BaseSVGPARValue extends AbstractSVGPreserveAspectRatio {
      protected boolean malformed;

      public BaseSVGPARValue() {
         this.invalidate();
      }

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedPreserveAspectRatio.this.element.createDOMException(var1, var2, var3);
      }

      protected void setAttributeValue(String var1) throws DOMException {
         try {
            SVGOMAnimatedPreserveAspectRatio.this.changing = true;
            SVGOMAnimatedPreserveAspectRatio.this.element.setAttributeNS((String)null, "preserveAspectRatio", var1);
            this.malformed = false;
         } finally {
            SVGOMAnimatedPreserveAspectRatio.this.changing = false;
         }

      }

      protected void invalidate() {
         String var1 = SVGOMAnimatedPreserveAspectRatio.this.element.getAttributeNS((String)null, "preserveAspectRatio");
         this.setValueAsString(var1);
      }
   }
}
