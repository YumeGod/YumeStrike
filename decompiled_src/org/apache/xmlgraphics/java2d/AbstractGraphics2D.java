package org.apache.xmlgraphics.java2d;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public abstract class AbstractGraphics2D extends Graphics2D implements Cloneable {
   protected GraphicContext gc;
   protected boolean textAsShapes = false;
   protected boolean inPossibleRecursion = false;

   public AbstractGraphics2D(boolean textAsShapes) {
      this.textAsShapes = textAsShapes;
   }

   public AbstractGraphics2D(AbstractGraphics2D g) {
      this.gc = (GraphicContext)g.gc.clone();
      this.gc.validateTransformStack();
      this.textAsShapes = g.textAsShapes;
   }

   public void translate(int x, int y) {
      this.gc.translate(x, y);
   }

   public Color getColor() {
      return this.gc.getColor();
   }

   public void setColor(Color c) {
      this.gc.setColor(c);
   }

   public void setPaintMode() {
      this.gc.setComposite(AlphaComposite.SrcOver);
   }

   public Font getFont() {
      return this.gc.getFont();
   }

   public void setFont(Font font) {
      this.gc.setFont(font);
   }

   public Rectangle getClipBounds() {
      return this.gc.getClipBounds();
   }

   public void clipRect(int x, int y, int width, int height) {
      this.gc.clipRect(x, y, width, height);
   }

   public void setClip(int x, int y, int width, int height) {
      this.gc.setClip(x, y, width, height);
   }

   public Shape getClip() {
      return this.gc.getClip();
   }

   public void setClip(Shape clip) {
      this.gc.setClip(clip);
   }

   public void drawLine(int x1, int y1, int x2, int y2) {
      Line2D line = new Line2D.Float((float)x1, (float)y1, (float)x2, (float)y2);
      this.draw(line);
   }

   public void fillRect(int x, int y, int width, int height) {
      Rectangle rect = new Rectangle(x, y, width, height);
      this.fill(rect);
   }

   public void drawRect(int x, int y, int width, int height) {
      Rectangle rect = new Rectangle(x, y, width, height);
      this.draw(rect);
   }

   public void clearRect(int x, int y, int width, int height) {
      Paint paint = this.gc.getPaint();
      this.gc.setColor(this.gc.getBackground());
      this.fillRect(x, y, width, height);
      this.gc.setPaint(paint);
   }

   public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
      RoundRectangle2D rect = new RoundRectangle2D.Float((float)x, (float)y, (float)width, (float)height, (float)arcWidth, (float)arcHeight);
      this.draw(rect);
   }

   public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
      RoundRectangle2D rect = new RoundRectangle2D.Float((float)x, (float)y, (float)width, (float)height, (float)arcWidth, (float)arcHeight);
      this.fill(rect);
   }

   public void drawOval(int x, int y, int width, int height) {
      Ellipse2D oval = new Ellipse2D.Float((float)x, (float)y, (float)width, (float)height);
      this.draw(oval);
   }

   public void fillOval(int x, int y, int width, int height) {
      Ellipse2D oval = new Ellipse2D.Float((float)x, (float)y, (float)width, (float)height);
      this.fill(oval);
   }

   public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
      Arc2D arc = new Arc2D.Float((float)x, (float)y, (float)width, (float)height, (float)startAngle, (float)arcAngle, 0);
      this.draw(arc);
   }

   public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
      Arc2D arc = new Arc2D.Float((float)x, (float)y, (float)width, (float)height, (float)startAngle, (float)arcAngle, 2);
      this.fill(arc);
   }

   public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
      if (nPoints > 0) {
         GeneralPath path = new GeneralPath();
         path.moveTo((float)xPoints[0], (float)yPoints[0]);

         for(int i = 1; i < nPoints; ++i) {
            path.lineTo((float)xPoints[i], (float)yPoints[i]);
         }

         this.draw(path);
      }

   }

   public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
      Polygon polygon = new Polygon(xPoints, yPoints, nPoints);
      this.draw(polygon);
   }

   public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
      Polygon polygon = new Polygon(xPoints, yPoints, nPoints);
      this.fill(polygon);
   }

   public void drawString(String str, int x, int y) {
      this.drawString(str, (float)x, (float)y);
   }

   public void drawString(AttributedCharacterIterator iterator, float x, float y) {
      if (this.inPossibleRecursion) {
         System.err.println("Called itself: drawString(AttributedCharacterIterator)");
      } else {
         this.inPossibleRecursion = true;
         TextLayout layout = new TextLayout(iterator, this.getFontRenderContext());
         layout.draw(this, x, y);
         this.inPossibleRecursion = false;
      }

   }

   public void drawString(AttributedCharacterIterator iterator, int x, int y) {
      this.drawString(iterator, (float)x, (float)y);
   }

   public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
      return this.drawImage(img, x, y, img.getWidth((ImageObserver)null), img.getHeight((ImageObserver)null), bgcolor, observer);
   }

   public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
      Paint paint = this.gc.getPaint();
      this.gc.setPaint(bgcolor);
      this.fillRect(x, y, width, height);
      this.gc.setPaint(paint);
      this.drawImage(img, x, y, width, height, observer);
      return true;
   }

   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
      BufferedImage src = new BufferedImage(img.getWidth((ImageObserver)null), img.getHeight((ImageObserver)null), 2);
      Graphics2D g = src.createGraphics();
      g.drawImage(img, 0, 0, (ImageObserver)null);
      g.dispose();
      src = src.getSubimage(sx1, sy1, sx2 - sx1, sy2 - sy1);
      return this.drawImage(src, dx1, dy1, dx2 - dx1, dy2 - dy1, observer);
   }

   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
      Paint paint = this.gc.getPaint();
      this.gc.setPaint(bgcolor);
      this.fillRect(dx1, dy1, dx2 - dx1, dy2 - dy1);
      this.gc.setPaint(paint);
      return this.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
   }

   public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
      boolean retVal = true;
      AffineTransform inverseTransform;
      if (xform.getDeterminant() != 0.0) {
         inverseTransform = null;

         try {
            inverseTransform = xform.createInverse();
         } catch (NoninvertibleTransformException var7) {
            throw new Error();
         }

         this.gc.transform(xform);
         retVal = this.drawImage(img, 0, 0, (ImageObserver)null);
         this.gc.transform(inverseTransform);
      } else {
         inverseTransform = new AffineTransform(this.gc.getTransform());
         this.gc.transform(xform);
         retVal = this.drawImage(img, 0, 0, (ImageObserver)null);
         this.gc.setTransform(inverseTransform);
      }

      return retVal;
   }

   public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
      img = op.filter(img, (BufferedImage)null);
      this.drawImage(img, x, y, (ImageObserver)null);
   }

   public void drawGlyphVector(GlyphVector g, float x, float y) {
      Shape glyphOutline = g.getOutline(x, y);
      this.fill(glyphOutline);
   }

   public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
      if (onStroke) {
         s = this.gc.getStroke().createStrokedShape(s);
      }

      s = this.gc.getTransform().createTransformedShape(s);
      return s.intersects(rect);
   }

   public void setComposite(Composite comp) {
      this.gc.setComposite(comp);
   }

   public void setPaint(Paint paint) {
      this.gc.setPaint(paint);
   }

   public void setStroke(Stroke s) {
      this.gc.setStroke(s);
   }

   public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
      this.gc.setRenderingHint(hintKey, hintValue);
   }

   public Object getRenderingHint(RenderingHints.Key hintKey) {
      return this.gc.getRenderingHint(hintKey);
   }

   public void setRenderingHints(Map hints) {
      this.gc.setRenderingHints(hints);
   }

   public void addRenderingHints(Map hints) {
      this.gc.addRenderingHints(hints);
   }

   public RenderingHints getRenderingHints() {
      return this.gc.getRenderingHints();
   }

   public void translate(double tx, double ty) {
      this.gc.translate(tx, ty);
   }

   public void rotate(double theta) {
      this.gc.rotate(theta);
   }

   public void rotate(double theta, double x, double y) {
      this.gc.rotate(theta, x, y);
   }

   public void scale(double sx, double sy) {
      this.gc.scale(sx, sy);
   }

   public void shear(double shx, double shy) {
      this.gc.shear(shx, shy);
   }

   public void transform(AffineTransform Tx) {
      this.gc.transform(Tx);
   }

   public void setTransform(AffineTransform Tx) {
      this.gc.setTransform(Tx);
   }

   public AffineTransform getTransform() {
      return this.gc.getTransform();
   }

   public Paint getPaint() {
      return this.gc.getPaint();
   }

   public Composite getComposite() {
      return this.gc.getComposite();
   }

   public void setBackground(Color color) {
      this.gc.setBackground(color);
   }

   public Color getBackground() {
      return this.gc.getBackground();
   }

   public Stroke getStroke() {
      return this.gc.getStroke();
   }

   public void clip(Shape s) {
      this.gc.clip(s);
   }

   public FontRenderContext getFontRenderContext() {
      return this.gc.getFontRenderContext();
   }

   public GraphicContext getGraphicContext() {
      return this.gc;
   }
}
