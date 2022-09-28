package org.apache.batik.bridge;

public class NoLoadScriptSecurity implements ScriptSecurity {
   public static final String ERROR_NO_SCRIPT_OF_TYPE_ALLOWED = "NoLoadScriptSecurity.error.no.script.of.type.allowed";
   protected SecurityException se;

   public void checkLoadScript() {
      throw this.se;
   }

   public NoLoadScriptSecurity(String var1) {
      this.se = new SecurityException(Messages.formatMessage("NoLoadScriptSecurity.error.no.script.of.type.allowed", new Object[]{var1}));
   }
}
