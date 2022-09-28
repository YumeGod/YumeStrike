package org.apache.xerces.dom3.as;

import org.w3c.dom.DOMException;

/** @deprecated */
public interface ASModel extends ASObject {
   boolean getIsNamespaceAware();

   short getUsageLocation();

   String getAsLocation();

   void setAsLocation(String var1);

   String getAsHint();

   void setAsHint(String var1);

   ASNamedObjectMap getElementDeclarations();

   ASNamedObjectMap getAttributeDeclarations();

   ASNamedObjectMap getNotationDeclarations();

   ASNamedObjectMap getEntityDeclarations();

   ASNamedObjectMap getContentModelDeclarations();

   void addASModel(ASModel var1);

   ASObjectList getASModels();

   void removeAS(ASModel var1);

   boolean validate();

   ASElementDeclaration createASElementDeclaration(String var1, String var2) throws DOMException;

   ASAttributeDeclaration createASAttributeDeclaration(String var1, String var2) throws DOMException;

   ASNotationDeclaration createASNotationDeclaration(String var1, String var2, String var3, String var4) throws DOMException;

   ASEntityDeclaration createASEntityDeclaration(String var1) throws DOMException;

   ASContentModel createASContentModel(int var1, int var2, short var3) throws DOMASException;
}
