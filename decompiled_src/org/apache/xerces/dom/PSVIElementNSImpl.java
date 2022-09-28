package org.apache.xerces.dom;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNotationDeclaration;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

public class PSVIElementNSImpl extends ElementNSImpl implements ElementPSVI {
   static final long serialVersionUID = 6815489624636016068L;
   protected XSElementDeclaration fDeclaration = null;
   protected XSTypeDefinition fTypeDecl = null;
   protected boolean fNil = false;
   protected boolean fSpecified = true;
   protected String fNormalizedValue = null;
   protected Object fActualValue = null;
   protected short fActualValueType = 45;
   protected ShortList fItemValueTypes = null;
   protected XSNotationDeclaration fNotation = null;
   protected XSSimpleTypeDefinition fMemberType = null;
   protected short fValidationAttempted = 0;
   protected short fValidity = 0;
   protected StringList fErrorCodes = null;
   protected String fValidationContext = null;
   protected XSModel fSchemaInformation = null;

   public PSVIElementNSImpl(CoreDocumentImpl var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public PSVIElementNSImpl(CoreDocumentImpl var1, String var2, String var3) {
      super(var1, var2, var3);
   }

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
      return this.fErrorCodes;
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

   public XSModel getSchemaInformation() {
      return this.fSchemaInformation;
   }

   public void setPSVI(ElementPSVI var1) {
      this.fDeclaration = var1.getElementDeclaration();
      this.fNotation = var1.getNotation();
      this.fValidationContext = var1.getValidationContext();
      this.fTypeDecl = var1.getTypeDefinition();
      this.fSchemaInformation = var1.getSchemaInformation();
      this.fValidity = var1.getValidity();
      this.fValidationAttempted = var1.getValidationAttempted();
      this.fErrorCodes = var1.getErrorCodes();
      this.fNormalizedValue = var1.getSchemaNormalizedValue();
      this.fActualValue = var1.getActualNormalizedValue();
      this.fActualValueType = var1.getActualNormalizedValueType();
      this.fItemValueTypes = var1.getItemValueTypes();
      this.fMemberType = var1.getMemberTypeDefinition();
      this.fSpecified = var1.getIsSchemaSpecified();
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

   private void writeObject(ObjectOutputStream var1) throws IOException {
      throw new NotSerializableException(this.getClass().getName());
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      throw new NotSerializableException(this.getClass().getName());
   }
}
