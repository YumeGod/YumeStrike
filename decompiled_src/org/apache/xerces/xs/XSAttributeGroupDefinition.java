package org.apache.xerces.xs;

public interface XSAttributeGroupDefinition extends XSObject {
   XSObjectList getAttributeUses();

   XSWildcard getAttributeWildcard();

   XSAnnotation getAnnotation();
}
