package org.apache.batik.ext.awt.image.codec.util;

import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ImageEncoderImpl implements ImageEncoder {
   protected OutputStream output;
   protected ImageEncodeParam param;

   public ImageEncoderImpl(OutputStream var1, ImageEncodeParam var2) {
      this.output = var1;
      this.param = var2;
   }

   public ImageEncodeParam getParam() {
      return this.param;
   }

   public void setParam(ImageEncodeParam var1) {
      this.param = var1;
   }

   public OutputStream getOutputStream() {
      return this.output;
   }

   public void encode(Raster var1, ColorModel var2) throws IOException {
      SingleTileRenderedImage var3 = new SingleTileRenderedImage(var1, var2);
      this.encode(var3);
   }

   public abstract void encode(RenderedImage var1) throws IOException;
}
