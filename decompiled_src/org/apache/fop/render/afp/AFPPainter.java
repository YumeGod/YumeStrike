package org.apache.fop.render.afp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Map;
import org.apache.fop.afp.AFPBorderPainter;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPUnitConverter;
import org.apache.fop.afp.AbstractAFPPainter;
import org.apache.fop.afp.BorderPaintingInfo;
import org.apache.fop.afp.DataStream;
import org.apache.fop.afp.RectanglePaintingInfo;
import org.apache.fop.afp.fonts.AFPFont;
import org.apache.fop.afp.fonts.AFPFontAttributes;
import org.apache.fop.afp.fonts.AFPPageFonts;
import org.apache.fop.afp.fonts.CharacterSet;
import org.apache.fop.afp.modca.AbstractPageObject;
import org.apache.fop.afp.modca.PresentationTextObject;
import org.apache.fop.afp.ptoca.PtocaBuilder;
import org.apache.fop.afp.ptoca.PtocaProducer;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.render.intermediate.AbstractIFPainter;
import org.apache.fop.render.intermediate.BorderPainter;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFState;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.RuleStyle;
import org.apache.fop.util.CharUtilities;
import org.apache.xmlgraphics.image.loader.ImageProcessingHints;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.w3c.dom.Document;

public class AFPPainter extends AbstractIFPainter {
   private static final int X = 0;
   private static final int Y = 1;
   private AFPDocumentHandler documentHandler;
   private AFPBorderPainterAdapter borderPainter;
   private AbstractAFPPainter rectanglePainter;
   private final AFPUnitConverter unitConv;

   public AFPPainter(AFPDocumentHandler documentHandler) {
      this.documentHandler = documentHandler;
      this.state = IFState.create();
      this.borderPainter = new AFPBorderPainterAdapter(new AFPBorderPainter(this.getPaintingState(), this.getDataStream()));
      this.rectanglePainter = documentHandler.createRectanglePainter();
      this.unitConv = this.getPaintingState().getUnitConverter();
   }

   protected IFContext getContext() {
      return this.documentHandler.getContext();
   }

   FontInfo getFontInfo() {
      return this.documentHandler.getFontInfo();
   }

   AFPPaintingState getPaintingState() {
      return this.documentHandler.getPaintingState();
   }

   DataStream getDataStream() {
      return this.documentHandler.getDataStream();
   }

   public void startViewport(AffineTransform transform, Dimension size, Rectangle clipRect) throws IFException {
      try {
         this.saveGraphicsState();
         this.concatenateTransformationMatrix(transform);
      } catch (IOException var5) {
         throw new IFException("I/O error in startViewport()", var5);
      }
   }

   public void endViewport() throws IFException {
      try {
         this.restoreGraphicsState();
      } catch (IOException var2) {
         throw new IFException("I/O error in endViewport()", var2);
      }
   }

   private void concatenateTransformationMatrix(AffineTransform at) {
      if (!at.isIdentity()) {
         this.getPaintingState().concatenate(at);
      }

   }

