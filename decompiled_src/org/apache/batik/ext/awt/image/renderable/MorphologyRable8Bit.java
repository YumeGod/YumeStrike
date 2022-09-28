package org.apache.batik.ext.awt.image.renderable;

import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.RenderContext;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.rendered.AffineRed;
import org.apache.batik.ext.awt.image.rendered.BufferedImageCachableRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.MorphologyOp;
import org.apache.batik.ext.awt.image.rendered.PadRed;
import org.apache.batik.ext.awt.image.rendered.RenderedImageCachableRed;

public class MorphologyRable8Bit extends AbstractRable implements MorphologyRable {
   private double radiusX;
   private double radiusY;
   private boolean doDilation;

   public MorphologyRable8Bit(Filter var1, double var2, double var4, boolean var6) {
      super((Filter)var1, (Map)null);
      this.setRadiusX(var2);
      this.setRadiusY(var4);
      this.setDoDilation(var6);
   }

   public Filter getSource() {
      return (Filter)this.getSources().get(0);
   }

   public void setSource(Filter var1) {
      this.init(var1, (Map)null);
   }

   public Rectangle2D getBounds2D() {
      return this.getSource().getBounds2D();
   }

   public void setRadiusX(double var1) {
      if (var1 <= 0.0) {
         throw new IllegalArgumentException();
      } else {
         this.touch();
         this.radiusX = var1;
      }
   }

   public void setRadiusY(double var1) {
      if (var1 <= 0.0) {
         throw new IllegalArgumentException();
      } else {
         this.touch();
         this.radiusY = var1;
      }
   }

   public void setDoDilation(boolean var1) {
      this.touch();
      this.doDilation = var1;
   }

   public boolean getDoDilation() {
      return this.doDilation;
   }

   public double getRadiusX() {
      return this.radiusX;
   }

   public double getRadiusY() {
      return this.radiusY;
   }

   public RenderedImage createRendering(RenderContext var1) {
      RenderingHints var2 = var1.getRenderingHints();
      if (var2 == null) {
         var2 = new RenderingHints((Map)null);
      }

      AffineTransform var3 = var1.getTransform();
      double var4 = var3.getScaleX();
      double var6 = var3.getScaleY();
      double var8 = var3.getShearX();
      double var10 = var3.getShearY();
      double var12 = var3.getTranslateX();
      double var14 = var3.getTranslateY();
      double var16 = Math.sqrt(var4 * var4 + var10 * var10);
      double var18 = Math.sqrt(var6 * var6 + var8 * var8);
      AffineTransform var20 = AffineTransform.getScaleInstance(var16, var18);
      int var21 = (int)Math.round(this.radiusX * var16);
      int var22 = (int)Math.round(this.radiusY * var18);
      MorphologyOp var23 = null;
      if (var21 > 0 && var22 > 0) {
         var23 = new MorphologyOp(var21, var22, this.doDilation);
      }

      AffineTransform var24 = new AffineTransform(var4 / var16, var10 / var16, var8 / var18, var6 / var18, var12, var14);
      Object var25 = var1.getAreaOfInterest();
      if (var25 == null) {
         var25 = this.getBounds2D();
      }

      Rectangle2D var26 = ((Shape)var25).getBounds2D();
      Rectangle2D.Double var38 = new Rectangle2D.Double(var26.getX() - (double)var21 / var16, var26.getY() - (double)var22 / var18, var26.getWidth() + (double)(2 * var21) / var16, var26.getHeight() + (double)(2 * var22) / var18);
      RenderedImage var27 = this.getSource().createRendering(new RenderContext(var20, var38, var2));
      if (var27 == null) {
         return null;
      } else {
         RenderedImageCachableRed var28 = new RenderedImageCachableRed(var27);
         Shape var29 = var20.createTransformedShape(((Shape)var25).getBounds2D());
         var26 = var29.getBounds2D();
         var38 = new Rectangle2D.Double(var26.getX() - (double)var21, var26.getY() - (double)var22, var26.getWidth() + (double)(2 * var21), var26.getHeight() + (double)(2 * var22));
         PadRed var39 = new PadRed(var28, var38.getBounds(), PadMode.ZERO_PAD, var2);
         ColorModel var30 = var27.getColorModel();
         Raster var31 = var39.getData();
         Point var32 = new Point(0, 0);
         WritableRaster var33 = Raster.createWritableRaster(var31.getSampleModel(), var31.getDataBuffer(), var32);
         BufferedImage var34 = new BufferedImage(var30, var33, var30.isAlphaPremultiplied(), (Hashtable)null);
         BufferedImage var35;
         if (var23 != null) {
            var35 = var23.filter((BufferedImage)var34, (BufferedImage)null);
         } else {
            var35 = var34;
         }

         int var36 = var39.getMinX();
         int var37 = var39.getMinY();
         Object var40 = new BufferedImageCachableRed(var35, var36, var37);
         if (!var24.isIdentity()) {
            var40 = new AffineRed((CachableRed)var40, var24, var2);
         }

         return (RenderedImage)var40;
      }
   }

   public Shape getDependencyRegion(int var1, Rectangle2D var2) {
      return super.getDependencyRegion(var1, var2);
   }

   public Shape getDirtyRegion(int var1, Rectangle2D var2) {
      return super.getDirtyRegion(var1, var2);
   }
}
