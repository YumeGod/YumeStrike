package org.apache.xerces.xpointer;

import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xinclude.XIncludeHandler;
import org.apache.xerces.xinclude.XIncludeNamespaceSupport;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLErrorHandler;

public final class XPointerHandler extends XIncludeHandler implements XPointerProcessor {
   protected Vector fXPointerParts = null;
   protected XPointerPart fXPointerPart = null;
   protected boolean fFoundMatchingPtrPart = false;
   protected XMLErrorReporter fXPointerErrorReporter;
   protected XMLErrorHandler fErrorHandler;
   protected SymbolTable fSymbolTable = null;
   private final String ELEMENT_SCHEME_NAME = "element";
   protected boolean fIsXPointerResolved = false;
   protected boolean fFixupBase = false;
   protected boolean fFixupLang = false;

   public XPointerHandler() {
      this.fXPointerParts = new Vector();
      this.fSymbolTable = new SymbolTable();
   }

   public XPointerHandler(SymbolTable var1, XMLErrorHandler var2, XMLErrorReporter var3) {
      this.fXPointerParts = new Vector();
      this.fSymbolTable = var1;
      this.fErrorHandler = var2;
      this.fXPointerErrorReporter = var3;
   }

   public void parseXPointer(String var1) throws XNIException {
      this.init();
      Tokens var2 = new Tokens(this.fSymbolTable);
      Scanner var3 = new Scanner(this.fSymbolTable) {
         protected void addToken(Tokens var1, int var2) throws XNIException {
            if (var2 != 0 && var2 != 1 && var2 != 3 && var2 != 4 && var2 != 2) {
               XPointerHandler.this.reportError("InvalidXPointerToken", new Object[]{var1.getTokenString(var2)});
            } else {
               super.addToken(var1, var2);
            }
         }
      };
      int var4 = var1.length();
      boolean var5 = var3.scanExpr(this.fSymbolTable, var2, var1, 0, var4);
      if (!var5) {
         this.reportError("InvalidXPointerExpression", new Object[]{var1});
      }

      while(true) {
         while(var2.hasMore()) {
            int var6 = var2.nextToken();
            String var7;
            switch (var6) {
               case 2:
                  var6 = var2.nextToken();
                  var7 = var2.getTokenString(var6);
                  if (var7 == null) {
                     this.reportError("InvalidXPointerExpression", new Object[]{var1});
                  }

                  ShortHandPointer var18 = new ShortHandPointer(this.fSymbolTable);
                  var18.setSchemeName(var7);
                  this.fXPointerParts.add(var18);
                  break;
               case 3:
                  var6 = var2.nextToken();
                  var7 = var2.getTokenString(var6);
                  var6 = var2.nextToken();
                  String var8 = var2.getTokenString(var6);
                  String var9 = var7 + var8;
                  int var10 = 0;
                  int var11 = 0;
                  var6 = var2.nextToken();
                  String var12 = var2.getTokenString(var6);
                  if (var12 != "XPTRTOKEN_OPEN_PAREN") {
                     if (var6 == 2) {
                        this.reportError("MultipleShortHandPointers", new Object[]{var1});
                     } else {
                        this.reportError("InvalidXPointerExpression", new Object[]{var1});
                     }
                  }

                  ++var10;

                  String var13;
                  for(var13 = null; var2.hasMore(); ++var10) {
                     var6 = var2.nextToken();
                     var13 = var2.getTokenString(var6);
                     if (var13 != "XPTRTOKEN_OPEN_PAREN") {
                        break;
                     }
                  }

                  var6 = var2.nextToken();
                  var13 = var2.getTokenString(var6);
                  var6 = var2.nextToken();
                  String var14 = var2.getTokenString(var6);
                  if (var14 != "XPTRTOKEN_CLOSE_PAREN") {
                     this.reportError("SchemeDataNotFollowedByCloseParenthesis", new Object[]{var1});
                  }

                  ++var11;

                  while(var2.hasMore() && var2.getTokenString(var2.peekToken()) == "XPTRTOKEN_OPEN_PAREN") {
                     ++var11;
                  }

                  if (var10 != var11) {
                     this.reportError("UnbalancedParenthesisInXPointerExpression", new Object[]{var1, new Integer(var10), new Integer(var11)});
                  }

                  if (var9.equals("element")) {
                     ElementSchemePointer var15 = new ElementSchemePointer(this.fSymbolTable, super.fErrorReporter);
                     var15.setSchemeName(var9);
                     var15.setSchemeData(var13);

                     try {
                        var15.parseXPointer(var13);
                        this.fXPointerParts.add(var15);
                     } catch (XNIException var17) {
                        throw new XNIException(var17);
                     }
                  } else {
                     this.reportWarning("SchemeUnsupported", new Object[]{var9});
                  }
                  break;
               default:
                  this.reportError("InvalidXPointerExpression", new Object[]{var1});
            }
         }

         return;
      }
   }

