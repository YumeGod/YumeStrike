package sleep.parser;

import java.util.LinkedList;

public class TokenParser implements ParserConstants {
   public static Statement ParseObject(Parser var0, TokenList var1) {
      Token[] var2 = var1.getTokens();
      String[] var3 = var1.getStrings();
      Statement var4 = new Statement();
      boolean var5 = false;
      if (var2.length < 1) {
         var0.reportError("Object Access: expression is empty", new Token(var1.toString(), var2[0].getHint()));
         return null;
      } else {
         StringBuffer var6;
         int var7;
         if (var3.length >= 2 && var3[1].equals(".")) {
            var6 = new StringBuffer();

            for(var7 = 0; var7 < var2.length - 1 && var3[var7 + 1].equals("."); var7 += 2) {
               var6.append(var3[var7]);
               var6.append(".");
            }

            if (var7 < var2.length) {
               var6.append(var3[var7]);
               ++var7;
            }

            Token var11 = var2[0].copy(var6.toString());
            TokenList var12 = new TokenList();
            var12.add(var11);

            while(var7 < var2.length) {
               var12.add(var2[var7]);
               ++var7;
            }

            return ParseObject(var0, var12);
         } else if (var3.length >= 3 && var3[2].equals(".")) {
            var6 = new StringBuffer();
            TokenList var8 = new TokenList();
            var8.add(var2[0]);

            for(var7 = 1; var7 < var2.length - 1 && var3[var7 + 1].equals("."); var7 += 2) {
               var6.append(var3[var7]);
               var6.append(".");
            }

            if (var7 < var2.length) {
               var6.append(var3[var7]);
               ++var7;
            }

            Token var9 = var2[1].copy(var6.toString());
            var8.add(var9);

            while(var7 < var2.length) {
               var8.add(var2[var7]);
               ++var7;
            }

            return ParseObject(var0, var8);
         } else {
            int var10;
            if (var3.length == 1) {
               var4.setType(446);
               var4.add(var2[0]);
               var10 = 1;
            } else if (var3.length >= 2 && Checkers.isClosureCall(var3[0], var3[1])) {
               var4.setType(446);
               var4.add(var2[0]);
               var10 = 1;
            } else if (var3.length >= 2 && Checkers.isObjectNew(var3[0], var3[1])) {
               var4.setType(441);
               var4.add(var2[1]);
               var10 = 2;
            } else if (var3.length >= 2 && Checkers.isClassIdentifier(var0, var3[0])) {
               var4.setType(443);
               var4.add(var2[0]);
               var4.add(var2[1]);
               var10 = 2;
            } else {
               var4.setType(442);
               var4.add(var2[0]);
               var4.add(var2[1]);
               var10 = 2;
            }

            if (var10 >= var2.length) {
               return var4;
            } else if (!var3[var10].equals("EOT")) {
               var0.reportError("Object Access: parameter separator is :", new Token(var1.toString(), var2[0].getHint()));
               return null;
            } else if (var10 + 1 >= var2.length) {
               var0.reportError("Object Access: can not specify empty arg list after :", new Token("[" + var1.toString().substring(0, var1.toString().length() - 4) + ":<null>]", var2[0].getHint()));
               return null;
            } else {
               ++var10;
               var6 = new StringBuffer(var3[var10]);
               ++var10;

               while(var10 < var2.length) {
                  var6.append(" ");
                  var6.append(var3[var10]);
                  ++var10;
               }

               var4.add(new Token(var6.toString(), var2[var10 - 1].getHint()));
               return var4;
            }
         }
      }
   }

