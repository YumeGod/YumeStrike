package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

public class mxCloudShape extends mxBasicShape {
   public Shape createShape(mxGraphics2DCanvas var1, mxCellState var2) {
      Rectangle var3 = var2.getRectangle();
      int var4 = var3.x;
      int var5 = var3.y;
      int var6 = var3.width;
      int var7 = var3.height;
      GeneralPath var8 = new GeneralPath();
      var8.moveTo((float)((double)var4 + 0.25 * (double)var6), (float)((double)var5 + 0.25 * (double)var7));
      var8.curveTo((float)((double)var4 + 0.05 * (double)var6), (float)((double)var5 + 0.25 * (double)var7), (float)var4, (float)((double)var5 + 0.5 * (double)var7), (float)((double)var4 + 0.16 * (double)var6), (float)((double)var5 + 0.55 * (double)var7));
      var8.curveTo((float)var4, (float)((double)var5 + 0.66 * (double)var7), (float)((double)var4 + 0.18 * (double)var6), (float)((double)var5 + 0.9 * (double)var7), (float)((double)var4 + 0.31 * (double)var6), (float)((double)var5 + 0.8 * (double)var7));
      var8.curveTo((float)((double)var4 + 0.4 * (double)var6), (float)(var5 + var7), (float)((double)var4 + 0.7 * (double)var6), (float)(var5 + var7), (float)((double)var4 + 0.8 * (double)var6), (float)((double)var5 + 0.8 * (double)var7));
      var8.curveTo((float)(var4 + var6), (float)((double)var5 + 0.8 * (double)var7), (float)(var4 + var6), (float)((double)var5 + 0.6 * (double)var7), (float)((double)var4 + 0.875 * (double)var6), (float)((double)var5 + 0.5 * (double)var7));
      var8.curveTo((float)(var4 + var6), (float)((double)var5 + 0.3 * (double)var7), (float)((double)var4 + 0.8 * (double)var6), (float)((double)var5 + 0.1 * (double)var7), (float)((double)var4 + 0.625 * (double)var6), (float)((double)var5 + 0.2 * (double)var7));
      var8.curveTo((float)((double)var4 + 0.5 * (double)var6), (float)((double)var5 + 0.05 * (double)var7), (float)((double)var4 + 0.3 * (double)var6), (float)((double)var5 + 0.05 * (double)var7), (float)((double)var4 + 0.25 * (double)var6), (float)((double)var5 + 0.25 * (double)var7));
      var8.closePath();
      return var8;
   }
}
