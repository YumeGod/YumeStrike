package org.apache.fop.layoutmgr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElementListObserver {
   private static List activeObservers = null;

   public static void addObserver(Observer observer) {
      if (!isObservationActive()) {
         activeObservers = new ArrayList();
      }

      activeObservers.add(observer);
   }

   public static void removeObserver(Observer observer) {
      if (isObservationActive()) {
         activeObservers.remove(observer);
      }

   }

   public static void observe(List elementList, String category, String id) {
      if (isObservationActive()) {
         if (category == null) {
            throw new NullPointerException("category must not be null");
         }

         Iterator i = activeObservers.iterator();

         while(i.hasNext()) {
            ((Observer)i.next()).observe(elementList, category, id);
         }
      }

   }

   public static boolean isObservationActive() {
      return activeObservers != null;
   }

   public interface Observer {
      void observe(List var1, String var2, String var3);
   }
}
