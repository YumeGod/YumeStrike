package org.apache.xml.serializer;

import java.io.IOException;
import java.io.Writer;
import org.apache.xml.serializer.utils.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public final class ToTextStream extends ToStream {
   protected void startDocumentInternal() throws SAXException {
      super.startDocumentInternal();
      super.m_needToCallStartDocument = false;
   }

   public void endDocument() throws SAXException {
      this.flushPending();
      this.flushWriter();
      if (super.m_tracer != null) {
         super.fireEndDoc();
      }

   }

   public void startElement(String namespaceURI, String localName, String name, Attributes atts) throws SAXException {
      if (super.m_tracer != null) {
         super.fireStartElem(name);
         this.firePseudoAttributes();
      }

   }

   public void endElement(String namespaceURI, String localName, String name) throws SAXException {
      if (super.m_tracer != null) {
         super.fireEndElem(name);
      }

   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      this.flushPending();

      try {
         if (this.inTemporaryOutputState()) {
            super.m_writer.write(ch, start, length);
         } else {
            this.writeNormalizedChars(ch, start, length, super.m_lineSepUse);
         }

         if (super.m_tracer != null) {
            super.fireCharEvent(ch, start, length);
         }

      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   public void charactersRaw(char[] ch, int start, int length) throws SAXException {
      try {
         this.writeNormalizedChars(ch, start, length, super.m_lineSepUse);
      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   void writeNormalizedChars(char[] ch, int start, int length, boolean useLineSep) throws IOException, SAXException {
      String encoding = this.getEncoding();
      Writer writer = super.m_writer;
      int end = start + length;
      char S_LINEFEED = true;

      for(int i = start; i < end; ++i) {
         char c = ch[i];
         if ('\n' == c && useLineSep) {
            writer.write(super.m_lineSep, 0, super.m_lineSepLen);
         } else if (super.m_encodingInfo.isInEncoding(c)) {
            writer.write(c);
         } else {
            String integralValue;
            if (Encodings.isHighUTF16Surrogate(c)) {
               int codePoint = this.writeUTF16Surrogate(c, ch, i, end);
               if (codePoint != 0) {
                  integralValue = Integer.toString(codePoint);
                  String msg = Utils.messages.createMessage("ER_ILLEGAL_CHARACTER", new Object[]{integralValue, encoding});
                  System.err.println(msg);
               }

               ++i;
            } else if (encoding != null) {
               writer.write(38);
               writer.write(35);
               writer.write(Integer.toString(c));
               writer.write(59);
               String integralValue = Integer.toString(c);
               integralValue = Utils.messages.createMessage("ER_ILLEGAL_CHARACTER", new Object[]{integralValue, encoding});
               System.err.println(integralValue);
            } else {
               writer.write(c);
            }
         }
      }

   }

   public void cdata(char[] ch, int start, int length) throws SAXException {
      try {
         this.writeNormalizedChars(ch, start, length, super.m_lineSepUse);
         if (super.m_tracer != null) {
            super.fireCDATAEvent(ch, start, length);
         }

      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      try {
         this.writeNormalizedChars(ch, start, length, super.m_lineSepUse);
      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   public void processingInstruction(String target, String data) throws SAXException {
      this.flushPending();
      if (super.m_tracer != null) {
         super.fireEscapingEvent(target, data);
      }

   }

   public void comment(String data) throws SAXException {
      int length = data.length();
      if (length > super.m_charsBuff.length) {
         super.m_charsBuff = new char[length * 2 + 1];
      }

      data.getChars(0, length, super.m_charsBuff, 0);
      this.comment(super.m_charsBuff, 0, length);
   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      this.flushPending();
      if (super.m_tracer != null) {
         super.fireCommentEvent(ch, start, length);
      }

   }

   public void entityReference(String name) throws SAXException {
      if (super.m_tracer != null) {
         super.fireEntityReference(name);
      }

   }

   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) {
   }

   public void endCDATA() throws SAXException {
   }

   public void endElement(String elemName) throws SAXException {
      if (super.m_tracer != null) {
         super.fireEndElem(elemName);
      }

   }

   public void startElement(String elementNamespaceURI, String elementLocalName, String elementName) throws SAXException {
      if (super.m_needToCallStartDocument) {
         this.startDocumentInternal();
      }

      if (super.m_tracer != null) {
         super.fireStartElem(elementName);
         this.firePseudoAttributes();
      }

   }

   public void characters(String characters) throws SAXException {
      int length = characters.length();
      if (length > super.m_charsBuff.length) {
         super.m_charsBuff = new char[length * 2 + 1];
      }

      characters.getChars(0, length, super.m_charsBuff, 0);
      this.characters(super.m_charsBuff, 0, length);
   }

   public void addAttribute(String name, String value) {
   }

   public void addUniqueAttribute(String qName, String value, int flags) throws SAXException {
   }

   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws SAXException {
      return false;
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
   }

   public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
   }

   public void flushPending() throws SAXException {
      if (super.m_needToCallStartDocument) {
         this.startDocumentInternal();
         super.m_needToCallStartDocument = false;
      }

   }
}
