package org.apache.xerces.impl.xs.identity;

public interface FieldActivator {
   void startValueScopeFor(IdentityConstraint var1, int var2);

   XPathMatcher activateField(Field var1, int var2);

   void setMayMatch(Field var1, Boolean var2);

   Boolean mayMatch(Field var1);

   void endValueScopeFor(IdentityConstraint var1, int var2);
}
