package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.identity.UniqueOrKey;
import org.apache.xerces.util.DOMUtil;
import org.w3c.dom.Element;

class XSDUniqueOrKeyTraverser extends XSDAbstractIDConstraintTraverser {
   public XSDUniqueOrKeyTraverser(XSDHandler var1, XSAttributeChecker var2) {
      super(var1, var2);
   }

   void traverse(Element var1, XSElementDecl var2, XSDocumentInfo var3, SchemaGrammar var4) {
      Object[] var5 = super.fAttrChecker.checkAttributes(var1, false, var3);
      String var6 = (String)var5[XSAttributeChecker.ATTIDX_NAME];
      if (var6 == null) {
         this.reportSchemaError("s4s-att-must-appear", new Object[]{DOMUtil.getLocalName(var1), SchemaSymbols.ATT_NAME}, var1);
         super.fAttrChecker.returnAttrArray(var5, var3);
      } else {
         UniqueOrKey var7 = null;
         if (DOMUtil.getLocalName(var1).equals(SchemaSymbols.ELT_UNIQUE)) {
            var7 = new UniqueOrKey(var3.fTargetNamespace, var6, var2.fName, (short)3);
         } else {
            var7 = new UniqueOrKey(var3.fTargetNamespace, var6, var2.fName, (short)1);
         }

         this.traverseIdentityConstraint(var7, var1, var3, var5);
         var4.addIDConstraintDecl(var2, var7);
         super.fAttrChecker.returnAttrArray(var5, var3);
      }
   }
}
