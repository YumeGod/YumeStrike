package org.apache.xerces.xni;

public interface XMLAttributes {
   int addAttribute(QName var1, String var2, String var3);

   void removeAllAttributes();

   void removeAttributeAt(int var1);

   int getLength();

   int getIndex(String var1);

   int getIndex(String var1, String var2);

   void setName(int var1, QName var2);

   void getName(int var1, QName var2);

   String getPrefix(int var1);

   String getURI(int var1);

   String getLocalName(int var1);

   String getQName(int var1);

   void setType(int var1, String var2);

   String getType(int var1);

   String getType(String var1);

   String getType(String var1, String var2);

   void setValue(int var1, String var2);

   String getValue(int var1);

   String getValue(String var1);

   String getValue(String var1, String var2);

   void setNonNormalizedValue(int var1, String var2);

   String getNonNormalizedValue(int var1);

   void setSpecified(int var1, boolean var2);

   boolean isSpecified(int var1);

   Augmentations getAugmentations(int var1);

   Augmentations getAugmentations(String var1, String var2);

   Augmentations getAugmentations(String var1);

   void setAugmentations(int var1, Augmentations var2);
}
