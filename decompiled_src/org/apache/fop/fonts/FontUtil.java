package org.apache.fop.fonts;

public class FontUtil {
   private static final String[] ITALIC_WORDS = new String[]{"italic", "oblique", "inclined"};
   private static final String[] LIGHT_WORDS = new String[]{"light"};
   private static final String[] MEDIUM_WORDS = new String[]{"medium"};
   private static final String[] DEMI_WORDS = new String[]{"demi", "semi"};
   private static final String[] BOLD_WORDS = new String[]{"bold"};
   private static final String[] EXTRA_BOLD_WORDS = new String[]{"extrabold", "extra bold", "black", "heavy", "ultra", "super"};

   public static int parseCSS2FontWeight(String text) {
      int weight = true;

      int weight;
      try {
         weight = Integer.parseInt(text);
         weight = weight / 100 * 100;
         weight = Math.max(weight, 100);
         weight = Math.min(weight, 900);
      } catch (NumberFormatException var3) {
         if (text.equals("normal")) {
            weight = 400;
         } else {
            if (!text.equals("bold")) {
               throw new IllegalArgumentException("Illegal value for font weight: '" + text + "'. Use one of: 100, 200, 300, " + "400, 500, 600, 700, 800, 900, " + "normal (=400), bold (=700)");
            }

            weight = 700;
         }
      }

      return weight;
   }

   public static String stripWhiteSpace(String str) {
      if (str != null) {
         StringBuffer stringBuffer = new StringBuffer(str.length());
         int i = 0;

         for(int strLen = str.length(); i < strLen; ++i) {
            char ch = str.charAt(i);
            if (ch != ' ' && ch != '\r' && ch != '\n' && ch != '\t') {
               stringBuffer.append(ch);
            }
         }

         return stringBuffer.toString();
      } else {
         return str;
      }
   }

   public static String guessStyle(String fontName) {
      if (fontName != null) {
         for(int i = 0; i < ITALIC_WORDS.length; ++i) {
            if (fontName.indexOf(ITALIC_WORDS[i]) != -1) {
               return "italic";
            }
         }
      }

      return "normal";
   }

   public static int guessWeight(String fontName) {
      int weight = 400;

      int i;
      for(i = 0; i < BOLD_WORDS.length; ++i) {
         if (fontName.indexOf(BOLD_WORDS[i]) != -1) {
            weight = 700;
            break;
         }
      }

      for(i = 0; i < MEDIUM_WORDS.length; ++i) {
         if (fontName.indexOf(MEDIUM_WORDS[i]) != -1) {
            weight = 500;
            break;
         }
      }

      for(i = 0; i < DEMI_WORDS.length; ++i) {
         if (fontName.indexOf(DEMI_WORDS[i]) != -1) {
            weight = 600;
            break;
         }
      }

      for(i = 0; i < EXTRA_BOLD_WORDS.length; ++i) {
         if (fontName.indexOf(EXTRA_BOLD_WORDS[i]) != -1) {
            weight = 800;
            break;
         }
      }

      for(i = 0; i < LIGHT_WORDS.length; ++i) {
         if (fontName.indexOf(LIGHT_WORDS[i]) != -1) {
            weight = 200;
            break;
         }
      }

      return weight;
   }
}
