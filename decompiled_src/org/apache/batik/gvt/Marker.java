package org.apache.batik.gvt;

import java.awt.geom.Point2D;

public class Marker {
   protected double orient;
   protected GraphicsNode markerNode;
   protected Point2D ref;

   public Marker(GraphicsNode var1, Point2D var2, double var3) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else if (var2 == null) {
         throw new IllegalArgumentException();
      } else {
         this.markerNode = var1;
         this.ref = var2;
         this.orient = var3;
      }
   }

   public Point2D getRef() {
      return (Point2D)this.ref.clone();
   }

   public double getOrient() {
      return this.orient;
   }

   public GraphicsNode getMarkerNode() {
      return this.markerNode;
   }
}
