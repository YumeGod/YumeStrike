package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatableLengthValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGLength;

public abstract class AbstractSVGAnimatedLength extends AbstractSVGAnimatedValue implements SVGAnimatedLength, LiveAttributeValue {
   public static final short HORIZONTAL_LENGTH = 2;
   public static final short VERTICAL_LENGTH = 1;
   public static final short OTHER_LENGTH = 0;
   protected short direction;
   protected BaseSVGLength baseVal;
   protected AnimSVGLength animVal;
   protected boolean changing;
   protected boolean nonNegative;

   public AbstractSVGAnimatedLength(AbstractElement var1, String var2, String var3, short var4, boolean var5) {
      super(var1, var2, var3);
      this.direction = var4;
      this.nonNegative = var5;
   }

   protected abstract String getDefaultValue();

   public SVGLength getBaseVal() {
      if (this.baseVal == null) {
         this.baseVal = new BaseSVGLength(this.direction);
      }

      return this.baseVal;
   }

   public SVGLength getAnimVal() {
      if (this.animVal == null) {
         this.animVal = new AnimSVGLength(this.direction);
      }

      return this.animVal;
   }

   public float getCheckedValue() {
      if (this.hasAnimVal) {
         if (this.animVal == null) {
            this.animVal = new AnimSVGLength(this.direction);
         }

         if (this.nonNegative && this.animVal.value < 0.0F) {
            throw new LiveAttributeException(this.element, this.localName, (short)2, this.animVal.getValueAsString());
         } else {
            return this.animVal.getValue();
         }
      } else {
         if (this.baseVal == null) {
            this.baseVal = new BaseSVGLength(this.direction);
         }

         this.baseVal.revalidate();
         if (this.baseVal.missing) {
            throw new LiveAttributeException(this.element, this.localName, (short)0, (String)null);
         } else if (this.baseVal.unitType == 0) {
            throw new LiveAttributeException(this.element, this.localName, (short)1, this.baseVal.getValueAsString());
         } else if (this.nonNegative && this.baseVal.value < 0.0F) {
            throw new LiveAttributeException(this.element, this.localName, (short)2, this.baseVal.getValueAsString());
         } else {
            return this.baseVal.getValue();
         }
      }
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         AnimatableLengthValue var2 = (AnimatableLengthValue)var1;
         if (this.animVal == null) {
            this.animVal = new AnimSVGLength(this.direction);
         }

         this.animVal.setAnimatedValue(var2.getLengthType(), var2.getLengthValue());
      }

      this.fireAnimatedAttributeListeners();
   }

   public AnimatableValue getUnderlyingValue(AnimationTarget var1) {
      SVGLength var2 = this.getBaseVal();
      return new AnimatableLengthValue(var1, var2.getUnitType(), var2.getValueInSpecifiedUnits(), var1.getPercentageInterpretation(this.getNamespaceURI(), this.getLocalName(), false));
   }

   public void attrAdded(Attr var1, String var2) {
      this.attrChanged();
   }

   public void attrModified(Attr var1, String var2, String var3) {
      this.attrChanged();
   }

   public void attrRemoved(Attr var1, String var2) {
      this.attrChanged();
   }

   protected void attrChanged() {
      if (!this.changing && this.baseVal != null) {
         this.baseVal.invalidate();
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   protected class AnimSVGLength extends AbstractSVGLength {
      public AnimSVGLength(short var2) {
         super(var2);
      }

      public short getUnitType() {
         return AbstractSVGAnimatedLength.this.hasAnimVal ? super.getUnitType() : AbstractSVGAnimatedLength.this.getBaseVal().getUnitType();
      }

      public float getValue() {
         return AbstractSVGAnimatedLength.this.hasAnimVal ? super.getValue() : AbstractSVGAnimatedLength.this.getBaseVal().getValue();
      }

      public float getValueInSpecifiedUnits() {
         return AbstractSVGAnimatedLength.this.hasAnimVal ? super.getValueInSpecifiedUnits() : AbstractSVGAnimatedLength.this.getBaseVal().getValueInSpecifiedUnits();
      }

      public String getValueAsString() {
         return AbstractSVGAnimatedLength.this.hasAnimVal ? super.getValueAsString() : AbstractSVGAnimatedLength.this.getBaseVal().getValueAsString();
      }

      public void setValue(float var1) throws DOMException {
         throw AbstractSVGAnimatedLength.this.element.createDOMException((short)7, "readonly.length", (Object[])null);
      }

      public void setValueInSpecifiedUnits(float var1) throws DOMException {
         throw AbstractSVGAnimatedLength.this.element.createDOMException((short)7, "readonly.length", (Object[])null);
      }

      public void setValueAsString(String var1) throws DOMException {
         throw AbstractSVGAnimatedLength.this.element.createDOMException((short)7, "readonly.length", (Object[])null);
      }

      public void newValueSpecifiedUnits(short var1, float var2) {
         throw AbstractSVGAnimatedLength.this.element.createDOMException((short)7, "readonly.length", (Object[])null);
      }

      public void convertToSpecifiedUnits(short var1) {
         throw AbstractSVGAnimatedLength.this.element.createDOMException((short)7, "readonly.length", (Object[])null);
      }

      protected SVGOMElement getAssociatedElement() {
         return (SVGOMElement)AbstractSVGAnimatedLength.this.element;
      }

      protected void setAnimatedValue(int var1, float var2) {
         super.newValueSpecifiedUnits((short)var1, var2);
      }
   }

   protected class BaseSVGLength extends AbstractSVGLength {
      protected boolean valid;
      protected boolean missing;

      public BaseSVGLength(short var2) {
         super(var2);
      }

      public void invalidate() {
         this.valid = false;
      }

      protected void reset() {
         try {
            AbstractSVGAnimatedLength.this.changing = true;
            this.valid = true;
            String var1 = this.getValueAsString();
            AbstractSVGAnimatedLength.this.element.setAttributeNS(AbstractSVGAnimatedLength.this.namespaceURI, AbstractSVGAnimatedLength.this.localName, var1);
         } finally {
            AbstractSVGAnimatedLength.this.changing = false;
         }

      }

      protected void revalidate() {
         if (!this.valid) {
            this.missing = false;
            this.valid = true;
            Attr var1 = AbstractSVGAnimatedLength.this.element.getAttributeNodeNS(AbstractSVGAnimatedLength.this.namespaceURI, AbstractSVGAnimatedLength.this.localName);
            String var2;
            if (var1 == null) {
               var2 = AbstractSVGAnimatedLength.this.getDefaultValue();
               if (var2 == null) {
                  this.missing = true;
                  return;
               }
            } else {
               var2 = var1.getValue();
            }

            this.parse(var2);
         }
      }

      protected SVGOMElement getAssociatedElement() {
         return (SVGOMElement)AbstractSVGAnimatedLength.this.element;
      }
   }
}
