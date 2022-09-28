package org.apache.fop.render.pcl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.render.ImageHandlerUtil;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.render.intermediate.AbstractIFPainter;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFState;
import org.apache.fop.render.java2d.FontMetricsMapper;
import org.apache.fop.render.java2d.Java2DPainter;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.RuleStyle;
import org.apache.fop.util.CharUtilities;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageProcessingHints;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.impl.ImageGraphics2D;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.w3c.dom.Document;

public class PCLPainter extends AbstractIFPainter implements PCLConstants {
   private static Log log;
   private static final boolean DEBUG = false;
   private PCLDocumentHandler parent;
   private PCLGenerator gen;
   private PCLPageDefinition currentPageDefinition;
   private int currentPrintDirection = 0;
   private Stack graphicContextStack = new Stack();
   private GraphicContext graphicContext = new GraphicContext();
   private static final double SAFETY_MARGIN_FACTOR = 0.05;

   public PCLPainter(PCLDocumentHandler parent, PCLPageDefinition pageDefinition) {
      this.parent = parent;
      this.gen = parent.getPCLGenerator();
      this.state = IFState.create();
      this.currentPageDefinition = pageDefinition;
   }

   public IFContext getContext() {
      return this.parent.getContext();
   }

   PCLRenderingUtil getPCLUtil() {
      return this.parent.getPCLUtil();
   }

   protected int getResolution() {
      int resolution = Math.round(this.getUserAgent().getTargetResolution());
      return resolution <= 300 ? 300 : 600;
   }

   private boolean isSpeedOptimized() {
      return this.getPCLUtil().getRenderingMode() == PCLRenderingMode.SPEED;
   }

   public void startViewport(AffineTransform transform, Dimension size, Rectangle clipRect) throws IFException {
      this.saveGraphicsState();

      try {
         this.concatenateTransformationMatrix(transform);
      } catch (IOException var5) {
         throw new IFException("I/O error in startViewport()", var5);
      }
   }

   public void endViewport() throws IFException {
      this.restoreGraphicsState();
   }

   public void startGroup(AffineTransform transform) throws IFException {
      this.saveGraphicsState();

      try {
         this.concatenateTransformationMatrix(transform);
      } catch (IOException var3) {
         throw new IFException("I/O error in startGroup()", var3);
      }
   }

   public void endGroup() throws IFException {
      this.restoreGraphicsState();
   }

   public void drawImage(String uri, Rectangle rect) throws IFException {
      this.drawImageUsingURI(uri, rect);
   }

   protected RenderingContext createRenderingContext() {
      PCLRenderingContext pdfContext = new PCLRenderingContext(this.getUserAgent(), this.gen, this.getPCLUtil()) {
         public Point2D transformedPoint(int x, int y) {
            return PCLPainter.this.transformedPoint(x, y);
         }

         public GraphicContext getGraphicContext() {
            return PCLPainter.this.graphicContext;
         }
      };
      return pdfContext;
   }

   public void drawImage(Document doc, Rectangle rect) throws IFException {
      this.drawImageUsingDocument(doc, rect);
   }

   public void clipRect(Rectangle rect) throws IFException {
   }

   public void fillRect(Rectangle rect, Paint fill) throws IFException {
      if (fill != null) {
         if (rect.width != 0 && rect.height != 0) {
            Color fillColor = null;
            if (fill != null) {
               if (!(fill instanceof Color)) {
                  throw new UnsupportedOperationException("Non-Color paints NYI");
               }

               fillColor = (Color)fill;

               try {
                  this.setCursorPos(rect.x, rect.y);
                  this.gen.fillRect(rect.width, rect.height, fillColor);
               } catch (IOException var5) {
                  throw new IFException("I/O error in fillRect()", var5);
               }
            }
         }

      }
   }

   public void drawBorderRect(final Rectangle rect, final BorderProps before, final BorderProps after, final BorderProps start, final BorderProps end) throws IFException {
      if (this.isSpeedOptimized()) {
         super.drawBorderRect(rect, before, after, start, end);
      } else {
         if (before != null || after != null || start != null || end != null) {
            final Dimension dim = rect.getSize();
            Graphics2DImagePainter painter = new Graphics2DImagePainter() {
               public void paint(Graphics2D g2d, Rectangle2D area) {
                  g2d.translate(-rect.x, -rect.y);
                  Java2DPainter painter = new Java2DPainter(g2d, PCLPainter.this.getContext(), PCLPainter.this.parent.getFontInfo(), PCLPainter.this.state);

                  try {
                     painter.drawBorderRect(rect, before, after, start, end);
                  } catch (IFException var5) {
                     throw new RuntimeException("Unexpected error while painting borders", var5);
                  }
               }

               public Dimension getImageSize() {
                  return dim.getSize();
               }
            };
            this.paintMarksAsBitmap(painter, rect);
         }

      }
   }

