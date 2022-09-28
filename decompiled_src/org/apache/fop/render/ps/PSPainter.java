package org.apache.fop.render.ps;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.fonts.LazyFont;
import org.apache.fop.fonts.SingleByteFont;
import org.apache.fop.fonts.Typeface;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.render.intermediate.AbstractIFPainter;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFState;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.RuleStyle;
import org.apache.fop.util.CharUtilities;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageProcessingHints;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSResource;
import org.w3c.dom.Document;

public class PSPainter extends AbstractIFPainter {
   private static Log log;
   private PSDocumentHandler documentHandler;
   private PSBorderPainter borderPainter;
   private boolean inTextMode = false;

   public PSPainter(PSDocumentHandler documentHandler) {
      this.documentHandler = documentHandler;
      this.borderPainter = new PSBorderPainter(documentHandler.gen);
      this.state = IFState.create();
   }

   protected IFContext getContext() {
      return this.documentHandler.getContext();
   }

   PSRenderingUtil getPSUtil() {
      return this.documentHandler.psUtil;
   }

   FontInfo getFontInfo() {
      return this.documentHandler.getFontInfo();
   }

   private PSGenerator getGenerator() {
      return this.documentHandler.gen;
   }

   public void startViewport(AffineTransform transform, Dimension size, Rectangle clipRect) throws IFException {
      try {
         PSGenerator generator = this.getGenerator();
         this.saveGraphicsState();
         generator.concatMatrix(toPoints(transform));
      } catch (IOException var5) {
         throw new IFException("I/O error in startViewport()", var5);
      }

      if (clipRect != null) {
         this.clipRect(clipRect);
      }

   }

   public void endViewport() throws IFException {
      try {
         this.restoreGraphicsState();
      } catch (IOException var2) {
         throw new IFException("I/O error in endViewport()", var2);
      }
   }

   public void startGroup(AffineTransform transform) throws IFException {
      try {
         PSGenerator generator = this.getGenerator();
         this.saveGraphicsState();
         generator.concatMatrix(toPoints(transform));
      } catch (IOException var3) {
         throw new IFException("I/O error in startGroup()", var3);
      }
   }

   public void endGroup() throws IFException {
      try {
         this.restoreGraphicsState();
      } catch (IOException var2) {
         throw new IFException("I/O error in endGroup()", var2);
      }
   }

   protected Map createDefaultImageProcessingHints(ImageSessionContext sessionContext) {
      Map hints = super.createDefaultImageProcessingHints(sessionContext);
      hints.put(ImageProcessingHints.TRANSPARENCY_INTENT, "ignore");
      return hints;
   }

   protected RenderingContext createRenderingContext() {
      PSRenderingContext psContext = new PSRenderingContext(this.getUserAgent(), this.getGenerator(), this.getFontInfo());
      return psContext;
   }

   protected void drawImageUsingImageHandler(ImageInfo info, Rectangle rect) throws ImageException, IOException {
      if (this.getPSUtil().isOptimizeResources() && !PSImageUtils.isImageInlined(info, (PSRenderingContext)this.createRenderingContext())) {
         if (log.isDebugEnabled()) {
            log.debug("Image " + info + " is embedded as a form later");
         }

         PSResource form = this.documentHandler.getFormForImage(info.getOriginalURI());
         PSImageUtils.drawForm(form, info, rect, this.getGenerator());
      } else {
         super.drawImageUsingImageHandler(info, rect);
      }

   }

   public void drawImage(String uri, Rectangle rect) throws IFException {
      try {
         this.endTextObject();
      } catch (IOException var4) {
         throw new IFException("I/O error in drawImage()", var4);
      }

      this.drawImageUsingURI(uri, rect);
   }

   public void drawImage(Document doc, Rectangle rect) throws IFException {
      try {
         this.endTextObject();
      } catch (IOException var4) {
         throw new IFException("I/O error in drawImage()", var4);
      }

      this.drawImageUsingDocument(doc, rect);
   }

