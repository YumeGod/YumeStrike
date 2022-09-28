package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import java.awt.Polygon;
import java.awt.Shape;

public class mxArrowShape extends mxBasicShape {
   public Shape createShape(mxGraphics2DCanvas var1, mxCellState var2) {
      double var3 = var1.getScale();
      mxPoint var5 = var2.getAbsolutePoint(0);
      mxPoint var6 = var2.getAbsolutePoint(var2.getAbsolutePointCount() - 1);
      double var7 = (double)mxConstants.ARROW_SPACING * var3;
      double var9 = (double)mxConstants.ARROW_WIDTH * var3;
      double var11 = (double)mxConstants.ARROW_SIZE * var3;
      double var13 = var6.getX() - var5.getX();
      double var15 = var6.getY() - var5.getY();
      double var17 = Math.sqrt(var13 * var13 + var15 * var15);
      double var19 = var17 - 2.0 * var7 - var11;
      double var21 = var13 / var17;
      double var23 = var15 / var17;
      double var25 = var19 * var21;
      double var27 = var19 * var23;
      double var29 = var9 * var23 / 3.0;
      double var31 = -var9 * var21 / 3.0;
      double var33 = var5.getX() - var29 / 2.0 + var7 * var21;
      double var35 = var5.getY() - var31 / 2.0 + var7 * var23;
      double var37 = var33 + var29;
      double var39 = var35 + var31;
      double var41 = var37 + var25;
      double var43 = var39 + var27;
      double var45 = var41 + var29;
      double var47 = var43 + var31;
      double var49 = var45 - 3.0 * var29;
      double var51 = var47 - 3.0 * var31;
      Polygon var53 = new Polygon();
      var53.addPoint((int)Math.round(var33), (int)Math.round(var35));
      var53.addPoint((int)Math.round(var37), (int)Math.round(var39));
      var53.addPoint((int)Math.round(var41), (int)Math.round(var43));
      var53.addPoint((int)Math.round(var45), (int)Math.round(var47));
      var53.addPoint((int)Math.round(var6.getX() - var7 * var21), (int)Math.round(var6.getY() - var7 * var23));
      var53.addPoint((int)Math.round(var49), (int)Math.round(var51));
      var53.addPoint((int)Math.round(var49 + var29), (int)Math.round(var51 + var31));
      return var53;
   }
}
