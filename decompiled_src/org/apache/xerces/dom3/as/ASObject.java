package org.apache.xerces.dom3.as;

/** @deprecated */
public interface ASObject {
   short AS_ELEMENT_DECLARATION = 1;
   short AS_ATTRIBUTE_DECLARATION = 2;
   short AS_NOTATION_DECLARATION = 3;
   short AS_ENTITY_DECLARATION = 4;
   short AS_CONTENTMODEL = 5;
   short AS_MODEL = 6;

   short getAsNodeType();

   ASModel getOwnerASModel();

   void setOwnerASModel(ASModel var1);

   String getNodeName();

   void setNodeName(String var1);

   String getPrefix();

   void setPrefix(String var1);

   String getLocalName();

   void setLocalName(String var1);

   String getNamespaceURI();

   void setNamespaceURI(String var1);

   ASObject cloneASObject(boolean var1);
}