   public void clipRect(Rectangle rect) throws IFException {
      try {
         PSGenerator generator = this.getGenerator();
         this.endTextObject();
         generator.defineRect((double)rect.x / 1000.0, (double)rect.y / 1000.0, (double)rect.width / 1000.0, (double)rect.height / 1000.0);
         generator.writeln("clip newpath");
      } catch (IOException var3) {
         throw new IFException("I/O error in clipRect()", var3);
      }
   }

   public void fillRect(Rectangle rect, Paint fill) throws IFException {
      if (fill != null) {
         if (rect.width != 0 && rect.height != 0) {
            try {
               this.endTextObject();
               PSGenerator generator = this.getGenerator();
               if (fill != null) {
                  if (!(fill instanceof Color)) {
                     throw new UnsupportedOperationException("Non-Color paints NYI");
                  }

                  generator.useColor((Color)fill);
               }

               generator.defineRect((double)rect.x / 1000.0, (double)rect.y / 1000.0, (double)rect.width / 1000.0, (double)rect.height / 1000.0);
               generator.writeln("fill");
            } catch (IOException var4) {
               throw new IFException("I/O error in fillRect()", var4);
            }
         }

      }
   }

   public void drawBorderRect(Rectangle rect, BorderProps before, BorderProps after, BorderProps start, BorderProps end) throws IFException {
      if (before != null || after != null || start != null || end != null) {
         try {
            this.endTextObject();
            this.borderPainter.drawBorders(rect, before, after, start, end);
         } catch (IOException var7) {
            throw new IFException("I/O error in drawBorderRect()", var7);
         }
      }

   }

   public void drawLine(Point start, Point end, int width, Color color, RuleStyle style) throws IFException {
      try {
         this.endTextObject();
         this.borderPainter.drawLine(start, end, width, color, style);
      } catch (IOException var7) {
         throw new IFException("I/O error in drawLine()", var7);
      }
   }

   private Typeface getTypeface(String fontName) {
      if (fontName == null) {
         throw new NullPointerException("fontName must not be null");
      } else {
         Typeface tf = (Typeface)this.getFontInfo().getFonts().get(fontName);
         if (tf instanceof LazyFont) {
            tf = ((LazyFont)tf).getRealFont();
         }

         return tf;
      }
   }

   protected void saveGraphicsState() throws IOException {
      this.endTextObject();
      this.getGenerator().saveGraphicsState();
   }

   protected void restoreGraphicsState() throws IOException {
      this.endTextObject();
      this.getGenerator().restoreGraphicsState();
   }

   protected void beginTextObject() throws IOException {
      if (!this.inTextMode) {
         PSGenerator generator = this.getGenerator();
         generator.saveGraphicsState();
         generator.writeln("BT");
         this.inTextMode = true;
      }

   }

   protected void endTextObject() throws IOException {
      if (this.inTextMode) {
         this.inTextMode = false;
         PSGenerator generator = this.getGenerator();
         generator.writeln("ET");
         generator.restoreGraphicsState();
      }

   }

   private String formatMptAsPt(PSGenerator gen, int value) {
      return gen.formatDouble((double)value / 1000.0);
   }

   public void drawText(int x, int y, int letterSpacing, int wordSpacing, int[] dx, String text) throws IFException {
      try {
         PSGenerator generator = this.getGenerator();
         generator.useColor(this.state.getTextColor());
         this.beginTextObject();
         FontTriplet triplet = new FontTriplet(this.state.getFontFamily(), this.state.getFontStyle(), this.state.getFontWeight());
         String fontKey = this.getFontInfo().getInternalFontKey(triplet);
         if (fontKey == null) {
            throw new IFException("Font not available: " + triplet, (Exception)null);
         } else {
            int sizeMillipoints = this.state.getFontSize();
            Typeface tf = this.getTypeface(fontKey);
            SingleByteFont singleByteFont = null;
            if (tf instanceof SingleByteFont) {
               singleByteFont = (SingleByteFont)tf;
            }

            Font font = this.getFontInfo().getFontInstance(triplet, sizeMillipoints);
            this.useFont(fontKey, sizeMillipoints);
            generator.writeln("1 0 0 -1 " + this.formatMptAsPt(generator, x) + " " + this.formatMptAsPt(generator, y) + " Tm");
            int textLen = text.length();
            if (singleByteFont != null && singleByteFont.hasAdditionalEncodings()) {
               int start = 0;
               int currentEncoding = -1;

               for(int i = 0; i < textLen; ++i) {
                  char c = text.charAt(i);
                  char mapped = tf.mapChar(c);
                  int encoding = mapped / 256;
                  if (currentEncoding != encoding) {
                     if (i > 0) {
                        this.writeText(text, start, i - start, letterSpacing, wordSpacing, dx, font, tf);
                     }

                     if (encoding == 0) {
                        this.useFont(fontKey, sizeMillipoints);
                     } else {
                        this.useFont(fontKey + "_" + Integer.toString(encoding), sizeMillipoints);
                     }

                     currentEncoding = encoding;
                     start = i;
                  }
               }

               this.writeText(text, start, textLen - start, letterSpacing, wordSpacing, dx, font, tf);
            } else {
               this.useFont(fontKey, sizeMillipoints);
               this.writeText(text, 0, textLen, letterSpacing, wordSpacing, dx, font, tf);
            }

         }
      } catch (IOException var21) {
         throw new IFException("I/O error in drawText()", var21);
      }
   }

