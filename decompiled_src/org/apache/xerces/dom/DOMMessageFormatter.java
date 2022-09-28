package org.apache.xerces.dom;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DOMMessageFormatter {
   public static final String DOM_DOMAIN = "http://www.w3.org/dom/DOMTR";
   public static final String XML_DOMAIN = "http://www.w3.org/TR/1998/REC-xml-19980210";
   public static final String SERIALIZER_DOMAIN = "http://apache.org/xml/serializer";
   private static ResourceBundle domResourceBundle = null;
   private static ResourceBundle xmlResourceBundle = null;
   private static ResourceBundle serResourceBundle = null;
   private static Locale locale = null;

   DOMMessageFormatter() {
      locale = Locale.getDefault();
   }

   public static String formatMessage(String var0, String var1, Object[] var2) throws MissingResourceException {
      ResourceBundle var3 = getResourceBundle(var0);
      if (var3 == null) {
         init();
         var3 = getResourceBundle(var0);
         if (var3 == null) {
            throw new MissingResourceException("Unknown domain" + var0, (String)null, var1);
         }
      }

      String var4;
      try {
         var4 = var1 + ": " + var3.getString(var1);
         if (var2 != null) {
            try {
               var4 = MessageFormat.format(var4, var2);
            } catch (Exception var7) {
               var4 = var3.getString("FormatFailed");
               var4 = var4 + " " + var3.getString(var1);
            }
         }
      } catch (MissingResourceException var8) {
         var4 = var3.getString("BadMessageKey");
         throw new MissingResourceException(var1, var4, var1);
      }

      if (var4 == null) {
         var4 = var1;
         if (var2.length > 0) {
            StringBuffer var5 = new StringBuffer(var1);
            var5.append('?');

            for(int var6 = 0; var6 < var2.length; ++var6) {
               if (var6 > 0) {
                  var5.append('&');
               }

               var5.append(String.valueOf(var2[var6]));
            }
         }
      }

      return var4;
   }

   static ResourceBundle getResourceBundle(String var0) {
      if (var0 != "http://www.w3.org/dom/DOMTR" && !var0.equals("http://www.w3.org/dom/DOMTR")) {
         if (var0 != "http://www.w3.org/TR/1998/REC-xml-19980210" && !var0.equals("http://www.w3.org/TR/1998/REC-xml-19980210")) {
            return var0 != "http://apache.org/xml/serializer" && !var0.equals("http://apache.org/xml/serializer") ? null : serResourceBundle;
         } else {
            return xmlResourceBundle;
         }
      } else {
         return domResourceBundle;
      }
   }

   public static void init() {
      if (locale != null) {
         domResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.DOMMessages", locale);
         serResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XMLSerializerMessages", locale);
         xmlResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XMLMessages", locale);
      } else {
         domResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.DOMMessages");
         serResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XMLSerializerMessages");
         xmlResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XMLMessages");
      }

   }

   public static void setLocale(Locale var0) {
      locale = var0;
   }
}
