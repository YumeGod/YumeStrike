package org.apache.batik.gvt.font;

import java.awt.font.LineMetrics;

public class GVTLineMetrics {
   protected float ascent;
   protected int baselineIndex;
   protected float[] baselineOffsets;
   protected float descent;
   protected float height;
   protected float leading;
   protected int numChars;
   protected float strikethroughOffset;
   protected float strikethroughThickness;
   protected float underlineOffset;
   protected float underlineThickness;
   protected float overlineOffset;
   protected float overlineThickness;

   public GVTLineMetrics(LineMetrics var1) {
      this.ascent = var1.getAscent();
      this.baselineIndex = var1.getBaselineIndex();
      this.baselineOffsets = var1.getBaselineOffsets();
      this.descent = var1.getDescent();
      this.height = var1.getHeight();
      this.leading = var1.getLeading();
      this.numChars = var1.getNumChars();
      this.strikethroughOffset = var1.getStrikethroughOffset();
      this.strikethroughThickness = var1.getStrikethroughThickness();
      this.underlineOffset = var1.getUnderlineOffset();
      this.underlineThickness = var1.getUnderlineThickness();
      this.overlineOffset = -this.ascent;
      this.overlineThickness = this.underlineThickness;
   }

   public GVTLineMetrics(LineMetrics var1, float var2) {
      this.ascent = var1.getAscent() * var2;
      this.baselineIndex = var1.getBaselineIndex();
      this.baselineOffsets = var1.getBaselineOffsets();

      for(int var3 = 0; var3 < this.baselineOffsets.length; ++var3) {
         float[] var10000 = this.baselineOffsets;
         var10000[var3] *= var2;
      }

      this.descent = var1.getDescent() * var2;
      this.height = var1.getHeight() * var2;
      this.leading = var1.getLeading();
      this.numChars = var1.getNumChars();
      this.strikethroughOffset = var1.getStrikethroughOffset() * var2;
      this.strikethroughThickness = var1.getStrikethroughThickness() * var2;
      this.underlineOffset = var1.getUnderlineOffset() * var2;
      this.underlineThickness = var1.getUnderlineThickness() * var2;
      this.overlineOffset = -this.ascent;
      this.overlineThickness = this.underlineThickness;
   }

   public GVTLineMetrics(float var1, int var2, float[] var3, float var4, float var5, float var6, int var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      this.ascent = var1;
      this.baselineIndex = var2;
      this.baselineOffsets = var3;
      this.descent = var4;
      this.height = var5;
      this.leading = var6;
      this.numChars = var7;
      this.strikethroughOffset = var8;
      this.strikethroughThickness = var9;
      this.underlineOffset = var10;
      this.underlineThickness = var11;
      this.overlineOffset = var12;
      this.overlineThickness = var13;
   }

   public float getAscent() {
      return this.ascent;
   }

   public int getBaselineIndex() {
      return this.baselineIndex;
   }

   public float[] getBaselineOffsets() {
      return this.baselineOffsets;
   }

   public float getDescent() {
      return this.descent;
   }

   public float getHeight() {
      return this.height;
   }

   public float getLeading() {
      return this.leading;
   }

   public int getNumChars() {
      return this.numChars;
   }

   public float getStrikethroughOffset() {
      return this.strikethroughOffset;
   }

   public float getStrikethroughThickness() {
      return this.strikethroughThickness;
   }

   public float getUnderlineOffset() {
      return this.underlineOffset;
   }

   public float getUnderlineThickness() {
      return this.underlineThickness;
   }

   public float getOverlineOffset() {
      return this.overlineOffset;
   }

   public float getOverlineThickness() {
      return this.overlineThickness;
   }
}
