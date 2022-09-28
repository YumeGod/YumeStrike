package socks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class BasicClient implements Runnable {
   protected Socket client = null;
   protected boolean alive = true;
   protected SocksProxy parent = null;
   protected int chid = 0;
   protected boolean started = false;
   protected InputStream in = null;
   protected OutputStream out = null;

   public void die() {
      synchronized(this) {
         this.alive = false;
      }

      this.parent.fireEvent(ProxyEvent.EVENT_CLOSE(this.chid));
      if (!this.started) {
         this.deny();
      } else {
         try {
            if (this.client != null) {
               this.client.close();
            }
         } catch (IOException var3) {
         }

      }
   }

   public boolean isAlive() {
      synchronized(this) {
         return this.alive;
      }
   }

   public BasicClient() {
   }

   public BasicClient(SocksProxy var1, Socket var2, int var3) {
      this.client = var2;
      this.parent = var1;
      this.chid = var3;
      (new Thread(this, "SOCKS4a Proxy INIT")).start();
   }

   protected void startReading(Socket var1) {
      try {
         byte[] var2 = new byte[65536];
         boolean var3 = false;

         while(this.isAlive()) {
            if (this.parent.hasSpace()) {
               int var6 = this.in.read(var2);
               if (var6 == -1) {
                  break;
               }

               this.parent.fireEvent(ProxyEvent.EVENT_READ(this.chid, var2, var6));
            } else {
               Thread.sleep(250L);
            }
         }

         this.die();
      } catch (InterruptedException var4) {
         this.die();
      } catch (IOException var5) {
         this.die();
      }

   }

   public void setup() throws IOException {
      this.in = this.client.getInputStream();
      this.out = this.client.getOutputStream();
   }

   public void start() {
      (new Thread(new Runnable() {
         public void run() {
            BasicClient.this.startReading(BasicClient.this.client);
         }
      }, "SOCKS4a Client Reader")).start();
   }

   public void write(byte[] var1, int var2, int var3) {
      try {
         this.out.write(var1, var2, var3);
         this.out.flush();
      } catch (IOException var5) {
         this.die();
      }

   }

   protected void deny() {
      try {
         if (this.client != null) {
            this.client.close();
         }
      } catch (IOException var2) {
      }

   }

   public abstract void run();
}
