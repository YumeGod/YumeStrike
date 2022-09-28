package org.apache.xmlgraphics.image.codec.util;

import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ImageEncoderImpl implements ImageEncoder {
   protected OutputStream output;
   protected ImageEncodeParam param;

   public ImageEncoderImpl(OutputStream output, ImageEncodeParam param) {
      this.output = output;
      this.param = param;
   }

   public ImageEncodeParam getParam() {
      return this.param;
   }

   public void setParam(ImageEncodeParam param) {
      this.param = param;
   }

   public OutputStream getOutputStream() {
      return this.output;
   }

   public void encode(Raster ras, ColorModel cm) throws IOException {
      RenderedImage im = new SingleTileRenderedImage(ras, cm);
      this.encode(im);
   }

   public abstract void encode(RenderedImage var1) throws IOException;
}
