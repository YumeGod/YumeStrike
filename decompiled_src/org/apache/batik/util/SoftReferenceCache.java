package org.apache.batik.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class SoftReferenceCache {
   protected final Map map = new HashMap();

   protected SoftReferenceCache() {
   }

   public synchronized void flush() {
      this.map.clear();
      this.notifyAll();
   }

   protected final synchronized boolean isPresentImpl(Object var1) {
      if (!this.map.containsKey(var1)) {
         return false;
      } else {
         Object var2 = this.map.get(var1);
         if (var2 == null) {
            return true;
         } else {
            SoftReference var3 = (SoftReference)var2;
            var2 = var3.get();
            if (var2 != null) {
               return true;
            } else {
               this.clearImpl(var1);
               return false;
            }
         }
      }
   }

   protected final synchronized boolean isDoneImpl(Object var1) {
      Object var2 = this.map.get(var1);
      if (var2 == null) {
         return false;
      } else {
         SoftReference var3 = (SoftReference)var2;
         var2 = var3.get();
         if (var2 != null) {
            return true;
         } else {
            this.clearImpl(var1);
            return false;
         }
      }
   }

   protected final synchronized Object requestImpl(Object var1) {
      if (this.map.containsKey(var1)) {
         Object var2;
         for(var2 = this.map.get(var1); var2 == null; var2 = this.map.get(var1)) {
            try {
               this.wait();
            } catch (InterruptedException var4) {
            }

            if (!this.map.containsKey(var1)) {
               break;
            }
         }

         if (var2 != null) {
            SoftReference var3 = (SoftReference)var2;
            var2 = var3.get();
            if (var2 != null) {
               return var2;
            }
         }
      }

      this.map.put(var1, (Object)null);
      return null;
   }

   protected final synchronized void clearImpl(Object var1) {
      this.map.remove(var1);
      this.notifyAll();
   }

   protected final synchronized void putImpl(Object var1, Object var2) {
      if (this.map.containsKey(var1)) {
         SoftRefKey var3 = new SoftRefKey(var2, var1);
         this.map.put(var1, var3);
         this.notifyAll();
      }

   }

   class SoftRefKey extends CleanerThread.SoftReferenceCleared {
      Object key;

      public SoftRefKey(Object var2, Object var3) {
         super(var2);
         this.key = var3;
      }

      public void cleared() {
         SoftReferenceCache var1 = SoftReferenceCache.this;
         if (var1 != null) {
            synchronized(var1) {
               if (var1.map.containsKey(this.key)) {
                  Object var3 = var1.map.remove(this.key);
                  if (this == var3) {
                     var1.notifyAll();
                  } else {
                     var1.map.put(this.key, var3);
                  }

               }
            }
         }
      }
   }
}
