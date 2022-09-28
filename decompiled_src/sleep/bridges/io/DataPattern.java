package sleep.bridges.io;

import java.nio.ByteOrder;
import java.util.HashMap;

public class DataPattern {
   public DataPattern next = null;
   public int count = 1;
   public char value = ' ';
   public int size = 0;
   public ByteOrder order;
   private static HashMap patternCache = new HashMap();

   public DataPattern() {
      this.order = ByteOrder.BIG_ENDIAN;
   }

   public static int EstimateSize(String var0) {
      DataPattern var1 = Parse(var0);

      int var2;
      for(var2 = 0; var1 != null; var1 = var1.next) {
         if (var1.count > 0) {
            var2 += var1.count * var1.size;
         }
      }

      return var2;
   }

   public static DataPattern Parse(String var0) {
      if (patternCache.get(var0) != null) {
         return (DataPattern)patternCache.get(var0);
      } else {
         DataPattern var1 = null;
         DataPattern var2 = null;
         StringBuffer var3 = null;

         for(int var4 = 0; var4 < var0.length(); ++var4) {
            if (Character.isLetter(var0.charAt(var4))) {
               if (var2 != null) {
                  if (var3.length() > 0) {
                     var2.count = Integer.parseInt(var3.toString());
                  }

                  var2.next = new DataPattern();
                  var2 = var2.next;
               } else {
                  var1 = new DataPattern();
                  var2 = var1;
               }

               var3 = new StringBuffer(3);
               var2.value = var0.charAt(var4);
               switch (var2.value) {
                  case 'B':
                  case 'C':
                  case 'H':
                  case 'b':
                  case 'h':
                  case 'o':
                  case 'x':
                     var2.size = 1;
                  case 'D':
                  case 'E':
                  case 'F':
                  case 'G':
                  case 'J':
                  case 'K':
                  case 'L':
                  case 'M':
                  case 'N':
                  case 'O':
                  case 'P':
                  case 'Q':
                  case 'R':
                  case 'T':
                  case 'V':
                  case 'W':
                  case 'X':
                  case 'Y':
                  case '[':
                  case '\\':
                  case ']':
                  case '^':
                  case '_':
                  case '`':
                  case 'a':
                  case 'e':
                  case 'g':
                  case 'j':
                  case 'k':
                  case 'm':
                  case 'n':
                  case 'p':
                  case 'q':
                  case 'r':
                  case 't':
                  case 'v':
                  case 'w':
                  case 'y':
                  default:
                     break;
                  case 'I':
                  case 'f':
                  case 'i':
                     var2.size = 4;
                     break;
                  case 'S':
                  case 'c':
                  case 's':
                     var2.size = 2;
                     break;
                  case 'U':
                  case 'u':
                     var2.count = -1;
                     var2.size = 2;
                     break;
                  case 'Z':
                  case 'z':
                     var2.count = -1;
                     var2.size = 1;
                     break;
                  case 'd':
                  case 'l':
                     var2.size = 8;
               }
            } else if (var0.charAt(var4) == '*') {
               var2.count = -1;
            } else if (var0.charAt(var4) == '!') {
               var2.order = ByteOrder.nativeOrder();
            } else if (var0.charAt(var4) == '-') {
               var2.order = ByteOrder.LITTLE_ENDIAN;
            } else if (var0.charAt(var4) == '+') {
               var2.order = ByteOrder.BIG_ENDIAN;
            } else if (Character.isDigit(var0.charAt(var4))) {
               var3.append(var0.charAt(var4));
            }
         }

         if (var3.length() > 0) {
            var2.count = Integer.parseInt(var3.toString());
         }

         patternCache.put(var0, var1);
         return var1;
      }
   }
}
