package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.util.XSNamedMapImpl;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSTypeDefinition;

public class XSElementDecl implements XSElementDeclaration {
   public static final short SCOPE_ABSENT = 0;
   public static final short SCOPE_GLOBAL = 1;
   public static final short SCOPE_LOCAL = 2;
   public String fName = null;
   public String fTargetNamespace = null;
   public XSTypeDefinition fType = null;
   short fMiscFlags = 0;
   public short fScope = 0;
   XSComplexTypeDecl fEnclosingCT = null;
   public short fBlock = 0;
   public short fFinal = 0;
   public XSAnnotationImpl fAnnotation = null;
   public ValidatedInfo fDefault = null;
   public XSElementDecl fSubGroup = null;
   static final int INITIAL_SIZE = 2;
   int fIDCPos = 0;
   IdentityConstraint[] fIDConstraints = new IdentityConstraint[2];
   private static final short CONSTRAINT_MASK = 3;
   private static final short NILLABLE = 4;
   private static final short ABSTRACT = 8;
   private String fDescription = null;

   public void setConstraintType(short var1) {
      this.fMiscFlags = (short)(this.fMiscFlags ^ this.fMiscFlags & 3);
      this.fMiscFlags = (short)(this.fMiscFlags | var1 & 3);
   }

   public void setIsNillable() {
      this.fMiscFlags = (short)(this.fMiscFlags | 4);
   }

   public void setIsAbstract() {
      this.fMiscFlags = (short)(this.fMiscFlags | 8);
   }

   public void setIsGlobal() {
      this.fScope = 1;
   }

   public void setIsLocal(XSComplexTypeDecl var1) {
      this.fScope = 2;
      this.fEnclosingCT = var1;
   }

   public void addIDConstraint(IdentityConstraint var1) {
      if (this.fIDCPos == this.fIDConstraints.length) {
         this.fIDConstraints = resize(this.fIDConstraints, this.fIDCPos * 2);
      }

      this.fIDConstraints[this.fIDCPos++] = var1;
   }

   public IdentityConstraint[] getIDConstraints() {
      if (this.fIDCPos == 0) {
         return null;
      } else {
         if (this.fIDCPos < this.fIDConstraints.length) {
            this.fIDConstraints = resize(this.fIDConstraints, this.fIDCPos);
         }

         return this.fIDConstraints;
      }
   }

   static final IdentityConstraint[] resize(IdentityConstraint[] var0, int var1) {
      IdentityConstraint[] var2 = new IdentityConstraint[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public String toString() {
      if (this.fDescription == null) {
         if (this.fTargetNamespace != null) {
            StringBuffer var1 = new StringBuffer(this.fTargetNamespace.length() + (this.fName != null ? this.fName.length() : 4) + 3);
            var1.append('"');
            var1.append(this.fTargetNamespace);
            var1.append('"');
            var1.append(':');
            var1.append(this.fName);
            this.fDescription = var1.toString();
         } else {
            this.fDescription = this.fName;
         }
      }

      return this.fDescription;
   }

   public int hashCode() {
      int var1 = this.fName.hashCode();
      if (this.fTargetNamespace != null) {
         var1 = (var1 << 16) + this.fTargetNamespace.hashCode();
      }

      return var1;
   }

   public boolean equals(Object var1) {
      return var1 == this;
   }

   public void reset() {
      this.fName = null;
      this.fTargetNamespace = null;
      this.fType = null;
      this.fMiscFlags = 0;
      this.fBlock = 0;
      this.fFinal = 0;
      this.fDefault = null;
      this.fAnnotation = null;
      this.fSubGroup = null;

      for(int var1 = 0; var1 < this.fIDCPos; ++var1) {
         this.fIDConstraints[var1] = null;
      }

      this.fIDCPos = 0;
   }

   public short getType() {
      return 2;
   }

   public String getName() {
      return this.fName;
   }

   public String getNamespace() {
      return this.fTargetNamespace;
   }

   public XSTypeDefinition getTypeDefinition() {
      return this.fType;
   }

   public short getScope() {
      return this.fScope;
   }

   public XSComplexTypeDefinition getEnclosingCTDefinition() {
      return this.fEnclosingCT;
   }

   public short getConstraintType() {
      return (short)(this.fMiscFlags & 3);
   }

   public String getConstraintValue() {
      return this.getConstraintType() == 0 ? null : this.fDefault.stringValue();
   }

   public boolean getNillable() {
      return (this.fMiscFlags & 4) != 0;
   }

   public XSNamedMap getIdentityConstraints() {
      return new XSNamedMapImpl(this.fIDConstraints, this.fIDCPos);
   }

   public XSElementDeclaration getSubstitutionGroupAffiliation() {
      return this.fSubGroup;
   }

   public boolean isSubstitutionGroupExclusion(short var1) {
      return (this.fFinal & var1) != 0;
   }

   public short getSubstitutionGroupExclusions() {
      return this.fFinal;
   }

   public boolean isDisallowedSubstitution(short var1) {
      return (this.fBlock & var1) != 0;
   }

   public short getDisallowedSubstitutions() {
      return this.fBlock;
   }

   public boolean getAbstract() {
      return (this.fMiscFlags & 8) != 0;
   }

   public XSAnnotation getAnnotation() {
      return this.fAnnotation;
   }

   public XSNamespaceItem getNamespaceItem() {
      return null;
   }

   public Object getActualVC() {
      return this.getConstraintType() == 0 ? null : this.fDefault.actualValue;
   }

   public short getActualVCType() {
      return this.getConstraintType() == 0 ? 45 : this.fDefault.actualValueType;
   }

   public ShortList getItemValueTypes() {
      return this.getConstraintType() == 0 ? null : this.fDefault.itemValueTypes;
   }
}
