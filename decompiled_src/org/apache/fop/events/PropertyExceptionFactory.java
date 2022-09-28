package org.apache.fop.events;

import java.util.Locale;
import org.apache.fop.fo.expr.PropertyException;

public class PropertyExceptionFactory implements EventExceptionManager.ExceptionFactory {
   public Throwable createException(Event event) {
      String msg = EventFormatter.format(event, Locale.ENGLISH);
      PropertyException ex = new PropertyException(msg);
      if (!Locale.ENGLISH.equals(Locale.getDefault())) {
         ex.setLocalizedMessage(EventFormatter.format(event));
      }

      return ex;
   }

   public Class getExceptionClass() {
      return PropertyException.class;
   }
}