   public static Statement ParsePredicate(Parser var0, TokenList var1) {
      Token[] var2 = var1.getTokens();
      String[] var3 = var1.getStrings();
      Statement var4 = new Statement();
      boolean var5 = false;
      boolean var6 = false;
      int var7 = 0;
      if (var2.length >= 3 && Checkers.isUniPredicate(var3[var7], var3[var7 + 1]) && Checkers.isIndexableItem(var3[var7 + 1], var3[var7 + 2])) {
         var4.add(var2[var7]);
         ++var7;
         var4.add(ParserUtilities.makeToken(var3[var7] + var3[var7 + 1], var2[0]));

         for(var7 += 2; var7 < var2.length; ++var7) {
            var4.add(var2[var7]);
         }

         return ParsePredicate(var0, var4);
      } else {
         if (var2.length == 3) {
            var5 = Checkers.isOrPredicate(var3[var7], var3[var7 + 1], var3[var7 + 2]);
            var6 = Checkers.isAndPredicate(var3[var7], var3[var7 + 1], var3[var7 + 2]);
         } else if (2 < var2.length) {
            int var8 = findPrecedentOperators(var4, var1, 0, "&& ||", 2);
            if (var8 != 0) {
               return ParsePredicate(var0, var4);
            }
         }

         if (var7 + 2 < var2.length && (var5 || var6)) {
            if (var5) {
               var4.setType(803);
            }

            if (var6) {
               var4.setType(804);
            }

            var4.add(var2[var7]);
            var4.add(var2[var7 + 2]);
            var7 += 3;
         } else if (var7 + 2 < var2.length && Checkers.isBiPredicate(var3[var7], var3[var7 + 1], var3[var7 + 2])) {
            var4.setType(801);
            var4.add(var2[var7]);
            var4.add(var2[var7 + 1]);
            var7 += 2;
         } else if (var7 + 1 < var2.length && Checkers.isUniPredicate(var3[var7], var3[var7 + 1])) {
            var4.setType(802);
            var4.add(var2[var7]);
            ++var7;
         } else if (Checkers.isExpression(var3[var7])) {
            var4.setType(805);
         } else {
            var4.setType(806);
         }

         if (var7 < var2.length) {
            StringBuffer var9 = new StringBuffer(var3[var7]);
            ++var7;

            while(var7 < var2.length) {
               var9.append(" ");
               var9.append(var3[var7]);
               ++var7;
            }

            var4.add(new Token(var9.toString(), var2[var7 - 1].getHint()));
         }

         return var4;
      }
   }

   protected static int findPrecedentOperators(Statement var0, TokenList var1, int var2, String var3, int var4) {
      String[] var5 = var1.getStrings();
      Token[] var6 = var1.getTokens();

      for(int var7 = var2; var7 < var6.length; ++var7) {
         if (var5[var7].equals("EOT")) {
            return var2;
         }

         if (var5[var7].length() == var4 && var3.indexOf(var5[var7]) > -1) {
            StringBuffer var8 = new StringBuffer(var5[var2]);

            for(int var9 = var2 + 1; var9 < var7; ++var9) {
               var8.append(" ");
               var8.append(var5[var9]);
            }

            StringBuffer var11 = new StringBuffer(var5[var7 + 1]);

            int var10;
            for(var10 = var7 + 2; var10 < var6.length && !var5[var10].equals("EOT"); ++var10) {
               var11.append(" ");
               var11.append(var5[var10]);
            }

            var0.add(new Token(var8.toString(), var6[var2].getHint()));
            var0.add(var6[var7]);
            var0.add(new Token(var11.toString(), var6[var7 + 1].getHint()));
            return var10;
         }
      }

      return var2;
   }

