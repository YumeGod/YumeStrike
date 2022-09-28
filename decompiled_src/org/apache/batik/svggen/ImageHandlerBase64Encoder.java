package org.apache.batik.svggen;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;
import org.apache.batik.util.Base64EncoderStream;
import org.w3c.dom.Element;

public class ImageHandlerBase64Encoder extends DefaultImageHandler {
   public void handleHREF(Image var1, Element var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      if (var1 == null) {
         throw new SVGGraphics2DRuntimeException("image should not be null");
      } else {
         int var4 = var1.getWidth((ImageObserver)null);
         int var5 = var1.getHeight((ImageObserver)null);
         if (var4 != 0 && var5 != 0) {
            if (var1 instanceof RenderedImage) {
               this.handleHREF((RenderedImage)var1, var2, var3);
            } else {
               BufferedImage var6 = new BufferedImage(var4, var5, 2);
               Graphics2D var7 = var6.createGraphics();
               var7.drawImage(var1, 0, 0, (ImageObserver)null);
               var7.dispose();
               this.handleHREF((RenderedImage)var6, var2, var3);
            }
         } else {
            this.handleEmptyImage(var2);
         }

      }
   }

   public void handleHREF(RenderableImage var1, Element var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      if (var1 == null) {
         throw new SVGGraphics2DRuntimeException("image should not be null");
      } else {
         RenderedImage var4 = var1.createDefaultRendering();
         if (var4 == null) {
            this.handleEmptyImage(var2);
         } else {
            this.handleHREF(var4, var2, var3);
         }

      }
   }

   protected void handleEmptyImage(Element var1) {
      var1.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "data:image/png;base64,");
      var1.setAttributeNS((String)null, "width", "0");
      var1.setAttributeNS((String)null, "height", "0");
   }

   public void handleHREF(RenderedImage var1, Element var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();
      Base64EncoderStream var5 = new Base64EncoderStream(var4);

      try {
         this.encodeImage(var1, var5);
         var5.close();
      } catch (IOException var7) {
         throw new SVGGraphics2DIOException("unexpected exception", var7);
      }

      var2.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "data:image/png;base64," + var4.toString());
   }

   public void encodeImage(RenderedImage var1, OutputStream var2) throws SVGGraphics2DIOException {
      try {
         ImageWriter var3 = ImageWriterRegistry.getInstance().getWriterFor("image/png");
         var3.writeImage(var1, var2);
      } catch (IOException var4) {
         throw new SVGGraphics2DIOException("unexpected exception");
      }
   }

   public BufferedImage buildBufferedImage(Dimension var1) {
      return new BufferedImage(var1.width, var1.height, 2);
   }
}
