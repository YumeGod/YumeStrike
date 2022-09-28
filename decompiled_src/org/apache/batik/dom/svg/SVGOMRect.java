package org.apache.batik.dom.svg;

import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGRect;

public class SVGOMRect implements SVGRect {
   protected float x;
   protected float y;
   protected float w;
   protected float h;

   public SVGOMRect() {
   }

   public SVGOMRect(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.w = var3;
      this.h = var4;
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

   public float getWidth() {
      return this.w;
   }

   public void setWidth(float var1) throws DOMException {
      this.w = var1;
   }

   public float getHeight() {
      return this.h;
   }

   public void setHeight(float var1) throws DOMException {
      this.h = var1;
   }
}
