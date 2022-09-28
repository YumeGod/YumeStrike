package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.logging.Log;

public class AvalonLogger implements Log, Serializable {
   private static Logger defaultLogger = null;
   private transient Logger logger = null;
   private String name = null;

   public AvalonLogger(Logger logger) {
      this.name = this.name;
      this.logger = logger;
   }

   public AvalonLogger(String name) {
      if (defaultLogger == null) {
         throw new NullPointerException("default logger has to be specified if this constructor is used!");
      } else {
         this.logger = this.getLogger();
      }
   }

   public Logger getLogger() {
      if (this.logger == null) {
         this.logger = defaultLogger.getChildLogger(this.name);
      }

      return this.logger;
   }

   public static void setDefaultLogger(Logger logger) {
      defaultLogger = logger;
   }

   public void debug(Object o, Throwable t) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(String.valueOf(o), t);
      }

   }

   public void debug(Object o) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(String.valueOf(o));
      }

   }

   public void error(Object o, Throwable t) {
      if (this.getLogger().isErrorEnabled()) {
         this.getLogger().error(String.valueOf(o), t);
      }

   }

   public void error(Object o) {
      if (this.getLogger().isErrorEnabled()) {
         this.getLogger().error(String.valueOf(o));
      }

   }

   public void fatal(Object o, Throwable t) {
      if (this.getLogger().isFatalErrorEnabled()) {
         this.getLogger().fatalError(String.valueOf(o), t);
      }

   }

   public void fatal(Object o) {
      if (this.getLogger().isFatalErrorEnabled()) {
         this.getLogger().fatalError(String.valueOf(o));
      }

   }

   public void info(Object o, Throwable t) {
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info(String.valueOf(o), t);
      }

   }

   public void info(Object o) {
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info(String.valueOf(o));
      }

   }

   public boolean isDebugEnabled() {
      return this.getLogger().isDebugEnabled();
   }

   public boolean isErrorEnabled() {
      return this.getLogger().isErrorEnabled();
   }

   public boolean isFatalEnabled() {
      return this.getLogger().isFatalErrorEnabled();
   }

   public boolean isInfoEnabled() {
      return this.getLogger().isInfoEnabled();
   }

   public boolean isTraceEnabled() {
      return this.getLogger().isDebugEnabled();
   }

   public boolean isWarnEnabled() {
      return this.getLogger().isWarnEnabled();
   }

   public void trace(Object o, Throwable t) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(String.valueOf(o), t);
      }

   }

   public void trace(Object o) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(String.valueOf(o));
      }

   }

   public void warn(Object o, Throwable t) {
      if (this.getLogger().isWarnEnabled()) {
         this.getLogger().warn(String.valueOf(o), t);
      }

   }

   public void warn(Object o) {
      if (this.getLogger().isWarnEnabled()) {
         this.getLogger().warn(String.valueOf(o));
      }

   }
}
