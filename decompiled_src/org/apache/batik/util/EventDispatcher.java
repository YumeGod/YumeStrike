package org.apache.batik.util;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class EventDispatcher {
   public static void fireEvent(final Dispatcher var0, final List var1, final Object var2, final boolean var3) {
      if (var3 && !EventQueue.isDispatchThread()) {
         Runnable var15 = new Runnable() {
            public void run() {
               EventDispatcher.fireEvent(var0, var1, var2, var3);
            }
         };

         try {
            EventQueue.invokeAndWait(var15);
         } catch (InvocationTargetException var9) {
            var9.printStackTrace();
         } catch (InterruptedException var10) {
         } catch (ThreadDeath var11) {
            throw var11;
         } catch (Throwable var12) {
            var12.printStackTrace();
         }

      } else {
         Object[] var4 = null;
         Throwable var5 = null;
         int var6 = 10;

         while(true) {
            --var6;
            if (var6 == 0) {
               break;
            }

            try {
               synchronized(var1) {
                  if (var1.size() == 0) {
                     return;
                  }

                  var4 = var1.toArray();
                  break;
               }
            } catch (Throwable var14) {
               var5 = var14;
            }
         }

         if (var4 == null) {
            if (var5 != null) {
               var5.printStackTrace();
            }

         } else {
            dispatchEvent(var0, var4, var2);
         }
      }
   }

   protected static void dispatchEvent(Dispatcher var0, Object[] var1, Object var2) {
      ThreadDeath var3 = null;

      try {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            try {
               Object var5;
               synchronized(var1) {
                  var5 = var1[var4];
                  if (var5 == null) {
                     continue;
                  }

                  var1[var4] = null;
               }

               var0.dispatch(var5, var2);
            } catch (ThreadDeath var9) {
               var3 = var9;
            } catch (Throwable var10) {
               var10.printStackTrace();
            }
         }
      } catch (ThreadDeath var11) {
         var3 = var11;
      } catch (Throwable var12) {
         if (var1[var1.length - 1] != null) {
            dispatchEvent(var0, var1, var2);
         }

         var12.printStackTrace();
      }

      if (var3 != null) {
         throw var3;
      }
   }

   public interface Dispatcher {
      void dispatch(Object var1, Object var2);
   }
}
