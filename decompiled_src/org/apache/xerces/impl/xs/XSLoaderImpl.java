package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.util.XSGrammarPool;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.LSInputList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.ls.LSInput;

public final class XSLoaderImpl implements XSLoader, DOMConfiguration {
   private final XSGrammarPool fGrammarPool = new XSGrammarMerger();
   private final XMLSchemaLoader fSchemaLoader = new XMLSchemaLoader();

   public XSLoaderImpl() {
      this.fSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
   }

   public DOMConfiguration getConfig() {
      return this;
   }

   public XSModel loadURIList(StringList var1) {
      int var2 = var1.getLength();
      if (var2 == 0) {
         return null;
      } else {
         try {
            this.fGrammarPool.clear();

            for(int var3 = 0; var3 < var2; ++var3) {
               this.fSchemaLoader.loadGrammar(new XMLInputSource((String)null, var1.item(var3), (String)null));
            }

            return this.fGrammarPool.toXSModel();
         } catch (Exception var4) {
            this.fSchemaLoader.reportDOMFatalError(var4);
            return null;
         }
      }
   }

   public XSModel loadInputList(LSInputList var1) {
      int var2 = var1.getLength();
      if (var2 == 0) {
         return null;
      } else {
         try {
            this.fGrammarPool.clear();

            for(int var3 = 0; var3 < var2; ++var3) {
               this.fSchemaLoader.loadGrammar(this.fSchemaLoader.dom2xmlInputSource(var1.item(var3)));
            }

            return this.fGrammarPool.toXSModel();
         } catch (Exception var4) {
            this.fSchemaLoader.reportDOMFatalError(var4);
            return null;
         }
      }
   }

   public XSModel loadURI(String var1) {
      try {
         this.fGrammarPool.clear();
         return ((XSGrammar)this.fSchemaLoader.loadGrammar(new XMLInputSource((String)null, var1, (String)null))).toXSModel();
      } catch (Exception var3) {
         this.fSchemaLoader.reportDOMFatalError(var3);
         return null;
      }
   }

   public XSModel load(LSInput var1) {
      try {
         this.fGrammarPool.clear();
         return ((XSGrammar)this.fSchemaLoader.loadGrammar(this.fSchemaLoader.dom2xmlInputSource(var1))).toXSModel();
      } catch (Exception var3) {
         this.fSchemaLoader.reportDOMFatalError(var3);
         return null;
      }
   }

   public void setParameter(String var1, Object var2) throws DOMException {
      this.fSchemaLoader.setParameter(var1, var2);
   }

   public Object getParameter(String var1) throws DOMException {
      return this.fSchemaLoader.getParameter(var1);
   }

   public boolean canSetParameter(String var1, Object var2) {
      return this.fSchemaLoader.canSetParameter(var1, var2);
   }

   public DOMStringList getParameterNames() {
      return this.fSchemaLoader.getParameterNames();
   }

   private static final class XSGrammarMerger extends XSGrammarPool {
      public XSGrammarMerger() {
      }

      public void putGrammar(Grammar var1) {
         SchemaGrammar var2 = this.toSchemaGrammar(super.getGrammar(var1.getGrammarDescription()));
         if (var2 != null) {
            SchemaGrammar var3 = this.toSchemaGrammar(var1);
            if (var3 != null) {
               this.mergeSchemaGrammars(var2, var3);
            }
         } else {
            super.putGrammar(var1);
         }

      }

      private SchemaGrammar toSchemaGrammar(Grammar var1) {
         return var1 instanceof SchemaGrammar ? (SchemaGrammar)var1 : null;
      }

      private void mergeSchemaGrammars(SchemaGrammar var1, SchemaGrammar var2) {
         XSNamedMap var3 = var2.getComponents((short)2);
         int var4 = var3.getLength();

         for(int var5 = 0; var5 < var4; ++var5) {
            XSElementDecl var6 = (XSElementDecl)var3.item(var5);
            if (var1.getGlobalElementDecl(var6.getName()) == null) {
               var1.addGlobalElementDecl(var6);
            }
         }

         var3 = var2.getComponents((short)1);
         var4 = var3.getLength();

         for(int var13 = 0; var13 < var4; ++var13) {
            XSAttributeDecl var7 = (XSAttributeDecl)var3.item(var13);
            if (var1.getGlobalAttributeDecl(var7.getName()) == null) {
               var1.addGlobalAttributeDecl(var7);
            }
         }

         var3 = var2.getComponents((short)3);
         var4 = var3.getLength();

         for(int var14 = 0; var14 < var4; ++var14) {
            XSTypeDefinition var8 = (XSTypeDefinition)var3.item(var14);
            if (var1.getGlobalTypeDecl(var8.getName()) == null) {
               var1.addGlobalTypeDecl(var8);
            }
         }

         var3 = var2.getComponents((short)5);
         var4 = var3.getLength();

         for(int var15 = 0; var15 < var4; ++var15) {
            XSAttributeGroupDecl var9 = (XSAttributeGroupDecl)var3.item(var15);
            if (var1.getGlobalAttributeGroupDecl(var9.getName()) == null) {
               var1.addGlobalAttributeGroupDecl(var9);
            }
         }

         var3 = var2.getComponents((short)7);
         var4 = var3.getLength();

         for(int var16 = 0; var16 < var4; ++var16) {
            XSGroupDecl var10 = (XSGroupDecl)var3.item(var16);
            if (var1.getGlobalGroupDecl(var10.getName()) == null) {
               var1.addGlobalGroupDecl(var10);
            }
         }

         var3 = var2.getComponents((short)11);
         var4 = var3.getLength();

         for(int var17 = 0; var17 < var4; ++var17) {
            XSNotationDecl var11 = (XSNotationDecl)var3.item(var17);
            if (var1.getGlobalNotationDecl(var11.getName()) == null) {
               var1.addGlobalNotationDecl(var11);
            }
         }

         XSObjectList var18 = var2.getAnnotations();
         var4 = var18.getLength();

         for(int var12 = 0; var12 < var4; ++var12) {
            var1.addAnnotation((XSAnnotationImpl)var18.item(var12));
         }

      }

      public boolean containsGrammar(XMLGrammarDescription var1) {
         return false;
      }

      public Grammar getGrammar(XMLGrammarDescription var1) {
         return null;
      }

      public Grammar retrieveGrammar(XMLGrammarDescription var1) {
         return null;
      }

      public Grammar[] retrieveInitialGrammarSet(String var1) {
         return new Grammar[0];
      }
   }
}
