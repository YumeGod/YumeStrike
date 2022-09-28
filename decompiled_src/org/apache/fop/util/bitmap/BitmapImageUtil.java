package org.apache.fop.util.bitmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class BitmapImageUtil {
   public static final boolean isMonochromeImage(RenderedImage img) {
      return getColorIndexSize(img) == 2;
   }

   public static final boolean isZeroBlack(RenderedImage img) {
      if (!isMonochromeImage(img)) {
         throw new IllegalArgumentException("Image is not a monochrome image!");
      } else {
         IndexColorModel icm = (IndexColorModel)img.getColorModel();
         int gray0 = convertToGray(icm.getRGB(0));
         int gray1 = convertToGray(icm.getRGB(1));
         return gray0 < gray1;
      }
   }

   public static final int convertToGray(int r, int g, int b) {
      return (r * 30 + g * 59 + b * 11) / 100;
   }

   public static final int convertToGray(int rgb) {
      int r = (rgb & 16711680) >> 16;
      int g = (rgb & '\uff00') >> 8;
      int b = rgb & 255;
      return convertToGray(r, g, b);
   }

   public static final int getColorIndexSize(RenderedImage img) {
      ColorModel cm = img.getColorModel();
      if (cm instanceof IndexColorModel) {
         IndexColorModel icm = (IndexColorModel)cm;
         return icm.getMapSize();
      } else {
         return 0;
      }
   }

   public static final boolean isGrayscaleImage(RenderedImage img) {
      return img.getColorModel().getColorSpace().getNumComponents() == 1;
   }

   public static final BufferedImage convertTosRGB(RenderedImage img, Dimension targetDimension) {
      return convertAndScaleImage(img, targetDimension, 1);
   }

   public static final BufferedImage convertToGrayscale(RenderedImage img, Dimension targetDimension) {
      return convertAndScaleImage(img, targetDimension, 10);
   }

   public static final BufferedImage convertToMonochrome(RenderedImage img, Dimension targetDimension) {
      return toBufferedImage(convertToMonochrome(img, targetDimension, 0.0F));
   }

   public static final RenderedImage convertToMonochrome(RenderedImage img, Dimension targetDimension, float quality) {
      if (!isMonochromeImage(img) && quality >= 0.5F) {
         Dimension orgDim = new Dimension(img.getWidth(), img.getHeight());
         BufferedImage bi;
         if (targetDimension != null && !orgDim.equals(targetDimension)) {
            ColorModel cm = img.getColorModel();
            BufferedImage tgt = new BufferedImage(cm, cm.createCompatibleWritableRaster(targetDimension.width, targetDimension.height), cm.isAlphaPremultiplied(), (Hashtable)null);
            transferImage(img, tgt);
            bi = tgt;
         } else {
            bi = toBufferedImage(img);
         }

         MonochromeBitmapConverter converter = createDefaultMonochromeBitmapConverter();
         if (quality >= 0.8F) {
            converter.setHint("quality", Boolean.TRUE.toString());
            bi = convertToGrayscale(bi, targetDimension);
         }

         try {
            return converter.convertToMonochrome(bi);
         } catch (Exception var7) {
            bi = convertToGrayscale(bi, targetDimension);
            return converter.convertToMonochrome(bi);
         }
      } else {
         return convertAndScaleImage(img, targetDimension, 12);
      }
   }

   private static BufferedImage convertAndScaleImage(RenderedImage img, Dimension targetDimension, int imageType) {
      Dimension bmpDimension = targetDimension;
      if (targetDimension == null) {
         bmpDimension = new Dimension(img.getWidth(), img.getHeight());
      }

      BufferedImage target = new BufferedImage(bmpDimension.width, bmpDimension.height, imageType);
      transferImage(img, target);
      return target;
   }

   public static BufferedImage toBufferedImage(RenderedImage img) {
      if (img instanceof BufferedImage) {
         return (BufferedImage)img;
      } else {
         WritableRaster wr = img.getColorModel().createCompatibleWritableRaster(img.getWidth(), img.getHeight());
         boolean premult = img.getColorModel().isAlphaPremultiplied();
         return new BufferedImage(img.getColorModel(), wr, premult, (Hashtable)null);
      }
   }

   private static void transferImage(RenderedImage source, BufferedImage target) {
      Graphics2D g2d = target.createGraphics();

      try {
         g2d.setBackground(Color.white);
         g2d.setColor(Color.black);
         g2d.clearRect(0, 0, target.getWidth(), target.getHeight());
         AffineTransform at = new AffineTransform();
         if (source.getWidth() != target.getWidth() || source.getHeight() != target.getHeight()) {
            double sx = (double)target.getWidth() / (double)source.getWidth();
            double sy = (double)target.getHeight() / (double)source.getHeight();
            at.scale(sx, sy);
         }

         g2d.drawRenderedImage(source, at);
      } finally {
         g2d.dispose();
      }

   }

   public static MonochromeBitmapConverter createDefaultMonochromeBitmapConverter() {
      MonochromeBitmapConverter converter = null;

      try {
         String clName = "org.apache.fop.util.bitmap.JAIMonochromeBitmapConverter";
         Class clazz = Class.forName(clName);
         converter = (MonochromeBitmapConverter)clazz.newInstance();
      } catch (ClassNotFoundException var3) {
      } catch (LinkageError var4) {
      } catch (InstantiationException var5) {
      } catch (IllegalAccessException var6) {
      }

      if (converter == null) {
         converter = new DefaultMonochromeBitmapConverter();
      }

      return (MonochromeBitmapConverter)converter;
   }
}
