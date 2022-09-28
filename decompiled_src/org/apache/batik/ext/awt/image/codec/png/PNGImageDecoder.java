package org.apache.batik.ext.awt.image.codec.png;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import org.apache.batik.ext.awt.image.codec.util.ImageDecoderImpl;
import org.apache.batik.ext.awt.image.codec.util.PropertyUtil;

public class PNGImageDecoder extends ImageDecoderImpl {
   public PNGImageDecoder(InputStream var1, PNGDecodeParam var2) {
      super((InputStream)var1, var2);
   }

   public RenderedImage decodeAsRenderedImage(int var1) throws IOException {
      if (var1 != 0) {
         throw new IOException(PropertyUtil.getString("PNGImageDecoder19"));
      } else {
         return new PNGImage(this.input, (PNGDecodeParam)this.param);
      }
   }
}
