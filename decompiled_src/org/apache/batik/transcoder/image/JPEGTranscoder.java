package org.apache.batik.transcoder.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.resources.Messages;

public class JPEGTranscoder extends ImageTranscoder {
   public static final TranscodingHints.Key KEY_QUALITY = new QualityKey();

   public JPEGTranscoder() {
      this.hints.put(ImageTranscoder.KEY_BACKGROUND_COLOR, Color.white);
   }

   public BufferedImage createImage(int var1, int var2) {
      return new BufferedImage(var1, var2, 1);
   }

   public void writeImage(BufferedImage var1, TranscoderOutput var2) throws TranscoderException {
      OutputStream var3 = var2.getOutputStream();
      OutputStreamWrapper var10 = new OutputStreamWrapper(var3);
      if (var10 == null) {
         throw new TranscoderException(Messages.formatMessage("jpeg.badoutput", (Object[])null));
      } else {
         try {
            float var4;
            if (this.hints.containsKey(KEY_QUALITY)) {
               var4 = (Float)this.hints.get(KEY_QUALITY);
            } else {
               TranscoderException var5 = new TranscoderException(Messages.formatMessage("jpeg.unspecifiedQuality", (Object[])null));
               this.handler.error(var5);
               var4 = 0.75F;
            }

            ImageWriter var11 = ImageWriterRegistry.getInstance().getWriterFor("image/jpeg");
            ImageWriterParams var6 = new ImageWriterParams();
            var6.setJPEGQuality(var4, true);
            float var7 = this.userAgent.getPixelUnitToMillimeter();
            int var8 = (int)(25.4 / (double)var7 + 0.5);
            var6.setResolution(var8);
            var11.writeImage(var1, var10, var6);
            var10.flush();
         } catch (IOException var9) {
            throw new TranscoderException(var9);
         }
      }
   }

   private static class OutputStreamWrapper extends OutputStream {
      OutputStream os;

      OutputStreamWrapper(OutputStream var1) {
         this.os = var1;
      }

      public void close() throws IOException {
         if (this.os != null) {
            try {
               this.os.close();
            } catch (IOException var2) {
               this.os = null;
            }

         }
      }

      public void flush() throws IOException {
         if (this.os != null) {
            try {
               this.os.flush();
            } catch (IOException var2) {
               this.os = null;
            }

         }
      }

      public void write(byte[] var1) throws IOException {
         if (this.os != null) {
            try {
               this.os.write(var1);
            } catch (IOException var3) {
               this.os = null;
            }

         }
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         if (this.os != null) {
            try {
               this.os.write(var1, var2, var3);
            } catch (IOException var5) {
               this.os = null;
            }

         }
      }

      public void write(int var1) throws IOException {
         if (this.os != null) {
            try {
               this.os.write(var1);
            } catch (IOException var3) {
               this.os = null;
            }

         }
      }
   }

   private static class QualityKey extends TranscodingHints.Key {
      private QualityKey() {
      }

      public boolean isCompatibleValue(Object var1) {
         if (!(var1 instanceof Float)) {
            return false;
         } else {
            float var2 = (Float)var1;
            return var2 > 0.0F && var2 <= 1.0F;
         }
      }

      // $FF: synthetic method
      QualityKey(Object var1) {
         this();
      }
   }
}
