package org.apache.bcel.verifier.exc;

public final class AssertionViolatedException extends RuntimeException {
   private String detailMessage;

   public AssertionViolatedException() {
   }

   public AssertionViolatedException(String message) {
      super(message = "INTERNAL ERROR: " + message);
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

   public static void main(String[] args) {
      AssertionViolatedException ave = new AssertionViolatedException("Oops!");
      ave.extendMessage("\nFOUND:\n\t", "\nExiting!!\n");
      throw ave;
   }

   public String getStackTrace() {
      return Utility.getStackTrace(this);
   }
}
