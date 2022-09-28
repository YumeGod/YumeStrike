package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.util.DOMUtil;
import org.w3c.dom.Element;

class XSDWildcardTraverser extends XSDAbstractTraverser {
   XSDWildcardTraverser(XSDHandler var1, XSAttributeChecker var2) {
      super(var1, var2);
   }

   XSParticleDecl traverseAny(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, false, var2);
      XSWildcardDecl var5 = this.traverseWildcardDecl(var1, var4, var2, var3);
      XSParticleDecl var6 = null;
      if (var5 != null) {
         int var7 = ((XInt)var4[XSAttributeChecker.ATTIDX_MINOCCURS]).intValue();
         int var8 = ((XInt)var4[XSAttributeChecker.ATTIDX_MAXOCCURS]).intValue();
         if (var8 != 0) {
            if (super.fSchemaHandler.fDeclPool != null) {
               var6 = super.fSchemaHandler.fDeclPool.getParticleDecl();
            } else {
               var6 = new XSParticleDecl();
            }

            var6.fType = 2;
            var6.fValue = var5;
            var6.fMinOccurs = var7;
            var6.fMaxOccurs = var8;
         }
      }

      super.fAttrChecker.returnAttrArray(var4, var2);
      return var6;
   }

   XSWildcardDecl traverseAnyAttribute(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, false, var2);
      XSWildcardDecl var5 = this.traverseWildcardDecl(var1, var4, var2, var3);
      super.fAttrChecker.returnAttrArray(var4, var2);
      return var5;
   }

   XSWildcardDecl traverseWildcardDecl(Element var1, Object[] var2, XSDocumentInfo var3, SchemaGrammar var4) {
      XSWildcardDecl var5 = new XSWildcardDecl();
      XInt var6 = (XInt)var2[XSAttributeChecker.ATTIDX_NAMESPACE];
      var5.fType = var6.shortValue();
      var5.fNamespaceList = (String[])var2[XSAttributeChecker.ATTIDX_NAMESPACE_LIST];
      XInt var7 = (XInt)var2[XSAttributeChecker.ATTIDX_PROCESSCONTENTS];
      var5.fProcessContents = var7.shortValue();
      Element var8 = DOMUtil.getFirstChildElement(var1);
      XSAnnotationImpl var9 = null;
      String var10;
      if (var8 != null) {
         if (DOMUtil.getLocalName(var8).equals(SchemaSymbols.ELT_ANNOTATION)) {
            var9 = this.traverseAnnotationDecl(var8, var2, false, var3);
            var8 = DOMUtil.getNextSiblingElement(var8);
         } else {
            var10 = DOMUtil.getSyntheticAnnotation(var1);
            if (var10 != null) {
               var9 = this.traverseSyntheticAnnotation(var1, var10, var2, false, var3);
            }
         }

         if (var8 != null) {
            this.reportSchemaError("s4s-elt-must-match.1", new Object[]{"wildcard", "(annotation?)", DOMUtil.getLocalName(var8)}, var1);
         }
      } else {
         var10 = DOMUtil.getSyntheticAnnotation(var1);
         if (var10 != null) {
            var9 = this.traverseSyntheticAnnotation(var1, var10, var2, false, var3);
         }
      }

      var5.fAnnotation = var9;
      return var5;
   }
}
