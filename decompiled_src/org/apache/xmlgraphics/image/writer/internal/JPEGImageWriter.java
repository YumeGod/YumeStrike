package org.apache.xmlgraphics.image.writer.internal;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.xmlgraphics.image.GraphicsUtil;
import org.apache.xmlgraphics.image.writer.AbstractImageWriter;
import org.apache.xmlgraphics.image.writer.ImageWriterParams;

public class JPEGImageWriter extends AbstractImageWriter {
   public void writeImage(RenderedImage image, OutputStream out) throws IOException {
      this.writeImage(image, out, (ImageWriterParams)null);
   }

   public void writeImage(RenderedImage image, OutputStream out, ImageWriterParams params) throws IOException {
      BufferedImage bi;
      if (image instanceof BufferedImage) {
         bi = (BufferedImage)image;
      } else {
         bi = GraphicsUtil.makeLinearBufferedImage(image.getWidth(), image.getHeight(), false);
      }

      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
      if (params != null) {
         JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
         if (params.getJPEGQuality() != null) {
            param.setQuality(params.getJPEGQuality(), params.getJPEGForceBaseline());
         }

         encoder.encode(bi, param);
      } else {
         encoder.encode(bi);
      }

   }

   public String getMIMEType() {
      return "image/jpeg";
   }
}
