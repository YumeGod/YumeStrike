package org.apache.xerces.impl.xpath.regex;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

class Token implements Serializable {
   private static final long serialVersionUID = 4049923761862293040L;
   static final boolean COUNTTOKENS = true;
   static int tokens = 0;
   static final int CHAR = 0;
   static final int DOT = 11;
   static final int CONCAT = 1;
   static final int UNION = 2;
   static final int CLOSURE = 3;
   static final int RANGE = 4;
   static final int NRANGE = 5;
   static final int PAREN = 6;
   static final int EMPTY = 7;
   static final int ANCHOR = 8;
   static final int NONGREEDYCLOSURE = 9;
   static final int STRING = 10;
   static final int BACKREFERENCE = 12;
   static final int LOOKAHEAD = 20;
   static final int NEGATIVELOOKAHEAD = 21;
   static final int LOOKBEHIND = 22;
   static final int NEGATIVELOOKBEHIND = 23;
   static final int INDEPENDENT = 24;
   static final int MODIFIERGROUP = 25;
   static final int CONDITION = 26;
   static final int UTF16_MAX = 1114111;
   int type;
   static Token token_dot = new Token(11);
   static Token token_0to9 = createRange();
   static Token token_wordchars;
   static Token token_not_0to9;
   static Token token_not_wordchars;
   static Token token_spaces;
   static Token token_not_spaces;
   static Token token_empty = new Token(7);
   static Token token_linebeginning = createAnchor(94);
   static Token token_linebeginning2 = createAnchor(64);
   static Token token_lineend = createAnchor(36);
   static Token token_stringbeginning = createAnchor(65);
   static Token token_stringend = createAnchor(122);
   static Token token_stringend2 = createAnchor(90);
   static Token token_wordedge = createAnchor(98);
   static Token token_not_wordedge = createAnchor(66);
   static Token token_wordbeginning = createAnchor(60);
   static Token token_wordend = createAnchor(62);
   static final int FC_CONTINUE = 0;
   static final int FC_TERMINAL = 1;
   static final int FC_ANY = 2;
   private static final Hashtable categories;
   private static final Hashtable categories2;
   private static final String[] categoryNames;
   static final int CHAR_INIT_QUOTE = 29;
   static final int CHAR_FINAL_QUOTE = 30;
   static final int CHAR_LETTER = 31;
   static final int CHAR_MARK = 32;
   static final int CHAR_NUMBER = 33;
   static final int CHAR_SEPARATOR = 34;
   static final int CHAR_OTHER = 35;
   static final int CHAR_PUNCTUATION = 36;
   static final int CHAR_SYMBOL = 37;
   private static final String[] blockNames;
   static final String blockRanges = "\u0000\u007f\u0080ÿĀſƀɏɐʯʰ˿̀ͯͰϿЀӿ\u0530֏\u0590\u05ff\u0600ۿ܀ݏހ\u07bfऀॿঀ\u09ff\u0a00\u0a7f\u0a80૿\u0b00\u0b7f\u0b80\u0bffఀ౿ಀ\u0cffഀൿ\u0d80\u0dff\u0e00\u0e7f\u0e80\u0effༀ\u0fffက႟Ⴀჿᄀᇿሀ\u137fᎠ\u13ff᐀ᙿ \u169fᚠ\u16ffក\u17ff᠀\u18afḀỿἀ\u1fff \u206f⁰\u209f₠\u20cf⃐\u20ff℀⅏⅐\u218f←⇿∀⋿⌀⏿␀\u243f⑀\u245f①⓿─╿▀▟■◿☀⛿✀➿⠀⣿⺀\u2eff⼀\u2fdf⿰\u2fff　〿\u3040ゟ゠ヿ\u3100ㄯ\u3130\u318f㆐㆟ㆠㆿ㈀㋿㌀㏿㐀䶵一\u9fffꀀ\ua48f꒐\ua4cf가힣\ue000\uf8ff豈\ufaffﬀﭏﭐ\ufdff︠︯︰﹏﹐\ufe6fﹰ\ufefe\ufeff\ufeff\uff00\uffef";
   static final int[] nonBMPBlockRanges;
   private static final int NONBMP_BLOCK_START = 84;
   static Hashtable nonxs;
   static final String viramaString = "्্੍્୍்్್്ฺ྄";
   private static Token token_grapheme;
   private static Token token_ccs;

   static ParenToken createLook(int var0, Token var1) {
      ++tokens;
      return new ParenToken(var0, var1, 0);
   }

   static ParenToken createParen(Token var0, int var1) {
      ++tokens;
      return new ParenToken(6, var0, var1);
   }

   static ClosureToken createClosure(Token var0) {
      ++tokens;
      return new ClosureToken(3, var0);
   }

   static ClosureToken createNGClosure(Token var0) {
      ++tokens;
      return new ClosureToken(9, var0);
   }

   static ConcatToken createConcat(Token var0, Token var1) {
      ++tokens;
      return new ConcatToken(var0, var1);
   }

   static UnionToken createConcat() {
      ++tokens;
      return new UnionToken(1);
   }

   static UnionToken createUnion() {
      ++tokens;
      return new UnionToken(2);
   }

   static Token createEmpty() {
      return token_empty;
   }

   static RangeToken createRange() {
      ++tokens;
      return new RangeToken(4);
   }

   static RangeToken createNRange() {
      ++tokens;
      return new RangeToken(5);
   }

   static CharToken createChar(int var0) {
      ++tokens;
      return new CharToken(0, var0);
   }

