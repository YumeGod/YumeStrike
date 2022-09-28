package org.apache.xerces.xpointer;

import java.util.Hashtable;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLErrorHandler;

class ElementSchemePointer implements XPointerPart {
   private String fSchemeName;
   private String fSchemeData;
   private String fShortHandPointerName;
   private boolean fIsResolveElement = false;
   private boolean fIsElementFound = false;
   private boolean fWasOnlyEmptyElementFound = false;
   boolean fIsShortHand = false;
   int fFoundDepth = 0;
   private int[] fChildSequence;
   private int fCurrentChildPosition = 1;
   private int fCurrentChildDepth = 0;
   private int[] fCurrentChildSequence;
   private boolean fIsFragmentResolved = false;
   private ShortHandPointer fShortHandPointer;
   protected XMLErrorReporter fErrorReporter;
   protected XMLErrorHandler fErrorHandler;
   private SymbolTable fSymbolTable;

   public ElementSchemePointer() {
   }

   public ElementSchemePointer(SymbolTable var1) {
      this.fSymbolTable = var1;
   }

   public ElementSchemePointer(SymbolTable var1, XMLErrorReporter var2) {
      this.fSymbolTable = var1;
      this.fErrorReporter = var2;
   }

   public void parseXPointer(String var1) throws XNIException {
      this.init();
      Tokens var2 = new Tokens(this.fSymbolTable);
      Scanner var3 = new Scanner(this.fSymbolTable) {
         protected void addToken(Tokens var1, int var2) throws XNIException {
            if (var2 != 1 && var2 != 0) {
               ElementSchemePointer.this.reportError("InvalidElementSchemeToken", new Object[]{var1.getTokenString(var2)});
            } else {
               super.addToken(var1, var2);
            }
         }
      };
      int var4 = var1.length();
      boolean var5 = var3.scanExpr(this.fSymbolTable, var2, var1, 0, var4);
      if (!var5) {
         this.reportError("InvalidElementSchemeXPointer", new Object[]{var1});
      }

      int[] var6 = new int[var2.getTokenCount() / 2 + 1];
      int var7 = 0;

      while(var2.hasMore()) {
         int var8 = var2.nextToken();
         switch (var8) {
            case 0:
               var8 = var2.nextToken();
               this.fShortHandPointerName = var2.getTokenString(var8);
               this.fShortHandPointer = new ShortHandPointer(this.fSymbolTable);
               this.fShortHandPointer.setSchemeName(this.fShortHandPointerName);
               break;
            case 1:
               var6[var7] = var2.nextToken();
               ++var7;
               break;
            default:
               this.reportError("InvalidElementSchemeXPointer", new Object[]{var1});
         }
      }

      this.fChildSequence = new int[var7];
      this.fCurrentChildSequence = new int[var7];
      System.arraycopy(var6, 0, this.fChildSequence, 0, var7);
   }

   public String getSchemeName() {
      return this.fSchemeName;
   }

   public String getSchemeData() {
      return this.fSchemeData;
   }

   public void setSchemeName(String var1) {
      this.fSchemeName = var1;
   }

   public void setSchemeData(String var1) {
      this.fSchemeData = var1;
   }

   public boolean resolveXPointer(QName var1, XMLAttributes var2, Augmentations var3, int var4) throws XNIException {
      boolean var5 = false;
      if (this.fShortHandPointerName != null) {
         var5 = this.fShortHandPointer.resolveXPointer(var1, var2, var3, var4);
         if (var5) {
            this.fIsResolveElement = true;
            this.fIsShortHand = true;
         } else {
            this.fIsResolveElement = false;
         }
      } else {
         this.fIsResolveElement = true;
      }

      if (this.fChildSequence.length > 0) {
         this.fIsFragmentResolved = this.matchChildSequence(var1, var4);
      } else if (var5 && this.fChildSequence.length <= 0) {
         this.fIsFragmentResolved = var5;
      } else {
         this.fIsFragmentResolved = false;
      }

      return this.fIsFragmentResolved;
   }

