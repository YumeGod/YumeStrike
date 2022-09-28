package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Map;

public class mxDefaultTextShape implements mxITextShape {
   public void paintShape(mxGraphics2DCanvas var1, String var2, mxCellState var3, Map var4) {
      Rectangle var5 = var3.getLabelBounds().getRectangle();
      Graphics2D var6 = var1.getGraphics();
      if (var6.getClipBounds() == null || var6.getClipBounds().intersects(var5)) {
         boolean var7 = mxUtils.isTrue(var4, mxConstants.STYLE_HORIZONTAL, true);
         double var8 = var1.getScale();
         int var10 = var5.x;
         int var11 = var5.y;
         int var12 = var5.width;
         int var13 = var5.height;
         if (!var7) {
            var6.rotate(-1.5707963267948966, (double)(var10 + var12 / 2), (double)(var11 + var13 / 2));
            var6.translate(var12 / 2 - var13 / 2, var13 / 2 - var12 / 2);
         }

         Color var14 = mxUtils.getColor(var4, mxConstants.STYLE_FONTCOLOR, Color.black);
         var6.setColor(var14);
         Font var15 = mxUtils.getFont(var4, var8);
         var6.setFont(var15);
         int var16 = mxUtils.getInt(var4, mxConstants.STYLE_FONTSIZE, mxConstants.DEFAULT_FONTSIZE);
         FontMetrics var17 = var6.getFontMetrics();
         int var18 = var15.getSize();
         double var19 = (double)var18 / (double)var16;
         double var21 = var19 / var8;
         var11 = (int)((double)var11 + (double)(2 * var17.getMaxAscent() - var17.getHeight()) + (double)mxConstants.LABEL_INSET * var8);
         String var23 = mxUtils.getString(var4, mxConstants.STYLE_VERTICAL_ALIGN, "middle");
         double var24 = 0.5;
         if (var23.equals("top")) {
            var24 = 0.0;
         } else if (var23.equals("bottom")) {
            var24 = 1.0;
         }

         var11 = (int)((double)var11 + (1.0 - var21) * (double)var13 * var24);
         String var26 = mxUtils.getString(var4, mxConstants.STYLE_ALIGN, "center");
         if (var26.equals("left")) {
            var10 = (int)((double)var10 + (double)mxConstants.LABEL_INSET * var8);
         } else if (var26.equals("right")) {
            var10 = (int)((double)var10 - (double)mxConstants.LABEL_INSET * var8);
         }

         String[] var27;
         if (mxUtils.getString(var4, mxConstants.STYLE_WHITE_SPACE, "nowrap").equals("wrap")) {
            Font var28 = var6.getFont();
            var6.setFont(mxUtils.getFont(var4, 1.0));
            FontMetrics var29 = var6.getFontMetrics();
            var27 = mxUtils.wordWrap(var2, var29, (int)(var3.getLabelBounds().getWidth() / var8 + 0.5) - mxConstants.LABEL_INSET * 2);
            var6.setFont(var28);
         } else {
            var27 = var2.split("\n");
         }

         for(int var31 = 0; var31 < var27.length; ++var31) {
            int var32 = 0;
            int var30;
            if (var26.equals("center")) {
               var30 = var17.stringWidth(var27[var31]);
               if (var7) {
                  var32 = (var12 - var30) / 2;
               } else {
                  var32 = (var13 - var30) / 2;
               }
            } else if (var26.equals("right")) {
               var30 = var17.stringWidth(var27[var31]);
               var32 = (var7 ? var12 : var13) - var30;
            }

            var6.drawString(var27[var31], var10 + var32, var11);
            this.postProcessLine(var2, var27[var31], var17, var1, var10 + var32, var11);
            var11 += var17.getHeight() + mxConstants.LINESPACING;
         }
      }

   }

   protected void postProcessLine(String var1, String var2, FontMetrics var3, mxGraphics2DCanvas var4, int var5, int var6) {
   }
}
