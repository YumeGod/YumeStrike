package org.apache.xerces.xni;

import java.util.Enumeration;

public interface NamespaceContext {
   String XML_URI = "http://www.w3.org/XML/1998/namespace".intern();
   String XMLNS_URI = "http://www.w3.org/2000/xmlns/".intern();

   void pushContext();

   void popContext();

   boolean declarePrefix(String var1, String var2);

   String getURI(String var1);

   String getPrefix(String var1);

   int getDeclaredPrefixCount();

   String getDeclaredPrefixAt(int var1);

   Enumeration getAllPrefixes();

   void reset();
}
