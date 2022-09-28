package org.apache.xmlgraphics.ps;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.xmlgraphics.image.GraphicsUtil;

public class ImageEncodingHelper {
   private static final ColorModel DEFAULT_RGB_COLOR_MODEL = new ComponentColorModel(ColorSpace.getInstance(1000), false, false, 1, 0);
   private final RenderedImage image;
   private ColorModel encodedColorModel;
   private boolean firstTileDump;
   private boolean enableCMYK;

   public ImageEncodingHelper(RenderedImage image) {
      this(image, false);
   }

   public ImageEncodingHelper(RenderedImage image, boolean enableCMYK) {
      this.image = image;
      this.enableCMYK = enableCMYK;
      this.determineEncodedColorModel();
   }

   public RenderedImage getImage() {
      return this.image;
   }

   public ColorModel getNativeColorModel() {
      return this.getImage().getColorModel();
   }

   public ColorModel getEncodedColorModel() {
      return this.encodedColorModel;
   }

   public boolean hasAlpha() {
      return this.image.getColorModel().hasAlpha();
   }

   public boolean isConverted() {
      return this.getNativeColorModel() != this.getEncodedColorModel();
   }

   private void writeRGBTo(OutputStream out) throws IOException {
      encodeRenderedImageAsRGB(this.image, out);
   }

   public static void encodeRenderedImageAsRGB(RenderedImage image, OutputStream out) throws IOException {
      Raster raster = image.getData();
      int nbands = raster.getNumBands();
      int dataType = raster.getDataBuffer().getDataType();
      Object var3;
      switch (dataType) {
         case 0:
            var3 = new byte[nbands];
            break;
         case 1:
            var3 = new short[nbands];
            break;
         case 2:
         default:
            throw new IllegalArgumentException("Unknown data buffer type: " + dataType);
         case 3:
            var3 = new int[nbands];
            break;
         case 4:
            var3 = new float[nbands];
            break;
         case 5:
            var3 = new double[nbands];
      }

      ColorModel colorModel = image.getColorModel();
      int w = image.getWidth();
      int h = image.getHeight();
      byte[] buf = new byte[w * 3];

      for(int y = 0; y < h; ++y) {
         int idx = -1;

         for(int x = 0; x < w; ++x) {
            int rgb = colorModel.getRGB(raster.getDataElements(x, y, var3));
            ++idx;
            buf[idx] = (byte)(rgb >> 16);
            ++idx;
            buf[idx] = (byte)(rgb >> 8);
            ++idx;
            buf[idx] = (byte)rgb;
         }

         out.write(buf);
      }

   }

   public static void encodeRGBAsGrayScale(byte[] raw, int width, int height, int bitsPerPixel, OutputStream out) throws IOException {
      int pixelsPerByte = 8 / bitsPerPixel;
      int bytewidth = width / pixelsPerByte;
      if (width % pixelsPerByte != 0) {
         ++bytewidth;
      }

      byte[] linedata = new byte[bytewidth];

      for(int y = 0; y < height; ++y) {
         byte ib = 0;
         int i = 3 * y * width;

         for(int x = 0; x < width; i += 3) {
            double greyVal = 0.212671 * (double)(raw[i] & 255) + 0.71516 * (double)(raw[i + 1] & 255) + 0.072169 * (double)(raw[i + 2] & 255);
            switch (bitsPerPixel) {
               case 1:
                  if (greyVal < 128.0) {
                     ib |= (byte)(1 << 7 - x % 8);
                  }
                  break;
               case 4:
                  greyVal /= 16.0;
                  ib |= (byte)((byte)((int)greyVal) << (1 - x % 2) * 4);
                  break;
               case 8:
                  ib = (byte)((int)greyVal);
                  break;
               default:
                  throw new UnsupportedOperationException("Unsupported bits per pixel: " + bitsPerPixel);
            }

            if (x % pixelsPerByte == pixelsPerByte - 1 || x + 1 == width) {
               linedata[x / pixelsPerByte] = ib;
               ib = 0;
            }

            ++x;
         }

         out.write(linedata);
      }

   }

   private boolean optimizedWriteTo(OutputStream out) throws IOException {
      if (this.firstTileDump) {
         Raster raster = this.image.getTile(0, 0);
         DataBuffer buffer = raster.getDataBuffer();
         if (buffer instanceof DataBufferByte) {
            out.write(((DataBufferByte)buffer).getData());
            return true;
         }
      }

      return false;
   }

   protected boolean isMultiTile() {
      int tilesX = this.image.getNumXTiles();
      int tilesY = this.image.getNumYTiles();
      return tilesX != 1 || tilesY != 1;
   }

   protected void determineEncodedColorModel() {
      this.firstTileDump = false;
      this.encodedColorModel = DEFAULT_RGB_COLOR_MODEL;
      ColorModel cm = this.image.getColorModel();
      ColorSpace cs = cm.getColorSpace();
      int numComponents = cm.getNumComponents();
      if (!this.isMultiTile()) {
         if (numComponents == 1 && cs.getType() == 6) {
            if (cm.getTransferType() == 0) {
               this.firstTileDump = true;
               this.encodedColorModel = cm;
            }
         } else if (cm instanceof IndexColorModel) {
            if (cm.getTransferType() == 0) {
               this.firstTileDump = true;
               this.encodedColorModel = cm;
            }
         } else if (cm instanceof ComponentColorModel && (numComponents == 3 || this.enableCMYK && numComponents == 4) && !cm.hasAlpha()) {
            Raster raster = this.image.getTile(0, 0);
            DataBuffer buffer = raster.getDataBuffer();
            SampleModel sampleModel = raster.getSampleModel();
            if (sampleModel instanceof PixelInterleavedSampleModel) {
               PixelInterleavedSampleModel piSampleModel = (PixelInterleavedSampleModel)sampleModel;
               int[] offsets = piSampleModel.getBandOffsets();

               for(int i = 0; i < offsets.length; ++i) {
                  if (offsets[i] != i) {
                     return;
                  }
               }
            }

            if (cm.getTransferType() == 0 && buffer.getOffset() == 0 && buffer.getNumBanks() == 1) {
               this.firstTileDump = true;
               this.encodedColorModel = cm;
            }
         }
      }

   }

   public void encode(OutputStream out) throws IOException {
      if (this.isConverted() || !this.optimizedWriteTo(out)) {
         this.writeRGBTo(out);
      }
   }

   public void encodeAlpha(OutputStream out) throws IOException {
      if (!this.hasAlpha()) {
         throw new IllegalStateException("Image doesn't have an alpha channel");
      } else {
         Raster alpha = GraphicsUtil.getAlphaRaster(this.image);
         DataBuffer buffer = alpha.getDataBuffer();
         if (buffer instanceof DataBufferByte) {
            out.write(((DataBufferByte)buffer).getData());
         } else {
            throw new UnsupportedOperationException("Alpha raster not supported: " + buffer.getClass().getName());
         }
      }
   }

   public static void encodePackedColorComponents(RenderedImage image, OutputStream out) throws IOException {
      ImageEncodingHelper helper = new ImageEncodingHelper(image, true);
      helper.encode(out);
   }

   public static ImageEncoder createRenderedImageEncoder(RenderedImage img) {
      return new RenderedImageEncoder(img);
   }

   private static class RenderedImageEncoder implements ImageEncoder {
      private final RenderedImage img;

      public RenderedImageEncoder(RenderedImage img) {
         this.img = img;
      }

      public void writeTo(OutputStream out) throws IOException {
         ImageEncodingHelper.encodePackedColorComponents(this.img, out);
      }

      public String getImplicitFilter() {
         return null;
      }
   }
}
