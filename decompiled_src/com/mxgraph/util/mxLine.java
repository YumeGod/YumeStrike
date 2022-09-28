package com.mxgraph.util;

import java.awt.geom.Line2D;

public class mxLine extends mxPoint {
   protected mxPoint endPoint;

   public mxLine(mxPoint var1, mxPoint var2) {
      this.setX(var1.getX());
      this.setY(var1.getY());
      this.endPoint = var2;
   }

   public mxPoint getEndPoint() {
      return this.endPoint;
   }

   public void setEndPoint(mxPoint var1) {
      this.endPoint = var1;
   }

   public double ptLineDistSq(mxPoint var1) {
      return (new Line2D.Double(this.getX(), this.getY(), this.endPoint.getX(), this.endPoint.getY())).ptLineDistSq(var1.getX(), var1.getY());
   }

   public double ptSegDistSq(mxPoint var1) {
      return (new Line2D.Double(this.getX(), this.getY(), this.endPoint.getX(), this.endPoint.getY())).ptSegDistSq(var1.getX(), var1.getY());
   }
}
