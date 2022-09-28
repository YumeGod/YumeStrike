package org.apache.xerces.impl.xpath.regex;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

class RegexParser {
   static final int T_CHAR = 0;
   static final int T_EOF = 1;
   static final int T_OR = 2;
   static final int T_STAR = 3;
   static final int T_PLUS = 4;
   static final int T_QUESTION = 5;
   static final int T_LPAREN = 6;
   static final int T_RPAREN = 7;
   static final int T_DOT = 8;
   static final int T_LBRACKET = 9;
   static final int T_BACKSOLIDUS = 10;
   static final int T_CARET = 11;
   static final int T_DOLLAR = 12;
   static final int T_LPAREN2 = 13;
   static final int T_LOOKAHEAD = 14;
   static final int T_NEGATIVELOOKAHEAD = 15;
   static final int T_LOOKBEHIND = 16;
   static final int T_NEGATIVELOOKBEHIND = 17;
   static final int T_INDEPENDENT = 18;
   static final int T_SET_OPERATIONS = 19;
   static final int T_POSIX_CHARCLASS_START = 20;
   static final int T_COMMENT = 21;
   static final int T_MODIFIERS = 22;
   static final int T_CONDITION = 23;
   static final int T_XMLSCHEMA_CC_SUBTRACTION = 24;
   int offset;
   String regex;
   int regexlen;
   int options;
   ResourceBundle resources;
   int chardata;
   int nexttoken;
   protected static final int S_NORMAL = 0;
   protected static final int S_INBRACKETS = 1;
   protected static final int S_INXBRACKETS = 2;
   int context = 0;
   int parennumber = 1;
   boolean hasBackReferences;
   Vector references = null;

   public RegexParser() {
      this.setLocale(Locale.getDefault());
   }

   public RegexParser(Locale var1) {
      this.setLocale(var1);
   }

   public void setLocale(Locale var1) {
      try {
         this.resources = ResourceBundle.getBundle("org.apache.xerces.impl.xpath.regex.message", var1);
      } catch (MissingResourceException var3) {
         throw new RuntimeException("Installation Problem???  Couldn't load messages: " + var3.getMessage());
      }
   }

   final ParseException ex(String var1, int var2) {
      return new ParseException(this.resources.getString(var1), var2);
   }

   private final boolean isSet(int var1) {
      return (this.options & var1) == var1;
   }

   synchronized Token parse(String var1, int var2) throws ParseException {
      this.options = var2;
      this.offset = 0;
      this.setContext(0);
      this.parennumber = 1;
      this.hasBackReferences = false;
      this.regex = var1;
      if (this.isSet(16)) {
         this.regex = REUtil.stripExtendedComment(this.regex);
      }

      this.regexlen = this.regex.length();
      this.next();
      Token var3 = this.parseRegex();
      if (this.offset != this.regexlen) {
         throw this.ex("parser.parse.1", this.offset);
      } else {
         if (this.references != null) {
            for(int var4 = 0; var4 < this.references.size(); ++var4) {
               ReferencePosition var5 = (ReferencePosition)this.references.elementAt(var4);
               if (this.parennumber <= var5.refNumber) {
                  throw this.ex("parser.parse.2", var5.position);
               }
            }

            this.references.removeAllElements();
         }

         return var3;
      }
   }

   protected final void setContext(int var1) {
      this.context = var1;
   }

   final int read() {
      return this.nexttoken;
   }

