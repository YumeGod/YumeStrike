package com.mxgraph.view;

import com.mxgraph.util.mxPoint;

public class mxConnectionConstraint {
   protected mxPoint point;
   protected boolean perimeter;

   public mxConnectionConstraint() {
      this((mxPoint)null);
   }

   public mxConnectionConstraint(mxPoint var1) {
      this(var1, true);
   }

   public mxConnectionConstraint(mxPoint var1, boolean var2) {
      this.setPoint(var1);
      this.setPerimeter(var2);
   }

   public mxPoint getPoint() {
      return this.point;
   }

   public void setPoint(mxPoint var1) {
      this.point = var1;
   }

   public boolean isPerimeter() {
      return this.perimeter;
   }

   public void setPerimeter(boolean var1) {
      this.perimeter = var1;
   }
}
