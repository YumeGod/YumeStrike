package org.apache.xalan.xsltc.trax;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.DOMWSFilter;
import org.apache.xalan.xsltc.dom.SAXImpl;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xalan.xsltc.runtime.output.TransletOutputHandlerFactory;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.XMLReaderManager;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

public final class TransformerImpl extends Transformer implements DOMCache, ErrorListener {
   private static final String EMPTY_STRING = "";
   private static final String NO_STRING = "no";
   private static final String YES_STRING = "yes";
   private static final String XML_STRING = "xml";
   private static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
   private static final String NAMESPACE_FEATURE = "http://xml.org/sax/features/namespaces";
   private AbstractTranslet _translet;
   private String _method;
   private String _encoding;
   private String _sourceSystemId;
   private ErrorListener _errorListener;
   private URIResolver _uriResolver;
   private Properties _properties;
   private Properties _propertiesClone;
   private TransletOutputHandlerFactory _tohFactory;
   private DOM _dom;
   private int _indentNumber;
   private TransformerFactoryImpl _tfactory;
   private OutputStream _ostream;
   private XSLTCDTMManager _dtmManager;
   private XMLReaderManager _readerManager;
   private boolean _isIdentity;
   private boolean _isSecureProcessing;
   private Hashtable _parameters;

   protected TransformerImpl(Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory) {
      this((Translet)null, outputProperties, indentNumber, tfactory);
      this._isIdentity = true;
   }

   protected TransformerImpl(Translet translet, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory) {
      this._translet = null;
      this._method = null;
      this._encoding = null;
      this._sourceSystemId = null;
      this._errorListener = this;
      this._uriResolver = null;
      this._tohFactory = null;
      this._dom = null;
      this._tfactory = null;
      this._ostream = null;
      this._dtmManager = null;
      this._readerManager = XMLReaderManager.getInstance();
      this._isIdentity = false;
      this._isSecureProcessing = false;
      this._parameters = null;
      this._translet = (AbstractTranslet)translet;
      this._properties = this.createOutputProperties(outputProperties);
      this._propertiesClone = (Properties)this._properties.clone();
      this._indentNumber = indentNumber;
      this._tfactory = tfactory;
   }

   public boolean isSecureProcessing() {
      return this._isSecureProcessing;
   }

   public void setSecureProcessing(boolean flag) {
      this._isSecureProcessing = flag;
   }

   protected AbstractTranslet getTranslet() {
      return this._translet;
   }

   public boolean isIdentity() {
      return this._isIdentity;
   }

   public void transform(Source source, Result result) throws TransformerException {
      if (!this._isIdentity) {
         if (this._translet == null) {
            ErrorMsg err = new ErrorMsg("JAXP_NO_TRANSLET_ERR");
            throw new TransformerException(err.toString());
         }

         this.transferOutputProperties(this._translet);
      }

      SerializationHandler toHandler = this.getOutputHandler(result);
      if (toHandler == null) {
         ErrorMsg err = new ErrorMsg("JAXP_NO_HANDLER_ERR");
         throw new TransformerException(err.toString());
      } else {
         if (this._uriResolver != null && !this._isIdentity) {
            this._translet.setDOMCache(this);
         }

         if (this._isIdentity) {
            this.transferOutputProperties(toHandler);
         }

         this.transform(source, toHandler, this._encoding);
         if (result instanceof DOMResult) {
            ((DOMResult)result).setNode(this._tohFactory.getNode());
         }

      }
   }

