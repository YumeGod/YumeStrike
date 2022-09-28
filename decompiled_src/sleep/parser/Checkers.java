package sleep.parser;

import java.util.Hashtable;

public class Checkers {
   protected static Hashtable keywords = new Hashtable();

   public static void addKeyword(String var0) {
      keywords.put(var0, Boolean.TRUE);
   }

   public static boolean isIfStatement(String var0, String var1, String var2) {
      return var0.toString().equals("if") && isExpression(var1.toString()) && isBlock(var2.toString());
   }

   public static boolean isElseStatement(String var0, String var1) {
      return var0.equals("else") && isBlock(var1);
   }

   public static boolean isElseIfStatement(String var0, String var1, String var2, String var3) {
      return var0.equals("else") && isIfStatement(var1, var2, var3);
   }

   public static final boolean isIncrementHack(String var0) {
      return isScalar(var0) && var0.length() > 3 && var0.substring(var0.length() - 2, var0.length()).equals("++");
   }

   public static final boolean isDecrementHack(String var0) {
      return isScalar(var0) && var0.length() > 3 && var0.substring(var0.length() - 2, var0.length()).equals("--");
   }

   public static final boolean isObjectNew(String var0, String var1) {
      return var0.equals("new");
   }

   public static final boolean isClosureCall(String var0, String var1) {
      return var1.equals("EOT");
   }

   public static final boolean isImportStatement(String var0, String var1) {
      return var0.equals("import");
   }

   public static final boolean isClassLiteral(String var0) {
      return var0.length() >= 2 && var0.charAt(0) == '^';
   }

