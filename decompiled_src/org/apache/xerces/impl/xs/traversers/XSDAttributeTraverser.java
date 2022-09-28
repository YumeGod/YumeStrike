package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Element;

class XSDAttributeTraverser extends XSDAbstractTraverser {
   public XSDAttributeTraverser(XSDHandler var1, XSAttributeChecker var2) {
      super(var1, var2);
   }

   protected XSAttributeUseImpl traverseLocal(Element var1, XSDocumentInfo var2, SchemaGrammar var3, XSComplexTypeDecl var4) {
      Object[] var5 = super.fAttrChecker.checkAttributes(var1, false, var2);
      String var6 = (String)var5[XSAttributeChecker.ATTIDX_DEFAULT];
      String var7 = (String)var5[XSAttributeChecker.ATTIDX_FIXED];
      String var8 = (String)var5[XSAttributeChecker.ATTIDX_NAME];
      QName var9 = (QName)var5[XSAttributeChecker.ATTIDX_REF];
      XInt var10 = (XInt)var5[XSAttributeChecker.ATTIDX_USE];
      XSAttributeDecl var11 = null;
      if (var1.getAttributeNode(SchemaSymbols.ATT_REF) != null) {
         if (var9 != null) {
            var11 = (XSAttributeDecl)super.fSchemaHandler.getGlobalDecl(var2, 1, var9, var1);
            Element var12 = DOMUtil.getFirstChildElement(var1);
            if (var12 != null && DOMUtil.getLocalName(var12).equals(SchemaSymbols.ELT_ANNOTATION)) {
               this.traverseAnnotationDecl(var12, var5, false, var2);
               var12 = DOMUtil.getNextSiblingElement(var12);
            }

            if (var12 != null) {
               this.reportSchemaError("src-attribute.3.2", new Object[]{var9.rawname}, var12);
            }

            var8 = var9.localpart;
         } else {
            var11 = null;
         }
      } else {
         var11 = this.traverseNamedAttr(var1, var5, var2, var3, false, var4);
      }

      byte var16 = 0;
      if (var6 != null) {
         var16 = 1;
      } else if (var7 != null) {
         var16 = 2;
         var6 = var7;
         var7 = null;
      }

      XSAttributeUseImpl var13 = null;
      if (var11 != null) {
         if (super.fSchemaHandler.fDeclPool != null) {
            var13 = super.fSchemaHandler.fDeclPool.getAttributeUse();
         } else {
            var13 = new XSAttributeUseImpl();
         }

         var13.fAttrDecl = var11;
         var13.fUse = var10.shortValue();
         var13.fConstraintType = var16;
         if (var6 != null) {
            var13.fDefault = new ValidatedInfo();
            var13.fDefault.normalizedValue = var6;
         }
      }

      if (var6 != null && var7 != null) {
         this.reportSchemaError("src-attribute.1", new Object[]{var8}, var1);
      }

      if (var16 == 1 && var10 != null && var10.intValue() != 0) {
         this.reportSchemaError("src-attribute.2", new Object[]{var8}, var1);
      }

      if (var6 != null && var13 != null) {
         super.fValidationState.setNamespaceSupport(var2.fNamespaceSupport);

         try {
            this.checkDefaultValid(var13);
         } catch (InvalidDatatypeValueException var15) {
            this.reportSchemaError(var15.getKey(), var15.getArgs(), var1);
            this.reportSchemaError("a-props-correct.2", new Object[]{var8, var6}, var1);
         }

         if (((XSSimpleType)var11.getTypeDefinition()).isIDType()) {
            this.reportSchemaError("a-props-correct.3", new Object[]{var8}, var1);
         }

         if (var13.fAttrDecl.getConstraintType() == 2 && var13.fConstraintType != 0 && (var13.fConstraintType != 2 || !var13.fAttrDecl.getValInfo().actualValue.equals(var13.fDefault.actualValue))) {
            this.reportSchemaError("au-props-correct.2", new Object[]{var8, var13.fAttrDecl.getValInfo().stringValue()}, var1);
         }
      }

      super.fAttrChecker.returnAttrArray(var5, var2);
      return var13;
   }

