package org.apache.james.mime4j.dom;

public class ServiceLoaderException extends RuntimeException {
   private static final long serialVersionUID = -2801857820835508778L;

   public ServiceLoaderException(String message) {
      super(message);
   }

   public ServiceLoaderException(Throwable cause) {
      super(cause);
   }

   public ServiceLoaderException(String message, Throwable cause) {
      super(message, cause);
   }
}