   public void drawLine(final Point start, final Point end, final int width, final Color color, final RuleStyle style) throws IFException {
      if (this.isSpeedOptimized()) {
         super.drawLine(start, end, width, color, style);
      } else {
         final Rectangle boundingBox = this.getLineBoundingBox(start, end, width);
         final Dimension dim = boundingBox.getSize();
         Graphics2DImagePainter painter = new Graphics2DImagePainter() {
            public void paint(Graphics2D g2d, Rectangle2D area) {
               g2d.translate(-boundingBox.x, -boundingBox.y);
               Java2DPainter painter = new Java2DPainter(g2d, PCLPainter.this.getContext(), PCLPainter.this.parent.getFontInfo(), PCLPainter.this.state);

               try {
                  painter.drawLine(start, end, width, color, style);
               } catch (IFException var5) {
                  throw new RuntimeException("Unexpected error while painting a line", var5);
               }
            }

            public Dimension getImageSize() {
               return dim.getSize();
            }
         };
         this.paintMarksAsBitmap(painter, boundingBox);
      }
   }

   private void paintMarksAsBitmap(Graphics2DImagePainter painter, Rectangle boundingBox) throws IFException {
      ImageInfo info = new ImageInfo((String)null, (String)null);
      ImageSize size = new ImageSize();
      size.setSizeInMillipoints(boundingBox.width, boundingBox.height);
      info.setSize(size);
      ImageGraphics2D img = new ImageGraphics2D(info, painter);
      Map hints = new HashMap();
      if (this.isSpeedOptimized()) {
         hints.put(ImageProcessingHints.BITMAP_TYPE_INTENT, "mono");
      } else {
         hints.put(ImageProcessingHints.BITMAP_TYPE_INTENT, "gray");
      }

      hints.put(ImageHandlerUtil.CONVERSION_MODE, "bitmap");
      PCLRenderingContext context = (PCLRenderingContext)this.createRenderingContext();
      context.setSourceTransparencyEnabled(true);

      try {
         this.drawImage(img, boundingBox, context, true, hints);
      } catch (IOException var9) {
         throw new IFException("I/O error while painting marks using a bitmap", var9);
      } catch (ImageException var10) {
         throw new IFException("Error while painting marks using a bitmap", var10);
      }
   }

   public void drawText(int x, int y, int letterSpacing, int wordSpacing, int[] dx, String text) throws IFException {
      try {
         FontTriplet triplet = new FontTriplet(this.state.getFontFamily(), this.state.getFontStyle(), this.state.getFontWeight());
         String fontKey = this.parent.getFontInfo().getInternalFontKey(triplet);
         boolean pclFont = this.getPCLUtil().isAllTextAsBitmaps() ? false : HardcodedFonts.setFont(this.gen, fontKey, this.state.getFontSize(), text);
         if (pclFont) {
            this.drawTextNative(x, y, letterSpacing, wordSpacing, dx, text, triplet);
         } else {
            this.drawTextAsBitmap(x, y, letterSpacing, wordSpacing, dx, text, triplet);
         }

      } catch (IOException var10) {
         throw new IFException("I/O error in drawText()", var10);
      }
   }

   private void drawTextNative(int x, int y, int letterSpacing, int wordSpacing, int[] dx, String text, FontTriplet triplet) throws IOException {
      Color textColor = this.state.getTextColor();
      if (textColor != null) {
         this.gen.setTransparencyMode(true, false);
         this.gen.selectGrayscale(textColor);
      }

      this.gen.setTransparencyMode(true, true);
      this.setCursorPos(x, y);
      float fontSize = (float)this.state.getFontSize() / 1000.0F;
      Font font = this.parent.getFontInfo().getFontInstance(triplet, this.state.getFontSize());
      int l = text.length();
      int dxl = dx != null ? dx.length : 0;
      StringBuffer sb = new StringBuffer(Math.max(16, l));
      if (dx != null && dxl > 0 && dx[0] != 0) {
         sb.append("\u001b&a+").append(this.gen.formatDouble2((double)dx[0] / 100.0)).append('H');
      }

      for(int i = 0; i < l; ++i) {
         char orgChar = text.charAt(i);
         float glyphAdjust = 0.0F;
         char ch;
         if (font.hasChar(orgChar)) {
            ch = font.mapChar(orgChar);
         } else if (CharUtilities.isFixedWidthSpace(orgChar)) {
            ch = font.mapChar(' ');
            int spaceDiff = font.getCharWidth(ch) - font.getCharWidth(orgChar);
            glyphAdjust = -((float)(10 * spaceDiff) / fontSize);
         } else {
            ch = font.mapChar(orgChar);
         }

         sb.append(ch);
         if (wordSpacing != 0 && CharUtilities.isAdjustableSpace(orgChar)) {
            glyphAdjust += (float)wordSpacing;
         }

         glyphAdjust += (float)letterSpacing;
         if (dx != null && i < dxl - 1) {
            glyphAdjust += (float)dx[i + 1];
         }

         if (glyphAdjust != 0.0F) {
            sb.append("\u001b&a+").append(this.gen.formatDouble2((double)glyphAdjust / 100.0)).append('H');
         }
      }

      this.gen.getOutputStream().write(sb.toString().getBytes(this.gen.getTextEncoding()));
   }