   public boolean resolveXPointer(QName var1, XMLAttributes var2, Augmentations var3, int var4) throws XNIException {
      boolean var5 = false;
      if (!this.fFoundMatchingPtrPart) {
         for(int var6 = 0; var6 < this.fXPointerParts.size(); ++var6) {
            this.fXPointerPart = (XPointerPart)this.fXPointerParts.get(var6);
            if (this.fXPointerPart.resolveXPointer(var1, var2, var3, var4)) {
               this.fFoundMatchingPtrPart = true;
               var5 = true;
            }
         }
      } else if (this.fXPointerPart.resolveXPointer(var1, var2, var3, var4)) {
         var5 = true;
      }

      if (!this.fIsXPointerResolved) {
         this.fIsXPointerResolved = var5;
      }

      return var5;
   }

   public boolean isFragmentResolved() throws XNIException {
      boolean var1 = this.fXPointerPart != null ? this.fXPointerPart.isFragmentResolved() : false;
      if (!this.fIsXPointerResolved) {
         this.fIsXPointerResolved = var1;
      }

      return var1;
   }

   public boolean isChildFragmentResolved() throws XNIException {
      boolean var1 = this.fXPointerPart != null ? this.fXPointerPart.isChildFragmentResolved() : false;
      return var1;
   }

   public boolean isXPointerResolved() throws XNIException {
      return this.fIsXPointerResolved;
   }

   public XPointerPart getXPointerPart() {
      return this.fXPointerPart;
   }

   private void reportError(String var1, Object[] var2) throws XNIException {
      throw new XNIException(super.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/XPTR").formatMessage(super.fErrorReporter.getLocale(), var1, var2));
   }

   private void reportWarning(String var1, Object[] var2) throws XNIException {
      this.fXPointerErrorReporter.reportError("http://www.w3.org/TR/XPTR", var1, var2, (short)0);
   }

   protected void initErrorReporter() {
      if (this.fXPointerErrorReporter == null) {
         this.fXPointerErrorReporter = new XMLErrorReporter();
      }

      if (this.fErrorHandler == null) {
         this.fErrorHandler = new XPointerErrorHandler();
      }

      this.fXPointerErrorReporter.putMessageFormatter("http://www.w3.org/TR/XPTR", new XPointerMessageFormatter());
   }

   protected void init() {
      this.fXPointerParts.clear();
      this.fXPointerPart = null;
      this.fFoundMatchingPtrPart = false;
      this.fIsXPointerResolved = false;
      this.initErrorReporter();
   }

   public Vector getPointerParts() {
      return this.fXPointerParts;
   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
      if (this.isChildFragmentResolved()) {
         super.comment(var1, var2);
      }
   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
      if (this.isChildFragmentResolved()) {
         super.processingInstruction(var1, var2, var3);
      }
   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      if (!this.resolveXPointer(var1, var2, var3, 0)) {
         if (this.fFixupBase) {
            this.processXMLBaseAttributes(var2);
         }

         if (this.fFixupLang) {
            this.processXMLLangAttributes(var2);
         }

         super.fNamespaceContext.setContextInvalid();
      } else {
         super.startElement(var1, var2, var3);
      }
   }

   public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      if (!this.resolveXPointer(var1, var2, var3, 2)) {
         if (this.fFixupBase) {
            this.processXMLBaseAttributes(var2);
         }

         if (this.fFixupLang) {
            this.processXMLLangAttributes(var2);
         }

         super.fNamespaceContext.setContextInvalid();
      } else {
         super.emptyElement(var1, var2, var3);
      }
   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
      if (this.isChildFragmentResolved()) {
         super.characters(var1, var2);
      }
   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      if (this.isChildFragmentResolved()) {
         super.ignorableWhitespace(var1, var2);
      }
   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      if (this.resolveXPointer(var1, (XMLAttributes)null, var2, 1)) {
         super.endElement(var1, var2);
      }
   }

