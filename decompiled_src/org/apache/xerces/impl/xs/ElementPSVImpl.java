package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNotationDeclaration;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

public class ElementPSVImpl implements ElementPSVI {
   protected XSElementDeclaration fDeclaration = null;
   protected XSTypeDefinition fTypeDecl = null;
   protected boolean fNil = false;
   protected boolean fSpecified = false;
   protected String fNormalizedValue = null;
   protected Object fActualValue = null;
   protected short fActualValueType = 45;
   protected ShortList fItemValueTypes = null;
   protected XSNotationDeclaration fNotation = null;
   protected XSSimpleTypeDefinition fMemberType = null;
   protected short fValidationAttempted = 0;
   protected short fValidity = 0;
   protected String[] fErrorCodes = null;
   protected String fValidationContext = null;
   protected SchemaGrammar[] fGrammars = null;
   protected XSModel fSchemaInformation = null;

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

   public boolean getNil() {
      return this.fNil;
   }

   public XSNotationDeclaration getNotation() {
      return this.fNotation;
   }

   public XSTypeDefinition getTypeDefinition() {
      return this.fTypeDecl;
   }

   public XSSimpleTypeDefinition getMemberTypeDefinition() {
      return this.fMemberType;
   }

   public XSElementDeclaration getElementDeclaration() {
      return this.fDeclaration;
   }

   public synchronized XSModel getSchemaInformation() {
      if (this.fSchemaInformation == null && this.fGrammars != null) {
         this.fSchemaInformation = new XSModelImpl(this.fGrammars);
      }

      return this.fSchemaInformation;
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
      this.fDeclaration = null;
      this.fTypeDecl = null;
      this.fNil = false;
      this.fSpecified = false;
      this.fNotation = null;
      this.fMemberType = null;
      this.fValidationAttempted = 0;
      this.fValidity = 0;
      this.fErrorCodes = null;
      this.fValidationContext = null;
      this.fNormalizedValue = null;
      this.fActualValue = null;
      this.fActualValueType = 45;
      this.fItemValueTypes = null;
   }
}
