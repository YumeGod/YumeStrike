package org.apache.xml.dtm.ref;

import java.io.IOException;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.ThreadControllerWrapper;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

public class IncrementalSAXSource_Filter implements IncrementalSAXSource, ContentHandler, DTDHandler, LexicalHandler, ErrorHandler, Runnable {
   boolean DEBUG = false;
   private CoroutineManager fCoroutineManager = null;
   private int fControllerCoroutineID = -1;
   private int fSourceCoroutineID = -1;
   private ContentHandler clientContentHandler = null;
   private LexicalHandler clientLexicalHandler = null;
   private DTDHandler clientDTDHandler = null;
   private ErrorHandler clientErrorHandler = null;
   private int eventcounter;
   private int frequency = 5;
   private boolean fNoMoreEvents = false;
   private XMLReader fXMLReader = null;
   private InputSource fXMLReaderInputSource = null;

   public IncrementalSAXSource_Filter() {
      this.init(new CoroutineManager(), -1, -1);
   }

   public IncrementalSAXSource_Filter(CoroutineManager co, int controllerCoroutineID) {
      this.init(co, controllerCoroutineID, -1);
   }

   public static IncrementalSAXSource createIncrementalSAXSource(CoroutineManager co, int controllerCoroutineID) {
      return new IncrementalSAXSource_Filter(co, controllerCoroutineID);
   }

   public void init(CoroutineManager co, int controllerCoroutineID, int sourceCoroutineID) {
      if (co == null) {
         co = new CoroutineManager();
      }

      this.fCoroutineManager = co;
      this.fControllerCoroutineID = co.co_joinCoroutineSet(controllerCoroutineID);
      this.fSourceCoroutineID = co.co_joinCoroutineSet(sourceCoroutineID);
      if (this.fControllerCoroutineID != -1 && this.fSourceCoroutineID != -1) {
         this.fNoMoreEvents = false;
         this.eventcounter = this.frequency;
      } else {
         throw new RuntimeException(XMLMessages.createXMLMessage("ER_COJOINROUTINESET_FAILED", (Object[])null));
      }
   }

   public void setXMLReader(XMLReader eventsource) {
      this.fXMLReader = eventsource;
      eventsource.setContentHandler(this);
      eventsource.setDTDHandler(this);
      eventsource.setErrorHandler(this);

      try {
         eventsource.setProperty("http://xml.org/sax/properties/lexical-handler", this);
      } catch (SAXNotRecognizedException var4) {
      } catch (SAXNotSupportedException var5) {
      }

   }

   public void setContentHandler(ContentHandler handler) {
      this.clientContentHandler = handler;
   }

   public void setDTDHandler(DTDHandler handler) {
      this.clientDTDHandler = handler;
   }

   public void setLexicalHandler(LexicalHandler handler) {
      this.clientLexicalHandler = handler;
   }

   public void setErrHandler(ErrorHandler handler) {
      this.clientErrorHandler = handler;
   }

   public void setReturnFrequency(int events) {
      if (events < 1) {
         events = 1;
      }

      this.frequency = this.eventcounter = events;
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      if (--this.eventcounter <= 0) {
         this.co_yield(true);
         this.eventcounter = this.frequency;
      }

      if (this.clientContentHandler != null) {
         this.clientContentHandler.characters(ch, start, length);
      }

   }

   public void endDocument() throws SAXException {
      if (this.clientContentHandler != null) {
         this.clientContentHandler.endDocument();
      }

      this.eventcounter = 0;
      this.co_yield(false);
   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      if (--this.eventcounter <= 0) {
         this.co_yield(true);
         this.eventcounter = this.frequency;
      }

      if (this.clientContentHandler != null) {
         this.clientContentHandler.endElement(namespaceURI, localName, qName);
      }

   }

   public void endPrefixMapping(String prefix) throws SAXException {
      if (--this.eventcounter <= 0) {
         this.co_yield(true);
         this.eventcounter = this.frequency;
      }

      if (this.clientContentHandler != null) {
         this.clientContentHandler.endPrefixMapping(prefix);
      }

   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      if (--this.eventcounter <= 0) {
         this.co_yield(true);
         this.eventcounter = this.frequency;
      }

      if (this.clientContentHandler != null) {
         this.clientContentHandler.ignorableWhitespace(ch, start, length);
      }

   }

