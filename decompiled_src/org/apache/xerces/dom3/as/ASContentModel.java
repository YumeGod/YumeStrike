package org.apache.xerces.dom3.as;

/** @deprecated */
public interface ASContentModel extends ASObject {
   int AS_UNBOUNDED = Integer.MAX_VALUE;
   short AS_SEQUENCE = 0;
   short AS_CHOICE = 1;
   short AS_ALL = 2;
   short AS_NONE = 3;

   short getListOperator();

   void setListOperator(short var1);

   int getMinOccurs();

   void setMinOccurs(int var1);

   int getMaxOccurs();

   void setMaxOccurs(int var1);

   ASObjectList getSubModels();

   void setSubModels(ASObjectList var1);

   void removesubModel(ASObject var1);

   void insertsubModel(ASObject var1) throws DOMASException;

   int appendsubModel(ASObject var1) throws DOMASException;
}
