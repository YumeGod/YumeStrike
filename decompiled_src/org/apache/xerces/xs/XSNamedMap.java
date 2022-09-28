package org.apache.xerces.xs;

public interface XSNamedMap {
   int getLength();

   XSObject item(int var1);

   XSObject itemByName(String var1, String var2);
}
