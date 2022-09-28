package org.apache.xmlgraphics.java2d;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphicContext implements Cloneable {
   protected AffineTransform defaultTransform;
   protected AffineTransform transform;
   protected List transformStack;
   protected boolean transformStackValid;
   protected Paint paint;
   protected Stroke stroke;
   protected Composite composite;
   protected Shape clip;
   protected RenderingHints hints;
   protected Font font;
   protected Color background;
   protected Color foreground;

   public GraphicContext() {
      this.defaultTransform = new AffineTransform();
      this.transform = new AffineTransform();
      this.transformStack = new ArrayList();
      this.transformStackValid = true;
      this.paint = Color.black;
      this.stroke = new BasicStroke();
      this.composite = AlphaComposite.SrcOver;
      this.clip = null;
      this.hints = new RenderingHints((Map)null);
      this.font = new Font("sanserif", 0, 12);
      this.background = new Color(0, 0, 0, 0);
      this.foreground = Color.black;
      this.hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
   }

   public GraphicContext(AffineTransform defaultDeviceTransform) {
      this();
      this.defaultTransform = new AffineTransform(defaultDeviceTransform);
      this.transform = new AffineTransform(this.defaultTransform);
      if (!this.defaultTransform.isIdentity()) {
         this.transformStack.add(TransformStackElement.createGeneralTransformElement(this.defaultTransform));
      }

   }

   protected GraphicContext(GraphicContext template) {
      this(template.defaultTransform);
      this.transform = new AffineTransform(template.transform);
      this.transformStack = new ArrayList(template.transformStack.size());

      for(int i = 0; i < template.transformStack.size(); ++i) {
         TransformStackElement stackElement = (TransformStackElement)template.transformStack.get(i);
         this.transformStack.add(stackElement.clone());
      }

      this.transformStackValid = template.transformStackValid;
      this.paint = template.paint;
      this.stroke = template.stroke;
      this.composite = template.composite;
      if (template.clip != null) {
         this.clip = new GeneralPath(template.clip);
      } else {
         this.clip = null;
      }

      this.hints = (RenderingHints)template.hints.clone();
      this.font = template.font;
      this.background = template.background;
      this.foreground = template.foreground;
   }

   public Object clone() {
      return new GraphicContext(this);
   }

   public Color getColor() {
      return this.foreground;
   }

   public void setColor(Color c) {
      if (c != null) {
         if (this.paint != c) {
            this.setPaint(c);
         }

      }
   }

   public Font getFont() {
      return this.font;
   }

   public void setFont(Font font) {
      if (font != null) {
         this.font = font;
      }

   }

   public Rectangle getClipBounds() {
      Shape c = this.getClip();
      return c == null ? null : c.getBounds();
   }

   public void clipRect(int x, int y, int width, int height) {
      this.clip(new Rectangle(x, y, width, height));
   }

   public void setClip(int x, int y, int width, int height) {
      this.setClip(new Rectangle(x, y, width, height));
   }

   public Shape getClip() {
      try {
         return this.transform.createInverse().createTransformedShape(this.clip);
      } catch (NoninvertibleTransformException var2) {
         return null;
      }
   }

   public void setClip(Shape clip) {
      if (clip != null) {
         this.clip = this.transform.createTransformedShape(clip);
      } else {
         this.clip = null;
      }

   }

   public void setComposite(Composite comp) {
      this.composite = comp;
   }

   public void setPaint(Paint paint) {
      if (paint != null) {
         this.paint = paint;
         if (paint instanceof Color) {
            this.foreground = (Color)paint;
         }

      }
   }

   public void setStroke(Stroke s) {
      this.stroke = s;
   }

   public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
      this.hints.put(hintKey, hintValue);
   }

   public Object getRenderingHint(RenderingHints.Key hintKey) {
      return this.hints.get(hintKey);
   }

   public void setRenderingHints(Map hints) {
      this.hints = new RenderingHints(hints);
   }

   public void addRenderingHints(Map hints) {
      this.hints.putAll(hints);
   }

   public RenderingHints getRenderingHints() {
      return this.hints;
   }

   public void translate(int x, int y) {
      if (x != 0 || y != 0) {
         this.transform.translate((double)x, (double)y);
         this.transformStack.add(TransformStackElement.createTranslateElement((double)x, (double)y));
      }

   }

   public void translate(double tx, double ty) {
      this.transform.translate(tx, ty);
      this.transformStack.add(TransformStackElement.createTranslateElement(tx, ty));
   }

   public void rotate(double theta) {
      this.transform.rotate(theta);
      this.transformStack.add(TransformStackElement.createRotateElement(theta));
   }

   public void rotate(double theta, double x, double y) {
      this.transform.rotate(theta, x, y);
      this.transformStack.add(TransformStackElement.createTranslateElement(x, y));
      this.transformStack.add(TransformStackElement.createRotateElement(theta));
      this.transformStack.add(TransformStackElement.createTranslateElement(-x, -y));
   }

   public void scale(double sx, double sy) {
      this.transform.scale(sx, sy);
      this.transformStack.add(TransformStackElement.createScaleElement(sx, sy));
   }

   public void shear(double shx, double shy) {
      this.transform.shear(shx, shy);
      this.transformStack.add(TransformStackElement.createShearElement(shx, shy));
   }

   public void transform(AffineTransform Tx) {
      this.transform.concatenate(Tx);
      this.transformStack.add(TransformStackElement.createGeneralTransformElement(Tx));
   }

   public void setTransform(AffineTransform Tx) {
      this.transform = new AffineTransform(Tx);
      this.invalidateTransformStack();
      if (!Tx.isIdentity()) {
         this.transformStack.add(TransformStackElement.createGeneralTransformElement(Tx));
      }

   }

   public void validateTransformStack() {
      this.transformStackValid = true;
   }

   public boolean isTransformStackValid() {
      return this.transformStackValid;
   }

   public TransformStackElement[] getTransformStack() {
      TransformStackElement[] stack = new TransformStackElement[this.transformStack.size()];
      this.transformStack.toArray(stack);
      return stack;
   }

   protected void invalidateTransformStack() {
      this.transformStack.clear();
      this.transformStackValid = false;
   }

   public AffineTransform getTransform() {
      return new AffineTransform(this.transform);
   }

   public Paint getPaint() {
      return this.paint;
   }

   public Composite getComposite() {
      return this.composite;
   }

   public void setBackground(Color color) {
      if (color != null) {
         this.background = color;
      }
   }

   public Color getBackground() {
      return this.background;
   }

   public Stroke getStroke() {
      return this.stroke;
   }

   public void clip(Shape s) {
      if (s != null) {
         s = this.transform.createTransformedShape(s);
      }

      if (this.clip != null) {
         Area newClip = new Area(this.clip);
         newClip.intersect(new Area(s));
         this.clip = new GeneralPath(newClip);
      } else {
         this.clip = s;
      }

   }

   public FontRenderContext getFontRenderContext() {
      Object antialiasingHint = this.hints.get(RenderingHints.KEY_TEXT_ANTIALIASING);
      boolean isAntialiased = true;
      if (antialiasingHint != RenderingHints.VALUE_TEXT_ANTIALIAS_ON && antialiasingHint != RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
         if (antialiasingHint != RenderingHints.VALUE_TEXT_ANTIALIAS_OFF) {
            antialiasingHint = this.hints.get(RenderingHints.KEY_ANTIALIASING);
            if (antialiasingHint != RenderingHints.VALUE_ANTIALIAS_ON && antialiasingHint != RenderingHints.VALUE_ANTIALIAS_DEFAULT && antialiasingHint == RenderingHints.VALUE_ANTIALIAS_OFF) {
               isAntialiased = false;
            }
         } else {
            isAntialiased = false;
         }
      }

      boolean useFractionalMetrics = true;
      if (this.hints.get(RenderingHints.KEY_FRACTIONALMETRICS) == RenderingHints.VALUE_FRACTIONALMETRICS_OFF) {
         useFractionalMetrics = false;
      }

      FontRenderContext frc = new FontRenderContext(this.defaultTransform, isAntialiased, useFractionalMetrics);
      return frc;
   }
}
