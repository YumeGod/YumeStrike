package org.apache.bcel.verifier.exc;

public class LocalVariableInfoInconsistentException extends ClassConstraintException {
   public LocalVariableInfoInconsistentException() {
   }

   public LocalVariableInfoInconsistentException(String message) {
      super(message);
   }
}
