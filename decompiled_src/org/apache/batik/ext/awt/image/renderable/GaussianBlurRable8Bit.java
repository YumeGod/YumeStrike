package org.apache.batik.ext.awt.image.renderable;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.rendered.AffineRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.GaussianBlurRed8Bit;
import org.apache.batik.ext.awt.image.rendered.PadRed;

public class GaussianBlurRable8Bit extends AbstractColorInterpolationRable implements GaussianBlurRable {
   private double stdDeviationX;
   private double stdDeviationY;
   static final double DSQRT2PI = Math.sqrt(6.283185307179586) * 3.0 / 4.0;
   public static final double eps = 1.0E-4;

   public GaussianBlurRable8Bit(Filter var1, double var2, double var4) {
      super((Filter)var1, (Map)null);
      this.setStdDeviationX(var2);
      this.setStdDeviationY(var4);
   }

   public void setStdDeviationX(double var1) {
      if (var1 < 0.0) {
         throw new IllegalArgumentException();
      } else {
         this.touch();
         this.stdDeviationX = var1;
      }
   }

   public void setStdDeviationY(double var1) {
      if (var1 < 0.0) {
         throw new IllegalArgumentException();
      } else {
         this.touch();
         this.stdDeviationY = var1;
      }
   }

   public double getStdDeviationX() {
      return this.stdDeviationX;
   }

   public double getStdDeviationY() {
      return this.stdDeviationY;
   }

   public void setSource(Filter var1) {
      this.init(var1, (Map)null);
   }

   public Rectangle2D getBounds2D() {
      Rectangle2D var1 = this.getSource().getBounds2D();
      float var2 = (float)(this.stdDeviationX * DSQRT2PI);
      float var3 = (float)(this.stdDeviationY * DSQRT2PI);
      float var4 = 3.0F * var2 / 2.0F;
      float var5 = 3.0F * var3 / 2.0F;
      return new Rectangle2D.Float((float)(var1.getMinX() - (double)var4), (float)(var1.getMinY() - (double)var5), (float)(var1.getWidth() + (double)(2.0F * var4)), (float)(var1.getHeight() + (double)(2.0F * var5)));
   }

   public Filter getSource() {
      return (Filter)this.getSources().get(0);
   }

   public static boolean eps_eq(double var0, double var2) {
      return var0 >= var2 - 1.0E-4 && var0 <= var2 + 1.0E-4;
   }

   public static boolean eps_abs_eq(double var0, double var2) {
      if (var0 < 0.0) {
         var0 = -var0;
      }

      if (var2 < 0.0) {
         var2 = -var2;
      }

      return eps_eq(var0, var2);
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
      double var20 = this.stdDeviationX * var16;
      double var22 = this.stdDeviationY * var18;
      AffineTransform var24;
      AffineTransform var25;
      int var26;
      int var27;
      if (var20 < 10.0 && var22 < 10.0 && eps_eq(var20, var22) && eps_abs_eq(var4 / var16, var6 / var18)) {
         var24 = var3;
         var25 = null;
         var26 = 0;
         var27 = 0;
      } else {
         if (var20 > 10.0) {
            var16 = var16 * 10.0 / var20;
            var20 = 10.0;
         }

         if (var22 > 10.0) {
            var18 = var18 * 10.0 / var22;
            var22 = 10.0;
         }

         var24 = AffineTransform.getScaleInstance(var16, var18);
         var25 = new AffineTransform(var4 / var16, var10 / var16, var8 / var18, var6 / var18, var12, var14);
         var26 = 1;
         var27 = 1;
      }

      Object var28 = var1.getAreaOfInterest();
      if (var28 == null) {
         var28 = this.getBounds2D();
      }

      Shape var29 = var24.createTransformedShape((Shape)var28);
      Rectangle var30 = var29.getBounds();
      var26 += GaussianBlurRed8Bit.surroundPixels(var20, var2);
      var27 += GaussianBlurRed8Bit.surroundPixels(var22, var2);
      var30.x -= var26;
      var30.y -= var27;
      var30.width += 2 * var26;
      var30.height += 2 * var27;

      Object var35;
      try {
         AffineTransform var32 = var24.createInverse();
         var35 = var32.createTransformedShape(var30).getBounds2D();
      } catch (NoninvertibleTransformException var34) {
         Rectangle2D var31 = ((Shape)var28).getBounds2D();
         var35 = new Rectangle2D.Double(var31.getX() - (double)var26 / var16, var31.getY() - (double)var27 / var18, var31.getWidth() + (double)(2 * var26) / var16, var31.getHeight() + (double)(2 * var27) / var18);
      }

      RenderedImage var36 = this.getSource().createRendering(new RenderContext(var24, (Shape)var35, var2));
      if (var36 == null) {
         return null;
      } else {
         Object var33 = this.convertSourceCS(var36);
         if (!var30.equals(((CachableRed)var33).getBounds())) {
            var33 = new PadRed((CachableRed)var33, var30, PadMode.ZERO_PAD, var2);
         }

         var33 = new GaussianBlurRed8Bit((CachableRed)var33, var20, var22, var2);
         if (var25 != null && !var25.isIdentity()) {
            var33 = new AffineRed((CachableRed)var33, var25, var2);
         }

         return (RenderedImage)var33;
      }
   }

   public Shape getDependencyRegion(int var1, Rectangle2D var2) {
      if (var1 != 0) {
         var2 = null;
      } else {
         float var3 = (float)(this.stdDeviationX * DSQRT2PI);
         float var4 = (float)(this.stdDeviationY * DSQRT2PI);
         float var5 = 3.0F * var3 / 2.0F;
         float var6 = 3.0F * var4 / 2.0F;
         Rectangle2D.Float var8 = new Rectangle2D.Float((float)(var2.getMinX() - (double)var5), (float)(var2.getMinY() - (double)var6), (float)(var2.getWidth() + (double)(2.0F * var5)), (float)(var2.getHeight() + (double)(2.0F * var6)));
         Rectangle2D var7 = this.getBounds2D();
         if (!var8.intersects(var7)) {
            return new Rectangle2D.Float();
         }

         var2 = var8.createIntersection(var7);
      }

      return var2;
   }

   public Shape getDirtyRegion(int var1, Rectangle2D var2) {
      Rectangle2D var3 = null;
      if (var1 == 0) {
         float var4 = (float)(this.stdDeviationX * DSQRT2PI);
         float var5 = (float)(this.stdDeviationY * DSQRT2PI);
         float var6 = 3.0F * var4 / 2.0F;
         float var7 = 3.0F * var5 / 2.0F;
         Rectangle2D.Float var9 = new Rectangle2D.Float((float)(var2.getMinX() - (double)var6), (float)(var2.getMinY() - (double)var7), (float)(var2.getWidth() + (double)(2.0F * var6)), (float)(var2.getHeight() + (double)(2.0F * var7)));
         Rectangle2D var8 = this.getBounds2D();
         if (!var9.intersects(var8)) {
            return new Rectangle2D.Float();
         }

         var3 = var9.createIntersection(var8);
      }

      return var3;
   }
}
