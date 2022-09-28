package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.xml.sax.SAXException;

public class XML11Serializer extends XMLSerializer {
   protected static final boolean DEBUG = false;
   protected NamespaceSupport fNSBinder;
   protected NamespaceSupport fLocalNSBinder;
   protected SymbolTable fSymbolTable;
   protected boolean fDOML1 = false;
   protected int fNamespaceCounter = 1;
   protected static final String PREFIX = "NS";
   protected boolean fNamespaces = false;
   private boolean fPreserveSpace;

   public XML11Serializer() {
      super._format.setVersion("1.1");
   }

   public XML11Serializer(OutputFormat var1) {
      super(var1);
      super._format.setVersion("1.1");
   }

   public XML11Serializer(Writer var1, OutputFormat var2) {
      super(var1, var2);
      super._format.setVersion("1.1");
   }

   public XML11Serializer(OutputStream var1, OutputFormat var2) {
      super(var1, var2 != null ? var2 : new OutputFormat("xml", (String)null, false));
      super._format.setVersion("1.1");
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      try {
         ElementState var4 = this.content();
         int var5;
         if (!var4.inCData && !var4.doCData) {
            if (var4.preserveSpace) {
               var5 = super._printer.getNextIndent();
               super._printer.setNextIndent(0);
               this.printText(var1, var2, var3, true, var4.unescaped);
               super._printer.setNextIndent(var5);
            } else {
               this.printText(var1, var2, var3, false, var4.unescaped);
            }
         } else {
            if (!var4.inCData) {
               super._printer.printText("<![CDATA[");
               var4.inCData = true;
            }

            var5 = super._printer.getNextIndent();
            super._printer.setNextIndent(0);
            int var7 = var2 + var3;

            for(int var8 = var2; var8 < var7; ++var8) {
               char var6 = var1[var8];
               if (var6 == ']' && var8 + 2 < var7 && var1[var8 + 1] == ']' && var1[var8 + 2] == '>') {
                  super._printer.printText("]]]]><![CDATA[>");
                  var8 += 2;
               } else if (!XML11Char.isXML11Valid(var6)) {
                  ++var8;
                  if (var8 < var7) {
                     this.surrogates(var6, var1[var8]);
                  } else {
                     this.fatalError("The character '" + var6 + "' is an invalid XML character");
                  }
               } else if (super._encodingInfo.isPrintable(var6) && XML11Char.isXML11ValidLiteral(var6)) {
                  super._printer.printText(var6);
               } else {
                  super._printer.printText("]]>&#x");
                  super._printer.printText(Integer.toHexString(var6));
                  super._printer.printText(";<![CDATA[");
               }
            }

            super._printer.setNextIndent(var5);
         }

      } catch (IOException var9) {
         throw new SAXException(var9);
      }
   }