   final void next() {
      if (this.offset >= this.regexlen) {
         this.chardata = -1;
         this.nexttoken = 1;
      } else {
         char var2 = this.regex.charAt(this.offset++);
         this.chardata = var2;
         byte var1;
         if (this.context == 1) {
            switch (var2) {
               case '-':
                  if (this.isSet(512) && this.offset < this.regexlen && this.regex.charAt(this.offset) == '[') {
                     ++this.offset;
                     var1 = 24;
                  } else {
                     var1 = 0;
                  }
                  break;
               case '[':
                  if (!this.isSet(512) && this.offset < this.regexlen && this.regex.charAt(this.offset) == ':') {
                     ++this.offset;
                     var1 = 20;
                     break;
                  }
               default:
                  if (REUtil.isHighSurrogate(var2) && this.offset < this.regexlen) {
                     char var3 = this.regex.charAt(this.offset);
                     if (REUtil.isLowSurrogate(var3)) {
                        this.chardata = REUtil.composeFromSurrogates(var2, var3);
                        ++this.offset;
                     }
                  }

                  var1 = 0;
                  break;
               case '\\':
                  var1 = 10;
                  if (this.offset >= this.regexlen) {
                     throw this.ex("parser.next.1", this.offset - 1);
                  }

                  this.chardata = this.regex.charAt(this.offset++);
            }

            this.nexttoken = var1;
         } else {
            switch (var2) {
               case '$':
                  var1 = 12;
                  break;
               case '(':
                  var1 = 6;
                  if (this.offset < this.regexlen && this.regex.charAt(this.offset) == '?') {
                     if (++this.offset >= this.regexlen) {
                        throw this.ex("parser.next.2", this.offset - 1);
                     }

                     var2 = this.regex.charAt(this.offset++);
                     switch (var2) {
                        case '!':
                           var1 = 15;
                           break;
                        case '#':
                           while(this.offset < this.regexlen) {
                              var2 = this.regex.charAt(this.offset++);
                              if (var2 == ')') {
                                 break;
                              }
                           }

                           if (var2 != ')') {
                              throw this.ex("parser.next.4", this.offset - 1);
                           }

                           var1 = 21;
                           break;
                        case ':':
                           var1 = 13;
                           break;
                        case '<':
                           if (this.offset >= this.regexlen) {
                              throw this.ex("parser.next.2", this.offset - 3);
                           }

                           var2 = this.regex.charAt(this.offset++);
                           if (var2 == '=') {
                              var1 = 16;
                           } else {
                              if (var2 != '!') {
                                 throw this.ex("parser.next.3", this.offset - 3);
                              }

                              var1 = 17;
                           }
                           break;
                        case '=':
                           var1 = 14;
                           break;
                        case '>':
                           var1 = 18;
                           break;
                        case '[':
                           var1 = 19;
                           break;
                        default:
                           if (var2 != '-' && ('a' > var2 || var2 > 'z') && ('A' > var2 || var2 > 'Z')) {
                              if (var2 != '(') {
                                 throw this.ex("parser.next.2", this.offset - 2);
                              }

                              var1 = 23;
                           } else {
                              --this.offset;
                              var1 = 22;
                           }
                     }
                  }
                  break;
               case ')':
                  var1 = 7;
                  break;
               case '*':
                  var1 = 3;
                  break;
               case '+':
                  var1 = 4;
                  break;
               case '.':
                  var1 = 8;
                  break;
               case '?':
                  var1 = 5;
                  break;
               case '[':
                  var1 = 9;
                  break;
               case '\\':
                  var1 = 10;
                  if (this.offset >= this.regexlen) {
                     throw this.ex("parser.next.1", this.offset - 1);
                  }

                  this.chardata = this.regex.charAt(this.offset++);
                  break;
               case '^':
                  var1 = 11;
                  break;
               case '|':
                  var1 = 2;
                  break;
               default:
                  var1 = 0;
            }

            this.nexttoken = var1;
         }
      }
   }

   Token parseRegex() throws ParseException {
      Object var1 = this.parseTerm();

      for(Token.UnionToken var2 = null; this.read() == 2; ((Token)var1).addChild(this.parseTerm())) {
         this.next();
         if (var2 == null) {
            var2 = Token.createUnion();
            var2.addChild((Token)var1);
            var1 = var2;
         }
      }

      return (Token)var1;
   }

   Token parseTerm() throws ParseException {
      int var1 = this.read();
      if (var1 != 2 && var1 != 7 && var1 != 1) {
         Object var2 = this.parseFactor();

         for(Token.UnionToken var3 = null; (var1 = this.read()) != 2 && var1 != 7 && var1 != 1; var3.addChild(this.parseFactor())) {
            if (var3 == null) {
               var3 = Token.createConcat();
               var3.addChild((Token)var2);
               var2 = var3;
            }
         }

         return (Token)var2;
      } else {
         return Token.createEmpty();
      }
   }

   Token processCaret() throws ParseException {
      this.next();
      return Token.token_linebeginning;
   }

   Token processDollar() throws ParseException {
      this.next();
      return Token.token_lineend;
   }

   Token processLookahead() throws ParseException {
      this.next();
      Token.ParenToken var1 = Token.createLook(20, this.parseRegex());
      if (this.read() != 7) {
         throw this.ex("parser.factor.1", this.offset - 1);
      } else {
         this.next();
         return var1;
      }
   }

