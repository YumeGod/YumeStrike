package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Locale;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/** @deprecated */
public class HTMLSerializer extends BaseMarkupSerializer {
   private boolean _xhtml;
   public static final String XHTMLNamespace = "http://www.w3.org/1999/xhtml";
   private String fUserXHTMLNamespace;

   protected HTMLSerializer(boolean var1, OutputFormat var2) {
      super(var2);
      this.fUserXHTMLNamespace = null;
      this._xhtml = var1;
   }

   public HTMLSerializer() {
      this(false, new OutputFormat("html", "ISO-8859-1", false));
   }

   public HTMLSerializer(OutputFormat var1) {
      this(false, var1 != null ? var1 : new OutputFormat("html", "ISO-8859-1", false));
   }

   public HTMLSerializer(Writer var1, OutputFormat var2) {
      this(false, var2 != null ? var2 : new OutputFormat("html", "ISO-8859-1", false));
      this.setOutputCharStream(var1);
   }

   public HTMLSerializer(OutputStream var1, OutputFormat var2) {
      this(false, var2 != null ? var2 : new OutputFormat("html", "ISO-8859-1", false));
      this.setOutputByteStream(var1);
   }

   public void setOutputFormat(OutputFormat var1) {
      super.setOutputFormat(var1 != null ? var1 : new OutputFormat("html", "ISO-8859-1", false));
   }

