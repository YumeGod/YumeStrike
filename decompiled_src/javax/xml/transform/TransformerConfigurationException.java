package javax.xml.transform;

public class TransformerConfigurationException extends TransformerException {
   public TransformerConfigurationException() {
      super("Configuration Error");
   }

   public TransformerConfigurationException(String var1) {
      super(var1);
   }

   public TransformerConfigurationException(Throwable var1) {
      super(var1);
   }

   public TransformerConfigurationException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public TransformerConfigurationException(String var1, SourceLocator var2) {
      super(var1, var2);
   }

   public TransformerConfigurationException(String var1, SourceLocator var2, Throwable var3) {
      super(var1, var2, var3);
   }
}
