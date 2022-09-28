package org.apache.batik.extension.svg;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.image.LinearTransfer;
import org.apache.batik.ext.awt.image.TransferFunction;
import org.apache.batik.ext.awt.image.renderable.AbstractColorInterpolationRable;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.rendered.ComponentTransferRed;

public class BatikHistogramNormalizationFilter8Bit extends AbstractColorInterpolationRable implements BatikHistogramNormalizationFilter {
   private float trim = 0.01F;
   protected int[] histo = null;
   protected float slope;
   protected float intercept;

   public void setSource(Filter var1) {
      this.init(var1, (Map)null);
   }

   public Filter getSource() {
      return (Filter)this.getSources().get(0);
   }

   public float getTrim() {
      return this.trim;
   }

   public void setTrim(float var1) {
      this.trim = var1;
      this.touch();
   }

   public BatikHistogramNormalizationFilter8Bit(Filter var1, float var2) {
      this.setSource(var1);
      this.setTrim(var2);
   }

   public void computeHistogram(RenderContext var1) {
      if (this.histo == null) {
         Filter var2 = this.getSource();
         float var3 = 100.0F / var2.getWidth();
         float var4 = 100.0F / var2.getHeight();
         if (var3 > var4) {
            var3 = var4;
         }

         AffineTransform var5 = AffineTransform.getScaleInstance((double)var3, (double)var3);
         var1 = new RenderContext(var5, var1.getRenderingHints());
         RenderedImage var6 = this.getSource().createRendering(var1);
         this.histo = (new HistogramRed(this.convertSourceCS(var6))).getHistogram();
         int var7 = (int)((double)((float)(var6.getWidth() * var6.getHeight()) * this.trim) + 0.5);
         int var8 = 0;

         int var9;
         for(var9 = 0; var9 < 255; ++var9) {
            var8 += this.histo[var9];
            if (var8 >= var7) {
               break;
            }
         }

         int var10 = var9;
         var8 = 0;

         for(var9 = 255; var9 > 0; --var9) {
            var8 += this.histo[var9];
            if (var8 >= var7) {
               break;
            }
         }

         this.slope = 255.0F / (float)(var9 - var10);
         this.intercept = this.slope * (float)(-var10) / 255.0F;
      }
   }

   public RenderedImage createRendering(RenderContext var1) {
      RenderedImage var2 = this.getSource().createRendering(var1);
      if (var2 == null) {
         return null;
      } else {
         this.computeHistogram(var1);
         SampleModel var3 = var2.getSampleModel();
         int var4 = var3.getNumBands();
         TransferFunction[] var5 = new TransferFunction[var4];
         LinearTransfer var6 = new LinearTransfer(this.slope, this.intercept);

         for(int var7 = 0; var7 < var5.length; ++var7) {
            var5[var7] = var6;
         }

         return new ComponentTransferRed(this.convertSourceCS(var2), var5, (RenderingHints)null);
      }
   }
}