   protected XSAttributeDecl traverseGlobal(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, true, var2);
      XSAttributeDecl var5 = this.traverseNamedAttr(var1, var4, var2, var3, true, (XSComplexTypeDecl)null);
      super.fAttrChecker.returnAttrArray(var4, var2);
      return var5;
   }

   XSAttributeDecl traverseNamedAttr(Element var1, Object[] var2, XSDocumentInfo var3, SchemaGrammar var4, boolean var5, XSComplexTypeDecl var6) {
      String var7 = (String)var2[XSAttributeChecker.ATTIDX_DEFAULT];
      String var8 = (String)var2[XSAttributeChecker.ATTIDX_FIXED];
      XInt var9 = (XInt)var2[XSAttributeChecker.ATTIDX_FORM];
      String var10 = (String)var2[XSAttributeChecker.ATTIDX_NAME];
      QName var11 = (QName)var2[XSAttributeChecker.ATTIDX_TYPE];
      XSAttributeDecl var12 = null;
      if (super.fSchemaHandler.fDeclPool != null) {
         var12 = super.fSchemaHandler.fDeclPool.getAttributeDecl();
      } else {
         var12 = new XSAttributeDecl();
      }

      if (var10 != null) {
         var10 = super.fSymbolTable.addSymbol(var10);
      }

      String var13 = null;
      XSComplexTypeDecl var14 = null;
      byte var15 = 0;
      if (var5) {
         var13 = var3.fTargetNamespace;
         var15 = 1;
      } else {
         if (var6 != null) {
            var14 = var6;
            var15 = 2;
         }

         if (var9 != null) {
            if (var9.intValue() == 1) {
               var13 = var3.fTargetNamespace;
            }
         } else if (var3.fAreLocalAttributesQualified) {
            var13 = var3.fTargetNamespace;
         }
      }

      ValidatedInfo var16 = null;
      byte var17 = 0;
      if (var5) {
         if (var8 != null) {
            var16 = new ValidatedInfo();
            var16.normalizedValue = var8;
            var17 = 2;
         } else if (var7 != null) {
            var16 = new ValidatedInfo();
            var16.normalizedValue = var7;
            var17 = 1;
         }
      }

      Element var18 = DOMUtil.getFirstChildElement(var1);
      XSAnnotationImpl var19 = null;
      if (var18 != null && DOMUtil.getLocalName(var18).equals(SchemaSymbols.ELT_ANNOTATION)) {
         var19 = this.traverseAnnotationDecl(var18, var2, false, var3);
         var18 = DOMUtil.getNextSiblingElement(var18);
      } else {
         String var20 = DOMUtil.getSyntheticAnnotation(var1);
         if (var20 != null) {
            var19 = this.traverseSyntheticAnnotation(var1, var20, var2, false, var3);
         }
      }

      XSSimpleType var24 = null;
      boolean var21 = false;
      if (var18 != null) {
         String var22 = DOMUtil.getLocalName(var18);
         if (var22.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
            var24 = super.fSchemaHandler.fSimpleTypeTraverser.traverseLocal(var18, var3, var4);
            var21 = true;
            var18 = DOMUtil.getNextSiblingElement(var18);
         }
      }

      if (var24 == null && var11 != null) {
         XSTypeDefinition var25 = (XSTypeDefinition)super.fSchemaHandler.getGlobalDecl(var3, 7, var11, var1);
         if (var25 != null && var25.getTypeCategory() == 16) {
            var24 = (XSSimpleType)var25;
         } else {
            this.reportSchemaError("src-resolve", new Object[]{var11.rawname, "simpleType definition"}, var1);
         }
      }

      if (var24 == null) {
         var24 = SchemaGrammar.fAnySimpleType;
      }

      var12.setValues(var10, var13, var24, var17, var15, var16, var14, var19);
      if (var5 && var10 != null) {
         var4.addGlobalAttributeDecl(var12);
      }

      if (var10 == null) {
         if (var5) {
            this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_ATTRIBUTE, SchemaSymbols.ATT_NAME}, var1);
         } else {
            this.reportSchemaError("src-attribute.3.1", (Object[])null, var1);
         }

         var10 = "(no name)";
      }

      if (var18 != null) {
         this.reportSchemaError("s4s-elt-must-match.1", new Object[]{var10, "(annotation?, (simpleType?))", DOMUtil.getLocalName(var18)}, var18);
      }

      if (var7 != null && var8 != null) {
         this.reportSchemaError("src-attribute.1", new Object[]{var10}, var1);
      }

      if (var21 && var11 != null) {
         this.reportSchemaError("src-attribute.4", new Object[]{var10}, var1);
      }

      this.checkNotationType(var10, var24, var1);
      if (var16 != null) {
         super.fValidationState.setNamespaceSupport(var3.fNamespaceSupport);

         try {
            this.checkDefaultValid(var12);
         } catch (InvalidDatatypeValueException var23) {
            this.reportSchemaError(var23.getKey(), var23.getArgs(), var1);
            this.reportSchemaError("a-props-correct.2", new Object[]{var10, var16.normalizedValue}, var1);
         }
      }

      if (var16 != null && var24.isIDType()) {
         this.reportSchemaError("a-props-correct.3", new Object[]{var10}, var1);
      }

      if (var10 != null && var10.equals(XMLSymbols.PREFIX_XMLNS)) {
         this.reportSchemaError("no-xmlns", (Object[])null, var1);
      }

      if (var13 != null && var13.equals(SchemaSymbols.URI_XSI)) {
         this.reportSchemaError("no-xsi", new Object[]{SchemaSymbols.URI_XSI}, var1);
      }

      return var12.getName() == null ? null : var12;
   }

   void checkDefaultValid(XSAttributeDecl var1) throws InvalidDatatypeValueException {
      ((XSSimpleType)var1.getTypeDefinition()).validate((String)var1.getValInfo().normalizedValue, super.fValidationState, var1.getValInfo());
      ((XSSimpleType)var1.getTypeDefinition()).validate((String)var1.getValInfo().stringValue(), super.fValidationState, var1.getValInfo());
   }

   void checkDefaultValid(XSAttributeUseImpl var1) throws InvalidDatatypeValueException {
      ((XSSimpleType)var1.fAttrDecl.getTypeDefinition()).validate((String)var1.fDefault.normalizedValue, super.fValidationState, var1.fDefault);
      ((XSSimpleType)var1.fAttrDecl.getTypeDefinition()).validate((String)var1.fDefault.stringValue(), super.fValidationState, var1.fDefault);
   }
}
