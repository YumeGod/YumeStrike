package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Enumeration;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XMLSerializer extends BaseMarkupSerializer {
   protected static final boolean DEBUG = false;
   protected NamespaceSupport fNSBinder;
   protected NamespaceSupport fLocalNSBinder;
   protected SymbolTable fSymbolTable;
   protected static final String PREFIX = "NS";
   protected boolean fNamespaces = false;
   protected boolean fNamespacePrefixes = true;
   private boolean fPreserveSpace;

   public XMLSerializer() {
      super(new OutputFormat("xml", (String)null, false));
   }

   public XMLSerializer(OutputFormat var1) {
      super(var1 != null ? var1 : new OutputFormat("xml", (String)null, false));
      super._format.setMethod("xml");
   }

   public XMLSerializer(Writer var1, OutputFormat var2) {
      super(var2 != null ? var2 : new OutputFormat("xml", (String)null, false));
      super._format.setMethod("xml");
      this.setOutputCharStream(var1);
   }

   public XMLSerializer(OutputStream var1, OutputFormat var2) {
      super(var2 != null ? var2 : new OutputFormat("xml", (String)null, false));
      super._format.setMethod("xml");
      this.setOutputByteStream(var1);
   }

   public void setOutputFormat(OutputFormat var1) {
      super.setOutputFormat(var1 != null ? var1 : new OutputFormat("xml", (String)null, false));
   }

   public void setNamespaces(boolean var1) {
      this.fNamespaces = var1;
      if (this.fNSBinder == null) {
         this.fNSBinder = new NamespaceSupport();
         this.fLocalNSBinder = new NamespaceSupport();
         this.fSymbolTable = new SymbolTable();
      }

   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      boolean var10 = false;

      try {
         String var11;
         if (super._printer == null) {
            var11 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", (Object[])null);
            throw new IllegalStateException(var11);
         } else {
            ElementState var7 = this.getElementState();
            if (this.isDocumentState()) {
               if (!super._started) {
                  this.startDocument(var2 != null && var2.length() != 0 ? var2 : var3);
               }
            } else {
               if (var7.empty) {
                  super._printer.printText('>');
               }

               if (var7.inCData) {
                  super._printer.printText("]]>");
                  var7.inCData = false;
               }

               if (super._indenting && !var7.preserveSpace && (var7.empty || var7.afterElement || var7.afterComment)) {
                  super._printer.breakLine();
               }
            }

            boolean var6 = var7.preserveSpace;
            var4 = this.extractNamespaces(var4);
            if (var3 == null || var3.length() == 0) {
               if (var2 == null) {
                  var11 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoName", (Object[])null);
                  throw new SAXException(var11);
               }

               if (var1 != null && !var1.equals("")) {
                  var11 = this.getPrefix(var1);
                  if (var11 != null && var11.length() > 0) {
                     var3 = var11 + ":" + var2;
                  } else {
                     var3 = var2;
                  }
               } else {
                  var3 = var2;
               }

               var10 = true;
            }

            super._printer.printText('<');
            super._printer.printText(var3);
            super._printer.indent();
            String var8;
            String var9;
            if (var4 != null) {
               for(int var5 = 0; var5 < var4.getLength(); ++var5) {
                  super._printer.printSpace();
                  var8 = var4.getQName(var5);
                  if (var8 != null && var8.length() == 0) {
                     var8 = var4.getLocalName(var5);
                     String var12 = var4.getURI(var5);
                     if (var12 != null && var12.length() != 0 && (var1 == null || var1.length() == 0 || !var12.equals(var1))) {
                        var11 = this.getPrefix(var12);
                        if (var11 != null && var11.length() > 0) {
                           var8 = var11 + ":" + var8;
                        }
                     }
                  }

                  var9 = var4.getValue(var5);
                  if (var9 == null) {
                     var9 = "";
                  }

                  super._printer.printText(var8);
                  super._printer.printText("=\"");
                  this.printEscaped(var9);
                  super._printer.printText('"');
                  if (var8.equals("xml:space")) {
                     if (var9.equals("preserve")) {
                        var6 = true;
                     } else {
                        var6 = super._format.getPreserveSpace();
                     }
                  }
               }
            }

            if (super._prefixes != null) {
               Enumeration var14 = super._prefixes.keys();

               while(var14.hasMoreElements()) {
                  super._printer.printSpace();
                  var9 = (String)var14.nextElement();
                  var8 = (String)super._prefixes.get(var9);
                  if (var8.length() == 0) {
                     super._printer.printText("xmlns=\"");
                     this.printEscaped(var9);
                     super._printer.printText('"');
                  } else {
                     super._printer.printText("xmlns:");
                     super._printer.printText(var8);
                     super._printer.printText("=\"");
                     this.printEscaped(var9);
                     super._printer.printText('"');
                  }
               }
            }

            var7 = this.enterElementState(var1, var2, var3, var6);
            var8 = var2 != null && var2.length() != 0 ? var1 + "^" + var2 : var3;
            var7.doCData = super._format.isCDataElement(var8);
            var7.unescaped = super._format.isNonEscapingElement(var8);
         }
      } catch (IOException var13) {
         throw new SAXException(var13);
      }
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      try {
         this.endElementIO(var1, var2, var3);
      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   public void endElementIO(String var1, String var2, String var3) throws IOException {
      super._printer.unindent();
      ElementState var4 = this.getElementState();
      if (var4.empty) {
         super._printer.printText("/>");
      } else {
         if (var4.inCData) {
            super._printer.printText("]]>");
         }

         if (super._indenting && !var4.preserveSpace && (var4.afterElement || var4.afterComment)) {
            super._printer.breakLine();
         }

         super._printer.printText("</");
         super._printer.printText(var4.rawName);
         super._printer.printText('>');
      }

      var4 = this.leaveElementState();
      var4.afterElement = true;
      var4.afterComment = false;
      var4.empty = false;
      if (this.isDocumentState()) {
         super._printer.flush();
      }

   }

   public void startElement(String var1, AttributeList var2) throws SAXException {
      try {
         if (super._printer == null) {
            String var8 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", (Object[])null);
            throw new IllegalStateException(var8);
         } else {
            ElementState var5 = this.getElementState();
            if (this.isDocumentState()) {
               if (!super._started) {
                  this.startDocument(var1);
               }
            } else {
               if (var5.empty) {
                  super._printer.printText('>');
               }

               if (var5.inCData) {
                  super._printer.printText("]]>");
                  var5.inCData = false;
               }

               if (super._indenting && !var5.preserveSpace && (var5.empty || var5.afterElement || var5.afterComment)) {
                  super._printer.breakLine();
               }
            }

            boolean var4 = var5.preserveSpace;
            super._printer.printText('<');
            super._printer.printText(var1);
            super._printer.indent();
            if (var2 != null) {
               for(int var3 = 0; var3 < var2.getLength(); ++var3) {
                  super._printer.printSpace();
                  String var6 = var2.getName(var3);
                  String var7 = var2.getValue(var3);
                  if (var7 != null) {
                     super._printer.printText(var6);
                     super._printer.printText("=\"");
                     this.printEscaped(var7);
                     super._printer.printText('"');
                  }

                  if (var6.equals("xml:space")) {
                     if (var7.equals("preserve")) {
                        var4 = true;
                     } else {
                        var4 = super._format.getPreserveSpace();
                     }
                  }
               }
            }

            var5 = this.enterElementState((String)null, (String)null, var1, var4);
            var5.doCData = super._format.isCDataElement(var1);
            var5.unescaped = super._format.isNonEscapingElement(var1);
         }
      } catch (IOException var9) {
         throw new SAXException(var9);
      }
   }

   public void endElement(String var1) throws SAXException {
      this.endElement((String)null, (String)null, var1);
   }

   protected void startDocument(String var1) throws IOException {
      String var3 = super._printer.leaveDTD();
      if (!super._started) {
         if (!super._format.getOmitXMLDeclaration()) {
            StringBuffer var4 = new StringBuffer("<?xml version=\"");
            if (super._format.getVersion() != null) {
               var4.append(super._format.getVersion());
            } else {
               var4.append("1.0");
            }

            var4.append('"');
            String var5 = super._format.getEncoding();
            if (var5 != null) {
               var4.append(" encoding=\"");
               var4.append(var5);
               var4.append('"');
            }

            if (super._format.getStandalone() && super._docTypeSystemId == null && super._docTypePublicId == null) {
               var4.append(" standalone=\"yes\"");
            }

            var4.append("?>");
            super._printer.printText(var4);
            super._printer.breakLine();
         }

         if (!super._format.getOmitDocumentType()) {
            if (super._docTypeSystemId != null) {
               super._printer.printText("<!DOCTYPE ");
               super._printer.printText(var1);
               if (super._docTypePublicId == null) {
                  super._printer.printText(" SYSTEM ");
                  this.printDoctypeURL(super._docTypeSystemId);
               } else {
                  super._printer.printText(" PUBLIC ");
                  this.printDoctypeURL(super._docTypePublicId);
                  if (super._indenting) {
                     super._printer.breakLine();

                     for(int var2 = 0; var2 < 18 + var1.length(); ++var2) {
                        super._printer.printText(" ");
                     }
                  } else {
                     super._printer.printText(" ");
                  }

                  this.printDoctypeURL(super._docTypeSystemId);
               }

               if (var3 != null && var3.length() > 0) {
                  super._printer.printText(" [");
                  this.printText(var3, true, true);
                  super._printer.printText(']');
               }

               super._printer.printText(">");
               super._printer.breakLine();
            } else if (var3 != null && var3.length() > 0) {
               super._printer.printText("<!DOCTYPE ");
               super._printer.printText(var1);
               super._printer.printText(" [");
               this.printText(var3, true, true);
               super._printer.printText("]>");
               super._printer.breakLine();
            }
         }
      }

      super._started = true;
      this.serializePreRoot();
   }

   protected void serializeElement(Element var1) throws IOException {
      if (this.fNamespaces) {
         this.fLocalNSBinder.reset();
         this.fNSBinder.pushContext();
      }

      String var9 = var1.getTagName();
      ElementState var6 = this.getElementState();
      if (this.isDocumentState()) {
         if (!super._started) {
            this.startDocument(var9);
         }
      } else {
         if (var6.empty) {
            super._printer.printText('>');
         }

         if (var6.inCData) {
            super._printer.printText("]]>");
            var6.inCData = false;
         }

         if (super._indenting && !var6.preserveSpace && (var6.empty || var6.afterElement || var6.afterComment)) {
            super._printer.breakLine();
         }
      }

      this.fPreserveSpace = var6.preserveSpace;
      int var13 = 0;
      NamedNodeMap var3 = null;
      if (var1.hasAttributes()) {
         var3 = var1.getAttributes();
         var13 = var3.getLength();
      }

      Attr var2;
      int var4;
      String var7;
      String var8;
      if (!this.fNamespaces) {
         super._printer.printText('<');
         super._printer.printText(var9);
         super._printer.indent();

         for(var4 = 0; var4 < var13; ++var4) {
            var2 = (Attr)var3.item(var4);
            var7 = var2.getName();
            var8 = var2.getValue();
            if (var8 == null) {
               var8 = "";
            }

            this.printAttribute(var7, var8, var2.getSpecified(), var2);
         }
      } else {
         String var10;
         String var12;
         String var14;
         boolean var15;
         for(var4 = 0; var4 < var13; ++var4) {
            var2 = (Attr)var3.item(var4);
            var12 = var2.getNamespaceURI();
            if (var12 != null && var12.equals(NamespaceContext.XMLNS_URI)) {
               var8 = var2.getNodeValue();
               if (var8 == null) {
                  var8 = XMLSymbols.EMPTY_STRING;
               }

               if (var8.equals(NamespaceContext.XMLNS_URI)) {
                  if (super.fDOMErrorHandler != null) {
                     var14 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CantBindXMLNS", (Object[])null);
                     this.modifyDOMError(var14, (short)2, (String)null, var2);
                     var15 = super.fDOMErrorHandler.handleError(super.fDOMError);
                     if (!var15) {
                        throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", (Object[])null));
                     }
                  }
               } else {
                  var10 = var2.getPrefix();
                  var10 = var10 != null && var10.length() != 0 ? this.fSymbolTable.addSymbol(var10) : XMLSymbols.EMPTY_STRING;
                  var14 = this.fSymbolTable.addSymbol(var2.getLocalName());
                  if (var10 == XMLSymbols.PREFIX_XMLNS) {
                     var8 = this.fSymbolTable.addSymbol(var8);
                     if (var8.length() != 0) {
                        this.fNSBinder.declarePrefix(var14, var8);
                     }
                  } else {
                     var8 = this.fSymbolTable.addSymbol(var8);
                     this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, var8);
                  }
               }
            }
         }

         var12 = var1.getNamespaceURI();
         var10 = var1.getPrefix();
         if (var12 != null && var10 != null && var12.length() == 0 && var10.length() != 0) {
            var10 = null;
            super._printer.printText('<');
            super._printer.printText(var1.getLocalName());
            super._printer.indent();
         } else {
            super._printer.printText('<');
            super._printer.printText(var9);
            super._printer.indent();
         }

         if (var12 == null) {
            if (var1.getLocalName() == null) {
               if (super.fDOMErrorHandler != null) {
                  var14 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalElementName", new Object[]{var1.getNodeName()});
                  this.modifyDOMError(var14, (short)2, (String)null, var1);
                  var15 = super.fDOMErrorHandler.handleError(super.fDOMError);
                  if (!var15) {
                     throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", (Object[])null));
                  }
               }
            } else {
               var12 = this.fNSBinder.getURI(XMLSymbols.EMPTY_STRING);
               if (var12 != null && var12.length() > 0) {
                  if (this.fNamespacePrefixes) {
                     this.printNamespaceAttr(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                  }

                  this.fLocalNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                  this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
               }
            }
         } else {
            var12 = this.fSymbolTable.addSymbol(var12);
            var10 = var10 != null && var10.length() != 0 ? this.fSymbolTable.addSymbol(var10) : XMLSymbols.EMPTY_STRING;
            if (this.fNSBinder.getURI(var10) != var12) {
               if (this.fNamespacePrefixes) {
                  this.printNamespaceAttr(var10, var12);
               }

               this.fLocalNSBinder.declarePrefix(var10, var12);
               this.fNSBinder.declarePrefix(var10, var12);
            }
         }

         for(var4 = 0; var4 < var13; ++var4) {
            var2 = (Attr)var3.item(var4);
            var8 = var2.getValue();
            var7 = var2.getNodeName();
            var12 = var2.getNamespaceURI();
            if (var12 != null && var12.length() == 0) {
               var12 = null;
               var7 = var2.getLocalName();
            }

            if (var8 == null) {
               var8 = XMLSymbols.EMPTY_STRING;
            }

            if (var12 != null) {
               var10 = var2.getPrefix();
               var10 = var10 == null ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(var10);
               var14 = this.fSymbolTable.addSymbol(var2.getLocalName());
               if (var12 != null && var12.equals(NamespaceContext.XMLNS_URI)) {
                  var10 = var2.getPrefix();
                  var10 = var10 != null && var10.length() != 0 ? this.fSymbolTable.addSymbol(var10) : XMLSymbols.EMPTY_STRING;
                  var14 = this.fSymbolTable.addSymbol(var2.getLocalName());
                  String var11;
                  if (var10 == XMLSymbols.PREFIX_XMLNS) {
                     var11 = this.fLocalNSBinder.getURI(var14);
                     var8 = this.fSymbolTable.addSymbol(var8);
                     if (var8.length() != 0 && var11 == null) {
                        if (this.fNamespacePrefixes) {
                           this.printNamespaceAttr(var14, var8);
                        }

                        this.fLocalNSBinder.declarePrefix(var14, var8);
                     }
                  } else {
                     var12 = this.fNSBinder.getURI(XMLSymbols.EMPTY_STRING);
                     var11 = this.fLocalNSBinder.getURI(XMLSymbols.EMPTY_STRING);
                     var8 = this.fSymbolTable.addSymbol(var8);
                     if (var11 == null && this.fNamespacePrefixes) {
                        this.printNamespaceAttr(XMLSymbols.EMPTY_STRING, var8);
                     }
                  }
               } else {
                  var12 = this.fSymbolTable.addSymbol(var12);
                  String var18 = this.fNSBinder.getURI(var10);
                  if (var10 == XMLSymbols.EMPTY_STRING || var18 != var12) {
                     var7 = var2.getNodeName();
                     String var16 = this.fNSBinder.getPrefix(var12);
                     if (var16 != null && var16 != XMLSymbols.EMPTY_STRING) {
                        var7 = var16 + ":" + var14;
                     } else {
                        if (var10 == XMLSymbols.EMPTY_STRING || this.fLocalNSBinder.getURI(var10) != null) {
                           int var17 = 1;

                           for(var10 = this.fSymbolTable.addSymbol("NS" + var17++); this.fLocalNSBinder.getURI(var10) != null; var10 = this.fSymbolTable.addSymbol("NS" + var17++)) {
                           }

                           var7 = var10 + ":" + var14;
                        }

                        if (this.fNamespacePrefixes) {
                           this.printNamespaceAttr(var10, var12);
                        }

                        var8 = this.fSymbolTable.addSymbol(var8);
                        this.fLocalNSBinder.declarePrefix(var10, var8);
                        this.fNSBinder.declarePrefix(var10, var12);
                     }
                  }

                  this.printAttribute(var7, var8 == null ? XMLSymbols.EMPTY_STRING : var8, var2.getSpecified(), var2);
               }
            } else if (var2.getLocalName() == null) {
               if (super.fDOMErrorHandler != null) {
                  var14 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalAttrName", new Object[]{var2.getNodeName()});
                  this.modifyDOMError(var14, (short)2, (String)null, var2);
                  var15 = super.fDOMErrorHandler.handleError(super.fDOMError);
                  if (!var15) {
                     throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SerializationStopped", (Object[])null));
                  }
               }

               this.printAttribute(var7, var8, var2.getSpecified(), var2);
            } else {
               this.printAttribute(var7, var8, var2.getSpecified(), var2);
            }
         }
      }

      if (var1.hasChildNodes()) {
         var6 = this.enterElementState((String)null, (String)null, var9, this.fPreserveSpace);
         var6.doCData = super._format.isCDataElement(var9);
         var6.unescaped = super._format.isNonEscapingElement(var9);

         for(Node var5 = var1.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
            this.serializeNode(var5);
         }

         if (this.fNamespaces) {
            this.fNSBinder.popContext();
         }

         this.endElementIO((String)null, (String)null, var9);
      } else {
         if (this.fNamespaces) {
            this.fNSBinder.popContext();
         }

         super._printer.unindent();
         super._printer.printText("/>");
         var6.afterElement = true;
         var6.afterComment = false;
         var6.empty = false;
         if (this.isDocumentState()) {
            super._printer.flush();
         }
      }

   }

   private void printNamespaceAttr(String var1, String var2) throws IOException {
      super._printer.printSpace();
      if (var1 == XMLSymbols.EMPTY_STRING) {
         super._printer.printText(XMLSymbols.PREFIX_XMLNS);
      } else {
         super._printer.printText("xmlns:" + var1);
      }

      super._printer.printText("=\"");
      this.printEscaped(var2);
      super._printer.printText('"');
   }

   private void printAttribute(String var1, String var2, boolean var3, Attr var4) throws IOException {
      if (var3 || (super.features & 64) == 0) {
         if (super.fDOMFilter != null && (super.fDOMFilter.getWhatToShow() & 2) != 0) {
            short var5 = super.fDOMFilter.acceptNode(var4);
            switch (var5) {
               case 2:
               case 3:
                  return;
            }
         }

         super._printer.printSpace();
         super._printer.printText(var1);
         super._printer.printText("=\"");
         this.printEscaped(var2);
         super._printer.printText('"');
      }

      if (var1.equals("xml:space")) {
         if (var2.equals("preserve")) {
            this.fPreserveSpace = true;
         } else {
            this.fPreserveSpace = super._format.getPreserveSpace();
         }
      }

   }

   protected String getEntityRef(int var1) {
      switch (var1) {
         case 34:
            return "quot";
         case 38:
            return "amp";
         case 39:
            return "apos";
         case 60:
            return "lt";
         case 62:
            return "gt";
         default:
            return null;
      }
   }

   private Attributes extractNamespaces(Attributes var1) throws SAXException {
      if (var1 == null) {
         return null;
      } else {
         int var7 = var1.getLength();
         AttributesImpl var2 = new AttributesImpl(var1);

         for(int var4 = var7 - 1; var4 >= 0; --var4) {
            String var3 = var2.getQName(var4);
            if (var3.startsWith("xmlns")) {
               if (var3.length() == 5) {
                  this.startPrefixMapping("", var1.getValue(var4));
                  var2.removeAttribute(var4);
               } else if (var3.charAt(5) == ':') {
                  this.startPrefixMapping(var3.substring(6), var1.getValue(var4));
                  var2.removeAttribute(var4);
               }
            }
         }

         return var2;
      }
   }

   protected void printEscaped(String var1) throws IOException {
      int var2 = var1.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         char var4 = var1.charAt(var3);
         if (!XMLChar.isValid(var4)) {
            ++var3;
            if (var3 < var2) {
               this.surrogates(var4, var1.charAt(var3));
            } else {
               this.fatalError("The character '" + (char)var4 + "' is an invalid XML character");
            }
         } else if (var4 != '\n' && var4 != '\r' && var4 != '\t') {
            if (var4 == '<') {
               super._printer.printText("&lt;");
            } else if (var4 == '&') {
               super._printer.printText("&amp;");
            } else if (var4 == '"') {
               super._printer.printText("&quot;");
            } else if (var4 >= ' ' && super._encodingInfo.isPrintable((char)var4)) {
               super._printer.printText((char)var4);
            } else {
               this.printHex(var4);
            }
         } else {
            this.printHex(var4);
         }
      }

   }

   protected void printXMLChar(int var1) throws IOException {
      if (var1 == 13) {
         this.printHex(var1);
      } else if (var1 == 60) {
         super._printer.printText("&lt;");
      } else if (var1 == 38) {
         super._printer.printText("&amp;");
      } else if (var1 == 62) {
         super._printer.printText("&gt;");
      } else if (var1 != 10 && var1 != 9 && (var1 < 32 || !super._encodingInfo.isPrintable((char)var1))) {
         this.printHex(var1);
      } else {
         super._printer.printText((char)var1);
      }

   }

   protected void printText(String var1, boolean var2, boolean var3) throws IOException {
      int var6 = var1.length();
      int var4;
      char var5;
      if (var2) {
         for(var4 = 0; var4 < var6; ++var4) {
            var5 = var1.charAt(var4);
            if (!XMLChar.isValid(var5)) {
               ++var4;
               if (var4 < var6) {
                  this.surrogates(var5, var1.charAt(var4));
               } else {
                  this.fatalError("The character '" + var5 + "' is an invalid XML character");
               }
            } else if (var3) {
               super._printer.printText(var5);
            } else {
               this.printXMLChar(var5);
            }
         }
      } else {
         for(var4 = 0; var4 < var6; ++var4) {
            var5 = var1.charAt(var4);
            if (!XMLChar.isValid(var5)) {
               ++var4;
               if (var4 < var6) {
                  this.surrogates(var5, var1.charAt(var4));
               } else {
                  this.fatalError("The character '" + var5 + "' is an invalid XML character");
               }
            } else if (var3) {
               super._printer.printText(var5);
            } else {
               this.printXMLChar(var5);
            }
         }
      }

   }

   protected void printText(char[] var1, int var2, int var3, boolean var4, boolean var5) throws IOException {
      char var7;
      if (var4) {
         while(var3-- > 0) {
            var7 = var1[var2++];
            if (!XMLChar.isValid(var7)) {
               if (var3-- > 0) {
                  this.surrogates(var7, var1[var2++]);
               } else {
                  this.fatalError("The character '" + var7 + "' is an invalid XML character");
               }
            } else if (var5) {
               super._printer.printText(var7);
            } else {
               this.printXMLChar(var7);
            }
         }
      } else {
         while(var3-- > 0) {
            var7 = var1[var2++];
            if (!XMLChar.isValid(var7)) {
               if (var3-- > 0) {
                  this.surrogates(var7, var1[var2++]);
               } else {
                  this.fatalError("The character '" + var7 + "' is an invalid XML character");
               }
            } else if (var5) {
               super._printer.printText(var7);
            } else {
               this.printXMLChar(var7);
            }
         }
      }

   }

   protected void checkUnboundNamespacePrefixedNode(Node var1) throws IOException {
      Node var3;
      if (this.fNamespaces) {
         for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var3) {
            var3 = var2.getNextSibling();
            String var4 = var2.getPrefix();
            var4 = var4 != null && var4.length() != 0 ? this.fSymbolTable.addSymbol(var4) : XMLSymbols.EMPTY_STRING;
            if (this.fNSBinder.getURI(var4) == null && var4 != null) {
               this.fatalError("The replacement text of the entity node '" + var1.getNodeName() + "' contains an element node '" + var2.getNodeName() + "' with an undeclared prefix '" + var4 + "'.");
            }

            if (var2.getNodeType() == 1) {
               NamedNodeMap var5 = var2.getAttributes();

               for(int var6 = 0; var6 < var5.getLength(); ++var6) {
                  String var7 = var5.item(var6).getPrefix();
                  var7 = var7 != null && var7.length() != 0 ? this.fSymbolTable.addSymbol(var7) : XMLSymbols.EMPTY_STRING;
                  if (this.fNSBinder.getURI(var7) == null && var7 != null) {
                     this.fatalError("The replacement text of the entity node '" + var1.getNodeName() + "' contains an element node '" + var2.getNodeName() + "' with an attribute '" + var5.item(var6).getNodeName() + "' an undeclared prefix '" + var7 + "'.");
                  }
               }
            }

            if (var2.hasChildNodes()) {
               this.checkUnboundNamespacePrefixedNode(var2);
            }
         }
      }

   }

   public boolean reset() {
      super.reset();
      if (this.fNSBinder != null) {
         this.fNSBinder.reset();
         this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
      }

      return true;
   }
}
