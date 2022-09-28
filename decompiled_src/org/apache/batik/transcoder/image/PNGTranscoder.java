package org.apache.batik.transcoder.image;

import java.awt.image.BufferedImage;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.OutputStream;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.resources.Messages;
import org.apache.batik.transcoder.keys.FloatKey;
import org.apache.batik.transcoder.keys.IntegerKey;

public class PNGTranscoder extends ImageTranscoder {
   public static final TranscodingHints.Key KEY_GAMMA = new FloatKey();
   public static final float[] DEFAULT_CHROMA = new float[]{0.3127F, 0.329F, 0.64F, 0.33F, 0.3F, 0.6F, 0.15F, 0.06F};
   public static final TranscodingHints.Key KEY_INDEXED = new IntegerKey();

   public PNGTranscoder() {
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
      OutputStream var3 = var2.getOutputStream();
      if (var3 == null) {
         throw new TranscoderException(Messages.formatMessage("png.badoutput", (Object[])null));
      } else {
         boolean var4 = false;
         if (this.hints.containsKey(KEY_FORCE_TRANSPARENT_WHITE)) {
            var4 = (Boolean)this.hints.get(KEY_FORCE_TRANSPARENT_WHITE);
         }

         if (var4) {
            SinglePixelPackedSampleModel var5 = (SinglePixelPackedSampleModel)var1.getSampleModel();
            this.forceTransparentWhite(var1, var5);
         }

         WriteAdapter var6 = this.getWriteAdapter("org.apache.batik.ext.awt.image.codec.png.PNGTranscoderInternalCodecWriteAdapter");
         if (var6 == null) {
            var6 = this.getWriteAdapter("org.apache.batik.transcoder.image.PNGTranscoderImageIOWriteAdapter");
         }

         if (var6 == null) {
            throw new TranscoderException("Could not write PNG file because no WriteAdapter is availble");
         } else {
            var6.writeImage(this, var1, var2);
         }
      }
   }

   public interface WriteAdapter {
      void writeImage(PNGTranscoder var1, BufferedImage var2, TranscoderOutput var3) throws TranscoderException;
   }
}
