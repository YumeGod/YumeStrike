package org.apache.batik.ext.awt.image.spi;

import java.awt.Image;
import org.apache.batik.ext.awt.image.renderable.Filter;

public abstract class BrokenLinkProvider {
   public static final String BROKEN_LINK_PROPERTY = "org.apache.batik.BrokenLinkImage";

   public abstract Filter getBrokenLinkImage(Object var1, String var2, Object[] var3);

   public static boolean hasBrokenLinkProperty(Filter var0) {
      Object var1 = var0.getProperty("org.apache.batik.BrokenLinkImage");
      if (var1 == null) {
         return false;
      } else {
         return var1 != Image.UndefinedProperty;
      }
   }
}
