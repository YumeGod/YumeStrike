package javax.xml.transform;

import java.util.Properties;

public abstract class Transformer {
   protected Transformer() {
   }

   public void reset() {
      throw new UnsupportedOperationException("This Transformer, \"" + this.getClass().getName() + "\", does not support the reset functionality." + "  Specification \"" + this.getClass().getPackage().getSpecificationTitle() + "\"" + " version \"" + this.getClass().getPackage().getSpecificationVersion() + "\"");
   }

   public abstract void transform(Source var1, Result var2) throws TransformerException;

   public abstract void setParameter(String var1, Object var2);

   public abstract Object getParameter(String var1);

   public abstract void clearParameters();

   public abstract void setURIResolver(URIResolver var1);

   public abstract URIResolver getURIResolver();

   public abstract void setOutputProperties(Properties var1);

   public abstract Properties getOutputProperties();

   public abstract void setOutputProperty(String var1, String var2) throws IllegalArgumentException;

   public abstract String getOutputProperty(String var1) throws IllegalArgumentException;

   public abstract void setErrorListener(ErrorListener var1) throws IllegalArgumentException;

   public abstract ErrorListener getErrorListener();
}
