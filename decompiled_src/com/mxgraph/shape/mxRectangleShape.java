package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Rectangle;
import java.util.Map;

public class mxRectangleShape extends mxBasicShape {
   public void paintShape(mxGraphics2DCanvas var1, mxCellState var2) {
      Map var3 = var2.getStyle();
      Rectangle var4;
      if (mxUtils.isTrue(var3, mxConstants.STYLE_ROUNDED, false)) {
         var4 = var2.getRectangle();
         int var5 = var4.x;
         int var6 = var4.y;
         int var7 = var4.width;
         int var8 = var4.height;
         int var9 = this.getArcSize(var7, var8);
         boolean var10 = this.hasShadow(var1, var2);
         int var11 = var10 ? mxConstants.SHADOW_OFFSETX : 0;
         int var12 = var10 ? mxConstants.SHADOW_OFFSETY : 0;
         if (var1.getGraphics().hitClip(var5, var6, var7 + var11, var8 + var12)) {
            if (var10) {
               var1.getGraphics().setColor(mxConstants.SHADOW_COLOR);
               var1.getGraphics().fillRoundRect(var5 + mxConstants.SHADOW_OFFSETX, var6 + mxConstants.SHADOW_OFFSETY, var7, var8, var9, var9);
            }

            if (this.configureGraphics(var1, var2, true)) {
               var1.getGraphics().fillRoundRect(var5, var6, var7, var8, var9, var9);
            }

            if (this.configureGraphics(var1, var2, false)) {
               var1.getGraphics().drawRoundRect(var5, var6, var7, var8, var9, var9);
            }
         }
      } else {
         var4 = var2.getRectangle();
         if (this.configureGraphics(var1, var2, true)) {
            var1.fillShape(var4, this.hasShadow(var1, var2));
         }

         if (this.configureGraphics(var1, var2, false)) {
            var1.getGraphics().drawRect(var4.x, var4.y, var4.width, var4.height);
         }
      }

   }

   public int getArcSize(int var1, int var2) {
      int var3;
      if (var1 <= var2) {
         var3 = (int)Math.round((double)var2 * mxConstants.RECTANGLE_ROUNDING_FACTOR);
         if (var3 > var1 / 2) {
            var3 = var1 / 2;
         }
      } else {
         var3 = (int)Math.round((double)var1 * mxConstants.RECTANGLE_ROUNDING_FACTOR);
         if (var3 > var2 / 2) {
            var3 = var2 / 2;
         }
      }

      return var3;
   }
}
