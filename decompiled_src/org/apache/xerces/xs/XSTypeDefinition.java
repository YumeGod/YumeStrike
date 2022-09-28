package org.apache.xerces.xs;

public interface XSTypeDefinition extends XSObject {
   short COMPLEX_TYPE = 15;
   short SIMPLE_TYPE = 16;

   short getTypeCategory();

   XSTypeDefinition getBaseType();

   boolean isFinal(short var1);

   short getFinal();

   boolean getAnonymous();

   boolean derivedFromType(XSTypeDefinition var1, short var2);

   boolean derivedFrom(String var1, String var2, short var3);
}
