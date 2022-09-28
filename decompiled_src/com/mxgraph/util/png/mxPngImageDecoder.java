package com.mxgraph.util.png;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;

public class mxPngImageDecoder {
   protected InputStream input;
   protected mxPngDecodeParam param;

   public mxPngImageDecoder(InputStream var1, mxPngDecodeParam var2) {
      this.input = var1;
      this.param = var2;
   }

   public mxPngDecodeParam getParam() {
      return this.param;
   }

   public void setParam(mxPngDecodeParam var1) {
      this.param = var1;
   }

   public InputStream getInputStream() {
      return this.input;
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

   public RenderedImage decodeAsRenderedImage(int var1) throws IOException {
      if (var1 != 0) {
         throw new IOException("PNGImageDecoder19");
      } else {
         return new PNGImage(this.input, this.param);
      }
   }
}
