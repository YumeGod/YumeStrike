package org.apache.batik.dom.svg;

import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGPoint;

public class SVGOMPoint implements SVGPoint {
   protected float x;
   protected float y;

   public SVGOMPoint() {
   }

   public SVGOMPoint(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public float getX() {
      return this.x;
   }

   public void setX(float var1) throws DOMException {
      this.x = var1;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float var1) throws DOMException {
      this.y = var1;
   }

   public SVGPoint matrixTransform(SVGMatrix var1) {
      return matrixTransform(this, var1);
   }

   public static SVGPoint matrixTransform(SVGPoint var0, SVGMatrix var1) {
      float var2 = var1.getA() * var0.getX() + var1.getC() * var0.getY() + var1.getE();
      float var3 = var1.getB() * var0.getX() + var1.getD() * var0.getY() + var1.getF();
      return new SVGOMPoint(var2, var3);
   }
}
