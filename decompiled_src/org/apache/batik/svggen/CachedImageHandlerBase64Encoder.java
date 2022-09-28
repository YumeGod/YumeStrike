package org.apache.batik.svggen;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;
import org.apache.batik.util.Base64EncoderStream;
import org.w3c.dom.Element;

public class CachedImageHandlerBase64Encoder extends DefaultCachedImageHandler {
   public CachedImageHandlerBase64Encoder() {
      this.setImageCacher(new ImageCacher.Embedded());
   }

   public Element createElement(SVGGeneratorContext var1) {
      Element var2 = var1.getDOMFactory().createElementNS("http://www.w3.org/2000/svg", "use");
      return var2;
   }

   public String getRefPrefix() {
      return "";
   }

   protected AffineTransform handleTransform(Element var1, double var2, double var4, double var6, double var8, double var10, double var12, SVGGeneratorContext var14) {
      AffineTransform var15 = new AffineTransform();
      double var16 = var10 / var6;
      double var18 = var12 / var8;
      var15.translate(var2, var4);
      if (var16 != 1.0 || var18 != 1.0) {
         var15.scale(var16, var18);
      }

      return !var15.isIdentity() ? var15 : null;
   }

   public void encodeImage(BufferedImage var1, OutputStream var2) throws IOException {
      Base64EncoderStream var3 = new Base64EncoderStream(var2);
      ImageWriter var4 = ImageWriterRegistry.getInstance().getWriterFor("image/png");
      var4.writeImage(var1, var3);
      var3.close();
   }

   public int getBufferedImageType() {
      return 2;
   }
}
