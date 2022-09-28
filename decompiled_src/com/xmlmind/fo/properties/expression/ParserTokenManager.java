package com.xmlmind.fo.properties.expression;

import java.io.IOException;
import java.io.PrintStream;

public class ParserTokenManager implements ParserConstants {
   public PrintStream debugStream;
   static final int[] jjnextStates = new int[]{4, 5, 8, 9, 11, 12, 17, 18, 20, 21, 22, 13, 25, 26, 27, 4, 5, 8, 9, 11, 12, 13, 6, 8, 9, 11, 12, 13, 17, 18, 20, 25, 8, 9, 11, 12, 13, 14, 15, 16};
   public static final String[] jjstrLiteralImages = new String[]{"", "+", "-", "*", "div", "mod", "(", ")", ",", null, null, null, null, null, null, null, null, null, null};
   public static final String[] lexStateNames = new String[]{"DEFAULT"};
   static final long[] jjtoToken = new long[]{294399L};
   static final long[] jjtoSkip = new long[]{512L};
   protected SimpleCharStream input_stream;
   private final int[] jjrounds;
   private final int[] jjstateSet;
   protected char curChar;
   int curLexState;
   int defaultLexState;
   int jjnewStateCnt;
   int jjround;
   int jjmatchedPos;
   int jjmatchedKind;

   public void setDebugStream(PrintStream var1) {
      this.debugStream = var1;
   }

   private final int jjStopStringLiteralDfa_0(int var1, long var2) {
      switch (var1) {
         case 0:
            if ((var2 & 4L) != 0L) {
               return 0;
            } else {
               if ((var2 & 48L) != 0L) {
                  this.jjmatchedKind = 13;
                  return 0;
               }

               return -1;
            }
         case 1:
            if ((var2 & 48L) != 0L) {
               this.jjmatchedKind = 13;
               this.jjmatchedPos = 1;
               return 0;
            }

            return -1;
         default:
            return -1;
      }
   }