   protected boolean matchChildSequence(QName var1, int var2) throws XNIException {
      if (this.fCurrentChildDepth >= this.fCurrentChildSequence.length) {
         int[] var3 = new int[this.fCurrentChildSequence.length];
         System.arraycopy(this.fCurrentChildSequence, 0, var3, 0, this.fCurrentChildSequence.length);
         this.fCurrentChildSequence = new int[this.fCurrentChildDepth * 2];
         System.arraycopy(var3, 0, this.fCurrentChildSequence, 0, var3.length);
      }

      if (this.fIsResolveElement) {
         if (var2 == 0) {
            this.fCurrentChildSequence[this.fCurrentChildDepth] = this.fCurrentChildPosition;
            ++this.fCurrentChildDepth;
            this.fCurrentChildPosition = 1;
            if (this.fCurrentChildDepth <= this.fFoundDepth || this.fFoundDepth == 0) {
               if (this.checkMatch()) {
                  this.fIsElementFound = true;
                  this.fFoundDepth = this.fCurrentChildDepth;
               } else {
                  this.fIsElementFound = false;
                  this.fFoundDepth = 0;
               }
            }
         } else if (var2 == 1) {
            if (this.fCurrentChildDepth == this.fFoundDepth) {
               this.fIsElementFound = true;
            } else if (this.fCurrentChildDepth < this.fFoundDepth && this.fFoundDepth != 0 || this.fCurrentChildDepth > this.fFoundDepth && this.fFoundDepth == 0) {
               this.fIsElementFound = false;
            }

            this.fCurrentChildSequence[this.fCurrentChildDepth] = 0;
            --this.fCurrentChildDepth;
            this.fCurrentChildPosition = this.fCurrentChildSequence[this.fCurrentChildDepth] + 1;
         } else if (var2 == 2) {
            this.fCurrentChildSequence[this.fCurrentChildDepth] = this.fCurrentChildPosition++;
            if (this.checkMatch()) {
               this.fIsElementFound = true;
               this.fWasOnlyEmptyElementFound = true;
            } else {
               this.fIsElementFound = false;
            }
         }
      }

      return this.fIsElementFound;
   }

   protected boolean checkMatch() {
      int var1;
      if (!this.fIsShortHand) {
         if (this.fChildSequence.length > this.fCurrentChildDepth + 1) {
            return false;
         }

         for(var1 = 0; var1 < this.fChildSequence.length; ++var1) {
            if (this.fChildSequence[var1] != this.fCurrentChildSequence[var1]) {
               return false;
            }
         }
      } else {
         if (this.fChildSequence.length > this.fCurrentChildDepth + 1) {
            return false;
         }

         for(var1 = 0; var1 < this.fChildSequence.length; ++var1) {
            if (this.fCurrentChildSequence.length < var1 + 2) {
               return false;
            }

            if (this.fChildSequence[var1] != this.fCurrentChildSequence[var1 + 1]) {
               return false;
            }
         }
      }

      return true;
   }

   public boolean isFragmentResolved() throws XNIException {
      return this.fIsFragmentResolved;
   }

   public boolean isChildFragmentResolved() {
      if (this.fIsShortHand && this.fShortHandPointer != null && this.fChildSequence.length <= 0) {
         return this.fShortHandPointer.isChildFragmentResolved();
      } else {
         return this.fWasOnlyEmptyElementFound ? !this.fWasOnlyEmptyElementFound : this.fIsFragmentResolved && this.fCurrentChildDepth >= this.fFoundDepth;
      }
   }

