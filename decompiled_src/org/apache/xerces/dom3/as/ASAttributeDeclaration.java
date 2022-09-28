package org.apache.xerces.dom3.as;

/** @deprecated */
public interface ASAttributeDeclaration extends ASObject {
   short VALUE_NONE = 0;
   short VALUE_DEFAULT = 1;
   short VALUE_FIXED = 2;

   ASDataType getDataType();

   void setDataType(ASDataType var1);

   String getDataValue();

   void setDataValue(String var1);

   String getEnumAttr();

   void setEnumAttr(String var1);

   ASObjectList getOwnerElements();

   void setOwnerElements(ASObjectList var1);

   short getDefaultType();

   void setDefaultType(short var1);
}
