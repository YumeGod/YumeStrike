package org.apache.batik.ext.awt.image.codec.imageio;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.renderable.DeferRable;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.RedRable;
import org.apache.batik.ext.awt.image.rendered.Any2sRGBRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.FormatRed;
import org.apache.batik.ext.awt.image.spi.ImageTagRegistry;
import org.apache.batik.ext.awt.image.spi.MagicNumberRegistryEntry;
import org.apache.batik.util.ParsedURL;

public abstract class AbstractImageIORegistryEntry extends MagicNumberRegistryEntry {
   public AbstractImageIORegistryEntry(String var1, String[] var2, String[] var3, MagicNumberRegistryEntry.MagicNumber[] var4) {
      super(var1, 1100.0F, var2, var3, var4);
   }

   public AbstractImageIORegistryEntry(String var1, String var2, String var3, int var4, byte[] var5) {
      super(var1, 1100.0F, var2, var3, var4, var5);
   }

   public Filter handleStream(final InputStream var1, ParsedURL var2, boolean var3) {
      final DeferRable var4 = new DeferRable();
      final String var6;
      final Object[] var7;
      if (var2 != null) {
         var6 = "url.format.unreadable";
         var7 = new Object[]{this.getFormatName(), var2};
      } else {
         var6 = "stream.format.unreadable";
         var7 = new Object[]{this.getFormatName()};
      }

      Thread var8 = new Thread() {
         public void run() {
            Object var1x;
            try {
               Iterator var2 = ImageIO.getImageReadersByMIMEType(AbstractImageIORegistryEntry.this.getMimeTypes().get(0).toString());
               if (!var2.hasNext()) {
                  throw new UnsupportedOperationException("No image reader for " + AbstractImageIORegistryEntry.this.getFormatName() + " available!");
               }

               ImageReader var3 = (ImageReader)var2.next();
               ImageInputStream var4x = ImageIO.createImageInputStream(var1);
               var3.setInput(var4x, true);
               byte var5 = 0;
               var4.setBounds(new Rectangle2D.Double(0.0, 0.0, (double)var3.getWidth(var5), (double)var3.getHeight(var5)));
               BufferedImage var7x = var3.read(var5);
               CachableRed var6x = GraphicsUtil.wrap(var7x);
               Any2sRGBRed var15 = new Any2sRGBRed(var6x);
               FormatRed var16 = new FormatRed(var15, GraphicsUtil.sRGB_Unpre);
               WritableRaster var8 = (WritableRaster)var16.getData();
               ColorModel var9 = var16.getColorModel();
               BufferedImage var10 = new BufferedImage(var9, var8, var9.isAlphaPremultiplied(), (Hashtable)null);
               var6x = GraphicsUtil.wrap(var10);
               var1x = new RedRable(var6x);
            } catch (IOException var11) {
               var1x = ImageTagRegistry.getBrokenLinkImage(AbstractImageIORegistryEntry.this, var6, var7);
            } catch (ThreadDeath var12) {
               Filter var14 = ImageTagRegistry.getBrokenLinkImage(AbstractImageIORegistryEntry.this, var6, var7);
               var4.setSource(var14);
               throw var12;
            } catch (Throwable var13) {
               var1x = ImageTagRegistry.getBrokenLinkImage(AbstractImageIORegistryEntry.this, var6, var7);
            }

            var4.setSource((Filter)var1x);
         }
      };
      var8.start();
      return var4;
   }
}
