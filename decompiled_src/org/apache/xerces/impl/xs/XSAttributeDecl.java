package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSSimpleTypeDefinition;

public class XSAttributeDecl implements XSAttributeDeclaration {
   public static final short SCOPE_ABSENT = 0;
   public static final short SCOPE_GLOBAL = 1;
   public static final short SCOPE_LOCAL = 2;
   String fName = null;
   String fTargetNamespace = null;
   XSSimpleType fType = null;
   short fConstraintType = 0;
   short fScope = 0;
   XSComplexTypeDecl fEnclosingCT = null;
   XSAnnotationImpl fAnnotation = null;
   ValidatedInfo fDefault = null;

   public void setValues(String var1, String var2, XSSimpleType var3, short var4, short var5, ValidatedInfo var6, XSComplexTypeDecl var7, XSAnnotationImpl var8) {
      this.fName = var1;
      this.fTargetNamespace = var2;
      this.fType = var3;
      this.fConstraintType = var4;
      this.fScope = var5;
      this.fDefault = var6;
      this.fEnclosingCT = var7;
      this.fAnnotation = var8;
   }

   public void reset() {
      this.fName = null;
      this.fTargetNamespace = null;
      this.fType = null;
      this.fConstraintType = 0;
      this.fScope = 0;
      this.fDefault = null;
      this.fAnnotation = null;
   }

   public short getType() {
      return 1;
   }

   public String getName() {
      return this.fName;
   }

   public String getNamespace() {
      return this.fTargetNamespace;
   }

   public XSSimpleTypeDefinition getTypeDefinition() {
      return this.fType;
   }

   public short getScope() {
      return this.fScope;
   }

   public XSComplexTypeDefinition getEnclosingCTDefinition() {
      return this.fEnclosingCT;
   }

   public short getConstraintType() {
      return this.fConstraintType;
   }

   public String getConstraintValue() {
      return this.getConstraintType() == 0 ? null : this.fDefault.stringValue();
   }

   public XSAnnotation getAnnotation() {
      return this.fAnnotation;
   }

   public ValidatedInfo getValInfo() {
      return this.fDefault;
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
