package org.apache.batik.ext.awt.image.codec.tiff;

import java.awt.image.BufferedImage;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.rendered.FormatRed;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.TIFFTranscoder;

public class TIFFTranscoderInternalCodecWriteAdapter implements TIFFTranscoder.WriteAdapter {
   public void writeImage(TIFFTranscoder var1, BufferedImage var2, TranscoderOutput var3) throws TranscoderException {
      TranscodingHints var4 = var1.getTranscodingHints();
      TIFFEncodeParam var5 = new TIFFEncodeParam();
      float var6 = var1.getUserAgent().getPixelUnitToMillimeter();
      int var7 = (int)((double)(100000.0F / var6) + 0.5);
      short var8 = 10000;
      long[] var9 = new long[]{(long)var7, (long)var8};
      TIFFField[] var10 = new TIFFField[]{new TIFFField(296, 3, 1, new char[]{'\u0003'}), new TIFFField(282, 5, 1, new long[][]{var9}), new TIFFField(283, 5, 1, new long[][]{var9})};
      var5.setExtraFields(var10);
      if (var4.containsKey(TIFFTranscoder.KEY_COMPRESSION_METHOD)) {
         String var11 = (String)var4.get(TIFFTranscoder.KEY_COMPRESSION_METHOD);
         if ("packbits".equals(var11)) {
            var5.setCompression(32773);
         } else if ("deflate".equals(var11)) {
            var5.setCompression(32946);
         }
      }

      try {
         int var21 = var2.getWidth();
         int var12 = var2.getHeight();
         SinglePixelPackedSampleModel var13 = (SinglePixelPackedSampleModel)var2.getSampleModel();
         OutputStream var14 = var3.getOutputStream();
         TIFFImageEncoder var15 = new TIFFImageEncoder(var14, var5);
         int var16 = var13.getNumBands();
         int[] var17 = new int[var16];

         for(int var18 = 0; var18 < var16; var17[var18] = var18++) {
         }

         PixelInterleavedSampleModel var22 = new PixelInterleavedSampleModel(0, var21, var12, var16, var21 * var16, var17);
         FormatRed var19 = new FormatRed(GraphicsUtil.wrap(var2), var22);
         var15.encode(var19);
         var14.flush();
      } catch (IOException var20) {
         throw new TranscoderException(var20);
      }
   }
}
