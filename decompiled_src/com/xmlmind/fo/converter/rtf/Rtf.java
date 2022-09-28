package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.util.Encoder;

public final class Rtf {
   public static final int CS_ANSI = 0;
   public static final int CS_SYMBOL = 2;
   public static final int CS_RUSSIAN = 204;
   public static final int CS_EASTERN_EUROPEAN = 238;
   public static final int UNIT_MILLIMETER = 1;
   public static final int UNIT_CENTIMETER = 2;
   public static final int UNIT_INCH = 3;
   public static final int UNIT_POINT = 4;

   public static int toTwips(double var0, int var2) {
      double var3;
      switch (var2) {
         case 1:
            var3 = var0 / 25.4 * 72.0;
            break;
         case 2:
            var3 = var0 / 2.54 * 72.0;
            break;
         case 3:
            var3 = var0 * 72.0;
            break;
         case 4:
         default:
            var3 = var0;
      }

      return toTwips(var3);
   }

   public static int toTwips(double var0) {
      return (int)Math.rint(20.0 * var0);
   }

   public static String escape(String var0) {
      return escape(var0, (Encoder)null);
   }

   public static String escape(String var0, Encoder var1) {
      int var2 = var0.length();
      StringBuffer var3 = new StringBuffer(var2);
      int var4;
      char var5;
      if (var1 != null) {
         for(var4 = 0; var4 < var2; ++var4) {
            var5 = var0.charAt(var4);
            switch (var5) {
               case '\n':
                  var3.append("\\line ");
                  break;
               case '\\':
                  var3.append("\\\\");
                  break;
               case '{':
                  var3.append("\\{");
                  break;
               case '}':
                  var3.append("\\}");
                  break;
               case ' ':
                  var3.append("\\~");
                  break;
               case '\u00ad':
                  var3.append("\\-");
                  break;
               case '‑':
                  var3.append("\\_");
                  break;
               default:
                  if (!var1.canEncode(var5)) {
                     var3.append("\\u" + Short.toString((short)var5) + "?");
                  } else {
                     var3.append(var5);
                  }
            }
         }
      } else {
         for(var4 = 0; var4 < var2; ++var4) {
            var5 = var0.charAt(var4);
            switch (var5) {
               case '\n':
                  var3.append("\\line ");
                  break;
               case '\\':
                  var3.append("\\\\");
                  break;
               case '{':
                  var3.append("\\{");
                  break;
               case '}':
                  var3.append("\\}");
                  break;
               case ' ':
                  var3.append("\\~");
                  break;
               case '\u00ad':
                  var3.append("\\-");
                  break;
               case '‑':
                  var3.append("\\_");
                  break;
               default:
                  var3.append(var5);
            }
         }
      }

      return var3.toString();
   }

