package org.apache.fop.render.pdf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.fonts.LazyFont;
import org.apache.fop.fonts.SingleByteFont;
import org.apache.fop.fonts.Typeface;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFNumber;
import org.apache.fop.pdf.PDFTextUtil;
import org.apache.fop.pdf.PDFXObject;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.render.intermediate.AbstractIFPainter;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFState;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.RuleStyle;
import org.apache.fop.util.CharUtilities;
import org.w3c.dom.Document;

public class PDFPainter extends AbstractIFPainter {
   private final PDFDocumentHandler documentHandler;
   protected PDFContentGenerator generator;
   private final PDFBorderPainter borderPainter;
   private boolean accessEnabled;
   private PDFLogicalStructureHandler.MarkedContentInfo imageMCI;
   private PDFLogicalStructureHandler logicalStructureHandler;

   public PDFPainter(PDFDocumentHandler documentHandler, PDFLogicalStructureHandler logicalStructureHandler) {
      this.documentHandler = documentHandler;
      this.logicalStructureHandler = logicalStructureHandler;
      this.generator = documentHandler.generator;
      this.borderPainter = new PDFBorderPainter(this.generator);
      this.state = IFState.create();
      this.accessEnabled = this.getUserAgent().isAccessibilityEnabled();
   }

   protected IFContext getContext() {
      return this.documentHandler.getContext();
   }

   PDFRenderingUtil getPDFUtil() {
      return this.documentHandler.pdfUtil;
   }

   PDFDocument getPDFDoc() {
      return this.documentHandler.pdfDoc;
   }

   FontInfo getFontInfo() {
      return this.documentHandler.getFontInfo();
   }

   public void startViewport(AffineTransform transform, Dimension size, Rectangle clipRect) throws IFException {
      this.generator.saveGraphicsState();
      this.generator.concatenate(toPoints(transform));
      if (clipRect != null) {
         this.clipRect(clipRect);
      }

   }

   public void endViewport() throws IFException {
      this.generator.restoreGraphicsState();
   }

   public void startGroup(AffineTransform transform) throws IFException {
      this.generator.saveGraphicsState();
      this.generator.concatenate(toPoints(transform));
   }

   public void endGroup() throws IFException {
      this.generator.restoreGraphicsState();
   }

   public void drawImage(String uri, Rectangle rect) throws IFException {
      PDFXObject xobject = this.getPDFDoc().getXObject(uri);
      String ptr;
      if (xobject != null) {
         if (this.accessEnabled) {
            ptr = this.getContext().getStructurePointer();
            this.prepareImageMCID(ptr);
            this.placeImageAccess(rect, xobject);
         } else {
            this.placeImage(rect, xobject);
         }
      } else {
         if (this.accessEnabled) {
            ptr = this.getContext().getStructurePointer();
            this.prepareImageMCID(ptr);
         }

         this.drawImageUsingURI(uri, rect);
         this.flushPDFDoc();
      }

   }

   private void prepareImageMCID(String ptr) {
      this.imageMCI = this.logicalStructureHandler.addImageContentItem(ptr);
   }

   protected RenderingContext createRenderingContext() {
      PDFRenderingContext pdfContext = new PDFRenderingContext(this.getUserAgent(), this.generator, this.documentHandler.currentPage, this.getFontInfo());
      pdfContext.setMarkedContentInfo(this.imageMCI);
      return pdfContext;
   }

   private void placeImage(Rectangle rect, PDFXObject xobj) {
      this.generator.saveGraphicsState();
      this.generator.add(format(rect.width) + " 0 0 " + format(-rect.height) + " " + format(rect.x) + " " + format(rect.y + rect.height) + " cm " + xobj.getName() + " Do\n");
      this.generator.restoreGraphicsState();
   }

   private void placeImageAccess(Rectangle rect, PDFXObject xobj) {
      this.generator.saveGraphicsState(this.imageMCI.tag, this.imageMCI.mcid);
      this.generator.add(format(rect.width) + " 0 0 " + format(-rect.height) + " " + format(rect.x) + " " + format(rect.y + rect.height) + " cm " + xobj.getName() + " Do\n");
      this.generator.restoreGraphicsStateAccess();
   }

   public void drawImage(Document doc, Rectangle rect) throws IFException {
      if (this.accessEnabled) {
         String ptr = this.getContext().getStructurePointer();
         this.prepareImageMCID(ptr);
      }

      this.drawImageUsingDocument(doc, rect);
      this.flushPDFDoc();
   }

   private void flushPDFDoc() throws IFException {
      try {
         this.generator.flushPDFDoc();
      } catch (IOException var2) {
         throw new IFException("I/O error flushing the PDF document", var2);
      }
   }

   protected static String format(int value) {
      return PDFNumber.doubleOut((double)((float)value / 1000.0F));
   }

   public void clipRect(Rectangle rect) throws IFException {
      this.generator.endTextObject();
      this.generator.clipRect(rect);
   }