   protected void reportError(String var1, Object[] var2) throws XNIException {
      throw new XNIException(this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/XPTR").formatMessage(this.fErrorReporter.getLocale(), var1, var2));
   }

   protected void initErrorReporter() {
      if (this.fErrorReporter == null) {
         this.fErrorReporter = new XMLErrorReporter();
      }

      if (this.fErrorHandler == null) {
         this.fErrorHandler = new XPointerErrorHandler();
      }

      this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/XPTR", new XPointerMessageFormatter());
   }

   protected void init() {
      this.fSchemeName = null;
      this.fSchemeData = null;
      this.fShortHandPointerName = null;
      this.fIsResolveElement = false;
      this.fIsElementFound = false;
      this.fWasOnlyEmptyElementFound = false;
      this.fFoundDepth = 0;
      this.fCurrentChildPosition = 1;
      this.fCurrentChildDepth = 0;
      this.fIsFragmentResolved = false;
      this.fShortHandPointer = null;
      this.initErrorReporter();
   }

   private class Scanner {
      private static final byte CHARTYPE_INVALID = 0;
      private static final byte CHARTYPE_OTHER = 1;
      private static final byte CHARTYPE_MINUS = 2;
      private static final byte CHARTYPE_PERIOD = 3;
      private static final byte CHARTYPE_SLASH = 4;
      private static final byte CHARTYPE_DIGIT = 5;
      private static final byte CHARTYPE_LETTER = 6;
      private static final byte CHARTYPE_UNDERSCORE = 7;
      private static final byte CHARTYPE_NONASCII = 8;
      private final byte[] fASCIICharMap;
      private SymbolTable fSymbolTable;

      private Scanner(SymbolTable var2) {
         this.fASCIICharMap = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 1, 1, 7, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 1, 1, 1};
         this.fSymbolTable = var2;
      }

      private boolean scanExpr(SymbolTable var1, Tokens var2, String var3, int var4, int var5) throws XNIException {
         String var8 = null;

         while(true) {
            while(var4 != var5) {
               char var6 = var3.charAt(var4);
               byte var9 = var6 >= 128 ? 8 : this.fASCIICharMap[var6];
               switch (var9) {
                  case 1:
                  case 2:
                  case 3:
                  case 5:
                  case 6:
                  case 7:
                  case 8:
                     int var7 = var4;
                     var4 = this.scanNCName(var3, var5, var4);
                     if (var4 == var7) {
                        ElementSchemePointer.this.reportError("InvalidNCNameInElementSchemeData", new Object[]{var3});
                        return false;
                     }

                     if (var4 < var5) {
                        var3.charAt(var4);
                     } else {
                        boolean var11 = true;
                     }

                     var8 = var1.addSymbol(var3.substring(var7, var4));
                     this.addToken(var2, 0);
                     var2.addToken(var8);
                     break;
                  case 4:
                     ++var4;
                     if (var4 == var5) {
                        return false;
                     }

                     this.addToken(var2, 1);
                     var6 = var3.charAt(var4);

                     int var10;
                     for(var10 = 0; var6 >= '0' && var6 <= '9'; var6 = var3.charAt(var4)) {
                        var10 = var10 * 10 + (var6 - 48);
                        ++var4;
                        if (var4 == var5) {
                           break;
                        }
                     }

                     if (var10 == 0) {
                        ElementSchemePointer.this.reportError("InvalidChildSequenceCharacter", new Object[]{new Character((char)var6)});
                        return false;
                     }

                     var2.addToken(var10);
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
            if (var5 != 6 && var5 != 7) {
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
               if (var5 != 6 && var5 != 5 && var5 != 3 && var5 != 2 && var5 != 7) {
                  break;
               }
            }
         }

         return var3;
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
      private static final int XPTRTOKEN_ELEM_NCNAME = 0;
      private static final int XPTRTOKEN_ELEM_CHILD = 1;
      private final String[] fgTokenNames;
      private static final int INITIAL_TOKEN_COUNT = 256;
      private int[] fTokens;
      private int fTokenCount;
      private int fCurrentTokenIndex;
      private SymbolTable fSymbolTable;
      private Hashtable fTokenNames;

      private Tokens(SymbolTable var2) {
         this.fgTokenNames = new String[]{"XPTRTOKEN_ELEM_NCNAME", "XPTRTOKEN_ELEM_CHILD"};
         this.fTokens = new int[256];
         this.fTokenCount = 0;
         this.fTokenNames = new Hashtable();
         this.fSymbolTable = var2;
         this.fTokenNames.put(new Integer(0), "XPTRTOKEN_ELEM_NCNAME");
         this.fTokenNames.put(new Integer(1), "XPTRTOKEN_ELEM_CHILD");
      }

      private String getTokenString(int var1) {
         return (String)this.fTokenNames.get(new Integer(var1));
      }

      private Integer getToken(int var1) {
         return (Integer)this.fTokenNames.get(new Integer(var1));
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
            ElementSchemePointer.this.reportError("XPointerElementSchemeProcessingError", (Object[])null);
         }

         return this.fTokens[this.fCurrentTokenIndex++];
      }

      private int peekToken() throws XNIException {
         if (this.fCurrentTokenIndex == this.fTokenCount) {
            ElementSchemePointer.this.reportError("XPointerElementSchemeProcessingError", (Object[])null);
         }

         return this.fTokens[this.fCurrentTokenIndex];
      }

      private String nextTokenAsString() throws XNIException {
         String var1 = this.getTokenString(this.nextToken());
         if (var1 == null) {
            ElementSchemePointer.this.reportError("XPointerElementSchemeProcessingError", (Object[])null);
         }

         return var1;
      }

      private int getTokenCount() {
         return this.fTokenCount;
      }

      // $FF: synthetic method
      Tokens(SymbolTable var2, Object var3) {
         this(var2);
      }
   }
}
