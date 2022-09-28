package javax.xml.parsers;

import javax.xml.validation.Schema;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public abstract class SAXParserFactory {
   private boolean validating = false;
   private boolean namespaceAware = false;

   protected SAXParserFactory() {
   }

   public static SAXParserFactory newInstance() {
      try {
         return (SAXParserFactory)FactoryFinder.find("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
      } catch (FactoryFinder.ConfigurationError var1) {
         throw new FactoryConfigurationError(var1.getException(), var1.getMessage());
      }
   }

   public abstract SAXParser newSAXParser() throws ParserConfigurationException, SAXException;

   public void setNamespaceAware(boolean var1) {
      this.namespaceAware = var1;
   }

   public void setValidating(boolean var1) {
      this.validating = var1;
   }

   public boolean isNamespaceAware() {
      return this.namespaceAware;
   }

   public boolean isValidating() {
      return this.validating;
   }

   public abstract void setFeature(String var1, boolean var2) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException;

   public abstract boolean getFeature(String var1) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException;

   public Schema getSchema() {
      throw new UnsupportedOperationException("This parser does not support specification \"" + this.getClass().getPackage().getSpecificationTitle() + "\" version \"" + this.getClass().getPackage().getSpecificationVersion() + "\"");
   }

   public void setSchema(Schema var1) {
      throw new UnsupportedOperationException("This parser does not support specification \"" + this.getClass().getPackage().getSpecificationTitle() + "\" version \"" + this.getClass().getPackage().getSpecificationVersion() + "\"");
   }

   public void setXIncludeAware(boolean var1) {
      throw new UnsupportedOperationException("This parser does not support specification \"" + this.getClass().getPackage().getSpecificationTitle() + "\" version \"" + this.getClass().getPackage().getSpecificationVersion() + "\"");
   }

   public boolean isXIncludeAware() {
      throw new UnsupportedOperationException("This parser does not support specification \"" + this.getClass().getPackage().getSpecificationTitle() + "\" version \"" + this.getClass().getPackage().getSpecificationVersion() + "\"");
   }
}
