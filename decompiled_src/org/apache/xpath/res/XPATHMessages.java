package org.apache.xpath.res;

import java.text.MessageFormat;
import java.util.ListResourceBundle;
import org.apache.xml.res.XMLMessages;

public class XPATHMessages extends XMLMessages {
   private static ListResourceBundle XPATHBundle = null;
   private static final String XPATH_ERROR_RESOURCES = "org.apache.xpath.res.XPATHErrorResources";

   public static final String createXPATHMessage(String msgKey, Object[] args) {
      if (XPATHBundle == null) {
         XPATHBundle = XMLMessages.loadResourceBundle("org.apache.xpath.res.XPATHErrorResources");
      }

      return XPATHBundle != null ? createXPATHMsg(XPATHBundle, msgKey, args) : "Could not load any resource bundles.";
   }

   public static final String createXPATHWarning(String msgKey, Object[] args) {
      if (XPATHBundle == null) {
         XPATHBundle = XMLMessages.loadResourceBundle("org.apache.xpath.res.XPATHErrorResources");
      }

      return XPATHBundle != null ? createXPATHMsg(XPATHBundle, msgKey, args) : "Could not load any resource bundles.";
   }

   public static final String createXPATHMsg(ListResourceBundle fResourceBundle, String msgKey, Object[] args) {
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
}
