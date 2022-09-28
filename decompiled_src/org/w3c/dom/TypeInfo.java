package org.w3c.dom;

public interface TypeInfo {
   int DERIVATION_RESTRICTION = 1;
   int DERIVATION_EXTENSION = 2;
   int DERIVATION_UNION = 4;
   int DERIVATION_LIST = 8;

   String getTypeName();

   String getTypeNamespace();

   boolean isDerivedFrom(String var1, String var2, int var3);
}
