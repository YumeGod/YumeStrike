package org.apache.batik.ext.awt.image.renderable;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.Light;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.rendered.AffineRed;
import org.apache.batik.ext.awt.image.rendered.BumpMap;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.DiffuseLightingRed;
import org.apache.batik.ext.awt.image.rendered.PadRed;

public class DiffuseLightingRable8Bit extends AbstractColorInterpolationRable implements DiffuseLightingRable {
   private double surfaceScale;
   private double kd;
   private Light light;
   private Rectangle2D litRegion;
   private float[] kernelUnitLength = null;

   public DiffuseLightingRable8Bit(Filter var1, Rectangle2D var2, Light var3, double var4, double var6, double[] var8) {
      super((Filter)var1, (Map)null);
      this.setLight(var3);
      this.setKd(var4);
      this.setSurfaceScale(var6);
      this.setLitRegion(var2);
      this.setKernelUnitLength(var8);
   }

   public Filter getSource() {
      return (Filter)this.getSources().get(0);
   }

   public void setSource(Filter var1) {
      this.init(var1, (Map)null);
   }

   public Rectangle2D getBounds2D() {
      return (Rectangle2D)this.litRegion.clone();
   }

   public Rectangle2D getLitRegion() {
      return this.getBounds2D();
   }

   public void setLitRegion(Rectangle2D var1) {
      this.touch();
      this.litRegion = var1;
   }

   public Light getLight() {
      return this.light;
   }

   public void setLight(Light var1) {
      this.touch();
      this.light = var1;
   }

   public double getSurfaceScale() {
      return this.surfaceScale;
   }

   public void setSurfaceScale(double var1) {
      this.touch();
      this.surfaceScale = var1;
   }

   public double getKd() {
      return this.kd;
   }

   public void setKd(double var1) {
      this.touch();
      this.kd = var1;
   }

   public double[] getKernelUnitLength() {
      if (this.kernelUnitLength == null) {
         return null;
      } else {
         double[] var1 = new double[]{(double)this.kernelUnitLength[0], (double)this.kernelUnitLength[1]};
         return var1;
      }
   }

   public void setKernelUnitLength(double[] var1) {
      this.touch();
      if (var1 == null) {
         this.kernelUnitLength = null;
      } else {
         if (this.kernelUnitLength == null) {
            this.kernelUnitLength = new float[2];
         }

         this.kernelUnitLength[0] = (float)var1[0];
         this.kernelUnitLength[1] = (float)var1[1];
      }
   }

   public RenderedImage createRendering(RenderContext var1) {
      Object var2 = var1.getAreaOfInterest();
      if (var2 == null) {
         var2 = this.getBounds2D();
      }

      Rectangle2D var3 = ((Shape)var2).getBounds2D();
      Rectangle2D.intersect(var3, this.getBounds2D(), var3);
      AffineTransform var4 = var1.getTransform();
      Rectangle var5 = var4.createTransformedShape(var3).getBounds();
      if (var5.width != 0 && var5.height != 0) {
         double var6 = var4.getScaleX();
         double var8 = var4.getScaleY();
         double var10 = var4.getShearX();
         double var12 = var4.getShearY();
         double var14 = var4.getTranslateX();
         double var16 = var4.getTranslateY();
         double var18 = Math.sqrt(var6 * var6 + var12 * var12);
         double var20 = Math.sqrt(var8 * var8 + var10 * var10);
         if (var18 != 0.0 && var20 != 0.0) {
            if (this.kernelUnitLength != null) {
               if (this.kernelUnitLength[0] > 0.0F && var18 > (double)(1.0F / this.kernelUnitLength[0])) {
                  var18 = (double)(1.0F / this.kernelUnitLength[0]);
               }

               if (this.kernelUnitLength[1] > 0.0F && var20 > (double)(1.0F / this.kernelUnitLength[1])) {
                  var20 = (double)(1.0F / this.kernelUnitLength[1]);
               }
            }

            AffineTransform var22 = AffineTransform.getScaleInstance(var18, var20);
            var5 = var22.createTransformedShape(var3).getBounds();
            var3.setRect(var3.getX() - 2.0 / var18, var3.getY() - 2.0 / var20, var3.getWidth() + 4.0 / var18, var3.getHeight() + 4.0 / var20);
            var1 = (RenderContext)var1.clone();
            var1.setAreaOfInterest(var3);
            var1.setTransform(var22);
            CachableRed var23 = GraphicsUtil.wrap(this.getSource().createRendering(var1));
            BumpMap var24 = new BumpMap(var23, this.surfaceScale, var18, var20);
            Object var28 = new DiffuseLightingRed(this.kd, this.light, var24, var5, 1.0 / var18, 1.0 / var20, this.isColorSpaceLinear());
            AffineTransform var25 = new AffineTransform(var6 / var18, var12 / var18, var10 / var20, var8 / var20, var14, var16);
            if (!var25.isIdentity()) {
               RenderingHints var26 = var1.getRenderingHints();
               Rectangle var27 = new Rectangle(var5.x - 1, var5.y - 1, var5.width + 2, var5.height + 2);
               PadRed var29 = new PadRed((CachableRed)var28, var27, PadMode.REPLICATE, var26);
               var28 = new AffineRed(var29, var25, var26);
            }

            return (RenderedImage)var28;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }
}
