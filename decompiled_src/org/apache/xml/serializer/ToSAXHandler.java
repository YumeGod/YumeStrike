package org.apache.xml.serializer;

import java.util.Vector;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;

public abstract class ToSAXHandler extends SerializerBase {
   protected ContentHandler m_saxHandler;
   protected LexicalHandler m_lexHandler;
   private boolean m_shouldGenerateNSAttribute = true;
   protected TransformStateSetter m_state = null;

   public ToSAXHandler() {
   }

   public ToSAXHandler(ContentHandler hdlr, LexicalHandler lex, String encoding) {
      this.setContentHandler(hdlr);
      this.setLexHandler(lex);
      this.setEncoding(encoding);
   }

   public ToSAXHandler(ContentHandler handler, String encoding) {
      this.setContentHandler(handler);
      this.setEncoding(encoding);
   }

   protected void startDocumentInternal() throws SAXException {
      if (super.m_needToCallStartDocument) {
         super.startDocumentInternal();
         this.m_saxHandler.startDocument();
         super.m_needToCallStartDocument = false;
      }

   }

   public void startDTD(String arg0, String arg1, String arg2) throws SAXException {
   }

   public void characters(String characters) throws SAXException {
      int len = characters.length();
      if (len > super.m_charsBuff.length) {
         super.m_charsBuff = new char[len * 2 + 1];
      }

      characters.getChars(0, len, super.m_charsBuff, 0);
      this.characters(super.m_charsBuff, 0, len);
   }

   public void comment(String comment) throws SAXException {
      this.flushPending();
      if (this.m_lexHandler != null) {
         int len = comment.length();
         if (len > super.m_charsBuff.length) {
            super.m_charsBuff = new char[len * 2 + 1];
         }

         comment.getChars(0, len, super.m_charsBuff, 0);
         this.m_lexHandler.comment(super.m_charsBuff, 0, len);
         if (super.m_tracer != null) {
            super.fireCommentEvent(super.m_charsBuff, 0, len);
         }
      }

   }

   public void processingInstruction(String target, String data) throws SAXException {
   }

   protected void closeStartTag() throws SAXException {
   }

   protected void closeCDATA() throws SAXException {
   }

   public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
      if (this.m_state != null) {
         this.m_state.resetState(this.getTransformer());
      }

      if (super.m_tracer != null) {
         super.fireStartElem(arg2);
      }

   }

   public void setLexHandler(LexicalHandler _lexHandler) {
      this.m_lexHandler = _lexHandler;
   }

   public void setContentHandler(ContentHandler _saxHandler) {
      this.m_saxHandler = _saxHandler;
      if (this.m_lexHandler == null && _saxHandler instanceof LexicalHandler) {
         this.m_lexHandler = (LexicalHandler)_saxHandler;
      }

   }

   public void setCdataSectionElements(Vector URI_and_localNames) {
   }

   public void setShouldOutputNSAttr(boolean doOutputNSAttr) {
      this.m_shouldGenerateNSAttribute = doOutputNSAttr;
   }

   boolean getShouldOutputNSAttr() {
      return this.m_shouldGenerateNSAttribute;
   }

   public void flushPending() throws SAXException {
      if (super.m_needToCallStartDocument) {
         this.startDocumentInternal();
         super.m_needToCallStartDocument = false;
      }

      if (super.m_elemContext.m_startTagOpen) {
         this.closeStartTag();
         super.m_elemContext.m_startTagOpen = false;
      }

      if (super.m_cdataTagOpen) {
         this.closeCDATA();
         super.m_cdataTagOpen = false;
      }

   }

   public void setTransformState(TransformStateSetter ts) {
      this.m_state = ts;
   }

   public void startElement(String uri, String localName, String qName) throws SAXException {
      if (this.m_state != null) {
         this.m_state.resetState(this.getTransformer());
      }

      if (super.m_tracer != null) {
         super.fireStartElem(qName);
      }

   }

   public void startElement(String qName) throws SAXException {
      if (this.m_state != null) {
         this.m_state.resetState(this.getTransformer());
      }

      if (super.m_tracer != null) {
         super.fireStartElem(qName);
      }

   }

   public void characters(Node node) throws SAXException {
      if (this.m_state != null) {
         this.m_state.setCurrentNode(node);
      }

      String data = node.getNodeValue();
      if (data != null) {
         this.characters(data);
      }

   }

   public void fatalError(SAXParseException exc) throws SAXException {
      super.fatalError(exc);
      super.m_needToCallStartDocument = false;
      if (this.m_saxHandler instanceof ErrorHandler) {
         ((ErrorHandler)this.m_saxHandler).fatalError(exc);
      }

   }

   public void error(SAXParseException exc) throws SAXException {
      super.error(exc);
      if (this.m_saxHandler instanceof ErrorHandler) {
         ((ErrorHandler)this.m_saxHandler).error(exc);
      }

   }

   public void warning(SAXParseException exc) throws SAXException {
      super.warning(exc);
      if (this.m_saxHandler instanceof ErrorHandler) {
         ((ErrorHandler)this.m_saxHandler).warning(exc);
      }

   }

   public boolean reset() {
      boolean wasReset = false;
      if (super.reset()) {
         this.resetToSAXHandler();
         wasReset = true;
      }

      return wasReset;
   }

   private void resetToSAXHandler() {
      this.m_lexHandler = null;
      this.m_saxHandler = null;
      this.m_state = null;
      this.m_shouldGenerateNSAttribute = false;
   }

   public void addUniqueAttribute(String qName, String value, int flags) throws SAXException {
      this.addAttribute(qName, value);
   }
}
