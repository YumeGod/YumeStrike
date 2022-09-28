package org.apache.fop.events;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.xmlgraphics.util.Service;

public class EventExceptionManager {
   private static final Map EXCEPTION_FACTORIES = new HashMap();

   public static void throwException(Event event, String exceptionClass) throws Throwable {
      if (exceptionClass != null) {
         ExceptionFactory factory = (ExceptionFactory)EXCEPTION_FACTORIES.get(exceptionClass);
         if (factory != null) {
            throw factory.createException(event);
         } else {
            throw new IllegalArgumentException("No such ExceptionFactory available: " + exceptionClass);
         }
      } else {
         String msg = EventFormatter.format(event);
         Throwable t = null;
         Iterator iter = event.getParams().values().iterator();

         while(iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof Throwable) {
               t = (Throwable)o;
               break;
            }
         }

         if (t != null) {
            throw new RuntimeException(msg, t);
         } else {
            throw new RuntimeException(msg);
         }
      }
   }

   static {
      Iterator iter = Service.providers(ExceptionFactory.class, true);

      while(iter.hasNext()) {
         ExceptionFactory factory = (ExceptionFactory)iter.next();
         EXCEPTION_FACTORIES.put(factory.getExceptionClass().getName(), factory);
      }

   }

   public interface ExceptionFactory {
      Throwable createException(Event var1);

      Class getExceptionClass();
   }
}
