package org.apache.xerces.xs;

public interface XSParticle extends XSObject {
   int getMinOccurs();

   int getMaxOccurs();

   boolean getMaxOccursUnbounded();

   XSTerm getTerm();
}
