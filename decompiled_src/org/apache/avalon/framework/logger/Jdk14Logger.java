package org.apache.avalon.framework.logger;

import java.util.logging.Level;

public final class Jdk14Logger implements Logger {
   private final java.util.logging.Logger m_logger;

   public Jdk14Logger(java.util.logging.Logger logImpl) {
      this.m_logger = logImpl;
   }

   public final void debug(String message) {
      this.m_logger.log(Level.FINE, message);
   }

   public final void debug(String message, Throwable throwable) {
      this.m_logger.log(Level.FINE, message, throwable);
   }

   public final boolean isDebugEnabled() {
      return this.m_logger.isLoggable(Level.FINE);
   }

   public final void info(String message) {
      this.m_logger.log(Level.INFO, message);
   }

   public final void info(String message, Throwable throwable) {
      this.m_logger.log(Level.INFO, message, throwable);
   }

   public final boolean isInfoEnabled() {
      return this.m_logger.isLoggable(Level.INFO);
   }

   public final void warn(String message) {
      this.m_logger.log(Level.WARNING, message);
   }

   public final void warn(String message, Throwable throwable) {
      this.m_logger.log(Level.WARNING, message, throwable);
   }

   public final boolean isWarnEnabled() {
      return this.m_logger.isLoggable(Level.WARNING);
   }

   public final void error(String message) {
      this.m_logger.log(Level.SEVERE, message);
   }

   public final void error(String message, Throwable throwable) {
      this.m_logger.log(Level.SEVERE, message, throwable);
   }

   public final boolean isErrorEnabled() {
      return this.m_logger.isLoggable(Level.SEVERE);
   }

   public final void fatalError(String message) {
      this.m_logger.log(Level.SEVERE, message);
   }

   public final void fatalError(String message, Throwable throwable) {
      this.m_logger.log(Level.SEVERE, message, throwable);
   }

   public final boolean isFatalErrorEnabled() {
      return this.m_logger.isLoggable(Level.SEVERE);
   }

   public final Logger getChildLogger(String name) {
      return new Jdk14Logger(java.util.logging.Logger.getLogger(this.m_logger.getName() + "." + name));
   }
}
