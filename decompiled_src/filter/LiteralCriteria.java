package filter;

public class LiteralCriteria implements Criteria {
   protected String value;

   public LiteralCriteria(String var1) {
      this.value = var1;
   }

   public boolean test(Object var1) {
      if (var1 == null) {
         return this.value.length() == 0;
      } else {
         return this.value.equals(var1.toString());
      }
   }
}
