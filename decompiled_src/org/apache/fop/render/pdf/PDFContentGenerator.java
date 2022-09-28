package org.apache.fop.render.pdf;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.pdf.PDFColor;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFNumber;
import org.apache.fop.pdf.PDFPaintingState;
import org.apache.fop.pdf.PDFResourceContext;
import org.apache.fop.pdf.PDFStream;
import org.apache.fop.pdf.PDFTextUtil;
import org.apache.fop.pdf.PDFXObject;

public class PDFContentGenerator {
   protected static final boolean WRITE_COMMENTS = true;
   private PDFDocument document;
   private OutputStream outputStream;
   private PDFResourceContext resourceContext;
   private PDFStream currentStream;
   protected PDFPaintingState currentState = null;
   protected PDFTextUtil textutil;
   private boolean inMarkedContentSequence;
   private boolean inArtifactMode;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public PDFContentGenerator(PDFDocument document, OutputStream out, PDFResourceContext resourceContext) {
      this.document = document;
      this.outputStream = out;
      this.resourceContext = resourceContext;
      this.currentStream = document.getFactory().makeStream("content", false);
      this.textutil = new PDFTextUtil() {
         protected void write(String code) {
            PDFContentGenerator.this.currentStream.add(code);
         }
      };
      this.currentState = new PDFPaintingState();
   }

   public PDFDocument getDocument() {
      return this.document;
   }

   public OutputStream getOutputStream() {
      return this.outputStream;
   }

   public PDFResourceContext getResourceContext() {
      return this.resourceContext;
   }

   public PDFStream getStream() {
      return this.currentStream;
   }

   public PDFPaintingState getState() {
      return this.currentState;
   }

   public PDFTextUtil getTextUtil() {
      return this.textutil;
   }

   public void flushPDFDoc() throws IOException {
      this.document.output(this.outputStream);
   }

   protected void comment(String text) {
      this.currentStream.add("% " + text + "\n");
   }

   protected void saveGraphicsState() {
      this.endTextObject();
      this.currentState.save();
      this.currentStream.add("q\n");
   }

   protected void saveGraphicsState(String structElemType, int sequenceNum) {
      this.endTextObject();
      this.currentState.save();
      this.beginMarkedContentSequence(structElemType, sequenceNum);
      this.currentStream.add("q\n");
   }

   protected void beginMarkedContentSequence(String structElemType, int mcid) {
      if (!$assertionsDisabled && this.inMarkedContentSequence) {
         throw new AssertionError();
      } else if (!$assertionsDisabled && this.inArtifactMode) {
         throw new AssertionError();
      } else {
         if (structElemType != null) {
            this.currentStream.add(structElemType + " <</MCID " + mcid + ">>\n" + "BDC\n");
         } else {
            this.currentStream.add("/Artifact\nBMC\n");
            this.inArtifactMode = true;
         }

         this.inMarkedContentSequence = true;
      }
   }

   void endMarkedContentSequence() {
      this.currentStream.add("EMC\n");
      this.inMarkedContentSequence = false;
      this.inArtifactMode = false;
   }

   protected void restoreGraphicsState(boolean popState) {
      this.endTextObject();
      this.currentStream.add("Q\n");
      if (popState) {
         this.currentState.restore();
      }

   }

   protected void restoreGraphicsState() {
      this.restoreGraphicsState(true);
   }

   protected void restoreGraphicsStateAccess() {
      this.endTextObject();
      this.currentStream.add("Q\n");
      if (this.inMarkedContentSequence) {
         this.endMarkedContentSequence();
      }

      this.currentState.restore();
   }

   protected void separateTextElements(String structElemType, int mcid) {
      this.textutil.endTextObject();
      this.endMarkedContentSequence();
      this.beginMarkedContentSequence(structElemType, mcid);
      this.textutil.beginTextObject();
   }

   protected void beginTextObject() {
      if (!this.textutil.isInTextObject()) {
         this.textutil.beginTextObject();
      }

   }

   protected void beginTextObject(String structElemType, int mcid) {
      if (!this.textutil.isInTextObject()) {
         this.beginMarkedContentSequence(structElemType, mcid);
         this.textutil.beginTextObject();
      }

   }

   protected void endTextObject() {
      if (this.textutil.isInTextObject()) {
         if (this.inMarkedContentSequence) {
            this.endMarkedContentSequence();
         }

         this.textutil.endTextObject();
      }

   }

   public void concatenate(AffineTransform transform) {
      if (!transform.isIdentity()) {
         this.currentState.concatenate(transform);
         this.currentStream.add(CTMHelper.toPDFString(transform, false) + " cm\n");
      }

   }

   public void clipRect(Rectangle rect) {
      StringBuffer sb = new StringBuffer();
      sb.append(format((float)rect.x / 1000.0F)).append(' ');
      sb.append(format((float)rect.y / 1000.0F)).append(' ');
      sb.append(format((float)rect.width / 1000.0F)).append(' ');
      sb.append(format((float)rect.height / 1000.0F)).append(" re W n\n");
      this.add(sb.toString());
   }

   public void add(String content) {
      this.currentStream.add(content);
   }

   public static final String format(float value) {
      return PDFNumber.doubleOut((double)value);
   }

   public void updateLineWidth(float width) {
      if (this.currentState.setLineWidth(width)) {
         this.currentStream.add(format(width) + " w\n");
      }

   }

   public void updateCharacterSpacing(float value) {
      if (this.getState().setCharacterSpacing(value)) {
         this.currentStream.add(format(value) + " Tc\n");
      }

   }

   public void setColor(Color col, boolean fill, PDFStream stream) {
      if (!$assertionsDisabled && stream == null) {
         throw new AssertionError();
      } else {
         PDFColor color = new PDFColor(this.document, col);
         stream.add(color.getColorSpaceOut(fill));
      }
   }

   public void setColor(Color col, boolean fill) {
      this.setColor(col, fill, this.getStream());
   }

   protected void setColor(Color col, boolean fill, StringBuffer pdf) {
      if (pdf != null) {
         PDFColor color = new PDFColor(this.document, col);
         pdf.append(color.getColorSpaceOut(fill));
      } else {
         this.setColor(col, fill, this.currentStream);
      }

   }

   public void updateColor(Color col, boolean fill, StringBuffer pdf) {
      if (col != null) {
         boolean update = false;
         if (fill) {
            update = this.getState().setBackColor(col);
         } else {
            update = this.getState().setColor(col);
         }

         if (update) {
            this.setColor(col, fill, pdf);
         }

      }
   }

   public void placeImage(float x, float y, float w, float h, PDFXObject xobj) {
      this.saveGraphicsState();
      this.add(format(w) + " 0 0 " + format(-h) + " " + format(x) + " " + format(y + h) + " cm\n" + xobj.getName() + " Do\n");
      this.restoreGraphicsState();
   }

   public void placeImage(float x, float y, float w, float h, PDFXObject xobj, String structElemType, int mcid) {
      this.saveGraphicsState(structElemType, mcid);
      this.add(format(w) + " 0 0 " + format(-h) + " " + format(x) + " " + format(y + h) + " cm\n" + xobj.getName() + " Do\n");
      this.restoreGraphicsStateAccess();
   }

   static {
      $assertionsDisabled = !PDFContentGenerator.class.desiredAssertionStatus();
   }
}
