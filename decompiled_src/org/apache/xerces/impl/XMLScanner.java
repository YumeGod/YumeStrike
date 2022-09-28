package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;

public abstract class XMLScanner implements XMLComponent {
   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
   protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
   protected static final boolean DEBUG_ATTR_NORMALIZATION = false;
   protected boolean fValidation = false;
   protected boolean fNamespaces;
   protected boolean fNotifyCharRefs = false;
   protected boolean fParserSettings = true;
   protected SymbolTable fSymbolTable;
   protected XMLErrorReporter fErrorReporter;
   protected XMLEntityManager fEntityManager;
   protected XMLEntityScanner fEntityScanner;
   protected int fEntityDepth;
   protected String fCharRefLiteral = null;
   protected boolean fScanningAttribute;
   protected boolean fReportEntity;
   protected static final String fVersionSymbol = "version".intern();
   protected static final String fEncodingSymbol = "encoding".intern();
   protected static final String fStandaloneSymbol = "standalone".intern();
   protected static final String fAmpSymbol = "amp".intern();
   protected static final String fLtSymbol = "lt".intern();
   protected static final String fGtSymbol = "gt".intern();
   protected static final String fQuotSymbol = "quot".intern();
   protected static final String fAposSymbol = "apos".intern();
   private XMLString fString = new XMLString();
   private XMLStringBuffer fStringBuffer = new XMLStringBuffer();
   private XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
   private XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();
   protected XMLResourceIdentifierImpl fResourceIdentifier = new XMLResourceIdentifierImpl();

   public void reset(XMLComponentManager var1) throws XMLConfigurationException {
      try {
         this.fParserSettings = var1.getFeature("http://apache.org/xml/features/internal/parser-settings");
      } catch (XMLConfigurationException var6) {
         this.fParserSettings = true;
      }

      if (!this.fParserSettings) {
         this.init();
      } else {
         this.fSymbolTable = (SymbolTable)var1.getProperty("http://apache.org/xml/properties/internal/symbol-table");
         this.fErrorReporter = (XMLErrorReporter)var1.getProperty("http://apache.org/xml/properties/internal/error-reporter");
         this.fEntityManager = (XMLEntityManager)var1.getProperty("http://apache.org/xml/properties/internal/entity-manager");

         try {
            this.fValidation = var1.getFeature("http://xml.org/sax/features/validation");
         } catch (XMLConfigurationException var5) {
            this.fValidation = false;
         }

         try {
            this.fNamespaces = var1.getFeature("http://xml.org/sax/features/namespaces");
         } catch (XMLConfigurationException var4) {
            this.fNamespaces = true;
         }

         try {
            this.fNotifyCharRefs = var1.getFeature("http://apache.org/xml/features/scanner/notify-char-refs");
         } catch (XMLConfigurationException var3) {
            this.fNotifyCharRefs = false;
         }

         this.init();
      }
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      if (var1.startsWith("http://apache.org/xml/properties/")) {
         int var3 = var1.length() - "http://apache.org/xml/properties/".length();
         if (var3 == "internal/symbol-table".length() && var1.endsWith("internal/symbol-table")) {
            this.fSymbolTable = (SymbolTable)var2;
         } else if (var3 == "internal/error-reporter".length() && var1.endsWith("internal/error-reporter")) {
            this.fErrorReporter = (XMLErrorReporter)var2;
         } else if (var3 == "internal/entity-manager".length() && var1.endsWith("internal/entity-manager")) {
            this.fEntityManager = (XMLEntityManager)var2;
         }
      }

   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
      if ("http://xml.org/sax/features/validation".equals(var1)) {
         this.fValidation = var2;
      } else if ("http://apache.org/xml/features/scanner/notify-char-refs".equals(var1)) {
         this.fNotifyCharRefs = var2;
      }

   }

   public boolean getFeature(String var1) throws XMLConfigurationException {
      if ("http://xml.org/sax/features/validation".equals(var1)) {
         return this.fValidation;
      } else if ("http://apache.org/xml/features/scanner/notify-char-refs".equals(var1)) {
         return this.fNotifyCharRefs;
      } else {
         throw new XMLConfigurationException((short)0, var1);
      }
   }

