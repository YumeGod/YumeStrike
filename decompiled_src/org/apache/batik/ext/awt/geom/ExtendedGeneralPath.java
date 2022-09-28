package org.apache.batik.ext.awt.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

public class ExtendedGeneralPath implements ExtendedShape, Cloneable {
   protected GeneralPath path;
   int numVals;
   int numSeg;
   float[] values;
   int[] types;
   float mx;
   float my;
   float cx;
   float cy;

   public ExtendedGeneralPath() {
      this.numVals = 0;
      this.numSeg = 0;
      this.values = null;
      this.types = null;
      this.path = new GeneralPath();
   }

   public ExtendedGeneralPath(int var1) {
      this.numVals = 0;
      this.numSeg = 0;
      this.values = null;
      this.types = null;
      this.path = new GeneralPath(var1);
   }

   public ExtendedGeneralPath(int var1, int var2) {
      this.numVals = 0;
      this.numSeg = 0;
      this.values = null;
      this.types = null;
      this.path = new GeneralPath(var1, var2);
   }

   public ExtendedGeneralPath(Shape var1) {
      this();
      this.append(var1, false);
   }

   public synchronized void arcTo(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) {
      if (var1 != 0.0F && var2 != 0.0F) {
         this.checkMoveTo();
         double var8 = (double)this.cx;
         double var10 = (double)this.cy;
         if (var8 != (double)var6 || var10 != (double)var7) {
            Arc2D var12 = computeArc(var8, var10, (double)var1, (double)var2, (double)var3, var4, var5, (double)var6, (double)var7);
            if (var12 != null) {
               AffineTransform var13 = AffineTransform.getRotateInstance(Math.toRadians((double)var3), var12.getCenterX(), var12.getCenterY());
               Shape var14 = var13.createTransformedShape(var12);
               this.path.append(var14, true);
               this.makeRoom(7);
               this.types[this.numSeg++] = 4321;
               this.values[this.numVals++] = var1;
               this.values[this.numVals++] = var2;
               this.values[this.numVals++] = var3;
               this.values[this.numVals++] = var4 ? 1.0F : 0.0F;
               this.values[this.numVals++] = var5 ? 1.0F : 0.0F;
               this.cx = this.values[this.numVals++] = var6;
               this.cy = this.values[this.numVals++] = var7;
            }
         }
      } else {
         this.lineTo(var6, var7);
      }
   }

   public static Arc2D computeArc(double var0, double var2, double var4, double var6, double var8, boolean var10, boolean var11, double var12, double var14) {
      double var16 = (var0 - var12) / 2.0;
      double var18 = (var2 - var14) / 2.0;
      var8 = Math.toRadians(var8 % 360.0);
      double var20 = Math.cos(var8);
      double var22 = Math.sin(var8);
      double var24 = var20 * var16 + var22 * var18;
      double var26 = -var22 * var16 + var20 * var18;
      var4 = Math.abs(var4);
      var6 = Math.abs(var6);
      double var28 = var4 * var4;
      double var30 = var6 * var6;
      double var32 = var24 * var24;
      double var34 = var26 * var26;
      double var36 = var32 / var28 + var34 / var30;
      if (var36 > 1.0) {
         var4 = Math.sqrt(var36) * var4;
         var6 = Math.sqrt(var36) * var6;
         var28 = var4 * var4;
         var30 = var6 * var6;
      }

      double var38 = var10 == var11 ? -1.0 : 1.0;
      double var40 = (var28 * var30 - var28 * var34 - var30 * var32) / (var28 * var34 + var30 * var32);
      var40 = var40 < 0.0 ? 0.0 : var40;
      double var42 = var38 * Math.sqrt(var40);
      double var44 = var42 * (var4 * var26 / var6);
      double var46 = var42 * -(var6 * var24 / var4);
      double var48 = (var0 + var12) / 2.0;
      double var50 = (var2 + var14) / 2.0;
      double var52 = var48 + (var20 * var44 - var22 * var46);
      double var54 = var50 + var22 * var44 + var20 * var46;
      double var56 = (var24 - var44) / var4;
      double var58 = (var26 - var46) / var6;
      double var60 = (-var24 - var44) / var4;
      double var62 = (-var26 - var46) / var6;
      double var66 = Math.sqrt(var56 * var56 + var58 * var58);
      var38 = var58 < 0.0 ? -1.0 : 1.0;
      double var68 = Math.toDegrees(var38 * Math.acos(var56 / var66));
      var66 = Math.sqrt((var56 * var56 + var58 * var58) * (var60 * var60 + var62 * var62));
      double var64 = var56 * var60 + var58 * var62;
      var38 = var56 * var62 - var58 * var60 < 0.0 ? -1.0 : 1.0;
      double var70 = Math.toDegrees(var38 * Math.acos(var64 / var66));
      if (!var11 && var70 > 0.0) {
         var70 -= 360.0;
      } else if (var11 && var70 < 0.0) {
         var70 += 360.0;
      }

      var70 %= 360.0;
      var68 %= 360.0;
      Arc2D.Double var72 = new Arc2D.Double();
      var72.x = var52 - var4;
      var72.y = var54 - var6;
      var72.width = var4 * 2.0;
      var72.height = var6 * 2.0;
      var72.start = -var68;
      var72.extent = -var70;
      return var72;
   }