   private static CharToken createAnchor(int var0) {
      ++tokens;
      return new CharToken(8, var0);
   }

   static StringToken createBackReference(int var0) {
      ++tokens;
      return new StringToken(12, (String)null, var0);
   }

   static StringToken createString(String var0) {
      ++tokens;
      return new StringToken(10, var0, 0);
   }

   static ModifierToken createModifierGroup(Token var0, int var1, int var2) {
      ++tokens;
      return new ModifierToken(var0, var1, var2);
   }

   static ConditionToken createCondition(int var0, Token var1, Token var2, Token var3) {
      ++tokens;
      return new ConditionToken(var0, var1, var2, var3);
   }

   protected Token(int var1) {
      this.type = var1;
   }

   int size() {
      return 0;
   }

   Token getChild(int var1) {
      return null;
   }

   void addChild(Token var1) {
      throw new RuntimeException("Not supported.");
   }

   protected void addRange(int var1, int var2) {
      throw new RuntimeException("Not supported.");
   }

   protected void sortRanges() {
      throw new RuntimeException("Not supported.");
   }

   protected void compactRanges() {
      throw new RuntimeException("Not supported.");
   }

   protected void mergeRanges(Token var1) {
      throw new RuntimeException("Not supported.");
   }

   protected void subtractRanges(Token var1) {
      throw new RuntimeException("Not supported.");
   }

   protected void intersectRanges(Token var1) {
      throw new RuntimeException("Not supported.");
   }

   static Token complementRanges(Token var0) {
      return RangeToken.complementRanges(var0);
   }

   void setMin(int var1) {
   }

   void setMax(int var1) {
   }

   int getMin() {
      return -1;
   }

   int getMax() {
      return -1;
   }

   int getReferenceNumber() {
      return 0;
   }

   String getString() {
      return null;
   }

   int getParenNumber() {
      return 0;
   }

   int getChar() {
      return -1;
   }

   public String toString() {
      return this.toString(0);
   }

   public String toString(int var1) {
      return this.type == 11 ? "." : "";
   }

   final int getMinLength() {
      switch (this.type) {
         case 0:
         case 4:
         case 5:
         case 11:
            return 1;
         case 1:
            int var1 = 0;

            for(int var2 = 0; var2 < this.size(); ++var2) {
               var1 += this.getChild(var2).getMinLength();
            }

            return var1;
         case 2:
         case 26:
            if (this.size() == 0) {
               return 0;
            }

            int var3 = this.getChild(0).getMinLength();

            for(int var4 = 1; var4 < this.size(); ++var4) {
               int var5 = this.getChild(var4).getMinLength();
               if (var5 < var3) {
                  var3 = var5;
               }
            }

            return var3;
         case 3:
         case 9:
            if (this.getMin() >= 0) {
               return this.getMin() * this.getChild(0).getMinLength();
            }

            return 0;
         case 6:
         case 24:
         case 25:
            return this.getChild(0).getMinLength();
         case 7:
         case 8:
            return 0;
         case 10:
            return this.getString().length();
         case 12:
            return 0;
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         default:
            throw new RuntimeException("Token#getMinLength(): Invalid Type: " + this.type);
         case 20:
         case 21:
         case 22:
         case 23:
            return 0;
      }
   }

   final int getMaxLength() {
      int var3;
      switch (this.type) {
         case 0:
            return 1;
         case 1:
            int var1 = 0;

            for(int var2 = 0; var2 < this.size(); ++var2) {
               var3 = this.getChild(var2).getMaxLength();
               if (var3 < 0) {
                  return -1;
               }

               var1 += var3;
            }

            return var1;
         case 2:
         case 26:
            if (this.size() == 0) {
               return 0;
            }

            var3 = this.getChild(0).getMaxLength();

            for(int var4 = 1; var3 >= 0 && var4 < this.size(); ++var4) {
               int var5 = this.getChild(var4).getMaxLength();
               if (var5 < 0) {
                  var3 = -1;
                  break;
               }

               if (var5 > var3) {
                  var3 = var5;
               }
            }

            return var3;
         case 3:
         case 9:
            if (this.getMax() >= 0) {
               return this.getMax() * this.getChild(0).getMaxLength();
            }

            return -1;
         case 4:
         case 5:
         case 11:
            return 2;
         case 6:
         case 24:
         case 25:
            return this.getChild(0).getMaxLength();
         case 7:
         case 8:
            return 0;
         case 10:
            return this.getString().length();
         case 12:
            return -1;
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         default:
            throw new RuntimeException("Token#getMaxLength(): Invalid Type: " + this.type);
         case 20:
         case 21:
         case 22:
         case 23:
            return 0;
      }
   }

   private static final boolean isSet(int var0, int var1) {
      return (var0 & var1) == var1;
   }

