package org.apache.fop.afp.svg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.AFPGraphics2D;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.fonts.AFPFont;
import org.apache.fop.afp.fonts.AFPFontAttributes;
import org.apache.fop.afp.fonts.AFPPageFonts;
import org.apache.fop.afp.modca.GraphicsObject;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.svg.FOPTextHandler;

public class AFPTextHandler implements FOPTextHandler {
   private static Log log;
   protected Font overrideFont = null;
   private final FontInfo fontInfo;

   public AFPTextHandler(FontInfo fontInfo) {
      this.fontInfo = fontInfo;
   }

   public FontInfo getFontInfo() {
      return this.fontInfo;
   }

   private int registerPageFont(AFPPageFonts pageFonts, String internalFontName, int fontSize) {
      AFPFont afpFont = (AFPFont)this.fontInfo.getFonts().get(internalFontName);
      AFPFontAttributes afpFontAttributes = pageFonts.registerFont(internalFontName, afpFont, fontSize);
      return afpFontAttributes.getFontReference();
   }

   public void drawString(String text, float x, float y) throws IOException {
      throw new UnsupportedOperationException("Deprecated method!");
   }

   public void drawString(Graphics2D g, String str, float x, float y) throws IOException {
      if (log.isDebugEnabled()) {
         log.debug("drawString() str=" + str + ", x=" + x + ", y=" + y);
      }

      if (g instanceof AFPGraphics2D) {
         AFPGraphics2D g2d = (AFPGraphics2D)g;
         GraphicsObject graphicsObj = g2d.getGraphicsObject();
         Color color = g2d.getColor();
         AFPPaintingState paintingState = g2d.getPaintingState();
         if (paintingState.setColor(color)) {
            graphicsObj.setColor(color);
         }

         int fontReference = false;
         AFPPageFonts pageFonts = paintingState.getPageFonts();
         int fontSize;
         String internalFontName;
         if (this.overrideFont != null) {
            internalFontName = this.overrideFont.getFontName();
            fontSize = this.overrideFont.getFontSize();
         } else {
            java.awt.Font awtFont = g2d.getFont();
            Font fopFont = this.fontInfo.getFontInstanceForAWTFont(awtFont);
            internalFontName = fopFont.getFontName();
            fontSize = fopFont.getFontSize();
         }

         fontSize = (int)Math.round(g2d.convertToAbsoluteLength((double)fontSize));
         int fontReference = this.registerPageFont(pageFonts, internalFontName, fontSize);
         graphicsObj.setCharacterSet(fontReference);
         graphicsObj.addString(str, Math.round(x), Math.round(y));
      } else {
         g.drawString(str, x, y);
      }

   }

   public void setOverrideFont(Font overrideFont) {
      this.overrideFont = overrideFont;
   }

   static {
      log = LogFactory.getLog(AFPTextHandler.class);
   }
}
