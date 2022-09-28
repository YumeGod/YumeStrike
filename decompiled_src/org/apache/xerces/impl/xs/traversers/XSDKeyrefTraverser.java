package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.identity.KeyRef;
import org.apache.xerces.impl.xs.identity.UniqueOrKey;
import org.apache.xerces.xni.QName;
import org.w3c.dom.Element;

class XSDKeyrefTraverser extends XSDAbstractIDConstraintTraverser {
   public XSDKeyrefTraverser(XSDHandler var1, XSAttributeChecker var2) {
      super(var1, var2);
   }

   void traverse(Element var1, XSElementDecl var2, XSDocumentInfo var3, SchemaGrammar var4) {
      Object[] var5 = super.fAttrChecker.checkAttributes(var1, false, var3);
      String var6 = (String)var5[XSAttributeChecker.ATTIDX_NAME];
      if (var6 == null) {
         this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_KEYREF, SchemaSymbols.ATT_NAME}, var1);
         super.fAttrChecker.returnAttrArray(var5, var3);
      } else {
         QName var7 = (QName)var5[XSAttributeChecker.ATTIDX_REFER];
         if (var7 == null) {
            this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_KEYREF, SchemaSymbols.ATT_REFER}, var1);
            super.fAttrChecker.returnAttrArray(var5, var3);
         } else {
            UniqueOrKey var8 = null;
            IdentityConstraint var9 = (IdentityConstraint)super.fSchemaHandler.getGlobalDecl(var3, 5, var7, var1);
            if (var9 != null) {
               if (var9.getCategory() != 1 && var9.getCategory() != 3) {
                  this.reportSchemaError("src-resolve", new Object[]{var7.rawname, "identity constraint key/unique"}, var1);
               } else {
                  var8 = (UniqueOrKey)var9;
               }
            }

            if (var8 == null) {
               super.fAttrChecker.returnAttrArray(var5, var3);
            } else {
               KeyRef var10 = new KeyRef(var3.fTargetNamespace, var6, var2.fName, var8);
               this.traverseIdentityConstraint(var10, var1, var3, var5);
               if (var8.getFieldCount() != var10.getFieldCount()) {
                  this.reportSchemaError("c-props-correct.2", new Object[]{var6, var8.getIdentityConstraintName()}, var1);
               } else {
                  var4.addIDConstraintDecl(var2, var10);
               }

               super.fAttrChecker.returnAttrArray(var5, var3);
            }
         }
      }
   }
}
