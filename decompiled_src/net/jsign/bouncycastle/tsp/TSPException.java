package net.jsign.bouncycastle.tsp;

public class TSPException extends Exception {
   Throwable underlyingException;

   public TSPException(String var1) {
      super(var1);
   }

   public TSPException(String var1, Throwable var2) {
      super(var1);
      this.underlyingException = var2;
   }

   public Exception getUnderlyingException() {
      return (Exception)this.underlyingException;
   }

   public Throwable getCause() {
      return this.underlyingException;
   }
}
