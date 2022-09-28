package org.apache.xerces.impl.xpath.regex;

import java.io.Serializable;
import java.text.CharacterIterator;

public class RegularExpression implements Serializable {
   private static final long serialVersionUID = 3905241217112815923L;
   static final boolean DEBUG = false;
   String regex;
   int options;
   int nofparen;
   Token tokentree;
   boolean hasBackReferences = false;
   transient int minlength;
   transient Op operations = null;
   transient int numberOfClosures;
   transient Context context = null;
   transient RangeToken firstChar = null;
   transient String fixedString = null;
   transient int fixedStringOptions;
   transient BMPattern fixedStringTable = null;
   transient boolean fixedStringOnly = false;
   static final int IGNORE_CASE = 2;
   static final int SINGLE_LINE = 4;
   static final int MULTIPLE_LINES = 8;
   static final int EXTENDED_COMMENT = 16;
   static final int USE_UNICODE_CATEGORY = 32;
   static final int UNICODE_WORD_BOUNDARY = 64;
   static final int PROHIBIT_HEAD_CHARACTER_OPTIMIZATION = 128;
   static final int PROHIBIT_FIXED_STRING_OPTIMIZATION = 256;
   static final int XMLSCHEMA_MODE = 512;
   static final int SPECIAL_COMMA = 1024;
   private static final int WT_IGNORE = 0;
   private static final int WT_LETTER = 1;
   private static final int WT_OTHER = 2;
   static final int LINE_FEED = 10;
   static final int CARRIAGE_RETURN = 13;
   static final int LINE_SEPARATOR = 8232;
   static final int PARAGRAPH_SEPARATOR = 8233;

   private synchronized void compile(Token var1) {
      if (this.operations == null) {
         this.numberOfClosures = 0;
         this.operations = this.compile(var1, (Op)null, false);
      }
   }

   private Op compile(Token var1, Op var2, boolean var3) {
      Object var4;
      switch (var1.type) {
         case 0:
            var4 = Op.createChar(var1.getChar());
            ((Op)var4).next = var2;
            break;
         case 1:
            var4 = var2;
            int var16;
            if (!var3) {
               for(var16 = var1.size() - 1; var16 >= 0; --var16) {
                  var4 = this.compile(var1.getChild(var16), (Op)var4, false);
               }

               return (Op)var4;
            } else {
               for(var16 = 0; var16 < var1.size(); ++var16) {
                  var4 = this.compile(var1.getChild(var16), (Op)var4, true);
               }

               return (Op)var4;
            }
         case 2:
            Op.UnionOp var5 = Op.createUnion(var1.size());

            for(int var6 = 0; var6 < var1.size(); ++var6) {
               var5.addElement(this.compile(var1.getChild(var6), var2, var3));
            }

            var4 = var5;
            break;
         case 3:
         case 9:
            Token var7 = var1.getChild(0);
            int var8 = var1.getMin();
            int var9 = var1.getMax();
            int var19;
            if (var8 >= 0 && var8 == var9) {
               var4 = var2;

               for(var19 = 0; var19 < var8; ++var19) {
                  var4 = this.compile(var7, (Op)var4, var3);
               }

               return (Op)var4;
            } else {
               if (var8 > 0 && var9 > 0) {
                  var9 -= var8;
               }

               if (var9 > 0) {
                  var4 = var2;

                  for(var19 = 0; var19 < var9; ++var19) {
                     Op.ChildOp var18 = Op.createQuestion(var1.type == 9);
                     var18.next = var2;
                     var18.setChild(this.compile(var7, (Op)var4, var3));
                     var4 = var18;
                  }
               } else {
                  Op.ChildOp var17;
                  if (var1.type == 9) {
                     var17 = Op.createNonGreedyClosure();
                  } else if (var7.getMinLength() == 0) {
                     var17 = Op.createClosure(this.numberOfClosures++);
                  } else {
                     var17 = Op.createClosure(-1);
                  }

                  var17.next = var2;
                  var17.setChild(this.compile(var7, var17, var3));
                  var4 = var17;
               }

               if (var8 > 0) {
                  for(var19 = 0; var19 < var8; ++var19) {
                     var4 = this.compile(var7, (Op)var4, var3);
                  }
               }
               break;
            }
         case 4:
         case 5:
            var4 = Op.createRange(var1);
            ((Op)var4).next = var2;
            break;
         case 6:
            if (var1.getParenNumber() == 0) {
               var4 = this.compile(var1.getChild(0), var2, var3);
            } else {
               Op.CharOp var15;
               if (var3) {
                  var15 = Op.createCapture(var1.getParenNumber(), var2);
                  var2 = this.compile(var1.getChild(0), var15, var3);
                  var4 = Op.createCapture(-var1.getParenNumber(), var2);
               } else {
                  var15 = Op.createCapture(-var1.getParenNumber(), var2);
                  var2 = this.compile(var1.getChild(0), var15, var3);
                  var4 = Op.createCapture(var1.getParenNumber(), var2);
               }
            }
            break;
         case 7:
            var4 = var2;
            break;
         case 8:
            var4 = Op.createAnchor(var1.getChar());
            ((Op)var4).next = var2;
            break;
         case 10:
            var4 = Op.createString(var1.getString());
            ((Op)var4).next = var2;
            break;
         case 11:
            var4 = Op.createDot();
            ((Op)var4).next = var2;
            break;
         case 12:
            var4 = Op.createBackReference(var1.getReferenceNumber());
            ((Op)var4).next = var2;
            break;
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         default:
            throw new RuntimeException("Unknown token type: " + var1.type);
         case 20:
            var4 = Op.createLook(20, var2, this.compile(var1.getChild(0), (Op)null, false));
            break;
         case 21:
            var4 = Op.createLook(21, var2, this.compile(var1.getChild(0), (Op)null, false));
            break;
         case 22:
            var4 = Op.createLook(22, var2, this.compile(var1.getChild(0), (Op)null, true));
            break;
         case 23:
            var4 = Op.createLook(23, var2, this.compile(var1.getChild(0), (Op)null, true));
            break;
         case 24:
            var4 = Op.createIndependent(var2, this.compile(var1.getChild(0), (Op)null, var3));
            break;
         case 25:
            var4 = Op.createModifier(var2, this.compile(var1.getChild(0), (Op)null, var3), ((Token.ModifierToken)var1).getOptions(), ((Token.ModifierToken)var1).getOptionsMask());
            break;
         case 26:
            Token.ConditionToken var10 = (Token.ConditionToken)var1;
            int var11 = var10.refNumber;
            Op var12 = var10.condition == null ? null : this.compile(var10.condition, (Op)null, var3);
            Op var13 = this.compile(var10.yes, var2, var3);
            Op var14 = var10.no == null ? null : this.compile(var10.no, var2, var3);
            var4 = Op.createCondition(var2, var11, var12, var13, var14);
      }

      return (Op)var4;
   }

   public boolean matches(char[] var1) {
      return this.matches((char[])var1, 0, var1.length, (Match)null);
   }

   public boolean matches(char[] var1, int var2, int var3) {
      return this.matches(var1, var2, var3, (Match)null);
   }

   public boolean matches(char[] var1, Match var2) {
      return this.matches((char[])var1, 0, var1.length, var2);
   }

