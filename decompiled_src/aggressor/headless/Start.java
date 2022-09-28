package aggressor.headless;

import aggressor.Aggressor;
import aggressor.MultiFrame;
import common.Authorization;
import common.Callback;
import common.CommonUtils;
import common.License;
import common.MudgeSanity;
import common.TeamQueue;
import common.TeamSocket;
import java.util.Map;
import sleep.parser.ParserConfig;
import ssl.ArmitageTrustListener;
import ssl.SecureSocket;

public class Start implements Callback, ArmitageTrustListener {
   protected MultiFrame window;
   protected TeamQueue tqueue = null;
   protected String desc = "";
   protected String script = "";

   public Start(MultiFrame var1) {
      this.window = var1;
   }

   public static void main(String[] var0) {
      ParserConfig.installEscapeConstant('c', "\u0003");
      ParserConfig.installEscapeConstant('U', "\u001f");
      ParserConfig.installEscapeConstant('o', "\u000f");
      License.checkLicenseConsole(new Authorization());
      String var1;
      int var2;
      String var3;
      String var4;
      if (var0.length == 5) {
         var1 = var0[0];
         var2 = CommonUtils.toNumber(var0[1], 50050);
         var3 = var0[2];
         var4 = var0[3];
         String var5 = var0[4];
         (new Start((MultiFrame)null)).go(var1, var2, var3, var4, var5);
      } else if (var0.length == 4) {
         var1 = var0[0];
         var2 = CommonUtils.toNumber(var0[1], 50050);
         var3 = var0[2];
         var4 = var0[3];
         (new Start((MultiFrame)null)).go(var1, var2, var3, var4, (String)null);
      } else {
         System.out.println("Welcome to the Cobalt Strike (Headless) Client. Version " + Aggressor.VERSION + "\nCopyright 2015, Strategic Cyber LLC\n\nQuick help:\n\n\t./agscript [host] [port] [user] [pass]\n\t\tConnect to a team server and start the Aggressor Script console\n\n\t./agscript [host] [port] [user] [pass] </path/to/file.cna>\n\t\tConnect to a team server and execute the specified script");
         System.exit(0);
      }

   }

   public boolean trust(String var1) {
      return true;
   }

   public void go(String var1, int var2, String var3, String var4, String var5) {
      this.script = var5;

      try {
         SecureSocket var6 = new SecureSocket(var1, var2, this);
         var6.authenticate(var4);
         TeamSocket var7 = new TeamSocket(var6.getSocket());
         this.tqueue = new TeamQueue(var7);
         this.tqueue.call("aggressor.authenticate", CommonUtils.args(var3, var4, Aggressor.VERSION), this);
      } catch (Exception var8) {
         MudgeSanity.logException("client connect", var8, true);
      }

   }

   public void result(String var1, Object var2) {
      if ("aggressor.authenticate".equals(var1)) {
         String var3 = var2 + "";
         if (var3.equals("SUCCESS")) {
            this.tqueue.call("aggressor.metadata", CommonUtils.args(System.currentTimeMillis()), this);
         } else {
            CommonUtils.print_error(var3);
            this.tqueue.close();
            System.exit(0);
         }
      } else if ("aggressor.metadata".equals(var1)) {
         new HeadlessClient(this.window, this.tqueue, (Map)var2, this.script);
      }

   }
}
