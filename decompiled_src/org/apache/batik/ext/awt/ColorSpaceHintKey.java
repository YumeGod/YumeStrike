package org.apache.batik.ext.awt;

import java.awt.RenderingHints;

public final class ColorSpaceHintKey extends RenderingHints.Key {
   public static Object VALUE_COLORSPACE_ARGB = new Object();
   public static Object VALUE_COLORSPACE_RGB = new Object();
   public static Object VALUE_COLORSPACE_GREY = new Object();
   public static Object VALUE_COLORSPACE_AGREY = new Object();
   public static Object VALUE_COLORSPACE_ALPHA = new Object();
   public static Object VALUE_COLORSPACE_ALPHA_CONVERT = new Object();
   public static final String PROPERTY_COLORSPACE = "org.apache.batik.gvt.filter.Colorspace";

   ColorSpaceHintKey(int var1) {
      super(var1);
   }

   public boolean isCompatibleValue(Object var1) {
      if (var1 == VALUE_COLORSPACE_ARGB) {
         return true;
      } else if (var1 == VALUE_COLORSPACE_RGB) {
         return true;
      } else if (var1 == VALUE_COLORSPACE_GREY) {
         return true;
      } else if (var1 == VALUE_COLORSPACE_AGREY) {
         return true;
      } else if (var1 == VALUE_COLORSPACE_ALPHA) {
         return true;
      } else {
         return var1 == VALUE_COLORSPACE_ALPHA_CONVERT;
      }
   }
}
