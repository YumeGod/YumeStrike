package org.apache.avalon.framework.parameters;

import org.apache.avalon.framework.CascadingException;

public final class ParameterException extends CascadingException {
   public ParameterException(String message) {
      this(message, (Throwable)null);
   }

   public ParameterException(String message, Throwable throwable) {
      super(message, throwable);
   }
}
