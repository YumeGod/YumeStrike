package org.apache.xerces.dom3.as;

/** @deprecated */
public interface ASEntityDeclaration extends ASObject {
   short INTERNAL_ENTITY = 1;
   short EXTERNAL_ENTITY = 2;

   short getEntityType();

   void setEntityType(short var1);

   String getEntityValue();

   void setEntityValue(String var1);

   String getSystemId();

   void setSystemId(String var1);

   String getPublicId();

   void setPublicId(String var1);
}
