package sleep.bridges;

import java.text.StringCharacterIterator;
import java.util.regex.PatternSyntaxException;

public class Transliteration {
   public static final int OPTION_DELETE = 1;
   public static final int OPTION_COMPLEMENT = 2;
   public static final int OPTION_SQUEEZE = 4;
   private static String AvailableOptions = "dDsSwW.\\-";
   private int options = 0;
   private Element pattern = null;

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      StringBuffer var2 = new StringBuffer();

      for(Element var3 = this.pattern; var3 != null; var3 = var3.next) {
         if (var3.isSpecial) {
            switch (var3.item) {
               case '.':
                  var1.append("[:ANY:]");
                  break;
               case 'D':
                  var1.append("[:non-digit:]");
                  break;
               case 'S':
                  var1.append("[:non-whitespace:]");
                  break;
               case 'W':
                  var1.append("[:non-word character:]");
                  break;
               case 'd':
                  var1.append("[:digit:]");
                  break;
               case 's':
                  var1.append("[:whitespace:]");
                  break;
               case 'w':
                  var1.append("[:word character:]");
                  break;
               default:
                  var1.append(var3.item);
            }
         } else {
            var1.append(var3.item);
         }

         var2.append(var3.replacement);
      }

      return "tr/" + var1 + "/" + var2 + "/";
   }

   private static String getRange(char var0, char var1) {
      StringBuffer var2 = new StringBuffer();
      char var3;
      if (var0 < var1) {
         for(var3 = var0; var3 < var1; ++var3) {
            var2.append(var3);
         }
      } else if (var0 > var1) {
         for(var3 = var0; var3 > var1; --var3) {
            var2.append(var3);
         }
      }

      return var2.toString();
   }

   private static String expandRanges(String var0) throws PatternSyntaxException {
      StringBuffer var1 = new StringBuffer(var0);

      for(int var2 = 0; var2 < var1.length(); ++var2) {
         if (var1.charAt(var2) == '\\') {
            ++var2;
         } else if (var1.charAt(var2) == '-') {
            if (var2 <= 0 || var2 >= var1.length() - 1) {
               throw new PatternSyntaxException("Dangling range operator '-'", var0, var0.length() - 1);
            }

            String var3 = getRange(var1.charAt(var2 - 1), var1.charAt(var2 + 1));
            var1.replace(var2 - 1, var2 + 1, var3);
            var2 += var3.length() - 2;
         }
      }

      return var1.toString();
   }

   private Element buildPattern(String var1, String var2) {
      Element var3 = null;
      Element var4 = null;
      var1 = expandRanges(var1);
      var2 = expandRanges(var2);
      StringCharacterIterator var5 = new StringCharacterIterator(var1);
      StringCharacterIterator var6 = new StringCharacterIterator(var2);

      while(var5.current() != '\uffff') {
         if (var4 == null) {
            var3 = new Element();
            var4 = var3;
         } else {
            var4.next = new Element();
            var4 = var4.next;
         }

         if (var5.current() == '\\') {
            var4.item = var5.next();
            var4.replacement = var6.current();
            if (var5.current() == '\uffff') {
               throw new PatternSyntaxException("attempting to escape end of pattern string", var1, var5.getEndIndex() - 1);
            }

            if (AvailableOptions.indexOf(var4.item) == -1) {
               throw new PatternSyntaxException("unrecognized escaped meta-character '" + var4.item + "'", var1, var5.getIndex());
            }

            var4.isSpecial = var5.current() != '\\' && var5.current() != '.' && var5.current() != '-';
         } else {
            var4.item = var5.current();
            var4.replacement = var6.current();
            var4.isSpecial = var5.current() == '.';
         }

         var5.next();
         var6.next();
         if (var6.current() == '\uffff' && (this.options & 1) != 1) {
            var6.last();
         }
      }

      return var3;
   }

   public static Transliteration compile(String var0, String var1) throws PatternSyntaxException {
      return compile(var0, var1, 0);
   }

   public static Transliteration compile(String var0, String var1, int var2) throws PatternSyntaxException {
      Transliteration var3 = new Transliteration();
      var3.options = var2;
      var3.pattern = var3.buildPattern(var0, var1);
      return var3;
   }

   private boolean isMatch(char var1, Element var2) {
      boolean var3 = false;
      if (var2.isSpecial) {
         switch (var2.item) {
            case '.':
               var3 = true;
               break;
            case 'D':
               var3 = !Character.isDigit(var1);
               break;
            case 'S':
               var3 = !Character.isWhitespace(var1);
               break;
            case 'W':
               var3 = !Character.isLetter(var1);
               break;
            case 'd':
               var3 = Character.isDigit(var1);
               break;
            case 's':
               var3 = Character.isWhitespace(var1);
               break;
            case 'w':
               var3 = Character.isLetter(var1);
         }
      } else {
         var3 = var2.item == var1;
      }

      if ((this.options & 2) == 2) {
         var3 = !var3;
      }

      return var3;
   }

   public String translate(String var1) {
      StringBuffer var2 = new StringBuffer();
      Element var3 = null;
      boolean var5 = false;

      for(int var6 = 0; var6 < var1.length(); ++var6) {
         char var4 = var1.charAt(var6);
         var3 = this.pattern;

         for(var5 = false; var3 != null; var3 = var3.next) {
            if (this.isMatch(var4, var3)) {
               if (var3.replacement != '\uffff') {
                  var2.append(var3.replacement);
               }

               while((this.options & 4) == 4 && var6 + 1 < var1.length() && var1.charAt(var6 + 1) == var4) {
                  ++var6;
               }

               var5 = true;
               break;
            }
         }

         if (!var5) {
            var2.append(var4);
         }
      }

      return var2.toString();
   }

   private static class Element {
      public char item;
      public char replacement;
      public boolean isSpecial;
      public boolean isWildcard;
      public Element next;

      private Element() {
         this.item = 'x';
         this.replacement = 'x';
         this.isSpecial = false;
         this.isWildcard = false;
         this.next = null;
      }

      // $FF: synthetic method
      Element(Object var1) {
         this();
      }
   }
}