   final int analyzeFirstCharacter(RangeToken var1, int var2) {
      switch (this.type) {
         case 0:
            int var10 = this.getChar();
            var1.addRange(var10, var10);
            if (var10 < 65536 && isSet(var2, 2)) {
               char var13 = Character.toUpperCase((char)var10);
               var1.addRange(var13, var13);
               var13 = Character.toLowerCase((char)var13);
               var1.addRange(var13, var13);
            }

            return 1;
         case 1:
            int var3 = 0;

            for(int var4 = 0; var4 < this.size() && (var3 = this.getChild(var4).analyzeFirstCharacter(var1, var2)) == 0; ++var4) {
            }

            return var3;
         case 2:
            if (this.size() == 0) {
               return 0;
            }

            int var5 = 0;
            boolean var6 = false;

            for(int var7 = 0; var7 < this.size(); ++var7) {
               var5 = this.getChild(var7).analyzeFirstCharacter(var1, var2);
               if (var5 == 2) {
                  break;
               }

               if (var5 == 0) {
                  var6 = true;
               }
            }

            return var6 ? 0 : var5;
         case 3:
         case 9:
            this.getChild(0).analyzeFirstCharacter(var1, var2);
            return 0;
         case 4:
            if (isSet(var2, 2)) {
               var1.mergeRanges(((RangeToken)this).getCaseInsensitiveToken());
            } else {
               var1.mergeRanges(this);
            }

            return 1;
         case 5:
            if (isSet(var2, 2)) {
               var1.mergeRanges(complementRanges(((RangeToken)this).getCaseInsensitiveToken()));
            } else {
               var1.mergeRanges(complementRanges(this));
            }

            return 1;
         case 6:
         case 24:
            return this.getChild(0).analyzeFirstCharacter(var1, var2);
         case 7:
         case 8:
            return 0;
         case 10:
            int var11 = this.getString().charAt(0);
            char var12;
            if (REUtil.isHighSurrogate(var11) && this.getString().length() >= 2 && REUtil.isLowSurrogate(var12 = this.getString().charAt(1))) {
               var11 = REUtil.composeFromSurrogates(var11, var12);
            }

            var1.addRange(var11, var11);
            if (var11 < 65536 && isSet(var2, 2)) {
               char var14 = Character.toUpperCase((char)var11);
               var1.addRange(var14, var14);
               var14 = Character.toLowerCase((char)var14);
               var1.addRange(var14, var14);
            }

            return 1;
         case 11:
            if (isSet(var2, 4)) {
               return 0;
            }

            return 0;
         case 12:
            var1.addRange(0, 1114111);
            return 2;
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         default:
            throw new RuntimeException("Token#analyzeHeadCharacter(): Invalid Type: " + this.type);
         case 20:
         case 21:
         case 22:
         case 23:
            return 0;
         case 25:
            var2 |= ((ModifierToken)this).getOptions();
            var2 &= ~((ModifierToken)this).getOptionsMask();
            return this.getChild(0).analyzeFirstCharacter(var1, var2);
         case 26:
            int var8 = this.getChild(0).analyzeFirstCharacter(var1, var2);
            if (this.size() == 1) {
               return 0;
            } else if (var8 == 2) {
               return var8;
            } else {
               int var9 = this.getChild(1).analyzeFirstCharacter(var1, var2);
               if (var9 == 2) {
                  return var9;
               } else {
                  return var8 != 0 && var9 != 0 ? 1 : 0;
               }
            }
      }
   }

   private final boolean isShorterThan(Token var1) {
      if (var1 == null) {
         return false;
      } else if (this.type == 10) {
         int var2 = this.getString().length();
         if (var1.type == 10) {
            int var3 = var1.getString().length();
            return var2 < var3;
         } else {
            throw new RuntimeException("Internal Error: Illegal type: " + var1.type);
         }
      } else {
         throw new RuntimeException("Internal Error: Illegal type: " + this.type);
      }
   }

   final void findFixedString(FixedStringContainer var1, int var2) {
      switch (this.type) {
         case 0:
            var1.token = null;
            return;
         case 1:
            Token var3 = null;
            int var4 = 0;
            int var5 = 0;

            for(; var5 < this.size(); ++var5) {
               this.getChild(var5).findFixedString(var1, var2);
               if (var3 == null || var3.isShorterThan(var1.token)) {
                  var3 = var1.token;
                  var4 = var1.options;
               }
            }

            var1.token = var3;
            var1.options = var4;
            return;
         case 2:
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         case 9:
         case 11:
         case 12:
         case 20:
         case 21:
         case 22:
         case 23:
         case 26:
            var1.token = null;
            return;
         case 6:
         case 24:
            this.getChild(0).findFixedString(var1, var2);
            return;
         case 10:
            var1.token = this;
            var1.options = var2;
            return;
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         default:
            throw new RuntimeException("Token#findFixedString(): Invalid Type: " + this.type);
         case 25:
            var2 |= ((ModifierToken)this).getOptions();
            var2 &= ~((ModifierToken)this).getOptionsMask();
            this.getChild(0).findFixedString(var1, var2);
      }
   }

   boolean match(int var1) {
      throw new RuntimeException("NFAArrow#match(): Internal error: " + this.type);
   }

