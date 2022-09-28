package org.apache.xerces.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.apache.xerces.xni.NamespaceContext;

public class NamespaceSupport implements NamespaceContext {
   protected String[] fNamespace = new String[32];
   protected int fNamespaceSize;
   protected int[] fContext = new int[8];
   protected int fCurrentContext;
   protected String[] fPrefixes = new String[16];

   public NamespaceSupport() {
   }

   public NamespaceSupport(NamespaceContext var1) {
      this.pushContext();
      Enumeration var2 = var1.getAllPrefixes();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         String var4 = var1.getURI(var3);
         this.declarePrefix(var3, var4);
      }

   }

   public void reset() {
      this.fNamespaceSize = 0;
      this.fCurrentContext = 0;
      this.fContext[this.fCurrentContext] = this.fNamespaceSize;
      this.fNamespace[this.fNamespaceSize++] = XMLSymbols.PREFIX_XML;
      this.fNamespace[this.fNamespaceSize++] = NamespaceContext.XML_URI;
      this.fNamespace[this.fNamespaceSize++] = XMLSymbols.PREFIX_XMLNS;
      this.fNamespace[this.fNamespaceSize++] = NamespaceContext.XMLNS_URI;
      ++this.fCurrentContext;
   }

   public void pushContext() {
      if (this.fCurrentContext + 1 == this.fContext.length) {
         int[] var1 = new int[this.fContext.length * 2];
         System.arraycopy(this.fContext, 0, var1, 0, this.fContext.length);
         this.fContext = var1;
      }

      this.fContext[++this.fCurrentContext] = this.fNamespaceSize;
   }

   public void popContext() {
      this.fNamespaceSize = this.fContext[this.fCurrentContext--];
   }

   public boolean declarePrefix(String var1, String var2) {
      if (var1 != XMLSymbols.PREFIX_XML && var1 != XMLSymbols.PREFIX_XMLNS) {
         for(int var3 = this.fNamespaceSize; var3 > this.fContext[this.fCurrentContext]; var3 -= 2) {
            if (this.fNamespace[var3 - 2] == var1) {
               this.fNamespace[var3 - 1] = var2;
               return true;
            }
         }

         if (this.fNamespaceSize == this.fNamespace.length) {
            String[] var4 = new String[this.fNamespaceSize * 2];
            System.arraycopy(this.fNamespace, 0, var4, 0, this.fNamespaceSize);
            this.fNamespace = var4;
         }

         this.fNamespace[this.fNamespaceSize++] = var1;
         this.fNamespace[this.fNamespaceSize++] = var2;
         return true;
      } else {
         return false;
      }
   }

   public String getURI(String var1) {
      for(int var2 = this.fNamespaceSize; var2 > 0; var2 -= 2) {
         if (this.fNamespace[var2 - 2] == var1) {
            return this.fNamespace[var2 - 1];
         }
      }

      return null;
   }

   public String getPrefix(String var1) {
      for(int var2 = this.fNamespaceSize; var2 > 0; var2 -= 2) {
         if (this.fNamespace[var2 - 1] == var1 && this.getURI(this.fNamespace[var2 - 2]) == var1) {
            return this.fNamespace[var2 - 2];
         }
      }

      return null;
   }

   public int getDeclaredPrefixCount() {
      return (this.fNamespaceSize - this.fContext[this.fCurrentContext]) / 2;
   }

   public String getDeclaredPrefixAt(int var1) {
      return this.fNamespace[this.fContext[this.fCurrentContext] + var1 * 2];
   }

   public Enumeration getAllPrefixes() {
      int var1 = 0;
      String[] var2;
      if (this.fPrefixes.length < this.fNamespace.length / 2) {
         var2 = new String[this.fNamespaceSize];
         this.fPrefixes = var2;
      }

      var2 = null;
      boolean var3 = true;

      for(int var4 = 2; var4 < this.fNamespaceSize - 2; var4 += 2) {
         String var6 = this.fNamespace[var4 + 2];

         for(int var5 = 0; var5 < var1; ++var5) {
            if (this.fPrefixes[var5] == var6) {
               var3 = false;
               break;
            }
         }

         if (var3) {
            this.fPrefixes[var1++] = var6;
         }

         var3 = true;
      }

      return new Prefixes(this.fPrefixes, var1);
   }

   public boolean containsPrefix(String var1) {
      for(int var2 = this.fNamespaceSize; var2 > 0; var2 -= 2) {
         if (this.fNamespace[var2 - 2] == var1) {
            return true;
         }
      }

      return false;
   }

   protected final class Prefixes implements Enumeration {
      private String[] prefixes;
      private int counter = 0;
      private int size = 0;

      public Prefixes(String[] var2, int var3) {
         this.prefixes = var2;
         this.size = var3;
      }

      public boolean hasMoreElements() {
         return this.counter < this.size;
      }

      public Object nextElement() {
         if (this.counter < this.size) {
            return NamespaceSupport.this.fPrefixes[this.counter++];
         } else {
            throw new NoSuchElementException("Illegal access to Namespace prefixes enumeration.");
         }
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();

         for(int var2 = 0; var2 < this.size; ++var2) {
            var1.append(this.prefixes[var2]);
            var1.append(" ");
         }

         return var1.toString();
      }
   }
}
