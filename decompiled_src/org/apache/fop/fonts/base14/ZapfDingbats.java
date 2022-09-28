package org.apache.fop.fonts.base14;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.fop.fonts.Base14Font;
import org.apache.fop.fonts.CodePointMapping;
import org.apache.fop.fonts.FontType;

public class ZapfDingbats extends Base14Font {
   private static final String fontName = "ZapfDingbats";
   private static final String fullName = "ITC Zapf Dingbats";
   private static final Set familyNames;
   private static final String encoding = "ZapfDingbatsEncoding";
   private static final int capHeight = 820;
   private static final int xHeight = 426;
   private static final int ascender = 820;
   private static final int descender = -143;
   private static final int firstChar = 32;
   private static final int lastChar = 255;
   private static final int[] width = new int[256];
   private final CodePointMapping mapping;
   private boolean enableKerning;

   public ZapfDingbats() {
      this(false);
   }

   public ZapfDingbats(boolean enableKerning) {
      this.mapping = CodePointMapping.getMapping("ZapfDingbatsEncoding");
      this.enableKerning = false;
      this.enableKerning = enableKerning;
   }

   public String getEncodingName() {
      return "ZapfDingbatsEncoding";
   }

   public String getFontName() {
      return "ZapfDingbats";
   }

   public String getEmbedFontName() {
      return this.getFontName();
   }

   public String getFullName() {
      return "ITC Zapf Dingbats";
   }

   public Set getFamilyNames() {
      return familyNames;
   }

   public FontType getFontType() {
      return FontType.TYPE1;
   }

   public int getAscender(int size) {
      return size * 820;
   }

   public int getCapHeight(int size) {
      return size * 820;
   }

   public int getDescender(int size) {
      return size * -143;
   }

   public int getXHeight(int size) {
      return size * 426;
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
      width[32] = 278;
      width[33] = 974;
      width[34] = 961;
      width[35] = 974;
      width[36] = 980;
      width[37] = 719;
      width[38] = 789;
      width[39] = 790;
      width[40] = 791;
      width[41] = 690;
      width[42] = 960;
      width[43] = 939;
      width[44] = 549;
      width[45] = 855;
      width[46] = 911;
      width[47] = 933;
      width[48] = 911;
      width[49] = 945;
      width[50] = 974;
      width[51] = 755;
      width[52] = 846;
      width[53] = 762;
      width[54] = 761;
      width[55] = 571;
      width[56] = 677;
      width[57] = 763;
      width[58] = 760;
      width[59] = 759;
      width[60] = 754;
      width[61] = 494;
      width[62] = 552;
      width[63] = 537;
      width[64] = 577;
      width[65] = 692;
      width[66] = 786;
      width[67] = 788;
      width[68] = 788;
      width[69] = 790;
      width[70] = 793;
      width[71] = 794;
      width[72] = 816;
      width[73] = 823;
      width[74] = 789;
      width[75] = 841;
      width[76] = 823;
      width[77] = 833;
      width[78] = 816;
      width[79] = 831;
      width[80] = 923;
      width[81] = 744;
      width[82] = 723;
      width[83] = 749;
      width[84] = 790;
      width[85] = 792;
      width[86] = 695;
      width[87] = 776;
      width[88] = 768;
      width[89] = 792;
      width[90] = 759;
      width[91] = 707;
      width[92] = 708;
      width[93] = 682;
      width[94] = 701;
      width[95] = 826;
      width[96] = 815;
      width[97] = 789;
      width[98] = 789;
      width[99] = 707;
      width[100] = 687;
      width[101] = 696;
      width[102] = 689;
      width[103] = 786;
      width[104] = 787;
      width[105] = 713;
      width[106] = 791;
      width[107] = 785;
      width[108] = 791;
      width[109] = 873;
      width[110] = 761;
      width[111] = 762;
      width[112] = 762;
      width[113] = 759;
      width[114] = 759;
      width[115] = 892;
      width[116] = 892;
      width[117] = 788;
      width[118] = 784;
      width[119] = 438;
      width[120] = 138;
      width[121] = 277;
      width[122] = 415;
      width[123] = 392;
      width[124] = 392;
      width[125] = 668;
      width[126] = 668;
      width[161] = 732;
      width[162] = 544;
      width[163] = 544;
      width[164] = 910;
      width[165] = 667;
      width[166] = 760;
      width[167] = 760;
      width[168] = 776;
      width[169] = 595;
      width[170] = 694;
      width[171] = 626;
      width[172] = 788;
      width[173] = 788;
      width[174] = 788;
      width[175] = 788;
      width[176] = 788;
      width[177] = 788;
      width[178] = 788;
      width[179] = 788;
      width[180] = 788;
      width[181] = 788;
      width[182] = 788;
      width[183] = 788;
      width[184] = 788;
      width[185] = 788;
      width[186] = 788;
      width[187] = 788;
      width[188] = 788;
      width[189] = 788;
      width[190] = 788;
      width[191] = 788;
      width[192] = 788;
      width[193] = 788;
      width[194] = 788;
      width[195] = 788;
      width[196] = 788;
      width[197] = 788;
      width[198] = 788;
      width[199] = 788;
      width[200] = 788;
      width[201] = 788;
      width[202] = 788;
      width[203] = 788;
      width[204] = 788;
      width[205] = 788;
      width[206] = 788;
      width[207] = 788;
      width[208] = 788;
      width[209] = 788;
      width[210] = 788;
      width[211] = 788;
      width[212] = 894;
      width[213] = 838;
      width[214] = 1016;
      width[215] = 458;
      width[216] = 748;
      width[217] = 924;
      width[218] = 748;
      width[219] = 918;
      width[220] = 927;
      width[221] = 928;
      width[222] = 928;
      width[223] = 834;
      width[224] = 873;
      width[225] = 828;
      width[226] = 924;
      width[227] = 924;
      width[228] = 917;
      width[229] = 930;
      width[230] = 931;
      width[231] = 463;
      width[232] = 883;
      width[233] = 836;
      width[234] = 836;
      width[235] = 867;
      width[236] = 867;
      width[237] = 696;
      width[238] = 696;
      width[239] = 874;
      width[241] = 874;
      width[242] = 760;
      width[243] = 946;
      width[244] = 771;
      width[245] = 865;
      width[246] = 771;
      width[247] = 888;
      width[248] = 967;
      width[249] = 888;
      width[250] = 831;
      width[251] = 873;
      width[252] = 927;
      width[253] = 970;
      width[254] = 918;
      width[137] = 410;
      width[135] = 509;
      width[140] = 334;
      width[134] = 509;
      width[128] = 390;
      width[138] = 234;
      width[132] = 276;
      width[129] = 390;
      width[136] = 410;
      width[131] = 317;
      width[130] = 317;
      width[133] = 276;
      width[141] = 334;
      width[139] = 234;
      familyNames = new HashSet();
      familyNames.add("ZapfDingbats");
   }
}
