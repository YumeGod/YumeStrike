package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatableRectValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.parser.DefaultNumberListHandler;
import org.apache.batik.parser.NumberListParser;
import org.apache.batik.parser.ParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGRect;

public class SVGOMAnimatedRect extends AbstractSVGAnimatedValue implements SVGAnimatedRect {
   protected BaseSVGRect baseVal;
   protected AnimSVGRect animVal;
   protected boolean changing;
   protected String defaultValue;

   public SVGOMAnimatedRect(AbstractElement var1, String var2, String var3, String var4) {
      super(var1, var2, var3);
      this.defaultValue = var4;
   }

   public SVGRect getBaseVal() {
      if (this.baseVal == null) {
         this.baseVal = new BaseSVGRect();
      }

      return this.baseVal;
   }

   public SVGRect getAnimVal() {
      if (this.animVal == null) {
         this.animVal = new AnimSVGRect();
      }

      return this.animVal;
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         AnimatableRectValue var2 = (AnimatableRectValue)var1;
         if (this.animVal == null) {
            this.animVal = new AnimSVGRect();
         }

         this.animVal.setAnimatedValue(var2.getX(), var2.getY(), var2.getWidth(), var2.getHeight());
      }

      this.fireAnimatedAttributeListeners();
   }

   public AnimatableValue getUnderlyingValue(AnimationTarget var1) {
      SVGRect var2 = this.getBaseVal();
      return new AnimatableRectValue(var1, var2.getX(), var2.getY(), var2.getWidth(), var2.getHeight());
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

   protected class AnimSVGRect extends SVGOMRect {
      public float getX() {
         return SVGOMAnimatedRect.this.hasAnimVal ? super.getX() : SVGOMAnimatedRect.this.getBaseVal().getX();
      }

      public float getY() {
         return SVGOMAnimatedRect.this.hasAnimVal ? super.getY() : SVGOMAnimatedRect.this.getBaseVal().getY();
      }

      public float getWidth() {
         return SVGOMAnimatedRect.this.hasAnimVal ? super.getWidth() : SVGOMAnimatedRect.this.getBaseVal().getWidth();
      }

      public float getHeight() {
         return SVGOMAnimatedRect.this.hasAnimVal ? super.getHeight() : SVGOMAnimatedRect.this.getBaseVal().getHeight();
      }

      public void setX(float var1) throws DOMException {
         throw SVGOMAnimatedRect.this.element.createDOMException((short)7, "readonly.length", (Object[])null);
      }

      public void setY(float var1) throws DOMException {
         throw SVGOMAnimatedRect.this.element.createDOMException((short)7, "readonly.length", (Object[])null);
      }

      public void setWidth(float var1) throws DOMException {
         throw SVGOMAnimatedRect.this.element.createDOMException((short)7, "readonly.length", (Object[])null);
      }

      public void setHeight(float var1) throws DOMException {
         throw SVGOMAnimatedRect.this.element.createDOMException((short)7, "readonly.length", (Object[])null);
      }

      protected void setAnimatedValue(float var1, float var2, float var3, float var4) {
         this.x = var1;
         this.y = var2;
         this.w = var3;
         this.h = var4;
      }
   }

   protected class BaseSVGRect extends SVGOMRect {
      protected boolean valid;

      public void invalidate() {
         this.valid = false;
      }

      protected void reset() {
         try {
            SVGOMAnimatedRect.this.changing = true;
            SVGOMAnimatedRect.this.element.setAttributeNS(SVGOMAnimatedRect.this.namespaceURI, SVGOMAnimatedRect.this.localName, Float.toString(this.x) + ' ' + this.y + ' ' + this.w + ' ' + this.h);
         } finally {
            SVGOMAnimatedRect.this.changing = false;
         }

      }

      protected void revalidate() {
         if (!this.valid) {
            Attr var1 = SVGOMAnimatedRect.this.element.getAttributeNodeNS(SVGOMAnimatedRect.this.namespaceURI, SVGOMAnimatedRect.this.localName);
            final String var2 = var1 == null ? SVGOMAnimatedRect.this.defaultValue : var1.getValue();
            final float[] var3 = new float[4];
            NumberListParser var4 = new NumberListParser();
            var4.setNumberListHandler(new DefaultNumberListHandler() {
               protected int count;

               public void endNumberList() {
                  if (this.count != 4) {
                     throw new LiveAttributeException(SVGOMAnimatedRect.this.element, SVGOMAnimatedRect.this.localName, (short)1, var2);
                  }
               }

               public void numberValue(float var1) throws ParseException {
                  if (this.count < 4) {
                     var3[this.count] = var1;
                  }

                  if (!(var1 < 0.0F) || this.count != 2 && this.count != 3) {
                     ++this.count;
                  } else {
                     throw new LiveAttributeException(SVGOMAnimatedRect.this.element, SVGOMAnimatedRect.this.localName, (short)1, var2);
                  }
               }
            });
            var4.parse(var2);
            this.x = var3[0];
            this.y = var3[1];
            this.w = var3[2];
            this.h = var3[3];
            this.valid = true;
         }
      }

      public float getX() {
         this.revalidate();
         return this.x;
      }

      public void setX(float var1) throws DOMException {
         this.x = var1;
         this.reset();
      }

      public float getY() {
         this.revalidate();
         return this.y;
      }

      public void setY(float var1) throws DOMException {
         this.y = var1;
         this.reset();
      }

      public float getWidth() {
         this.revalidate();
         return this.w;
      }

      public void setWidth(float var1) throws DOMException {
         this.w = var1;
         this.reset();
      }

      public float getHeight() {
         this.revalidate();
         return this.h;
      }

      public void setHeight(float var1) throws DOMException {
         this.h = var1;
         this.reset();
      }
   }
}
