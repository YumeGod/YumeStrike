package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

class XSDElementTraverser extends XSDAbstractTraverser {
   protected final XSElementDecl fTempElementDecl = new XSElementDecl();
   boolean fDeferTraversingLocalElements;

   XSDElementTraverser(XSDHandler var1, XSAttributeChecker var2) {
      super(var1, var2);
   }

   XSParticleDecl traverseLocal(Element var1, XSDocumentInfo var2, SchemaGrammar var3, int var4, XSObject var5) {
      XSParticleDecl var6 = null;
      if (super.fSchemaHandler.fDeclPool != null) {
         var6 = super.fSchemaHandler.fDeclPool.getParticleDecl();
      } else {
         var6 = new XSParticleDecl();
      }

      if (this.fDeferTraversingLocalElements) {
         var6.fType = 1;
         Attr var7 = var1.getAttributeNode(SchemaSymbols.ATT_MINOCCURS);
         if (var7 != null) {
            String var8 = var7.getValue();

            try {
               int var9 = Integer.parseInt(var8.trim());
               if (var9 >= 0) {
                  var6.fMinOccurs = var9;
               }
            } catch (NumberFormatException var10) {
            }
         }

         super.fSchemaHandler.fillInLocalElemInfo(var1, var2, var4, var5, var6);
      } else {
         this.traverseLocal(var6, var1, var2, var3, var4, var5, (String[])null);
         if (var6.fType == 0) {
            var6 = null;
         }
      }

      return var6;
   }

   protected void traverseLocal(XSParticleDecl var1, Element var2, XSDocumentInfo var3, SchemaGrammar var4, int var5, XSObject var6, String[] var7) {
      if (var7 != null) {
         var3.fNamespaceSupport.setEffectiveContext(var7);
      }

      Object[] var8 = super.fAttrChecker.checkAttributes(var2, false, var3);
      QName var9 = (QName)var8[XSAttributeChecker.ATTIDX_REF];
      XInt var10 = (XInt)var8[XSAttributeChecker.ATTIDX_MINOCCURS];
      XInt var11 = (XInt)var8[XSAttributeChecker.ATTIDX_MAXOCCURS];
      XSElementDecl var12 = null;
      if (var2.getAttributeNode(SchemaSymbols.ATT_REF) != null) {
         if (var9 != null) {
            var12 = (XSElementDecl)super.fSchemaHandler.getGlobalDecl(var3, 3, var9, var2);
            Element var13 = DOMUtil.getFirstChildElement(var2);
            if (var13 != null && DOMUtil.getLocalName(var13).equals(SchemaSymbols.ELT_ANNOTATION)) {
               this.traverseAnnotationDecl(var13, var8, false, var3);
               var13 = DOMUtil.getNextSiblingElement(var13);
            }

            if (var13 != null) {
               this.reportSchemaError("src-element.2.2", new Object[]{var9.rawname, DOMUtil.getLocalName(var13)}, var13);
            }
         } else {
            var12 = null;
         }
      } else {
         var12 = this.traverseNamedElement(var2, var8, var3, var4, false, var6);
      }

      var1.fMinOccurs = var10.intValue();
      var1.fMaxOccurs = var11.intValue();
      if (var12 != null) {
         var1.fType = 1;
         var1.fValue = var12;
      } else {
         var1.fType = 0;
      }

      Long var14 = (Long)var8[XSAttributeChecker.ATTIDX_FROMDEFAULT];
      this.checkOccurrences(var1, SchemaSymbols.ELT_ELEMENT, (Element)var2.getParentNode(), var5, var14);
      super.fAttrChecker.returnAttrArray(var8, var3);
   }

