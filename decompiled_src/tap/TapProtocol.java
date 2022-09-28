package tap;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TapProtocol extends TapManager {
   protected String host = null;

   public boolean isActive() {
      return this.host != null;
   }

   public void setRemoteHost(String var1) {
      this.host = var1;
   }

   public String getRemoteHost() {
      return this.host;
   }

   public TapProtocol(String var1) {
      super(var1);
   }

   public int readFrame(byte[] var1) {
      var1[0] = 0;
      var1[1] = 0;
      return this.readFrame(this.fd, 2, var1) + 2;
   }

   public byte[] readKillFrame() {
      byte[] var1 = new byte[]{0, 2, 0, 0};
      return var1;
   }

   public byte[] protocol(int var1, byte[] var2) {
      try {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream(32);
         DataOutputStream var4 = new DataOutputStream(var3);
         var4.writeShort(var1);
         if (var2.length > 0) {
            var4.write(var2, 0, var2.length);
         }

         return var3.toByteArray();
      } catch (IOException var5) {
         var5.printStackTrace();
         return new byte[0];
      }
   }
}
