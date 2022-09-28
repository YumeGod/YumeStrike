package org.apache.avalon.framework.configuration;

public interface Configuration {
   String getName();

   String getLocation();

   String getNamespace() throws ConfigurationException;

   Configuration getChild(String var1);

   Configuration getChild(String var1, boolean var2);

   Configuration[] getChildren();

   Configuration[] getChildren(String var1);

   String[] getAttributeNames();

   String getAttribute(String var1) throws ConfigurationException;

   int getAttributeAsInteger(String var1) throws ConfigurationException;

   long getAttributeAsLong(String var1) throws ConfigurationException;

   float getAttributeAsFloat(String var1) throws ConfigurationException;

   boolean getAttributeAsBoolean(String var1) throws ConfigurationException;

   String getValue() throws ConfigurationException;

   int getValueAsInteger() throws ConfigurationException;

   float getValueAsFloat() throws ConfigurationException;

   boolean getValueAsBoolean() throws ConfigurationException;

   long getValueAsLong() throws ConfigurationException;

   String getValue(String var1);

   int getValueAsInteger(int var1);

   long getValueAsLong(long var1);

   float getValueAsFloat(float var1);

   boolean getValueAsBoolean(boolean var1);

   String getAttribute(String var1, String var2);

   int getAttributeAsInteger(String var1, int var2);

   long getAttributeAsLong(String var1, long var2);

   float getAttributeAsFloat(String var1, float var2);

   boolean getAttributeAsBoolean(String var1, boolean var2);
}
