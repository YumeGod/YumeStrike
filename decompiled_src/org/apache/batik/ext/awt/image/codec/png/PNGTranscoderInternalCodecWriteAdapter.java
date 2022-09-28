package org.apache.batik.ext.awt.image.codec.png;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.ext.awt.image.rendered.IndexImage;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.PNGTranscoder;

public class PNGTranscoderInternalCodecWriteAdapter implements PNGTranscoder.WriteAdapter {
   public void writeImage(PNGTranscoder var1, BufferedImage var2, TranscoderOutput var3) throws TranscoderException {
      TranscodingHints var4 = var1.getTranscodingHints();
      boolean var5 = true;
      if (var4.containsKey(PNGTranscoder.KEY_INDEXED)) {
         int var12 = (Integer)var4.get(PNGTranscoder.KEY_INDEXED);
         if (var12 == 1 || var12 == 2 || var12 == 4 || var12 == 8) {
            var2 = IndexImage.getIndexedImage(var2, 1 << var12);
         }
      }

      PNGEncodeParam var6 = PNGEncodeParam.getDefaultEncodeParam(var2);
      if (var6 instanceof PNGEncodeParam.RGB) {
         ((PNGEncodeParam.RGB)var6).setBackgroundRGB(new int[]{255, 255, 255});
      }

      float var7;
      if (var4.containsKey(PNGTranscoder.KEY_GAMMA)) {
         var7 = (Float)var4.get(PNGTranscoder.KEY_GAMMA);
         if (var7 > 0.0F) {
            var6.setGamma(var7);
         }

         var6.setChromaticity(PNGTranscoder.DEFAULT_CHROMA);
      } else {
         var6.setSRGBIntent(0);
      }

      var7 = var1.getUserAgent().getPixelUnitToMillimeter();
      int var8 = (int)((double)(1000.0F / var7) + 0.5);
      var6.setPhysicalDimension(var8, var8, 1);

      try {
         OutputStream var9 = var3.getOutputStream();
         PNGImageEncoder var10 = new PNGImageEncoder(var9, var6);
         var10.encode(var2);
         var9.flush();
      } catch (IOException var11) {
         throw new TranscoderException(var11);
      }
   }
}
