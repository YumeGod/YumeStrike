package org.apache.avalon.framework.logger;

import org.apache.log.Hierarchy;
import org.apache.log.LogEvent;
import org.apache.log.LogTarget;
import org.apache.log.Priority;

public final class LogKit2AvalonLoggerAdapter implements LogTarget {
   private final Logger m_logger;

   public static org.apache.log.Logger createLogger(Logger logger) {
      Hierarchy hierarchy = new Hierarchy();
      org.apache.log.Logger logKitLogger = hierarchy.getLoggerFor("");
      LogKit2AvalonLoggerAdapter target = new LogKit2AvalonLoggerAdapter(logger);
      logKitLogger.setLogTargets(new LogTarget[]{target});
      if (logger.isDebugEnabled()) {
         logKitLogger.setPriority(Priority.DEBUG);
      } else if (logger.isInfoEnabled()) {
         logKitLogger.setPriority(Priority.INFO);
      } else if (logger.isWarnEnabled()) {
         logKitLogger.setPriority(Priority.WARN);
      } else if (logger.isErrorEnabled()) {
         logKitLogger.setPriority(Priority.ERROR);
      } else if (logger.isFatalErrorEnabled()) {
         logKitLogger.setPriority(Priority.FATAL_ERROR);
      }

      return logKitLogger;
   }

   public LogKit2AvalonLoggerAdapter(Logger logger) {
      if (null == logger) {
         throw new NullPointerException("logger");
      } else {
         this.m_logger = logger;
      }
   }

   public void processEvent(LogEvent event) {
      String message = event.getMessage();
      Throwable throwable = event.getThrowable();
      Priority priority = event.getPriority();
      if (Priority.DEBUG == priority) {
         this.m_logger.debug(message, throwable);
      } else if (Priority.INFO == priority) {
         this.m_logger.info(message, throwable);
      } else if (Priority.WARN == priority) {
         this.m_logger.warn(message, throwable);
      } else if (Priority.ERROR == priority) {
         this.m_logger.error(message, throwable);
      } else {
         this.m_logger.fatalError(message, throwable);
      }

   }
}
