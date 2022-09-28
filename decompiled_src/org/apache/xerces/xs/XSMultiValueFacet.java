package org.apache.xerces.xs;

public interface XSMultiValueFacet extends XSObject {
   short getFacetKind();

   StringList getLexicalFacetValues();

   XSObjectList getAnnotations();
}
