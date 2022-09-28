package endpoint;

import icmp.Server;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import tap.TapProtocol;

public class ICMP extends Base implements Server.IcmpListener {
   public static final short COMMAND_READ = 204;
   public static final short COMMAND_WRITE = 221;
   protected byte[] buffer = new byte[1048576];
   protected LinkedList outframes = new LinkedList();
   protected int outsize = 0;

   public ICMP(TapProtocol var1) throws IOException {
      super(var1);
      this.start();
   }

   public void processFrame(byte[] var1) {
      synchronized(this) {
         this.outframes.add(new Snapshot(var1));

         Snapshot var3;
         for(this.outsize += var1.length + 2; this.outsize > 8192; this.outsize -= var3.data.length + 2) {
            var3 = (Snapshot)this.outframes.removeFirst();
         }

      }
   }

   public void processWrite(byte[] var1, int var2, DataInputStream var3) throws IOException {
      while(var2 < var1.length && var1.length > 1) {
         int var4 = var3.readUnsignedShort();
         if (var2 + var4 > var1.length) {
            System.err.println("#########Next read " + var2 + ": " + var4 + " exceeds " + var1.length);
            return;
         }

         var2 += var4 + 2;
         var3.readFully(this.buffer, 0, var4);
         this.tap.writeFrame(this.buffer, var4);
         this.rx += (long)var4;
      }

   }

   public byte[] processRead(byte[] var1, int var2, DataInputStream var3) throws IOException {
      int var4 = 0;
      ByteArrayOutputStream var5 = new ByteArrayOutputStream(this.outsize);
      synchronized(this) {
         DataOutputStream var7 = new DataOutputStream(var5);
         var7.writeInt(0);

         while(this.outframes.size() > 0) {
            Snapshot var8 = (Snapshot)this.outframes.removeFirst();
            var7.writeShort(var8.data.length);
            var7.write(var8.data, 0, var8.data.length);
            var4 += var8.data.length + 2;
         }

         this.outsize -= var4;
         return var5.toByteArray();
      }
   }

   public byte[] icmp_ping(String var1, byte[] var2) {
      if (this.tap.isStopped()) {
         return null;
      } else {
         this.tap.setRemoteHost(var1);
         DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var2, 0, var2.length));
         int var4 = 1;

         try {
            short var5 = var3.readShort();
            var4 += 2;
            if (var5 == 221) {
               this.processWrite(var2, var4, var3);
               return this.processRead(var2, var4, var3);
            } else if (var5 == 204) {
               return this.processRead(var2, var4, var3);
            } else {
               System.err.println("INVALID ICMP COMMAND: " + var5 + " len: " + var2.length);
               return new byte[0];
            }
         } catch (IOException var6) {
            var6.printStackTrace();
            return null;
         }
      }
   }

   public void shutdown() {
   }

   private static final class Snapshot {
      byte[] data;

      public Snapshot(byte[] var1) {
         this.data = var1;
      }
   }
}
