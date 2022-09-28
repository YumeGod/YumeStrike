package org.apache.fop.svg;

import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.batik.bridge.SVGFontFamily;
import org.apache.batik.gvt.font.GVTFont;
import org.apache.batik.gvt.font.GVTFontFamily;
import org.apache.batik.gvt.renderer.StrokingTextPainter;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.TextSpanLayout;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.util.CharUtilities;

public abstract class NativeTextPainter extends StrokingTextPainter {
   protected Log log;
   protected final FontInfo fontInfo;

   public NativeTextPainter(FontInfo fontInfo) {
      this.log = LogFactory.getLog(NativeTextPainter.class);
      this.fontInfo = fontInfo;
   }

   protected abstract boolean isSupported(Graphics2D var1);

   protected abstract void paintTextRun(StrokingTextPainter.TextRun var1, Graphics2D var2) throws IOException;

   protected void paintTextRuns(List textRuns, Graphics2D g2d) {
      if (this.log.isTraceEnabled()) {
         this.log.trace("paintTextRuns: count = " + textRuns.size());
      }

      if (!this.isSupported(g2d)) {
         super.paintTextRuns(textRuns, g2d);
      } else {
         for(int i = 0; i < textRuns.size(); ++i) {
            StrokingTextPainter.TextRun textRun = (StrokingTextPainter.TextRun)textRuns.get(i);

            try {
               this.paintTextRun(textRun, g2d);
            } catch (IOException var6) {
               throw new RuntimeException(var6);
            }
         }

      }
   }

   protected Font[] findFonts(AttributedCharacterIterator aci) {
      List fonts = new ArrayList();
      List gvtFonts = (List)aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.GVT_FONT_FAMILIES);
      Float posture = (Float)aci.getAttribute(TextAttribute.POSTURE);
      Float taWeight = (Float)aci.getAttribute(TextAttribute.WEIGHT);
      Float fontSize = (Float)aci.getAttribute(TextAttribute.SIZE);
      String style = posture != null && (double)posture > 0.0 ? "italic" : "normal";
      int weight = taWeight != null && (double)taWeight > 1.0 ? 700 : 400;
      String firstFontFamily = null;
      GVTFont gvtFont = (GVTFont)aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.GVT_FONT);
      if (gvtFont != null) {
         try {
            String gvtFontFamily = gvtFont.getFamilyName();
            if (this.log.isDebugEnabled()) {
               this.log.debug("Matching font family: " + gvtFontFamily);
            }

            if (this.fontInfo.hasFont(gvtFontFamily, style, weight)) {
               FontTriplet triplet = this.fontInfo.fontLookup(gvtFontFamily, style, weight);
               int fsize = (int)(fontSize * 1000.0F);
               fonts.add(this.fontInfo.getFontInstance(triplet, fsize));
            }

            firstFontFamily = gvtFontFamily;
         } catch (Exception var16) {
         }
      }

      if (gvtFonts != null) {
         Iterator i = gvtFonts.iterator();

         while(i.hasNext()) {
            GVTFontFamily fam = (GVTFontFamily)i.next();
            if (fam instanceof SVGFontFamily) {
               return null;
            }

            String fontFamily = fam.getFamilyName();
            if (this.log.isDebugEnabled()) {
               this.log.debug("Matching font family: " + fontFamily);
            }

            if (this.fontInfo.hasFont(fontFamily, style, weight)) {
               FontTriplet triplet = this.fontInfo.fontLookup(fontFamily, style, weight);
               int fsize = (int)(fontSize * 1000.0F);
               fonts.add(this.fontInfo.getFontInstance(triplet, fsize));
            }

            if (firstFontFamily == null) {
               firstFontFamily = fontFamily;
            }
         }
      }

      if (fonts.size() == 0) {
         if (firstFontFamily == null) {
            firstFontFamily = "any";
         }

         FontTriplet triplet = this.fontInfo.fontLookup(firstFontFamily, style, weight);
         int fsize = (int)(fontSize * 1000.0F);
         fonts.add(this.fontInfo.getFontInstance(triplet, fsize));
      }

      return (Font[])fonts.toArray(new Font[fonts.size()]);
   }

   protected CharSequence collectCharacters(AttributedCharacterIterator runaci) {
      StringBuffer chars = new StringBuffer();
      runaci.first();

      while(runaci.getIndex() < runaci.getEndIndex()) {
         chars.append(runaci.current());
         runaci.next();
      }

      return chars;
   }

   protected final void logTextRun(AttributedCharacterIterator runaci, TextSpanLayout layout) {
      if (this.log.isTraceEnabled()) {
         int charCount = runaci.getEndIndex() - runaci.getBeginIndex();
         this.log.trace("================================================");
         this.log.trace("New text run:");
         this.log.trace("char count: " + charCount);
         this.log.trace("range: " + runaci.getBeginIndex() + " - " + runaci.getEndIndex());
         this.log.trace("glyph count: " + layout.getGlyphCount());
      }

   }

   protected final void logCharacter(char ch, TextSpanLayout layout, int index, boolean visibleChar) {
      if (this.log.isTraceEnabled()) {
         this.log.trace("glyph " + index + " -> " + layout.getGlyphIndex(index) + " => " + ch);
         if (CharUtilities.isAnySpace(ch) && ch != ' ') {
            this.log.trace("Space found: " + Integer.toHexString(ch));
         } else if (ch == 8205) {
            this.log.trace("ZWJ found: " + Integer.toHexString(ch));
         } else if (ch == 173) {
            this.log.trace("Soft hyphen found: " + Integer.toHexString(ch));
         }

         if (!visibleChar) {
            this.log.trace("Invisible glyph found: " + Integer.toHexString(ch));
         }
      }

   }
}