   XSElementDecl traverseGlobal(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, true, var2);
      XSElementDecl var5 = this.traverseNamedElement(var1, var4, var2, var3, true, (XSObject)null);
      super.fAttrChecker.returnAttrArray(var4, var2);
      return var5;
   }

   XSElementDecl traverseNamedElement(Element var1, Object[] var2, XSDocumentInfo var3, SchemaGrammar var4, boolean var5, XSObject var6) {
      Boolean var7 = (Boolean)var2[XSAttributeChecker.ATTIDX_ABSTRACT];
      XInt var8 = (XInt)var2[XSAttributeChecker.ATTIDX_BLOCK];
      String var9 = (String)var2[XSAttributeChecker.ATTIDX_DEFAULT];
      XInt var10 = (XInt)var2[XSAttributeChecker.ATTIDX_FINAL];
      String var11 = (String)var2[XSAttributeChecker.ATTIDX_FIXED];
      XInt var12 = (XInt)var2[XSAttributeChecker.ATTIDX_FORM];
      String var13 = (String)var2[XSAttributeChecker.ATTIDX_NAME];
      Boolean var14 = (Boolean)var2[XSAttributeChecker.ATTIDX_NILLABLE];
      QName var15 = (QName)var2[XSAttributeChecker.ATTIDX_SUBSGROUP];
      QName var16 = (QName)var2[XSAttributeChecker.ATTIDX_TYPE];
      XSElementDecl var17 = null;
      if (super.fSchemaHandler.fDeclPool != null) {
         var17 = super.fSchemaHandler.fDeclPool.getElementDecl();
      } else {
         var17 = new XSElementDecl();
      }

      if (var13 != null) {
         var17.fName = super.fSymbolTable.addSymbol(var13);
      }

      if (var5) {
         var17.fTargetNamespace = var3.fTargetNamespace;
         var17.setIsGlobal();
      } else {
         if (var6 instanceof XSComplexTypeDecl) {
            var17.setIsLocal((XSComplexTypeDecl)var6);
         }

         if (var12 != null) {
            if (var12.intValue() == 1) {
               var17.fTargetNamespace = var3.fTargetNamespace;
            } else {
               var17.fTargetNamespace = null;
            }
         } else if (var3.fAreLocalElementsQualified) {
            var17.fTargetNamespace = var3.fTargetNamespace;
         } else {
            var17.fTargetNamespace = null;
         }
      }

      var17.fBlock = var8 == null ? var3.fBlockDefault : var8.shortValue();
      var17.fFinal = var10 == null ? var3.fFinalDefault : var10.shortValue();
      var17.fBlock = (short)(var17.fBlock & 7);
      var17.fFinal = (short)(var17.fFinal & 3);
      if (var14) {
         var17.setIsNillable();
      }

      if (var7 != null && var7) {
         var17.setIsAbstract();
      }

      if (var11 != null) {
         var17.fDefault = new ValidatedInfo();
         var17.fDefault.normalizedValue = var11;
         var17.setConstraintType((short)2);
      } else if (var9 != null) {
         var17.fDefault = new ValidatedInfo();
         var17.fDefault.normalizedValue = var9;
         var17.setConstraintType((short)1);
      } else {
         var17.setConstraintType((short)0);
      }

      if (var15 != null) {
         var17.fSubGroup = (XSElementDecl)super.fSchemaHandler.getGlobalDecl(var3, 3, var15, var1);
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

      var17.fAnnotation = var19;
      Object var23 = null;
      boolean var21 = false;
      String var22;
      if (var18 != null) {
         var22 = DOMUtil.getLocalName(var18);
         if (var22.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
            var23 = super.fSchemaHandler.fComplexTypeTraverser.traverseLocal(var18, var3, var4);
            var21 = true;
            var18 = DOMUtil.getNextSiblingElement(var18);
         } else if (var22.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
            var23 = super.fSchemaHandler.fSimpleTypeTraverser.traverseLocal(var18, var3, var4);
            var21 = true;
            var18 = DOMUtil.getNextSiblingElement(var18);
         }
      }

      if (var23 == null && var16 != null) {
         var23 = (XSTypeDefinition)super.fSchemaHandler.getGlobalDecl(var3, 7, var16, var1);
      }

      if (var23 == null && var17.fSubGroup != null) {
         var23 = var17.fSubGroup.fType;
      }

      if (var23 == null) {
         var23 = SchemaGrammar.fAnyType;
      }

      var17.fType = (XSTypeDefinition)var23;
      if (var18 != null) {
         var22 = DOMUtil.getLocalName(var18);

         while(var18 != null && (var22.equals(SchemaSymbols.ELT_KEY) || var22.equals(SchemaSymbols.ELT_KEYREF) || var22.equals(SchemaSymbols.ELT_UNIQUE))) {
            if (!var22.equals(SchemaSymbols.ELT_KEY) && !var22.equals(SchemaSymbols.ELT_UNIQUE)) {
               if (var22.equals(SchemaSymbols.ELT_KEYREF)) {
                  super.fSchemaHandler.storeKeyRef(var18, var3, var17);
               }
            } else {
               DOMUtil.setHidden(var18);
               super.fSchemaHandler.fUniqueOrKeyTraverser.traverse(var18, var17, var3, var4);
               if (DOMUtil.getAttrValue(var18, SchemaSymbols.ATT_NAME).length() != 0) {
                  super.fSchemaHandler.checkForDuplicateNames(var3.fTargetNamespace == null ? "," + DOMUtil.getAttrValue(var18, SchemaSymbols.ATT_NAME) : var3.fTargetNamespace + "," + DOMUtil.getAttrValue(var18, SchemaSymbols.ATT_NAME), super.fSchemaHandler.getIDRegistry(), super.fSchemaHandler.getIDRegistry_sub(), var18, var3);
               }
            }

            var18 = DOMUtil.getNextSiblingElement(var18);
            if (var18 != null) {
               var22 = DOMUtil.getLocalName(var18);
            }
         }
      }

      if (var5 && var13 != null) {
         var4.addGlobalElementDecl(var17);
      }

      if (var13 == null) {
         if (var5) {
            this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_ELEMENT, SchemaSymbols.ATT_NAME}, var1);
         } else {
            this.reportSchemaError("src-element.2.1", (Object[])null, var1);
         }

         var13 = "(no name)";
      }

      if (var18 != null) {
         this.reportSchemaError("s4s-elt-must-match.1", new Object[]{var13, "(annotation?, (simpleType | complexType)?, (unique | key | keyref)*))", DOMUtil.getLocalName(var18)}, var18);
      }

      if (var9 != null && var11 != null) {
         this.reportSchemaError("src-element.1", new Object[]{var13}, var1);
      }

      if (var21 && var16 != null) {
         this.reportSchemaError("src-element.3", new Object[]{var13}, var1);
      }

      this.checkNotationType(var13, (XSTypeDefinition)var23, var1);
      if (var17.fDefault != null) {
         super.fValidationState.setNamespaceSupport(var3.fNamespaceSupport);
         if (XSConstraints.ElementDefaultValidImmediate(var17.fType, var17.fDefault.normalizedValue, super.fValidationState, var17.fDefault) == null) {
            this.reportSchemaError("e-props-correct.2", new Object[]{var13, var17.fDefault.normalizedValue}, var1);
            var17.setConstraintType((short)0);
         }
      }

      if (var17.fSubGroup != null && !XSConstraints.checkTypeDerivationOk(var17.fType, var17.fSubGroup.fType, var17.fSubGroup.fFinal)) {
         this.reportSchemaError("e-props-correct.4", new Object[]{var13, var15.prefix + ":" + var15.localpart}, var1);
      }

      if (var17.fDefault != null && (((XSTypeDefinition)var23).getTypeCategory() == 16 && ((XSSimpleType)var23).isIDType() || ((XSTypeDefinition)var23).getTypeCategory() == 15 && ((XSComplexTypeDecl)var23).containsTypeID())) {
         this.reportSchemaError("e-props-correct.5", new Object[]{var17.fName}, var1);
      }

      return var17.fName == null ? null : var17;
   }

   void reset(SymbolTable var1, boolean var2) {
      super.reset(var1, var2);
      this.fDeferTraversingLocalElements = true;
   }
}
