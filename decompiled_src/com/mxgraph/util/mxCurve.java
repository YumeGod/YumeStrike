package com.mxgraph.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class mxCurve {
   protected Map points;
   protected double minXBounds = 1.0E7;
   protected double maxXBounds = 0.0;
   protected double minYBounds = 1.0E7;
   protected double maxYBounds = 0.0;
   protected Map intervals;
   protected Map curveLengths;
   public static String CORE_CURVE = "Center_curve";
   public static String LABEL_CURVE = "Label_curve";
   protected double labelBuffer;
   public List guidePoints;
   protected boolean valid;

   public void setLabelBuffer(double var1) {
      this.labelBuffer = var1;
   }

   public mxRectangle getBounds() {
      if (!this.valid) {
         this.createCoreCurve();
      }

      return new mxRectangle(this.minXBounds, this.minYBounds, this.maxXBounds - this.minXBounds, this.maxYBounds - this.minYBounds);
   }

   public mxCurve() {
      this.labelBuffer = mxConstants.DEFAULT_LABEL_BUFFER;
      this.guidePoints = new ArrayList();
      this.valid = false;
   }

   public mxCurve(List var1) {
      this.labelBuffer = mxConstants.DEFAULT_LABEL_BUFFER;
      this.guidePoints = new ArrayList();
      this.valid = false;
      this.guidePoints = var1;
   }

   protected int getLowerIndexOfSegment(String var1, double var2) {
      double[] var4 = this.getIntervals(var1);
      if (var4 == null) {
         return 0;
      } else {
         int var5 = var4.length;
         if (!(var2 <= 0.0) && var5 >= 2) {
            if (var2 >= 1.0) {
               return var5 - 2;
            } else {
               int var6 = (int)((double)var5 * var2);
               if (var6 >= var5) {
                  var6 = var5 - 1;
               }

               int var7 = -1;
               int var8 = var5;

               for(int var9 = 0; var9 < var5; ++var9) {
                  double var10 = var4[var6];
                  double var12 = 0.5;
                  if (var2 < var10) {
                     var8 = Math.min(var8, var6);
                     var12 = -0.5;
                  } else if (var2 > var10) {
                     var7 = Math.max(var7, var6);
                  } else if (var6 == 0) {
                     var7 = 0;
                     var8 = 1;
                  } else {
                     var7 = var6 - 1;
                     var8 = var6;
                  }

                  int var14 = var8 - var7;
                  if (var14 == 1) {
                     break;
                  }

                  var6 = (int)((double)var6 + (double)var14 * var12);
                  if (var6 == var7) {
                     var6 = var7 + 1;
                  }

                  if (var6 == var8) {
                     var6 = var8 - 1;
                  }
               }

               return var7 != var8 - 1 ? -1 : var7;
            }
         } else {
            return 0;
         }
      }
   }

   public mxLine getCurveParallel(String var1, double var2) {
      mxPoint[] var4 = this.getCurvePoints(var1);
      double[] var5 = this.getIntervals(var1);
      if (var4 != null && var4.length > 0 && var5 != null && var2 >= 0.0 && var2 <= 1.0) {
         if (var4.length == 1) {
            mxPoint var18 = var4[0];
            return new mxLine(new mxPoint(var18.getX(), var18.getY()), new mxPoint(1.0, 0.0));
         } else {
            int var6 = this.getLowerIndexOfSegment(var1, var2);
            mxPoint var7 = var4[var6];
            double var8 = var4[var6 + 1].getX() - var7.getX();
            double var10 = var4[var6 + 1].getY() - var7.getY();
            double var12 = (var2 - var5[var6]) / (var5[var6 + 1] - var5[var6]);
            double var14 = Math.sqrt(var8 * var8 + var10 * var10);
            mxPoint var16 = new mxPoint(var7.getX() + var8 * var12, var7.getY() + var10 * var12);
            mxPoint var17 = new mxPoint(var8 / var14, var10 / var14);
            return new mxLine(var16, var17);
         }
      } else {
         return new mxLine(new mxPoint(0.0, 0.0), new mxPoint(1.0, 0.0));
      }
   }

   public mxPoint[] getCurveSection(String var1, double var2, double var4) {
      mxPoint[] var6 = this.getCurvePoints(var1);
      double[] var7 = this.getIntervals(var1);
      if (var6 != null && var6.length > 0 && var7 != null && var2 >= 0.0 && var2 <= 1.0 && var4 >= 0.0 && var4 <= 1.0) {
         if (var6.length == 1) {
            mxPoint var24 = var6[0];
            return new mxPoint[]{new mxPoint(var24.getX(), var24.getY())};
         } else {
            int var8 = this.getLowerIndexOfSegment(var1, var2);
            mxPoint var9 = var6[var8];
            double var10 = var6[var8 + 1].getX() - var9.getX();
            double var12 = var6[var8 + 1].getY() - var9.getY();
            double var14 = (var2 - var7[var8]) / (var7[var8 + 1] - var7[var8]);
            Math.sqrt(var10 * var10 + var12 * var12);
            mxPoint var18 = new mxPoint(var9.getX() + var10 * var14, var9.getY() + var12 * var14);
            ArrayList var19 = new ArrayList();
            var19.add(var18);
            ++var8;

            mxPoint var22;
            for(double var20 = var7[var8]; var20 <= var4; var20 = var7[var8]) {
               var22 = var6[var8];
               var19.add(var22);
               ++var8;
            }

            if (var8 > 0 && var8 < var6.length && var4 > var7[var8 - 1]) {
               var9 = var6[var8 - 1];
               var10 = var6[var8].getX() - var9.getX();
               var12 = var6[var8].getY() - var9.getY();
               var14 = (var4 - var7[var8 - 1]) / (var7[var8] - var7[var8 - 1]);
               Math.sqrt(var10 * var10 + var12 * var12);
               var22 = new mxPoint(var9.getX() + var10 * var14, var9.getY() + var12 * var14);
               var19.add(var22);
            }

            mxPoint[] var23 = new mxPoint[var19.size()];
            return (mxPoint[])var19.toArray(var23);
         }
      } else {
         return null;
      }
   }

   public boolean intersectsRect(mxRectangle var1) {
      if (!this.getBounds().getRectangle().intersects(var1.getRectangle())) {
         return false;
      } else {
         mxPoint[] var2 = this.getCurvePoints(CORE_CURVE);
         if (var2 != null && var2.length > 1) {
            int var3;
            for(var3 = 1; var3 < var2.length; ++var3) {
               if (var1.contains(var2[var3].getX(), var2[var3].getY()) || var1.contains(var2[var3 - 1].getX(), var2[var3 - 1].getY())) {
                  return true;
               }
            }

            for(var3 = 1; var3 < var2.length; ++var3) {
               if (var1.intersectLine(var2[var3].getX(), var2[var3].getY(), var2[var3 - 1].getX(), var2[var3 - 1].getY()) != null) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      }
   }

   public mxPoint intersectsRectPerimeter(String var1, mxRectangle var2) {
      mxPoint var3 = null;
      mxPoint[] var4 = this.getCurvePoints(var1);
      if (var4 != null && var4.length > 1) {
         int var5 = this.intersectRectPerimeterSeg(var1, var2);
         if (var5 != -1) {
            var3 = this.intersectRectPerimeterPoint(var1, var2, var5);
         }
      }

      return var3;
   }

   public double intersectsRectPerimeterDist(String var1, mxRectangle var2) {
      double var3 = -1.0;
      mxPoint[] var5 = this.getCurvePoints(var1);
      double[] var6 = this.getIntervals(var1);
      if (var5 != null && var5.length > 1) {
         int var7 = this.intersectRectPerimeterSeg(var1, var2);
         mxPoint var8 = null;
         if (var7 != -1) {
            var8 = this.intersectRectPerimeterPoint(var1, var2, var7);
         }

         if (var8 != null) {
            double var9 = var5[var7 - 1].getX();
            double var11 = var5[var7 - 1].getY();
            double var13 = var6[var7 - 1] * this.getCurveLength(var1);
            double var15 = var8.getX() - var9;
            double var17 = var8.getY() - var11;
            double var19 = Math.sqrt(var15 * var15 + var17 * var17);
            var3 = var13 + var19;
         }
      }

      return var3;
   }

   protected int intersectRectPerimeterSeg(String var1, mxRectangle var2) {
      mxPoint[] var3 = this.getCurvePoints(var1);
      if (var3 != null && var3.length > 1) {
         for(int var4 = 1; var4 < var3.length; ++var4) {
            if (var2.intersectLine(var3[var4].getX(), var3[var4].getY(), var3[var4 - 1].getX(), var3[var4 - 1].getY()) != null) {
               return var4;
            }
         }
      }

      return -1;
   }

   protected mxPoint intersectRectPerimeterPoint(String var1, mxRectangle var2, int var3) {
      mxPoint var4 = null;
      mxPoint[] var5 = this.getCurvePoints(var1);
      if (var5 != null && var5.length > 1 && var3 >= 0 && var3 < var5.length) {
         double var6 = var5[var3 - 1].getX();
         double var8 = var5[var3 - 1].getY();
         double var10 = var5[var3].getX();
         double var12 = var5[var3].getY();
         var4 = var2.intersectLine(var6, var8, var10, var12);
      }

      return var4;
   }

   public mxRectangle getRelativeFromAbsPoint(mxPoint var1, String var2) {
      mxPoint[] var3 = this.getCurvePoints(var2);
      double[] var4 = this.getIntervals(var2);
      int var5 = 0;
      double var6 = 1.0E7;

      for(int var8 = 1; var8 < var3.length; ++var8) {
         mxLine var9 = new mxLine(var3[var8 - 1], var3[var8]);
         double var10 = var9.ptSegDistSq(var1);
         if (var10 < var6) {
            var6 = var10;
            var5 = var8 - 1;
         }
      }

      mxPoint var55 = var3[var5];
      mxPoint var56 = var3[var5 + 1];
      mxLine var57 = new mxLine(var55, var56);
      double var11 = var57.ptLineDistSq(var1);
      double var13 = Math.sqrt(Math.min(var11, var6));
      double var15 = var56.getX() - var55.getX();
      double var17 = var56.getY() - var55.getY();
      double var19 = Math.sqrt(var15 * var15 + var17 * var17);
      double var21 = var15 / var19;
      double var23 = var17 / var19;
      double var25 = var1.getX() - var23 * var13 - var56.getX();
      double var27 = var1.getY() + var21 * var13 - var56.getY();
      double var29 = var1.getX() + var23 * var13 - var56.getX();
      double var31 = var1.getY() - var21 * var13 - var56.getY();
      double var33 = var25 * var25 + var27 * var27;
      double var35 = var29 * var29 + var31 * var31;
      double var37 = 0.0;
      double var39 = 0.0;
      if (var35 < var33) {
         var13 = -var13;
      }

      var37 = var1.getX() - var23 * var13;
      var39 = var1.getY() + var21 * var13;
      double var41 = 0.0;
      double var43 = 0.0;
      double var45 = 0.0;
      double var47;
      double var49;
      if (var6 != var11) {
         var47 = Math.abs(var37 - var55.getX()) + Math.abs(var39 - var55.getY());
         var49 = Math.abs(var37 - var56.getX()) + Math.abs(var39 - var56.getY());
         if (var47 < var49) {
            var41 = var4[var5];
            var43 = var37 - var55.getX();
            var45 = var39 - var55.getY();
         } else {
            var41 = var4[var5 + 1];
            var43 = var37 - var56.getX();
            var45 = var39 - var56.getY();
         }
      } else {
         var47 = Math.sqrt((var56.getX() - var37) * (var56.getX() - var37) + (var55.getY() - var39) * (var55.getY() - var39));
         var49 = Math.sqrt((var37 - var56.getX()) * (var37 - var56.getX()) + (var39 - var56.getY()) * (var39 - var56.getY()));
         double var51 = var47 / var49;
         double var53 = var4[var5 + 1] - var4[var5];
         var41 = var4[var5] + var53 * var51;
      }

      if (var41 > 1.0) {
         var41 = 1.0;
      }

      return new mxRectangle(var41, var13, var43, var45);
   }

   protected void createCoreCurve() {
      this.valid = false;
      if (this.guidePoints != null && !this.guidePoints.isEmpty()) {
         for(int var1 = 0; var1 < this.guidePoints.size(); ++var1) {
            if (this.guidePoints.get(var1) == null) {
               return;
            }
         }

         this.minXBounds = this.minYBounds = 1.0E7;
         this.maxXBounds = this.maxYBounds = 0.0;
         mxSpline var44 = new mxSpline(this.guidePoints);
         double var2 = var44.getLength();
         if (!Double.isNaN(var2) && var44.checkValues() && !(var2 < 1.0)) {
            mxSpline1D var4 = var44.getSplineX();
            mxSpline1D var5 = var44.getSplineY();
            double var6 = 12.0 / var2;
            double var8 = 1.0 / var2;
            double var10 = var6;
            double var12 = 0.15;
            double var14 = 0.3;
            double var16 = (var14 + var12) / 2.0;
            double var18 = -1.0;
            double var20 = -1.0;
            double var22 = -1.0;
            double var24 = -1.0;
            double var26 = 1.0;
            ArrayList var28 = new ArrayList();
            ArrayList var29 = new ArrayList();
            boolean var30 = false;

            mxPoint var47;
            for(double var31 = 0.0; var31 <= 1.5; var31 += var10) {
               if (var31 > 1.0) {
                  var31 = 1.0001;
                  mxPoint var46 = (mxPoint)this.guidePoints.get(this.guidePoints.size() - 1);
                  var47 = new mxPoint(var46.getX(), var46.getY());
                  var28.add(var47);
                  var29.add(var31);
                  this.updateBounds(var46.getX(), var46.getY());
                  break;
               }

               boolean var33 = true;
               double var34 = var4.getFastValue(var31);
               double var36 = var5.getFastValue(var31);
               if (var18 != -1.0 && var30 && var31 != 1.0001) {
                  double var38 = Math.abs((var20 - var18) * var26 + var20 - var34);
                  double var40 = Math.abs((var24 - var22) * var26 + var24 - var36);
                  double var42;
                  if ((var38 > var14 || var40 > var14) && var10 != var8) {
                     var42 = var14 / Math.max(var38, var40);
                     if (var10 * var42 <= var8) {
                        var26 = var8 / var10;
                     } else {
                        var26 = var42;
                     }

                     var31 -= var10;
                     var10 *= var26;
                     var33 = false;
                  } else if (var38 < var12 && var40 < var12) {
                     var26 = 1.4;
                     var10 *= var26;
                  } else {
                     var42 = var16 / Math.max(var38, var40);
                     var26 = var42 / 4.0;
                     var10 *= var26;
                  }

                  if (var33) {
                     var18 = var20;
                     var22 = var24;
                     var20 = var34;
                     var24 = var36;
                  }
               } else if (var18 == -1.0) {
                  var20 = var34;
                  var18 = var34;
                  var24 = var36;
                  var22 = var36;
               } else if (var18 == var20 && var22 == var24) {
                  var20 = var34;
                  var24 = var36;
                  var30 = true;
               }

               if (var33) {
                  mxPoint var51 = new mxPoint(var34, var36);
                  var28.add(var51);
                  var29.add(var31);
                  this.updateBounds(var34, var36);
               }
            }

            if (var28.size() >= 2) {
               mxPoint[] var45 = new mxPoint[var28.size()];
               int var32 = 0;

               for(Iterator var48 = var28.iterator(); var48.hasNext(); var45[var32++] = var47) {
                  var47 = (mxPoint)var48.next();
               }

               this.points = new Hashtable();
               this.curveLengths = new Hashtable();
               this.points.put(CORE_CURVE, var45);
               this.curveLengths.put(CORE_CURVE, var2);
               double[] var49 = new double[var29.size()];
               var32 = 0;

               Double var35;
               for(Iterator var50 = var29.iterator(); var50.hasNext(); var49[var32++] = var35) {
                  var35 = (Double)var50.next();
               }

               this.intervals = new Hashtable();
               this.intervals.put(CORE_CURVE, var49);
               this.valid = true;
            }
         }
      }
   }

   public void configureLabelCurve() {
   }

   public boolean isLabelReversed() {
      if (this.valid) {
         mxPoint[] var1 = this.getCurvePoints(CORE_CURVE);
         double var2 = var1[var1.length - 1].getX() - var1[0].getX();
         if (var2 < 0.0) {
            return true;
         }
      }

      return false;
   }

   protected void createLabelCurve() {
      mxPoint[] var1 = this.getBaseLabelCurve();
      boolean var2 = this.isLabelReversed();
      ArrayList var3 = new ArrayList();

      for(int var4 = 1; var4 < var1.length; ++var4) {
         int var5 = var4;
         int var6 = var4 - 1;
         if (var2) {
            var5 = var1.length - var4 - 1;
            var6 = var1.length - var4;
         }

         mxPoint var7 = var1[var5];
         mxPoint var8 = var1[var6];
         double var9 = var8.getX() - var7.getX();
         double var11 = var8.getY() - var7.getY();
         double var13 = Math.sqrt(var9 * var9 + var11 * var11);
         double var15 = var9 / var13;
         double var17 = var11 / var13;
         double var19 = (var8.getX() + var7.getX()) / 2.0;
         double var21 = (var8.getY() + var7.getY()) / 2.0;
         if (var4 == 1) {
            mxPoint var23 = new mxPoint(var8.getX() - var17 * this.labelBuffer, var8.getY() + var15 * this.labelBuffer);
            var3.add(var23);
            this.updateBounds(var23.getX(), var23.getY());
         }

         double var29 = var19 - var17 * this.labelBuffer;
         double var25 = var21 + var15 * this.labelBuffer;
         mxPoint var27 = new mxPoint(var29, var25);
         this.updateBounds(var29, var29);
         var3.add(var27);
         if (var4 == var1.length - 1) {
            mxPoint var28 = new mxPoint(var7.getX() - var17 * this.labelBuffer, var7.getY() + var15 * this.labelBuffer);
            var3.add(var28);
            this.updateBounds(var28.getX(), var28.getY());
         }
      }

      mxPoint[] var30 = new mxPoint[var3.size()];
      this.points.put(LABEL_CURVE, (mxPoint[])((mxPoint[])var3.toArray(var30)));
      this.populateIntervals(LABEL_CURVE);
   }

   protected mxPoint[] getBaseLabelCurve() {
      return this.getCurvePoints(CORE_CURVE);
   }

   protected void populateIntervals(String var1) {
      mxPoint[] var2 = (mxPoint[])this.points.get(var1);
      double[] var3 = new double[var2.length];
      double var4 = 0.0;
      var3[0] = 0.0;

      int var6;
      for(var6 = 0; var6 < var2.length - 1; ++var6) {
         double var7 = var2[var6 + 1].getX() - var2[var6].getX();
         double var9 = var2[var6 + 1].getY() - var2[var6].getY();
         double var11 = Math.sqrt(var7 * var7 + var9 * var9);
         var4 += var11;
         var3[var6 + 1] = var4;
      }

      for(var6 = 0; var6 < var3.length; ++var6) {
         if (var6 == var3.length - 1) {
            var3[var6] = 1.0001;
         } else {
            var3[var6] /= var4;
         }
      }

      this.intervals.put(var1, var3);
      this.curveLengths.put(var1, var4);
   }

   public void updateCurve(List var1) {
      boolean var2 = false;
      if (var1.size() != this.guidePoints.size()) {
         var2 = true;
      } else if (!this.guidePoints.equals(var1)) {
         var2 = true;
      }

      if (var2) {
         this.guidePoints = var1;
         this.points = new Hashtable();
      }

   }

   public mxPoint[] getCurvePoints(String var1) {
      if (this.points == null || this.points.get(CORE_CURVE) == null) {
         this.createCoreCurve();
      }

      if (this.valid) {
         if (this.points.get(LABEL_CURVE) == null && var1 == LABEL_CURVE) {
            this.createLabelCurve();
         }

         return (mxPoint[])this.points.get(var1);
      } else {
         return null;
      }
   }

   public double[] getIntervals(String var1) {
      if (this.intervals == null) {
         this.createCoreCurve();
      }

      if (this.valid) {
         if (this.points.get(LABEL_CURVE) == null && var1 == LABEL_CURVE) {
            this.createLabelCurve();
         }

         return (double[])this.intervals.get(var1);
      } else {
         return null;
      }
   }

   public double getCurveLength(String var1) {
      if (this.intervals == null) {
         this.createCoreCurve();
      }

      if (this.valid) {
         if (this.intervals.get(var1) == null) {
            this.createLabelCurve();
         }

         return (Double)this.curveLengths.get(var1);
      } else {
         return 0.0;
      }
   }

   protected void updateBounds(double var1, double var3) {
      this.minXBounds = Math.min(this.minXBounds, var1);
      this.maxXBounds = Math.max(this.maxXBounds, var1);
      this.minYBounds = Math.min(this.minYBounds, var3);
      this.maxYBounds = Math.max(this.maxYBounds, var3);
   }

   public List getGuidePoints() {
      return this.guidePoints;
   }
}