   public boolean matches(char[] var1, int var2, int var3, Match var4) {
      synchronized(this) {
         if (this.operations == null) {
            this.prepare();
         }

         if (this.context == null) {
            this.context = new Context();
         }
      }

      Context var6 = null;
      Context var7 = this.context;
      synchronized(var7) {
         var6 = this.context.inuse ? new Context() : this.context;
         var6.reset(var1, var2, var3, this.numberOfClosures);
      }

      if (var4 != null) {
         var4.setNumberOfGroups(this.nofparen);
         var4.setSource(var1);
      } else if (this.hasBackReferences) {
         var4 = new Match();
         var4.setNumberOfGroups(this.nofparen);
      }

      var6.match = var4;
      int var8;
      if (isSet(this.options, 512)) {
         var8 = this.matchCharArray(var6, this.operations, var6.start, 1, this.options);
         if (var8 == var6.limit) {
            if (var6.match != null) {
               var6.match.setBeginning(0, var6.start);
               var6.match.setEnd(0, var8);
            }

            var6.inuse = false;
            return true;
         } else {
            return false;
         }
      } else if (this.fixedStringOnly) {
         var8 = this.fixedStringTable.matches(var1, var6.start, var6.limit);
         if (var8 >= 0) {
            if (var6.match != null) {
               var6.match.setBeginning(0, var8);
               var6.match.setEnd(0, var8 + this.fixedString.length());
            }

            var6.inuse = false;
            return true;
         } else {
            var6.inuse = false;
            return false;
         }
      } else {
         if (this.fixedString != null) {
            var8 = this.fixedStringTable.matches(var1, var6.start, var6.limit);
            if (var8 < 0) {
               var6.inuse = false;
               return false;
            }
         }

         var8 = var6.limit - this.minlength;
         int var10 = -1;
         int var9;
         if (this.operations != null && this.operations.type == 7 && this.operations.getChild().type == 0) {
            if (isSet(this.options, 4)) {
               var9 = var6.start;
               var10 = this.matchCharArray(var6, this.operations, var6.start, 1, this.options);
            } else {
               boolean var16 = true;

               for(var9 = var6.start; var9 <= var8; ++var9) {
                  char var17 = var1[var9];
                  if (isEOLChar(var17)) {
                     var16 = true;
                  } else {
                     if (var16 && 0 <= (var10 = this.matchCharArray(var6, this.operations, var9, 1, this.options))) {
                        break;
                     }

                     var16 = false;
                  }
               }
            }
         } else if (this.firstChar != null) {
            RangeToken var11 = this.firstChar;
            int var12;
            if (isSet(this.options, 2)) {
               var11 = this.firstChar.getCaseInsensitiveToken();

               for(var9 = var6.start; var9 <= var8; ++var9) {
                  var12 = var1[var9];
                  if (REUtil.isHighSurrogate(var12) && var9 + 1 < var6.limit) {
                     var12 = REUtil.composeFromSurrogates(var12, var1[var9 + 1]);
                     if (!var11.match(var12)) {
                        continue;
                     }
                  } else if (!var11.match(var12)) {
                     char var13 = Character.toUpperCase((char)var12);
                     if (!var11.match(var13) && !var11.match(Character.toLowerCase(var13))) {
                        continue;
                     }
                  }

                  if (0 <= (var10 = this.matchCharArray(var6, this.operations, var9, 1, this.options))) {
                     break;
                  }
               }
            } else {
               for(var9 = var6.start; var9 <= var8; ++var9) {
                  var12 = var1[var9];
                  if (REUtil.isHighSurrogate(var12) && var9 + 1 < var6.limit) {
                     var12 = REUtil.composeFromSurrogates(var12, var1[var9 + 1]);
                  }

                  if (var11.match(var12) && 0 <= (var10 = this.matchCharArray(var6, this.operations, var9, 1, this.options))) {
                     break;
                  }
               }
            }
         } else {
            for(var9 = var6.start; var9 <= var8 && 0 > (var10 = this.matchCharArray(var6, this.operations, var9, 1, this.options)); ++var9) {
            }
         }

         if (var10 >= 0) {
            if (var6.match != null) {
               var6.match.setBeginning(0, var9);
               var6.match.setEnd(0, var10);
            }

            var6.inuse = false;
            return true;
         } else {
            var6.inuse = false;
            return false;
         }
      }
   }

