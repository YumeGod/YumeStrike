package org.apache.xerces.xs;

public interface XSSimpleTypeDefinition extends XSTypeDefinition {
   short VARIETY_ABSENT = 0;
   short VARIETY_ATOMIC = 1;
   short VARIETY_LIST = 2;
   short VARIETY_UNION = 3;
   short FACET_NONE = 0;
   short FACET_LENGTH = 1;
   short FACET_MINLENGTH = 2;
   short FACET_MAXLENGTH = 4;
   short FACET_PATTERN = 8;
   short FACET_WHITESPACE = 16;
   short FACET_MAXINCLUSIVE = 32;
   short FACET_MAXEXCLUSIVE = 64;
   short FACET_MINEXCLUSIVE = 128;
   short FACET_MININCLUSIVE = 256;
   short FACET_TOTALDIGITS = 512;
   short FACET_FRACTIONDIGITS = 1024;
   short FACET_ENUMERATION = 2048;
   short ORDERED_FALSE = 0;
   short ORDERED_PARTIAL = 1;
   short ORDERED_TOTAL = 2;

   short getVariety();

   XSSimpleTypeDefinition getPrimitiveType();

   short getBuiltInKind();

   XSSimpleTypeDefinition getItemType();

   XSObjectList getMemberTypes();

   short getDefinedFacets();

   boolean isDefinedFacet(short var1);

   short getFixedFacets();

   boolean isFixedFacet(short var1);

   String getLexicalFacetValue(short var1);

   StringList getLexicalEnumeration();

   StringList getLexicalPattern();

   short getOrdered();

   boolean getFinite();

   boolean getBounded();

   boolean getNumeric();

   XSObjectList getFacets();

   XSObjectList getMultiValueFacets();

   XSObjectList getAnnotations();
}
