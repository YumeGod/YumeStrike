package sleep.parser;

import java.util.Iterator;

public class ParserUtilities {
   public static Token combineTokens(Token var0, Token var1) {
      return new Token(var0.toString() + var1.toString(), var0.getHint());
   }

   public static Token makeToken(String var0, Token var1) {
      return new Token(var0, var1.getHint());
   }

   public static Token[] get(Token[] var0, int var1, int var2) {
      Token[] var3 = new Token[var2 - var1];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = var0[var1 + var4];
      }

      return var3;
   }

   public static Token join(Token[] var0) {
      return join(var0, " ");
   }

   public static Token join(Token[] var0, String var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var0.length; ++var3) {
         if ((var3 <= 0 || var0[var3].getHint() != var0[var3 - 1].getTopHint()) && var3 != 0) {
            int var4 = var0[var3].getHint() - var0[var3 - 1].getTopHint();

            for(int var5 = 0; var5 < var4; ++var5) {
               var2.append("\n");
            }
         } else {
            var2.append(var1);
         }

         var2.append(var0[var3].toString());
      }

      return new Token(var2.toString(), var0[0].getHint());
   }

   public static Token extract(Token var0) {
      return new Token(extract(var0.toString()), var0.getHint());
   }

   public static String extract(String var0) {
      return var0.substring(1, var0.length() - 1);
   }

   public static TokenList groupByBlockTerm(Parser var0, Token var1) {
      StringIterator var2 = new StringIterator(var1.toString(), var1.getHint());
      TokenList var3 = LexicalAnalyzer.GroupBlockTokens(var0, var2);
      return groupByTerm(var3);
   }

   public static TokenList groupByMessageTerm(Parser var0, Token var1) {
      StringIterator var2 = new StringIterator(var1.toString(), var1.getHint());
      TokenList var3 = LexicalAnalyzer.GroupExpressionIndexTokens(var0, var2);
      return groupByTerm(var3);
   }

   public static TokenList groupByParameterTerm(Parser var0, Token var1) {
      StringIterator var2 = new StringIterator(var1.toString(), var1.getHint());
      TokenList var3 = LexicalAnalyzer.GroupParameterTokens(var0, var2);
      return groupByTerm(var3);
   }

   private static TokenList groupByTerm(TokenList var0) {
      TokenList var1 = new TokenList();
      if (var0.getList().size() == 0) {
         return var1;
      } else {
         StringBuffer var2 = new StringBuffer();
         int var3 = -1;
         Iterator var4 = var0.getList().iterator();

         while(var4.hasNext()) {
            Token var5 = (Token)var4.next();
            var3 = var3 == -1 ? var5.getHint() : var3;
            if (var5.toString().equals("EOT")) {
               var1.add(new Token(var2.toString(), var3));
               var2 = new StringBuffer();
               var3 = -1;
            } else {
               if (var2.length() > 0) {
                  var2.append(" ");
               }

               var2.append(var5.toString());
            }
         }

         if (var2.length() > 0) {
            var1.add(new Token(var2.toString(), var3));
         }

         return var1;
      }
   }
}
