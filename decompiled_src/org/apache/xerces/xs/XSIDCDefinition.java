package org.apache.xerces.xs;

public interface XSIDCDefinition extends XSObject {
   short IC_KEY = 1;
   short IC_KEYREF = 2;
   short IC_UNIQUE = 3;

   short getCategory();

   String getSelectorStr();

   StringList getFieldStrs();

   XSIDCDefinition getRefKey();

   XSObjectList getAnnotations();
}
