package org.apache.fop.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommandLineLogger implements Log {
   public static final int LOG_LEVEL_TRACE = 1;
   public static final int LOG_LEVEL_DEBUG = 2;
   public static final int LOG_LEVEL_INFO = 3;
   public static final int LOG_LEVEL_WARN = 4;
   public static final int LOG_LEVEL_ERROR = 5;
   public static final int LOG_LEVEL_FATAL = 6;
   private int logLevel;
   private String logName;

   public CommandLineLogger(String logName) {
      this.logName = logName;
      this.setLogLevel((String)LogFactory.getFactory().getAttribute("level"));
   }

   public void setLogLevel(String level) {
      if ("fatal".equals(level)) {
         this.logLevel = 6;
      } else if ("error".equals(level)) {
         this.logLevel = 5;
      } else if ("warn".equals(level)) {
         this.logLevel = 4;
      } else if ("info".equals(level)) {
         this.logLevel = 3;
      } else if ("debug".equals(level)) {
         this.logLevel = 2;
      } else if ("trace".equals(level)) {
         this.logLevel = 1;
      } else {
         this.logLevel = 3;
      }

   }

   public final boolean isTraceEnabled() {
      return this.logLevel <= 1;
   }

   public final boolean isDebugEnabled() {
      return this.logLevel <= 2;
   }

   public final boolean isInfoEnabled() {
      return this.logLevel <= 3;
   }

   public final boolean isWarnEnabled() {
      return this.logLevel <= 4;
   }

   public final boolean isErrorEnabled() {
      return this.logLevel <= 5;
   }

   public final boolean isFatalEnabled() {
      return this.logLevel <= 6;
   }

   public final void trace(Object message) {
      if (this.isTraceEnabled()) {
         this.log(1, message, (Throwable)null);
      }

   }

   public final void trace(Object message, Throwable t) {
      if (this.isTraceEnabled()) {
         this.log(1, message, t);
      }

   }

   public final void debug(Object message) {
      if (this.isDebugEnabled()) {
         this.log(2, message, (Throwable)null);
      }

   }

   public final void debug(Object message, Throwable t) {
      if (this.isDebugEnabled()) {
         this.log(2, message, t);
      }

   }

   public final void info(Object message) {
      if (this.isInfoEnabled()) {
         this.log(3, message, (Throwable)null);
      }

   }

   public final void info(Object message, Throwable t) {
      if (this.isInfoEnabled()) {
         this.log(3, message, t);
      }

   }

   public final void warn(Object message) {
      if (this.isWarnEnabled()) {
         this.log(4, message, (Throwable)null);
      }

   }

   public final void warn(Object message, Throwable t) {
      if (this.isWarnEnabled()) {
         this.log(4, message, t);
      }

   }

   public final void error(Object message) {
      if (this.isErrorEnabled()) {
         this.log(5, message, (Throwable)null);
      }

   }

   public final void error(Object message, Throwable t) {
      if (this.isErrorEnabled()) {
         this.log(5, message, t);
      }

   }

   public final void fatal(Object message) {
      if (this.isFatalEnabled()) {
         this.log(6, message, (Throwable)null);
      }

   }

   public final void fatal(Object message, Throwable t) {
      if (this.isFatalEnabled()) {
         this.log(6, message, t);
      }

   }

   protected void log(int type, Object message, Throwable t) {
      StringBuffer buf = new StringBuffer();
      buf.append(String.valueOf(message));
      if (t != null) {
         buf.append("\n");
         if (!this.isDebugEnabled()) {
            buf.append(t.toString());
            buf.append("\n");
         } else {
            StringWriter sw = new StringWriter(1024);
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            buf.append(sw.toString());
         }
      }

      if (type >= 4) {
         System.err.println(buf);
      } else {
         System.out.println(buf);
      }

   }
}
