package org.apache.fop.render.ps;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.xmlgraphics.image.loader.impl.ImageRawJPEG;
import org.apache.xmlgraphics.ps.ImageEncoder;

public class ImageEncoderJPEG implements ImageEncoder {
   private final ImageRawJPEG jpeg;

   public ImageEncoderJPEG(ImageRawJPEG jpeg) {
      this.jpeg = jpeg;
   }

   public void writeTo(OutputStream out) throws IOException {
      this.jpeg.writeTo(out);
   }

   public String getImplicitFilter() {
      return "<< >> /DCTDecode";
   }
}
