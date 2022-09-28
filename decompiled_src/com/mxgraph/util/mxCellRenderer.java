package com.mxgraph.util;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxHtmlCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.canvas.mxVmlCanvas;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import com.mxgraph.view.mxTemporaryCellStates;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.w3c.dom.Document;

public class mxCellRenderer {
   private mxCellRenderer() {
   }

   public static mxICanvas drawCells(mxGraph var0, Object[] var1, double var2, mxRectangle var4, CanvasFactory var5) {
      mxICanvas var6 = null;
      if (var1 == null) {
         var1 = new Object[]{var0.getModel().getRoot()};
      }

      if (var1 != null) {
         mxGraphView var7 = var0.getView();
         boolean var8 = var7.isEventsEnabled();
         var7.setEventsEnabled(false);
         mxTemporaryCellStates var9 = new mxTemporaryCellStates(var7, var2, var1);

         try {
            if (var4 == null) {
               var4 = var0.getPaintBounds(var1);
            }

            if (var4 != null && var4.getWidth() > 0.0 && var4.getHeight() > 0.0) {
               Rectangle var10 = var4.getRectangle();
               var6 = var5.createCanvas(var10.width + 1, var10.height + 1);
               if (var6 != null) {
                  double var11 = var6.getScale();
                  Point var13 = var6.getTranslate();

                  try {
                     var6.setTranslate(-var10.x, -var10.y);
                     var6.setScale(var7.getScale());

                     for(int var14 = 0; var14 < var1.length; ++var14) {
                        var0.drawCell(var6, var1[var14]);
                     }
                  } finally {
                     var6.setScale(var11);
                     var6.setTranslate(var13.x, var13.y);
                  }
               }
            }
         } finally {
            var9.destroy();
            var7.setEventsEnabled(var8);
         }
      }

      return var6;
   }

   public static BufferedImage createBufferedImage(mxGraph var0, Object[] var1, double var2, Color var4, boolean var5, mxRectangle var6) {
      return createBufferedImage(var0, var1, var2, var4, var5, var6, new mxGraphics2DCanvas());
   }

   public static BufferedImage createBufferedImage(mxGraph var0, Object[] var1, double var2, final Color var4, final boolean var5, mxRectangle var6, final mxGraphics2DCanvas var7) {
      mxImageCanvas var8 = (mxImageCanvas)drawCells(var0, var1, var2, var6, new CanvasFactory() {
         public mxICanvas createCanvas(int var1, int var2) {
            return new mxImageCanvas(var7, var1, var2, var4, var5);
         }
      });
      return var8 != null ? var8.destroy() : null;
   }

   public static Document createHtmlDocument(mxGraph var0, Object[] var1, double var2, Color var4, mxRectangle var5) {
      mxHtmlCanvas var6 = (mxHtmlCanvas)drawCells(var0, var1, var2, var5, new CanvasFactory() {
         public mxICanvas createCanvas(int var1, int var2) {
            return new mxHtmlCanvas(mxUtils.createHtmlDocument());
         }
      });
      return var6.getDocument();
   }

   public static Document createSvgDocument(mxGraph var0, Object[] var1, double var2, Color var4, mxRectangle var5) {
      mxSvgCanvas var6 = (mxSvgCanvas)drawCells(var0, var1, var2, var5, new CanvasFactory() {
         public mxICanvas createCanvas(int var1, int var2) {
            return new mxSvgCanvas(mxUtils.createSvgDocument(var1, var2));
         }
      });
      return var6.getDocument();
   }

   public static Document createVmlDocument(mxGraph var0, Object[] var1, double var2, Color var4, mxRectangle var5) {
      mxVmlCanvas var6 = (mxVmlCanvas)drawCells(var0, var1, var2, var5, new CanvasFactory() {
         public mxICanvas createCanvas(int var1, int var2) {
            return new mxVmlCanvas(mxUtils.createVmlDocument());
         }
      });
      return var6.getDocument();
   }

   public abstract static class CanvasFactory {
      public abstract mxICanvas createCanvas(int var1, int var2);
   }
}