   Token processNegativelookahead() throws ParseException {
      this.next();
      Token.ParenToken var1 = Token.createLook(21, this.parseRegex());
      if (this.read() != 7) {
         throw this.ex("parser.factor.1", this.offset - 1);
      } else {
         this.next();
         return var1;
      }
   }

   Token processLookbehind() throws ParseException {
      this.next();
      Token.ParenToken var1 = Token.createLook(22, this.parseRegex());
      if (this.read() != 7) {
         throw this.ex("parser.factor.1", this.offset - 1);
      } else {
         this.next();
         return var1;
      }
   }

   Token processNegativelookbehind() throws ParseException {
      this.next();
      Token.ParenToken var1 = Token.createLook(23, this.parseRegex());
      if (this.read() != 7) {
         throw this.ex("parser.factor.1", this.offset - 1);
      } else {
         this.next();
         return var1;
      }
   }

   Token processBacksolidus_A() throws ParseException {
      this.next();
      return Token.token_stringbeginning;
   }

   Token processBacksolidus_Z() throws ParseException {
      this.next();
      return Token.token_stringend2;
   }

   Token processBacksolidus_z() throws ParseException {
      this.next();
      return Token.token_stringend;
   }

   Token processBacksolidus_b() throws ParseException {
      this.next();
      return Token.token_wordedge;
   }

   Token processBacksolidus_B() throws ParseException {
      this.next();
      return Token.token_not_wordedge;
   }

   Token processBacksolidus_lt() throws ParseException {
      this.next();
      return Token.token_wordbeginning;
   }

   Token processBacksolidus_gt() throws ParseException {
      this.next();
      return Token.token_wordend;
   }

   Token processStar(Token var1) throws ParseException {
      this.next();
      if (this.read() == 5) {
         this.next();
         return Token.createNGClosure(var1);
      } else {
         return Token.createClosure(var1);
      }
   }

   Token processPlus(Token var1) throws ParseException {
      this.next();
      if (this.read() == 5) {
         this.next();
         return Token.createConcat(var1, Token.createNGClosure(var1));
      } else {
         return Token.createConcat(var1, Token.createClosure(var1));
      }
   }

   Token processQuestion(Token var1) throws ParseException {
      this.next();
      Token.UnionToken var2 = Token.createUnion();
      if (this.read() == 5) {
         this.next();
         var2.addChild(Token.createEmpty());
         var2.addChild(var1);
      } else {
         var2.addChild(var1);
         var2.addChild(Token.createEmpty());
      }

      return var2;
   }

   boolean checkQuestion(int var1) {
      return var1 < this.regexlen && this.regex.charAt(var1) == '?';
   }

   Token processParen() throws ParseException {
      this.next();
      int var1 = this.parennumber++;
      Token.ParenToken var2 = Token.createParen(this.parseRegex(), var1);
      if (this.read() != 7) {
         throw this.ex("parser.factor.1", this.offset - 1);
      } else {
         this.next();
         return var2;
      }
   }

   Token processParen2() throws ParseException {
      this.next();
      Token.ParenToken var1 = Token.createParen(this.parseRegex(), 0);
      if (this.read() != 7) {
         throw this.ex("parser.factor.1", this.offset - 1);
      } else {
         this.next();
         return var1;
      }
   }

   Token processCondition() throws ParseException {
      if (this.offset + 1 >= this.regexlen) {
         throw this.ex("parser.factor.4", this.offset);
      } else {
         int var1 = -1;
         Token var2 = null;
         char var3 = this.regex.charAt(this.offset);
         if ('1' <= var3 && var3 <= '9') {
            var1 = var3 - 48;
            this.hasBackReferences = true;
            if (this.references == null) {
               this.references = new Vector();
            }

            this.references.addElement(new ReferencePosition(var1, this.offset));
            ++this.offset;
            if (this.regex.charAt(this.offset) != ')') {
               throw this.ex("parser.factor.1", this.offset);
            }

            ++this.offset;
         } else {
            if (var3 == '?') {
               --this.offset;
            }

            this.next();
            var2 = this.parseFactor();
            switch (var2.type) {
               case 8:
                  if (this.read() != 7) {
                     throw this.ex("parser.factor.1", this.offset - 1);
                  }
               case 20:
               case 21:
               case 22:
               case 23:
                  break;
               default:
                  throw this.ex("parser.factor.5", this.offset);
            }
         }

         this.next();
         Token var4 = this.parseRegex();
         Token var5 = null;
         if (var4.type == 2) {
            if (var4.size() != 2) {
               throw this.ex("parser.factor.6", this.offset);
            }

            var5 = var4.getChild(1);
            var4 = var4.getChild(0);
         }

         if (this.read() != 7) {
            throw this.ex("parser.factor.1", this.offset - 1);
         } else {
            this.next();
            return Token.createCondition(var1, var2, var4, var5);
         }
      }
   }

