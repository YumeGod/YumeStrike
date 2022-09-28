package socks;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BeaconProxyListener implements ProxyListener {
   public byte[] writeMessage(int var1, byte[] var2, int var3) throws IOException {
      ByteArrayOutputStream var4 = new ByteArrayOutputStream(256 + var3);
      DataOutputStream var5 = new DataOutputStream(var4);
      var5.writeInt(15);
      var5.writeInt(var3 + 4);
      var5.writeInt(var1);
      var5.write(var2, 0, var3);
      return var4.toByteArray();
   }

   public byte[] connectMessage(int var1, String var2, int var3) throws IOException {
      ByteArrayOutputStream var4 = new ByteArrayOutputStream(256);
      DataOutputStream var5 = new DataOutputStream(var4);
      var5.writeInt(14);
      var5.writeInt(var2.length() + 6);
      var5.writeInt(var1);
      var5.writeShort(var3);
      var5.writeBytes(var2);
      return var4.toByteArray();
   }

   public byte[] closeMessage(int var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(256);
      DataOutputStream var3 = new DataOutputStream(var2);
      var3.writeInt(16);
      var3.writeInt(4);
      var3.writeInt(var1);
      return var2.toByteArray();
   }

   public byte[] listenMessage(int var1, int var2) throws IOException {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream(256);
      DataOutputStream var4 = new DataOutputStream(var3);
      var4.writeInt(17);
      var4.writeInt(6);
      var4.writeInt(var1);
      var4.writeShort(var2);
      return var3.toByteArray();
   }

   public void proxyEvent(SocksProxy var1, ProxyEvent var2) {
      try {
         byte[] var3;
         switch (var2.getType()) {
            case 0:
               var3 = this.closeMessage(var2.getChannelId());
               var1.read(var3);
               break;
            case 1:
               var3 = this.connectMessage(var2.getChannelId(), var2.getHost(), var2.getPort());
               var1.read(var3);
               break;
            case 2:
               var3 = this.listenMessage(var2.getChannelId(), var2.getPort());
               var1.read(var3);
               break;
            case 3:
               var3 = this.writeMessage(var2.getChannelId(), var2.getData(), var2.getDataLength());
               var1.read(var3);
         }
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }
}
