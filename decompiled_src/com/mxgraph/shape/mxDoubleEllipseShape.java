package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Rectangle;

public class mxDoubleEllipseShape extends mxEllipseShape {
   public void paintShape(mxGraphics2DCanvas var1, mxCellState var2) {
      super.paintShape(var1, var2);
      int var3 = (int)Math.round((double)(mxUtils.getFloat(var2.getStyle(), mxConstants.STYLE_STROKEWIDTH, 1.0F) + 3.0F) * var1.getScale());
      Rectangle var4 = var2.getRectangle();
      int var5 = var4.x + var3;
      int var6 = var4.y + var3;
      int var7 = var4.width - 2 * var3;
      int var8 = var4.height - 2 * var3;
      var1.getGraphics().drawOval(var5, var6, var7, var8);
   }
}
