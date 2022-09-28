package org.apache.batik.ext.awt.image.codec.imageio;

import java.awt.image.BufferedImage;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.rendered.FormatRed;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.TIFFTranscoder;

public class TIFFTranscoderImageIOWriteAdapter implements TIFFTranscoder.WriteAdapter {
   public void writeImage(TIFFTranscoder var1, BufferedImage var2, TranscoderOutput var3) throws TranscoderException {
      TranscodingHints var4 = var1.getTranscodingHints();
      ImageWriter var5 = ImageWriterRegistry.getInstance().getWriterFor("image/tiff");
      ImageWriterParams var6 = new ImageWriterParams();
      float var7 = var1.getUserAgent().getPixelUnitToMillimeter();
      int var8 = (int)(25.4 / (double)var7 + 0.5);
      var6.setResolution(var8);
      if (var4.containsKey(TIFFTranscoder.KEY_COMPRESSION_METHOD)) {
         String var9 = (String)var4.get(TIFFTranscoder.KEY_COMPRESSION_METHOD);
         if ("packbits".equals(var9)) {
            var6.setCompressionMethod("PackBits");
         } else if ("deflate".equals(var9)) {
            var6.setCompressionMethod("Deflate");
         } else if ("lzw".equals(var9)) {
            var6.setCompressionMethod("LZW");
         } else if ("jpeg".equals(var9)) {
            var6.setCompressionMethod("JPEG");
         }
      }

      try {
         OutputStream var18 = var3.getOutputStream();
         int var10 = var2.getWidth();
         int var11 = var2.getHeight();
         SinglePixelPackedSampleModel var12 = (SinglePixelPackedSampleModel)var2.getSampleModel();
         int var13 = var12.getNumBands();
         int[] var14 = new int[var13];

         for(int var15 = 0; var15 < var13; var14[var15] = var15++) {
         }

         PixelInterleavedSampleModel var19 = new PixelInterleavedSampleModel(0, var10, var11, var13, var10 * var13, var14);
         FormatRed var16 = new FormatRed(GraphicsUtil.wrap(var2), var19);
         var5.writeImage(var16, var18, var6);
         var18.flush();
      } catch (IOException var17) {
         throw new TranscoderException(var17);
      }
   }
}
