package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMLocatorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.util.XMLChar;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSSerializerFilter;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public abstract class BaseMarkupSerializer implements ContentHandler, DocumentHandler, LexicalHandler, DTDHandler, DeclHandler, DOMSerializer, Serializer {
   protected short features = -1;
   protected DOMErrorHandler fDOMErrorHandler;
   protected final DOMErrorImpl fDOMError = new DOMErrorImpl();
   protected LSSerializerFilter fDOMFilter;
   protected EncodingInfo _encodingInfo;
   private ElementState[] _elementStates = new ElementState[10];
   private int _elementStateCount;
   private Vector _preRoot;
   protected boolean _started;
   private boolean _prepared;
   protected Hashtable _prefixes;
   protected String _docTypePublicId;
   protected String _docTypeSystemId;
   protected OutputFormat _format;
   protected Printer _printer;
   protected boolean _indenting;
   protected final StringBuffer fStrBuffer = new StringBuffer(40);
   private Writer _writer;
   private OutputStream _output;
   protected Node fCurrentNode = null;
   // $FF: synthetic field
   static Class class$java$lang$String;

   protected BaseMarkupSerializer(OutputFormat var1) {
      for(int var2 = 0; var2 < this._elementStates.length; ++var2) {
         this._elementStates[var2] = new ElementState();
      }

      this._format = var1;
   }

   public DocumentHandler asDocumentHandler() throws IOException {
      this.prepare();
      return this;
   }

   public ContentHandler asContentHandler() throws IOException {
      this.prepare();
      return this;
   }

   public DOMSerializer asDOMSerializer() throws IOException {
      this.prepare();
      return this;
   }

   public void setOutputByteStream(OutputStream var1) {
      if (var1 == null) {
         String var2 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ArgumentIsNull", new Object[]{"output"});
         throw new NullPointerException(var2);
      } else {
         this._output = var1;
         this._writer = null;
         this.reset();
      }
   }

   public void setOutputCharStream(Writer var1) {
      if (var1 == null) {
         String var2 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ArgumentIsNull", new Object[]{"writer"});
         throw new NullPointerException(var2);
      } else {
         this._writer = var1;
         this._output = null;
         this.reset();
      }
   }

   public void setOutputFormat(OutputFormat var1) {
      if (var1 == null) {
         String var2 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ArgumentIsNull", new Object[]{"format"});
         throw new NullPointerException(var2);
      } else {
         this._format = var1;
         this.reset();
      }
   }

   public boolean reset() {
      if (this._elementStateCount > 1) {
         String var1 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ResetInMiddle", (Object[])null);
         throw new IllegalStateException(var1);
      } else {
         this._prepared = false;
         this.fCurrentNode = null;
         this.fStrBuffer.setLength(0);
         return true;
      }
   }

   protected void prepare() throws IOException {
      if (!this._prepared) {
         if (this._writer == null && this._output == null) {
            String var2 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", (Object[])null);
            throw new IOException(var2);
         } else {
            this._encodingInfo = this._format.getEncodingInfo();
            if (this._output != null) {
               this._writer = this._encodingInfo.getWriter(this._output);
            }

            if (this._format.getIndenting()) {
               this._indenting = true;
               this._printer = new IndentPrinter(this._writer, this._format);
            } else {
               this._indenting = false;
               this._printer = new Printer(this._writer, this._format);
            }

            this._elementStateCount = 0;
            ElementState var1 = this._elementStates[0];
            var1.namespaceURI = null;
            var1.localName = null;
            var1.rawName = null;
            var1.preserveSpace = this._format.getPreserveSpace();
            var1.empty = true;
            var1.afterElement = false;
            var1.afterComment = false;
            var1.doCData = var1.inCData = false;
            var1.prefixes = null;
            this._docTypePublicId = this._format.getDoctypePublic();
            this._docTypeSystemId = this._format.getDoctypeSystem();
            this._started = false;
            this._prepared = true;
         }
      }
   }

   public void serialize(Element var1) throws IOException {
      this.reset();
      this.prepare();
      this.serializeNode(var1);
      this._printer.flush();
      if (this._printer.getException() != null) {
         throw this._printer.getException();
      }
   }

   public void serialize(DocumentFragment var1) throws IOException {
      this.reset();
      this.prepare();
      this.serializeNode(var1);
      this._printer.flush();
      if (this._printer.getException() != null) {
         throw this._printer.getException();
      }
   }

   public void serialize(Document var1) throws IOException {
      this.reset();
      this.prepare();
      this.serializeNode(var1);
      this.serializePreRoot();
      this._printer.flush();
      if (this._printer.getException() != null) {
         throw this._printer.getException();
      }
   }

   public void startDocument() throws SAXException {
      try {
         this.prepare();
      } catch (IOException var2) {
         throw new SAXException(var2.toString());
      }
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      try {
         ElementState var4 = this.content();
         int var5;
         if (!var4.inCData && !var4.doCData) {
            if (var4.preserveSpace) {
               var5 = this._printer.getNextIndent();
               this._printer.setNextIndent(0);
               this.printText(var1, var2, var3, true, var4.unescaped);
               this._printer.setNextIndent(var5);
            } else {
               this.printText(var1, var2, var3, false, var4.unescaped);
            }
         } else {
            if (!var4.inCData) {
               this._printer.printText("<![CDATA[");
               var4.inCData = true;
            }

            var5 = this._printer.getNextIndent();
            this._printer.setNextIndent(0);
            int var7 = var2 + var3;

            for(int var8 = var2; var8 < var7; ++var8) {
               char var6 = var1[var8];
               if (var6 == ']' && var8 + 2 < var7 && var1[var8 + 1] == ']' && var1[var8 + 2] == '>') {
                  this._printer.printText("]]]]><![CDATA[>");
                  var8 += 2;
               } else if (!XMLChar.isValid(var6)) {
                  ++var8;
                  if (var8 < var7) {
                     this.surrogates(var6, var1[var8]);
                  } else {
                     this.fatalError("The character '" + var6 + "' is an invalid XML character");
                  }
               } else if ((var6 < ' ' || !this._encodingInfo.isPrintable(var6) || var6 == 247) && var6 != '\n' && var6 != '\r' && var6 != '\t') {
                  this._printer.printText("]]>&#x");
                  this._printer.printText(Integer.toHexString(var6));
                  this._printer.printText(";<![CDATA[");
               } else {
                  this._printer.printText(var6);
               }
            }

            this._printer.setNextIndent(var5);
         }

      } catch (IOException var9) {
         throw new SAXException(var9);
      }
   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
      try {
         this.content();
         if (this._indenting) {
            this._printer.setThisIndent(0);

            for(int var4 = var2; var3-- > 0; ++var4) {
               this._printer.printText(var1[var4]);
            }
         }

      } catch (IOException var6) {
         throw new SAXException(var6);
      }
   }

   public final void processingInstruction(String var1, String var2) throws SAXException {
      try {
         this.processingInstructionIO(var1, var2);
      } catch (IOException var4) {
         throw new SAXException(var4);
      }
   }

   public void processingInstructionIO(String var1, String var2) throws IOException {
      ElementState var4 = this.content();
      int var3 = var1.indexOf("?>");
      if (var3 >= 0) {
         this.fStrBuffer.append("<?").append(var1.substring(0, var3));
      } else {
         this.fStrBuffer.append("<?").append(var1);
      }

      if (var2 != null) {
         this.fStrBuffer.append(' ');
         var3 = var2.indexOf("?>");
         if (var3 >= 0) {
            this.fStrBuffer.append(var2.substring(0, var3));
         } else {
            this.fStrBuffer.append(var2);
         }
      }

      this.fStrBuffer.append("?>");
      if (this.isDocumentState()) {
         if (this._preRoot == null) {
            this._preRoot = new Vector();
         }

         this._preRoot.addElement(this.fStrBuffer.toString());
      } else {
         this._printer.indent();
         this.printText(this.fStrBuffer.toString(), true, true);
         this._printer.unindent();
         if (this._indenting) {
            var4.afterElement = true;
         }
      }

      this.fStrBuffer.setLength(0);
   }

   public void comment(char[] var1, int var2, int var3) throws SAXException {
      try {
         this.comment(new String(var1, var2, var3));
      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   public void comment(String var1) throws IOException {
      if (!this._format.getOmitComments()) {
         ElementState var3 = this.content();
         int var2 = var1.indexOf("-->");
         if (var2 >= 0) {
            this.fStrBuffer.append("<!--").append(var1.substring(0, var2)).append("-->");
         } else {
            this.fStrBuffer.append("<!--").append(var1).append("-->");
         }

         if (this.isDocumentState()) {
            if (this._preRoot == null) {
               this._preRoot = new Vector();
            }

            this._preRoot.addElement(this.fStrBuffer.toString());
         } else {
            if (this._indenting && !var3.preserveSpace) {
               this._printer.breakLine();
            }

            this._printer.indent();
            this.printText(this.fStrBuffer.toString(), true, true);
            this._printer.unindent();
            if (this._indenting) {
               var3.afterElement = true;
            }
         }

         this.fStrBuffer.setLength(0);
         var3.afterComment = true;
         var3.afterElement = false;
      }
   }

   public void startCDATA() {
      ElementState var1 = this.getElementState();
      var1.doCData = true;
   }

   public void endCDATA() {
      ElementState var1 = this.getElementState();
      var1.doCData = false;
   }

   public void startNonEscaping() {
      ElementState var1 = this.getElementState();
      var1.unescaped = true;
   }

   public void endNonEscaping() {
      ElementState var1 = this.getElementState();
      var1.unescaped = false;
   }

   public void startPreserving() {
      ElementState var1 = this.getElementState();
      var1.preserveSpace = true;
   }

   public void endPreserving() {
      ElementState var1 = this.getElementState();
      var1.preserveSpace = false;
   }

   public void endDocument() throws SAXException {
      try {
         this.serializePreRoot();
         this._printer.flush();
      } catch (IOException var2) {
         throw new SAXException(var2);
      }
   }

   public void startEntity(String var1) {
   }

   public void endEntity(String var1) {
   }

   public void setDocumentLocator(Locator var1) {
   }

   public void skippedEntity(String var1) throws SAXException {
      try {
         this.endCDATA();
         this.content();
         this._printer.printText('&');
         this._printer.printText(var1);
         this._printer.printText(';');
      } catch (IOException var3) {
         throw new SAXException(var3);
      }
   }

   public void startPrefixMapping(String var1, String var2) throws SAXException {
      if (this._prefixes == null) {
         this._prefixes = new Hashtable();
      }

      this._prefixes.put(var2, var1 == null ? "" : var1);
   }

   public void endPrefixMapping(String var1) throws SAXException {
   }

   public final void startDTD(String var1, String var2, String var3) throws SAXException {
      try {
         this._printer.enterDTD();
         this._docTypePublicId = var2;
         this._docTypeSystemId = var3;
      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   public void endDTD() {
   }

   public void elementDecl(String var1, String var2) throws SAXException {
      try {
         this._printer.enterDTD();
         this._printer.printText("<!ELEMENT ");
         this._printer.printText(var1);
         this._printer.printText(' ');
         this._printer.printText(var2);
         this._printer.printText('>');
         if (this._indenting) {
            this._printer.breakLine();
         }

      } catch (IOException var4) {
         throw new SAXException(var4);
      }
   }

   public void attributeDecl(String var1, String var2, String var3, String var4, String var5) throws SAXException {
      try {
         this._printer.enterDTD();
         this._printer.printText("<!ATTLIST ");
         this._printer.printText(var1);
         this._printer.printText(' ');
         this._printer.printText(var2);
         this._printer.printText(' ');
         this._printer.printText(var3);
         if (var4 != null) {
            this._printer.printText(' ');
            this._printer.printText(var4);
         }

         if (var5 != null) {
            this._printer.printText(" \"");
            this.printEscaped(var5);
            this._printer.printText('"');
         }

         this._printer.printText('>');
         if (this._indenting) {
            this._printer.breakLine();
         }

      } catch (IOException var7) {
         throw new SAXException(var7);
      }
   }

   public void internalEntityDecl(String var1, String var2) throws SAXException {
      try {
         this._printer.enterDTD();
         this._printer.printText("<!ENTITY ");
         this._printer.printText(var1);
         this._printer.printText(" \"");
         this.printEscaped(var2);
         this._printer.printText("\">");
         if (this._indenting) {
            this._printer.breakLine();
         }

      } catch (IOException var4) {
         throw new SAXException(var4);
      }
   }

   public void externalEntityDecl(String var1, String var2, String var3) throws SAXException {
      try {
         this._printer.enterDTD();
         this.unparsedEntityDecl(var1, var2, var3, (String)null);
      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   public void unparsedEntityDecl(String var1, String var2, String var3, String var4) throws SAXException {
      try {
         this._printer.enterDTD();
         if (var2 == null) {
            this._printer.printText("<!ENTITY ");
            this._printer.printText(var1);
            this._printer.printText(" SYSTEM ");
            this.printDoctypeURL(var3);
         } else {
            this._printer.printText("<!ENTITY ");
            this._printer.printText(var1);
            this._printer.printText(" PUBLIC ");
            this.printDoctypeURL(var2);
            this._printer.printText(' ');
            this.printDoctypeURL(var3);
         }

         if (var4 != null) {
            this._printer.printText(" NDATA ");
            this._printer.printText(var4);
         }

         this._printer.printText('>');
         if (this._indenting) {
            this._printer.breakLine();
         }

      } catch (IOException var6) {
         throw new SAXException(var6);
      }
   }

   public void notationDecl(String var1, String var2, String var3) throws SAXException {
      try {
         this._printer.enterDTD();
         if (var2 != null) {
            this._printer.printText("<!NOTATION ");
            this._printer.printText(var1);
            this._printer.printText(" PUBLIC ");
            this.printDoctypeURL(var2);
            if (var3 != null) {
               this._printer.printText(' ');
               this.printDoctypeURL(var3);
            }
         } else {
            this._printer.printText("<!NOTATION ");
            this._printer.printText(var1);
            this._printer.printText(" SYSTEM ");
            this.printDoctypeURL(var3);
         }

         this._printer.printText('>');
         if (this._indenting) {
            this._printer.breakLine();
         }

      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   protected void serializeNode(Node var1) throws IOException {
      this.fCurrentNode = var1;
      Node var16;
      String var17;
      short var18;
      short var19;
      switch (var1.getNodeType()) {
         case 1:
            if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 1) != 0) {
               var18 = this.fDOMFilter.acceptNode(var1);
               switch (var18) {
                  case 2:
                     return;
                  case 3:
                     for(Node var20 = var1.getFirstChild(); var20 != null; var20 = var20.getNextSibling()) {
                        this.serializeNode(var20);
                     }

                     return;
               }
            }

            this.serializeElement((Element)var1);
         case 2:
         case 6:
         case 10:
         default:
            break;
         case 3:
            var17 = var1.getNodeValue();
            if (var17 != null) {
               if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 4) != 0) {
                  var19 = this.fDOMFilter.acceptNode(var1);
                  switch (var19) {
                     case 2:
                     case 3:
                        break;
                     default:
                        this.characters(var17);
                  }
               } else if (!this._indenting || this.getElementState().preserveSpace || var17.replace('\n', ' ').trim().length() != 0) {
                  this.characters(var17);
               }
            }
            break;
         case 4:
            var17 = var1.getNodeValue();
            if ((this.features & 8) != 0) {
               if (var17 != null) {
                  if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 8) != 0) {
                     var19 = this.fDOMFilter.acceptNode(var1);
                     switch (var19) {
                        case 2:
                        case 3:
                           return;
                     }
                  }

                  this.startCDATA();
                  this.characters(var17);
                  this.endCDATA();
               }
            } else {
               this.characters(var17);
            }
            break;
         case 5:
            this.endCDATA();
            this.content();
            if ((this.features & 4) == 0 && var1.getFirstChild() != null) {
               for(var16 = var1.getFirstChild(); var16 != null; var16 = var16.getNextSibling()) {
                  this.serializeNode(var16);
               }

               return;
            } else {
               if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 16) != 0) {
                  var19 = this.fDOMFilter.acceptNode(var1);
                  switch (var19) {
                     case 2:
                        return;
                     case 3:
                        for(var16 = var1.getFirstChild(); var16 != null; var16 = var16.getNextSibling()) {
                           this.serializeNode(var16);
                        }

                        return;
                  }
               }

               this.checkUnboundNamespacePrefixedNode(var1);
               this._printer.printText("&");
               this._printer.printText(var1.getNodeName());
               this._printer.printText(";");
               break;
            }
         case 7:
            if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 64) != 0) {
               var18 = this.fDOMFilter.acceptNode(var1);
               switch (var18) {
                  case 2:
                  case 3:
                     return;
               }
            }

            this.processingInstructionIO(var1.getNodeName(), var1.getNodeValue());
            break;
         case 8:
            if (!this._format.getOmitComments()) {
               var17 = var1.getNodeValue();
               if (var17 != null) {
                  if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 128) != 0) {
                     var19 = this.fDOMFilter.acceptNode(var1);
                     switch (var19) {
                        case 2:
                        case 3:
                           return;
                     }
                  }

                  this.comment(var17);
               }
            }
            break;
         case 9:
            DocumentType var2 = ((Document)var1).getDoctype();
            if (var2 != null) {
               DOMImplementation var3 = ((Document)var1).getImplementation();

               try {
                  this._printer.enterDTD();
                  this._docTypePublicId = var2.getPublicId();
                  this._docTypeSystemId = var2.getSystemId();
                  String var8 = var2.getInternalSubset();
                  if (var8 != null && var8.length() > 0) {
                     this._printer.printText(var8);
                  }

                  this.endDTD();
               } catch (NoSuchMethodError var15) {
                  Class var9 = var2.getClass();
                  String var10 = null;
                  String var11 = null;

                  java.lang.reflect.Method var12;
                  try {
                     var12 = var9.getMethod("getPublicId", (Class[])null);
                     if (var12.getReturnType().equals(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String)) {
                        var10 = (String)var12.invoke(var2, (Object[])null);
                     }
                  } catch (Exception var14) {
                  }

                  try {
                     var12 = var9.getMethod("getSystemId", (Class[])null);
                     if (var12.getReturnType().equals(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String)) {
                        var11 = (String)var12.invoke(var2, (Object[])null);
                     }
                  } catch (Exception var13) {
                  }

                  this._printer.enterDTD();
                  this._docTypePublicId = var10;
                  this._docTypeSystemId = var11;
                  this.endDTD();
               }
            }
         case 11:
            for(var16 = var1.getFirstChild(); var16 != null; var16 = var16.getNextSibling()) {
               this.serializeNode(var16);
            }
      }

   }

   protected ElementState content() throws IOException {
      ElementState var1 = this.getElementState();
      if (!this.isDocumentState()) {
         if (var1.inCData && !var1.doCData) {
            this._printer.printText("]]>");
            var1.inCData = false;
         }

         if (var1.empty) {
            this._printer.printText('>');
            var1.empty = false;
         }

         var1.afterElement = false;
         var1.afterComment = false;
      }

      return var1;
   }

   protected void characters(String var1) throws IOException {
      ElementState var2 = this.content();
      if (!var2.inCData && !var2.doCData) {
         if (var2.preserveSpace) {
            int var3 = this._printer.getNextIndent();
            this._printer.setNextIndent(0);
            this.printText(var1, true, var2.unescaped);
            this._printer.setNextIndent(var3);
         } else {
            this.printText(var1, false, var2.unescaped);
         }
      } else {
         if (!var2.inCData) {
            this._printer.printText("<![CDATA[");
            var2.inCData = true;
         }

         int var4 = this._printer.getNextIndent();
         this._printer.setNextIndent(0);
         this.printCDATAText(var1);
         this._printer.setNextIndent(var4);
      }

   }

   protected abstract String getEntityRef(int var1);

   protected abstract void serializeElement(Element var1) throws IOException;

   protected void serializePreRoot() throws IOException {
      if (this._preRoot != null) {
         for(int var1 = 0; var1 < this._preRoot.size(); ++var1) {
            this.printText((String)this._preRoot.elementAt(var1), true, true);
            if (this._indenting) {
               this._printer.breakLine();
            }
         }

         this._preRoot.removeAllElements();
      }

   }

   protected void printCDATAText(String var1) throws IOException {
      int var2 = var1.length();

      for(int var4 = 0; var4 < var2; ++var4) {
         char var3 = var1.charAt(var4);
         if (var3 == ']' && var4 + 2 < var2 && var1.charAt(var4 + 1) == ']' && var1.charAt(var4 + 2) == '>') {
            if (this.fDOMErrorHandler != null) {
               String var5;
               if ((this.features & 16) == 0) {
                  var5 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "EndingCDATA", (Object[])null);
                  if ((this.features & 2) != 0) {
                     this.modifyDOMError(var5, (short)3, "wf-invalid-character", this.fCurrentNode);
                     this.fDOMErrorHandler.handleError(this.fDOMError);
                     throw new LSException((short)82, var5);
                  }

                  this.modifyDOMError(var5, (short)2, "cdata-section-not-splitted", this.fCurrentNode);
                  if (!this.fDOMErrorHandler.handleError(this.fDOMError)) {
                     throw new LSException((short)82, var5);
                  }
               } else {
                  var5 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SplittingCDATA", (Object[])null);
                  this.modifyDOMError(var5, (short)1, (String)null, this.fCurrentNode);
                  this.fDOMErrorHandler.handleError(this.fDOMError);
               }
            }

            this._printer.printText("]]]]><![CDATA[>");
            var4 += 2;
         } else if (!XMLChar.isValid(var3)) {
            ++var4;
            if (var4 < var2) {
               this.surrogates(var3, var1.charAt(var4));
            } else {
               this.fatalError("The character '" + var3 + "' is an invalid XML character");
            }
         } else if ((var3 < ' ' || !this._encodingInfo.isPrintable(var3) || var3 == 247) && var3 != '\n' && var3 != '\r' && var3 != '\t') {
            this._printer.printText("]]>&#x");
            this._printer.printText(Integer.toHexString(var3));
            this._printer.printText(";<![CDATA[");
         } else {
            this._printer.printText(var3);
         }
      }

   }

   protected void surrogates(int var1, int var2) throws IOException {
      if (XMLChar.isHighSurrogate(var1)) {
         if (!XMLChar.isLowSurrogate(var2)) {
            this.fatalError("The character '" + (char)var2 + "' is an invalid XML character");
         } else {
            int var3 = XMLChar.supplemental((char)var1, (char)var2);
            if (!XMLChar.isValid(var3)) {
               this.fatalError("The character '" + (char)var3 + "' is an invalid XML character");
            } else if (this.content().inCData) {
               this._printer.printText("]]>&#x");
               this._printer.printText(Integer.toHexString(var3));
               this._printer.printText(";<![CDATA[");
            } else {
               this.printHex(var3);
            }
         }
      } else {
         this.fatalError("The character '" + (char)var1 + "' is an invalid XML character");
      }

   }

   protected void printText(char[] var1, int var2, int var3, boolean var4, boolean var5) throws IOException {
      char var7;
      if (var4) {
         while(true) {
            while(var3-- > 0) {
               var7 = var1[var2];
               ++var2;
               if (var7 != '\n' && var7 != '\r' && !var5) {
                  this.printEscaped(var7);
               } else {
                  this._printer.printText(var7);
               }
            }

            return;
         }
      } else {
         while(true) {
            while(var3-- > 0) {
               var7 = var1[var2];
               ++var2;
               if (var7 != ' ' && var7 != '\f' && var7 != '\t' && var7 != '\n' && var7 != '\r') {
                  if (var5) {
                     this._printer.printText(var7);
                  } else {
                     this.printEscaped(var7);
                  }
               } else {
                  this._printer.printSpace();
               }
            }

            return;
         }
      }
   }

   protected void printText(String var1, boolean var2, boolean var3) throws IOException {
      int var4;
      char var5;
      if (var2) {
         for(var4 = 0; var4 < var1.length(); ++var4) {
            var5 = var1.charAt(var4);
            if (var5 != '\n' && var5 != '\r' && !var3) {
               this.printEscaped(var5);
            } else {
               this._printer.printText(var5);
            }
         }
      } else {
         for(var4 = 0; var4 < var1.length(); ++var4) {
            var5 = var1.charAt(var4);
            if (var5 != ' ' && var5 != '\f' && var5 != '\t' && var5 != '\n' && var5 != '\r') {
               if (var3) {
                  this._printer.printText(var5);
               } else {
                  this.printEscaped(var5);
               }
            } else {
               this._printer.printSpace();
            }
         }
      }

   }

   protected void printDoctypeURL(String var1) throws IOException {
      this._printer.printText('"');

      for(int var2 = 0; var2 < var1.length(); ++var2) {
         if (var1.charAt(var2) != '"' && var1.charAt(var2) >= ' ' && var1.charAt(var2) <= 127) {
            this._printer.printText(var1.charAt(var2));
         } else {
            this._printer.printText('%');
            this._printer.printText(Integer.toHexString(var1.charAt(var2)));
         }
      }

      this._printer.printText('"');
   }

   protected void printEscaped(int var1) throws IOException {
      String var2 = this.getEntityRef(var1);
      if (var2 != null) {
         this._printer.printText('&');
         this._printer.printText(var2);
         this._printer.printText(';');
      } else if ((var1 < 32 || !this._encodingInfo.isPrintable((char)var1) || var1 == 247) && var1 != 10 && var1 != 13 && var1 != 9) {
         this.printHex(var1);
      } else if (var1 < 65536) {
         this._printer.printText((char)var1);
      } else {
         this._printer.printText((char)((var1 - 65536 >> 10) + '\ud800'));
         this._printer.printText((char)((var1 - 65536 & 1023) + '\udc00'));
      }

   }

   final void printHex(int var1) throws IOException {
      this._printer.printText("&#x");
      this._printer.printText(Integer.toHexString(var1));
      this._printer.printText(';');
   }

   protected void printEscaped(String var1) throws IOException {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         int var3 = var1.charAt(var2);
         if ((var3 & 'ﰀ') == 55296 && var2 + 1 < var1.length()) {
            char var4 = var1.charAt(var2 + 1);
            if ((var4 & 'ﰀ') == 56320) {
               var3 = 65536 + (var3 - '\ud800' << 10) + var4 - '\udc00';
               ++var2;
            }
         }

         this.printEscaped(var3);
      }

   }

   protected ElementState getElementState() {
      return this._elementStates[this._elementStateCount];
   }

   protected ElementState enterElementState(String var1, String var2, String var3, boolean var4) {
      if (this._elementStateCount + 1 == this._elementStates.length) {
         ElementState[] var6 = new ElementState[this._elementStates.length + 10];

         for(int var7 = 0; var7 < this._elementStates.length; ++var7) {
            var6[var7] = this._elementStates[var7];
         }

         for(int var8 = this._elementStates.length; var8 < var6.length; ++var8) {
            var6[var8] = new ElementState();
         }

         this._elementStates = var6;
      }

      ++this._elementStateCount;
      ElementState var5 = this._elementStates[this._elementStateCount];
      var5.namespaceURI = var1;
      var5.localName = var2;
      var5.rawName = var3;
      var5.preserveSpace = var4;
      var5.empty = true;
      var5.afterElement = false;
      var5.afterComment = false;
      var5.doCData = var5.inCData = false;
      var5.unescaped = false;
      var5.prefixes = this._prefixes;
      this._prefixes = null;
      return var5;
   }

   protected ElementState leaveElementState() {
      if (this._elementStateCount > 0) {
         this._prefixes = null;
         --this._elementStateCount;
         return this._elementStates[this._elementStateCount];
      } else {
         String var1 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "Internal", (Object[])null);
         throw new IllegalStateException(var1);
      }
   }

   protected boolean isDocumentState() {
      return this._elementStateCount == 0;
   }

   protected String getPrefix(String var1) {
      String var2;
      if (this._prefixes != null) {
         var2 = (String)this._prefixes.get(var1);
         if (var2 != null) {
            return var2;
         }
      }

      if (this._elementStateCount == 0) {
         return null;
      } else {
         for(int var3 = this._elementStateCount; var3 > 0; --var3) {
            if (this._elementStates[var3].prefixes != null) {
               var2 = (String)this._elementStates[var3].prefixes.get(var1);
               if (var2 != null) {
                  return var2;
               }
            }
         }

         return null;
      }
   }

   protected DOMError modifyDOMError(String var1, short var2, String var3, Node var4) {
      this.fDOMError.reset();
      this.fDOMError.fMessage = var1;
      this.fDOMError.fType = var3;
      this.fDOMError.fSeverity = var2;
      this.fDOMError.fLocator = new DOMLocatorImpl(-1, -1, -1, var4, (String)null);
      return this.fDOMError;
   }

   protected void fatalError(String var1) throws IOException {
      if (this.fDOMErrorHandler != null) {
         this.modifyDOMError(var1, (short)3, (String)null, this.fCurrentNode);
         this.fDOMErrorHandler.handleError(this.fDOMError);
      } else {
         throw new IOException(var1);
      }
   }

   protected void checkUnboundNamespacePrefixedNode(Node var1) throws IOException {
   }

   public abstract void endElement(String var1, String var2, String var3) throws SAXException;

   public abstract void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException;

   public abstract void endElement(String var1) throws SAXException;

   public abstract void startElement(String var1, AttributeList var2) throws SAXException;

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
