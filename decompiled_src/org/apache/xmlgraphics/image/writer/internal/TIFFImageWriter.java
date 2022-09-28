package org.apache.xmlgraphics.image.writer.internal;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.xmlgraphics.image.codec.tiff.TIFFEncodeParam;
import org.apache.xmlgraphics.image.codec.tiff.TIFFField;
import org.apache.xmlgraphics.image.codec.tiff.TIFFImageEncoder;
import org.apache.xmlgraphics.image.writer.AbstractImageWriter;
import org.apache.xmlgraphics.image.writer.ImageWriterParams;
import org.apache.xmlgraphics.image.writer.MultiImageWriter;

public class TIFFImageWriter extends AbstractImageWriter {
   public void writeImage(RenderedImage image, OutputStream out) throws IOException {
      this.writeImage(image, out, (ImageWriterParams)null);
   }

   public void writeImage(RenderedImage image, OutputStream out, ImageWriterParams params) throws IOException {
      TIFFEncodeParam encodeParams = this.createTIFFEncodeParams(params);
      this.updateParams(encodeParams, params, image);
      TIFFImageEncoder encoder = new TIFFImageEncoder(out, encodeParams);
      encoder.encode(image);
   }

   private void updateParams(TIFFEncodeParam encodeParams, ImageWriterParams params, RenderedImage image) {
      if (encodeParams.getCompression() == 7) {
         ColorModel cm = image.getColorModel();
         int imageType = cm.getColorSpace().getType();
         byte colorID;
         switch (imageType) {
            case 3:
               if (cm.hasAlpha()) {
                  colorID = 7;
               } else {
                  colorID = 3;
               }
               break;
            case 4:
            case 7:
            case 8:
            default:
               colorID = 0;
               break;
            case 5:
               if (cm.hasAlpha()) {
                  colorID = 7;
               } else {
                  colorID = 3;
               }
               break;
            case 6:
               colorID = 1;
               break;
            case 9:
               colorID = 4;
         }

         JPEGEncodeParam jpegParam = JPEGCodec.getDefaultJPEGEncodeParam(image.getData(), colorID);
         if (params.getJPEGQuality() != null || params.getJPEGForceBaseline() != null) {
            float qual = params.getJPEGQuality() != null ? params.getJPEGQuality() : 0.75F;
            boolean force = params.getJPEGForceBaseline() != null ? params.getJPEGForceBaseline() : false;
            jpegParam.setQuality(qual, force);
         }

         encodeParams.setJPEGEncodeParam(jpegParam);
      }

   }

   private TIFFEncodeParam createTIFFEncodeParams(ImageWriterParams params) {
      TIFFEncodeParam encodeParams = new TIFFEncodeParam();
      if (params == null) {
         encodeParams.setCompression(1);
      } else {
         if (params.getCompressionMethod() == null) {
            encodeParams.setCompression(32773);
         } else if ("PackBits".equalsIgnoreCase(params.getCompressionMethod())) {
            encodeParams.setCompression(32773);
         } else if ("NONE".equalsIgnoreCase(params.getCompressionMethod())) {
            encodeParams.setCompression(1);
         } else if ("JPEG".equalsIgnoreCase(params.getCompressionMethod())) {
            encodeParams.setCompression(7);
         } else {
            if (!"Deflate".equalsIgnoreCase(params.getCompressionMethod())) {
               throw new UnsupportedOperationException("Compression method not supported: " + params.getCompressionMethod());
            }

            encodeParams.setCompression(32946);
         }

         if (params.getResolution() != null) {
            float pixSzMM = 25.4F / params.getResolution().floatValue();
            int numPix = (int)((double)(100000.0F / pixSzMM) + 0.5);
            int denom = 10000;
            long[] rational = new long[]{(long)numPix, (long)denom};
            TIFFField[] fields = new TIFFField[]{new TIFFField(296, 3, 1, new char[]{'\u0003'}), new TIFFField(282, 5, 1, new long[][]{rational}), new TIFFField(283, 5, 1, new long[][]{rational})};
            encodeParams.setExtraFields(fields);
         }
      }

      return encodeParams;
   }

   public String getMIMEType() {
      return "image/tiff";
   }

   public MultiImageWriter createMultiImageWriter(OutputStream out) throws IOException {
      return new TIFFMultiImageWriter(out);
   }

   public boolean supportsMultiImageWriter() {
      return true;
   }

   private class TIFFMultiImageWriter implements MultiImageWriter {
      private OutputStream out;
      private TIFFEncodeParam encodeParams;
      private TIFFImageEncoder encoder;
      private Object context;

      public TIFFMultiImageWriter(OutputStream out) throws IOException {
         this.out = out;
      }

      public void writeImage(RenderedImage image, ImageWriterParams params) throws IOException {
         if (this.encoder == null) {
            this.encodeParams = TIFFImageWriter.this.createTIFFEncodeParams(params);
            TIFFImageWriter.this.updateParams(this.encodeParams, params, image);
            this.encoder = new TIFFImageEncoder(this.out, this.encodeParams);
         }

         this.context = this.encoder.encodeMultiple(this.context, image);
      }

      public void close() throws IOException {
         if (this.encoder != null) {
            this.encoder.finishMultiple(this.context);
         }

         this.encoder = null;
         this.encodeParams = null;
         this.out.flush();
      }
   }
}
