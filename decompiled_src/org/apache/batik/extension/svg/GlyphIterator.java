package org.apache.batik.extension.svg;

import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import org.apache.batik.gvt.font.AWTGVTFont;
import org.apache.batik.gvt.font.GVTFont;
import org.apache.batik.gvt.font.GVTGlyphVector;
import org.apache.batik.gvt.font.GVTLineMetrics;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;

public class GlyphIterator {
   public static final AttributedCharacterIterator.Attribute PREFORMATTED;
   public static final AttributedCharacterIterator.Attribute FLOW_LINE_BREAK;
   public static final AttributedCharacterIterator.Attribute TEXT_COMPOUND_ID;
   public static final AttributedCharacterIterator.Attribute GVT_FONT;
   public static final char SOFT_HYPHEN = '\u00ad';
   public static final char ZERO_WIDTH_SPACE = '\u200b';
   public static final char ZERO_WIDTH_JOINER = '\u200d';
   int idx = -1;
   int chIdx = -1;
   int lineIdx = -1;
   int aciIdx = -1;
   int charCount = -1;
   float adv = 0.0F;
   float adj = 0.0F;
   int runLimit = 0;
   int lineBreakRunLimit = 0;
   int lineBreakCount = 0;
   GVTFont font = null;
   int fontStart = 0;
   float maxAscent = 0.0F;
   float maxDescent = 0.0F;
   float maxFontSize = 0.0F;
   float width = 0.0F;
   char ch = 0;
   int numGlyphs = 0;
   AttributedCharacterIterator aci;
   GVTGlyphVector gv;
   float[] gp;
   FontRenderContext frc;
   int[] leftShiftIdx = null;
   float[] leftShiftAmt = null;
   int leftShift = 0;
   Point2D gvBase = null;

   public GlyphIterator(AttributedCharacterIterator var1, GVTGlyphVector var2) {
      this.aci = var1;
      this.gv = var2;
      this.idx = 0;
      this.chIdx = 0;
      this.lineIdx = 0;
      this.aciIdx = var1.getBeginIndex();
      this.charCount = var2.getCharacterCount(this.idx, this.idx);
      this.ch = var1.first();
      this.frc = var2.getFontRenderContext();
      this.font = (GVTFont)var1.getAttribute(GVT_FONT);
      if (this.font == null) {
         this.font = new AWTGVTFont(var1.getAttributes());
      }

      this.fontStart = this.aciIdx;
      this.maxFontSize = -3.4028235E38F;
      this.maxAscent = -3.4028235E38F;
      this.maxDescent = -3.4028235E38F;
      this.runLimit = var1.getRunLimit(TEXT_COMPOUND_ID);
      this.lineBreakRunLimit = var1.getRunLimit(FLOW_LINE_BREAK);
      Object var3 = var1.getAttribute(FLOW_LINE_BREAK);
      this.lineBreakCount = var3 == null ? 0 : 1;
      this.numGlyphs = var2.getNumGlyphs();
      this.gp = var2.getGlyphPositions(0, this.numGlyphs + 1, (float[])null);
      this.gvBase = new Point2D.Float(this.gp[0], this.gp[1]);
      this.adv = this.getCharWidth();
      this.adj = this.getCharAdvance();
   }

   public GlyphIterator(GlyphIterator var1) {
      var1.copy(this);
   }

   public GlyphIterator copy() {
      return new GlyphIterator(this);
   }

   public GlyphIterator copy(GlyphIterator var1) {
      if (var1 == null) {
         return new GlyphIterator(this);
      } else {
         var1.idx = this.idx;
         var1.chIdx = this.chIdx;
         var1.aciIdx = this.aciIdx;
         var1.charCount = this.charCount;
         var1.adv = this.adv;
         var1.adj = this.adj;
         var1.runLimit = this.runLimit;
         var1.ch = this.ch;
         var1.numGlyphs = this.numGlyphs;
         var1.gp = this.gp;
         var1.gvBase = this.gvBase;
         var1.lineBreakRunLimit = this.lineBreakRunLimit;
         var1.lineBreakCount = this.lineBreakCount;
         var1.frc = this.frc;
         var1.font = this.font;
         var1.fontStart = this.fontStart;
         var1.maxAscent = this.maxAscent;
         var1.maxDescent = this.maxDescent;
         var1.maxFontSize = this.maxFontSize;
         var1.leftShift = this.leftShift;
         var1.leftShiftIdx = this.leftShiftIdx;
         var1.leftShiftAmt = this.leftShiftAmt;
         return var1;
      }
   }

