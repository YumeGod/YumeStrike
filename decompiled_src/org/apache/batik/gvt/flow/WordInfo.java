package org.apache.batik.gvt.flow;

import org.apache.batik.gvt.font.GVTFont;
import org.apache.batik.gvt.font.GVTLineMetrics;

class WordInfo {
   int index = -1;
   float ascent = -1.0F;
   float descent = -1.0F;
   float lineHeight = -1.0F;
   GlyphGroupInfo[] glyphGroups = null;
   Object flowLine = null;

   WordInfo(int var1) {
      this.index = var1;
   }

   WordInfo(int var1, float var2, float var3, float var4, GlyphGroupInfo[] var5) {
      this.index = var1;
      this.ascent = var2;
      this.descent = var3;
      this.lineHeight = var4;
      this.glyphGroups = var5;
   }

   public int getIndex() {
      return this.index;
   }

   public float getAscent() {
      return this.ascent;
   }

   public void setAscent(float var1) {
      this.ascent = var1;
   }

   public float getDescent() {
      return this.descent;
   }

   public void setDescent(float var1) {
      this.descent = var1;
   }

   public void addLineMetrics(GVTFont var1, GVTLineMetrics var2) {
      if (this.ascent < var2.getAscent()) {
         this.ascent = var2.getAscent();
      }

      if (this.descent < var2.getDescent()) {
         this.descent = var2.getDescent();
      }

   }

   public float getLineHeight() {
      return this.lineHeight;
   }

   public void setLineHeight(float var1) {
      this.lineHeight = var1;
   }

   public void addLineHeight(float var1) {
      if (this.lineHeight < var1) {
         this.lineHeight = var1;
      }

   }

   public Object getFlowLine() {
      return this.flowLine;
   }

   public void setFlowLine(Object var1) {
      this.flowLine = var1;
   }

   public int getNumGlyphGroups() {
      return this.glyphGroups == null ? -1 : this.glyphGroups.length;
   }

   public void setGlyphGroups(GlyphGroupInfo[] var1) {
      this.glyphGroups = var1;
   }

   public GlyphGroupInfo getGlyphGroup(int var1) {
      return this.glyphGroups == null ? null : this.glyphGroups[var1];
   }
}
