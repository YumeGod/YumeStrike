package org.apache.fop.svg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.Iterator;
import java.util.List;
import org.apache.batik.dom.svg.SVGOMTextElement;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;
import org.apache.batik.gvt.font.GVTFontFamily;
import org.apache.batik.gvt.renderer.StrokingTextPainter;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.Mark;
import org.apache.batik.gvt.text.TextPaintInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.AFPGraphics2D;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;

public abstract class AbstractFOPTextPainter implements TextPainter {
   protected Log log;
   private final FOPTextHandler nativeTextHandler;
   protected static final TextPainter PROXY_PAINTER = StrokingTextPainter.getInstance();

   public AbstractFOPTextPainter(FOPTextHandler nativeTextHandler) {
      this.log = LogFactory.getLog(AbstractFOPTextPainter.class);
      this.nativeTextHandler = nativeTextHandler;
   }

   public void paint(TextNode node, Graphics2D g2d) {
      Point2D loc = node.getLocation();
      if (this.isSupportedGraphics2D(g2d) && !this.hasUnsupportedAttributes(node)) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("painting text node " + node + " normally.");
         }

         this.paintTextRuns(node.getTextRuns(), g2d, loc);
      } else {
         if (this.log.isDebugEnabled()) {
            this.log.debug("painting text node " + node + " by stroking due to unsupported attributes or an incompatible Graphics2D");
         }

         PROXY_PAINTER.paint(node, g2d);
      }

   }

   protected abstract boolean isSupportedGraphics2D(Graphics2D var1);

   private boolean hasUnsupportedAttributes(TextNode node) {
      Iterator iter = node.getTextRuns().iterator();

      boolean hasUnsupported;
      do {
         if (!iter.hasNext()) {
            return false;
         }

         StrokingTextPainter.TextRun run = (StrokingTextPainter.TextRun)iter.next();
         AttributedCharacterIterator aci = run.getACI();
         hasUnsupported = this.hasUnsupportedAttributes(aci);
      } while(!hasUnsupported);

      return true;
   }

   private boolean hasUnsupportedAttributes(AttributedCharacterIterator aci) {
      boolean hasUnsupported = false;
      Font font = this.getFont(aci);
      String text = this.getText(aci);
      if (this.hasUnsupportedGlyphs(text, font)) {
         this.log.trace("-> Unsupported glyphs found");
         hasUnsupported = true;
      }

      TextPaintInfo tpi = (TextPaintInfo)aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.PAINT_INFO);
      if (tpi != null && (tpi.strokeStroke != null && tpi.strokePaint != null || tpi.strikethroughStroke != null || tpi.underlineStroke != null || tpi.overlineStroke != null)) {
         this.log.trace("-> under/overlines etc. found");
         hasUnsupported = true;
      }

      Paint foreground = (Paint)aci.getAttribute(TextAttribute.FOREGROUND);
      if (foreground instanceof Color) {
         Color col = (Color)foreground;
         if (col.getAlpha() != 255) {
            this.log.trace("-> transparency found");
            hasUnsupported = true;
         }
      }

      Object letSpace = aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.LETTER_SPACING);
      if (letSpace != null) {
         this.log.trace("-> letter spacing found");
         hasUnsupported = true;
      }

      Object wordSpace = aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.WORD_SPACING);
      if (wordSpace != null) {
         this.log.trace("-> word spacing found");
         hasUnsupported = true;
      }

      Object lengthAdjust = aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.LENGTH_ADJUST);
      if (lengthAdjust != null) {
         this.log.trace("-> length adjustments found");
         hasUnsupported = true;
      }

      Object writeMod = aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE);
      if (writeMod != null && !GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE_LTR.equals(writeMod)) {
         this.log.trace("-> Unsupported writing modes found");
         hasUnsupported = true;
      }

      Object vertOr = aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.VERTICAL_ORIENTATION);
      if (GVTAttributedCharacterIterator.TextAttribute.ORIENTATION_ANGLE.equals(vertOr)) {
         this.log.trace("-> vertical orientation found");
         hasUnsupported = true;
      }

      Object rcDel = aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.TEXT_COMPOUND_DELIMITER);
      if (rcDel != null && !(rcDel instanceof SVGOMTextElement)) {
         this.log.trace("-> spans found");
         hasUnsupported = true;
      }

      if (hasUnsupported) {
         this.log.trace("Unsupported attributes found in ACI, using StrokingTextPainter");
      }

      return hasUnsupported;
   }

   protected void paintTextRuns(List textRuns, Graphics2D g2d, Point2D loc) {
      Point2D currentloc = loc;

      StrokingTextPainter.TextRun run;
      for(Iterator i = textRuns.iterator(); i.hasNext(); currentloc = this.paintTextRun(run, g2d, currentloc)) {
         run = (StrokingTextPainter.TextRun)i.next();
      }

   }

   protected Point2D paintTextRun(StrokingTextPainter.TextRun run, Graphics2D g2d, Point2D loc) {
      AttributedCharacterIterator aci = run.getACI();
      aci.first();
      this.updateLocationFromACI(aci, loc);
      AffineTransform at = g2d.getTransform();
      loc = at.transform(loc, (Point2D)null);
      Font font = this.getFont(aci);
      if (font != null) {
         this.nativeTextHandler.setOverrideFont(font);
      }

      TextPaintInfo tpi = (TextPaintInfo)aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.PAINT_INFO);
      if (tpi == null) {
         return loc;
      } else {
         Paint foreground = tpi.fillPaint;
         if (foreground instanceof Color) {
            Color col = (Color)foreground;
            g2d.setColor(col);
         }

         g2d.setPaint(foreground);
         TextNode.Anchor anchor = (TextNode.Anchor)aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.ANCHOR_TYPE);
         String txt = this.getText(aci);
         float advance = this.getStringWidth(txt, font);
         float tx = 0.0F;
         if (anchor != null) {
            switch (anchor.getType()) {
               case 1:
                  tx = -advance / 2.0F;
                  break;
               case 2:
                  tx = -advance;
            }
         }

         double x = loc.getX();
         double y = loc.getY();

         try {
            this.nativeTextHandler.drawString(g2d, txt, (float)x + tx, (float)y);
         } catch (IOException var21) {
            if (g2d instanceof AFPGraphics2D) {
               ((AFPGraphics2D)g2d).handleIOException(var21);
            }
         } finally {
            this.nativeTextHandler.setOverrideFont((Font)null);
         }

         loc.setLocation(loc.getX() + (double)advance, loc.getY());
         return loc;
      }
   }

   protected String getText(AttributedCharacterIterator aci) {
      StringBuffer sb = new StringBuffer(aci.getEndIndex() - aci.getBeginIndex());

      for(char c = aci.first(); c != '\uffff'; c = aci.next()) {
         sb.append(c);
      }

      return sb.toString();
   }

   private void updateLocationFromACI(AttributedCharacterIterator aci, Point2D loc) {
      Float xpos = (Float)aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.X);
      Float ypos = (Float)aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.Y);
      Float dxpos = (Float)aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.DX);
      Float dypos = (Float)aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.DY);
      if (xpos != null) {
         loc.setLocation(xpos.doubleValue(), loc.getY());
      }

      if (ypos != null) {
         loc.setLocation(loc.getX(), ypos.doubleValue());
      }

      if (dxpos != null) {
         loc.setLocation(loc.getX() + dxpos.doubleValue(), loc.getY());
      }

      if (dypos != null) {
         loc.setLocation(loc.getX(), loc.getY() + dypos.doubleValue());
      }

   }

   private String getStyle(AttributedCharacterIterator aci) {
      Float posture = (Float)aci.getAttribute(TextAttribute.POSTURE);
      return posture != null && (double)posture > 0.0 ? "italic" : "normal";
   }

   private int getWeight(AttributedCharacterIterator aci) {
      Float taWeight = (Float)aci.getAttribute(TextAttribute.WEIGHT);
      return taWeight != null && (double)taWeight > 1.0 ? 700 : 400;
   }

   private Font getFont(AttributedCharacterIterator aci) {
      Float fontSize = (Float)aci.getAttribute(TextAttribute.SIZE);
      if (fontSize == null) {
         fontSize = new Float(10.0F);
      }

      String style = this.getStyle(aci);
      int weight = this.getWeight(aci);
      FontInfo fontInfo = this.nativeTextHandler.getFontInfo();
      String fontFamily = null;
      List gvtFonts = (List)aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.GVT_FONT_FAMILIES);
      if (gvtFonts != null) {
         Iterator i = gvtFonts.iterator();

         while(i.hasNext()) {
            GVTFontFamily fam = (GVTFontFamily)i.next();
            fontFamily = fam.getFamilyName();
            if (fontInfo.hasFont(fontFamily, style, weight)) {
               FontTriplet triplet = fontInfo.fontLookup(fontFamily, style, weight);
               int fsize = (int)(fontSize * 1000.0F);
               return fontInfo.getFontInstance(triplet, fsize);
            }
         }
      }

      FontTriplet triplet = fontInfo.fontLookup((String)"any", style, 400);
      int fsize = (int)(fontSize * 1000.0F);
      return fontInfo.getFontInstance(triplet, fsize);
   }

   private float getStringWidth(String str, Font font) {
      float wordWidth = 0.0F;
      float whitespaceWidth = (float)font.getWidth(font.mapChar(' '));

      for(int i = 0; i < str.length(); ++i) {
         char c = str.charAt(i);
         float charWidth;
         if (c != ' ' && c != '\n' && c != '\r' && c != '\t') {
            charWidth = (float)font.getWidth(font.mapChar(c));
            if (charWidth <= 0.0F) {
               charWidth = whitespaceWidth;
            }
         } else {
            charWidth = whitespaceWidth;
         }

         wordWidth += charWidth;
      }

      return wordWidth / 1000.0F;
   }

   private boolean hasUnsupportedGlyphs(String str, Font font) {
      for(int i = 0; i < str.length(); ++i) {
         char c = str.charAt(i);
         if (c != ' ' && c != '\n' && c != '\r' && c != '\t' && !font.hasChar(c)) {
            return true;
         }
      }

      return false;
   }

   public Shape getOutline(TextNode node) {
      return PROXY_PAINTER.getOutline(node);
   }

   public Rectangle2D getBounds2D(TextNode node) {
      return PROXY_PAINTER.getBounds2D(node);
   }

   public Rectangle2D getGeometryBounds(TextNode node) {
      return PROXY_PAINTER.getGeometryBounds(node);
   }

   public Mark getMark(TextNode node, int pos, boolean all) {
      return null;
   }

   public Mark selectAt(double x, double y, TextNode node) {
      return null;
   }

   public Mark selectTo(double x, double y, Mark beginMark) {
      return null;
   }

   public Mark selectFirst(TextNode node) {
      return null;
   }

   public Mark selectLast(TextNode node) {
      return null;
   }

   public int[] getSelected(Mark start, Mark finish) {
      return null;
   }

   public Shape getHighlightShape(Mark beginMark, Mark endMark) {
      return null;
   }
}
