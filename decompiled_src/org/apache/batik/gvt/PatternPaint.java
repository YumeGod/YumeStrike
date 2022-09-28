package org.apache.batik.gvt;

import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;

public class PatternPaint implements Paint {
   private GraphicsNode node;
   private Rectangle2D patternRegion;
   private AffineTransform patternTransform;
   private Filter tile;
   private boolean overflow;
   private PatternPaintContext lastContext;

   public PatternPaint(GraphicsNode var1, Rectangle2D var2, boolean var3, AffineTransform var4) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else if (var2 == null) {
         throw new IllegalArgumentException();
      } else {
         this.node = var1;
         this.patternRegion = var2;
         this.overflow = var3;
         this.patternTransform = var4;
         CompositeGraphicsNode var5 = new CompositeGraphicsNode();
         var5.getChildren().add(var1);
         Filter var6 = var5.getGraphicsNodeRable(true);
         Rectangle2D var7 = (Rectangle2D)var2.clone();
         if (var3) {
            Rectangle2D var8 = var5.getBounds();
            var7.add(var8);
         }

         this.tile = new PadRable8Bit(var6, var7, PadMode.ZERO_PAD);
      }
   }

   public GraphicsNode getGraphicsNode() {
      return this.node;
   }

   public Rectangle2D getPatternRect() {
      return (Rectangle2D)this.patternRegion.clone();
   }

   public AffineTransform getPatternTransform() {
      return this.patternTransform;
   }

   public boolean getOverflow() {
      return this.overflow;
   }

   public PaintContext createContext(ColorModel var1, Rectangle var2, Rectangle2D var3, AffineTransform var4, RenderingHints var5) {
      if (this.patternTransform != null) {
         var4 = new AffineTransform(var4);
         var4.concatenate(this.patternTransform);
      }

      if (this.lastContext != null && this.lastContext.getColorModel().equals(var1)) {
         double[] var6 = new double[6];
         double[] var7 = new double[6];
         var4.getMatrix(var6);
         this.lastContext.getUsr2Dev().getMatrix(var7);
         if (var6[0] == var7[0] && var6[1] == var7[1] && var6[2] == var7[2] && var6[3] == var7[3]) {
            if (var6[4] == var7[4] && var6[5] == var7[5]) {
               return this.lastContext;
            }

            return new PatternPaintContextWrapper(this.lastContext, (int)(var7[4] - var6[4] + 0.5), (int)(var7[5] - var6[5] + 0.5));
         }
      }

      this.lastContext = new PatternPaintContext(var1, var4, var5, this.tile, this.patternRegion, this.overflow);
      return this.lastContext;
   }

   public int getTransparency() {
      return 3;
   }

   static class PatternPaintContextWrapper implements PaintContext {
      PatternPaintContext ppc;
      int xShift;
      int yShift;

      PatternPaintContextWrapper(PatternPaintContext var1, int var2, int var3) {
         this.ppc = var1;
         this.xShift = var2;
         this.yShift = var3;
      }

      public void dispose() {
      }

      public ColorModel getColorModel() {
         return this.ppc.getColorModel();
      }

      public Raster getRaster(int var1, int var2, int var3, int var4) {
         return this.ppc.getRaster(var1 + this.xShift, var2 + this.yShift, var3, var4);
      }
   }
}
