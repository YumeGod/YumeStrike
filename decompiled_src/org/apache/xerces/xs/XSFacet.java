package org.apache.xerces.xs;

public interface XSFacet extends XSObject {
   short getFacetKind();

   String getLexicalFacetValue();

   boolean getFixed();

   XSAnnotation getAnnotation();
}
