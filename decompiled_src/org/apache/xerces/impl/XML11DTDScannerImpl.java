package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;

public class XML11DTDScannerImpl extends XMLDTDScannerImpl {
   private String[] fStrings = new String[3];
   private XMLString fString = new XMLString();
   private XMLStringBuffer fStringBuffer = new XMLStringBuffer();
   private XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
   private XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();

   public XML11DTDScannerImpl() {
   }

   public XML11DTDScannerImpl(SymbolTable var1, XMLErrorReporter var2, XMLEntityManager var3) {
      super(var1, var2, var3);
   }

   protected boolean scanPubidLiteral(XMLString var1) throws IOException, XNIException {
      int var2 = super.fEntityScanner.scanChar();
      if (var2 != 39 && var2 != 34) {
         this.reportFatalError("QuoteRequiredInPublicID", (Object[])null);
         return false;
      } else {
         this.fStringBuffer.clear();
         boolean var3 = true;
         boolean var4 = true;

         while(true) {
            while(true) {
               int var5 = super.fEntityScanner.scanChar();
               if (var5 != 32 && var5 != 10 && var5 != 13 && var5 != 133 && var5 != 8232) {
                  if (var5 == var2) {
                     if (var3) {
                        --this.fStringBuffer.length;
                     }

                     var1.setValues(this.fStringBuffer);
                     return var4;
                  }

                  if (XMLChar.isPubid(var5)) {
                     this.fStringBuffer.append((char)var5);
                     var3 = false;
                  } else {
                     if (var5 == -1) {
                        this.reportFatalError("PublicIDUnterminated", (Object[])null);
                        return false;
                     }

                     var4 = false;
                     this.reportFatalError("InvalidCharInPublicID", new Object[]{Integer.toHexString(var5)});
                  }
               } else if (!var3) {
                  this.fStringBuffer.append(' ');
                  var3 = true;
               }
            }
         }
      }
   }

   protected void normalizeWhitespace(XMLString var1) {
      int var2 = var1.offset + var1.length;

      for(int var3 = var1.offset; var3 < var2; ++var3) {
         char var4 = var1.ch[var3];
         if (XMLChar.isSpace(var4)) {
            var1.ch[var3] = ' ';
         }
      }

   }

   protected void normalizeWhitespace(XMLString var1, int var2) {
      int var3 = var1.offset + var1.length;

      for(int var4 = var1.offset + var2; var4 < var3; ++var4) {
         char var5 = var1.ch[var4];
         if (XMLChar.isSpace(var5)) {
            var1.ch[var4] = ' ';
         }
      }

   }

   protected int isUnchangedByNormalization(XMLString var1) {
      int var2 = var1.offset + var1.length;

      for(int var3 = var1.offset; var3 < var2; ++var3) {
         char var4 = var1.ch[var3];
         if (XMLChar.isSpace(var4)) {
            return var3 - var1.offset;
         }
      }

      return -1;
   }

   protected boolean isInvalid(int var1) {
      return !XML11Char.isXML11Valid(var1);
   }

   protected boolean isInvalidLiteral(int var1) {
      return !XML11Char.isXML11ValidLiteral(var1);
   }

   protected boolean isValidNameChar(int var1) {
      return XML11Char.isXML11Name(var1);
   }

   protected boolean isValidNameStartChar(int var1) {
      return XML11Char.isXML11NameStart(var1);
   }

   protected boolean isValidNCName(int var1) {
      return XML11Char.isXML11NCName(var1);
   }

   protected boolean isValidNameStartHighSurrogate(int var1) {
      return XML11Char.isXML11NameHighSurrogate(var1);
   }

   protected boolean versionSupported(String var1) {
      return var1.equals("1.1") || var1.equals("1.0");
   }

   protected String getVersionNotSupportedKey() {
      return "VersionNotSupported11";
   }
}
