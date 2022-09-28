package org.apache.xerces.xs;

public interface XSElementDeclaration extends XSTerm {
   XSTypeDefinition getTypeDefinition();

   short getScope();

   XSComplexTypeDefinition getEnclosingCTDefinition();

   short getConstraintType();

   String getConstraintValue();

   Object getActualVC() throws XSException;

   short getActualVCType() throws XSException;

   ShortList getItemValueTypes() throws XSException;

   boolean getNillable();

   XSNamedMap getIdentityConstraints();

   XSElementDeclaration getSubstitutionGroupAffiliation();

   boolean isSubstitutionGroupExclusion(short var1);

   short getSubstitutionGroupExclusions();

   boolean isDisallowedSubstitution(short var1);

   short getDisallowedSubstitutions();

   boolean getAbstract();

   XSAnnotation getAnnotation();
}
