package org.apache.james.mime4j;

import java.io.IOException;

public class MimeIOException extends IOException {
   private static final long serialVersionUID = 5393613459533735409L;

   public MimeIOException(MimeException cause) {
      super(cause == null ? null : cause.getMessage());
      this.initCause(cause);
   }

   public MimeException getCause() {
      return (MimeException)super.getCause();
   }
}
