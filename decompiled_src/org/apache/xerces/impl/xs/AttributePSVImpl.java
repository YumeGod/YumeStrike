package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

public class AttributePSVImpl implements AttributePSVI {
   protected XSAttributeDeclaration fDeclaration = null;
   protected XSTypeDefinition fTypeDecl = null;
   protected boolean fSpecified = false;
   protected String fNormalizedValue = null;
   protected Object fActualValue = null;
   protected short fActualValueType = 45;
   protected ShortList fItemValueTypes = null;
   protected XSSimpleTypeDefinition fMemberType = null;
   protected short fValidationAttempted = 0;
   protected short fValidity = 0;
   protected String[] fErrorCodes = null;
   protected String fValidationContext = null;

   public String getSchemaDefault() {
      return this.fDeclaration == null ? null : this.fDeclaration.getConstraintValue();
   }

   public String getSchemaNormalizedValue() {
      return this.fNormalizedValue;
   }

   public boolean getIsSchemaSpecified() {
      return this.fSpecified;
   }

   public short getValidationAttempted() {
      return this.fValidationAttempted;
   }

   public short getValidity() {
      return this.fValidity;
   }

   public StringList getErrorCodes() {
      return this.fErrorCodes == null ? null : new StringListImpl(this.fErrorCodes, this.fErrorCodes.length);
   }

   public String getValidationContext() {
      return this.fValidationContext;
   }

   public XSTypeDefinition getTypeDefinition() {
      return this.fTypeDecl;
   }

   public XSSimpleTypeDefinition getMemberTypeDefinition() {
      return this.fMemberType;
   }

   public XSAttributeDeclaration getAttributeDeclaration() {
      return this.fDeclaration;
   }

   public Object getActualNormalizedValue() {
      return this.fActualValue;
   }

   public short getActualNormalizedValueType() {
      return this.fActualValueType;
   }

   public ShortList getItemValueTypes() {
      return this.fItemValueTypes;
   }

   public void reset() {
      this.fNormalizedValue = null;
      this.fActualValue = null;
      this.fActualValueType = 45;
      this.fItemValueTypes = null;
      this.fDeclaration = null;
      this.fTypeDecl = null;
      this.fSpecified = false;
      this.fMemberType = null;
      this.fValidationAttempted = 0;
      this.fValidity = 0;
      this.fErrorCodes = null;
      this.fValidationContext = null;
   }
}
