package org.apache.batik.util.resources;

import java.util.Locale;
import java.util.MissingResourceException;
import org.apache.batik.i18n.LocalizableSupport;

public class Messages {
   protected static final String RESOURCES = "org.apache.batik.util.resources.Messages";
   protected static LocalizableSupport localizableSupport;
   // $FF: synthetic field
   static Class class$org$apache$batik$util$resources$Messages;

   protected Messages() {
   }

   public static void setLocale(Locale var0) {
      localizableSupport.setLocale(var0);
   }

   public static Locale getLocale() {
      return localizableSupport.getLocale();
   }

   public static String formatMessage(String var0, Object[] var1) throws MissingResourceException {
      return localizableSupport.formatMessage(var0, var1);
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
      localizableSupport = new LocalizableSupport("org.apache.batik.util.resources.Messages", (class$org$apache$batik$util$resources$Messages == null ? (class$org$apache$batik$util$resources$Messages = class$("org.apache.batik.util.resources.Messages")) : class$org$apache$batik$util$resources$Messages).getClassLoader());
   }
}
