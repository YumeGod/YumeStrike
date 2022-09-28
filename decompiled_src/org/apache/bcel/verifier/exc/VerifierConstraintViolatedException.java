package org.apache.bcel.verifier.exc;

public abstract class VerifierConstraintViolatedException extends RuntimeException {
   private String detailMessage;

   VerifierConstraintViolatedException() {
   }

   VerifierConstraintViolatedException(String message) {
      super(message);
      this.detailMessage = message;
   }

   public void extendMessage(String pre, String post) {
      if (pre == null) {
         pre = "";
      }

      if (this.detailMessage == null) {
         this.detailMessage = "";
      }

      if (post == null) {
         post = "";
      }

      this.detailMessage = pre + this.detailMessage + post;
   }

   public String getMessage() {
      return this.detailMessage;
   }
}
