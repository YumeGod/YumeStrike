package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Properties;
import org.apache.xml.serializer.utils.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public final class ToHTMLStream extends ToStream {
   protected boolean m_inDTD = false;
   private boolean m_inBlockElem = false;
   private static final CharInfo m_htmlcharInfo = CharInfo.getCharInfo("org.apache.xml.serializer.HTMLEntities", "html");
   static final Trie m_elementFlags = new Trie();
   private static final ElemDesc m_dummy;
   private boolean m_specialEscapeURLs = true;
   private boolean m_omitMetaTag = false;
   private Trie m_htmlInfo;

   static void initTagReference(Trie m_elementFlags) {
      m_elementFlags.put("BASEFONT", new ElemDesc(2));
      m_elementFlags.put("FRAME", new ElemDesc(10));
      m_elementFlags.put("FRAMESET", new ElemDesc(8));
      m_elementFlags.put("NOFRAMES", new ElemDesc(8));
      m_elementFlags.put("ISINDEX", new ElemDesc(10));
      m_elementFlags.put("APPLET", new ElemDesc(2097152));
      m_elementFlags.put("CENTER", new ElemDesc(8));
      m_elementFlags.put("DIR", new ElemDesc(8));
      m_elementFlags.put("MENU", new ElemDesc(8));
      m_elementFlags.put("TT", new ElemDesc(4096));
      m_elementFlags.put("I", new ElemDesc(4096));
      m_elementFlags.put("B", new ElemDesc(4096));
      m_elementFlags.put("BIG", new ElemDesc(4096));
      m_elementFlags.put("SMALL", new ElemDesc(4096));
      m_elementFlags.put("EM", new ElemDesc(8192));
      m_elementFlags.put("STRONG", new ElemDesc(8192));
      m_elementFlags.put("DFN", new ElemDesc(8192));
      m_elementFlags.put("CODE", new ElemDesc(8192));
      m_elementFlags.put("SAMP", new ElemDesc(8192));
      m_elementFlags.put("KBD", new ElemDesc(8192));
      m_elementFlags.put("VAR", new ElemDesc(8192));
      m_elementFlags.put("CITE", new ElemDesc(8192));
      m_elementFlags.put("ABBR", new ElemDesc(8192));
      m_elementFlags.put("ACRONYM", new ElemDesc(8192));
      m_elementFlags.put("SUP", new ElemDesc(98304));
      m_elementFlags.put("SUB", new ElemDesc(98304));
      m_elementFlags.put("SPAN", new ElemDesc(98304));
      m_elementFlags.put("BDO", new ElemDesc(98304));
      m_elementFlags.put("BR", new ElemDesc(98314));
      m_elementFlags.put("BODY", new ElemDesc(8));
      m_elementFlags.put("ADDRESS", new ElemDesc(56));
      m_elementFlags.put("DIV", new ElemDesc(56));
      m_elementFlags.put("A", new ElemDesc(32768));
      m_elementFlags.put("MAP", new ElemDesc(98312));
      m_elementFlags.put("AREA", new ElemDesc(10));
      m_elementFlags.put("LINK", new ElemDesc(131082));
      m_elementFlags.put("IMG", new ElemDesc(2195458));
      m_elementFlags.put("OBJECT", new ElemDesc(2326528));
      m_elementFlags.put("PARAM", new ElemDesc(2));
      m_elementFlags.put("HR", new ElemDesc(58));
      m_elementFlags.put("P", new ElemDesc(56));
      m_elementFlags.put("H1", new ElemDesc(262152));
      m_elementFlags.put("H2", new ElemDesc(262152));
      m_elementFlags.put("H3", new ElemDesc(262152));
      m_elementFlags.put("H4", new ElemDesc(262152));
      m_elementFlags.put("H5", new ElemDesc(262152));
      m_elementFlags.put("H6", new ElemDesc(262152));
      m_elementFlags.put("PRE", new ElemDesc(1048584));
      m_elementFlags.put("Q", new ElemDesc(98304));
      m_elementFlags.put("BLOCKQUOTE", new ElemDesc(56));
      m_elementFlags.put("INS", new ElemDesc(0));
      m_elementFlags.put("DEL", new ElemDesc(0));
      m_elementFlags.put("DL", new ElemDesc(56));
      m_elementFlags.put("DT", new ElemDesc(8));
      m_elementFlags.put("DD", new ElemDesc(8));
      m_elementFlags.put("OL", new ElemDesc(524296));
      m_elementFlags.put("UL", new ElemDesc(524296));
      m_elementFlags.put("LI", new ElemDesc(8));
      m_elementFlags.put("FORM", new ElemDesc(8));
      m_elementFlags.put("LABEL", new ElemDesc(16384));
      m_elementFlags.put("INPUT", new ElemDesc(18434));
      m_elementFlags.put("SELECT", new ElemDesc(18432));
      m_elementFlags.put("OPTGROUP", new ElemDesc(0));
      m_elementFlags.put("OPTION", new ElemDesc(0));
      m_elementFlags.put("TEXTAREA", new ElemDesc(18432));
      m_elementFlags.put("FIELDSET", new ElemDesc(24));
      m_elementFlags.put("LEGEND", new ElemDesc(0));
      m_elementFlags.put("BUTTON", new ElemDesc(18432));
      m_elementFlags.put("TABLE", new ElemDesc(56));
      m_elementFlags.put("CAPTION", new ElemDesc(8));
      m_elementFlags.put("THEAD", new ElemDesc(8));
      m_elementFlags.put("TFOOT", new ElemDesc(8));
      m_elementFlags.put("TBODY", new ElemDesc(8));
      m_elementFlags.put("COLGROUP", new ElemDesc(8));
      m_elementFlags.put("COL", new ElemDesc(10));
      m_elementFlags.put("TR", new ElemDesc(8));
      m_elementFlags.put("TH", new ElemDesc(0));
      m_elementFlags.put("TD", new ElemDesc(0));
      m_elementFlags.put("HEAD", new ElemDesc(4194312));
      m_elementFlags.put("TITLE", new ElemDesc(8));
      m_elementFlags.put("BASE", new ElemDesc(10));
      m_elementFlags.put("META", new ElemDesc(131082));
      m_elementFlags.put("STYLE", new ElemDesc(131336));
      m_elementFlags.put("SCRIPT", new ElemDesc(229632));
      m_elementFlags.put("NOSCRIPT", new ElemDesc(56));
      m_elementFlags.put("HTML", new ElemDesc(8));
      m_elementFlags.put("FONT", new ElemDesc(4096));
      m_elementFlags.put("S", new ElemDesc(4096));
      m_elementFlags.put("STRIKE", new ElemDesc(4096));
      m_elementFlags.put("U", new ElemDesc(4096));
      m_elementFlags.put("NOBR", new ElemDesc(4096));
      m_elementFlags.put("IFRAME", new ElemDesc(56));
      m_elementFlags.put("LAYER", new ElemDesc(56));
      m_elementFlags.put("ILAYER", new ElemDesc(56));
      ElemDesc elemDesc = (ElemDesc)m_elementFlags.get("A");
      elemDesc.setAttr("HREF", 2);
      elemDesc.setAttr("NAME", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("AREA");
      elemDesc.setAttr("HREF", 2);
      elemDesc.setAttr("NOHREF", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("BASE");
      elemDesc.setAttr("HREF", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("BUTTON");
      elemDesc.setAttr("DISABLED", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("BLOCKQUOTE");
      elemDesc.setAttr("CITE", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("DEL");
      elemDesc.setAttr("CITE", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("DIR");
      elemDesc.setAttr("COMPACT", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("DIV");
      elemDesc.setAttr("SRC", 2);
      elemDesc.setAttr("NOWRAP", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("DL");
      elemDesc.setAttr("COMPACT", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("FORM");
      elemDesc.setAttr("ACTION", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("FRAME");
      elemDesc.setAttr("SRC", 2);
      elemDesc.setAttr("LONGDESC", 2);
      elemDesc.setAttr("NORESIZE", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("HEAD");
      elemDesc.setAttr("PROFILE", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("HR");
      elemDesc.setAttr("NOSHADE", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("IFRAME");
      elemDesc.setAttr("SRC", 2);
      elemDesc.setAttr("LONGDESC", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("ILAYER");
      elemDesc.setAttr("SRC", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("IMG");
      elemDesc.setAttr("SRC", 2);
      elemDesc.setAttr("LONGDESC", 2);
      elemDesc.setAttr("USEMAP", 2);
      elemDesc.setAttr("ISMAP", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("INPUT");
      elemDesc.setAttr("SRC", 2);
      elemDesc.setAttr("USEMAP", 2);
      elemDesc.setAttr("CHECKED", 4);
      elemDesc.setAttr("DISABLED", 4);
      elemDesc.setAttr("ISMAP", 4);
      elemDesc.setAttr("READONLY", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("INS");
      elemDesc.setAttr("CITE", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("LAYER");
      elemDesc.setAttr("SRC", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("LINK");
      elemDesc.setAttr("HREF", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("MENU");
      elemDesc.setAttr("COMPACT", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("OBJECT");
      elemDesc.setAttr("CLASSID", 2);
      elemDesc.setAttr("CODEBASE", 2);
      elemDesc.setAttr("DATA", 2);
      elemDesc.setAttr("ARCHIVE", 2);
      elemDesc.setAttr("USEMAP", 2);
      elemDesc.setAttr("DECLARE", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("OL");
      elemDesc.setAttr("COMPACT", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("OPTGROUP");
      elemDesc.setAttr("DISABLED", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("OPTION");
      elemDesc.setAttr("SELECTED", 4);
      elemDesc.setAttr("DISABLED", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("Q");
      elemDesc.setAttr("CITE", 2);
      elemDesc = (ElemDesc)m_elementFlags.get("SCRIPT");
      elemDesc.setAttr("SRC", 2);
      elemDesc.setAttr("FOR", 2);
      elemDesc.setAttr("DEFER", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("SELECT");
      elemDesc.setAttr("DISABLED", 4);
      elemDesc.setAttr("MULTIPLE", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("TABLE");
      elemDesc.setAttr("NOWRAP", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("TD");
      elemDesc.setAttr("NOWRAP", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("TEXTAREA");
      elemDesc.setAttr("DISABLED", 4);
      elemDesc.setAttr("READONLY", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("TH");
      elemDesc.setAttr("NOWRAP", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("TR");
      elemDesc.setAttr("NOWRAP", 4);
      elemDesc = (ElemDesc)m_elementFlags.get("UL");
      elemDesc.setAttr("COMPACT", 4);
   }

   public void setSpecialEscapeURLs(boolean bool) {
      this.m_specialEscapeURLs = bool;
   }

   public void setOmitMetaTag(boolean bool) {
      this.m_omitMetaTag = bool;
   }

   public void setOutputFormat(Properties format) {
      this.m_specialEscapeURLs = OutputPropertyUtils.getBooleanProperty("{http://xml.apache.org/xalan}use-url-escaping", format);
      this.m_omitMetaTag = OutputPropertyUtils.getBooleanProperty("{http://xml.apache.org/xalan}omit-meta-tag", format);
      super.setOutputFormat(format);
   }

   private final boolean getSpecialEscapeURLs() {
      return this.m_specialEscapeURLs;
   }

   private final boolean getOmitMetaTag() {
      return this.m_omitMetaTag;
   }

   public static final ElemDesc getElemDesc(String name) {
      Object obj = m_elementFlags.get(name);
      return null != obj ? (ElemDesc)obj : m_dummy;
   }

   private ElemDesc getElemDesc2(String name) {
      Object obj = this.m_htmlInfo.get2(name);
      return null != obj ? (ElemDesc)obj : m_dummy;
   }

   public ToHTMLStream() {
      this.m_htmlInfo = new Trie(m_elementFlags);
      super.m_charInfo = m_htmlcharInfo;
      super.m_prefixMap = new NamespaceMappings();
   }

   protected void startDocumentInternal() throws SAXException {
      super.startDocumentInternal();
      super.m_needToCallStartDocument = false;
      super.m_needToOutputDocTypeDecl = true;
      super.m_startNewLine = false;
      this.setOmitXMLDeclaration(true);
      if (super.m_needToOutputDocTypeDecl) {
         String doctypeSystem = this.getDoctypeSystem();
         String doctypePublic = this.getDoctypePublic();
         if (null != doctypeSystem || null != doctypePublic) {
            Writer writer = super.m_writer;

            try {
               writer.write("<!DOCTYPE html");
               if (null != doctypePublic) {
                  writer.write(" PUBLIC \"");
                  writer.write(doctypePublic);
                  writer.write(34);
               }

               if (null != doctypeSystem) {
                  if (null == doctypePublic) {
                     writer.write(" SYSTEM \"");
                  } else {
                     writer.write(" \"");
                  }

                  writer.write(doctypeSystem);
                  writer.write(34);
               }

               writer.write(62);
               this.outputLineSep();
            } catch (IOException var5) {
               throw new SAXException(var5);
            }
         }
      }

      super.m_needToOutputDocTypeDecl = false;
   }

   public final void endDocument() throws SAXException {
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

   public void startElement(String namespaceURI, String localName, String name, Attributes atts) throws SAXException {
      ElemContext elemContext = super.m_elemContext;
      if (elemContext.m_startTagOpen) {
         this.closeStartTag();
         elemContext.m_startTagOpen = false;
      } else if (super.m_cdataTagOpen) {
         this.closeCDATA();
         super.m_cdataTagOpen = false;
      } else if (super.m_needToCallStartDocument) {
         this.startDocumentInternal();
         super.m_needToCallStartDocument = false;
      }

      if (null != namespaceURI && namespaceURI.length() > 0) {
         super.startElement(namespaceURI, localName, name, atts);
      } else {
         try {
            ElemDesc elemDesc = this.getElemDesc2(name);
            int elemFlags = elemDesc.getFlags();
            if (super.m_doIndent) {
               boolean isBlockElement = (elemFlags & 8) != 0;
               if (super.m_ispreserve) {
                  super.m_ispreserve = false;
               } else if (null != elemContext.m_elementName && (!this.m_inBlockElem || isBlockElement)) {
                  super.m_startNewLine = true;
                  this.indent();
               }

               this.m_inBlockElem = !isBlockElement;
            }

            if (atts != null) {
               this.addAttributes(atts);
            }

            super.m_isprevtext = false;
            Writer writer = super.m_writer;
            writer.write(60);
            writer.write(name);
            if (super.m_tracer != null) {
               this.firePseudoAttributes();
            }

            if ((elemFlags & 2) != 0) {
               super.m_elemContext = elemContext.push();
               super.m_elemContext.m_elementName = name;
               super.m_elemContext.m_elementDesc = elemDesc;
            } else {
               elemContext = elemContext.push(namespaceURI, localName, name);
               super.m_elemContext = elemContext;
               elemContext.m_elementDesc = elemDesc;
               elemContext.m_isRaw = (elemFlags & 256) != 0;
               if ((elemFlags & 4194304) != 0) {
                  this.closeStartTag();
                  elemContext.m_startTagOpen = false;
                  if (!this.m_omitMetaTag) {
                     if (super.m_doIndent) {
                        this.indent();
                     }

                     writer.write("<META http-equiv=\"Content-Type\" content=\"text/html; charset=");
                     String encoding = this.getEncoding();
                     String encode = Encodings.getMimeEncoding(encoding);
                     writer.write(encode);
                     writer.write("\">");
                  }
               }

            }
         } catch (IOException var11) {
            throw new SAXException(var11);
         }
      }
   }

   public final void endElement(String namespaceURI, String localName, String name) throws SAXException {
      if (super.m_cdataTagOpen) {
         this.closeCDATA();
      }

      if (null != namespaceURI && namespaceURI.length() > 0) {
         super.endElement(namespaceURI, localName, name);
      } else {
         try {
            ElemContext elemContext = super.m_elemContext;
            ElemDesc elemDesc = elemContext.m_elementDesc;
            int elemFlags = elemDesc.getFlags();
            boolean elemEmpty = (elemFlags & 2) != 0;
            if (super.m_doIndent) {
               boolean isBlockElement = (elemFlags & 8) != 0;
               boolean shouldIndent = false;
               if (super.m_ispreserve) {
                  super.m_ispreserve = false;
               } else if (super.m_doIndent && (!this.m_inBlockElem || isBlockElement)) {
                  super.m_startNewLine = true;
                  shouldIndent = true;
               }

               if (!elemContext.m_startTagOpen && shouldIndent) {
                  this.indent(elemContext.m_currentElemDepth - 1);
               }

               this.m_inBlockElem = !isBlockElement;
            }

            Writer writer = super.m_writer;
            if (!elemContext.m_startTagOpen) {
               writer.write("</");
               writer.write(name);
               writer.write(62);
            } else {
               if (super.m_tracer != null) {
                  super.fireStartElem(name);
               }

               int nAttrs = super.m_attributes.getLength();
               if (nAttrs > 0) {
                  this.processAttributes(super.m_writer, nAttrs);
                  super.m_attributes.clear();
               }

               if (!elemEmpty) {
                  writer.write("></");
                  writer.write(name);
                  writer.write(62);
               } else {
                  writer.write(62);
               }
            }

            if ((elemFlags & 2097152) != 0) {
               super.m_ispreserve = true;
            }

            super.m_isprevtext = false;
            if (super.m_tracer != null) {
               super.fireEndElem(name);
            }

            if (elemEmpty) {
               super.m_elemContext = elemContext.m_prev;
            } else {
               if (!elemContext.m_startTagOpen && super.m_doIndent && !super.m_preserves.isEmpty()) {
                  super.m_preserves.pop();
               }

               super.m_elemContext = elemContext.m_prev;
            }
         } catch (IOException var10) {
            throw new SAXException(var10);
         }
      }
   }

   protected void processAttribute(Writer writer, String name, String value, ElemDesc elemDesc) throws IOException {
      writer.write(32);
      if ((value.length() == 0 || value.equalsIgnoreCase(name)) && elemDesc != null && elemDesc.isAttrFlagSet(name, 4)) {
         writer.write(name);
      } else {
         writer.write(name);
         writer.write("=\"");
         if (elemDesc != null && elemDesc.isAttrFlagSet(name, 2)) {
            this.writeAttrURI(writer, value, this.m_specialEscapeURLs);
         } else {
            this.writeAttrString(writer, value, this.getEncoding());
         }

         writer.write(34);
      }

   }

   private boolean isASCIIDigit(char c) {
      return c >= '0' && c <= '9';
   }

   private static String makeHHString(int i) {
      String s = Integer.toHexString(i).toUpperCase();
      if (s.length() == 1) {
         s = "0" + s;
      }

      return s;
   }

   private boolean isHHSign(String str) {
      boolean sign = true;

      try {
         char var3 = (char)Integer.parseInt(str, 16);
      } catch (NumberFormatException var4) {
         sign = false;
      }

      return sign;
   }

   public void writeAttrURI(Writer writer, String string, boolean doURLEscaping) throws IOException {
      int end = string.length();
      if (end > super.m_attrBuff.length) {
         super.m_attrBuff = new char[end * 2 + 1];
      }

      string.getChars(0, end, super.m_attrBuff, 0);
      char[] chars = super.m_attrBuff;
      int cleanStart = 0;
      int cleanLength = 0;
      char ch = 0;

      for(int i = 0; i < end; ++i) {
         ch = chars[i];
         if (ch >= ' ' && ch <= '~') {
            if (ch == '"') {
               if (cleanLength > 0) {
                  writer.write(chars, cleanStart, cleanLength);
                  cleanLength = 0;
               }

               if (doURLEscaping) {
                  writer.write("%22");
               } else {
                  writer.write("&quot;");
               }

               cleanStart = i + 1;
            } else {
               ++cleanLength;
            }
         } else {
            if (cleanLength > 0) {
               writer.write(chars, cleanStart, cleanLength);
               cleanLength = 0;
            }

            if (doURLEscaping) {
               if (ch <= 127) {
                  writer.write(37);
                  writer.write(makeHHString(ch));
               } else {
                  int highSurrogate;
                  int wwww;
                  if (ch <= 2047) {
                     highSurrogate = ch >> 6 | 192;
                     wwww = ch & 63 | 128;
                     writer.write(37);
                     writer.write(makeHHString(highSurrogate));
                     writer.write(37);
                     writer.write(makeHHString(wwww));
                  } else {
                     int uuuuu;
                     if (Encodings.isHighUTF16Surrogate(ch)) {
                        highSurrogate = ch & 1023;
                        wwww = (highSurrogate & 960) >> 6;
                        uuuuu = wwww + 1;
                        int zzzz = (highSurrogate & 60) >> 2;
                        int yyyyyy = (highSurrogate & 3) << 4 & 48;
                        ++i;
                        ch = chars[i];
                        int lowSurrogate = ch & 1023;
                        yyyyyy |= (lowSurrogate & 960) >> 6;
                        int xxxxxx = lowSurrogate & 63;
                        int byte1 = 240 | uuuuu >> 2;
                        int byte2 = 128 | (uuuuu & 3) << 4 & 48 | zzzz;
                        int byte3 = 128 | yyyyyy;
                        int byte4 = 128 | xxxxxx;
                        writer.write(37);
                        writer.write(makeHHString(byte1));
                        writer.write(37);
                        writer.write(makeHHString(byte2));
                        writer.write(37);
                        writer.write(makeHHString(byte3));
                        writer.write(37);
                        writer.write(makeHHString(byte4));
                     } else {
                        highSurrogate = ch >> 12 | 224;
                        wwww = (ch & 4032) >> 6 | 128;
                        uuuuu = ch & 63 | 128;
                        writer.write(37);
                        writer.write(makeHHString(highSurrogate));
                        writer.write(37);
                        writer.write(makeHHString(wwww));
                        writer.write(37);
                        writer.write(makeHHString(uuuuu));
                     }
                  }
               }
            } else if (this.escapingNotNeeded(ch)) {
               writer.write(ch);
            } else {
               writer.write("&#");
               writer.write(Integer.toString(ch));
               writer.write(59);
            }

            cleanStart = i + 1;
         }
      }

      if (cleanLength > 1) {
         if (cleanStart == 0) {
            writer.write(string);
         } else {
            writer.write(chars, cleanStart, cleanLength);
         }
      } else if (cleanLength == 1) {
         writer.write(ch);
      }

   }

   public void writeAttrString(Writer writer, String string, String encoding) throws IOException {
      int end = string.length();
      if (end > super.m_attrBuff.length) {
         super.m_attrBuff = new char[end * 2 + 1];
      }

      string.getChars(0, end, super.m_attrBuff, 0);
      char[] chars = super.m_attrBuff;
      int cleanStart = 0;
      int cleanLength = 0;
      char ch = 0;

      for(int i = 0; i < end; ++i) {
         ch = chars[i];
         if (this.escapingNotNeeded(ch) && !super.m_charInfo.isSpecialAttrChar(ch)) {
            ++cleanLength;
         } else if ('<' != ch && '>' != ch) {
            if ('&' == ch && i + 1 < end && '{' == chars[i + 1]) {
               ++cleanLength;
            } else {
               if (cleanLength > 0) {
                  writer.write(chars, cleanStart, cleanLength);
                  cleanLength = 0;
               }

               int pos = this.accumDefaultEntity(writer, ch, i, chars, end, false, true);
               if (i != pos) {
                  i = pos - 1;
               } else {
                  if (Encodings.isHighUTF16Surrogate(ch)) {
                     this.writeUTF16Surrogate(ch, chars, i, end);
                     ++i;
                  }

                  String outputStringForChar = super.m_charInfo.getOutputStringForChar(ch);
                  if (null != outputStringForChar) {
                     writer.write(outputStringForChar);
                  } else if (this.escapingNotNeeded(ch)) {
                     writer.write(ch);
                  } else {
                     writer.write("&#");
                     writer.write(Integer.toString(ch));
                     writer.write(59);
                  }
               }

               cleanStart = i + 1;
            }
         } else {
            ++cleanLength;
         }
      }

      if (cleanLength > 1) {
         if (cleanStart == 0) {
            writer.write(string);
         } else {
            writer.write(chars, cleanStart, cleanLength);
         }
      } else if (cleanLength == 1) {
         writer.write(ch);
      }

   }

   public final void characters(char[] chars, int start, int length) throws SAXException {
      if (super.m_elemContext.m_isRaw) {
         try {
            if (super.m_elemContext.m_startTagOpen) {
               this.closeStartTag();
               super.m_elemContext.m_startTagOpen = false;
            }

            super.m_ispreserve = true;
            this.writeNormalizedChars(chars, start, length, false, super.m_lineSepUse);
            if (super.m_tracer != null) {
               super.fireCharEvent(chars, start, length);
            }

         } catch (IOException var5) {
            throw new SAXException(Utils.messages.createMessage("ER_OIERROR", (Object[])null), var5);
         }
      } else {
         super.characters(chars, start, length);
      }
   }

   public final void cdata(char[] ch, int start, int length) throws SAXException {
      if (null == super.m_elemContext.m_elementName || !super.m_elemContext.m_elementName.equalsIgnoreCase("SCRIPT") && !super.m_elemContext.m_elementName.equalsIgnoreCase("STYLE")) {
         super.cdata(ch, start, length);
      } else {
         try {
            if (super.m_elemContext.m_startTagOpen) {
               this.closeStartTag();
               super.m_elemContext.m_startTagOpen = false;
            }

            super.m_ispreserve = true;
            if (this.shouldIndent()) {
               this.indent();
            }

            this.writeNormalizedChars(ch, start, length, true, super.m_lineSepUse);
         } catch (IOException var5) {
            throw new SAXException(Utils.messages.createMessage("ER_OIERROR", (Object[])null), var5);
         }
      }

   }

   public void processingInstruction(String target, String data) throws SAXException {
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

            writer.write(data);
            writer.write(62);
            if (super.m_elemContext.m_currentElemDepth <= 0) {
               this.outputLineSep();
            }

            super.m_startNewLine = true;
         } catch (IOException var4) {
            throw new SAXException(var4);
         }
      }

      if (super.m_tracer != null) {
         super.fireEscapingEvent(target, data);
      }

   }

   public final void entityReference(String name) throws SAXException {
      try {
         Writer writer = super.m_writer;
         writer.write(38);
         writer.write(name);
         writer.write(59);
      } catch (IOException var3) {
         throw new SAXException(var3);
      }
   }

   public final void endElement(String elemName) throws SAXException {
      this.endElement((String)null, (String)null, elemName);
   }

   public void processAttributes(Writer writer, int nAttrs) throws IOException, SAXException {
      for(int i = 0; i < nAttrs; ++i) {
         this.processAttribute(writer, super.m_attributes.getQName(i), super.m_attributes.getValue(i), super.m_elemContext.m_elementDesc);
      }

   }

   protected void closeStartTag() throws SAXException {
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
         if (super.m_cdataSectionElements != null) {
            super.m_elemContext.m_isCdataSection = this.isCdataSection();
         }

         if (super.m_doIndent) {
            super.m_isprevtext = false;
            super.m_preserves.push(super.m_ispreserve);
         }

      } catch (IOException var2) {
         throw new SAXException(var2);
      }
   }

   protected synchronized void init(OutputStream output, Properties format) throws UnsupportedEncodingException {
      if (null == format) {
         format = OutputPropertiesFactory.getDefaultMethodProperties("html");
      }

      super.init(output, format, false);
   }

   public void setOutputStream(OutputStream output) {
      try {
         Properties format;
         if (null == super.m_format) {
            format = OutputPropertiesFactory.getDefaultMethodProperties("html");
         } else {
            format = super.m_format;
         }

         this.init(output, format, true);
      } catch (UnsupportedEncodingException var3) {
      }

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

   public void startDTD(String name, String publicId, String systemId) throws SAXException {
      this.m_inDTD = true;
      super.startDTD(name, publicId, systemId);
   }

   public void endDTD() throws SAXException {
      this.m_inDTD = false;
   }

   public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
   }

   public void elementDecl(String name, String model) throws SAXException {
   }

   public void internalEntityDecl(String name, String value) throws SAXException {
   }

   public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
   }

   public void addUniqueAttribute(String name, String value, int flags) throws SAXException {
      try {
         Writer writer = super.m_writer;
         if ((flags & 1) > 0 && m_htmlcharInfo.onlyQuotAmpLtGt) {
            writer.write(32);
            writer.write(name);
            writer.write("=\"");
            writer.write(value);
            writer.write(34);
         } else if ((flags & 2) > 0 && (value.length() == 0 || value.equalsIgnoreCase(name))) {
            writer.write(32);
            writer.write(name);
         } else {
            writer.write(32);
            writer.write(name);
            writer.write("=\"");
            if ((flags & 4) > 0) {
               this.writeAttrURI(writer, value, this.m_specialEscapeURLs);
            } else {
               this.writeAttrString(writer, value, this.getEncoding());
            }

            writer.write(34);
         }

      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      if (!this.m_inDTD) {
         super.comment(ch, start, length);
      }
   }

   public boolean reset() {
      boolean ret = super.reset();
      if (!ret) {
         return false;
      } else {
         this.initToHTMLStream();
         return true;
      }
   }

   private void initToHTMLStream() {
      this.m_inBlockElem = false;
      this.m_inDTD = false;
      this.m_omitMetaTag = false;
      this.m_specialEscapeURLs = true;
   }

   static {
      initTagReference(m_elementFlags);
      m_dummy = new ElemDesc(8);
   }

   static class Trie {
      public static final int ALPHA_SIZE = 128;
      final Node m_Root;
      private char[] m_charBuffer = new char[0];
      private final boolean m_lowerCaseOnly;

      public Trie() {
         this.m_Root = new Node();
         this.m_lowerCaseOnly = false;
      }

      public Trie(boolean lowerCaseOnly) {
         this.m_Root = new Node();
         this.m_lowerCaseOnly = lowerCaseOnly;
      }

      public Object put(String key, Object value) {
         int len = key.length();
         if (len > this.m_charBuffer.length) {
            this.m_charBuffer = new char[len];
         }

         Node node = this.m_Root;

         label30:
         for(int i = 0; i < len; ++i) {
            Node nextNode = node.m_nextChar[Character.toLowerCase(key.charAt(i))];
            if (nextNode == null) {
               while(true) {
                  if (i >= len) {
                     break label30;
                  }

                  Node newNode = new Node();
                  if (this.m_lowerCaseOnly) {
                     node.m_nextChar[Character.toLowerCase(key.charAt(i))] = newNode;
                  } else {
                     node.m_nextChar[Character.toUpperCase(key.charAt(i))] = newNode;
                     node.m_nextChar[Character.toLowerCase(key.charAt(i))] = newNode;
                  }

                  node = newNode;
                  ++i;
               }
            }

            node = nextNode;
         }

         Object ret = node.m_Value;
         node.m_Value = value;
         return ret;
      }

      public Object get(String key) {
         int len = key.length();
         if (this.m_charBuffer.length < len) {
            return null;
         } else {
            Node node = this.m_Root;
            int ch;
            switch (len) {
               case 0:
                  return null;
               case 1:
                  ch = key.charAt(0);
                  if (ch < 128) {
                     node = node.m_nextChar[ch];
                     if (node != null) {
                        return node.m_Value;
                     }
                  }

                  return null;
               default:
                  for(ch = 0; ch < len; ++ch) {
                     char ch = key.charAt(ch);
                     if (128 <= ch) {
                        return null;
                     }

                     node = node.m_nextChar[ch];
                     if (node == null) {
                        return null;
                     }
                  }

                  return node.m_Value;
            }
         }
      }

      public Trie(Trie existingTrie) {
         this.m_Root = existingTrie.m_Root;
         this.m_lowerCaseOnly = existingTrie.m_lowerCaseOnly;
         int max = existingTrie.getLongestKeyLength();
         this.m_charBuffer = new char[max];
      }

      public Object get2(String key) {
         int len = key.length();
         if (this.m_charBuffer.length < len) {
            return null;
         } else {
            Node node = this.m_Root;
            int i;
            switch (len) {
               case 0:
                  return null;
               case 1:
                  i = key.charAt(0);
                  if (i < 128) {
                     node = node.m_nextChar[i];
                     if (node != null) {
                        return node.m_Value;
                     }
                  }

                  return null;
               default:
                  key.getChars(0, len, this.m_charBuffer, 0);

                  for(i = 0; i < len; ++i) {
                     char ch = this.m_charBuffer[i];
                     if (128 <= ch) {
                        return null;
                     }

                     node = node.m_nextChar[ch];
                     if (node == null) {
                        return null;
                     }
                  }

                  return node.m_Value;
            }
         }
      }

      public int getLongestKeyLength() {
         return this.m_charBuffer.length;
      }

      private class Node {
         final Node[] m_nextChar = new Node[128];
         Object m_Value = null;

         Node() {
         }
      }
   }
}
