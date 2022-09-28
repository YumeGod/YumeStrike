package icmp;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable {
   protected Map listeners = new HashMap();

   public void addIcmpListener(String var1, IcmpListener var2) {
      synchronized(this.listeners) {
         this.listeners.put(var1, var2);
      }
   }

   public Server() {
      System.err.println("\u001b[01;33m[!]\u001b[0m Disabled ICMP replies for this host.");
      System.err.println("\tTo undo this: sysctl -w net.ipv4.icmp_echo_ignore_all=0");

      try {
         Runtime.getRuntime().exec("sysctl -w net.ipv4.icmp_echo_ignore_all=1");
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      (new Thread(this)).start();
   }

   public String toHost(byte[] var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.length && var1[var3] != 0; ++var3) {
         var2.append((char)var1[var3]);
      }

      return var2.toString();
   }

   public void run() {
      byte[] var1 = new byte[128];
      byte[] var2 = new byte[65536];

      while(true) {
         int var3;
         do {
            var3 = this.recv_icmp(var1, var2);
         } while(var3 <= 4);

         byte[] var4 = Arrays.copyOfRange(var2, 4, var3);
         String var5 = this.toHost(var1);
         String var6 = new String(var2, 0, 4);
         synchronized(this.listeners) {
            IcmpListener var8 = (IcmpListener)this.listeners.get(var6);
            if (var8 == null) {
               var8 = (IcmpListener)this.listeners.get("foob");
            }

            if (var8 != null) {
               byte[] var9 = var8.icmp_ping(var5, var4);
               if (var9 != null) {
                  this.reply_icmp(var9);
               }
            }
         }
      }
   }

   protected native int recv_icmp(byte[] var1, byte[] var2);

   protected native void reply_icmp(byte[] var1);

   public static void main(String[] var0) throws Exception {
      System.loadLibrary("icmp");
      Server var1 = new Server();
      var1.addIcmpListener("foob", new IcmpListener() {
         public byte[] icmp_ping(String var1, byte[] var2) {
            System.err.println("Received: " + new String(var2));
            System.err.println("From:     " + var1);
            return "hey, this is a reply".getBytes();
         }
      });

      while(true) {
         Thread.sleep(1000L);
      }
   }

   public interface IcmpListener {
      byte[] icmp_ping(String var1, byte[] var2);
   }
}
