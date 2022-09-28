package org.apache.xmlgraphics.java2d;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

public class StrokingTextHandler implements TextHandler {
   private AbstractGraphics2D g2d;

   /** @deprecated */
   public StrokingTextHandler(AbstractGraphics2D g2d) {
      this();
      this.g2d = g2d;
   }

   public StrokingTextHandler() {
   }

   public void drawString(Graphics2D g2d, String text, float x, float y) {
      Font awtFont = g2d.getFont();
      FontRenderContext frc = g2d.getFontRenderContext();
      GlyphVector gv = awtFont.createGlyphVector(frc, text);
      Shape glyphOutline = gv.getOutline(x, y);
      g2d.fill(glyphOutline);
   }

   public void drawString(String text, float x, float y) {
      if (this.g2d == null) {
         throw new NullPointerException("Use legacy constructor when calling this deprecated method!");
      } else {
         this.drawString(this.g2d, text, x, y);
      }
   }
}
