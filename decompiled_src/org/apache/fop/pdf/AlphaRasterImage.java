package org.apache.fop.pdf;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.xmlgraphics.image.GraphicsUtil;

public class AlphaRasterImage implements PDFImage {
   private int bitsPerComponent;
   private PDFDeviceColorSpace colorSpace;
   private Raster alpha;
   private String key;

   public AlphaRasterImage(String k, Raster alpha) {
      this.key = k;
      this.bitsPerComponent = 8;
      this.colorSpace = new PDFDeviceColorSpace(1);
      if (alpha == null) {
         throw new NullPointerException("Parameter alpha must not be null");
      } else {
         this.alpha = alpha;
      }
   }

   public AlphaRasterImage(String k, RenderedImage image) {
      this(k, GraphicsUtil.getAlphaRaster(image));
   }

   public void setup(PDFDocument doc) {
   }

   public String getKey() {
      return this.key;
   }

   public int getWidth() {
      return this.alpha.getWidth();
   }

   public int getHeight() {
      return this.alpha.getHeight();
   }

   public PDFDeviceColorSpace getColorSpace() {
      return this.colorSpace;
   }

   public int getBitsPerComponent() {
      return this.bitsPerComponent;
   }

   public boolean isTransparent() {
      return false;
   }

   public PDFColor getTransparentColor() {
      return null;
   }

   public String getMask() {
      return null;
   }

   public String getSoftMask() {
      return null;
   }

   public PDFReference getSoftMaskReference() {
      return null;
   }

   public boolean isInverted() {
      return false;
   }

   public void outputContents(OutputStream out) throws IOException {
      int w = this.getWidth();
      int h = this.getHeight();
      int nbands = this.alpha.getNumBands();
      if (nbands != 1) {
         throw new UnsupportedOperationException("Expected only one band/component for the alpha channel");
      } else {
         int dataType = this.alpha.getDataBuffer().getDataType();
         if (dataType == 0) {
            byte[] line = new byte[nbands * w];

            for(int y = 0; y < h; ++y) {
               this.alpha.getDataElements(0, y, w, 1, line);
               out.write(line);
            }
         } else if (dataType == 1) {
            short[] sline = new short[nbands * w];
            byte[] line = new byte[nbands * w];

            for(int y = 0; y < h; ++y) {
               this.alpha.getDataElements(0, y, w, 1, sline);

               for(int i = 0; i < w; ++i) {
                  line[i] = (byte)(sline[i] >> 8);
               }

               out.write(line);
            }
         } else {
            if (dataType != 3) {
               throw new UnsupportedOperationException("Unsupported DataBuffer type: " + this.alpha.getDataBuffer().getClass().getName());
            }

            int shift = 24;
            SampleModel sampleModel = this.alpha.getSampleModel();
            if (sampleModel instanceof SinglePixelPackedSampleModel) {
               SinglePixelPackedSampleModel m = (SinglePixelPackedSampleModel)sampleModel;
               shift = m.getBitOffsets()[0];
            }

            int[] iline = new int[nbands * w];
            byte[] line = new byte[nbands * w];

            for(int y = 0; y < h; ++y) {
               this.alpha.getDataElements(0, y, w, 1, iline);

               for(int i = 0; i < w; ++i) {
                  line[i] = (byte)(iline[i] >> shift);
               }

               out.write(line);
            }
         }

      }
   }

   public void populateXObjectDictionary(PDFDictionary dict) {
   }

   public PDFICCStream getICCStream() {
      return null;
   }

   public boolean isPS() {
      return false;
   }

   public String getFilterHint() {
      return "image";
   }

   public PDFFilter getPDFFilter() {
      return null;
   }
}
