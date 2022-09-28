package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public final class ToXMLSAXHandler extends ToSAXHandler {
   protected boolean m_escapeSetting = false;

   public ToXMLSAXHandler() {
      super.m_prefixMap = new NamespaceMappings();
      this.initCDATA();
   }

   public Properties getOutputFormat() {
      return null;
   }

   public OutputStream getOutputStream() {
      return null;
   }

   public Writer getWriter() {
      return null;
   }

   public void indent(int n) throws SAXException {
   }

   public void serialize(Node node) throws IOException {
   }

   public boolean setEscaping(boolean escape) throws SAXException {
      boolean oldEscapeSetting = this.m_escapeSetting;
      this.m_escapeSetting = escape;
      if (escape) {
         this.processingInstruction("javax.xml.transform.enable-output-escaping", "");
      } else {
         this.processingInstruction("javax.xml.transform.disable-output-escaping", "");
      }

      return oldEscapeSetting;
   }

   public void setOutputFormat(Properties format) {
   }

   public void setOutputStream(OutputStream output) {
   }

   public void setWriter(Writer writer) {
   }

   public void attributeDecl(String arg0, String arg1, String arg2, String arg3, String arg4) throws SAXException {
   }

   public void elementDecl(String arg0, String arg1) throws SAXException {
   }

   public void externalEntityDecl(String arg0, String arg1, String arg2) throws SAXException {
   }

   public void internalEntityDecl(String arg0, String arg1) throws SAXException {
   }

   public void endDocument() throws SAXException {
      this.flushPending();
      super.m_saxHandler.endDocument();
      if (super.m_tracer != null) {
         super.fireEndDoc();
      }

   }

   protected void closeStartTag() throws SAXException {
      super.m_elemContext.m_startTagOpen = false;
      String localName = SerializerBase.getLocalName(super.m_elemContext.m_elementName);
      String uri = this.getNamespaceURI(super.m_elemContext.m_elementName, true);
      if (super.m_needToCallStartDocument) {
         this.startDocumentInternal();
      }

      super.m_saxHandler.startElement(uri, localName, super.m_elemContext.m_elementName, super.m_attributes);
      super.m_attributes.clear();
      if (super.m_state != null) {
         super.m_state.setCurrentNode((Node)null);
      }

   }

   public void closeCDATA() throws SAXException {
      if (super.m_lexHandler != null && super.m_cdataTagOpen) {
         super.m_lexHandler.endCDATA();
      }

      super.m_cdataTagOpen = false;
   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      this.flushPending();
      if (namespaceURI == null) {
         if (super.m_elemContext.m_elementURI != null) {
            namespaceURI = super.m_elemContext.m_elementURI;
         } else {
            namespaceURI = this.getNamespaceURI(qName, true);
         }
      }

      if (localName == null) {
         if (super.m_elemContext.m_elementLocalName != null) {
            localName = super.m_elemContext.m_elementLocalName;
         } else {
            localName = SerializerBase.getLocalName(qName);
         }
      }

      super.m_saxHandler.endElement(namespaceURI, localName, qName);
      if (super.m_tracer != null) {
         super.fireEndElem(qName);
      }

      super.m_prefixMap.popNamespaces(super.m_elemContext.m_currentElemDepth, super.m_saxHandler);
      super.m_elemContext = super.m_elemContext.m_prev;
   }

   public void endPrefixMapping(String prefix) throws SAXException {
   }

   public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
      super.m_saxHandler.ignorableWhitespace(arg0, arg1, arg2);
   }

   public void setDocumentLocator(Locator arg0) {
      super.m_saxHandler.setDocumentLocator(arg0);
   }

   public void skippedEntity(String arg0) throws SAXException {
      super.m_saxHandler.skippedEntity(arg0);
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      this.startPrefixMapping(prefix, uri, true);
   }

   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws SAXException {
      int pushDepth;
      if (shouldFlush) {
         this.flushPending();
         pushDepth = super.m_elemContext.m_currentElemDepth + 1;
      } else {
         pushDepth = super.m_elemContext.m_currentElemDepth;
      }

      boolean pushed = super.m_prefixMap.pushNamespace(prefix, uri, pushDepth);
      if (pushed) {
         super.m_saxHandler.startPrefixMapping(prefix, uri);
         if (this.getShouldOutputNSAttr()) {
            String name;
            if ("".equals(prefix)) {
               name = "xmlns";
               this.addAttributeAlways("http://www.w3.org/2000/xmlns/", name, name, "CDATA", uri, false);
            } else if (!"".equals(uri)) {
               name = "xmlns:" + prefix;
               this.addAttributeAlways("http://www.w3.org/2000/xmlns/", prefix, name, "CDATA", uri, false);
            }
         }
      }

      return pushed;
   }

   public void comment(char[] arg0, int arg1, int arg2) throws SAXException {
      this.flushPending();
      if (super.m_lexHandler != null) {
         super.m_lexHandler.comment(arg0, arg1, arg2);
      }

      if (super.m_tracer != null) {
         super.fireCommentEvent(arg0, arg1, arg2);
      }

   }

   public void endCDATA() throws SAXException {
   }

   public void endDTD() throws SAXException {
      if (super.m_lexHandler != null) {
         super.m_lexHandler.endDTD();
      }

   }

   public void startEntity(String arg0) throws SAXException {
      if (super.m_lexHandler != null) {
         super.m_lexHandler.startEntity(arg0);
      }

   }

   public void characters(String chars) throws SAXException {
      int length = chars.length();
      if (length > super.m_charsBuff.length) {
         super.m_charsBuff = new char[length * 2 + 1];
      }

      chars.getChars(0, length, super.m_charsBuff, 0);
      this.characters(super.m_charsBuff, 0, length);
   }

   public ToXMLSAXHandler(ContentHandler handler, String encoding) {
      super(handler, encoding);
      this.initCDATA();
      super.m_prefixMap = new NamespaceMappings();
   }

   public ToXMLSAXHandler(ContentHandler handler, LexicalHandler lex, String encoding) {
      super(handler, lex, encoding);
      this.initCDATA();
      super.m_prefixMap = new NamespaceMappings();
   }

   public void startElement(String elementNamespaceURI, String elementLocalName, String elementName) throws SAXException {
      this.startElement(elementNamespaceURI, elementLocalName, elementName, (Attributes)null);
   }

   public void startElement(String elementName) throws SAXException {
      this.startElement((String)null, (String)null, elementName, (Attributes)null);
   }

   public void characters(char[] ch, int off, int len) throws SAXException {
      if (super.m_needToCallStartDocument) {
         this.startDocumentInternal();
         super.m_needToCallStartDocument = false;
      }

      if (super.m_elemContext.m_startTagOpen) {
         this.closeStartTag();
         super.m_elemContext.m_startTagOpen = false;
      }

      if (super.m_elemContext.m_isCdataSection && !super.m_cdataTagOpen && super.m_lexHandler != null) {
         super.m_lexHandler.startCDATA();
         super.m_cdataTagOpen = true;
      }

      super.m_saxHandler.characters(ch, off, len);
      if (super.m_tracer != null) {
         this.fireCharEvent(ch, off, len);
      }

   }

   public void endElement(String elemName) throws SAXException {
      this.endElement((String)null, (String)null, elemName);
   }

   public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
      this.startPrefixMapping(prefix, uri, false);
   }

   public void processingInstruction(String target, String data) throws SAXException {
      this.flushPending();
      super.m_saxHandler.processingInstruction(target, data);
      if (super.m_tracer != null) {
         super.fireEscapingEvent(target, data);
      }

   }

   protected boolean popNamespace(String prefix) {
      try {
         if (super.m_prefixMap.popNamespace(prefix)) {
            super.m_saxHandler.endPrefixMapping(prefix);
            return true;
         }
      } catch (SAXException var3) {
      }

      return false;
   }

   public void startCDATA() throws SAXException {
      if (!super.m_cdataTagOpen) {
         this.flushPending();
         if (super.m_lexHandler != null) {
            super.m_lexHandler.startCDATA();
            super.m_cdataTagOpen = true;
         }
      }

   }

   public void startElement(String namespaceURI, String localName, String name, Attributes atts) throws SAXException {
      this.flushPending();
      super.startElement(namespaceURI, localName, name, atts);
      if (super.m_needToOutputDocTypeDecl) {
         String doctypeSystem = this.getDoctypeSystem();
         if (doctypeSystem != null && super.m_lexHandler != null) {
            String doctypePublic = this.getDoctypePublic();
            if (doctypeSystem != null) {
               super.m_lexHandler.startDTD(name, doctypePublic, doctypeSystem);
            }
         }

         super.m_needToOutputDocTypeDecl = false;
      }

      super.m_elemContext = super.m_elemContext.push(namespaceURI, localName, name);
      if (namespaceURI != null) {
         this.ensurePrefixIsDeclared(namespaceURI, name);
      }

      if (atts != null) {
         this.addAttributes(atts);
      }

      super.m_elemContext.m_isCdataSection = this.isCdataSection();
   }

   private void ensurePrefixIsDeclared(String ns, String rawName) throws SAXException {
      if (ns != null && ns.length() > 0) {
         int index;
         boolean no_prefix = (index = rawName.indexOf(":")) < 0;
         String prefix = no_prefix ? "" : rawName.substring(0, index);
         if (null != prefix) {
            String foundURI = super.m_prefixMap.lookupNamespace(prefix);
            if (null == foundURI || !foundURI.equals(ns)) {
               this.startPrefixMapping(prefix, ns, false);
               if (this.getShouldOutputNSAttr()) {
                  this.addAttributeAlways("http://www.w3.org/2000/xmlns/", no_prefix ? "xmlns" : prefix, no_prefix ? "xmlns" : "xmlns:" + prefix, "CDATA", ns, false);
               }
            }
         }
      }

   }

   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) throws SAXException {
      if (super.m_elemContext.m_startTagOpen) {
         this.ensurePrefixIsDeclared(uri, rawName);
         this.addAttributeAlways(uri, localName, rawName, type, value, false);
      }

   }

   public boolean reset() {
      boolean wasReset = false;
      if (super.reset()) {
         this.resetToXMLSAXHandler();
         wasReset = true;
      }

      return wasReset;
   }

   private void resetToXMLSAXHandler() {
      this.m_escapeSetting = false;
   }
}
