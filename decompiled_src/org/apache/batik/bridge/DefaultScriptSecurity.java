package org.apache.batik.bridge;

import org.apache.batik.util.ParsedURL;

public class DefaultScriptSecurity implements ScriptSecurity {
   public static final String DATA_PROTOCOL = "data";
   public static final String ERROR_CANNOT_ACCESS_DOCUMENT_URL = "DefaultScriptSecurity.error.cannot.access.document.url";
   public static final String ERROR_SCRIPT_FROM_DIFFERENT_URL = "DefaultScriptSecurity.error.script.from.different.url";
   protected SecurityException se;

   public void checkLoadScript() {
      if (this.se != null) {
         throw this.se;
      }
   }

   public DefaultScriptSecurity(String var1, ParsedURL var2, ParsedURL var3) {
      if (var3 == null) {
         this.se = new SecurityException(Messages.formatMessage("DefaultScriptSecurity.error.cannot.access.document.url", new Object[]{var2}));
      } else {
         String var4 = var3.getHost();
         String var5 = var2.getHost();
         if (var4 != var5 && (var4 == null || !var4.equals(var5)) && !var3.equals(var2) && (var2 == null || !"data".equals(var2.getProtocol()))) {
            this.se = new SecurityException(Messages.formatMessage("DefaultScriptSecurity.error.script.from.different.url", new Object[]{var2}));
         }
      }

   }
}