   protected void printEscaped(String var1) throws IOException {
      int var2 = var1.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         char var4 = var1.charAt(var3);
         if (!XML11Char.isXML11Valid(var4)) {
            ++var3;
            if (var3 < var2) {
               this.surrogates(var4, var1.charAt(var3));
            } else {
               this.fatalError("The character '" + (char)var4 + "' is an invalid XML character");
            }
         } else if (var4 != '\n' && var4 != '\r' && var4 != '\t' && var4 != 133 && var4 != 8232) {
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

   protected final void printCDATAText(String var1) throws IOException {
      int var2 = var1.length();

      for(int var4 = 0; var4 < var2; ++var4) {
         char var3 = var1.charAt(var4);
         if (var3 == ']' && var4 + 2 < var2 && var1.charAt(var4 + 1) == ']' && var1.charAt(var4 + 2) == '>') {
            if (super.fDOMErrorHandler != null) {
               String var5;
               if ((super.features & 16) == 0 && (super.features & 2) == 0) {
                  var5 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "EndingCDATA", (Object[])null);
                  this.modifyDOMError(var5, (short)3, (String)null, super.fCurrentNode);
                  boolean var6 = super.fDOMErrorHandler.handleError(super.fDOMError);
                  if (!var6) {
                     throw new IOException();
                  }
               } else {
                  var5 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SplittingCDATA", (Object[])null);
                  this.modifyDOMError(var5, (short)1, (String)null, super.fCurrentNode);
                  super.fDOMErrorHandler.handleError(super.fDOMError);
               }
            }

            super._printer.printText("]]]]><![CDATA[>");
            var4 += 2;
         } else if (!XML11Char.isXML11Valid(var3)) {
            ++var4;
            if (var4 < var2) {
               this.surrogates(var3, var1.charAt(var4));
            } else {
               this.fatalError("The character '" + var3 + "' is an invalid XML character");
            }
         } else if (super._encodingInfo.isPrintable(var3) && XML11Char.isXML11ValidLiteral(var3)) {
            super._printer.printText(var3);
         } else {
            super._printer.printText("]]>&#x");
            super._printer.printText(Integer.toHexString(var3));
            super._printer.printText(";<![CDATA[");
         }
      }

   }

   protected final void printXMLChar(int var1) throws IOException {
      if (var1 != 13 && var1 != 133 && var1 != 8232) {
         if (var1 == 60) {
            super._printer.printText("&lt;");
         } else if (var1 == 38) {
            super._printer.printText("&amp;");
         } else if (var1 == 62) {
            super._printer.printText("&gt;");
         } else if (super._encodingInfo.isPrintable((char)var1) && XML11Char.isXML11ValidLiteral(var1)) {
            super._printer.printText((char)var1);
         } else {
            this.printHex(var1);
         }
      } else {
         this.printHex(var1);
      }

   }

   protected final void surrogates(int var1, int var2) throws IOException {
      if (XMLChar.isHighSurrogate(var1)) {
         if (!XMLChar.isLowSurrogate(var2)) {
            this.fatalError("The character '" + (char)var2 + "' is an invalid XML character");
         } else {
            int var3 = XMLChar.supplemental((char)var1, (char)var2);
            if (!XML11Char.isXML11Valid(var3)) {
               this.fatalError("The character '" + (char)var3 + "' is an invalid XML character");
            } else if (this.content().inCData) {
               super._printer.printText("]]>&#x");
               super._printer.printText(Integer.toHexString(var3));
               super._printer.printText(";<![CDATA[");
            } else {
               this.printHex(var3);
            }
         }
      } else {
         this.fatalError("The character '" + (char)var1 + "' is an invalid XML character");
      }

   }

   protected void printText(String var1, boolean var2, boolean var3) throws IOException {
      int var6 = var1.length();
      int var4;
      char var5;
      if (var2) {
         for(var4 = 0; var4 < var6; ++var4) {
            var5 = var1.charAt(var4);
            if (!XML11Char.isXML11Valid(var5)) {
               ++var4;
               if (var4 < var6) {
                  this.surrogates(var5, var1.charAt(var4));
               } else {
                  this.fatalError("The character '" + var5 + "' is an invalid XML character");
               }
            } else if (var3 && XML11Char.isXML11ValidLiteral(var5)) {
               super._printer.printText(var5);
            } else {
               this.printXMLChar(var5);
            }
         }
      } else {
         for(var4 = 0; var4 < var6; ++var4) {
            var5 = var1.charAt(var4);
            if (!XML11Char.isXML11Valid(var5)) {
               ++var4;
               if (var4 < var6) {
                  this.surrogates(var5, var1.charAt(var4));
               } else {
                  this.fatalError("The character '" + var5 + "' is an invalid XML character");
               }
            } else if (var3 && XML11Char.isXML11ValidLiteral(var5)) {
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
         while(true) {
            while(var3-- > 0) {
               var7 = var1[var2++];
               if (!XML11Char.isXML11Valid(var7)) {
                  if (var3-- > 0) {
                     this.surrogates(var7, var1[var2++]);
                  } else {
                     this.fatalError("The character '" + var7 + "' is an invalid XML character");
                  }
               } else if (var5 && XML11Char.isXML11ValidLiteral(var7)) {
                  super._printer.printText(var7);
               } else {
                  this.printXMLChar(var7);
               }
            }

            return;
         }
      } else {
         while(true) {
            while(var3-- > 0) {
               var7 = var1[var2++];
               if (!XML11Char.isXML11Valid(var7)) {
                  if (var3-- > 0) {
                     this.surrogates(var7, var1[var2++]);
                  } else {
                     this.fatalError("The character '" + var7 + "' is an invalid XML character");
                  }
               } else if (var5 && XML11Char.isXML11ValidLiteral(var7)) {
                  super._printer.printText(var7);
               } else {
                  this.printXMLChar(var7);
               }
            }

            return;
         }
      }
   }

   public boolean reset() {
      super.reset();
      return true;
   }
}