   private final int jjStartNfa_0(int var1, long var2) {
      return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(var1, var2), var1 + 1);
   }

   private final int jjStopAtPos(int var1, int var2) {
      this.jjmatchedKind = var2;
      this.jjmatchedPos = var1;
      return var1 + 1;
   }

   private final int jjStartNfaWithStates_0(int var1, int var2, int var3) {
      this.jjmatchedKind = var2;
      this.jjmatchedPos = var1;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return var1 + 1;
      }

      return this.jjMoveNfa_0(var3, var1 + 1);
   }

   private final int jjMoveStringLiteralDfa0_0() {
      switch (this.curChar) {
         case '(':
            return this.jjStopAtPos(0, 6);
         case ')':
            return this.jjStopAtPos(0, 7);
         case '*':
            return this.jjStopAtPos(0, 3);
         case '+':
            return this.jjStopAtPos(0, 1);
         case ',':
            return this.jjStopAtPos(0, 8);
         case '-':
            return this.jjStartNfaWithStates_0(0, 2, 0);
         case 'd':
            return this.jjMoveStringLiteralDfa1_0(16L);
         case 'm':
            return this.jjMoveStringLiteralDfa1_0(32L);
         default:
            return this.jjMoveNfa_0(1, 0);
      }
   }

   private final int jjMoveStringLiteralDfa1_0(long var1) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_0(0, var1);
         return 1;
      }

      switch (this.curChar) {
         case 'i':
            return this.jjMoveStringLiteralDfa2_0(var1, 16L);
         case 'o':
            return this.jjMoveStringLiteralDfa2_0(var1, 32L);
         default:
            return this.jjStartNfa_0(0, var1);
      }
   }

   private final int jjMoveStringLiteralDfa2_0(long var1, long var3) {
      if ((var3 &= var1) == 0L) {
         return this.jjStartNfa_0(0, var1);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(1, var3);
            return 2;
         }

         switch (this.curChar) {
            case 'd':
               if ((var3 & 32L) != 0L) {
                  return this.jjStartNfaWithStates_0(2, 5, 0);
               }
               break;
            case 'v':
               if ((var3 & 16L) != 0L) {
                  return this.jjStartNfaWithStates_0(2, 4, 0);
               }
         }

         return this.jjStartNfa_0(1, var3);
      }
   }

   private final void jjCheckNAdd(int var1) {
      if (this.jjrounds[var1] != this.jjround) {
         this.jjstateSet[this.jjnewStateCnt++] = var1;
         this.jjrounds[var1] = this.jjround;
      }

   }

   private final void jjAddStates(int var1, int var2) {
      do {
         this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[var1];
      } while(var1++ != var2);

   }

   private final void jjCheckNAddTwoStates(int var1, int var2) {
      this.jjCheckNAdd(var1);
      this.jjCheckNAdd(var2);
   }

   private final void jjCheckNAddStates(int var1, int var2) {
      do {
         this.jjCheckNAdd(jjnextStates[var1]);
      } while(var1++ != var2);

   }

   private final void jjCheckNAddStates(int var1) {
      this.jjCheckNAdd(jjnextStates[var1]);
      this.jjCheckNAdd(jjnextStates[var1 + 1]);
   }

   private final int jjMoveNfa_0(int var1, int var2) {
      int var4 = 0;
      this.jjnewStateCnt = 28;
      int var5 = 1;
      this.jjstateSet[0] = var1;
      int var7 = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long var12;
         if (this.curChar < '@') {
            var12 = 1L << this.curChar;

            do {
               --var5;
               switch (this.jjstateSet[var5]) {
                  case 0:
                     if (this.curChar == '-') {
                        var7 = 13;
                        this.jjCheckNAdd(0);
                     }
                     break;
                  case 1:
                     if ((287948901175001088L & var12) != 0L) {
                        if (var7 > 12) {
                           var7 = 12;
                        }

                        this.jjCheckNAddStates(0, 11);
                     } else if (this.curChar == '.') {
                        this.jjCheckNAddStates(12, 14);
                     } else if (this.curChar == '#') {
                        this.jjCheckNAdd(2);
                     } else if (this.curChar == '-') {
                        if (var7 > 13) {
                           var7 = 13;
                        }

                        this.jjCheckNAdd(0);
                     }
                     break;
                  case 2:
                     if ((287948901175001088L & var12) != 0L) {
                        if (var7 > 14) {
                           var7 = 14;
                        }

                        this.jjCheckNAdd(2);
                     }
                     break;
                  case 3:
                     if ((287948901175001088L & var12) != 0L) {
                        if (var7 > 12) {
                           var7 = 12;
                        }

                        this.jjCheckNAddStates(0, 11);
                     }
                     break;
                  case 4:
                     if ((287948901175001088L & var12) != 0L) {
                        this.jjCheckNAddStates(15, 21);
                     }
                     break;
                  case 5:
                     if (this.curChar == '.') {
                        this.jjCheckNAddStates(22, 27);
                     }
                     break;
                  case 6:
                     if ((287948901175001088L & var12) != 0L) {
                        this.jjCheckNAddStates(22, 27);
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
                  default:
                     break;
                  case 17:
                     if ((287948901175001088L & var12) != 0L) {
                        this.jjCheckNAddStates(28, 30);
                     }
                     break;
                  case 18:
                     if (this.curChar == '.') {
                        this.jjCheckNAddTwoStates(19, 20);
                     }
                     break;
                  case 19:
                     if ((287948901175001088L & var12) != 0L) {
                        this.jjCheckNAddTwoStates(19, 20);
                     }
                     break;
                  case 20:
                     if (this.curChar == '%') {
                        var7 = 11;
                     }
                     break;
                  case 21:
                     if ((287948901175001088L & var12) != 0L) {
                        if (var7 > 12) {
                           var7 = 12;
                        }

                        this.jjCheckNAddTwoStates(21, 22);
                     }
                     break;
                  case 22:
                     if (this.curChar == '.') {
                        if (var7 > 12) {
                           var7 = 12;
                        }

                        this.jjCheckNAdd(23);
                     }
                     break;
                  case 23:
                     if ((287948901175001088L & var12) != 0L) {
                        if (var7 > 12) {
                           var7 = 12;
                        }

                        this.jjCheckNAdd(23);
                     }
                     break;
                  case 24:
                     if (this.curChar == '.') {
                        this.jjCheckNAddStates(12, 14);
                     }
                     break;
                  case 25:
                     if ((287948901175001088L & var12) != 0L) {
                        this.jjCheckNAddStates(31, 36);
                     }
                     break;
                  case 26:
                     if ((287948901175001088L & var12) != 0L) {
                        this.jjCheckNAddTwoStates(26, 20);
                     }
                     break;
                  case 27:
                     if ((287948901175001088L & var12) != 0L) {
                        if (var7 > 12) {
                           var7 = 12;
                        }

                        this.jjCheckNAdd(27);
                     }
               }
            } while(var5 != var4);
         } else if (this.curChar < 128) {
            var12 = 1L << (this.curChar & 63);

            do {
               --var5;
               switch (this.jjstateSet[var5]) {
                  case 0:
                  case 1:
                     if ((576460743713488896L & var12) != 0L) {
                        if (var7 > 13) {
                           var7 = 13;
                        }

                        this.jjCheckNAdd(0);
                     }
                     break;
                  case 2:
                     if ((541165879422L & var12) != 0L) {
                        if (var7 > 14) {
                           var7 = 14;
                        }

                        this.jjstateSet[this.jjnewStateCnt++] = 2;
                     }
                  case 3:
                  case 4:
                  case 5:
                  case 6:
                  default:
                     break;
                  case 7:
                     if (this.curChar == 'm' && var7 > 10) {
                        var7 = 10;
                     }
                     break;
                  case 8:
                     if (this.curChar == 'c') {
                        this.jjCheckNAdd(7);
                     }
                     break;
                  case 9:
                     if (this.curChar == 'm') {
                        this.jjCheckNAdd(7);
                     }
                     break;
                  case 10:
                     if (this.curChar == 'n' && var7 > 10) {
                        var7 = 10;
                     }
                     break;
                  case 11:
                     if (this.curChar == 'i') {
                        this.jjstateSet[this.jjnewStateCnt++] = 10;
                     }
                     break;
                  case 12:
                     if (this.curChar == 'e') {
                        this.jjCheckNAdd(7);
                     }
                     break;
                  case 13:
                     if (this.curChar == 'p') {
                        this.jjAddStates(37, 39);
                     }
                     break;
                  case 14:
                     if (this.curChar == 't' && var7 > 10) {
                        var7 = 10;
                     }
                     break;
                  case 15:
                     if (this.curChar == 'c' && var7 > 10) {
                        var7 = 10;
                     }
                     break;
                  case 16:
                     if (this.curChar == 'x' && var7 > 10) {
                        var7 = 10;
                     }
               }
            } while(var5 != var4);
         } else {
            int var8 = (this.curChar & 255) >> 6;
            long var9 = 1L << (this.curChar & 63);

            do {
               --var5;
               switch (this.jjstateSet[var5]) {
               }
            } while(var5 != var4);
         }

         if (var7 != Integer.MAX_VALUE) {
            this.jjmatchedKind = var7;
            this.jjmatchedPos = var2;
            var7 = Integer.MAX_VALUE;
         }

         ++var2;
         if ((var5 = this.jjnewStateCnt) == (var4 = 28 - (this.jjnewStateCnt = var4))) {
            return var2;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var11) {
            return var2;
         }
      }
   }

   public ParserTokenManager(SimpleCharStream var1) {
      this.debugStream = System.out;
      this.jjrounds = new int[28];
      this.jjstateSet = new int[56];
      this.curLexState = 0;
      this.defaultLexState = 0;
      this.input_stream = var1;
   }

   public ParserTokenManager(SimpleCharStream var1, int var2) {
      this(var1);
      this.SwitchTo(var2);
   }

   public void ReInit(SimpleCharStream var1) {
      this.jjmatchedPos = this.jjnewStateCnt = 0;
      this.curLexState = this.defaultLexState;
      this.input_stream = var1;
      this.ReInitRounds();
   }

   private final void ReInitRounds() {
      this.jjround = -2147483647;

      for(int var1 = 28; var1-- > 0; this.jjrounds[var1] = Integer.MIN_VALUE) {
      }

   }

   public void ReInit(SimpleCharStream var1, int var2) {
      this.ReInit(var1);
      this.SwitchTo(var2);
   }

   public void SwitchTo(int var1) {
      if (var1 < 1 && var1 >= 0) {
         this.curLexState = var1;
      } else {
         throw new TokenMgrError("Error: Ignoring invalid lexical state : " + var1 + ". State unchanged.", 2);
      }
   }

   protected Token jjFillToken() {
      Token var1 = Token.newToken(this.jjmatchedKind);
      var1.kind = this.jjmatchedKind;
      String var2 = jjstrLiteralImages[this.jjmatchedKind];
      var1.image = var2 == null ? this.input_stream.GetImage() : var2;
      var1.beginLine = this.input_stream.getBeginLine();
      var1.beginColumn = this.input_stream.getBeginColumn();
      var1.endLine = this.input_stream.getEndLine();
      var1.endColumn = this.input_stream.getEndColumn();
      return var1;
   }

   public Token getNextToken() {
      Object var2 = null;
      boolean var4 = false;

      Token var3;
      do {
         label75:
         while(true) {
            try {
               this.curChar = this.input_stream.BeginToken();
            } catch (IOException var10) {
               this.jjmatchedKind = 0;
               var3 = this.jjFillToken();
               return var3;
            }

            try {
               this.input_stream.backup(0);

               while(true) {
                  if (this.curChar > ' ' || (4294967296L & 1L << this.curChar) == 0L) {
                     break label75;
                  }

                  this.curChar = this.input_stream.BeginToken();
               }
            } catch (IOException var12) {
            }
         }

         this.jjmatchedKind = Integer.MAX_VALUE;
         this.jjmatchedPos = 0;
         int var13 = this.jjMoveStringLiteralDfa0_0();
         if (this.jjmatchedPos == 0 && this.jjmatchedKind > 18) {
            this.jjmatchedKind = 18;
         }

         if (this.jjmatchedKind == Integer.MAX_VALUE) {
            int var5 = this.input_stream.getEndLine();
            int var6 = this.input_stream.getEndColumn();
            String var7 = null;
            boolean var8 = false;

            try {
               this.input_stream.readChar();
               this.input_stream.backup(1);
            } catch (IOException var11) {
               var8 = true;
               var7 = var13 <= 1 ? "" : this.input_stream.GetImage();
               if (this.curChar != '\n' && this.curChar != '\r') {
                  ++var6;
               } else {
                  ++var5;
                  var6 = 0;
               }
            }

            if (!var8) {
               this.input_stream.backup(1);
               var7 = var13 <= 1 ? "" : this.input_stream.GetImage();
            }

            throw new TokenMgrError(var8, this.curLexState, var5, var6, var7, this.curChar, 0);
         }

         if (this.jjmatchedPos + 1 < var13) {
            this.input_stream.backup(var13 - this.jjmatchedPos - 1);
         }
      } while((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) == 0L);

      var3 = this.jjFillToken();
      return var3;
   }
}
