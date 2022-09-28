package org.apache.xml.serializer;

import java.io.IOException;
import java.io.Writer;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import org.apache.xml.serializer.utils.Utils;
import org.xml.sax.SAXException;

public final class ToXMLStream extends ToStream {
   boolean m_cdataTagOpen = false;
   private static CharInfo m_xmlcharInfo = CharInfo.getCharInfo("org.apache.xml.serializer.XMLEntities", "xml");

   public ToXMLStream() {
      super.m_charInfo = m_xmlcharInfo;
      this.initCDATA();
      super.m_prefixMap = new NamespaceMappings();
   }

   public void CopyFrom(ToXMLStream xmlListener) {
      super.m_writer = xmlListener.m_writer;
      String encoding = xmlListener.getEncoding();
      this.setEncoding(encoding);
      this.setOmitXMLDeclaration(xmlListener.getOmitXMLDeclaration());
      super.m_ispreserve = xmlListener.m_ispreserve;
      super.m_preserves = xmlListener.m_preserves;
      super.m_isprevtext = xmlListener.m_isprevtext;
      super.m_doIndent = xmlListener.m_doIndent;
      this.setIndentAmount(xmlListener.getIndentAmount());
      super.m_startNewLine = xmlListener.m_startNewLine;
      super.m_needToOutputDocTypeDecl = xmlListener.m_needToOutputDocTypeDecl;
      this.setDoctypeSystem(xmlListener.getDoctypeSystem());
      this.setDoctypePublic(xmlListener.getDoctypePublic());
      this.setStandalone(xmlListener.getStandalone());
      this.setMediaType(xmlListener.getMediaType());
      super.m_encodingInfo = xmlListener.m_encodingInfo;
      super.m_spaceBeforeClose = xmlListener.m_spaceBeforeClose;
      super.m_cdataStartCalled = xmlListener.m_cdataStartCalled;
   }

   public void startDocumentInternal() throws SAXException {
      if (super.m_needToCallStartDocument) {
         super.startDocumentInternal();
         super.m_needToCallStartDocument = false;
         if (super.m_inEntityRef) {
            return;
         }

         super.m_needToOutputDocTypeDecl = true;
         super.m_startNewLine = false;
         String version = this.getXMLVersion();
         if (!this.getOmitXMLDeclaration()) {
            String encoding = Encodings.getMimeEncoding(this.getEncoding());
            String standalone;
            if (super.m_standaloneWasSpecified) {
               standalone = " standalone=\"" + this.getStandalone() + "\"";
            } else {
               standalone = "";
            }

            try {
               Writer writer = super.m_writer;
               writer.write("<?xml version=\"");
               writer.write(version);
               writer.write("\" encoding=\"");
               writer.write(encoding);
               writer.write(34);
               writer.write(standalone);
               writer.write("?>");
               if (super.m_doIndent) {
                  writer.write(super.m_lineSep, 0, super.m_lineSepLen);
               }
            } catch (IOException var5) {
               throw new SAXException(var5);
            }
         }
      }

   }

   public void endDocument() throws SAXException {
      this.flushPending();
      if (super.m_doIndent && !super.m_isprevtext) {
         try {
            this.outputLineSep();
         } catch (IOException var2) {
            throw new SAXException(var2);
         }
      }

      this.flushWriter();
      if (super.m_tracer != null) {
         super.fireEndDoc();
      }

   }

   public void startPreserving() throws SAXException {
      super.m_preserves.push(true);
      super.m_ispreserve = true;
   }

   public void endPreserving() throws SAXException {
      super.m_ispreserve = super.m_preserves.isEmpty() ? false : super.m_preserves.pop();
   }

   public void processingInstruction(String target, String data) throws SAXException {
      if (!super.m_inEntityRef) {
         this.flushPending();
         if (target.equals("javax.xml.transform.disable-output-escaping")) {
            this.startNonEscaping();
         } else if (target.equals("javax.xml.transform.enable-output-escaping")) {
            this.endNonEscaping();
         } else {
            try {
               if (super.m_elemContext.m_startTagOpen) {
                  this.closeStartTag();
                  super.m_elemContext.m_startTagOpen = false;
               } else if (super.m_needToCallStartDocument) {
                  this.startDocumentInternal();
               }

               if (this.shouldIndent()) {
                  this.indent();
               }

               Writer writer = super.m_writer;
               writer.write("<?");
               writer.write(target);
               if (data.length() > 0 && !Character.isSpaceChar(data.charAt(0))) {
                  writer.write(32);
               }

               int indexOfQLT = data.indexOf("?>");
               if (indexOfQLT >= 0) {
                  if (indexOfQLT > 0) {
                     writer.write(data.substring(0, indexOfQLT));
                  }

                  writer.write("? >");
                  if (indexOfQLT + 2 < data.length()) {
                     writer.write(data.substring(indexOfQLT + 2));
                  }
               } else {
                  writer.write(data);
               }

               writer.write(63);
               writer.write(62);
               if (super.m_elemContext.m_currentElemDepth <= 0) {
                  writer.write(super.m_lineSep, 0, super.m_lineSepLen);
               }

               super.m_startNewLine = true;
            } catch (IOException var5) {
               throw new SAXException(var5);
            }
         }

         if (super.m_tracer != null) {
            super.fireEscapingEvent(target, data);
         }

      }
   }