   public synchronized void moveTo(float var1, float var2) {
      this.makeRoom(2);
      this.types[this.numSeg++] = 0;
      this.cx = this.mx = this.values[this.numVals++] = var1;
      this.cy = this.my = this.values[this.numVals++] = var2;
   }

   public synchronized void lineTo(float var1, float var2) {
      this.checkMoveTo();
      this.path.lineTo(var1, var2);
      this.makeRoom(2);
      this.types[this.numSeg++] = 1;
      this.cx = this.values[this.numVals++] = var1;
      this.cy = this.values[this.numVals++] = var2;
   }

   public synchronized void quadTo(float var1, float var2, float var3, float var4) {
      this.checkMoveTo();
      this.path.quadTo(var1, var2, var3, var4);
      this.makeRoom(4);
      this.types[this.numSeg++] = 2;
      this.values[this.numVals++] = var1;
      this.values[this.numVals++] = var2;
      this.cx = this.values[this.numVals++] = var3;
      this.cy = this.values[this.numVals++] = var4;
   }

   public synchronized void curveTo(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.checkMoveTo();
      this.path.curveTo(var1, var2, var3, var4, var5, var6);
      this.makeRoom(6);
      this.types[this.numSeg++] = 3;
      this.values[this.numVals++] = var1;
      this.values[this.numVals++] = var2;
      this.values[this.numVals++] = var3;
      this.values[this.numVals++] = var4;
      this.cx = this.values[this.numVals++] = var5;
      this.cy = this.values[this.numVals++] = var6;
   }

   public synchronized void closePath() {
      if (this.numSeg == 0 || this.types[this.numSeg - 1] != 4) {
         if (this.numSeg != 0 && this.types[this.numSeg - 1] != 0) {
            this.path.closePath();
         }

         this.makeRoom(0);
         this.types[this.numSeg++] = 4;
         this.cx = this.mx;
         this.cy = this.my;
      }
   }

   protected void checkMoveTo() {
      if (this.numSeg != 0) {
         switch (this.types[this.numSeg - 1]) {
            case 0:
               this.path.moveTo(this.values[this.numVals - 2], this.values[this.numVals - 1]);
               break;
            case 4:
               if (this.numSeg == 1) {
                  return;
               }

               if (this.types[this.numSeg - 2] == 0) {
                  this.path.moveTo(this.values[this.numVals - 2], this.values[this.numVals - 1]);
               }
         }

      }
   }

   public void append(Shape var1, boolean var2) {
      this.append(var1.getPathIterator(new AffineTransform()), var2);
   }

