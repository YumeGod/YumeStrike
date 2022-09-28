package org.apache.xmlgraphics.image.codec.util;

import java.util.MissingResourceException;
import org.apache.xmlgraphics.util.i18n.LocalizableSupport;

public class PropertyUtil {
   protected static final String RESOURCES = "org.apache.xmlgraphics.image.codec.Messages";
   protected static LocalizableSupport localizableSupport;

   public static String getString(String key) {
      try {
         return localizableSupport.formatMessage(key, (Object[])null);
      } catch (MissingResourceException var2) {
         return key;
      }
   }

   static {
      localizableSupport = new LocalizableSupport("org.apache.xmlgraphics.image.codec.Messages", PropertyUtil.class.getClassLoader());
   }
}