   public int getGlyphIndex() {
      return this.idx;
   }

   public char getChar() {
      return this.ch;
   }

   public int getACIIndex() {
      return this.aciIdx;
   }

   public float getAdv() {
      return this.adv;
   }

   public Point2D getOrigin() {
      return this.gvBase;
   }

   public float getAdj() {
      return this.adj;
   }

   public float getMaxFontSize() {
      if (this.aciIdx >= this.fontStart) {
         int var1 = this.aciIdx + this.charCount;
         this.updateLineMetrics(var1);
         this.fontStart = var1;
      }

      return this.maxFontSize;
   }

   public float getMaxAscent() {
      if (this.aciIdx >= this.fontStart) {
         int var1 = this.aciIdx + this.charCount;
         this.updateLineMetrics(var1);
         this.fontStart = var1;
      }

      return this.maxAscent;
   }

   public float getMaxDescent() {
      if (this.aciIdx >= this.fontStart) {
         int var1 = this.aciIdx + this.charCount;
         this.updateLineMetrics(var1);
         this.fontStart = var1;
      }

      return this.maxDescent;
   }

   public boolean isLastChar() {
      return this.idx == this.numGlyphs - 1;
   }

   public boolean done() {
      return this.idx >= this.numGlyphs;
   }

   public boolean isBreakChar() {
      switch (this.ch) {
         case '\t':
         case ' ':
            return true;
         case '\u00ad':
            return true;
         case '\u200b':
            return true;
         case '\u200d':
            return false;
         default:
            return false;
      }
   }

   protected boolean isPrinting(char var1) {
      switch (this.ch) {
         case '\t':
         case ' ':
            return false;
         case '\u00ad':
            return true;
         case '\u200b':
            return false;
         case '\u200d':
            return false;
         default:
            return true;
      }
   }

   public int getLineBreaks() {
      int var1 = 0;
      if (this.aciIdx + this.charCount >= this.lineBreakRunLimit) {
         var1 = this.lineBreakCount;
         this.aci.setIndex(this.aciIdx + this.charCount);
         this.lineBreakRunLimit = this.aci.getRunLimit(FLOW_LINE_BREAK);
         this.aci.setIndex(this.aciIdx);
         Object var2 = this.aci.getAttribute(FLOW_LINE_BREAK);
         this.lineBreakCount = var2 == null ? 0 : 1;
      }

      return var1;
   }

   public void nextChar() {
      float var1;
      if (this.ch == 173 || this.ch == 8203 || this.ch == 8205) {
         this.gv.setGlyphVisible(this.idx, false);
         var1 = this.getCharAdvance();
         this.adj -= var1;
         this.addLeftShift(this.idx, var1);
      }

      this.aciIdx += this.charCount;
      this.ch = this.aci.setIndex(this.aciIdx);
      ++this.idx;
      this.charCount = this.gv.getCharacterCount(this.idx, this.idx);
      if (this.idx != this.numGlyphs) {
         if (this.aciIdx >= this.runLimit) {
            this.updateLineMetrics(this.aciIdx);
            this.runLimit = this.aci.getRunLimit(TEXT_COMPOUND_ID);
            this.font = (GVTFont)this.aci.getAttribute(GVT_FONT);
            if (this.font == null) {
               this.font = new AWTGVTFont(this.aci.getAttributes());
            }

            this.fontStart = this.aciIdx;
         }

         var1 = this.getCharAdvance();
         this.adj += var1;
         if (this.isPrinting()) {
            this.chIdx = this.idx;
            float var2 = this.getCharWidth();
            this.adv = this.adj - (var1 - var2);
         }

      }
   }

   protected void addLeftShift(int var1, float var2) {
      if (this.leftShiftIdx == null) {
         this.leftShiftIdx = new int[1];
         this.leftShiftIdx[0] = var1;
         this.leftShiftAmt = new float[1];
         this.leftShiftAmt[0] = var2;
      } else {
         int[] var3 = new int[this.leftShiftIdx.length + 1];
         System.arraycopy(this.leftShiftIdx, 0, var3, 0, this.leftShiftIdx.length);
         var3[this.leftShiftIdx.length] = var1;
         this.leftShiftIdx = var3;
         float[] var4 = new float[this.leftShiftAmt.length + 1];
         System.arraycopy(this.leftShiftAmt, 0, var4, 0, this.leftShiftAmt.length);
         var4[this.leftShiftAmt.length] = var2;
         this.leftShiftAmt = var4;
      }

   }

