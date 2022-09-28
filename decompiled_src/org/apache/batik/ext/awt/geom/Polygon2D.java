package org.apache.batik.ext.awt.geom;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Polygon2D implements Shape, Cloneable, Serializable {
   public int npoints;
   public float[] xpoints;
   public float[] ypoints;
   protected Rectangle2D bounds;
   private GeneralPath path;
   private GeneralPath closedPath;

   public Polygon2D() {
      this.xpoints = new float[4];
      this.ypoints = new float[4];
   }

   public Polygon2D(Rectangle2D var1) {
      if (var1 == null) {
         throw new IndexOutOfBoundsException("null Rectangle");
      } else {
         this.npoints = 4;
         this.xpoints = new float[4];
         this.ypoints = new float[4];
         this.xpoints[0] = (float)var1.getMinX();
         this.ypoints[0] = (float)var1.getMinY();
         this.xpoints[1] = (float)var1.getMaxX();
         this.ypoints[1] = (float)var1.getMinY();
         this.xpoints[2] = (float)var1.getMaxX();
         this.ypoints[2] = (float)var1.getMaxY();
         this.xpoints[3] = (float)var1.getMinX();
         this.ypoints[3] = (float)var1.getMaxY();
         this.calculatePath();
      }
   }

   public Polygon2D(Polygon var1) {
      if (var1 == null) {
         throw new IndexOutOfBoundsException("null Polygon");
      } else {
         this.npoints = var1.npoints;
         this.xpoints = new float[var1.npoints];
         this.ypoints = new float[var1.npoints];

         for(int var2 = 0; var2 < var1.npoints; ++var2) {
            this.xpoints[var2] = (float)var1.xpoints[var2];
            this.ypoints[var2] = (float)var1.ypoints[var2];
         }

         this.calculatePath();
      }
   }

   public Polygon2D(float[] var1, float[] var2, int var3) {
      if (var3 <= var1.length && var3 <= var2.length) {
         this.npoints = var3;
         this.xpoints = new float[var3];
         this.ypoints = new float[var3];
         System.arraycopy(var1, 0, this.xpoints, 0, var3);
         System.arraycopy(var2, 0, this.ypoints, 0, var3);
         this.calculatePath();
      } else {
         throw new IndexOutOfBoundsException("npoints > xpoints.length || npoints > ypoints.length");
      }
   }

   public Polygon2D(int[] var1, int[] var2, int var3) {
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

   public void reset() {
      this.npoints = 0;
      this.bounds = null;
      this.path = new GeneralPath();
      this.closedPath = null;
   }

   public Object clone() {
      Polygon2D var1 = new Polygon2D();

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

   public Polyline2D getPolyline2D() {
      Polyline2D var1 = new Polyline2D(this.xpoints, this.ypoints, this.npoints);
      var1.addPoint(this.xpoints[0], this.ypoints[0]);
      return var1;
   }

   public Polygon getPolygon() {
      int[] var1 = new int[this.npoints];
      int[] var2 = new int[this.npoints];

      for(int var3 = 0; var3 < this.npoints; ++var3) {
         var1[var3] = (int)this.xpoints[var3];
         var2[var3] = (int)this.ypoints[var3];
      }

      return new Polygon(var1, var2, this.npoints);
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

   public boolean contains(Point var1) {
      return this.contains(var1.x, var1.y);
   }

   public boolean contains(int var1, int var2) {
      return this.contains((double)var1, (double)var2);
   }

   public Rectangle2D getBounds2D() {
      return this.bounds;
   }

   public Rectangle getBounds() {
      return this.bounds == null ? null : this.bounds.getBounds();
   }

   public boolean contains(double var1, double var3) {
      if (this.npoints > 2 && this.bounds.contains(var1, var3)) {
         this.updateComputingPath();
         return this.closedPath.contains(var1, var3);
      } else {
         return false;
      }
   }

   private void updateComputingPath() {
      if (this.npoints >= 1 && this.closedPath == null) {
         this.closedPath = (GeneralPath)this.path.clone();
         this.closedPath.closePath();
      }

   }

   public boolean contains(Point2D var1) {
      return this.contains(var1.getX(), var1.getY());
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
      if (this.npoints > 0 && this.bounds.intersects(var1, var3, var5, var7)) {
         this.updateComputingPath();
         return this.closedPath.contains(var1, var3, var5, var7);
      } else {
         return false;
      }
   }

   public boolean contains(Rectangle2D var1) {
      return this.contains(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
   }

   public PathIterator getPathIterator(AffineTransform var1) {
      this.updateComputingPath();
      return this.closedPath == null ? null : this.closedPath.getPathIterator(var1);
   }

   public PathIterator getPathIterator(AffineTransform var1, double var2) {
      return this.getPathIterator(var1);
   }
}
