package org.apache.xerces.dom;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

public class PSVIAttrNSImpl extends AttrNSImpl implements AttributePSVI {
   static final long serialVersionUID = -3241738699421018889L;
   protected XSAttributeDeclaration fDeclaration = null;
   protected XSTypeDefinition fTypeDecl = null;
   protected boolean fSpecified = true;
   protected String fNormalizedValue = null;
   protected Object fActualValue = null;
   protected short fActualValueType = 45;
   protected ShortList fItemValueTypes = null;
   protected XSSimpleTypeDefinition fMemberType = null;
   protected short fValidationAttempted = 0;
   protected short fValidity = 0;
   protected StringList fErrorCodes = null;
   protected String fValidationContext = null;

   public PSVIAttrNSImpl(CoreDocumentImpl var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public PSVIAttrNSImpl(CoreDocumentImpl var1, String var2, String var3) {
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

   public XSTypeDefinition getTypeDefinition() {
      return this.fTypeDecl;
   }

   public XSSimpleTypeDefinition getMemberTypeDefinition() {
      return this.fMemberType;
   }

   public XSAttributeDeclaration getAttributeDeclaration() {
      return this.fDeclaration;
   }

   public void setPSVI(AttributePSVI var1) {
      this.fDeclaration = var1.getAttributeDeclaration();
      this.fValidationContext = var1.getValidationContext();
      this.fValidity = var1.getValidity();
      this.fValidationAttempted = var1.getValidationAttempted();
      this.fErrorCodes = var1.getErrorCodes();
      this.fNormalizedValue = var1.getSchemaNormalizedValue();
      this.fActualValue = var1.getActualNormalizedValue();
      this.fActualValueType = var1.getActualNormalizedValueType();
      this.fItemValueTypes = var1.getItemValueTypes();
      this.fTypeDecl = var1.getTypeDefinition();
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
