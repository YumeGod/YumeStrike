package org.apache.xerces.xni;

public interface XMLResourceIdentifier {
   void setPublicId(String var1);

   String getPublicId();

   void setExpandedSystemId(String var1);

   String getExpandedSystemId();

   void setLiteralSystemId(String var1);

   String getLiteralSystemId();

   void setBaseSystemId(String var1);

   String getBaseSystemId();

   void setNamespace(String var1);

   String getNamespace();
}
