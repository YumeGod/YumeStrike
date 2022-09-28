package org.apache.xml.res;

import java.text.MessageFormat;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XMLMessages {
   protected Locale fLocale = Locale.getDefault();
   private static ListResourceBundle XMLBundle = null;
   private static final String XML_ERROR_RESOURCES = "org.apache.xml.res.XMLErrorResources";
   protected static final String BAD_CODE = "BAD_CODE";
   protected static final String FORMAT_FAILED = "FORMAT_FAILED";

   public void setLocale(Locale locale) {
      this.fLocale = locale;
   }

   public Locale getLocale() {
      return this.fLocale;
   }

   public static final String createXMLMessage(String msgKey, Object[] args) {
      if (XMLBundle == null) {
         XMLBundle = loadResourceBundle("org.apache.xml.res.XMLErrorResources");
      }

      return XMLBundle != null ? createMsg(XMLBundle, msgKey, args) : "Could not load any resource bundles.";
   }

   public static final String createMsg(ListResourceBundle fResourceBundle, String msgKey, Object[] args) {
      String fmsg = null;
      boolean throwex = false;
      String msg = null;
      if (msgKey != null) {
         msg = fResourceBundle.getString(msgKey);
      }

      if (msg == null) {
         msg = fResourceBundle.getString("BAD_CODE");
         throwex = true;
      }

      if (args != null) {
         try {
            int n = args.length;

            for(int i = 0; i < n; ++i) {
               if (null == args[i]) {
                  args[i] = "";
               }
            }

            fmsg = MessageFormat.format(msg, args);
         } catch (Exception var8) {
            fmsg = fResourceBundle.getString("FORMAT_FAILED");
            fmsg = fmsg + " " + msg;
         }
      } else {
         fmsg = msg;
      }

      if (throwex) {
         throw new RuntimeException(fmsg);
      } else {
         return fmsg;
      }
   }

   public static ListResourceBundle loadResourceBundle(String className) throws MissingResourceException {
      Locale locale = Locale.getDefault();

      try {
         return (ListResourceBundle)ResourceBundle.getBundle(className, locale);
      } catch (MissingResourceException var5) {
         try {
            return (ListResourceBundle)ResourceBundle.getBundle(className, new Locale("en", "US"));
         } catch (MissingResourceException var4) {
            throw new MissingResourceException("Could not load any resource bundles." + className, className, "");
         }
      }
   }

   protected static String getResourceSuffix(Locale locale) {
      String suffix = "_" + locale.getLanguage();
      String country = locale.getCountry();
      if (country.equals("TW")) {
         suffix = suffix + "_" + country;
      }

      return suffix;
   }
}