   protected static RangeToken getRange(String var0, boolean var1) {
      if (categories.size() == 0) {
         Hashtable var2 = categories;
         synchronized(var2) {
            Token[] var3 = new Token[categoryNames.length];

            for(int var4 = 0; var4 < var3.length; ++var4) {
               var3[var4] = createRange();
            }

            for(int var6 = 0; var6 < 65536; ++var6) {
               int var5 = Character.getType((char)var6);
               if (var5 == 21 || var5 == 22) {
                  if (var6 == 171 || var6 == 8216 || var6 == 8219 || var6 == 8220 || var6 == 8223 || var6 == 8249) {
                     var5 = 29;
                  }

                  if (var6 == 187 || var6 == 8217 || var6 == 8221 || var6 == 8250) {
                     var5 = 30;
                  }
               }

               var3[var5].addRange(var6, var6);
               byte var21;
               switch (var5) {
                  case 0:
                  case 15:
                  case 16:
                  case 18:
                  case 19:
                     var21 = 35;
                     break;
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                     var21 = 31;
                     break;
                  case 6:
                  case 7:
                  case 8:
                     var21 = 32;
                     break;
                  case 9:
                  case 10:
                  case 11:
                     var21 = 33;
                     break;
                  case 12:
                  case 13:
                  case 14:
                     var21 = 34;
                     break;
                  case 17:
                  default:
                     throw new RuntimeException("org.apache.xerces.utils.regex.Token#getRange(): Unknown Unicode category: " + var5);
                  case 20:
                  case 21:
                  case 22:
                  case 23:
                  case 24:
                  case 29:
                  case 30:
                     var21 = 36;
                     break;
                  case 25:
                  case 26:
                  case 27:
                  case 28:
                     var21 = 37;
               }

               var3[var21].addRange(var6, var6);
            }

            var3[0].addRange(65536, 1114111);

            for(int var7 = 0; var7 < var3.length; ++var7) {
               if (categoryNames[var7] != null) {
                  if (var7 == 0) {
                     var3[var7].addRange(65536, 1114111);
                  }

                  categories.put(categoryNames[var7], var3[var7]);
                  categories2.put(categoryNames[var7], complementRanges(var3[var7]));
               }
            }

            StringBuffer var8 = new StringBuffer(50);

            RangeToken var10;
            for(int var9 = 0; var9 < blockNames.length; ++var9) {
               var10 = createRange();
               int var11;
               int var13;
               if (var9 < 84) {
                  var11 = var9 * 2;
                  char var12 = "\u0000\u007f\u0080ÿĀſƀɏɐʯʰ˿̀ͯͰϿЀӿ\u0530֏\u0590\u05ff\u0600ۿ܀ݏހ\u07bfऀॿঀ\u09ff\u0a00\u0a7f\u0a80૿\u0b00\u0b7f\u0b80\u0bffఀ౿ಀ\u0cffഀൿ\u0d80\u0dff\u0e00\u0e7f\u0e80\u0effༀ\u0fffက႟Ⴀჿᄀᇿሀ\u137fᎠ\u13ff᐀ᙿ \u169fᚠ\u16ffក\u17ff᠀\u18afḀỿἀ\u1fff \u206f⁰\u209f₠\u20cf⃐\u20ff℀⅏⅐\u218f←⇿∀⋿⌀⏿␀\u243f⑀\u245f①⓿─╿▀▟■◿☀⛿✀➿⠀⣿⺀\u2eff⼀\u2fdf⿰\u2fff　〿\u3040ゟ゠ヿ\u3100ㄯ\u3130\u318f㆐㆟ㆠㆿ㈀㋿㌀㏿㐀䶵一\u9fffꀀ\ua48f꒐\ua4cf가힣\ue000\uf8ff豈\ufaffﬀﭏﭐ\ufdff︠︯︰﹏﹐\ufe6fﹰ\ufefe\ufeff\ufeff\uff00\uffef".charAt(var11);
                  var13 = "\u0000\u007f\u0080ÿĀſƀɏɐʯʰ˿̀ͯͰϿЀӿ\u0530֏\u0590\u05ff\u0600ۿ܀ݏހ\u07bfऀॿঀ\u09ff\u0a00\u0a7f\u0a80૿\u0b00\u0b7f\u0b80\u0bffఀ౿ಀ\u0cffഀൿ\u0d80\u0dff\u0e00\u0e7f\u0e80\u0effༀ\u0fffက႟Ⴀჿᄀᇿሀ\u137fᎠ\u13ff᐀ᙿ \u169fᚠ\u16ffក\u17ff᠀\u18afḀỿἀ\u1fff \u206f⁰\u209f₠\u20cf⃐\u20ff℀⅏⅐\u218f←⇿∀⋿⌀⏿␀\u243f⑀\u245f①⓿─╿▀▟■◿☀⛿✀➿⠀⣿⺀\u2eff⼀\u2fdf⿰\u2fff　〿\u3040ゟ゠ヿ\u3100ㄯ\u3130\u318f㆐㆟ㆠㆿ㈀㋿㌀㏿㐀䶵一\u9fffꀀ\ua48f꒐\ua4cf가힣\ue000\uf8ff豈\ufaffﬀﭏﭐ\ufdff︠︯︰﹏﹐\ufe6fﹰ\ufefe\ufeff\ufeff\uff00\uffef".charAt(var11 + 1);
                  var10.addRange(var12, var13);
               } else {
                  var11 = (var9 - 84) * 2;
                  var10.addRange(nonBMPBlockRanges[var11], nonBMPBlockRanges[var11 + 1]);
               }

               String var23 = blockNames[var9];
               if (var23.equals("Specials")) {
                  var10.addRange(65520, 65533);
               }

               if (var23.equals("Private Use")) {
                  var10.addRange(983040, 1048573);
                  var10.addRange(1048576, 1114109);
               }

               categories.put(var23, var10);
               categories2.put(var23, complementRanges(var10));
               var8.setLength(0);
               var8.append("Is");
               if (var23.indexOf(32) >= 0) {
                  for(var13 = 0; var13 < var23.length(); ++var13) {
                     if (var23.charAt(var13) != ' ') {
                        var8.append(var23.charAt(var13));
                     }
                  }
               } else {
                  var8.append(var23);
               }

               setAlias(var8.toString(), var23, true);
            }

            setAlias("ASSIGNED", "Cn", false);
            setAlias("UNASSIGNED", "Cn", true);
            var10 = createRange();
            var10.addRange(0, 1114111);
            categories.put("ALL", var10);
            categories2.put("ALL", complementRanges(var10));
            registerNonXS("ASSIGNED");
            registerNonXS("UNASSIGNED");
            registerNonXS("ALL");
            RangeToken var22 = createRange();
            var22.mergeRanges(var3[1]);
            var22.mergeRanges(var3[2]);
            var22.mergeRanges(var3[5]);
            categories.put("IsAlpha", var22);
            categories2.put("IsAlpha", complementRanges(var22));
            registerNonXS("IsAlpha");
            RangeToken var24 = createRange();
            var24.mergeRanges(var22);
            var24.mergeRanges(var3[9]);
            categories.put("IsAlnum", var24);
            categories2.put("IsAlnum", complementRanges(var24));
            registerNonXS("IsAlnum");
            RangeToken var25 = createRange();
            var25.mergeRanges(token_spaces);
            var25.mergeRanges(var3[34]);
            categories.put("IsSpace", var25);
            categories2.put("IsSpace", complementRanges(var25));
            registerNonXS("IsSpace");
            RangeToken var14 = createRange();
            var14.mergeRanges(var24);
            var14.addRange(95, 95);
            categories.put("IsWord", var14);
            categories2.put("IsWord", complementRanges(var14));
            registerNonXS("IsWord");
            RangeToken var15 = createRange();
            var15.addRange(0, 127);
            categories.put("IsASCII", var15);
            categories2.put("IsASCII", complementRanges(var15));
            registerNonXS("IsASCII");
            RangeToken var16 = createRange();
            var16.mergeRanges(var3[35]);
            var16.addRange(32, 32);
            categories.put("IsGraph", complementRanges(var16));
            categories2.put("IsGraph", var16);
            registerNonXS("IsGraph");
            RangeToken var17 = createRange();
            var17.addRange(48, 57);
            var17.addRange(65, 70);
            var17.addRange(97, 102);
            categories.put("IsXDigit", complementRanges(var17));
            categories2.put("IsXDigit", var17);
            registerNonXS("IsXDigit");
            setAlias("IsDigit", "Nd", true);
            setAlias("IsUpper", "Lu", true);
            setAlias("IsLower", "Ll", true);
            setAlias("IsCntrl", "C", true);
            setAlias("IsPrint", "C", false);
            setAlias("IsPunct", "P", true);
            registerNonXS("IsDigit");
            registerNonXS("IsUpper");
            registerNonXS("IsLower");
            registerNonXS("IsCntrl");
            registerNonXS("IsPrint");
            registerNonXS("IsPunct");
            setAlias("alpha", "IsAlpha", true);
            setAlias("alnum", "IsAlnum", true);
            setAlias("ascii", "IsASCII", true);
            setAlias("cntrl", "IsCntrl", true);
            setAlias("digit", "IsDigit", true);
            setAlias("graph", "IsGraph", true);
            setAlias("lower", "IsLower", true);
            setAlias("print", "IsPrint", true);
            setAlias("punct", "IsPunct", true);
            setAlias("space", "IsSpace", true);
            setAlias("upper", "IsUpper", true);
            setAlias("word", "IsWord", true);
            setAlias("xdigit", "IsXDigit", true);
            registerNonXS("alpha");
            registerNonXS("alnum");
            registerNonXS("ascii");
            registerNonXS("cntrl");
            registerNonXS("digit");
            registerNonXS("graph");
            registerNonXS("lower");
            registerNonXS("print");
            registerNonXS("punct");
            registerNonXS("space");
            registerNonXS("upper");
            registerNonXS("word");
            registerNonXS("xdigit");
         }
      }

      RangeToken var20 = var1 ? (RangeToken)categories.get(var0) : (RangeToken)categories2.get(var0);
      return var20;
   }

