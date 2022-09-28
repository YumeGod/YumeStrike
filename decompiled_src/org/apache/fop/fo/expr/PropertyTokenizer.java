package org.apache.fop.fo.expr;

class PropertyTokenizer {
   static final int TOK_EOF = 0;
   static final int TOK_NCNAME = 1;
   static final int TOK_MULTIPLY = 2;
   static final int TOK_LPAR = 3;
   static final int TOK_RPAR = 4;
   static final int TOK_LITERAL = 5;
   static final int TOK_NUMBER = 6;
   static final int TOK_FUNCTION_LPAR = 7;
   static final int TOK_PLUS = 8;
   static final int TOK_MINUS = 9;
   static final int TOK_MOD = 10;
   static final int TOK_DIV = 11;
   static final int TOK_NUMERIC = 12;
   static final int TOK_COMMA = 13;
   static final int TOK_PERCENT = 14;
   static final int TOK_COLORSPEC = 15;
   static final int TOK_FLOAT = 16;
   static final int TOK_INTEGER = 17;
   protected int currentToken = 0;
   protected String currentTokenValue = null;
   protected int currentUnitLength = 0;
   private int currentTokenStartIndex = 0;
   private String expr;
   private int exprIndex = 0;
   private int exprLength;
   private boolean recognizeOperator = false;
   private static final String NAME_START_CHARS = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
   private static final String NAME_CHARS = ".-0123456789";
   private static final String DIGITS = "0123456789";
   private static final String HEX_CHARS = "0123456789abcdefABCDEF";

   PropertyTokenizer(String s) {
      this.expr = s;
      this.exprLength = s.length();
   }

   void next() throws PropertyException {
      this.currentTokenValue = null;
      this.currentTokenStartIndex = this.exprIndex;
      boolean currentMaybeOperator = this.recognizeOperator;
      this.recognizeOperator = true;

      while(this.exprIndex < this.exprLength) {
         char c = this.expr.charAt(this.exprIndex++);
         switch (c) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               this.currentTokenStartIndex = this.exprIndex;
               break;
            case '\u000b':
            case '\f':
            case '\u000e':
            case '\u000f':
            case '\u0010':
            case '\u0011':
            case '\u0012':
            case '\u0013':
            case '\u0014':
            case '\u0015':
            case '\u0016':
            case '\u0017':
            case '\u0018':
            case '\u0019':
            case '\u001a':
            case '\u001b':
            case '\u001c':
            case '\u001d':
            case '\u001e':
            case '\u001f':
            case '!':
            case '$':
            case '%':
            case '&':
            case '/':
            default:
               --this.exprIndex;
               this.scanName();
               if (this.exprIndex == this.currentTokenStartIndex) {
                  throw new PropertyException("illegal character");
               }

               this.currentTokenValue = this.expr.substring(this.currentTokenStartIndex, this.exprIndex);
               if (this.currentTokenValue.equals("mod")) {
                  this.currentToken = 10;
                  return;
               }

               if (this.currentTokenValue.equals("div")) {
                  this.currentToken = 11;
                  return;
               }

               if (this.followingParen()) {
                  this.currentToken = 7;
                  this.recognizeOperator = false;
               } else {
                  this.currentToken = 1;
                  this.recognizeOperator = false;
               }

               return;
            case '"':
            case '\'':
               this.exprIndex = this.expr.indexOf(c, this.exprIndex);
               if (this.exprIndex < 0) {
                  this.exprIndex = this.currentTokenStartIndex + 1;
                  throw new PropertyException("missing quote");
               }

               this.currentTokenValue = this.expr.substring(this.currentTokenStartIndex + 1, this.exprIndex++);
               this.currentToken = 5;
               return;
            case '#':
               this.nextColor();
               return;
            case '(':
               this.currentToken = 3;
               this.recognizeOperator = false;
               return;
            case ')':
               this.currentToken = 4;
               return;
            case '*':
               this.currentToken = 2;
               return;
            case '+':
               this.recognizeOperator = false;
               this.currentToken = 8;
               return;
            case ',':
               this.recognizeOperator = false;
               this.currentToken = 13;
               return;
            case '-':
               this.recognizeOperator = false;
               this.currentToken = 9;
               return;
            case '.':
               this.nextDecimalPoint();
               return;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
               this.scanDigits();
               boolean bSawDecimal;
               if (this.exprIndex < this.exprLength && this.expr.charAt(this.exprIndex) == '.') {
                  ++this.exprIndex;
                  bSawDecimal = true;
                  if (this.exprIndex < this.exprLength && isDigit(this.expr.charAt(this.exprIndex))) {
                     ++this.exprIndex;
                     this.scanDigits();
                  }
               } else {
                  bSawDecimal = false;
               }

               if (this.exprIndex < this.exprLength && this.expr.charAt(this.exprIndex) == '%') {
                  ++this.exprIndex;
                  this.currentToken = 14;
               } else {
                  this.currentUnitLength = this.exprIndex;
                  this.scanName();
                  this.currentUnitLength = this.exprIndex - this.currentUnitLength;
                  this.currentToken = this.currentUnitLength > 0 ? 12 : (bSawDecimal ? 16 : 17);
               }

               this.currentTokenValue = this.expr.substring(this.currentTokenStartIndex, this.exprIndex);
               return;
         }
      }

