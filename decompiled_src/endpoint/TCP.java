package endpoint;

import common.MudgeSanity;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import tap.EncryptedTap;
import tap.TapProtocol;

public class TCP extends Base implements Runnable {
   protected ServerSocket server = null;
   protected Socket client = null;
   protected DataOutputStream out;
   protected DataInputStream in;
   protected int port;
   protected boolean listen;
   protected byte[] buffer = new byte[65536];

   public TCP(TapProtocol var1, int var2, boolean var3) throws IOException {
      super(var1);
      this.port = var2;
      this.listen = var3;
      (new Thread(this)).start();
   }

   public static void main(String[] var0) {
      System.loadLibrary("tapmanager");
      EncryptedTap var1 = new EncryptedTap("phear0", "foobar".getBytes());

      try {
         new TCP(var1, 31337, true);

         while(true) {
            Thread.sleep(1000L);
         }
      } catch (InterruptedException var3) {
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   public void process() throws IOException {
      if (this.listen) {
         this.server = new ServerSocket(this.port);
         this.server.setSoTimeout(0);
         this.server.setReuseAddress(true);
         this.client = this.server.accept();
         this.processData();
      } else {
         while(!this.tap.isStopped()) {
            try {
               Thread.sleep(10000L);
               this.client = new Socket("127.0.0.1", this.port);
            } catch (Exception var2) {
               this.client = null;
               MudgeSanity.logException(this.tap.getInterface() + " VPN connect (local) failed", var2, true);
            }

            if (this.client != null) {
               this.processData();
            }
         }
      }

   }

   public void processData() throws IOException {
      try {
         this.in = new DataInputStream(this.client.getInputStream());
         this.out = new DataOutputStream(new BufferedOutputStream(this.client.getOutputStream(), 65536));
         this.tap.setRemoteHost(this.client.getInetAddress().getHostAddress());
         this.start();

         while(!this.tap.isStopped()) {
            int var1 = this.in.readUnsignedShort();
            this.in.readFully(this.buffer, 0, var1);
            this.rx += (long)var1;
            this.tap.writeFrame(this.buffer, var1);
         }
      } catch (EOFException var2) {
         MudgeSanity.logException(this.tap.getInterface() + " VPN connect (local) not ready", var2, true);
         this.tap.setRemoteHost("not connected");
         this.in.close();
         this.out.close();
      }

   }

   public synchronized void processFrame(byte[] var1) {
      if (this.out != null) {
         try {
            this.out.writeShort(var1.length);
            this.out.write(var1, 0, var1.length);
            this.out.flush();
         } catch (IOException var3) {
            this.stop();
            var3.printStackTrace();
         }

      }
   }

   public void shutdown() {
      try {
         this.client = null;
         if (this.out != null) {
            this.out.close();
         }

         if (this.in != null) {
            this.in.close();
         }

         if (this.server != null) {
            this.server.close();
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void run() {
      try {
         this.process();
      } catch (IOException var2) {
         this.stop();
         var2.printStackTrace();
      }

   }
}
