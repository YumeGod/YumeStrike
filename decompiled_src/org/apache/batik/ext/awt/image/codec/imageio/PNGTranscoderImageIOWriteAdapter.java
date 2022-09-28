package org.apache.batik.ext.awt.image.codec.imageio;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.ext.awt.image.rendered.IndexImage;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.PNGTranscoder;

public class PNGTranscoderImageIOWriteAdapter implements PNGTranscoder.WriteAdapter {
   public void writeImage(PNGTranscoder var1, BufferedImage var2, TranscoderOutput var3) throws TranscoderException {
      TranscodingHints var4 = var1.getTranscodingHints();
      boolean var5 = true;
      if (var4.containsKey(PNGTranscoder.KEY_INDEXED)) {
         int var12 = (Integer)var4.get(PNGTranscoder.KEY_INDEXED);
         if (var12 == 1 || var12 == 2 || var12 == 4 || var12 == 8) {
            var2 = IndexImage.getIndexedImage(var2, 1 << var12);
         }
      }

      ImageWriter var6 = ImageWriterRegistry.getInstance().getWriterFor("image/png");
      ImageWriterParams var7 = new ImageWriterParams();
      float var8 = var1.getUserAgent().getPixelUnitToMillimeter();
      int var9 = (int)(25.4 / (double)var8 + 0.5);
      var7.setResolution(var9);

      try {
         OutputStream var10 = var3.getOutputStream();
         var6.writeImage(var2, var10, var7);
         var10.flush();
      } catch (IOException var11) {
         throw new TranscoderException(var11);
      }
   }
}
