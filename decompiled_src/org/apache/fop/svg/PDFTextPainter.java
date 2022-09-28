package org.apache.fop.svg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import org.apache.batik.gvt.font.GVTGlyphVector;
import org.apache.batik.gvt.renderer.StrokingTextPainter;
import org.apache.batik.gvt.text.TextPaintInfo;
import org.apache.batik.gvt.text.TextSpanLayout;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.util.CharUtilities;

class PDFTextPainter extends NativeTextPainter {
   private static final boolean DEBUG = false;

   public PDFTextPainter(FontInfo fi) {
      super(fi);
   }

   protected boolean isSupported(Graphics2D g2d) {
      return g2d instanceof PDFGraphics2D;
   }

   protected void paintTextRun(StrokingTextPainter.TextRun textRun, Graphics2D g2d) {
      AttributedCharacterIterator runaci = textRun.getACI();
      runaci.first();
      TextPaintInfo tpi = (TextPaintInfo)runaci.getAttribute(PAINT_INFO);
      if (tpi != null && tpi.visible) {
         if (tpi != null && tpi.composite != null) {
            g2d.setComposite(tpi.composite);
         }

         TextSpanLayout layout = textRun.getLayout();
         this.logTextRun(runaci, layout);
         CharSequence chars = this.collectCharacters(runaci);
         runaci.first();
         final PDFGraphics2D pdf = (PDFGraphics2D)g2d;
         PDFTextUtil textUtil = new PDFTextUtil(pdf.fontInfo) {
            protected void write(String code) {
               pdf.currentStream.write(code);
            }
         };
         GeneralPath debugShapes = null;
         Font[] fonts = this.findFonts(runaci);
         if (fonts != null && fonts.length != 0) {
            textUtil.saveGraphicsState();
            textUtil.concatMatrix(g2d.getTransform());
            Shape imclip = g2d.getClip();
            pdf.writeClip(imclip);
            this.applyColorAndPaint(tpi, pdf);
            textUtil.beginTextObject();
            textUtil.setFonts(fonts);
            boolean stroke = tpi.strokePaint != null && tpi.strokeStroke != null;
            textUtil.setTextRenderingMode(tpi.fillPaint != null, stroke, false);
            AffineTransform localTransform = new AffineTransform();
            Point2D prevPos = null;
            double prevVisibleCharWidth = 0.0;
            GVTGlyphVector gv = layout.getGlyphVector();
            int index = 0;

            for(int c = gv.getNumGlyphs(); index < c; ++index) {
               char ch = chars.charAt(index);
               boolean visibleChar = gv.isGlyphVisible(index) || CharUtilities.isAnySpace(ch) && !CharUtilities.isZeroWidthSpace(ch);
               this.logCharacter(ch, layout, index, visibleChar);
               if (visibleChar) {
                  Point2D glyphPos = gv.getGlyphPosition(index);
                  AffineTransform glyphTransform = gv.getGlyphTransform(index);
                  if (this.log.isTraceEnabled()) {
                     this.log.trace("pos " + glyphPos + ", transform " + glyphTransform);
                  }

                  localTransform.setToIdentity();
                  localTransform.translate(glyphPos.getX(), glyphPos.getY());
                  if (glyphTransform != null) {
                     localTransform.concatenate(glyphTransform);
                  }

                  localTransform.scale(1.0, -1.0);
                  boolean yPosChanged = prevPos == null || prevPos.getY() != glyphPos.getY() || glyphTransform != null;
                  if (yPosChanged) {
                     if (index > 0) {
                        textUtil.writeTJ();
                        textUtil.writeTextMatrix(localTransform);
                     }
                  } else {
                     double xdiff = glyphPos.getX() - prevPos.getX();
                     Font font = textUtil.getCurrentFont();
                     double effxdiff = 1000.0 * xdiff - prevVisibleCharWidth;
                     if (effxdiff != 0.0) {
                        double adjust = -effxdiff / (double)font.getFontSize();
                        textUtil.adjustGlyphTJ(adjust * 1000.0);
                     }

                     if (this.log.isTraceEnabled()) {
                        this.log.trace("==> x diff: " + xdiff + ", " + effxdiff + ", charWidth: " + prevVisibleCharWidth);
                     }
                  }

                  Font f = textUtil.selectFontForChar(ch);
                  if (f != textUtil.getCurrentFont()) {
                     textUtil.writeTJ();
                     textUtil.setCurrentFont(f);
                     textUtil.writeTf(f);
                     textUtil.writeTextMatrix(localTransform);
                  }

                  char paintChar = CharUtilities.isAnySpace(ch) ? 32 : ch;
                  textUtil.writeTJChar(paintChar);
                  prevPos = glyphPos;
                  prevVisibleCharWidth = (double)textUtil.getCurrentFont().getCharWidth(chars.charAt(index));
               }
            }

            textUtil.writeTJ();
            textUtil.endTextObject();
            textUtil.restoreGraphicsState();
         } else {
            textRun.getLayout().draw(g2d);
         }
      }
   }

   private void applyColorAndPaint(TextPaintInfo tpi, PDFGraphics2D pdf) {
      Paint fillPaint = tpi.fillPaint;
      Paint strokePaint = tpi.strokePaint;
      Stroke stroke = tpi.strokeStroke;
      int fillAlpha = 255;
      Color col;
      if (fillPaint instanceof Color) {
         col = (Color)fillPaint;
         pdf.applyColor(col, true);
         fillAlpha = col.getAlpha();
      }

      if (strokePaint instanceof Color) {
         col = (Color)strokePaint;
         pdf.applyColor(col, false);
      }

      pdf.applyPaint(fillPaint, true);
      pdf.applyStroke(stroke);
      if (strokePaint != null) {
         pdf.applyPaint(strokePaint, false);
      }

      pdf.applyAlpha(fillAlpha, 255);
   }
}
