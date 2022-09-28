package org.apache.batik.svggen;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import org.apache.batik.util.XMLConstants;
import org.w3c.dom.Element;

public class DefaultImageHandler implements ImageHandler, ErrorConstants, XMLConstants {
   public void handleImage(Image var1, Element var2, SVGGeneratorContext var3) {
      var2.setAttributeNS((String)null, "width", String.valueOf(var1.getWidth((ImageObserver)null)));
      var2.setAttributeNS((String)null, "height", String.valueOf(var1.getHeight((ImageObserver)null)));

      try {
         this.handleHREF(var1, var2, var3);
      } catch (SVGGraphics2DIOException var7) {
         SVGGraphics2DIOException var4 = var7;

         try {
            var3.errorHandler.handleError(var4);
         } catch (SVGGraphics2DIOException var6) {
            throw new SVGGraphics2DRuntimeException(var6);
         }
      }

   }

   public void handleImage(RenderedImage var1, Element var2, SVGGeneratorContext var3) {
      var2.setAttributeNS((String)null, "width", String.valueOf(var1.getWidth()));
      var2.setAttributeNS((String)null, "height", String.valueOf(var1.getHeight()));

      try {
         this.handleHREF(var1, var2, var3);
      } catch (SVGGraphics2DIOException var7) {
         SVGGraphics2DIOException var4 = var7;

         try {
            var3.errorHandler.handleError(var4);
         } catch (SVGGraphics2DIOException var6) {
            throw new SVGGraphics2DRuntimeException(var6);
         }
      }

   }

   public void handleImage(RenderableImage var1, Element var2, SVGGeneratorContext var3) {
      var2.setAttributeNS((String)null, "width", String.valueOf(var1.getWidth()));
      var2.setAttributeNS((String)null, "height", String.valueOf(var1.getHeight()));

      try {
         this.handleHREF(var1, var2, var3);
      } catch (SVGGraphics2DIOException var7) {
         SVGGraphics2DIOException var4 = var7;

         try {
            var3.errorHandler.handleError(var4);
         } catch (SVGGraphics2DIOException var6) {
            throw new SVGGraphics2DRuntimeException(var6);
         }
      }

   }

   protected void handleHREF(Image var1, Element var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      var2.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", var1.toString());
   }

   protected void handleHREF(RenderedImage var1, Element var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      var2.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", var1.toString());
   }

   protected void handleHREF(RenderableImage var1, Element var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      var2.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", var1.toString());
   }
}
