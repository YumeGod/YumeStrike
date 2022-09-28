package org.apache.xerces.xni.parser;

public interface XMLComponent {
   void reset(XMLComponentManager var1) throws XMLConfigurationException;

   String[] getRecognizedFeatures();

   void setFeature(String var1, boolean var2) throws XMLConfigurationException;

   String[] getRecognizedProperties();

   void setProperty(String var1, Object var2) throws XMLConfigurationException;

   Boolean getFeatureDefault(String var1);

   Object getPropertyDefault(String var1);
}