   public void startCDATA(Augmentations var1) throws XNIException {
      if (this.isChildFragmentResolved()) {
         super.startCDATA(var1);
      }
   }

   public void endCDATA(Augmentations var1) throws XNIException {
      if (this.isChildFragmentResolved()) {
         super.endCDATA(var1);
      }
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      if (var1 == "http://apache.org/xml/properties/internal/error-reporter") {
         if (var2 != null) {
            this.fXPointerErrorReporter = (XMLErrorReporter)var2;
         } else {
            this.fXPointerErrorReporter = null;
         }
      }

      if (var1 == "http://apache.org/xml/properties/internal/error-handler") {
         if (var2 != null) {
            this.fErrorHandler = (XMLErrorHandler)var2;
         } else {
            this.fErrorHandler = null;
         }
      }

      if (var1 == "http://apache.org/xml/features/xinclude/fixup-language") {
         if (var2 != null) {
            this.fFixupLang = (Boolean)var2;
         } else {
            this.fFixupLang = false;
         }
      }

      if (var1 == "http://apache.org/xml/features/xinclude/fixup-base-uris") {
         if (var2 != null) {
            this.fFixupBase = (Boolean)var2;
         } else {
            this.fFixupBase = false;
         }
      }

      if (var1 == "http://apache.org/xml/properties/internal/namespace-context") {
         super.fNamespaceContext = (XIncludeNamespaceSupport)var2;
      }

      super.setProperty(var1, var2);
   }

   private class Scanner {
      private static final byte CHARTYPE_INVALID = 0;
      private static final byte CHARTYPE_OTHER = 1;
      private static final byte CHARTYPE_WHITESPACE = 2;
      private static final byte CHARTYPE_CARRET = 3;
      private static final byte CHARTYPE_OPEN_PAREN = 4;
      private static final byte CHARTYPE_CLOSE_PAREN = 5;
      private static final byte CHARTYPE_MINUS = 6;
      private static final byte CHARTYPE_PERIOD = 7;
      private static final byte CHARTYPE_SLASH = 8;
      private static final byte CHARTYPE_DIGIT = 9;
      private static final byte CHARTYPE_COLON = 10;
      private static final byte CHARTYPE_EQUAL = 11;
      private static final byte CHARTYPE_LETTER = 12;
      private static final byte CHARTYPE_UNDERSCORE = 13;
      private static final byte CHARTYPE_NONASCII = 14;
      private final byte[] fASCIICharMap;
      private SymbolTable fSymbolTable;

      private Scanner(SymbolTable var2) {
         this.fASCIICharMap = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 4, 5, 1, 1, 1, 6, 7, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10, 1, 1, 11, 1, 1, 1, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 1, 1, 1, 3, 13, 1, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 1, 1, 1, 1, 1};
         this.fSymbolTable = var2;
      }

