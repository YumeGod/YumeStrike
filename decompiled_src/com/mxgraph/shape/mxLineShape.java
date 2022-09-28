package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

public class mxLineShape extends mxBasicShape {
   public void paintShape(mxGraphics2DCanvas var1, mxCellState var2) {
      if (this.configureGraphics(var1, var2, false)) {
         boolean var3 = mxUtils.isTrue(var2.getStyle(), mxConstants.STYLE_ROUNDED, false) && var1.getScale() > mxConstants.MIN_SCALE_FOR_ROUNDED_LINES;
         var1.paintPolyline(this.createPoints(var1, var2), var3);
      }

   }

   public mxPoint[] createPoints(mxGraphics2DCanvas var1, mxCellState var2) {
      String var3 = mxUtils.getString(var2.getStyle(), mxConstants.STYLE_DIRECTION, "east");
      mxPoint var4;
      mxPoint var5;
      double var6;
      if (!var3.equals("east") && !var3.equals("west")) {
         var6 = var2.getCenterX();
         var4 = new mxPoint(var6, var2.getY());
         var5 = new mxPoint(var6, var2.getY() + var2.getHeight());
      } else {
         var6 = var2.getCenterY();
         var4 = new mxPoint(var2.getX(), var6);
         var5 = new mxPoint(var2.getX() + var2.getWidth(), var6);
      }

      mxPoint[] var8 = new mxPoint[]{var4, var5};
      return var8;
   }
}
