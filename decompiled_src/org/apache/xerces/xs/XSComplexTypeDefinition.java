package org.apache.xerces.xs;

public interface XSComplexTypeDefinition extends XSTypeDefinition {
   short CONTENTTYPE_EMPTY = 0;
   short CONTENTTYPE_SIMPLE = 1;
   short CONTENTTYPE_ELEMENT = 2;
   short CONTENTTYPE_MIXED = 3;

   short getDerivationMethod();

   boolean getAbstract();

   XSObjectList getAttributeUses();

   XSWildcard getAttributeWildcard();

   short getContentType();

   XSSimpleTypeDefinition getSimpleType();

   XSParticle getParticle();

   boolean isProhibitedSubstitution(short var1);

   short getProhibitedSubstitutions();

   XSObjectList getAnnotations();
}
