package filter;

public class NegateCriteria implements Criteria {
   protected Criteria parent;

   public NegateCriteria(Criteria var1) {
      this.parent = var1;
   }

   public boolean test(Object var1) {
      return !this.parent.test(var1);
   }
}
