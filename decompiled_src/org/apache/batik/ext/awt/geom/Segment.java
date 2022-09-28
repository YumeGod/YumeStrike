package org.apache.batik.ext.awt.geom;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface Segment extends Cloneable {
   double minX();

   double maxX();

   double minY();

   double maxY();

   Rectangle2D getBounds2D();

   Point2D.Double evalDt(double var1);

   Point2D.Double eval(double var1);

   Segment getSegment(double var1, double var3);

   Segment splitBefore(double var1);

   Segment splitAfter(double var1);

   void subdivide(Segment var1, Segment var2);

   void subdivide(double var1, Segment var3, Segment var4);

   double getLength();

   double getLength(double var1);

   SplitResults split(double var1);

   public static class SplitResults {
      Segment[] above;
      Segment[] below;

      SplitResults(Segment[] var1, Segment[] var2) {
         this.below = var1;
         this.above = var2;
      }

      Segment[] getBelow() {
         return this.below;
      }

      Segment[] getAbove() {
         return this.above;
      }
   }
}
