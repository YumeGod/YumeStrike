package org.apache.avalon.framework.logger;

public final class ConsoleLogger implements Logger {
   public static final int LEVEL_DEBUG = 0;
   public static final int LEVEL_INFO = 1;
   public static final int LEVEL_WARN = 2;
   public static final int LEVEL_ERROR = 3;
   public static final int LEVEL_FATAL = 4;
   public static final int LEVEL_DISABLED = 5;
   private final int m_logLevel;

   public ConsoleLogger() {
      this(0);
   }

   public ConsoleLogger(int logLevel) {
      this.m_logLevel = logLevel;
   }

   public void debug(String message) {
      this.debug(message, (Throwable)null);
   }

   public void debug(String message, Throwable throwable) {
      if (this.m_logLevel <= 0) {
         System.out.print("[DEBUG] ");
         System.out.println(message);
         if (null != throwable) {
            throwable.printStackTrace(System.out);
         }
      }

   }

   public boolean isDebugEnabled() {
      return this.m_logLevel <= 0;
   }

   public void info(String message) {
      this.info(message, (Throwable)null);
   }

   public void info(String message, Throwable throwable) {
      if (this.m_logLevel <= 1) {
         System.out.print("[INFO] ");
         System.out.println(message);
         if (null != throwable) {
            throwable.printStackTrace(System.out);
         }
      }

   }

   public boolean isInfoEnabled() {
      return this.m_logLevel <= 1;
   }

   public void warn(String message) {
      this.warn(message, (Throwable)null);
   }

   public void warn(String message, Throwable throwable) {
      if (this.m_logLevel <= 2) {
         System.out.print("[WARNING] ");
         System.out.println(message);
         if (null != throwable) {
            throwable.printStackTrace(System.out);
         }
      }

   }

   public boolean isWarnEnabled() {
      return this.m_logLevel <= 2;
   }

   public void error(String message) {
      this.error(message, (Throwable)null);
   }

   public void error(String message, Throwable throwable) {
      if (this.m_logLevel <= 3) {
         System.out.print("[ERROR] ");
         System.out.println(message);
         if (null != throwable) {
            throwable.printStackTrace(System.out);
         }
      }

   }

   public boolean isErrorEnabled() {
      return this.m_logLevel <= 3;
   }

   public void fatalError(String message) {
      this.fatalError(message, (Throwable)null);
   }

   public void fatalError(String message, Throwable throwable) {
      if (this.m_logLevel <= 4) {
         System.out.print("[FATAL ERROR] ");
         System.out.println(message);
         if (null != throwable) {
            throwable.printStackTrace(System.out);
         }
      }

   }

   public boolean isFatalErrorEnabled() {
      return this.m_logLevel <= 4;
   }

   public Logger getChildLogger(String name) {
      return this;
   }
}
