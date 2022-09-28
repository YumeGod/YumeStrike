package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.w3c.dom.Element;

class XSDAttributeGroupTraverser extends XSDAbstractTraverser {
   XSDAttributeGroupTraverser(XSDHandler var1, XSAttributeChecker var2) {
      super(var1, var2);
   }

   XSAttributeGroupDecl traverseLocal(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, false, var2);
      QName var5 = (QName)var4[XSAttributeChecker.ATTIDX_REF];
      XSAttributeGroupDecl var6 = null;
      if (var5 == null) {
         this.reportSchemaError("s4s-att-must-appear", new Object[]{"attributeGroup (local)", "ref"}, var1);
         super.fAttrChecker.returnAttrArray(var4, var2);
         return null;
      } else {
         var6 = (XSAttributeGroupDecl)super.fSchemaHandler.getGlobalDecl(var2, 2, var5, var1);
         Element var7 = DOMUtil.getFirstChildElement(var1);
         if (var7 != null) {
            String var8 = DOMUtil.getLocalName(var7);
            if (var8.equals(SchemaSymbols.ELT_ANNOTATION)) {
               this.traverseAnnotationDecl(var7, var4, false, var2);
               var7 = DOMUtil.getNextSiblingElement(var7);
            }

            if (var7 != null) {
               Object[] var9 = new Object[]{var5.rawname, "(annotation?)", DOMUtil.getLocalName(var7)};
               this.reportSchemaError("s4s-elt-must-match.1", var9, var7);
            }
         }

         super.fAttrChecker.returnAttrArray(var4, var2);
         return var6;
      }
   }

   XSAttributeGroupDecl traverseGlobal(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      XSAttributeGroupDecl var4 = new XSAttributeGroupDecl();
      Object[] var5 = super.fAttrChecker.checkAttributes(var1, true, var2);
      String var6 = (String)var5[XSAttributeChecker.ATTIDX_NAME];
      if (var6 == null) {
         this.reportSchemaError("s4s-att-must-appear", new Object[]{"attributeGroup (global)", "name"}, var1);
         var6 = "no name";
      }

      var4.fName = var6;
      var4.fTargetNamespace = var2.fTargetNamespace;
      Element var7 = DOMUtil.getFirstChildElement(var1);
      XSAnnotationImpl var8 = null;
      if (var7 != null && DOMUtil.getLocalName(var7).equals(SchemaSymbols.ELT_ANNOTATION)) {
         var8 = this.traverseAnnotationDecl(var7, var5, false, var2);
         var7 = DOMUtil.getNextSiblingElement(var7);
      } else {
         String var9 = DOMUtil.getSyntheticAnnotation(var1);
         if (var9 != null) {
            var8 = this.traverseSyntheticAnnotation(var1, var9, var5, false, var2);
         }
      }

      Element var13 = this.traverseAttrsAndAttrGrps(var7, var4, var2, var3, (XSComplexTypeDecl)null);
      if (var13 != null) {
         Object[] var10 = new Object[]{var6, "(annotation?, ((attribute | attributeGroup)*, anyAttribute?))", DOMUtil.getLocalName(var13)};
         this.reportSchemaError("s4s-elt-must-match.1", var10, var13);
      }

      var4.removeProhibitedAttrs();
      XSAttributeGroupDecl var12 = (XSAttributeGroupDecl)super.fSchemaHandler.getGrpOrAttrGrpRedefinedByRestriction(2, new QName(XMLSymbols.EMPTY_STRING, var6, var6, var2.fTargetNamespace), var2, var1);
      if (var12 != null) {
         Object[] var11 = var4.validRestrictionOf(var6, var12);
         if (var11 != null) {
            this.reportSchemaError((String)var11[var11.length - 1], var11, var7);
            this.reportSchemaError("src-redefine.7.2.2", new Object[]{var6, var11[var11.length - 1]}, var7);
         }
      }

      var4.fAnnotation = var8;
      var3.addGlobalAttributeGroupDecl(var4);
      super.fAttrChecker.returnAttrArray(var5, var2);
      return var4;
   }
}