   public void entityReference(String name) throws SAXException {
      if (super.m_elemContext.m_startTagOpen) {
         this.closeStartTag();
         super.m_elemContext.m_startTagOpen = false;
      }

      try {
         if (this.shouldIndent()) {
            this.indent();
         }

         Writer writer = super.m_writer;
         writer.write(38);
         writer.write(name);
         writer.write(59);
      } catch (IOException var3) {
         throw new SAXException(var3);
      }

      if (super.m_tracer != null) {
         super.fireEntityReference(name);
      }

   }

   public void addUniqueAttribute(String name, String value, int flags) throws SAXException {
      if (super.m_elemContext.m_startTagOpen) {
         try {
            String patchedName = this.patchName(name);
            Writer writer = super.m_writer;
            if ((flags & 1) > 0 && m_xmlcharInfo.onlyQuotAmpLtGt) {
               writer.write(32);
               writer.write(patchedName);
               writer.write("=\"");
               writer.write(value);
               writer.write(34);
            } else {
               writer.write(32);
               writer.write(patchedName);
               writer.write("=\"");
               this.writeAttrString(writer, value, this.getEncoding());
               writer.write(34);
            }
         } catch (IOException var6) {
            throw new SAXException(var6);
         }
      }

   }

   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean xslAttribute) throws SAXException {
      if (super.m_elemContext.m_startTagOpen) {
         boolean was_added = this.addAttributeAlways(uri, localName, rawName, type, value, xslAttribute);
         if (was_added && !xslAttribute && !rawName.startsWith("xmlns")) {
            String prefixUsed = this.ensureAttributesNamespaceIsDeclared(uri, localName, rawName);
            if (prefixUsed != null && rawName != null && !rawName.startsWith(prefixUsed)) {
               rawName = prefixUsed + ":" + localName;
            }
         }

         this.addAttributeAlways(uri, localName, rawName, type, value, xslAttribute);
      } else {
         String msg = Utils.messages.createMessage("ER_ILLEGAL_ATTRIBUTE_POSITION", new Object[]{localName});

         try {
            Transformer tran = super.getTransformer();
            ErrorListener errHandler = tran.getErrorListener();
            if (null != errHandler && super.m_sourceLocator != null) {
               errHandler.warning(new TransformerException(msg, super.m_sourceLocator));
            } else {
               System.out.println(msg);
            }
         } catch (Exception var10) {
         }
      }

   }

   public void endElement(String elemName) throws SAXException {
      this.endElement((String)null, (String)null, elemName);
   }

   public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
      if (super.m_elemContext.m_elementURI == null) {
         String prefix1 = SerializerBase.getPrefixPart(super.m_elemContext.m_elementName);
         if (prefix1 == null && "".equals(prefix)) {
            super.m_elemContext.m_elementURI = uri;
         }
      }

      this.startPrefixMapping(prefix, uri, false);
   }

   protected boolean pushNamespace(String prefix, String uri) {
      try {
         if (super.m_prefixMap.pushNamespace(prefix, uri, super.m_elemContext.m_currentElemDepth)) {
            this.startPrefixMapping(prefix, uri);
            return true;
         }
      } catch (SAXException var4) {
      }

      return false;
   }

   public boolean reset() {
      boolean wasReset = false;
      if (super.reset()) {
         this.resetToXMLStream();
         wasReset = true;
      }

      return wasReset;
   }

   private void resetToXMLStream() {
      this.m_cdataTagOpen = false;
   }

   private String getXMLVersion() {
      String xmlVersion = this.getVersion();
      if (xmlVersion != null && !xmlVersion.equals("1.0")) {
         if (xmlVersion.equals("1.1")) {
            xmlVersion = "1.1";
         } else {
            String msg = Utils.messages.createMessage("ER_XML_VERSION_NOT_SUPPORTED", new Object[]{xmlVersion});

            try {
               Transformer tran = super.getTransformer();
               ErrorListener errHandler = tran.getErrorListener();
               if (null != errHandler && super.m_sourceLocator != null) {
                  errHandler.warning(new TransformerException(msg, super.m_sourceLocator));
               } else {
                  System.out.println(msg);
               }
            } catch (Exception var5) {
            }

            xmlVersion = "1.0";
         }
      } else {
         xmlVersion = "1.0";
      }

      return xmlVersion;
   }
}