   private void writeText(String text, int start, int len, int letterSpacing, int wordSpacing, int[] dx, Font font, Typeface tf) throws IOException {
      PSGenerator generator = this.getGenerator();
      int end = start + len;
      int initialSize = len + len / 2;
      boolean hasLetterSpacing = letterSpacing != 0;
      boolean needTJ = false;
      int lineStart = 0;
      StringBuffer accText = new StringBuffer(initialSize);
      StringBuffer sb = new StringBuffer(initialSize);
      int dxl = dx != null ? dx.length : 0;

      for(int i = start; i < end; ++i) {
         char orgChar = text.charAt(i);
         int glyphAdjust = 0;
         char ch;
         if (CharUtilities.isFixedWidthSpace(orgChar)) {
            ch = font.mapChar(' ');
            int cw = font.getCharWidth(orgChar);
            glyphAdjust = font.getCharWidth(ch) - cw;
         } else {
            if (wordSpacing != 0 && CharUtilities.isAdjustableSpace(orgChar)) {
               glyphAdjust -= wordSpacing;
            }

            ch = font.mapChar(orgChar);
            font.getCharWidth(orgChar);
         }

         if (dx != null && i < dxl - 1) {
            glyphAdjust -= dx[i + 1];
         }

         char codepoint = (char)(ch % 256);
         PSGenerator.escapeChar(codepoint, accText);
         if (glyphAdjust != 0) {
            needTJ = true;
            if (sb.length() == 0) {
               sb.append('[');
            }

            if (accText.length() > 0) {
               if (sb.length() - lineStart + accText.length() > 200) {
                  sb.append('\n');
                  lineStart = sb.length();
               }

               sb.append('(');
               sb.append(accText);
               sb.append(") ");
               accText.setLength(0);
            }

            sb.append(Integer.toString(glyphAdjust)).append(' ');
         }
      }

      if (needTJ) {
         if (accText.length() > 0) {
            sb.append('(');
            sb.append(accText);
            sb.append(')');
         }

         if (hasLetterSpacing) {
            sb.append("] " + this.formatMptAsPt(generator, letterSpacing) + " ATJ");
         } else {
            sb.append("] TJ");
         }
      } else {
         sb.append('(').append(accText).append(")");
         if (hasLetterSpacing) {
            StringBuffer spb = new StringBuffer();
            spb.append(this.formatMptAsPt(generator, letterSpacing)).append(" 0 ");
            sb.insert(0, spb.toString());
            sb.append(" ashow");
         } else {
            sb.append(" show");
         }
      }

      generator.writeln(sb.toString());
   }

   private void useFont(String key, int size) throws IOException {
      PSResource res = this.documentHandler.getPSResourceForFontKey(key);
      PSGenerator generator = this.getGenerator();
      generator.useFont("/" + res.getName(), (float)size / 1000.0F);
      generator.getResourceTracker().notifyResourceUsageOnPage(res);
   }

   static {
      log = LogFactory.getLog(PSPainter.class);
   }
}
