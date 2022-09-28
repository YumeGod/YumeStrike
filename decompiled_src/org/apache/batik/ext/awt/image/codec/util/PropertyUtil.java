package org.apache.batik.ext.awt.image.codec.util;

import java.util.MissingResourceException;
import org.apache.batik.i18n.LocalizableSupport;

public class PropertyUtil {
   protected static final String RESOURCES = "org.apache.batik.bridge.resources.properties";
   protected static LocalizableSupport localizableSupport;
   // $FF: synthetic field
   static Class class$org$apache$batik$ext$awt$image$codec$util$PropertyUtil;

   public static String getString(String var0) {
      try {
         return localizableSupport.formatMessage(var0, (Object[])null);
      } catch (MissingResourceException var2) {
         return var0;
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      localizableSupport = new LocalizableSupport("org.apache.batik.bridge.resources.properties", (class$org$apache$batik$ext$awt$image$codec$util$PropertyUtil == null ? (class$org$apache$batik$ext$awt$image$codec$util$PropertyUtil = class$("org.apache.batik.ext.awt.image.codec.util.PropertyUtil")) : class$org$apache$batik$ext$awt$image$codec$util$PropertyUtil).getClassLoader());
   }
}
