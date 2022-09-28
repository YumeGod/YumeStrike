package org.apache.xerces.parsers;

import java.io.IOException;
import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XSMessageFormatter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.SynchronizedSymbolTable;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLGrammarCachingConfiguration extends XIncludeAwareParserConfiguration {
   public static final int BIG_PRIME = 2039;
   protected static final SynchronizedSymbolTable fStaticSymbolTable = new SynchronizedSymbolTable(2039);
   protected static final XMLGrammarPoolImpl fStaticGrammarPool = new XMLGrammarPoolImpl();
   protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
   protected XMLSchemaLoader fSchemaLoader;
   protected XMLDTDLoader fDTDLoader;

   public XMLGrammarCachingConfiguration() {
      this(fStaticSymbolTable, fStaticGrammarPool, (XMLComponentManager)null);
   }

   public XMLGrammarCachingConfiguration(SymbolTable var1) {
      this(var1, fStaticGrammarPool, (XMLComponentManager)null);
   }

   public XMLGrammarCachingConfiguration(SymbolTable var1, XMLGrammarPool var2) {
      this(var1, var2, (XMLComponentManager)null);
   }

   public XMLGrammarCachingConfiguration(SymbolTable var1, XMLGrammarPool var2, XMLComponentManager var3) {
      super(var1, var2, var3);
      this.fSchemaLoader = new XMLSchemaLoader(super.fSymbolTable);
      this.fSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", super.fGrammarPool);
      this.fDTDLoader = new XMLDTDLoader(super.fSymbolTable, super.fGrammarPool);
   }

   public void lockGrammarPool() {
      super.fGrammarPool.lockPool();
   }

   public void clearGrammarPool() {
      super.fGrammarPool.clear();
   }

   public void unlockGrammarPool() {
      super.fGrammarPool.unlockPool();
   }

   public Grammar parseGrammar(String var1, String var2) throws XNIException, IOException {
      XMLInputSource var3 = new XMLInputSource((String)null, var2, (String)null);
      return this.parseGrammar(var1, var3);
   }

   public Grammar parseGrammar(String var1, XMLInputSource var2) throws XNIException, IOException {
      if (var1.equals("http://www.w3.org/2001/XMLSchema")) {
         return this.parseXMLSchema(var2);
      } else {
         return var1.equals("http://www.w3.org/TR/REC-xml") ? this.parseDTD(var2) : null;
      }
   }

   protected void checkFeature(String var1) throws XMLConfigurationException {
      super.checkFeature(var1);
   }

   protected void checkProperty(String var1) throws XMLConfigurationException {
      super.checkProperty(var1);
   }

   SchemaGrammar parseXMLSchema(XMLInputSource var1) throws IOException {
      XMLEntityResolver var2 = this.getEntityResolver();
      if (var2 != null) {
         this.fSchemaLoader.setEntityResolver(var2);
      }

      if (super.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
         super.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
      }

      this.fSchemaLoader.setProperty("http://apache.org/xml/properties/internal/error-reporter", super.fErrorReporter);
      String var3 = "http://apache.org/xml/properties/";
      String var4 = var3 + "schema/external-schemaLocation";
      this.fSchemaLoader.setProperty(var4, this.getProperty(var4));
      var4 = var3 + "schema/external-noNamespaceSchemaLocation";
      this.fSchemaLoader.setProperty(var4, this.getProperty(var4));
      var4 = "http://java.sun.com/xml/jaxp/properties/schemaSource";
      this.fSchemaLoader.setProperty(var4, this.getProperty(var4));
      this.fSchemaLoader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", this.getFeature("http://apache.org/xml/features/validation/schema-full-checking"));
      SchemaGrammar var5 = (SchemaGrammar)this.fSchemaLoader.loadGrammar(var1);
      if (var5 != null) {
         super.fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", new Grammar[]{var5});
      }

      return var5;
   }

   DTDGrammar parseDTD(XMLInputSource var1) throws IOException {
      XMLEntityResolver var2 = this.getEntityResolver();
      if (var2 != null) {
         this.fDTDLoader.setEntityResolver(var2);
      }

      this.fDTDLoader.setProperty("http://apache.org/xml/properties/internal/error-reporter", super.fErrorReporter);
      DTDGrammar var3 = (DTDGrammar)this.fDTDLoader.loadGrammar(var1);
      if (var3 != null) {
         super.fGrammarPool.cacheGrammars("http://www.w3.org/TR/REC-xml", new Grammar[]{var3});
      }

      return var3;
   }
}