   Token processModifiers() throws ParseException {
      int var1 = 0;
      int var2 = 0;

      int var3;
      int var4;
      for(var3 = -1; this.offset < this.regexlen; ++this.offset) {
         var3 = this.regex.charAt(this.offset);
         var4 = REUtil.getOptionValue(var3);
         if (var4 == 0) {
            break;
         }

         var1 |= var4;
      }

      if (this.offset >= this.regexlen) {
         throw this.ex("parser.factor.2", this.offset - 1);
      } else {
         if (var3 == 45) {
            ++this.offset;

            while(this.offset < this.regexlen) {
               var3 = this.regex.charAt(this.offset);
               var4 = REUtil.getOptionValue(var3);
               if (var4 == 0) {
                  break;
               }

               var2 |= var4;
               ++this.offset;
            }

            if (this.offset >= this.regexlen) {
               throw this.ex("parser.factor.2", this.offset - 1);
            }
         }

         Token.ModifierToken var5;
         if (var3 == 58) {
            ++this.offset;
            this.next();
            var5 = Token.createModifierGroup(this.parseRegex(), var1, var2);
            if (this.read() != 7) {
               throw this.ex("parser.factor.1", this.offset - 1);
            }

            this.next();
         } else {
            if (var3 != 41) {
               throw this.ex("parser.factor.3", this.offset);
            }

            ++this.offset;
            this.next();
            var5 = Token.createModifierGroup(this.parseRegex(), var1, var2);
         }

         return var5;
      }
   }

   Token processIndependent() throws ParseException {
      this.next();
      Token.ParenToken var1 = Token.createLook(24, this.parseRegex());
      if (this.read() != 7) {
         throw this.ex("parser.factor.1", this.offset - 1);
      } else {
         this.next();
         return var1;
      }
   }

   Token processBacksolidus_c() throws ParseException {
      char var1;
      if (this.offset < this.regexlen && ((var1 = this.regex.charAt(this.offset++)) & '￠') == 64) {
         this.next();
         return Token.createChar(var1 - 64);
      } else {
         throw this.ex("parser.atom.1", this.offset - 1);
      }
   }

   Token processBacksolidus_C() throws ParseException {
      throw this.ex("parser.process.1", this.offset);
   }

   Token processBacksolidus_i() throws ParseException {
      Token.CharToken var1 = Token.createChar(105);
      this.next();
      return var1;
   }

   Token processBacksolidus_I() throws ParseException {
      throw this.ex("parser.process.1", this.offset);
   }

   Token processBacksolidus_g() throws ParseException {
      this.next();
      return Token.getGraphemePattern();
   }

   Token processBacksolidus_X() throws ParseException {
      this.next();
      return Token.getCombiningCharacterSequence();
   }

   Token processBackreference() throws ParseException {
      int var1 = this.chardata - 48;
      Token.StringToken var2 = Token.createBackReference(var1);
      this.hasBackReferences = true;
      if (this.references == null) {
         this.references = new Vector();
      }

      this.references.addElement(new ReferencePosition(var1, this.offset - 2));
      this.next();
      return var2;
   }

