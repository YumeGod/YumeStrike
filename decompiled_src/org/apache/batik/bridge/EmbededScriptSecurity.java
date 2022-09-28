package org.apache.batik.bridge;

import org.apache.batik.util.ParsedURL;

public class EmbededScriptSecurity implements ScriptSecurity {
   public static final String DATA_PROTOCOL = "data";
   public static final String ERROR_CANNOT_ACCESS_DOCUMENT_URL = "DefaultScriptSecurity.error.cannot.access.document.url";
   public static final String ERROR_SCRIPT_NOT_EMBEDED = "EmbededScriptSecurity.error.script.not.embeded";
   protected SecurityException se;

   public void checkLoadScript() {
      if (this.se != null) {
         throw this.se;
      }
   }

   public EmbededScriptSecurity(String var1, ParsedURL var2, ParsedURL var3) {
      if (var3 == null) {
         this.se = new SecurityException(Messages.formatMessage("DefaultScriptSecurity.error.cannot.access.document.url", new Object[]{var2}));
      } else if (!var3.equals(var2) && (var2 == null || !"data".equals(var2.getProtocol()))) {
         this.se = new SecurityException(Messages.formatMessage("EmbededScriptSecurity.error.script.not.embeded", new Object[]{var2}));
      }

   }
}
