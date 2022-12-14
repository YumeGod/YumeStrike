package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

public class mxHexagonShape extends mxBasicShape {
   public Shape createShape(mxGraphics2DCanvas var1, mxCellState var2) {
      Rectangle var3 = var2.getRectangle();
      int var4 = var3.x;
      int var5 = var3.y;
      int var6 = var3.width;
      int var7 = var3.height;
      String var8 = mxUtils.getString(var2.getStyle(), mxConstants.STYLE_DIRECTION, "east");
      Polygon var9 = new Polygon();
      if (!var8.equals("north") && !var8.equals("south")) {
         var9.addPoint(var4 + (int)(0.25 * (double)var6), var5);
         var9.addPoint(var4 + (int)(0.75 * (double)var6), var5);
         var9.addPoint(var4 + var6, var5 + (int)(0.5 * (double)var7));
         var9.addPoint(var4 + (int)(0.75 * (double)var6), var5 + var7);
         var9.addPoint(var4 + (int)(0.25 * (double)var6), var5 + var7);
         var9.addPoint(var4, var5 + (int)(0.5 * (double)var7));
      } else {
         var9.addPoint(var4 + (int)(0.5 * (double)var6), var5);
         var9.addPoint(var4 + var6, var5 + (int)(0.25 * (double)var7));
         var9.addPoint(var4 + var6, var5 + (int)(0.75 * (double)var7));
         var9.addPoint(var4 + (int)(0.5 * (double)var6), var5 + var7);
         var9.addPoint(var4, var5 + (int)(0.75 * (double)var7));
         var9.addPoint(var4, var5 + (int)(0.25 * (double)var7));
      }

      return var9;
   }
}