   public void processingInstruction(String target, String data) throws SAXException {
      if (--this.eventcounter <= 0) {
         this.co_yield(true);
         this.eventcounter = this.frequency;
      }

      if (this.clientContentHandler != null) {
         this.clientContentHandler.processingInstruction(target, data);
      }

   }

   public void setDocumentLocator(Locator locator) {
      if (--this.eventcounter <= 0) {
         this.eventcounter = this.frequency;
      }

      if (this.clientContentHandler != null) {
         this.clientContentHandler.setDocumentLocator(locator);
      }

   }

   public void skippedEntity(String name) throws SAXException {
      if (--this.eventcounter <= 0) {
         this.co_yield(true);
         this.eventcounter = this.frequency;
      }

      if (this.clientContentHandler != null) {
         this.clientContentHandler.skippedEntity(name);
      }

   }

   public void startDocument() throws SAXException {
      this.co_entry_pause();
      if (--this.eventcounter <= 0) {
         this.co_yield(true);
         this.eventcounter = this.frequency;
      }

      if (this.clientContentHandler != null) {
         this.clientContentHandler.startDocument();
      }

   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
      if (--this.eventcounter <= 0) {
         this.co_yield(true);
         this.eventcounter = this.frequency;
      }

      if (this.clientContentHandler != null) {
         this.clientContentHandler.startElement(namespaceURI, localName, qName, atts);
      }

   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      if (--this.eventcounter <= 0) {
         this.co_yield(true);
         this.eventcounter = this.frequency;
      }

      if (this.clientContentHandler != null) {
         this.clientContentHandler.startPrefixMapping(prefix, uri);
      }

   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      if (null != this.clientLexicalHandler) {
         this.clientLexicalHandler.comment(ch, start, length);
      }

   }

   public void endCDATA() throws SAXException {
      if (null != this.clientLexicalHandler) {
         this.clientLexicalHandler.endCDATA();
      }

   }

   public void endDTD() throws SAXException {
      if (null != this.clientLexicalHandler) {
         this.clientLexicalHandler.endDTD();
      }

   }

   public void endEntity(String name) throws SAXException {
      if (null != this.clientLexicalHandler) {
         this.clientLexicalHandler.endEntity(name);
      }

   }

   public void startCDATA() throws SAXException {
      if (null != this.clientLexicalHandler) {
         this.clientLexicalHandler.startCDATA();
      }

   }

   public void startDTD(String name, String publicId, String systemId) throws SAXException {
      if (null != this.clientLexicalHandler) {
         this.clientLexicalHandler.startDTD(name, publicId, systemId);
      }

   }

   public void startEntity(String name) throws SAXException {
      if (null != this.clientLexicalHandler) {
         this.clientLexicalHandler.startEntity(name);
      }

   }

   public void notationDecl(String a, String b, String c) throws SAXException {
      if (null != this.clientDTDHandler) {
         this.clientDTDHandler.notationDecl(a, b, c);
      }

   }

   public void unparsedEntityDecl(String a, String b, String c, String d) throws SAXException {
      if (null != this.clientDTDHandler) {
         this.clientDTDHandler.unparsedEntityDecl(a, b, c, d);
      }

   }

   public void error(SAXParseException exception) throws SAXException {
      if (null != this.clientErrorHandler) {
         this.clientErrorHandler.error(exception);
      }

   }

   public void fatalError(SAXParseException exception) throws SAXException {
      if (null != this.clientErrorHandler) {
         this.clientErrorHandler.error(exception);
      }

      this.eventcounter = 0;
      this.co_yield(false);
   }

   public void warning(SAXParseException exception) throws SAXException {
      if (null != this.clientErrorHandler) {
         this.clientErrorHandler.error(exception);
      }

   }

   public int getSourceCoroutineID() {
      return this.fSourceCoroutineID;
   }

   public int getControllerCoroutineID() {
      return this.fControllerCoroutineID;
   }

