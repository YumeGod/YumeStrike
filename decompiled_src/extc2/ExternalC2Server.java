package extc2;

import beacon.BeaconSetup;
import common.CommonUtils;
import common.MudgeSanity;
import common.ScListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ExternalC2Server implements Runnable {
   protected int bindport;
   protected String bindaddr;
   protected ServerSocket server;
   protected boolean running;
   protected BeaconSetup setup;
   protected ScListener listener;

   public void die() {
      try {
         if (this.server != null) {
            this.server.close();
         }
      } catch (IOException var2) {
         MudgeSanity.logException("stop server", var2, false);
      }

   }

   public boolean isRunning() {
      return this.running;
   }

   public ExternalC2Server(BeaconSetup var1, int var2) {
      this(var1, (ScListener)null, "0.0.0.0", var2);
   }

   public ExternalC2Server(BeaconSetup var1, ScListener var2, String var3, int var4) {
      this.server = null;
      this.running = true;
      this.setup = null;
      this.listener = null;
      this.bindaddr = var3;
      this.bindport = var4;
      this.setup = var1;
      this.listener = var2;
   }

   public void start() throws IOException {
      this.server = new ServerSocket(this.bindport, 128, InetAddress.getByName(this.bindaddr));
      this.server.setSoTimeout(0);
      (new Thread(this, "External C2 Server " + this.bindaddr + ":" + this.bindport)).start();
   }

   private void waitForClient(ServerSocket var1) throws IOException {
      Socket var2 = var1.accept();
      var2.setKeepAlive(true);
      var2.setSoTimeout(0);
      new ExternalC2Session(this.setup, this.listener, var2);
   }

   public void run() {
      try {
         CommonUtils.print_good("External C2 Server up on " + this.bindaddr + ":" + this.bindport);

         while(true) {
            this.waitForClient(this.server);
         }
      } catch (Exception var2) {
         MudgeSanity.logException("External C2 Server Accept Loop", var2, false);
         this.running = false;
      }
   }
}
