package org.apache.avalon.framework.logger;

/** @deprecated */
public abstract class AbstractLoggable implements Loggable {
   private org.apache.log.Logger m_logger;

   public void setLogger(org.apache.log.Logger logger) {
      this.m_logger = logger;
   }

   protected final org.apache.log.Logger getLogger() {
      return this.m_logger;
   }

   protected void setupLogger(Object component) {
      this.setupLogger(component, (String)null);
   }

   protected void setupLogger(Object component, String subCategory) {
      org.apache.log.Logger logger = this.m_logger;
      if (null != subCategory) {
         logger = this.m_logger.getChildLogger(subCategory);
      }

      this.setupLogger(component, logger);
   }

   protected void setupLogger(Object component, org.apache.log.Logger logger) {
      if (component instanceof Loggable) {
         ((Loggable)component).setLogger(logger);
      }

   }
}
