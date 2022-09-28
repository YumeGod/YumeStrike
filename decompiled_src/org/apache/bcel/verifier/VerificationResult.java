package org.apache.bcel.verifier;

public class VerificationResult {
   public static final int VERIFIED_NOTYET = 0;
   public static final int VERIFIED_OK = 1;
   public static final int VERIFIED_REJECTED = 2;
   private static final String VERIFIED_NOTYET_MSG = "Not yet verified.";
   private static final String VERIFIED_OK_MSG = "Passed verification.";
   public static final VerificationResult VR_NOTYET = new VerificationResult(0, "Not yet verified.");
   public static final VerificationResult VR_OK = new VerificationResult(1, "Passed verification.");
   private int numeric;
   private String detailMessage;

   private VerificationResult() {
   }

   public VerificationResult(int status, String message) {
      this.numeric = status;
      this.detailMessage = message;
   }

   public int getStatus() {
      return this.numeric;
   }

   public String getMessage() {
      return this.detailMessage;
   }

   public boolean equals(Object o) {
      if (!(o instanceof VerificationResult)) {
         return false;
      } else {
         VerificationResult other = (VerificationResult)o;
         return other.numeric == this.numeric && other.detailMessage.equals(this.detailMessage);
      }
   }

   public String toString() {
      String ret = "";
      if (this.numeric == 0) {
         ret = "VERIFIED_NOTYET";
      }

      if (this.numeric == 1) {
         ret = "VERIFIED_OK";
      }

      if (this.numeric == 2) {
         ret = "VERIFIED_REJECTED";
      }

      ret = ret + "\n" + this.detailMessage + "\n";
      return ret;
   }
}
