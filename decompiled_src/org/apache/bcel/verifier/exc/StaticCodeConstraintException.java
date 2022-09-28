package org.apache.bcel.verifier.exc;

public abstract class StaticCodeConstraintException extends CodeConstraintException {
   public StaticCodeConstraintException(String message) {
      super(message);
   }
}
