package org.apache.xerces.parsers;

import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xinclude.XIncludeHandler;
import org.apache.xerces.xinclude.XIncludeNamespaceSupport;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XIncludeAwareParserConfiguration extends XML11Configuration {
   protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
   protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
   protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
   protected static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
   protected static final String XINCLUDE_HANDLER = "http://apache.org/xml/properties/internal/xinclude-handler";
   protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
   protected XIncludeHandler fXIncludeHandler;
   protected NamespaceSupport fNonXIncludeNSContext;
   protected XIncludeNamespaceSupport fXIncludeNSContext;
   protected NamespaceContext fCurrentNSContext;
   protected boolean fXIncludeEnabled;

   public XIncludeAwareParserConfiguration() {
      this((SymbolTable)null, (XMLGrammarPool)null, (XMLComponentManager)null);
   }

   public XIncludeAwareParserConfiguration(SymbolTable var1) {
      this(var1, (XMLGrammarPool)null, (XMLComponentManager)null);
   }

   public XIncludeAwareParserConfiguration(SymbolTable var1, XMLGrammarPool var2) {
      this(var1, var2, (XMLComponentManager)null);
   }

   public XIncludeAwareParserConfiguration(SymbolTable var1, XMLGrammarPool var2, XMLComponentManager var3) {
      super(var1, var2, var3);
      this.fXIncludeEnabled = false;
      String[] var4 = new String[]{"http://xml.org/sax/features/allow-dtd-events-after-endDTD", "http://apache.org/xml/features/xinclude/fixup-base-uris", "http://apache.org/xml/features/xinclude/fixup-language"};
      this.addRecognizedFeatures(var4);
      String[] var5 = new String[]{"http://apache.org/xml/properties/internal/xinclude-handler", "http://apache.org/xml/properties/internal/namespace-context"};
      this.addRecognizedProperties(var5);
      this.setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", true);
      this.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
      this.setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
      this.fNonXIncludeNSContext = new NamespaceSupport();
      this.fCurrentNSContext = this.fNonXIncludeNSContext;
      this.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNonXIncludeNSContext);
   }

   protected void configurePipeline() {
      super.configurePipeline();
      if (this.fXIncludeEnabled) {
         if (this.fXIncludeHandler == null) {
            this.fXIncludeHandler = new XIncludeHandler();
            this.setProperty("http://apache.org/xml/properties/internal/xinclude-handler", this.fXIncludeHandler);
            this.addCommonComponent(this.fXIncludeHandler);
            this.fXIncludeHandler.reset(this);
         }

         if (this.fCurrentNSContext != this.fXIncludeNSContext) {
            if (this.fXIncludeNSContext == null) {
               this.fXIncludeNSContext = new XIncludeNamespaceSupport();
            }

            this.fCurrentNSContext = this.fXIncludeNSContext;
            this.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fXIncludeNSContext);
         }

         super.fDTDScanner.setDTDHandler(super.fDTDProcessor);
         super.fDTDProcessor.setDTDSource(super.fDTDScanner);
         super.fDTDProcessor.setDTDHandler(this.fXIncludeHandler);
         this.fXIncludeHandler.setDTDSource(super.fDTDProcessor);
         this.fXIncludeHandler.setDTDHandler(super.fDTDHandler);
         if (super.fDTDHandler != null) {
            super.fDTDHandler.setDTDSource(this.fXIncludeHandler);
         }

         XMLDocumentSource var1 = null;
         if (super.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE) {
            var1 = super.fSchemaValidator.getDocumentSource();
         } else {
            var1 = super.fLastComponent;
            super.fLastComponent = this.fXIncludeHandler;
         }

         XMLDocumentHandler var2 = var1.getDocumentHandler();
         var1.setDocumentHandler(this.fXIncludeHandler);
         this.fXIncludeHandler.setDocumentSource(var1);
         if (var2 != null) {
            this.fXIncludeHandler.setDocumentHandler(var2);
            var2.setDocumentSource(this.fXIncludeHandler);
         }
      } else if (this.fCurrentNSContext != this.fNonXIncludeNSContext) {
         this.fCurrentNSContext = this.fNonXIncludeNSContext;
         this.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNonXIncludeNSContext);
      }

   }

   protected void configureXML11Pipeline() {
      super.configureXML11Pipeline();
      if (this.fXIncludeEnabled) {
         if (this.fXIncludeHandler == null) {
            this.fXIncludeHandler = new XIncludeHandler();
            this.setProperty("http://apache.org/xml/properties/internal/xinclude-handler", this.fXIncludeHandler);
            this.addCommonComponent(this.fXIncludeHandler);
            this.fXIncludeHandler.reset(this);
         }

         if (this.fCurrentNSContext != this.fXIncludeNSContext) {
            if (this.fXIncludeNSContext == null) {
               this.fXIncludeNSContext = new XIncludeNamespaceSupport();
            }

            this.fCurrentNSContext = this.fXIncludeNSContext;
            this.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fXIncludeNSContext);
         }

         super.fXML11DTDScanner.setDTDHandler(super.fXML11DTDProcessor);
         super.fXML11DTDProcessor.setDTDSource(super.fXML11DTDScanner);
         super.fXML11DTDProcessor.setDTDHandler(this.fXIncludeHandler);
         this.fXIncludeHandler.setDTDSource(super.fXML11DTDProcessor);
         this.fXIncludeHandler.setDTDHandler(super.fDTDHandler);
         if (super.fDTDHandler != null) {
            super.fDTDHandler.setDTDSource(this.fXIncludeHandler);
         }

         XMLDocumentSource var1 = null;
         if (super.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE) {
            var1 = super.fSchemaValidator.getDocumentSource();
         } else {
            var1 = super.fLastComponent;
            super.fLastComponent = this.fXIncludeHandler;
         }

         XMLDocumentHandler var2 = var1.getDocumentHandler();
         var1.setDocumentHandler(this.fXIncludeHandler);
         this.fXIncludeHandler.setDocumentSource(var1);
         if (var2 != null) {
            this.fXIncludeHandler.setDocumentHandler(var2);
            var2.setDocumentSource(this.fXIncludeHandler);
         }
      } else if (this.fCurrentNSContext != this.fNonXIncludeNSContext) {
         this.fCurrentNSContext = this.fNonXIncludeNSContext;
         this.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNonXIncludeNSContext);
      }

   }

   public boolean getFeature(String var1) throws XMLConfigurationException {
      if (var1.equals("http://apache.org/xml/features/internal/parser-settings")) {
         return super.fConfigUpdated;
      } else {
         return var1.equals("http://apache.org/xml/features/xinclude") ? this.fXIncludeEnabled : super.getFeature0(var1);
      }
   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
      if (var1.equals("http://apache.org/xml/features/xinclude")) {
         this.fXIncludeEnabled = var2;
         super.fConfigUpdated = true;
      } else {
         super.setFeature(var1, var2);
      }
   }
}
