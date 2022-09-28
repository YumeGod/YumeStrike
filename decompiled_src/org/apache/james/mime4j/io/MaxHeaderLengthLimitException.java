package org.apache.james.mime4j.io;

import org.apache.james.mime4j.MimeException;

public class MaxHeaderLengthLimitException extends MimeException {
   private static final long serialVersionUID = 8924290744274769913L;

   public MaxHeaderLengthLimitException(String message) {
      super(message);
   }
}
