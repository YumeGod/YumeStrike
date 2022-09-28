package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.WrappedRuntimeException;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract class ToStream extends SerializerBase {
   private static final String COMMENT_BEGIN = "<!--";
   private static final String COMMENT_END = "-->";
   protected BoolStack m_disableOutputEscapingStates = new BoolStack();
   EncodingInfo m_encodingInfo = new EncodingInfo((String)null, (String)null);
   protected BoolStack m_preserves = new BoolStack();
   protected boolean m_ispreserve = false;
   protected boolean m_isprevtext = false;
   protected char[] m_lineSep = System.getProperty("line.separator").toCharArray();
   protected boolean m_lineSepUse = true;
   protected int m_lineSepLen;
   protected CharInfo m_charInfo;
   boolean m_shouldFlush;
   protected boolean m_spaceBeforeClose;
   boolean m_startNewLine;
   protected boolean m_inDoctype;
   boolean m_isUTF8;
   protected Properties m_format;
   protected boolean m_cdataStartCalled;
   private boolean m_expandDTDEntities;
   private boolean m_escaping;

   public ToStream() {
      this.m_lineSepLen = this.m_lineSep.length;
      this.m_shouldFlush = true;
      this.m_spaceBeforeClose = false;
      this.m_inDoctype = false;
      this.m_isUTF8 = false;
      this.m_cdataStartCalled = false;
      this.m_expandDTDEntities = true;
      this.m_escaping = true;
   }

   protected void closeCDATA() throws SAXException {
      try {
         super.m_writer.write("]]>");
         super.m_cdataTagOpen = false;
      } catch (IOException var2) {
         throw new SAXException(var2);
      }
   }

   public void serialize(Node node) throws IOException {
      try {
         TreeWalker walker = new TreeWalker(this);
         walker.traverse(node);
      } catch (SAXException var3) {
         throw new WrappedRuntimeException(var3);
      }
   }

   protected final void flushWriter() throws SAXException {
      Writer writer = super.m_writer;
      if (null != writer) {
         try {
            if (writer instanceof WriterToUTF8Buffered) {
               if (this.m_shouldFlush) {
                  ((WriterToUTF8Buffered)writer).flush();
               } else {
                  ((WriterToUTF8Buffered)writer).flushBuffer();
               }
            }

            if (writer instanceof WriterToASCI) {
               if (this.m_shouldFlush) {
                  writer.flush();
               }
            } else {
               writer.flush();
            }
         } catch (IOException var3) {
            throw new SAXException(var3);
         }
      }

   }

   public OutputStream getOutputStream() {
      if (super.m_writer instanceof WriterToUTF8Buffered) {
         return ((WriterToUTF8Buffered)super.m_writer).getOutputStream();
      } else {
         return super.m_writer instanceof WriterToASCI ? ((WriterToASCI)super.m_writer).getOutputStream() : null;
      }
   }

   public void elementDecl(String name, String model) throws SAXException {
      if (!super.m_inExternalDTD) {
         try {
            Writer writer = super.m_writer;
            this.DTDprolog();
            writer.write("<!ELEMENT ");
            writer.write(name);
            writer.write(32);
            writer.write(model);
            writer.write(62);
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
         } catch (IOException var4) {
            throw new SAXException(var4);
         }
      }
   }

   public void internalEntityDecl(String name, String value) throws SAXException {
      if (!super.m_inExternalDTD) {
         try {
            this.DTDprolog();
            this.outputEntityDecl(name, value);
         } catch (IOException var4) {
            throw new SAXException(var4);
         }
      }
   }

   void outputEntityDecl(String name, String value) throws IOException {
      Writer writer = super.m_writer;
      writer.write("<!ENTITY ");
      writer.write(name);
      writer.write(" \"");
      writer.write(value);
      writer.write("\">");
      writer.write(this.m_lineSep, 0, this.m_lineSepLen);
   }

   protected final void outputLineSep() throws IOException {
      super.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
   }

   public void setOutputFormat(Properties format) {
      boolean shouldFlush = this.m_shouldFlush;
      this.init(super.m_writer, format, false, false);
      this.m_shouldFlush = shouldFlush;
   }

   private synchronized void init(Writer writer, Properties format, boolean defaultProperties, boolean shouldFlush) {
      this.m_shouldFlush = shouldFlush;
      if (super.m_tracer != null && !(writer instanceof SerializerTraceWriter)) {
         super.m_writer = new SerializerTraceWriter(writer, super.m_tracer);
      } else {
         super.m_writer = writer;
      }

      this.m_format = format;
      this.setCdataSectionElements("cdata-section-elements", format);
      this.setIndentAmount(OutputPropertyUtils.getIntProperty("{http://xml.apache.org/xalan}indent-amount", format));
      this.setIndent(OutputPropertyUtils.getBooleanProperty("indent", format));
      String sep = format.getProperty("{http://xml.apache.org/xalan}line-separator");
      if (sep != null) {
         this.m_lineSep = sep.toCharArray();
         this.m_lineSepLen = sep.length();
      }

      boolean shouldNotWriteXMLHeader = OutputPropertyUtils.getBooleanProperty("omit-xml-declaration", format);
      this.setOmitXMLDeclaration(shouldNotWriteXMLHeader);
      this.setDoctypeSystem(format.getProperty("doctype-system"));
      String doctypePublic = format.getProperty("doctype-public");
      this.setDoctypePublic(doctypePublic);
      String version;
      if (format.get("standalone") != null) {
         version = format.getProperty("standalone");
         if (defaultProperties) {
            this.setStandaloneInternal(version);
         } else {
            this.setStandalone(version);
         }
      }

      this.setMediaType(format.getProperty("media-type"));
      if (null != doctypePublic && doctypePublic.startsWith("-//W3C//DTD XHTML")) {
         this.m_spaceBeforeClose = true;
      }

      version = this.getVersion();
      if (null == version) {
         version = format.getProperty("version");
         this.setVersion(version);
      }

      String encoding = this.getEncoding();
      if (null == encoding) {
         encoding = Encodings.getMimeEncoding(format.getProperty("encoding"));
         this.setEncoding(encoding);
      }

      this.m_isUTF8 = encoding.equals("UTF-8");
      String entitiesFileName = (String)format.get("{http://xml.apache.org/xalan}entities");
      if (null != entitiesFileName) {
         String method = (String)format.get("method");
         this.m_charInfo = CharInfo.getCharInfo(entitiesFileName, method);
      }

   }

   private synchronized void init(Writer writer, Properties format) {
      this.init(writer, format, false, false);
   }

   protected synchronized void init(OutputStream output, Properties format, boolean defaultProperties) throws UnsupportedEncodingException {
      String encoding = this.getEncoding();
      if (encoding == null) {
         encoding = Encodings.getMimeEncoding(format.getProperty("encoding"));
         this.setEncoding(encoding);
      }

      if (encoding.equalsIgnoreCase("UTF-8")) {
         this.m_isUTF8 = true;
         this.init(new WriterToUTF8Buffered(output), format, defaultProperties, true);
      } else if (!encoding.equals("WINDOWS-1250") && !encoding.equals("US-ASCII") && !encoding.equals("ASCII")) {
         Writer osw;
         try {
            osw = Encodings.getWriter(output, encoding);
         } catch (UnsupportedEncodingException var7) {
            System.out.println("Warning: encoding \"" + encoding + "\" not supported" + ", using " + "UTF-8");
            encoding = "UTF-8";
            this.setEncoding(encoding);
            osw = Encodings.getWriter(output, encoding);
         }

         this.init(osw, format, defaultProperties, true);
      } else {
         this.init(new WriterToASCI(output), format, defaultProperties, true);
      }

   }

   public Properties getOutputFormat() {
      return this.m_format;
   }

   public void setWriter(Writer writer) {
      if (super.m_tracer != null && !(writer instanceof SerializerTraceWriter)) {
         super.m_writer = new SerializerTraceWriter(writer, super.m_tracer);
      } else {
         super.m_writer = writer;
      }

   }

   public boolean setLineSepUse(boolean use_sytem_line_break) {
      boolean oldValue = this.m_lineSepUse;
      this.m_lineSepUse = use_sytem_line_break;
      return oldValue;
   }

   public void setOutputStream(OutputStream output) {
      try {
         Properties format;
         if (null == this.m_format) {
            format = OutputPropertiesFactory.getDefaultMethodProperties("xml");
         } else {
            format = this.m_format;
         }

         this.init(output, format, true);
      } catch (UnsupportedEncodingException var3) {
      }

   }

   public boolean setEscaping(boolean escape) {
      boolean temp = this.m_escaping;
      this.m_escaping = escape;
      return temp;
   }

   protected void indent(int depth) throws IOException {
      if (this.m_startNewLine) {
         this.outputLineSep();
      }

      if (super.m_indentAmount > 0) {
         this.printSpace(depth * super.m_indentAmount);
      }

   }

   protected void indent() throws IOException {
      this.indent(super.m_elemContext.m_currentElemDepth);
   }

   private void printSpace(int n) throws IOException {
      Writer writer = super.m_writer;

      for(int i = 0; i < n; ++i) {
         writer.write(32);
      }

   }

   public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
      if (!super.m_inExternalDTD) {
         try {
            Writer writer = super.m_writer;
            this.DTDprolog();
            writer.write("<!ATTLIST ");
            writer.write(eName);
            writer.write(32);
            writer.write(aName);
            writer.write(32);
            writer.write(type);
            if (valueDefault != null) {
               writer.write(32);
               writer.write(valueDefault);
            }

            writer.write(62);
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
         } catch (IOException var7) {
            throw new SAXException(var7);
         }
      }
   }

   public Writer getWriter() {
      return super.m_writer;
   }

   public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
      try {
         this.DTDprolog();
         super.m_writer.write("<!ENTITY ");
         super.m_writer.write(name);
         if (publicId != null) {
            super.m_writer.write(" PUBLIC \"");
            super.m_writer.write(publicId);
         } else {
            super.m_writer.write(" SYSTEM \"");
            super.m_writer.write(systemId);
         }

         super.m_writer.write("\" >");
         super.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   protected boolean escapingNotNeeded(char ch) {
      boolean ret;
      if (ch < 127) {
         if (ch < ' ' && '\n' != ch && '\r' != ch && '\t' != ch) {
            ret = false;
         } else {
            ret = true;
         }
      } else {
         ret = this.m_encodingInfo.isInEncoding(ch);
      }

      return ret;
   }

   protected int writeUTF16Surrogate(char c, char[] ch, int i, int end) throws IOException {
      int codePoint = 0;
      if (i + 1 >= end) {
         throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(c)}));
      } else {
         char low = ch[i + 1];
         if (!Encodings.isLowUTF16Surrogate(low)) {
            throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(c) + " " + Integer.toHexString(low)}));
         } else {
            Writer writer = super.m_writer;
            if (this.m_encodingInfo.isInEncoding(c, low)) {
               writer.write(ch, i, 2);
            } else {
               String encoding = this.getEncoding();
               if (encoding != null) {
                  codePoint = Encodings.toCodePoint(c, low);
                  writer.write(38);
                  writer.write(35);
                  writer.write(Integer.toString(codePoint));
                  writer.write(59);
               } else {
                  writer.write(ch, i, 2);
               }
            }

            return codePoint;
         }
      }
   }

   protected int accumDefaultEntity(Writer writer, char ch, int i, char[] chars, int len, boolean fromTextNode, boolean escLF) throws IOException {
      if (!escLF && '\n' == ch) {
         writer.write(this.m_lineSep, 0, this.m_lineSepLen);
      } else {
         if ((!fromTextNode || !this.m_charInfo.isSpecialTextChar(ch)) && (fromTextNode || !this.m_charInfo.isSpecialAttrChar(ch))) {
            return i;
         }

         String outputStringForChar = this.m_charInfo.getOutputStringForChar(ch);
         if (null == outputStringForChar) {
            return i;
         }

         writer.write(outputStringForChar);
      }

      return i + 1;
   }

   void writeNormalizedChars(char[] ch, int start, int length, boolean isCData, boolean useSystemLineSeparator) throws IOException, SAXException {
      Writer writer = super.m_writer;
      int end = start + length;

      for(int i = start; i < end; ++i) {
         char c = ch[i];
         if ('\n' == c && useSystemLineSeparator) {
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
         } else {
            String intStr;
            if (isCData && !this.escapingNotNeeded(c)) {
               if (super.m_cdataTagOpen) {
                  this.closeCDATA();
               }

               if (Encodings.isHighUTF16Surrogate(c)) {
                  this.writeUTF16Surrogate(c, ch, i, end);
                  ++i;
               } else {
                  writer.write("&#");
                  intStr = Integer.toString(c);
                  writer.write(intStr);
                  writer.write(59);
               }
            } else if (isCData && i < end - 2 && ']' == c && ']' == ch[i + 1] && '>' == ch[i + 2]) {
               writer.write("]]]]><![CDATA[>");
               i += 2;
            } else if (this.escapingNotNeeded(c)) {
               if (isCData && !super.m_cdataTagOpen) {
                  writer.write("<![CDATA[");
                  super.m_cdataTagOpen = true;
               }

               writer.write(c);
            } else if (Encodings.isHighUTF16Surrogate(c)) {
               if (super.m_cdataTagOpen) {
                  this.closeCDATA();
               }

               this.writeUTF16Surrogate(c, ch, i, end);
               ++i;
            } else {
               if (super.m_cdataTagOpen) {
                  this.closeCDATA();
               }

               writer.write("&#");
               intStr = Integer.toString(c);
               writer.write(intStr);
               writer.write(59);
            }
         }
      }

   }

   public void endNonEscaping() throws SAXException {
      this.m_disableOutputEscapingStates.pop();
   }

   public void startNonEscaping() throws SAXException {
      this.m_disableOutputEscapingStates.push(true);
   }

   protected void cdata(char[] ch, int start, int length) throws SAXException {
      try {
         if (super.m_elemContext.m_startTagOpen) {
            this.closeStartTag();
            super.m_elemContext.m_startTagOpen = false;
         }

         this.m_ispreserve = true;
         if (this.shouldIndent()) {
            this.indent();
         }

         boolean writeCDataBrackets = length >= 1 && this.escapingNotNeeded(ch[start]);
         if (writeCDataBrackets && !super.m_cdataTagOpen) {
            super.m_writer.write("<![CDATA[");
            super.m_cdataTagOpen = true;
         }

         if (this.isEscapingDisabled()) {
            this.charactersRaw(ch, start, length);
         } else {
            this.writeNormalizedChars(ch, start, length, true, this.m_lineSepUse);
         }

         if (writeCDataBrackets && ch[start + length - 1] == ']') {
            this.closeCDATA();
         }

         if (super.m_tracer != null) {
            super.fireCDATAEvent(ch, start, length);
         }

      } catch (IOException var6) {
         throw new SAXException(Utils.messages.createMessage("ER_OIERROR", (Object[])null), var6);
      }
   }

   private boolean isEscapingDisabled() {
      return this.m_disableOutputEscapingStates.peekOrFalse();
   }

   protected void charactersRaw(char[] ch, int start, int length) throws SAXException {
      if (!super.m_inEntityRef) {
         try {
            if (super.m_elemContext.m_startTagOpen) {
               this.closeStartTag();
               super.m_elemContext.m_startTagOpen = false;
            }

            this.m_ispreserve = true;
            super.m_writer.write(ch, start, length);
         } catch (IOException var5) {
            throw new SAXException(var5);
         }
      }
   }

   public void characters(char[] chars, int start, int length) throws SAXException {
      if (length != 0 && (!super.m_inEntityRef || this.m_expandDTDEntities)) {
         if (super.m_elemContext.m_startTagOpen) {
            this.closeStartTag();
            super.m_elemContext.m_startTagOpen = false;
         } else if (super.m_needToCallStartDocument) {
            this.startDocumentInternal();
         }

         if (!this.m_cdataStartCalled && !super.m_elemContext.m_isCdataSection) {
            if (super.m_cdataTagOpen) {
               this.closeCDATA();
            }

            if (!this.m_disableOutputEscapingStates.peekOrFalse() && this.m_escaping) {
               if (super.m_elemContext.m_startTagOpen) {
                  this.closeStartTag();
                  super.m_elemContext.m_startTagOpen = false;
               }

               try {
                  int end = start + length;
                  int lastDirty = start - 1;

                  int i;
                  char ch1;
                  for(i = start; i < end && ((ch1 = chars[i]) == ' ' || ch1 == '\n' && this.m_lineSepUse || ch1 == '\r' || ch1 == '\t'); ++i) {
                     if (!this.m_charInfo.isTextASCIIClean(ch1)) {
                        lastDirty = this.processDirty(chars, end, i, ch1, lastDirty, true);
                        i = lastDirty;
                     }
                  }

                  if (i < end) {
                     this.m_ispreserve = true;
                  }

                  int ch;
                  for(boolean isXML10 = "1.0".equals(this.getVersion()); i < end; ++i) {
                     while(i < end && (ch = chars[i]) < 127 && this.m_charInfo.isTextASCIIClean(ch)) {
                        ++i;
                     }

                     if (i == end) {
                        break;
                     }

                     ch = chars[i];
                     if ((isCharacterInC0orC1Range((char)ch) || !isXML10 && isNELorLSEPCharacter((char)ch) || !this.escapingNotNeeded((char)ch) || this.m_charInfo.isSpecialTextChar(ch)) && 34 != ch) {
                        lastDirty = this.processDirty(chars, end, i, (char)ch, lastDirty, true);
                        i = lastDirty;
                     }
                  }

                  int startClean = lastDirty + 1;
                  if (i > startClean) {
                     ch = i - startClean;
                     super.m_writer.write(chars, startClean, ch);
                  }

                  this.m_isprevtext = true;
               } catch (IOException var11) {
                  throw new SAXException(var11);
               }

               if (super.m_tracer != null) {
                  super.fireCharEvent(chars, start, length);
               }

            } else {
               this.charactersRaw(chars, start, length);
               if (super.m_tracer != null) {
                  super.fireCharEvent(chars, start, length);
               }

            }
         } else {
            this.cdata(chars, start, length);
         }
      }
   }

   private static boolean isCharacterInC0orC1Range(char ch) {
      if (ch != '\t' && ch != '\n' && ch != '\r') {
         return ch >= 127 && ch <= 159 || ch >= 1 && ch <= 31;
      } else {
         return false;
      }
   }

   private static boolean isNELorLSEPCharacter(char ch) {
      return ch == 133 || ch == 8232;
   }

   private int processDirty(char[] chars, int end, int i, char ch, int lastDirty, boolean fromTextNode) throws IOException {
      int startClean = lastDirty + 1;
      if (i > startClean) {
         int lengthClean = i - startClean;
         super.m_writer.write(chars, startClean, lengthClean);
      }

      if ('\n' == ch && fromTextNode) {
         super.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
      } else {
         startClean = this.accumDefaultEscape(super.m_writer, ch, i, chars, end, fromTextNode, false);
         i = startClean - 1;
      }

      return i;
   }

   public void characters(String s) throws SAXException {
      if (!super.m_inEntityRef || this.m_expandDTDEntities) {
         int length = s.length();
         if (length > super.m_charsBuff.length) {
            super.m_charsBuff = new char[length * 2 + 1];
         }

         s.getChars(0, length, super.m_charsBuff, 0);
         this.characters(super.m_charsBuff, 0, length);
      }
   }

   protected int accumDefaultEscape(Writer writer, char ch, int i, char[] chars, int len, boolean fromTextNode, boolean escLF) throws IOException {
      int pos = this.accumDefaultEntity(writer, ch, i, chars, len, fromTextNode, escLF);
      if (i == pos) {
         if (Encodings.isHighUTF16Surrogate(ch)) {
            int codePoint = false;
            if (i + 1 >= len) {
               throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(ch)}));
            }

            ++i;
            char next = chars[i];
            if (!Encodings.isLowUTF16Surrogate(next)) {
               throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(ch) + " " + Integer.toHexString(next)}));
            }

            int codePoint = Encodings.toCodePoint(ch, next);
            writer.write("&#");
            writer.write(Integer.toString(codePoint));
            writer.write(59);
            pos += 2;
         } else {
            if (isCharacterInC0orC1Range(ch) || "1.1".equals(this.getVersion()) && isNELorLSEPCharacter(ch)) {
               writer.write("&#");
               writer.write(Integer.toString(ch));
               writer.write(59);
            } else if ((!this.escapingNotNeeded(ch) || fromTextNode && this.m_charInfo.isSpecialTextChar(ch) || !fromTextNode && this.m_charInfo.isSpecialAttrChar(ch)) && super.m_elemContext.m_currentElemDepth > 0) {
               writer.write("&#");
               writer.write(Integer.toString(ch));
               writer.write(59);
            } else {
               writer.write(ch);
            }

            ++pos;
         }
      }

      return pos;
   }

   public void startElement(String namespaceURI, String localName, String name, Attributes atts) throws SAXException {
      if (!super.m_inEntityRef) {
         if (super.m_needToCallStartDocument) {
            this.startDocumentInternal();
            super.m_needToCallStartDocument = false;
         } else if (super.m_cdataTagOpen) {
            this.closeCDATA();
         }

         try {
            if (super.m_needToOutputDocTypeDecl && null != this.getDoctypeSystem()) {
               this.outputDocTypeDecl(name, true);
            }

            super.m_needToOutputDocTypeDecl = false;
            if (super.m_elemContext.m_startTagOpen) {
               this.closeStartTag();
               super.m_elemContext.m_startTagOpen = false;
            }

            if (namespaceURI != null) {
               this.ensurePrefixIsDeclared(namespaceURI, name);
            }

            this.m_ispreserve = false;
            if (this.shouldIndent() && this.m_startNewLine) {
               this.indent();
            }

            this.m_startNewLine = true;
            Writer writer = super.m_writer;
            writer.write(60);
            writer.write(name);
         } catch (IOException var6) {
            throw new SAXException(var6);
         }

         if (atts != null) {
            this.addAttributes(atts);
         }

         super.m_elemContext = super.m_elemContext.push(namespaceURI, localName, name);
         this.m_isprevtext = false;
         if (super.m_tracer != null) {
            this.firePseudoAttributes();
         }

      }
   }

   public void startElement(String elementNamespaceURI, String elementLocalName, String elementName) throws SAXException {
      this.startElement(elementNamespaceURI, elementLocalName, elementName, (Attributes)null);
   }

   public void startElement(String elementName) throws SAXException {
      this.startElement((String)null, (String)null, elementName, (Attributes)null);
   }

   void outputDocTypeDecl(String name, boolean closeDecl) throws SAXException {
      if (super.m_cdataTagOpen) {
         this.closeCDATA();
      }

      try {
         Writer writer = super.m_writer;
         writer.write("<!DOCTYPE ");
         writer.write(name);
         String doctypePublic = this.getDoctypePublic();
         if (null != doctypePublic) {
            writer.write(" PUBLIC \"");
            writer.write(doctypePublic);
            writer.write(34);
         }

         String doctypeSystem = this.getDoctypeSystem();
         if (null != doctypeSystem) {
            if (null == doctypePublic) {
               writer.write(" SYSTEM \"");
            } else {
               writer.write(" \"");
            }

            writer.write(doctypeSystem);
            if (closeDecl) {
               writer.write("\">");
               writer.write(this.m_lineSep, 0, this.m_lineSepLen);
               closeDecl = false;
            } else {
               writer.write(34);
            }
         }

         boolean dothis = false;
         if (dothis && closeDecl) {
            writer.write(62);
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
         }

      } catch (IOException var7) {
         throw new SAXException(var7);
      }
   }

   public void processAttributes(Writer writer, int nAttrs) throws IOException, SAXException {
      String encoding = this.getEncoding();

      for(int i = 0; i < nAttrs; ++i) {
         String name = super.m_attributes.getQName(i);
         String value = super.m_attributes.getValue(i);
         writer.write(32);
         writer.write(name);
         writer.write("=\"");
         this.writeAttrString(writer, value, encoding);
         writer.write(34);
      }

   }

   public void writeAttrString(Writer writer, String string, String encoding) throws IOException {
      int len = string.length();
      if (len > super.m_attrBuff.length) {
         super.m_attrBuff = new char[len * 2 + 1];
      }

      string.getChars(0, len, super.m_attrBuff, 0);
      char[] stringChars = super.m_attrBuff;

      for(int i = 0; i < len; ++i) {
         char ch = stringChars[i];
         if (this.escapingNotNeeded(ch) && !this.m_charInfo.isSpecialAttrChar(ch)) {
            writer.write(ch);
         } else {
            this.accumDefaultEscape(writer, ch, i, stringChars, len, false, true);
         }
      }

   }

   public void endElement(String namespaceURI, String localName, String name) throws SAXException {
      if (!super.m_inEntityRef) {
         super.m_prefixMap.popNamespaces(super.m_elemContext.m_currentElemDepth, (ContentHandler)null);

         try {
            Writer writer = super.m_writer;
            if (super.m_elemContext.m_startTagOpen) {
               if (super.m_tracer != null) {
                  super.fireStartElem(super.m_elemContext.m_elementName);
               }

               int nAttrs = super.m_attributes.getLength();
               if (nAttrs > 0) {
                  this.processAttributes(super.m_writer, nAttrs);
                  super.m_attributes.clear();
               }

               if (this.m_spaceBeforeClose) {
                  writer.write(" />");
               } else {
                  writer.write("/>");
               }
            } else {
               if (super.m_cdataTagOpen) {
                  this.closeCDATA();
               }

               if (this.shouldIndent()) {
                  this.indent(super.m_elemContext.m_currentElemDepth - 1);
               }

               writer.write(60);
               writer.write(47);
               writer.write(name);
               writer.write(62);
            }
         } catch (IOException var6) {
            throw new SAXException(var6);
         }

         if (!super.m_elemContext.m_startTagOpen && super.m_doIndent) {
            this.m_ispreserve = this.m_preserves.isEmpty() ? false : this.m_preserves.pop();
         }

         this.m_isprevtext = false;
         if (super.m_tracer != null) {
            super.fireEndElem(name);
         }

         super.m_elemContext = super.m_elemContext.m_prev;
      }
   }

   public void endElement(String name) throws SAXException {
      this.endElement((String)null, (String)null, name);
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
         String name;
         if ("".equals(prefix)) {
            name = "xmlns";
            this.addAttributeAlways("http://www.w3.org/2000/xmlns/", name, name, "CDATA", uri, false);
         } else if (!"".equals(uri)) {
            name = "xmlns:" + prefix;
            this.addAttributeAlways("http://www.w3.org/2000/xmlns/", prefix, name, "CDATA", uri, false);
         }
      }

      return pushed;
   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      if (!super.m_inEntityRef) {
         if (super.m_elemContext.m_startTagOpen) {
            this.closeStartTag();
            super.m_elemContext.m_startTagOpen = false;
         } else if (super.m_needToCallStartDocument) {
            this.startDocumentInternal();
            super.m_needToCallStartDocument = false;
         }

         try {
            if (this.shouldIndent()) {
               this.indent();
            }

            int limit = start + length;
            boolean wasDash = false;
            if (super.m_cdataTagOpen) {
               this.closeCDATA();
            }

            Writer writer = super.m_writer;
            writer.write("<!--");

            for(int i = start; i < limit; ++i) {
               if (wasDash && ch[i] == '-') {
                  writer.write(ch, start, i - start);
                  writer.write(" -");
                  start = i + 1;
               }

               wasDash = ch[i] == '-';
            }

            if (length > 0) {
               int remainingChars = limit - start;
               if (remainingChars > 0) {
                  writer.write(ch, start, remainingChars);
               }

               if (ch[limit - 1] == '-') {
                  writer.write(32);
               }
            }

            writer.write("-->");
         } catch (IOException var10) {
            throw new SAXException(var10);
         }

         this.m_startNewLine = true;
         if (super.m_tracer != null) {
            super.fireCommentEvent(ch, start, length);
         }

      }
   }

   public void endCDATA() throws SAXException {
      if (super.m_cdataTagOpen) {
         this.closeCDATA();
      }

      this.m_cdataStartCalled = false;
   }

   public void endDTD() throws SAXException {
      try {
         if (super.m_needToOutputDocTypeDecl) {
            this.outputDocTypeDecl(super.m_elemContext.m_elementName, false);
            super.m_needToOutputDocTypeDecl = false;
         }

         Writer writer = super.m_writer;
         if (!this.m_inDoctype) {
            writer.write("]>");
         } else {
            writer.write(62);
         }

         writer.write(this.m_lineSep, 0, this.m_lineSepLen);
      } catch (IOException var2) {
         throw new SAXException(var2);
      }
   }

   public void endPrefixMapping(String prefix) throws SAXException {
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      if (0 != length) {
         this.characters(ch, start, length);
      }
   }

   public void skippedEntity(String name) throws SAXException {
   }

   public void startCDATA() throws SAXException {
      this.m_cdataStartCalled = true;
   }

   public void startEntity(String name) throws SAXException {
      if (name.equals("[dtd]")) {
         super.m_inExternalDTD = true;
      }

      if (!this.m_expandDTDEntities && !super.m_inExternalDTD) {
         this.startNonEscaping();
         this.characters("&" + name + ';');
         this.endNonEscaping();
      }

      super.m_inEntityRef = true;
   }

   protected void closeStartTag() throws SAXException {
      if (super.m_elemContext.m_startTagOpen) {
         try {
            if (super.m_tracer != null) {
               super.fireStartElem(super.m_elemContext.m_elementName);
            }

            int nAttrs = super.m_attributes.getLength();
            if (nAttrs > 0) {
               this.processAttributes(super.m_writer, nAttrs);
               super.m_attributes.clear();
            }

            super.m_writer.write(62);
         } catch (IOException var2) {
            throw new SAXException(var2);
         }

         if (super.m_cdataSectionElements != null) {
            super.m_elemContext.m_isCdataSection = this.isCdataSection();
         }

         if (super.m_doIndent) {
            this.m_isprevtext = false;
            this.m_preserves.push(this.m_ispreserve);
         }
      }

   }

   public void startDTD(String name, String publicId, String systemId) throws SAXException {
      this.setDoctypeSystem(systemId);
      this.setDoctypePublic(publicId);
      super.m_elemContext.m_elementName = name;
      this.m_inDoctype = true;
   }

   public int getIndentAmount() {
      return super.m_indentAmount;
   }

   public void setIndentAmount(int m_indentAmount) {
      super.m_indentAmount = m_indentAmount;
   }

   protected boolean shouldIndent() {
      return super.m_doIndent && !this.m_ispreserve && !this.m_isprevtext;
   }

   private void setCdataSectionElements(String key, Properties props) {
      String s = props.getProperty(key);
      if (null != s) {
         Vector v = new Vector();
         int l = s.length();
         boolean inCurly = false;
         StringBuffer buf = new StringBuffer();

         for(int i = 0; i < l; ++i) {
            char c = s.charAt(i);
            if (Character.isWhitespace(c)) {
               if (!inCurly) {
                  if (buf.length() > 0) {
                     this.addCdataSectionElement(buf.toString(), v);
                     buf.setLength(0);
                  }
                  continue;
               }
            } else if ('{' == c) {
               inCurly = true;
            } else if ('}' == c) {
               inCurly = false;
            }

            buf.append(c);
         }

         if (buf.length() > 0) {
            this.addCdataSectionElement(buf.toString(), v);
            buf.setLength(0);
         }

         this.setCdataSectionElements(v);
      }

   }

   private void addCdataSectionElement(String URI_and_localName, Vector v) {
      StringTokenizer tokenizer = new StringTokenizer(URI_and_localName, "{}", false);
      String s1 = tokenizer.nextToken();
      String s2 = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
      if (null == s2) {
         v.addElement((Object)null);
         v.addElement(s1);
      } else {
         v.addElement(s1);
         v.addElement(s2);
      }

   }

   public void setCdataSectionElements(Vector URI_and_localNames) {
      super.m_cdataSectionElements = URI_and_localNames;
   }

   protected String ensureAttributesNamespaceIsDeclared(String ns, String localName, String rawName) throws SAXException {
      if (ns != null && ns.length() > 0) {
         int index = false;
         int index;
         String prefixFromRawName = (index = rawName.indexOf(":")) < 0 ? "" : rawName.substring(0, index);
         String prefix;
         if (index > 0) {
            prefix = super.m_prefixMap.lookupNamespace(prefixFromRawName);
            if (prefix != null && prefix.equals(ns)) {
               return null;
            } else {
               this.startPrefixMapping(prefixFromRawName, ns, false);
               this.addAttribute("http://www.w3.org/2000/xmlns/", prefixFromRawName, "xmlns:" + prefixFromRawName, "CDATA", ns, false);
               return prefixFromRawName;
            }
         } else {
            prefix = super.m_prefixMap.lookupPrefix(ns);
            if (prefix == null) {
               prefix = super.m_prefixMap.generateNextPrefix();
               this.startPrefixMapping(prefix, ns, false);
               this.addAttribute("http://www.w3.org/2000/xmlns/", prefix, "xmlns:" + prefix, "CDATA", ns, false);
            }

            return prefix;
         }
      } else {
         return null;
      }
   }

   void ensurePrefixIsDeclared(String ns, String rawName) throws SAXException {
      if (ns != null && ns.length() > 0) {
         int index;
         boolean no_prefix = (index = rawName.indexOf(":")) < 0;
         String prefix = no_prefix ? "" : rawName.substring(0, index);
         if (null != prefix) {
            String foundURI = super.m_prefixMap.lookupNamespace(prefix);
            if (null == foundURI || !foundURI.equals(ns)) {
               this.startPrefixMapping(prefix, ns);
               this.addAttributeAlways("http://www.w3.org/2000/xmlns/", no_prefix ? "xmlns" : prefix, no_prefix ? "xmlns" : "xmlns:" + prefix, "CDATA", ns, false);
            }
         }
      }

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

   public void setContentHandler(ContentHandler ch) {
   }

   public boolean addAttributeAlways(String uri, String localName, String rawName, String type, String value, boolean xslAttribute) {
      int index;
      if (uri != null && localName != null && uri.length() != 0) {
         index = super.m_attributes.getIndex(uri, localName);
      } else {
         index = super.m_attributes.getIndex(rawName);
      }

      boolean was_added;
      if (index >= 0) {
         String old_value = null;
         if (super.m_tracer != null) {
            old_value = super.m_attributes.getValue(index);
            if (value.equals(old_value)) {
               old_value = null;
            }
         }

         super.m_attributes.setValue(index, value);
         was_added = false;
         if (old_value != null) {
            this.firePseudoAttributes();
         }
      } else {
         if (xslAttribute) {
            int colonIndex = rawName.indexOf(58);
            if (colonIndex > 0) {
               String prefix = rawName.substring(0, colonIndex);
               NamespaceMappings.MappingRecord existing_mapping = super.m_prefixMap.getMappingFromPrefix(prefix);
               if (existing_mapping != null && existing_mapping.m_declarationDepth == super.m_elemContext.m_currentElemDepth && !existing_mapping.m_uri.equals(uri)) {
                  prefix = super.m_prefixMap.lookupPrefix(uri);
                  if (prefix == null) {
                     prefix = super.m_prefixMap.generateNextPrefix();
                  }

                  rawName = prefix + ':' + localName;
               }
            }

            try {
               this.ensureAttributesNamespaceIsDeclared(uri, localName, rawName);
            } catch (SAXException var12) {
               var12.printStackTrace();
            }
         }

         super.m_attributes.addAttribute(uri, localName, rawName, type, value);
         was_added = true;
         if (super.m_tracer != null) {
            this.firePseudoAttributes();
         }
      }

      return was_added;
   }

   protected void firePseudoAttributes() {
      if (super.m_tracer != null) {
         try {
            super.m_writer.flush();
            StringBuffer sb = new StringBuffer();
            int nAttrs = super.m_attributes.getLength();
            if (nAttrs > 0) {
               Writer writer = new WritertoStringBuffer(sb);
               this.processAttributes(writer, nAttrs);
            }

            sb.append('>');
            char[] ch = sb.toString().toCharArray();
            super.m_tracer.fireGenerateEvent(11, ch, 0, ch.length);
         } catch (IOException var4) {
         } catch (SAXException var5) {
         }
      }

   }

   public void setTransformer(Transformer transformer) {
      super.setTransformer(transformer);
      if (super.m_tracer != null && !(super.m_writer instanceof SerializerTraceWriter)) {
         super.m_writer = new SerializerTraceWriter(super.m_writer, super.m_tracer);
      }

   }

   public boolean reset() {
      boolean wasReset = false;
      if (super.reset()) {
         this.resetToStream();
         wasReset = true;
      }

      return wasReset;
   }

   private void resetToStream() {
      this.m_cdataStartCalled = false;
      this.m_disableOutputEscapingStates.clear();
      this.m_escaping = true;
      this.m_inDoctype = false;
      this.m_ispreserve = false;
      this.m_ispreserve = false;
      this.m_isprevtext = false;
      this.m_isUTF8 = false;
      this.m_preserves.clear();
      this.m_shouldFlush = true;
      this.m_spaceBeforeClose = false;
      this.m_startNewLine = false;
      this.m_lineSepUse = true;
      this.m_expandDTDEntities = true;
   }

   public void setEncoding(String encoding) {
      String old = this.getEncoding();
      super.setEncoding(encoding);
      if (old == null || !old.equals(encoding)) {
         this.m_encodingInfo = Encodings.getEncodingInfo(encoding);
         if (encoding != null && this.m_encodingInfo.name == null) {
            String msg = Utils.messages.createMessage("ER_ENCODING_NOT_SUPPORTED", new Object[]{encoding});

            try {
               Transformer tran = super.getTransformer();
               if (tran != null) {
                  ErrorListener errHandler = tran.getErrorListener();
                  if (null != errHandler && super.m_sourceLocator != null) {
                     errHandler.warning(new TransformerException(msg, super.m_sourceLocator));
                  } else {
                     System.out.println(msg);
                  }
               } else {
                  System.out.println(msg);
               }
            } catch (Exception var6) {
            }
         }
      }

   }

   public void notationDecl(String name, String pubID, String sysID) throws SAXException {
      try {
         this.DTDprolog();
         super.m_writer.write("<!NOTATION ");
         super.m_writer.write(name);
         if (pubID != null) {
            super.m_writer.write(" PUBLIC \"");
            super.m_writer.write(pubID);
         } else {
            super.m_writer.write(" SYSTEM \"");
            super.m_writer.write(sysID);
         }

         super.m_writer.write("\" >");
         super.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void unparsedEntityDecl(String name, String pubID, String sysID, String notationName) throws SAXException {
      try {
         this.DTDprolog();
         super.m_writer.write("<!ENTITY ");
         super.m_writer.write(name);
         if (pubID != null) {
            super.m_writer.write(" PUBLIC \"");
            super.m_writer.write(pubID);
         } else {
            super.m_writer.write(" SYSTEM \"");
            super.m_writer.write(sysID);
         }

         super.m_writer.write("\" NDATA ");
         super.m_writer.write(notationName);
         super.m_writer.write(" >");
         super.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   private void DTDprolog() throws SAXException, IOException {
      Writer writer = super.m_writer;
      if (super.m_needToOutputDocTypeDecl) {
         this.outputDocTypeDecl(super.m_elemContext.m_elementName, false);
         super.m_needToOutputDocTypeDecl = false;
      }

      if (this.m_inDoctype) {
         writer.write(" [");
         writer.write(this.m_lineSep, 0, this.m_lineSepLen);
         this.m_inDoctype = false;
      }

   }

   public void setDTDEntityExpansion(boolean expand) {
      this.m_expandDTDEntities = expand;
   }

   static final class BoolStack {
      private boolean[] m_values;
      private int m_allocatedSize;
      private int m_index;

      public BoolStack() {
         this(32);
      }

      public BoolStack(int size) {
         this.m_allocatedSize = size;
         this.m_values = new boolean[size];
         this.m_index = -1;
      }

      public final int size() {
         return this.m_index + 1;
      }

      public final void clear() {
         this.m_index = -1;
      }

      public final boolean push(boolean val) {
         if (this.m_index == this.m_allocatedSize - 1) {
            this.grow();
         }

         return this.m_values[++this.m_index] = val;
      }

      public final boolean pop() {
         return this.m_values[this.m_index--];
      }

      public final boolean popAndTop() {
         --this.m_index;
         return this.m_index >= 0 ? this.m_values[this.m_index] : false;
      }

      public final void setTop(boolean b) {
         this.m_values[this.m_index] = b;
      }

      public final boolean peek() {
         return this.m_values[this.m_index];
      }

      public final boolean peekOrFalse() {
         return this.m_index > -1 ? this.m_values[this.m_index] : false;
      }

      public final boolean peekOrTrue() {
         return this.m_index > -1 ? this.m_values[this.m_index] : true;
      }

      public boolean isEmpty() {
         return this.m_index == -1;
      }

      private void grow() {
         this.m_allocatedSize *= 2;
         boolean[] newVector = new boolean[this.m_allocatedSize];
         System.arraycopy(this.m_values, 0, newVector, 0, this.m_index + 1);
         this.m_values = newVector;
      }
   }

   private class WritertoStringBuffer extends Writer {
      private final StringBuffer m_stringbuf;

      WritertoStringBuffer(StringBuffer sb) {
         this.m_stringbuf = sb;
      }

      public void write(char[] arg0, int arg1, int arg2) throws IOException {
         this.m_stringbuf.append(arg0, arg1, arg2);
      }

      public void flush() throws IOException {
      }

      public void close() throws IOException {
      }

      public void write(int i) {
         this.m_stringbuf.append((char)i);
      }

      public void write(String s) {
         this.m_stringbuf.append(s);
      }
   }
}
