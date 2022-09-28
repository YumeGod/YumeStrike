package org.apache.xerces.xs;

public interface XSObject {
   short getType();

   String getName();

   String getNamespace();

   XSNamespaceItem getNamespaceItem();
}