   public void fillRect(Rectangle rect, Paint fill) throws IFException {
      if (fill != null) {
         if (rect.width != 0 && rect.height != 0) {
            this.generator.endTextObject();
            if (fill != null) {
               if (!(fill instanceof Color)) {
                  throw new UnsupportedOperationException("Non-Color paints NYI");
               }

               this.generator.updateColor((Color)fill, true, (StringBuffer)null);
            }

            StringBuffer sb = new StringBuffer();
            sb.append(format(rect.x)).append(' ');
            sb.append(format(rect.y)).append(' ');
            sb.append(format(rect.width)).append(' ');
            sb.append(format(rect.height)).append(" re");
            if (fill != null) {
               sb.append(" f");
            }

            sb.append('\n');
            this.generator.add(sb.toString());
         }

      }
   }

   public void drawBorderRect(Rectangle rect, BorderProps before, BorderProps after, BorderProps start, BorderProps end) throws IFException {
      if (before != null || after != null || start != null || end != null) {
         this.generator.endTextObject();

         try {
            this.borderPainter.drawBorders(rect, before, after, start, end);
         } catch (IOException var7) {
            throw new IFException("I/O error while drawing borders", var7);
         }
      }

   }

   public void drawLine(Point start, Point end, int width, Color color, RuleStyle style) throws IFException {
      this.generator.endTextObject();
      this.borderPainter.drawLine(start, end, width, color, style);
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

   public void drawText(int x, int y, int letterSpacing, int wordSpacing, int[] dx, String text) throws IFException {
      if (this.accessEnabled) {
         String ptr = this.getContext().getStructurePointer();
         PDFLogicalStructureHandler.MarkedContentInfo mci = this.logicalStructureHandler.addTextContentItem(ptr);
         if (this.generator.getTextUtil().isInTextObject()) {
            this.generator.separateTextElements(mci.tag, mci.mcid);
         }

         this.generator.updateColor(this.state.getTextColor(), true, (StringBuffer)null);
         this.generator.beginTextObject(mci.tag, mci.mcid);
      } else {
         this.generator.updateColor(this.state.getTextColor(), true, (StringBuffer)null);
         this.generator.beginTextObject();
      }

      FontTriplet triplet = new FontTriplet(this.state.getFontFamily(), this.state.getFontStyle(), this.state.getFontWeight());
      String fontKey = this.getFontInfo().getInternalFontKey(triplet);
      int sizeMillipoints = this.state.getFontSize();
      float fontSize = (float)sizeMillipoints / 1000.0F;
      Typeface tf = this.getTypeface(fontKey);
      SingleByteFont singleByteFont = null;
      if (tf instanceof SingleByteFont) {
         singleByteFont = (SingleByteFont)tf;
      }

      Font font = this.getFontInfo().getFontInstance(triplet, sizeMillipoints);
      String fontName = font.getFontName();
      PDFTextUtil textutil = this.generator.getTextUtil();
      textutil.updateTf(fontKey, (double)fontSize, tf.isMultiByte());
      this.generator.updateCharacterSpacing((float)letterSpacing / 1000.0F);
      textutil.writeTextMatrix(new AffineTransform(1.0F, 0.0F, 0.0F, -1.0F, (float)x / 1000.0F, (float)y / 1000.0F));
      int l = text.length();
      int dxl = dx != null ? dx.length : 0;
      if (dx != null && dxl > 0 && dx[0] != 0) {
         textutil.adjustGlyphTJ((double)((float)(-dx[0]) / fontSize));
      }

      for(int i = 0; i < l; ++i) {
         char orgChar = text.charAt(i);
         float glyphAdjust = 0.0F;
         char ch;
         int encoding;
         if (font.hasChar(orgChar)) {
            ch = font.mapChar(orgChar);
            if (singleByteFont != null && singleByteFont.hasAdditionalEncodings()) {
               encoding = ch / 256;
               if (encoding == 0) {
                  textutil.updateTf(fontName, (double)fontSize, tf.isMultiByte());
               } else {
                  textutil.updateTf(fontName + "_" + Integer.toString(encoding), (double)fontSize, tf.isMultiByte());
                  ch = (char)(ch % 256);
               }
            }

            if (wordSpacing != 0 && CharUtilities.isAdjustableSpace(orgChar)) {
               glyphAdjust += (float)wordSpacing;
            }
         } else if (CharUtilities.isFixedWidthSpace(orgChar)) {
            ch = font.mapChar(' ');
            encoding = font.getCharWidth(ch) - font.getCharWidth(orgChar);
            glyphAdjust = (float)(-encoding);
         } else {
            ch = font.mapChar(orgChar);
            if (wordSpacing != 0 && CharUtilities.isAdjustableSpace(orgChar)) {
               glyphAdjust += (float)wordSpacing;
            }
         }

         textutil.writeTJMappedChar(ch);
         if (dx != null && i < dxl - 1) {
            glyphAdjust += (float)dx[i + 1];
         }

         if (glyphAdjust != 0.0F) {
            textutil.adjustGlyphTJ((double)(-glyphAdjust / fontSize));
         }
      }

      textutil.writeTJ();
   }
}
