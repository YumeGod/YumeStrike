package org.apache.fop.render.pcl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import org.apache.xmlgraphics.java2d.AbstractGraphics2D;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.util.UnitConv;

public class PCLGraphics2D extends AbstractGraphics2D {
   protected PCLGenerator gen;
   private final boolean failOnUnsupportedFeature = true;
   private boolean clippingDisabled = false;
   private Graphics2D fmg;

   public PCLGraphics2D(PCLGenerator gen) {
      super(true);
      BufferedImage bi = new BufferedImage(1, 1, 2);
      this.fmg = bi.createGraphics();
      this.gen = gen;
   }

   public PCLGraphics2D(PCLGraphics2D g) {
      super(true);
      BufferedImage bi = new BufferedImage(1, 1, 2);
      this.fmg = bi.createGraphics();
      this.gen = g.gen;
   }

   public Graphics create() {
      PCLGraphics2D copy = new PCLGraphics2D(this);
      copy.setGraphicContext((GraphicContext)this.getGraphicContext().clone());
      return copy;
   }

   public void dispose() {
      this.gen = null;
   }

   public void setGraphicContext(GraphicContext c) {
      this.gc = c;
   }

   public void setClippingDisabled(boolean value) {
      this.clippingDisabled = value;
   }

   public void handleIOException(IOException ioe) {
      ioe.printStackTrace();
   }

   protected void handleUnsupportedFeature(String msg) {
      this.getClass();
      throw new UnsupportedOperationException(msg);
   }

   public GraphicsConfiguration getDeviceConfiguration() {
      return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
   }

   protected void applyStroke(Stroke stroke) throws IOException {
      if (stroke instanceof BasicStroke) {
         BasicStroke bs = (BasicStroke)stroke;
         float[] da = bs.getDashArray();
         int len;
         float perc;
         if (da == null) {
            this.gen.writeText("LT;");
         } else {
            this.gen.writeText("UL1,");
            len = Math.min(20, da.length);
            float patternLen = 0.0F;

            int idx;
            for(idx = 0; idx < len; ++idx) {
               patternLen += da[idx];
            }

            if (len == 1) {
               patternLen *= 2.0F;
            }

            for(idx = 0; idx < len; ++idx) {
               perc = da[idx] * 100.0F / patternLen;
               this.gen.writeText(this.gen.formatDouble2((double)perc));
               if (idx < da.length - 1) {
                  this.gen.writeText(",");
               }
            }

            if (len == 1) {
               this.gen.writeText("," + this.gen.formatDouble2((double)(da[0] * 100.0F / patternLen)));
            }

            this.gen.writeText(";");
            Point2D ptLen = new Point2D.Double((double)patternLen, 0.0);
            this.getTransform().deltaTransform(ptLen, ptLen);
            double transLen = UnitConv.pt2mm(ptLen.distance(0.0, 0.0));
            this.gen.writeText("LT1," + this.gen.formatDouble4(transLen) + ",1;");
         }

         this.gen.writeText("LA1");
         len = bs.getEndCap();
         switch (len) {
            case 0:
               this.gen.writeText(",1");
               break;
            case 1:
               this.gen.writeText(",4");
               break;
            case 2:
               this.gen.writeText(",2");
               break;
            default:
               System.err.println("Unsupported line cap: " + len);
         }

         this.gen.writeText(",2");
         int lj = bs.getLineJoin();
         switch (lj) {
            case 0:
               this.gen.writeText(",1");
               break;
            case 1:
               this.gen.writeText(",4");
               break;
            case 2:
               this.gen.writeText(",5");
               break;
            default:
               System.err.println("Unsupported line join: " + lj);
         }

         float ml = bs.getMiterLimit();
         this.gen.writeText(",3" + this.gen.formatDouble4((double)ml));
         perc = bs.getLineWidth();
         Point2D ptSrc = new Point2D.Double((double)perc, 0.0);
         Point2D ptDest = this.getTransform().deltaTransform(ptSrc, (Point2D)null);
         double transDist = UnitConv.pt2mm(ptDest.distance(0.0, 0.0));
         this.gen.writeText(";PW" + this.gen.formatDouble4(transDist) + ";");
      } else {
         this.handleUnsupportedFeature("Unsupported Stroke: " + stroke.getClass().getName());
      }

   }

