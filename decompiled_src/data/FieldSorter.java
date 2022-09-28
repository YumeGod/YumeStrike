package data;

import java.util.Comparator;
import java.util.Map;

public class FieldSorter implements Comparator {
   protected String field;
   protected Comparator smarts;

   public FieldSorter(String var1, Comparator var2) {
      this.field = var1;
      this.smarts = var2;
   }

   public int compare(Object var1, Object var2) {
      Map var3 = (Map)var1;
      Map var4 = (Map)var2;
      return this.smarts.compare(var3.get(this.field), var4.get(this.field));
   }
}
