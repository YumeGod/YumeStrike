package org.apache.avalon.framework;

public class CascadingError extends Error implements CascadingThrowable {
   private final Throwable m_throwable;

   public CascadingError(String message, Throwable throwable) {
      super(message);
      this.m_throwable = throwable;
   }

   public final Throwable getCause() {
      return this.m_throwable;
   }
}
