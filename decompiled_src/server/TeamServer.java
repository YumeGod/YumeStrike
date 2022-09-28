package server;

import c2profile.Loader;
import c2profile.Profile;
import common.AssertUtils;
import common.Authorization;
import common.CommonUtils;
import common.Keys;
import common.License;
import common.MudgeSanity;
import common.Requirements;
import common.TeamSocket;
import dns.QuickSecurity;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import ssl.PostAuthentication;
import ssl.SecureServerSocket;

public class TeamServer {
   protected int port;
   protected String host;
   protected Resources resources;
   protected Map calls = new HashMap();
   protected Profile c2profile = null;
   protected String pass;
   protected Authorization auth;
   private static String host_help = "It's best if your targets can reach your team server via this IP address. It's OK if this IP address is a redirector.\n\nWhy does this matter?\n\nCobalt Strike uses this IP address as a default throughout its workflows. Cobalt Strike's DNS Beacon also uses this IP address for its HTTP channel. The Covert VPN feature uses this IP too. If your target can't reach your team server via this IP, it's possible some CS features may not work as expected.";

   public TeamServer(String var1, int var2, String var3, Profile var4, Authorization var5) {
      this.host = var1;
      this.port = var2;
      this.pass = var3;
      this.c2profile = var4;
      this.auth = var5;
   }

   public void go() {
      try {
         new ProfileEdits(this.c2profile);
         this.c2profile.addParameter(".watermark", this.auth.getWatermark());
         this.c2profile.addParameter(".self", CommonUtils.readAndSumFi1e(TeamServer.class.getProtectionDomain().getCodeSource().getLocation().getPath()));
         this.resources = new Resources(this.calls);
         this.resources.put("c2profile", this.c2profile);
         this.resources.put("localip", this.host);
         this.resources.put("password", this.pass);
         (new TestCall()).register(this.calls);
         WebCalls var1 = new WebCalls(this.resources);
         var1.register(this.calls);
         this.resources.put("webcalls", var1);
         (new Listeners(this.resources)).register(this.calls);
         (new Beacons(this.resources)).register(this.calls);
         (new Phisher(this.resources)).register(this.calls);
         (new VPN(this.resources)).register(this.calls);
         (new BrowserPivotCalls(this.resources)).register(this.calls);
         (new DownloadCalls(this.resources)).register(this.calls);
         Iterator var2 = Keys.getDataModelIterator();

         while(var2.hasNext()) {
            (new DataCalls(this.resources, (String)var2.next())).register(this.calls);
         }

         if (!ServerUtils.hasPublicStage(this.resources)) {
            CommonUtils.print_warn("Woah! Your profile disables hosted payload stages. Payload staging won't work.");
         }

         SecureServerSocket var3 = new SecureServerSocket(this.port);
         CommonUtils.print_good("Team server is up on " + this.port);
         CommonUtils.print_info("SHA256 hash of SSL cert is: " + var3.fingerprint());
         this.resources.call("listeners.go");

         while(true) {
            var3.acceptAndAuthenticate(this.pass, new PostAuthentication() {
               public void clientAuthenticated(Socket var1) {
                  try {
                     var1.setSoTimeout(0);
                     TeamSocket var2 = new TeamSocket(var1);
                     (new Thread(new ManageUser(var2, TeamServer.this.resources, TeamServer.this.calls), "Manage: unauth'd user")).start();
                  } catch (Exception var3) {
                     MudgeSanity.logException("Start client thread", var3, false);
                  }

               }
            });
         }
      } catch (Exception var4) {
         MudgeSanity.logException("team server startup", var4, false);
      }
   }

   public static void main(String[] var0) {
      int var1 = CommonUtils.toNumber(System.getProperty("cobaltstrike.server_port", "50050"), 50050);
      if (!AssertUtils.TestPort(var1)) {
         System.exit(0);
      }

      Requirements.checkConsole();
      Authorization var2 = new Authorization();
      License.checkLicenseConsole(var2);
      MudgeSanity.systemDetail("scheme", QuickSecurity.getCryptoScheme() + "");
      if (var0.length != 0 && (var0.length != 1 || !"-h".equals(var0[0]) && !"--help".equals(var0[0]))) {
         if (var0.length != 2 && var0.length != 3 && var0.length != 4) {
            CommonUtils.print_error("Missing arguments to start team server\n\t./teamserver <host> <password> [/path/to/c2.profile] [YYYY-MM-DD]");
         } else if (!CommonUtils.isIP(var0[0])) {
            CommonUtils.print_error("The team server <host> must be an IP address. " + host_help);
         } else if ("127.0.0.1".equals(var0[0])) {
            CommonUtils.print_error("Don't use 127.0.0.1 for the team server <host>. " + host_help);
         } else if ("0.0.0.0".equals(var0[0])) {
            CommonUtils.print_error("Don't use 0.0.0.0 for the team server <host>. " + host_help);
         } else if (var0.length == 2) {
            MudgeSanity.systemDetail("c2Profile", "default");
            TeamServer var3 = new TeamServer(var0[0], var1, var0[1], Loader.LoadDefaultProfile(), var2);
            var3.go();
         } else if (var0.length == 3 || var0.length == 4) {
            MudgeSanity.systemDetail("c2Profile", var0[2]);
            Profile var6 = Loader.LoadProfile(var0[2]);
            if (var6 == null) {
               CommonUtils.print_error("exiting because of errors in " + var0[2] + ". Use ./c2lint to check the file");
               System.exit(0);
            }

            CommonUtils.print_good("I see you're into threat replication. " + var0[2] + " loaded.");
            if (var0.length == 4) {
               long var4 = CommonUtils.parseDate(var0[3], "yyyy-MM-dd");
               if (var4 < System.currentTimeMillis()) {
                  CommonUtils.print_error("Beacon kill date " + var0[3] + " is in the past!");
                  System.exit(0);
               } else if (var4 > 0L) {
                  CommonUtils.print_good("Beacon kill date is: " + var0[3] + "!");
                  var6.addParameter(".killdate", var0[3]);
               } else {
                  CommonUtils.print_error("Invalid kill date: '" + var0[3] + "' (format is YYYY-MM-DD)");
                  System.exit(0);
               }

               MudgeSanity.systemDetail("kill date", var0[3]);
            } else {
               MudgeSanity.systemDetail("kill date", "none");
            }

            TeamServer var7 = new TeamServer(var0[0], var1, var0[1], var6, var2);
            var7.go();
         }
      } else {
         CommonUtils.print_info("./teamserver <host> <password> [/path/to/c2.profile] [YYYY-MM-DD]\n\n\t<host> is the (default) IP address of this Cobalt Strike team server\n\t<password> is the shared password to connect to this server\n\t[/path/to/c2.profile] is your Malleable C2 profile\n\t[YYYY-MM-DD] is a kill date for Beacon payloads run from this server\n");
      }

   }
}
