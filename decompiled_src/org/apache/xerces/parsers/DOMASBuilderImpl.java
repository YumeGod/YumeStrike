package org.apache.xerces.parsers;

import java.util.Vector;
import org.apache.xerces.dom.ASModelImpl;
import org.apache.xerces.dom3.as.ASModel;
import org.apache.xerces.dom3.as.DOMASBuilder;
import org.apache.xerces.dom3.as.DOMASException;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.ls.LSInput;

/** @deprecated */
public class DOMASBuilderImpl extends DOMParserImpl implements DOMASBuilder {
   protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
   protected XSGrammarBucket fGrammarBucket;
   protected ASModelImpl fAbstractSchema;

   public DOMASBuilderImpl() {
      super((XMLParserConfiguration)(new XMLGrammarCachingConfiguration()));
   }

   public DOMASBuilderImpl(XMLGrammarCachingConfiguration var1) {
      super((XMLParserConfiguration)var1);
   }

   public DOMASBuilderImpl(SymbolTable var1) {
      super((XMLParserConfiguration)(new XMLGrammarCachingConfiguration(var1)));
   }

   public DOMASBuilderImpl(SymbolTable var1, XMLGrammarPool var2) {
      super((XMLParserConfiguration)(new XMLGrammarCachingConfiguration(var1, var2)));
   }

   public ASModel getAbstractSchema() {
      return this.fAbstractSchema;
   }

   public void setAbstractSchema(ASModel var1) {
      this.fAbstractSchema = (ASModelImpl)var1;
      Object var2 = (XMLGrammarPool)super.fConfiguration.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
      if (var2 == null) {
         var2 = new XMLGrammarPoolImpl();
         super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", var2);
      }

      if (this.fAbstractSchema != null) {
         this.initGrammarPool(this.fAbstractSchema, (XMLGrammarPool)var2);
      }

   }

   public ASModel parseASURI(String var1) throws DOMASException, Exception {
      XMLInputSource var2 = new XMLInputSource((String)null, var1, (String)null);
      return this.parseASInputSource(var2);
   }

   public ASModel parseASInputSource(LSInput var1) throws DOMASException, Exception {
      XMLInputSource var2 = this.dom2xmlInputSource(var1);

      try {
         return this.parseASInputSource(var2);
      } catch (XNIException var5) {
         Exception var4 = var5.getException();
         throw var4;
      }
   }

   ASModel parseASInputSource(XMLInputSource var1) throws Exception {
      if (this.fGrammarBucket == null) {
         this.fGrammarBucket = new XSGrammarBucket();
      }

      this.initGrammarBucket();
      XMLGrammarCachingConfiguration var2 = (XMLGrammarCachingConfiguration)super.fConfiguration;
      var2.lockGrammarPool();
      SchemaGrammar var3 = var2.parseXMLSchema(var1);
      var2.unlockGrammarPool();
      ASModelImpl var4 = null;
      if (var3 != null) {
         var4 = new ASModelImpl();
         this.fGrammarBucket.putGrammar(var3, true);
         this.addGrammars(var4, this.fGrammarBucket);
      }

      return var4;
   }

   private void initGrammarBucket() {
      this.fGrammarBucket.reset();
      if (this.fAbstractSchema != null) {
         this.initGrammarBucketRecurse(this.fAbstractSchema);
      }

   }

   private void initGrammarBucketRecurse(ASModelImpl var1) {
      if (var1.getGrammar() != null) {
         this.fGrammarBucket.putGrammar(var1.getGrammar());
      }

      for(int var2 = 0; var2 < var1.getInternalASModels().size(); ++var2) {
         ASModelImpl var3 = (ASModelImpl)var1.getInternalASModels().elementAt(var2);
         this.initGrammarBucketRecurse(var3);
      }

   }

   private void addGrammars(ASModelImpl var1, XSGrammarBucket var2) {
      SchemaGrammar[] var3 = var2.getGrammars();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         ASModelImpl var5 = new ASModelImpl();
         var5.setGrammar(var3[var4]);
         var1.addASModel(var5);
      }

   }

   private void initGrammarPool(ASModelImpl var1, XMLGrammarPool var2) {
      Grammar[] var3 = new Grammar[1];
      if ((var3[0] = var1.getGrammar()) != null) {
         var2.cacheGrammars(var3[0].getGrammarDescription().getGrammarType(), var3);
      }

      Vector var4 = var1.getInternalASModels();

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         this.initGrammarPool((ASModelImpl)var4.elementAt(var5), var2);
      }

   }
}
