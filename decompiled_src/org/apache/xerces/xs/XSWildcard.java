package org.apache.xerces.xs;

public interface XSWildcard extends XSTerm {
   short NSCONSTRAINT_ANY = 1;
   short NSCONSTRAINT_NOT = 2;
   short NSCONSTRAINT_LIST = 3;
   short PC_STRICT = 1;
   short PC_SKIP = 2;
   short PC_LAX = 3;

   short getConstraintType();

   StringList getNsConstraintList();

   short getProcessContents();

   XSAnnotation getAnnotation();
}
