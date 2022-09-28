package org.apache.fop.fonts.base14;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.fop.fonts.Base14Font;
import org.apache.fop.fonts.CodePointMapping;
import org.apache.fop.fonts.FontType;

public class Symbol extends Base14Font {
   private static final String fontName = "Symbol";
   private static final String fullName = "Symbol";
   private static final Set familyNames;
   private static final String encoding = "SymbolEncoding";
   private static final int capHeight = 1010;
   private static final int xHeight = 520;
   private static final int ascender = 1010;
   private static final int descender = -293;
   private static final int firstChar = 32;
   private static final int lastChar = 255;
   private static final int[] width = new int[256];
   private final CodePointMapping mapping;
   private boolean enableKerning;

   public Symbol() {
      this(false);
   }

   public Symbol(boolean enableKerning) {
      this.mapping = CodePointMapping.getMapping("SymbolEncoding");
      this.enableKerning = false;
      this.enableKerning = enableKerning;
   }

   public String getEncodingName() {
      return "SymbolEncoding";
   }

   public String getFontName() {
      return "Symbol";
   }

   public String getEmbedFontName() {
      return this.getFontName();
   }

   public String getFullName() {
      return "Symbol";
   }

   public Set getFamilyNames() {
      return familyNames;
   }

   public FontType getFontType() {
      return FontType.TYPE1;
   }

   public int getAscender(int size) {
      return size * 1010;
   }

   public int getCapHeight(int size) {
      return size * 1010;
   }

   public int getDescender(int size) {
      return size * -293;
   }

   public int getXHeight(int size) {
      return size * 520;
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
      return false;
   }

   public Map getKerningInfo() {
      return Collections.EMPTY_MAP;
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
      width[32] = 250;
      width[33] = 333;
      width[34] = 713;
      width[35] = 500;
      width[36] = 549;
      width[37] = 833;
      width[38] = 778;
      width[39] = 439;
      width[40] = 333;
      width[41] = 333;
      width[42] = 500;
      width[43] = 549;
      width[44] = 250;
      width[45] = 549;
      width[46] = 250;
      width[47] = 278;
      width[48] = 500;
      width[49] = 500;
      width[50] = 500;
      width[51] = 500;
      width[52] = 500;
      width[53] = 500;
      width[54] = 500;
      width[55] = 500;
      width[56] = 500;
      width[57] = 500;
      width[58] = 278;
      width[59] = 278;
      width[60] = 549;
      width[61] = 549;
      width[62] = 549;
      width[63] = 444;
      width[64] = 549;
      width[65] = 722;
      width[66] = 667;
      width[67] = 722;
      width[68] = 612;
      width[69] = 611;
      width[70] = 763;
      width[71] = 603;
      width[72] = 722;
      width[73] = 333;
      width[74] = 631;
      width[75] = 722;
      width[76] = 686;
      width[77] = 889;
      width[78] = 722;
      width[79] = 722;
      width[80] = 768;
      width[81] = 741;
      width[82] = 556;
      width[83] = 592;
      width[84] = 611;
      width[85] = 690;
      width[86] = 439;
      width[87] = 768;
      width[88] = 645;
      width[89] = 795;
      width[90] = 611;
      width[91] = 333;
      width[92] = 863;
      width[93] = 333;
      width[94] = 658;
      width[95] = 500;
      width[96] = 500;
      width[97] = 631;
      width[98] = 549;
      width[99] = 549;
      width[100] = 494;
      width[101] = 439;
      width[102] = 521;
      width[103] = 411;
      width[104] = 603;
      width[105] = 329;
      width[106] = 603;
      width[107] = 549;
      width[108] = 549;
      width[109] = 576;
      width[110] = 521;
      width[111] = 549;
      width[112] = 549;
      width[113] = 521;
      width[114] = 549;
      width[115] = 603;
      width[116] = 439;
      width[117] = 576;
      width[118] = 713;
      width[119] = 686;
      width[120] = 493;
      width[121] = 686;
      width[122] = 494;
      width[123] = 480;
      width[124] = 200;
      width[125] = 480;
      width[126] = 549;
      width[160] = 750;
      width[161] = 620;
      width[162] = 247;
      width[163] = 549;
      width[164] = 167;
      width[165] = 713;
      width[166] = 500;
      width[167] = 753;
      width[168] = 753;
      width[169] = 753;
      width[170] = 753;
      width[171] = 1042;
      width[172] = 987;
      width[173] = 603;
      width[174] = 987;
      width[175] = 603;
      width[176] = 400;
      width[177] = 549;
      width[178] = 411;
      width[179] = 549;
      width[180] = 549;
      width[181] = 713;
      width[182] = 494;
      width[183] = 460;
      width[184] = 549;
      width[185] = 549;
      width[186] = 549;
      width[187] = 549;
      width[188] = 1000;
      width[189] = 603;
      width[190] = 1000;
      width[191] = 658;
      width[192] = 823;
      width[193] = 686;
      width[194] = 795;
      width[195] = 987;
      width[196] = 768;
      width[197] = 768;
      width[198] = 823;
      width[199] = 768;
      width[200] = 768;
      width[201] = 713;
      width[202] = 713;
      width[203] = 713;
      width[204] = 713;
      width[205] = 713;
      width[206] = 713;
      width[207] = 713;
      width[208] = 768;
      width[209] = 713;
      width[210] = 790;
      width[211] = 790;
      width[212] = 890;
      width[213] = 823;
      width[214] = 549;
      width[215] = 250;
      width[216] = 713;
      width[217] = 603;
      width[218] = 603;
      width[219] = 1042;
      width[220] = 987;
      width[221] = 603;
      width[222] = 987;
      width[223] = 603;
      width[224] = 494;
      width[225] = 329;
      width[226] = 790;
      width[227] = 790;
      width[228] = 786;
      width[229] = 713;
      width[230] = 384;
      width[231] = 384;
      width[232] = 384;
      width[233] = 384;
      width[234] = 384;
      width[235] = 384;
      width[236] = 494;
      width[237] = 494;
      width[238] = 494;
      width[239] = 494;
      width[241] = 329;
      width[242] = 274;
      width[243] = 686;
      width[244] = 686;
      width[245] = 686;
      width[246] = 384;
      width[247] = 384;
      width[248] = 384;
      width[249] = 384;
      width[250] = 384;
      width[251] = 384;
      width[252] = 494;
      width[253] = 494;
      width[254] = 494;
      familyNames = new HashSet();
      familyNames.add("Symbol");
   }
}
