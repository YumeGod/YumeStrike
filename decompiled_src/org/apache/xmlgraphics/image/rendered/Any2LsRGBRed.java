package org.apache.xmlgraphics.image.rendered;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.xmlgraphics.image.GraphicsUtil;

public class Any2LsRGBRed extends AbstractRed {
   boolean srcIssRGB = false;
   private static final double GAMMA = 2.4;
   private static final double LFACT = 0.07739938080495357;
   private static final int[] sRGBToLsRGBLut = new int[256];

   public Any2LsRGBRed(CachableRed src) {
      super((CachableRed)src, src.getBounds(), fixColorModel(src), fixSampleModel(src), src.getTileGridXOffset(), src.getTileGridYOffset(), (Map)null);
      ColorModel srcCM = src.getColorModel();
      if (srcCM != null) {
         ColorSpace srcCS = srcCM.getColorSpace();
         if (srcCS == ColorSpace.getInstance(1000)) {
            this.srcIssRGB = true;
         }

      }
   }

   public static final double sRGBToLsRGB(double value) {
      return value <= 0.003928 ? value * 0.07739938080495357 : Math.pow((value + 0.055) / 1.055, 2.4);
   }

   public WritableRaster copyData(WritableRaster wr) {
      CachableRed src = (CachableRed)this.getSources().get(0);
      ColorModel srcCM = src.getColorModel();
      SampleModel srcSM = src.getSampleModel();
      if (this.srcIssRGB && Any2sRGBRed.is_INT_PACK_COMP(wr.getSampleModel())) {
         src.copyData(wr);
         if (srcCM.hasAlpha()) {
            GraphicsUtil.coerceData(wr, srcCM, false);
         }

         Any2sRGBRed.applyLut_INT(wr, sRGBToLsRGBLut);
         return wr;
      } else {
         if (srcCM == null) {
            float[][] matrix = (float[][])null;
            switch (srcSM.getNumBands()) {
               case 1:
                  matrix = new float[1][3];
                  matrix[0][0] = 1.0F;
                  matrix[0][1] = 1.0F;
                  matrix[0][2] = 1.0F;
                  break;
               case 2:
                  matrix = new float[2][4];
                  matrix[0][0] = 1.0F;
                  matrix[0][1] = 1.0F;
                  matrix[0][2] = 1.0F;
                  matrix[1][3] = 1.0F;
                  break;
               case 3:
                  matrix = new float[3][3];
                  matrix[0][0] = 1.0F;
                  matrix[1][1] = 1.0F;
                  matrix[2][2] = 1.0F;
                  break;
               default:
                  matrix = new float[srcSM.getNumBands()][4];
                  matrix[0][0] = 1.0F;
                  matrix[1][1] = 1.0F;
                  matrix[2][2] = 1.0F;
                  matrix[3][3] = 1.0F;
            }

            Raster srcRas = src.getData(wr.getBounds());
            BandCombineOp op = new BandCombineOp(matrix, (RenderingHints)null);
            op.filter(srcRas, wr);
         } else {
            ColorModel dstCM = this.getColorModel();
            BufferedImage dstBI;
            if (!dstCM.hasAlpha()) {
               dstBI = new BufferedImage(dstCM, wr.createWritableTranslatedChild(0, 0), dstCM.isAlphaPremultiplied(), (Hashtable)null);
            } else {
               SinglePixelPackedSampleModel dstSM = (SinglePixelPackedSampleModel)wr.getSampleModel();
               int[] masks = dstSM.getBitMasks();
               SampleModel dstSMNoA = new SinglePixelPackedSampleModel(dstSM.getDataType(), dstSM.getWidth(), dstSM.getHeight(), dstSM.getScanlineStride(), new int[]{masks[0], masks[1], masks[2]});
               ColorModel dstCMNoA = GraphicsUtil.Linear_sRGB;
               WritableRaster dstWr = Raster.createWritableRaster(dstSMNoA, wr.getDataBuffer(), new Point(0, 0));
               dstWr = dstWr.createWritableChild(wr.getMinX() - wr.getSampleModelTranslateX(), wr.getMinY() - wr.getSampleModelTranslateY(), wr.getWidth(), wr.getHeight(), 0, 0, (int[])null);
               dstBI = new BufferedImage(dstCMNoA, dstWr, false, (Hashtable)null);
            }

            ColorModel srcBICM = srcCM;
            WritableRaster srcWr;
            if (srcCM.hasAlpha() && srcCM.isAlphaPremultiplied()) {
               Rectangle wrR = wr.getBounds();
               SampleModel sm = srcCM.createCompatibleSampleModel(wrR.width, wrR.height);
               srcWr = Raster.createWritableRaster(sm, new Point(wrR.x, wrR.y));
               src.copyData(srcWr);
               srcBICM = GraphicsUtil.coerceData(srcWr, srcCM, false);
            } else {
               Raster srcRas = src.getData(wr.getBounds());
               srcWr = GraphicsUtil.makeRasterWritable(srcRas);
            }

            BufferedImage srcBI = new BufferedImage(srcBICM, srcWr.createWritableTranslatedChild(0, 0), false, (Hashtable)null);
            ColorConvertOp op = new ColorConvertOp((RenderingHints)null);
            op.filter(srcBI, dstBI);
            if (dstCM.hasAlpha()) {
               copyBand(srcWr, srcSM.getNumBands() - 1, wr, this.getSampleModel().getNumBands() - 1);
            }
         }

         return wr;
      }
   }

   protected static ColorModel fixColorModel(CachableRed src) {
      ColorModel cm = src.getColorModel();
      if (cm != null) {
         return cm.hasAlpha() ? GraphicsUtil.Linear_sRGB_Unpre : GraphicsUtil.Linear_sRGB;
      } else {
         SampleModel sm = src.getSampleModel();
         switch (sm.getNumBands()) {
            case 1:
               return GraphicsUtil.Linear_sRGB;
            case 2:
               return GraphicsUtil.Linear_sRGB_Unpre;
            case 3:
               return GraphicsUtil.Linear_sRGB;
            default:
               return GraphicsUtil.Linear_sRGB_Unpre;
         }
      }
   }

   protected static SampleModel fixSampleModel(CachableRed src) {
      SampleModel sm = src.getSampleModel();
      ColorModel cm = src.getColorModel();
      boolean alpha = false;
      if (cm != null) {
         alpha = cm.hasAlpha();
      } else {
         switch (sm.getNumBands()) {
            case 1:
            case 3:
               alpha = false;
               break;
            default:
               alpha = true;
         }
      }

      return alpha ? new SinglePixelPackedSampleModel(3, sm.getWidth(), sm.getHeight(), new int[]{16711680, 65280, 255, -16777216}) : new SinglePixelPackedSampleModel(3, sm.getWidth(), sm.getHeight(), new int[]{16711680, 65280, 255});
   }

   static {
      double scale = 0.00392156862745098;

      for(int i = 0; i < 256; ++i) {
         double value = sRGBToLsRGB((double)i * 0.00392156862745098);
         sRGBToLsRGBLut[i] = (int)Math.round(value * 255.0);
      }

   }
}