   Token parseFactor() throws ParseException {
      int var1;
      var1 = this.read();
      label68:
      switch (var1) {
         case 10:
            switch (this.chardata) {
               case 60:
                  return this.processBacksolidus_lt();
               case 62:
                  return this.processBacksolidus_gt();
               case 65:
                  return this.processBacksolidus_A();
               case 66:
                  return this.processBacksolidus_B();
               case 90:
                  return this.processBacksolidus_Z();
               case 98:
                  return this.processBacksolidus_b();
               case 122:
                  return this.processBacksolidus_z();
               default:
                  break label68;
            }
         case 11:
            return this.processCaret();
         case 12:
            return this.processDollar();
         case 13:
         case 18:
         case 19:
         case 20:
         default:
            break;
         case 14:
            return this.processLookahead();
         case 15:
            return this.processNegativelookahead();
         case 16:
            return this.processLookbehind();
         case 17:
            return this.processNegativelookbehind();
         case 21:
            this.next();
            return Token.createEmpty();
      }

      Object var2 = this.parseAtom();
      var1 = this.read();
      switch (var1) {
         case 0:
            if (this.chardata == 123 && this.offset < this.regexlen) {
               int var3 = this.offset;
               boolean var4 = false;
               boolean var5 = true;
               char var6;
               if ((var6 = this.regex.charAt(var3++)) < '0' || var6 > '9') {
                  throw this.ex("parser.quantifier.1", this.offset);
               } else {
                  int var7 = var6 - 48;

                  while(var3 < this.regexlen && (var6 = this.regex.charAt(var3++)) >= '0' && var6 <= '9') {
                     var7 = var7 * 10 + var6 - 48;
                     if (var7 < 0) {
                        throw this.ex("parser.quantifier.5", this.offset);
                     }
                  }

                  int var8 = var7;
                  if (var6 == ',') {
                     if (var3 >= this.regexlen) {
                        throw this.ex("parser.quantifier.3", this.offset);
                     }

                     if ((var6 = this.regex.charAt(var3++)) >= '0' && var6 <= '9') {
                        var8 = var6 - 48;

                        while(var3 < this.regexlen && (var6 = this.regex.charAt(var3++)) >= '0' && var6 <= '9') {
                           var8 = var8 * 10 + var6 - 48;
                           if (var8 < 0) {
                              throw this.ex("parser.quantifier.5", this.offset);
                           }
                        }

                        if (var7 > var8) {
                           throw this.ex("parser.quantifier.4", this.offset);
                        }
                     } else {
                        var8 = -1;
                     }
                  }

                  if (var6 != '}') {
                     throw this.ex("parser.quantifier.2", this.offset);
                  } else {
                     if (this.checkQuestion(var3)) {
                        var2 = Token.createNGClosure((Token)var2);
                        this.offset = var3 + 1;
                     } else {
                        var2 = Token.createClosure((Token)var2);
                        this.offset = var3;
                     }

                     ((Token)var2).setMin(var7);
                     ((Token)var2).setMax(var8);
                     this.next();
                  }
               }
            }
         case 1:
         case 2:
         default:
            return (Token)var2;
         case 3:
            return this.processStar((Token)var2);
         case 4:
            return this.processPlus((Token)var2);
         case 5:
            return this.processQuestion((Token)var2);
      }
   }

