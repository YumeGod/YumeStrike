package org.apache.batik.ext.awt.geom;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PathLength {
   protected Shape path;
   protected List segments;
   protected int[] segmentIndexes;
   protected float pathLength;
   protected boolean initialised;

   public PathLength(Shape var1) {
      this.setPath(var1);
   }

   public Shape getPath() {
      return this.path;
   }

   public void setPath(Shape var1) {
      this.path = var1;
      this.initialised = false;
   }

   public float lengthOfPath() {
      if (!this.initialised) {
         this.initialise();
      }

      return this.pathLength;
   }

   protected void initialise() {
      this.pathLength = 0.0F;
      PathIterator var1 = this.path.getPathIterator(new AffineTransform());
      SingleSegmentPathIterator var2 = new SingleSegmentPathIterator();
      this.segments = new ArrayList(20);
      ArrayList var3 = new ArrayList(20);
      int var4 = 0;
      int var5 = -1;
      float var6 = 0.0F;
      float var7 = 0.0F;
      float var8 = 0.0F;
      float var9 = 0.0F;
      float[] var10 = new float[6];
      this.segments.add(new PathSegment(0, 0.0F, 0.0F, 0.0F, var5));

      while(true) {
         label37:
         while(!var1.isDone()) {
            ++var5;
            var3.add(new Integer(var4));
            int var11 = var1.currentSegment(var10);
            switch (var11) {
               case 0:
                  this.segments.add(new PathSegment(var11, var10[0], var10[1], this.pathLength, var5));
                  var8 = var10[0];
                  var9 = var10[1];
                  var6 = var8;
                  var7 = var9;
                  ++var4;
                  var1.next();
                  break;
               case 1:
                  this.pathLength = (float)((double)this.pathLength + Point2D.distance((double)var8, (double)var9, (double)var10[0], (double)var10[1]));
                  this.segments.add(new PathSegment(var11, var10[0], var10[1], this.pathLength, var5));
                  var8 = var10[0];
                  var9 = var10[1];
                  ++var4;
                  var1.next();
                  break;
               case 2:
               case 3:
               default:
                  var2.setPathIterator(var1, (double)var8, (double)var9);
                  FlatteningPathIterator var12 = new FlatteningPathIterator(var2, 0.009999999776482582);

                  while(true) {
                     if (var12.isDone()) {
                        continue label37;
                     }

                     var11 = var12.currentSegment(var10);
                     if (var11 == 1) {
                        this.pathLength = (float)((double)this.pathLength + Point2D.distance((double)var8, (double)var9, (double)var10[0], (double)var10[1]));
                        this.segments.add(new PathSegment(var11, var10[0], var10[1], this.pathLength, var5));
                        var8 = var10[0];
                        var9 = var10[1];
                        ++var4;
                     }

                     var12.next();
                  }
               case 4:
                  this.pathLength = (float)((double)this.pathLength + Point2D.distance((double)var8, (double)var9, (double)var6, (double)var7));
                  this.segments.add(new PathSegment(1, var6, var7, this.pathLength, var5));
                  var8 = var6;
                  var9 = var7;
                  ++var4;
                  var1.next();
            }
         }

         this.segmentIndexes = new int[var3.size()];

         for(int var13 = 0; var13 < this.segmentIndexes.length; ++var13) {
            this.segmentIndexes[var13] = (Integer)var3.get(var13);
         }

         this.initialised = true;
         return;
      }
   }

   public int getNumberOfSegments() {
      if (!this.initialised) {
         this.initialise();
      }

      return this.segmentIndexes.length;
   }

   public float getLengthAtSegment(int var1) {
      if (!this.initialised) {
         this.initialise();
      }

      if (var1 <= 0) {
         return 0.0F;
      } else if (var1 >= this.segmentIndexes.length) {
         return this.pathLength;
      } else {
         PathSegment var2 = (PathSegment)this.segments.get(this.segmentIndexes[var1]);
         return var2.getLength();
      }
   }

   public int segmentAtLength(float var1) {
      int var2 = this.findUpperIndex(var1);
      if (var2 == -1) {
         return -1;
      } else {
         PathSegment var3;
         if (var2 == 0) {
            var3 = (PathSegment)this.segments.get(var2);
            return var3.getIndex();
         } else {
            var3 = (PathSegment)this.segments.get(var2 - 1);
            return var3.getIndex();
         }
      }
   }

   public Point2D pointAtLength(int var1, float var2) {
      if (!this.initialised) {
         this.initialise();
      }

      if (var1 >= 0 && var1 < this.segmentIndexes.length) {
         PathSegment var3 = (PathSegment)this.segments.get(this.segmentIndexes[var1]);
         float var4 = var3.getLength();
         float var5;
         if (var1 == this.segmentIndexes.length - 1) {
            var5 = this.pathLength;
         } else {
            var3 = (PathSegment)this.segments.get(this.segmentIndexes[var1 + 1]);
            var5 = var3.getLength();
         }

         return this.pointAtLength(var4 + (var5 - var4) * var2);
      } else {
         return null;
      }
   }

   public Point2D pointAtLength(float var1) {
      int var2 = this.findUpperIndex(var1);
      if (var2 == -1) {
         return null;
      } else {
         PathSegment var3 = (PathSegment)this.segments.get(var2);
         if (var2 == 0) {
            return new Point2D.Float(var3.getX(), var3.getY());
         } else {
            PathSegment var4 = (PathSegment)this.segments.get(var2 - 1);
            float var5 = var1 - var4.getLength();
            double var6 = Math.atan2((double)(var3.getY() - var4.getY()), (double)(var3.getX() - var4.getX()));
            float var8 = (float)((double)var4.getX() + (double)var5 * Math.cos(var6));
            float var9 = (float)((double)var4.getY() + (double)var5 * Math.sin(var6));
            return new Point2D.Float(var8, var9);
         }
      }
   }

   public float angleAtLength(int var1, float var2) {
      if (!this.initialised) {
         this.initialise();
      }

      if (var1 >= 0 && var1 < this.segmentIndexes.length) {
         PathSegment var3 = (PathSegment)this.segments.get(this.segmentIndexes[var1]);
         float var4 = var3.getLength();
         float var5;
         if (var1 == this.segmentIndexes.length - 1) {
            var5 = this.pathLength;
         } else {
            var3 = (PathSegment)this.segments.get(this.segmentIndexes[var1 + 1]);
            var5 = var3.getLength();
         }

         return this.angleAtLength(var4 + (var5 - var4) * var2);
      } else {
         return 0.0F;
      }
   }

   public float angleAtLength(float var1) {
      int var2 = this.findUpperIndex(var1);
      if (var2 == -1) {
         return 0.0F;
      } else {
         PathSegment var3 = (PathSegment)this.segments.get(var2);
         if (var2 == 0) {
            var2 = 1;
         }

         PathSegment var4 = (PathSegment)this.segments.get(var2 - 1);
         return (float)Math.atan2((double)(var3.getY() - var4.getY()), (double)(var3.getX() - var4.getX()));
      }
   }

   public int findUpperIndex(float var1) {
      if (!this.initialised) {
         this.initialise();
      }

      if (!(var1 < 0.0F) && !(var1 > this.pathLength)) {
         int var2 = 0;
         int var3 = this.segments.size() - 1;

         int var4;
         while(var2 != var3) {
            var4 = var2 + var3 >> 1;
            PathSegment var5 = (PathSegment)this.segments.get(var4);
            if (var5.getLength() >= var1) {
               var3 = var4;
            } else {
               var2 = var4 + 1;
            }
         }

         while(true) {
            PathSegment var8 = (PathSegment)this.segments.get(var3);
            if (var8.getSegType() != 0 || var3 == this.segments.size() - 1) {
               var4 = -1;
               int var9 = 0;

               for(int var6 = this.segments.size(); var4 <= 0 && var9 < var6; ++var9) {
                  PathSegment var7 = (PathSegment)this.segments.get(var9);
                  if (var7.getLength() >= var1 && var7.getSegType() != 0) {
                     var4 = var9;
                  }
               }

               return var4;
            }

            ++var3;
         }
      } else {
         return -1;
      }
   }

   protected static class PathSegment {
      protected final int segType;
      protected float x;
      protected float y;
      protected float length;
      protected int index;

      PathSegment(int var1, float var2, float var3, float var4, int var5) {
         this.segType = var1;
         this.x = var2;
         this.y = var3;
         this.length = var4;
         this.index = var5;
      }

      public int getSegType() {
         return this.segType;
      }

      public float getX() {
         return this.x;
      }

      public void setX(float var1) {
         this.x = var1;
      }

      public float getY() {
         return this.y;
      }

      public void setY(float var1) {
         this.y = var1;
      }

      public float getLength() {
         return this.length;
      }

      public void setLength(float var1) {
         this.length = var1;
      }

      public int getIndex() {
         return this.index;
      }

      public void setIndex(int var1) {
         this.index = var1;
      }
   }

   protected static class SingleSegmentPathIterator implements PathIterator {
      protected PathIterator it;
      protected boolean done;
      protected boolean moveDone;
      protected double x;
      protected double y;

      public void setPathIterator(PathIterator var1, double var2, double var4) {
         this.it = var1;
         this.x = var2;
         this.y = var4;
         this.done = false;
         this.moveDone = false;
      }

      public int currentSegment(double[] var1) {
         int var2 = this.it.currentSegment(var1);
         if (!this.moveDone) {
            var1[0] = this.x;
            var1[1] = this.y;
            return 0;
         } else {
            return var2;
         }
      }

      public int currentSegment(float[] var1) {
         int var2 = this.it.currentSegment(var1);
         if (!this.moveDone) {
            var1[0] = (float)this.x;
            var1[1] = (float)this.y;
            return 0;
         } else {
            return var2;
         }
      }

      public int getWindingRule() {
         return this.it.getWindingRule();
      }

      public boolean isDone() {
         return this.done || this.it.isDone();
      }

      public void next() {
         if (!this.done) {
            if (!this.moveDone) {
               this.moveDone = true;
            } else {
               this.it.next();
               this.done = true;
            }
         }

      }
   }
}
