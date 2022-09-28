package org.apache.batik.dom.svg;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGMatrix;

public abstract class AbstractSVGMatrix implements SVGMatrix {
   protected static final AffineTransform FLIP_X_TRANSFORM = new AffineTransform(-1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F);
   protected static final AffineTransform FLIP_Y_TRANSFORM = new AffineTransform(1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F);

   protected abstract AffineTransform getAffineTransform();

   public float getA() {
      return (float)this.getAffineTransform().getScaleX();
   }

   public void setA(float var1) throws DOMException {
      AffineTransform var2 = this.getAffineTransform();
      var2.setTransform((double)var1, var2.getShearY(), var2.getShearX(), var2.getScaleY(), var2.getTranslateX(), var2.getTranslateY());
   }

   public float getB() {
      return (float)this.getAffineTransform().getShearY();
   }

   public void setB(float var1) throws DOMException {
      AffineTransform var2 = this.getAffineTransform();
      var2.setTransform(var2.getScaleX(), (double)var1, var2.getShearX(), var2.getScaleY(), var2.getTranslateX(), var2.getTranslateY());
   }

   public float getC() {
      return (float)this.getAffineTransform().getShearX();
   }

   public void setC(float var1) throws DOMException {
      AffineTransform var2 = this.getAffineTransform();
      var2.setTransform(var2.getScaleX(), var2.getShearY(), (double)var1, var2.getScaleY(), var2.getTranslateX(), var2.getTranslateY());
   }

   public float getD() {
      return (float)this.getAffineTransform().getScaleY();
   }

   public void setD(float var1) throws DOMException {
      AffineTransform var2 = this.getAffineTransform();
      var2.setTransform(var2.getScaleX(), var2.getShearY(), var2.getShearX(), (double)var1, var2.getTranslateX(), var2.getTranslateY());
   }

   public float getE() {
      return (float)this.getAffineTransform().getTranslateX();
   }

   public void setE(float var1) throws DOMException {
      AffineTransform var2 = this.getAffineTransform();
      var2.setTransform(var2.getScaleX(), var2.getShearY(), var2.getShearX(), var2.getScaleY(), (double)var1, var2.getTranslateY());
   }

   public float getF() {
      return (float)this.getAffineTransform().getTranslateY();
   }

   public void setF(float var1) throws DOMException {
      AffineTransform var2 = this.getAffineTransform();
      var2.setTransform(var2.getScaleX(), var2.getShearY(), var2.getShearX(), var2.getScaleY(), var2.getTranslateX(), (double)var1);
   }

   public SVGMatrix multiply(SVGMatrix var1) {
      AffineTransform var2 = new AffineTransform(var1.getA(), var1.getB(), var1.getC(), var1.getD(), var1.getE(), var1.getF());
      AffineTransform var3 = (AffineTransform)this.getAffineTransform().clone();
      var3.concatenate(var2);
      return new SVGOMMatrix(var3);
   }

   public SVGMatrix inverse() throws SVGException {
      try {
         return new SVGOMMatrix(this.getAffineTransform().createInverse());
      } catch (NoninvertibleTransformException var2) {
         throw new SVGOMException((short)2, var2.getMessage());
      }
   }

   public SVGMatrix translate(float var1, float var2) {
      AffineTransform var3 = (AffineTransform)this.getAffineTransform().clone();
      var3.translate((double)var1, (double)var2);
      return new SVGOMMatrix(var3);
   }

   public SVGMatrix scale(float var1) {
      AffineTransform var2 = (AffineTransform)this.getAffineTransform().clone();
      var2.scale((double)var1, (double)var1);
      return new SVGOMMatrix(var2);
   }

   public SVGMatrix scaleNonUniform(float var1, float var2) {
      AffineTransform var3 = (AffineTransform)this.getAffineTransform().clone();
      var3.scale((double)var1, (double)var2);
      return new SVGOMMatrix(var3);
   }

   public SVGMatrix rotate(float var1) {
      AffineTransform var2 = (AffineTransform)this.getAffineTransform().clone();
      var2.rotate(Math.toRadians((double)var1));
      return new SVGOMMatrix(var2);
   }

   public SVGMatrix rotateFromVector(float var1, float var2) throws SVGException {
      if (var1 != 0.0F && var2 != 0.0F) {
         AffineTransform var3 = (AffineTransform)this.getAffineTransform().clone();
         var3.rotate(Math.atan2((double)var2, (double)var1));
         return new SVGOMMatrix(var3);
      } else {
         throw new SVGOMException((short)1, "");
      }
   }

   public SVGMatrix flipX() {
      AffineTransform var1 = (AffineTransform)this.getAffineTransform().clone();
      var1.concatenate(FLIP_X_TRANSFORM);
      return new SVGOMMatrix(var1);
   }

   public SVGMatrix flipY() {
      AffineTransform var1 = (AffineTransform)this.getAffineTransform().clone();
      var1.concatenate(FLIP_Y_TRANSFORM);
      return new SVGOMMatrix(var1);
   }

   public SVGMatrix skewX(float var1) {
      AffineTransform var2 = (AffineTransform)this.getAffineTransform().clone();
      var2.concatenate(AffineTransform.getShearInstance(Math.tan(Math.toRadians((double)var1)), 0.0));
      return new SVGOMMatrix(var2);
   }

   public SVGMatrix skewY(float var1) {
      AffineTransform var2 = (AffineTransform)this.getAffineTransform().clone();
      var2.concatenate(AffineTransform.getShearInstance(0.0, Math.tan(Math.toRadians((double)var1))));
      return new SVGOMMatrix(var2);
   }
}
