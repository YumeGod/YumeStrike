package sleep.parser;

public class LexicalAnalyzer {
   protected static Rule PAREN_RULE = new Rule("Mismatched Parentheses - missing open paren", "Mismatched Parentheses - missing close paren", '(', ')');
   protected static Rule BLOCK_RULE = new Rule("Mismatched Braces - missing open brace", "Mismatched Braces - missing close brace", '{', '}');
   protected static Rule INDEX_RULE = new Rule("Mismatched Indices - missing open index", "Mismatched Indices - missing close index", '[', ']');
   protected static Rule DQUOTE_RULE = new Rule("Runaway string", '"');
   protected static Rule SQUOTE_RULE = new Rule("Runaway string", '\'');
   protected static Rule BACKTICK_RULE = new Rule("Runaway string", '`');
   protected static Rule COMMENT_RULE = new CommentRule();
   protected static char EndOfTerm = ';';

   private static boolean isSkippable(Parser var0, char var1) {
      return isWhite(var1) || isEndOfTerm(var0, var1) || isEndOfLine(var1);
   }

   private static boolean isWhite(char var0) {
      return var0 == ' ' || var0 == '\t';
   }

   private static boolean isBuiltInOperator(char var0, StringBuffer var1, StringIterator var2) {
      return var0 == '.' && (var1.length() <= 0 || !Character.isDigit(var1.charAt(var1.length() - 1)) || var1.charAt(0) == '$') && !var2.isNextChar('=');
   }

   private static boolean isEndOfTerm(Parser var0, char var1) {
      return var1 == var0.EndOfTerm;
   }

   private static boolean isEndOfLine(char var0) {
      return var0 == '\n' || var0 == '\r';
   }

   public static TokenList GroupBlockTokens(Parser var0, StringIterator var1) {
      return GroupTokens(var0, var1, ';');
   }

   public static TokenList GroupExpressionIndexTokens(Parser var0, StringIterator var1) {
      return GroupTokens(var0, var1, ':');
   }

   private static TokenList GroupTokens(Parser var0, StringIterator var1, char var2) {
      var0.setEndOfTerm(var2);
      Token[] var3 = CreateTerms(var0, var1).getTokens();
      TokenList var4 = new TokenList();
      StringBuffer var5 = new StringBuffer();
      int var6 = 0;

      for(int var7 = 0; var7 < var3.length; ++var7) {
         if (var7 + 1 < var3.length) {
            String var8 = var3[var7].toString();
            String var9 = var3[var7 + 1].toString();
            var6 = var7;
            if (var7 + 2 < var3.length && Checkers.isClassLiteral(var8) && var9.equals(".")) {
               var5.append(var3[var7]);

               while(var7 + 2 < var3.length && var3[var7 + 1].toString().equals(".") && Checkers.isClassPiece(var3[var7 + 2].toString())) {
                  var5.append(".");
                  var5.append(var3[var7 + 2]);
                  var7 += 2;
               }
            } else if (Checkers.isFunctionCall(var8, var9) || Checkers.isIndexableItem(var8, var9)) {
               var5.append(var8.toString());
               var5.append(var9.toString());
               ++var7;

               while(var7 + 1 < var3.length && Checkers.isIndex(var3[var7 + 1].toString())) {
                  var5.append(var3[var7 + 1].toString());
                  ++var7;
               }
            }
         }

         if (var5.length() > 0) {
            var4.add(ParserUtilities.makeToken(var5.toString(), var3[var6]));
            var5 = new StringBuffer();
         } else if (!Checkers.isComment(var3[var7].toString())) {
            var4.add(var3[var7]);
         } else {
            var0.addComment(var3[var7].toString());
         }
      }

      return var4;
   }

   public static TokenList GroupParameterTokens(Parser var0, StringIterator var1) {
      return GroupTokens(var0, var1, ',');
   }

   public static TokenList CreateTerms(Parser var0, StringIterator var1) {
      return CreateTerms(var0, var1, true, true);
   }

   public static TokenList CreateTerms(Parser var0, StringIterator var1, boolean var2, boolean var3) {
      Rule[] var4 = new Rule[]{PAREN_RULE.copyRule(), BLOCK_RULE.copyRule(), DQUOTE_RULE.copyRule(), SQUOTE_RULE.copyRule(), INDEX_RULE.copyRule(), BACKTICK_RULE.copyRule(), COMMENT_RULE};
      return CreateTerms(var0, var1, var4, var2, var3);
   }

