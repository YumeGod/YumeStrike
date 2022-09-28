package javax.xml.parsers;

public class FactoryConfigurationError extends Error {
   private Exception exception;

   public FactoryConfigurationError() {
      this.exception = null;
   }

   public FactoryConfigurationError(String var1) {
      super(var1);
      this.exception = null;
   }

   public FactoryConfigurationError(Exception var1) {
      super(var1.toString());
      this.exception = var1;
   }

   public FactoryConfigurationError(Exception var1, String var2) {
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