   public void append(PathIterator var1, boolean var2) {
      double[] var3 = new double[6];

      while(!var1.isDone()) {
         Arrays.fill(var3, 0.0);
         int var4 = var1.currentSegment(var3);
         var1.next();
         if (var2 && this.numVals != 0) {
            if (var4 == 0) {
               double var5 = var3[0];
               double var7 = var3[1];
               if (var5 == (double)this.cx && var7 == (double)this.cy) {
                  if (var1.isDone()) {
                     break;
                  }

                  var4 = var1.currentSegment(var3);
                  var1.next();
               } else {
                  var4 = 1;
               }
            }

            var2 = false;
         }

         switch (var4) {
            case 0:
               this.moveTo((float)var3[0], (float)var3[1]);
               break;
            case 1:
               this.lineTo((float)var3[0], (float)var3[1]);
               break;
            case 2:
               this.quadTo((float)var3[0], (float)var3[1], (float)var3[2], (float)var3[3]);
               break;
            case 3:
               this.curveTo((float)var3[0], (float)var3[1], (float)var3[2], (float)var3[3], (float)var3[4], (float)var3[5]);
               break;
            case 4:
               this.closePath();
         }
      }

   }

   public void append(ExtendedPathIterator var1, boolean var2) {
      float[] var3 = new float[7];

      while(!var1.isDone()) {
         Arrays.fill(var3, 0.0F);
         int var4 = var1.currentSegment(var3);
         var1.next();
         if (var2 && this.numVals != 0) {
            if (var4 == 0) {
               float var5 = var3[0];
               float var6 = var3[1];
               if (var5 == this.cx && var6 == this.cy) {
                  if (var1.isDone()) {
                     break;
                  }

                  var4 = var1.currentSegment(var3);
                  var1.next();
               } else {
                  var4 = 1;
               }
            }

            var2 = false;
         }

         switch (var4) {
            case 0:
               this.moveTo(var3[0], var3[1]);
               break;
            case 1:
               this.lineTo(var3[0], var3[1]);
               break;
            case 2:
               this.quadTo(var3[0], var3[1], var3[2], var3[3]);
               break;
            case 3:
               this.curveTo(var3[0], var3[1], var3[2], var3[3], var3[4], var3[5]);
               break;
            case 4:
               this.closePath();
               break;
            case 4321:
               this.arcTo(var3[0], var3[1], var3[2], var3[3] != 0.0F, var3[4] != 0.0F, var3[5], var3[6]);
         }
      }

   }

   public synchronized int getWindingRule() {
      return this.path.getWindingRule();
   }

   public void setWindingRule(int var1) {
      this.path.setWindingRule(var1);
   }

   public synchronized Point2D getCurrentPoint() {
      return this.numVals == 0 ? null : new Point2D.Double((double)this.cx, (double)this.cy);
   }

   public synchronized void reset() {
      this.path.reset();
      this.numSeg = 0;
      this.numVals = 0;
      this.values = null;
      this.types = null;
   }

   public void transform(AffineTransform var1) {
      if (var1.getType() != 0) {
         throw new IllegalArgumentException("ExtendedGeneralPaths can not be transformed");
      }
   }

   public synchronized Shape createTransformedShape(AffineTransform var1) {
      return this.path.createTransformedShape(var1);
   }

   public synchronized Rectangle getBounds() {
      return this.path.getBounds();
   }

   public synchronized Rectangle2D getBounds2D() {
      return this.path.getBounds2D();
   }

   public boolean contains(double var1, double var3) {
      return this.path.contains(var1, var3);
   }

   public boolean contains(Point2D var1) {
      return this.path.contains(var1);
   }

   public boolean contains(double var1, double var3, double var5, double var7) {
      return this.path.contains(var1, var3, var5, var7);
   }

   public boolean contains(Rectangle2D var1) {
      return this.path.contains(var1);
   }

   public boolean intersects(double var1, double var3, double var5, double var7) {
      return this.path.intersects(var1, var3, var5, var7);
   }

   public boolean intersects(Rectangle2D var1) {
      return this.path.intersects(var1);
   }

   public PathIterator getPathIterator(AffineTransform var1) {
      return this.path.getPathIterator(var1);
   }

   public PathIterator getPathIterator(AffineTransform var1, double var2) {
      return this.path.getPathIterator(var1, var2);
   }

   public ExtendedPathIterator getExtendedPathIterator() {
      return new EPI();
   }

