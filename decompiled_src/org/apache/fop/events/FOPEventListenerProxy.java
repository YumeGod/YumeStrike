package org.apache.fop.events;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.events.model.EventSeverity;
import org.apache.fop.fo.FOValidationEventProducer;
import org.apache.fop.layoutmgr.BlockLevelEventProducer;

public class FOPEventListenerProxy implements EventListener {
   private static final String FOVALIDATION_EVENT_ID_PREFIX;
   private static final String BLOCK_LEVEL_EVENT_ID_PREFIX;
   private EventListener delegate;
   private FOUserAgent userAgent;

   public FOPEventListenerProxy(EventListener delegate, FOUserAgent userAgent) {
      this.delegate = delegate;
      this.userAgent = userAgent;
   }

   public synchronized void processEvent(Event event) {
      Boolean canRecover;
      if (event.getEventID().startsWith(FOVALIDATION_EVENT_ID_PREFIX)) {
         canRecover = (Boolean)event.getParam("canRecover");
         if (Boolean.TRUE.equals(canRecover) && !this.userAgent.validateStrictly()) {
            event.setSeverity(EventSeverity.WARN);
         }
      } else if (event.getEventID().startsWith(BLOCK_LEVEL_EVENT_ID_PREFIX)) {
         canRecover = (Boolean)event.getParam("canRecover");
         if (Boolean.TRUE.equals(canRecover)) {
            event.setSeverity(EventSeverity.WARN);
         }
      }

      this.delegate.processEvent(event);
   }

   static {
      FOVALIDATION_EVENT_ID_PREFIX = FOValidationEventProducer.class.getName();
      BLOCK_LEVEL_EVENT_ID_PREFIX = BlockLevelEventProducer.class.getName();
   }
}
