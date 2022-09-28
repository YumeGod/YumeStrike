package org.w3c.dom;

public interface Attr extends Node {
   String getName();

   boolean getSpecified();

   String getValue();

   void setValue(String var1) throws DOMException;

   Element getOwnerElement();

   TypeInfo getSchemaTypeInfo();

   boolean isId();
}
