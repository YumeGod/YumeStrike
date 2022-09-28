package org.apache.batik.ext.awt.image.codec.png;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
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

public class PNGRegistryEntry extends MagicNumberRegistryEntry {
   static final byte[] signature = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};

   public PNGRegistryEntry() {
      super("PNG", (String)"png", (String)"image/png", 0, signature);
   }

   public Filter handleStream(final InputStream var1, ParsedURL var2, final boolean var3) {
      final DeferRable var4 = new DeferRable();
      final String var7;
      final Object[] var8;
      if (var2 != null) {
         var7 = "url.format.unreadable";
         var8 = new Object[]{"PNG", var2};
      } else {
         var7 = "stream.format.unreadable";
         var8 = new Object[]{"PNG"};
      }

      Thread var9 = new Thread() {
         public void run() {
            Object var1x;
            try {
               PNGDecodeParam var2 = new PNGDecodeParam();
               var2.setExpandPalette(true);
               if (var3) {
                  var2.setPerformGammaCorrection(false);
               } else {
                  var2.setPerformGammaCorrection(true);
                  var2.setDisplayExponent(2.2F);
               }

               PNGRed var3x = new PNGRed(var1, var2);
               var4.setBounds(new Rectangle2D.Double(0.0, 0.0, (double)var3x.getWidth(), (double)var3x.getHeight()));
               Any2sRGBRed var11 = new Any2sRGBRed(var3x);
               FormatRed var12 = new FormatRed(var11, GraphicsUtil.sRGB_Unpre);
               WritableRaster var4x = (WritableRaster)var12.getData();
               ColorModel var5 = var12.getColorModel();
               BufferedImage var6 = new BufferedImage(var5, var4x, var5.isAlphaPremultiplied(), (Hashtable)null);
               CachableRed var13 = GraphicsUtil.wrap(var6);
               var1x = new RedRable(var13);
            } catch (IOException var7x) {
               var1x = ImageTagRegistry.getBrokenLinkImage(PNGRegistryEntry.this, var7, var8);
            } catch (ThreadDeath var8x) {
               Filter var10 = ImageTagRegistry.getBrokenLinkImage(PNGRegistryEntry.this, var7, var8);
               var4.setSource(var10);
               throw var8x;
            } catch (Throwable var9) {
               var1x = ImageTagRegistry.getBrokenLinkImage(PNGRegistryEntry.this, var7, var8);
            }

            var4.setSource((Filter)var1x);
         }
      };
      var9.start();
      return var4;
   }
}
