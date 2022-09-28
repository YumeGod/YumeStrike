package org.apache.avalon.framework.configuration;

public interface MutableConfiguration extends Configuration {
   void setValue(String var1);

   void setValue(int var1);

   void setValue(long var1);

   void setValue(boolean var1);

   void setValue(float var1);

   void setAttribute(String var1, String var2);

   void setAttribute(String var1, int var2);

   void setAttribute(String var1, long var2);

   void setAttribute(String var1, boolean var2);

   void setAttribute(String var1, float var2);

   void addChild(Configuration var1);

   void addAll(Configuration var1);

   void addAllAttributes(Configuration var1);

   void addAllChildren(Configuration var1);

   void removeChild(Configuration var1);

   MutableConfiguration getMutableChild(String var1) throws ConfigurationException;

   MutableConfiguration getMutableChild(String var1, boolean var2) throws ConfigurationException;

   MutableConfiguration[] getMutableChildren() throws ConfigurationException;

   MutableConfiguration[] getMutableChildren(String var1) throws ConfigurationException;
}
