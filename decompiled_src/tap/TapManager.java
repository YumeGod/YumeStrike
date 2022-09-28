package tap;

public class TapManager {
   protected boolean stopped = false;
   protected int fd;
   protected byte[] buffer = new byte[65536];
   protected String ifname;

   public static void main(String[] var0) {
      System.loadLibrary("tapmanager");
      TapManager var1 = new TapManager(var0[0]);
      byte[] var2 = new byte[65536];

      while(true) {
         int var3 = var1.readFrame(var2);
         System.err.println("Read " + var3 + " bytes");
      }
   }

   public String getInterface() {
      return this.ifname;
   }

   public TapManager(String var1) {
      this.ifname = var1;
      this.fd = this.startTap(var1);
      if (this.fd < 0) {
         throw new RuntimeException("Could not allocate tap: " + this.fd);
      }
   }

   public native int startTap(String var1);

   protected native int readFrame(int var1, int var2, byte[] var3);

   public byte[] readFrame() {
      int var1 = this.readFrame(this.buffer);
      byte[] var2 = new byte[var1];
      System.arraycopy(this.buffer, 0, var2, 0, var1);
      return var2;
   }

   public int readFrame(byte[] var1) {
      return this.readFrame(this.fd, 0, var1);
   }

   protected native void writeFrame(int var1, byte[] var2, int var3);

   protected native void setHWAddress(int var1, byte[] var2);

   protected native void stopInterface(int var1);

   public void stop() {
      this.stopped = true;
      this.stopInterface(this.fd);
   }

   public boolean isStopped() {
      return this.stopped;
   }

   public void setHWAddress(byte[] var1) {
      if (var1.length != 6) {
         throw new IllegalArgumentException("Hardware Address must be 6 bytes");
      } else {
         this.setHWAddress(this.fd, var1);
      }
   }

   public void writeFrame(byte[] var1, int var2) {
      if (var2 <= var1.length && var2 <= 65535) {
         this.writeFrame(this.fd, var1, var2);
      } else {
         throw new IllegalArgumentException("Bad frame size");
      }
   }
}
