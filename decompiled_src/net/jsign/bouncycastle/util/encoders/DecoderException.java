package net.jsign.bouncycastle.util.encoders;

public class DecoderException extends IllegalStateException {
   private Throwable cause;

   DecoderException(String var1, Throwable var2) {
      super(var1);
      this.cause = var2;
   }

   public Throwable getCause() {
      return this.cause;
   }
}
