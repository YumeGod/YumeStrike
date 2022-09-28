package socks;

import java.io.IOException;
import java.net.Socket;

public class ProxyClient extends BasicClient {
   protected SocksCommand command = null;

   public ProxyClient(SocksProxy var1, Socket var2, int var3) {
      super(var1, var2, var3);
   }

   public void start() {
      try {
         this.command.reply(this.out, 90);
         this.started = true;
      } catch (IOException var2) {
         this.die();
         return;
      }

      super.start();
   }

   protected void deny() {
      try {
         this.command.reply(this.out, 91);
         super.deny();
      } catch (IOException var2) {
      }

   }

   public void run() {
      try {
         this.setup();
         this.command = new SocksCommand(this.in);
         if (this.command.getCommand() == 1) {
            this.parent.fireEvent(ProxyEvent.EVENT_CONNECT(this.chid, this.command.getHost(), this.command.getPort()));
         } else {
            this.parent.fireEvent(ProxyEvent.EVENT_LISTEN(this.chid, this.command.getHost(), this.command.getPort()));
         }

      } catch (IOException var4) {
         try {
            this.client.close();
         } catch (IOException var3) {
         }

      }
   }
}
