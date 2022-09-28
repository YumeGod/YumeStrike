package org.apache.xerces.jaxp;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.validation.Schema;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.jaxp.validation.XSGrammarPoolContainer;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.PSVIProvider;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class SAXParserImpl extends SAXParser implements JAXPConstants, PSVIProvider {
   private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
   private static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
   private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
   private static final String XMLSCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
   private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
   private JAXPSAXParser xmlReader;
   private String schemaLanguage;
   private final Schema grammar;
   private XMLComponent fSchemaValidator;
   private XMLComponentManager fSchemaValidatorComponentManager;
   private ValidationManager fSchemaValidationManager;
   private final ErrorHandler fInitErrorHandler;
   private final EntityResolver fInitEntityResolver;

   SAXParserImpl(SAXParserFactoryImpl var1, Hashtable var2) throws SAXException {
      this(var1, var2, false);
   }

   SAXParserImpl(SAXParserFactoryImpl var1, Hashtable var2, boolean var3) throws SAXException {
      this.schemaLanguage = null;
      this.xmlReader = new JAXPSAXParser(this);
      this.xmlReader.setFeature0("http://xml.org/sax/features/namespaces", var1.isNamespaceAware());
      this.xmlReader.setFeature0("http://xml.org/sax/features/namespace-prefixes", !var1.isNamespaceAware());
      if (var1.isXIncludeAware()) {
         this.xmlReader.setFeature0("http://apache.org/xml/features/xinclude", true);
      }

      if (var3) {
         this.xmlReader.setProperty0("http://apache.org/xml/properties/security-manager", new SecurityManager());
      }

      this.setFeatures(var2);
      if (var1.isValidating()) {
         this.fInitErrorHandler = new DefaultValidationErrorHandler();
         this.xmlReader.setErrorHandler(this.fInitErrorHandler);
      } else {
         this.fInitErrorHandler = this.xmlReader.getErrorHandler();
      }

      this.xmlReader.setFeature0("http://xml.org/sax/features/validation", var1.isValidating());
      this.grammar = var1.getSchema();
      if (this.grammar != null) {
         XMLParserConfiguration var4 = this.xmlReader.getXMLParserConfiguration();
         Object var5 = null;
         if (this.grammar instanceof XSGrammarPoolContainer) {
            var5 = new XMLSchemaValidator();
            this.fSchemaValidationManager = new ValidationManager();
            UnparsedEntityHandler var6 = new UnparsedEntityHandler(this.fSchemaValidationManager);
            var4.setDTDHandler(var6);
            var6.setDTDHandler(this.xmlReader);
            this.xmlReader.setDTDSource(var6);
            this.fSchemaValidatorComponentManager = new SchemaValidatorConfiguration(var4, (XSGrammarPoolContainer)this.grammar, this.fSchemaValidationManager);
         } else {
            var5 = new JAXPValidatorComponent(this.grammar.newValidatorHandler());
            this.fSchemaValidatorComponentManager = var4;
         }

         var4.addRecognizedFeatures(((XMLComponent)var5).getRecognizedFeatures());
         var4.addRecognizedProperties(((XMLComponent)var5).getRecognizedProperties());
         var4.setDocumentHandler((XMLDocumentHandler)var5);
         ((XMLDocumentSource)var5).setDocumentHandler(this.xmlReader);
         this.xmlReader.setDocumentSource((XMLDocumentSource)var5);
         this.fSchemaValidator = (XMLComponent)var5;
      }

      this.fInitEntityResolver = this.xmlReader.getEntityResolver();
   }

   private void setFeatures(Hashtable var1) throws SAXNotSupportedException, SAXNotRecognizedException {
      if (var1 != null) {
         Enumeration var2 = var1.keys();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            boolean var4 = (Boolean)var1.get(var3);
            this.xmlReader.setFeature0(var3, var4);
         }
      }

   }

   public Parser getParser() throws SAXException {
      return this.xmlReader;
   }

   public XMLReader getXMLReader() {
      return this.xmlReader;
   }

   public boolean isNamespaceAware() {
      try {
         return this.xmlReader.getFeature("http://xml.org/sax/features/namespaces");
      } catch (SAXException var2) {
         throw new IllegalStateException(var2.getMessage());
      }
   }

   public boolean isValidating() {
      try {
         return this.xmlReader.getFeature("http://xml.org/sax/features/validation");
      } catch (SAXException var2) {
         throw new IllegalStateException(var2.getMessage());
      }
   }

   public boolean isXIncludeAware() {
      try {
         return this.xmlReader.getFeature("http://apache.org/xml/features/xinclude");
      } catch (SAXException var2) {
         return false;
      }
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      this.xmlReader.setProperty(var1, var2);
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      return this.xmlReader.getProperty(var1);
   }

   public Schema getSchema() {
      return this.grammar;
   }

   public void reset() {
      try {
         this.xmlReader.restoreInitState();
      } catch (SAXException var2) {
      }

      this.xmlReader.setContentHandler((ContentHandler)null);
      this.xmlReader.setDTDHandler((DTDHandler)null);
      if (this.xmlReader.getErrorHandler() != this.fInitErrorHandler) {
         this.xmlReader.setErrorHandler(this.fInitErrorHandler);
      }

      if (this.xmlReader.getEntityResolver() != this.fInitEntityResolver) {
         this.xmlReader.setEntityResolver(this.fInitEntityResolver);
      }

   }

   public ElementPSVI getElementPSVI() {
      return this.xmlReader.getElementPSVI();
   }

   public AttributePSVI getAttributePSVI(int var1) {
      return this.xmlReader.getAttributePSVI(var1);
   }

   public AttributePSVI getAttributePSVIByName(String var1, String var2) {
      return this.xmlReader.getAttributePSVIByName(var1, var2);
   }

   public static class JAXPSAXParser extends org.apache.xerces.parsers.SAXParser {
      private HashMap fInitFeatures = new HashMap();
      private HashMap fInitProperties = new HashMap();
      private SAXParserImpl fSAXParser;

      public JAXPSAXParser() {
      }

      JAXPSAXParser(SAXParserImpl var1) {
         this.fSAXParser = var1;
      }

      public synchronized void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
         if (var1 == null) {
            throw new NullPointerException();
         } else if (var1.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            try {
               this.setProperty("http://apache.org/xml/properties/security-manager", var2 ? new SecurityManager() : null);
            } catch (SAXNotRecognizedException var5) {
               if (var2) {
                  throw var5;
               }
            } catch (SAXNotSupportedException var6) {
               if (var2) {
                  throw var6;
               }
            }

         } else {
            if (!this.fInitFeatures.containsKey(var1)) {
               boolean var3 = super.getFeature(var1);
               this.fInitFeatures.put(var1, var3 ? Boolean.TRUE : Boolean.FALSE);
            }

            if (this.fSAXParser != null && this.fSAXParser.fSchemaValidator != null) {
               this.setSchemaValidatorFeature(var1, var2);
            }

            super.setFeature(var1, var2);
         }
      }

      public synchronized boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
         if (var1 == null) {
            throw new NullPointerException();
         } else if (var1.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            try {
               return super.getProperty("http://apache.org/xml/properties/security-manager") != null;
            } catch (SAXException var3) {
               return false;
            }
         } else {
            return super.getFeature(var1);
         }
      }

      public synchronized void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            if (this.fSAXParser != null) {
               if ("http://java.sun.com/xml/jaxp/properties/schemaLanguage".equals(var1)) {
                  if (this.fSAXParser.grammar != null) {
                     throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "schema-already-specified", new Object[]{var1}));
                  }

                  if ("http://www.w3.org/2001/XMLSchema".equals(var2)) {
                     if (this.fSAXParser.isValidating()) {
                        this.fSAXParser.schemaLanguage = "http://www.w3.org/2001/XMLSchema";
                        this.setFeature("http://apache.org/xml/features/validation/schema", true);
                        if (!this.fInitProperties.containsKey("http://java.sun.com/xml/jaxp/properties/schemaLanguage")) {
                           this.fInitProperties.put("http://java.sun.com/xml/jaxp/properties/schemaLanguage", super.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage"));
                        }

                        super.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
                     }
                  } else {
                     if (var2 != null) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "schema-not-supported", (Object[])null));
                     }

                     this.fSAXParser.schemaLanguage = null;
                     this.setFeature("http://apache.org/xml/features/validation/schema", false);
                  }

                  return;
               }

               if ("http://java.sun.com/xml/jaxp/properties/schemaSource".equals(var1)) {
                  if (this.fSAXParser.grammar != null) {
                     throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "schema-already-specified", new Object[]{var1}));
                  }

                  String var3 = (String)this.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
                  if (var3 != null && "http://www.w3.org/2001/XMLSchema".equals(var3)) {
                     if (!this.fInitProperties.containsKey("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
                        this.fInitProperties.put("http://java.sun.com/xml/jaxp/properties/schemaSource", super.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource"));
                     }

                     super.setProperty(var1, var2);
                     return;
                  }

                  throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "jaxp-order-not-supported", new Object[]{"http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://java.sun.com/xml/jaxp/properties/schemaSource"}));
               }
            }

            if (!this.fInitProperties.containsKey(var1)) {
               this.fInitProperties.put(var1, super.getProperty(var1));
            }

            if (this.fSAXParser != null && this.fSAXParser.fSchemaValidator != null) {
               this.setSchemaValidatorProperty(var1, var2);
            }

            super.setProperty(var1, var2);
         }
      }

      public synchronized Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            return this.fSAXParser != null && "http://java.sun.com/xml/jaxp/properties/schemaLanguage".equals(var1) ? this.fSAXParser.schemaLanguage : super.getProperty(var1);
         }
      }

      synchronized void restoreInitState() throws SAXNotRecognizedException, SAXNotSupportedException {
         Iterator var1;
         Map.Entry var2;
         String var3;
         if (!this.fInitFeatures.isEmpty()) {
            var1 = this.fInitFeatures.entrySet().iterator();

            while(var1.hasNext()) {
               var2 = (Map.Entry)var1.next();
               var3 = (String)var2.getKey();
               boolean var4 = (Boolean)var2.getValue();
               super.setFeature(var3, var4);
            }

            this.fInitFeatures.clear();
         }

         if (!this.fInitProperties.isEmpty()) {
            var1 = this.fInitProperties.entrySet().iterator();

            while(var1.hasNext()) {
               var2 = (Map.Entry)var1.next();
               var3 = (String)var2.getKey();
               Object var5 = var2.getValue();
               super.setProperty(var3, var5);
            }

            this.fInitProperties.clear();
         }

      }

      public void parse(InputSource var1) throws SAXException, IOException {
         if (this.fSAXParser != null && this.fSAXParser.fSchemaValidator != null) {
            if (this.fSAXParser.fSchemaValidationManager != null) {
               this.fSAXParser.fSchemaValidationManager.reset();
            }

            this.resetSchemaValidator();
         }

         super.parse(var1);
      }

      public void parse(String var1) throws SAXException, IOException {
         if (this.fSAXParser != null && this.fSAXParser.fSchemaValidator != null) {
            if (this.fSAXParser.fSchemaValidationManager != null) {
               this.fSAXParser.fSchemaValidationManager.reset();
            }

            this.resetSchemaValidator();
         }

         super.parse(var1);
      }

      XMLParserConfiguration getXMLParserConfiguration() {
         return super.fConfiguration;
      }

      void setFeature0(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
         super.setFeature(var1, var2);
      }

      boolean getFeature0(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
         return super.getFeature(var1);
      }

      void setProperty0(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
         super.setProperty(var1, var2);
      }

      Object getProperty0(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
         return super.getProperty(var1);
      }

      private void setSchemaValidatorFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
         try {
            this.fSAXParser.fSchemaValidator.setFeature(var1, var2);
         } catch (XMLConfigurationException var5) {
            String var4 = var5.getIdentifier();
            if (var5.getType() == 0) {
               throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{var4}));
            } else {
               throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "feature-not-supported", new Object[]{var4}));
            }
         }
      }

      private void setSchemaValidatorProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
         try {
            this.fSAXParser.fSchemaValidator.setProperty(var1, var2);
         } catch (XMLConfigurationException var5) {
            String var4 = var5.getIdentifier();
            if (var5.getType() == 0) {
               throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-recognized", new Object[]{var4}));
            } else {
               throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-supported", new Object[]{var4}));
            }
         }
      }

      private void resetSchemaValidator() throws SAXException {
         try {
            this.fSAXParser.fSchemaValidator.reset(this.fSAXParser.fSchemaValidatorComponentManager);
         } catch (XMLConfigurationException var2) {
            throw new SAXException(var2);
         }
      }
   }
}
