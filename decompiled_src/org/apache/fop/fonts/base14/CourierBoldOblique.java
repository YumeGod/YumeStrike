package org.apache.fop.fonts.base14;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.fop.fonts.Base14Font;
import org.apache.fop.fonts.CodePointMapping;
import org.apache.fop.fonts.FontType;

public class CourierBoldOblique extends Base14Font {
   private static final String fontName = "Courier-BoldOblique";
   private static final String fullName = "Courier Bold Oblique";
   private static final Set familyNames;
   private static final String encoding = "WinAnsiEncoding";
   private static final int capHeight = 562;
   private static final int xHeight = 439;
   private static final int ascender = 626;
   private static final int descender = -142;
   private static final int firstChar = 32;
   private static final int lastChar = 255;
   private static final int[] width = new int[256];
   private final CodePointMapping mapping;
   private boolean enableKerning;

   public CourierBoldOblique() {
      this(false);
   }

   public CourierBoldOblique(boolean enableKerning) {
      this.mapping = CodePointMapping.getMapping("WinAnsiEncoding");
      this.enableKerning = false;
      this.enableKerning = enableKerning;
   }

   public String getEncodingName() {
      return "WinAnsiEncoding";
   }

   public String getFontName() {
      return "Courier-BoldOblique";
   }

   public String getEmbedFontName() {
      return this.getFontName();
   }

   public String getFullName() {
      return "Courier Bold Oblique";
   }

   public Set getFamilyNames() {
      return familyNames;
   }

   public FontType getFontType() {
      return FontType.TYPE1;
   }

   public int getAscender(int size) {
      return size * 626;
   }

   public int getCapHeight(int size) {
      return size * 562;
   }

   public int getDescender(int size) {
      return size * -142;
   }

   public int getXHeight(int size) {
      return size * 439;
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
      width[65] = 600;
      width[198] = 600;
      width[193] = 600;
      width[194] = 600;
      width[196] = 600;
      width[192] = 600;
      width[197] = 600;
      width[195] = 600;
      width[66] = 600;
      width[67] = 600;
      width[199] = 600;
      width[68] = 600;
      width[69] = 600;
      width[201] = 600;
      width[202] = 600;
      width[203] = 600;
      width[200] = 600;
      width[208] = 600;
      width[128] = 600;
      width[70] = 600;
      width[71] = 600;
      width[72] = 600;
      width[73] = 600;
      width[205] = 600;
      width[206] = 600;
      width[207] = 600;
      width[204] = 600;
      width[74] = 600;
      width[75] = 600;
      width[76] = 600;
      width[77] = 600;
      width[78] = 600;
      width[209] = 600;
      width[79] = 600;
      width[140] = 600;
      width[211] = 600;
      width[212] = 600;
      width[214] = 600;
      width[210] = 600;
      width[216] = 600;
      width[213] = 600;
      width[80] = 600;
      width[81] = 600;
      width[82] = 600;
      width[83] = 600;
      width[138] = 600;
      width[84] = 600;
      width[222] = 600;
      width[85] = 600;
      width[218] = 600;
      width[219] = 600;
      width[220] = 600;
      width[217] = 600;
      width[86] = 600;
      width[87] = 600;
      width[88] = 600;
      width[89] = 600;
      width[221] = 600;
      width[159] = 600;
      width[90] = 600;
      width[142] = 600;
      width[97] = 600;
      width[225] = 600;
      width[226] = 600;
      width[180] = 600;
      width[228] = 600;
      width[230] = 600;
      width[224] = 600;
      width[38] = 600;
      width[229] = 600;
      width[94] = 600;
      width[126] = 600;
      width[42] = 600;
      width[64] = 600;
      width[227] = 600;
      width[98] = 600;
      width[92] = 600;
      width[124] = 600;
      width[123] = 600;
      width[125] = 600;
      width[91] = 600;
      width[93] = 600;
      width[166] = 600;
      width[149] = 600;
      width[99] = 600;
      width[231] = 600;
      width[184] = 600;
      width[162] = 600;
      width[136] = 600;
      width[58] = 600;
      width[44] = 600;
      width[169] = 600;
      width[164] = 600;
      width[100] = 600;
      width[134] = 600;
      width[135] = 600;
      width[176] = 600;
      width[168] = 600;
      width[247] = 600;
      width[36] = 600;
      width[101] = 600;
      width[233] = 600;
      width[234] = 600;
      width[235] = 600;
      width[232] = 600;
      width[56] = 600;
      width[133] = 600;
      width[151] = 600;
      width[150] = 600;
      width[61] = 600;
      width[240] = 600;
      width[33] = 600;
      width[161] = 600;
      width[102] = 600;
      width[53] = 600;
      width[131] = 600;
      width[52] = 600;
      width[103] = 600;
      width[223] = 600;
      width[96] = 600;
      width[62] = 600;
      width[171] = 600;
      width[187] = 600;
      width[139] = 600;
      width[155] = 600;
      width[104] = 600;
      width[45] = 600;
      width[105] = 600;
      width[237] = 600;
      width[238] = 600;
      width[239] = 600;
      width[236] = 600;
      width[106] = 600;
      width[107] = 600;
      width[108] = 600;
      width[60] = 600;
      width[172] = 600;
      width[109] = 600;
      width[175] = 600;
      width[181] = 600;
      width[215] = 600;
      width[110] = 600;
      width[57] = 600;
      width[241] = 600;
      width[35] = 600;
      width[111] = 600;
      width[243] = 600;
      width[244] = 600;
      width[246] = 600;
      width[156] = 600;
      width[242] = 600;
      width[49] = 600;
      width[189] = 600;
      width[188] = 600;
      width[185] = 600;
      width[170] = 600;
      width[186] = 600;
      width[248] = 600;
      width[245] = 600;
      width[112] = 600;
      width[182] = 600;
      width[40] = 600;
      width[41] = 600;
      width[37] = 600;
      width[46] = 600;
      width[183] = 600;
      width[137] = 600;
      width[43] = 600;
      width[177] = 600;
      width[113] = 600;
      width[63] = 600;
      width[191] = 600;
      width[34] = 600;
      width[132] = 600;
      width[147] = 600;
      width[148] = 600;
      width[145] = 600;
      width[146] = 600;
      width[130] = 600;
      width[39] = 600;
      width[114] = 600;
      width[174] = 600;
      width[115] = 600;
      width[154] = 600;
      width[167] = 600;
      width[59] = 600;
      width[55] = 600;
      width[54] = 600;
      width[47] = 600;
      width[32] = 600;
      width[163] = 600;
      width[116] = 600;
      width[254] = 600;
      width[51] = 600;
      width[190] = 600;
      width[179] = 600;
      width[152] = 600;
      width[153] = 600;
      width[50] = 600;
      width[178] = 600;
      width[117] = 600;
      width[250] = 600;
      width[251] = 600;
      width[252] = 600;
      width[249] = 600;
      width[95] = 600;
      width[118] = 600;
      width[119] = 600;
      width[120] = 600;
      width[121] = 600;
      width[253] = 600;
      width[255] = 600;
      width[165] = 600;
      width[122] = 600;
      width[158] = 600;
      width[48] = 600;
      familyNames = new HashSet();
      familyNames.add("Courier");
   }
}
