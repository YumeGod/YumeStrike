package org.apache.batik.ext.awt.g2d;

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

   public GraphicContext(AffineTransform var1) {
      this();
      this.defaultTransform = new AffineTransform(var1);
      this.transform = new AffineTransform(this.defaultTransform);
      if (!this.defaultTransform.isIdentity()) {
         this.transformStack.add(TransformStackElement.createGeneralTransformElement(this.defaultTransform));
      }

   }

   public Object clone() {
      GraphicContext var1 = new GraphicContext(this.defaultTransform);
      var1.transform = new AffineTransform(this.transform);
      var1.transformStack = new ArrayList(this.transformStack.size());

      for(int var2 = 0; var2 < this.transformStack.size(); ++var2) {
         TransformStackElement var3 = (TransformStackElement)this.transformStack.get(var2);
         var1.transformStack.add(var3.clone());
      }

      var1.transformStackValid = this.transformStackValid;
      var1.paint = this.paint;
      var1.stroke = this.stroke;
      var1.composite = this.composite;
      if (this.clip != null) {
         var1.clip = new GeneralPath(this.clip);
      } else {
         var1.clip = null;
      }

      var1.hints = (RenderingHints)this.hints.clone();
      var1.font = this.font;
      var1.background = this.background;
      var1.foreground = this.foreground;
      return var1;
   }

   public Color getColor() {
      return this.foreground;
   }

   public void setColor(Color var1) {
      if (var1 != null) {
         if (this.paint != var1) {
            this.setPaint(var1);
         }

      }
   }

   public Font getFont() {
      return this.font;
   }

   public void setFont(Font var1) {
      if (var1 != null) {
         this.font = var1;
      }

   }

   public Rectangle getClipBounds() {
      Shape var1 = this.getClip();
      return var1 == null ? null : var1.getBounds();
   }

   public void clipRect(int var1, int var2, int var3, int var4) {
      this.clip(new Rectangle(var1, var2, var3, var4));
   }

   public void setClip(int var1, int var2, int var3, int var4) {
      this.setClip(new Rectangle(var1, var2, var3, var4));
   }

   public Shape getClip() {
      try {
         return this.transform.createInverse().createTransformedShape(this.clip);
      } catch (NoninvertibleTransformException var2) {
         return null;
      }
   }

   public void setClip(Shape var1) {
      if (var1 != null) {
         this.clip = this.transform.createTransformedShape(var1);
      } else {
         this.clip = null;
      }

   }

   public void setComposite(Composite var1) {
      this.composite = var1;
   }

   public void setPaint(Paint var1) {
      if (var1 != null) {
         this.paint = var1;
         if (var1 instanceof Color) {
            this.foreground = (Color)var1;
         }

      }
   }

   public void setStroke(Stroke var1) {
      this.stroke = var1;
   }

   public void setRenderingHint(RenderingHints.Key var1, Object var2) {
      this.hints.put(var1, var2);
   }

   public Object getRenderingHint(RenderingHints.Key var1) {
      return this.hints.get(var1);
   }

   public void setRenderingHints(Map var1) {
      this.hints = new RenderingHints(var1);
   }

   public void addRenderingHints(Map var1) {
      this.hints.putAll(var1);
   }

   public RenderingHints getRenderingHints() {
      return this.hints;
   }

   public void translate(int var1, int var2) {
      if (var1 != 0 || var2 != 0) {
         this.transform.translate((double)var1, (double)var2);
         this.transformStack.add(TransformStackElement.createTranslateElement((double)var1, (double)var2));
      }

   }

   public void translate(double var1, double var3) {
      this.transform.translate(var1, var3);
      this.transformStack.add(TransformStackElement.createTranslateElement(var1, var3));
   }

   public void rotate(double var1) {
      this.transform.rotate(var1);
      this.transformStack.add(TransformStackElement.createRotateElement(var1));
   }

   public void rotate(double var1, double var3, double var5) {
      this.transform.rotate(var1, var3, var5);
      this.transformStack.add(TransformStackElement.createTranslateElement(var3, var5));
      this.transformStack.add(TransformStackElement.createRotateElement(var1));
      this.transformStack.add(TransformStackElement.createTranslateElement(-var3, -var5));
   }

   public void scale(double var1, double var3) {
      this.transform.scale(var1, var3);
      this.transformStack.add(TransformStackElement.createScaleElement(var1, var3));
   }

   public void shear(double var1, double var3) {
      this.transform.shear(var1, var3);
      this.transformStack.add(TransformStackElement.createShearElement(var1, var3));
   }

   public void transform(AffineTransform var1) {
      this.transform.concatenate(var1);
      this.transformStack.add(TransformStackElement.createGeneralTransformElement(var1));
   }

   public void setTransform(AffineTransform var1) {
      this.transform = new AffineTransform(var1);
      this.invalidateTransformStack();
      if (!var1.isIdentity()) {
         this.transformStack.add(TransformStackElement.createGeneralTransformElement(var1));
      }

   }

   public void validateTransformStack() {
      this.transformStackValid = true;
   }

   public boolean isTransformStackValid() {
      return this.transformStackValid;
   }

   public TransformStackElement[] getTransformStack() {
      TransformStackElement[] var1 = new TransformStackElement[this.transformStack.size()];
      this.transformStack.toArray(var1);
      return var1;
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

   public void setBackground(Color var1) {
      if (var1 != null) {
         this.background = var1;
      }
   }

   public Color getBackground() {
      return this.background;
   }

   public Stroke getStroke() {
      return this.stroke;
   }

   public void clip(Shape var1) {
      if (var1 != null) {
         var1 = this.transform.createTransformedShape(var1);
      }

      if (this.clip != null) {
         Area var2 = new Area(this.clip);
         var2.intersect(new Area(var1));
         this.clip = new GeneralPath(var2);
      } else {
         this.clip = var1;
      }

   }

   public FontRenderContext getFontRenderContext() {
      Object var1 = this.hints.get(RenderingHints.KEY_TEXT_ANTIALIASING);
      boolean var2 = true;
      if (var1 != RenderingHints.VALUE_TEXT_ANTIALIAS_ON && var1 != RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
         if (var1 != RenderingHints.VALUE_TEXT_ANTIALIAS_OFF) {
            var1 = this.hints.get(RenderingHints.KEY_ANTIALIASING);
            if (var1 != RenderingHints.VALUE_ANTIALIAS_ON && var1 != RenderingHints.VALUE_ANTIALIAS_DEFAULT && var1 == RenderingHints.VALUE_ANTIALIAS_OFF) {
               var2 = false;
            }
         } else {
            var2 = false;
         }
      }

      boolean var3 = true;
      if (this.hints.get(RenderingHints.KEY_FRACTIONALMETRICS) == RenderingHints.VALUE_FRACTIONALMETRICS_OFF) {
         var3 = false;
      }

      FontRenderContext var4 = new FontRenderContext(this.defaultTransform, var2, var3);
      return var4;
   }
}