   private Rectangle getTextBoundingBox(int x, int y, int letterSpacing, int wordSpacing, int[] dx, String text, Font font, FontMetricsMapper metrics) {
      int maxAscent = metrics.getMaxAscent(font.getFontSize()) / 1000;
      int descent = metrics.getDescender(font.getFontSize()) / 1000;
      int safetyMargin = (int)(0.05 * (double)font.getFontSize());
      Rectangle boundingRect = new Rectangle(x, y - maxAscent - safetyMargin, 0, maxAscent - descent + 2 * safetyMargin);
      int l = text.length();
      int dxl = dx != null ? dx.length : 0;
      if (dx != null && dxl > 0 && dx[0] != 0) {
         boundingRect.setLocation(boundingRect.x - (int)Math.ceil((double)((float)dx[0] / 10.0F)), boundingRect.y);
      }

      float width = 0.0F;

      int i;
      for(i = 0; i < l; ++i) {
         char orgChar = text.charAt(i);
         float glyphAdjust = 0.0F;
         int cw = font.getCharWidth(orgChar);
         if (wordSpacing != 0 && CharUtilities.isAdjustableSpace(orgChar)) {
            glyphAdjust += (float)wordSpacing;
         }

         glyphAdjust += (float)letterSpacing;
         if (dx != null && i < dxl - 1) {
            glyphAdjust += (float)dx[i + 1];
         }

         width += (float)cw + glyphAdjust;
      }

      i = font.getFontSize() / 3;
      boundingRect.setSize((int)Math.ceil((double)width) + i, boundingRect.height);
      return boundingRect;
   }

   private void drawTextAsBitmap(final int x, final int y, final int letterSpacing, final int wordSpacing, final int[] dx, final String text, FontTriplet triplet) throws IFException {
      Font font = this.parent.getFontInfo().getFontInstance(triplet, this.state.getFontSize());
      FontMetricsMapper mapper = (FontMetricsMapper)this.parent.getFontInfo().getMetricsFor(font.getFontName());
      final int maxAscent = mapper.getMaxAscent(font.getFontSize()) / 1000;
      final int ascent = mapper.getAscender(font.getFontSize()) / 1000;
      final int descent = mapper.getDescender(font.getFontSize()) / 1000;
      int safetyMargin = (int)(0.05 * (double)font.getFontSize());
      final int baselineOffset = maxAscent + safetyMargin;
      Rectangle boundingBox = this.getTextBoundingBox(x, y, letterSpacing, wordSpacing, dx, text, font, mapper);
      final Dimension dim = boundingBox.getSize();
      Graphics2DImagePainter painter = new Graphics2DImagePainter() {
         public void paint(Graphics2D g2d, Rectangle2D area) {
            g2d.translate(-x, -y + baselineOffset);
            Java2DPainter painter = new Java2DPainter(g2d, PCLPainter.this.getContext(), PCLPainter.this.parent.getFontInfo(), PCLPainter.this.state);

            try {
               painter.drawText(x, y, letterSpacing, wordSpacing, dx, text);
            } catch (IFException var5) {
               throw new RuntimeException("Unexpected error while painting text", var5);
            }
         }

         public Dimension getImageSize() {
            return dim.getSize();
         }
      };
      this.paintMarksAsBitmap(painter, boundingBox);
   }

   private void saveGraphicsState() {
      this.graphicContextStack.push(this.graphicContext);
      this.graphicContext = (GraphicContext)this.graphicContext.clone();
   }

   private void restoreGraphicsState() {
      this.graphicContext = (GraphicContext)this.graphicContextStack.pop();
   }

   private void concatenateTransformationMatrix(AffineTransform transform) throws IOException {
      if (!transform.isIdentity()) {
         this.graphicContext.transform(transform);
         this.changePrintDirection();
      }

   }

   private Point2D transformedPoint(int x, int y) {
      return PCLRenderingUtil.transformedPoint(x, y, this.graphicContext.getTransform(), this.currentPageDefinition, this.currentPrintDirection);
   }

   private void changePrintDirection() throws IOException {
      AffineTransform at = this.graphicContext.getTransform();
      int newDir = PCLRenderingUtil.determinePrintDirection(at);
      if (newDir != this.currentPrintDirection) {
         this.currentPrintDirection = newDir;
         this.gen.changePrintDirection(this.currentPrintDirection);
      }

   }

   void setCursorPos(int x, int y) throws IOException {
      Point2D transPoint = this.transformedPoint(x, y);
      this.gen.setCursorPos(transPoint.getX(), transPoint.getY());
   }

   static {
      log = LogFactory.getLog(PCLPainter.class);
   }
}
