package org.apache.fop.fonts.base14;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.fop.fonts.Base14Font;
import org.apache.fop.fonts.CodePointMapping;
import org.apache.fop.fonts.FontType;

public class HelveticaBold extends Base14Font {
   private static final String fontName = "Helvetica-Bold";
   private static final String fullName = "Helvetica Bold";
   private static final Set familyNames;
   private static final String encoding = "WinAnsiEncoding";
   private static final int capHeight = 718;
   private static final int xHeight = 532;
   private static final int ascender = 718;
   private static final int descender = -207;
   private static final int firstChar = 32;
   private static final int lastChar = 255;
   private static final int[] width = new int[256];
   private final CodePointMapping mapping;
   private static final Map kerning;
   private boolean enableKerning;

   public HelveticaBold() {
      this(false);
   }

   public HelveticaBold(boolean enableKerning) {
      this.mapping = CodePointMapping.getMapping("WinAnsiEncoding");
      this.enableKerning = false;
      this.enableKerning = enableKerning;
   }

   public String getEncodingName() {
      return "WinAnsiEncoding";
   }

   public String getFontName() {
      return "Helvetica-Bold";
   }

   public String getEmbedFontName() {
      return this.getFontName();
   }

   public String getFullName() {
      return "Helvetica Bold";
   }

   public Set getFamilyNames() {
      return familyNames;
   }

   public FontType getFontType() {
      return FontType.TYPE1;
   }

   public int getAscender(int size) {
      return size * 718;
   }

   public int getCapHeight(int size) {
      return size * 718;
   }

   public int getDescender(int size) {
      return size * -207;
   }

   public int getXHeight(int size) {
      return size * 532;
   }

   public int getFirstChar() {
      return 32;
   }

   public int getLastChar() {
      return 255;
   }

   public int getWidth(int i, int size) {
      return size * width[i];
   }

   public int[] getWidths() {
      int[] arr = new int[this.getLastChar() - this.getFirstChar() + 1];
      System.arraycopy(width, this.getFirstChar(), arr, 0, this.getLastChar() - this.getFirstChar() + 1);
      return arr;
   }

   public boolean hasKerningInfo() {
      return this.enableKerning;
   }

   public Map getKerningInfo() {
      return kerning;
   }

   public char mapChar(char c) {
      this.notifyMapOperation();
      char d = this.mapping.mapChar(c);
      if (d != 0) {
         return d;
      } else {
         this.warnMissingGlyph(c);
         return '#';
      }
   }

   public boolean hasChar(char c) {
      return this.mapping.mapChar(c) > 0;
   }