   private int matchCharArray(Context var1, Op var2, int var3, int var4, int var5) {
      char[] var6 = var1.charTarget;

      while(var2 != null) {
         if (var3 > var1.limit || var3 < var1.start) {
            return -1;
         }

         int var8;
         int var9;
         int var13;
         int var15;
         int var18;
         switch (var2.type) {
            case 0:
               if (var4 > 0) {
                  if (var3 >= var1.limit) {
                     return -1;
                  }

                  var13 = var6[var3];
                  if (isSet(var5, 4)) {
                     if (REUtil.isHighSurrogate(var13) && var3 + 1 < var1.limit) {
                        ++var3;
                     }
                  } else {
                     if (REUtil.isHighSurrogate(var13) && var3 + 1 < var1.limit) {
                        ++var3;
                        var13 = REUtil.composeFromSurrogates(var13, var6[var3]);
                     }

                     if (isEOLChar(var13)) {
                        return -1;
                     }
                  }

                  ++var3;
               } else {
                  var13 = var3 - 1;
                  if (var13 >= var1.limit || var13 < 0) {
                     return -1;
                  }

                  var8 = var6[var13];
                  if (isSet(var5, 4)) {
                     if (REUtil.isLowSurrogate(var8) && var13 - 1 >= 0) {
                        --var13;
                     }
                  } else {
                     if (REUtil.isLowSurrogate(var8) && var13 - 1 >= 0) {
                        --var13;
                        var8 = REUtil.composeFromSurrogates(var6[var13], var8);
                     }

                     if (!isEOLChar(var8)) {
                        return -1;
                     }
                  }

                  var3 = var13;
               }

               var2 = var2.next;
               break;
            case 1:
               if (isSet(var5, 2)) {
                  var13 = var2.getData();
                  if (var4 > 0) {
                     if (var3 >= var1.limit || !matchIgnoreCase(var13, var6[var3])) {
                        return -1;
                     }

                     ++var3;
                  } else {
                     var8 = var3 - 1;
                     if (var8 >= var1.limit || var8 < 0 || !matchIgnoreCase(var13, var6[var8])) {
                        return -1;
                     }

                     var3 = var8;
                  }
               } else {
                  var13 = var2.getData();
                  if (var4 > 0) {
                     if (var3 >= var1.limit || var13 != var6[var3]) {
                        return -1;
                     }

                     ++var3;
                  } else {
                     var8 = var3 - 1;
                     if (var8 >= var1.limit || var8 < 0 || var13 != var6[var8]) {
                        return -1;
                     }

                     var3 = var8;
                  }
               }

               var2 = var2.next;
               break;
            case 2:
            case 12:
            case 13:
            case 14:
            case 17:
            case 18:
            case 19:
            default:
               throw new RuntimeException("Unknown operation type: " + var2.type);
            case 3:
            case 4:
               if (var4 > 0) {
                  if (var3 >= var1.limit) {
                     return -1;
                  }

                  var13 = var6[var3];
                  if (REUtil.isHighSurrogate(var13) && var3 + 1 < var1.limit) {
                     ++var3;
                     var13 = REUtil.composeFromSurrogates(var13, var6[var3]);
                  }

                  RangeToken var19 = var2.getToken();
                  if (isSet(var5, 2)) {
                     var19 = var19.getCaseInsensitiveToken();
                     if (!var19.match(var13)) {
                        if (var13 >= 65536) {
                           return -1;
                        }

                        char var17;
                        if (!var19.match(var17 = Character.toUpperCase((char)var13)) && !var19.match(Character.toLowerCase(var17))) {
                           return -1;
                        }
                     }
                  } else if (!var19.match(var13)) {
                     return -1;
                  }

                  ++var3;
               } else {
                  var13 = var3 - 1;
                  if (var13 >= var1.limit || var13 < 0) {
                     return -1;
                  }

                  var8 = var6[var13];
                  if (REUtil.isLowSurrogate(var8) && var13 - 1 >= 0) {
                     --var13;
                     var8 = REUtil.composeFromSurrogates(var6[var13], var8);
                  }

                  RangeToken var16 = var2.getToken();
                  if (isSet(var5, 2)) {
                     var16 = var16.getCaseInsensitiveToken();
                     if (!var16.match(var8)) {
                        if (var8 >= 65536) {
                           return -1;
                        }

                        char var20;
                        if (!var16.match(var20 = Character.toUpperCase((char)var8)) && !var16.match(Character.toLowerCase(var20))) {
                           return -1;
                        }
                     }
                  } else if (!var16.match(var8)) {
                     return -1;
                  }

                  var3 = var13;
               }

               var2 = var2.next;
               break;
            case 5:
               boolean var7 = false;
               switch (var2.getData()) {
                  case 36:
                     if (isSet(var5, 8)) {
                        if (var3 == var1.limit || var3 < var1.limit && isEOLChar(var6[var3])) {
                           break;
                        }

                        return -1;
                     } else {
                        if (var3 != var1.limit && (var3 + 1 != var1.limit || !isEOLChar(var6[var3])) && (var3 + 2 != var1.limit || var6[var3] != '\r' || var6[var3 + 1] != '\n')) {
                           return -1;
                        }
                        break;
                     }
                  case 60:
                     if (var1.length == 0 || var3 == var1.limit) {
                        return -1;
                     }

                     if (getWordType(var6, var1.start, var1.limit, var3, var5) != 1 || getPreviousWordType(var6, var1.start, var1.limit, var3, var5) != 2) {
                        return -1;
                     }
                     break;
                  case 62:
                     if (var1.length == 0 || var3 == var1.start) {
                        return -1;
                     }

                     if (getWordType(var6, var1.start, var1.limit, var3, var5) != 2 || getPreviousWordType(var6, var1.start, var1.limit, var3, var5) != 1) {
                        return -1;
                     }
                     break;
                  case 64:
                     if (var3 != var1.start && (var3 <= var1.start || !isEOLChar(var6[var3 - 1]))) {
                        return -1;
                     }
                     break;
                  case 65:
                     if (var3 != var1.start) {
                        return -1;
                     }
                     break;
                  case 66:
                     if (var1.length == 0) {
                        var7 = true;
                     } else {
                        var8 = getWordType(var6, var1.start, var1.limit, var3, var5);
                        var7 = var8 == 0 || var8 == getPreviousWordType(var6, var1.start, var1.limit, var3, var5);
                     }

                     if (!var7) {
                        return -1;
                     }
                     break;
                  case 90:
                     if (var3 == var1.limit || var3 + 1 == var1.limit && isEOLChar(var6[var3]) || var3 + 2 == var1.limit && var6[var3] == '\r' && var6[var3 + 1] == '\n') {
                        break;
                     }

                     return -1;
                  case 94:
                     if (isSet(var5, 8)) {
                        if (var3 != var1.start && (var3 <= var1.start || !isEOLChar(var6[var3 - 1]))) {
                           return -1;
                        }
                     } else if (var3 != var1.start) {
                        return -1;
                     }
                     break;
                  case 98:
                     if (var1.length == 0) {
                        return -1;
                     }

                     var8 = getWordType(var6, var1.start, var1.limit, var3, var5);
                     if (var8 == 0) {
                        return -1;
                     }

                     var9 = getPreviousWordType(var6, var1.start, var1.limit, var3, var5);
                     if (var8 == var9) {
                        return -1;
                     }
                     break;
                  case 122:
                     if (var3 != var1.limit) {
                        return -1;
                     }
               }

               var2 = var2.next;
               break;
            case 6:
               String var14 = var2.getString();
               var9 = var14.length();
               if (!isSet(var5, 2)) {
                  if (var4 > 0) {
                     if (!regionMatches(var6, var3, var1.limit, var14, var9)) {
                        return -1;
                     }

                     var3 += var9;
                  } else {
                     if (!regionMatches(var6, var3 - var9, var1.limit, var14, var9)) {
                        return -1;
                     }

                     var3 -= var9;
                  }
               } else if (var4 > 0) {
                  if (!regionMatchesIgnoreCase(var6, var3, var1.limit, var14, var9)) {
                     return -1;
                  }

                  var3 += var9;
               } else {
                  if (!regionMatchesIgnoreCase(var6, var3 - var9, var1.limit, var14, var9)) {
                     return -1;
                  }

                  var3 -= var9;
               }

               var2 = var2.next;
               break;
            case 7:
               var8 = var2.getData();
               if (var8 >= 0) {
                  var9 = var1.offsets[var8];
                  if (var9 >= 0 && var9 == var3) {
                     var1.offsets[var8] = -1;
                     var2 = var2.next;
                     break;
                  }

                  var1.offsets[var8] = var3;
               }

               var9 = this.matchCharArray(var1, var2.getChild(), var3, var4, var5);
               if (var8 >= 0) {
                  var1.offsets[var8] = -1;
               }

               if (var9 >= 0) {
                  return var9;
               }

               var2 = var2.next;
               break;
            case 8:
            case 10:
               var8 = this.matchCharArray(var1, var2.next, var3, var4, var5);
               if (var8 >= 0) {
                  return var8;
               }

               var2 = var2.getChild();
               break;
            case 9:
               var8 = this.matchCharArray(var1, var2.getChild(), var3, var4, var5);
               if (var8 >= 0) {
                  return var8;
               }

               var2 = var2.next;
               break;
            case 11:
               for(var8 = 0; var8 < var2.size(); ++var8) {
                  var9 = this.matchCharArray(var1, var2.elementAt(var8), var3, var4, var5);
                  if (var9 >= 0) {
                     return var9;
                  }
               }

               return -1;
            case 15:
               var9 = var2.getData();
               if (var1.match != null && var9 > 0) {
                  var15 = var1.match.getBeginning(var9);
                  var1.match.setBeginning(var9, var3);
                  var18 = this.matchCharArray(var1, var2.next, var3, var4, var5);
                  if (var18 < 0) {
                     var1.match.setBeginning(var9, var15);
                  }

                  return var18;
               }

               if (var1.match != null && var9 < 0) {
                  var15 = -var9;
                  var18 = var1.match.getEnd(var15);
                  var1.match.setEnd(var15, var3);
                  int var12 = this.matchCharArray(var1, var2.next, var3, var4, var5);
                  if (var12 < 0) {
                     var1.match.setEnd(var15, var18);
                  }

                  return var12;
               }

               var2 = var2.next;
               break;
            case 16:
               var8 = var2.getData();
               if (var8 > 0 && var8 < this.nofparen) {
                  if (var1.match.getBeginning(var8) >= 0 && var1.match.getEnd(var8) >= 0) {
                     var9 = var1.match.getBeginning(var8);
                     var15 = var1.match.getEnd(var8) - var9;
                     if (!isSet(var5, 2)) {
                        if (var4 > 0) {
                           if (!regionMatches(var6, var3, var1.limit, var9, var15)) {
                              return -1;
                           }

                           var3 += var15;
                        } else {
                           if (!regionMatches(var6, var3 - var15, var1.limit, var9, var15)) {
                              return -1;
                           }

                           var3 -= var15;
                        }
                     } else if (var4 > 0) {
                        if (!regionMatchesIgnoreCase(var6, var3, var1.limit, var9, var15)) {
                           return -1;
                        }

                        var3 += var15;
                     } else {
                        if (!regionMatchesIgnoreCase(var6, var3 - var15, var1.limit, var9, var15)) {
                           return -1;
                        }

                        var3 -= var15;
                     }

                     var2 = var2.next;
                     break;
                  }

                  return -1;
               }

               throw new RuntimeException("Internal Error: Reference number must be more than zero: " + var8);
            case 20:
               if (0 > this.matchCharArray(var1, var2.getChild(), var3, 1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 21:
               if (0 <= this.matchCharArray(var1, var2.getChild(), var3, 1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 22:
               if (0 > this.matchCharArray(var1, var2.getChild(), var3, -1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 23:
               if (0 <= this.matchCharArray(var1, var2.getChild(), var3, -1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 24:
               var15 = this.matchCharArray(var1, var2.getChild(), var3, var4, var5);
               if (var15 < 0) {
                  return var15;
               }

               var3 = var15;
               var2 = var2.next;
               break;
            case 25:
               var15 = var5 | var2.getData();
               var15 &= ~var2.getData2();
               var18 = this.matchCharArray(var1, var2.getChild(), var3, var4, var15);
               if (var18 < 0) {
                  return var18;
               }

               var3 = var18;
               var2 = var2.next;
               break;
            case 26:
               Op.ConditionOp var10 = (Op.ConditionOp)var2;
               boolean var11 = false;
               if (var10.refNumber > 0) {
                  if (var10.refNumber >= this.nofparen) {
                     throw new RuntimeException("Internal Error: Reference number must be more than zero: " + var10.refNumber);
                  }

                  var11 = var1.match.getBeginning(var10.refNumber) >= 0 && var1.match.getEnd(var10.refNumber) >= 0;
               } else {
                  var11 = 0 <= this.matchCharArray(var1, var10.condition, var3, var4, var5);
               }

               if (var11) {
                  var2 = var10.yes;
               } else if (var10.no != null) {
                  var2 = var10.no;
               } else {
                  var2 = var10.next;
               }
         }
      }

      return isSet(var5, 512) && var3 != var1.limit ? -1 : var3;
   }

   private static final int getPreviousWordType(char[] var0, int var1, int var2, int var3, int var4) {
      --var3;

      int var5;
      for(var5 = getWordType(var0, var1, var2, var3, var4); var5 == 0; var5 = getWordType(var0, var1, var2, var3, var4)) {
         --var3;
      }

      return var5;
   }

   private static final int getWordType(char[] var0, int var1, int var2, int var3, int var4) {
      return var3 >= var1 && var3 < var2 ? getWordType0(var0[var3], var4) : 2;
   }

   private static final boolean regionMatches(char[] var0, int var1, int var2, String var3, int var4) {
      if (var1 < 0) {
         return false;
      } else if (var2 - var1 < var4) {
         return false;
      } else {
         int var5 = 0;

         while(var4-- > 0) {
            if (var0[var1++] != var3.charAt(var5++)) {
               return false;
            }
         }

         return true;
      }
   }

   private static final boolean regionMatches(char[] var0, int var1, int var2, int var3, int var4) {
      if (var1 < 0) {
         return false;
      } else if (var2 - var1 < var4) {
         return false;
      } else {
         int var5 = var3;

         while(var4-- > 0) {
            if (var0[var1++] != var0[var5++]) {
               return false;
            }
         }

         return true;
      }
   }

   private static final boolean regionMatchesIgnoreCase(char[] var0, int var1, int var2, String var3, int var4) {
      if (var1 < 0) {
         return false;
      } else if (var2 - var1 < var4) {
         return false;
      } else {
         int var5 = 0;

         while(var4-- > 0) {
            char var6 = var0[var1++];
            char var7 = var3.charAt(var5++);
            if (var6 != var7) {
               char var8 = Character.toUpperCase(var6);
               char var9 = Character.toUpperCase(var7);
               if (var8 != var9 && Character.toLowerCase(var8) != Character.toLowerCase(var9)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private static final boolean regionMatchesIgnoreCase(char[] var0, int var1, int var2, int var3, int var4) {
      if (var1 < 0) {
         return false;
      } else if (var2 - var1 < var4) {
         return false;
      } else {
         int var5 = var3;

         while(var4-- > 0) {
            char var6 = var0[var1++];
            char var7 = var0[var5++];
            if (var6 != var7) {
               char var8 = Character.toUpperCase(var6);
               char var9 = Character.toUpperCase(var7);
               if (var8 != var9 && Character.toLowerCase(var8) != Character.toLowerCase(var9)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public boolean matches(String var1) {
      return this.matches((String)var1, 0, var1.length(), (Match)null);
   }

   public boolean matches(String var1, int var2, int var3) {
      return this.matches(var1, var2, var3, (Match)null);
   }

   public boolean matches(String var1, Match var2) {
      return this.matches((String)var1, 0, var1.length(), var2);
   }

   public boolean matches(String var1, int var2, int var3, Match var4) {
      synchronized(this) {
         if (this.operations == null) {
            this.prepare();
         }

         if (this.context == null) {
            this.context = new Context();
         }
      }

      Context var6 = null;
      Context var7 = this.context;
      synchronized(var7) {
         var6 = this.context.inuse ? new Context() : this.context;
         var6.reset(var1, var2, var3, this.numberOfClosures);
      }

      if (var4 != null) {
         var4.setNumberOfGroups(this.nofparen);
         var4.setSource(var1);
      } else if (this.hasBackReferences) {
         var4 = new Match();
         var4.setNumberOfGroups(this.nofparen);
      }

      var6.match = var4;
      int var8;
      if (isSet(this.options, 512)) {
         var8 = this.matchString(var6, this.operations, var6.start, 1, this.options);
         if (var8 == var6.limit) {
            if (var6.match != null) {
               var6.match.setBeginning(0, var6.start);
               var6.match.setEnd(0, var8);
            }

            var6.inuse = false;
            return true;
         } else {
            return false;
         }
      } else if (this.fixedStringOnly) {
         var8 = this.fixedStringTable.matches(var1, var6.start, var6.limit);
         if (var8 >= 0) {
            if (var6.match != null) {
               var6.match.setBeginning(0, var8);
               var6.match.setEnd(0, var8 + this.fixedString.length());
            }

            var6.inuse = false;
            return true;
         } else {
            var6.inuse = false;
            return false;
         }
      } else {
         if (this.fixedString != null) {
            var8 = this.fixedStringTable.matches(var1, var6.start, var6.limit);
            if (var8 < 0) {
               var6.inuse = false;
               return false;
            }
         }

         var8 = var6.limit - this.minlength;
         int var10 = -1;
         int var9;
         if (this.operations != null && this.operations.type == 7 && this.operations.getChild().type == 0) {
            if (isSet(this.options, 4)) {
               var9 = var6.start;
               var10 = this.matchString(var6, this.operations, var6.start, 1, this.options);
            } else {
               boolean var16 = true;

               for(var9 = var6.start; var9 <= var8; ++var9) {
                  char var17 = var1.charAt(var9);
                  if (isEOLChar(var17)) {
                     var16 = true;
                  } else {
                     if (var16 && 0 <= (var10 = this.matchString(var6, this.operations, var9, 1, this.options))) {
                        break;
                     }

                     var16 = false;
                  }
               }
            }
         } else if (this.firstChar != null) {
            RangeToken var11 = this.firstChar;
            int var12;
            if (isSet(this.options, 2)) {
               var11 = this.firstChar.getCaseInsensitiveToken();

               for(var9 = var6.start; var9 <= var8; ++var9) {
                  var12 = var1.charAt(var9);
                  if (REUtil.isHighSurrogate(var12) && var9 + 1 < var6.limit) {
                     var12 = REUtil.composeFromSurrogates(var12, var1.charAt(var9 + 1));
                     if (!var11.match(var12)) {
                        continue;
                     }
                  } else if (!var11.match(var12)) {
                     char var13 = Character.toUpperCase((char)var12);
                     if (!var11.match(var13) && !var11.match(Character.toLowerCase(var13))) {
                        continue;
                     }
                  }

                  if (0 <= (var10 = this.matchString(var6, this.operations, var9, 1, this.options))) {
                     break;
                  }
               }
            } else {
               for(var9 = var6.start; var9 <= var8; ++var9) {
                  var12 = var1.charAt(var9);
                  if (REUtil.isHighSurrogate(var12) && var9 + 1 < var6.limit) {
                     var12 = REUtil.composeFromSurrogates(var12, var1.charAt(var9 + 1));
                  }

                  if (var11.match(var12) && 0 <= (var10 = this.matchString(var6, this.operations, var9, 1, this.options))) {
                     break;
                  }
               }
            }
         } else {
            for(var9 = var6.start; var9 <= var8 && 0 > (var10 = this.matchString(var6, this.operations, var9, 1, this.options)); ++var9) {
            }
         }

         if (var10 >= 0) {
            if (var6.match != null) {
               var6.match.setBeginning(0, var9);
               var6.match.setEnd(0, var10);
            }

            var6.inuse = false;
            return true;
         } else {
            var6.inuse = false;
            return false;
         }
      }
   }

   private int matchString(Context var1, Op var2, int var3, int var4, int var5) {
      String var6 = var1.strTarget;

      while(var2 != null) {
         if (var3 > var1.limit || var3 < var1.start) {
            return -1;
         }

         int var8;
         int var9;
         int var13;
         int var15;
         int var18;
         switch (var2.type) {
            case 0:
               if (var4 > 0) {
                  if (var3 >= var1.limit) {
                     return -1;
                  }

                  var13 = var6.charAt(var3);
                  if (isSet(var5, 4)) {
                     if (REUtil.isHighSurrogate(var13) && var3 + 1 < var1.limit) {
                        ++var3;
                     }
                  } else {
                     if (REUtil.isHighSurrogate(var13) && var3 + 1 < var1.limit) {
                        ++var3;
                        var13 = REUtil.composeFromSurrogates(var13, var6.charAt(var3));
                     }

                     if (isEOLChar(var13)) {
                        return -1;
                     }
                  }

                  ++var3;
               } else {
                  var13 = var3 - 1;
                  if (var13 >= var1.limit || var13 < 0) {
                     return -1;
                  }

                  var8 = var6.charAt(var13);
                  if (isSet(var5, 4)) {
                     if (REUtil.isLowSurrogate(var8) && var13 - 1 >= 0) {
                        --var13;
                     }
                  } else {
                     if (REUtil.isLowSurrogate(var8) && var13 - 1 >= 0) {
                        --var13;
                        var8 = REUtil.composeFromSurrogates(var6.charAt(var13), var8);
                     }

                     if (!isEOLChar(var8)) {
                        return -1;
                     }
                  }

                  var3 = var13;
               }

               var2 = var2.next;
               break;
            case 1:
               if (isSet(var5, 2)) {
                  var13 = var2.getData();
                  if (var4 > 0) {
                     if (var3 >= var1.limit || !matchIgnoreCase(var13, var6.charAt(var3))) {
                        return -1;
                     }

                     ++var3;
                  } else {
                     var8 = var3 - 1;
                     if (var8 >= var1.limit || var8 < 0 || !matchIgnoreCase(var13, var6.charAt(var8))) {
                        return -1;
                     }

                     var3 = var8;
                  }
               } else {
                  var13 = var2.getData();
                  if (var4 > 0) {
                     if (var3 >= var1.limit || var13 != var6.charAt(var3)) {
                        return -1;
                     }

                     ++var3;
                  } else {
                     var8 = var3 - 1;
                     if (var8 >= var1.limit || var8 < 0 || var13 != var6.charAt(var8)) {
                        return -1;
                     }

                     var3 = var8;
                  }
               }

               var2 = var2.next;
               break;
            case 2:
            case 12:
            case 13:
            case 14:
            case 17:
            case 18:
            case 19:
            default:
               throw new RuntimeException("Unknown operation type: " + var2.type);
            case 3:
            case 4:
               if (var4 > 0) {
                  if (var3 >= var1.limit) {
                     return -1;
                  }

                  var13 = var6.charAt(var3);
                  if (REUtil.isHighSurrogate(var13) && var3 + 1 < var1.limit) {
                     ++var3;
                     var13 = REUtil.composeFromSurrogates(var13, var6.charAt(var3));
                  }

                  RangeToken var19 = var2.getToken();
                  if (isSet(var5, 2)) {
                     var19 = var19.getCaseInsensitiveToken();
                     if (!var19.match(var13)) {
                        if (var13 >= 65536) {
                           return -1;
                        }

                        char var17;
                        if (!var19.match(var17 = Character.toUpperCase((char)var13)) && !var19.match(Character.toLowerCase(var17))) {
                           return -1;
                        }
                     }
                  } else if (!var19.match(var13)) {
                     return -1;
                  }

                  ++var3;
               } else {
                  var13 = var3 - 1;
                  if (var13 >= var1.limit || var13 < 0) {
                     return -1;
                  }

                  var8 = var6.charAt(var13);
                  if (REUtil.isLowSurrogate(var8) && var13 - 1 >= 0) {
                     --var13;
                     var8 = REUtil.composeFromSurrogates(var6.charAt(var13), var8);
                  }

                  RangeToken var16 = var2.getToken();
                  if (isSet(var5, 2)) {
                     var16 = var16.getCaseInsensitiveToken();
                     if (!var16.match(var8)) {
                        if (var8 >= 65536) {
                           return -1;
                        }

                        char var20;
                        if (!var16.match(var20 = Character.toUpperCase((char)var8)) && !var16.match(Character.toLowerCase(var20))) {
                           return -1;
                        }
                     }
                  } else if (!var16.match(var8)) {
                     return -1;
                  }

                  var3 = var13;
               }

               var2 = var2.next;
               break;
            case 5:
               boolean var7 = false;
               switch (var2.getData()) {
                  case 36:
                     if (isSet(var5, 8)) {
                        if (var3 == var1.limit || var3 < var1.limit && isEOLChar(var6.charAt(var3))) {
                           break;
                        }

                        return -1;
                     } else {
                        if (var3 != var1.limit && (var3 + 1 != var1.limit || !isEOLChar(var6.charAt(var3))) && (var3 + 2 != var1.limit || var6.charAt(var3) != '\r' || var6.charAt(var3 + 1) != '\n')) {
                           return -1;
                        }
                        break;
                     }
                  case 60:
                     if (var1.length == 0 || var3 == var1.limit) {
                        return -1;
                     }

                     if (getWordType(var6, var1.start, var1.limit, var3, var5) != 1 || getPreviousWordType(var6, var1.start, var1.limit, var3, var5) != 2) {
                        return -1;
                     }
                     break;
                  case 62:
                     if (var1.length == 0 || var3 == var1.start) {
                        return -1;
                     }

                     if (getWordType(var6, var1.start, var1.limit, var3, var5) != 2 || getPreviousWordType(var6, var1.start, var1.limit, var3, var5) != 1) {
                        return -1;
                     }
                     break;
                  case 64:
                     if (var3 != var1.start && (var3 <= var1.start || !isEOLChar(var6.charAt(var3 - 1)))) {
                        return -1;
                     }
                     break;
                  case 65:
                     if (var3 != var1.start) {
                        return -1;
                     }
                     break;
                  case 66:
                     if (var1.length == 0) {
                        var7 = true;
                     } else {
                        var8 = getWordType(var6, var1.start, var1.limit, var3, var5);
                        var7 = var8 == 0 || var8 == getPreviousWordType(var6, var1.start, var1.limit, var3, var5);
                     }

                     if (!var7) {
                        return -1;
                     }
                     break;
                  case 90:
                     if (var3 == var1.limit || var3 + 1 == var1.limit && isEOLChar(var6.charAt(var3)) || var3 + 2 == var1.limit && var6.charAt(var3) == '\r' && var6.charAt(var3 + 1) == '\n') {
                        break;
                     }

                     return -1;
                  case 94:
                     if (isSet(var5, 8)) {
                        if (var3 != var1.start && (var3 <= var1.start || !isEOLChar(var6.charAt(var3 - 1)))) {
                           return -1;
                        }
                     } else if (var3 != var1.start) {
                        return -1;
                     }
                     break;
                  case 98:
                     if (var1.length == 0) {
                        return -1;
                     }

                     var8 = getWordType(var6, var1.start, var1.limit, var3, var5);
                     if (var8 == 0) {
                        return -1;
                     }

                     var9 = getPreviousWordType(var6, var1.start, var1.limit, var3, var5);
                     if (var8 == var9) {
                        return -1;
                     }
                     break;
                  case 122:
                     if (var3 != var1.limit) {
                        return -1;
                     }
               }

               var2 = var2.next;
               break;
            case 6:
               String var14 = var2.getString();
               var9 = var14.length();
               if (!isSet(var5, 2)) {
                  if (var4 > 0) {
                     if (!regionMatches(var6, var3, var1.limit, var14, var9)) {
                        return -1;
                     }

                     var3 += var9;
                  } else {
                     if (!regionMatches(var6, var3 - var9, var1.limit, var14, var9)) {
                        return -1;
                     }

                     var3 -= var9;
                  }
               } else if (var4 > 0) {
                  if (!regionMatchesIgnoreCase(var6, var3, var1.limit, var14, var9)) {
                     return -1;
                  }

                  var3 += var9;
               } else {
                  if (!regionMatchesIgnoreCase(var6, var3 - var9, var1.limit, var14, var9)) {
                     return -1;
                  }

                  var3 -= var9;
               }

               var2 = var2.next;
               break;
            case 7:
               var8 = var2.getData();
               if (var8 >= 0) {
                  var9 = var1.offsets[var8];
                  if (var9 >= 0 && var9 == var3) {
                     var1.offsets[var8] = -1;
                     var2 = var2.next;
                     break;
                  }

                  var1.offsets[var8] = var3;
               }

               var9 = this.matchString(var1, var2.getChild(), var3, var4, var5);
               if (var8 >= 0) {
                  var1.offsets[var8] = -1;
               }

               if (var9 >= 0) {
                  return var9;
               }

               var2 = var2.next;
               break;
            case 8:
            case 10:
               var8 = this.matchString(var1, var2.next, var3, var4, var5);
               if (var8 >= 0) {
                  return var8;
               }

               var2 = var2.getChild();
               break;
            case 9:
               var8 = this.matchString(var1, var2.getChild(), var3, var4, var5);
               if (var8 >= 0) {
                  return var8;
               }

               var2 = var2.next;
               break;
            case 11:
               for(var8 = 0; var8 < var2.size(); ++var8) {
                  var9 = this.matchString(var1, var2.elementAt(var8), var3, var4, var5);
                  if (var9 >= 0) {
                     return var9;
                  }
               }

               return -1;
            case 15:
               var9 = var2.getData();
               if (var1.match != null && var9 > 0) {
                  var15 = var1.match.getBeginning(var9);
                  var1.match.setBeginning(var9, var3);
                  var18 = this.matchString(var1, var2.next, var3, var4, var5);
                  if (var18 < 0) {
                     var1.match.setBeginning(var9, var15);
                  }

                  return var18;
               }

               if (var1.match != null && var9 < 0) {
                  var15 = -var9;
                  var18 = var1.match.getEnd(var15);
                  var1.match.setEnd(var15, var3);
                  int var12 = this.matchString(var1, var2.next, var3, var4, var5);
                  if (var12 < 0) {
                     var1.match.setEnd(var15, var18);
                  }

                  return var12;
               }

               var2 = var2.next;
               break;
            case 16:
               var8 = var2.getData();
               if (var8 > 0 && var8 < this.nofparen) {
                  if (var1.match.getBeginning(var8) >= 0 && var1.match.getEnd(var8) >= 0) {
                     var9 = var1.match.getBeginning(var8);
                     var15 = var1.match.getEnd(var8) - var9;
                     if (!isSet(var5, 2)) {
                        if (var4 > 0) {
                           if (!regionMatches(var6, var3, var1.limit, var9, var15)) {
                              return -1;
                           }

                           var3 += var15;
                        } else {
                           if (!regionMatches(var6, var3 - var15, var1.limit, var9, var15)) {
                              return -1;
                           }

                           var3 -= var15;
                        }
                     } else if (var4 > 0) {
                        if (!regionMatchesIgnoreCase(var6, var3, var1.limit, var9, var15)) {
                           return -1;
                        }

                        var3 += var15;
                     } else {
                        if (!regionMatchesIgnoreCase(var6, var3 - var15, var1.limit, var9, var15)) {
                           return -1;
                        }

                        var3 -= var15;
                     }

                     var2 = var2.next;
                     break;
                  }

                  return -1;
               }

               throw new RuntimeException("Internal Error: Reference number must be more than zero: " + var8);
            case 20:
               if (0 > this.matchString(var1, var2.getChild(), var3, 1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 21:
               if (0 <= this.matchString(var1, var2.getChild(), var3, 1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 22:
               if (0 > this.matchString(var1, var2.getChild(), var3, -1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 23:
               if (0 <= this.matchString(var1, var2.getChild(), var3, -1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 24:
               var15 = this.matchString(var1, var2.getChild(), var3, var4, var5);
               if (var15 < 0) {
                  return var15;
               }

               var3 = var15;
               var2 = var2.next;
               break;
            case 25:
               var15 = var5 | var2.getData();
               var15 &= ~var2.getData2();
               var18 = this.matchString(var1, var2.getChild(), var3, var4, var15);
               if (var18 < 0) {
                  return var18;
               }

               var3 = var18;
               var2 = var2.next;
               break;
            case 26:
               Op.ConditionOp var10 = (Op.ConditionOp)var2;
               boolean var11 = false;
               if (var10.refNumber > 0) {
                  if (var10.refNumber >= this.nofparen) {
                     throw new RuntimeException("Internal Error: Reference number must be more than zero: " + var10.refNumber);
                  }

                  var11 = var1.match.getBeginning(var10.refNumber) >= 0 && var1.match.getEnd(var10.refNumber) >= 0;
               } else {
                  var11 = 0 <= this.matchString(var1, var10.condition, var3, var4, var5);
               }

               if (var11) {
                  var2 = var10.yes;
               } else if (var10.no != null) {
                  var2 = var10.no;
               } else {
                  var2 = var10.next;
               }
         }
      }

      return isSet(var5, 512) && var3 != var1.limit ? -1 : var3;
   }

   private static final int getPreviousWordType(String var0, int var1, int var2, int var3, int var4) {
      --var3;

      int var5;
      for(var5 = getWordType(var0, var1, var2, var3, var4); var5 == 0; var5 = getWordType(var0, var1, var2, var3, var4)) {
         --var3;
      }

      return var5;
   }

   private static final int getWordType(String var0, int var1, int var2, int var3, int var4) {
      return var3 >= var1 && var3 < var2 ? getWordType0(var0.charAt(var3), var4) : 2;
   }

   private static final boolean regionMatches(String var0, int var1, int var2, String var3, int var4) {
      return var2 - var1 < var4 ? false : var0.regionMatches(var1, var3, 0, var4);
   }

   private static final boolean regionMatches(String var0, int var1, int var2, int var3, int var4) {
      return var2 - var1 < var4 ? false : var0.regionMatches(var1, var0, var3, var4);
   }

   private static final boolean regionMatchesIgnoreCase(String var0, int var1, int var2, String var3, int var4) {
      return var0.regionMatches(true, var1, var3, 0, var4);
   }

   private static final boolean regionMatchesIgnoreCase(String var0, int var1, int var2, int var3, int var4) {
      return var2 - var1 < var4 ? false : var0.regionMatches(true, var1, var0, var3, var4);
   }

   public boolean matches(CharacterIterator var1) {
      return this.matches(var1, (Match)null);
   }

   public boolean matches(CharacterIterator var1, Match var2) {
      int var3 = var1.getBeginIndex();
      int var4 = var1.getEndIndex();
      synchronized(this) {
         if (this.operations == null) {
            this.prepare();
         }

         if (this.context == null) {
            this.context = new Context();
         }
      }

      Context var6 = null;
      Context var7 = this.context;
      synchronized(var7) {
         var6 = this.context.inuse ? new Context() : this.context;
         var6.reset(var1, var3, var4, this.numberOfClosures);
      }

      if (var2 != null) {
         var2.setNumberOfGroups(this.nofparen);
         var2.setSource(var1);
      } else if (this.hasBackReferences) {
         var2 = new Match();
         var2.setNumberOfGroups(this.nofparen);
      }

      var6.match = var2;
      int var8;
      if (isSet(this.options, 512)) {
         var8 = this.matchCharacterIterator(var6, this.operations, var6.start, 1, this.options);
         if (var8 == var6.limit) {
            if (var6.match != null) {
               var6.match.setBeginning(0, var6.start);
               var6.match.setEnd(0, var8);
            }

            var6.inuse = false;
            return true;
         } else {
            return false;
         }
      } else if (this.fixedStringOnly) {
         var8 = this.fixedStringTable.matches(var1, var6.start, var6.limit);
         if (var8 >= 0) {
            if (var6.match != null) {
               var6.match.setBeginning(0, var8);
               var6.match.setEnd(0, var8 + this.fixedString.length());
            }

            var6.inuse = false;
            return true;
         } else {
            var6.inuse = false;
            return false;
         }
      } else {
         if (this.fixedString != null) {
            var8 = this.fixedStringTable.matches(var1, var6.start, var6.limit);
            if (var8 < 0) {
               var6.inuse = false;
               return false;
            }
         }

         var8 = var6.limit - this.minlength;
         int var10 = -1;
         int var9;
         if (this.operations != null && this.operations.type == 7 && this.operations.getChild().type == 0) {
            if (isSet(this.options, 4)) {
               var9 = var6.start;
               var10 = this.matchCharacterIterator(var6, this.operations, var6.start, 1, this.options);
            } else {
               boolean var16 = true;

               for(var9 = var6.start; var9 <= var8; ++var9) {
                  char var17 = var1.setIndex(var9);
                  if (isEOLChar(var17)) {
                     var16 = true;
                  } else {
                     if (var16 && 0 <= (var10 = this.matchCharacterIterator(var6, this.operations, var9, 1, this.options))) {
                        break;
                     }

                     var16 = false;
                  }
               }
            }
         } else if (this.firstChar != null) {
            RangeToken var11 = this.firstChar;
            int var12;
            if (isSet(this.options, 2)) {
               var11 = this.firstChar.getCaseInsensitiveToken();

               for(var9 = var6.start; var9 <= var8; ++var9) {
                  var12 = var1.setIndex(var9);
                  if (REUtil.isHighSurrogate(var12) && var9 + 1 < var6.limit) {
                     var12 = REUtil.composeFromSurrogates(var12, var1.setIndex(var9 + 1));
                     if (!var11.match(var12)) {
                        continue;
                     }
                  } else if (!var11.match(var12)) {
                     char var13 = Character.toUpperCase((char)var12);
                     if (!var11.match(var13) && !var11.match(Character.toLowerCase(var13))) {
                        continue;
                     }
                  }

                  if (0 <= (var10 = this.matchCharacterIterator(var6, this.operations, var9, 1, this.options))) {
                     break;
                  }
               }
            } else {
               for(var9 = var6.start; var9 <= var8; ++var9) {
                  var12 = var1.setIndex(var9);
                  if (REUtil.isHighSurrogate(var12) && var9 + 1 < var6.limit) {
                     var12 = REUtil.composeFromSurrogates(var12, var1.setIndex(var9 + 1));
                  }

                  if (var11.match(var12) && 0 <= (var10 = this.matchCharacterIterator(var6, this.operations, var9, 1, this.options))) {
                     break;
                  }
               }
            }
         } else {
            for(var9 = var6.start; var9 <= var8 && 0 > (var10 = this.matchCharacterIterator(var6, this.operations, var9, 1, this.options)); ++var9) {
            }
         }

         if (var10 >= 0) {
            if (var6.match != null) {
               var6.match.setBeginning(0, var9);
               var6.match.setEnd(0, var10);
            }

            var6.inuse = false;
            return true;
         } else {
            var6.inuse = false;
            return false;
         }
      }
   }

   private int matchCharacterIterator(Context var1, Op var2, int var3, int var4, int var5) {
      CharacterIterator var6 = var1.ciTarget;

      while(var2 != null) {
         if (var3 > var1.limit || var3 < var1.start) {
            return -1;
         }

         int var8;
         int var9;
         int var13;
         int var15;
         int var18;
         switch (var2.type) {
            case 0:
               if (var4 > 0) {
                  if (var3 >= var1.limit) {
                     return -1;
                  }

                  var13 = var6.setIndex(var3);
                  if (isSet(var5, 4)) {
                     if (REUtil.isHighSurrogate(var13) && var3 + 1 < var1.limit) {
                        ++var3;
                     }
                  } else {
                     if (REUtil.isHighSurrogate(var13) && var3 + 1 < var1.limit) {
                        ++var3;
                        var13 = REUtil.composeFromSurrogates(var13, var6.setIndex(var3));
                     }

                     if (isEOLChar(var13)) {
                        return -1;
                     }
                  }

                  ++var3;
               } else {
                  var13 = var3 - 1;
                  if (var13 >= var1.limit || var13 < 0) {
                     return -1;
                  }

                  var8 = var6.setIndex(var13);
                  if (isSet(var5, 4)) {
                     if (REUtil.isLowSurrogate(var8) && var13 - 1 >= 0) {
                        --var13;
                     }
                  } else {
                     if (REUtil.isLowSurrogate(var8) && var13 - 1 >= 0) {
                        --var13;
                        var8 = REUtil.composeFromSurrogates(var6.setIndex(var13), var8);
                     }

                     if (!isEOLChar(var8)) {
                        return -1;
                     }
                  }

                  var3 = var13;
               }

               var2 = var2.next;
               break;
            case 1:
               if (isSet(var5, 2)) {
                  var13 = var2.getData();
                  if (var4 > 0) {
                     if (var3 >= var1.limit || !matchIgnoreCase(var13, var6.setIndex(var3))) {
                        return -1;
                     }

                     ++var3;
                  } else {
                     var8 = var3 - 1;
                     if (var8 >= var1.limit || var8 < 0 || !matchIgnoreCase(var13, var6.setIndex(var8))) {
                        return -1;
                     }

                     var3 = var8;
                  }
               } else {
                  var13 = var2.getData();
                  if (var4 > 0) {
                     if (var3 >= var1.limit || var13 != var6.setIndex(var3)) {
                        return -1;
                     }

                     ++var3;
                  } else {
                     var8 = var3 - 1;
                     if (var8 >= var1.limit || var8 < 0 || var13 != var6.setIndex(var8)) {
                        return -1;
                     }

                     var3 = var8;
                  }
               }

               var2 = var2.next;
               break;
            case 2:
            case 12:
            case 13:
            case 14:
            case 17:
            case 18:
            case 19:
            default:
               throw new RuntimeException("Unknown operation type: " + var2.type);
            case 3:
            case 4:
               if (var4 > 0) {
                  if (var3 >= var1.limit) {
                     return -1;
                  }

                  var13 = var6.setIndex(var3);
                  if (REUtil.isHighSurrogate(var13) && var3 + 1 < var1.limit) {
                     ++var3;
                     var13 = REUtil.composeFromSurrogates(var13, var6.setIndex(var3));
                  }

                  RangeToken var19 = var2.getToken();
                  if (isSet(var5, 2)) {
                     var19 = var19.getCaseInsensitiveToken();
                     if (!var19.match(var13)) {
                        if (var13 >= 65536) {
                           return -1;
                        }

                        char var17;
                        if (!var19.match(var17 = Character.toUpperCase((char)var13)) && !var19.match(Character.toLowerCase(var17))) {
                           return -1;
                        }
                     }
                  } else if (!var19.match(var13)) {
                     return -1;
                  }

                  ++var3;
               } else {
                  var13 = var3 - 1;
                  if (var13 >= var1.limit || var13 < 0) {
                     return -1;
                  }

                  var8 = var6.setIndex(var13);
                  if (REUtil.isLowSurrogate(var8) && var13 - 1 >= 0) {
                     --var13;
                     var8 = REUtil.composeFromSurrogates(var6.setIndex(var13), var8);
                  }

                  RangeToken var16 = var2.getToken();
                  if (isSet(var5, 2)) {
                     var16 = var16.getCaseInsensitiveToken();
                     if (!var16.match(var8)) {
                        if (var8 >= 65536) {
                           return -1;
                        }

                        char var20;
                        if (!var16.match(var20 = Character.toUpperCase((char)var8)) && !var16.match(Character.toLowerCase(var20))) {
                           return -1;
                        }
                     }
                  } else if (!var16.match(var8)) {
                     return -1;
                  }

                  var3 = var13;
               }

               var2 = var2.next;
               break;
            case 5:
               boolean var7 = false;
               switch (var2.getData()) {
                  case 36:
                     if (isSet(var5, 8)) {
                        if (var3 == var1.limit || var3 < var1.limit && isEOLChar(var6.setIndex(var3))) {
                           break;
                        }

                        return -1;
                     } else {
                        if (var3 != var1.limit && (var3 + 1 != var1.limit || !isEOLChar(var6.setIndex(var3))) && (var3 + 2 != var1.limit || var6.setIndex(var3) != '\r' || var6.setIndex(var3 + 1) != '\n')) {
                           return -1;
                        }
                        break;
                     }
                  case 60:
                     if (var1.length == 0 || var3 == var1.limit) {
                        return -1;
                     }

                     if (getWordType(var6, var1.start, var1.limit, var3, var5) != 1 || getPreviousWordType(var6, var1.start, var1.limit, var3, var5) != 2) {
                        return -1;
                     }
                     break;
                  case 62:
                     if (var1.length == 0 || var3 == var1.start) {
                        return -1;
                     }

                     if (getWordType(var6, var1.start, var1.limit, var3, var5) != 2 || getPreviousWordType(var6, var1.start, var1.limit, var3, var5) != 1) {
                        return -1;
                     }
                     break;
                  case 64:
                     if (var3 != var1.start && (var3 <= var1.start || !isEOLChar(var6.setIndex(var3 - 1)))) {
                        return -1;
                     }
                     break;
                  case 65:
                     if (var3 != var1.start) {
                        return -1;
                     }
                     break;
                  case 66:
                     if (var1.length == 0) {
                        var7 = true;
                     } else {
                        var8 = getWordType(var6, var1.start, var1.limit, var3, var5);
                        var7 = var8 == 0 || var8 == getPreviousWordType(var6, var1.start, var1.limit, var3, var5);
                     }

                     if (!var7) {
                        return -1;
                     }
                     break;
                  case 90:
                     if (var3 == var1.limit || var3 + 1 == var1.limit && isEOLChar(var6.setIndex(var3)) || var3 + 2 == var1.limit && var6.setIndex(var3) == '\r' && var6.setIndex(var3 + 1) == '\n') {
                        break;
                     }

                     return -1;
                  case 94:
                     if (isSet(var5, 8)) {
                        if (var3 != var1.start && (var3 <= var1.start || !isEOLChar(var6.setIndex(var3 - 1)))) {
                           return -1;
                        }
                     } else if (var3 != var1.start) {
                        return -1;
                     }
                     break;
                  case 98:
                     if (var1.length == 0) {
                        return -1;
                     }

                     var8 = getWordType(var6, var1.start, var1.limit, var3, var5);
                     if (var8 == 0) {
                        return -1;
                     }

                     var9 = getPreviousWordType(var6, var1.start, var1.limit, var3, var5);
                     if (var8 == var9) {
                        return -1;
                     }
                     break;
                  case 122:
                     if (var3 != var1.limit) {
                        return -1;
                     }
               }

               var2 = var2.next;
               break;
            case 6:
               String var14 = var2.getString();
               var9 = var14.length();
               if (!isSet(var5, 2)) {
                  if (var4 > 0) {
                     if (!regionMatches(var6, var3, var1.limit, var14, var9)) {
                        return -1;
                     }

                     var3 += var9;
                  } else {
                     if (!regionMatches(var6, var3 - var9, var1.limit, var14, var9)) {
                        return -1;
                     }

                     var3 -= var9;
                  }
               } else if (var4 > 0) {
                  if (!regionMatchesIgnoreCase(var6, var3, var1.limit, var14, var9)) {
                     return -1;
                  }

                  var3 += var9;
               } else {
                  if (!regionMatchesIgnoreCase(var6, var3 - var9, var1.limit, var14, var9)) {
                     return -1;
                  }

                  var3 -= var9;
               }

               var2 = var2.next;
               break;
            case 7:
               var8 = var2.getData();
               if (var8 >= 0) {
                  var9 = var1.offsets[var8];
                  if (var9 >= 0 && var9 == var3) {
                     var1.offsets[var8] = -1;
                     var2 = var2.next;
                     break;
                  }

                  var1.offsets[var8] = var3;
               }

               var9 = this.matchCharacterIterator(var1, var2.getChild(), var3, var4, var5);
               if (var8 >= 0) {
                  var1.offsets[var8] = -1;
               }

               if (var9 >= 0) {
                  return var9;
               }

               var2 = var2.next;
               break;
            case 8:
            case 10:
               var8 = this.matchCharacterIterator(var1, var2.next, var3, var4, var5);
               if (var8 >= 0) {
                  return var8;
               }

               var2 = var2.getChild();
               break;
            case 9:
               var8 = this.matchCharacterIterator(var1, var2.getChild(), var3, var4, var5);
               if (var8 >= 0) {
                  return var8;
               }

               var2 = var2.next;
               break;
            case 11:
               for(var8 = 0; var8 < var2.size(); ++var8) {
                  var9 = this.matchCharacterIterator(var1, var2.elementAt(var8), var3, var4, var5);
                  if (var9 >= 0) {
                     return var9;
                  }
               }

               return -1;
            case 15:
               var9 = var2.getData();
               if (var1.match != null && var9 > 0) {
                  var15 = var1.match.getBeginning(var9);
                  var1.match.setBeginning(var9, var3);
                  var18 = this.matchCharacterIterator(var1, var2.next, var3, var4, var5);
                  if (var18 < 0) {
                     var1.match.setBeginning(var9, var15);
                  }

                  return var18;
               }

               if (var1.match != null && var9 < 0) {
                  var15 = -var9;
                  var18 = var1.match.getEnd(var15);
                  var1.match.setEnd(var15, var3);
                  int var12 = this.matchCharacterIterator(var1, var2.next, var3, var4, var5);
                  if (var12 < 0) {
                     var1.match.setEnd(var15, var18);
                  }

                  return var12;
               }

               var2 = var2.next;
               break;
            case 16:
               var8 = var2.getData();
               if (var8 > 0 && var8 < this.nofparen) {
                  if (var1.match.getBeginning(var8) >= 0 && var1.match.getEnd(var8) >= 0) {
                     var9 = var1.match.getBeginning(var8);
                     var15 = var1.match.getEnd(var8) - var9;
                     if (!isSet(var5, 2)) {
                        if (var4 > 0) {
                           if (!regionMatches(var6, var3, var1.limit, var9, var15)) {
                              return -1;
                           }

                           var3 += var15;
                        } else {
                           if (!regionMatches(var6, var3 - var15, var1.limit, var9, var15)) {
                              return -1;
                           }

                           var3 -= var15;
                        }
                     } else if (var4 > 0) {
                        if (!regionMatchesIgnoreCase(var6, var3, var1.limit, var9, var15)) {
                           return -1;
                        }

                        var3 += var15;
                     } else {
                        if (!regionMatchesIgnoreCase(var6, var3 - var15, var1.limit, var9, var15)) {
                           return -1;
                        }

                        var3 -= var15;
                     }

                     var2 = var2.next;
                     break;
                  }

                  return -1;
               }

               throw new RuntimeException("Internal Error: Reference number must be more than zero: " + var8);
            case 20:
               if (0 > this.matchCharacterIterator(var1, var2.getChild(), var3, 1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 21:
               if (0 <= this.matchCharacterIterator(var1, var2.getChild(), var3, 1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 22:
               if (0 > this.matchCharacterIterator(var1, var2.getChild(), var3, -1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 23:
               if (0 <= this.matchCharacterIterator(var1, var2.getChild(), var3, -1, var5)) {
                  return -1;
               }

               var2 = var2.next;
               break;
            case 24:
               var15 = this.matchCharacterIterator(var1, var2.getChild(), var3, var4, var5);
               if (var15 < 0) {
                  return var15;
               }

               var3 = var15;
               var2 = var2.next;
               break;
            case 25:
               var15 = var5 | var2.getData();
               var15 &= ~var2.getData2();
               var18 = this.matchCharacterIterator(var1, var2.getChild(), var3, var4, var15);
               if (var18 < 0) {
                  return var18;
               }

               var3 = var18;
               var2 = var2.next;
               break;
            case 26:
               Op.ConditionOp var10 = (Op.ConditionOp)var2;
               boolean var11 = false;
               if (var10.refNumber > 0) {
                  if (var10.refNumber >= this.nofparen) {
                     throw new RuntimeException("Internal Error: Reference number must be more than zero: " + var10.refNumber);
                  }

                  var11 = var1.match.getBeginning(var10.refNumber) >= 0 && var1.match.getEnd(var10.refNumber) >= 0;
               } else {
                  var11 = 0 <= this.matchCharacterIterator(var1, var10.condition, var3, var4, var5);
               }

               if (var11) {
                  var2 = var10.yes;
               } else if (var10.no != null) {
                  var2 = var10.no;
               } else {
                  var2 = var10.next;
               }
         }
      }

      return isSet(var5, 512) && var3 != var1.limit ? -1 : var3;
   }

   private static final int getPreviousWordType(CharacterIterator var0, int var1, int var2, int var3, int var4) {
      --var3;

      int var5;
      for(var5 = getWordType(var0, var1, var2, var3, var4); var5 == 0; var5 = getWordType(var0, var1, var2, var3, var4)) {
         --var3;
      }

      return var5;
   }

   private static final int getWordType(CharacterIterator var0, int var1, int var2, int var3, int var4) {
      return var3 >= var1 && var3 < var2 ? getWordType0(var0.setIndex(var3), var4) : 2;
   }

   private static final boolean regionMatches(CharacterIterator var0, int var1, int var2, String var3, int var4) {
      if (var1 < 0) {
         return false;
      } else if (var2 - var1 < var4) {
         return false;
      } else {
         int var5 = 0;

         while(var4-- > 0) {
            if (var0.setIndex(var1++) != var3.charAt(var5++)) {
               return false;
            }
         }

         return true;
      }
   }

   private static final boolean regionMatches(CharacterIterator var0, int var1, int var2, int var3, int var4) {
      if (var1 < 0) {
         return false;
      } else if (var2 - var1 < var4) {
         return false;
      } else {
         int var5 = var3;

         while(var4-- > 0) {
            if (var0.setIndex(var1++) != var0.setIndex(var5++)) {
               return false;
            }
         }

         return true;
      }
   }

   private static final boolean regionMatchesIgnoreCase(CharacterIterator var0, int var1, int var2, String var3, int var4) {
      if (var1 < 0) {
         return false;
      } else if (var2 - var1 < var4) {
         return false;
      } else {
         int var5 = 0;

         while(var4-- > 0) {
            char var6 = var0.setIndex(var1++);
            char var7 = var3.charAt(var5++);
            if (var6 != var7) {
               char var8 = Character.toUpperCase(var6);
               char var9 = Character.toUpperCase(var7);
               if (var8 != var9 && Character.toLowerCase(var8) != Character.toLowerCase(var9)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private static final boolean regionMatchesIgnoreCase(CharacterIterator var0, int var1, int var2, int var3, int var4) {
      if (var1 < 0) {
         return false;
      } else if (var2 - var1 < var4) {
         return false;
      } else {
         int var5 = var3;

         while(var4-- > 0) {
            char var6 = var0.setIndex(var1++);
            char var7 = var0.setIndex(var5++);
            if (var6 != var7) {
               char var8 = Character.toUpperCase(var6);
               char var9 = Character.toUpperCase(var7);
               if (var8 != var9 && Character.toLowerCase(var8) != Character.toLowerCase(var9)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   void prepare() {
      this.compile(this.tokentree);
      this.minlength = this.tokentree.getMinLength();
      this.firstChar = null;
      if (!isSet(this.options, 128) && !isSet(this.options, 512)) {
         RangeToken var1 = Token.createRange();
         int var2 = this.tokentree.analyzeFirstCharacter(var1, this.options);
         if (var2 == 1) {
            var1.compactRanges();
            this.firstChar = var1;
         }
      }

      if (this.operations != null && (this.operations.type == 6 || this.operations.type == 1) && this.operations.next == null) {
         this.fixedStringOnly = true;
         if (this.operations.type == 6) {
            this.fixedString = this.operations.getString();
         } else if (this.operations.getData() >= 65536) {
            this.fixedString = REUtil.decomposeToSurrogates(this.operations.getData());
         } else {
            char[] var4 = new char[]{(char)this.operations.getData()};
            this.fixedString = new String(var4);
         }

         this.fixedStringOptions = this.options;
         this.fixedStringTable = new BMPattern(this.fixedString, 256, isSet(this.fixedStringOptions, 2));
      } else if (!isSet(this.options, 256) && !isSet(this.options, 512)) {
         Token.FixedStringContainer var3 = new Token.FixedStringContainer();
         this.tokentree.findFixedString(var3, this.options);
         this.fixedString = var3.token == null ? null : var3.token.getString();
         this.fixedStringOptions = var3.options;
         if (this.fixedString != null && this.fixedString.length() < 2) {
            this.fixedString = null;
         }

         if (this.fixedString != null) {
            this.fixedStringTable = new BMPattern(this.fixedString, 256, isSet(this.fixedStringOptions, 2));
         }
      }

   }

   private static final boolean isSet(int var0, int var1) {
      return (var0 & var1) == var1;
   }

   public RegularExpression(String var1) throws ParseException {
      this.setPattern(var1, (String)null);
   }

   public RegularExpression(String var1, String var2) throws ParseException {
      this.setPattern(var1, var2);
   }

   RegularExpression(String var1, Token var2, int var3, boolean var4, int var5) {
      this.regex = var1;
      this.tokentree = var2;
      this.nofparen = var3;
      this.options = var5;
      this.hasBackReferences = var4;
   }

   public void setPattern(String var1) throws ParseException {
      this.setPattern(var1, this.options);
   }

   private void setPattern(String var1, int var2) throws ParseException {
      this.regex = var1;
      this.options = var2;
      Object var3 = isSet(this.options, 512) ? new ParserForXMLSchema() : new RegexParser();
      this.tokentree = ((RegexParser)var3).parse(this.regex, this.options);
      this.nofparen = ((RegexParser)var3).parennumber;
      this.hasBackReferences = ((RegexParser)var3).hasBackReferences;
      this.operations = null;
      this.context = null;
   }

   public void setPattern(String var1, String var2) throws ParseException {
      this.setPattern(var1, REUtil.parseOptions(var2));
   }

   public String getPattern() {
      return this.regex;
   }

   public String toString() {
      return this.tokentree.toString(this.options);
   }

   public String getOptions() {
      return REUtil.createOptionString(this.options);
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof RegularExpression)) {
         return false;
      } else {
         RegularExpression var2 = (RegularExpression)var1;
         return this.regex.equals(var2.regex) && this.options == var2.options;
      }
   }

   boolean equals(String var1, int var2) {
      return this.regex.equals(var1) && this.options == var2;
   }

   public int hashCode() {
      return (this.regex + "/" + this.getOptions()).hashCode();
   }

   public int getNumberOfGroups() {
      return this.nofparen;
   }

   private static final int getWordType0(char var0, int var1) {
      // $FF: Couldn't be decompiled
   }

   private static final boolean isEOLChar(int var0) {
      return var0 == 10 || var0 == 13 || var0 == 8232 || var0 == 8233;
   }

   private static final boolean isWordChar(int var0) {
      if (var0 == 95) {
         return true;
      } else if (var0 < 48) {
         return false;
      } else if (var0 > 122) {
         return false;
      } else if (var0 <= 57) {
         return true;
      } else if (var0 < 65) {
         return false;
      } else if (var0 <= 90) {
         return true;
      } else {
         return var0 >= 97;
      }
   }

   private static final boolean matchIgnoreCase(int var0, int var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 <= 65535 && var1 <= 65535) {
         char var2 = Character.toUpperCase((char)var0);
         char var3 = Character.toUpperCase((char)var1);
         if (var2 == var3) {
            return true;
         } else {
            return Character.toLowerCase(var2) == Character.toLowerCase(var3);
         }
      } else {
         return false;
      }
   }

   static final class Context {
      CharacterIterator ciTarget;
      String strTarget;
      char[] charTarget;
      int start;
      int limit;
      int length;
      Match match;
      boolean inuse = false;
      int[] offsets;

      private void resetCommon(int var1) {
         this.length = this.limit - this.start;
         this.inuse = true;
         this.match = null;
         if (this.offsets == null || this.offsets.length != var1) {
            this.offsets = new int[var1];
         }

         for(int var2 = 0; var2 < var1; ++var2) {
            this.offsets[var2] = -1;
         }

      }

      void reset(CharacterIterator var1, int var2, int var3, int var4) {
         this.ciTarget = var1;
         this.start = var2;
         this.limit = var3;
         this.resetCommon(var4);
      }

      void reset(String var1, int var2, int var3, int var4) {
         this.strTarget = var1;
         this.start = var2;
         this.limit = var3;
         this.resetCommon(var4);
      }

      void reset(char[] var1, int var2, int var3, int var4) {
         this.charTarget = var1;
         this.start = var2;
         this.limit = var3;
         this.resetCommon(var4);
      }
   }
}