   public static final boolean isClassPiece(String var0) {
      if (var0.length() >= 1 && !isVariable(var0) && Character.isJavaIdentifierStart(var0.charAt(0))) {
         for(int var1 = 1; var1 < var0.length(); ++var1) {
            if (!Character.isJavaIdentifierPart(var0.charAt(var1)) && var0.charAt(var1) != '.') {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static final boolean isClassIdentifier(Parser var0, String var1) {
      return isClassPiece(var1) && var0.findImportedClass(var1) != null;
   }

   public static final boolean isBindFilter(String var0, String var1, String var2, String var3) {
      return isBlock(var3);
   }

   public static final boolean isBindPredicate(String var0, String var1, String var2) {
      return isExpression(var1) && isBlock(var2);
   }

   public static final boolean isBind(String var0, String var1, String var2) {
      return !var1.equals("=") && isBlock(var2);
   }

   public static boolean isHash(String var0) {
      return var0.charAt(0) == '%';
   }

   public static boolean isArray(String var0) {
      return var0.charAt(0) == '@';
   }

   public static boolean isFunctionReferenceToken(String var0) {
      return var0.charAt(0) == '&' && var0.length() > 1 && !var0.equals("&&");
   }

   public static final boolean isVariableReference(String var0) {
      return var0.length() >= 3 && var0.charAt(0) == '\\' && !var0.equals("\\$null") && isVariable(var0.substring(1));
   }

   public static final boolean isVariable(String var0) {
      return isScalar(var0) || isHash(var0) || isArray(var0);
   }

   public static final boolean isScalar(String var0) {
      return var0.charAt(0) == '$';
   }

   public static boolean isIndex(String var0) {
      return var0.charAt(0) == '[' && var0.charAt(var0.length() - 1) == ']';
   }

   public static boolean isExpression(String var0) {
      return var0.charAt(0) == '(' && var0.charAt(var0.length() - 1) == ')';
   }

   public static boolean isBlock(String var0) {
      return var0.charAt(0) == '{' && var0.charAt(var0.length() - 1) == '}';
   }

   public static boolean isFunctionCall(String var0, String var1) {
      return (isFunction(var0) || var0.equals("@") || var0.equals("%")) && isExpression(var1);
   }

   public static boolean isFunction(String var0) {
      return (Character.isJavaIdentifierStart(var0.charAt(0)) || var0.charAt(0) == '&') && var0.charAt(0) != '$' && keywords.get(var0) == null;
   }

   public static boolean isDataLiteral(String var0) {
      return var0.length() > 2 && (var0.substring(0, 2).equals("@(") || var0.substring(0, 2).equals("%("));
   }

   public static boolean isFunctionCall(String var0) {
      return (isFunction(var0) || isDataLiteral(var0)) && var0.indexOf(40) > -1 && var0.indexOf(41) > -1;
   }

   public static boolean isIndexableItem(String var0, String var1) {
      return isIndex(var1) && (isFunctionCall(var0) || isExpression(var0) || isVariable(var0) || isIndex(var0) || isFunctionReferenceToken(var0) || isBacktick(var0));
   }

   public static boolean isIndexableItem(String var0) {
      if (var0.charAt(var0.length() - 1) == ']') {
         int var1 = var0.lastIndexOf(91);
         if (var1 > 0) {
            return isIndexableItem(var0.substring(0, var1), var0.substring(var1, var0.length()));
         }
      }

      return false;
   }

   public static boolean isHashIndex(String var0) {
      return isHash(var0) && var0.indexOf(91) > -1 && var0.indexOf(93) > -1;
   }

   public static boolean isArrayIndex(String var0) {
      return isArray(var0) && var0.indexOf(91) > -1 && var0.indexOf(93) > -1;
   }

   public static boolean isOperator(String var0, String var1, String var2) {
      return true;
   }

   public static final boolean isSpecialWhile(String var0, String var1, String var2, String var3) {
      return isWhile(var0, var2, var3) && isVariable(var1);
   }

   public static final boolean isWhile(String var0, String var1, String var2) {
      return var0.equals("while") && isExpression(var1) && isBlock(var2);
   }

   public static final boolean isFor(String var0, String var1, String var2) {
      return var0.equals("for") && isExpression(var1) && isBlock(var2);
   }

   public static final boolean isTryCatch(String var0, String var1, String var2, String var3, String var4) {
      return var0.equals("try") && var2.equals("catch") && isBlock(var1) && isBlock(var4) && isScalar(var3);
   }

   public static final boolean isForeach(String var0, String var1, String var2, String var3) {
      return var0.equals("foreach") && isVariable(var1) && isExpression(var2) && isBlock(var3);
   }

   public static final boolean isSpecialForeach(String var0, String var1, String var2, String var3, String var4, String var5) {
      return var0.equals("foreach") && isVariable(var1) && var2.equals("=>") && isVariable(var3) && isExpression(var4) && isBlock(var5);
   }

   public static final boolean isAssert(String var0) {
      return var0.equals("assert");
   }

   public static final boolean isReturn(String var0) {
      return var0.equals("return") || var0.equals("done") || var0.equals("halt") || var0.equals("break") || var0.equals("yield") || var0.equals("continue") || var0.equals("throw") || var0.equals("callcc");
   }

   public static final boolean isString(String var0) {
      return var0.charAt(0) == '"' && var0.charAt(var0.length() - 1) == '"';
   }

   public static final boolean isBacktick(String var0) {
      return var0.charAt(0) == '`' && var0.charAt(var0.length() - 1) == '`';
   }

   public static final boolean isLiteral(String var0) {
      return var0.charAt(0) == '\'' && var0.charAt(var0.length() - 1) == '\'';
   }

   public static final boolean isNumber(String var0) {
      try {
         if (var0.endsWith("L")) {
            var0 = var0.substring(0, var0.length() - 1);
            Long.decode(var0);
         } else {
            Integer.decode(var0);
         }

         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static final boolean isDouble(String var0) {
      try {
         Double.parseDouble(var0);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static final boolean isBoolean(String var0) {
      return var0.equals("true") || var0.equals("false");
   }

   public static final boolean isBiPredicate(String var0, String var1, String var2) {
      return true;
   }

   public static final boolean isUniPredicate(String var0, String var1) {
      return var0.charAt(0) == '-' || var0.length() > 1 && var0.charAt(0) == '!' && var0.charAt(1) == '-';
   }

   public static final boolean isAndPredicate(String var0, String var1, String var2) {
      return var1.equals("&&");
   }

   public static final boolean isOrPredicate(String var0, String var1, String var2) {
      return var1.equals("||");
   }

   public static final boolean isComment(String var0) {
      return var0.charAt(0) == '#' && var0.charAt(var0.length() - 1) == '\n';
   }

   public static final boolean isEndOfVar(char var0) {
      return var0 == ' ' || var0 == '\t' || var0 == '\n' || var0 == '$' || var0 == '\\';
   }

   static {
      keywords.put("if", Boolean.TRUE);
      keywords.put("for", Boolean.TRUE);
      keywords.put("while", Boolean.TRUE);
      keywords.put("foreach", Boolean.TRUE);
      keywords.put("&&", Boolean.TRUE);
      keywords.put("||", Boolean.TRUE);
      keywords.put("EOT", Boolean.TRUE);
      keywords.put("EOL", Boolean.TRUE);
      keywords.put("return", Boolean.TRUE);
      keywords.put("halt", Boolean.TRUE);
      keywords.put("done", Boolean.TRUE);
      keywords.put("break", Boolean.TRUE);
      keywords.put("continue", Boolean.TRUE);
      keywords.put("yield", Boolean.TRUE);
      keywords.put("throw", Boolean.TRUE);
      keywords.put("try", Boolean.TRUE);
      keywords.put("catch", Boolean.TRUE);
      keywords.put("assert", Boolean.TRUE);
   }
}
