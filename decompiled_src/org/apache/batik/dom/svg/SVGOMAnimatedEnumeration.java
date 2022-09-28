package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatableStringValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedEnumeration;

public class SVGOMAnimatedEnumeration extends AbstractSVGAnimatedValue implements SVGAnimatedEnumeration {
   protected String[] values;
   protected short defaultValue;
   protected boolean valid;
   protected short baseVal;
   protected short animVal;
   protected boolean changing;

   public SVGOMAnimatedEnumeration(AbstractElement var1, String var2, String var3, String[] var4, short var5) {
      super(var1, var2, var3);
      this.values = var4;
      this.defaultValue = var5;
   }

   public short getBaseVal() {
      if (!this.valid) {
         this.update();
      }

      return this.baseVal;
   }

   public String getBaseValAsString() {
      if (!this.valid) {
         this.update();
      }

      return this.values[this.baseVal];
   }

   protected void update() {
      String var1 = this.element.getAttributeNS(this.namespaceURI, this.localName);
      if (var1.length() == 0) {
         this.baseVal = this.defaultValue;
      } else {
         this.baseVal = this.getEnumerationNumber(var1);
      }

      this.valid = true;
   }

   protected short getEnumerationNumber(String var1) {
      for(short var2 = 0; var2 < this.values.length; ++var2) {
         if (var1.equals(this.values[var2])) {
            return var2;
         }
      }

      return 0;
   }

   public void setBaseVal(short var1) throws DOMException {
      if (var1 >= 0 && var1 < this.values.length) {
         try {
            this.baseVal = var1;
            this.valid = true;
            this.changing = true;
            this.element.setAttributeNS(this.namespaceURI, this.localName, this.values[var1]);
         } finally {
            this.changing = false;
         }
      }

   }

   public short getAnimVal() {
      if (this.hasAnimVal) {
         return this.animVal;
      } else {
         if (!this.valid) {
            this.update();
         }

         return this.baseVal;
      }
   }

   public short getCheckedVal() {
      if (this.hasAnimVal) {
         return this.animVal;
      } else {
         if (!this.valid) {
            this.update();
         }

         if (this.baseVal == 0) {
            throw new LiveAttributeException(this.element, this.localName, (short)1, this.getBaseValAsString());
         } else {
            return this.baseVal;
         }
      }
   }

   public AnimatableValue getUnderlyingValue(AnimationTarget var1) {
      return new AnimatableStringValue(var1, this.getBaseValAsString());
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

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         this.animVal = this.getEnumerationNumber(((AnimatableStringValue)var1).getString());
         this.fireAnimatedAttributeListeners();
      }

      this.fireAnimatedAttributeListeners();
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
