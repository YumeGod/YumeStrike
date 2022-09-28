package org.apache.bcel.verifier.exc;

public abstract class CodeConstraintException extends VerificationException {
   CodeConstraintException() {
   }

   CodeConstraintException(String message) {
      super(message);
   }
}
