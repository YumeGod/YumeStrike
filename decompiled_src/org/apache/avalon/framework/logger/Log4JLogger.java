package org.apache.avalon.framework.logger;

import org.apache.log4j.Level;

public final class Log4JLogger implements Logger {
   private static final String FQCN;
   private final org.apache.log4j.Logger m_logger;
   // $FF: synthetic field
   static Class class$org$apache$avalon$framework$logger$Log4JLogger;

   public Log4JLogger(org.apache.log4j.Logger logImpl) {
      this.m_logger = logImpl;
   }

   public final void debug(String message) {
      this.m_logger.log(FQCN, Level.DEBUG, message, (Throwable)null);
   }

   public final void debug(String message, Throwable throwable) {
      this.m_logger.log(FQCN, Level.DEBUG, message, throwable);
   }

   public final boolean isDebugEnabled() {
      return this.m_logger.isDebugEnabled();
   }

   public final void info(String message) {
      this.m_logger.log(FQCN, Level.INFO, message, (Throwable)null);
   }

   public final void info(String message, Throwable throwable) {
      this.m_logger.log(FQCN, Level.INFO, message, throwable);
   }

   public final boolean isInfoEnabled() {
      return this.m_logger.isInfoEnabled();
   }

   public final void warn(String message) {
      this.m_logger.log(FQCN, Level.WARN, message, (Throwable)null);
   }

   public final void warn(String message, Throwable throwable) {
      this.m_logger.log(FQCN, Level.WARN, message, throwable);
   }

   public final boolean isWarnEnabled() {
      return this.m_logger.isEnabledFor(Level.WARN);
   }

   public final void error(String message) {
      this.m_logger.log(FQCN, Level.ERROR, message, (Throwable)null);
   }

   public final void error(String message, Throwable throwable) {
      this.m_logger.log(FQCN, Level.ERROR, message, throwable);
   }

   public final boolean isErrorEnabled() {
      return this.m_logger.isEnabledFor(Level.ERROR);
   }

   public final void fatalError(String message) {
      this.m_logger.log(FQCN, Level.FATAL, message, (Throwable)null);
   }

   public final void fatalError(String message, Throwable throwable) {
      this.m_logger.log(FQCN, Level.ERROR, message, throwable);
   }

   public final boolean isFatalErrorEnabled() {
      return this.m_logger.isEnabledFor(Level.FATAL);
   }

   public final Logger getChildLogger(String name) {
      return new Log4JLogger(org.apache.log4j.Logger.getLogger(this.m_logger.getName() + "." + name));
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      FQCN = (class$org$apache$avalon$framework$logger$Log4JLogger == null ? (class$org$apache$avalon$framework$logger$Log4JLogger = class$("org.apache.avalon.framework.logger.Log4JLogger")) : class$org$apache$avalon$framework$logger$Log4JLogger).getName();
   }
}
