package org.apache.batik.transcoder.image;

import java.awt.image.BufferedImage;
import java.awt.image.SinglePixelPackedSampleModel;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.keys.StringKey;

public class TIFFTranscoder extends ImageTranscoder {
   public static final TranscodingHints.Key KEY_FORCE_TRANSPARENT_WHITE;
   public static final TranscodingHints.Key KEY_COMPRESSION_METHOD;

   public TIFFTranscoder() {
      this.hints.put(KEY_FORCE_TRANSPARENT_WHITE, Boolean.FALSE);
   }

   public UserAgent getUserAgent() {
      return this.userAgent;
   }

   public BufferedImage createImage(int var1, int var2) {
      return new BufferedImage(var1, var2, 2);
   }

   private WriteAdapter getWriteAdapter(String var1) {
      try {
         Class var3 = Class.forName(var1);
         WriteAdapter var2 = (WriteAdapter)var3.newInstance();
         return var2;
      } catch (ClassNotFoundException var4) {
         return null;
      } catch (InstantiationException var5) {
         return null;
      } catch (IllegalAccessException var6) {
         return null;
      }
   }

   public void writeImage(BufferedImage var1, TranscoderOutput var2) throws TranscoderException {
      boolean var3 = false;
      if (this.hints.containsKey(PNGTranscoder.KEY_FORCE_TRANSPARENT_WHITE)) {
         var3 = (Boolean)this.hints.get(PNGTranscoder.KEY_FORCE_TRANSPARENT_WHITE);
      }

      if (var3) {
         SinglePixelPackedSampleModel var4 = (SinglePixelPackedSampleModel)var1.getSampleModel();
         this.forceTransparentWhite(var1, var4);
      }

      WriteAdapter var5 = this.getWriteAdapter("org.apache.batik.ext.awt.image.codec.tiff.TIFFTranscoderInternalCodecWriteAdapter");
      if (var5 == null) {
         var5 = this.getWriteAdapter("org.apache.batik.transcoder.image.TIFFTranscoderImageIOWriteAdapter");
      }

      if (var5 == null) {
         throw new TranscoderException("Could not write TIFF file because no WriteAdapter is availble");
      } else {
         var5.writeImage(this, var1, var2);
      }
   }

   static {
      KEY_FORCE_TRANSPARENT_WHITE = ImageTranscoder.KEY_FORCE_TRANSPARENT_WHITE;
      KEY_COMPRESSION_METHOD = new StringKey();
   }

   public interface WriteAdapter {
      void writeImage(TIFFTranscoder var1, BufferedImage var2, TranscoderOutput var3) throws TranscoderException;
   }
}