   protected static RangeToken getRange(String var0, boolean var1, boolean var2) {
      RangeToken var3 = getRange(var0, var1);
      if (var2 && var3 != null && isRegisterNonXS(var0)) {
         var3 = null;
      }

      return var3;
   }

   protected static void registerNonXS(String var0) {
      if (nonxs == null) {
         nonxs = new Hashtable();
      }

      nonxs.put(var0, var0);
   }

   protected static boolean isRegisterNonXS(String var0) {
      return nonxs == null ? false : nonxs.containsKey(var0);
   }

   private static void setAlias(String var0, String var1, boolean var2) {
      Token var3 = (Token)categories.get(var1);
      Token var4 = (Token)categories2.get(var1);
      if (var2) {
         categories.put(var0, var3);
         categories2.put(var0, var4);
      } else {
         categories2.put(var0, var3);
         categories.put(var0, var4);
      }

   }

   static synchronized Token getGraphemePattern() {
      if (token_grapheme != null) {
         return token_grapheme;
      } else {
         RangeToken var0 = createRange();
         var0.mergeRanges(getRange("ASSIGNED", true));
         var0.subtractRanges(getRange("M", true));
         var0.subtractRanges(getRange("C", true));
         RangeToken var1 = createRange();

         for(int var2 = 0; var2 < "्্੍્୍்్್്ฺ྄".length(); ++var2) {
            char var3 = "्্੍્୍்్್്ฺ྄".charAt(var2);
            var1.addRange(var2, var2);
         }

         RangeToken var6 = createRange();
         var6.mergeRanges(getRange("M", true));
         var6.addRange(4448, 4607);
         var6.addRange(65438, 65439);
         UnionToken var4 = createUnion();
         var4.addChild(var0);
         var4.addChild(token_empty);
         UnionToken var5 = createUnion();
         var5.addChild(createConcat(var1, getRange("L", true)));
         var5.addChild(var6);
         ClosureToken var7 = createClosure(var5);
         ConcatToken var8 = createConcat(var4, var7);
         token_grapheme = var8;
         return token_grapheme;
      }
   }

