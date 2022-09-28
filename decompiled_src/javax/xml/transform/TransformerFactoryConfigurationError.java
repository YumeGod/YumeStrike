package javax.xml.transform;

public class TransformerFactoryConfigurationError extends Error {
   private Exception exception;

   public TransformerFactoryConfigurationError() {
      this.exception = null;
   }

   public TransformerFactoryConfigurationError(String var1) {
      super(var1);
      this.exception = null;
   }

   public TransformerFactoryConfigurationError(Exception var1) {
      super(var1.toString());
      this.exception = var1;
   }

   public TransformerFactoryConfigurationError(Exception var1, String var2) {
      super(var2);
      this.exception = var1;
   }

   public String getMessage() {
      String var1 = super.getMessage();
      return var1 == null && this.exception != null ? this.exception.getMessage() : var1;
   }

   public Exception getException() {
      return this.exception;
   }
}
