package org.apache.xerces.parsers;

import java.io.IOException;
import org.apache.xerces.util.EntityResolver2Wrapper;
import org.apache.xerces.util.EntityResolverWrapper;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.helpers.LocatorImpl;

public class DOMParser extends AbstractDOMParser {
   protected static final String USE_ENTITY_RESOLVER2 = "http://xml.org/sax/features/use-entity-resolver2";
   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
   private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/grammar-pool"};
   protected boolean fUseEntityResolver2;

   public DOMParser(XMLParserConfiguration var1) {
      super(var1);
      this.fUseEntityResolver2 = true;
   }

   public DOMParser() {
      this((SymbolTable)null, (XMLGrammarPool)null);
   }

   public DOMParser(SymbolTable var1) {
      this(var1, (XMLGrammarPool)null);
   }

   public DOMParser(SymbolTable var1, XMLGrammarPool var2) {
      super((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeAwareParserConfiguration"));
      this.fUseEntityResolver2 = true;
      super.fConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
      if (var1 != null) {
         super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", var1);
      }

      if (var2 != null) {
         super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", var2);
      }

   }

   public void parse(String var1) throws SAXException, IOException {
      XMLInputSource var2 = new XMLInputSource((String)null, var1, (String)null);

      try {
         this.parse(var2);
      } catch (XMLParseException var6) {
         Exception var4 = var6.getException();
         if (var4 == null) {
            LocatorImpl var8 = new LocatorImpl();
            var8.setPublicId(var6.getPublicId());
            var8.setSystemId(var6.getExpandedSystemId());
            var8.setLineNumber(var6.getLineNumber());
            var8.setColumnNumber(var6.getColumnNumber());
            throw new SAXParseException(var6.getMessage(), var8);
         } else if (var4 instanceof SAXException) {
            throw (SAXException)var4;
         } else if (var4 instanceof IOException) {
            throw (IOException)var4;
         } else {
            throw new SAXException(var4);
         }
      } catch (XNIException var7) {
         var7.printStackTrace();
         Exception var5 = var7.getException();
         if (var5 == null) {
            throw new SAXException(var7.getMessage());
         } else if (var5 instanceof SAXException) {
            throw (SAXException)var5;
         } else if (var5 instanceof IOException) {
            throw (IOException)var5;
         } else {
            throw new SAXException(var5);
         }
      }
   }

   public void parse(InputSource var1) throws SAXException, IOException {
      try {
         XMLInputSource var2 = new XMLInputSource(var1.getPublicId(), var1.getSystemId(), (String)null);
         var2.setByteStream(var1.getByteStream());
         var2.setCharacterStream(var1.getCharacterStream());
         var2.setEncoding(var1.getEncoding());
         this.parse(var2);
      } catch (XMLParseException var5) {
         Exception var3 = var5.getException();
         if (var3 == null) {
            LocatorImpl var7 = new LocatorImpl();
            var7.setPublicId(var5.getPublicId());
            var7.setSystemId(var5.getExpandedSystemId());
            var7.setLineNumber(var5.getLineNumber());
            var7.setColumnNumber(var5.getColumnNumber());
            throw new SAXParseException(var5.getMessage(), var7);
         } else if (var3 instanceof SAXException) {
            throw (SAXException)var3;
         } else if (var3 instanceof IOException) {
            throw (IOException)var3;
         } else {
            throw new SAXException(var3);
         }
      } catch (XNIException var6) {
         Exception var4 = var6.getException();
         if (var4 == null) {
            throw new SAXException(var6.getMessage());
         } else if (var4 instanceof SAXException) {
            throw (SAXException)var4;
         } else if (var4 instanceof IOException) {
            throw (IOException)var4;
         } else {
            throw new SAXException(var4);
         }
      }
   }

   public void setEntityResolver(EntityResolver var1) {
      try {
         XMLEntityResolver var2 = (XMLEntityResolver)super.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
         if (this.fUseEntityResolver2 && var1 instanceof EntityResolver2) {
            if (var2 instanceof EntityResolver2Wrapper) {
               EntityResolver2Wrapper var5 = (EntityResolver2Wrapper)var2;
               var5.setEntityResolver((EntityResolver2)var1);
            } else {
               super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolver2Wrapper((EntityResolver2)var1));
            }
         } else if (var2 instanceof EntityResolverWrapper) {
            EntityResolverWrapper var3 = (EntityResolverWrapper)var2;
            var3.setEntityResolver(var1);
         } else {
            super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolverWrapper(var1));
         }
      } catch (XMLConfigurationException var4) {
      }

   }