   static synchronized Token getCombiningCharacterSequence() {
      if (token_ccs != null) {
         return token_ccs;
      } else {
         ClosureToken var0 = createClosure(getRange("M", true));
         ConcatToken var1 = createConcat(getRange("M", false), var0);
         token_ccs = var1;
         return token_ccs;
      }
   }

   static {
      token_0to9.addRange(48, 57);
      token_wordchars = createRange();
      token_wordchars.addRange(48, 57);
      token_wordchars.addRange(65, 90);
      token_wordchars.addRange(95, 95);
      token_wordchars.addRange(97, 122);
      token_spaces = createRange();
      token_spaces.addRange(9, 9);
      token_spaces.addRange(10, 10);
      token_spaces.addRange(12, 12);
      token_spaces.addRange(13, 13);
      token_spaces.addRange(32, 32);
      token_not_0to9 = complementRanges(token_0to9);
      token_not_wordchars = complementRanges(token_wordchars);
      token_not_spaces = complementRanges(token_spaces);
      categories = new Hashtable();
      categories2 = new Hashtable();
      categoryNames = new String[]{"Cn", "Lu", "Ll", "Lt", "Lm", "Lo", "Mn", "Me", "Mc", "Nd", "Nl", "No", "Zs", "Zl", "Zp", "Cc", "Cf", null, "Co", "Cs", "Pd", "Ps", "Pe", "Pc", "Po", "Sm", "Sc", "Sk", "So", "Pi", "Pf", "L", "M", "N", "Z", "C", "P", "S"};
      blockNames = new String[]{"Basic Latin", "Latin-1 Supplement", "Latin Extended-A", "Latin Extended-B", "IPA Extensions", "Spacing Modifier Letters", "Combining Diacritical Marks", "Greek", "Cyrillic", "Armenian", "Hebrew", "Arabic", "Syriac", "Thaana", "Devanagari", "Bengali", "Gurmukhi", "Gujarati", "Oriya", "Tamil", "Telugu", "Kannada", "Malayalam", "Sinhala", "Thai", "Lao", "Tibetan", "Myanmar", "Georgian", "Hangul Jamo", "Ethiopic", "Cherokee", "Unified Canadian Aboriginal Syllabics", "Ogham", "Runic", "Khmer", "Mongolian", "Latin Extended Additional", "Greek Extended", "General Punctuation", "Superscripts and Subscripts", "Currency Symbols", "Combining Marks for Symbols", "Letterlike Symbols", "Number Forms", "Arrows", "Mathematical Operators", "Miscellaneous Technical", "Control Pictures", "Optical Character Recognition", "Enclosed Alphanumerics", "Box Drawing", "Block Elements", "Geometric Shapes", "Miscellaneous Symbols", "Dingbats", "Braille Patterns", "CJK Radicals Supplement", "Kangxi Radicals", "Ideographic Description Characters", "CJK Symbols and Punctuation", "Hiragana", "Katakana", "Bopomofo", "Hangul Compatibility Jamo", "Kanbun", "Bopomofo Extended", "Enclosed CJK Letters and Months", "CJK Compatibility", "CJK Unified Ideographs Extension A", "CJK Unified Ideographs", "Yi Syllables", "Yi Radicals", "Hangul Syllables", "Private Use", "CJK Compatibility Ideographs", "Alphabetic Presentation Forms", "Arabic Presentation Forms-A", "Combining Half Marks", "CJK Compatibility Forms", "Small Form Variants", "Arabic Presentation Forms-B", "Specials", "Halfwidth and Fullwidth Forms", "Old Italic", "Gothic", "Deseret", "Byzantine Musical Symbols", "Musical Symbols", "Mathematical Alphanumeric Symbols", "CJK Unified Ideographs Extension B", "CJK Compatibility Ideographs Supplement", "Tags"};
      nonBMPBlockRanges = new int[]{66304, 66351, 66352, 66383, 66560, 66639, 118784, 119039, 119040, 119295, 119808, 120831, 131072, 173782, 194560, 195103, 917504, 917631};
      nonxs = null;
      token_grapheme = null;
      token_ccs = null;
   }

   static class UnionToken extends Token implements Serializable {
      private static final long serialVersionUID = 3256723987530003507L;
      Vector children;

      UnionToken(int var1) {
         super(var1);
      }

      void addChild(Token var1) {
         if (var1 != null) {
            if (this.children == null) {
               this.children = new Vector();
            }

            if (super.type == 2) {
               this.children.addElement(var1);
            } else {
               int var2;
               if (var1.type == 1) {
                  for(var2 = 0; var2 < var1.size(); ++var2) {
                     this.addChild(var1.getChild(var2));
                  }

               } else {
                  var2 = this.children.size();
                  if (var2 == 0) {
                     this.children.addElement(var1);
                  } else {
                     Object var3 = (Token)this.children.elementAt(var2 - 1);
                     if ((((Token)var3).type == 0 || ((Token)var3).type == 10) && (var1.type == 0 || var1.type == 10)) {
                        int var5 = var1.type == 0 ? 2 : var1.getString().length();
                        StringBuffer var4;
                        int var6;
                        if (((Token)var3).type == 0) {
                           var4 = new StringBuffer(2 + var5);
                           var6 = ((Token)var3).getChar();
                           if (var6 >= 65536) {
                              var4.append(REUtil.decomposeToSurrogates(var6));
                           } else {
                              var4.append((char)var6);
                           }

                           var3 = Token.createString((String)null);
                           this.children.setElementAt(var3, var2 - 1);
                        } else {
                           var4 = new StringBuffer(((Token)var3).getString().length() + var5);
                           var4.append(((Token)var3).getString());
                        }

                        if (var1.type == 0) {
                           var6 = var1.getChar();
                           if (var6 >= 65536) {
                              var4.append(REUtil.decomposeToSurrogates(var6));
                           } else {
                              var4.append((char)var6);
                           }
                        } else {
                           var4.append(var1.getString());
                        }

                        ((StringToken)var3).string = new String(var4);
                     } else {
                        this.children.addElement(var1);
                     }
                  }
               }
            }
         }
      }