   public Object clone() {
      try {
         ExtendedGeneralPath var1 = (ExtendedGeneralPath)super.clone();
         var1.path = (GeneralPath)this.path.clone();
         if (this.values != null) {
            var1.values = new float[this.values.length];
            System.arraycopy(this.values, 0, var1.values, 0, this.values.length);
         }

         var1.numVals = this.numVals;
         if (this.types != null) {
            var1.types = new int[this.types.length];
            System.arraycopy(this.types, 0, var1.types, 0, this.types.length);
         }

         var1.numSeg = this.numSeg;
         return var1;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   private void makeRoom(int var1) {
      if (this.values == null) {
         this.values = new float[2 * var1];
         this.types = new int[2];
         this.numVals = 0;
         this.numSeg = 0;
      } else {
         int var2 = this.numVals + var1;
         if (var2 > this.values.length) {
            int var3 = this.values.length * 2;
            if (var3 < var2) {
               var3 = var2;
            }

            float[] var4 = new float[var3];
            System.arraycopy(this.values, 0, var4, 0, this.numVals);
            this.values = var4;
         }

         if (this.numSeg == this.types.length) {
            int[] var5 = new int[this.types.length * 2];
            System.arraycopy(this.types, 0, var5, 0, this.types.length);
            this.types = var5;
         }

      }
   }

   class EPI implements ExtendedPathIterator {
      int segNum = 0;
      int valsIdx = 0;

      public int currentSegment() {
         return ExtendedGeneralPath.this.types[this.segNum];
      }

      public int currentSegment(double[] var1) {
         int var2 = ExtendedGeneralPath.this.types[this.segNum];
         switch (var2) {
            case 0:
            case 1:
               var1[0] = (double)ExtendedGeneralPath.this.values[this.valsIdx];
               var1[1] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 1];
               break;
            case 2:
               var1[0] = (double)ExtendedGeneralPath.this.values[this.valsIdx];
               var1[1] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 1];
               var1[2] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 2];
               var1[3] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 3];
               break;
            case 3:
               var1[0] = (double)ExtendedGeneralPath.this.values[this.valsIdx];
               var1[1] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 1];
               var1[2] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 2];
               var1[3] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 3];
               var1[4] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 4];
               var1[5] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 5];
            case 4:
            default:
               break;
            case 4321:
               var1[0] = (double)ExtendedGeneralPath.this.values[this.valsIdx];
               var1[1] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 1];
               var1[2] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 2];
               var1[3] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 3];
               var1[4] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 4];
               var1[5] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 5];
               var1[6] = (double)ExtendedGeneralPath.this.values[this.valsIdx + 6];
         }

         return var2;
      }

      public int currentSegment(float[] var1) {
         int var2 = ExtendedGeneralPath.this.types[this.segNum];
         switch (var2) {
            case 0:
            case 1:
               var1[0] = ExtendedGeneralPath.this.values[this.valsIdx];
               var1[1] = ExtendedGeneralPath.this.values[this.valsIdx + 1];
               break;
            case 2:
               System.arraycopy(ExtendedGeneralPath.this.values, this.valsIdx, var1, 0, 4);
               break;
            case 3:
               System.arraycopy(ExtendedGeneralPath.this.values, this.valsIdx, var1, 0, 6);
            case 4:
            default:
               break;
            case 4321:
               System.arraycopy(ExtendedGeneralPath.this.values, this.valsIdx, var1, 0, 7);
         }

         return var2;
      }

      public int getWindingRule() {
         return ExtendedGeneralPath.this.path.getWindingRule();
      }

      public boolean isDone() {
         return this.segNum == ExtendedGeneralPath.this.numSeg;
      }

      public void next() {
         int var1 = ExtendedGeneralPath.this.types[this.segNum++];
         switch (var1) {
            case 0:
            case 1:
               this.valsIdx += 2;
               break;
            case 2:
               this.valsIdx += 4;
               break;
            case 3:
               this.valsIdx += 6;
            case 4:
            default:
               break;
            case 4321:
               this.valsIdx += 7;
         }

      }
   }
}
