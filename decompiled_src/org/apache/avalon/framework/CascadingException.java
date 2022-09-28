package org.apache.avalon.framework;

public class CascadingException extends Exception implements CascadingThrowable {
   private final Throwable m_throwable;

   public CascadingException(String message) {
      this(message, (Throwable)null);
   }

   public CascadingException(String message, Throwable throwable) {
      super(message);
      this.m_throwable = throwable;
   }

   public final Throwable getCause() {
      return this.m_throwable;
   }
}
