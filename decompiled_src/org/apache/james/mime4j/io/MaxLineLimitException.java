package org.apache.james.mime4j.io;

import java.io.IOException;

public class MaxLineLimitException extends IOException {
   private static final long serialVersionUID = 1855987166990764426L;

   public MaxLineLimitException(String message) {
      super(message);
   }
}
