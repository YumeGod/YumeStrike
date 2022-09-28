package org.apache.xerces.xs;

public interface XSModelGroup extends XSTerm {
   short COMPOSITOR_SEQUENCE = 1;
   short COMPOSITOR_CHOICE = 2;
   short COMPOSITOR_ALL = 3;

   short getCompositor();

   XSObjectList getParticles();

   XSAnnotation getAnnotation();
}