   static {
      width[65] = 722;
      width[198] = 1000;
      width[193] = 722;
      width[194] = 722;
      width[196] = 722;
      width[192] = 722;
      width[197] = 722;
      width[195] = 722;
      width[66] = 722;
      width[67] = 722;
      width[199] = 722;
      width[68] = 722;
      width[69] = 667;
      width[201] = 667;
      width[202] = 667;
      width[203] = 667;
      width[200] = 667;
      width[208] = 722;
      width[128] = 556;
      width[70] = 611;
      width[71] = 778;
      width[72] = 722;
      width[73] = 278;
      width[205] = 278;
      width[206] = 278;
      width[207] = 278;
      width[204] = 278;
      width[74] = 556;
      width[75] = 722;
      width[76] = 611;
      width[77] = 833;
      width[78] = 722;
      width[209] = 722;
      width[79] = 778;
      width[140] = 1000;
      width[211] = 778;
      width[212] = 778;
      width[214] = 778;
      width[210] = 778;
      width[216] = 778;
      width[213] = 778;
      width[80] = 667;
      width[81] = 778;
      width[82] = 722;
      width[83] = 667;
      width[138] = 667;
      width[84] = 611;
      width[222] = 667;
      width[85] = 722;
      width[218] = 722;
      width[219] = 722;
      width[220] = 722;
      width[217] = 722;
      width[86] = 667;
      width[87] = 944;
      width[88] = 667;
      width[89] = 667;
      width[221] = 667;
      width[159] = 667;
      width[90] = 611;
      width[142] = 611;
      width[97] = 556;
      width[225] = 556;
      width[226] = 556;
      width[180] = 333;
      width[228] = 556;
      width[230] = 889;
      width[224] = 556;
      width[38] = 722;
      width[229] = 556;
      width[94] = 584;
      width[126] = 584;
      width[42] = 389;
      width[64] = 975;
      width[227] = 556;
      width[98] = 611;
      width[92] = 278;
      width[124] = 280;
      width[123] = 389;
      width[125] = 389;
      width[91] = 333;
      width[93] = 333;
      width[166] = 280;
      width[149] = 350;
      width[99] = 556;
      width[231] = 556;
      width[184] = 333;
      width[162] = 556;
      width[136] = 333;
      width[58] = 333;
      width[44] = 278;
      width[169] = 737;
      width[164] = 556;
      width[100] = 611;
      width[134] = 556;
      width[135] = 556;
      width[176] = 400;
      width[168] = 333;
      width[247] = 584;
      width[36] = 556;
      width[101] = 556;
      width[233] = 556;
      width[234] = 556;
      width[235] = 556;
      width[232] = 556;
      width[56] = 556;
      width[133] = 1000;
      width[151] = 1000;
      width[150] = 556;
      width[61] = 584;
      width[240] = 611;
      width[33] = 333;
      width[161] = 333;
      width[102] = 333;
      width[53] = 556;
      width[131] = 556;
      width[52] = 556;
      width[103] = 611;
      width[223] = 611;
      width[96] = 333;
      width[62] = 584;
      width[171] = 556;
      width[187] = 556;
      width[139] = 333;
      width[155] = 333;
      width[104] = 611;
      width[45] = 333;
      width[105] = 278;
      width[237] = 278;
      width[238] = 278;
      width[239] = 278;
      width[236] = 278;
      width[106] = 278;
      width[107] = 556;
      width[108] = 278;
      width[60] = 584;
      width[172] = 584;
      width[109] = 889;
      width[175] = 333;
      width[181] = 611;
      width[215] = 584;
      width[110] = 611;
      width[57] = 556;
      width[241] = 611;
      width[35] = 556;
      width[111] = 611;
      width[243] = 611;
      width[244] = 611;
      width[246] = 611;
      width[156] = 944;
      width[242] = 611;
      width[49] = 556;
      width[189] = 834;
      width[188] = 834;
      width[185] = 333;
      width[170] = 370;
      width[186] = 365;
      width[248] = 611;
      width[245] = 611;
      width[112] = 611;
      width[182] = 556;
      width[40] = 333;
      width[41] = 333;
      width[37] = 889;
      width[46] = 278;
      width[183] = 278;
      width[137] = 1000;
      width[43] = 584;
      width[177] = 584;
      width[113] = 611;
      width[63] = 611;
      width[191] = 611;
      width[34] = 474;
      width[132] = 500;
      width[147] = 500;
      width[148] = 500;
      width[145] = 278;
      width[146] = 278;
      width[130] = 278;
      width[39] = 238;
      width[114] = 389;
      width[174] = 737;
      width[115] = 556;
      width[154] = 556;
      width[167] = 556;
      width[59] = 333;
      width[55] = 556;
      width[54] = 556;
      width[47] = 278;
      width[32] = 278;
      width[163] = 556;
      width[116] = 333;
      width[254] = 611;
      width[51] = 556;
      width[190] = 834;
      width[179] = 333;
      width[152] = 333;
      width[153] = 1000;
      width[50] = 556;
      width[178] = 333;
      width[117] = 611;
      width[250] = 611;
      width[251] = 611;
      width[252] = 611;
      width[249] = 611;
      width[95] = 556;
      width[118] = 556;
      width[119] = 778;
      width[120] = 556;
      width[121] = 556;
      width[253] = 556;
      width[255] = 556;
      width[165] = 556;
      width[122] = 500;
      width[158] = 500;
      width[48] = 556;
      kerning = new HashMap();
      Integer first = new Integer(107);
      Map pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      Integer second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-15));
      first = new Integer(79);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(65);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(87);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(89);
      ((Map)pairs).put(second, new Integer(-70));
      second = new Integer(84);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(86);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(88);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-40));
      first = new Integer(104);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-20));
      first = new Integer(99);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(107);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(104);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(108);
      ((Map)pairs).put(second, new Integer(-20));
      first = new Integer(87);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-60));
      second = new Integer(45);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(79);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(58);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(97);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(65);
      ((Map)pairs).put(second, new Integer(-60));
      second = new Integer(117);
      ((Map)pairs).put(second, new Integer(-45));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(101);
      ((Map)pairs).put(second, new Integer(-35));
      second = new Integer(59);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-80));
      first = new Integer(112);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-15));
      first = new Integer(80);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(97);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(65);
      ((Map)pairs).put(second, new Integer(-100));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-120));
      second = new Integer(101);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-120));
      first = new Integer(86);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-90));
      second = new Integer(45);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(79);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(58);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(97);
      ((Map)pairs).put(second, new Integer(-60));
      second = new Integer(65);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(117);
      ((Map)pairs).put(second, new Integer(-60));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-120));
      second = new Integer(71);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(101);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(59);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-120));
      first = new Integer(59);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(32);
      ((Map)pairs).put(second, new Integer(-40));
      first = new Integer(118);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(97);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-80));
      first = new Integer(32);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(87);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(147);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(89);
      ((Map)pairs).put(second, new Integer(-120));
      second = new Integer(84);
      ((Map)pairs).put(second, new Integer(-100));
      second = new Integer(145);
      ((Map)pairs).put(second, new Integer(-60));
      second = new Integer(86);
      ((Map)pairs).put(second, new Integer(-80));
      first = new Integer(97);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(119);
      ((Map)pairs).put(second, new Integer(-15));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(103);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(118);
      ((Map)pairs).put(second, new Integer(-15));
      first = new Integer(65);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(79);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(119);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(87);
      ((Map)pairs).put(second, new Integer(-60));
      second = new Integer(67);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(81);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(71);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(86);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(118);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(85);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(117);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(89);
      ((Map)pairs).put(second, new Integer(-110));
      second = new Integer(84);
      ((Map)pairs).put(second, new Integer(-90));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-30));
      first = new Integer(70);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(97);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(65);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-100));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-100));
      first = new Integer(85);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(65);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-30));
      first = new Integer(115);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(119);
      ((Map)pairs).put(second, new Integer(-15));
      first = new Integer(111);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(119);
      ((Map)pairs).put(second, new Integer(-15));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(120);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(118);
      ((Map)pairs).put(second, new Integer(-20));
      first = new Integer(122);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(101);
      ((Map)pairs).put(second, new Integer(10));
      first = new Integer(100);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(100);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(119);
      ((Map)pairs).put(second, new Integer(-15));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-15));
      second = new Integer(118);
      ((Map)pairs).put(second, new Integer(-15));
      first = new Integer(68);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(65);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(87);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(89);
      ((Map)pairs).put(second, new Integer(-70));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(86);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-30));
      first = new Integer(146);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(100);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(32);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(146);
      ((Map)pairs).put(second, new Integer(-46));
      second = new Integer(114);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(108);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(115);
      ((Map)pairs).put(second, new Integer(-60));
      second = new Integer(118);
      ((Map)pairs).put(second, new Integer(-20));
      first = new Integer(82);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(79);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(87);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(85);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(89);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(84);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(86);
      ((Map)pairs).put(second, new Integer(-50));
      first = new Integer(75);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-35));
      second = new Integer(79);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(117);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(101);
      ((Map)pairs).put(second, new Integer(-15));
      first = new Integer(58);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(32);
      ((Map)pairs).put(second, new Integer(-40));
      first = new Integer(119);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-40));
      first = new Integer(114);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(100);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(45);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(99);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(116);
      ((Map)pairs).put(second, new Integer(20));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(10));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-60));
      second = new Integer(103);
      ((Map)pairs).put(second, new Integer(-15));
      second = new Integer(113);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(115);
      ((Map)pairs).put(second, new Integer(-15));
      second = new Integer(118);
      ((Map)pairs).put(second, new Integer(10));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-60));
      first = new Integer(145);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(145);
      ((Map)pairs).put(second, new Integer(-46));
      first = new Integer(108);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(119);
      ((Map)pairs).put(second, new Integer(-15));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-15));
      first = new Integer(103);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(103);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(101);
      ((Map)pairs).put(second, new Integer(10));
      first = new Integer(66);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(65);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(85);
      ((Map)pairs).put(second, new Integer(-10));
      first = new Integer(98);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(117);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(108);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(118);
      ((Map)pairs).put(second, new Integer(-20));
      first = new Integer(76);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(148);
      ((Map)pairs).put(second, new Integer(-140));
      second = new Integer(146);
      ((Map)pairs).put(second, new Integer(-140));
      second = new Integer(87);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(89);
      ((Map)pairs).put(second, new Integer(-120));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(84);
      ((Map)pairs).put(second, new Integer(-90));
      second = new Integer(86);
      ((Map)pairs).put(second, new Integer(-110));
      first = new Integer(81);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(85);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(20));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(20));
      first = new Integer(44);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(148);
      ((Map)pairs).put(second, new Integer(-120));
      second = new Integer(32);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(146);
      ((Map)pairs).put(second, new Integer(-120));
      first = new Integer(148);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(32);
      ((Map)pairs).put(second, new Integer(-80));
      first = new Integer(109);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(117);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-30));
      first = new Integer(102);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(148);
      ((Map)pairs).put(second, new Integer(30));
      second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(146);
      ((Map)pairs).put(second, new Integer(30));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(101);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-10));
      first = new Integer(74);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(65);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(117);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-20));
      first = new Integer(89);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-100));
      second = new Integer(79);
      ((Map)pairs).put(second, new Integer(-70));
      second = new Integer(58);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(97);
      ((Map)pairs).put(second, new Integer(-90));
      second = new Integer(65);
      ((Map)pairs).put(second, new Integer(-110));
      second = new Integer(117);
      ((Map)pairs).put(second, new Integer(-100));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-100));
      second = new Integer(101);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(59);
      ((Map)pairs).put(second, new Integer(-50));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-100));
      first = new Integer(84);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(79);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(58);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(119);
      ((Map)pairs).put(second, new Integer(-60));
      second = new Integer(114);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(59);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(45);
      ((Map)pairs).put(second, new Integer(-120));
      second = new Integer(65);
      ((Map)pairs).put(second, new Integer(-90));
      second = new Integer(97);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(117);
      ((Map)pairs).put(second, new Integer(-90));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-60));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(101);
      ((Map)pairs).put(second, new Integer(-60));
      first = new Integer(121);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(111);
      ((Map)pairs).put(second, new Integer(-25));
      second = new Integer(97);
      ((Map)pairs).put(second, new Integer(-30));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(-80));
      second = new Integer(101);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(-80));
      first = new Integer(46);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(148);
      ((Map)pairs).put(second, new Integer(-120));
      second = new Integer(32);
      ((Map)pairs).put(second, new Integer(-40));
      second = new Integer(146);
      ((Map)pairs).put(second, new Integer(-120));
      first = new Integer(110);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(117);
      ((Map)pairs).put(second, new Integer(-10));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-20));
      second = new Integer(118);
      ((Map)pairs).put(second, new Integer(-40));
      first = new Integer(120);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(101);
      ((Map)pairs).put(second, new Integer(-10));
      first = new Integer(101);
      pairs = (Map)kerning.get(first);
      if (pairs == null) {
         pairs = new HashMap();
         kerning.put(first, pairs);
      }

      second = new Integer(119);
      ((Map)pairs).put(second, new Integer(-15));
      second = new Integer(121);
      ((Map)pairs).put(second, new Integer(-15));
      second = new Integer(46);
      ((Map)pairs).put(second, new Integer(20));
      second = new Integer(120);
      ((Map)pairs).put(second, new Integer(-15));
      second = new Integer(118);
      ((Map)pairs).put(second, new Integer(-15));
      second = new Integer(44);
      ((Map)pairs).put(second, new Integer(10));
      familyNames = new HashSet();
      familyNames.add("Helvetica");
   }
}
