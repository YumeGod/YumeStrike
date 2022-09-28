package org.apache.fop.render.java2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import org.apache.fop.fonts.FontInfo;

public class Java2DGraphicsState {
   private Graphics2D currentGraphics;
   private BasicStroke currentStroke;
   private float currentStrokeWidth;
   private int currentStrokeStyle;
   private FontInfo fontInfo;
   private AffineTransform initialTransform;

   public Java2DGraphicsState(Graphics2D graphics, FontInfo fontInfo, AffineTransform at) {
      this.fontInfo = fontInfo;
      this.currentGraphics = graphics;
      this.initialTransform = at;
      this.currentGraphics.setTransform(at);
   }

   public Java2DGraphicsState(Java2DGraphicsState org) {
      this.currentGraphics = (Graphics2D)org.currentGraphics.create();
      this.fontInfo = org.fontInfo;
      this.initialTransform = org.initialTransform;
      this.currentStroke = org.currentStroke;
      this.currentStrokeStyle = org.currentStrokeStyle;
      this.currentStrokeWidth = org.currentStrokeWidth;
   }

   public Graphics2D getGraph() {
      return this.currentGraphics;
   }

   public void dispose() {
      this.currentGraphics.dispose();
      this.currentGraphics = null;
   }

   public boolean updateColor(Color col) {
      if (!col.equals(this.getGraph().getColor())) {
         this.getGraph().setColor(col);
         return true;
      } else {
         return false;
      }
   }

   public Color getColor() {
      return this.currentGraphics.getColor();
   }

   public boolean updateFont(String name, int size) {
      FontMetricsMapper mapper = (FontMetricsMapper)this.fontInfo.getMetricsFor(name);
      boolean updateName = !mapper.getFontName().equals(this.getGraph().getFont().getFontName());
      boolean updateSize = size != this.getGraph().getFont().getSize() * 1000;
      if (!updateName && !updateSize) {
         return false;
      } else {
         Font font = mapper.getFont(size);
         this.currentGraphics.setFont(font);
         return true;
      }
   }

   public Font getFont() {
      return this.currentGraphics.getFont();
   }

   public boolean updateStroke(float width, int style) {
      boolean update = false;
      if (width != this.currentStrokeWidth || style != this.currentStrokeStyle) {
         update = true;
         switch (style) {
            case 31:
               this.currentStroke = new BasicStroke(width, 0, 2, 0.0F, new float[]{8.0F, 2.0F}, 0.0F);
               this.currentGraphics.setStroke(this.currentStroke);
               this.currentStrokeWidth = width;
               this.currentStrokeStyle = style;
               break;
            case 36:
               this.currentStroke = new BasicStroke(width, 1, 2, 0.0F, new float[]{0.0F, 2.0F * width}, width);
               this.currentGraphics.setStroke(this.currentStroke);
               this.currentStrokeWidth = width;
               this.currentStrokeStyle = style;
               break;
            default:
               this.currentStroke = new BasicStroke(width);
               this.currentGraphics.setStroke(this.currentStroke);
               this.currentStrokeWidth = width;
               this.currentStrokeStyle = style;
         }
      }

      return update;
   }

   public BasicStroke getStroke() {
      return (BasicStroke)this.currentGraphics.getStroke();
   }

   public boolean updatePaint(Paint p) {
      if (this.getGraph().getPaint() == null) {
         if (p != null) {
            this.getGraph().setPaint(p);
            return true;
         }
      } else if (!p.equals(this.getGraph().getPaint())) {
         this.getGraph().setPaint(p);
         return true;
      }

      return false;
   }

   public boolean updateClip(Shape cl) {
      if (this.getGraph().getClip() != null) {
         Area newClip = new Area(this.getGraph().getClip());
         newClip.intersect(new Area(cl));
         this.getGraph().setClip(new GeneralPath(newClip));
      } else {
         this.getGraph().setClip(cl);
      }

      return true;
   }

   public void transform(AffineTransform tf) {
      if (!tf.isIdentity()) {
         this.getGraph().transform(tf);
      }

   }

   public AffineTransform getTransform() {
      return this.getGraph().getTransform();
   }

   public String toString() {
      String s = "Java2DGraphicsState " + this.currentGraphics.toString() + ", Stroke (width: " + this.currentStrokeWidth + " style: " + this.currentStrokeStyle + "), " + this.getTransform();
      return s;
   }
}
