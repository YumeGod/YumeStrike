package org.apache.xerces.impl.msg;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.xerces.util.MessageFormatter;

public class XMLMessageFormatter implements MessageFormatter {
   public static final String XML_DOMAIN = "http://www.w3.org/TR/1998/REC-xml-19980210";
   public static final String XMLNS_DOMAIN = "http://www.w3.org/TR/1999/REC-xml-names-19990114";
   private Locale fLocale = null;
   private ResourceBundle fResourceBundle = null;

   public String formatMessage(Locale var1, String var2, Object[] var3) throws MissingResourceException {
      if (this.fResourceBundle == null || var1 != this.fLocale) {
         if (var1 != null) {
            this.fResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XMLMessages", var1);
            this.fLocale = var1;
         }

         if (this.fResourceBundle == null) {
            this.fResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XMLMessages");
         }
      }

      String var4;
      try {
         var4 = this.fResourceBundle.getString(var2);
         if (var3 != null) {
            try {
               var4 = MessageFormat.format(var4, var3);
            } catch (Exception var7) {
               var4 = this.fResourceBundle.getString("FormatFailed");
               var4 = var4 + " " + this.fResourceBundle.getString(var2);
            }
         }
      } catch (MissingResourceException var8) {
         var4 = this.fResourceBundle.getString("BadMessageKey");
         throw new MissingResourceException(var2, var4, var2);
      }

      if (var4 == null) {
         var4 = var2;
         if (var3.length > 0) {
            StringBuffer var5 = new StringBuffer(var2);
            var5.append('?');

            for(int var6 = 0; var6 < var3.length; ++var6) {
               if (var6 > 0) {
                  var5.append('&');
               }

               var5.append(String.valueOf(var3[var6]));
            }
         }
      }

      return var4;
   }
}
