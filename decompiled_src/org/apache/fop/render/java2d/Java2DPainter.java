package org.apache.fop.render.java2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.render.intermediate.AbstractIFPainter;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFState;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.RuleStyle;
import org.apache.fop.util.CharUtilities;
import org.w3c.dom.Document;

public class Java2DPainter extends AbstractIFPainter {
   private static Log log;
   protected IFContext ifContext;
   protected FontInfo fontInfo;
   private Java2DBorderPainter borderPainter;
   protected Java2DGraphicsState g2dState;
   private Stack g2dStateStack;

   public Java2DPainter(Graphics2D g2d, IFContext context, FontInfo fontInfo) {
      this(g2d, context, fontInfo, (IFState)null);
   }

   public Java2DPainter(Graphics2D g2d, IFContext context, FontInfo fontInfo, IFState state) {
      this.g2dStateStack = new Stack();
      this.ifContext = context;
      if (state != null) {
         this.state = state.push();
      } else {
         this.state = IFState.create();
      }

      this.fontInfo = fontInfo;
      this.g2dState = new Java2DGraphicsState(g2d, fontInfo, g2d.getTransform());
      this.borderPainter = new Java2DBorderPainter(this);
   }

   public IFContext getContext() {
      return this.ifContext;
   }

   protected FontInfo getFontInfo() {
      return this.fontInfo;
   }

   protected Java2DGraphicsState getState() {
      return this.g2dState;
   }

   public void startViewport(AffineTransform transform, Dimension size, Rectangle clipRect) throws IFException {
      this.saveGraphicsState();

      try {
         this.concatenateTransformationMatrix(transform);
         this.clipRect(clipRect);
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
      Java2DRenderingContext java2dContext = new Java2DRenderingContext(this.getUserAgent(), this.g2dState.getGraph(), this.getFontInfo());
      return java2dContext;
   }

   public void drawImage(Document doc, Rectangle rect) throws IFException {
      this.drawImageUsingDocument(doc, rect);
   }

   public void clipRect(Rectangle rect) throws IFException {
      this.getState().updateClip(rect);
   }

   public void fillRect(Rectangle rect, Paint fill) throws IFException {
      if (fill != null) {
         if (rect.width != 0 && rect.height != 0) {
            this.g2dState.updatePaint(fill);
            this.g2dState.getGraph().fill(rect);
         }

      }
   }

   public void drawBorderRect(Rectangle rect, BorderProps before, BorderProps after, BorderProps start, BorderProps end) throws IFException {
      if (before != null || after != null || start != null || end != null) {
         try {
            this.borderPainter.drawBorders(rect, before, after, start, end);
         } catch (IOException var7) {
            throw new IllegalStateException("Unexpected I/O error");
         }
      }

   }

   public void drawLine(Point start, Point end, int width, Color color, RuleStyle style) throws IFException {
      this.borderPainter.drawLine(start, end, width, color, style);
   }

   public void drawText(int x, int y, int letterSpacing, int wordSpacing, int[] dx, String text) throws IFException {
      this.g2dState.updateColor(this.state.getTextColor());
      FontTriplet triplet = new FontTriplet(this.state.getFontFamily(), this.state.getFontStyle(), this.state.getFontWeight());
      Font font = this.getFontInfo().getFontInstance(triplet, this.state.getFontSize());
      this.g2dState.updateFont(font.getFontName(), this.state.getFontSize() * 1000);
      Graphics2D g2d = this.g2dState.getGraph();
      GlyphVector gv = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), text);
      Point2D cursor = new Point2D.Float(0.0F, 0.0F);
      int l = text.length();
      int dxl = dx != null ? dx.length : 0;
      if (dx != null && dxl > 0 && dx[0] != 0) {
         cursor.setLocation(cursor.getX() - (double)((float)dx[0] / 10.0F), cursor.getY());
         gv.setGlyphPosition(0, cursor);
      }

      for(int i = 0; i < l; ++i) {
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

         cursor.setLocation(cursor.getX() + (double)cw + (double)glyphAdjust, cursor.getY());
         gv.setGlyphPosition(i + 1, cursor);
      }

      g2d.drawGlyphVector(gv, (float)x, (float)y);
   }

   protected void saveGraphicsState() {
      this.g2dStateStack.push(this.g2dState);
      this.g2dState = new Java2DGraphicsState(this.g2dState);
   }

   protected void restoreGraphicsState() {
      this.g2dState.dispose();
      this.g2dState = (Java2DGraphicsState)this.g2dStateStack.pop();
   }

   private void concatenateTransformationMatrix(AffineTransform transform) throws IOException {
      this.g2dState.transform(transform);
   }

   static {
      log = LogFactory.getLog(Java2DPainter.class);
   }
}
