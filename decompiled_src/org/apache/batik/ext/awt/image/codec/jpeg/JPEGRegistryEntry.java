package org.apache.batik.ext.awt.image.codec.jpeg;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.TruncatedFileException;
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

public class JPEGRegistryEntry extends MagicNumberRegistryEntry {
   static final byte[] sigJPEG = new byte[]{-1, -40, -1};
   static final String[] exts = new String[]{"jpeg", "jpg"};
   static final String[] mimeTypes = new String[]{"image/jpeg", "image/jpg"};
   static final MagicNumberRegistryEntry.MagicNumber[] magicNumbers;

   public JPEGRegistryEntry() {
      super("JPEG", exts, mimeTypes, magicNumbers);
   }

   public Filter handleStream(final InputStream var1, ParsedURL var2, boolean var3) {
      final DeferRable var4 = new DeferRable();
      final String var6;
      final Object[] var7;
      if (var2 != null) {
         var6 = "url.format.unreadable";
         var7 = new Object[]{"JPEG", var2};
      } else {
         var6 = "stream.format.unreadable";
         var7 = new Object[]{"JPEG"};
      }

      Thread var8 = new Thread() {
         public void run() {
            Object var1x;
            try {
               JPEGImageDecoder var2 = JPEGCodec.createJPEGDecoder(var1);

               BufferedImage var3;
               try {
                  var3 = var2.decodeAsBufferedImage();
               } catch (TruncatedFileException var7x) {
                  var3 = var7x.getBufferedImage();
                  if (var3 == null) {
                     throw new IOException("JPEG File was truncated");
                  }
               }

               var4.setBounds(new Rectangle2D.Double(0.0, 0.0, (double)var3.getWidth(), (double)var3.getHeight()));
               CachableRed var4x = GraphicsUtil.wrap(var3);
               Any2sRGBRed var12 = new Any2sRGBRed(var4x);
               FormatRed var13 = new FormatRed(var12, GraphicsUtil.sRGB_Unpre);
               WritableRaster var5 = (WritableRaster)var13.getData();
               ColorModel var6x = var13.getColorModel();
               var3 = new BufferedImage(var6x, var5, var6x.isAlphaPremultiplied(), (Hashtable)null);
               var4x = GraphicsUtil.wrap(var3);
               var1x = new RedRable(var4x);
            } catch (IOException var8) {
               var1x = ImageTagRegistry.getBrokenLinkImage(JPEGRegistryEntry.this, var6, var7);
            } catch (ThreadDeath var9) {
               Filter var11 = ImageTagRegistry.getBrokenLinkImage(JPEGRegistryEntry.this, var6, var7);
               var4.setSource(var11);
               throw var9;
            } catch (Throwable var10) {
               var1x = ImageTagRegistry.getBrokenLinkImage(JPEGRegistryEntry.this, var6, var7);
            }

            var4.setSource((Filter)var1x);
         }
      };
      var8.start();
      return var4;
   }

   static {
      magicNumbers = new MagicNumberRegistryEntry.MagicNumber[]{new MagicNumberRegistryEntry.MagicNumber(0, sigJPEG)};
   }
}
