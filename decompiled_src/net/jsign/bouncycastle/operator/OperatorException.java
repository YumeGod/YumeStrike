package net.jsign.bouncycastle.operator;

public class OperatorException extends Exception {
   private Throwable cause;

   public OperatorException(String var1, Throwable var2) {
      super(var1);
      this.cause = var2;
   }

   public OperatorException(String var1) {
      super(var1);
   }

   public Throwable getCause() {
      return this.cause;
   }
}
