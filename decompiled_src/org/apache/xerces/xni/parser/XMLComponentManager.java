package org.apache.xerces.xni.parser;

public interface XMLComponentManager {
   boolean getFeature(String var1) throws XMLConfigurationException;

   Object getProperty(String var1) throws XMLConfigurationException;
}
