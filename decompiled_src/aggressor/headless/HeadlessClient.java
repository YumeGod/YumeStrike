package aggressor.headless;

import aggressor.AggressorClient;
import aggressor.MultiFrame;
import common.CommonUtils;
import common.MudgeSanity;
import common.TeamQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HeadlessClient extends AggressorClient implements Runnable {
   protected String scriptf;

   public void disconnected() {
      CommonUtils.print_error("Disconnected from team server.");
      System.exit(0);
   }

   public void result(String var1, Object var2) {
      if ("server_error".equals(var1)) {
         CommonUtils.print_error("Server error: " + var2);
      }

   }

   public void loadScripts() {
      if (this.scriptf == null) {
         try {
            this.engine.loadScript("scripts/console.cna", CommonUtils.resource("scripts/console.cna"));
         } catch (Exception var3) {
            MudgeSanity.logException("Loading scripts/console.cna", var3, false);
         }

         (new Thread(this, "Aggressor Script Console")).start();
      } else {
         try {
            this.engine.loadScript(this.scriptf);
         } catch (Exception var2) {
            MudgeSanity.logException("Loading " + this.scriptf, var2, true);
            System.exit(0);
         }
      }

   }

   public void run() {
      BufferedReader var1 = new BufferedReader(new InputStreamReader(System.in));

      while(true) {
         while(true) {
            try {
               System.out.print("\u001b[4maggressor\u001b[0m> ");
               String var2 = var1.readLine();
               if (var2 != null && !"".equals(var2)) {
                  this.engine.getConsoleInterface().processCommand(var2);
               }
            } catch (IOException var3) {
            }
         }
      }
   }

   public HeadlessClient(MultiFrame var1, TeamQueue var2, Map var3, String var4) {
      this.scriptf = var4;
      this.setup(var1, var2, var3, new HashMap());
   }

   public boolean isHeadless() {
      return true;
   }
}
