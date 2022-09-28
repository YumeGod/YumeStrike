package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.impl.xpath.XPathException;
import org.apache.xerces.impl.xs.util.ShortListImpl;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

public class Field {
   protected XPath fXPath;
   protected IdentityConstraint fIdentityConstraint;

   public Field(XPath var1, IdentityConstraint var2) {
      this.fXPath = var1;
      this.fIdentityConstraint = var2;
   }

   public org.apache.xerces.impl.xpath.XPath getXPath() {
      return this.fXPath;
   }

   public IdentityConstraint getIdentityConstraint() {
      return this.fIdentityConstraint;
   }

   public XPathMatcher createMatcher(FieldActivator var1, ValueStore var2) {
      return new Matcher(this.fXPath, var1, var2);
   }

   public String toString() {
      return this.fXPath.toString();
   }

   protected class Matcher extends XPathMatcher {
      protected FieldActivator fFieldActivator;
      protected ValueStore fStore;

      public Matcher(XPath var2, FieldActivator var3, ValueStore var4) {
         super(var2);
         this.fFieldActivator = var3;
         this.fStore = var4;
      }

      protected void matched(Object var1, short var2, ShortList var3, boolean var4) {
         super.matched(var1, var2, var3, var4);
         if (var4 && Field.this.fIdentityConstraint.getCategory() == 1) {
            String var5 = "KeyMatchesNillable";
            this.fStore.reportError(var5, new Object[]{Field.this.fIdentityConstraint.getElementName()});
         }

         this.fStore.addValue(Field.this, var1, this.convertToPrimitiveKind(var2), this.convertToPrimitiveKind(var3));
         this.fFieldActivator.setMayMatch(Field.this, Boolean.FALSE);
      }

      private short convertToPrimitiveKind(short var1) {
         if (var1 <= 20) {
            return var1;
         } else if (var1 <= 29) {
            return 2;
         } else {
            return var1 <= 42 ? 4 : var1;
         }
      }

      private ShortList convertToPrimitiveKind(ShortList var1) {
         if (var1 != null) {
            int var3 = var1.getLength();

            int var2;
            for(var2 = 0; var2 < var3; ++var2) {
               short var4 = var1.item(var2);
               if (var4 != this.convertToPrimitiveKind(var4)) {
                  break;
               }
            }

            if (var2 != var3) {
               short[] var6 = new short[var3];

               for(int var5 = 0; var5 < var2; ++var5) {
                  var6[var5] = var1.item(var5);
               }

               while(var2 < var3) {
                  var6[var2] = this.convertToPrimitiveKind(var1.item(var2));
                  ++var2;
               }

               return new ShortListImpl(var6, var6.length);
            }
         }

         return var1;
      }

      protected void handleContent(XSTypeDefinition var1, boolean var2, Object var3, short var4, ShortList var5) {
         if (var1 == null || var1.getTypeCategory() == 15 && ((XSComplexTypeDefinition)var1).getContentType() != 1) {
            this.fStore.reportError("cvc-id.3", new Object[]{Field.this.fIdentityConstraint.getName(), Field.this.fIdentityConstraint.getElementName()});
         }

         super.fMatchedString = var3;
         this.matched(super.fMatchedString, var4, var5, var2);
      }
   }

   public static class XPath extends org.apache.xerces.impl.xpath.XPath {
      public XPath(String var1, SymbolTable var2, NamespaceContext var3) throws XPathException {
         super(!var1.trim().startsWith("/") && !var1.trim().startsWith(".") ? "./" + var1 : var1, var2, var3);

         for(int var4 = 0; var4 < super.fLocationPaths.length; ++var4) {
            for(int var5 = 0; var5 < super.fLocationPaths[var4].steps.length; ++var5) {
               org.apache.xerces.impl.xpath.XPath.Axis var6 = super.fLocationPaths[var4].steps[var5].axis;
               if (var6.type == 2 && var5 < super.fLocationPaths[var4].steps.length - 1) {
                  throw new XPathException("c-fields-xpaths");
               }
            }
         }

      }
   }
}
