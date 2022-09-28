package org.apache.batik.gvt.font;

import java.awt.font.GlyphMetrics;
import java.awt.geom.Rectangle2D;

public class GVTGlyphMetrics {
   private GlyphMetrics gm;
   private float verticalAdvance;

   public GVTGlyphMetrics(GlyphMetrics var1, float var2) {
      this.gm = var1;
      this.verticalAdvance = var2;
   }

   public GVTGlyphMetrics(float var1, float var2, Rectangle2D var3, byte var4) {
      this.gm = new GlyphMetrics(var1, var3, var4);
      this.verticalAdvance = var2;
   }

   public float getHorizontalAdvance() {
      return this.gm.getAdvance();
   }

   public float getVerticalAdvance() {
      return this.verticalAdvance;
   }

   public Rectangle2D getBounds2D() {
      return this.gm.getBounds2D();
   }

   public float getLSB() {
      return this.gm.getLSB();
   }

   public float getRSB() {
      return this.gm.getRSB();
   }

   public int getType() {
      return this.gm.getType();
   }

   public boolean isCombining() {
      return this.gm.isCombining();
   }

   public boolean isComponent() {
      return this.gm.isComponent();
   }

   public boolean isLigature() {
      return this.gm.isLigature();
   }

   public boolean isStandard() {
      return this.gm.isStandard();
   }

   public boolean isWhitespace() {
      return this.gm.isWhitespace();
   }
}
