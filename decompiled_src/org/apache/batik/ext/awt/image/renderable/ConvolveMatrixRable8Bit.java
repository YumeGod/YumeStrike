package org.apache.batik.ext.awt.image.renderable;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.RenderContext;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.rendered.AffineRed;
import org.apache.batik.ext.awt.image.rendered.BufferedImageCachableRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.PadRed;

public class ConvolveMatrixRable8Bit extends AbstractColorInterpolationRable implements ConvolveMatrixRable {
   Kernel kernel;
   Point target;
   float bias;
   boolean kernelHasNegValues;
   PadMode edgeMode;
   float[] kernelUnitLength = new float[2];
   boolean preserveAlpha = false;

   public ConvolveMatrixRable8Bit(Filter var1) {
      super(var1);
   }

   public Filter getSource() {
      return (Filter)this.getSources().get(0);
   }

   public void setSource(Filter var1) {
      this.init(var1);
   }

   public Kernel getKernel() {
      return this.kernel;
   }

   public void setKernel(Kernel var1) {
      this.touch();
      this.kernel = var1;
      this.kernelHasNegValues = false;
      float[] var2 = var1.getKernelData((float[])null);

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3] < 0.0F) {
            this.kernelHasNegValues = true;
            break;
         }
      }

   }

   public Point getTarget() {
      return (Point)this.target.clone();
   }

   public void setTarget(Point var1) {
      this.touch();
      this.target = (Point)var1.clone();
   }

   public double getBias() {
      return (double)this.bias;
   }

   public void setBias(double var1) {
      this.touch();
      this.bias = (float)var1;
   }

   public PadMode getEdgeMode() {
      return this.edgeMode;
   }

   public void setEdgeMode(PadMode var1) {
      this.touch();
      this.edgeMode = var1;
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

   public boolean getPreserveAlpha() {
      return this.preserveAlpha;
   }

   public void setPreserveAlpha(boolean var1) {
      this.touch();
      this.preserveAlpha = var1;
   }

   public void fixAlpha(BufferedImage var1) {
      if (var1.getColorModel().hasAlpha() && var1.isAlphaPremultiplied()) {
         if (GraphicsUtil.is_INT_PACK_Data(var1.getSampleModel(), true)) {
            this.fixAlpha_INT_PACK(var1.getRaster());
         } else {
            this.fixAlpha_FALLBACK(var1.getRaster());
         }

      }
   }

   public void fixAlpha_INT_PACK(WritableRaster var1) {
      SinglePixelPackedSampleModel var2 = (SinglePixelPackedSampleModel)var1.getSampleModel();
      int var3 = var1.getWidth();
      int var4 = var2.getScanlineStride();
      DataBufferInt var5 = (DataBufferInt)var1.getDataBuffer();
      int var6 = var5.getOffset() + var2.getOffset(var1.getMinX() - var1.getSampleModelTranslateX(), var1.getMinY() - var1.getSampleModelTranslateY());
      int[] var7 = var5.getBankData()[0];

      for(int var8 = 0; var8 < var1.getHeight(); ++var8) {
         int var9 = var6 + var8 * var4;

         for(int var10 = var9 + var3; var9 < var10; ++var9) {
            int var11 = var7[var9];
            int var12 = var11 >>> 24;
            int var13 = var11 >> 16 & 255;
            if (var12 < var13) {
               var12 = var13;
            }

            var13 = var11 >> 8 & 255;
            if (var12 < var13) {
               var12 = var13;
            }

            var13 = var11 & 255;
            if (var12 < var13) {
               var12 = var13;
            }

            var7[var9] = var11 & 16777215 | var12 << 24;
         }
      }

   }

   public void fixAlpha_FALLBACK(WritableRaster var1) {
      int var2 = var1.getMinX();
      int var3 = var1.getWidth();
      int var4 = var1.getMinY();
      int var5 = var4 + var1.getHeight() - 1;
      int var6 = var1.getNumBands();
      int[] var12 = null;

      for(int var9 = var4; var9 <= var5; ++var9) {
         var12 = var1.getPixels(var2, var9, var3, 1, var12);
         int var11 = 0;

         for(int var8 = 0; var8 < var3; ++var8) {
            int var7 = var12[var11];

            for(int var10 = 1; var10 < var6; ++var10) {
               if (var12[var11 + var10] > var7) {
                  var7 = var12[var11 + var10];
               }
            }

            var12[var11 + var6 - 1] = var7;
            var11 += var6;
         }

         var1.setPixels(var2, var9, var3, 1, var12);
      }

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
      if (this.kernelUnitLength != null) {
         if ((double)this.kernelUnitLength[0] > 0.0) {
            var16 = (double)(1.0F / this.kernelUnitLength[0]);
         }

         if ((double)this.kernelUnitLength[1] > 0.0) {
            var18 = (double)(1.0F / this.kernelUnitLength[1]);
         }
      }

      Object var20 = var1.getAreaOfInterest();
      if (var20 == null) {
         var20 = this.getBounds2D();
      }

      Rectangle2D var21 = ((Shape)var20).getBounds2D();
      int var22 = this.kernel.getWidth();
      int var23 = this.kernel.getHeight();
      int var24 = this.target.x;
      int var25 = this.target.y;
      double var26 = var21.getX() - (double)var24 / var16;
      double var28 = var21.getY() - (double)var25 / var18;
      double var30 = var26 + var21.getWidth() + (double)(var22 - 1) / var16;
      double var32 = var28 + var21.getHeight() + (double)(var23 - 1) / var18;
      Rectangle2D.Double var48 = new Rectangle2D.Double(Math.floor(var26), Math.floor(var28), Math.ceil(var30 - Math.floor(var26)), Math.ceil(var32 - Math.floor(var28)));
      AffineTransform var49 = AffineTransform.getScaleInstance(var16, var18);
      AffineTransform var27 = new AffineTransform(var4 / var16, var10 / var16, var8 / var18, var6 / var18, var12, var14);
      RenderedImage var50 = this.getSource().createRendering(new RenderContext(var49, var48, var2));
      if (var50 == null) {
         return null;
      } else {
         Object var29 = this.convertSourceCS(var50);
         Shape var52 = var49.createTransformedShape((Shape)var20);
         Rectangle2D var31 = var52.getBounds2D();
         var48 = new Rectangle2D.Double(Math.floor(var31.getX() - (double)var24), Math.floor(var31.getY() - (double)var25), Math.ceil(var31.getX() + var31.getWidth()) - Math.floor(var31.getX()) + (double)(var22 - 1), Math.ceil(var31.getY() + var31.getHeight()) - Math.floor(var31.getY()) + (double)(var23 - 1));
         if (!var48.getBounds().equals(((CachableRed)var29).getBounds())) {
            if (this.edgeMode == PadMode.WRAP) {
               throw new IllegalArgumentException("edgeMode=\"wrap\" is not supported by ConvolveMatrix.");
            }

            var29 = new PadRed((CachableRed)var29, var48.getBounds(), this.edgeMode, var2);
         }

         if ((double)this.bias != 0.0) {
            throw new IllegalArgumentException("Only bias equal to zero is supported in ConvolveMatrix.");
         } else {
            ConvolveOp var53 = new ConvolveOp(this.kernel, 1, var2);
            ColorModel var33 = ((CachableRed)var29).getColorModel();
            Raster var34 = ((CachableRed)var29).getData();
            WritableRaster var35 = GraphicsUtil.makeRasterWritable(var34, 0, 0);
            int var36 = this.target.x - this.kernel.getXOrigin();
            int var37 = this.target.y - this.kernel.getYOrigin();
            int var38 = (int)(var48.getX() + (double)var36);
            int var39 = (int)(var48.getY() + (double)var37);
            BufferedImage var40;
            BufferedImage var41;
            if (!this.preserveAlpha) {
               var33 = GraphicsUtil.coerceData(var35, var33, true);
               var41 = new BufferedImage(var33, var35, var33.isAlphaPremultiplied(), (Hashtable)null);
               var40 = var53.filter(var41, (BufferedImage)null);
               if (this.kernelHasNegValues) {
                  this.fixAlpha(var40);
               }
            } else {
               var41 = new BufferedImage(var33, var35, var33.isAlphaPremultiplied(), (Hashtable)null);
               DirectColorModel var54 = new DirectColorModel(ColorSpace.getInstance(1004), 24, 16711680, 65280, 255, 0, false, 3);
               BufferedImage var42 = new BufferedImage(var54, var54.createCompatibleWritableRaster(var35.getWidth(), var35.getHeight()), var54.isAlphaPremultiplied(), (Hashtable)null);
               GraphicsUtil.copyData(var41, var42);
               ColorModel var43 = GraphicsUtil.Linear_sRGB_Unpre;
               var40 = new BufferedImage(var43, var43.createCompatibleWritableRaster(var35.getWidth(), var35.getHeight()), var43.isAlphaPremultiplied(), (Hashtable)null);
               WritableRaster var44 = Raster.createWritableRaster(var54.createCompatibleSampleModel(var35.getWidth(), var35.getHeight()), var40.getRaster().getDataBuffer(), new Point(0, 0));
               BufferedImage var45 = new BufferedImage(var54, var44, var54.isAlphaPremultiplied(), (Hashtable)null);
               var53.filter(var42, var45);
               Rectangle var46 = var35.getBounds();
               Rectangle var47 = new Rectangle(var46.x - var36, var46.y - var37, var46.width, var46.height);
               GraphicsUtil.copyBand(var35, var46, var35.getNumBands() - 1, var40.getRaster(), var47, var40.getRaster().getNumBands() - 1);
            }

            BufferedImageCachableRed var51 = new BufferedImageCachableRed(var40, var38, var39);
            var29 = new PadRed(var51, var31.getBounds(), PadMode.ZERO_PAD, var2);
            if (!var27.isIdentity()) {
               var29 = new AffineRed((CachableRed)var29, var27, (RenderingHints)null);
            }

            return (RenderedImage)var29;
         }
      }
   }
}