      int size() {
         return this.children == null ? 0 : this.children.size();
      }

      Token getChild(int var1) {
         return (Token)this.children.elementAt(var1);
      }

      public String toString(int var1) {
         String var2;
         StringBuffer var3;
         int var4;
         if (super.type == 1) {
            if (this.children.size() == 2) {
               Token var5 = this.getChild(0);
               Token var6 = this.getChild(1);
               if (var6.type == 3 && var6.getChild(0) == var5) {
                  var2 = var5.toString(var1) + "+";
               } else if (var6.type == 9 && var6.getChild(0) == var5) {
                  var2 = var5.toString(var1) + "+?";
               } else {
                  var2 = var5.toString(var1) + var6.toString(var1);
               }
            } else {
               var3 = new StringBuffer();

               for(var4 = 0; var4 < this.children.size(); ++var4) {
                  var3.append(((Token)this.children.elementAt(var4)).toString(var1));
               }

               var2 = new String(var3);
            }

            return var2;
         } else {
            if (this.children.size() == 2 && this.getChild(1).type == 7) {
               var2 = this.getChild(0).toString(var1) + "?";
            } else if (this.children.size() == 2 && this.getChild(0).type == 7) {
               var2 = this.getChild(1).toString(var1) + "??";
            } else {
               var3 = new StringBuffer();
               var3.append(((Token)this.children.elementAt(0)).toString(var1));

               for(var4 = 1; var4 < this.children.size(); ++var4) {
                  var3.append('|');
                  var3.append(((Token)this.children.elementAt(var4)).toString(var1));
               }

               var2 = new String(var3);
            }

            return var2;
         }
      }
   }

   static class ModifierToken extends Token implements Serializable {
      private static final long serialVersionUID = 3258689892778324790L;
      Token child;
      int add;
      int mask;

      ModifierToken(Token var1, int var2, int var3) {
         super(25);
         this.child = var1;
         this.add = var2;
         this.mask = var3;
      }

      int size() {
         return 1;
      }

      Token getChild(int var1) {
         return this.child;
      }

      int getOptions() {
         return this.add;
      }

      int getOptionsMask() {
         return this.mask;
      }

      public String toString(int var1) {
         return "(?" + (this.add == 0 ? "" : REUtil.createOptionString(this.add)) + (this.mask == 0 ? "" : REUtil.createOptionString(this.mask)) + ":" + this.child.toString(var1) + ")";
      }
   }

   static class ConditionToken extends Token implements Serializable {
      private static final long serialVersionUID = 3761408607870399794L;
      int refNumber;
      Token condition;
      Token yes;
      Token no;

      ConditionToken(int var1, Token var2, Token var3, Token var4) {
         super(26);
         this.refNumber = var1;
         this.condition = var2;
         this.yes = var3;
         this.no = var4;
      }

      int size() {
         return this.no == null ? 1 : 2;
      }

      Token getChild(int var1) {
         if (var1 == 0) {
            return this.yes;
         } else if (var1 == 1) {
            return this.no;
         } else {
            throw new RuntimeException("Internal Error: " + var1);
         }
      }

      public String toString(int var1) {
         String var2;
         if (this.refNumber > 0) {
            var2 = "(?(" + this.refNumber + ")";
         } else if (this.condition.type == 8) {
            var2 = "(?(" + this.condition + ")";
         } else {
            var2 = "(?" + this.condition;
         }

         if (this.no == null) {
            var2 = var2 + this.yes + ")";
         } else {
            var2 = var2 + this.yes + "|" + this.no + ")";
         }

         return var2;
      }
   }

   static class ParenToken extends Token implements Serializable {
      private static final long serialVersionUID = 3257572797621219636L;
      Token child;
      int parennumber;

      ParenToken(int var1, Token var2, int var3) {
         super(var1);
         this.child = var2;
         this.parennumber = var3;
      }

      int size() {
         return 1;
      }

      Token getChild(int var1) {
         return this.child;
      }

      int getParenNumber() {
         return this.parennumber;
      }

      public String toString(int var1) {
         String var2 = null;
         switch (super.type) {
            case 6:
               if (this.parennumber == 0) {
                  var2 = "(?:" + this.child.toString(var1) + ")";
               } else {
                  var2 = "(" + this.child.toString(var1) + ")";
               }
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            default:
               break;
            case 20:
               var2 = "(?=" + this.child.toString(var1) + ")";
               break;
            case 21:
               var2 = "(?!" + this.child.toString(var1) + ")";
               break;
            case 22:
               var2 = "(?<=" + this.child.toString(var1) + ")";
               break;
            case 23:
               var2 = "(?<!" + this.child.toString(var1) + ")";
               break;
            case 24:
               var2 = "(?>" + this.child.toString(var1) + ")";
         }

         return var2;
      }
   }

