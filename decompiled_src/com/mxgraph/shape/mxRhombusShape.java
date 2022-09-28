package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

public class mxRhombusShape extends mxBasicShape {
   public Shape createShape(mxGraphics2DCanvas var1, mxCellState var2) {
      Rectangle var3 = var2.getRectangle();
      int var4 = var3.x;
      int var5 = var3.y;
      int var6 = var3.width;
      int var7 = var3.height;
      int var8 = var6 / 2;
      int var9 = var7 / 2;
      Polygon var10 = new Polygon();
      var10.addPoint(var4 + var8, var5);
      var10.addPoint(var4 + var6, var5 + var9);
      var10.addPoint(var4 + var8, var5 + var7);
      var10.addPoint(var4, var5 + var9);
      return var10;
   }
}
