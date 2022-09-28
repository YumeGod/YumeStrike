package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSNamespaceItem;

public class XSAttributeUseImpl implements XSAttributeUse {
   public XSAttributeDecl fAttrDecl = null;
   public short fUse = 0;
   public short fConstraintType = 0;
   public ValidatedInfo fDefault = null;

   public void reset() {
      this.fDefault = null;
      this.fAttrDecl = null;
      this.fUse = 0;
      this.fConstraintType = 0;
   }

   public short getType() {
      return 4;
   }

   public String getName() {
      return null;
   }

   public String getNamespace() {
      return null;
   }

   public boolean getRequired() {
      return this.fUse == 1;
   }

   public XSAttributeDeclaration getAttrDeclaration() {
      return this.fAttrDecl;
   }

   public short getConstraintType() {
      return this.fConstraintType;
   }

   public String getConstraintValue() {
      return this.getConstraintType() == 0 ? null : (this.fDefault != null && this.fDefault.actualValue != null ? this.fDefault.actualValue.toString() : null);
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
