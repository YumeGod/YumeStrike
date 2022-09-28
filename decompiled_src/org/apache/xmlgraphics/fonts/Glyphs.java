package org.apache.xmlgraphics.fonts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import org.apache.commons.io.IOUtils;

public class Glyphs {
   public static final String NOTDEF = ".notdef";
   public static final String[] MAC_GLYPH_NAMES;
   public static final String[] TEX8R_GLYPH_NAMES;
   public static final char[] WINANSI_ENCODING;
   public static final char[] ADOBECYRILLIC_ENCODING;
   private static final String[] UNICODE_GLYPHS;
   private static final String[] DINGBATS_GLYPHS;
   private static final Map CHARNAME_ALTERNATIVES;
   private static final Map CHARNAMES_TO_UNICODE;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   private static void addAlternatives(Map map, String[] alternatives) {
      int i = 0;

      for(int c = alternatives.length; i < c; ++i) {
         String[] alt = new String[c - 1];
         int idx = 0;

         for(int j = 0; j < c; ++j) {
            if (i != j) {
               alt[idx] = alternatives[j];
               ++idx;
            }
         }

         map.put(alternatives[i], alt);
      }

   }

   private static String[] loadGlyphList(String filename, Map charNameToUnicodeMap) {
      List lines = new ArrayList();
      InputStream in = Glyphs.class.getResourceAsStream(filename);
      if (in == null) {
         throw new Error("Cannot load " + filename + ". The Glyphs class cannot properly be initialized!");
      } else {
         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "US-ASCII"));

