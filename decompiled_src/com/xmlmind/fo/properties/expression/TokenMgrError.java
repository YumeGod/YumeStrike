package com.xmlmind.fo.properties.expression;

public class TokenMgrError extends Error {
   static final int LEXICAL_ERROR = 0;
   static final int STATIC_LEXER_ERROR = 1;
   static final int INVALID_LEXICAL_STATE = 2;
   static final int LOOP_DETECTED = 3;
   int errorCode;

   protected static final String addEscapes(String var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         switch (var0.charAt(var3)) {
            case '\u0000':
               break;
            case '\b':
               var1.append("\\b");
               break;
            case '\t':
               var1.append("\\t");
               break;
            case '\n':
               var1.append("\\n");
               break;
            case '\f':
               var1.append("\\f");
               break;
            case '\r':
               var1.append("\\r");
               break;
            case '"':
               var1.append("\\\"");
               break;
            case '\'':
               var1.append("\\'");
               break;
            case '\\':
               var1.append("\\\\");
               break;
            default:
               char var2;
               if ((var2 = var0.charAt(var3)) >= ' ' && var2 <= '~') {
                  var1.append(var2);
               } else {
                  String var4 = "0000" + Integer.toString(var2, 16);
                  var1.append("\\u" + var4.substring(var4.length() - 4, var4.length()));
               }
         }
      }

      return var1.toString();
   }

   protected static String LexicalError(boolean var0, int var1, int var2, int var3, String var4, char var5) {
      return "Lexical error at line " + var2 + ", column " + var3 + ".  Encountered: " + (var0 ? "<EOF> " : "\"" + addEscapes(String.valueOf(var5)) + "\"" + " (" + var5 + "), ") + "after : \"" + addEscapes(var4) + "\"";
   }

   public String getMessage() {
      return super.getMessage();
   }

   public TokenMgrError() {
   }

   public TokenMgrError(String var1, int var2) {
      super(var1);
      this.errorCode = var2;
   }

   public TokenMgrError(boolean var1, int var2, int var3, int var4, String var5, char var6, int var7) {
      this(LexicalError(var1, var2, var3, var4, var5, var6), var7);
   }
}
