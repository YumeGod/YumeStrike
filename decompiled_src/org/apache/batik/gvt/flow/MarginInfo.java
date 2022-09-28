package org.apache.batik.gvt.flow;

public class MarginInfo {
   public static final int ALIGN_START = 0;
   public static final int ALIGN_MIDDLE = 1;
   public static final int ALIGN_END = 2;
   public static final int ALIGN_FULL = 3;
   protected float top;
   protected float right;
   protected float bottom;
   protected float left;
   protected float indent;
   protected int alignment;
   protected float lineHeight;
   protected boolean fontSizeRelative;
   protected boolean flowRegionBreak;

   public MarginInfo(float var1, float var2, float var3, float var4, float var5, int var6, float var7, boolean var8, boolean var9) {
      this.top = var1;
      this.right = var2;
      this.bottom = var3;
      this.left = var4;
      this.indent = var5;
      this.alignment = var6;
      this.lineHeight = var7;
      this.fontSizeRelative = var8;
      this.flowRegionBreak = var9;
   }

   public MarginInfo(float var1, int var2) {
      this.setMargin(var1);
      this.indent = 0.0F;
      this.alignment = var2;
      this.flowRegionBreak = false;
   }

   public void setMargin(float var1) {
      this.top = var1;
      this.right = var1;
      this.bottom = var1;
      this.left = var1;
   }

   public float getTopMargin() {
      return this.top;
   }

   public float getRightMargin() {
      return this.right;
   }

   public float getBottomMargin() {
      return this.bottom;
   }

   public float getLeftMargin() {
      return this.left;
   }

   public float getIndent() {
      return this.indent;
   }

   public int getTextAlignment() {
      return this.alignment;
   }

   public float getLineHeight() {
      return this.lineHeight;
   }

   public boolean isFontSizeRelative() {
      return this.fontSizeRelative;
   }

   public boolean isFlowRegionBreak() {
      return this.flowRegionBreak;
   }
}
