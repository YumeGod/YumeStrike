package org.apache.batik.svggen;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import org.w3c.dom.Element;

public abstract class DefaultCachedImageHandler implements CachedImageHandler, SVGSyntax, ErrorConstants {
   static final String XLINK_NAMESPACE_URI = "http://www.w3.org/1999/xlink";
   static final AffineTransform IDENTITY = new AffineTransform();
   private static Method createGraphics = null;
   private static boolean initDone = false;
   private static final Class[] paramc;
   private static Object[] paramo;
   protected ImageCacher imageCacher;
   // $FF: synthetic field
   static Class class$java$awt$image$BufferedImage;

   public ImageCacher getImageCacher() {
      return this.imageCacher;
   }

   void setImageCacher(ImageCacher var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         DOMTreeManager var2 = null;
         if (this.imageCacher != null) {
            var2 = this.imageCacher.getDOMTreeManager();
         }

         this.imageCacher = var1;
         if (var2 != null) {
            this.imageCacher.setDOMTreeManager(var2);
         }

      }
   }

   public void setDOMTreeManager(DOMTreeManager var1) {
      this.imageCacher.setDOMTreeManager(var1);
   }

   private static Graphics2D createGraphics(BufferedImage var0) {
      if (!initDone) {
         try {
            Class var1 = Class.forName("org.apache.batik.ext.awt.image.GraphicsUtil");
            createGraphics = var1.getMethod("createGraphics", paramc);
            paramo = new Object[1];
         } catch (Throwable var7) {
         } finally {
            initDone = true;
         }
      }

      if (createGraphics == null) {
         return var0.createGraphics();
      } else {
         paramo[0] = var0;
         Graphics2D var9 = null;

         try {
            var9 = (Graphics2D)createGraphics.invoke((Object)null, paramo);
         } catch (Exception var6) {
         }

         return var9;
      }
   }

   public Element createElement(SVGGeneratorContext var1) {
      Element var2 = var1.getDOMFactory().createElementNS("http://www.w3.org/2000/svg", "image");
      return var2;
   }

   public AffineTransform handleImage(Image var1, Element var2, int var3, int var4, int var5, int var6, SVGGeneratorContext var7) {
      int var8 = var1.getWidth((ImageObserver)null);
      int var9 = var1.getHeight((ImageObserver)null);
      AffineTransform var10 = null;
      if (var8 != 0 && var9 != 0 && var5 != 0 && var6 != 0) {
         try {
            this.handleHREF(var1, var2, var7);
         } catch (SVGGraphics2DIOException var14) {
            SVGGraphics2DIOException var11 = var14;

            try {
               var7.errorHandler.handleError(var11);
            } catch (SVGGraphics2DIOException var13) {
               throw new SVGGraphics2DRuntimeException(var13);
            }
         }

         var10 = this.handleTransform(var2, (double)var3, (double)var4, (double)var8, (double)var9, (double)var5, (double)var6, var7);
      } else {
         this.handleEmptyImage(var2);
      }

      return var10;
   }

   public AffineTransform handleImage(RenderedImage var1, Element var2, int var3, int var4, int var5, int var6, SVGGeneratorContext var7) {
      int var8 = var1.getWidth();
      int var9 = var1.getHeight();
      AffineTransform var10 = null;
      if (var8 != 0 && var9 != 0 && var5 != 0 && var6 != 0) {
         try {
            this.handleHREF(var1, var2, var7);
         } catch (SVGGraphics2DIOException var14) {
            SVGGraphics2DIOException var11 = var14;

            try {
               var7.errorHandler.handleError(var11);
            } catch (SVGGraphics2DIOException var13) {
               throw new SVGGraphics2DRuntimeException(var13);
            }
         }

         var10 = this.handleTransform(var2, (double)var3, (double)var4, (double)var8, (double)var9, (double)var5, (double)var6, var7);
      } else {
         this.handleEmptyImage(var2);
      }

      return var10;
   }

   public AffineTransform handleImage(RenderableImage var1, Element var2, double var3, double var5, double var7, double var9, SVGGeneratorContext var11) {
      double var12 = (double)var1.getWidth();
      double var14 = (double)var1.getHeight();
      AffineTransform var16 = null;
      if (var12 != 0.0 && var14 != 0.0 && var7 != 0.0 && var9 != 0.0) {
         try {
            this.handleHREF(var1, var2, var11);
         } catch (SVGGraphics2DIOException var20) {
            SVGGraphics2DIOException var17 = var20;

            try {
               var11.errorHandler.handleError(var17);
            } catch (SVGGraphics2DIOException var19) {
               throw new SVGGraphics2DRuntimeException(var19);
            }
         }

         var16 = this.handleTransform(var2, var3, var5, var12, var14, var7, var9, var11);
      } else {
         this.handleEmptyImage(var2);
      }

      return var16;
   }

   protected AffineTransform handleTransform(Element var1, double var2, double var4, double var6, double var8, double var10, double var12, SVGGeneratorContext var14) {
      var1.setAttributeNS((String)null, "x", var14.doubleString(var2));
      var1.setAttributeNS((String)null, "y", var14.doubleString(var4));
      var1.setAttributeNS((String)null, "width", var14.doubleString(var10));
      var1.setAttributeNS((String)null, "height", var14.doubleString(var12));
      return null;
   }

   protected void handleEmptyImage(Element var1) {
      var1.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "");
      var1.setAttributeNS((String)null, "width", "0");
      var1.setAttributeNS((String)null, "height", "0");
   }

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
               BufferedImage var6 = this.buildBufferedImage(new Dimension(var4, var5));
               Graphics2D var7 = createGraphics(var6);
               var7.drawImage(var1, 0, 0, (ImageObserver)null);
               var7.dispose();
               this.handleHREF((RenderedImage)var6, var2, var3);
            }
         } else {
            this.handleEmptyImage(var2);
         }

      }
   }

   public BufferedImage buildBufferedImage(Dimension var1) {
      return new BufferedImage(var1.width, var1.height, this.getBufferedImageType());
   }

   protected void handleHREF(RenderedImage var1, Element var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      BufferedImage var4 = null;
      if (var1 instanceof BufferedImage && ((BufferedImage)var1).getType() == this.getBufferedImageType()) {
         var4 = (BufferedImage)var1;
      } else {
         Dimension var5 = new Dimension(var1.getWidth(), var1.getHeight());
         var4 = this.buildBufferedImage(var5);
         Graphics2D var6 = createGraphics(var4);
         var6.drawRenderedImage(var1, IDENTITY);
         var6.dispose();
      }

      this.cacheBufferedImage(var2, var4, var3);
   }

   protected void handleHREF(RenderableImage var1, Element var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      Dimension var4 = new Dimension((int)Math.ceil((double)var1.getWidth()), (int)Math.ceil((double)var1.getHeight()));
      BufferedImage var5 = this.buildBufferedImage(var4);
      Graphics2D var6 = createGraphics(var5);
      var6.drawRenderableImage(var1, IDENTITY);
      var6.dispose();
      this.handleHREF((RenderedImage)var5, var2, var3);
   }

   protected void cacheBufferedImage(Element var1, BufferedImage var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      if (var3 == null) {
         throw new SVGGraphics2DRuntimeException("generatorContext should not be null");
      } else {
         ByteArrayOutputStream var4;
         try {
            var4 = new ByteArrayOutputStream();
            this.encodeImage(var2, var4);
            var4.flush();
            var4.close();
         } catch (IOException var6) {
            throw new SVGGraphics2DIOException("unexpected exception", var6);
         }

         String var5 = this.imageCacher.lookup(var4, var2.getWidth(), var2.getHeight(), var3);
         var1.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", this.getRefPrefix() + var5);
      }
   }

   public abstract String getRefPrefix();

   public abstract void encodeImage(BufferedImage var1, OutputStream var2) throws IOException;

   public abstract int getBufferedImageType();

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      paramc = new Class[]{class$java$awt$image$BufferedImage == null ? (class$java$awt$image$BufferedImage = class$("java.awt.image.BufferedImage")) : class$java$awt$image$BufferedImage};
      paramo = null;
   }
}
