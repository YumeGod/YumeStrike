package org.apache.fop.events;

import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import org.apache.fop.events.model.EventSeverity;

public class Event extends EventObject {
   private static final long serialVersionUID = -1310594422868258083L;
   private String eventGroupID;
   private String eventKey;
   private EventSeverity severity;
   private Map params;

   public Event(Object source, String eventID, EventSeverity severity, Map params) {
      super(source);
      int pos = eventID.lastIndexOf(46);
      if (pos >= 0 && pos != eventID.length() - 1) {
         this.eventGroupID = eventID.substring(0, pos);
         this.eventKey = eventID.substring(pos + 1);
      } else {
         this.eventKey = eventID;
      }

      this.setSeverity(severity);
      this.params = params;
   }

   public String getEventID() {
      return this.eventGroupID == null ? this.eventKey : this.eventGroupID + '.' + this.eventKey;
   }

   public String getEventGroupID() {
      return this.eventGroupID;
   }

   public String getEventKey() {
      return this.eventKey;
   }

   public EventSeverity getSeverity() {
      return this.severity;
   }

   public void setSeverity(EventSeverity severity) {
      this.severity = severity;
   }

   public Object getParam(String key) {
      return this.params != null ? this.params.get(key) : null;
   }

   public Map getParams() {
      return Collections.unmodifiableMap(this.params);
   }

   public static ParamsBuilder paramsBuilder() {
      return new ParamsBuilder();
   }

   public static class ParamsBuilder {
      private Map params;

      public ParamsBuilder param(String name, Object value) {
         if (this.params == null) {
            this.params = new HashMap();
         }

         this.params.put(name, value);
         return this;
      }

      public Map build() {
         return this.params;
      }
   }
}
