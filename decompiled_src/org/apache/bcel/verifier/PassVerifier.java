package org.apache.bcel.verifier;

import java.util.ArrayList;

public abstract class PassVerifier {
   private ArrayList messages = new ArrayList();
   private VerificationResult verificationResult = null;

   public VerificationResult verify() {
      if (this.verificationResult == null) {
         this.verificationResult = this.do_verify();
      }

      return this.verificationResult;
   }

   public abstract VerificationResult do_verify();

   public void addMessage(String message) {
      this.messages.add(message);
   }

   public String[] getMessages() {
      this.verify();
      String[] ret = new String[this.messages.size()];

      for(int i = 0; i < this.messages.size(); ++i) {
         ret[i] = (String)this.messages.get(i);
      }

      return ret;
   }
}
