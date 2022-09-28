package org.apache.batik.gvt.flow;

import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.batik.gvt.font.GVTFont;
import org.apache.batik.gvt.font.GVTLineMetrics;

public class BlockInfo {
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
   protected List fontList;
   protected Map fontAttrs;
   protected float ascent = -1.0F;
   protected float descent = -1.0F;
   protected boolean flowRegionBreak;

   public BlockInfo(float var1, float var2, float var3, float var4, float var5, int var6, float var7, List var8, Map var9, boolean var10) {
      this.top = var1;
      this.right = var2;
      this.bottom = var3;
      this.left = var4;
      this.indent = var5;
      this.alignment = var6;
      this.lineHeight = var7;
      this.fontList = var8;
      this.fontAttrs = var9;
      this.flowRegionBreak = var10;
   }

   public BlockInfo(float var1, int var2) {
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

   public void initLineInfo(FontRenderContext var1) {
      float var2 = 12.0F;
      Float var3 = (Float)this.fontAttrs.get(TextAttribute.SIZE);
      if (var3 != null) {
         var2 = var3;
      }

      Iterator var4 = this.fontList.iterator();
      if (var4.hasNext()) {
         GVTFont var5 = (GVTFont)var4.next();
         GVTLineMetrics var6 = var5.getLineMetrics("", var1);
         this.ascent = var6.getAscent();
         this.descent = var6.getDescent();
      }

      if (this.ascent == -1.0F) {
         this.ascent = var2 * 0.8F;
         this.descent = var2 * 0.2F;
      }

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

   public List getFontList() {
      return this.fontList;
   }

   public Map getFontAttrs() {
      return this.fontAttrs;
   }

   public float getAscent() {
      return this.ascent;
   }

   public float getDescent() {
      return this.descent;
   }

   public boolean isFlowRegionBreak() {
      return this.flowRegionBreak;
   }
}