   static class ClosureToken extends Token implements Serializable {
      private static final long serialVersionUID = 3545230349706932537L;
      int min;
      int max;
      Token child;

      ClosureToken(int var1, Token var2) {
         super(var1);
         this.child = var2;
         this.setMin(-1);
         this.setMax(-1);
      }

      int size() {
         return 1;
      }

      Token getChild(int var1) {
         return this.child;
      }

      final void setMin(int var1) {
         this.min = var1;
      }

      final void setMax(int var1) {
         this.max = var1;
      }

      final int getMin() {
         return this.min;
      }

      final int getMax() {
         return this.max;
      }

      public String toString(int var1) {
         String var2;
         if (super.type == 3) {
            if (this.getMin() < 0 && this.getMax() < 0) {
               var2 = this.child.toString(var1) + "*";
            } else if (this.getMin() == this.getMax()) {
               var2 = this.child.toString(var1) + "{" + this.getMin() + "}";
            } else if (this.getMin() >= 0 && this.getMax() >= 0) {
               var2 = this.child.toString(var1) + "{" + this.getMin() + "," + this.getMax() + "}";
            } else {
               if (this.getMin() < 0 || this.getMax() >= 0) {
                  throw new RuntimeException("Token#toString(): CLOSURE " + this.getMin() + ", " + this.getMax());
               }

               var2 = this.child.toString(var1) + "{" + this.getMin() + ",}";
            }
         } else if (this.getMin() < 0 && this.getMax() < 0) {
            var2 = this.child.toString(var1) + "*?";
         } else if (this.getMin() == this.getMax()) {
            var2 = this.child.toString(var1) + "{" + this.getMin() + "}?";
         } else if (this.getMin() >= 0 && this.getMax() >= 0) {
            var2 = this.child.toString(var1) + "{" + this.getMin() + "," + this.getMax() + "}?";
         } else {
            if (this.getMin() < 0 || this.getMax() >= 0) {
               throw new RuntimeException("Token#toString(): NONGREEDYCLOSURE " + this.getMin() + ", " + this.getMax());
            }

            var2 = this.child.toString(var1) + "{" + this.getMin() + ",}?";
         }

         return var2;
      }
   }

   static class CharToken extends Token implements Serializable {
      private static final long serialVersionUID = 3257284751277569842L;
      int chardata;

      CharToken(int var1, int var2) {
         super(var1);
         this.chardata = var2;
      }

      int getChar() {
         return this.chardata;
      }

      public String toString(int var1) {
         String var2;
         switch (super.type) {
            case 0:
               switch (this.chardata) {
                  case 9:
                     var2 = "\\t";
                     return var2;
                  case 10:
                     var2 = "\\n";
                     return var2;
                  case 12:
                     var2 = "\\f";
                     return var2;
                  case 13:
                     var2 = "\\r";
                     return var2;
                  case 27:
                     var2 = "\\e";
                     return var2;
                  case 40:
                  case 41:
                  case 42:
                  case 43:
                  case 46:
                  case 63:
                  case 91:
                  case 92:
                  case 123:
                  case 124:
                     var2 = "\\" + (char)this.chardata;
                     return var2;
                  default:
                     if (this.chardata >= 65536) {
                        String var3 = "0" + Integer.toHexString(this.chardata);
                        var2 = "\\v" + var3.substring(var3.length() - 6, var3.length());
                     } else {
                        var2 = "" + (char)this.chardata;
                     }

                     return var2;
               }
            case 8:
               if (this != Token.token_linebeginning && this != Token.token_lineend) {
                  var2 = "\\" + (char)this.chardata;
               } else {
                  var2 = "" + (char)this.chardata;
               }
               break;
            default:
               var2 = null;
         }

         return var2;
      }

      boolean match(int var1) {
         if (super.type == 0) {
            return var1 == this.chardata;
         } else {
            throw new RuntimeException("NFAArrow#match(): Internal error: " + super.type);
         }
      }
   }

   static class ConcatToken extends Token implements Serializable {
      private static final long serialVersionUID = 4050760502994940212L;
      Token child;
      Token child2;

      ConcatToken(Token var1, Token var2) {
         super(1);
         this.child = var1;
         this.child2 = var2;
      }

      int size() {
         return 2;
      }

      Token getChild(int var1) {
         return var1 == 0 ? this.child : this.child2;
      }

      public String toString(int var1) {
         String var2;
         if (this.child2.type == 3 && this.child2.getChild(0) == this.child) {
            var2 = this.child.toString(var1) + "+";
         } else if (this.child2.type == 9 && this.child2.getChild(0) == this.child) {
            var2 = this.child.toString(var1) + "+?";
         } else {
            var2 = this.child.toString(var1) + this.child2.toString(var1);
         }

         return var2;
      }
   }

   static class StringToken extends Token implements Serializable {
      private static final long serialVersionUID = 3257288015452780086L;
      String string;
      int refNumber;

      StringToken(int var1, String var2, int var3) {
         super(var1);
         this.string = var2;
         this.refNumber = var3;
      }

      int getReferenceNumber() {
         return this.refNumber;
      }

      String getString() {
         return this.string;
      }

      public String toString(int var1) {
         return super.type == 12 ? "\\" + this.refNumber : REUtil.quoteMeta(this.string);
      }
   }

   static class FixedStringContainer {
      Token token = null;
      int options = 0;
   }
}