   Token parseAtom() throws ParseException {
      int var1 = this.read();
      Object var2 = null;
      switch (var1) {
         case 0:
            if (this.chardata != 93 && this.chardata != 123 && this.chardata != 125) {
               var2 = Token.createChar(this.chardata);
               int var4 = this.chardata;
               this.next();
               if (REUtil.isHighSurrogate(var4) && this.read() == 0 && REUtil.isLowSurrogate(this.chardata)) {
                  char[] var5 = new char[]{(char)var4, (char)this.chardata};
                  var2 = Token.createParen(Token.createString(new String(var5)), 0);
                  this.next();
               }
               break;
            }

            throw this.ex("parser.atom.4", this.offset - 1);
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 7:
         case 11:
         case 12:
         case 14:
         case 15:
         case 16:
         case 17:
         case 20:
         case 21:
         default:
            throw this.ex("parser.atom.4", this.offset - 1);
         case 6:
            return this.processParen();
         case 8:
            this.next();
            var2 = Token.token_dot;
            break;
         case 9:
            return this.parseCharacterClass(true);
         case 10:
            int var3;
            switch (this.chardata) {
               case 49:
               case 50:
               case 51:
               case 52:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
                  return this.processBackreference();
               case 58:
               case 59:
               case 60:
               case 61:
               case 62:
               case 63:
               case 64:
               case 65:
               case 66:
               case 69:
               case 70:
               case 71:
               case 72:
               case 74:
               case 75:
               case 76:
               case 77:
               case 78:
               case 79:
               case 81:
               case 82:
               case 84:
               case 85:
               case 86:
               case 89:
               case 90:
               case 91:
               case 92:
               case 93:
               case 94:
               case 95:
               case 96:
               case 97:
               case 98:
               case 104:
               case 106:
               case 107:
               case 108:
               case 109:
               case 111:
               case 113:
               default:
                  var2 = Token.createChar(this.chardata);
                  break;
               case 67:
                  return this.processBacksolidus_C();
               case 68:
               case 83:
               case 87:
               case 100:
               case 115:
               case 119:
                  Token var6 = this.getTokenForShorthand(this.chardata);
                  this.next();
                  return var6;
               case 73:
                  return this.processBacksolidus_I();
               case 80:
               case 112:
                  var3 = this.offset;
                  var2 = this.processBacksolidus_pP(this.chardata);
                  if (var2 == null) {
                     throw this.ex("parser.atom.5", var3);
                  }
                  break;
               case 88:
                  return this.processBacksolidus_X();
               case 99:
                  return this.processBacksolidus_c();
               case 101:
               case 102:
               case 110:
               case 114:
               case 116:
               case 117:
               case 118:
               case 120:
                  var3 = this.decodeEscaped();
                  if (var3 < 65536) {
                     var2 = Token.createChar(var3);
                  } else {
                     var2 = Token.createString(REUtil.decomposeToSurrogates(var3));
                  }
                  break;
               case 103:
                  return this.processBacksolidus_g();
               case 105:
                  return this.processBacksolidus_i();
            }

            this.next();
            break;
         case 13:
            return this.processParen2();
         case 18:
            return this.processIndependent();
         case 19:
            return this.parseSetOperations();
         case 22:
            return this.processModifiers();
         case 23:
            return this.processCondition();
      }

      return (Token)var2;
   }

   protected RangeToken processBacksolidus_pP(int var1) throws ParseException {
      this.next();
      if (this.read() == 0 && this.chardata == 123) {
         boolean var2 = var1 == 112;
         int var3 = this.offset;
         int var4 = this.regex.indexOf(125, var3);
         if (var4 < 0) {
            throw this.ex("parser.atom.3", this.offset);
         } else {
            String var5 = this.regex.substring(var3, var4);
            this.offset = var4 + 1;
            return Token.getRange(var5, var2, this.isSet(512));
         }
      } else {
         throw this.ex("parser.atom.2", this.offset - 1);
      }
   }

   int processCIinCharacterClass(RangeToken var1, int var2) {
      return this.decodeEscaped();
   }

   protected RangeToken parseCharacterClass(boolean var1) throws ParseException {
      this.setContext(1);
      this.next();
      boolean var2 = false;
      RangeToken var3 = null;
      RangeToken var4;
      if (this.read() == 0 && this.chardata == 94) {
         var2 = true;
         this.next();
         if (var1) {
            var4 = Token.createNRange();
         } else {
            var3 = Token.createRange();
            var3.addRange(0, 1114111);
            var4 = Token.createRange();
         }
      } else {
         var4 = Token.createRange();
      }

      boolean var6 = true;

      int var5;
      while((var5 = this.read()) != 1 && (var5 != 0 || this.chardata != 93 || var6)) {
         var6 = false;
         int var7 = this.chardata;
         boolean var8 = false;
         int var9;
         if (var5 == 10) {
            switch (var7) {
               case 67:
               case 73:
               case 99:
               case 105:
                  var7 = this.processCIinCharacterClass(var4, var7);
                  if (var7 < 0) {
                     var8 = true;
                  }
                  break;
               case 68:
               case 83:
               case 87:
               case 100:
               case 115:
               case 119:
                  var4.mergeRanges(this.getTokenForShorthand(var7));
                  var8 = true;
                  break;
               case 80:
               case 112:
                  var9 = this.offset;
                  RangeToken var13 = this.processBacksolidus_pP(var7);
                  if (var13 == null) {
                     throw this.ex("parser.atom.5", var9);
                  }

                  var4.mergeRanges(var13);
                  var8 = true;
                  break;
               default:
                  var7 = this.decodeEscaped();
            }
         } else if (var5 == 20) {
            var9 = this.regex.indexOf(58, this.offset);
            if (var9 < 0) {
               throw this.ex("parser.cc.1", this.offset);
            }

            boolean var10 = true;
            if (this.regex.charAt(this.offset) == '^') {
               ++this.offset;
               var10 = false;
            }

            String var11 = this.regex.substring(this.offset, var9);
            RangeToken var12 = Token.getRange(var11, var10, this.isSet(512));
            if (var12 == null) {
               throw this.ex("parser.cc.3", this.offset);
            }

            var4.mergeRanges(var12);
            var8 = true;
            if (var9 + 1 >= this.regexlen || this.regex.charAt(var9 + 1) != ']') {
               throw this.ex("parser.cc.1", var9);
            }

            this.offset = var9 + 2;
         }

         this.next();
         if (!var8) {
            if (this.read() == 0 && this.chardata == 45) {
               this.next();
               if ((var5 = this.read()) == 1) {
                  throw this.ex("parser.cc.2", this.offset);
               }

               if (var5 == 0 && this.chardata == 93) {
                  var4.addRange(var7, var7);
                  var4.addRange(45, 45);
               } else {
                  var9 = this.chardata;
                  if (var5 == 10) {
                     var9 = this.decodeEscaped();
                  }

                  this.next();
                  var4.addRange(var7, var9);
               }
            } else {
               var4.addRange(var7, var7);
            }
         }

         if (this.isSet(1024) && this.read() == 0 && this.chardata == 44) {
            this.next();
         }
      }

      if (this.read() == 1) {
         throw this.ex("parser.cc.2", this.offset);
      } else {
         if (!var1 && var2) {
            var3.subtractRanges(var4);
            var4 = var3;
         }

         var4.sortRanges();
         var4.compactRanges();
         this.setContext(0);
         this.next();
         return var4;
      }
   }

