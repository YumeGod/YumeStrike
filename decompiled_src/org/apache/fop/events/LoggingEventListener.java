package org.apache.fop.events;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.events.model.EventSeverity;

public class LoggingEventListener implements EventListener {
   private static Log defaultLog;
   private Log log;
   private boolean skipFatal;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public LoggingEventListener() {
      this(defaultLog);
   }

   public LoggingEventListener(Log log) {
      this(log, true);
   }

   public LoggingEventListener(Log log, boolean skipFatal) {
      this.log = log;
      this.skipFatal = skipFatal;
   }

   public Log getLog() {
      return this.log;
   }

   public void processEvent(Event event) {
      String msg = EventFormatter.format(event);
      EventSeverity severity = event.getSeverity();
      if (severity == EventSeverity.INFO) {
         this.log.info(msg);
      } else if (severity == EventSeverity.WARN) {
         this.log.warn(msg);
      } else if (severity == EventSeverity.ERROR) {
         this.log.error(msg);
      } else if (severity == EventSeverity.FATAL) {
         if (!this.skipFatal) {
            this.log.fatal(msg);
         }
      } else if (!$assertionsDisabled) {
         throw new AssertionError();
      }

   }

   static {
      $assertionsDisabled = !LoggingEventListener.class.desiredAssertionStatus();
      defaultLog = LogFactory.getLog(LoggingEventListener.class);
   }
}
