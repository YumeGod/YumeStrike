package org.apache.batik.util;

public class HaltingThread extends Thread {
   protected boolean beenHalted = false;

   public HaltingThread() {
   }

   public HaltingThread(Runnable var1) {
      super(var1);
   }

   public HaltingThread(String var1) {
      super(var1);
   }

   public HaltingThread(Runnable var1, String var2) {
      super(var1, var2);
   }

   public boolean isHalted() {
      synchronized(this) {
         return this.beenHalted;
      }
   }

   public void halt() {
      synchronized(this) {
         this.beenHalted = true;
      }
   }

   public void clearHalted() {
      synchronized(this) {
         this.beenHalted = false;
      }
   }

   public static void haltThread() {
      haltThread(Thread.currentThread());
   }

   public static void haltThread(Thread var0) {
      if (var0 instanceof HaltingThread) {
         ((HaltingThread)var0).halt();
      }

   }

   public static boolean hasBeenHalted() {
      return hasBeenHalted(Thread.currentThread());
   }

   public static boolean hasBeenHalted(Thread var0) {
      return var0 instanceof HaltingThread ? ((HaltingThread)var0).isHalted() : false;
   }
}
