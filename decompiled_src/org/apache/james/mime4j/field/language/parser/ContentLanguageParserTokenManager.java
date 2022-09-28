package org.apache.james.mime4j.field.language.parser;

import java.io.IOException;
import java.io.PrintStream;

public class ContentLanguageParserTokenManager implements ContentLanguageParserConstants {
   int commentNest;
   public PrintStream debugStream;
   static final long[] jjbitVec0 = new long[]{0L, 0L, -1L, -1L};
   static final int[] jjnextStates = new int[0];
   public static final String[] jjstrLiteralImages = new String[]{"", ",", "-", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, ".", null, null};
   public static final String[] lexStateNames = new String[]{"DEFAULT", "INCOMMENT", "NESTED_COMMENT", "INQUOTEDSTRING"};
   public static final int[] jjnewLexState = new int[]{-1, -1, -1, -1, 1, 0, -1, 2, -1, -1, -1, -1, -1, 3, -1, -1, 0, -1, -1, -1, -1, -1, -1};
   static final long[] jjtoToken = new long[]{2031623L};
   static final long[] jjtoSkip = new long[]{40L};
   static final long[] jjtoSpecial = new long[]{8L};
   static final long[] jjtoMore = new long[]{65488L};
   protected SimpleCharStream input_stream;
   private final int[] jjrounds;
   private final int[] jjstateSet;
   private final StringBuilder jjimage;
   private StringBuilder image;
   private int jjimageLen;
   private int lengthOfMatch;
   protected char curChar;
   int curLexState;
   int defaultLexState;
   int jjnewStateCnt;
   int jjround;
   int jjmatchedPos;
   int jjmatchedKind;

   public void setDebugStream(PrintStream ds) {
      this.debugStream = ds;
   }

   private final int jjStopStringLiteralDfa_0(int pos, long active0) {
      switch (pos) {
         default:
            return -1;
      }
   }

