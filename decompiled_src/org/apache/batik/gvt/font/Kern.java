package org.apache.batik.gvt.font;

import java.util.Arrays;

public class Kern {
   private int[] firstGlyphCodes;
   private int[] secondGlyphCodes;
   private UnicodeRange[] firstUnicodeRanges;
   private UnicodeRange[] secondUnicodeRanges;
   private float kerningAdjust;

   public Kern(int[] var1, int[] var2, UnicodeRange[] var3, UnicodeRange[] var4, float var5) {
      this.firstGlyphCodes = var1;
      this.secondGlyphCodes = var2;
      this.firstUnicodeRanges = var3;
      this.secondUnicodeRanges = var4;
      this.kerningAdjust = var5;
      if (var1 != null) {
         Arrays.sort(this.firstGlyphCodes);
      }

      if (var2 != null) {
         Arrays.sort(this.secondGlyphCodes);
      }

   }

   public boolean matchesFirstGlyph(int var1, String var2) {
      if (this.firstGlyphCodes != null) {
         int var3 = Arrays.binarySearch(this.firstGlyphCodes, var1);
         if (var3 >= 0) {
            return true;
         }
      }

      if (var2.length() < 1) {
         return false;
      } else {
         char var5 = var2.charAt(0);

         for(int var4 = 0; var4 < this.firstUnicodeRanges.length; ++var4) {
            if (this.firstUnicodeRanges[var4].contains(var5)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean matchesFirstGlyph(int var1, char var2) {
      int var3;
      if (this.firstGlyphCodes != null) {
         var3 = Arrays.binarySearch(this.firstGlyphCodes, var1);
         if (var3 >= 0) {
            return true;
         }
      }

      for(var3 = 0; var3 < this.firstUnicodeRanges.length; ++var3) {
         if (this.firstUnicodeRanges[var3].contains(var2)) {
            return true;
         }
      }

      return false;
   }

   public boolean matchesSecondGlyph(int var1, String var2) {
      if (this.secondGlyphCodes != null) {
         int var3 = Arrays.binarySearch(this.secondGlyphCodes, var1);
         if (var3 >= 0) {
            return true;
         }
      }

      if (var2.length() < 1) {
         return false;
      } else {
         char var5 = var2.charAt(0);

         for(int var4 = 0; var4 < this.secondUnicodeRanges.length; ++var4) {
            if (this.secondUnicodeRanges[var4].contains(var5)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean matchesSecondGlyph(int var1, char var2) {
      int var3;
      if (this.secondGlyphCodes != null) {
         var3 = Arrays.binarySearch(this.secondGlyphCodes, var1);
         if (var3 >= 0) {
            return true;
         }
      }

      for(var3 = 0; var3 < this.secondUnicodeRanges.length; ++var3) {
         if (this.secondUnicodeRanges[var3].contains(var2)) {
            return true;
         }
      }

      return false;
   }

   public float getAdjustValue() {
      return this.kerningAdjust;
   }
}
