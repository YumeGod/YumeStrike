package org.apache.batik.ext.awt.image.codec.tiff;

import java.io.IOException;
import java.io.InputStream;
import org.apache.batik.ext.awt.image.codec.util.SeekableStream;
import org.apache.batik.ext.awt.image.renderable.DeferRable;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.RedRable;
import org.apache.batik.ext.awt.image.rendered.Any2sRGBRed;
import org.apache.batik.ext.awt.image.spi.ImageTagRegistry;
import org.apache.batik.ext.awt.image.spi.MagicNumberRegistryEntry;
import org.apache.batik.util.ParsedURL;

public class TIFFRegistryEntry extends MagicNumberRegistryEntry {
   static final byte[] sig1 = new byte[]{73, 73, 42, 0};
   static final byte[] sig2 = new byte[]{77, 77, 0, 42};
   static MagicNumberRegistryEntry.MagicNumber[] magicNumbers;
   static final String[] exts;
   static final String[] mimeTypes;

   public TIFFRegistryEntry() {
      super("TIFF", exts, mimeTypes, magicNumbers);
   }

   public Filter handleStream(final InputStream var1, ParsedURL var2, boolean var3) {
      final DeferRable var4 = new DeferRable();
      final String var6;
      final Object[] var7;
      if (var2 != null) {
         var6 = "url.format.unreadable";
         var7 = new Object[]{"TIFF", var2};
      } else {
         var6 = "stream.format.unreadable";
         var7 = new Object[]{"TIFF"};
      }

      Thread var8 = new Thread() {
         public void run() {
            Object var1x;
            try {
               TIFFDecodeParam var2 = new TIFFDecodeParam();
               SeekableStream var3 = SeekableStream.wrapInputStream(var1, true);
               TIFFImage var4x = new TIFFImage(var3, var2, 0);
               Any2sRGBRed var9 = new Any2sRGBRed(var4x);
               var1x = new RedRable(var9);
            } catch (IOException var5) {
               var1x = ImageTagRegistry.getBrokenLinkImage(TIFFRegistryEntry.this, var6, var7);
            } catch (ThreadDeath var6x) {
               Filter var8 = ImageTagRegistry.getBrokenLinkImage(TIFFRegistryEntry.this, var6, var7);
               var4.setSource(var8);
               throw var6x;
            } catch (Throwable var7x) {
               var1x = ImageTagRegistry.getBrokenLinkImage(TIFFRegistryEntry.this, var6, var7);
            }

            var4.setSource((Filter)var1x);
         }
      };
      var8.start();
      return var4;
   }

   static {
      magicNumbers = new MagicNumberRegistryEntry.MagicNumber[]{new MagicNumberRegistryEntry.MagicNumber(0, sig1), new MagicNumberRegistryEntry.MagicNumber(0, sig2)};
      exts = new String[]{"tiff", "tif"};
      mimeTypes = new String[]{"image/tiff", "image/tif"};
   }
}
