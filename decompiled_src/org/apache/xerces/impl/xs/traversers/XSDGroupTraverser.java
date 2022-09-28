package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSGroupDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.w3c.dom.Element;

class XSDGroupTraverser extends XSDAbstractParticleTraverser {
   XSDGroupTraverser(XSDHandler var1, XSAttributeChecker var2) {
      super(var1, var2);
   }

   XSParticleDecl traverseLocal(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, false, var2);
      QName var5 = (QName)var4[XSAttributeChecker.ATTIDX_REF];
      XInt var6 = (XInt)var4[XSAttributeChecker.ATTIDX_MINOCCURS];
      XInt var7 = (XInt)var4[XSAttributeChecker.ATTIDX_MAXOCCURS];
      XSGroupDecl var8 = null;
      if (var5 == null) {
         this.reportSchemaError("s4s-att-must-appear", new Object[]{"group (local)", "ref"}, var1);
      } else {
         var8 = (XSGroupDecl)super.fSchemaHandler.getGlobalDecl(var2, 4, var5, var1);
      }

      Element var9 = DOMUtil.getFirstChildElement(var1);
      if (var9 != null && DOMUtil.getLocalName(var9).equals(SchemaSymbols.ELT_ANNOTATION)) {
         this.traverseAnnotationDecl(var9, var4, false, var2);
         var9 = DOMUtil.getNextSiblingElement(var9);
      }

      if (var9 != null) {
         this.reportSchemaError("s4s-elt-must-match.1", new Object[]{"group (local)", "(annotation?)", DOMUtil.getLocalName(var1)}, var1);
      }

      int var10 = var6.intValue();
      int var11 = var7.intValue();
      XSParticleDecl var12 = null;
      if (var8 != null && var8.fModelGroup != null && (var10 != 0 || var11 != 0)) {
         if (super.fSchemaHandler.fDeclPool != null) {
            var12 = super.fSchemaHandler.fDeclPool.getParticleDecl();
         } else {
            var12 = new XSParticleDecl();
         }

         var12.fType = 3;
         var12.fValue = var8.fModelGroup;
         var12.fMinOccurs = var10;
         var12.fMaxOccurs = var11;
      }

      super.fAttrChecker.returnAttrArray(var4, var2);
      return var12;
   }

   XSGroupDecl traverseGlobal(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, true, var2);
      String var5 = (String)var4[XSAttributeChecker.ATTIDX_NAME];
      if (var5 == null) {
         this.reportSchemaError("s4s-att-must-appear", new Object[]{"group (global)", "name"}, var1);
      }

      XSGroupDecl var6 = null;
      XSParticleDecl var7 = null;
      Element var8 = DOMUtil.getFirstChildElement(var1);
      XSAnnotationImpl var9 = null;
      if (var8 == null) {
         this.reportSchemaError("s4s-elt-must-match.2", new Object[]{"group (global)", "(annotation?, (all | choice | sequence))"}, var1);
      } else {
         var6 = new XSGroupDecl();
         String var10 = var8.getLocalName();
         if (var10.equals(SchemaSymbols.ELT_ANNOTATION)) {
            var9 = this.traverseAnnotationDecl(var8, var4, true, var2);
            var8 = DOMUtil.getNextSiblingElement(var8);
            if (var8 != null) {
               var10 = var8.getLocalName();
            }
         } else {
            String var11 = DOMUtil.getSyntheticAnnotation(var1);
            if (var11 != null) {
               var9 = this.traverseSyntheticAnnotation(var1, var11, var4, false, var2);
            }
         }

         if (var8 == null) {
            this.reportSchemaError("s4s-elt-must-match.2", new Object[]{"group (global)", "(annotation?, (all | choice | sequence))"}, var1);
         } else if (var10.equals(SchemaSymbols.ELT_ALL)) {
            var7 = this.traverseAll(var8, var2, var3, 4, var6);
         } else if (var10.equals(SchemaSymbols.ELT_CHOICE)) {
            var7 = this.traverseChoice(var8, var2, var3, 4, var6);
         } else if (var10.equals(SchemaSymbols.ELT_SEQUENCE)) {
            var7 = this.traverseSequence(var8, var2, var3, 4, var6);
         } else {
            this.reportSchemaError("s4s-elt-must-match.1", new Object[]{"group (global)", "(annotation?, (all | choice | sequence))", DOMUtil.getLocalName(var8)}, var8);
         }

         if (var8 != null && DOMUtil.getNextSiblingElement(var8) != null) {
            this.reportSchemaError("s4s-elt-must-match.1", new Object[]{"group (global)", "(annotation?, (all | choice | sequence))", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(var8))}, DOMUtil.getNextSiblingElement(var8));
         }

         if (var5 != null) {
            var6.fName = var5;
            var6.fTargetNamespace = var2.fTargetNamespace;
            if (var7 != null) {
               var6.fModelGroup = (XSModelGroupImpl)var7.fValue;
            }

            var6.fAnnotation = var9;
            var3.addGlobalGroupDecl(var6);
         } else {
            var6 = null;
         }
      }

      if (var6 != null) {
         Object var12 = super.fSchemaHandler.getGrpOrAttrGrpRedefinedByRestriction(4, new QName(XMLSymbols.EMPTY_STRING, var5, var5, var2.fTargetNamespace), var2, var1);
         if (var12 != null) {
            var3.addRedefinedGroupDecl(var6, (XSGroupDecl)var12, super.fSchemaHandler.element2Locator(var1));
         }
      }

      super.fAttrChecker.returnAttrArray(var4, var2);
      return var6;
   }
}
