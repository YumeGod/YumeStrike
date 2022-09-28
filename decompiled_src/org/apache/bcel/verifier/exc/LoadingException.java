package org.apache.bcel.verifier.exc;

public class LoadingException extends VerifierConstraintViolatedException {
   private String detailMessage;

   public LoadingException() {
   }

   public LoadingException(String message) {
      super(message);
      this.detailMessage = message;
   }
}