   protected RangeToken parseSetOperations() throws ParseException {
      RangeToken var1 = this.parseCharacterClass(false);

      int var2;
      while((var2 = this.read()) != 7) {
         int var3 = this.chardata;
         if ((var2 != 0 || var3 != 45 && var3 != 38) && var2 != 4) {
            throw this.ex("parser.ope.2", this.offset - 1);
         }

         this.next();
         if (this.read() != 9) {
            throw this.ex("parser.ope.1", this.offset - 1);
         }

         RangeToken var4 = this.parseCharacterClass(false);
         if (var2 == 4) {
            var1.mergeRanges(var4);
         } else if (var3 == 45) {
            var1.subtractRanges(var4);
         } else {
            if (var3 != 38) {
               throw new RuntimeException("ASSERT");
            }

            var1.intersectRanges(var4);
         }
      }

      this.next();
      return var1;
   }

   Token getTokenForShorthand(int var1) {
      Object var2;
      switch (var1) {
         case 68:
            var2 = this.isSet(32) ? Token.getRange("Nd", false) : Token.token_not_0to9;
            break;
         case 83:
            var2 = this.isSet(32) ? Token.getRange("IsSpace", false) : Token.token_not_spaces;
            break;
         case 87:
            var2 = this.isSet(32) ? Token.getRange("IsWord", false) : Token.token_not_wordchars;
            break;
         case 100:
            var2 = this.isSet(32) ? Token.getRange("Nd", true) : Token.token_0to9;
            break;
         case 115:
            var2 = this.isSet(32) ? Token.getRange("IsSpace", true) : Token.token_spaces;
            break;
         case 119:
            var2 = this.isSet(32) ? Token.getRange("IsWord", true) : Token.token_wordchars;
            break;
         default:
            throw new RuntimeException("Internal Error: shorthands: \\u" + Integer.toString(var1, 16));
      }

      return (Token)var2;
   }

