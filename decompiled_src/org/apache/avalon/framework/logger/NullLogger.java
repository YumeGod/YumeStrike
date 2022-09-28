package org.apache.avalon.framework.logger;

public final class NullLogger implements Logger {
   public void debug(String message) {
   }

   public void debug(String message, Throwable throwable) {
   }

   public boolean isDebugEnabled() {
      return false;
   }

   public void info(String message) {
   }

   public void info(String message, Throwable throwable) {
   }

   public boolean isInfoEnabled() {
      return false;
   }

   public void warn(String message) {
   }

   public void warn(String message, Throwable throwable) {
   }

   public boolean isWarnEnabled() {
      return false;
   }

   public void error(String message) {
   }

   public void error(String message, Throwable throwable) {
   }

   public boolean isErrorEnabled() {
      return false;
   }

   public void fatalError(String message) {
   }

   public void fatalError(String message, Throwable throwable) {
   }

   public boolean isFatalErrorEnabled() {
      return false;
   }

   public Logger getChildLogger(String name) {
      return this;
   }
}