   public static LinkedList ParseIdea(Parser var0, TokenList var1) {
      Token[] var2 = var1.getTokens();
      String[] var3 = var1.getStrings();
      LinkedList var4 = new LinkedList();

      for(int var6 = 0; var6 < var2.length; ++var6) {
         Statement var5 = new Statement();
         int var8;
         if (var6 + 2 < var2.length && Checkers.isOperator(var3[var6], var3[var6 + 1], var3[var6 + 2])) {
            var5.setType(603);
            int var10 = findPrecedentOperators(var5, var1, var6, "=>", 2);
            if (var10 != var6) {
               var5.setType(612);
               var6 = var10;
            } else {
               var10 = findPrecedentOperators(var5, var1, var6, "+ - .", 1);
               if (var10 != var6) {
                  var6 = var10;
               } else {
                  var5.add(var2[var6]);
                  var5.add(var2[var6 + 1]);
                  var8 = var2[var6 + 2].getHint();
                  StringBuffer var9 = new StringBuffer(var3[var6 + 2]);

                  for(var6 += 3; var6 < var2.length && !var3[var6].equals("EOT"); ++var6) {
                     var9.append(" ");
                     var9.append(var3[var6]);
                  }

                  var5.add(new Token(var9.toString(), var8));
               }
            }
         } else if (Checkers.isIndexableItem(var3[var6])) {
            var5.setType(710);
            Token[] var7 = LexicalAnalyzer.CreateTerms(var0, new StringIterator(var3[var6], var2[var6].getHint())).getTokens();
            var8 = 0;
            if (var8 + 1 < var7.length && Checkers.isFunctionCall(var7[0].toString(), var7[1].toString())) {
               var5.add(ParserUtilities.combineTokens(var7[0], var7[1]));
               var8 += 2;
            }

            while(var8 < var7.length) {
               var5.add(var7[var8]);
               ++var8;
            }
         } else if (Checkers.isIndex(var3[var6])) {
            var5.setType(611);
            var5.add(var2[var6]);
         } else if (Checkers.isFunctionCall(var3[var6])) {
            var5.setType(604);
            var5.add(var2[var6]);
         } else if (Checkers.isIncrementHack(var3[var6])) {
            var5.setType(901);
            var5.add(var2[var6]);
         } else if (Checkers.isDecrementHack(var3[var6])) {
            var5.setType(902);
            var5.add(var2[var6]);
         } else if (Checkers.isVariableReference(var3[var6])) {
            var5.setType(705);
            var5.add(var2[var6]);
         } else if (Checkers.isVariable(var3[var6])) {
            var5.setType(701);
            var5.add(var2[var6]);
         } else if (Checkers.isExpression(var3[var6])) {
            var5.setType(601);
            var5.add(var2[var6]);
         } else if (Checkers.isFunction(var3[var6]) && Checkers.isFunctionReferenceToken(var3[var6])) {
            var5.setType(604);
            var5.add(var2[var6]);
         } else if (Checkers.isString(var3[var6])) {
            var5.setType(605);
            var5.add(var2[var6]);
         } else if (Checkers.isBacktick(var3[var6])) {
            var5.setType(506);
            var5.add(var2[var6]);
         } else if (Checkers.isLiteral(var3[var6])) {
            var5.setType(606);
            var5.add(var2[var6]);
         } else if (Checkers.isNumber(var3[var6])) {
            var5.setType(607);
            var5.add(var2[var6]);
         } else if (Checkers.isDouble(var3[var6])) {
            var5.setType(608);
            var5.add(var2[var6]);
         } else if (Checkers.isBoolean(var3[var6])) {
            var5.setType(609);
            var5.add(var2[var6]);
         } else if (Checkers.isBlock(var3[var6])) {
            var5.setType(613);
            var5.add(var2[var6]);
         } else {
            if (!Checkers.isClassLiteral(var3[var6])) {
               var0.reportError("Unknown expression", new Token(var1.toString(), var2[var6].getHint()));
               return null;
            }

            var5.setType(614);
            var5.add(var2[var6]);
         }

         var4.add(var5);
      }

      return var4;
   }