   protected void updateLineMetrics(int var1) {
      GVTLineMetrics var2 = this.font.getLineMetrics((CharacterIterator)this.aci, this.fontStart, var1, this.frc);
      float var3 = var2.getAscent();
      float var4 = var2.getDescent();
      float var5 = this.font.getSize();
      if (var3 > this.maxAscent) {
         this.maxAscent = var3;
      }

      if (var4 > this.maxDescent) {
         this.maxDescent = var4;
      }

      if (var5 > this.maxFontSize) {
         this.maxFontSize = var5;
      }

   }

   public LineInfo newLine(Point2D.Float var1, float var2, boolean var3, Point2D.Float var4) {
      if (this.ch == 173) {
         this.gv.setGlyphVisible(this.idx, true);
      }

      int var5 = 0;
      int var6;
      if (this.leftShiftIdx != null) {
         var6 = this.leftShiftIdx[var5];
      } else {
         var6 = this.idx + 1;
      }

      for(int var7 = this.lineIdx; var7 <= this.idx; ++var7) {
         if (var7 == var6) {
            this.leftShift = (int)((float)this.leftShift + this.leftShiftAmt[var5++]);
            if (var5 < this.leftShiftIdx.length) {
               var6 = this.leftShiftIdx[var5];
            }
         }

         this.gv.setGlyphPosition(var7, new Point2D.Float(this.gp[2 * var7] - (float)this.leftShift, this.gp[2 * var7 + 1]));
      }

      this.leftShiftIdx = null;
      this.leftShiftAmt = null;
      int var8;
      float var13;
      if (this.chIdx == 0 && !this.isPrinting()) {
         var13 = 0.0F;
         var8 = 0;
      } else {
         var13 = this.getCharWidth(this.chIdx);
         var8 = this.chIdx + 1;
      }

      int var9 = this.idx + 1;
      float var10 = this.adv;

      float var11;
      for(var11 = this.adj; !this.done(); var11 += this.adj) {
         this.adv = 0.0F;
         this.adj = 0.0F;
         if (this.ch == 8203 || this.ch == 8205) {
            this.gv.setGlyphVisible(this.idx, false);
         }

         this.ch = 0;
         this.nextChar();
         if (this.isPrinting()) {
            break;
         }

         var9 = this.idx + 1;
      }

      for(int var12 = var8; var12 < var9; ++var12) {
         this.gv.setGlyphVisible(var12, false);
      }

      this.maxAscent = -3.4028235E38F;
      this.maxDescent = -3.4028235E38F;
      this.maxFontSize = -3.4028235E38F;
      LineInfo var14 = new LineInfo(var1, this.aci, this.gv, this.lineIdx, var9, var11, var10, var13, var2, var3, var4);
      this.lineIdx = this.idx;
      return var14;
   }

   public boolean isPrinting() {
      return this.aci.getAttribute(PREFORMATTED) == Boolean.TRUE ? true : this.isPrinting(this.ch);
   }

   public float getCharAdvance() {
      return this.getCharAdvance(this.idx);
   }

   public float getCharWidth() {
      return this.getCharWidth(this.idx);
   }

   protected float getCharAdvance(int var1) {
      return this.gp[2 * var1 + 2] - this.gp[2 * var1];
   }

   protected float getCharWidth(int var1) {
      Rectangle2D var2 = this.gv.getGlyphVisualBounds(var1).getBounds2D();
      Point2D var3 = this.gv.getGlyphPosition(var1);
      return (float)(var2.getX() + var2.getWidth() - var3.getX());
   }

   static {
      PREFORMATTED = GVTAttributedCharacterIterator.TextAttribute.PREFORMATTED;
      FLOW_LINE_BREAK = GVTAttributedCharacterIterator.TextAttribute.FLOW_LINE_BREAK;
      TEXT_COMPOUND_ID = GVTAttributedCharacterIterator.TextAttribute.TEXT_COMPOUND_ID;
      GVT_FONT = GVTAttributedCharacterIterator.TextAttribute.GVT_FONT;
   }
}
