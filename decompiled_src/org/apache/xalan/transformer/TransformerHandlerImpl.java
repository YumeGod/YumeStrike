package org.apache.xalan.transformer;

import java.io.IOException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.ref.IncrementalSAXSource_Filter;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xpath.XPathContext;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class TransformerHandlerImpl implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler, LexicalHandler, TransformerHandler, DeclHandler {
   private final boolean m_optimizer;
   private final boolean m_incremental;
   private final boolean m_source_location;
   private boolean m_insideParse = false;
   private static boolean DEBUG = false;
   private TransformerImpl m_transformer;
   private String m_baseSystemID;
   private Result m_result = null;
   private Locator m_locator = null;
   private EntityResolver m_entityResolver = null;
   private DTDHandler m_dtdHandler = null;
   private ContentHandler m_contentHandler = null;
   private ErrorHandler m_errorHandler = null;
   private LexicalHandler m_lexicalHandler = null;
   private DeclHandler m_declHandler = null;
   DTM m_dtm;

   public TransformerHandlerImpl(TransformerImpl transformer, boolean doFragment, String baseSystemID) {
      this.m_transformer = transformer;
      this.m_baseSystemID = baseSystemID;
      XPathContext xctxt = transformer.getXPathContext();
      DTM dtm = xctxt.getDTM((Source)null, true, transformer, true, true);
      this.m_dtm = dtm;
      dtm.setDocumentBaseURI(baseSystemID);
      this.m_contentHandler = dtm.getContentHandler();
      this.m_dtdHandler = dtm.getDTDHandler();
      this.m_entityResolver = dtm.getEntityResolver();
      this.m_errorHandler = dtm.getErrorHandler();
      this.m_lexicalHandler = dtm.getLexicalHandler();
      this.m_incremental = transformer.getIncremental();
      this.m_optimizer = transformer.getOptimize();
      this.m_source_location = transformer.getSource_location();
   }

   protected void clearCoRoutine() {
      this.clearCoRoutine((SAXException)null);
   }

   protected void clearCoRoutine(SAXException ex) {
      if (null != ex) {
         this.m_transformer.setExceptionThrown(ex);
      }

      if (this.m_dtm instanceof SAX2DTM) {
         if (DEBUG) {
            System.err.println("In clearCoRoutine...");
         }

         try {
            SAX2DTM sax2dtm = (SAX2DTM)this.m_dtm;
            if (null != this.m_contentHandler && this.m_contentHandler instanceof IncrementalSAXSource_Filter) {
               IncrementalSAXSource_Filter sp = (IncrementalSAXSource_Filter)this.m_contentHandler;
               sp.deliverMoreNodes(false);
            }

            sax2dtm.clearCoRoutine(true);
            this.m_contentHandler = null;
            this.m_dtdHandler = null;
            this.m_entityResolver = null;
            this.m_errorHandler = null;
            this.m_lexicalHandler = null;
         } catch (Throwable var4) {
            var4.printStackTrace();
         }

         if (DEBUG) {
            System.err.println("...exiting clearCoRoutine");
         }
      }

   }

   public void setResult(Result result) throws IllegalArgumentException {
      if (null == result) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_RESULT_NULL", (Object[])null));
      } else {
         try {
            SerializationHandler xoh = this.m_transformer.createSerializationHandler(result);
            this.m_transformer.setSerializationHandler(xoh);
         } catch (TransformerException var3) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_RESULT_COULD_NOT_BE_SET", (Object[])null));
         }

         this.m_result = result;
      }
   }

   public void setSystemId(String systemID) {
      this.m_baseSystemID = systemID;
      this.m_dtm.setDocumentBaseURI(systemID);
   }

   public String getSystemId() {
      return this.m_baseSystemID;
   }

   public Transformer getTransformer() {
      return this.m_transformer;
   }

   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
      return this.m_entityResolver != null ? this.m_entityResolver.resolveEntity(publicId, systemId) : null;
   }

   public void notationDecl(String name, String publicId, String systemId) throws SAXException {
      if (this.m_dtdHandler != null) {
         this.m_dtdHandler.notationDecl(name, publicId, systemId);
      }

   }

   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
      if (this.m_dtdHandler != null) {
         this.m_dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
      }

   }

   public void setDocumentLocator(Locator locator) {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#setDocumentLocator: " + locator.getSystemId());
      }

      this.m_locator = locator;
      if (null == this.m_baseSystemID) {
         this.setSystemId(locator.getSystemId());
      }

      if (this.m_contentHandler != null) {
         this.m_contentHandler.setDocumentLocator(locator);
      }

   }

   public void startDocument() throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#startDocument");
      }

      this.m_insideParse = true;
      if (this.m_contentHandler != null) {
         if (this.m_incremental) {
            this.m_transformer.setSourceTreeDocForThread(this.m_dtm.getDocument());
            int cpriority = Thread.currentThread().getPriority();
            this.m_transformer.runTransformThread(cpriority);
         }

         this.m_contentHandler.startDocument();
      }

   }

   public void endDocument() throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#endDocument");
      }

      this.m_insideParse = false;
      if (this.m_contentHandler != null) {
         this.m_contentHandler.endDocument();
      }

      if (this.m_incremental) {
         this.m_transformer.waitTransformThread();
      } else {
         this.m_transformer.setSourceTreeDocForThread(this.m_dtm.getDocument());
         this.m_transformer.run();
      }

   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#startPrefixMapping: " + prefix + ", " + uri);
      }

      if (this.m_contentHandler != null) {
         this.m_contentHandler.startPrefixMapping(prefix, uri);
      }

   }

   public void endPrefixMapping(String prefix) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#endPrefixMapping: " + prefix);
      }

      if (this.m_contentHandler != null) {
         this.m_contentHandler.endPrefixMapping(prefix);
      }

   }

   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#startElement: " + qName);
      }

      if (this.m_contentHandler != null) {
         this.m_contentHandler.startElement(uri, localName, qName, atts);
      }

   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#endElement: " + qName);
      }

      if (this.m_contentHandler != null) {
         this.m_contentHandler.endElement(uri, localName, qName);
      }

   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#characters: " + start + ", " + length);
      }

      if (this.m_contentHandler != null) {
         this.m_contentHandler.characters(ch, start, length);
      }

   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#ignorableWhitespace: " + start + ", " + length);
      }

      if (this.m_contentHandler != null) {
         this.m_contentHandler.ignorableWhitespace(ch, start, length);
      }

   }

   public void processingInstruction(String target, String data) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#processingInstruction: " + target + ", " + data);
      }

      if (this.m_contentHandler != null) {
         this.m_contentHandler.processingInstruction(target, data);
      }

   }

   public void skippedEntity(String name) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#skippedEntity: " + name);
      }

      if (this.m_contentHandler != null) {
         this.m_contentHandler.skippedEntity(name);
      }

   }

   public void warning(SAXParseException e) throws SAXException {
      ErrorListener errorListener = this.m_transformer.getErrorListener();
      if (errorListener instanceof ErrorHandler) {
         ((ErrorHandler)errorListener).warning(e);
      } else {
         try {
            errorListener.warning(new TransformerException(e));
         } catch (TransformerException var4) {
            throw e;
         }
      }

   }

   public void error(SAXParseException e) throws SAXException {
      ErrorListener errorListener = this.m_transformer.getErrorListener();
      if (errorListener instanceof ErrorHandler) {
         ((ErrorHandler)errorListener).error(e);
         if (null != this.m_errorHandler) {
            this.m_errorHandler.error(e);
         }
      } else {
         try {
            errorListener.error(new TransformerException(e));
            if (null != this.m_errorHandler) {
               this.m_errorHandler.error(e);
            }
         } catch (TransformerException var4) {
            throw e;
         }
      }

   }

   public void fatalError(SAXParseException e) throws SAXException {
      if (null != this.m_errorHandler) {
         try {
            this.m_errorHandler.fatalError(e);
         } catch (SAXParseException var5) {
         }
      }

      ErrorListener errorListener = this.m_transformer.getErrorListener();
      if (errorListener instanceof ErrorHandler) {
         ((ErrorHandler)errorListener).fatalError(e);
         if (null != this.m_errorHandler) {
            this.m_errorHandler.fatalError(e);
         }
      } else {
         try {
            errorListener.fatalError(new TransformerException(e));
            if (null != this.m_errorHandler) {
               this.m_errorHandler.fatalError(e);
            }
         } catch (TransformerException var4) {
            throw e;
         }
      }

   }

   public void startDTD(String name, String publicId, String systemId) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#startDTD: " + name + ", " + publicId + ", " + systemId);
      }

      if (null != this.m_lexicalHandler) {
         this.m_lexicalHandler.startDTD(name, publicId, systemId);
      }

   }

   public void endDTD() throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#endDTD");
      }

      if (null != this.m_lexicalHandler) {
         this.m_lexicalHandler.endDTD();
      }

   }

   public void startEntity(String name) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#startEntity: " + name);
      }

      if (null != this.m_lexicalHandler) {
         this.m_lexicalHandler.startEntity(name);
      }

   }

   public void endEntity(String name) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#endEntity: " + name);
      }

      if (null != this.m_lexicalHandler) {
         this.m_lexicalHandler.endEntity(name);
      }

   }

   public void startCDATA() throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#startCDATA");
      }

      if (null != this.m_lexicalHandler) {
         this.m_lexicalHandler.startCDATA();
      }

   }

   public void endCDATA() throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#endCDATA");
      }

      if (null != this.m_lexicalHandler) {
         this.m_lexicalHandler.endCDATA();
      }

   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#comment: " + start + ", " + length);
      }

      if (null != this.m_lexicalHandler) {
         this.m_lexicalHandler.comment(ch, start, length);
      }

   }

   public void elementDecl(String name, String model) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#elementDecl: " + name + ", " + model);
      }

      if (null != this.m_declHandler) {
         this.m_declHandler.elementDecl(name, model);
      }

   }

   public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#attributeDecl: " + eName + ", " + aName + ", etc...");
      }

      if (null != this.m_declHandler) {
         this.m_declHandler.attributeDecl(eName, aName, type, valueDefault, value);
      }

   }

   public void internalEntityDecl(String name, String value) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#internalEntityDecl: " + name + ", " + value);
      }

      if (null != this.m_declHandler) {
         this.m_declHandler.internalEntityDecl(name, value);
      }

   }

   public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
      if (DEBUG) {
         System.out.println("TransformerHandlerImpl#externalEntityDecl: " + name + ", " + publicId + ", " + systemId);
      }

      if (null != this.m_declHandler) {
         this.m_declHandler.externalEntityDecl(name, publicId, systemId);
      }

   }
}
