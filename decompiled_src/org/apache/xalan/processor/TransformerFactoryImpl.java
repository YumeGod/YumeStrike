package org.apache.xalan.processor;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TrAXFilter;
import org.apache.xalan.transformer.TransformerIdentityImpl;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.DOM2Helper;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.StopParseException;
import org.apache.xml.utils.StylesheetPIHandler;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.TreeWalker;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class TransformerFactoryImpl extends SAXTransformerFactory {
   public static final String XSLT_PROPERTIES = "org/apache/xalan/res/XSLTInfo.properties";
   private boolean m_isSecureProcessing = false;
   public static final String FEATURE_INCREMENTAL = "http://xml.apache.org/xalan/features/incremental";
   public static final String FEATURE_OPTIMIZE = "http://xml.apache.org/xalan/features/optimize";
   public static final String FEATURE_SOURCE_LOCATION = "http://xml.apache.org/xalan/properties/source-location";
   private String m_DOMsystemID = null;
   private boolean m_optimize = true;
   private boolean m_source_location = false;
   private boolean m_incremental = false;
   URIResolver m_uriResolver;
   private ErrorListener m_errorListener = new DefaultErrorHandler(false);

   public Templates processFromNode(Node node) throws TransformerConfigurationException {
      try {
         TemplatesHandler builder = this.newTemplatesHandler();
         TreeWalker walker = new TreeWalker(builder, new DOM2Helper(), builder.getSystemId());
         walker.traverse(node);
         return builder.getTemplates();
      } catch (SAXException var11) {
         SAXException se = var11;
         if (this.m_errorListener != null) {
            try {
               this.m_errorListener.fatalError(new TransformerException(se));
               return null;
            } catch (TransformerConfigurationException var7) {
               throw var7;
            } catch (TransformerException var8) {
               throw new TransformerConfigurationException(var8);
            }
         } else {
            throw new TransformerConfigurationException(XSLMessages.createMessage("ER_PROCESSFROMNODE_FAILED", (Object[])null), var11);
         }
      } catch (TransformerConfigurationException var12) {
         throw var12;
      } catch (Exception var13) {
         Exception e = var13;
         if (this.m_errorListener != null) {
            try {
               this.m_errorListener.fatalError(new TransformerException(e));
               return null;
            } catch (TransformerConfigurationException var9) {
               throw var9;
            } catch (TransformerException var10) {
               throw new TransformerConfigurationException(var10);
            }
         } else {
            throw new TransformerConfigurationException(XSLMessages.createMessage("ER_PROCESSFROMNODE_FAILED", (Object[])null), var13);
         }
      }
   }

   String getDOMsystemID() {
      return this.m_DOMsystemID;
   }

   Templates processFromNode(Node node, String systemID) throws TransformerConfigurationException {
      this.m_DOMsystemID = systemID;
      return this.processFromNode(node);
   }

   public Source getAssociatedStylesheet(Source source, String media, String title, String charset) throws TransformerConfigurationException {
      InputSource isource = null;
      Node node = null;
      XMLReader reader = null;
      String baseID;
      if (source instanceof DOMSource) {
         DOMSource dsource = (DOMSource)source;
         node = dsource.getNode();
         baseID = dsource.getSystemId();
      } else {
         isource = SAXSource.sourceToInputSource(source);
         baseID = isource.getSystemId();
      }

      StylesheetPIHandler handler = new StylesheetPIHandler(baseID, media, title, charset);
      if (this.m_uriResolver != null) {
         handler.setURIResolver(this.m_uriResolver);
      }

      try {
         if (null != node) {
            TreeWalker walker = new TreeWalker(handler, new DOM2Helper(), baseID);
            walker.traverse(node);
         } else {
            try {
               SAXParserFactory factory = SAXParserFactory.newInstance();
               factory.setNamespaceAware(true);
               if (this.m_isSecureProcessing) {
                  try {
                     factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                  } catch (SAXException var14) {
                  }
               }

               SAXParser jaxpParser = factory.newSAXParser();
               reader = jaxpParser.getXMLReader();
            } catch (ParserConfigurationException var15) {
               throw new SAXException(var15);
            } catch (FactoryConfigurationError var16) {
               throw new SAXException(var16.toString());
            } catch (NoSuchMethodError var17) {
            } catch (AbstractMethodError var18) {
            }

            if (null == reader) {
               reader = XMLReaderFactory.createXMLReader();
            }

            reader.setContentHandler(handler);
            reader.parse(isource);
         }
      } catch (StopParseException var19) {
      } catch (SAXException var20) {
         throw new TransformerConfigurationException("getAssociatedStylesheets failed", var20);
      } catch (IOException var21) {
         throw new TransformerConfigurationException("getAssociatedStylesheets failed", var21);
      }

      return handler.getAssociatedStylesheet();
   }

   public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
      return new StylesheetHandler(this);
   }

   public void setFeature(String name, boolean value) throws TransformerConfigurationException {
      if (name == null) {
         throw new NullPointerException(XSLMessages.createMessage("ER_SET_FEATURE_NULL_NAME", (Object[])null));
      } else if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
         this.m_isSecureProcessing = value;
      } else {
         throw new TransformerConfigurationException(XSLMessages.createMessage("ER_UNSUPPORTED_FEATURE", new Object[]{name}));
      }
   }

   public boolean getFeature(String name) {
      if (name == null) {
         throw new NullPointerException(XSLMessages.createMessage("ER_GET_FEATURE_NULL_NAME", (Object[])null));
      } else if ("http://javax.xml.transform.dom.DOMResult/feature" != name && "http://javax.xml.transform.dom.DOMSource/feature" != name && "http://javax.xml.transform.sax.SAXResult/feature" != name && "http://javax.xml.transform.sax.SAXSource/feature" != name && "http://javax.xml.transform.stream.StreamResult/feature" != name && "http://javax.xml.transform.stream.StreamSource/feature" != name && "http://javax.xml.transform.sax.SAXTransformerFactory/feature" != name && "http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter" != name) {
         if (!"http://javax.xml.transform.dom.DOMResult/feature".equals(name) && !"http://javax.xml.transform.dom.DOMSource/feature".equals(name) && !"http://javax.xml.transform.sax.SAXResult/feature".equals(name) && !"http://javax.xml.transform.sax.SAXSource/feature".equals(name) && !"http://javax.xml.transform.stream.StreamResult/feature".equals(name) && !"http://javax.xml.transform.stream.StreamSource/feature".equals(name) && !"http://javax.xml.transform.sax.SAXTransformerFactory/feature".equals(name) && !"http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter".equals(name)) {
            return name.equals("http://javax.xml.XMLConstants/feature/secure-processing") ? this.m_isSecureProcessing : false;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   public void setAttribute(String name, Object value) throws IllegalArgumentException {
      if (name.equals("http://xml.apache.org/xalan/features/incremental")) {
         if (value instanceof Boolean) {
            this.m_incremental = (Boolean)value;
         } else {
            if (!(value instanceof String)) {
               throw new IllegalArgumentException(XSLMessages.createMessage("ER_BAD_VALUE", new Object[]{name, value}));
            }

            this.m_incremental = new Boolean((String)value);
         }
      } else if (name.equals("http://xml.apache.org/xalan/features/optimize")) {
         if (value instanceof Boolean) {
            this.m_optimize = (Boolean)value;
         } else {
            if (!(value instanceof String)) {
               throw new IllegalArgumentException(XSLMessages.createMessage("ER_BAD_VALUE", new Object[]{name, value}));
            }

            this.m_optimize = new Boolean((String)value);
         }
      } else {
         if (!name.equals("http://xml.apache.org/xalan/properties/source-location")) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_NOT_SUPPORTED", new Object[]{name}));
         }

         if (value instanceof Boolean) {
            this.m_source_location = (Boolean)value;
         } else {
            if (!(value instanceof String)) {
               throw new IllegalArgumentException(XSLMessages.createMessage("ER_BAD_VALUE", new Object[]{name, value}));
            }

            this.m_source_location = new Boolean((String)value);
         }
      }

   }

   public Object getAttribute(String name) throws IllegalArgumentException {
      if (name.equals("http://xml.apache.org/xalan/features/incremental")) {
         return new Boolean(this.m_incremental);
      } else if (name.equals("http://xml.apache.org/xalan/features/optimize")) {
         return new Boolean(this.m_optimize);
      } else if (name.equals("http://xml.apache.org/xalan/properties/source-location")) {
         return new Boolean(this.m_source_location);
      } else {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_ATTRIB_VALUE_NOT_RECOGNIZED", new Object[]{name}));
      }
   }

   public XMLFilter newXMLFilter(Source src) throws TransformerConfigurationException {
      Templates templates = this.newTemplates(src);
      return templates == null ? null : this.newXMLFilter(templates);
   }

   public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
      try {
         return new TrAXFilter(templates);
      } catch (TransformerConfigurationException var7) {
         TransformerConfigurationException ex = var7;
         if (this.m_errorListener != null) {
            try {
               this.m_errorListener.fatalError(ex);
               return null;
            } catch (TransformerConfigurationException var5) {
               throw var5;
            } catch (TransformerException var6) {
               throw new TransformerConfigurationException(var6);
            }
         } else {
            throw var7;
         }
      }
   }

   public TransformerHandler newTransformerHandler(Source src) throws TransformerConfigurationException {
      Templates templates = this.newTemplates(src);
      return templates == null ? null : this.newTransformerHandler(templates);
   }

   public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
      try {
         TransformerImpl transformer = (TransformerImpl)templates.newTransformer();
         transformer.setURIResolver(this.m_uriResolver);
         TransformerHandler th = (TransformerHandler)transformer.getInputContentHandler(true);
         return th;
      } catch (TransformerConfigurationException var7) {
         TransformerConfigurationException ex = var7;
         if (this.m_errorListener != null) {
            try {
               this.m_errorListener.fatalError(ex);
               return null;
            } catch (TransformerConfigurationException var5) {
               throw var5;
            } catch (TransformerException var6) {
               throw new TransformerConfigurationException(var6);
            }
         } else {
            throw var7;
         }
      }
   }

   public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
      return new TransformerIdentityImpl(this.m_isSecureProcessing);
   }

   public Transformer newTransformer(Source source) throws TransformerConfigurationException {
      try {
         Templates tmpl = this.newTemplates(source);
         if (tmpl == null) {
            return null;
         } else {
            Transformer transformer = tmpl.newTransformer();
            transformer.setURIResolver(this.m_uriResolver);
            return transformer;
         }
      } catch (TransformerConfigurationException var7) {
         TransformerConfigurationException ex = var7;
         if (this.m_errorListener != null) {
            try {
               this.m_errorListener.fatalError(ex);
               return null;
            } catch (TransformerConfigurationException var5) {
               throw var5;
            } catch (TransformerException var6) {
               throw new TransformerConfigurationException(var6);
            }
         } else {
            throw var7;
         }
      }
   }

   public Transformer newTransformer() throws TransformerConfigurationException {
      return new TransformerIdentityImpl(this.m_isSecureProcessing);
   }

   public Templates newTemplates(Source source) throws TransformerConfigurationException {
      String baseID = source.getSystemId();
      if (null != baseID) {
         baseID = SystemIDResolver.getAbsoluteURI(baseID);
      }

      if (source instanceof DOMSource) {
         DOMSource dsource = (DOMSource)source;
         Node node = dsource.getNode();
         if (null != node) {
            return this.processFromNode(node, baseID);
         } else {
            String messageStr = XSLMessages.createMessage("ER_ILLEGAL_DOMSOURCE_INPUT", (Object[])null);
            throw new IllegalArgumentException(messageStr);
         }
      } else {
         TemplatesHandler builder = this.newTemplatesHandler();
         builder.setSystemId(baseID);

         try {
            InputSource isource = SAXSource.sourceToInputSource(source);
            isource.setSystemId(baseID);
            XMLReader reader = null;
            if (source instanceof SAXSource) {
               reader = ((SAXSource)source).getXMLReader();
            }

            if (null == reader) {
               try {
                  SAXParserFactory factory = SAXParserFactory.newInstance();
                  factory.setNamespaceAware(true);
                  if (this.m_isSecureProcessing) {
                     try {
                        factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                     } catch (SAXException var14) {
                     }
                  }

                  SAXParser jaxpParser = factory.newSAXParser();
                  reader = jaxpParser.getXMLReader();
               } catch (ParserConfigurationException var15) {
                  throw new SAXException(var15);
               } catch (FactoryConfigurationError var16) {
                  throw new SAXException(var16.toString());
               } catch (NoSuchMethodError var17) {
               } catch (AbstractMethodError var18) {
               }
            }

            if (null == reader) {
               reader = XMLReaderFactory.createXMLReader();
            }

            reader.setContentHandler(builder);
            reader.parse(isource);
         } catch (SAXException var19) {
            SAXException se = var19;
            if (this.m_errorListener == null) {
               throw new TransformerConfigurationException(var19.getMessage(), var19);
            }

            try {
               this.m_errorListener.fatalError(new TransformerException(se));
            } catch (TransformerConfigurationException var12) {
               throw var12;
            } catch (TransformerException var13) {
               throw new TransformerConfigurationException(var13);
            }
         } catch (Exception var20) {
            Exception e = var20;
            if (this.m_errorListener != null) {
               try {
                  this.m_errorListener.fatalError(new TransformerException(e));
                  return null;
               } catch (TransformerConfigurationException var10) {
                  throw var10;
               } catch (TransformerException var11) {
                  throw new TransformerConfigurationException(var11);
               }
            }

            throw new TransformerConfigurationException(var20.getMessage(), var20);
         }

         return builder.getTemplates();
      }
   }

   public void setURIResolver(URIResolver resolver) {
      this.m_uriResolver = resolver;
   }

   public URIResolver getURIResolver() {
      return this.m_uriResolver;
   }

   public ErrorListener getErrorListener() {
      return this.m_errorListener;
   }

   public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
      if (null == listener) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_ERRORLISTENER", (Object[])null));
      } else {
         this.m_errorListener = listener;
      }
   }

   public boolean isSecureProcessing() {
      return this.m_isSecureProcessing;
   }
}
