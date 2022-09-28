package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxLightweightLabel;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Map;
import javax.swing.CellRendererPane;

public class mxHtmlTextShape implements mxITextShape {
   public void paintShape(mxGraphics2DCanvas var1, String var2, mxCellState var3, Map var4) {
      mxLightweightLabel var5 = mxLightweightLabel.getSharedInstance();
      CellRendererPane var6 = var1.getRendererPane();
      Rectangle var7 = var3.getLabelBounds().getRectangle();
      Graphics2D var8 = var1.getGraphics();
      if (var5 != null && var6 != null && (var8.getClipBounds() == null || var8.getClipBounds().intersects(var7))) {
         double var9 = var1.getScale();
         int var11 = var7.x;
         int var12 = var7.y;
         int var13 = var7.width;
         int var14 = var7.height;
         if (!mxUtils.isTrue(var4, mxConstants.STYLE_HORIZONTAL, true)) {
            var8.rotate(-1.5707963267948966, (double)(var11 + var13 / 2), (double)(var12 + var14 / 2));
            var8.translate(var13 / 2 - var14 / 2, var14 / 2 - var13 / 2);
            int var15 = var13;
            var13 = var14;
            var14 = var15;
         }

         var5.setText(mxUtils.createHtmlDocument(var4, var2));
         var5.setFont(mxUtils.getFont(var4, var1.getScale()));
         var8.scale(var9, var9);
         var6.paintComponent(var8, var5, var6, (int)((double)var11 / var9) + mxConstants.LABEL_INSET, (int)((double)var12 / var9) + mxConstants.LABEL_INSET, (int)((double)var13 / var9), (int)((double)var14 / var9), true);
      }

   }
}
