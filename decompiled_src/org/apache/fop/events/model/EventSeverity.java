package org.apache.fop.events.model;

import java.io.ObjectStreamException;
import java.io.Serializable;

public final class EventSeverity implements Serializable {
   private static final long serialVersionUID = 4108175215810759243L;
   public static final EventSeverity INFO = new EventSeverity("INFO");
   public static final EventSeverity WARN = new EventSeverity("WARN");
   public static final EventSeverity ERROR = new EventSeverity("ERROR");
   public static final EventSeverity FATAL = new EventSeverity("FATAL");
   private String name;

   private EventSeverity(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public static EventSeverity valueOf(String name) {
      if (INFO.getName().equalsIgnoreCase(name)) {
         return INFO;
      } else if (WARN.getName().equalsIgnoreCase(name)) {
         return WARN;
      } else if (ERROR.getName().equalsIgnoreCase(name)) {
         return ERROR;
      } else if (FATAL.getName().equalsIgnoreCase(name)) {
         return FATAL;
      } else {
         throw new IllegalArgumentException("Illegal value for enumeration: " + name);
      }
   }

   private Object readResolve() throws ObjectStreamException {
      return valueOf(this.getName());
   }

   public String toString() {
      return "EventSeverity:" + this.name;
   }
}
