package com.mxgraph.model;

import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import java.util.ArrayList;
import java.util.List;

public class mxGeometry extends mxRectangle {
   private static final long serialVersionUID = 2649828026610336589L;
   public static transient boolean TRANSLATE_CONTROL_POINTS = true;
   protected mxRectangle alternateBounds;
   protected mxPoint sourcePoint;
   protected mxPoint targetPoint;
   protected List points;
   protected mxPoint offset;
   protected boolean relative;

   public mxGeometry() {
      this(0.0, 0.0, 0.0, 0.0);
   }

   public mxGeometry(double var1, double var3, double var5, double var7) {
      super(var1, var3, var5, var7);
      this.relative = false;
   }

   public mxRectangle getAlternateBounds() {
      return this.alternateBounds;
   }

   public void setAlternateBounds(mxRectangle var1) {
      this.alternateBounds = var1;
   }

   public mxPoint getSourcePoint() {
      return this.sourcePoint;
   }

   public void setSourcePoint(mxPoint var1) {
      this.sourcePoint = var1;
   }

   public mxPoint getTargetPoint() {
      return this.targetPoint;
   }

   public void setTargetPoint(mxPoint var1) {
      this.targetPoint = var1;
   }

   public List getPoints() {
      return this.points;
   }

   public void setPoints(List var1) {
      this.points = var1;
   }

   public mxPoint getOffset() {
      return this.offset;
   }

   public void setOffset(mxPoint var1) {
      this.offset = var1;
   }

   public boolean isRelative() {
      return this.relative;
   }

   public void setRelative(boolean var1) {
      this.relative = var1;
   }

   public void swap() {
      if (this.alternateBounds != null) {
         mxRectangle var1 = new mxRectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
         this.x = this.alternateBounds.getX();
         this.y = this.alternateBounds.getY();
         this.width = this.alternateBounds.getWidth();
         this.height = this.alternateBounds.getHeight();
         this.alternateBounds = var1;
      }

   }

   public mxPoint getTerminalPoint(boolean var1) {
      return var1 ? this.sourcePoint : this.targetPoint;
   }

   public mxPoint setTerminalPoint(mxPoint var1, boolean var2) {
      if (var2) {
         this.sourcePoint = var1;
      } else {
         this.targetPoint = var1;
      }

      return var1;
   }

   public void translate(double var1, double var3) {
      if (!this.isRelative()) {
         this.x += var1;
         this.y += var3;
      }

      if (this.sourcePoint != null) {
         this.sourcePoint.setX(this.sourcePoint.getX() + var1);
         this.sourcePoint.setY(this.sourcePoint.getY() + var3);
      }

      if (this.targetPoint != null) {
         this.targetPoint.setX(this.targetPoint.getX() + var1);
         this.targetPoint.setY(this.targetPoint.getY() + var3);
      }

      if (TRANSLATE_CONTROL_POINTS && this.points != null) {
         int var5 = this.points.size();

         for(int var6 = 0; var6 < var5; ++var6) {
            mxPoint var7 = (mxPoint)this.points.get(var6);
            var7.setX(var7.getX() + var1);
            var7.setY(var7.getY() + var3);
         }
      }

   }

   public Object clone() {
      mxGeometry var1 = (mxGeometry)super.clone();
      var1.setX(this.getX());
      var1.setY(this.getY());
      var1.setWidth(this.getWidth());
      var1.setHeight(this.getHeight());
      var1.setRelative(this.isRelative());
      List var2 = this.getPoints();
      if (var2 != null) {
         var1.points = new ArrayList(var2.size());

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            var1.points.add((mxPoint)((mxPoint)var2.get(var3)).clone());
         }
      }

      mxPoint var7 = this.getTargetPoint();
      if (var7 != null) {
         var1.setTargetPoint((mxPoint)var7.clone());
      }

      mxPoint var4 = this.getSourcePoint();
      if (var4 != null) {
         this.setSourcePoint((mxPoint)var4.clone());
      }

      mxPoint var5 = this.getOffset();
      if (var5 != null) {
         var1.setOffset((mxPoint)var5.clone());
      }

      mxRectangle var6 = this.getAlternateBounds();
      if (var6 != null) {
         this.setAlternateBounds((mxRectangle)var6.clone());
      }

      return var1;
   }
}