   public CoroutineManager getCoroutineManager() {
      return this.fCoroutineManager;
   }

   protected void count_and_yield(boolean moreExpected) throws SAXException {
      if (!moreExpected) {
         this.eventcounter = 0;
      }

      if (--this.eventcounter <= 0) {
         this.co_yield(true);
         this.eventcounter = this.frequency;
      }

   }

   private void co_entry_pause() throws SAXException {
      if (this.fCoroutineManager == null) {
         this.init((CoroutineManager)null, -1, -1);
      }

      try {
         Object arg = this.fCoroutineManager.co_entry_pause(this.fSourceCoroutineID);
         if (arg == Boolean.FALSE) {
            this.co_yield(false);
         }

      } catch (NoSuchMethodException var2) {
         if (this.DEBUG) {
            var2.printStackTrace();
         }

         throw new SAXException(var2);
      }
   }

   private void co_yield(boolean moreRemains) throws SAXException {
      if (!this.fNoMoreEvents) {
         try {
            Object arg = Boolean.FALSE;
            if (moreRemains) {
               arg = this.fCoroutineManager.co_resume(Boolean.TRUE, this.fSourceCoroutineID, this.fControllerCoroutineID);
            }

            if (arg == Boolean.FALSE) {
               this.fNoMoreEvents = true;
               if (this.fXMLReader != null) {
                  throw new StopException();
               }

               this.fCoroutineManager.co_exit_to(Boolean.FALSE, this.fSourceCoroutineID, this.fControllerCoroutineID);
            }

         } catch (NoSuchMethodException var3) {
            this.fNoMoreEvents = true;
            this.fCoroutineManager.co_exit(this.fSourceCoroutineID);
            throw new SAXException(var3);
         }
      }
   }

   public void startParse(InputSource source) throws SAXException {
      if (this.fNoMoreEvents) {
         throw new SAXException(XMLMessages.createXMLMessage("ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", (Object[])null));
      } else if (this.fXMLReader == null) {
         throw new SAXException(XMLMessages.createXMLMessage("ER_XMLRDR_NOT_BEFORE_STARTPARSE", (Object[])null));
      } else {
         this.fXMLReaderInputSource = source;
         ThreadControllerWrapper.runThread(this, -1);
      }
   }

   public void run() {
      if (this.fXMLReader != null) {
         if (this.DEBUG) {
            System.out.println("IncrementalSAXSource_Filter parse thread launched");
         }

         Object arg = Boolean.FALSE;

         try {
            this.fXMLReader.parse(this.fXMLReaderInputSource);
         } catch (IOException var7) {
            arg = var7;
         } catch (StopException var8) {
            if (this.DEBUG) {
               System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
            }
         } catch (SAXException var9) {
            Exception inner = var9.getException();
            if (inner instanceof StopException) {
               if (this.DEBUG) {
                  System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
               }
            } else {
               if (this.DEBUG) {
                  System.out.println("Active IncrementalSAXSource_Filter UNEXPECTED SAX exception: " + inner);
                  inner.printStackTrace();
               }

               arg = var9;
            }
         }

         this.fXMLReader = null;

         try {
            this.fNoMoreEvents = true;
            this.fCoroutineManager.co_exit_to(arg, this.fSourceCoroutineID, this.fControllerCoroutineID);
         } catch (NoSuchMethodException var6) {
            var6.printStackTrace(System.err);
            this.fCoroutineManager.co_exit(this.fSourceCoroutineID);
         }

      }
   }

   public Object deliverMoreNodes(boolean parsemore) {
      if (this.fNoMoreEvents) {
         return Boolean.FALSE;
      } else {
         try {
            Object result = this.fCoroutineManager.co_resume(parsemore ? Boolean.TRUE : Boolean.FALSE, this.fControllerCoroutineID, this.fSourceCoroutineID);
            if (result == Boolean.FALSE) {
               this.fCoroutineManager.co_exit(this.fControllerCoroutineID);
            }

            return result;
         } catch (NoSuchMethodException var3) {
            return var3;
         }
      }
   }

   class StopException extends RuntimeException {
      static final long serialVersionUID = -1129245796185754956L;
   }
}
