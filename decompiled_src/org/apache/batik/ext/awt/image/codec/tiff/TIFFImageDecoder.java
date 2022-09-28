package org.apache.batik.ext.awt.image.codec.tiff;

import java.awt.image.RenderedImage;
import java.io.IOException;
import org.apache.batik.ext.awt.image.codec.util.ImageDecodeParam;
import org.apache.batik.ext.awt.image.codec.util.ImageDecoderImpl;
import org.apache.batik.ext.awt.image.codec.util.SeekableStream;

public class TIFFImageDecoder extends ImageDecoderImpl {
   public static final int TIFF_IMAGE_WIDTH = 256;
   public static final int TIFF_IMAGE_LENGTH = 257;
   public static final int TIFF_BITS_PER_SAMPLE = 258;
   public static final int TIFF_COMPRESSION = 259;
   public static final int TIFF_PHOTOMETRIC_INTERPRETATION = 262;
   public static final int TIFF_FILL_ORDER = 266;
   public static final int TIFF_STRIP_OFFSETS = 273;
   public static final int TIFF_SAMPLES_PER_PIXEL = 277;
   public static final int TIFF_ROWS_PER_STRIP = 278;
   public static final int TIFF_STRIP_BYTE_COUNTS = 279;
   public static final int TIFF_X_RESOLUTION = 282;
   public static final int TIFF_Y_RESOLUTION = 283;
   public static final int TIFF_PLANAR_CONFIGURATION = 284;
   public static final int TIFF_T4_OPTIONS = 292;
   public static final int TIFF_T6_OPTIONS = 293;
   public static final int TIFF_RESOLUTION_UNIT = 296;
   public static final int TIFF_PREDICTOR = 317;
   public static final int TIFF_COLORMAP = 320;
   public static final int TIFF_TILE_WIDTH = 322;
   public static final int TIFF_TILE_LENGTH = 323;
   public static final int TIFF_TILE_OFFSETS = 324;
   public static final int TIFF_TILE_BYTE_COUNTS = 325;
   public static final int TIFF_EXTRA_SAMPLES = 338;
   public static final int TIFF_SAMPLE_FORMAT = 339;
   public static final int TIFF_S_MIN_SAMPLE_VALUE = 340;
   public static final int TIFF_S_MAX_SAMPLE_VALUE = 341;

   public TIFFImageDecoder(SeekableStream var1, ImageDecodeParam var2) {
      super(var1, var2);
   }

   public int getNumPages() throws IOException {
      return TIFFDirectory.getNumDirectories(this.input);
   }

   public RenderedImage decodeAsRenderedImage(int var1) throws IOException {
      if (var1 >= 0 && var1 < this.getNumPages()) {
         return new TIFFImage(this.input, (TIFFDecodeParam)this.param, var1);
      } else {
         throw new IOException("TIFFImageDecoder0");
      }
   }
}