   protected void applyPaint(Paint paint) throws IOException {
      if (paint instanceof Color) {
         Color col = (Color)paint;
         int shade = this.gen.convertToPCLShade(col);
         this.gen.writeText("TR0;FT10," + shade + ";");
      } else {
         this.handleUnsupportedFeature("Unsupported Paint: " + paint.getClass().getName());
      }

   }

   private void writeClip(Shape imclip) throws IOException {
      if (!this.clippingDisabled) {
         if (imclip != null) {
            this.handleUnsupportedFeature("Clipping is not supported. Shape: " + imclip);
         }

      }
   }

   public void draw(Shape s) {
      try {
         AffineTransform trans = this.getTransform();
         Shape imclip = this.getClip();
         this.writeClip(imclip);
         if (!Color.black.equals(this.getColor())) {
            this.handleUnsupportedFeature("Only black is supported as stroke color: " + this.getColor());
         }

         this.applyStroke(this.getStroke());
         PathIterator iter = s.getPathIterator(trans);
         this.processPathIteratorStroke(iter);
         this.writeClip((Shape)null);
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

   }

   public void fill(Shape s) {
      try {
         AffineTransform trans = this.getTransform();
         Shape imclip = this.getClip();
         this.writeClip(imclip);
         this.applyPaint(this.getPaint());
         PathIterator iter = s.getPathIterator(trans);
         this.processPathIteratorFill(iter);
         this.writeClip((Shape)null);
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

   }

   public void processPathIteratorStroke(PathIterator iter) throws IOException {
      this.gen.writeText("\n");
      double[] vals = new double[6];
      boolean penDown = false;
      double x = 0.0;
      double y = 0.0;
      StringBuffer sb = new StringBuffer(256);
      this.penUp(sb);

      while(!iter.isDone()) {
         int type = iter.currentSegment(vals);
         if (type == 4) {
            this.gen.writeText("PM;");
            this.gen.writeText(sb.toString());
            this.gen.writeText("PM2;EP;");
            sb.setLength(0);
            iter.next();
         } else {
            if (type == 0) {
               this.gen.writeText(sb.toString());
               sb.setLength(0);
               if (penDown) {
                  this.penUp(sb);
                  penDown = false;
               }
            } else if (!penDown) {
               this.penDown(sb);
               penDown = true;
            }

            switch (type) {
               case 0:
                  x = vals[0];
                  y = vals[1];
                  this.plotAbsolute(x, y, sb);
                  this.gen.writeText(sb.toString());
                  sb.setLength(0);
                  break;
               case 1:
                  x = vals[0];
                  y = vals[1];
                  this.plotAbsolute(x, y, sb);
                  break;
               case 2:
                  double originX = x;
                  double originY = y;
                  x = vals[2];
                  y = vals[3];
                  this.quadraticBezierAbsolute(originX, originY, vals[0], vals[1], x, y, sb);
                  break;
               case 3:
                  x = vals[4];
                  y = vals[5];
                  this.bezierAbsolute(vals[0], vals[1], vals[2], vals[3], x, y, sb);
               case 4:
            }

            iter.next();
         }
      }

      sb.append("\n");
      this.gen.writeText(sb.toString());
   }

   public void processPathIteratorFill(PathIterator iter) throws IOException {
      this.gen.writeText("\n");
      double[] vals = new double[6];
      boolean penDown = false;
      double x = 0.0;
      double y = 0.0;
      boolean pendingPM0 = true;
      StringBuffer sb = new StringBuffer(256);
      this.penUp(sb);

      while(!iter.isDone()) {
         int type = iter.currentSegment(vals);
         if (type == 4) {
            sb.append("PM1;");
            iter.next();
         } else {
            if (type == 0) {
               if (penDown) {
                  this.penUp(sb);
                  penDown = false;
               }
            } else if (!penDown) {
               this.penDown(sb);
               penDown = true;
            }

            switch (type) {
               case 0:
                  x = vals[0];
                  y = vals[1];
                  this.plotAbsolute(x, y, sb);
                  break;
               case 1:
                  x = vals[0];
                  y = vals[1];
                  this.plotAbsolute(x, y, sb);
                  break;
               case 2:
                  double originX = x;
                  double originY = y;
                  x = vals[2];
                  y = vals[3];
                  this.quadraticBezierAbsolute(originX, originY, vals[0], vals[1], x, y, sb);
                  break;
               case 3:
                  x = vals[4];
                  y = vals[5];
                  this.bezierAbsolute(vals[0], vals[1], vals[2], vals[3], x, y, sb);
                  break;
               default:
                  throw new IllegalStateException("Must not get here");
            }

            if (pendingPM0) {
               pendingPM0 = false;
               sb.append("PM;");
            }

            iter.next();
         }
      }

      sb.append("PM2;");
      this.fillPolygon(iter.getWindingRule(), sb);
      sb.append("\n");
      this.gen.writeText(sb.toString());
   }

   private void fillPolygon(int windingRule, StringBuffer sb) {
      int fillMethod = windingRule == 0 ? 0 : 1;
      sb.append("FP").append(fillMethod).append(";");
   }

   private void plotAbsolute(double x, double y, StringBuffer sb) {
      sb.append("PA").append(this.gen.formatDouble4(x));
      sb.append(",").append(this.gen.formatDouble4(y)).append(";");
   }

   private void bezierAbsolute(double x1, double y1, double x2, double y2, double x3, double y3, StringBuffer sb) {
      sb.append("BZ").append(this.gen.formatDouble4(x1));
      sb.append(",").append(this.gen.formatDouble4(y1));
      sb.append(",").append(this.gen.formatDouble4(x2));
      sb.append(",").append(this.gen.formatDouble4(y2));
      sb.append(",").append(this.gen.formatDouble4(x3));
      sb.append(",").append(this.gen.formatDouble4(y3)).append(";");
   }

   private void quadraticBezierAbsolute(double originX, double originY, double x1, double y1, double x2, double y2, StringBuffer sb) {
      double nx1 = originX + 0.6666666666666666 * (x1 - originX);
      double ny1 = originY + 0.6666666666666666 * (y1 - originY);
      double nx2 = nx1 + 0.3333333333333333 * (x2 - originX);
      double ny2 = ny1 + 0.3333333333333333 * (y2 - originY);
      this.bezierAbsolute(nx1, ny1, nx2, ny2, x2, y2, sb);
   }

   private void penDown(StringBuffer sb) {
      sb.append("PD;");
   }

   private void penUp(StringBuffer sb) {
      sb.append("PU;");
   }

   public void drawString(String s, float x, float y) {
      Font awtFont = this.getFont();
      FontRenderContext frc = this.getFontRenderContext();
      GlyphVector gv = awtFont.createGlyphVector(frc, s);
      Shape glyphOutline = gv.getOutline(x, y);
      this.fill(glyphOutline);
   }

   public void drawString(AttributedCharacterIterator iterator, float x, float y) {
      this.handleUnsupportedFeature("drawString NYI");
   }

   public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
      this.handleUnsupportedFeature("Bitmap images are not supported");
   }

   public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
      this.handleUnsupportedFeature("Bitmap images are not supported");
   }

   public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
      this.handleUnsupportedFeature("Bitmap images are not supported");
      return false;
   }

   public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
      this.handleUnsupportedFeature("Bitmap images are not supported");
      return false;
   }

   public void copyArea(int x, int y, int width, int height, int dx, int dy) {
      this.handleUnsupportedFeature("copyArea NYI");
   }

   public void setXORMode(Color c1) {
      this.handleUnsupportedFeature("setXORMode NYI");
   }

   protected BufferedImage buildBufferedImage(Dimension size) {
      return new BufferedImage(size.width, size.height, 10);
   }

   public FontMetrics getFontMetrics(Font f) {
      return this.fmg.getFontMetrics(f);
   }
}
