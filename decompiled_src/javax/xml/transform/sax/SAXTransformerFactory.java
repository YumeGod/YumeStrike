package javax.xml.transform.sax;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import org.xml.sax.XMLFilter;

public abstract class SAXTransformerFactory extends TransformerFactory {
   public static final String FEATURE = "http://javax.xml.transform.sax.SAXTransformerFactory/feature";
   public static final String FEATURE_XMLFILTER = "http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter";

   protected SAXTransformerFactory() {
   }

   public abstract TransformerHandler newTransformerHandler(Source var1) throws TransformerConfigurationException;

   public abstract TransformerHandler newTransformerHandler(Templates var1) throws TransformerConfigurationException;

   public abstract TransformerHandler newTransformerHandler() throws TransformerConfigurationException;

   public abstract TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException;

   public abstract XMLFilter newXMLFilter(Source var1) throws TransformerConfigurationException;

   public abstract XMLFilter newXMLFilter(Templates var1) throws TransformerConfigurationException;
}
