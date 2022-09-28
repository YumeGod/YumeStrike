package org.apache.xerces.parsers;

import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xinclude.XIncludeHandler;
import org.apache.xerces.xinclude.XIncludeNamespaceSupport;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XIncludeParserConfiguration extends XML11Configuration {
   private XIncludeHandler fXIncludeHandler;
   protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
   protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
   protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
   protected static final String XINCLUDE_HANDLER = "http://apache.org/xml/properties/internal/xinclude-handler";
   protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";

   public XIncludeParserConfiguration() {
      this((SymbolTable)null, (XMLGrammarPool)null, (XMLComponentManager)null);
   }

   public XIncludeParserConfiguration(SymbolTable var1) {
      this(var1, (XMLGrammarPool)null, (XMLComponentManager)null);
   }

   public XIncludeParserConfiguration(SymbolTable var1, XMLGrammarPool var2) {
      this(var1, var2, (XMLComponentManager)null);
   }

   public XIncludeParserConfiguration(SymbolTable var1, XMLGrammarPool var2, XMLComponentManager var3) {
      super(var1, var2, var3);
      this.fXIncludeHandler = new XIncludeHandler();
      this.addCommonComponent(this.fXIncludeHandler);
      String[] var4 = new String[]{"http://xml.org/sax/features/allow-dtd-events-after-endDTD", "http://apache.org/xml/features/xinclude/fixup-base-uris", "http://apache.org/xml/features/xinclude/fixup-language"};
      this.addRecognizedFeatures(var4);
      String[] var5 = new String[]{"http://apache.org/xml/properties/internal/xinclude-handler", "http://apache.org/xml/properties/internal/namespace-context"};
      this.addRecognizedProperties(var5);
      this.setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", true);
      this.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
      this.setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
      this.setProperty("http://apache.org/xml/properties/internal/xinclude-handler", this.fXIncludeHandler);
      this.setProperty("http://apache.org/xml/properties/internal/namespace-context", new XIncludeNamespaceSupport());
   }

   protected void configurePipeline() {
      super.configurePipeline();
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

   }

   protected void configureXML11Pipeline() {
      super.configureXML11Pipeline();
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

   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      if (var1.equals("http://apache.org/xml/properties/internal/xinclude-handler")) {
      }

      super.setProperty(var1, var2);
   }
}
