package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.util.DOMEntityResolverWrapper;
import org.apache.xerces.util.DOMInputSource;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.SAXInputSource;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

public final class XMLSchemaFactory extends SchemaFactory {
   private static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
   private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
   private final XMLSchemaLoader fXMLSchemaLoader = new XMLSchemaLoader();
   private ErrorHandler fErrorHandler;
   private LSResourceResolver fLSResourceResolver;
   private final DOMEntityResolverWrapper fDOMEntityResolverWrapper = new DOMEntityResolverWrapper();
   private ErrorHandlerWrapper fErrorHandlerWrapper = new ErrorHandlerWrapper(DraconianErrorHandler.getInstance());
   private SecurityManager fSecurityManager;
   private XMLGrammarPoolWrapper fXMLGrammarPoolWrapper = new XMLGrammarPoolWrapper();

   public XMLSchemaFactory() {
      this.fXMLSchemaLoader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
      this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fXMLGrammarPoolWrapper);
      this.fXMLSchemaLoader.setEntityResolver(this.fDOMEntityResolverWrapper);
      this.fXMLSchemaLoader.setErrorHandler(this.fErrorHandlerWrapper);
   }

   public boolean isSchemaLanguageSupported(String var1) {
      if (var1 == null) {
         throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "SchemaLanguageNull", (Object[])null));
      } else if (var1.length() == 0) {
         throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "SchemaLanguageLengthZero", (Object[])null));
      } else {
         return var1.equals("http://www.w3.org/2001/XMLSchema");
      }
   }

   public LSResourceResolver getResourceResolver() {
      return this.fLSResourceResolver;
   }

   public void setResourceResolver(LSResourceResolver var1) {
      this.fLSResourceResolver = var1;
      this.fDOMEntityResolverWrapper.setEntityResolver(var1);
      this.fXMLSchemaLoader.setEntityResolver(this.fDOMEntityResolverWrapper);
   }

   public ErrorHandler getErrorHandler() {
      return this.fErrorHandler;
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.fErrorHandler = var1;
      this.fErrorHandlerWrapper.setErrorHandler((ErrorHandler)(var1 != null ? var1 : DraconianErrorHandler.getInstance()));
      this.fXMLSchemaLoader.setErrorHandler(this.fErrorHandlerWrapper);
   }

   public Schema newSchema(Source[] var1) throws SAXException {
      XMLGrammarPoolImplExtension var2 = new XMLGrammarPoolImplExtension();
      this.fXMLGrammarPoolWrapper.setGrammarPool(var2);
      XMLInputSource[] var3 = new XMLInputSource[var1.length];

      for(int var6 = 0; var6 < var1.length; ++var6) {
         Source var7 = var1[var6];
         String var10;
         if (var7 instanceof StreamSource) {
            StreamSource var8 = (StreamSource)var7;
            String var9 = var8.getPublicId();
            var10 = var8.getSystemId();
            InputStream var4 = var8.getInputStream();
            Reader var5 = var8.getReader();
            var3[var6] = new XMLInputSource(var9, var10, (String)null);
            var3[var6].setByteStream(var4);
            var3[var6].setCharacterStream(var5);
         } else if (var7 instanceof SAXSource) {
            SAXSource var14 = (SAXSource)var7;
            InputSource var17 = var14.getInputSource();
            if (var17 == null) {
               throw new SAXException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "SAXSourceNullInputSource", (Object[])null));
            }

            var3[var6] = new SAXInputSource(var14.getXMLReader(), var17);
         } else {
            if (!(var7 instanceof DOMSource)) {
               if (var7 == null) {
                  throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "SchemaSourceArrayMemberNull", (Object[])null));
               }

               throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "SchemaFactorySourceUnrecognized", new Object[]{var7.getClass().getName()}));
            }

            DOMSource var15 = (DOMSource)var7;
            Node var18 = var15.getNode();
            var10 = var15.getSystemId();
            var3[var6] = new DOMInputSource(var18, var10);
         }
      }

      try {
         this.fXMLSchemaLoader.loadGrammar(var3);
      } catch (XNIException var11) {
         throw Util.toSAXException(var11);
      } catch (IOException var12) {
         SAXParseException var19 = new SAXParseException(var12.getMessage(), (Locator)null, var12);
         this.fErrorHandler.error(var19);
         throw var19;
      }

      this.fXMLGrammarPoolWrapper.setGrammarPool((XMLGrammarPool)null);
      int var13 = var2.getGrammarCount();
      if (var13 > 1) {
         return new XMLSchema(new ReadOnlyGrammarPool(var2));
      } else if (var13 == 1) {
         Grammar[] var16 = var2.retrieveInitialGrammarSet("http://www.w3.org/2001/XMLSchema");
         return new SimpleXMLSchema(var16[0]);
      } else {
         return EmptyXMLSchema.getInstance();
      }
   }

   public Schema newSchema() throws SAXException {
      return new WeakReferenceXMLSchema();
   }

   public boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "FeatureNameNull", (Object[])null));
      } else if (var1.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
         return this.fSecurityManager != null;
      } else {
         try {
            return this.fXMLSchemaLoader.getFeature(var1);
         } catch (XMLConfigurationException var4) {
            String var3 = var4.getIdentifier();
            if (var4.getType() == 0) {
               throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), "feature-not-recognized", new Object[]{var3}));
            } else {
               throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), "feature-not-supported", new Object[]{var3}));
            }
         }
      }
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "ProperyNameNull", (Object[])null));
      } else if (var1.equals("http://apache.org/xml/properties/security-manager")) {
         return this.fSecurityManager;
      } else if (var1.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
         throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), "property-not-supported", new Object[]{var1}));
      } else {
         try {
            return this.fXMLSchemaLoader.getProperty(var1);
         } catch (XMLConfigurationException var4) {
            String var3 = var4.getIdentifier();
            if (var4.getType() == 0) {
               throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), "property-not-recognized", new Object[]{var3}));
            } else {
               throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), "property-not-supported", new Object[]{var3}));
            }
         }
      }
   }

   public void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "FeatureNameNull", (Object[])null));
      } else if (var1.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
         this.fSecurityManager = var2 ? new SecurityManager() : null;
         this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
      } else {
         try {
            this.fXMLSchemaLoader.setFeature(var1, var2);
         } catch (XMLConfigurationException var5) {
            String var4 = var5.getIdentifier();
            if (var5.getType() == 0) {
               throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), "feature-not-recognized", new Object[]{var4}));
            } else {
               throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), "feature-not-supported", new Object[]{var4}));
            }
         }
      }
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "ProperyNameNull", (Object[])null));
      } else if (var1.equals("http://apache.org/xml/properties/security-manager")) {
         this.fSecurityManager = (SecurityManager)var2;
         this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
      } else if (var1.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
         throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), "property-not-supported", new Object[]{var1}));
      } else {
         try {
            this.fXMLSchemaLoader.setProperty(var1, var2);
         } catch (XMLConfigurationException var5) {
            String var4 = var5.getIdentifier();
            if (var5.getType() == 0) {
               throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), "property-not-recognized", new Object[]{var4}));
            } else {
               throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), "property-not-supported", new Object[]{var4}));
            }
         }
      }
   }

   static class XMLGrammarPoolWrapper implements XMLGrammarPool {
      private XMLGrammarPool fGrammarPool;

      public Grammar[] retrieveInitialGrammarSet(String var1) {
         return this.fGrammarPool.retrieveInitialGrammarSet(var1);
      }

      public void cacheGrammars(String var1, Grammar[] var2) {
         this.fGrammarPool.cacheGrammars(var1, var2);
      }

      public Grammar retrieveGrammar(XMLGrammarDescription var1) {
         return this.fGrammarPool.retrieveGrammar(var1);
      }

      public void lockPool() {
         this.fGrammarPool.lockPool();
      }

      public void unlockPool() {
         this.fGrammarPool.unlockPool();
      }

      public void clear() {
         this.fGrammarPool.clear();
      }

      void setGrammarPool(XMLGrammarPool var1) {
         this.fGrammarPool = var1;
      }

      XMLGrammarPool getGrammarPool() {
         return this.fGrammarPool;
      }
   }

   static class XMLGrammarPoolImplExtension extends XMLGrammarPoolImpl {
      public XMLGrammarPoolImplExtension() {
      }

      public XMLGrammarPoolImplExtension(int var1) {
         super(var1);
      }

      int getGrammarCount() {
         return super.fGrammarCount;
      }
   }
}