   public SerializationHandler getOutputHandler(Result result) throws TransformerException {
      this._method = (String)this._properties.get("method");
      this._encoding = this._properties.getProperty("encoding");
      this._tohFactory = TransletOutputHandlerFactory.newInstance();
      this._tohFactory.setEncoding(this._encoding);
      if (this._method != null) {
         this._tohFactory.setOutputMethod(this._method);
      }

      if (this._indentNumber >= 0) {
         this._tohFactory.setIndentNumber(this._indentNumber);
      }

      try {
         if (result instanceof SAXResult) {
            SAXResult target = (SAXResult)result;
            ContentHandler handler = target.getHandler();
            this._tohFactory.setHandler(handler);
            LexicalHandler lexicalHandler = target.getLexicalHandler();
            if (lexicalHandler != null) {
               this._tohFactory.setLexicalHandler(lexicalHandler);
            }

            this._tohFactory.setOutputType(1);
            return this._tohFactory.getSerializationHandler();
         } else if (result instanceof DOMResult) {
            this._tohFactory.setNode(((DOMResult)result).getNode());
            this._tohFactory.setNextSibling(((DOMResult)result).getNextSibling());
            this._tohFactory.setOutputType(2);
            return this._tohFactory.getSerializationHandler();
         } else if (result instanceof StreamResult) {
            StreamResult target = (StreamResult)result;
            this._tohFactory.setOutputType(0);
            Writer writer = target.getWriter();
            if (writer != null) {
               this._tohFactory.setWriter(writer);
               return this._tohFactory.getSerializationHandler();
            } else {
               OutputStream ostream = target.getOutputStream();
               if (ostream != null) {
                  this._tohFactory.setOutputStream(ostream);
                  return this._tohFactory.getSerializationHandler();
               } else {
                  String systemId = result.getSystemId();
                  if (systemId == null) {
                     ErrorMsg err = new ErrorMsg("JAXP_NO_RESULT_ERR");
                     throw new TransformerException(err.toString());
                  } else {
                     URL url = null;
                     if (systemId.startsWith("file:")) {
                        url = new URL(systemId);
                        this._tohFactory.setOutputStream(this._ostream = new FileOutputStream(url.getFile()));
                        return this._tohFactory.getSerializationHandler();
                     } else if (systemId.startsWith("http:")) {
                        url = new URL(systemId);
                        URLConnection connection = url.openConnection();
                        this._tohFactory.setOutputStream(this._ostream = connection.getOutputStream());
                        return this._tohFactory.getSerializationHandler();
                     } else {
                        url = (new File(systemId)).toURL();
                        this._tohFactory.setOutputStream(this._ostream = new FileOutputStream(url.getFile()));
                        return this._tohFactory.getSerializationHandler();
                     }
                  }
               }
            }
         } else {
            return null;
         }
      } catch (UnknownServiceException var8) {
         throw new TransformerException(var8);
      } catch (ParserConfigurationException var9) {
         throw new TransformerException(var9);
      } catch (IOException var10) {
         throw new TransformerException(var10);
      }
   }

   protected void setDOM(DOM dom) {
      this._dom = dom;
   }

   private DOM getDOM(Source source) throws TransformerException {
      try {
         DOM dom = null;
         if (source != null) {
            DOMWSFilter wsfilter;
            if (this._translet != null && this._translet instanceof StripFilter) {
               wsfilter = new DOMWSFilter(this._translet);
            } else {
               wsfilter = null;
            }

            boolean hasIdCall = this._translet != null ? this._translet.hasIdCall() : false;
            if (this._dtmManager == null) {
               this._dtmManager = (XSLTCDTMManager)this._tfactory.getDTMManagerClass().newInstance();
            }

            dom = (DOM)this._dtmManager.getDTM(source, false, wsfilter, true, false, false, 0, hasIdCall);
         } else {
            if (this._dom == null) {
               return null;
            }

            dom = this._dom;
            this._dom = null;
         }

         if (!this._isIdentity) {
            this._translet.prepassDocument(dom);
         }

         return dom;
      } catch (Exception var5) {
         if (this._errorListener != null) {
            this.postErrorToListener(var5.getMessage());
         }

         throw new TransformerException(var5);
      }
   }

   protected TransformerFactoryImpl getTransformerFactory() {
      return this._tfactory;
   }

   protected TransletOutputHandlerFactory getTransletOutputHandlerFactory() {
      return this._tohFactory;
   }

   private void transformIdentity(Source source, SerializationHandler handler) throws Exception {
      if (source != null) {
         this._sourceSystemId = source.getSystemId();
      }

      if (source instanceof StreamSource) {
         StreamSource stream = (StreamSource)source;
         InputStream streamInput = stream.getInputStream();
         Reader streamReader = stream.getReader();
         XMLReader reader = this._readerManager.getXMLReader();

         try {
            try {
               reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
            } catch (SAXException var22) {
            }

            reader.setContentHandler(handler);
            InputSource input;
            if (streamInput != null) {
               input = new InputSource(streamInput);
               input.setSystemId(this._sourceSystemId);
            } else if (streamReader != null) {
               input = new InputSource(streamReader);
               input.setSystemId(this._sourceSystemId);
            } else {
               if (this._sourceSystemId == null) {
                  ErrorMsg err = new ErrorMsg("JAXP_NO_SOURCE_ERR");
                  throw new TransformerException(err.toString());
               }

               input = new InputSource(this._sourceSystemId);
            }

            reader.parse(input);
         } finally {
            this._readerManager.releaseXMLReader(reader);
         }
      } else if (source instanceof SAXSource) {
         SAXSource sax = (SAXSource)source;
         XMLReader reader = sax.getXMLReader();
         InputSource input = sax.getInputSource();
         boolean userReader = true;

         try {
            if (reader == null) {
               reader = this._readerManager.getXMLReader();
               userReader = false;
            }

            try {
               reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
            } catch (SAXException var21) {
            }

            reader.setContentHandler(handler);
            reader.parse(input);
         } finally {
            if (!userReader) {
               this._readerManager.releaseXMLReader(reader);
            }

         }
      } else if (source instanceof DOMSource) {
         DOMSource domsrc = (DOMSource)source;
         (new DOM2TO(domsrc.getNode(), handler)).parse();
      } else {
         if (!(source instanceof XSLTCSource)) {
            ErrorMsg err = new ErrorMsg("JAXP_NO_SOURCE_ERR");
            throw new TransformerException(err.toString());
         }

         DOM dom = ((XSLTCSource)source).getDOM((XSLTCDTMManager)null, this._translet);
         ((SAXImpl)dom).copy(handler);
      }

   }

