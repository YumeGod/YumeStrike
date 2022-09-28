package org.apache.xerces.impl.xs;

import org.apache.xerces.util.NamespaceSupport;

public class SchemaNamespaceSupport extends NamespaceSupport {
   public SchemaNamespaceSupport() {
   }

   public SchemaNamespaceSupport(SchemaNamespaceSupport var1) {
      super.fNamespaceSize = var1.fNamespaceSize;
      if (super.fNamespace.length < super.fNamespaceSize) {
         super.fNamespace = new String[super.fNamespaceSize];
      }

      System.arraycopy(var1.fNamespace, 0, super.fNamespace, 0, super.fNamespaceSize);
      super.fCurrentContext = var1.fCurrentContext;
      if (super.fContext.length <= super.fCurrentContext) {
         super.fContext = new int[super.fCurrentContext + 1];
      }

      System.arraycopy(var1.fContext, 0, super.fContext, 0, super.fCurrentContext + 1);
   }

   public void setEffectiveContext(String[] var1) {
      if (var1 != null && var1.length != 0) {
         this.pushContext();
         int var2 = super.fNamespaceSize + var1.length;
         if (super.fNamespace.length < var2) {
            String[] var3 = new String[var2];
            System.arraycopy(super.fNamespace, 0, var3, 0, super.fNamespace.length);
            super.fNamespace = var3;
         }

         System.arraycopy(var1, 0, super.fNamespace, super.fNamespaceSize, var1.length);
         super.fNamespaceSize = var2;
      }
   }

   public String[] getEffectiveLocalContext() {
      String[] var1 = null;
      if (super.fCurrentContext >= 3) {
         int var2 = super.fContext[3];
         int var3 = super.fNamespaceSize - var2;
         if (var3 > 0) {
            var1 = new String[var3];
            System.arraycopy(super.fNamespace, var2, var1, 0, var3);
         }
      }

      return var1;
   }

   public void makeGlobal() {
      if (super.fCurrentContext >= 3) {
         super.fCurrentContext = 3;
         super.fNamespaceSize = super.fContext[3];
      }

   }
}
