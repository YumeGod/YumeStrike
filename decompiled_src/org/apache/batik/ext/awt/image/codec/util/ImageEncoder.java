package org.apache.batik.ext.awt.image.codec.util;

import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

public interface ImageEncoder {
   ImageEncodeParam getParam();

   void setParam(ImageEncodeParam var1);

   OutputStream getOutputStream();

   void encode(Raster var1, ColorModel var2) throws IOException;

   void encode(RenderedImage var1) throws IOException;
}
