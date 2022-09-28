package org.apache.batik.util;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class CleanerThread extends Thread {
   static volatile ReferenceQueue queue = null;
   static CleanerThread thread = null;
   // $FF: synthetic field
   static Class class$org$apache$batik$util$CleanerThread;

   public static ReferenceQueue getReferenceQueue() {
      if (queue == null) {
         synchronized(class$org$apache$batik$util$CleanerThread == null ? (class$org$apache$batik$util$CleanerThread = class$("org.apache.batik.util.CleanerThread")) : class$org$apache$batik$util$CleanerThread) {
            queue = new ReferenceQueue();
            thread = new CleanerThread();
         }
      }

      return queue;
   }

   protected CleanerThread() {
      super("Batik CleanerThread");
      this.setDaemon(true);
      this.start();
   }

   public void run() {
      while(true) {
         while(true) {
            try {
               Reference var1;
               try {
                  var1 = queue.remove();
               } catch (InterruptedException var3) {
                  continue;
               }

               if (var1 instanceof ReferenceCleared) {
                  ReferenceCleared var2 = (ReferenceCleared)var1;
                  var2.cleared();
               }
            } catch (ThreadDeath var4) {
               throw var4;
            } catch (Throwable var5) {
               var5.printStackTrace();
            }
         }
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public abstract static class PhantomReferenceCleared extends PhantomReference implements ReferenceCleared {
      public PhantomReferenceCleared(Object var1) {
         super(var1, CleanerThread.getReferenceQueue());
      }
   }

   public abstract static class WeakReferenceCleared extends WeakReference implements ReferenceCleared {
      public WeakReferenceCleared(Object var1) {
         super(var1, CleanerThread.getReferenceQueue());
      }
   }

   public abstract static class SoftReferenceCleared extends SoftReference implements ReferenceCleared {
      public SoftReferenceCleared(Object var1) {
         super(var1, CleanerThread.getReferenceQueue());
      }
   }

   public interface ReferenceCleared {
      void cleared();
   }
}
