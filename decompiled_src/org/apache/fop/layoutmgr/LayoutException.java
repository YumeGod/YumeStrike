package org.apache.fop.layoutmgr;

import java.util.Locale;
import org.apache.fop.events.Event;
import org.apache.fop.events.EventExceptionManager;
import org.apache.fop.events.EventFormatter;

public class LayoutException extends RuntimeException {
   private static final long serialVersionUID = 5157080040923740433L;
   private String localizedMessage;
   private LayoutManager layoutManager;

   public LayoutException(String message) {
      this(message, (LayoutManager)null);
   }

   public LayoutException(String message, LayoutManager lm) {
      super(message);
      this.layoutManager = lm;
   }

   public void setLocalizedMessage(String msg) {
      this.localizedMessage = msg;
   }

   public String getLocalizedMessage() {
      return this.localizedMessage != null ? this.localizedMessage : super.getLocalizedMessage();
   }

   public LayoutManager getLayoutManager() {
      return this.layoutManager;
   }

   public static class LayoutExceptionFactory implements EventExceptionManager.ExceptionFactory {
      public Throwable createException(Event event) {
         Object source = event.getSource();
         LayoutManager lm = source instanceof LayoutManager ? (LayoutManager)source : null;
         String msg = EventFormatter.format(event, Locale.ENGLISH);
         LayoutException ex = new LayoutException(msg, lm);
         if (!Locale.ENGLISH.equals(Locale.getDefault())) {
            ex.setLocalizedMessage(EventFormatter.format(event));
         }

         return ex;
      }

      public Class getExceptionClass() {
         return LayoutException.class;
      }
   }
}
