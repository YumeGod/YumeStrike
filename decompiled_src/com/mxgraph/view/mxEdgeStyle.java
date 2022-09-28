package com.mxgraph.view;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import java.util.List;

public class mxEdgeStyle {
   public static mxEdgeStyleFunction EntityRelation = new mxEdgeStyleFunction() {
      public void apply(mxCellState var1, mxCellState var2, mxCellState var3, List var4, List var5) {
         mxGraphView var6 = var1.getView();
         mxIGraphModel var7 = var6.getGraph().getModel();
         double var8 = mxUtils.getDouble(var1.getStyle(), mxConstants.STYLE_SEGMENT, (double)mxConstants.ENTITY_SEGMENT) * var1.view.getScale();
         mxPoint var10 = var1.getAbsolutePoint(0);
         mxPoint var11 = var1.getAbsolutePoint(var1.getAbsolutePointCount() - 1);
         boolean var12 = false;
         if (var10 != null) {
            var2 = new mxCellState();
            var2.setX(var10.getX());
            var2.setY(var10.getY());
         } else if (var2 != null) {
            mxGeometry var13 = var7.getGeometry(var2.cell);
            if (var13.isRelative()) {
               var12 = var13.getX() <= 0.5;
            } else if (var3 != null) {
               var12 = var3.getX() + var3.getWidth() < var2.getX();
            }
         }

         boolean var30 = true;
         if (var11 != null) {
            var3 = new mxCellState();
            var3.setX(var11.getX());
            var3.setY(var11.getY());
         } else if (var3 != null) {
            mxGeometry var14 = var7.getGeometry(var3.cell);
            if (var14.isRelative()) {
               var30 = var14.getX() <= 0.5;
            } else if (var2 != null) {
               var30 = var2.getX() + var2.getWidth() < var3.getX();
            }
         }

         if (var2 != null && var3 != null) {
            double var31 = var12 ? var2.getX() : var2.getX() + var2.getWidth();
            double var16 = var6.getRoutingCenterY(var2);
            double var18 = var30 ? var3.getX() : var3.getX() + var3.getWidth();
            double var20 = var6.getRoutingCenterY(var3);
            double var24 = var12 ? -var8 : var8;
            mxPoint var26 = new mxPoint(var31 + var24, var16);
            var5.add(var26);
            var24 = var30 ? -var8 : var8;
            mxPoint var27 = new mxPoint(var18 + var24, var20);
            double var28;
            if (var12 == var30) {
               var28 = var12 ? Math.min(var31, var18) - var8 : Math.max(var31, var18) + var8;
               var5.add(new mxPoint(var28, var16));
               var5.add(new mxPoint(var28, var20));
            } else if (var26.getX() < var27.getX() == var12) {
               var28 = var16 + (var20 - var16) / 2.0;
               var5.add(new mxPoint(var26.getX(), var28));
               var5.add(new mxPoint(var27.getX(), var28));
            }

            var5.add(var27);
         }

      }
   };
   public static mxEdgeStyleFunction Loop = new mxEdgeStyleFunction() {
      public void apply(mxCellState var1, mxCellState var2, mxCellState var3, List var4, List var5) {
         if (var2 != null) {
            mxGraphView var6 = var1.getView();
            mxGraph var7 = var6.getGraph();
            mxPoint var8 = var4 != null && var4.size() > 0 ? (mxPoint)var4.get(0) : null;
            if (var8 != null) {
               var8 = var6.transformControlPoint(var1, var8);
               if (var2.contains(var8.getX(), var8.getY())) {
                  var8 = null;
               }
            }

            double var9 = 0.0;
            double var11 = 0.0;
            double var13 = 0.0;
            double var15 = 0.0;
            double var17 = mxUtils.getDouble(var1.getStyle(), mxConstants.STYLE_SEGMENT, (double)var7.getGridSize()) * var6.getScale();
            String var19 = mxUtils.getString(var1.getStyle(), mxConstants.STYLE_DIRECTION, "west");
            if (!var19.equals("north") && !var19.equals("south")) {
               var13 = var6.getRoutingCenterY(var2);
               var15 = var17;
            } else {
               var9 = var6.getRoutingCenterX(var2);
               var11 = var17;
            }

            if (var8 != null && !(var8.getX() < var2.getX()) && !(var8.getX() > var2.getX() + var2.getWidth())) {
               if (var8 != null) {
                  var9 = var6.getRoutingCenterX(var2);
                  var11 = Math.max(Math.abs(var9 - var8.getX()), var15);
                  var13 = var8.getY();
                  var15 = 0.0;
               }
            } else if (var8 != null) {
               var9 = var8.getX();
               var15 = Math.max(Math.abs(var13 - var8.getY()), var15);
            } else if (var19.equals("north")) {
               var13 = var2.getY() - 2.0 * var11;
            } else if (var19.equals("south")) {
               var13 = var2.getY() + var2.getHeight() + 2.0 * var11;
            } else if (var19.equals("east")) {
               var9 = var2.getX() - 2.0 * var15;
            } else {
               var9 = var2.getX() + var2.getWidth() + 2.0 * var15;
            }

            var5.add(new mxPoint(var9 - var11, var13 - var15));
            var5.add(new mxPoint(var9 + var11, var13 + var15));
         }

      }
   };
   public static mxEdgeStyleFunction ElbowConnector = new mxEdgeStyleFunction() {
      public void apply(mxCellState var1, mxCellState var2, mxCellState var3, List var4, List var5) {
         mxPoint var6 = var4 != null && var4.size() > 0 ? (mxPoint)var4.get(0) : null;
         boolean var7 = false;
         boolean var8 = false;
         if (var2 != null && var3 != null) {
            double var9;
            double var11;
            double var13;
            double var15;
            if (var6 != null) {
               var9 = Math.min(var2.getX(), var3.getX());
               var11 = Math.max(var2.getX() + var2.getWidth(), var3.getX() + var3.getWidth());
               var13 = Math.min(var2.getY(), var3.getY());
               var15 = Math.max(var2.getY() + var2.getHeight(), var3.getY() + var3.getHeight());
               var6 = var1.getView().transformControlPoint(var1, var6);
               var7 = var6.getY() < var13 || var6.getY() > var15;
               var8 = var6.getX() < var9 || var6.getX() > var11;
            } else {
               var9 = Math.max(var2.getX(), var3.getX());
               var11 = Math.min(var2.getX() + var2.getWidth(), var3.getX() + var3.getWidth());
               var7 = var9 == var11;
               if (!var7) {
                  var13 = Math.max(var2.getY(), var3.getY());
                  var15 = Math.min(var2.getY() + var2.getHeight(), var3.getY() + var3.getHeight());
                  var8 = var13 == var15;
               }
            }
         }

         if (var8 || !var7 && !mxUtils.getString(var1.getStyle(), mxConstants.STYLE_ELBOW, "").equals("vertical")) {
            mxEdgeStyle.SideToSide.apply(var1, var2, var3, var4, var5);
         } else {
            mxEdgeStyle.TopToBottom.apply(var1, var2, var3, var4, var5);
         }

      }
   };
   public static mxEdgeStyleFunction SideToSide = new mxEdgeStyleFunction() {
      public void apply(mxCellState var1, mxCellState var2, mxCellState var3, List var4, List var5) {
         mxGraphView var6 = var1.getView();
         mxPoint var7 = var4 != null && var4.size() > 0 ? (mxPoint)var4.get(0) : null;
         mxPoint var8 = var1.getAbsolutePoint(0);
         mxPoint var9 = var1.getAbsolutePoint(var1.getAbsolutePointCount() - 1);
         if (var7 != null) {
            var7 = var6.transformControlPoint(var1, var7);
         }

         if (var8 != null) {
            var2 = new mxCellState();
            var2.setX(var8.getX());
            var2.setY(var8.getY());
         }

         if (var9 != null) {
            var3 = new mxCellState();
            var3.setX(var9.getX());
            var3.setY(var9.getY());
         }

         if (var2 != null && var3 != null) {
            double var10 = Math.max(var2.getX(), var3.getX());
            double var12 = Math.min(var2.getX() + var2.getWidth(), var3.getX() + var3.getWidth());
            double var14 = var7 != null ? var7.getX() : var12 + (var10 - var12) / 2.0;
            double var16 = var6.getRoutingCenterY(var2);
            double var18 = var6.getRoutingCenterY(var3);
            if (var7 != null) {
               if (var7.getY() >= var2.getY() && var7.getY() <= var2.getY() + var2.getHeight()) {
                  var16 = var7.getY();
               }

               if (var7.getY() >= var3.getY() && var7.getY() <= var3.getY() + var3.getHeight()) {
                  var18 = var7.getY();
               }
            }

            if (!var3.contains(var14, var16) && !var2.contains(var14, var16)) {
               var5.add(new mxPoint(var14, var16));
            }

            if (!var3.contains(var14, var18) && !var2.contains(var14, var18)) {
               var5.add(new mxPoint(var14, var18));
            }

            if (var5.size() == 1) {
               if (var7 != null) {
                  var5.add(new mxPoint(var14, var7.getY()));
               } else {
                  double var20 = Math.max(var2.getY(), var3.getY());
                  double var22 = Math.min(var2.getY() + var2.getHeight(), var3.getY() + var3.getHeight());
                  var5.add(new mxPoint(var14, var20 + (var22 - var20) / 2.0));
               }
            }
         }

      }
   };
   public static mxEdgeStyleFunction TopToBottom = new mxEdgeStyleFunction() {
      public void apply(mxCellState var1, mxCellState var2, mxCellState var3, List var4, List var5) {
         mxGraphView var6 = var1.getView();
         mxPoint var7 = var4 != null && var4.size() > 0 ? (mxPoint)var4.get(0) : null;
         mxPoint var8 = var1.getAbsolutePoint(0);
         mxPoint var9 = var1.getAbsolutePoint(var1.getAbsolutePointCount() - 1);
         if (var7 != null) {
            var7 = var6.transformControlPoint(var1, var7);
         }

         if (var8 != null) {
            var2 = new mxCellState();
            var2.setX(var8.getX());
            var2.setY(var8.getY());
         }

         if (var9 != null) {
            var3 = new mxCellState();
            var3.setX(var9.getX());
            var3.setY(var9.getY());
         }

         if (var2 != null && var3 != null) {
            double var10 = Math.max(var2.getY(), var3.getY());
            double var12 = Math.min(var2.getY() + var2.getHeight(), var3.getY() + var3.getHeight());
            double var14 = var6.getRoutingCenterX(var2);
            if (var7 != null && var7.getX() >= var2.getX() && var7.getX() <= var2.getX() + var2.getWidth()) {
               var14 = var7.getX();
            }

            double var16 = var7 != null ? var7.getY() : var12 + (var10 - var12) / 2.0;
            if (!var3.contains(var14, var16) && !var2.contains(var14, var16)) {
               var5.add(new mxPoint(var14, var16));
            }

            if (var7 != null && var7.getX() >= var3.getX() && var7.getX() <= var3.getX() + var3.getWidth()) {
               var14 = var7.getX();
            } else {
               var14 = var6.getRoutingCenterX(var3);
            }

            if (!var3.contains(var14, var16) && !var2.contains(var14, var16)) {
               var5.add(new mxPoint(var14, var16));
            }

            if (var5.size() == 1) {
               if (var7 != null) {
                  var5.add(new mxPoint(var7.getX(), var16));
               } else {
                  double var18 = Math.max(var2.getX(), var3.getX());
                  double var20 = Math.min(var2.getX() + var2.getWidth(), var3.getX() + var3.getWidth());
                  var5.add(new mxPoint(var18 + (var20 - var18) / 2.0, var16));
               }
            }
         }

      }
   };

   public interface mxEdgeStyleFunction {
      void apply(mxCellState var1, mxCellState var2, mxCellState var3, List var4, List var5);
   }
}
