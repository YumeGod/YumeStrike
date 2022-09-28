package socks;

public class ProxyEvent {
   public static final int PROXY_CLOSE = 0;
   public static final int PROXY_CONNECT = 1;
   public static final int PROXY_LISTEN = 2;
   public static final int PROXY_READ = 3;
   public int chid;
   public int type;
   public byte[] data;
   public int length;
   public String host;
   public int port;

   public static ProxyEvent EVENT_CLOSE(int var0) {
      return new ProxyEvent(0, var0);
   }

   public static ProxyEvent EVENT_CONNECT(int var0, String var1, int var2) {
      return new ProxyEvent(1, var0, var1, var2);
   }

   public static ProxyEvent EVENT_LISTEN(int var0, String var1, int var2) {
      return new ProxyEvent(2, var0, var1, var2);
   }

   public static ProxyEvent EVENT_READ(int var0, byte[] var1, int var2) {
      return new ProxyEvent(3, var0, var1, var2);
   }

   public ProxyEvent(int var1, int var2) {
      this.type = var1;
      this.chid = var2;
   }

   public ProxyEvent(int var1, int var2, byte[] var3, int var4) {
      this.chid = var2;
      this.type = var1;
      this.data = var3;
      this.length = var4;
   }

   public ProxyEvent(int var1, int var2, String var3, int var4) {
      this.chid = var2;
      this.type = var1;
      this.host = var3;
      this.port = var4;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public int getType() {
      return this.type;
   }

   public int getChannelId() {
      return this.chid;
   }

   public byte[] getData() {
      return this.data;
   }

   public int getDataLength() {
      return this.length;
   }

   public String toString() {
      switch (this.type) {
         case 0:
            return "close@" + this.chid;
         case 1:
            return "connect to " + this.host + ":" + this.port + "@" + this.chid;
         case 2:
            return "listen on " + this.host + ":" + this.port + "@" + this.chid;
         case 3:
            return "read " + this.length + " bytes@" + this.chid;
         default:
            return "uknown event type: " + this.type + "@" + this.chid;
      }
   }
}
