package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.util.Map;

public class mxBasicShape implements mxIShape {
   public void paintShape(mxGraphics2DCanvas var1, mxCellState var2) {
      Shape var3 = this.createShape(var1, var2);
      if (var3 != null) {
         if (this.configureGraphics(var1, var2, true)) {
            var1.fillShape(var3, this.hasShadow(var1, var2));
         }

         if (this.configureGraphics(var1, var2, false)) {
            var1.getGraphics().draw(var3);
         }
      }

   }

   public Shape createShape(mxGraphics2DCanvas var1, mxCellState var2) {
      return null;
   }

   protected boolean configureGraphics(mxGraphics2DCanvas var1, mxCellState var2, boolean var3) {
      Map var4 = var2.getStyle();
      if (var3) {
         Paint var7 = var1.createFillPaint(var2, var4);
         if (var7 != null) {
            var1.getGraphics().setPaint(var7);
            return true;
         } else {
            Color var6 = this.getFillColor(var1, var2);
            var1.getGraphics().setColor(var6);
            return var6 != null;
         }
      } else {
         var1.getGraphics().setPaint((Paint)null);
         Color var5 = this.getStrokeColor(var1, var2);
         var1.getGraphics().setColor(var5);
         var1.getGraphics().setStroke(var1.createStroke(var4));
         return var5 != null;
      }
   }

   public boolean hasShadow(mxGraphics2DCanvas var1, mxCellState var2) {
      return mxUtils.isTrue(var2.getStyle(), mxConstants.STYLE_SHADOW, false);
   }

   public Color getFillColor(mxGraphics2DCanvas var1, mxCellState var2) {
      return mxUtils.getColor(var2.getStyle(), mxConstants.STYLE_FILLCOLOR);
   }

   public Color getStrokeColor(mxGraphics2DCanvas var1, mxCellState var2) {
      return mxUtils.getColor(var2.getStyle(), mxConstants.STYLE_STROKECOLOR);
   }
}
