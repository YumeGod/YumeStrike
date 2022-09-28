package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Rectangle;

public class mxSwimlaneShape extends mxBasicShape {
   public void paintShape(mxGraphics2DCanvas var1, mxCellState var2) {
      int var3 = (int)Math.round((double)mxUtils.getInt(var2.getStyle(), mxConstants.STYLE_STARTSIZE, mxConstants.DEFAULT_STARTSIZE) * var1.getScale());
      Rectangle var4 = var2.getRectangle();
      if (mxUtils.isTrue(var2.getStyle(), mxConstants.STYLE_HORIZONTAL, true)) {
         if (this.configureGraphics(var1, var2, true)) {
            var1.fillShape(new Rectangle(var4.x, var4.y, var4.width, Math.min(var4.height, var3)));
         }

         if (this.configureGraphics(var1, var2, false)) {
            var1.getGraphics().drawRect(var4.x, var4.y, var4.width, Math.min(var4.height, var3));
            var1.getGraphics().drawRect(var4.x, var4.y + var3, var4.width, var4.height - var3);
         }
      } else {
         if (this.configureGraphics(var1, var2, true)) {
            var1.fillShape(new Rectangle(var4.x, var4.y, Math.min(var4.width, var3), var4.height));
         }

         if (this.configureGraphics(var1, var2, false)) {
            var1.getGraphics().drawRect(var4.x, var4.y, Math.min(var4.width, var3), var4.height);
            var1.getGraphics().drawRect(var4.x + var3, var4.y, var4.width - var3, var4.height);
         }
      }

   }
}