      this.currentToken = 0;
   }

   private void nextDecimalPoint() throws PropertyException {
      if (this.exprIndex < this.exprLength && isDigit(this.expr.charAt(this.exprIndex))) {
         ++this.exprIndex;
         this.scanDigits();
         if (this.exprIndex < this.exprLength && this.expr.charAt(this.exprIndex) == '%') {
            ++this.exprIndex;
            this.currentToken = 14;
         } else {
            this.currentUnitLength = this.exprIndex;
            this.scanName();
            this.currentUnitLength = this.exprIndex - this.currentUnitLength;
            this.currentToken = this.currentUnitLength > 0 ? 12 : 16;
         }

         this.currentTokenValue = this.expr.substring(this.currentTokenStartIndex, this.exprIndex);
      } else {
         throw new PropertyException("illegal character '.'");
      }
   }

   private void nextColor() throws PropertyException {
      if (this.exprIndex < this.exprLength && isHexDigit(this.expr.charAt(this.exprIndex))) {
         ++this.exprIndex;
         this.scanHexDigits();
         int len = this.exprIndex - this.currentTokenStartIndex - 1;
         if (len % 3 == 0) {
            this.currentToken = 15;
         } else {
            this.scanRestOfName();
            this.currentToken = 1;
         }

         this.currentTokenValue = this.expr.substring(this.currentTokenStartIndex, this.exprIndex);
      } else {
         throw new PropertyException("illegal character '#'");
      }
   }

   private void scanName() {
      if (this.exprIndex < this.exprLength && isNameStartChar(this.expr.charAt(this.exprIndex))) {
         this.scanRestOfName();
      }

   }

   private void scanRestOfName() {
      while(++this.exprIndex < this.exprLength && isNameChar(this.expr.charAt(this.exprIndex))) {
      }

   }

   private void scanDigits() {
      while(this.exprIndex < this.exprLength && isDigit(this.expr.charAt(this.exprIndex))) {
         ++this.exprIndex;
      }

   }

   private void scanHexDigits() {
      while(this.exprIndex < this.exprLength && isHexDigit(this.expr.charAt(this.exprIndex))) {
         ++this.exprIndex;
      }

   }

   private boolean followingParen() {
      int i = this.exprIndex;

      while(i < this.exprLength) {
         switch (this.expr.charAt(i)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               ++i;
               break;
            case '(':
               this.exprIndex = i + 1;
               return true;
            default:
               return false;
         }
      }

      return false;
   }

   private static final boolean isDigit(char c) {
      return "0123456789".indexOf(c) >= 0;
   }

   private static final boolean isHexDigit(char c) {
      return "0123456789abcdefABCDEF".indexOf(c) >= 0;
   }

   private static final boolean isSpace(char c) {
      switch (c) {
         case '\t':
         case '\n':
         case '\r':
         case ' ':
            return true;
         default:
            return false;
      }
   }

   private static final boolean isNameStartChar(char c) {
      return "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) >= 0 || c >= 128;
   }

   private static final boolean isNameChar(char c) {
      return "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) >= 0 || ".-0123456789".indexOf(c) >= 0 || c >= 128;
   }
}