   private final int jjStartNfa_0(int pos, long active0) {
      return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(pos, active0), pos + 1);
   }

   private int jjStopAtPos(int pos, int kind) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;
      return pos + 1;
   }

   private int jjMoveStringLiteralDfa0_0() {
      switch (this.curChar) {
         case '"':
            return this.jjStopAtPos(0, 13);
         case '#':
         case '$':
         case '%':
         case '&':
         case '\'':
         case ')':
         case '*':
         case '+':
         default:
            return this.jjMoveNfa_0(4, 0);
         case '(':
            return this.jjStopAtPos(0, 4);
         case ',':
            return this.jjStopAtPos(0, 1);
         case '-':
            return this.jjStopAtPos(0, 2);
         case '.':
            return this.jjStopAtPos(0, 20);
      }
   }

   private int jjMoveNfa_0(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 4;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if ((4294977024L & l) != 0L) {
                        kind = 3;
                        this.jjCheckNAdd(0);
                     }
                     break;
                  case 1:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 17) {
                           kind = 17;
                        }

                        this.jjCheckNAdd(1);
                     }
                  case 2:
                  default:
                     break;
                  case 3:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAdd(3);
                     }
                     break;
                  case 4:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAdd(3);
                     } else if ((4294977024L & l) != 0L) {
                        if (kind > 3) {
                           kind = 3;
                        }

                        this.jjCheckNAdd(0);
                     }

                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 17) {
                           kind = 17;
                        }

                        this.jjCheckNAdd(1);
                     }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 2:
                     if ((576460743847706622L & l) != 0L) {
                        if (kind > 18) {
                           kind = 18;
                        }

                        this.jjCheckNAdd(2);
                     }
                     break;
                  case 3:
                     if ((576460743847706622L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAdd(3);
                     }
                     break;
                  case 4:
                     if ((576460743847706622L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAdd(3);
                     }

                     if ((576460743847706622L & l) != 0L) {
                        if (kind > 18) {
                           kind = 18;
                        }

                        this.jjCheckNAdd(2);
                     }
               }
            } while(i != startsAt);
         } else {
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch (this.jjstateSet[i]) {
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 4 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var9) {
            return curPos;
         }
      }
   }

   private final int jjStopStringLiteralDfa_1(int pos, long active0) {
      switch (pos) {
         default:
            return -1;
      }
   }

   private final int jjStartNfa_1(int pos, long active0) {
      return this.jjMoveNfa_1(this.jjStopStringLiteralDfa_1(pos, active0), pos + 1);
   }

   private int jjMoveStringLiteralDfa0_1() {
      switch (this.curChar) {
         case '(':
            return this.jjStopAtPos(0, 7);
         case ')':
            return this.jjStopAtPos(0, 5);
         default:
            return this.jjMoveNfa_1(0, 0);
      }
   }

   private int jjMoveNfa_1(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 3;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if (kind > 8) {
                        kind = 8;
                     }
                     break;
                  case 1:
                     if (kind > 6) {
                        kind = 6;
                     }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if (kind > 8) {
                        kind = 8;
                     }

                     if (this.curChar == '\\') {
                        this.jjstateSet[this.jjnewStateCnt++] = 1;
                     }
                     break;
                  case 1:
                     if (kind > 6) {
                        kind = 6;
                     }
                     break;
                  case 2:
                     if (kind > 8) {
                        kind = 8;
                     }
               }
            } while(i != startsAt);
         } else {
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if ((jjbitVec0[i2] & l2) != 0L && kind > 8) {
                        kind = 8;
                     }
                     break;
                  case 1:
                     if ((jjbitVec0[i2] & l2) != 0L && kind > 6) {
                        kind = 6;
                     }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 3 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var9) {
            return curPos;
         }
      }
   }

   private final int jjStopStringLiteralDfa_3(int pos, long active0) {
      switch (pos) {
         default:
            return -1;
      }
   }

   private final int jjStartNfa_3(int pos, long active0) {
      return this.jjMoveNfa_3(this.jjStopStringLiteralDfa_3(pos, active0), pos + 1);
   }

   private int jjMoveStringLiteralDfa0_3() {
      switch (this.curChar) {
         case '"':
            return this.jjStopAtPos(0, 16);
         default:
            return this.jjMoveNfa_3(0, 0);
      }
   }

   private int jjMoveNfa_3(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 3;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                  case 2:
                     if ((-17179869185L & l) != 0L) {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAdd(2);
                     }
                     break;
                  case 1:
                     if (kind > 14) {
                        kind = 14;
                     }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if ((-268435457L & l) != 0L) {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAdd(2);
                     } else if (this.curChar == '\\') {
                        this.jjstateSet[this.jjnewStateCnt++] = 1;
                     }
                     break;
                  case 1:
                     if (kind > 14) {
                        kind = 14;
                     }
                     break;
                  case 2:
                     if ((-268435457L & l) != 0L) {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAdd(2);
                     }
               }
            } while(i != startsAt);
         } else {
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                  case 2:
                     if ((jjbitVec0[i2] & l2) != 0L) {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAdd(2);
                     }
                     break;
                  case 1:
                     if ((jjbitVec0[i2] & l2) != 0L && kind > 14) {
                        kind = 14;
                     }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 3 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var9) {
            return curPos;
         }
      }
   }

   private final int jjStopStringLiteralDfa_2(int pos, long active0) {
      switch (pos) {
         default:
            return -1;
      }
   }

   private final int jjStartNfa_2(int pos, long active0) {
      return this.jjMoveNfa_2(this.jjStopStringLiteralDfa_2(pos, active0), pos + 1);
   }

   private int jjMoveStringLiteralDfa0_2() {
      switch (this.curChar) {
         case '(':
            return this.jjStopAtPos(0, 10);
         case ')':
            return this.jjStopAtPos(0, 11);
         default:
            return this.jjMoveNfa_2(0, 0);
      }
   }

   private int jjMoveNfa_2(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 3;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if (kind > 12) {
                        kind = 12;
                     }
                     break;
                  case 1:
                     if (kind > 9) {
                        kind = 9;
                     }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if (kind > 12) {
                        kind = 12;
                     }

                     if (this.curChar == '\\') {
                        this.jjstateSet[this.jjnewStateCnt++] = 1;
                     }
                     break;
                  case 1:
                     if (kind > 9) {
                        kind = 9;
                     }
                     break;
                  case 2:
                     if (kind > 12) {
                        kind = 12;
                     }
               }
            } while(i != startsAt);
         } else {
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if ((jjbitVec0[i2] & l2) != 0L && kind > 12) {
                        kind = 12;
                     }
                     break;
                  case 1:
                     if ((jjbitVec0[i2] & l2) != 0L && kind > 9) {
                        kind = 9;
                     }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 3 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var9) {
            return curPos;
         }
      }
   }

   public ContentLanguageParserTokenManager(SimpleCharStream stream) {
      this.debugStream = System.out;
      this.jjrounds = new int[4];
      this.jjstateSet = new int[8];
      this.jjimage = new StringBuilder();
      this.image = this.jjimage;
      this.curLexState = 0;
      this.defaultLexState = 0;
      this.input_stream = stream;
   }

   public ContentLanguageParserTokenManager(SimpleCharStream stream, int lexState) {
      this(stream);
      this.SwitchTo(lexState);
   }

   public void ReInit(SimpleCharStream stream) {
      this.jjmatchedPos = this.jjnewStateCnt = 0;
      this.curLexState = this.defaultLexState;
      this.input_stream = stream;
      this.ReInitRounds();
   }

   private void ReInitRounds() {
      this.jjround = -2147483647;

      for(int i = 4; i-- > 0; this.jjrounds[i] = Integer.MIN_VALUE) {
      }

   }

   public void ReInit(SimpleCharStream stream, int lexState) {
      this.ReInit(stream);
      this.SwitchTo(lexState);
   }

   public void SwitchTo(int lexState) {
      if (lexState < 4 && lexState >= 0) {
         this.curLexState = lexState;
      } else {
         throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
      }
   }

   protected Token jjFillToken() {
      String im = jjstrLiteralImages[this.jjmatchedKind];
      String curTokenImage = im == null ? this.input_stream.GetImage() : im;
      int beginLine = this.input_stream.getBeginLine();
      int beginColumn = this.input_stream.getBeginColumn();
      int endLine = this.input_stream.getEndLine();
      int endColumn = this.input_stream.getEndColumn();
      Token t = Token.newToken(this.jjmatchedKind, curTokenImage);
      t.beginLine = beginLine;
      t.endLine = endLine;
      t.beginColumn = beginColumn;
      t.endColumn = endColumn;
      return t;
   }

   public Token getNextToken() {
      Token specialToken = null;
      int curPos = 0;

      label96:
      while(true) {
         Token matchedToken;
         try {
            this.curChar = this.input_stream.BeginToken();
         } catch (IOException var9) {
            this.jjmatchedKind = 0;
            matchedToken = this.jjFillToken();
            matchedToken.specialToken = specialToken;
            return matchedToken;
         }

         this.image = this.jjimage;
         this.image.setLength(0);
         this.jjimageLen = 0;

         while(true) {
            switch (this.curLexState) {
               case 0:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  curPos = this.jjMoveStringLiteralDfa0_0();
                  break;
               case 1:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  curPos = this.jjMoveStringLiteralDfa0_1();
                  break;
               case 2:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  curPos = this.jjMoveStringLiteralDfa0_2();
                  break;
               case 3:
                  this.jjmatchedKind = Integer.MAX_VALUE;
                  this.jjmatchedPos = 0;
                  curPos = this.jjMoveStringLiteralDfa0_3();
            }

            if (this.jjmatchedKind == Integer.MAX_VALUE) {
               break label96;
            }

            if (this.jjmatchedPos + 1 < curPos) {
               this.input_stream.backup(curPos - this.jjmatchedPos - 1);
            }

            if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
               matchedToken = this.jjFillToken();
               matchedToken.specialToken = specialToken;
               this.TokenLexicalActions(matchedToken);
               if (jjnewLexState[this.jjmatchedKind] != -1) {
                  this.curLexState = jjnewLexState[this.jjmatchedKind];
               }

               return matchedToken;
            }

            if ((jjtoSkip[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
               if ((jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
                  matchedToken = this.jjFillToken();
                  if (specialToken == null) {
                     specialToken = matchedToken;
                  } else {
                     matchedToken.specialToken = specialToken;
                     specialToken = specialToken.next = matchedToken;
                  }
               }

               if (jjnewLexState[this.jjmatchedKind] != -1) {
                  this.curLexState = jjnewLexState[this.jjmatchedKind];
               }
               break;
            }

            this.MoreLexicalActions();
            if (jjnewLexState[this.jjmatchedKind] != -1) {
               this.curLexState = jjnewLexState[this.jjmatchedKind];
            }

            curPos = 0;
            this.jjmatchedKind = Integer.MAX_VALUE;

            try {
               this.curChar = this.input_stream.readChar();
            } catch (IOException var11) {
               break label96;
            }
         }
      }

      int error_line = this.input_stream.getEndLine();
      int error_column = this.input_stream.getEndColumn();
      String error_after = null;
      boolean EOFSeen = false;

      try {
         this.input_stream.readChar();
         this.input_stream.backup(1);
      } catch (IOException var10) {
         EOFSeen = true;
         error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
         if (this.curChar != '\n' && this.curChar != '\r') {
            ++error_column;
         } else {
            ++error_line;
            error_column = 0;
         }
      }

      if (!EOFSeen) {
         this.input_stream.backup(1);
         error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
      }

      throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
   }

   void MoreLexicalActions() {
      this.jjimageLen += this.lengthOfMatch = this.jjmatchedPos + 1;
      switch (this.jjmatchedKind) {
         case 6:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            this.image.deleteCharAt(this.image.length() - 2);
            break;
         case 7:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            this.commentNest = 1;
         case 8:
         case 12:
         default:
            break;
         case 9:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            this.image.deleteCharAt(this.image.length() - 2);
            break;
         case 10:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            ++this.commentNest;
            break;
         case 11:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            --this.commentNest;
            if (this.commentNest == 0) {
               this.SwitchTo(1);
            }
            break;
         case 13:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            this.image.deleteCharAt(this.image.length() - 1);
            break;
         case 14:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            this.image.deleteCharAt(this.image.length() - 2);
      }

   }

   void TokenLexicalActions(Token matchedToken) {
      switch (this.jjmatchedKind) {
         case 16:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
            matchedToken.image = this.image.substring(0, this.image.length() - 1);
         default:
      }
   }

   private void jjCheckNAdd(int state) {
      if (this.jjrounds[state] != this.jjround) {
         this.jjstateSet[this.jjnewStateCnt++] = state;
         this.jjrounds[state] = this.jjround;
      }

   }

   private void jjAddStates(int start, int end) {
      do {
         this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
      } while(start++ != end);

   }

   private void jjCheckNAddTwoStates(int state1, int state2) {
      this.jjCheckNAdd(state1);
      this.jjCheckNAdd(state2);
   }
}
