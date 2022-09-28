package org.apache.xerces.dom3.as;

/** @deprecated */
public interface ASElementDeclaration extends ASObject {
   short EMPTY_CONTENTTYPE = 1;
   short ANY_CONTENTTYPE = 2;
   short MIXED_CONTENTTYPE = 3;
   short ELEMENTS_CONTENTTYPE = 4;

   boolean getStrictMixedContent();

   void setStrictMixedContent(boolean var1);

   ASDataType getElementType();

   void setElementType(ASDataType var1);

   boolean getIsPCDataOnly();

   void setIsPCDataOnly(boolean var1);

   short getContentType();

   void setContentType(short var1);

   String getSystemId();

   void setSystemId(String var1);

   ASContentModel getAsCM();

   void setAsCM(ASContentModel var1);

   ASNamedObjectMap getASAttributeDecls();

   void setASAttributeDecls(ASNamedObjectMap var1);

   void addASAttributeDecl(ASAttributeDeclaration var1);

   ASAttributeDeclaration removeASAttributeDecl(ASAttributeDeclaration var1);
}
