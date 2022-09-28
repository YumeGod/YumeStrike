package beacon;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BeaconPipes {
   protected Map pipes = new HashMap();

   public void reset() {
      synchronized(this) {
         this.pipes = new HashMap();
      }
   }

   public void register(String var1, String var2) {
      synchronized(this) {
         LinkedHashSet var4 = (LinkedHashSet)this.pipes.get(var1);
         if (var4 == null) {
            var4 = new LinkedHashSet();
            this.pipes.put(var1, var4);
         }

         var4.add(var2);
      }
   }

   public void clear(String var1) {
      synchronized(this) {
         this.pipes.remove(var1);
      }
   }

   public List children(String var1) {
      synchronized(this) {
         LinkedHashSet var3 = (LinkedHashSet)this.pipes.get(var1);
         return var3 == null ? new LinkedList() : new LinkedList(var3);
      }
   }

   public void deregister(String var1, String var2) {
      synchronized(this) {
         LinkedHashSet var4 = (LinkedHashSet)this.pipes.get(var1);
         if (var4 != null) {
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               String var6 = var5.next() + "";
               if (var6.equals(var2)) {
                  var5.remove();
               }
            }

         }
      }
   }

   public boolean isChild(String var1, String var2) {
      synchronized(this) {
         LinkedHashSet var4 = (LinkedHashSet)this.pipes.get(var1);
         return var4 == null ? false : var4.contains(var2);
      }
   }
}
