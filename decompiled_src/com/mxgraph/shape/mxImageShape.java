package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Color;
import java.awt.Rectangle;

public class mxImageShape extends mxRectangleShape {
   public void paintShape(mxGraphics2DCanvas var1, mxCellState var2) {
      super.paintShape(var1, var2);
      var1.drawImage(this.getImageBounds(var1, var2), this.getImageForStyle(var1, var2));
   }

   public Rectangle getImageBounds(mxGraphics2DCanvas var1, mxCellState var2) {
      return var2.getRectangle();
   }

   public String getImageForStyle(mxGraphics2DCanvas var1, mxCellState var2) {
      return var1.getImageForStyle(var2.getStyle());
   }

   public Color getFillColor(mxGraphics2DCanvas var1, mxCellState var2) {
      return mxUtils.getColor(var2.getStyle(), mxConstants.STYLE_IMAGE_BACKGROUND);
   }

   public Color getStrokeColor(mxGraphics2DCanvas var1, mxCellState var2) {
      return mxUtils.getColor(var2.getStyle(), mxConstants.STYLE_IMAGE_BORDER);
   }
}
