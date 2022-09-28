package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

public class mxActorShape extends mxBasicShape {
   public Shape createShape(mxGraphics2DCanvas var1, mxCellState var2) {
      Rectangle var3 = var2.getRectangle();
      int var4 = var3.x;
      int var5 = var3.y;
      int var6 = var3.width;
      int var7 = var3.height;
      float var8 = (float)(var6 * 2 / 6);
      GeneralPath var9 = new GeneralPath();
      var9.moveTo((float)var4, (float)(var5 + var7));
      var9.curveTo((float)var4, (float)(var5 + 3 * var7 / 5), (float)var4, (float)(var5 + 2 * var7 / 5), (float)(var4 + var6 / 2), (float)(var5 + 2 * var7 / 5));
      var9.curveTo((float)(var4 + var6 / 2) - var8, (float)(var5 + 2 * var7 / 5), (float)(var4 + var6 / 2) - var8, (float)var5, (float)(var4 + var6 / 2), (float)var5);
      var9.curveTo((float)(var4 + var6 / 2) + var8, (float)var5, (float)(var4 + var6 / 2) + var8, (float)(var5 + 2 * var7 / 5), (float)(var4 + var6 / 2), (float)(var5 + 2 * var7 / 5));
      var9.curveTo((float)(var4 + var6), (float)(var5 + 2 * var7 / 5), (float)(var4 + var6), (float)(var5 + 3 * var7 / 5), (float)(var4 + var6), (float)(var5 + var7));
      var9.closePath();
      return var9;
   }
}
