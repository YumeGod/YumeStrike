package org.apache.fop.svg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import org.apache.fop.Version;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontSetup;
import org.apache.fop.pdf.PDFAnnotList;
import org.apache.fop.pdf.PDFColor;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFNumber;
import org.apache.fop.pdf.PDFPage;
import org.apache.fop.pdf.PDFPaintingState;
import org.apache.fop.pdf.PDFResources;
import org.apache.fop.pdf.PDFStream;

public class PDFDocumentGraphics2D extends PDFGraphics2D {
   private final PDFContext pdfContext;
   private int width;
   private int height;
   private float svgWidth;
   private float svgHeight;
   public static final int NORMAL_PDF_RESOLUTION = 72;
   public static final int DEFAULT_NATIVE_DPI = 300;
   private float deviceDPI;
   protected Shape initialClip;
   protected AffineTransform initialTransform;

   public PDFDocumentGraphics2D(boolean textAsShapes) {
      super(textAsShapes);
      this.deviceDPI = 300.0F;
      this.pdfDoc = new PDFDocument("Apache FOP Version " + Version.getVersion() + ": PDFDocumentGraphics2D");
      this.pdfContext = new PDFContext();
   }

   public PDFDocumentGraphics2D(boolean textAsShapes, OutputStream stream, int width, int height) throws IOException {
      this(textAsShapes);
      this.setupDocument(stream, width, height);
   }

   public PDFDocumentGraphics2D() {
      this(false);
   }

   public void setupDocument(OutputStream stream, int width, int height) throws IOException {
      this.width = width;
      this.height = height;
      this.pdfDoc.outputHeader(stream);
      this.setOutputStream(stream);
   }

   public void setupDefaultFontInfo() {
      if (this.fontInfo == null) {
         FontInfo fontInfo = new FontInfo();
         FontSetup.setup(fontInfo);
         this.setFontInfo(fontInfo);
      }

   }

   public void setDeviceDPI(float deviceDPI) {
      this.deviceDPI = deviceDPI;
   }

   public float getDeviceDPI() {
      return this.deviceDPI;
   }

   public void setFontInfo(FontInfo fontInfo) {
      this.fontInfo = fontInfo;
   }

   public FontInfo getFontInfo() {
      return this.fontInfo;
   }

   public PDFDocument getPDFDocument() {
      return this.pdfDoc;
   }

   public PDFContext getPDFContext() {
      return this.pdfContext;
   }

   public void setSVGDimension(float w, float h) {
      this.svgWidth = w;
      this.svgHeight = h;
   }

   public void setBackgroundColor(Color col) {
      PDFColor currentColour = new PDFColor(col.getRed(), col.getGreen(), col.getBlue());
      this.currentStream.write("q\n");
      this.currentStream.write(currentColour.getColorSpaceOut(true));
      this.currentStream.write("0 0 " + this.width + " " + this.height + " re\n");
      this.currentStream.write("f\n");
      this.currentStream.write("Q\n");
   }

   public void nextPage() {
      this.closePage();
   }

   protected void closePage() {
      if (this.pdfContext.isPagePending()) {
         PDFStream pdfStream = this.pdfDoc.getFactory().makeStream("content", false);
         pdfStream.add(this.getString());
         this.currentStream = null;
         this.pdfDoc.registerObject(pdfStream);
         this.pdfContext.getCurrentPage().setContents(pdfStream);
         PDFAnnotList annots = this.pdfContext.getCurrentPage().getAnnotations();
         if (annots != null) {
            this.pdfDoc.addObject(annots);
         }

         this.pdfDoc.addObject(this.pdfContext.getCurrentPage());
         this.pdfContext.clearCurrentPage();
      }
   }

   protected void preparePainting() {
      if (!this.pdfContext.isPagePending()) {
         if (!this.textAsShapes && this.getFontInfo() == null) {
            this.setupDefaultFontInfo();
         }

         try {
            this.startPage();
         } catch (IOException var2) {
            this.handleIOException(var2);
         }

      }
   }

   protected void startPage() throws IOException {
      if (this.pdfContext.isPagePending()) {
         throw new IllegalStateException("Close page first before starting another");
      } else {
         this.paintingState = new PDFPaintingState();
         if (this.initialTransform == null) {
            this.initialTransform = this.getTransform();
            this.initialClip = this.getClip();
         } else {
            this.setTransform(this.initialTransform);
            this.setClip(this.initialClip);
         }

         this.currentFontName = "";
         this.currentFontSize = 0.0F;
         if (this.currentStream == null) {
            this.currentStream = new StringWriter();
         }

         PDFResources pdfResources = this.pdfDoc.getResources();
         PDFPage page = this.pdfDoc.getFactory().makePage(pdfResources, this.width, this.height);
         this.resourceContext = page;
         this.pdfContext.setCurrentPage(page);
         this.pageRef = page.referencePDF();
         AffineTransform at = new AffineTransform(1.0, 0.0, 0.0, -1.0, 0.0, (double)this.height);
         this.currentStream.write("1 0 0 -1 0 " + this.height + " cm\n");
         double s;
         if (this.svgWidth != 0.0F) {
            s = (double)((float)this.width / this.svgWidth);
            double scaleY = (double)((float)this.height / this.svgHeight);
            at.scale(s, scaleY);
            this.currentStream.write("" + PDFNumber.doubleOut(s) + " 0 0 " + PDFNumber.doubleOut(scaleY) + " 0 0 cm\n");
         }

         if (this.deviceDPI != 72.0F) {
            s = (double)(72.0F / this.deviceDPI);
            at.scale(s, s);
            this.currentStream.write("" + PDFNumber.doubleOut(s) + " 0 0 " + PDFNumber.doubleOut(s) + " 0 0 cm\n");
            this.scale(1.0 / s, 1.0 / s);
         }

         this.paintingState.concatenate(at);
         this.pdfContext.increasePageCount();
      }
   }

   public void finish() throws IOException {
      this.closePage();
      if (this.fontInfo != null) {
         this.pdfDoc.getResources().addFonts(this.pdfDoc, this.fontInfo);
      }

      this.pdfDoc.output(this.outputStream);
      this.pdfDoc.outputTrailer(this.outputStream);
      this.outputStream.flush();
   }

   public PDFDocumentGraphics2D(PDFDocumentGraphics2D g) {
      super(g);
      this.deviceDPI = 300.0F;
      this.pdfContext = g.pdfContext;
      this.width = g.width;
      this.height = g.height;
      this.svgWidth = g.svgWidth;
      this.svgHeight = g.svgHeight;
   }

   public Graphics create() {
      return new PDFDocumentGraphics2D(this);
   }

   public void drawString(String s, float x, float y) {
      if (super.textAsShapes) {
         Font font = super.getFont();
         FontRenderContext frc = super.getFontRenderContext();
         GlyphVector gv = font.createGlyphVector(frc, s);
         Shape glyphOutline = gv.getOutline(x, y);
         super.fill(glyphOutline);
      } else {
         super.drawString(s, x, y);
      }

   }
}
