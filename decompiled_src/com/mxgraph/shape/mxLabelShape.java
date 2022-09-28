package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Map;

public class mxLabelShape extends mxImageShape {
   public Rectangle getImageBounds(mxGraphics2DCanvas var1, mxCellState var2) {
      Map var3 = var2.getStyle();
      double var4 = var1.getScale();
      String var6 = mxUtils.getString(var3, mxConstants.STYLE_IMAGE_ALIGN, "center");
      String var7 = mxUtils.getString(var3, mxConstants.STYLE_IMAGE_VERTICAL_ALIGN, "middle");
      int var8 = (int)((double)mxUtils.getInt(var3, mxConstants.STYLE_IMAGE_WIDTH, mxConstants.DEFAULT_IMAGESIZE) * var4);
      int var9 = (int)((double)mxUtils.getInt(var3, mxConstants.STYLE_IMAGE_HEIGHT, mxConstants.DEFAULT_IMAGESIZE) * var4);
      int var10 = (int)((double)mxUtils.getInt(var3, mxConstants.STYLE_SPACING, 2) * var4);
      mxRectangle var11 = new mxRectangle(var2);
      if (var6.equals("left")) {
         var11.setX(var11.getX() + (double)var10);
      } else if (var6.equals("right")) {
         var11.setX(var11.getX() + var11.getWidth() - (double)var8 - (double)var10);
      } else {
         var11.setX(var11.getX() + (var11.getWidth() - (double)var8) / 2.0);
      }

      if (var7.equals("top")) {
         var11.setY(var11.getY() + (double)var10);
      } else if (var7.equals("bottom")) {
         var11.setY(var11.getY() + var11.getHeight() - (double)var9 - (double)var10);
      } else {
         var11.setY(var11.getY() + (var11.getHeight() - (double)var9) / 2.0);
      }

      return var11.getRectangle();
   }

   public Color getFillColor(mxGraphics2DCanvas var1, mxCellState var2) {
      return mxUtils.getColor(var2.getStyle(), mxConstants.STYLE_FILLCOLOR);
   }

   public Color getStrokeColor(mxGraphics2DCanvas var1, mxCellState var2) {
      return mxUtils.getColor(var2.getStyle(), mxConstants.STYLE_STROKECOLOR);
   }
}