   public void startGroup(AffineTransform transform) throws IFException {
      try {
         this.saveGraphicsState();
         this.concatenateTransformationMatrix(transform);
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
      AFPRenderingContext psContext = new AFPRenderingContext(this.getUserAgent(), this.documentHandler.getResourceManager(), this.getPaintingState(), this.getFontInfo(), this.getContext().getForeignAttributes());
      return psContext;
   }

   public void drawImage(String uri, Rectangle rect) throws IFException {
      String name = this.documentHandler.getPageSegmentNameFor(uri);
      if (name != null) {
         float[] srcPts = new float[]{(float)rect.x, (float)rect.y};
         int[] coords = this.unitConv.mpts2units(srcPts);
         int width = Math.round(this.unitConv.mpt2units((float)rect.width));
         int height = Math.round(this.unitConv.mpt2units((float)rect.height));
         this.getDataStream().createIncludePageSegment(name, coords[0], coords[1], width, height);
      } else {
         this.drawImageUsingURI(uri, rect);
      }

   }

   public void drawImage(Document doc, Rectangle rect) throws IFException {
      this.drawImageUsingDocument(doc, rect);
   }

   public void clipRect(Rectangle rect) throws IFException {
   }

   private float toPoint(int mpt) {
      return (float)mpt / 1000.0F;
   }

   public void fillRect(Rectangle rect, Paint fill) throws IFException {
      if (fill != null) {
         if (rect.width != 0 && rect.height != 0) {
            if (!(fill instanceof Color)) {
               throw new UnsupportedOperationException("Non-Color paints NYI");
            }

            this.getPaintingState().setColor((Color)fill);
            RectanglePaintingInfo rectanglePaintInfo = new RectanglePaintingInfo(this.toPoint(rect.x), this.toPoint(rect.y), this.toPoint(rect.width), this.toPoint(rect.height));

            try {
               this.rectanglePainter.paint(rectanglePaintInfo);
            } catch (IOException var5) {
               throw new IFException("IO error while painting rectangle", var5);
            }
         }

      }
   }

   public void drawBorderRect(Rectangle rect, BorderProps before, BorderProps after, BorderProps start, BorderProps end) throws IFException {
      if (before != null || after != null || start != null || end != null) {
         try {
            this.borderPainter.drawBorders(rect, before, after, start, end);
         } catch (IOException var7) {
            throw new IFException("IO error while painting borders", var7);
         }
      }

   }

   public void drawLine(Point start, Point end, int width, Color color, RuleStyle style) throws IFException {
      try {
         this.borderPainter.drawLine(start, end, width, color, style);
      } catch (IOException var7) {
         throw new IFException("I/O error in drawLine()", var7);
      }
   }

   public void drawText(int x, int y, final int letterSpacing, final int wordSpacing, final int[] dx, final String text) throws IFException {
      int fontSize = this.state.getFontSize();
      this.getPaintingState().setFontSize(fontSize);
      FontTriplet triplet = new FontTriplet(this.state.getFontFamily(), this.state.getFontStyle(), this.state.getFontWeight());
      String fontKey = this.getFontInfo().getInternalFontKey(triplet);
      if (fontKey == null) {
         triplet = new FontTriplet("any", "normal", 400);
         fontKey = this.getFontInfo().getInternalFontKey(triplet);
      }

      Map fontMetricMap = this.documentHandler.getFontInfo().getFonts();
      AFPFont afpFont = (AFPFont)fontMetricMap.get(fontKey);
      final Font font = this.getFontInfo().getFontInstance(triplet, fontSize);
      AFPPageFonts pageFonts = this.getPaintingState().getPageFonts();
      AFPFontAttributes fontAttributes = pageFonts.registerFont(fontKey, afpFont, fontSize);
      final int fontReference = fontAttributes.getFontReference();
      final int[] coords = this.unitConv.mpts2units(new float[]{(float)x, (float)y});
      final CharacterSet charSet = afpFont.getCharacterSet(fontSize);
      if (afpFont.isEmbeddable()) {
         try {
            this.documentHandler.getResourceManager().embedFont(afpFont, charSet);
         } catch (IOException var22) {
            throw new IFException("Error while embedding font resources", var22);
         }
      }

      AbstractPageObject page = this.getDataStream().getCurrentPage();
      PresentationTextObject pto = page.getPresentationTextObject();

      try {
         pto.createControlSequences(new PtocaProducer() {
            public void produce(PtocaBuilder builder) throws IOException {
               Point p = AFPPainter.this.getPaintingState().getPoint(coords[0], coords[1]);
               builder.setTextOrientation(AFPPainter.this.getPaintingState().getRotation());
               builder.absoluteMoveBaseline(p.y);
               builder.absoluteMoveInline(p.x);
               builder.setExtendedTextColor(AFPPainter.this.state.getTextColor());
               builder.setCodedFont((byte)fontReference);
               int l = text.length();
               int dxl = dx != null ? dx.length : 0;
               StringBuffer sb = new StringBuffer();
               if (dxl > 0 && dx[0] != 0) {
                  int dxu = Math.round(AFPPainter.this.unitConv.mpt2units((float)dx[0]));
                  builder.relativeMoveInline(-dxu);
               }

               boolean usePTOCAWordSpacing = true;
               int interCharacterAdjustment = 0;
               if (letterSpacing != 0) {
                  interCharacterAdjustment = Math.round(AFPPainter.this.unitConv.mpt2units((float)letterSpacing));
               }

               builder.setInterCharacterAdjustment(interCharacterAdjustment);
               int spaceWidth = font.getCharWidth(' ');
               int fixedSpaceCharacterIncrement = Math.round(AFPPainter.this.unitConv.mpt2units((float)(spaceWidth + letterSpacing)));
               int varSpaceCharacterIncrement = fixedSpaceCharacterIncrement;
               if (wordSpacing != 0) {
                  varSpaceCharacterIncrement = Math.round(AFPPainter.this.unitConv.mpt2units((float)(spaceWidth + wordSpacing + letterSpacing)));
               }

               builder.setVariableSpaceCharacterIncrement(varSpaceCharacterIncrement);
               boolean fixedSpaceMode = false;

               for(int i = 0; i < l; ++i) {
                  char orgChar = text.charAt(i);
                  float glyphAdjust = 0.0F;
                  int increment;
                  if (CharUtilities.isFixedWidthSpace(orgChar)) {
                     this.flushText(builder, sb, charSet);
                     builder.setVariableSpaceCharacterIncrement(fixedSpaceCharacterIncrement);
                     fixedSpaceMode = true;
                     sb.append(' ');
                     increment = font.getCharWidth(orgChar);
                     glyphAdjust += (float)(increment - spaceWidth);
                  } else {
                     if (fixedSpaceMode) {
                        this.flushText(builder, sb, charSet);
                        builder.setVariableSpaceCharacterIncrement(varSpaceCharacterIncrement);
                        fixedSpaceMode = false;
                     }

                     char ch;
                     if (orgChar == 160) {
                        ch = ' ';
                     } else {
                        ch = orgChar;
                     }

                     sb.append(ch);
                  }

                  if (i < dxl - 1) {
                     glyphAdjust += (float)dx[i + 1];
                  }

                  if (glyphAdjust != 0.0F) {
                     this.flushText(builder, sb, charSet);
                     increment = Math.round(AFPPainter.this.unitConv.mpt2units(glyphAdjust));
                     builder.relativeMoveInline(increment);
                  }
               }

               this.flushText(builder, sb, charSet);
            }

            private void flushText(PtocaBuilder builder, StringBuffer sb, CharacterSet charSetx) throws IOException {
               if (sb.length() > 0) {
                  builder.addTransparentData(charSetx.encodeChars(sb));
                  sb.setLength(0);
               }

            }
         });
      } catch (IOException var21) {
         throw new IFException("I/O error in drawText()", var21);
      }
   }

   protected void saveGraphicsState() throws IOException {
      this.getPaintingState().save();
   }

   protected void restoreGraphicsState() throws IOException {
      this.getPaintingState().restore();
   }

   private static class AFPBorderPainterAdapter extends BorderPainter {
      private AFPBorderPainter delegate;

      public AFPBorderPainterAdapter(AFPBorderPainter borderPainter) {
         this.delegate = borderPainter;
      }

      protected void clip() throws IOException {
      }

      protected void closePath() throws IOException {
      }

      protected void moveTo(int x, int y) throws IOException {
      }

      protected void lineTo(int x, int y) throws IOException {
      }

      protected void saveGraphicsState() throws IOException {
      }

      protected void restoreGraphicsState() throws IOException {
      }

      private float toPoints(int mpt) {
         return (float)mpt / 1000.0F;
      }

      protected void drawBorderLine(int x1, int y1, int x2, int y2, boolean horz, boolean startOrBefore, int style, Color color) throws IOException {
         BorderPaintingInfo borderPaintInfo = new BorderPaintingInfo(this.toPoints(x1), this.toPoints(y1), this.toPoints(x2), this.toPoints(y2), horz, style, color);
         this.delegate.paint(borderPaintInfo);
      }

      public void drawLine(Point start, Point end, int width, Color color, RuleStyle style) throws IOException {
         if (start.y != end.y) {
            throw new UnsupportedOperationException("Can only deal with horizontal lines right now");
         } else {
            int halfWidth = width / 2;
            this.drawBorderLine(start.x, start.y - halfWidth, end.x, start.y + halfWidth, true, true, style.getEnumValue(), color);
         }
      }
   }
}
