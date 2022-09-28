package org.apache.james.mime4j.field.datetime.parser;

import java.io.IOException;
import java.io.PrintStream;

public class DateTimeParserTokenManager implements DateTimeParserConstants {
   static int commentNest;
   public PrintStream debugStream;
   static final long[] jjbitVec0 = new long[]{0L, 0L, -1L, -1L};
   static final int[] jjnextStates = new int[0];
   public static final String[] jjstrLiteralImages = new String[]{"", "\r", "\n", ",", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ":", null, "UT", "GMT", "EST", "EDT", "CST", "CDT", "MST", "MDT", "PST", "PDT", null, null, null, null, null, null, null, null, null, null, null, null, null, null};
   public static final String[] lexStateNames = new String[]{"DEFAULT", "INCOMMENT", "NESTED_COMMENT"};
   public static final int[] jjnewLexState = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 0, -1, 2, -1, -1, -1, -1, -1, -1, -1, -1};
   static final long[] jjtoToken = new long[]{70437463654399L};
   static final long[] jjtoSkip = new long[]{343597383680L};
   static final long[] jjtoSpecial = new long[]{68719476736L};
   static final long[] jjtoMore = new long[]{69956427317248L};
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
         case 0:
            if ((active0 & 34334373872L) != 0L) {
               this.jjmatchedKind = 35;
               return -1;
            }

            return -1;
         case 1:
            if ((active0 & 34334373872L) != 0L) {
               if (this.jjmatchedPos == 0) {
                  this.jjmatchedKind = 35;
                  this.jjmatchedPos = 0;
               }

               return -1;
            }

            return -1;
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
         case '\n':
            return this.jjStopAtPos(0, 2);
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
         case ' ':
         case '!':
         case '"':
         case '#':
         case '$':
         case '%':
         case '&':
         case '\'':
         case ')':
         case '*':
         case '+':
         case '-':
         case '.':
         case '/':
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
         case ';':
         case '<':
         case '=':
         case '>':
         case '?':
         case '@':
         case 'B':
         case 'H':
         case 'I':
         case 'K':
         case 'L':
         case 'Q':
         case 'R':
         case 'V':
         default:
            return this.jjMoveNfa_0(0, 0);
         case '\r':
            return this.jjStopAtPos(0, 1);
         case '(':
            return this.jjStopAtPos(0, 37);
         case ',':
            return this.jjStopAtPos(0, 3);
         case ':':
            return this.jjStopAtPos(0, 23);
         case 'A':
            return this.jjMoveStringLiteralDfa1_0(278528L);
         case 'C':
            return this.jjMoveStringLiteralDfa1_0(1610612736L);
         case 'D':
            return this.jjMoveStringLiteralDfa1_0(4194304L);
         case 'E':
            return this.jjMoveStringLiteralDfa1_0(402653184L);
         case 'F':
            return this.jjMoveStringLiteralDfa1_0(4352L);
         case 'G':
            return this.jjMoveStringLiteralDfa1_0(67108864L);
         case 'J':
            return this.jjMoveStringLiteralDfa1_0(198656L);
         case 'M':
            return this.jjMoveStringLiteralDfa1_0(6442491920L);
         case 'N':
            return this.jjMoveStringLiteralDfa1_0(2097152L);
         case 'O':
            return this.jjMoveStringLiteralDfa1_0(1048576L);
         case 'P':
            return this.jjMoveStringLiteralDfa1_0(25769803776L);
         case 'S':
            return this.jjMoveStringLiteralDfa1_0(525824L);
         case 'T':
            return this.jjMoveStringLiteralDfa1_0(160L);
         case 'U':
            return this.jjMoveStringLiteralDfa1_0(33554432L);
         case 'W':
            return this.jjMoveStringLiteralDfa1_0(64L);
      }
   }

   private int jjMoveStringLiteralDfa1_0(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_0(0, active0);
         return 1;
      }

      switch (this.curChar) {
         case 'D':
            return this.jjMoveStringLiteralDfa2_0(active0, 22817013760L);
         case 'M':
            return this.jjMoveStringLiteralDfa2_0(active0, 67108864L);
         case 'S':
            return this.jjMoveStringLiteralDfa2_0(active0, 11408506880L);
         case 'T':
            if ((active0 & 33554432L) != 0L) {
               return this.jjStopAtPos(1, 25);
            }
         case 'E':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'L':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         case 'Z':
         case '[':
         case '\\':
         case ']':
         case '^':
         case '_':
         case '`':
         case 'b':
         case 'd':
         case 'f':
         case 'g':
         case 'i':
         case 'j':
         case 'k':
         case 'l':
         case 'm':
         case 'n':
         case 'q':
         case 's':
         case 't':
         default:
            return this.jjStartNfa_0(0, active0);
         case 'a':
            return this.jjMoveStringLiteralDfa2_0(active0, 43520L);
         case 'c':
            return this.jjMoveStringLiteralDfa2_0(active0, 1048576L);
         case 'e':
            return this.jjMoveStringLiteralDfa2_0(active0, 4722752L);
         case 'h':
            return this.jjMoveStringLiteralDfa2_0(active0, 128L);
         case 'o':
            return this.jjMoveStringLiteralDfa2_0(active0, 2097168L);
         case 'p':
            return this.jjMoveStringLiteralDfa2_0(active0, 16384L);
         case 'r':
            return this.jjMoveStringLiteralDfa2_0(active0, 256L);
         case 'u':
            return this.jjMoveStringLiteralDfa2_0(active0, 459808L);
      }
   }

   private int jjMoveStringLiteralDfa2_0(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_0(0, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(1, active0);
            return 2;
         }

         switch (this.curChar) {
            case 'T':
               if ((active0 & 67108864L) != 0L) {
                  return this.jjStopAtPos(2, 26);
               }

               if ((active0 & 134217728L) != 0L) {
                  return this.jjStopAtPos(2, 27);
               }

               if ((active0 & 268435456L) != 0L) {
                  return this.jjStopAtPos(2, 28);
               }

               if ((active0 & 536870912L) != 0L) {
                  return this.jjStopAtPos(2, 29);
               }

               if ((active0 & 1073741824L) != 0L) {
                  return this.jjStopAtPos(2, 30);
               }

               if ((active0 & 2147483648L) != 0L) {
                  return this.jjStopAtPos(2, 31);
               }

               if ((active0 & 4294967296L) != 0L) {
                  return this.jjStopAtPos(2, 32);
               }

               if ((active0 & 8589934592L) != 0L) {
                  return this.jjStopAtPos(2, 33);
               }

               if ((active0 & 17179869184L) != 0L) {
                  return this.jjStopAtPos(2, 34);
               }
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '_':
            case '`':
            case 'a':
            case 'f':
            case 'h':
            case 'j':
            case 'k':
            case 'm':
            case 'o':
            case 'q':
            case 's':
            case 'w':
            case 'x':
            default:
               break;
            case 'b':
               if ((active0 & 4096L) != 0L) {
                  return this.jjStopAtPos(2, 12);
               }
               break;
            case 'c':
               if ((active0 & 4194304L) != 0L) {
                  return this.jjStopAtPos(2, 22);
               }
               break;
            case 'd':
               if ((active0 & 64L) != 0L) {
                  return this.jjStopAtPos(2, 6);
               }
               break;
            case 'e':
               if ((active0 & 32L) != 0L) {
                  return this.jjStopAtPos(2, 5);
               }
               break;
            case 'g':
               if ((active0 & 262144L) != 0L) {
                  return this.jjStopAtPos(2, 18);
               }
               break;
            case 'i':
               if ((active0 & 256L) != 0L) {
                  return this.jjStopAtPos(2, 8);
               }
               break;
            case 'l':
               if ((active0 & 131072L) != 0L) {
                  return this.jjStopAtPos(2, 17);
               }
               break;
            case 'n':
               if ((active0 & 16L) != 0L) {
                  return this.jjStopAtPos(2, 4);
               }

               if ((active0 & 1024L) != 0L) {
                  return this.jjStopAtPos(2, 10);
               }

               if ((active0 & 2048L) != 0L) {
                  return this.jjStopAtPos(2, 11);
               }

               if ((active0 & 65536L) != 0L) {
                  return this.jjStopAtPos(2, 16);
               }
               break;
            case 'p':
               if ((active0 & 524288L) != 0L) {
                  return this.jjStopAtPos(2, 19);
               }
               break;
            case 'r':
               if ((active0 & 8192L) != 0L) {
                  return this.jjStopAtPos(2, 13);
               }

               if ((active0 & 16384L) != 0L) {
                  return this.jjStopAtPos(2, 14);
               }
               break;
            case 't':
               if ((active0 & 512L) != 0L) {
                  return this.jjStopAtPos(2, 9);
               }

               if ((active0 & 1048576L) != 0L) {
                  return this.jjStopAtPos(2, 20);
               }
               break;
            case 'u':
               if ((active0 & 128L) != 0L) {
                  return this.jjStopAtPos(2, 7);
               }
               break;
            case 'v':
               if ((active0 & 2097152L) != 0L) {
                  return this.jjStopAtPos(2, 21);
               }
               break;
            case 'y':
               if ((active0 & 32768L) != 0L) {
                  return this.jjStopAtPos(2, 15);
               }
         }

         return this.jjStartNfa_0(1, active0);
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
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 46) {
                           kind = 46;
                        }

                        this.jjCheckNAdd(3);
                     } else if ((4294967808L & l) != 0L) {
                        if (kind > 36) {
                           kind = 36;
                        }

                        this.jjCheckNAdd(2);
                     } else if ((43980465111040L & l) != 0L && kind > 24) {
                        kind = 24;
                     }
                  case 1:
                  default:
                     break;
                  case 2:
                     if ((4294967808L & l) != 0L) {
                        kind = 36;
                        this.jjCheckNAdd(2);
                     }
                     break;
                  case 3:
                     if ((287948901175001088L & l) != 0L) {
                        kind = 46;
                        this.jjCheckNAdd(3);
                     }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if ((576456345801194494L & l) != 0L) {
                        kind = 35;
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
            return this.jjStopAtPos(0, 40);
         case ')':
            return this.jjStopAtPos(0, 38);
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
                     if (kind > 41) {
                        kind = 41;
                     }
                     break;
                  case 1:
                     if (kind > 39) {
                        kind = 39;
                     }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if (kind > 41) {
                        kind = 41;
                     }

                     if (this.curChar == '\\') {
                        this.jjstateSet[this.jjnewStateCnt++] = 1;
                     }
                     break;
                  case 1:
                     if (kind > 39) {
                        kind = 39;
                     }
                     break;
                  case 2:
                     if (kind > 41) {
                        kind = 41;
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
                     if ((jjbitVec0[i2] & l2) != 0L && kind > 41) {
                        kind = 41;
                     }
                     break;
                  case 1:
                     if ((jjbitVec0[i2] & l2) != 0L && kind > 39) {
                        kind = 39;
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
            return this.jjStopAtPos(0, 43);
         case ')':
            return this.jjStopAtPos(0, 44);
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
                     if (kind > 45) {
                        kind = 45;
                     }
                     break;
                  case 1:
                     if (kind > 42) {
                        kind = 42;
                     }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if (kind > 45) {
                        kind = 45;
                     }

                     if (this.curChar == '\\') {
                        this.jjstateSet[this.jjnewStateCnt++] = 1;
                     }
                     break;
                  case 1:
                     if (kind > 42) {
                        kind = 42;
                     }
                     break;
                  case 2:
                     if (kind > 45) {
                        kind = 45;
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
                     if ((jjbitVec0[i2] & l2) != 0L && kind > 45) {
                        kind = 45;
                     }
                     break;
                  case 1:
                     if ((jjbitVec0[i2] & l2) != 0L && kind > 42) {
                        kind = 42;
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

   public DateTimeParserTokenManager(SimpleCharStream stream) {
      this.debugStream = System.out;
      this.jjrounds = new int[4];
      this.jjstateSet = new int[8];
      this.jjimage = new StringBuilder();
      this.image = this.jjimage;
      this.curLexState = 0;
      this.defaultLexState = 0;
      this.input_stream = stream;
   }

   public DateTimeParserTokenManager(SimpleCharStream stream, int lexState) {
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
      if (lexState < 3 && lexState >= 0) {
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

      label95:
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
            }

            if (this.jjmatchedKind == Integer.MAX_VALUE) {
               break label95;
            }

            if (this.jjmatchedPos + 1 < curPos) {
               this.input_stream.backup(curPos - this.jjmatchedPos - 1);
            }

            if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
               matchedToken = this.jjFillToken();
               matchedToken.specialToken = specialToken;
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
               break label95;
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
         case 39:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            this.image.deleteCharAt(this.image.length() - 2);
            break;
         case 40:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            commentNest = 1;
         case 41:
         default:
            break;
         case 42:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            this.image.deleteCharAt(this.image.length() - 2);
            break;
         case 43:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            ++commentNest;
            break;
         case 44:
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            --commentNest;
            if (commentNest == 0) {
               this.SwitchTo(1);
            }
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
