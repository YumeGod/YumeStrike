package org.apache.batik.gvt.flow;

import org.apache.batik.gvt.font.GVTGlyphVector;

class GlyphGroupInfo {
   int start;
   int end;
   int glyphCount;
   int lastGlyphCount;
   boolean hideLast;
   float advance;
   float lastAdvance;
   int range;
   GVTGlyphVector gv;
   boolean[] hide;

   public GlyphGroupInfo(GVTGlyphVector var1, int var2, int var3, boolean[] var4, boolean var5, float[] var6, float[] var7, float[] var8, boolean[] var9) {
      this.gv = var1;
      this.start = var2;
      this.end = var3;
      this.hide = new boolean[this.end - this.start + 1];
      this.hideLast = var5;
      System.arraycopy(var4, this.start, this.hide, 0, this.hide.length);
      float var10 = var6[2 * var3 + 2] - var6[2 * var2];
      float var11 = var10;
      var10 += var7[var3];
      int var12 = var3 - var2 + 1;

      int var13;
      for(var13 = var2; var13 < var3; ++var13) {
         if (var4[var13]) {
            --var12;
         }
      }

      var13 = var12;

      for(int var14 = var3; var14 >= var2; --var14) {
         var11 += var8[var14];
         if (!var9[var14]) {
            break;
         }

         --var13;
      }

      if (this.hideLast) {
         --var13;
      }

      this.glyphCount = var12;
      this.lastGlyphCount = var13;
      this.advance = var10;
      this.lastAdvance = var11;
   }

   public GVTGlyphVector getGlyphVector() {
      return this.gv;
   }

   public int getStart() {
      return this.start;
   }

   public int getEnd() {
      return this.end;
   }

   public int getGlyphCount() {
      return this.glyphCount;
   }

   public int getLastGlyphCount() {
      return this.lastGlyphCount;
   }

   public boolean[] getHide() {
      return this.hide;
   }

   public boolean getHideLast() {
      return this.hideLast;
   }

   public float getAdvance() {
      return this.advance;
   }

   public float getLastAdvance() {
      return this.lastAdvance;
   }

   public void setRange(int var1) {
      this.range = var1;
   }

   public int getRange() {
      return this.range;
   }
}
