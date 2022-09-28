package net.jsign.commons.cli;

import java.util.Collection;
import java.util.Iterator;

public class AmbiguousOptionException extends UnrecognizedOptionException {
   private static final long serialVersionUID = 5829816121277947229L;
   private final Collection matchingOptions;

   public AmbiguousOptionException(String option, Collection matchingOptions) {
      super(createMessage(option, matchingOptions), option);
      this.matchingOptions = matchingOptions;
   }

   public Collection getMatchingOptions() {
      return this.matchingOptions;
   }

   private static String createMessage(String option, Collection matchingOptions) {
      StringBuilder buf = new StringBuilder("Ambiguous option: '");
      buf.append(option);
      buf.append("'  (could be: ");
      Iterator it = matchingOptions.iterator();

      while(it.hasNext()) {
         buf.append("'");
         buf.append((String)it.next());
         buf.append("'");
         if (it.hasNext()) {
            buf.append(", ");
         }
      }

      buf.append(")");
      return buf.toString();
   }
}
