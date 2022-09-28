package org.apache.xerces.impl.xs.traversers;

import java.util.Stack;
import java.util.Vector;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.util.SymbolTable;
import org.w3c.dom.Element;

class XSDocumentInfo {
   protected SchemaNamespaceSupport fNamespaceSupport;
   protected SchemaNamespaceSupport fNamespaceSupportRoot;
   protected Stack SchemaNamespaceSupportStack = new Stack();
   protected boolean fAreLocalAttributesQualified;
   protected boolean fAreLocalElementsQualified;
   protected short fBlockDefault;
   protected short fFinalDefault;
   String fTargetNamespace;
   protected boolean fIsChameleonSchema;
   protected Element fSchemaElement;
   Vector fImportedNS = new Vector();
   protected ValidationState fValidationContext = new ValidationState();
   SymbolTable fSymbolTable = null;
   protected XSAttributeChecker fAttrChecker;
   protected Object[] fSchemaAttrs;
   protected XSAnnotationInfo fAnnotations = null;
   private Vector fReportedTNS = null;

   XSDocumentInfo(Element var1, XSAttributeChecker var2, SymbolTable var3) throws XMLSchemaException {
      this.fSchemaElement = var1;
      this.fNamespaceSupport = new SchemaNamespaceSupport();
      this.fNamespaceSupport.reset();
      this.fIsChameleonSchema = false;
      this.fSymbolTable = var3;
      this.fAttrChecker = var2;
      if (var1 != null) {
         this.fSchemaAttrs = var2.checkAttributes(var1, true, this);
         if (this.fSchemaAttrs == null) {
            throw new XMLSchemaException((String)null, (Object[])null);
         }

         this.fAreLocalAttributesQualified = ((XInt)this.fSchemaAttrs[XSAttributeChecker.ATTIDX_AFORMDEFAULT]).intValue() == 1;
         this.fAreLocalElementsQualified = ((XInt)this.fSchemaAttrs[XSAttributeChecker.ATTIDX_EFORMDEFAULT]).intValue() == 1;
         this.fBlockDefault = ((XInt)this.fSchemaAttrs[XSAttributeChecker.ATTIDX_BLOCKDEFAULT]).shortValue();
         this.fFinalDefault = ((XInt)this.fSchemaAttrs[XSAttributeChecker.ATTIDX_FINALDEFAULT]).shortValue();
         this.fTargetNamespace = (String)this.fSchemaAttrs[XSAttributeChecker.ATTIDX_TARGETNAMESPACE];
         if (this.fTargetNamespace != null) {
            this.fTargetNamespace = var3.addSymbol(this.fTargetNamespace);
         }

         this.fNamespaceSupportRoot = new SchemaNamespaceSupport(this.fNamespaceSupport);
         this.fValidationContext.setNamespaceSupport(this.fNamespaceSupport);
         this.fValidationContext.setSymbolTable(var3);
      }

   }

   void backupNSSupport(SchemaNamespaceSupport var1) {
      this.SchemaNamespaceSupportStack.push(this.fNamespaceSupport);
      if (var1 == null) {
         var1 = this.fNamespaceSupportRoot;
      }

      this.fNamespaceSupport = new SchemaNamespaceSupport(var1);
      this.fValidationContext.setNamespaceSupport(this.fNamespaceSupport);
   }

   void restoreNSSupport() {
      this.fNamespaceSupport = (SchemaNamespaceSupport)this.SchemaNamespaceSupportStack.pop();
      this.fValidationContext.setNamespaceSupport(this.fNamespaceSupport);
   }

   public String toString() {
      return this.fTargetNamespace == null ? "no targetNamspace" : "targetNamespace is " + this.fTargetNamespace;
   }

   public void addAllowedNS(String var1) {
      this.fImportedNS.addElement(var1 == null ? "" : var1);
   }

   public boolean isAllowedNS(String var1) {
      return this.fImportedNS.contains(var1 == null ? "" : var1);
   }

   final boolean needReportTNSError(String var1) {
      if (this.fReportedTNS == null) {
         this.fReportedTNS = new Vector();
      } else if (this.fReportedTNS.contains(var1)) {
         return false;
      }

      this.fReportedTNS.addElement(var1);
      return true;
   }

   Object[] getSchemaAttrs() {
      return this.fSchemaAttrs;
   }

   void returnSchemaAttrs() {
      this.fAttrChecker.returnAttrArray(this.fSchemaAttrs, (XSDocumentInfo)null);
      this.fSchemaAttrs = null;
   }

   void addAnnotation(XSAnnotationInfo var1) {
      var1.next = this.fAnnotations;
      this.fAnnotations = var1;
   }

   XSAnnotationInfo getAnnotations() {
      return this.fAnnotations;
   }

   void removeAnnotations() {
      this.fAnnotations = null;
   }
}