   public static LinkedList ParseBlocks(Parser var0, TokenList var1) {
      String[] var2 = var1.getStrings();
      Token[] var3 = var1.getTokens();
      LinkedList var4 = new LinkedList();

      for(int var7 = 0; var7 < var3.length; ++var7) {
         Statement var5 = new Statement();
         if (var7 + 5 < var3.length && Checkers.isSpecialForeach(var2[var7], var2[var7 + 1], var2[var7 + 2], var2[var7 + 3], var2[var7 + 4], var2[var7 + 5])) {
            var5.setType(402);
            var5.add(var3[var7]);
            var5.add(var3[var7 + 1]);
            var5.add(var3[var7 + 2]);
            var5.add(var3[var7 + 3]);
            var5.add(var3[var7 + 4]);
            var5.add(var3[var7 + 5]);
            var7 += 5;
         } else if (var7 + 4 < var3.length && Checkers.isTryCatch(var2[var7], var2[var7 + 1], var2[var7 + 2], var2[var7 + 3], var2[var7 + 4])) {
            var5.setType(403);
            var5.add(var3[var7]);
            var5.add(var3[var7 + 1]);
            var5.add(var3[var7 + 2]);
            var5.add(var3[var7 + 3]);
            var5.add(var3[var7 + 4]);
            var7 += 4;
         } else if (var7 + 3 < var3.length && Checkers.isSpecialWhile(var2[var7], var2[var7 + 1], var2[var7 + 2], var2[var7 + 3])) {
            var5.setType(101);
            var5.add(var3[var7]);
            var5.add(var3[var7 + 1]);
            var5.add(var3[var7 + 2]);
            var5.add(var3[var7 + 3]);
            var7 += 3;
         } else if (var7 + 3 < var3.length && Checkers.isForeach(var2[var7], var2[var7 + 1], var2[var7 + 2], var2[var7 + 3])) {
            var5.setType(400);
            var5.add(var3[var7]);
            var5.add(var3[var7 + 1]);
            var5.add(var3[var7 + 2]);
            var5.add(var3[var7 + 3]);
            var7 += 3;
         } else if (var7 + 2 < var3.length && Checkers.isIfStatement(var2[var7], var2[var7 + 1], var2[var7 + 2])) {
            var5.setType(301);
            var5.add(var3[var7]);
            var5.add(var3[var7 + 1]);
            var5.add(var3[var7 + 2]);

            for(var7 += 3; var7 + 3 < var3.length && Checkers.isElseIfStatement(var2[var7], var2[var7 + 1], var2[var7 + 2], var2[var7 + 3]); var7 += 4) {
               var5.add(var3[var7]);
               var5.add(var3[var7 + 1]);
               var5.add(var3[var7 + 2]);
               var5.add(var3[var7 + 3]);
            }

            if (var7 + 1 < var3.length && Checkers.isElseStatement(var2[var7], var2[var7 + 1])) {
               var5.add(var3[var7]);
               var5.add(var3[var7 + 1]);
               var7 += 2;
            }

            --var7;
         } else if (var7 + 2 < var3.length && Checkers.isWhile(var2[var7], var2[var7 + 1], var2[var7 + 2])) {
            var5.setType(100);
            var5.add(var3[var7]);
            var5.add(var3[var7 + 1]);
            var5.add(var3[var7 + 2]);
            var7 += 2;
         } else if (var7 + 2 < var3.length && Checkers.isFor(var2[var7], var2[var7 + 1], var2[var7 + 2])) {
            var5.setType(401);
            var5.add(var3[var7]);
            var5.add(var3[var7 + 1]);
            var5.add(var3[var7 + 2]);
            var7 += 2;
         } else {
            StringBuffer var8;
            int var9;
            if (!Checkers.isReturn(var2[var7]) && !Checkers.isAssert(var2[var7])) {
               if (var7 + 1 < var3.length && Checkers.isImportStatement(var2[var7], var2[var7 + 1])) {
                  var5.setType(444);
                  ++var7;
                  var8 = new StringBuffer();

                  while(var7 < var2.length && !var2[var7].equals("EOT")) {
                     if (var2[var7].equals("from:")) {
                        if (var8.length() == 0) {
                           var0.reportError("Attempted to import '' from:", new Token("import from:", var3[var7].getHint(), "import from:".length()));
                           return null;
                        }

                        var5.add(new Token(var8.toString(), var3[var7].getHint()));
                        var8 = new StringBuffer();
                     } else {
                        var8.append(var2[var7]);
                     }

                     ++var7;
                     if (var7 >= var3.length) {
                        var0.reportError("Missing terminator", new Token(var8.toString(), var3[var7 - 1].getHint(), var8.toString().length()));
                        return null;
                     }
                  }

                  if (var8.length() > 0) {
                     var5.add(new Token(var8.toString(), var3[var7].getHint()));
                  }
               } else if (Checkers.isIndex(var2[var7])) {
                  var5.setType(611);
                  var5.add(var3[var7]);
               } else if (!var2[var7].equals("EOT")) {
                  if (var7 + 2 < var3.length && Checkers.isBindPredicate(var2[var7], var2[var7 + 1], var2[var7 + 2])) {
                     var5.setType(504);
                     var5.add(var3[var7]);
                     var5.add(var3[var7 + 1]);
                     var5.add(var3[var7 + 2]);
                     var7 += 2;
                  } else if (var7 + 2 < var3.length && Checkers.isBind(var2[var7], var2[var7 + 1], var2[var7 + 2])) {
                     var5.setType(502);
                     var5.add(var3[var7]);
                     var5.add(var3[var7 + 1]);
                     var5.add(var3[var7 + 2]);
                     var7 += 2;
                  } else if (Checkers.isIncrementHack(var2[var7])) {
                     var5.setType(901);
                     var5.add(var3[var7]);
                  } else if (Checkers.isDecrementHack(var2[var7])) {
                     var5.setType(902);
                     var5.add(var3[var7]);
                  } else if (Checkers.isBlock(var2[var7])) {
                     var5.setType(150);
                     var5.add(var3[var7]);
                  } else if (Checkers.isBacktick(var2[var7])) {
                     var5.setType(506);
                     var5.add(var3[var7]);
                  } else if (Checkers.isFunctionCall(var2[var7])) {
                     var5.setType(604);
                     var5.add(var3[var7]);
                  } else if (var7 + 3 < var3.length && Checkers.isBindFilter(var2[var7], var2[var7 + 1], var2[var7 + 2], var2[var7 + 3])) {
                     var5.setType(505);
                     var5.add(var3[var7]);
                     var5.add(var3[var7 + 1]);
                     var5.add(var3[var7 + 2]);
                     var5.add(var3[var7 + 3]);
                     var7 += 3;
                  } else {
                     int var6;
                     if ((var6 = findPrecedentOperators(var5, var1, var7, "+= -= *= .= /= %= |= &= ^=", 2)) == var7 && (var6 = findPrecedentOperators(var5, var1, var7, "<<= >>= **=", 3)) == var7) {
                        if ((var6 = findPrecedentOperators(var5, var1, var7, "=", 1)) == var7) {
                           TokenList var10 = new TokenList();

                           for(var9 = var7; var9 < var3.length && !var2[var9].equals("EOT") && var2[var9].indexOf(10) == -1; ++var9) {
                              var10.add(var3[var9]);
                           }

                           var0.reportError("Syntax error", new Token(var10.toString(), var3[var7].getHint()));
                           return null;
                        }

                        if (Checkers.isExpression(var2[var7])) {
                           var5.setType(202);
                        } else {
                           var5.setType(200);
                        }

                        var7 = var6;
                     } else {
                        if (Checkers.isExpression(var2[var7])) {
                           var5.setType(204);
                        } else {
                           var5.setType(203);
                        }

                        var7 = var6;
                     }
                  }
               }
            } else {
               if (Checkers.isAssert(var2[var7])) {
                  var5.setType(507);
               } else {
                  var5.setType(500);
               }

               var5.add(var3[var7]);
               ++var7;
               var8 = new StringBuffer();
               if (var7 == var3.length) {
                  var0.reportError("Missing terminator", new Token(var8.toString(), var3[var7 - 1].getHint(), var8.toString().length()));
                  return null;
               }

               var9 = var3[var7].getHint();

               while(var7 < var2.length && !var2[var7].equals("EOT")) {
                  var8.append(var2[var7]);
                  var8.append(" ");
                  ++var7;
                  if (var7 >= var3.length) {
                     var0.reportError("Missing terminator", new Token(var8.toString(), var3[var7 - 1].getHint(), var8.toString().length()));
                     return null;
                  }
               }

               if (var8.length() > 0) {
                  var5.add(new Token(var8.toString(), var9));
               }
            }
         }

         if (var5.getType() != 0) {
            var4.add(var5);
         }
      }

      return var4;
   }
}
