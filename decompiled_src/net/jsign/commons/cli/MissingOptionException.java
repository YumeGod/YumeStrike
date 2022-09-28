package net.jsign.commons.cli;

import java.util.Iterator;
import java.util.List;

public class MissingOptionException extends ParseException {
   private static final long serialVersionUID = 8161889051578563249L;
   private List missingOptions;

   public MissingOptionException(String message) {
      super(message);
   }

   public MissingOptionException(List missingOptions) {
      this(createMessage(missingOptions));
      this.missingOptions = missingOptions;
   }

   public List getMissingOptions() {
      return this.missingOptions;
   }

   private static String createMessage(List missingOptions) {
      StringBuilder buf = new StringBuilder("Missing required option");
      buf.append(missingOptions.size() == 1 ? "" : "s");
      buf.append(": ");
      Iterator it = missingOptions.iterator();

      while(it.hasNext()) {
         buf.append(it.next());
         if (it.hasNext()) {
            buf.append(", ");
         }
      }

      return buf.toString();
   }
}
