package org.apache.xerces.impl.dtd;

import java.io.EOFException;
import java.io.IOException;
import java.util.Locale;
import org.apache.xerces.impl.XMLDTDScannerImpl;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.util.DefaultErrorHandler;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarLoader;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLDTDLoader extends XMLDTDProcessor implements XMLGrammarLoader {
   protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
   private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/warn-on-duplicate-attdef", "http://apache.org/xml/features/scanner/notify-char-refs", "http://apache.org/xml/features/standard-uri-conformant"};
   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
   public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
   private static final String[] LOADER_RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/validator/dtd"};
   private boolean fStrictURI;
   protected XMLEntityResolver fEntityResolver;
   protected XMLDTDScannerImpl fDTDScanner;
   protected XMLEntityManager fEntityManager;
   protected Locale fLocale;

   public XMLDTDLoader() {
      this(new SymbolTable());
   }

   public XMLDTDLoader(SymbolTable var1) {
      this(var1, (XMLGrammarPool)null);
   }

   public XMLDTDLoader(SymbolTable var1, XMLGrammarPool var2) {
      this(var1, var2, (XMLErrorReporter)null, new XMLEntityManager());
   }

   XMLDTDLoader(SymbolTable var1, XMLGrammarPool var2, XMLErrorReporter var3, XMLEntityResolver var4) {
      this.fStrictURI = false;
      super.fSymbolTable = var1;
      super.fGrammarPool = var2;
      if (var3 == null) {
         var3 = new XMLErrorReporter();
         var3.setProperty("http://apache.org/xml/properties/internal/error-handler", new DefaultErrorHandler());
      }

      super.fErrorReporter = var3;
      if (super.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
         XMLMessageFormatter var5 = new XMLMessageFormatter();
         super.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", var5);
         super.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", var5);
      }

      this.fEntityResolver = var4;
      if (this.fEntityResolver instanceof XMLEntityManager) {
         this.fEntityManager = (XMLEntityManager)this.fEntityResolver;
      } else {
         this.fEntityManager = new XMLEntityManager();
      }

      this.fEntityManager.setProperty("http://apache.org/xml/properties/internal/error-reporter", var3);
      this.fDTDScanner = new XMLDTDScannerImpl(super.fSymbolTable, super.fErrorReporter, this.fEntityManager);
      this.fDTDScanner.setDTDHandler(this);
      this.fDTDScanner.setDTDContentModelHandler(this);
      this.reset();
   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
      if (var1.equals("http://xml.org/sax/features/validation")) {
         super.fValidation = var2;
      } else if (var1.equals("http://apache.org/xml/features/validation/warn-on-duplicate-attdef")) {
         super.fWarnDuplicateAttdef = var2;
      } else if (var1.equals("http://apache.org/xml/features/scanner/notify-char-refs")) {
         this.fDTDScanner.setFeature(var1, var2);
      } else {
         if (!var1.equals("http://apache.org/xml/features/standard-uri-conformant")) {
            throw new XMLConfigurationException((short)0, var1);
         }

         this.fStrictURI = var2;
      }

   }

   public String[] getRecognizedProperties() {
      return (String[])LOADER_RECOGNIZED_PROPERTIES.clone();
   }

   public Object getProperty(String var1) throws XMLConfigurationException {
      if (var1.equals("http://apache.org/xml/properties/internal/symbol-table")) {
         return super.fSymbolTable;
      } else if (var1.equals("http://apache.org/xml/properties/internal/error-reporter")) {
         return super.fErrorReporter;
      } else if (var1.equals("http://apache.org/xml/properties/internal/error-handler")) {
         return super.fErrorReporter.getErrorHandler();
      } else if (var1.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
         return this.fEntityResolver;
      } else if (var1.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
         return super.fGrammarPool;
      } else if (var1.equals("http://apache.org/xml/properties/internal/validator/dtd")) {
         return super.fValidator;
      } else {
         throw new XMLConfigurationException((short)0, var1);
      }
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      if (var1.equals("http://apache.org/xml/properties/internal/symbol-table")) {
         super.fSymbolTable = (SymbolTable)var2;
         this.fDTDScanner.setProperty(var1, var2);
         this.fEntityManager.setProperty(var1, var2);
      } else if (var1.equals("http://apache.org/xml/properties/internal/error-reporter")) {
         super.fErrorReporter = (XMLErrorReporter)var2;
         if (super.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
            XMLMessageFormatter var3 = new XMLMessageFormatter();
            super.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", var3);
            super.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", var3);
         }

         this.fDTDScanner.setProperty(var1, var2);
         this.fEntityManager.setProperty(var1, var2);
      } else if (var1.equals("http://apache.org/xml/properties/internal/error-handler")) {
         super.fErrorReporter.setProperty(var1, var2);
      } else if (var1.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
         this.fEntityResolver = (XMLEntityResolver)var2;
      } else {
         if (!var1.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
            throw new XMLConfigurationException((short)0, var1);
         }

         super.fGrammarPool = (XMLGrammarPool)var2;
      }

   }

   public boolean getFeature(String var1) throws XMLConfigurationException {
      if (var1.equals("http://xml.org/sax/features/validation")) {
         return super.fValidation;
      } else if (var1.equals("http://apache.org/xml/features/validation/warn-on-duplicate-attdef")) {
         return super.fWarnDuplicateAttdef;
      } else if (var1.equals("http://apache.org/xml/features/scanner/notify-char-refs")) {
         return this.fDTDScanner.getFeature(var1);
      } else {
         throw new XMLConfigurationException((short)0, var1);
      }
   }

   public void setLocale(Locale var1) {
      this.fLocale = var1;
   }

   public Locale getLocale() {
      return this.fLocale;
   }

   public void setErrorHandler(XMLErrorHandler var1) {
      super.fErrorReporter.setProperty("http://apache.org/xml/properties/internal/error-handler", var1);
   }

   public XMLErrorHandler getErrorHandler() {
      return super.fErrorReporter.getErrorHandler();
   }

   public void setEntityResolver(XMLEntityResolver var1) {
      this.fEntityResolver = var1;
   }

   public XMLEntityResolver getEntityResolver() {
      return this.fEntityResolver;
   }

   public Grammar loadGrammar(XMLInputSource var1) throws IOException, XNIException {
      this.reset();
      String var2 = XMLEntityManager.expandSystemId(var1.getSystemId(), var1.getBaseSystemId(), this.fStrictURI);
      super.fDTDGrammar = new DTDGrammar(super.fSymbolTable, new XMLDTDDescription(var1.getPublicId(), var1.getSystemId(), var1.getBaseSystemId(), var2, (String)null));
      super.fGrammarBucket = new DTDGrammarBucket();
      super.fGrammarBucket.setStandalone(false);
      super.fGrammarBucket.setActiveGrammar(super.fDTDGrammar);

      try {
         this.fDTDScanner.setInputSource(var1);
         this.fDTDScanner.scanDTDExternalSubset(true);
      } catch (EOFException var8) {
      } finally {
         this.fEntityManager.closeReaders();
      }

      if (super.fDTDGrammar != null && super.fGrammarPool != null) {
         super.fGrammarPool.cacheGrammars("http://www.w3.org/TR/REC-xml", new Grammar[]{super.fDTDGrammar});
      }

      return super.fDTDGrammar;
   }

   protected void reset() {
      super.reset();
      this.fDTDScanner.reset();
      this.fEntityManager.reset();
      super.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
   }
}
