package org.apache.xerces.xinclude;

import java.util.Enumeration;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;

public class MultipleScopeNamespaceSupport extends NamespaceSupport {
   protected int[] fScope = new int[8];
   protected int fCurrentScope = 0;

   public MultipleScopeNamespaceSupport() {
      this.fScope[0] = 0;
   }

   public MultipleScopeNamespaceSupport(NamespaceContext var1) {
      super(var1);
      this.fScope[0] = 0;
   }

   public Enumeration getAllPrefixes() {
      int var1 = 0;
      String[] var2;
      if (super.fPrefixes.length < super.fNamespace.length / 2) {
         var2 = new String[super.fNamespaceSize];
         super.fPrefixes = var2;
      }

      var2 = null;
      boolean var3 = true;

      for(int var4 = super.fContext[this.fScope[this.fCurrentScope]]; var4 <= super.fNamespaceSize - 2; var4 += 2) {
         String var6 = super.fNamespace[var4];

         for(int var5 = 0; var5 < var1; ++var5) {
            if (super.fPrefixes[var5] == var6) {
               var3 = false;
               break;
            }
         }

         if (var3) {
            super.fPrefixes[var1++] = var6;
         }

         var3 = true;
      }

      return new NamespaceSupport.Prefixes(super.fPrefixes, var1);
   }

   public int getScopeForContext(int var1) {
      int var2;
      for(var2 = this.fCurrentScope; var1 < this.fScope[var2]; --var2) {
      }

      return var2;
   }

   public String getPrefix(String var1) {
      return this.getPrefix(var1, super.fNamespaceSize, super.fContext[this.fScope[this.fCurrentScope]]);
   }

   public String getURI(String var1) {
      return this.getURI(var1, super.fNamespaceSize, super.fContext[this.fScope[this.fCurrentScope]]);
   }

   public String getPrefix(String var1, int var2) {
      return this.getPrefix(var1, super.fContext[var2 + 1], super.fContext[this.fScope[this.getScopeForContext(var2)]]);
   }

   public String getURI(String var1, int var2) {
      return this.getURI(var1, super.fContext[var2 + 1], super.fContext[this.fScope[this.getScopeForContext(var2)]]);
   }

   public String getPrefix(String var1, int var2, int var3) {
      if (var1 == NamespaceContext.XML_URI) {
         return XMLSymbols.PREFIX_XML;
      } else if (var1 == NamespaceContext.XMLNS_URI) {
         return XMLSymbols.PREFIX_XMLNS;
      } else {
         for(int var4 = var2; var4 > var3; var4 -= 2) {
            if (super.fNamespace[var4 - 1] == var1 && this.getURI(super.fNamespace[var4 - 2]) == var1) {
               return super.fNamespace[var4 - 2];
            }
         }

         return null;
      }
   }

   public String getURI(String var1, int var2, int var3) {
      if (var1 == XMLSymbols.PREFIX_XML) {
         return NamespaceContext.XML_URI;
      } else if (var1 == XMLSymbols.PREFIX_XMLNS) {
         return NamespaceContext.XMLNS_URI;
      } else {
         for(int var4 = var2; var4 > var3; var4 -= 2) {
            if (super.fNamespace[var4 - 2] == var1) {
               return super.fNamespace[var4 - 1];
            }
         }

         return null;
      }
   }

   public void reset() {
      super.fCurrentContext = this.fScope[this.fCurrentScope];
      super.fNamespaceSize = super.fContext[super.fCurrentContext];
   }

   public void pushScope() {
      if (this.fCurrentScope + 1 == this.fScope.length) {
         int[] var1 = new int[this.fScope.length * 2];
         System.arraycopy(this.fScope, 0, var1, 0, this.fScope.length);
         this.fScope = var1;
      }

      this.pushContext();
      this.fScope[++this.fCurrentScope] = super.fCurrentContext;
   }

   public void popScope() {
      super.fCurrentContext = this.fScope[this.fCurrentScope--];
      this.popContext();
   }
}
