package com.xmlmind.fo.properties.expression;

public class ParseException extends Exception {
   protected boolean specialConstructor;
   public Token currentToken;
   public int[][] expectedTokenSequences;
   public String[] tokenImage;
   protected String eol = System.getProperty("line.separator", "\n");

   public ParseException(Token var1, int[][] var2, String[] var3) {
      super("");
      this.specialConstructor = true;
      this.currentToken = var1;
      this.expectedTokenSequences = var2;
      this.tokenImage = var3;
   }

   public ParseException() {
      this.specialConstructor = false;
   }

   public ParseException(String var1) {
      super(var1);
      this.specialConstructor = false;
   }

   public String getMessage() {
      if (!this.specialConstructor) {
         return super.getMessage();
      } else {
         String var1 = "";
         int var2 = 0;

         for(int var3 = 0; var3 < this.expectedTokenSequences.length; ++var3) {
            if (var2 < this.expectedTokenSequences[var3].length) {
               var2 = this.expectedTokenSequences[var3].length;
            }

            for(int var4 = 0; var4 < this.expectedTokenSequences[var3].length; ++var4) {
               var1 = var1 + this.tokenImage[this.expectedTokenSequences[var3][var4]] + " ";
            }

            if (this.expectedTokenSequences[var3][this.expectedTokenSequences[var3].length - 1] != 0) {
               var1 = var1 + "...";
            }

            var1 = var1 + this.eol + "    ";
         }

         String var6 = "Encountered \"";
         Token var7 = this.currentToken.next;

         for(int var5 = 0; var5 < var2; ++var5) {
            if (var5 != 0) {
               var6 = var6 + " ";
            }

            if (var7.kind == 0) {
               var6 = var6 + this.tokenImage[0];
               break;
            }

            var6 = var6 + this.add_escapes(var7.image);
            var7 = var7.next;
         }

         var6 = var6 + "\" at line " + this.currentToken.next.beginLine + ", column " + this.currentToken.next.beginColumn;
         var6 = var6 + "." + this.eol;
         if (this.expectedTokenSequences.length == 1) {
            var6 = var6 + "Was expecting:" + this.eol + "    ";
         } else {
            var6 = var6 + "Was expecting one of:" + this.eol + "    ";
         }

         var6 = var6 + var1;
         return var6;
      }
   }

   protected String add_escapes(String var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var4 = 0; var4 < var1.length(); ++var4) {
         switch (var1.charAt(var4)) {
            case '\u0000':
               break;
            case '\b':
               var2.append("\\b");
               break;
            case '\t':
               var2.append("\\t");
               break;
            case '\n':
               var2.append("\\n");
               break;
            case '\f':
               var2.append("\\f");
               break;
            case '\r':
               var2.append("\\r");
               break;
            case '"':
               var2.append("\\\"");
               break;
            case '\'':
               var2.append("\\'");
               break;
            case '\\':
               var2.append("\\\\");
               break;
            default:
               char var3;
               if ((var3 = var1.charAt(var4)) >= ' ' && var3 <= '~') {
                  var2.append(var3);
               } else {
                  String var5 = "0000" + Integer.toString(var3, 16);
                  var2.append("\\u" + var5.substring(var5.length() - 4, var5.length()));
               }
         }
      }

      return var2.toString();
   }
}
