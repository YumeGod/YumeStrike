package endpoint;

import tap.TapProtocol;

public abstract class Base {
   protected TapProtocol tap;
   protected long rx;
   protected long tx;
   protected FrameReader r;

   public Base(TapProtocol var1) {
      this.tap = var1;
   }

   public void start() {
      this.r = new FrameReader();
      (new Thread(this.r)).start();
   }

   public void setHWAddress(byte[] var1) {
      this.tap.setHWAddress(var1);
   }

   public abstract void processFrame(byte[] var1);

   public long getTransmittedBytes() {
      return this.tx;
   }

   public long getReceivedBytes() {
      return this.rx;
   }

   public abstract void shutdown();

   public void quit() {
      if (!this.tap.isStopped() && !"disconnected".equals(this.tap.getRemoteHost()) && !"not connected".equals(this.tap.getRemoteHost()) && this.tap.getRemoteHost() != null) {
         this.processFrame(this.tap.readKillFrame());
      }

      this.stop();
   }

   public void stop() {
      this.tap.setRemoteHost("disconnected");
      this.tap.stop();
      this.shutdown();
   }

   private class FrameReader implements Runnable {
      public boolean going;

      private FrameReader() {
         this.going = true;
      }

      public void run() {
         while(!Base.this.tap.isStopped()) {
            byte[] var1 = Base.this.tap.readFrame();
            Base var10000 = Base.this;
            var10000.tx += (long)var1.length;
            Base.this.processFrame(var1);
         }

      }

      // $FF: synthetic method
      FrameReader(Object var2) {
         this();
      }
   }
}