   private void transform(Source source, SerializationHandler handler, String encoding) throws TransformerException {
      try {
         if (source instanceof StreamSource && ((Source)source).getSystemId() == null && ((StreamSource)source).getInputStream() == null && ((StreamSource)source).getReader() == null || source instanceof SAXSource && ((SAXSource)source).getInputSource() == null && ((SAXSource)source).getXMLReader() == null || source instanceof DOMSource && ((DOMSource)source).getNode() == null) {
            DocumentBuilderFactory builderF = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderF.newDocumentBuilder();
            String systemID = ((Source)source).getSystemId();
            source = new DOMSource(builder.newDocument());
            if (systemID != null) {
               ((Source)source).setSystemId(systemID);
            }
         }

         if (this._isIdentity) {
            this.transformIdentity((Source)source, handler);
         } else {
            this._translet.transform(this.getDOM((Source)source), handler);
         }
      } catch (TransletException var15) {
         if (this._errorListener != null) {
            this.postErrorToListener(var15.getMessage());
         }

         throw new TransformerException(var15);
      } catch (RuntimeException var16) {
         if (this._errorListener != null) {
            this.postErrorToListener(var16.getMessage());
         }

         throw new TransformerException(var16);
      } catch (Exception var17) {
         if (this._errorListener != null) {
            this.postErrorToListener(var17.getMessage());
         }

         throw new TransformerException(var17);
      } finally {
         this._dtmManager = null;
      }

      if (this._ostream != null) {
         try {
            this._ostream.close();
         } catch (IOException var14) {
         }

         this._ostream = null;
      }

   }

   public ErrorListener getErrorListener() {
      return this._errorListener;
   }

