package org.apache.fop.fo;

import org.apache.fop.apps.FOPException;
import org.xml.sax.Locator;

public class ValidationException extends FOPException {
   public ValidationException(String message) {
      super(message);
   }

   public ValidationException(String message, Locator locator) {
      super(message, locator);
   }
}
