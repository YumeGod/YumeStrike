package org.apache.xerces.xni.grammars;

import java.io.IOException;
import java.util.Locale;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;

public interface XMLGrammarLoader {
   String[] getRecognizedFeatures();

   boolean getFeature(String var1) throws XMLConfigurationException;

   void setFeature(String var1, boolean var2) throws XMLConfigurationException;

   String[] getRecognizedProperties();

   Object getProperty(String var1) throws XMLConfigurationException;

   void setProperty(String var1, Object var2) throws XMLConfigurationException;

   void setLocale(Locale var1);

   Locale getLocale();

   void setErrorHandler(XMLErrorHandler var1);

   XMLErrorHandler getErrorHandler();

   void setEntityResolver(XMLEntityResolver var1);

   XMLEntityResolver getEntityResolver();

   Grammar loadGrammar(XMLInputSource var1) throws IOException, XNIException;
}
