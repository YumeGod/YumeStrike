package net.jsign.bouncycastle.cert;

import java.io.IOException;

public class CertIOException extends IOException {
   private Throwable cause;

   public CertIOException(String var1, Throwable var2) {
      super(var1);
      this.cause = var2;
   }

   public CertIOException(String var1) {
      super(var1);
   }

   public Throwable getCause() {
      return this.cause;
   }
}
