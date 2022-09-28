package endpoint;

import cloudstrike.Response;
import cloudstrike.WebServer;
import cloudstrike.WebService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import tap.EncryptedTap;
import tap.TapProtocol;

public class HTTP extends Base implements WebService {
   protected byte[] buffer = new byte[1048576];
   protected ByteArrayOutputStream outframes = new ByteArrayOutputStream(1048576);
   protected DataOutputStream outhandle;
   protected WebServer server;
   protected String hook;

   public HTTP(TapProtocol var1) throws IOException {
      super(var1);
      this.outhandle = new DataOutputStream(this.outframes);
      this.server = null;
      this.hook = "";
      this.start();
   }

   public static void main(String[] var0) {
      System.loadLibrary("tapmanager");
      EncryptedTap var1 = new EncryptedTap("phear0", "foobar".getBytes());

      try {
         WebServer var2 = new WebServer(80);
         HTTP var3 = new HTTP(var1);
         var3.setup(var2, ".json");

         while(true) {
            Thread.sleep(1000L);
         }
      } catch (InterruptedException var4) {
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public synchronized void processFrame(byte[] var1) {
      try {
         this.outhandle.writeShort(var1.length);
         this.outhandle.write(var1, 0, var1.length);
      } catch (IOException var3) {
         System.err.println("Size: " + var1.length);
         var3.printStackTrace();
         this.stop();
      }

   }

   public void setup(WebServer var1, String var2) {
      var1.register("/send" + var2, this);
      var1.register("/receive" + var2, this);
      this.server = var1;
      this.hook = var2;
   }

   public void shutdown() {
      this.server.deregister("/send" + this.hook);
      this.server.deregister("/receive" + this.hook);
   }

   public Response serve(String var1, String var2, Properties var3, Properties var4) {
      if (var1.startsWith("/send") && var4.containsKey("input") && var4.get("input") instanceof InputStream && var4.containsKey("length") && var4.get("length") instanceof Long) {
         try {
            Long var14 = (Long)var4.get("length");
            long var6 = var14;
            DataInputStream var8 = new DataInputStream((InputStream)var4.get("input"));
            int var9 = 0;

            while((long)var9 < var6) {
               int var10 = var8.readUnsignedShort();
               if ((long)(var9 + var10) > var6) {
                  break;
               }

               var9 += var10 + 2;
               var8.readFully(this.buffer, 0, var10);
               this.rx += (long)var10;
               this.tap.writeFrame(this.buffer, var10);
            }
         } catch (IOException var13) {
            var13.printStackTrace();
         }

         return new Response("200 OK", "application/json", "{ \"status\": \"OK\" }");
      } else if (var1.startsWith("/receive")) {
         this.tap.setRemoteHost((var3.get("REMOTE_ADDRESS") + "").substring(1));
         ByteArrayInputStream var5 = null;
         synchronized(this) {
            var5 = new ByteArrayInputStream(this.outframes.toByteArray());
            this.outframes.reset();
         }

         return new Response("200 OK", "application/octet-stream", var5);
      } else {
         return new Response("200 OK", "text/plain", "file not found");
      }
   }

   public String toString() {
      return "tunnels " + this.tap.getInterface();
   }

   public String getType() {
      return "tunnel";
   }

   public List cleanupJobs() {
      return new LinkedList();
   }

   public boolean suppressEvent(String var1) {
      return true;
   }

   public boolean isFuzzy() {
      return false;
   }
}