   public EntityResolver getEntityResolver() {
      Object var1 = null;

      try {
         XMLEntityResolver var2 = (XMLEntityResolver)super.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
         if (var2 != null) {
            if (var2 instanceof EntityResolverWrapper) {
               var1 = ((EntityResolverWrapper)var2).getEntityResolver();
            } else if (var2 instanceof EntityResolver2Wrapper) {
               var1 = ((EntityResolver2Wrapper)var2).getEntityResolver();
            }
         }
      } catch (XMLConfigurationException var3) {
      }

      return (EntityResolver)var1;
   }

   public void setErrorHandler(ErrorHandler var1) {
      try {
         XMLErrorHandler var2 = (XMLErrorHandler)super.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
         if (var2 instanceof ErrorHandlerWrapper) {
            ErrorHandlerWrapper var3 = (ErrorHandlerWrapper)var2;
            var3.setErrorHandler(var1);
         } else {
            super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", new ErrorHandlerWrapper(var1));
         }
      } catch (XMLConfigurationException var4) {
      }

   }

   public ErrorHandler getErrorHandler() {
      ErrorHandler var1 = null;

      try {
         XMLErrorHandler var2 = (XMLErrorHandler)super.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
         if (var2 != null && var2 instanceof ErrorHandlerWrapper) {
            var1 = ((ErrorHandlerWrapper)var2).getErrorHandler();
         }
      } catch (XMLConfigurationException var3) {
      }

      return var1;
   }

   public void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      try {
         if (var1.equals("http://xml.org/sax/features/use-entity-resolver2")) {
            if (var2 != this.fUseEntityResolver2) {
               this.fUseEntityResolver2 = var2;
               this.setEntityResolver(this.getEntityResolver());
            }

         } else {
            super.fConfiguration.setFeature(var1, var2);
         }
      } catch (XMLConfigurationException var5) {
         String var4 = var5.getIdentifier();
         if (var5.getType() == 0) {
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{var4}));
         } else {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "feature-not-supported", new Object[]{var4}));
         }
      }
   }

   public boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      try {
         return var1.equals("http://xml.org/sax/features/use-entity-resolver2") ? this.fUseEntityResolver2 : super.fConfiguration.getFeature(var1);
      } catch (XMLConfigurationException var4) {
         String var3 = var4.getIdentifier();
         if (var4.getType() == 0) {
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{var3}));
         } else {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "feature-not-supported", new Object[]{var3}));
         }
      }
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      try {
         super.fConfiguration.setProperty(var1, var2);
      } catch (XMLConfigurationException var5) {
         String var4 = var5.getIdentifier();
         if (var5.getType() == 0) {
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-recognized", new Object[]{var4}));
         } else {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-supported", new Object[]{var4}));
         }
      }
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1.equals("http://apache.org/xml/properties/dom/current-element-node")) {
         boolean var2 = false;

         try {
            var2 = this.getFeature("http://apache.org/xml/features/dom/defer-node-expansion");
         } catch (XMLConfigurationException var4) {
         }

         if (var2) {
            throw new SAXNotSupportedException("Current element node cannot be queried when node expansion is deferred.");
         } else {
            return super.fCurrentNode != null && super.fCurrentNode.getNodeType() == 1 ? super.fCurrentNode : null;
         }
      } else {
         try {
            return super.fConfiguration.getProperty(var1);
         } catch (XMLConfigurationException var5) {
            String var3 = var5.getIdentifier();
            if (var5.getType() == 0) {
               throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-recognized", new Object[]{var3}));
            } else {
               throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-supported", new Object[]{var3}));
            }
         }
      }
   }

   public XMLParserConfiguration getXMLParserConfiguration() {
      return super.fConfiguration;
   }
}
