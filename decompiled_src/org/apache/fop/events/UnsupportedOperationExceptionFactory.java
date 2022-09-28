package org.apache.fop.events;

import java.util.Locale;

public class UnsupportedOperationExceptionFactory implements EventExceptionManager.ExceptionFactory {
   public Throwable createException(Event event) {
      String msg = EventFormatter.format(event, Locale.ENGLISH);
      UnsupportedOperationException ex = new UnsupportedOperationException(msg);
      return ex;
   }

   public Class getExceptionClass() {
      return UnsupportedOperationException.class;
   }
}