      private boolean scanExpr(SymbolTable var1, Tokens var2, String var3, int var4, int var5) throws XNIException {
         int var7 = 0;
         int var8 = 0;
         boolean var11 = false;
         String var12 = null;
         String var13 = null;
         String var14 = null;
         StringBuffer var15 = new StringBuffer();

         while(true) {
            if (var4 != var5) {
               int var6;
               for(var6 = var3.charAt(var4); var6 == 32 || var6 == 10 || var6 == 9 || var6 == 13; var6 = var3.charAt(var4)) {
                  ++var4;
                  if (var4 == var5) {
                     break;
                  }
               }

               if (var4 != var5) {
                  byte var16 = var6 >= 128 ? 14 : this.fASCIICharMap[var6];
                  switch (var16) {
                     case 1:
                     case 2:
                     case 3:
                     case 6:
                     case 7:
                     case 8:
                     case 9:
                     case 10:
                     case 11:
                     case 12:
                     case 13:
                     case 14:
                        boolean var17;
                        if (var7 == 0) {
                           int var9 = var4;
                           var4 = this.scanNCName(var3, var5, var4);
                           if (var4 == var9) {
                              XPointerHandler.this.reportError("InvalidShortHandPointer", new Object[]{var3});
                              return false;
                           }

                           if (var4 < var5) {
                              var6 = var3.charAt(var4);
                           } else {
                              var6 = -1;
                           }

                           var12 = var1.addSymbol(var3.substring(var9, var4));
                           var13 = XMLSymbols.EMPTY_STRING;
                           if (var6 == 58) {
                              ++var4;
                              if (var4 == var5) {
                                 return false;
                              }

                              var3.charAt(var4);
                              var13 = var12;
                              var9 = var4;
                              var4 = this.scanNCName(var3, var5, var4);
                              if (var4 == var9) {
                                 return false;
                              }

                              if (var4 < var5) {
                                 var3.charAt(var4);
                              } else {
                                 var17 = true;
                              }

                              var11 = true;
                              var12 = var1.addSymbol(var3.substring(var9, var4));
                           }

                           if (var4 != var5) {
                              this.addToken(var2, 3);
                              var2.addToken(var13);
                              var2.addToken(var12);
                              var11 = false;
                           } else if (var4 == var5) {
                              this.addToken(var2, 2);
                              var2.addToken(var12);
                              var11 = false;
                           }

                           var8 = 0;
                           continue;
                        }

                        if (var7 > 0 && var8 == 0 && var12 != null) {
                           int var10 = var4;
                           var4 = this.scanData(var3, var15, var5, var4);
                           if (var4 == var10) {
                              XPointerHandler.this.reportError("InvalidSchemeDataInXPointer", new Object[]{var3});
                              return false;
                           }

                           if (var4 < var5) {
                              var3.charAt(var4);
                           } else {
                              var17 = true;
                           }

                           var14 = var1.addSymbol(var15.toString());
                           this.addToken(var2, 4);
                           var2.addToken(var14);
                           var7 = 0;
                           var15.delete(0, var15.length());
                           continue;
                        }

                        return false;
                     case 4:
                        this.addToken(var2, 0);
                        ++var7;
                        ++var4;
                        continue;
                     case 5:
                        this.addToken(var2, 1);
                        ++var8;
                        ++var4;
                     default:
                        continue;
                  }
               }
            }

            return true;
         }
      }

      private int scanNCName(String var1, int var2, int var3) {
         char var4 = var1.charAt(var3);
         byte var5;
         if (var4 >= 128) {
            if (!XMLChar.isNameStart(var4)) {
               return var3;
            }
         } else {
            var5 = this.fASCIICharMap[var4];
            if (var5 != 12 && var5 != 13) {
               return var3;
            }
         }

         while(true) {
            ++var3;
            if (var3 >= var2) {
               break;
            }

            var4 = var1.charAt(var3);
            if (var4 >= 128) {
               if (!XMLChar.isName(var4)) {
                  break;
               }
            } else {
               var5 = this.fASCIICharMap[var4];
               if (var5 != 12 && var5 != 9 && var5 != 7 && var5 != 6 && var5 != 13) {
                  break;
               }
            }
         }

         return var3;
      }

      private int scanData(String var1, StringBuffer var2, int var3, int var4) {
         while(true) {
            while(true) {
               while(true) {
                  if (var4 != var3) {
                     char var5 = var1.charAt(var4);
                     byte var6 = var5 >= 128 ? 14 : this.fASCIICharMap[var5];
                     if (var6 == 4) {
                        var2.append(var5);
                        ++var4;
                        var4 = this.scanData(var1, var2, var3, var4);
                        if (var4 == var3) {
                           return var4;
                        }

                        var5 = var1.charAt(var4);
                        var6 = var5 >= 128 ? 14 : this.fASCIICharMap[var5];
                        if (var6 != 5) {
                           return var3;
                        }

                        var2.append((char)var5);
                        ++var4;
                        continue;
                     }

                     if (var6 == 5) {
                        return var4;
                     }

                     if (var6 != 3) {
                        var2.append((char)var5);
                        ++var4;
                        continue;
                     }

                     ++var4;
                     var5 = var1.charAt(var4);
                     var6 = var5 >= 128 ? 14 : this.fASCIICharMap[var5];
                     if (var6 == 3 || var6 == 4 || var6 == 5) {
                        var2.append((char)var5);
                        ++var4;
                        continue;
                     }
                  }

                  return var4;
               }
            }
         }
      }

