package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class mxCylinderShape extends mxBasicShape {
   public void paintShape(mxGraphics2DCanvas var1, mxCellState var2) {
      Rectangle var3 = var2.getRectangle();
      int var4 = var3.x;
      int var5 = var3.y;
      int var6 = var3.width;
      int var7 = var3.height;
      int var8 = var7 / 4;
      int var9 = var8 / 2;
      if (this.configureGraphics(var1, var2, true)) {
         Area var11 = new Area(new Rectangle(var4, var5 + var8 / 2, var6, var7 - var8));
         var11.add(new Area(new Rectangle(var4, var5 + var8 / 2, var6, var7 - var8)));
         var11.add(new Area(new Ellipse2D.Float((float)var4, (float)var5, (float)var6, (float)var8)));
         var11.add(new Area(new Ellipse2D.Float((float)var4, (float)(var5 + var7 - var8), (float)var6, (float)var8)));
         var1.fillShape(var11, this.hasShadow(var1, var2));
      }

      if (this.configureGraphics(var1, var2, false)) {
         var1.getGraphics().drawOval(var4, var5, var6, var8);
         var1.getGraphics().drawLine(var4, var5 + var9, var4, var5 + var7 - var9);
         var1.getGraphics().drawLine(var4 + var6, var5 + var9, var4 + var6, var5 + var7 - var9);
         var1.getGraphics().drawArc(var4, var5 + var7 - var8, var6, var8, 0, -180);
      }

   }
}
