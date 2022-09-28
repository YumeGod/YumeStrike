package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAngle;
import org.w3c.dom.svg.SVGAnimatedAngle;
import org.w3c.dom.svg.SVGAnimatedEnumeration;

public class SVGOMAnimatedMarkerOrientValue extends AbstractSVGAnimatedValue {
   protected boolean valid;
   protected AnimatedAngle animatedAngle = new AnimatedAngle();
   protected AnimatedEnumeration animatedEnumeration = new AnimatedEnumeration();
   protected BaseSVGAngle baseAngleVal;
   protected short baseEnumerationVal;
   protected AnimSVGAngle animAngleVal;
   protected short animEnumerationVal;
   protected boolean changing;

   public SVGOMAnimatedMarkerOrientValue(AbstractElement var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      throw new UnsupportedOperationException("Animation of marker orient value is not implemented");
   }

   public AnimatableValue getUnderlyingValue(AnimationTarget var1) {
      throw new UnsupportedOperationException("Animation of marker orient value is not implemented");
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

   public void setAnimatedValueToAngle(short var1, float var2) {
      this.hasAnimVal = true;
      this.animAngleVal.setAnimatedValue(var1, var2);
      this.animEnumerationVal = 2;
      this.fireAnimatedAttributeListeners();
   }

   public void setAnimatedValueToAuto() {
      this.hasAnimVal = true;
      this.animAngleVal.setAnimatedValue(1, 0.0F);
      this.animEnumerationVal = 1;
      this.fireAnimatedAttributeListeners();
   }

   public void resetAnimatedValue() {
      this.hasAnimVal = false;
      this.fireAnimatedAttributeListeners();
   }

   public SVGAnimatedAngle getAnimatedAngle() {
      return this.animatedAngle;
   }

   public SVGAnimatedEnumeration getAnimatedEnumeration() {
      return this.animatedEnumeration;
   }

   protected class AnimatedEnumeration implements SVGAnimatedEnumeration {
      public short getBaseVal() {
         if (SVGOMAnimatedMarkerOrientValue.this.baseAngleVal == null) {
            SVGOMAnimatedMarkerOrientValue.this.baseAngleVal = SVGOMAnimatedMarkerOrientValue.this.new BaseSVGAngle();
         }

         SVGOMAnimatedMarkerOrientValue.this.baseAngleVal.revalidate();
         return SVGOMAnimatedMarkerOrientValue.this.baseEnumerationVal;
      }

      public void setBaseVal(short var1) throws DOMException {
         if (var1 == 1) {
            SVGOMAnimatedMarkerOrientValue.this.baseEnumerationVal = var1;
            if (SVGOMAnimatedMarkerOrientValue.this.baseAngleVal == null) {
               SVGOMAnimatedMarkerOrientValue.this.baseAngleVal = SVGOMAnimatedMarkerOrientValue.this.new BaseSVGAngle();
            }

            SVGOMAnimatedMarkerOrientValue.this.baseAngleVal.unitType = 1;
            SVGOMAnimatedMarkerOrientValue.this.baseAngleVal.value = 0.0F;
            SVGOMAnimatedMarkerOrientValue.this.baseAngleVal.reset();
         } else if (var1 == 2) {
            SVGOMAnimatedMarkerOrientValue.this.baseEnumerationVal = var1;
            if (SVGOMAnimatedMarkerOrientValue.this.baseAngleVal == null) {
               SVGOMAnimatedMarkerOrientValue.this.baseAngleVal = SVGOMAnimatedMarkerOrientValue.this.new BaseSVGAngle();
            }

            SVGOMAnimatedMarkerOrientValue.this.baseAngleVal.reset();
         }

      }

      public short getAnimVal() {
         if (SVGOMAnimatedMarkerOrientValue.this.hasAnimVal) {
            return SVGOMAnimatedMarkerOrientValue.this.animEnumerationVal;
         } else {
            if (SVGOMAnimatedMarkerOrientValue.this.baseAngleVal == null) {
               SVGOMAnimatedMarkerOrientValue.this.baseAngleVal = SVGOMAnimatedMarkerOrientValue.this.new BaseSVGAngle();
            }

            SVGOMAnimatedMarkerOrientValue.this.baseAngleVal.revalidate();
            return SVGOMAnimatedMarkerOrientValue.this.baseEnumerationVal;
         }
      }
   }

   protected class AnimatedAngle implements SVGAnimatedAngle {
      public SVGAngle getBaseVal() {
         if (SVGOMAnimatedMarkerOrientValue.this.baseAngleVal == null) {
            SVGOMAnimatedMarkerOrientValue.this.baseAngleVal = SVGOMAnimatedMarkerOrientValue.this.new BaseSVGAngle();
         }

         return SVGOMAnimatedMarkerOrientValue.this.baseAngleVal;
      }

      public SVGAngle getAnimVal() {
         if (SVGOMAnimatedMarkerOrientValue.this.animAngleVal == null) {
            SVGOMAnimatedMarkerOrientValue.this.animAngleVal = SVGOMAnimatedMarkerOrientValue.this.new AnimSVGAngle();
         }

         return SVGOMAnimatedMarkerOrientValue.this.animAngleVal;
      }
   }

   protected class AnimSVGAngle extends SVGOMAngle {
      public short getUnitType() {
         return SVGOMAnimatedMarkerOrientValue.this.hasAnimVal ? super.getUnitType() : SVGOMAnimatedMarkerOrientValue.this.animatedAngle.getBaseVal().getUnitType();
      }

      public float getValue() {
         return SVGOMAnimatedMarkerOrientValue.this.hasAnimVal ? super.getValue() : SVGOMAnimatedMarkerOrientValue.this.animatedAngle.getBaseVal().getValue();
      }

      public float getValueInSpecifiedUnits() {
         return SVGOMAnimatedMarkerOrientValue.this.hasAnimVal ? super.getValueInSpecifiedUnits() : SVGOMAnimatedMarkerOrientValue.this.animatedAngle.getBaseVal().getValueInSpecifiedUnits();
      }

      public String getValueAsString() {
         return SVGOMAnimatedMarkerOrientValue.this.hasAnimVal ? super.getValueAsString() : SVGOMAnimatedMarkerOrientValue.this.animatedAngle.getBaseVal().getValueAsString();
      }

      public void setValue(float var1) throws DOMException {
         throw SVGOMAnimatedMarkerOrientValue.this.element.createDOMException((short)7, "readonly.angle", (Object[])null);
      }

      public void setValueInSpecifiedUnits(float var1) throws DOMException {
         throw SVGOMAnimatedMarkerOrientValue.this.element.createDOMException((short)7, "readonly.angle", (Object[])null);
      }

      public void setValueAsString(String var1) throws DOMException {
         throw SVGOMAnimatedMarkerOrientValue.this.element.createDOMException((short)7, "readonly.angle", (Object[])null);
      }

      public void newValueSpecifiedUnits(short var1, float var2) {
         throw SVGOMAnimatedMarkerOrientValue.this.element.createDOMException((short)7, "readonly.angle", (Object[])null);
      }

      public void convertToSpecifiedUnits(short var1) {
         throw SVGOMAnimatedMarkerOrientValue.this.element.createDOMException((short)7, "readonly.angle", (Object[])null);
      }

      protected void setAnimatedValue(int var1, float var2) {
         super.newValueSpecifiedUnits((short)var1, var2);
      }
   }

   protected class BaseSVGAngle extends SVGOMAngle {
      public void invalidate() {
         SVGOMAnimatedMarkerOrientValue.this.valid = false;
      }

      protected void reset() {
         try {
            SVGOMAnimatedMarkerOrientValue.this.changing = true;
            SVGOMAnimatedMarkerOrientValue.this.valid = true;
            String var1;
            if (SVGOMAnimatedMarkerOrientValue.this.baseEnumerationVal == 2) {
               var1 = this.getValueAsString();
            } else {
               if (SVGOMAnimatedMarkerOrientValue.this.baseEnumerationVal != 1) {
                  return;
               }

               var1 = "auto";
            }

            SVGOMAnimatedMarkerOrientValue.this.element.setAttributeNS(SVGOMAnimatedMarkerOrientValue.this.namespaceURI, SVGOMAnimatedMarkerOrientValue.this.localName, var1);
         } finally {
            SVGOMAnimatedMarkerOrientValue.this.changing = false;
         }

      }

      protected void revalidate() {
         if (!SVGOMAnimatedMarkerOrientValue.this.valid) {
            Attr var1 = SVGOMAnimatedMarkerOrientValue.this.element.getAttributeNodeNS(SVGOMAnimatedMarkerOrientValue.this.namespaceURI, SVGOMAnimatedMarkerOrientValue.this.localName);
            if (var1 == null) {
               this.unitType = 1;
               this.value = 0.0F;
            } else {
               this.parse(var1.getValue());
            }

            SVGOMAnimatedMarkerOrientValue.this.valid = true;
         }

      }

      protected void parse(String var1) {
         if (var1.equals("auto")) {
            this.unitType = 1;
            this.value = 0.0F;
            SVGOMAnimatedMarkerOrientValue.this.baseEnumerationVal = 1;
         } else {
            super.parse(var1);
            if (this.unitType == 0) {
               SVGOMAnimatedMarkerOrientValue.this.baseEnumerationVal = 0;
            } else {
               SVGOMAnimatedMarkerOrientValue.this.baseEnumerationVal = 2;
            }
         }

      }
   }
}
