package org.apache.fop.events;

import java.util.Locale;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class ValidationExceptionFactory implements EventExceptionManager.ExceptionFactory {
   public Throwable createException(Event event) {
      Locator loc = (Locator)event.getParam("loc");
      String msg = EventFormatter.format(event, Locale.ENGLISH);
      ValidationException ex = new ValidationException(msg, loc);
      if (!Locale.ENGLISH.equals(Locale.getDefault())) {
         ex.setLocalizedMessage(EventFormatter.format(event));
      }

      return ex;
   }

   public Class getExceptionClass() {
      return ValidationException.class;
   }
}
