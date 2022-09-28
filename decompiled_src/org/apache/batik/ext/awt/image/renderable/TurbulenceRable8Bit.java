package org.apache.batik.ext.awt.image.renderable;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import org.apache.batik.ext.awt.image.rendered.TurbulencePatternRed;

public class TurbulenceRable8Bit extends AbstractColorInterpolationRable implements TurbulenceRable {
   int seed = 0;
   int numOctaves = 1;
   double baseFreqX = 0.0;
   double baseFreqY = 0.0;
   boolean stitched = false;
   boolean fractalNoise = false;
   Rectangle2D region;

   public TurbulenceRable8Bit(Rectangle2D var1) {
      this.region = var1;
   }

   public TurbulenceRable8Bit(Rectangle2D var1, int var2, int var3, double var4, double var6, boolean var8, boolean var9) {
      this.seed = var2;
      this.numOctaves = var3;
      this.baseFreqX = var4;
      this.baseFreqY = var6;
      this.stitched = var8;
      this.fractalNoise = var9;
      this.region = var1;
   }

   public Rectangle2D getTurbulenceRegion() {
      return (Rectangle2D)this.region.clone();
   }

   public Rectangle2D getBounds2D() {
      return (Rectangle2D)this.region.clone();
   }

   public int getSeed() {
      return this.seed;
   }

   public int getNumOctaves() {
      return this.numOctaves;
   }

   public double getBaseFrequencyX() {
      return this.baseFreqX;
   }

   public double getBaseFrequencyY() {
      return this.baseFreqY;
   }

   public boolean isStitched() {
      return this.stitched;
   }

   public boolean isFractalNoise() {
      return this.fractalNoise;
   }

   public void setTurbulenceRegion(Rectangle2D var1) {
      this.touch();
      this.region = var1;
   }

   public void setSeed(int var1) {
      this.touch();
      this.seed = var1;
   }

   public void setNumOctaves(int var1) {
      this.touch();
      this.numOctaves = var1;
   }

   public void setBaseFrequencyX(double var1) {
      this.touch();
      this.baseFreqX = var1;
   }

   public void setBaseFrequencyY(double var1) {
      this.touch();
      this.baseFreqY = var1;
   }

   public void setStitched(boolean var1) {
      this.touch();
      this.stitched = var1;
   }

   public void setFractalNoise(boolean var1) {
      this.touch();
      this.fractalNoise = var1;
   }

   public RenderedImage createRendering(RenderContext var1) {
      Shape var3 = var1.getAreaOfInterest();
      Rectangle2D var2;
      if (var3 == null) {
         var2 = this.getBounds2D();
      } else {
         Rectangle2D var4 = this.getBounds2D();
         var2 = var3.getBounds2D();
         if (!var2.intersects(var4)) {
            return null;
         }

         Rectangle2D.intersect(var2, var4, var2);
      }

      AffineTransform var11 = var1.getTransform();
      Rectangle var5 = var11.createTransformedShape(var2).getBounds();
      if (var5.width > 0 && var5.height > 0) {
         ColorSpace var6 = this.getOperationColorSpace();
         Rectangle2D var7 = null;
         if (this.stitched) {
            var7 = (Rectangle2D)this.region.clone();
         }

         AffineTransform var8 = new AffineTransform();

         try {
            var8 = var11.createInverse();
         } catch (NoninvertibleTransformException var10) {
         }

         return new TurbulencePatternRed(this.baseFreqX, this.baseFreqY, this.numOctaves, this.seed, this.fractalNoise, var7, var8, var5, var6, true);
      } else {
         return null;
      }
   }
}