            String line;
            while((line = reader.readLine()) != null) {
               if (!line.startsWith("#")) {
                  lines.add(line);
               }
            }
         } catch (UnsupportedEncodingException var18) {
            throw new Error("Incompatible JVM! US-ASCII encoding is not supported. The Glyphs class cannot properly be initialized!");
         } catch (IOException var19) {
            throw new Error("I/O error while loading " + filename + ". The Glyphs class cannot properly be initialized!");
         } finally {
            IOUtils.closeQuietly(in);
         }

         String[] arr = new String[lines.size() * 2];
         int pos = 0;
         StringBuffer buf = new StringBuffer();
         int i = 0;

         for(int c = lines.size(); i < c; ++i) {
            String line = (String)lines.get(i);
            int semicolon = line.indexOf(59);
            if (semicolon > 0) {
               String charName = line.substring(0, semicolon);
               String rawUnicode = line.substring(semicolon + 1);
               buf.setLength(0);
               StringTokenizer tokenizer = new StringTokenizer(rawUnicode, " ", false);

               String unicode;
               while(tokenizer.hasMoreTokens()) {
                  unicode = tokenizer.nextToken();
                  if (!$assertionsDisabled && unicode.length() != 4) {
                     throw new AssertionError();
                  }

                  buf.append(hexToChar(unicode));
               }

               unicode = buf.toString();
               arr[pos] = unicode;
               ++pos;
               arr[pos] = charName;
               ++pos;
               if (!$assertionsDisabled && charNameToUnicodeMap.containsKey(charName)) {
                  throw new AssertionError();
               }

               charNameToUnicodeMap.put(charName, unicode);
            }
         }

         return arr;
      }
   }

   private static char hexToChar(String hex) {
      return (char)Integer.parseInt(hex, 16);
   }

   public static final String charToGlyphName(char ch) {
      return stringToGlyph(Character.toString(ch));
   }

   public static final String getUnicodeSequenceForGlyphName(String glyphName) {
      int period = glyphName.indexOf(46);
      if (period >= 0) {
         glyphName = glyphName.substring(0, period);
      }

      StringBuffer sb = new StringBuffer();
      StringTokenizer tokenizer = new StringTokenizer(glyphName, "_", false);

      while(true) {
         while(true) {
            while(tokenizer.hasMoreTokens()) {
               String token = tokenizer.nextToken();
               String sequence = (String)CHARNAMES_TO_UNICODE.get(token);
               if (sequence == null) {
                  if (token.startsWith("uni")) {
                     int len = token.length();

                     for(int pos = 3; pos + 4 <= len; pos += 4) {
                        try {
                           sb.append(hexToChar(token.substring(pos, pos + 4)));
                        } catch (NumberFormatException var10) {
                           return null;
                        }
                     }
                  } else if (token.startsWith("u")) {
                     if (token.length() > 5) {
                        return null;
                     }

                     if (token.length() < 5) {
                        return null;
                     }

                     try {
                        sb.append(hexToChar(token.substring(1, 5)));
                     } catch (NumberFormatException var9) {
                        return null;
                     }
                  }
               } else {
                  sb.append(sequence);
               }
            }

            if (sb.length() == 0) {
               return null;
            }

            return sb.toString();
         }
      }
   }

   /** @deprecated */
   public static final String glyphToString(String name) {
      for(int i = 0; i < UNICODE_GLYPHS.length; i += 2) {
         if (UNICODE_GLYPHS[i + 1].equals(name)) {
            return UNICODE_GLYPHS[i];
         }
      }

      return "";
   }

   public static String stringToGlyph(String name) {
      int i;
      for(i = 0; i < UNICODE_GLYPHS.length; i += 2) {
         if (UNICODE_GLYPHS[i].equals(name)) {
            return UNICODE_GLYPHS[i + 1];
         }
      }

      for(i = 0; i < DINGBATS_GLYPHS.length; i += 2) {
         if (DINGBATS_GLYPHS[i].equals(name)) {
            return DINGBATS_GLYPHS[i + 1];
         }
      }

      return "";
   }

   public static String[] getCharNameAlternativesFor(String charName) {
      return (String[])CHARNAME_ALTERNATIVES.get(charName);
   }

   static {
      $assertionsDisabled = !Glyphs.class.desiredAssertionStatus();
      MAC_GLYPH_NAMES = new String[]{".notdef", ".null", "CR", "space", "exclam", "quotedbl", "numbersign", "dollar", "percent", "ampersand", "quotesingle", "parenleft", "parenright", "asterisk", "plus", "comma", "hyphen", "period", "slash", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "colon", "semicolon", "less", "equal", "greater", "question", "at", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash", "bracketright", "asciicircum", "underscore", "grave", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "braceleft", "bar", "braceright", "asciitilde", "Adieresis", "Aring", "Ccedilla", "Eacute", "Ntilde", "Odieresis", "Udieresis", "aacute", "agrave", "acircumflex", "adieresis", "atilde", "aring", "ccedilla", "eacute", "egrave", "ecircumflex", "edieresis", "iacute", "igrave", "icircumflex", "idieresis", "ntilde", "oacute", "ograve", "ocircumflex", "odieresis", "otilde", "uacute", "ugrave", "ucircumflex", "udieresis", "dagger", "degree", "cent", "sterling", "section", "bullet", "paragraph", "germandbls", "registered", "copyright", "trademark", "acute", "dieresis", "notequal", "AE", "Oslash", "infinity", "plusminus", "lessequal", "greaterequal", "yen", "mu", "partialdiff", "Sigma", "Pi", "pi", "integral", "ordfeminine", "ordmasculine", "Omega", "ae", "oslash", "questiondown", "exclamdown", "logicalnot", "radical", "florin", "approxequal", "Delta", "guillemotleft", "guillemotright", "ellipsis", "nbspace", "Agrave", "Atilde", "Otilde", "OE", "oe", "endash", "emdash", "quotedblleft", "quotedblright", "quoteleft", "quoteright", "divide", "lozenge", "ydieresis", "Ydieresis", "fraction", "currency", "guilsinglleft", "guilsinglright", "fi", "fl", "daggerdbl", "periodcentered", "quotesinglbase", "quotedblbase", "perthousand", "Acircumflex", "Ecircumflex", "Aacute", "Edieresis", "Egrave", "Iacute", "Icircumflex", "Idieresis", "Igrave", "Oacute", "Ocircumflex", "applelogo", "Ograve", "Uacute", "Ucircumflex", "Ugrave", "dotlessi", "circumflex", "tilde", "macron", "breve", "dotaccent", "ring", "cedilla", "hungarumlaut", "ogonek", "caron", "Lslash", "lslash", "Scaron", "scaron", "Zcaron", "zcaron", "brokenbar", "Eth", "eth", "Yacute", "yacute", "Thorn", "thorn", "minus", "multiply", "onesuperior", "twosuperior", "threesuperior", "onehalf", "onequarter", "threequarters", "franc", "Gbreve", "gbreve", "Idot", "Scedilla", "scedilla", "Cacute", "cacute", "Ccaron", "ccaron", "dmacron"};
      TEX8R_GLYPH_NAMES = new String[]{".notdef", "dotaccent", "fi", "fl", "fraction", "hungarumlaut", "Lslash", "lslash", "ogonek", "ring", ".notdef", "breve", "minus", ".notdef", "Zcaron", "zcaron", "caron", "dotlessi", "dotlessj", "ff", "ffi", "ffl", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef", "grave", "quotesingle", "space", "exclam", "quotedbl", "numbersign", "dollar", "percent", "ampersand", "quoteright", "parenleft", "parenright", "asterisk", "plus", "comma", "hyphen", "period", "slash", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "colon", "semicolon", "less", "equal", "greater", "question", "at", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash", "bracketright", "asciicircum", "underscore", "quoteleft", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "braceleft", "bar", "braceright", "asciitilde", ".notdef", "Euro", ".notdef", "quotesinglbase", "florin", "quotedblbase", "ellipsis", "dagger", "daggerdbl", "circumflex", "perthousand", "Scaron", "guilsinglleft", "OE", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef", "quotedblleft", "quotedblright", "bullet", "endash", "emdash", "tilde", "trademark", "scaron", "guilsinglright", "oe", ".notdef", ".notdef", "Ydieresis", ".notdef", "exclamdown", "cent", "sterling", "currency", "yen", "brokenbar", "section", "dieresis", "copyright", "ordfeminine", "guillemotleft", "logicalnot", "hyphen", "registered", "macron", "degree", "plusminus", "twosuperior", "threesuperior", "acute", "mu", "paragraph", "periodcentered", "cedilla", "onesuperior", "ordmasculine", "guillemotright", "onequarter", "onehalf", "threequarters", "questiondown", "Agrave", "Aacute", "Acircumflex", "Atilde", "Adieresis", "Aring", "AE", "Ccedilla", "Egrave", "Eacute", "Ecircumflex", "Edieresis", "Igrave", "Iacute", "Icircumflex", "Idieresis", "Eth", "Ntilde", "Ograve", "Oacute", "Ocircumflex", "Otilde", "Odieresis", "multiply", "Oslash", "Ugrave", "Uacute", "Ucircumflex", "Udieresis", "Yacute", "Thorn", "germandbls", "agrave", "aacute", "acircumflex", "atilde", "adieresis", "aring", "ae", "ccedilla", "egrave", "eacute", "ecircumflex", "edieresis", "igrave", "iacute", "icircumflex", "idieresis", "eth", "ntilde", "ograve", "oacute", "ocircumflex", "otilde", "odieresis", "divide", "oslash", "ugrave", "uacute", "ucircumflex", "udieresis", "yacute", "thorn", "ydieresis"};
      WINANSI_ENCODING = new char[]{'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '‘', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', '•', '€', '•', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '•', 'Ž', '•', '•', '‘', '’', '“', '”', '•', '–', '—', '~', '™', 'š', '›', 'œ', '•', 'ž', 'Ÿ', ' ', '¡', '¢', '£', '¤', '¥', '¦', '§', '¨', '©', 'ª', '«', '¬', '\u00ad', '®', '¯', '°', '±', '²', '³', '´', 'µ', '¶', '·', '¸', '¹', 'º', '»', '¼', '½', '¾', '¿', 'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Æ', 'Ç', 'È', 'É', 'Ê', 'Ë', 'Ì', 'Í', 'Î', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', '×', 'Ø', 'Ù', 'Ú', 'Û', 'Ü', 'Ý', 'Þ', 'ß', 'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó', 'ô', 'õ', 'ö', '÷', 'ø', 'ù', 'ú', 'û', 'ü', 'ý', 'þ', 'ÿ'};
      ADOBECYRILLIC_ENCODING = new char[]{'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', '\u0000', 'Ђ', 'Ѓ', '‚', 'ѓ', '„', '…', '†', '‡', '€', '‰', 'Љ', '‹', 'Њ', 'Ќ', 'Ћ', 'Џ', 'ђ', '‘', '’', '“', '”', '•', '–', '—', '\u0000', '™', 'љ', '›', 'њ', 'ќ', 'ћ', 'џ', ' ', 'Ў', 'ў', 'Ј', '¤', 'Ґ', '¦', '§', 'Ё', '©', 'Є', '«', '¬', '\u00ad', '®', 'Ї', '°', '±', 'І', 'і', 'ґ', 'µ', '¶', '·', 'ё', '№', 'є', '»', 'ј', 'Ѕ', 'ѕ', 'ї', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};
      Map map = new TreeMap();
      UNICODE_GLYPHS = loadGlyphList("glyphlist.txt", map);
      DINGBATS_GLYPHS = loadGlyphList("zapfdingbats.txt", map);
      CHARNAMES_TO_UNICODE = Collections.unmodifiableMap(map);
      map = new TreeMap();
      addAlternatives(map, new String[]{"Omega", "Omegagreek"});
      addAlternatives(map, new String[]{"Delta", "Deltagreek"});
      addAlternatives(map, new String[]{"fraction", "divisionslash"});
      addAlternatives(map, new String[]{"hyphen", "sfthyphen", "softhyphen", "minus"});
      addAlternatives(map, new String[]{"macron", "overscore"});
      addAlternatives(map, new String[]{"mu", "mu1", "mugreek"});
      addAlternatives(map, new String[]{"periodcentered", "middot", "bulletoperator", "anoteleia"});
      addAlternatives(map, new String[]{"space", "nonbreakingspace", "nbspace"});
      addAlternatives(map, new String[]{"zero", "zerooldstyle"});
      addAlternatives(map, new String[]{"one", "oneoldstyle"});
      addAlternatives(map, new String[]{"two", "twooldstyle"});
      addAlternatives(map, new String[]{"three", "threeoldstyle"});
      addAlternatives(map, new String[]{"four", "fouroldstyle"});
      addAlternatives(map, new String[]{"five", "fiveoldstyle"});
      addAlternatives(map, new String[]{"six", "sixoldstyle"});
      addAlternatives(map, new String[]{"seven", "sevenoldstyle"});
      addAlternatives(map, new String[]{"eight", "eightoldstyle"});
      addAlternatives(map, new String[]{"nine", "nineoldstyle"});
      addAlternatives(map, new String[]{"cent", "centoldstyle"});
      addAlternatives(map, new String[]{"dollar", "dollaroldstyle"});
      addAlternatives(map, new String[]{"Acyrillic", "afii10017"});
      addAlternatives(map, new String[]{"Becyrillic", "afii10018"});
      addAlternatives(map, new String[]{"Vecyrillic", "afii10019"});
      addAlternatives(map, new String[]{"Gecyrillic", "afii10020"});
      addAlternatives(map, new String[]{"Decyrillic", "afii10021"});
      addAlternatives(map, new String[]{"Iecyrillic", "afii10022"});
      addAlternatives(map, new String[]{"Iocyrillic", "afii10023"});
      addAlternatives(map, new String[]{"Zhecyrillic", "afii10024"});
      addAlternatives(map, new String[]{"Zecyrillic", "afii10025"});
      addAlternatives(map, new String[]{"Iicyrillic", "afii10026"});
      addAlternatives(map, new String[]{"Iishortcyrillic", "afii10027"});
      addAlternatives(map, new String[]{"Kacyrillic", "afii10028"});
      addAlternatives(map, new String[]{"Elcyrillic", "afii10029"});
      addAlternatives(map, new String[]{"Emcyrillic", "afii10030"});
      addAlternatives(map, new String[]{"Encyrillic", "afii10031"});
      addAlternatives(map, new String[]{"Ocyrillic", "afii10032"});
      addAlternatives(map, new String[]{"Pecyrillic", "afii10033"});
      addAlternatives(map, new String[]{"Ercyrillic", "afii10034"});
      addAlternatives(map, new String[]{"Escyrillic", "afii10035"});
      addAlternatives(map, new String[]{"Tecyrillic", "afii10036"});
      addAlternatives(map, new String[]{"Ucyrillic", "afii10037"});
      addAlternatives(map, new String[]{"Efcyrillic", "afii10038"});
      addAlternatives(map, new String[]{"Khacyrillic", "afii10039"});
      addAlternatives(map, new String[]{"Tsecyrillic", "afii10040"});
      addAlternatives(map, new String[]{"Checyrillic", "afii10041"});
      addAlternatives(map, new String[]{"Shacyrillic", "afii10042"});
      addAlternatives(map, new String[]{"Shchacyrillic", "afii10043"});
      addAlternatives(map, new String[]{"Hardsigncyrillic", "afii10044"});
      addAlternatives(map, new String[]{"Yericyrillic", "afii10045"});
      addAlternatives(map, new String[]{"Softsigncyrillic", "afii10046"});
      addAlternatives(map, new String[]{"Ereversedcyrillic", "afii10047"});
      addAlternatives(map, new String[]{"IUcyrillic", "afii10048"});
      addAlternatives(map, new String[]{"IAcyrillic", "afii10049"});
      addAlternatives(map, new String[]{"acyrillic", "afii10065"});
      addAlternatives(map, new String[]{"becyrillic", "afii10066"});
      addAlternatives(map, new String[]{"vecyrillic", "afii10067"});
      addAlternatives(map, new String[]{"gecyrillic", "afii10068"});
      addAlternatives(map, new String[]{"decyrillic", "afii10069"});
      addAlternatives(map, new String[]{"iecyrillic", "afii10070"});
      addAlternatives(map, new String[]{"iocyrillic", "afii10071"});
      addAlternatives(map, new String[]{"zhecyrillic", "afii10072"});
      addAlternatives(map, new String[]{"zecyrillic", "afii10073"});
      addAlternatives(map, new String[]{"iicyrillic", "afii10074"});
      addAlternatives(map, new String[]{"iishortcyrillic", "afii10075"});
      addAlternatives(map, new String[]{"kacyrillic", "afii10076"});
      addAlternatives(map, new String[]{"elcyrillic", "afii10077"});
      addAlternatives(map, new String[]{"emcyrillic", "afii10078"});
      addAlternatives(map, new String[]{"encyrillic", "afii10079"});
      addAlternatives(map, new String[]{"ocyrillic", "afii10080"});
      addAlternatives(map, new String[]{"pecyrillic", "afii10081"});
      addAlternatives(map, new String[]{"ercyrillic", "afii10082"});
      addAlternatives(map, new String[]{"escyrillic", "afii10083"});
      addAlternatives(map, new String[]{"tecyrillic", "afii10084"});
      addAlternatives(map, new String[]{"ucyrillic", "afii10085"});
      addAlternatives(map, new String[]{"efcyrillic", "afii10086"});
      addAlternatives(map, new String[]{"khacyrillic", "afii10087"});
      addAlternatives(map, new String[]{"tsecyrillic", "afii10088"});
      addAlternatives(map, new String[]{"checyrillic", "afii10089"});
      addAlternatives(map, new String[]{"shacyrillic", "afii10090"});
      addAlternatives(map, new String[]{"shchacyrillic", "afii10091"});
      addAlternatives(map, new String[]{"hardsigncyrillic", "afii10092"});
      addAlternatives(map, new String[]{"yericyrillic", "afii10093"});
      addAlternatives(map, new String[]{"softsigncyrillic", "afii10094"});
      addAlternatives(map, new String[]{"ereversedcyrillic", "afii10095"});
      addAlternatives(map, new String[]{"iucyrillic", "afii10096"});
      addAlternatives(map, new String[]{"iacyrillic", "afii10097"});
      addAlternatives(map, new String[]{"Gheupturncyrillic", "afii10050"});
      addAlternatives(map, new String[]{"Djecyrillic", "afii10051"});
      addAlternatives(map, new String[]{"Gjecyrillic", "afii10052"});
      addAlternatives(map, new String[]{"Ecyrillic", "afii10053"});
      addAlternatives(map, new String[]{"Dzecyrillic", "afii10054"});
      addAlternatives(map, new String[]{"Icyrillic", "afii10055"});
      addAlternatives(map, new String[]{"Yicyrillic", "afii10056"});
      addAlternatives(map, new String[]{"Jecyrillic", "afii10057"});
      addAlternatives(map, new String[]{"Ljecyrillic", "afii10058"});
      addAlternatives(map, new String[]{"Njecyrillic", "afii10059"});
      addAlternatives(map, new String[]{"Tshecyrillic", "afii10060"});
      addAlternatives(map, new String[]{"Kjecyrillic", "afii10061"});
      addAlternatives(map, new String[]{"Ushortcyrillic", "afii10062"});
      addAlternatives(map, new String[]{"Dzhecyrillic", "afii10145"});
      addAlternatives(map, new String[]{"Yatcyrillic", "afii10146"});
      addAlternatives(map, new String[]{"Fitacyrillic", "afii10147"});
      addAlternatives(map, new String[]{"Izhitsacyrillic", "afii10148"});
      addAlternatives(map, new String[]{"gheupturncyrillic", "afii10098"});
      addAlternatives(map, new String[]{"djecyrillic", "afii10099"});
      addAlternatives(map, new String[]{"gjecyrillic", "afii10100"});
      addAlternatives(map, new String[]{"ecyrillic", "afii10101"});
      addAlternatives(map, new String[]{"dzecyrillic", "afii10102"});
      addAlternatives(map, new String[]{"icyrillic", "afii10103"});
      addAlternatives(map, new String[]{"yicyrillic", "afii10104"});
      addAlternatives(map, new String[]{"jecyrillic", "afii10105"});
      addAlternatives(map, new String[]{"ljecyrillic", "afii10106"});
      addAlternatives(map, new String[]{"njecyrillic", "afii10107"});
      addAlternatives(map, new String[]{"tshecyrillic", "afii10108"});
      addAlternatives(map, new String[]{"kjecyrillic", "afii10109"});
      addAlternatives(map, new String[]{"ushortcyrillic", "afii10110"});
      addAlternatives(map, new String[]{"dzhecyrillic", "afii10193"});
      addAlternatives(map, new String[]{"yatcyrillic", "afii10194"});
      addAlternatives(map, new String[]{"fitacyrillic", "afii10195"});
      addAlternatives(map, new String[]{"izhitsacyrillic", "afii10196"});
      CHARNAME_ALTERNATIVES = Collections.unmodifiableMap(map);
   }
}
