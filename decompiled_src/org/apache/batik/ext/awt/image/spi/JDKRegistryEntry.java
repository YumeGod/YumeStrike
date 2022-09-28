package org.apache.batik.ext.awt.image.spi;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.renderable.DeferRable;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.RedRable;
import org.apache.batik.util.ParsedURL;

public class JDKRegistryEntry extends AbstractRegistryEntry implements URLRegistryEntry {
   public static final float PRIORITY = 1000000.0F;

   public JDKRegistryEntry() {
      super("JDK", 1000000.0F, new String[0], new String[]{"image/gif"});
   }

   public boolean isCompatibleURL(ParsedURL var1) {
      try {
         new URL(var1.toString());
         return true;
      } catch (MalformedURLException var3) {
         return false;
      }
   }

   public Filter handleURL(ParsedURL var1, boolean var2) {
      final URL var3;
      try {
         var3 = new URL(var1.toString());
      } catch (MalformedURLException var8) {
         return null;
      }

      final DeferRable var4 = new DeferRable();
      final String var5;
      final Object[] var6;
      if (var1 != null) {
         var5 = "url.format.unreadable";
         var6 = new Object[]{"JDK", var3};
      } else {
         var5 = "stream.format.unreadable";
         var6 = new Object[]{"JDK"};
      }

      Thread var7 = new Thread() {
         public void run() {
            Object var1 = null;

            try {
               Toolkit var2 = Toolkit.getDefaultToolkit();
               Image var3x = var2.createImage(var3);
               if (var3x != null) {
                  RenderedImage var4x = JDKRegistryEntry.this.loadImage(var3x, var4);
                  if (var4x != null) {
                     var1 = new RedRable(GraphicsUtil.wrap(var4x));
                  }
               }
            } catch (ThreadDeath var5x) {
               Filter var7 = ImageTagRegistry.getBrokenLinkImage(JDKRegistryEntry.this, var5, var6);
               var4.setSource(var7);
               throw var5x;
            } catch (Throwable var6x) {
            }

            if (var1 == null) {
               var1 = ImageTagRegistry.getBrokenLinkImage(JDKRegistryEntry.this, var5, var6);
            }

            var4.setSource((Filter)var1);
         }
      };
      var7.start();
      return var4;
   }

   public RenderedImage loadImage(Image var1, DeferRable var2) {
      if (var1 instanceof RenderedImage) {
         return (RenderedImage)var1;
      } else {
         MyImgObs var3 = new MyImgObs();
         Toolkit.getDefaultToolkit().prepareImage(var1, -1, -1, var3);
         var3.waitTilWidthHeightDone();
         if (var3.imageError) {
            return null;
         } else {
            int var4 = var3.width;
            int var5 = var3.height;
            var2.setBounds(new Rectangle2D.Double(0.0, 0.0, (double)var4, (double)var5));
            BufferedImage var6 = new BufferedImage(var4, var5, 2);
            Graphics2D var7 = var6.createGraphics();
            var3.waitTilImageDone();
            if (var3.imageError) {
               return null;
            } else {
               var2.setProperties(new HashMap());
               var7.drawImage(var1, 0, 0, (ImageObserver)null);
               var7.dispose();
               return var6;
            }
         }
      }
   }

   public static class MyImgObs implements ImageObserver {
      boolean widthDone = false;
      boolean heightDone = false;
      boolean imageDone = false;
      int width = -1;
      int height = -1;
      boolean imageError = false;
      int IMG_BITS = 224;

      public void clear() {
         this.width = -1;
         this.height = -1;
         this.widthDone = false;
         this.heightDone = false;
         this.imageDone = false;
      }

      public boolean imageUpdate(Image var1, int var2, int var3, int var4, int var5, int var6) {
         synchronized(this) {
            boolean var8 = false;
            if ((var2 & 1) != 0) {
               this.width = var5;
            }

            if ((var2 & 2) != 0) {
               this.height = var6;
            }

            if ((var2 & 32) != 0) {
               this.width = var5;
               this.height = var6;
            }

            if ((var2 & this.IMG_BITS) != 0) {
               if (!this.widthDone || !this.heightDone || !this.imageDone) {
                  this.widthDone = true;
                  this.heightDone = true;
                  this.imageDone = true;
                  var8 = true;
               }

               if ((var2 & 64) != 0) {
                  this.imageError = true;
               }
            }

            if (!this.widthDone && this.width != -1) {
               var8 = true;
               this.widthDone = true;
            }

            if (!this.heightDone && this.height != -1) {
               var8 = true;
               this.heightDone = true;
            }

            if (var8) {
               this.notifyAll();
            }

            return true;
         }
      }

      public synchronized void waitTilWidthHeightDone() {
         while(!this.widthDone || !this.heightDone) {
            try {
               this.wait();
            } catch (InterruptedException var2) {
            }
         }

      }

      public synchronized void waitTilWidthDone() {
         while(!this.widthDone) {
            try {
               this.wait();
            } catch (InterruptedException var2) {
            }
         }

      }

      public synchronized void waitTilHeightDone() {
         while(!this.heightDone) {
            try {
               this.wait();
            } catch (InterruptedException var2) {
            }
         }

      }

      public synchronized void waitTilImageDone() {
         while(!this.imageDone) {
            try {
               this.wait();
            } catch (InterruptedException var2) {
            }
         }

      }
   }
}
