package org.apache.james.mime4j.dom.field;

import org.apache.james.mime4j.MimeException;

public class ParseException extends MimeException {
   private static final long serialVersionUID = 1L;

   protected ParseException(String message) {
      super(message);
   }

   protected ParseException(Throwable cause) {
      super(cause);
   }

   protected ParseException(String message, Throwable cause) {
      super(message, cause);
   }
}
