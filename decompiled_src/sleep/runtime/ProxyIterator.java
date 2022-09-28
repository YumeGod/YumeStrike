package sleep.runtime;

import java.util.Iterator;
import sleep.engine.ObjectUtilities;

public class ProxyIterator implements Iterator {
   protected Iterator realIterator;
   protected boolean modifyAllow;

   public ProxyIterator(Iterator var1, boolean var2) {
      this.realIterator = var1;
      this.modifyAllow = var2;
   }

   public static boolean isIterator(Scalar var0) {
      return var0.getActualValue() != null && var0.objectValue() instanceof Iterator;
   }

   public boolean hasNext() {
      return this.realIterator.hasNext();
   }

   public Object next() {
      Object var1 = this.realIterator.next();
      return ObjectUtilities.BuildScalar(true, var1);
   }

   public void remove() {
      if (this.modifyAllow) {
         this.realIterator.remove();
      } else {
         throw new RuntimeException("iterator is read-only");
      }
   }
}