      protected void addToken(Tokens var1, int var2) throws XNIException {
         var1.addToken(var2);
      }

      // $FF: synthetic method
      Scanner(SymbolTable var2, Object var3) {
         this(var2);
      }
   }

   private final class Tokens {
      private static final int XPTRTOKEN_OPEN_PAREN = 0;
      private static final int XPTRTOKEN_CLOSE_PAREN = 1;
      private static final int XPTRTOKEN_SHORTHAND = 2;
      private static final int XPTRTOKEN_SCHEMENAME = 3;
      private static final int XPTRTOKEN_SCHEMEDATA = 4;
      private final String[] fgTokenNames;
      private static final int INITIAL_TOKEN_COUNT = 256;
      private int[] fTokens;
      private int fTokenCount;
      private int fCurrentTokenIndex;
      private SymbolTable fSymbolTable;
      private Hashtable fTokenNames;

      private Tokens(SymbolTable var2) {
         this.fgTokenNames = new String[]{"XPTRTOKEN_OPEN_PAREN", "XPTRTOKEN_CLOSE_PAREN", "XPTRTOKEN_SHORTHAND", "XPTRTOKEN_SCHEMENAME", "XPTRTOKEN_SCHEMEDATA"};
         this.fTokens = new int[256];
         this.fTokenCount = 0;
         this.fTokenNames = new Hashtable();
         this.fSymbolTable = var2;
         this.fTokenNames.put(new Integer(0), "XPTRTOKEN_OPEN_PAREN");
         this.fTokenNames.put(new Integer(1), "XPTRTOKEN_CLOSE_PAREN");
         this.fTokenNames.put(new Integer(2), "XPTRTOKEN_SHORTHAND");
         this.fTokenNames.put(new Integer(3), "XPTRTOKEN_SCHEMENAME");
         this.fTokenNames.put(new Integer(4), "XPTRTOKEN_SCHEMEDATA");
      }

      private String getTokenString(int var1) {
         return (String)this.fTokenNames.get(new Integer(var1));
      }

      private void addToken(String var1) {
         Integer var2 = (Integer)this.fTokenNames.get(var1);
         if (var2 == null) {
            var2 = new Integer(this.fTokenNames.size());
            this.fTokenNames.put(var2, var1);
         }

         this.addToken(var2);
      }

      private void addToken(int var1) {
         try {
            this.fTokens[this.fTokenCount] = var1;
         } catch (ArrayIndexOutOfBoundsException var4) {
            int[] var3 = this.fTokens;
            this.fTokens = new int[this.fTokenCount << 1];
            System.arraycopy(var3, 0, this.fTokens, 0, this.fTokenCount);
            this.fTokens[this.fTokenCount] = var1;
         }

         ++this.fTokenCount;
      }

      private void rewind() {
         this.fCurrentTokenIndex = 0;
      }

      private boolean hasMore() {
         return this.fCurrentTokenIndex < this.fTokenCount;
      }

      private int nextToken() throws XNIException {
         if (this.fCurrentTokenIndex == this.fTokenCount) {
            XPointerHandler.this.reportError("XPointerProcessingError", (Object[])null);
         }

         return this.fTokens[this.fCurrentTokenIndex++];
      }

      private int peekToken() throws XNIException {
         if (this.fCurrentTokenIndex == this.fTokenCount) {
            XPointerHandler.this.reportError("XPointerProcessingError", (Object[])null);
         }

         return this.fTokens[this.fCurrentTokenIndex];
      }

      private String nextTokenAsString() throws XNIException {
         String var1 = this.getTokenString(this.nextToken());
         if (var1 == null) {
            XPointerHandler.this.reportError("XPointerProcessingError", (Object[])null);
         }

         return var1;
      }

      // $FF: synthetic method
      Tokens(SymbolTable var2, Object var3) {
         this(var2);
      }
   }
}