   public void setXHTMLNamespace(String var1) {
      this.fUserXHTMLNamespace = var1;
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      boolean var11 = false;

      try {
         if (super._printer == null) {
            throw new IllegalStateException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", (Object[])null));
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

               if (super._indenting && !var7.preserveSpace && (var7.empty || var7.afterElement)) {
                  super._printer.breakLine();
               }
            }

            boolean var6 = var7.preserveSpace;
            boolean var12 = var1 != null && var1.length() != 0;
            if (var3 == null || var3.length() == 0) {
               var3 = var2;
               if (var12) {
                  String var13 = this.getPrefix(var1);
                  if (var13 != null && var13.length() != 0) {
                     var3 = var13 + ":" + var2;
                  }
               }

               var11 = true;
            }

            String var10;
            if (!var12) {
               var10 = var3;
            } else if (!var1.equals("http://www.w3.org/1999/xhtml") && (this.fUserXHTMLNamespace == null || !this.fUserXHTMLNamespace.equals(var1))) {
               var10 = null;
            } else {
               var10 = var2;
            }

            super._printer.printText('<');
            if (this._xhtml) {
               super._printer.printText(var3.toLowerCase(Locale.ENGLISH));
            } else {
               super._printer.printText(var3);
            }

            super._printer.indent();
            String var8;
            String var9;
            if (var4 != null) {
               for(int var5 = 0; var5 < var4.getLength(); ++var5) {
                  super._printer.printSpace();
                  var8 = var4.getQName(var5).toLowerCase(Locale.ENGLISH);
                  var9 = var4.getValue(var5);
                  if (!this._xhtml && !var12) {
                     if (var9 == null) {
                        var9 = "";
                     }

                     if (!super._format.getPreserveEmptyAttributes() && var9.length() == 0) {
                        super._printer.printText(var8);
                     } else if (HTMLdtd.isURI(var3, var8)) {
                        super._printer.printText(var8);
                        super._printer.printText("=\"");
                        super._printer.printText(this.escapeURI(var9));
                        super._printer.printText('"');
                     } else if (HTMLdtd.isBoolean(var3, var8)) {
                        super._printer.printText(var8);
                     } else {
                        super._printer.printText(var8);
                        super._printer.printText("=\"");
                        this.printEscaped(var9);
                        super._printer.printText('"');
                     }
                  } else if (var9 == null) {
                     super._printer.printText(var8);
                     super._printer.printText("=\"\"");
                  } else {
                     super._printer.printText(var8);
                     super._printer.printText("=\"");
                     this.printEscaped(var9);
                     super._printer.printText('"');
                  }
               }
            }

            if (var10 != null && HTMLdtd.isPreserveSpace(var10)) {
               var6 = true;
            }

            if (var11) {
               Enumeration var15 = super._prefixes.keys();

               while(var15.hasMoreElements()) {
                  super._printer.printSpace();
                  var9 = (String)var15.nextElement();
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
            if (var10 != null && (var10.equalsIgnoreCase("A") || var10.equalsIgnoreCase("TD"))) {
               var7.empty = false;
               super._printer.printText('>');
            }

            if (var10 != null && (var3.equalsIgnoreCase("SCRIPT") || var3.equalsIgnoreCase("STYLE"))) {
               if (this._xhtml) {
                  var7.doCData = true;
               } else {
                  var7.unescaped = true;
               }
            }

         }
      } catch (IOException var14) {
         throw new SAXException(var14);
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
      String var5;
      if (var4.namespaceURI != null && var4.namespaceURI.length() != 0) {
         if (!var4.namespaceURI.equals("http://www.w3.org/1999/xhtml") && (this.fUserXHTMLNamespace == null || !this.fUserXHTMLNamespace.equals(var4.namespaceURI))) {
            var5 = null;
         } else {
            var5 = var4.localName;
         }
      } else {
         var5 = var4.rawName;
      }

      if (this._xhtml) {
         if (var4.empty) {
            super._printer.printText(" />");
         } else {
            if (var4.inCData) {
               super._printer.printText("]]>");
            }

            super._printer.printText("</");
            super._printer.printText(var4.rawName.toLowerCase(Locale.ENGLISH));
            super._printer.printText('>');
         }
      } else {
         if (var4.empty) {
            super._printer.printText('>');
         }

         if (var5 == null || !HTMLdtd.isOnlyOpening(var5)) {
            if (super._indenting && !var4.preserveSpace && var4.afterElement) {
               super._printer.breakLine();
            }

            if (var4.inCData) {
               super._printer.printText("]]>");
            }

            super._printer.printText("</");
            super._printer.printText(var4.rawName);
            super._printer.printText('>');
         }
      }

      var4 = this.leaveElementState();
      if (var5 == null || !var5.equalsIgnoreCase("A") && !var5.equalsIgnoreCase("TD")) {
         var4.afterElement = true;
      }

      var4.empty = false;
      if (this.isDocumentState()) {
         super._printer.flush();
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      try {
         ElementState var4 = this.content();
         var4.doCData = false;
         super.characters(var1, var2, var3);
      } catch (IOException var6) {
         throw new SAXException(var6);
      }
   }

   public void startElement(String var1, AttributeList var2) throws SAXException {
      try {
         if (super._printer == null) {
            throw new IllegalStateException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", (Object[])null));
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

               if (super._indenting && !var5.preserveSpace && (var5.empty || var5.afterElement)) {
                  super._printer.breakLine();
               }
            }

            boolean var4 = var5.preserveSpace;
            super._printer.printText('<');
            if (this._xhtml) {
               super._printer.printText(var1.toLowerCase(Locale.ENGLISH));
            } else {
               super._printer.printText(var1);
            }

            super._printer.indent();
            if (var2 != null) {
               for(int var3 = 0; var3 < var2.getLength(); ++var3) {
                  super._printer.printSpace();
                  String var6 = var2.getName(var3).toLowerCase(Locale.ENGLISH);
                  String var7 = var2.getValue(var3);
                  if (this._xhtml) {
                     if (var7 == null) {
                        super._printer.printText(var6);
                        super._printer.printText("=\"\"");
                     } else {
                        super._printer.printText(var6);
                        super._printer.printText("=\"");
                        this.printEscaped(var7);
                        super._printer.printText('"');
                     }
                  } else {
                     if (var7 == null) {
                        var7 = "";
                     }

                     if (!super._format.getPreserveEmptyAttributes() && var7.length() == 0) {
                        super._printer.printText(var6);
                     } else if (HTMLdtd.isURI(var1, var6)) {
                        super._printer.printText(var6);
                        super._printer.printText("=\"");
                        super._printer.printText(this.escapeURI(var7));
                        super._printer.printText('"');
                     } else if (HTMLdtd.isBoolean(var1, var6)) {
                        super._printer.printText(var6);
                     } else {
                        super._printer.printText(var6);
                        super._printer.printText("=\"");
                        this.printEscaped(var7);
                        super._printer.printText('"');
                     }
                  }
               }
            }

            if (HTMLdtd.isPreserveSpace(var1)) {
               var4 = true;
            }

            var5 = this.enterElementState((String)null, (String)null, var1, var4);
            if (var1.equalsIgnoreCase("A") || var1.equalsIgnoreCase("TD")) {
               var5.empty = false;
               super._printer.printText('>');
            }

            if (var1.equalsIgnoreCase("SCRIPT") || var1.equalsIgnoreCase("STYLE")) {
               if (this._xhtml) {
                  var5.doCData = true;
               } else {
                  var5.unescaped = true;
               }
            }

         }
      } catch (IOException var9) {
         throw new SAXException(var9);
      }
   }

   public void endElement(String var1) throws SAXException {
      this.endElement((String)null, (String)null, var1);
   }

   protected void startDocument(String var1) throws IOException {
      super._printer.leaveDTD();
      if (!super._started) {
         if (super._docTypePublicId == null && super._docTypeSystemId == null) {
            if (this._xhtml) {
               super._docTypePublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
               super._docTypeSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
            } else {
               super._docTypePublicId = "-//W3C//DTD HTML 4.01//EN";
               super._docTypeSystemId = "http://www.w3.org/TR/html4/strict.dtd";
            }
         }

         if (!super._format.getOmitDocumentType()) {
            if (super._docTypePublicId == null || this._xhtml && super._docTypeSystemId == null) {
               if (super._docTypeSystemId != null) {
                  if (this._xhtml) {
                     super._printer.printText("<!DOCTYPE html SYSTEM ");
                  } else {
                     super._printer.printText("<!DOCTYPE HTML SYSTEM ");
                  }

                  this.printDoctypeURL(super._docTypeSystemId);
                  super._printer.printText('>');
                  super._printer.breakLine();
               }
            } else {
               if (this._xhtml) {
                  super._printer.printText("<!DOCTYPE html PUBLIC ");
               } else {
                  super._printer.printText("<!DOCTYPE HTML PUBLIC ");
               }

               this.printDoctypeURL(super._docTypePublicId);
               if (super._docTypeSystemId != null) {
                  if (super._indenting) {
                     super._printer.breakLine();
                     super._printer.printText("                      ");
                  } else {
                     super._printer.printText(' ');
                  }

                  this.printDoctypeURL(super._docTypeSystemId);
               }

               super._printer.printText('>');
               super._printer.breakLine();
            }
         }
      }

      super._started = true;
      this.serializePreRoot();
   }

   protected void serializeElement(Element var1) throws IOException {
      String var10 = var1.getTagName();
      ElementState var6 = this.getElementState();
      if (this.isDocumentState()) {
         if (!super._started) {
            this.startDocument(var10);
         }
      } else {
         if (var6.empty) {
            super._printer.printText('>');
         }

         if (super._indenting && !var6.preserveSpace && (var6.empty || var6.afterElement)) {
            super._printer.breakLine();
         }
      }

      boolean var7 = var6.preserveSpace;
      super._printer.printText('<');
      if (this._xhtml) {
         super._printer.printText(var10.toLowerCase(Locale.ENGLISH));
      } else {
         super._printer.printText(var10);
      }

      super._printer.indent();
      NamedNodeMap var3 = var1.getAttributes();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.getLength(); ++var4) {
            Attr var2 = (Attr)var3.item(var4);
            String var8 = var2.getName().toLowerCase(Locale.ENGLISH);
            String var9 = var2.getValue();
            if (var2.getSpecified()) {
               super._printer.printSpace();
               if (this._xhtml) {
                  if (var9 == null) {
                     super._printer.printText(var8);
                     super._printer.printText("=\"\"");
                  } else {
                     super._printer.printText(var8);
                     super._printer.printText("=\"");
                     this.printEscaped(var9);
                     super._printer.printText('"');
                  }
               } else {
                  if (var9 == null) {
                     var9 = "";
                  }

                  if (!super._format.getPreserveEmptyAttributes() && var9.length() == 0) {
                     super._printer.printText(var8);
                  } else if (HTMLdtd.isURI(var10, var8)) {
                     super._printer.printText(var8);
                     super._printer.printText("=\"");
                     super._printer.printText(this.escapeURI(var9));
                     super._printer.printText('"');
                  } else if (HTMLdtd.isBoolean(var10, var8)) {
                     super._printer.printText(var8);
                  } else {
                     super._printer.printText(var8);
                     super._printer.printText("=\"");
                     this.printEscaped(var9);
                     super._printer.printText('"');
                  }
               }
            }
         }
      }

      if (HTMLdtd.isPreserveSpace(var10)) {
         var7 = true;
      }

      if (!var1.hasChildNodes() && HTMLdtd.isEmptyTag(var10)) {
         super._printer.unindent();
         if (this._xhtml) {
            super._printer.printText(" />");
         } else {
            super._printer.printText('>');
         }

         var6.afterElement = true;
         var6.empty = false;
         if (this.isDocumentState()) {
            super._printer.flush();
         }
      } else {
         var6 = this.enterElementState((String)null, (String)null, var10, var7);
         if (var10.equalsIgnoreCase("A") || var10.equalsIgnoreCase("TD")) {
            var6.empty = false;
            super._printer.printText('>');
         }

         if (var10.equalsIgnoreCase("SCRIPT") || var10.equalsIgnoreCase("STYLE")) {
            if (this._xhtml) {
               var6.doCData = true;
            } else {
               var6.unescaped = true;
            }
         }

         for(Node var5 = var1.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
            this.serializeNode(var5);
         }

         this.endElementIO((String)null, (String)null, var10);
      }

   }

   protected void characters(String var1) throws IOException {
      ElementState var2 = this.content();
      super.characters(var1);
   }

   protected String getEntityRef(int var1) {
      return HTMLdtd.fromChar(var1);
   }

   protected String escapeURI(String var1) {
      int var2 = var1.indexOf("\"");
      return var2 >= 0 ? var1.substring(0, var2) : var1;
   }
}
