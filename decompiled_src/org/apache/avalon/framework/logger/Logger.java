package org.apache.avalon.framework.logger;

public interface Logger {
   void debug(String var1);

   void debug(String var1, Throwable var2);

   boolean isDebugEnabled();

   void info(String var1);

   void info(String var1, Throwable var2);

   boolean isInfoEnabled();

   void warn(String var1);

   void warn(String var1, Throwable var2);

   boolean isWarnEnabled();

   void error(String var1);

   void error(String var1, Throwable var2);

   boolean isErrorEnabled();

   void fatalError(String var1);

   void fatalError(String var1, Throwable var2);

   boolean isFatalErrorEnabled();

   Logger getChildLogger(String var1);
}
