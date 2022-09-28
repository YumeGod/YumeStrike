package org.apache.avalon.framework.logger;

public final class LogKitLogger implements Logger {
   private final org.apache.log.Logger m_logger;

   public LogKitLogger(org.apache.log.Logger logImpl) {
      this.m_logger = logImpl;
   }

   public final void debug(String message) {
      this.m_logger.debug(message);
   }

   public final void debug(String message, Throwable throwable) {
      this.m_logger.debug(message, throwable);
   }

   public final boolean isDebugEnabled() {
      return this.m_logger.isDebugEnabled();
   }

   public final void info(String message) {
      this.m_logger.info(message);
   }

   public final void info(String message, Throwable throwable) {
      this.m_logger.info(message, throwable);
   }

   public final boolean isInfoEnabled() {
      return this.m_logger.isInfoEnabled();
   }

   public final void warn(String message) {
      this.m_logger.warn(message);
   }

   public final void warn(String message, Throwable throwable) {
      this.m_logger.warn(message, throwable);
   }

   public final boolean isWarnEnabled() {
      return this.m_logger.isWarnEnabled();
   }

   public final void error(String message) {
      this.m_logger.error(message);
   }

   public final void error(String message, Throwable throwable) {
      this.m_logger.error(message, throwable);
   }

   public final boolean isErrorEnabled() {
      return this.m_logger.isErrorEnabled();
   }

   public final void fatalError(String message) {
      this.m_logger.fatalError(message);
   }

   public final void fatalError(String message, Throwable throwable) {
      this.m_logger.fatalError(message, throwable);
   }

   public final boolean isFatalErrorEnabled() {
      return this.m_logger.isFatalErrorEnabled();
   }

   public final Logger getChildLogger(String name) {
      return new LogKitLogger(this.m_logger.getChildLogger(name));
   }
}
