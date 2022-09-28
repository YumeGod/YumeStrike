package org.apache.batik.dom.svg;

import java.awt.geom.AffineTransform;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGTransform;

public abstract class AbstractSVGTransform implements SVGTransform {
   protected short type = 0;
   protected AffineTransform affineTransform;
   protected float angle;
   protected float x;
   protected float y;

   protected abstract SVGMatrix createMatrix();

   protected void setType(short var1) {
      this.type = var1;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public void assign(AbstractSVGTransform var1) {
      this.type = var1.type;
      this.affineTransform = var1.affineTransform;
      this.angle = var1.angle;
      this.x = var1.x;
      this.y = var1.y;
   }

   public short getType() {
      return this.type;
   }

   public SVGMatrix getMatrix() {
      return this.createMatrix();
   }

   public float getAngle() {
      return this.angle;
   }

   public void setMatrix(SVGMatrix var1) {
      this.type = 1;
      this.affineTransform = new AffineTransform(var1.getA(), var1.getB(), var1.getC(), var1.getD(), var1.getE(), var1.getF());
   }

   public void setTranslate(float var1, float var2) {
      this.type = 2;
      this.affineTransform = AffineTransform.getTranslateInstance((double)var1, (double)var2);
   }

   public void setScale(float var1, float var2) {
      this.type = 3;
      this.affineTransform = AffineTransform.getScaleInstance((double)var1, (double)var2);
   }

   public void setRotate(float var1, float var2, float var3) {
      this.type = 4;
      this.affineTransform = AffineTransform.getRotateInstance(Math.toRadians((double)var1), (double)var2, (double)var3);
      this.angle = var1;
      this.x = var2;
      this.y = var3;
   }

   public void setSkewX(float var1) {
      this.type = 5;
      this.affineTransform = AffineTransform.getShearInstance(Math.tan(Math.toRadians((double)var1)), 0.0);
      this.angle = var1;
   }

   public void setSkewY(float var1) {
      this.type = 6;
      this.affineTransform = AffineTransform.getShearInstance(0.0, Math.tan(Math.toRadians((double)var1)));
      this.angle = var1;
   }
}
