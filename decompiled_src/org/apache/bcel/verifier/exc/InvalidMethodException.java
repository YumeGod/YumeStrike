package org.apache.bcel.verifier.exc;

public class InvalidMethodException extends RuntimeException {
   private InvalidMethodException() {
   }

   public InvalidMethodException(String message) {
      super(message);
   }
}
