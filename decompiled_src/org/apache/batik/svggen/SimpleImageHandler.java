package org.apache.batik.svggen;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import org.w3c.dom.Element;

public class SimpleImageHandler implements GenericImageHandler, SVGSyntax, ErrorConstants {
   static final String XLINK_NAMESPACE_URI = "http://www.w3.org/1999/xlink";
   protected ImageHandler imageHandler;

   public SimpleImageHandler(ImageHandler var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.imageHandler = var1;
      }
   }

   public void setDOMTreeManager(DOMTreeManager var1) {
   }

   public Element createElement(SVGGeneratorContext var1) {
      Element var2 = var1.getDOMFactory().createElementNS("http://www.w3.org/2000/svg", "image");
      return var2;
   }

   public AffineTransform handleImage(Image var1, Element var2, int var3, int var4, int var5, int var6, SVGGeneratorContext var7) {
      int var8 = var1.getWidth((ImageObserver)null);
      int var9 = var1.getHeight((ImageObserver)null);
      if (var8 != 0 && var9 != 0 && var5 != 0 && var6 != 0) {
         this.imageHandler.handleImage(var1, var2, var7);
         this.setImageAttributes(var2, (double)var3, (double)var4, (double)var5, (double)var6, var7);
      } else {
         this.handleEmptyImage(var2);
      }

      return null;
   }

   public AffineTransform handleImage(RenderedImage var1, Element var2, int var3, int var4, int var5, int var6, SVGGeneratorContext var7) {
      int var8 = var1.getWidth();
      int var9 = var1.getHeight();
      if (var8 != 0 && var9 != 0 && var5 != 0 && var6 != 0) {
         this.imageHandler.handleImage(var1, var2, var7);
         this.setImageAttributes(var2, (double)var3, (double)var4, (double)var5, (double)var6, var7);
      } else {
         this.handleEmptyImage(var2);
      }

      return null;
   }

   public AffineTransform handleImage(RenderableImage var1, Element var2, double var3, double var5, double var7, double var9, SVGGeneratorContext var11) {
      double var12 = (double)var1.getWidth();
      double var14 = (double)var1.getHeight();
      if (var12 != 0.0 && var14 != 0.0 && var7 != 0.0 && var9 != 0.0) {
         this.imageHandler.handleImage(var1, var2, var11);
         this.setImageAttributes(var2, var3, var5, var7, var9, var11);
      } else {
         this.handleEmptyImage(var2);
      }

      return null;
   }

   protected void setImageAttributes(Element var1, double var2, double var4, double var6, double var8, SVGGeneratorContext var10) {
      var1.setAttributeNS((String)null, "x", var10.doubleString(var2));
      var1.setAttributeNS((String)null, "y", var10.doubleString(var4));
      var1.setAttributeNS((String)null, "width", var10.doubleString(var6));
      var1.setAttributeNS((String)null, "height", var10.doubleString(var8));
      var1.setAttributeNS((String)null, "preserveAspectRatio", "none");
   }

   protected void handleEmptyImage(Element var1) {
      var1.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "");
      var1.setAttributeNS((String)null, "width", "0");
      var1.setAttributeNS((String)null, "height", "0");
   }
}
