package javax.xml.transform;

public abstract class TransformerFactory {
   protected TransformerFactory() {
   }

   public static TransformerFactory newInstance() throws TransformerFactoryConfigurationError {
      try {
         return (TransformerFactory)FactoryFinder.find("javax.xml.transform.TransformerFactory", "org.apache.xalan.processor.TransformerFactoryImpl");
      } catch (FactoryFinder.ConfigurationError var1) {
         throw new TransformerFactoryConfigurationError(var1.getException(), var1.getMessage());
      }
   }

   public abstract Transformer newTransformer(Source var1) throws TransformerConfigurationException;

   public abstract Transformer newTransformer() throws TransformerConfigurationException;

   public abstract Templates newTemplates(Source var1) throws TransformerConfigurationException;

   public abstract Source getAssociatedStylesheet(Source var1, String var2, String var3, String var4) throws TransformerConfigurationException;

   public abstract void setURIResolver(URIResolver var1);

   public abstract URIResolver getURIResolver();

   public abstract void setFeature(String var1, boolean var2) throws TransformerConfigurationException;

   public abstract boolean getFeature(String var1);

   public abstract void setAttribute(String var1, Object var2);

   public abstract Object getAttribute(String var1);

   public abstract void setErrorListener(ErrorListener var1);

   public abstract ErrorListener getErrorListener();
}