   public static int toSymbolChar(char var0) {
      switch (var0) {
         case ' ':
            return 32;
         case '!':
            return 33;
         case '#':
            return 35;
         case '%':
            return 37;
         case '&':
            return 38;
         case '(':
            return 40;
         case ')':
            return 41;
         case '+':
            return 43;
         case ',':
            return 44;
         case '.':
            return 46;
         case '/':
            return 47;
         case '0':
            return 48;
         case '1':
            return 49;
         case '2':
            return 50;
         case '3':
            return 51;
         case '4':
            return 52;
         case '5':
            return 53;
         case '6':
            return 54;
         case '7':
            return 55;
         case '8':
            return 56;
         case '9':
            return 57;
         case ':':
            return 58;
         case ';':
            return 59;
         case '<':
            return 60;
         case '=':
            return 61;
         case '>':
            return 62;
         case '?':
            return 63;
         case '[':
            return 91;
         case ']':
            return 93;
         case '_':
            return 95;
         case '{':
            return 123;
         case '|':
            return 124;
         case '}':
            return 125;
         case '©':
            return 211;
         case '¬':
            return 216;
         case '®':
            return 210;
         case '°':
            return 176;
         case '±':
            return 177;
         case '×':
            return 180;
         case '÷':
            return 184;
         case 'ƒ':
            return 166;
         case 'Α':
            return 65;
         case 'Β':
            return 66;
         case 'Γ':
            return 71;
         case 'Δ':
            return 68;
         case 'Ε':
            return 69;
         case 'Ζ':
            return 90;
         case 'Η':
            return 72;
         case 'Θ':
            return 81;
         case 'Ι':
            return 73;
         case 'Κ':
            return 75;
         case 'Λ':
            return 76;
         case 'Μ':
            return 77;
         case 'Ν':
            return 78;
         case 'Ξ':
            return 88;
         case 'Ο':
            return 79;
         case 'Π':
            return 80;
         case 'Ρ':
            return 82;
         case 'Σ':
            return 83;
         case 'Τ':
            return 84;
         case 'Υ':
            return 85;
         case 'Φ':
            return 70;
         case 'Χ':
            return 67;
         case 'Ψ':
            return 89;
         case 'Ω':
            return 87;
         case 'α':
            return 97;
         case 'β':
            return 98;
         case 'γ':
            return 103;
         case 'δ':
            return 100;
         case 'ε':
            return 101;
         case 'ζ':
            return 122;
         case 'η':
            return 104;
         case 'θ':
            return 113;
         case 'ι':
            return 105;
         case 'κ':
            return 107;
         case 'λ':
            return 108;
         case 'μ':
            return 109;
         case 'ν':
            return 110;
         case 'ξ':
            return 120;
         case 'ο':
            return 111;
         case 'π':
            return 112;
         case 'ρ':
            return 114;
         case 'ς':
            return 86;
         case 'σ':
            return 115;
         case 'τ':
            return 116;
         case 'υ':
            return 117;
         case 'φ':
            return 102;
         case 'χ':
            return 99;
         case 'ψ':
            return 121;
         case 'ω':
            return 119;
         case 'ϑ':
            return 74;
         case 'ϒ':
            return 161;
         case 'ϕ':
            return 106;
         case 'ϖ':
            return 118;
         case '‒':
         case '–':
         case '—':
         case '−':
            return 45;
         case '•':
            return 183;
         case '…':
            return 188;
         case '′':
            return 162;
         case '″':
            return 178;
         case '⁄':
            return 164;
         case '€':
            return 160;
         case 'ℑ':
            return 193;
         case '℘':
            return 195;
         case 'ℜ':
            return 194;
         case '™':
            return 212;
         case 'ℵ':
            return 192;
         case '←':
            return 172;
         case '↑':
            return 173;
         case '→':
            return 174;
         case '↓':
            return 175;
         case '↔':
            return 171;
         case '↵':
            return 191;
         case '⇐':
            return 220;
         case '⇑':
            return 221;
         case '⇒':
            return 222;
         case '⇓':
            return 223;
         case '⇔':
            return 219;
         case '∀':
            return 34;
         case '∂':
            return 182;
         case '∃':
            return 36;
         case '∅':
            return 198;
         case '∇':
            return 209;
         case '∈':
            return 206;
         case '∉':
            return 207;
         case '∍':
            return 39;
         case '∏':
            return 213;
         case '∑':
            return 229;
         case '∗':
            return 42;
         case '√':
            return 214;
         case '∝':
            return 181;
         case '∞':
            return 165;
         case '∠':
            return 208;
         case '∧':
            return 217;
         case '∨':
            return 218;
         case '∩':
            return 199;
         case '∪':
            return 200;
         case '∫':
            return 242;
         case '∴':
            return 92;
         case '∼':
            return 126;
         case '≅':
            return 64;
         case '≈':
            return 187;
         case '≠':
            return 185;
         case '≡':
            return 186;
         case '≤':
            return 163;
         case '≥':
            return 179;
         case '⊂':
            return 204;
         case '⊃':
            return 201;
         case '⊄':
            return 203;
         case '⊆':
            return 205;
         case '⊇':
            return 202;
         case '⊕':
            return 197;
         case '⊗':
            return 196;
         case '⊥':
            return 94;
         case '⋄':
            return 224;
         case '⋅':
            return 215;
         case '⌠':
            return 243;
         case '⌡':
            return 245;
         case '⎛':
            return 230;
         case '⎜':
            return 231;
         case '⎝':
            return 232;
         case '⎞':
            return 246;
         case '⎟':
            return 247;
         case '⎠':
            return 248;
         case '⎡':
            return 233;
         case '⎢':
            return 234;
         case '⎣':
            return 235;
         case '⎤':
            return 249;
         case '⎥':
            return 250;
         case '⎦':
            return 251;
         case '⎧':
            return 236;
         case '⎨':
            return 237;
         case '⎩':
            return 238;
         case '⎪':
            return 239;
         case '⎫':
            return 252;
         case '⎬':
            return 253;
         case '⎭':
            return 254;
         case '⎮':
            return 244;
         case '⎯':
            return 190;
         case '⏐':
            return 189;
         case '♠':
            return 170;
         case '♣':
            return 167;
         case '♥':
            return 169;
         case '♦':
            return 168;
         case '〈':
            return 225;
         case '〉':
            return 241;
         case '\uf8e5':
            return 96;
         case '\uf8ff':
            return 240;
         default:
            return -1;
      }
   }

   public static int colorIndex(Color var0, ColorTable var1) {
      int var2 = -1;
      if (var0 != null) {
         if (var0.name != null) {
            var2 = var1.index(var0.name);
         } else {
            var2 = var1.index(var0.red, var0.green, var0.blue);
         }

         if (var2 < 0) {
            var2 = var1.add(var0.red, var0.green, var0.blue);
         }
      }

      return var2;
   }
}
