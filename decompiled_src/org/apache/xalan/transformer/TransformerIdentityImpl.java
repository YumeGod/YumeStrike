package org.apache.xalan.transformer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
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
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xml.serializer.TreeWalker;
import org.apache.xml.utils.DOMBuilder;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLReaderManager;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class TransformerIdentityImpl extends Transformer implements TransformerHandler, DeclHandler {
   boolean m_flushedStartDoc;
   private FileOutputStream m_outputStream;
   private ContentHandler m_resultContentHandler;
   private LexicalHandler m_resultLexicalHandler;
   private DTDHandler m_resultDTDHandler;
   private DeclHandler m_resultDeclHandler;
   private Serializer m_serializer;
   private Result m_result;
   private String m_systemID;
   private Hashtable m_params;
   private ErrorListener m_errorListener;
   URIResolver m_URIResolver;
   private OutputProperties m_outputFormat;
   boolean m_foundFirstElement;
   private boolean m_isSecureProcessing;

   public TransformerIdentityImpl(boolean isSecureProcessing) {
      this.m_flushedStartDoc = false;
      this.m_outputStream = null;
      this.m_errorListener = new DefaultErrorHandler(false);
      this.m_isSecureProcessing = false;
      this.m_outputFormat = new OutputProperties("xml");
      this.m_isSecureProcessing = isSecureProcessing;
   }

   public TransformerIdentityImpl() {
      this(false);
   }

   public void setResult(Result result) throws IllegalArgumentException {
      if (null == result) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_RESULT_NULL", (Object[])null));
      } else {
         this.m_result = result;
      }
   }

   public void setSystemId(String systemID) {
      this.m_systemID = systemID;
   }

   public String getSystemId() {
      return this.m_systemID;
   }

   public Transformer getTransformer() {
      return this;
   }

   public void reset() {
      this.m_flushedStartDoc = false;
      this.m_foundFirstElement = false;
      this.m_outputStream = null;
      this.m_params.clear();
      this.m_result = null;
      this.m_resultContentHandler = null;
      this.m_resultDeclHandler = null;
      this.m_resultDTDHandler = null;
      this.m_resultLexicalHandler = null;
      this.m_serializer = null;
      this.m_systemID = null;
      this.m_URIResolver = null;
      this.m_outputFormat = new OutputProperties("xml");
   }

   private void createResultContentHandler(Result outputTarget) throws TransformerException {
      if (outputTarget instanceof SAXResult) {
         SAXResult saxResult = (SAXResult)outputTarget;
         this.m_resultContentHandler = saxResult.getHandler();
         this.m_resultLexicalHandler = saxResult.getLexicalHandler();
         if (this.m_resultContentHandler instanceof Serializer) {
            this.m_serializer = (Serializer)this.m_resultContentHandler;
         }
      } else if (outputTarget instanceof DOMResult) {
         DOMResult domResult = (DOMResult)outputTarget;
         Node outputNode = domResult.getNode();
         Node nextSibling = domResult.getNextSibling();
         Document doc;
         short type;
         if (null != outputNode) {
            type = ((Node)outputNode).getNodeType();
            doc = 9 == type ? (Document)outputNode : ((Node)outputNode).getOwnerDocument();
         } else {
            try {
               DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
               dbf.setNamespaceAware(true);
               if (this.m_isSecureProcessing) {
                  try {
                     dbf.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                  } catch (ParserConfigurationException var9) {
                  }
               }

               DocumentBuilder db = dbf.newDocumentBuilder();
               doc = db.newDocument();
            } catch (ParserConfigurationException var11) {
               throw new TransformerException(var11);
            }

            outputNode = doc;
            type = doc.getNodeType();
            ((DOMResult)outputTarget).setNode(doc);
         }

         DOMBuilder domBuilder = 11 == type ? new DOMBuilder(doc, (DocumentFragment)outputNode) : new DOMBuilder(doc, (Node)outputNode);
         if (nextSibling != null) {
            domBuilder.setNextSibling(nextSibling);
         }

         this.m_resultContentHandler = domBuilder;
         this.m_resultLexicalHandler = domBuilder;
      } else {
         if (!(outputTarget instanceof StreamResult)) {
            throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_TRANSFORM_TO_RESULT_TYPE", new Object[]{outputTarget.getClass().getName()}));
         }

         StreamResult sresult = (StreamResult)outputTarget;
         String method = this.m_outputFormat.getProperty("method");

         try {
            Serializer serializer = SerializerFactory.getSerializer(this.m_outputFormat.getProperties());
            this.m_serializer = serializer;
            if (null != sresult.getWriter()) {
               serializer.setWriter(sresult.getWriter());
            } else if (null != sresult.getOutputStream()) {
               serializer.setOutputStream(sresult.getOutputStream());
            } else {
               if (null == sresult.getSystemId()) {
                  throw new TransformerException(XSLMessages.createMessage("ER_NO_OUTPUT_SPECIFIED", (Object[])null));
               }

               String fileURL = sresult.getSystemId();
               if (fileURL.startsWith("file:///")) {
                  if (fileURL.substring(8).indexOf(":") > 0) {
                     fileURL = fileURL.substring(8);
                  } else {
                     fileURL = fileURL.substring(7);
                  }
               } else if (fileURL.startsWith("file:/")) {
                  if (fileURL.substring(6).indexOf(":") > 0) {
                     fileURL = fileURL.substring(6);
                  } else {
                     fileURL = fileURL.substring(5);
                  }
               }

               this.m_outputStream = new FileOutputStream(fileURL);
               serializer.setOutputStream(this.m_outputStream);
            }

            this.m_resultContentHandler = serializer.asContentHandler();
         } catch (IOException var10) {
            throw new TransformerException(var10);
         }
      }

      if (this.m_resultContentHandler instanceof DTDHandler) {
         this.m_resultDTDHandler = (DTDHandler)this.m_resultContentHandler;
      }

      if (this.m_resultContentHandler instanceof DeclHandler) {
         this.m_resultDeclHandler = (DeclHandler)this.m_resultContentHandler;
      }

      if (this.m_resultContentHandler instanceof LexicalHandler) {
         this.m_resultLexicalHandler = (LexicalHandler)this.m_resultContentHandler;
      }

   }

   public void transform(Source source, Result outputTarget) throws TransformerException {
      this.createResultContentHandler(outputTarget);
      String data;
      if (source instanceof StreamSource && ((Source)source).getSystemId() == null && ((StreamSource)source).getInputStream() == null && ((StreamSource)source).getReader() == null || source instanceof SAXSource && ((SAXSource)source).getInputSource() == null && ((SAXSource)source).getXMLReader() == null || source instanceof DOMSource && ((DOMSource)source).getNode() == null) {
         try {
            DocumentBuilderFactory builderF = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderF.newDocumentBuilder();
            data = ((Source)source).getSystemId();
            source = new DOMSource(builder.newDocument());
            if (data != null) {
               ((Source)source).setSystemId(data);
            }
         } catch (ParserConfigurationException var60) {
            throw new TransformerException(var60.getMessage());
         }
      }

      try {
         if (source instanceof DOMSource) {
            DOMSource dsource = (DOMSource)source;
            this.m_systemID = dsource.getSystemId();
            Node dNode = dsource.getNode();
            if (null == dNode) {
               data = XSLMessages.createMessage("ER_ILLEGAL_DOMSOURCE_INPUT", (Object[])null);
               throw new IllegalArgumentException(data);
            }

            try {
               if (dNode.getNodeType() == 2) {
                  this.startDocument();
               }

               try {
                  if (dNode.getNodeType() == 2) {
                     data = dNode.getNodeValue();
                     char[] chars = data.toCharArray();
                     this.characters(chars, 0, chars.length);
                  } else {
                     TreeWalker walker = new TreeWalker(this, this.m_systemID);
                     walker.traverse(dNode);
                  }

                  return;
               } finally {
                  if (dNode.getNodeType() == 2) {
                     this.endDocument();
                  }

               }
            } catch (SAXException var62) {
               throw new TransformerException(var62);
            }
         }

         InputSource xmlSource = SAXSource.sourceToInputSource((Source)source);
         if (null == xmlSource) {
            throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_TRANSFORM_SOURCE_TYPE", new Object[]{source.getClass().getName()}));
         }

         if (null != xmlSource.getSystemId()) {
            this.m_systemID = xmlSource.getSystemId();
         }

         XMLReader reader = null;
         boolean managedReader = false;

         try {
            if (source instanceof SAXSource) {
               reader = ((SAXSource)source).getXMLReader();
            }

            if (null == reader) {
               try {
                  reader = XMLReaderManager.getInstance().getXMLReader();
                  managedReader = true;
               } catch (SAXException var59) {
                  throw new TransformerException(var59);
               }
            } else {
               try {
                  reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
               } catch (SAXException var58) {
               }
            }

            ContentHandler inputHandler = this;
            reader.setContentHandler(this);
            if (this instanceof DTDHandler) {
               reader.setDTDHandler((DTDHandler)this);
            }

            try {
               if (inputHandler instanceof LexicalHandler) {
                  reader.setProperty("http://xml.org/sax/properties/lexical-handler", inputHandler);
               }

               if (inputHandler instanceof DeclHandler) {
                  reader.setProperty("http://xml.org/sax/properties/declaration-handler", inputHandler);
               }
            } catch (SAXException var57) {
            }

            try {
               if (inputHandler instanceof LexicalHandler) {
                  reader.setProperty("http://xml.org/sax/handlers/LexicalHandler", inputHandler);
               }

               if (inputHandler instanceof DeclHandler) {
                  reader.setProperty("http://xml.org/sax/handlers/DeclHandler", inputHandler);
               }
            } catch (SAXNotRecognizedException var56) {
            }

            reader.parse(xmlSource);
         } catch (WrappedRuntimeException var63) {
            for(Throwable throwable = var63.getException(); throwable instanceof WrappedRuntimeException; throwable = ((WrappedRuntimeException)throwable).getException()) {
            }

            throw new TransformerException(var63.getException());
         } catch (SAXException var64) {
            throw new TransformerException(var64);
         } catch (IOException var65) {
            throw new TransformerException(var65);
         } finally {
            if (managedReader) {
               XMLReaderManager.getInstance().releaseXMLReader(reader);
            }

         }
      } finally {
         if (null != this.m_outputStream) {
            try {
               this.m_outputStream.close();
            } catch (IOException var55) {
            }

            this.m_outputStream = null;
         }

      }

   }

   public void setParameter(String name, Object value) {
      if (value == null) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_INVALID_SET_PARAM_VALUE", new Object[]{name}));
      } else {
         if (null == this.m_params) {
            this.m_params = new Hashtable();
         }

         this.m_params.put(name, value);
      }
   }

   public Object getParameter(String name) {
      return null == this.m_params ? null : this.m_params.get(name);
   }

   public void clearParameters() {
      if (null != this.m_params) {
         this.m_params.clear();
      }
   }

   public void setURIResolver(URIResolver resolver) {
      this.m_URIResolver = resolver;
   }

   public URIResolver getURIResolver() {
      return this.m_URIResolver;
   }

   public void setOutputProperties(Properties oformat) throws IllegalArgumentException {
      if (null != oformat) {
         String method = (String)oformat.get("method");
         if (null != method) {
            this.m_outputFormat = new OutputProperties(method);
         } else {
            this.m_outputFormat = new OutputProperties();
         }

         this.m_outputFormat.copyFrom(oformat);
      } else {
         this.m_outputFormat = null;
      }

   }

   public Properties getOutputProperties() {
      return (Properties)this.m_outputFormat.getProperties().clone();
   }

   public void setOutputProperty(String name, String value) throws IllegalArgumentException {
      if (!OutputProperties.isLegalPropertyKey(name)) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_OUTPUT_PROPERTY_NOT_RECOGNIZED", new Object[]{name}));
      } else {
         this.m_outputFormat.setProperty(name, value);
      }
   }

   public String getOutputProperty(String name) throws IllegalArgumentException {
      String value = null;
      OutputProperties props = this.m_outputFormat;
      value = props.getProperty(name);
      if (null == value && !OutputProperties.isLegalPropertyKey(name)) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_OUTPUT_PROPERTY_NOT_RECOGNIZED", new Object[]{name}));
      } else {
         return value;
      }
   }

   public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
      if (listener == null) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_NULL_ERROR_HANDLER", (Object[])null));
      } else {
         this.m_errorListener = listener;
      }
   }

   public ErrorListener getErrorListener() {
      return this.m_errorListener;
   }

   public void notationDecl(String name, String publicId, String systemId) throws SAXException {
      if (null != this.m_resultDTDHandler) {
         this.m_resultDTDHandler.notationDecl(name, publicId, systemId);
      }

   }

   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
      if (null != this.m_resultDTDHandler) {
         this.m_resultDTDHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
      }

   }

   public void setDocumentLocator(Locator locator) {
      try {
         if (null == this.m_resultContentHandler) {
            this.createResultContentHandler(this.m_result);
         }
      } catch (TransformerException var3) {
         throw new WrappedRuntimeException(var3);
      }

      this.m_resultContentHandler.setDocumentLocator(locator);
   }

   public void startDocument() throws SAXException {
      try {
         if (null == this.m_resultContentHandler) {
            this.createResultContentHandler(this.m_result);
         }
      } catch (TransformerException var2) {
         throw new SAXException(var2.getMessage(), var2);
      }

      this.m_flushedStartDoc = false;
      this.m_foundFirstElement = false;
   }

   protected final void flushStartDoc() throws SAXException {
      if (!this.m_flushedStartDoc) {
         if (this.m_resultContentHandler == null) {
            try {
               this.createResultContentHandler(this.m_result);
            } catch (TransformerException var2) {
               throw new SAXException(var2);
            }
         }

         this.m_resultContentHandler.startDocument();
         this.m_flushedStartDoc = true;
      }

   }

   public void endDocument() throws SAXException {
      this.flushStartDoc();
      this.m_resultContentHandler.endDocument();
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      this.flushStartDoc();
      this.m_resultContentHandler.startPrefixMapping(prefix, uri);
   }

   public void endPrefixMapping(String prefix) throws SAXException {
      this.flushStartDoc();
      this.m_resultContentHandler.endPrefixMapping(prefix);
   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if (!this.m_foundFirstElement && null != this.m_serializer) {
         this.m_foundFirstElement = true;

         Serializer newSerializer;
         try {
            newSerializer = SerializerSwitcher.switchSerializerIfHTML(uri, localName, this.m_outputFormat.getProperties(), this.m_serializer);
         } catch (TransformerException var8) {
            throw new SAXException(var8);
         }

         if (newSerializer != this.m_serializer) {
            try {
               this.m_resultContentHandler = newSerializer.asContentHandler();
            } catch (IOException var7) {
               throw new SAXException(var7);
            }

            if (this.m_resultContentHandler instanceof DTDHandler) {
               this.m_resultDTDHandler = (DTDHandler)this.m_resultContentHandler;
            }

            if (this.m_resultContentHandler instanceof LexicalHandler) {
               this.m_resultLexicalHandler = (LexicalHandler)this.m_resultContentHandler;
            }

            this.m_serializer = newSerializer;
         }
      }

      this.flushStartDoc();
      this.m_resultContentHandler.startElement(uri, localName, qName, attributes);
   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      this.m_resultContentHandler.endElement(uri, localName, qName);
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      this.flushStartDoc();
      this.m_resultContentHandler.characters(ch, start, length);
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      this.m_resultContentHandler.ignorableWhitespace(ch, start, length);
   }

   public void processingInstruction(String target, String data) throws SAXException {
      this.flushStartDoc();
      this.m_resultContentHandler.processingInstruction(target, data);
   }

   public void skippedEntity(String name) throws SAXException {
      this.flushStartDoc();
      this.m_resultContentHandler.skippedEntity(name);
   }

   public void startDTD(String name, String publicId, String systemId) throws SAXException {
      this.flushStartDoc();
      if (null != this.m_resultLexicalHandler) {
         this.m_resultLexicalHandler.startDTD(name, publicId, systemId);
      }

   }

   public void endDTD() throws SAXException {
      if (null != this.m_resultLexicalHandler) {
         this.m_resultLexicalHandler.endDTD();
      }

   }

   public void startEntity(String name) throws SAXException {
      if (null != this.m_resultLexicalHandler) {
         this.m_resultLexicalHandler.startEntity(name);
      }

   }

   public void endEntity(String name) throws SAXException {
      if (null != this.m_resultLexicalHandler) {
         this.m_resultLexicalHandler.endEntity(name);
      }

   }

   public void startCDATA() throws SAXException {
      if (null != this.m_resultLexicalHandler) {
         this.m_resultLexicalHandler.startCDATA();
      }

   }

   public void endCDATA() throws SAXException {
      if (null != this.m_resultLexicalHandler) {
         this.m_resultLexicalHandler.endCDATA();
      }

   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      this.flushStartDoc();
      if (null != this.m_resultLexicalHandler) {
         this.m_resultLexicalHandler.comment(ch, start, length);
      }

   }

   public void elementDecl(String name, String model) throws SAXException {
      if (null != this.m_resultDeclHandler) {
         this.m_resultDeclHandler.elementDecl(name, model);
      }

   }

   public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
      if (null != this.m_resultDeclHandler) {
         this.m_resultDeclHandler.attributeDecl(eName, aName, type, valueDefault, value);
      }

   }

   public void internalEntityDecl(String name, String value) throws SAXException {
      if (null != this.m_resultDeclHandler) {
         this.m_resultDeclHandler.internalEntityDecl(name, value);
      }

   }

   public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
      if (null != this.m_resultDeclHandler) {
         this.m_resultDeclHandler.externalEntityDecl(name, publicId, systemId);
      }

   }
}
