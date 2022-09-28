package filter;

public class NTLMHashCriteria implements Criteria {
   public boolean test(Object var1) {
      if (var1 == null) {
         return false;
      } else {
         return var1.toString().length() == 32;
      }
   }
}
