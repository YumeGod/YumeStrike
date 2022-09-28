package org.apache.fop.fo.pagination;

import java.util.Locale;
import org.apache.fop.events.Event;
import org.apache.fop.events.EventExceptionManager;
import org.apache.fop.events.EventFormatter;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

public class PageProductionException extends RuntimeException {
   private static final long serialVersionUID = -5126033718398975158L;
   private String localizedMessage;
   private Locator locator;

   public PageProductionException(String message, Locator locator) {
      super(message);
      this.setLocator(locator);
   }

   public void setLocator(Locator locator) {
      this.locator = new LocatorImpl(locator);
   }

   public Locator getLocator() {
      return this.locator;
   }

   public void setLocalizedMessage(String msg) {
      this.localizedMessage = msg;
   }

   public String getLocalizedMessage() {
      return this.localizedMessage != null ? this.localizedMessage : super.getLocalizedMessage();
   }

   public static class PageProductionExceptionFactory implements EventExceptionManager.ExceptionFactory {
      public Throwable createException(Event event) {
         Object obj = event.getParam("loc");
         Locator loc = obj instanceof Locator ? (Locator)obj : null;
         String msg = EventFormatter.format(event, Locale.ENGLISH);
         PageProductionException ex = new PageProductionException(msg, loc);
         if (!Locale.ENGLISH.equals(Locale.getDefault())) {
            ex.setLocalizedMessage(EventFormatter.format(event));
         }

         return ex;
      }

      public Class getExceptionClass() {
         return PageProductionException.class;
      }
   }
}
