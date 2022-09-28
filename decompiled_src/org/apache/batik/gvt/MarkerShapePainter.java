package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.apache.batik.ext.awt.geom.ExtendedPathIterator;
import org.apache.batik.ext.awt.geom.ExtendedShape;
import org.apache.batik.ext.awt.geom.ShapeExtender;

public class MarkerShapePainter implements ShapePainter {
   protected ExtendedShape extShape;
   protected Marker startMarker;
   protected Marker middleMarker;
   protected Marker endMarker;
   private ProxyGraphicsNode startMarkerProxy;
   private ProxyGraphicsNode[] middleMarkerProxies;
   private ProxyGraphicsNode endMarkerProxy;
   private CompositeGraphicsNode markerGroup;
   private Rectangle2D dPrimitiveBounds;
   private Rectangle2D dGeometryBounds;

   public MarkerShapePainter(Shape var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         if (var1 instanceof ExtendedShape) {
            this.extShape = (ExtendedShape)var1;
         } else {
            this.extShape = new ShapeExtender(var1);
         }

      }
   }

   public void paint(Graphics2D var1) {
      if (this.markerGroup == null) {
         this.buildMarkerGroup();
      }

      if (this.markerGroup.getChildren().size() > 0) {
         this.markerGroup.paint(var1);
      }

   }

   public Shape getPaintedArea() {
      if (this.markerGroup == null) {
         this.buildMarkerGroup();
      }

      return this.markerGroup.getOutline();
   }

   public Rectangle2D getPaintedBounds2D() {
      if (this.markerGroup == null) {
         this.buildMarkerGroup();
      }

      return this.markerGroup.getPrimitiveBounds();
   }

   public boolean inPaintedArea(Point2D var1) {
      if (this.markerGroup == null) {
         this.buildMarkerGroup();
      }

      GraphicsNode var2 = this.markerGroup.nodeHitAt(var1);
      return var2 != null;
   }

   public Shape getSensitiveArea() {
      return null;
   }

   public Rectangle2D getSensitiveBounds2D() {
      return null;
   }

   public boolean inSensitiveArea(Point2D var1) {
      return false;
   }

   public void setShape(Shape var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         if (var1 instanceof ExtendedShape) {
            this.extShape = (ExtendedShape)var1;
         } else {
            this.extShape = new ShapeExtender(var1);
         }

         this.startMarkerProxy = null;
         this.middleMarkerProxies = null;
         this.endMarkerProxy = null;
         this.markerGroup = null;
      }
   }

   public ExtendedShape getExtShape() {
      return this.extShape;
   }

   public Shape getShape() {
      return this.extShape;
   }

   public Marker getStartMarker() {
      return this.startMarker;
   }

   public void setStartMarker(Marker var1) {
      this.startMarker = var1;
      this.startMarkerProxy = null;
      this.markerGroup = null;
   }

   public Marker getMiddleMarker() {
      return this.middleMarker;
   }

   public void setMiddleMarker(Marker var1) {
      this.middleMarker = var1;
      this.middleMarkerProxies = null;
      this.markerGroup = null;
   }

   public Marker getEndMarker() {
      return this.endMarker;
   }

   public void setEndMarker(Marker var1) {
      this.endMarker = var1;
      this.endMarkerProxy = null;
      this.markerGroup = null;
   }

   protected void buildMarkerGroup() {
      if (this.startMarker != null && this.startMarkerProxy == null) {
         this.startMarkerProxy = this.buildStartMarkerProxy();
      }

      if (this.middleMarker != null && this.middleMarkerProxies == null) {
         this.middleMarkerProxies = this.buildMiddleMarkerProxies();
      }

      if (this.endMarker != null && this.endMarkerProxy == null) {
         this.endMarkerProxy = this.buildEndMarkerProxy();
      }

      CompositeGraphicsNode var1 = new CompositeGraphicsNode();
      List var2 = var1.getChildren();
      if (this.startMarkerProxy != null) {
         var2.add(this.startMarkerProxy);
      }

      if (this.middleMarkerProxies != null) {
         for(int var3 = 0; var3 < this.middleMarkerProxies.length; ++var3) {
            var2.add(this.middleMarkerProxies[var3]);
         }
      }

      if (this.endMarkerProxy != null) {
         var2.add(this.endMarkerProxy);
      }

      this.markerGroup = var1;
   }

   protected ProxyGraphicsNode buildStartMarkerProxy() {
      ExtendedPathIterator var1 = this.getExtShape().getExtendedPathIterator();
      double[] var2 = new double[7];
      boolean var3 = false;
      if (var1.isDone()) {
         return null;
      } else {
         int var9 = var1.currentSegment(var2);
         if (var9 != 0) {
            return null;
         } else {
            var1.next();
            Point2D.Double var4 = new Point2D.Double(var2[0], var2[1]);
            double var5 = this.startMarker.getOrient();
            if (Double.isNaN(var5) && !var1.isDone()) {
               double[] var7 = new double[7];
               boolean var8 = false;
               int var11 = var1.currentSegment(var7);
               if (var11 == 4) {
                  var11 = 1;
                  var7[0] = var2[0];
                  var7[1] = var2[1];
               }

               var5 = this.computeRotation((double[])null, 0, var2, var9, var7, var11);
            }

            AffineTransform var10 = this.computeMarkerTransform(this.startMarker, var4, var5);
            ProxyGraphicsNode var12 = new ProxyGraphicsNode();
            var12.setSource(this.startMarker.getMarkerNode());
            var12.setTransform(var10);
            return var12;
         }
      }
   }

   protected ProxyGraphicsNode buildEndMarkerProxy() {
      ExtendedPathIterator var1 = this.getExtShape().getExtendedPathIterator();
      int var2 = 0;
      if (var1.isDone()) {
         return null;
      } else {
         double[] var3 = new double[7];
         double[] var4 = new double[2];
         boolean var5 = false;
         int var16 = var1.currentSegment(var3);
         if (var16 != 0) {
            return null;
         } else {
            ++var2;
            var4[0] = var3[0];
            var4[1] = var3[1];
            var1.next();
            double[] var6 = new double[7];
            double[] var7 = new double[]{var3[0], var3[1], var3[2], var3[3], var3[4], var3[5], var3[6]};
            Object var8 = null;
            int var9 = var16;

            int var10;
            for(var10 = 0; !var1.isDone(); ++var2) {
               double[] var17 = var6;
               var6 = var7;
               var7 = var17;
               var10 = var9;
               var9 = var1.currentSegment(var17);
               if (var9 == 0) {
                  var4[0] = var17[0];
                  var4[1] = var17[1];
               } else if (var9 == 4) {
                  var9 = 1;
                  var17[0] = var4[0];
                  var17[1] = var4[1];
               }

               var1.next();
            }

            if (var2 < 2) {
               return null;
            } else {
               Point2D var11 = this.getSegmentTerminatingPoint(var7, var9);
               double var12 = this.endMarker.getOrient();
               if (Double.isNaN(var12)) {
                  var12 = this.computeRotation(var6, var10, var7, var9, (double[])null, 0);
               }

               AffineTransform var14 = this.computeMarkerTransform(this.endMarker, var11, var12);
               ProxyGraphicsNode var15 = new ProxyGraphicsNode();
               var15.setSource(this.endMarker.getMarkerNode());
               var15.setTransform(var14);
               return var15;
            }
         }
      }
   }

   protected ProxyGraphicsNode[] buildMiddleMarkerProxies() {
      ExtendedPathIterator var1 = this.getExtShape().getExtendedPathIterator();
      double[] var2 = new double[7];
      double[] var3 = new double[7];
      double[] var4 = new double[7];
      Object var5 = null;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      if (var1.isDone()) {
         return null;
      } else {
         int var13 = var1.currentSegment(var2);
         double[] var9 = new double[2];
         if (var13 != 0) {
            return null;
         } else {
            var9[0] = var2[0];
            var9[1] = var2[1];
            var1.next();
            if (var1.isDone()) {
               return null;
            } else {
               int var14 = var1.currentSegment(var3);
               if (var14 == 0) {
                  var9[0] = var3[0];
                  var9[1] = var3[1];
               } else if (var14 == 4) {
                  var14 = 1;
                  var3[0] = var9[0];
                  var3[1] = var9[1];
               }

               var1.next();
               ArrayList var10 = new ArrayList();

               while(!var1.isDone()) {
                  int var15 = var1.currentSegment(var4);
                  if (var15 == 0) {
                     var9[0] = var4[0];
                     var9[1] = var4[1];
                  } else if (var15 == 4) {
                     var15 = 1;
                     var4[0] = var9[0];
                     var4[1] = var9[1];
                  }

                  var10.add(this.createMiddleMarker(var2, var13, var3, var14, var4, var15));
                  double[] var12 = var2;
                  var2 = var3;
                  var13 = var14;
                  var3 = var4;
                  var14 = var15;
                  var4 = var12;
                  var1.next();
               }

               ProxyGraphicsNode[] var11 = new ProxyGraphicsNode[var10.size()];
               var10.toArray(var11);
               return var11;
            }
         }
      }
   }

   private ProxyGraphicsNode createMiddleMarker(double[] var1, int var2, double[] var3, int var4, double[] var5, int var6) {
      Point2D var7 = this.getSegmentTerminatingPoint(var3, var4);
      double var8 = this.middleMarker.getOrient();
      if (Double.isNaN(var8)) {
         var8 = this.computeRotation(var1, var2, var3, var4, var5, var6);
      }

      AffineTransform var10 = this.computeMarkerTransform(this.middleMarker, var7, var8);
      ProxyGraphicsNode var11 = new ProxyGraphicsNode();
      var11.setSource(this.middleMarker.getMarkerNode());
      var11.setTransform(var10);
      return var11;
   }

   private double computeRotation(double[] var1, int var2, double[] var3, int var4, double[] var5, int var6) {
      double[] var7 = this.computeInSlope(var1, var2, var3, var4);
      double[] var8 = this.computeOutSlope(var3, var4, var5, var6);
      if (var7 == null) {
         var7 = var8;
      }

      if (var8 == null) {
         var8 = var7;
      }

      if (var7 == null) {
         return 0.0;
      } else {
         double var9 = var7[0] + var8[0];
         double var11 = var7[1] + var8[1];
         return var9 == 0.0 && var11 == 0.0 ? Math.toDegrees(Math.atan2(var7[1], var7[0])) + 90.0 : Math.toDegrees(Math.atan2(var11, var9));
      }
   }

   private double[] computeInSlope(double[] var1, int var2, double[] var3, int var4) {
      Point2D var5 = this.getSegmentTerminatingPoint(var3, var4);
      double var6 = 0.0;
      double var8 = 0.0;
      Point2D var10;
      switch (var4) {
         case 0:
         default:
            return null;
         case 1:
            var10 = this.getSegmentTerminatingPoint(var1, var2);
            var6 = var5.getX() - var10.getX();
            var8 = var5.getY() - var10.getY();
            break;
         case 2:
            var6 = var5.getX() - var3[0];
            var8 = var5.getY() - var3[1];
            break;
         case 3:
            var6 = var5.getX() - var3[2];
            var8 = var5.getY() - var3[3];
            break;
         case 4:
            throw new Error("should not have SEG_CLOSE here");
         case 4321:
            var10 = this.getSegmentTerminatingPoint(var1, var2);
            boolean var11 = var3[3] != 0.0;
            boolean var12 = var3[4] != 0.0;
            Arc2D var13 = ExtendedGeneralPath.computeArc(var10.getX(), var10.getY(), var3[0], var3[1], var3[2], var11, var12, var3[5], var3[6]);
            double var14 = var13.getAngleStart() + var13.getAngleExtent();
            var14 = Math.toRadians(var14);
            var6 = -var13.getWidth() / 2.0 * Math.sin(var14);
            var8 = var13.getHeight() / 2.0 * Math.cos(var14);
            if (var3[2] != 0.0) {
               double var16 = Math.toRadians(-var3[2]);
               double var18 = Math.sin(var16);
               double var20 = Math.cos(var16);
               double var22 = var6 * var20 - var8 * var18;
               double var24 = var6 * var18 + var8 * var20;
               var6 = var22;
               var8 = var24;
            }

            if (var12) {
               var6 = -var6;
            } else {
               var8 = -var8;
            }
      }

      return var6 == 0.0 && var8 == 0.0 ? null : this.normalize(new double[]{var6, var8});
   }

   private double[] computeOutSlope(double[] var1, int var2, double[] var3, int var4) {
      Point2D var5 = this.getSegmentTerminatingPoint(var1, var2);
      double var6 = 0.0;
      double var8 = 0.0;
      switch (var4) {
         case 0:
         default:
            return null;
         case 1:
         case 2:
         case 3:
            var6 = var3[0] - var5.getX();
            var8 = var3[1] - var5.getY();
         case 4:
            break;
         case 4321:
            boolean var10 = var3[3] != 0.0;
            boolean var11 = var3[4] != 0.0;
            Arc2D var12 = ExtendedGeneralPath.computeArc(var5.getX(), var5.getY(), var3[0], var3[1], var3[2], var10, var11, var3[5], var3[6]);
            double var13 = var12.getAngleStart();
            var13 = Math.toRadians(var13);
            var6 = -var12.getWidth() / 2.0 * Math.sin(var13);
            var8 = var12.getHeight() / 2.0 * Math.cos(var13);
            if (var3[2] != 0.0) {
               double var15 = Math.toRadians(-var3[2]);
               double var17 = Math.sin(var15);
               double var19 = Math.cos(var15);
               double var21 = var6 * var19 - var8 * var17;
               double var23 = var6 * var17 + var8 * var19;
               var6 = var21;
               var8 = var23;
            }

            if (var11) {
               var6 = -var6;
            } else {
               var8 = -var8;
            }
      }

      return var6 == 0.0 && var8 == 0.0 ? null : this.normalize(new double[]{var6, var8});
   }

   public double[] normalize(double[] var1) {
      double var2 = Math.sqrt(var1[0] * var1[0] + var1[1] * var1[1]);
      var1[0] /= var2;
      var1[1] /= var2;
      return var1;
   }

   private AffineTransform computeMarkerTransform(Marker var1, Point2D var2, double var3) {
      Point2D var5 = var1.getRef();
      AffineTransform var6 = new AffineTransform();
      var6.translate(var2.getX() - var5.getX(), var2.getY() - var5.getY());
      if (!Double.isNaN(var3)) {
         var6.rotate(Math.toRadians(var3), var5.getX(), var5.getY());
      }

      return var6;
   }

   protected Point2D getSegmentTerminatingPoint(double[] var1, int var2) {
      switch (var2) {
         case 0:
            return new Point2D.Double(var1[0], var1[1]);
         case 1:
            return new Point2D.Double(var1[0], var1[1]);
         case 2:
            return new Point2D.Double(var1[2], var1[3]);
         case 3:
            return new Point2D.Double(var1[4], var1[5]);
         case 4:
         default:
            throw new Error("invalid segmentType:" + var2);
         case 4321:
            return new Point2D.Double(var1[5], var1[6]);
      }
   }
}
