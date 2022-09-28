package org.apache.xerces.xpointer;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.xerces.util.MessageFormatter;

class XPointerMessageFormatter implements MessageFormatter {
   public static final String XPOINTER_DOMAIN = "http://www.w3.org/TR/XPTR";
   private Locale fLocale = null;
   private ResourceBundle fResourceBundle = null;

   public String formatMessage(Locale var1, String var2, Object[] var3) throws MissingResourceException {
      if (this.fResourceBundle == null || var1 != this.fLocale) {
         if (var1 != null) {
            this.fResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XPointerMessages", var1);
            this.fLocale = var1;
         }

         if (this.fResourceBundle == null) {
            this.fResourceBundle = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XPointerMessages");
         }
      }

      String var4 = this.fResourceBundle.getString(var2);
      if (var3 != null) {
         try {
            var4 = MessageFormat.format(var4, var3);
         } catch (Exception var6) {
            var4 = this.fResourceBundle.getString("FormatFailed");
            var4 = var4 + " " + this.fResourceBundle.getString(var2);
         }
      }

      if (var4 == null) {
         var4 = this.fResourceBundle.getString("BadMessageKey");
         throw new MissingResourceException(var4, "org.apache.xerces.impl.msg.XPointerMessages", var2);
      } else {
         return var4;
      }
   }
}
