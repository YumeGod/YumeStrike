package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxLine;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class mxConnectorShape extends mxBasicShape {
   public void paintShape(mxGraphics2DCanvas var1, mxCellState var2) {
      if (var2.getAbsolutePointCount() > 1 && this.configureGraphics(var1, var2, false)) {
         ArrayList var3 = new ArrayList(var2.getAbsolutePoints());
         Map var4 = var2.getStyle();
         boolean var5 = mxUtils.isTrue(var4, mxConstants.STYLE_DASHED);
         Object var6 = var4.get(mxConstants.STYLE_DASHED);
         if (var5) {
            var4.remove(mxConstants.STYLE_DASHED);
            var1.getGraphics().setStroke(var1.createStroke(var4));
         }

         this.translatePoint(var3, 0, this.paintMarker(var1, var2.getAbsolutePoints(), var4, true));
         this.translatePoint(var3, var3.size() - 1, this.paintMarker(var1, var2.getAbsolutePoints(), var4, false));
         if (var5) {
            var4.put(mxConstants.STYLE_DASHED, var6);
            var1.getGraphics().setStroke(var1.createStroke(var4));
         }

         this.paintPolyline(var1, var3, var2.getStyle());
      }

   }

   protected void paintPolyline(mxGraphics2DCanvas var1, List var2, Map var3) {
      boolean var4 = this.isRounded(var3) && var1.getScale() > mxConstants.MIN_SCALE_FOR_ROUNDED_LINES;
      var1.paintPolyline((mxPoint[])var2.toArray(new mxPoint[var2.size()]), var4);
   }

   public boolean isRounded(Map var1) {
      return mxUtils.isTrue(var1, mxConstants.STYLE_ROUNDED, false);
   }

   private void translatePoint(List var1, int var2, mxPoint var3) {
      if (var3 != null) {
         mxPoint var4 = (mxPoint)((mxPoint)var1.get(var2)).clone();
         var4.setX(var4.getX() + var3.getX());
         var4.setY(var4.getY() + var3.getY());
         var1.set(var2, var4);
      }

   }

   public mxPoint paintMarker(mxGraphics2DCanvas var1, List var2, Map var3, boolean var4) {
      float var5 = (float)((double)mxUtils.getFloat(var3, mxConstants.STYLE_STROKEWIDTH, 1.0F) * var1.getScale());
      String var6 = mxUtils.getString(var3, var4 ? mxConstants.STYLE_STARTARROW : mxConstants.STYLE_ENDARROW, "");
      float var7 = mxUtils.getFloat(var3, var4 ? mxConstants.STYLE_STARTSIZE : mxConstants.STYLE_ENDSIZE, (float)mxConstants.DEFAULT_MARKERSIZE);
      Color var8 = mxUtils.getColor(var3, mxConstants.STYLE_STROKECOLOR);
      var1.getGraphics().setColor(var8);
      double var9 = (double)var7 * var1.getScale();
      mxLine var11 = this.getMarkerVector(var2, var4, var9);
      mxPoint var12 = new mxPoint(var11.getX(), var11.getY());
      mxPoint var13 = var11.getEndPoint();
      mxPoint var14 = null;
      double var15 = var13.getX() - var12.getX();
      double var17 = var13.getY() - var12.getY();
      double var19 = Math.max(1.0, Math.sqrt(var15 * var15 + var17 * var17));
      double var21 = var15 / var19;
      double var23 = var17 / var19;
      double var25 = var21 * var9;
      double var27 = var23 * var9;
      double var29 = var21 * (double)var5;
      double var31 = var23 * (double)var5;
      var13 = (mxPoint)var13.clone();
      var13.setX(var13.getX() - var29);
      var13.setY(var13.getY() - var31);
      Polygon var33;
      if (!var6.equals("classic") && !var6.equals("block")) {
         if (var6.equals("open")) {
            var25 *= 1.2;
            var27 *= 1.2;
            var1.getGraphics().draw(new Line2D.Float((float)((int)Math.round(var13.getX() - var25 - var27 / 2.0)), (float)((int)Math.round(var13.getY() - var27 + var25 / 2.0)), (float)((int)Math.round(var13.getX() - var25 / 6.0)), (float)((int)Math.round(var13.getY() - var27 / 6.0))));
            var1.getGraphics().draw(new Line2D.Float((float)((int)Math.round(var13.getX() - var25 / 6.0)), (float)((int)Math.round(var13.getY() - var27 / 6.0)), (float)((int)Math.round(var13.getX() + var27 / 2.0 - var25)), (float)((int)Math.round(var13.getY() - var27 - var25 / 2.0))));
            var14 = new mxPoint(-var25 / 2.0 - var29 / 2.0, -var27 / 2.0 - var31 / 2.0);
         } else if (var6.equals("oval")) {
            var25 *= 1.2;
            var27 *= 1.2;
            var9 *= 1.2;
            int var38 = (int)Math.round(var13.getX() - var25 / 2.0);
            int var34 = (int)Math.round(var13.getY() - var27 / 2.0);
            int var35 = (int)Math.round(var9 / 2.0);
            int var36 = (int)Math.round(var9);
            Ellipse2D.Float var37 = new Ellipse2D.Float((float)(var38 - var35), (float)(var34 - var35), (float)var36, (float)var36);
            var1.fillShape(var37);
            var1.getGraphics().draw(var37);
            var14 = new mxPoint(-var25 / 2.0 - var29 / 2.0, -var27 / 2.0 - var31 / 2.0);
         } else if (var6.equals("diamond")) {
            var25 *= 1.2;
            var27 *= 1.2;
            var33 = new Polygon();
            var33.addPoint((int)Math.round(var13.getX()), (int)Math.round(var13.getY()));
            var33.addPoint((int)Math.round(var13.getX() - var25 / 2.0 - var27 / 2.0), (int)Math.round(var13.getY() + var25 / 2.0 - var27 / 2.0));
            var33.addPoint((int)Math.round(var13.getX() - var25), (int)Math.round(var13.getY() - var27));
            var33.addPoint((int)Math.round(var13.getX() - var25 / 2.0 + var27 / 2.0), (int)Math.round(var13.getY() - var27 / 2.0 - var25 / 2.0));
            var1.fillShape(var33);
            var1.getGraphics().draw(var33);
            var14 = new mxPoint(-var25 / 2.0 - var29 / 2.0, -var27 / 2.0 - var31 / 2.0);
         } else {
            var25 = var15 * (double)var5 / var19;
            var27 = var17 * (double)var5 / var19;
            var14 = new mxPoint(-var29 / 2.0, -var31 / 2.0);
         }
      } else {
         var33 = new Polygon();
         var33.addPoint((int)Math.round(var13.getX()), (int)Math.round(var13.getY()));
         var33.addPoint((int)Math.round(var13.getX() - var25 - var27 / 2.0), (int)Math.round(var13.getY() - var27 + var25 / 2.0));
         if (var6.equals("classic")) {
            var33.addPoint((int)Math.round(var13.getX() - var25 * 3.0 / 4.0), (int)Math.round(var13.getY() - var27 * 3.0 / 4.0));
         }

         var33.addPoint((int)Math.round(var13.getX() + var27 / 2.0 - var25), (int)Math.round(var13.getY() - var27 - var25 / 2.0));
         var1.fillShape(var33);
         var1.getGraphics().draw(var33);
         var14 = new mxPoint(-var25 - var29 / 2.0, -var27 - var31 / 2.0);
      }

      return var14;
   }

   protected mxLine getMarkerVector(List var1, boolean var2, double var3) {
      if (var2) {
         return new mxLine((mxPoint)var1.get(1), (mxPoint)var1.get(0));
      } else {
         int var5 = var1.size();
         return new mxLine((mxPoint)var1.get(var5 - 2), (mxPoint)var1.get(var5 - 1));
      }
   }
}
