package org.w3c.dom;

public interface NameList {
   String getName(int var1);

   String getNamespaceURI(int var1);

   int getLength();

   boolean contains(String var1);

   boolean containsNS(String var1, String var2);
}
