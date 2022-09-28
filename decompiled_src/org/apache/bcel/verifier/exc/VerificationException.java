package org.apache.bcel.verifier.exc;

public abstract class VerificationException extends VerifierConstraintViolatedException {
   VerificationException() {
   }

   VerificationException(String message) {
      super(message);
   }
}
