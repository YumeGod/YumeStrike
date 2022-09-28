package org.apache.avalon.framework.context;

import org.apache.avalon.framework.CascadingException;

public class ContextException extends CascadingException {
   public ContextException(String message) {
      this(message, (Throwable)null);
   }

   public ContextException(String message, Throwable throwable) {
      super(message, throwable);
   }
}
