package org.apache.batik.svggen;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import org.w3c.dom.Element;

public abstract class AbstractImageHandlerEncoder extends DefaultImageHandler {
   private static final AffineTransform IDENTITY = new AffineTransform();
   private String imageDir = "";
   private String urlRoot = "";
   private static Method createGraphics = null;
   private static boolean initDone = false;
   private static final Class[] paramc;
   private static Object[] paramo;
   // $FF: synthetic field
   static Class class$java$awt$image$BufferedImage;

   private static Graphics2D createGraphics(BufferedImage var0) {
      if (!initDone) {
         try {
            Class var1 = Class.forName("org.apache.batik.ext.awt.image.GraphicsUtil");
            createGraphics = var1.getMethod("createGraphics", paramc);
            paramo = new Object[1];
         } catch (ThreadDeath var8) {
            throw var8;
         } catch (Throwable var9) {
         } finally {
            initDone = true;
         }
      }

      if (createGraphics == null) {
         return var0.createGraphics();
      } else {
         paramo[0] = var0;
         Graphics2D var11 = null;

         try {
            var11 = (Graphics2D)createGraphics.invoke((Object)null, paramo);
         } catch (Exception var7) {
         }

         return var11;
      }
   }

   public AbstractImageHandlerEncoder(String var1, String var2) throws SVGGraphics2DIOException {
      if (var1 == null) {
         throw new SVGGraphics2DRuntimeException("imageDir should not be null");
      } else {
         File var3 = new File(var1);
         if (!var3.exists()) {
            throw new SVGGraphics2DRuntimeException("imageDir does not exist");
         } else {
            this.imageDir = var1;
            if (var2 != null) {
               this.urlRoot = var2;
            } else {
               try {
                  this.urlRoot = var3.toURL().toString();
               } catch (MalformedURLException var5) {
                  throw new SVGGraphics2DIOException("cannot convert imageDir to a URL value : " + var5.getMessage(), var5);
               }
            }

         }
      }
   }

   protected void handleHREF(Image var1, Element var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      Dimension var4 = new Dimension(var1.getWidth((ImageObserver)null), var1.getHeight((ImageObserver)null));
      BufferedImage var5 = this.buildBufferedImage(var4);
      Graphics2D var6 = createGraphics(var5);
      var6.drawImage(var1, 0, 0, (ImageObserver)null);
      var6.dispose();
      this.saveBufferedImageToFile(var2, var5, var3);
   }

   protected void handleHREF(RenderedImage var1, Element var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      Dimension var4 = new Dimension(var1.getWidth(), var1.getHeight());
      BufferedImage var5 = this.buildBufferedImage(var4);
      Graphics2D var6 = createGraphics(var5);
      var6.drawRenderedImage(var1, IDENTITY);
      var6.dispose();
      this.saveBufferedImageToFile(var2, var5, var3);
   }

   protected void handleHREF(RenderableImage var1, Element var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      Dimension var4 = new Dimension((int)Math.ceil((double)var1.getWidth()), (int)Math.ceil((double)var1.getHeight()));
      BufferedImage var5 = this.buildBufferedImage(var4);
      Graphics2D var6 = createGraphics(var5);
      var6.drawRenderableImage(var1, IDENTITY);
      var6.dispose();
      this.saveBufferedImageToFile(var2, var5, var3);
   }

   private void saveBufferedImageToFile(Element var1, BufferedImage var2, SVGGeneratorContext var3) throws SVGGraphics2DIOException {
      if (var3 == null) {
         throw new SVGGraphics2DRuntimeException("generatorContext should not be null");
      } else {
         File var4 = null;

         while(var4 == null) {
            String var5 = var3.idGenerator.generateID(this.getPrefix());
            var4 = new File(this.imageDir, var5 + this.getSuffix());
            if (var4.exists()) {
               var4 = null;
            }
         }

         this.encodeImage(var2, var4);
         var1.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", this.urlRoot + "/" + var4.getName());
      }
   }

   public abstract String getSuffix();

   public abstract String getPrefix();

   public abstract void encodeImage(BufferedImage var1, File var2) throws SVGGraphics2DIOException;

   public abstract BufferedImage buildBufferedImage(Dimension var1);

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
