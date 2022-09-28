package org.apache.avalon.framework;

public class CascadingRuntimeException extends RuntimeException implements CascadingThrowable {
   private final Throwable m_throwable;

   public CascadingRuntimeException(String message, Throwable throwable) {
      super(message);
      this.m_throwable = throwable;
   }

   public final Throwable getCause() {
      return this.m_throwable;
   }
}
