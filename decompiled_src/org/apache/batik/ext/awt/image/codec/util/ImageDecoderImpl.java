package org.apache.batik.ext.awt.image.codec.util;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;

public abstract class ImageDecoderImpl implements ImageDecoder {
   protected SeekableStream input;
   protected ImageDecodeParam param;

   public ImageDecoderImpl(SeekableStream var1, ImageDecodeParam var2) {
      this.input = var1;
      this.param = var2;
   }

   public ImageDecoderImpl(InputStream var1, ImageDecodeParam var2) {
      this.input = new ForwardSeekableStream(var1);
      this.param = var2;
   }

   public ImageDecodeParam getParam() {
      return this.param;
   }

   public void setParam(ImageDecodeParam var1) {
      this.param = var1;
   }

   public SeekableStream getInputStream() {
      return this.input;
   }

   public int getNumPages() throws IOException {
      return 1;
   }

   public Raster decodeAsRaster() throws IOException {
      return this.decodeAsRaster(0);
   }

   public Raster decodeAsRaster(int var1) throws IOException {
      RenderedImage var2 = this.decodeAsRenderedImage(var1);
      return var2.getData();
   }

   public RenderedImage decodeAsRenderedImage() throws IOException {
      return this.decodeAsRenderedImage(0);
   }

   public abstract RenderedImage decodeAsRenderedImage(int var1) throws IOException;
}
