package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Log4JLogger implements Log, Serializable {
   private static final String FQCN;
   private static final boolean is12;
   private transient Logger logger = null;
   private String name = null;
   // $FF: synthetic field
   static Class class$org$apache$commons$logging$impl$Log4JLogger;
   // $FF: synthetic field
   static Class class$org$apache$log4j$Level;
   // $FF: synthetic field
   static Class class$org$apache$log4j$Priority;

   public Log4JLogger() {
   }

   public Log4JLogger(String name) {
      this.name = name;
      this.logger = this.getLogger();
   }

   public Log4JLogger(Logger logger) {
      this.name = logger.getName();
      this.logger = logger;
   }

   public void trace(Object message) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.DEBUG, message, (Throwable)null);
      } else {
         this.getLogger().log(FQCN, Level.DEBUG, message, (Throwable)null);
      }

   }

   public void trace(Object message, Throwable t) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.DEBUG, message, t);
      } else {
         this.getLogger().log(FQCN, Level.DEBUG, message, t);
      }

   }

   public void debug(Object message) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.DEBUG, message, (Throwable)null);
      } else {
         this.getLogger().log(FQCN, Level.DEBUG, message, (Throwable)null);
      }

   }

   public void debug(Object message, Throwable t) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.DEBUG, message, t);
      } else {
         this.getLogger().log(FQCN, Level.DEBUG, message, t);
      }

   }

   public void info(Object message) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.INFO, message, (Throwable)null);
      } else {
         this.getLogger().log(FQCN, Level.INFO, message, (Throwable)null);
      }

   }

   public void info(Object message, Throwable t) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.INFO, message, t);
      } else {
         this.getLogger().log(FQCN, Level.INFO, message, t);
      }

   }

   public void warn(Object message) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.WARN, message, (Throwable)null);
      } else {
         this.getLogger().log(FQCN, Level.WARN, message, (Throwable)null);
      }

   }

   public void warn(Object message, Throwable t) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.WARN, message, t);
      } else {
         this.getLogger().log(FQCN, Level.WARN, message, t);
      }

   }

   public void error(Object message) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.ERROR, message, (Throwable)null);
      } else {
         this.getLogger().log(FQCN, Level.ERROR, message, (Throwable)null);
      }

   }

   public void error(Object message, Throwable t) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.ERROR, message, t);
      } else {
         this.getLogger().log(FQCN, Level.ERROR, message, t);
      }

   }

   public void fatal(Object message) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.FATAL, message, (Throwable)null);
      } else {
         this.getLogger().log(FQCN, Level.FATAL, message, (Throwable)null);
      }

   }

   public void fatal(Object message, Throwable t) {
      if (is12) {
         this.getLogger().log(FQCN, (Priority)Level.FATAL, message, t);
      } else {
         this.getLogger().log(FQCN, Level.FATAL, message, t);
      }

   }

   public Logger getLogger() {
      if (this.logger == null) {
         this.logger = Logger.getLogger(this.name);
      }

      return this.logger;
   }

   public boolean isDebugEnabled() {
      return this.getLogger().isDebugEnabled();
   }

   public boolean isErrorEnabled() {
      return is12 ? this.getLogger().isEnabledFor((Priority)Level.ERROR) : this.getLogger().isEnabledFor(Level.ERROR);
   }

   public boolean isFatalEnabled() {
      return is12 ? this.getLogger().isEnabledFor((Priority)Level.FATAL) : this.getLogger().isEnabledFor(Level.FATAL);
   }

   public boolean isInfoEnabled() {
      return this.getLogger().isInfoEnabled();
   }

   public boolean isTraceEnabled() {
      return this.getLogger().isDebugEnabled();
   }

   public boolean isWarnEnabled() {
      return is12 ? this.getLogger().isEnabledFor((Priority)Level.WARN) : this.getLogger().isEnabledFor(Level.WARN);
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
      FQCN = (class$org$apache$commons$logging$impl$Log4JLogger == null ? (class$org$apache$commons$logging$impl$Log4JLogger = class$("org.apache.commons.logging.impl.Log4JLogger")) : class$org$apache$commons$logging$impl$Log4JLogger).getName();
      is12 = (class$org$apache$log4j$Priority == null ? (class$org$apache$log4j$Priority = class$("org.apache.log4j.Priority")) : class$org$apache$log4j$Priority).isAssignableFrom(class$org$apache$log4j$Level == null ? (class$org$apache$log4j$Level = class$("org.apache.log4j.Level")) : class$org$apache$log4j$Level);
   }
}
