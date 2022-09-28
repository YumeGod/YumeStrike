package org.apache.avalon.framework.logger;

public abstract class AbstractLogEnabled implements LogEnabled {
   private Logger m_logger;

   public void enableLogging(Logger logger) {
      this.m_logger = logger;
   }

   protected final Logger getLogger() {
      return this.m_logger;
   }

   protected void setupLogger(Object component) {
      this.setupLogger(component, (String)null);
   }

   protected void setupLogger(Object component, String subCategory) {
      Logger logger = this.m_logger;
      if (null != subCategory) {
         logger = this.m_logger.getChildLogger(subCategory);
      }

      this.setupLogger(component, logger);
   }

   protected void setupLogger(Object component, Logger logger) {
      if (component instanceof LogEnabled) {
         ((LogEnabled)component).enableLogging(logger);
      }

   }
}
