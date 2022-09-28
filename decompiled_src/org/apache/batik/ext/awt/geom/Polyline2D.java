package org.apache.batik.ext.awt.geom;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Polyline2D implements Shape, Cloneable, Serializable {
   private static final float ASSUME_ZERO = 0.001F;
   public int npoints;
   public float[] xpoints;
   public float[] ypoints;
   protected Rectangle2D bounds;
   private GeneralPath path;
   private GeneralPath closedPath;

   public Polyline2D() {
      this.xpoints = new float[4];
      this.ypoints = new float[4];
   }

   public Polyline2D(float[] var1, float[] var2, int var3) {
      if (var3 <= var1.length && var3 <= var2.length) {
         this.npoints = var3;
         this.xpoints = new float[var3 + 1];
         this.ypoints = new float[var3 + 1];
         System.arraycopy(var1, 0, this.xpoints, 0, var3);
         System.arraycopy(var2, 0, this.ypoints, 0, var3);
         this.calculatePath();
      } else {
         throw new IndexOutOfBoundsException("npoints > xpoints.length || npoints > ypoints.length");
      }
   }

   public Polyline2D(int[] var1, int[] var2, int var3) {
      if (var3 <= var1.length && var3 <= var2.length) {
         this.npoints = var3;
         this.xpoints = new float[var3];
         this.ypoints = new float[var3];

         for(int var4 = 0; var4 < var3; ++var4) {
            this.xpoints[var4] = (float)var1[var4];
            this.ypoints[var4] = (float)var2[var4];
         }

         this.calculatePath();
      } else {
         throw new IndexOutOfBoundsException("npoints > xpoints.length || npoints > ypoints.length");
      }
   }

   public Polyline2D(Line2D var1) {
      this.npoints = 2;
      this.xpoints = new float[2];
      this.ypoints = new float[2];
      this.xpoints[0] = (float)var1.getX1();
      this.xpoints[1] = (float)var1.getX2();
      this.ypoints[0] = (float)var1.getY1();
      this.ypoints[1] = (float)var1.getY2();
      this.calculatePath();
   }

   public void reset() {
      this.npoints = 0;
      this.bounds = null;
      this.path = new GeneralPath();
      this.closedPath = null;
   }

   public Object clone() {
      Polyline2D var1 = new Polyline2D();

      for(int var2 = 0; var2 < this.npoints; ++var2) {
         var1.addPoint(this.xpoints[var2], this.ypoints[var2]);
      }

      return var1;
   }

   private void calculatePath() {
      this.path = new GeneralPath();
      this.path.moveTo(this.xpoints[0], this.ypoints[0]);

      for(int var1 = 1; var1 < this.npoints; ++var1) {
         this.path.lineTo(this.xpoints[var1], this.ypoints[var1]);
      }

      this.bounds = this.path.getBounds2D();
      this.closedPath = null;
   }

   private void updatePath(float var1, float var2) {
      this.closedPath = null;
      if (this.path == null) {
         this.path = new GeneralPath(0);
         this.path.moveTo(var1, var2);
         this.bounds = new Rectangle2D.Float(var1, var2, 0.0F, 0.0F);
      } else {
         this.path.lineTo(var1, var2);
         float var3 = (float)this.bounds.getMaxX();
         float var4 = (float)this.bounds.getMaxY();
         float var5 = (float)this.bounds.getMinX();
         float var6 = (float)this.bounds.getMinY();
         if (var1 < var5) {
            var5 = var1;
         } else if (var1 > var3) {
            var3 = var1;
         }

         if (var2 < var6) {
            var6 = var2;
         } else if (var2 > var4) {
            var4 = var2;
         }

         this.bounds = new Rectangle2D.Float(var5, var6, var3 - var5, var4 - var6);
      }

   }

   public void addPoint(Point2D var1) {
      this.addPoint((float)var1.getX(), (float)var1.getY());
   }

   public void addPoint(float var1, float var2) {
      if (this.npoints == this.xpoints.length) {
         float[] var3 = new float[this.npoints * 2];
         System.arraycopy(this.xpoints, 0, var3, 0, this.npoints);
         this.xpoints = var3;
         var3 = new float[this.npoints * 2];
         System.arraycopy(this.ypoints, 0, var3, 0, this.npoints);
         this.ypoints = var3;
      }

      this.xpoints[this.npoints] = var1;
      this.ypoints[this.npoints] = var2;
      ++this.npoints;
      this.updatePath(var1, var2);
   }

   public Rectangle getBounds() {
      return this.bounds == null ? null : this.bounds.getBounds();
   }

   private void updateComputingPath() {
      if (this.npoints >= 1 && this.closedPath == null) {
         this.closedPath = (GeneralPath)this.path.clone();
         this.closedPath.closePath();
      }

   }

   public boolean contains(Point var1) {
      return false;
   }

   public boolean contains(double var1, double var3) {
      return false;
   }

   public boolean contains(int var1, int var2) {
      return false;
   }

   public Rectangle2D getBounds2D() {
      return this.bounds;
   }

   public boolean contains(Point2D var1) {
      return false;
   }

   public boolean intersects(double var1, double var3, double var5, double var7) {
      if (this.npoints > 0 && this.bounds.intersects(var1, var3, var5, var7)) {
         this.updateComputingPath();
         return this.closedPath.intersects(var1, var3, var5, var7);
      } else {
         return false;
      }
   }

   public boolean intersects(Rectangle2D var1) {
      return this.intersects(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
   }

   public boolean contains(double var1, double var3, double var5, double var7) {
      return false;
   }

   public boolean contains(Rectangle2D var1) {
      return false;
   }

   public PathIterator getPathIterator(AffineTransform var1) {
      return this.path == null ? null : this.path.getPathIterator(var1);
   }

   public Polygon2D getPolygon2D() {
      Polygon2D var1 = new Polygon2D();

      for(int var2 = 0; var2 < this.npoints - 1; ++var2) {
         var1.addPoint(this.xpoints[var2], this.ypoints[var2]);
      }

      Point2D.Double var4 = new Point2D.Double((double)this.xpoints[0], (double)this.ypoints[0]);
      Point2D.Double var3 = new Point2D.Double((double)this.xpoints[this.npoints - 1], (double)this.ypoints[this.npoints - 1]);
      if (var4.distance(var3) > 0.0010000000474974513) {
         var1.addPoint(this.xpoints[this.npoints - 1], this.ypoints[this.npoints - 1]);
      }

      return var1;
   }

   public PathIterator getPathIterator(AffineTransform var1, double var2) {
      return this.path.getPathIterator(var1);
   }
}
