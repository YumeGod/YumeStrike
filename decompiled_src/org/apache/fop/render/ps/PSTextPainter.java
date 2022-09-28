package org.apache.fop.render.ps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.gvt.font.GVTGlyphVector;
import org.apache.batik.gvt.renderer.StrokingTextPainter;
import org.apache.batik.gvt.text.TextPaintInfo;
import org.apache.batik.gvt.text.TextSpanLayout;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.svg.NativeTextPainter;
import org.apache.fop.util.CharUtilities;
import org.apache.xmlgraphics.java2d.ps.PSGraphics2D;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSResource;

public class PSTextPainter extends NativeTextPainter {
   private static final boolean DEBUG = false;
   private FontResourceCache fontResources;
   private static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();

   public PSTextPainter(FontInfo fontInfo) {
      super(fontInfo);
      this.fontResources = new FontResourceCache(fontInfo);
   }

   protected boolean isSupported(Graphics2D g2d) {
      return g2d instanceof PSGraphics2D;
   }

   protected void paintTextRun(StrokingTextPainter.TextRun textRun, Graphics2D g2d) throws IOException {
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
         PSGraphics2D ps = (PSGraphics2D)g2d;
         PSGenerator gen = ps.getPSGenerator();
         ps.preparePainting();
         GeneralPath debugShapes = null;
         TextUtil textUtil = new TextUtil(gen);
         textUtil.setupFonts(runaci);
         if (!textUtil.hasFonts()) {
            textRun.getLayout().draw(g2d);
         } else {
            gen.saveGraphicsState();
            gen.concatMatrix(g2d.getTransform());
            Shape imclip = g2d.getClip();
            this.clip(ps, imclip);
            gen.writeln("BT");
            AffineTransform localTransform = new AffineTransform();
            Point2D prevPos = null;
            GVTGlyphVector gv = layout.getGlyphVector();
            PSTextRun psRun = new PSTextRun();
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
                  boolean flushCurrentRun = false;
                  if (glyphTransform != null) {
                     flushCurrentRun = true;
                  }

                  if (psRun.getRunLength() >= 128) {
                     flushCurrentRun = true;
                  }

                  Point2D.Double relPos;
                  if (prevPos == null) {
                     relPos = new Point2D.Double(0.0, 0.0);
                  } else {
                     relPos = new Point2D.Double(glyphPos.getX() - prevPos.getX(), glyphPos.getY() - prevPos.getY());
                  }

                  if (psRun.vertChanges == 0 && psRun.getHorizRunLength() > 2 && relPos.getY() != 0.0) {
                     flushCurrentRun = true;
                  }

                  char paintChar = CharUtilities.isAnySpace(ch) ? 32 : ch;
                  Font f = textUtil.selectFontForChar(paintChar);
                  char mapped = f.mapChar(ch);
                  boolean fontChanging = textUtil.isFontChanging(f, mapped);
                  if (fontChanging) {
                     flushCurrentRun = true;
                  }

                  if (flushCurrentRun) {
                     psRun.paint(ps, textUtil, tpi);
                     psRun.reset();
                  }

                  psRun.addCharacter(paintChar, relPos);
                  psRun.noteStartingTransformation(localTransform);
                  if (fontChanging) {
                     textUtil.setCurrentFont(f, mapped);
                  }

                  prevPos = glyphPos;
               }
            }

