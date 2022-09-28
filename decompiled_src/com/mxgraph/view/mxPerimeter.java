package com.mxgraph.view;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;

public class mxPerimeter {
   public static mxPerimeterFunction RectanglePerimeter = new mxPerimeterFunction() {
      public mxPoint apply(mxRectangle var1, mxCellState var2, mxPoint var3, boolean var4) {
         double var5 = var1.getCenterX();
         double var7 = var1.getCenterY();
         double var9 = var3.getX() - var5;
         double var11 = var3.getY() - var7;
         double var13 = Math.atan2(var11, var9);
         mxPoint var15 = new mxPoint();
         double var16 = Math.PI;
         double var18 = 1.5707963267948966;
         double var20 = var18 - var13;
         double var22 = Math.atan2(var1.getHeight(), var1.getWidth());
         if (!(var13 < -var16 + var22) && !(var13 > var16 - var22)) {
            if (var13 < -var22) {
               var15.setY(var1.getY());
               var15.setX(var5 - var1.getHeight() * Math.tan(var20) / 2.0);
            } else if (var13 < var22) {
               var15.setX(var1.getX() + var1.getWidth());
               var15.setY(var7 + var1.getWidth() * Math.tan(var13) / 2.0);
            } else {
               var15.setY(var1.getY() + var1.getHeight());
               var15.setX(var5 + var1.getHeight() * Math.tan(var20) / 2.0);
            }
         } else {
            var15.setX(var1.getX());
            var15.setY(var7 - var1.getWidth() * Math.tan(var13) / 2.0);
         }

         if (var4) {
            if (var3.getX() >= var1.getX() && var3.getX() <= var1.getX() + var1.getWidth()) {
               var15.setX(var3.getX());
            } else if (var3.getY() >= var1.getY() && var3.getY() <= var1.getY() + var1.getHeight()) {
               var15.setY(var3.getY());
            }

            if (var3.getX() < var1.getX()) {
               var15.setX(var1.getX());
            } else if (var3.getX() > var1.getX() + var1.getWidth()) {
               var15.setX(var1.getX() + var1.getWidth());
            }

            if (var3.getY() < var1.getY()) {
               var15.setY(var1.getY());
            } else if (var3.getY() > var1.getY() + var1.getHeight()) {
               var15.setY(var1.getY() + var1.getHeight());
            }
         }

         return var15;
      }
   };
   public static mxPerimeterFunction EllipsePerimeter = new mxPerimeterFunction() {
      public mxPoint apply(mxRectangle var1, mxCellState var2, mxPoint var3, boolean var4) {
         double var5 = var1.getX();
         double var7 = var1.getY();
         double var9 = var1.getWidth() / 2.0;
         double var11 = var1.getHeight() / 2.0;
         double var13 = var5 + var9;
         double var15 = var7 + var11;
         double var17 = var3.getX();
         double var19 = var3.getY();
         double var21 = var17 - var13;
         double var23 = var19 - var15;
         if (var21 == 0.0) {
            return new mxPoint(var13, var15 + var11 * var23 / Math.abs(var23));
         } else {
            double var25;
            double var27;
            if (var4) {
               if (var19 >= var7 && var19 <= var7 + var1.getHeight()) {
                  var25 = var19 - var15;
                  var27 = Math.sqrt(var9 * var9 * (1.0 - var25 * var25 / (var11 * var11)));
                  if (Double.isNaN(var27)) {
                     var27 = 0.0;
                  }

                  if (var17 <= var5) {
                     var27 = -var27;
                  }

                  return new mxPoint(var13 + var27, var19);
               }

               if (var17 >= var5 && var17 <= var5 + var1.getWidth()) {
                  var25 = var17 - var13;
                  var27 = Math.sqrt(var11 * var11 * (1.0 - var25 * var25 / (var9 * var9)));
                  if (Double.isNaN(var27)) {
                     var27 = 0.0;
                  }

                  if (var19 <= var7) {
                     var27 = -var27;
                  }

                  return new mxPoint(var17, var15 + var27);
               }
            }

            var25 = var23 / var21;
            var27 = var15 - var25 * var13;
            double var29 = var9 * var9 * var25 * var25 + var11 * var11;
            double var31 = -2.0 * var13 * var29;
            double var33 = var9 * var9 * var25 * var25 * var13 * var13 + var11 * var11 * var13 * var13 - var9 * var9 * var11 * var11;
            double var35 = Math.sqrt(var31 * var31 - 4.0 * var29 * var33);
            double var37 = (-var31 + var35) / (2.0 * var29);
            double var39 = (-var31 - var35) / (2.0 * var29);
            double var41 = var25 * var37 + var27;
            double var43 = var25 * var39 + var27;
            double var45 = Math.sqrt(Math.pow(var37 - var17, 2.0) + Math.pow(var41 - var19, 2.0));
            double var47 = Math.sqrt(Math.pow(var39 - var17, 2.0) + Math.pow(var43 - var19, 2.0));
            double var49 = 0.0;
            double var51 = 0.0;
            if (var45 < var47) {
               var49 = var37;
               var51 = var41;
            } else {
               var49 = var39;
               var51 = var43;
            }

            return new mxPoint(var49, var51);
         }
      }
   };
   public static mxPerimeterFunction RhombusPerimeter = new mxPerimeterFunction() {
      public mxPoint apply(mxRectangle var1, mxCellState var2, mxPoint var3, boolean var4) {
         double var5 = var1.getX();
         double var7 = var1.getY();
         double var9 = var1.getWidth();
         double var11 = var1.getHeight();
         double var13 = var5 + var9 / 2.0;
         double var15 = var7 + var11 / 2.0;
         double var17 = var3.getX();
         double var19 = var3.getY();
         if (var13 == var17) {
            return var15 > var19 ? new mxPoint(var13, var7) : new mxPoint(var13, var7 + var11);
         } else if (var15 == var19) {
            return var13 > var17 ? new mxPoint(var5, var15) : new mxPoint(var5 + var9, var15);
         } else {
            double var21 = var13;
            double var23 = var15;
            if (var4) {
               if (var17 >= var5 && var17 <= var5 + var9) {
                  var21 = var17;
               } else if (var19 >= var7 && var19 <= var7 + var11) {
                  var23 = var19;
               }
            }

            if (var17 < var13) {
               return var19 < var15 ? mxUtils.intersection(var17, var19, var21, var23, var13, var7, var5, var15) : mxUtils.intersection(var17, var19, var21, var23, var13, var7 + var11, var5, var15);
            } else {
               return var19 < var15 ? mxUtils.intersection(var17, var19, var21, var23, var13, var7, var5 + var9, var15) : mxUtils.intersection(var17, var19, var21, var23, var13, var7 + var11, var5 + var9, var15);
            }
         }
      }
   };
   public static mxPerimeterFunction TrianglePerimeter = new mxPerimeterFunction() {
      public mxPoint apply(mxRectangle var1, mxCellState var2, mxPoint var3, boolean var4) {
         String var5 = var2 != null ? mxUtils.getString(var2.style, mxConstants.STYLE_DIRECTION, "east") : "east";
         boolean var6 = var5.equals("north") || var5.equals("south");
         double var7 = var1.getX();
         double var9 = var1.getY();
         double var11 = var1.getWidth();
         double var13 = var1.getHeight();
         double var15 = var7 + var11 / 2.0;
         double var17 = var9 + var13 / 2.0;
         mxPoint var19 = new mxPoint(var7, var9);
         mxPoint var20 = new mxPoint(var7 + var11, var17);
         mxPoint var21 = new mxPoint(var7, var9 + var13);
         if (var5.equals("north")) {
            var19 = var21;
            var20 = new mxPoint(var15, var9);
            var21 = new mxPoint(var7 + var11, var9 + var13);
         } else if (var5.equals("south")) {
            var20 = new mxPoint(var15, var9 + var13);
            var21 = new mxPoint(var7 + var11, var9);
         } else if (var5.equals("west")) {
            var19 = new mxPoint(var7 + var11, var9);
            var20 = new mxPoint(var7, var17);
            var21 = new mxPoint(var7 + var11, var9 + var13);
         }

         double var22 = var3.getX() - var15;
         double var24 = var3.getY() - var17;
         double var26 = var6 ? Math.atan2(var22, var24) : Math.atan2(var24, var22);
         double var28 = var6 ? Math.atan2(var11, var13) : Math.atan2(var13, var11);
         boolean var30 = false;
         if (!var5.equals("north") && !var5.equals("west")) {
            var30 = var26 < -3.141592653589793 + var28 || var26 > Math.PI - var28;
         } else {
            var30 = var26 > -var28 && var26 < var28;
         }

         mxPoint var31 = null;
         if (var30) {
            if (var4 && (var6 && var3.getX() >= var19.getX() && var3.getX() <= var21.getX() || !var6 && var3.getY() >= var19.getY() && var3.getY() <= var21.getY())) {
               if (var6) {
                  var31 = new mxPoint(var3.getX(), var19.getY());
               } else {
                  var31 = new mxPoint(var19.getX(), var3.getY());
               }
            } else if (var5.equals("east")) {
               var31 = new mxPoint(var7, var9 + var13 / 2.0 - var11 * Math.tan(var26) / 2.0);
            } else if (var5.equals("north")) {
               var31 = new mxPoint(var7 + var11 / 2.0 + var13 * Math.tan(var26) / 2.0, var9 + var13);
            } else if (var5.equals("south")) {
               var31 = new mxPoint(var7 + var11 / 2.0 - var13 * Math.tan(var26) / 2.0, var9);
            } else if (var5.equals("west")) {
               var31 = new mxPoint(var7 + var11, var9 + var13 / 2.0 + var11 * Math.tan(var26) / 2.0);
            }
         } else {
            if (var4) {
               mxPoint var32 = new mxPoint(var15, var17);
               if (var3.getY() >= var9 && var3.getY() <= var9 + var13) {
                  var32.setX(var6 ? var15 : (var5.equals("west") ? var7 + var11 : var7));
                  var32.setY(var3.getY());
               } else if (var3.getX() >= var7 && var3.getX() <= var7 + var11) {
                  var32.setX(var3.getX());
                  var32.setY(!var6 ? var17 : (var5.equals("north") ? var9 + var13 : var9));
               }

               var22 = var3.getX() - var32.getX();
               var24 = var3.getY() - var32.getY();
               var15 = var32.getX();
               var17 = var32.getY();
            }

            if ((!var6 || !(var3.getX() <= var7 + var11 / 2.0)) && (var6 || !(var3.getY() <= var9 + var13 / 2.0))) {
               var31 = mxUtils.intersection(var3.getX(), var3.getY(), var15, var17, var20.getX(), var20.getY(), var21.getX(), var21.getY());
            } else {
               var31 = mxUtils.intersection(var3.getX(), var3.getY(), var15, var17, var19.getX(), var19.getY(), var20.getX(), var20.getY());
            }
         }

         if (var31 == null) {
            var31 = new mxPoint(var15, var17);
         }

         return var31;
      }
   };

   public interface mxPerimeterFunction {
      mxPoint apply(mxRectangle var1, mxCellState var2, mxPoint var3, boolean var4);
   }
}
