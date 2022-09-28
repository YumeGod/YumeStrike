package org.apache.bcel.verifier.exc;

public class ClassConstraintException extends VerificationException {
   private String detailMessage;

   public ClassConstraintException() {
   }

   public ClassConstraintException(String message) {
      super(message);
      this.detailMessage = message;
   }
}
