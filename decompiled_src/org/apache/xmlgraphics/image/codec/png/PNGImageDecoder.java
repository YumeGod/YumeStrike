package org.apache.xmlgraphics.image.codec.png;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import org.apache.xmlgraphics.image.codec.util.ImageDecoderImpl;
import org.apache.xmlgraphics.image.codec.util.PropertyUtil;

public class PNGImageDecoder extends ImageDecoderImpl {
   public PNGImageDecoder(InputStream input, PNGDecodeParam param) {
      super((InputStream)input, param);
   }

   public RenderedImage decodeAsRenderedImage(int page) throws IOException {
      if (page != 0) {
         throw new IOException(PropertyUtil.getString("PNGImageDecoder19"));
      } else {
         return new PNGImage(this.input, (PNGDecodeParam)this.param);
      }
   }
}
