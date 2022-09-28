package org.apache.batik.ext.awt.g2d;

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

   public AbstractGraphics2D(boolean var1) {
      this.textAsShapes = var1;
   }

   public AbstractGraphics2D(AbstractGraphics2D var1) {
      this.gc = (GraphicContext)var1.gc.clone();
      this.gc.validateTransformStack();
      this.textAsShapes = var1.textAsShapes;
   }

   public void translate(int var1, int var2) {
      this.gc.translate(var1, var2);
   }

   public Color getColor() {
      return this.gc.getColor();
   }

   public void setColor(Color var1) {
      this.gc.setColor(var1);
   }

   public void setPaintMode() {
      this.gc.setComposite(AlphaComposite.SrcOver);
   }

   public Font getFont() {
      return this.gc.getFont();
   }

   public void setFont(Font var1) {
      this.gc.setFont(var1);
   }

   public Rectangle getClipBounds() {
      return this.gc.getClipBounds();
   }

   public void clipRect(int var1, int var2, int var3, int var4) {
      this.gc.clipRect(var1, var2, var3, var4);
   }

   public void setClip(int var1, int var2, int var3, int var4) {
      this.gc.setClip(var1, var2, var3, var4);
   }

   public Shape getClip() {
      return this.gc.getClip();
   }

   public void setClip(Shape var1) {
      this.gc.setClip(var1);
   }

   public void drawLine(int var1, int var2, int var3, int var4) {
      Line2D.Float var5 = new Line2D.Float((float)var1, (float)var2, (float)var3, (float)var4);
      this.draw(var5);
   }

   public void fillRect(int var1, int var2, int var3, int var4) {
      Rectangle var5 = new Rectangle(var1, var2, var3, var4);
      this.fill(var5);
   }

   public void drawRect(int var1, int var2, int var3, int var4) {
      Rectangle var5 = new Rectangle(var1, var2, var3, var4);
      this.draw(var5);
   }

   public void clearRect(int var1, int var2, int var3, int var4) {
      Paint var5 = this.gc.getPaint();
      this.gc.setColor(this.gc.getBackground());
      this.fillRect(var1, var2, var3, var4);
      this.gc.setPaint(var5);
   }

   public void drawRoundRect(int var1, int var2, int var3, int var4, int var5, int var6) {
      RoundRectangle2D.Float var7 = new RoundRectangle2D.Float((float)var1, (float)var2, (float)var3, (float)var4, (float)var5, (float)var6);
      this.draw(var7);
   }

   public void fillRoundRect(int var1, int var2, int var3, int var4, int var5, int var6) {
      RoundRectangle2D.Float var7 = new RoundRectangle2D.Float((float)var1, (float)var2, (float)var3, (float)var4, (float)var5, (float)var6);
      this.fill(var7);
   }

   public void drawOval(int var1, int var2, int var3, int var4) {
      Ellipse2D.Float var5 = new Ellipse2D.Float((float)var1, (float)var2, (float)var3, (float)var4);
      this.draw(var5);
   }

   public void fillOval(int var1, int var2, int var3, int var4) {
      Ellipse2D.Float var5 = new Ellipse2D.Float((float)var1, (float)var2, (float)var3, (float)var4);
      this.fill(var5);
   }

   public void drawArc(int var1, int var2, int var3, int var4, int var5, int var6) {
      Arc2D.Float var7 = new Arc2D.Float((float)var1, (float)var2, (float)var3, (float)var4, (float)var5, (float)var6, 0);
      this.draw(var7);
   }

   public void fillArc(int var1, int var2, int var3, int var4, int var5, int var6) {
      Arc2D.Float var7 = new Arc2D.Float((float)var1, (float)var2, (float)var3, (float)var4, (float)var5, (float)var6, 2);
      this.fill(var7);
   }

   public void drawPolyline(int[] var1, int[] var2, int var3) {
      if (var3 > 0) {
         GeneralPath var4 = new GeneralPath();
         var4.moveTo((float)var1[0], (float)var2[0]);

         for(int var5 = 1; var5 < var3; ++var5) {
            var4.lineTo((float)var1[var5], (float)var2[var5]);
         }

         this.draw(var4);
      }

   }

   public void drawPolygon(int[] var1, int[] var2, int var3) {
      Polygon var4 = new Polygon(var1, var2, var3);
      this.draw(var4);
   }

   public void fillPolygon(int[] var1, int[] var2, int var3) {
      Polygon var4 = new Polygon(var1, var2, var3);
      this.fill(var4);
   }

   public void drawString(String var1, int var2, int var3) {
      this.drawString(var1, (float)var2, (float)var3);
   }

   public void drawString(AttributedCharacterIterator var1, int var2, int var3) {
      this.drawString(var1, (float)var2, (float)var3);
   }

   public boolean drawImage(Image var1, int var2, int var3, Color var4, ImageObserver var5) {
      return this.drawImage(var1, var2, var3, var1.getWidth((ImageObserver)null), var1.getHeight((ImageObserver)null), var4, var5);
   }

   public boolean drawImage(Image var1, int var2, int var3, int var4, int var5, Color var6, ImageObserver var7) {
      Paint var8 = this.gc.getPaint();
      this.gc.setPaint(var6);
      this.fillRect(var2, var3, var4, var5);
      this.gc.setPaint(var8);
      this.drawImage(var1, var2, var3, var4, var5, var7);
      return true;
   }

   public boolean drawImage(Image var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ImageObserver var10) {
      BufferedImage var11 = new BufferedImage(var1.getWidth((ImageObserver)null), var1.getHeight((ImageObserver)null), 2);
      Graphics2D var12 = var11.createGraphics();
      var12.drawImage(var1, 0, 0, (ImageObserver)null);
      var12.dispose();
      var11 = var11.getSubimage(var6, var7, var8 - var6, var9 - var7);
      return this.drawImage(var11, var2, var3, var4 - var2, var5 - var3, var10);
   }

   public boolean drawImage(Image var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, Color var10, ImageObserver var11) {
      Paint var12 = this.gc.getPaint();
      this.gc.setPaint(var10);
      this.fillRect(var2, var3, var4 - var2, var5 - var3);
      this.gc.setPaint(var12);
      return this.drawImage(var1, var2, var3, var4, var5, var6, var7, var8, var9, var11);
   }

   public boolean drawImage(Image var1, AffineTransform var2, ImageObserver var3) {
      boolean var4 = true;
      AffineTransform var5;
      if (var2.getDeterminant() != 0.0) {
         var5 = null;

         try {
            var5 = var2.createInverse();
         } catch (NoninvertibleTransformException var7) {
            throw new Error(var7.getMessage());
         }

         this.gc.transform(var2);
         var4 = this.drawImage(var1, 0, 0, (ImageObserver)null);
         this.gc.transform(var5);
      } else {
         var5 = new AffineTransform(this.gc.getTransform());
         this.gc.transform(var2);
         var4 = this.drawImage(var1, 0, 0, (ImageObserver)null);
         this.gc.setTransform(var5);
      }

      return var4;
   }

   public void drawImage(BufferedImage var1, BufferedImageOp var2, int var3, int var4) {
      var1 = var2.filter(var1, (BufferedImage)null);
      this.drawImage(var1, var3, var4, (ImageObserver)null);
   }

   public void drawGlyphVector(GlyphVector var1, float var2, float var3) {
      Shape var4 = var1.getOutline(var2, var3);
      this.fill(var4);
   }

   public boolean hit(Rectangle var1, Shape var2, boolean var3) {
      if (var3) {
         var2 = this.gc.getStroke().createStrokedShape(var2);
      }

      var2 = this.gc.getTransform().createTransformedShape(var2);
      return var2.intersects(var1);
   }

   public void setComposite(Composite var1) {
      this.gc.setComposite(var1);
   }

   public void setPaint(Paint var1) {
      this.gc.setPaint(var1);
   }

   public void setStroke(Stroke var1) {
      this.gc.setStroke(var1);
   }

   public void setRenderingHint(RenderingHints.Key var1, Object var2) {
      this.gc.setRenderingHint(var1, var2);
   }

   public Object getRenderingHint(RenderingHints.Key var1) {
      return this.gc.getRenderingHint(var1);
   }

   public void setRenderingHints(Map var1) {
      this.gc.setRenderingHints(var1);
   }

   public void addRenderingHints(Map var1) {
      this.gc.addRenderingHints(var1);
   }

   public RenderingHints getRenderingHints() {
      return this.gc.getRenderingHints();
   }

   public void translate(double var1, double var3) {
      this.gc.translate(var1, var3);
   }

   public void rotate(double var1) {
      this.gc.rotate(var1);
   }

   public void rotate(double var1, double var3, double var5) {
      this.gc.rotate(var1, var3, var5);
   }

   public void scale(double var1, double var3) {
      this.gc.scale(var1, var3);
   }

   public void shear(double var1, double var3) {
      this.gc.shear(var1, var3);
   }

   public void transform(AffineTransform var1) {
      this.gc.transform(var1);
   }

   public void setTransform(AffineTransform var1) {
      this.gc.setTransform(var1);
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

   public void setBackground(Color var1) {
      this.gc.setBackground(var1);
   }

   public Color getBackground() {
      return this.gc.getBackground();
   }

   public Stroke getStroke() {
      return this.gc.getStroke();
   }

   public void clip(Shape var1) {
      this.gc.clip(var1);
   }

   public FontRenderContext getFontRenderContext() {
      return this.gc.getFontRenderContext();
   }

   public GraphicContext getGraphicContext() {
      return this.gc;
   }
}