   private static String AdvanceTerms(Parser var0, StringIterator var1, Rule var2, Rule[] var3) {
      var2.witnessOpen(new Token(var1.getEntireLine(), var1.getLineNumber(), var1.getLineMarker()));
      StringBuffer var4 = new StringBuffer();
      int var5 = var1.getLineNumber();

      while(true) {
         while(var1.hasNext()) {
            char var6 = var1.next();
            if (var6 != '\\' || var2.getType() != Rule.PRESERVE_SINGLE || var2 == COMMENT_RULE) {
               if (var2.isRight(var6) || var2.isMatch(var6)) {
                  var2.witnessClose(new Token(var1.getEntireLine(), var1.getLineNumber(), var1.getLineMarker()));
                  return var4.toString();
               }

               if (var2.getType() != Rule.PRESERVE_SINGLE && var2 != COMMENT_RULE) {
                  boolean var7 = false;

                  for(int var8 = 0; var8 < var3.length; ++var8) {
                     if (var3[var8].isLeft(var6) || var3[var8].isMatch(var6)) {
                        String var9 = AdvanceTerms(var0, var1, var3[var8], var3);
                        if (var9 == null) {
                           return null;
                        }

                        var4.append(var3[var8].wrap(var9));
                        var7 = true;
                        break;
                     }

                     if (var3[var8].isRight(var6) && var3[var8] != var2) {
                        var3[var8].witnessClose(new Token(var1.getEntireLine(), var1.getLineNumber(), var1.getLineMarker()));
                     }
                  }

                  if (!var7) {
                     var4.append(var6);
                  }
               } else {
                  var4.append(var6);
               }
            } else if (!var1.hasNext() && var0 != null) {
               var0.reportError("Escape is end of string", new Token(var4.toString(), var1.getLineNumber(), var1.getLineMarker()));
            } else {
               var4.append(var6);
               var4.append(var1.next());
            }
         }

         return null;
      }
   }

   public static TokenList CreateTerms(Parser var0, StringIterator var1, Rule[] var2, boolean var3, boolean var4) {
      TokenList var5 = new TokenList();
      boolean var6 = false;
      StringBuffer var8 = new StringBuffer();

      while(true) {
         while(true) {
            Token var7;
            int var9;
            do {
               if (!var1.hasNext()) {
                  if (var8.length() > 0) {
                     var7 = new Token(trim(var0, var8.toString()), var1.getLineNumber());
                     var5.add(var7);
                  }

                  for(var9 = 0; var9 < var2.length; ++var9) {
                     if (!var2[var9].isBalanced()) {
                        var0.reportError(var2[var9].getSyntaxError());
                     }
                  }

                  return var5;
               }

               var6 = false;
               var9 = var1.next();

               for(int var10 = 0; var10 < var2.length; ++var10) {
                  if (var2[var10].isLeft((char)var9) || var2[var10].isMatch((char)var9)) {
                     if (var8.length() > 0) {
                        var7 = new Token(trim(var0, var8.toString()), var1.getLineNumber());
                        var5.add(var7);
                        var8 = new StringBuffer();
                     }

                     int var11 = var1.getLineNumber();
                     String var12 = AdvanceTerms(var0, var1, var2[var10], var2);
                     if (var12 == null) {
                        var12 = "";
                     }

                     var7 = new Token(var2[var10].wrap(var12), var11);
                     var5.add(var7);
                     var6 = true;
                     break;
                  }

                  if (var2[var10].isRight((char)var9)) {
                     var2[var10].witnessClose(new Token(var1.getEntireLine(), var1.getLineNumber()));
                  }
               }
            } while(var6);

            if (isEndOfTerm(var0, (char)var9)) {
               if (var8.length() > 0) {
                  var7 = new Token(trim(var0, var8.toString()), var1.getLineNumber());
                  var5.add(var7);
                  var8 = new StringBuffer();
               }

               var7 = new Token("EOT", var1.getLineNumber());
               var5.add(var7);
            } else if (isBuiltInOperator((char)var9, var8, var1)) {
               if (var8.length() > 0) {
                  var7 = new Token(trim(var0, var8.toString()), var1.getLineNumber());
                  var5.add(var7);
                  var8 = new StringBuffer();
               }

               var5.add(new Token(var9 + "", var1.getLineNumber()));
            } else if (isSkippable(var0, (char)var9)) {
               if (var8.length() > 0) {
                  if (var8.length() == 1 && var8.charAt(0) == '%') {
                     var7 = new Token("% ", var1.getLineNumber());
                  } else {
                     var7 = new Token(trim(var0, var8.toString()), var1.getLineNumber());
                  }

                  var5.add(var7);
                  var8 = new StringBuffer();
               }
            } else {
               var8.append((char)var9);
            }
         }
      }
   }

   public static String trim(Parser var0, String var1) {
      if (var1.length() != 0 && !var1.equals(" ")) {
         int var2;
         for(var2 = 0; var2 < var1.length() && isSkippable(var0, var1.charAt(var2)); ++var2) {
         }

         int var3;
         for(var3 = var1.length() - 1; var3 > 0 && isSkippable(var0, var1.charAt(var3)); --var3) {
         }

         return var2 > var3 ? "" : var1.substring(var2, var3 + 1);
      } else {
         return "";
      }
   }
}
