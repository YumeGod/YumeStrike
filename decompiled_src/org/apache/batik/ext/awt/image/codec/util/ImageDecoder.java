package org.apache.batik.ext.awt.image.codec.util;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;

public interface ImageDecoder {
   ImageDecodeParam getParam();

   void setParam(ImageDecodeParam var1);

   SeekableStream getInputStream();

   int getNumPages() throws IOException;

   Raster decodeAsRaster() throws IOException;

   Raster decodeAsRaster(int var1) throws IOException;

   RenderedImage decodeAsRenderedImage() throws IOException;

   RenderedImage decodeAsRenderedImage(int var1) throws IOException;
}
