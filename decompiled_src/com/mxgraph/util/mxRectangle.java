package com.mxgraph.util;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class mxRectangle extends mxPoint {
   private static final long serialVersionUID = -3793966043543578946L;
   protected double width;
   protected double height;

   public mxRectangle() {
      this(0.0, 0.0, 0.0, 0.0);
   }

   public mxRectangle(Rectangle2D var1) {
      this(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
   }

   public mxRectangle(mxRectangle var1) {
      this(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
   }

   public mxRectangle(double var1, double var3, double var5, double var7) {
      super(var1, var3);
      this.setWidth(var5);
      this.setHeight(var7);
   }

   public double getWidth() {
      return this.width;
   }

   public void setWidth(double var1) {
      this.width = var1;
   }

   public double getHeight() {
      return this.height;
   }

   public void setHeight(double var1) {
      this.height = var1;
   }

   public void add(mxRectangle var1) {
      if (var1 != null) {
         double var2 = Math.min(this.x, var1.x);
         double var4 = Math.min(this.y, var1.y);
         double var6 = Math.max(this.x + this.width, var1.x + var1.width);
         double var8 = Math.max(this.y + this.height, var1.y + var1.height);
         this.x = var2;
         this.y = var4;
         this.width = var6 - var2;
         this.height = var8 - var4;
      }

   }

   public double getCenterX() {
      return this.getX() + this.getWidth() / 2.0;
   }

   public double getCenterY() {
      return this.getY() + this.getHeight() / 2.0;
   }

   public void grow(double var1) {
      this.x -= var1;
      this.y -= var1;
      this.width += 2.0 * var1;
      this.height += 2.0 * var1;
   }

   public boolean contains(double var1, double var3) {
      return this.x <= var1 && this.x + this.width >= var1 && this.y <= var3 && this.y + this.height >= var3;
   }

   public mxPoint intersectLine(double var1, double var3, double var5, double var7) {
      mxPoint var9 = null;
      var9 = mxUtils.intersection(this.x, this.y, this.x + this.width, this.y, var1, var3, var5, var7);
      if (var9 == null) {
         var9 = mxUtils.intersection(this.x + this.width, this.y, this.x + this.width, this.y + this.height, var1, var3, var5, var7);
      }

      if (var9 == null) {
         var9 = mxUtils.intersection(this.x + this.width, this.y + this.height, this.x, this.y + this.height, var1, var3, var5, var7);
      }

      if (var9 == null) {
         var9 = mxUtils.intersection(this.x, this.y, this.x, this.y + this.height, var1, var3, var5, var7);
      }

      return var9;
   }

   public Rectangle getRectangle() {
      int var1 = (int)Math.round(this.x);
      int var2 = (int)Math.round(this.y);
      int var3 = (int)Math.round(this.width - (double)var1 + this.x);
      int var4 = (int)Math.round(this.height - (double)var2 + this.y);
      return new Rectangle(var1, var2, var3, var4);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof mxRectangle)) {
         return false;
      } else {
         mxRectangle var2 = (mxRectangle)var1;
         return var2.getX() == this.getX() && var2.getY() == this.getY() && var2.getWidth() == this.getWidth() && var2.getHeight() == this.getHeight();
      }
   }

   public Object clone() {
      mxRectangle var1 = (mxRectangle)super.clone();
      var1.setWidth(this.getWidth());
      var1.setHeight(this.getHeight());
      return var1;
   }
}
