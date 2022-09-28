package org.apache.fop.afp.util;

import java.awt.geom.Point2D;

public class CubicBezierApproximator {
   public static double[][] fixedMidPointApproximation(double[] cubicControlPointCoords) {
      if (cubicControlPointCoords.length < 8) {
         throw new IllegalArgumentException("Must have at least 8 coordinates");
      } else {
         Point2D p0 = new Point2D.Double(cubicControlPointCoords[0], cubicControlPointCoords[1]);
         Point2D p1 = new Point2D.Double(cubicControlPointCoords[2], cubicControlPointCoords[3]);
         Point2D p2 = new Point2D.Double(cubicControlPointCoords[4], cubicControlPointCoords[5]);
         Point2D p3 = new Point2D.Double(cubicControlPointCoords[6], cubicControlPointCoords[7]);
         Point2D pa = getPointOnSegment(p0, p1, 0.75);
         Point2D pb = getPointOnSegment(p3, p2, 0.75);
         double dx = (p3.getX() - p0.getX()) / 16.0;
         double dy = (p3.getY() - p0.getY()) / 16.0;
         Point2D pc1 = getPointOnSegment(p0, p1, 0.375);
         Point2D pc2 = getPointOnSegment(pa, pb, 0.375);
         Point2D pc2 = movePoint(pc2, -dx, -dy);
         Point2D pc3 = getPointOnSegment(pb, pa, 0.375);
         Point2D pc3 = movePoint(pc3, dx, dy);
         Point2D pc4 = getPointOnSegment(p3, p2, 0.375);
         Point2D pa1 = getMidPoint(pc1, pc2);
         Point2D pa2 = getMidPoint(pa, pb);
         Point2D pa3 = getMidPoint(pc3, pc4);
         return new double[][]{{pc1.getX(), pc1.getY(), pa1.getX(), pa1.getY()}, {pc2.getX(), pc2.getY(), pa2.getX(), pa2.getY()}, {pc3.getX(), pc3.getY(), pa3.getX(), pa3.getY()}, {pc4.getX(), pc4.getY(), p3.getX(), p3.getY()}};
      }
   }

   private static Point2D.Double movePoint(Point2D point, double dx, double dy) {
      return new Point2D.Double(point.getX() + dx, point.getY() + dy);
   }

   private static Point2D getMidPoint(Point2D p0, Point2D p1) {
      return getPointOnSegment(p0, p1, 0.5);
   }

   private static Point2D getPointOnSegment(Point2D p0, Point2D p1, double ratio) {
      double x = p0.getX() + (p1.getX() - p0.getX()) * ratio;
      double y = p0.getY() + (p1.getY() - p0.getY()) * ratio;
      return new Point2D.Double(x, y);
   }
}
