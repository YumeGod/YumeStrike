package org.apache.batik.ext.awt;

import java.awt.RenderingHints;

public final class RenderingHintsKeyExt {
   public static final int KEY_BASE;
   public static final RenderingHints.Key KEY_TRANSCODING;
   public static final String VALUE_TRANSCODING_PRINTING = "Printing";
   public static final String VALUE_TRANSCODING_VECTOR = "Vector";
   public static final RenderingHints.Key KEY_AREA_OF_INTEREST;
   public static final RenderingHints.Key KEY_BUFFERED_IMAGE;
   public static final RenderingHints.Key KEY_COLORSPACE;
   public static final RenderingHints.Key KEY_AVOID_TILE_PAINTING;
   public static final Object VALUE_AVOID_TILE_PAINTING_ON = new Object();
   public static final Object VALUE_AVOID_TILE_PAINTING_OFF = new Object();
   public static final Object VALUE_AVOID_TILE_PAINTING_DEFAULT = new Object();

   private RenderingHintsKeyExt() {
   }

   static {
      int var0 = 10100;
      TranscodingHintKey var1 = null;
      AreaOfInterestHintKey var2 = null;
      BufferedImageHintKey var3 = null;
      ColorSpaceHintKey var4 = null;
      AvoidTilingHintKey var5 = null;

      while(true) {
         int var6 = var0;

         try {
            var1 = new TranscodingHintKey(var6++);
            var2 = new AreaOfInterestHintKey(var6++);
            var3 = new BufferedImageHintKey(var6++);
            var4 = new ColorSpaceHintKey(var6++);
            var5 = new AvoidTilingHintKey(var6++);
            break;
         } catch (Exception var8) {
            System.err.println("You have loaded the Batik jar files more than once\nin the same JVM this is likely a problem with the\nway you are loading the Batik jar files.");
            var0 = (int)(Math.random() * 2000000.0);
         }
      }

      KEY_BASE = var0;
      KEY_TRANSCODING = var1;
      KEY_AREA_OF_INTEREST = var2;
      KEY_BUFFERED_IMAGE = var3;
      KEY_COLORSPACE = var4;
      KEY_AVOID_TILE_PAINTING = var5;
   }
}
