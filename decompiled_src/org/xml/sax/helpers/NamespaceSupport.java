package org.xml.sax.helpers;

import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class NamespaceSupport {
   public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
   public static final String NSDECL = "http://www.w3.org/xmlns/2000/";
   private static final Enumeration EMPTY_ENUMERATION = (new Vector()).elements();
   private Context[] contexts;
   private Context currentContext;
   private int contextPos;
   private boolean namespaceDeclUris;

   public NamespaceSupport() {
      this.reset();
   }

   public void reset() {
      this.contexts = new Context[32];
      this.namespaceDeclUris = false;
      this.contextPos = 0;
      this.contexts[this.contextPos] = this.currentContext = new Context();
      this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
   }

   public void pushContext() {
      int var1 = this.contexts.length;
      ++this.contextPos;
      if (this.contextPos >= var1) {
         Context[] var2 = new Context[var1 * 2];
         System.arraycopy(this.contexts, 0, var2, 0, var1);
         var1 *= 2;
         this.contexts = var2;
      }

      this.currentContext = this.contexts[this.contextPos];
      if (this.currentContext == null) {
         this.contexts[this.contextPos] = this.currentContext = new Context();
      }

      if (this.contextPos > 0) {
         this.currentContext.setParent(this.contexts[this.contextPos - 1]);
      }

   }

   public void popContext() {
      this.contexts[this.contextPos].clear();
      --this.contextPos;
      if (this.contextPos < 0) {
         throw new EmptyStackException();
      } else {
         this.currentContext = this.contexts[this.contextPos];
      }
   }

   public boolean declarePrefix(String var1, String var2) {
      if (!var1.equals("xml") && !var1.equals("xmlns")) {
         this.currentContext.declarePrefix(var1, var2);
         return true;
      } else {
         return false;
      }
   }

   public String[] processName(String var1, String[] var2, boolean var3) {
      String[] var4 = this.currentContext.processName(var1, var3);
      if (var4 == null) {
         return null;
      } else {
         var2[0] = var4[0];
         var2[1] = var4[1];
         var2[2] = var4[2];
         return var2;
      }
   }

   public String getURI(String var1) {
      return this.currentContext.getURI(var1);
   }

   public Enumeration getPrefixes() {
      return this.currentContext.getPrefixes();
   }

   public String getPrefix(String var1) {
      return this.currentContext.getPrefix(var1);
   }

   public Enumeration getPrefixes(String var1) {
      Vector var2 = new Vector();
      Enumeration var3 = this.getPrefixes();

      while(var3.hasMoreElements()) {
         String var4 = (String)var3.nextElement();
         if (var1.equals(this.getURI(var4))) {
            var2.addElement(var4);
         }
      }

      return var2.elements();
   }

   public Enumeration getDeclaredPrefixes() {
      return this.currentContext.getDeclaredPrefixes();
   }

   public void setNamespaceDeclUris(boolean var1) {
      if (this.contextPos != 0) {
         throw new IllegalStateException();
      } else if (var1 != this.namespaceDeclUris) {
         this.namespaceDeclUris = var1;
         if (var1) {
            this.currentContext.declarePrefix("xmlns", "http://www.w3.org/xmlns/2000/");
         } else {
            this.contexts[this.contextPos] = this.currentContext = new Context();
            this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
         }

      }
   }

   public boolean isNamespaceDeclUris() {
      return this.namespaceDeclUris;
   }

   final class Context {
      Hashtable prefixTable;
      Hashtable uriTable;
      Hashtable elementNameTable;
      Hashtable attributeNameTable;
      String defaultNS = null;
      private Vector declarations = null;
      private boolean declSeen = false;
      private Context parent = null;

      Context() {
         this.copyTables();
      }

      void setParent(Context var1) {
         this.parent = var1;
         this.declarations = null;
         this.prefixTable = var1.prefixTable;
         this.uriTable = var1.uriTable;
         this.elementNameTable = var1.elementNameTable;
         this.attributeNameTable = var1.attributeNameTable;
         this.defaultNS = var1.defaultNS;
         this.declSeen = false;
      }

      void clear() {
         this.parent = null;
         this.prefixTable = null;
         this.uriTable = null;
         this.elementNameTable = null;
         this.attributeNameTable = null;
         this.defaultNS = null;
      }

      void declarePrefix(String var1, String var2) {
         if (!this.declSeen) {
            this.copyTables();
         }

         if (this.declarations == null) {
            this.declarations = new Vector();
         }

         var1 = var1.intern();
         var2 = var2.intern();
         if ("".equals(var1)) {
            if ("".equals(var2)) {
               this.defaultNS = null;
            } else {
               this.defaultNS = var2;
            }
         } else {
            this.prefixTable.put(var1, var2);
            this.uriTable.put(var2, var1);
         }

         this.declarations.addElement(var1);
      }

      String[] processName(String var1, boolean var2) {
         Hashtable var4;
         if (var2) {
            var4 = this.attributeNameTable;
         } else {
            var4 = this.elementNameTable;
         }

         String[] var3 = (String[])var4.get(var1);
         if (var3 != null) {
            return var3;
         } else {
            var3 = new String[]{null, null, var1.intern()};
            int var5 = var1.indexOf(58);
            if (var5 == -1) {
               if (var2) {
                  if (var1 == "xmlns" && NamespaceSupport.this.namespaceDeclUris) {
                     var3[0] = "http://www.w3.org/xmlns/2000/";
                  } else {
                     var3[0] = "";
                  }
               } else if (this.defaultNS == null) {
                  var3[0] = "";
               } else {
                  var3[0] = this.defaultNS;
               }

               var3[1] = var3[2];
            } else {
               String var6 = var1.substring(0, var5);
               String var7 = var1.substring(var5 + 1);
               String var8;
               if ("".equals(var6)) {
                  var8 = this.defaultNS;
               } else {
                  var8 = (String)this.prefixTable.get(var6);
               }

               if (var8 == null || !var2 && "xmlns".equals(var6)) {
                  return null;
               }

               var3[0] = var8;
               var3[1] = var7.intern();
            }

            var4.put(var3[2], var3);
            return var3;
         }
      }

      String getURI(String var1) {
         if ("".equals(var1)) {
            return this.defaultNS;
         } else {
            return this.prefixTable == null ? null : (String)this.prefixTable.get(var1);
         }
      }

      String getPrefix(String var1) {
         return this.uriTable == null ? null : (String)this.uriTable.get(var1);
      }

      Enumeration getDeclaredPrefixes() {
         return this.declarations == null ? NamespaceSupport.EMPTY_ENUMERATION : this.declarations.elements();
      }

      Enumeration getPrefixes() {
         return this.prefixTable == null ? NamespaceSupport.EMPTY_ENUMERATION : this.prefixTable.keys();
      }

      private void copyTables() {
         if (this.prefixTable != null) {
            this.prefixTable = (Hashtable)this.prefixTable.clone();
         } else {
            this.prefixTable = new Hashtable();
         }

         if (this.uriTable != null) {
            this.uriTable = (Hashtable)this.uriTable.clone();
         } else {
            this.uriTable = new Hashtable();
         }

         this.elementNameTable = new Hashtable();
         this.attributeNameTable = new Hashtable();
         this.declSeen = true;
      }
   }
}