   public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
      if (listener == null) {
         ErrorMsg err = new ErrorMsg("ERROR_LISTENER_NULL_ERR", "Transformer");
         throw new IllegalArgumentException(err.toString());
      } else {
         this._errorListener = listener;
         if (this._translet != null) {
            this._translet.setMessageHandler(new MessageHandler(this._errorListener));
         }

      }
   }

   private void postErrorToListener(String message) {
      try {
         this._errorListener.error(new TransformerException(message));
      } catch (TransformerException var3) {
      }

   }

   private void postWarningToListener(String message) {
      try {
         this._errorListener.warning(new TransformerException(message));
      } catch (TransformerException var3) {
      }

   }

   private String makeCDATAString(Hashtable cdata) {
      if (cdata == null) {
         return null;
      } else {
         StringBuffer result = new StringBuffer();
         Enumeration elements = cdata.keys();
         if (elements.hasMoreElements()) {
            result.append((String)elements.nextElement());

            while(elements.hasMoreElements()) {
               String element = (String)elements.nextElement();
               result.append(' ');
               result.append(element);
            }
         }

         return result.toString();
      }
   }

   public Properties getOutputProperties() {
      return (Properties)this._properties.clone();
   }

   public String getOutputProperty(String name) throws IllegalArgumentException {
      if (!this.validOutputProperty(name)) {
         ErrorMsg err = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", name);
         throw new IllegalArgumentException(err.toString());
      } else {
         return this._properties.getProperty(name);
      }
   }

   public void setOutputProperties(Properties properties) throws IllegalArgumentException {
      if (properties != null) {
         Enumeration names = properties.propertyNames();

         while(names.hasMoreElements()) {
            String name = (String)names.nextElement();
            if (!this.isDefaultProperty(name, properties)) {
               if (!this.validOutputProperty(name)) {
                  ErrorMsg err = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", name);
                  throw new IllegalArgumentException(err.toString());
               }

               this._properties.setProperty(name, properties.getProperty(name));
            }
         }
      } else {
         this._properties = this._propertiesClone;
      }

   }

   public void setOutputProperty(String name, String value) throws IllegalArgumentException {
      if (!this.validOutputProperty(name)) {
         ErrorMsg err = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", name);
         throw new IllegalArgumentException(err.toString());
      } else {
         this._properties.setProperty(name, value);
      }
   }

   private void transferOutputProperties(AbstractTranslet translet) {
      if (this._properties != null) {
         Enumeration names = this._properties.propertyNames();

         while(true) {
            while(true) {
               String name;
               String value;
               do {
                  if (!names.hasMoreElements()) {
                     return;
                  }

                  name = (String)names.nextElement();
                  value = (String)this._properties.get(name);
               } while(value == null);

               if (name.equals("encoding")) {
                  translet._encoding = value;
               } else if (name.equals("method")) {
                  translet._method = value;
               } else if (name.equals("doctype-public")) {
                  translet._doctypePublic = value;
               } else if (name.equals("doctype-system")) {
                  translet._doctypeSystem = value;
               } else if (name.equals("media-type")) {
                  translet._mediaType = value;
               } else if (name.equals("standalone")) {
                  translet._standalone = value;
               } else if (name.equals("version")) {
                  translet._version = value;
               } else if (name.equals("omit-xml-declaration")) {
                  translet._omitHeader = value != null && value.toLowerCase().equals("yes");
               } else if (!name.equals("indent")) {
                  if (name.equals("cdata-section-elements") && value != null) {
                     translet._cdata = null;
                     StringTokenizer e = new StringTokenizer(value);

                     while(e.hasMoreTokens()) {
                        translet.addCdataElement(e.nextToken());
                     }
                  }
               } else {
                  translet._indent = value != null && value.toLowerCase().equals("yes");
               }
            }
         }
      }
   }

   public void transferOutputProperties(SerializationHandler handler) {
      if (this._properties != null) {
         String doctypePublic = null;
         String doctypeSystem = null;
         Enumeration names = this._properties.propertyNames();

         while(true) {
            while(true) {
               String name;
               String value;
               do {
                  if (!names.hasMoreElements()) {
                     if (doctypePublic != null || doctypeSystem != null) {
                        handler.setDoctype(doctypeSystem, doctypePublic);
                     }

                     return;
                  }

                  name = (String)names.nextElement();
                  value = (String)this._properties.get(name);
               } while(value == null);

               if (name.equals("doctype-public")) {
                  doctypePublic = value;
               } else if (name.equals("doctype-system")) {
                  doctypeSystem = value;
               } else if (name.equals("media-type")) {
                  handler.setMediaType(value);
               } else if (name.equals("standalone")) {
                  handler.setStandalone(value);
               } else if (name.equals("version")) {
                  handler.setVersion(value);
               } else if (name.equals("omit-xml-declaration")) {
                  handler.setOmitXMLDeclaration(value != null && value.toLowerCase().equals("yes"));
               } else if (name.equals("indent")) {
                  handler.setIndent(value != null && value.toLowerCase().equals("yes"));
               } else if (name.equals("cdata-section-elements") && value != null) {
                  StringTokenizer e = new StringTokenizer(value);
                  Vector uriAndLocalNames = null;

                  while(e.hasMoreTokens()) {
                     String token = e.nextToken();
                     int lastcolon = token.lastIndexOf(58);
                     String uri;
                     String localName;
                     if (lastcolon > 0) {
                        uri = token.substring(0, lastcolon);
                        localName = token.substring(lastcolon + 1);
                     } else {
                        uri = null;
                        localName = token;
                     }

                     if (uriAndLocalNames == null) {
                        uriAndLocalNames = new Vector();
                     }

                     uriAndLocalNames.addElement(uri);
                     uriAndLocalNames.addElement(localName);
                  }

                  handler.setCdataSectionElements(uriAndLocalNames);
               }
            }
         }
      }
   }

   private Properties createOutputProperties(Properties outputProperties) {
      Properties defaults = new Properties();
      this.setDefaults(defaults, "xml");
      Properties base = new Properties(defaults);
      if (outputProperties != null) {
         Enumeration names = outputProperties.propertyNames();

         while(names.hasMoreElements()) {
            String name = (String)names.nextElement();
            base.setProperty(name, outputProperties.getProperty(name));
         }
      } else {
         base.setProperty("encoding", this._translet._encoding);
         if (this._translet._method != null) {
            base.setProperty("method", this._translet._method);
         }
      }

      String method = base.getProperty("method");
      if (method != null) {
         if (method.equals("html")) {
            this.setDefaults(defaults, "html");
         } else if (method.equals("text")) {
            this.setDefaults(defaults, "text");
         }
      }

      return base;
   }

   private void setDefaults(Properties props, String method) {
      Properties method_props = OutputPropertiesFactory.getDefaultMethodProperties(method);
      Enumeration names = method_props.propertyNames();

      while(names.hasMoreElements()) {
         String name = (String)names.nextElement();
         props.setProperty(name, method_props.getProperty(name));
      }

   }

   private boolean validOutputProperty(String name) {
      return name.equals("encoding") || name.equals("method") || name.equals("indent") || name.equals("doctype-public") || name.equals("doctype-system") || name.equals("cdata-section-elements") || name.equals("media-type") || name.equals("omit-xml-declaration") || name.equals("standalone") || name.equals("version") || name.charAt(0) == '{';
   }

   private boolean isDefaultProperty(String name, Properties properties) {
      return properties.get(name) == null;
   }

   public void setParameter(String name, Object value) {
      if (value == null) {
         ErrorMsg err = new ErrorMsg("JAXP_INVALID_SET_PARAM_VALUE", name);
         throw new IllegalArgumentException(err.toString());
      } else {
         if (this._isIdentity) {
            if (this._parameters == null) {
               this._parameters = new Hashtable();
            }

            this._parameters.put(name, value);
         } else {
            this._translet.addParameter(name, value);
         }

      }
   }

   public void clearParameters() {
      if (this._isIdentity && this._parameters != null) {
         this._parameters.clear();
      } else {
         this._translet.clearParameters();
      }

   }

   public final Object getParameter(String name) {
      if (this._isIdentity) {
         return this._parameters != null ? this._parameters.get(name) : null;
      } else {
         return this._translet.getParameter(name);
      }
   }

   public URIResolver getURIResolver() {
      return this._uriResolver;
   }

   public void setURIResolver(URIResolver resolver) {
      this._uriResolver = resolver;
   }

   public DOM retrieveDocument(String baseURI, String href, Translet translet) {
      try {
         if (href.length() == 0) {
            href = new String(baseURI);
         }

         Source resolvedSource = this._uriResolver.resolve(href, baseURI);
         if (resolvedSource == null) {
            StreamSource streamSource = new StreamSource(SystemIDResolver.getAbsoluteURI(href, baseURI));
            return this.getDOM(streamSource);
         } else {
            return this.getDOM(resolvedSource);
         }
      } catch (TransformerException var6) {
         if (this._errorListener != null) {
            this.postErrorToListener("File not found: " + var6.getMessage());
         }

         return null;
      }
   }

   public void error(TransformerException e) throws TransformerException {
      Throwable wrapped = e.getException();
      if (wrapped != null) {
         System.err.println(new ErrorMsg("ERROR_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
      } else {
         System.err.println(new ErrorMsg("ERROR_MSG", e.getMessageAndLocation()));
      }

      throw e;
   }

   public void fatalError(TransformerException e) throws TransformerException {
      Throwable wrapped = e.getException();
      if (wrapped != null) {
         System.err.println(new ErrorMsg("FATAL_ERR_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
      } else {
         System.err.println(new ErrorMsg("FATAL_ERR_MSG", e.getMessageAndLocation()));
      }

      throw e;
   }

   public void warning(TransformerException e) throws TransformerException {
      Throwable wrapped = e.getException();
      if (wrapped != null) {
         System.err.println(new ErrorMsg("WARNING_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
      } else {
         System.err.println(new ErrorMsg("WARNING_MSG", e.getMessageAndLocation()));
      }

   }

   public void reset() {
      this._method = null;
      this._encoding = null;
      this._sourceSystemId = null;
      this._errorListener = this;
      this._uriResolver = null;
      this._dom = null;
      this._parameters = null;
      this._indentNumber = 0;
      this.setOutputProperties((Properties)null);
   }

   static class MessageHandler extends org.apache.xalan.xsltc.runtime.MessageHandler {
      private ErrorListener _errorListener;

      public MessageHandler(ErrorListener errorListener) {
         this._errorListener = errorListener;
      }

      public void displayMessage(String msg) {
         if (this._errorListener == null) {
            System.err.println(msg);
         } else {
            try {
               this._errorListener.warning(new TransformerException(msg));
            } catch (TransformerException var3) {
            }
         }

      }
   }
}
