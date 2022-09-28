package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class PostTable implements Table {
   private static final String[] macGlyphName = new String[]{".notdef", "null", "CR", "space", "exclam", "quotedbl", "numbersign", "dollar", "percent", "ampersand", "quotesingle", "parenleft", "parenright", "asterisk", "plus", "comma", "hyphen", "period", "slash", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "colon", "semicolon", "less", "equal", "greater", "question", "at", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash", "bracketright", "asciicircum", "underscore", "grave", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "braceleft", "bar", "braceright", "asciitilde", "Adieresis", "Aring", "Ccedilla", "Eacute", "Ntilde", "Odieresis", "Udieresis", "aacute", "agrave", "acircumflex", "adieresis", "atilde", "aring", "ccedilla", "eacute", "egrave", "ecircumflex", "edieresis", "iacute", "igrave", "icircumflex", "idieresis", "ntilde", "oacute", "ograve", "ocircumflex", "odieresis", "otilde", "uacute", "ugrave", "ucircumflex", "udieresis", "dagger", "degree", "cent", "sterling", "section", "bullet", "paragraph", "germandbls", "registered", "copyright", "trademark", "acute", "dieresis", "notequal", "AE", "Oslash", "infinity", "plusminus", "lessequal", "greaterequal", "yen", "mu", "partialdiff", "summation", "product", "pi", "integral'", "ordfeminine", "ordmasculine", "Omega", "ae", "oslash", "questiondown", "exclamdown", "logicalnot", "radical", "florin", "approxequal", "increment", "guillemotleft", "guillemotright", "ellipsis", "nbspace", "Agrave", "Atilde", "Otilde", "OE", "oe", "endash", "emdash", "quotedblleft", "quotedblright", "quoteleft", "quoteright", "divide", "lozenge", "ydieresis", "Ydieresis", "fraction", "currency", "guilsinglleft", "guilsinglright", "fi", "fl", "daggerdbl", "middot", "quotesinglbase", "quotedblbase", "perthousand", "Acircumflex", "Ecircumflex", "Aacute", "Edieresis", "Egrave", "Iacute", "Icircumflex", "Idieresis", "Igrave", "Oacute", "Ocircumflex", "", "Ograve", "Uacute", "Ucircumflex", "Ugrave", "dotlessi", "circumflex", "tilde", "overscore", "breve", "dotaccent", "ring", "cedilla", "hungarumlaut", "ogonek", "caron", "Lslash", "lslash", "Scaron", "scaron", "Zcaron", "zcaron", "brokenbar", "Eth", "eth", "Yacute", "yacute", "Thorn", "thorn", "minus", "multiply", "onesuperior", "twosuperior", "threesuperior", "onehalf", "onequarter", "threequarters", "franc", "Gbreve", "gbreve", "Idot", "Scedilla", "scedilla", "Cacute", "cacute", "Ccaron", "ccaron", ""};
   private int version;
   private int italicAngle;
   private short underlinePosition;
   private short underlineThickness;
   private int isFixedPitch;
   private int minMemType42;
   private int maxMemType42;
   private int minMemType1;
   private int maxMemType1;
   private int numGlyphs;
   private int[] glyphNameIndex;
   private String[] psGlyphName;

   protected PostTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      this.version = var2.readInt();
      this.italicAngle = var2.readInt();
      this.underlinePosition = var2.readShort();
      this.underlineThickness = var2.readShort();
      this.isFixedPitch = var2.readInt();
      this.minMemType42 = var2.readInt();
      this.maxMemType42 = var2.readInt();
      this.minMemType1 = var2.readInt();
      this.maxMemType1 = var2.readInt();
      if (this.version == 131072) {
         this.numGlyphs = var2.readUnsignedShort();
         this.glyphNameIndex = new int[this.numGlyphs];

         int var3;
         for(var3 = 0; var3 < this.numGlyphs; ++var3) {
            this.glyphNameIndex[var3] = var2.readUnsignedShort();
         }

         var3 = this.highestGlyphNameIndex();
         if (var3 > 257) {
            var3 -= 257;
            this.psGlyphName = new String[var3];

            for(int var4 = 0; var4 < var3; ++var4) {
               int var5 = var2.readUnsignedByte();
               byte[] var6 = new byte[var5];
               var2.readFully(var6);
               this.psGlyphName[var4] = new String(var6);
            }
         }
      } else if (this.version == 131077) {
      }

   }

   private int highestGlyphNameIndex() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.numGlyphs; ++var2) {
         if (var1 < this.glyphNameIndex[var2]) {
            var1 = this.glyphNameIndex[var2];
         }
      }

      return var1;
   }

   public String getGlyphName(int var1) {
      if (this.version == 131072) {
         return this.glyphNameIndex[var1] > 257 ? this.psGlyphName[this.glyphNameIndex[var1] - 258] : macGlyphName[this.glyphNameIndex[var1]];
      } else {
         return null;
      }
   }

   public int getType() {
      return 1886352244;
   }
}