   protected void reset() {
      this.init();
      this.fValidation = true;
      this.fNotifyCharRefs = false;
   }

   protected void scanXMLDeclOrTextDecl(boolean var1, String[] var2) throws IOException, XNIException {
      String var3 = null;
      String var4 = null;
      String var5 = null;
      int var10 = 0;
      boolean var11 = false;
      boolean var12 = this.fEntityScanner.skipDeclSpaces();
      XMLEntityManager.ScannedEntity var13 = this.fEntityManager.getCurrentEntity();
      boolean var14 = var13.literal;

      for(var13.literal = false; this.fEntityScanner.peekChar() != 63; var12 = this.fEntityScanner.skipDeclSpaces()) {
         var11 = true;
         String var15 = this.scanPseudoAttribute(var1, this.fString);
         switch (var10) {
            case 0:
               if (var15 == fVersionSymbol) {
                  if (!var12) {
                     this.reportFatalError(var1 ? "SpaceRequiredBeforeVersionInTextDecl" : "SpaceRequiredBeforeVersionInXMLDecl", (Object[])null);
                  }

                  var3 = this.fString.toString();
                  var10 = 1;
                  if (!this.versionSupported(var3)) {
                     this.reportFatalError(this.getVersionNotSupportedKey(), new Object[]{var3});
                  }
               } else if (var15 == fEncodingSymbol) {
                  if (!var1) {
                     this.reportFatalError("VersionInfoRequired", (Object[])null);
                  }

                  if (!var12) {
                     this.reportFatalError(var1 ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", (Object[])null);
                  }

                  var4 = this.fString.toString();
                  var10 = var1 ? 3 : 2;
               } else if (var1) {
                  this.reportFatalError("EncodingDeclRequired", (Object[])null);
               } else {
                  this.reportFatalError("VersionInfoRequired", (Object[])null);
               }
               break;
            case 1:
               if (var15 == fEncodingSymbol) {
                  if (!var12) {
                     this.reportFatalError(var1 ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", (Object[])null);
                  }

                  var4 = this.fString.toString();
                  var10 = var1 ? 3 : 2;
               } else {
                  if (!var1 && var15 == fStandaloneSymbol) {
                     if (!var12) {
                        this.reportFatalError("SpaceRequiredBeforeStandalone", (Object[])null);
                     }

                     var5 = this.fString.toString();
                     var10 = 3;
                     if (!var5.equals("yes") && !var5.equals("no")) {
                        this.reportFatalError("SDDeclInvalid", new Object[]{var5});
                     }
                     continue;
                  }

                  this.reportFatalError("EncodingDeclRequired", (Object[])null);
               }
               break;
            case 2:
               if (var15 == fStandaloneSymbol) {
                  if (!var12) {
                     this.reportFatalError("SpaceRequiredBeforeStandalone", (Object[])null);
                  }

                  var5 = this.fString.toString();
                  var10 = 3;
                  if (!var5.equals("yes") && !var5.equals("no")) {
                     this.reportFatalError("SDDeclInvalid", new Object[]{var5});
                  }
               } else {
                  this.reportFatalError("EncodingDeclRequired", (Object[])null);
               }
               break;
            default:
               this.reportFatalError("NoMorePseudoAttributes", (Object[])null);
         }
      }

      if (var14) {
         var13.literal = true;
      }

      if (var1 && var10 != 3) {
         this.reportFatalError("MorePseudoAttributes", (Object[])null);
      }

      if (var1) {
         if (!var11 && var4 == null) {
            this.reportFatalError("EncodingDeclRequired", (Object[])null);
         }
      } else if (!var11 && var3 == null) {
         this.reportFatalError("VersionInfoRequired", (Object[])null);
      }

      if (!this.fEntityScanner.skipChar(63)) {
         this.reportFatalError("XMLDeclUnterminated", (Object[])null);
      }

      if (!this.fEntityScanner.skipChar(62)) {
         this.reportFatalError("XMLDeclUnterminated", (Object[])null);
      }

      var2[0] = var3;
      var2[1] = var4;
      var2[2] = var5;
   }

   public String scanPseudoAttribute(boolean var1, XMLString var2) throws IOException, XNIException {
      String var3 = this.fEntityScanner.scanName();
      XMLEntityManager.print(this.fEntityManager.getCurrentEntity());
      if (var3 == null) {
         this.reportFatalError("PseudoAttrNameExpected", (Object[])null);
      }

      this.fEntityScanner.skipDeclSpaces();
      if (!this.fEntityScanner.skipChar(61)) {
         this.reportFatalError(var1 ? "EqRequiredInTextDecl" : "EqRequiredInXMLDecl", new Object[]{var3});
      }

      this.fEntityScanner.skipDeclSpaces();
      int var4 = this.fEntityScanner.peekChar();
      if (var4 != 39 && var4 != 34) {
         this.reportFatalError(var1 ? "QuoteRequiredInTextDecl" : "QuoteRequiredInXMLDecl", new Object[]{var3});
      }

      this.fEntityScanner.scanChar();
      int var5 = this.fEntityScanner.scanLiteral(var4, var2);
      if (var5 != var4) {
         this.fStringBuffer2.clear();

         do {
            this.fStringBuffer2.append(var2);
            if (var5 != -1) {
               if (var5 != 38 && var5 != 37 && var5 != 60 && var5 != 93) {
                  if (XMLChar.isHighSurrogate(var5)) {
                     this.scanSurrogates(this.fStringBuffer2);
                  } else if (this.isInvalidLiteral(var5)) {
                     String var6 = var1 ? "InvalidCharInTextDecl" : "InvalidCharInXMLDecl";
                     this.reportFatalError(var6, new Object[]{Integer.toString(var5, 16)});
                     this.fEntityScanner.scanChar();
                  }
               } else {
                  this.fStringBuffer2.append((char)this.fEntityScanner.scanChar());
               }
            }

            var5 = this.fEntityScanner.scanLiteral(var4, var2);
         } while(var5 != var4);

         this.fStringBuffer2.append(var2);
         var2.setValues(this.fStringBuffer2);
      }

      if (!this.fEntityScanner.skipChar(var4)) {
         this.reportFatalError(var1 ? "CloseQuoteMissingInTextDecl" : "CloseQuoteMissingInXMLDecl", new Object[]{var3});
      }

      return var3;
   }

   protected void scanPI() throws IOException, XNIException {
      this.fReportEntity = false;
      String var1 = null;
      if (this.fNamespaces) {
         var1 = this.fEntityScanner.scanNCName();
      } else {
         var1 = this.fEntityScanner.scanName();
      }

      if (var1 == null) {
         this.reportFatalError("PITargetRequired", (Object[])null);
      }

      this.scanPIData(var1, this.fString);
      this.fReportEntity = true;
   }

   protected void scanPIData(String var1, XMLString var2) throws IOException, XNIException {
      int var3;
      if (var1.length() == 3) {
         var3 = Character.toLowerCase(var1.charAt(0));
         char var4 = Character.toLowerCase(var1.charAt(1));
         char var5 = Character.toLowerCase(var1.charAt(2));
         if (var3 == 120 && var4 == 'm' && var5 == 'l') {
            this.reportFatalError("ReservedPITarget", (Object[])null);
         }
      }

      if (!this.fEntityScanner.skipSpaces()) {
         if (this.fEntityScanner.skipString("?>")) {
            var2.clear();
            return;
         }

         if (this.fNamespaces && this.fEntityScanner.peekChar() == 58) {
            this.fEntityScanner.scanChar();
            XMLStringBuffer var6 = new XMLStringBuffer(var1);
            var6.append(":");
            String var7 = this.fEntityScanner.scanName();
            if (var7 != null) {
               var6.append(var7);
            }

            this.reportFatalError("ColonNotLegalWithNS", new Object[]{var6.toString()});
            this.fEntityScanner.skipSpaces();
         } else {
            this.reportFatalError("SpaceRequiredInPI", (Object[])null);
         }
      }

      this.fStringBuffer.clear();
      if (this.fEntityScanner.scanData("?>", this.fStringBuffer)) {
         do {
            var3 = this.fEntityScanner.peekChar();
            if (var3 != -1) {
               if (XMLChar.isHighSurrogate(var3)) {
                  this.scanSurrogates(this.fStringBuffer);
               } else if (this.isInvalidLiteral(var3)) {
                  this.reportFatalError("InvalidCharInPI", new Object[]{Integer.toHexString(var3)});
                  this.fEntityScanner.scanChar();
               }
            }
         } while(this.fEntityScanner.scanData("?>", this.fStringBuffer));
      }

      var2.setValues(this.fStringBuffer);
   }

   protected void scanComment(XMLStringBuffer var1) throws IOException, XNIException {
      var1.clear();

      while(this.fEntityScanner.scanData("--", var1)) {
         int var2 = this.fEntityScanner.peekChar();
         if (var2 != -1) {
            if (XMLChar.isHighSurrogate(var2)) {
               this.scanSurrogates(var1);
            } else if (this.isInvalidLiteral(var2)) {
               this.reportFatalError("InvalidCharInComment", new Object[]{Integer.toHexString(var2)});
               this.fEntityScanner.scanChar();
            }
         }
      }

      if (!this.fEntityScanner.skipChar(62)) {
         this.reportFatalError("DashDashInComment", (Object[])null);
      }

   }

   protected boolean scanAttributeValue(XMLString var1, XMLString var2, String var3, boolean var4, String var5) throws IOException, XNIException {
      int var6 = this.fEntityScanner.peekChar();
      if (var6 != 39 && var6 != 34) {
         this.reportFatalError("OpenQuoteExpected", new Object[]{var5, var3});
      }

      this.fEntityScanner.scanChar();
      int var7 = this.fEntityDepth;
      int var8 = this.fEntityScanner.scanLiteral(var6, var1);
      int var9 = 0;
      int var10;
      if (var8 == var6 && (var9 = this.isUnchangedByNormalization(var1)) == -1) {
         var2.setValues(var1);
         var10 = this.fEntityScanner.scanChar();
         if (var10 != var6) {
            this.reportFatalError("CloseQuoteExpected", new Object[]{var5, var3});
         }

         return true;
      } else {
         this.fStringBuffer2.clear();
         this.fStringBuffer2.append(var1);
         this.normalizeWhitespace(var1, var9);
         if (var8 != var6) {
            this.fScanningAttribute = true;
            this.fStringBuffer.clear();

            do {
               this.fStringBuffer.append(var1);
               if (var8 == 38) {
                  this.fEntityScanner.skipChar(38);
                  if (var7 == this.fEntityDepth) {
                     this.fStringBuffer2.append('&');
                  }

                  if (this.fEntityScanner.skipChar(35)) {
                     if (var7 == this.fEntityDepth) {
                        this.fStringBuffer2.append('#');
                     }

                     var10 = this.scanCharReferenceValue(this.fStringBuffer, this.fStringBuffer2);
                     if (var10 != -1) {
                     }
                  } else {
                     String var11 = this.fEntityScanner.scanName();
                     if (var11 == null) {
                        this.reportFatalError("NameRequiredInReference", (Object[])null);
                     } else if (var7 == this.fEntityDepth) {
                        this.fStringBuffer2.append(var11);
                     }

                     if (!this.fEntityScanner.skipChar(59)) {
                        this.reportFatalError("SemicolonRequiredInReference", new Object[]{var11});
                     } else if (var7 == this.fEntityDepth) {
                        this.fStringBuffer2.append(';');
                     }

                     if (var11 == fAmpSymbol) {
                        this.fStringBuffer.append('&');
                     } else if (var11 == fAposSymbol) {
                        this.fStringBuffer.append('\'');
                     } else if (var11 == fLtSymbol) {
                        this.fStringBuffer.append('<');
                     } else if (var11 == fGtSymbol) {
                        this.fStringBuffer.append('>');
                     } else if (var11 == fQuotSymbol) {
                        this.fStringBuffer.append('"');
                     } else if (this.fEntityManager.isExternalEntity(var11)) {
                        this.reportFatalError("ReferenceToExternalEntity", new Object[]{var11});
                     } else {
                        if (!this.fEntityManager.isDeclaredEntity(var11)) {
                           if (var4) {
                              if (this.fValidation) {
                                 this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[]{var11}, (short)1);
                              }
                           } else {
                              this.reportFatalError("EntityNotDeclared", new Object[]{var11});
                           }
                        }

                        this.fEntityManager.startEntity(var11, true);
                     }
                  }
               } else if (var8 == 60) {
                  this.reportFatalError("LessthanInAttValue", new Object[]{var5, var3});
                  this.fEntityScanner.scanChar();
                  if (var7 == this.fEntityDepth) {
                     this.fStringBuffer2.append((char)var8);
                  }
               } else if (var8 != 37 && var8 != 93) {
                  if (var8 != 10 && var8 != 13) {
                     if (var8 != -1 && XMLChar.isHighSurrogate(var8)) {
                        this.fStringBuffer3.clear();
                        if (this.scanSurrogates(this.fStringBuffer3)) {
                           this.fStringBuffer.append((XMLString)this.fStringBuffer3);
                           if (var7 == this.fEntityDepth) {
                              this.fStringBuffer2.append((XMLString)this.fStringBuffer3);
                           }
                        }
                     } else if (var8 != -1 && this.isInvalidLiteral(var8)) {
                        this.reportFatalError("InvalidCharInAttValue", new Object[]{var5, var3, Integer.toString(var8, 16)});
                        this.fEntityScanner.scanChar();
                        if (var7 == this.fEntityDepth) {
                           this.fStringBuffer2.append((char)var8);
                        }
                     }
                  } else {
                     this.fEntityScanner.scanChar();
                     this.fStringBuffer.append(' ');
                     if (var7 == this.fEntityDepth) {
                        this.fStringBuffer2.append('\n');
                     }
                  }
               } else {
                  this.fEntityScanner.scanChar();
                  this.fStringBuffer.append((char)var8);
                  if (var7 == this.fEntityDepth) {
                     this.fStringBuffer2.append((char)var8);
                  }
               }

               var8 = this.fEntityScanner.scanLiteral(var6, var1);
               if (var7 == this.fEntityDepth) {
                  this.fStringBuffer2.append(var1);
               }

               this.normalizeWhitespace(var1);
            } while(var8 != var6 || var7 != this.fEntityDepth);

            this.fStringBuffer.append(var1);
            var1.setValues(this.fStringBuffer);
            this.fScanningAttribute = false;
         }

         var2.setValues(this.fStringBuffer2);
         var10 = this.fEntityScanner.scanChar();
         if (var10 != var6) {
            this.reportFatalError("CloseQuoteExpected", new Object[]{var5, var3});
         }

         return var2.equals(var1.ch, var1.offset, var1.length);
      }
   }

   protected void scanExternalID(String[] var1, boolean var2) throws IOException, XNIException {
      String var3 = null;
      String var4 = null;
      if (this.fEntityScanner.skipString("PUBLIC")) {
         if (!this.fEntityScanner.skipSpaces()) {
            this.reportFatalError("SpaceRequiredAfterPUBLIC", (Object[])null);
         }

         this.scanPubidLiteral(this.fString);
         var4 = this.fString.toString();
         if (!this.fEntityScanner.skipSpaces() && !var2) {
            this.reportFatalError("SpaceRequiredBetweenPublicAndSystem", (Object[])null);
         }
      }

      if (var4 != null || this.fEntityScanner.skipString("SYSTEM")) {
         if (var4 == null && !this.fEntityScanner.skipSpaces()) {
            this.reportFatalError("SpaceRequiredAfterSYSTEM", (Object[])null);
         }

         int var5 = this.fEntityScanner.peekChar();
         if (var5 != 39 && var5 != 34) {
            if (var4 != null && var2) {
               var1[0] = null;
               var1[1] = var4;
               return;
            }

            this.reportFatalError("QuoteRequiredInSystemID", (Object[])null);
         }

         this.fEntityScanner.scanChar();
         Object var6 = this.fString;
         if (this.fEntityScanner.scanLiteral(var5, (XMLString)var6) != var5) {
            this.fStringBuffer.clear();

            while(true) {
               this.fStringBuffer.append((XMLString)var6);
               int var7 = this.fEntityScanner.peekChar();
               if (XMLChar.isMarkup(var7) || var7 == 93) {
                  this.fStringBuffer.append((char)this.fEntityScanner.scanChar());
               }

               if (this.fEntityScanner.scanLiteral(var5, (XMLString)var6) == var5) {
                  this.fStringBuffer.append((XMLString)var6);
                  var6 = this.fStringBuffer;
                  break;
               }
            }
         }

         var3 = ((XMLString)var6).toString();
         if (!this.fEntityScanner.skipChar(var5)) {
            this.reportFatalError("SystemIDUnterminated", (Object[])null);
         }
      }

      var1[0] = var3;
      var1[1] = var4;
   }

   protected boolean scanPubidLiteral(XMLString var1) throws IOException, XNIException {
      int var2 = this.fEntityScanner.scanChar();
      if (var2 != 39 && var2 != 34) {
         this.reportFatalError("QuoteRequiredInPublicID", (Object[])null);
         return false;
      } else {
         this.fStringBuffer.clear();
         boolean var3 = true;
         boolean var4 = true;

         while(true) {
            while(true) {
               int var5 = this.fEntityScanner.scanChar();
               if (var5 != 32 && var5 != 10 && var5 != 13) {
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
         if (var4 < ' ') {
            var1.ch[var3] = ' ';
         }
      }

   }

   protected void normalizeWhitespace(XMLString var1, int var2) {
      int var3 = var1.offset + var1.length;

      for(int var4 = var1.offset + var2; var4 < var3; ++var4) {
         char var5 = var1.ch[var4];
         if (var5 < ' ') {
            var1.ch[var4] = ' ';
         }
      }

   }

   protected int isUnchangedByNormalization(XMLString var1) {
      int var2 = var1.offset + var1.length;

      for(int var3 = var1.offset; var3 < var2; ++var3) {
         char var4 = var1.ch[var3];
         if (var4 < ' ') {
            return var3 - var1.offset;
         }
      }

      return -1;
   }

   public void startEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      ++this.fEntityDepth;
      this.fEntityScanner = this.fEntityManager.getEntityScanner();
   }

   public void endEntity(String var1, Augmentations var2) throws XNIException {
      --this.fEntityDepth;
   }

   protected int scanCharReferenceValue(XMLStringBuffer var1, XMLStringBuffer var2) throws IOException, XNIException {
      boolean var3 = false;
      boolean var4;
      int var5;
      if (this.fEntityScanner.skipChar(120)) {
         if (var2 != null) {
            var2.append('x');
         }

         var3 = true;
         this.fStringBuffer3.clear();
         var4 = true;
         var5 = this.fEntityScanner.peekChar();
         var4 = var5 >= 48 && var5 <= 57 || var5 >= 97 && var5 <= 102 || var5 >= 65 && var5 <= 70;
         if (var4) {
            if (var2 != null) {
               var2.append((char)var5);
            }

            this.fEntityScanner.scanChar();
            this.fStringBuffer3.append((char)var5);

            do {
               var5 = this.fEntityScanner.peekChar();
               var4 = var5 >= 48 && var5 <= 57 || var5 >= 97 && var5 <= 102 || var5 >= 65 && var5 <= 70;
               if (var4) {
                  if (var2 != null) {
                     var2.append((char)var5);
                  }

                  this.fEntityScanner.scanChar();
                  this.fStringBuffer3.append((char)var5);
               }
            } while(var4);
         } else {
            this.reportFatalError("HexdigitRequiredInCharRef", (Object[])null);
         }
      } else {
         this.fStringBuffer3.clear();
         var4 = true;
         var5 = this.fEntityScanner.peekChar();
         var4 = var5 >= 48 && var5 <= 57;
         if (var4) {
            if (var2 != null) {
               var2.append((char)var5);
            }

            this.fEntityScanner.scanChar();
            this.fStringBuffer3.append((char)var5);

            do {
               var5 = this.fEntityScanner.peekChar();
               var4 = var5 >= 48 && var5 <= 57;
               if (var4) {
                  if (var2 != null) {
                     var2.append((char)var5);
                  }

                  this.fEntityScanner.scanChar();
                  this.fStringBuffer3.append((char)var5);
               }
            } while(var4);
         } else {
            this.reportFatalError("DigitRequiredInCharRef", (Object[])null);
         }
      }

      if (!this.fEntityScanner.skipChar(59)) {
         this.reportFatalError("SemicolonRequiredInCharRef", (Object[])null);
      }

      if (var2 != null) {
         var2.append(';');
      }

      int var8 = -1;

      try {
         var8 = Integer.parseInt(this.fStringBuffer3.toString(), var3 ? 16 : 10);
         if (this.isInvalid(var8)) {
            StringBuffer var9 = new StringBuffer(this.fStringBuffer3.length + 1);
            if (var3) {
               var9.append('x');
            }

            var9.append(this.fStringBuffer3.ch, this.fStringBuffer3.offset, this.fStringBuffer3.length);
            this.reportFatalError("InvalidCharRef", new Object[]{var9.toString()});
         }
      } catch (NumberFormatException var7) {
         StringBuffer var6 = new StringBuffer(this.fStringBuffer3.length + 1);
         if (var3) {
            var6.append('x');
         }

         var6.append(this.fStringBuffer3.ch, this.fStringBuffer3.offset, this.fStringBuffer3.length);
         this.reportFatalError("InvalidCharRef", new Object[]{var6.toString()});
      }

      if (!XMLChar.isSupplemental(var8)) {
         var1.append((char)var8);
      } else {
         var1.append(XMLChar.highSurrogate(var8));
         var1.append(XMLChar.lowSurrogate(var8));
      }

      if (this.fNotifyCharRefs && var8 != -1) {
         String var10 = "#" + (var3 ? "x" : "") + this.fStringBuffer3.toString();
         if (!this.fScanningAttribute) {
            this.fCharRefLiteral = var10;
         }
      }

      return var8;
   }

   protected boolean isInvalid(int var1) {
      return XMLChar.isInvalid(var1);
   }

   protected boolean isInvalidLiteral(int var1) {
      return XMLChar.isInvalid(var1);
   }

   protected boolean isValidNameChar(int var1) {
      return XMLChar.isName(var1);
   }

   protected boolean isValidNameStartChar(int var1) {
      return XMLChar.isNameStart(var1);
   }

   protected boolean isValidNCName(int var1) {
      return XMLChar.isNCName(var1);
   }

   protected boolean isValidNameStartHighSurrogate(int var1) {
      return false;
   }

   protected boolean versionSupported(String var1) {
      return var1.equals("1.0");
   }

   protected String getVersionNotSupportedKey() {
      return "VersionNotSupported";
   }

   protected boolean scanSurrogates(XMLStringBuffer var1) throws IOException, XNIException {
      int var2 = this.fEntityScanner.scanChar();
      int var3 = this.fEntityScanner.peekChar();
      if (!XMLChar.isLowSurrogate(var3)) {
         this.reportFatalError("InvalidCharInContent", new Object[]{Integer.toString(var2, 16)});
         return false;
      } else {
         this.fEntityScanner.scanChar();
         int var4 = XMLChar.supplemental((char)var2, (char)var3);
         if (this.isInvalid(var4)) {
            this.reportFatalError("InvalidCharInContent", new Object[]{Integer.toString(var4, 16)});
            return false;
         } else {
            var1.append((char)var2);
            var1.append((char)var3);
            return true;
         }
      }
   }

   protected void reportFatalError(String var1, Object[] var2) throws XNIException {
      this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", var1, var2, (short)2);
   }

   private void init() {
      this.fEntityScanner = null;
      this.fEntityDepth = 0;
      this.fReportEntity = true;
      this.fResourceIdentifier.clear();
   }

   public abstract Object getPropertyDefault(String var1);

   public abstract Boolean getFeatureDefault(String var1);

   public abstract String[] getRecognizedProperties();

   public abstract String[] getRecognizedFeatures();
}