            psRun.paint(ps, textUtil, tpi);
            gen.writeln("ET");
            gen.restoreGraphicsState();
         }
      }
   }

   private void applyColor(Paint paint, PSGenerator gen) throws IOException {
      if (paint != null) {
         if (paint instanceof Color) {
            Color col = (Color)paint;
            gen.useColor(col);
         } else {
            this.log.warn("Paint not supported: " + paint.toString());
         }

      }
   }

   private PSResource getResourceForFont(Font f, String postfix) {
      String key = postfix != null ? f.getFontName() + '_' + postfix : f.getFontName();
      return this.fontResources.getPSResourceForFontKey(key);
   }

   private void clip(PSGraphics2D ps, Shape shape) throws IOException {
      if (shape != null) {
         ps.getPSGenerator().writeln("newpath");
         PathIterator iter = shape.getPathIterator(IDENTITY_TRANSFORM);
         ps.processPathIterator(iter);
         ps.getPSGenerator().writeln("clip");
      }
   }

   private class PSTextRun {
      private AffineTransform textTransform;
      private List relativePositions;
      private StringBuffer currentChars;
      private int horizChanges;
      private int vertChanges;

      private PSTextRun() {
         this.relativePositions = new LinkedList();
         this.currentChars = new StringBuffer();
         this.horizChanges = 0;
         this.vertChanges = 0;
      }

      public void reset() {
         this.textTransform = null;
         this.currentChars.setLength(0);
         this.horizChanges = 0;
         this.vertChanges = 0;
         this.relativePositions.clear();
      }

      public int getHorizRunLength() {
         return this.vertChanges == 0 && this.getRunLength() > 0 ? this.getRunLength() : 0;
      }

      public void addCharacter(char paintChar, Point2D relPos) {
         this.addRelativePosition(relPos);
         this.currentChars.append(paintChar);
      }

      private void addRelativePosition(Point2D relPos) {
         if (this.getRunLength() > 0) {
            if (relPos.getX() != 0.0) {
               ++this.horizChanges;
            }

            if (relPos.getY() != 0.0) {
               ++this.vertChanges;
            }
         }

         this.relativePositions.add(relPos);
      }

      public void noteStartingTransformation(AffineTransform transform) {
         if (this.textTransform == null) {
            this.textTransform = new AffineTransform(transform);
         }

      }

      public int getRunLength() {
         return this.currentChars.length();
      }

      private boolean isXShow() {
         return this.vertChanges == 0;
      }

      private boolean isYShow() {
         return this.horizChanges == 0;
      }

      public void paint(PSGraphics2D g2d, TextUtil textUtil, TextPaintInfo tpi) throws IOException {
         if (this.getRunLength() > 0) {
            if (PSTextPainter.this.log.isDebugEnabled()) {
               PSTextPainter.this.log.debug("Text run: " + this.currentChars);
            }

            textUtil.writeTextMatrix(this.textTransform);
            if (this.isXShow()) {
               PSTextPainter.this.log.debug("Horizontal text: xshow");
               this.paintXYShow(g2d, textUtil, tpi.fillPaint, true, false);
            } else if (this.isYShow()) {
               PSTextPainter.this.log.debug("Vertical text: yshow");
               this.paintXYShow(g2d, textUtil, tpi.fillPaint, false, true);
            } else {
               PSTextPainter.this.log.debug("Arbitrary text: xyshow");
               this.paintXYShow(g2d, textUtil, tpi.fillPaint, true, true);
            }

            boolean stroke = tpi.strokePaint != null && tpi.strokeStroke != null;
            if (stroke) {
               PSTextPainter.this.log.debug("Stroked glyph outlines");
               this.paintStrokedGlyphs(g2d, textUtil, tpi.strokePaint, tpi.strokeStroke);
            }
         }

      }

      private void paintXYShow(PSGraphics2D g2d, TextUtil textUtil, Paint paint, boolean x, boolean y) throws IOException {
         PSGenerator gen = textUtil.gen;
         char firstChar = this.currentChars.charAt(0);
         Font f = textUtil.selectFontForChar(firstChar);
         char mapped = f.mapChar(firstChar);
         textUtil.selectFont(f, mapped);
         textUtil.setCurrentFont(f, mapped);
         PSTextPainter.this.applyColor(paint, gen);
         StringBuffer sb = new StringBuffer();
         sb.append('(');
         int idx = 0;

         for(int c = this.currentChars.length(); idx < c; ++idx) {
            char ch = this.currentChars.charAt(idx);
            mapped = f.mapChar(ch);
            PSGenerator.escapeChar(mapped, sb);
         }

         sb.append(')');
         if (x || y) {
            sb.append("\n[");
            idx = 0;

            for(Iterator iter = this.relativePositions.iterator(); iter.hasNext(); ++idx) {
               Point2D pt = (Point2D)iter.next();
               if (idx > 0) {
                  if (x) {
                     sb.append(this.format(gen, pt.getX()));
                  }

                  if (y) {
                     if (x) {
                        sb.append(' ');
                     }

                     sb.append(this.format(gen, -pt.getY()));
                  }

                  if (idx % 8 == 0) {
                     sb.append('\n');
                  } else {
                     sb.append(' ');
                  }
               }
            }

            if (x) {
               sb.append('0');
            }

            if (y) {
               if (x) {
                  sb.append(' ');
               }

               sb.append('0');
            }

            sb.append(']');
         }

         sb.append(' ');
         if (x) {
            sb.append('x');
         }

         if (y) {
            sb.append('y');
         }

         sb.append("show");
         gen.writeln(sb.toString());
      }

      private String format(PSGenerator gen, double coord) {
         return Math.abs(coord) < 1.0E-5 ? "0" : gen.formatDouble5(coord);
      }

      private void paintStrokedGlyphs(PSGraphics2D g2d, TextUtil textUtil, Paint strokePaint, Stroke stroke) throws IOException {
         PSGenerator gen = textUtil.gen;
         PSTextPainter.this.applyColor(strokePaint, gen);
         PSGraphics2D.applyStroke(stroke, gen);
         Font f = null;
         Iterator iter = this.relativePositions.iterator();
         iter.next();
         Point2D pos = new Point2D.Double(0.0, 0.0);
         gen.writeln("0 0 M");
         int i = 0;

         for(int c = this.currentChars.length(); i < c; ++i) {
            char ch = this.currentChars.charAt(0);
            if (i == 0) {
               f = textUtil.selectFontForChar(ch);
            }

            char mapped = f.mapChar(ch);
            if (i == 0) {
               textUtil.selectFont(f, mapped);
               textUtil.setCurrentFont(f, mapped);
            }

            mapped = f.mapChar(this.currentChars.charAt(i));
            char codepoint = (char)(mapped % 256);
            gen.write("(" + codepoint + ")");
            gen.writeln(" false charpath");
            if (iter.hasNext()) {
               Point2D pt = (Point2D)iter.next();
               pos.setLocation(pos.getX() + pt.getX(), pos.getY() - pt.getY());
               gen.writeln(gen.formatDouble5(pos.getX()) + " " + gen.formatDouble5(pos.getY()) + " M");
            }
         }

         gen.writeln("stroke");
      }

      // $FF: synthetic method
      PSTextRun(Object x1) {
         this();
      }
   }

   private class TextUtil {
      private PSGenerator gen;
      private Font[] fonts;
      private Font currentFont;
      private int currentEncoding = -1;

      public TextUtil(PSGenerator gen) {
         this.gen = gen;
      }

      public Font selectFontForChar(char ch) {
         int i = 0;

         for(int c = this.fonts.length; i < c; ++i) {
            if (this.fonts[i].hasChar(ch)) {
               return this.fonts[i];
            }
         }

         return this.fonts[0];
      }

      public void writeTextMatrix(AffineTransform transform) throws IOException {
         double[] matrix = new double[6];
         transform.getMatrix(matrix);
         this.gen.writeln(this.gen.formatDouble5(matrix[0]) + " " + this.gen.formatDouble5(matrix[1]) + " " + this.gen.formatDouble5(matrix[2]) + " " + this.gen.formatDouble5(matrix[3]) + " " + this.gen.formatDouble5(matrix[4]) + " " + this.gen.formatDouble5(matrix[5]) + " Tm");
      }

      public boolean isFontChanging(Font f, char mapped) {
         if (f != this.getCurrentFont()) {
            int encoding = mapped / 256;
            if (encoding != this.getCurrentFontEncoding()) {
               return true;
            }
         }

         return false;
      }

      public void selectFont(Font f, char mapped) throws IOException {
         int encoding = mapped / 256;
         String postfix = encoding == 0 ? null : Integer.toString(encoding);
         PSResource res = PSTextPainter.this.getResourceForFont(f, postfix);
         this.gen.useFont("/" + res.getName(), (float)f.getFontSize() / 1000.0F);
         this.gen.getResourceTracker().notifyResourceUsageOnPage(res);
      }

      public Font getCurrentFont() {
         return this.currentFont;
      }

      public int getCurrentFontEncoding() {
         return this.currentEncoding;
      }

      public void setCurrentFont(Font font, int encoding) {
         this.currentFont = font;
         this.currentEncoding = encoding;
      }

      public void setCurrentFont(Font font, char mapped) {
         int encoding = mapped / 256;
         this.setCurrentFont(font, encoding);
      }

      public void setupFonts(AttributedCharacterIterator runaci) {
         this.fonts = PSTextPainter.this.findFonts(runaci);
      }

      public boolean hasFonts() {
         return this.fonts != null && this.fonts.length > 0;
      }
   }
}
