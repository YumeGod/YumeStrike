package org.apache.batik.ext.awt.image.renderable;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.rendered.FloodRed;
import org.apache.batik.ext.awt.image.rendered.PadRed;

public class FloodRable8Bit extends AbstractRable implements FloodRable {
   Paint floodPaint;
   Rectangle2D floodRegion;

   public FloodRable8Bit(Rectangle2D var1, Paint var2) {
      this.setFloodPaint(var2);
      this.setFloodRegion(var1);
   }

   public void setFloodPaint(Paint var1) {
      this.touch();
      if (var1 == null) {
         this.floodPaint = new Color(0, 0, 0, 0);
      } else {
         this.floodPaint = var1;
      }

   }

   public Paint getFloodPaint() {
      return this.floodPaint;
   }

   public Rectangle2D getBounds2D() {
      return (Rectangle2D)this.floodRegion.clone();
   }

   public Rectangle2D getFloodRegion() {
      return (Rectangle2D)this.floodRegion.clone();
   }

   public void setFloodRegion(Rectangle2D var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.touch();
         this.floodRegion = var1;
      }
   }

   public RenderedImage createRendering(RenderContext var1) {
      AffineTransform var2 = var1.getTransform();
      if (var2 == null) {
         var2 = new AffineTransform();
      }

      Rectangle2D var3 = this.getBounds2D();
      Shape var5 = var1.getAreaOfInterest();
      Rectangle2D var4;
      if (var5 == null) {
         var4 = var3;
      } else {
         var4 = var5.getBounds2D();
         if (!var3.intersects(var4)) {
            return null;
         }

         Rectangle2D.intersect(var3, var4, var4);
      }

      Rectangle var6 = var2.createTransformedShape(var4).getBounds();
      if (var6.width > 0 && var6.height > 0) {
         FloodRed var7 = new FloodRed(var6, this.getFloodPaint());
         PadRed var8 = new PadRed(var7, var6, PadMode.ZERO_PAD, (RenderingHints)null);
         return var8;
      } else {
         return null;
      }
   }
}
