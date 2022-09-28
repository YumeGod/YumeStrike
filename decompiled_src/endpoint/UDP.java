package endpoint;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import tap.EncryptedTap;
import tap.TapProtocol;

public class UDP extends Base implements Runnable {
   protected DatagramSocket server;
   protected DatagramPacket in_packet = new DatagramPacket(new byte[65536], 65536);
   protected DatagramPacket out_packet = new DatagramPacket(new byte[65536], 65536);
   protected byte[] buffer = new byte[65536];

   public UDP(TapProtocol var1, int var2) throws IOException {
      super(var1);
      this.server = new DatagramSocket(var2);
      (new Thread(this)).start();
   }

   public static void main(String[] var0) {
      System.loadLibrary("tapmanager");
      EncryptedTap var1 = new EncryptedTap("phear0", "foobar".getBytes());

      try {
         new UDP(var1, 31337);

         while(true) {
            Thread.sleep(1000L);
         }
      } catch (InterruptedException var3) {
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   public synchronized void processFrame(byte[] var1) {
      try {
         this.out_packet.setData(var1);
         this.server.send(this.out_packet);
      } catch (IOException var3) {
         this.stop();
         var3.printStackTrace();
      }

   }

   public void shutdown() {
      try {
         this.server.close();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void run() {
      try {
         this.server.receive(this.out_packet);
         this.tap.setRemoteHost(this.out_packet.getAddress().getHostAddress());
         this.start();

         while(!this.tap.isStopped()) {
            this.server.receive(this.in_packet);
            this.rx += (long)this.in_packet.getLength();
            this.tap.writeFrame(this.in_packet.getData(), this.in_packet.getLength());
         }
      } catch (IOException var2) {
         this.stop();
         var2.printStackTrace();
      }

   }
}
