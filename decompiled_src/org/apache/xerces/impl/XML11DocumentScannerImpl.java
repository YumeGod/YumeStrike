package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;

public class XML11DocumentScannerImpl extends XMLDocumentScannerImpl {
   private String[] fStrings = new String[3];
   private XMLString fString = new XMLString();
   private XMLStringBuffer fStringBuffer = new XMLStringBuffer();
   private XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
   private XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();

   protected int scanContent() throws IOException, XNIException {
      Object var1 = this.fString;
      int var2 = super.fEntityScanner.scanContent((XMLString)var1);
      if (var2 == 13 || var2 == 133 || var2 == 8232) {
         super.fEntityScanner.scanChar();
         this.fStringBuffer.clear();
         this.fStringBuffer.append(this.fString);
         this.fStringBuffer.append((char)var2);
         var1 = this.fStringBuffer;
         var2 = -1;
      }

      if (super.fDocumentHandler != null && ((XMLString)var1).length > 0) {
         super.fDocumentHandler.characters((XMLString)var1, (Augmentations)null);
      }

      if (var2 == 93 && this.fString.length == 0) {
         this.fStringBuffer.clear();
         this.fStringBuffer.append((char)super.fEntityScanner.scanChar());
         super.fInScanContent = true;
         if (super.fEntityScanner.skipChar(93)) {
            this.fStringBuffer.append(']');

            while(super.fEntityScanner.skipChar(93)) {
               this.fStringBuffer.append(']');
            }

            if (super.fEntityScanner.skipChar(62)) {
               this.reportFatalError("CDEndInContent", (Object[])null);
            }
         }

         if (super.fDocumentHandler != null && this.fStringBuffer.length != 0) {
            super.fDocumentHandler.characters(this.fStringBuffer, (Augmentations)null);
         }

         super.fInScanContent = false;
         var2 = -1;
      }

      return var2;
   }

   protected boolean scanAttributeValue(XMLString var1, XMLString var2, String var3, boolean var4, String var5) throws IOException, XNIException {
      int var6 = super.fEntityScanner.peekChar();
      if (var6 != 39 && var6 != 34) {
         this.reportFatalError("OpenQuoteExpected", new Object[]{var5, var3});
      }

      super.fEntityScanner.scanChar();
      int var7 = super.fEntityDepth;
      int var8 = super.fEntityScanner.scanLiteral(var6, var1);
      int var9 = 0;
      int var10;
      if (var8 == var6 && (var9 = this.isUnchangedByNormalization(var1)) == -1) {
         var2.setValues(var1);
         var10 = super.fEntityScanner.scanChar();
         if (var10 != var6) {
            this.reportFatalError("CloseQuoteExpected", new Object[]{var5, var3});
         }

         return true;
      } else {
         this.fStringBuffer2.clear();
         this.fStringBuffer2.append(var1);
         this.normalizeWhitespace(var1, var9);
         if (var8 != var6) {
            super.fScanningAttribute = true;
            this.fStringBuffer.clear();

            do {
               this.fStringBuffer.append(var1);
               if (var8 == 38) {
                  super.fEntityScanner.skipChar(38);
                  if (var7 == super.fEntityDepth) {
                     this.fStringBuffer2.append('&');
                  }

                  if (super.fEntityScanner.skipChar(35)) {
                     if (var7 == super.fEntityDepth) {
                        this.fStringBuffer2.append('#');
                     }

                     var10 = this.scanCharReferenceValue(this.fStringBuffer, this.fStringBuffer2);
                     if (var10 != -1) {
                     }
                  } else {
                     String var11 = super.fEntityScanner.scanName();
                     if (var11 == null) {
                        this.reportFatalError("NameRequiredInReference", (Object[])null);
                     } else if (var7 == super.fEntityDepth) {
                        this.fStringBuffer2.append(var11);
                     }

                     if (!super.fEntityScanner.skipChar(59)) {
                        this.reportFatalError("SemicolonRequiredInReference", new Object[]{var11});
                     } else if (var7 == super.fEntityDepth) {
                        this.fStringBuffer2.append(';');
                     }

                     if (var11 == XMLScanner.fAmpSymbol) {
                        this.fStringBuffer.append('&');
                     } else if (var11 == XMLScanner.fAposSymbol) {
                        this.fStringBuffer.append('\'');
                     } else if (var11 == XMLScanner.fLtSymbol) {
                        this.fStringBuffer.append('<');
                     } else if (var11 == XMLScanner.fGtSymbol) {
                        this.fStringBuffer.append('>');
                     } else if (var11 == XMLScanner.fQuotSymbol) {
                        this.fStringBuffer.append('"');
                     } else if (super.fEntityManager.isExternalEntity(var11)) {
                        this.reportFatalError("ReferenceToExternalEntity", new Object[]{var11});
                     } else {
                        if (!super.fEntityManager.isDeclaredEntity(var11)) {
                           if (var4) {
                              if (super.fValidation) {
                                 super.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[]{var11}, (short)1);
                              }
                           } else {
                              this.reportFatalError("EntityNotDeclared", new Object[]{var11});
                           }
                        }

                        super.fEntityManager.startEntity(var11, true);
                     }
                  }
               } else if (var8 == 60) {
                  this.reportFatalError("LessthanInAttValue", new Object[]{var5, var3});
                  super.fEntityScanner.scanChar();
                  if (var7 == super.fEntityDepth) {
                     this.fStringBuffer2.append((char)var8);
                  }
               } else if (var8 != 37 && var8 != 93) {
                  if (var8 != 10 && var8 != 13 && var8 != 133 && var8 != 8232) {
                     if (var8 != -1 && XMLChar.isHighSurrogate(var8)) {
                        this.fStringBuffer3.clear();
                        if (this.scanSurrogates(this.fStringBuffer3)) {
                           this.fStringBuffer.append((XMLString)this.fStringBuffer3);
                           if (var7 == super.fEntityDepth) {
                              this.fStringBuffer2.append((XMLString)this.fStringBuffer3);
                           }
                        }
                     } else if (var8 != -1 && XML11Char.isXML11Invalid(var8)) {
                        this.reportFatalError("InvalidCharInAttValue", new Object[]{var5, var3, Integer.toString(var8, 16)});
                        super.fEntityScanner.scanChar();
                        if (var7 == super.fEntityDepth) {
                           this.fStringBuffer2.append((char)var8);
                        }
                     }
                  } else {
                     super.fEntityScanner.scanChar();
                     this.fStringBuffer.append(' ');
                     if (var7 == super.fEntityDepth) {
                        this.fStringBuffer2.append('\n');
                     }
                  }
               } else {
                  super.fEntityScanner.scanChar();
                  this.fStringBuffer.append((char)var8);
                  if (var7 == super.fEntityDepth) {
                     this.fStringBuffer2.append((char)var8);
                  }
               }

               var8 = super.fEntityScanner.scanLiteral(var6, var1);
               if (var7 == super.fEntityDepth) {
                  this.fStringBuffer2.append(var1);
               }

               this.normalizeWhitespace(var1);
            } while(var8 != var6 || var7 != super.fEntityDepth);

            this.fStringBuffer.append(var1);
            var1.setValues(this.fStringBuffer);
            super.fScanningAttribute = false;
         }

         var2.setValues(this.fStringBuffer2);
         var10 = super.fEntityScanner.scanChar();
         if (var10 != var6) {
            this.reportFatalError("CloseQuoteExpected", new Object[]{var5, var3});
         }

         return var2.equals(var1.ch, var1.offset, var1.length);
      }
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
      return XML11Char.isXML11Invalid(var1);
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
