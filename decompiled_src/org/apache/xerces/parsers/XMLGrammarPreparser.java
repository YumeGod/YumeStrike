package org.apache.xerces.parsers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarLoader;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLGrammarPreparser {
   private static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
   protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
   private static final Hashtable KNOWN_LOADERS = new Hashtable();
   private static final String[] RECOGNIZED_PROPERTIES;
   protected SymbolTable fSymbolTable;
   protected XMLErrorReporter fErrorReporter;
   protected XMLEntityResolver fEntityResolver;
   protected XMLGrammarPool fGrammarPool;
   protected Locale fLocale;
   private Hashtable fLoaders;

   public XMLGrammarPreparser() {
      this(new SymbolTable());
   }

   public XMLGrammarPreparser(SymbolTable var1) {
      this.fSymbolTable = var1;
      this.fLoaders = new Hashtable();
      this.setLocale(Locale.getDefault());
      this.fErrorReporter = new XMLErrorReporter();
      this.fErrorReporter.setLocale(this.fLocale);
      this.fEntityResolver = new XMLEntityManager();
   }

   public boolean registerPreparser(String var1, XMLGrammarLoader var2) {
      if (var2 == null) {
         if (KNOWN_LOADERS.containsKey(var1)) {
            String var3 = (String)KNOWN_LOADERS.get(var1);

            try {
               ClassLoader var4 = ObjectFactory.findClassLoader();
               XMLGrammarLoader var5 = (XMLGrammarLoader)ObjectFactory.newInstance(var3, var4, true);
               this.fLoaders.put(var1, var5);
               return true;
            } catch (Exception var6) {
               return false;
            }
         } else {
            return false;
         }
      } else {
         this.fLoaders.put(var1, var2);
         return true;
      }
   }

   public Grammar preparseGrammar(String var1, XMLInputSource var2) throws XNIException, IOException {
      if (this.fLoaders.containsKey(var1)) {
         XMLGrammarLoader var3 = (XMLGrammarLoader)this.fLoaders.get(var1);
         var3.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
         var3.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fEntityResolver);
         var3.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
         if (this.fGrammarPool != null) {
            try {
               var3.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
            } catch (Exception var5) {
            }
         }

         return var3.loadGrammar(var2);
      } else {
         return null;
      }
   }

   public void setLocale(Locale var1) {
      this.fLocale = var1;
   }

   public Locale getLocale() {
      return this.fLocale;
   }

   public void setErrorHandler(XMLErrorHandler var1) {
      this.fErrorReporter.setProperty("http://apache.org/xml/properties/internal/error-handler", var1);
   }

   public XMLErrorHandler getErrorHandler() {
      return this.fErrorReporter.getErrorHandler();
   }

   public void setEntityResolver(XMLEntityResolver var1) {
      this.fEntityResolver = var1;
   }

   public XMLEntityResolver getEntityResolver() {
      return this.fEntityResolver;
   }

   public void setGrammarPool(XMLGrammarPool var1) {
      this.fGrammarPool = var1;
   }

   public XMLGrammarPool getGrammarPool() {
      return this.fGrammarPool;
   }

   public XMLGrammarLoader getLoader(String var1) {
      return (XMLGrammarLoader)this.fLoaders.get(var1);
   }

   public void setFeature(String var1, boolean var2) {
      Enumeration var3 = this.fLoaders.elements();

      while(var3.hasMoreElements()) {
         XMLGrammarLoader var4 = (XMLGrammarLoader)var3.nextElement();

         try {
            var4.setFeature(var1, var2);
         } catch (Exception var6) {
         }
      }

      if (var1.equals("http://apache.org/xml/features/continue-after-fatal-error")) {
         this.fErrorReporter.setFeature("http://apache.org/xml/features/continue-after-fatal-error", var2);
      }

   }

   public void setProperty(String var1, Object var2) {
      Enumeration var3 = this.fLoaders.elements();

      while(var3.hasMoreElements()) {
         XMLGrammarLoader var4 = (XMLGrammarLoader)var3.nextElement();

         try {
            var4.setProperty(var1, var2);
         } catch (Exception var6) {
         }
      }

   }

   public boolean getFeature(String var1, String var2) {
      XMLGrammarLoader var3 = (XMLGrammarLoader)this.fLoaders.get(var1);
      return var3.getFeature(var2);
   }

   public Object getProperty(String var1, String var2) {
      XMLGrammarLoader var3 = (XMLGrammarLoader)this.fLoaders.get(var1);
      return var3.getProperty(var2);
   }

   static {
      KNOWN_LOADERS.put("http://www.w3.org/2001/XMLSchema", "org.apache.xerces.impl.xs.XMLSchemaLoader");
      KNOWN_LOADERS.put("http://www.w3.org/TR/REC-xml", "org.apache.xerces.impl.dtd.XMLDTDLoader");
      RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/grammar-pool"};
   }
}
