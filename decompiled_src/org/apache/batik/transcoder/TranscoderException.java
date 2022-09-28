package org.apache.batik.transcoder;

public class TranscoderException extends Exception {
   protected Exception ex;

   public TranscoderException(String var1) {
      this(var1, (Exception)null);
   }

   public TranscoderException(Exception var1) {
      this((String)null, var1);
   }

   public TranscoderException(String var1, Exception var2) {
      super(var1);
      this.ex = var2;
   }

   public String getMessage() {
      String var1 = super.getMessage();
      if (this.ex != null) {
         var1 = var1 + "\nEnclosed Exception:\n";
         var1 = var1 + this.ex.getMessage();
      }

      return var1;
   }

   public Exception getException() {
      return this.ex;
   }
}