   int decodeEscaped() throws ParseException {
      if (this.read() != 10) {
         throw this.ex("parser.next.1", this.offset - 1);
      } else {
         int var1 = this.chardata;
         boolean var2;
         int var3;
         int var4;
         switch (var1) {
            case 65:
            case 90:
            case 122:
               throw this.ex("parser.descape.5", this.offset - 2);
            case 101:
               var1 = 27;
               break;
            case 102:
               var1 = 12;
               break;
            case 110:
               var1 = 10;
               break;
            case 114:
               var1 = 13;
               break;
            case 116:
               var1 = 9;
               break;
            case 117:
               var2 = false;
               this.next();
               if (this.read() != 0 || (var4 = hexChar(this.chardata)) < 0) {
                  throw this.ex("parser.descape.1", this.offset - 1);
               }

               var3 = var4;
               this.next();
               if (this.read() == 0 && (var4 = hexChar(this.chardata)) >= 0) {
                  var3 = var3 * 16 + var4;
                  this.next();
                  if (this.read() == 0 && (var4 = hexChar(this.chardata)) >= 0) {
                     var3 = var3 * 16 + var4;
                     this.next();
                     if (this.read() == 0 && (var4 = hexChar(this.chardata)) >= 0) {
                        var3 = var3 * 16 + var4;
                        var1 = var3;
                        break;
                     }

                     throw this.ex("parser.descape.1", this.offset - 1);
                  }

                  throw this.ex("parser.descape.1", this.offset - 1);
               }

               throw this.ex("parser.descape.1", this.offset - 1);
            case 118:
               this.next();
               if (this.read() == 0 && (var4 = hexChar(this.chardata)) >= 0) {
                  var3 = var4;
                  this.next();
                  if (this.read() == 0 && (var4 = hexChar(this.chardata)) >= 0) {
                     var3 = var3 * 16 + var4;
                     this.next();
                     if (this.read() == 0 && (var4 = hexChar(this.chardata)) >= 0) {
                        var3 = var3 * 16 + var4;
                        this.next();
                        if (this.read() == 0 && (var4 = hexChar(this.chardata)) >= 0) {
                           var3 = var3 * 16 + var4;
                           this.next();
                           if (this.read() == 0 && (var4 = hexChar(this.chardata)) >= 0) {
                              var3 = var3 * 16 + var4;
                              this.next();
                              if (this.read() == 0 && (var4 = hexChar(this.chardata)) >= 0) {
                                 var3 = var3 * 16 + var4;
                                 if (var3 > 1114111) {
                                    throw this.ex("parser.descappe.4", this.offset - 1);
                                 }

                                 var1 = var3;
                                 break;
                              }

                              throw this.ex("parser.descape.1", this.offset - 1);
                           }

                           throw this.ex("parser.descape.1", this.offset - 1);
                        }

                        throw this.ex("parser.descape.1", this.offset - 1);
                     }

                     throw this.ex("parser.descape.1", this.offset - 1);
                  }

                  throw this.ex("parser.descape.1", this.offset - 1);
               }

               throw this.ex("parser.descape.1", this.offset - 1);
            case 120:
               this.next();
               if (this.read() != 0) {
                  throw this.ex("parser.descape.1", this.offset - 1);
               }

               if (this.chardata == 123) {
                  var2 = false;
                  var3 = 0;

                  while(true) {
                     this.next();
                     if (this.read() != 0) {
                        throw this.ex("parser.descape.1", this.offset - 1);
                     }

                     if ((var4 = hexChar(this.chardata)) < 0) {
                        if (this.chardata != 125) {
                           throw this.ex("parser.descape.3", this.offset - 1);
                        }

                        if (var3 > 1114111) {
                           throw this.ex("parser.descape.4", this.offset - 1);
                        }

                        var1 = var3;
                        break;
                     }

                     if (var3 > var3 * 16) {
                        throw this.ex("parser.descape.2", this.offset - 1);
                     }

                     var3 = var3 * 16 + var4;
                  }
               } else {
                  var2 = false;
                  if (this.read() == 0 && (var4 = hexChar(this.chardata)) >= 0) {
                     var3 = var4;
                     this.next();
                     if (this.read() == 0 && (var4 = hexChar(this.chardata)) >= 0) {
                        var3 = var3 * 16 + var4;
                        var1 = var3;
                        return var1;
                     }

                     throw this.ex("parser.descape.1", this.offset - 1);
                  }

                  throw this.ex("parser.descape.1", this.offset - 1);
               }
         }

         return var1;
      }
   }

   private static final int hexChar(int var0) {
      if (var0 < 48) {
         return -1;
      } else if (var0 > 102) {
         return -1;
      } else if (var0 <= 57) {
         return var0 - 48;
      } else if (var0 < 65) {
         return -1;
      } else if (var0 <= 70) {
         return var0 - 65 + 10;
      } else {
         return var0 < 97 ? -1 : var0 - 97 + 10;
      }
   }

   static class ReferencePosition {
      int refNumber;
      int position;

      ReferencePosition(int var1, int var2) {
         this.refNumber = var1;
         this.position = var2;
      }
   }
}
