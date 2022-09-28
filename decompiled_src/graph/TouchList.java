package graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TouchList extends LinkedList {
   protected Set touched = new HashSet();

   public void startUpdates() {
      this.touched.clear();
   }

   public void touch(Object var1) {
      this.touched.add(var1);
   }

   public List clearUntouched() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (!this.touched.contains(var3)) {
            var1.add(var3);
            var2.remove();
         }
      }

      return var1;
   }
}
