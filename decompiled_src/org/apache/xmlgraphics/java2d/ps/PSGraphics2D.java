package org.apache.xmlgraphics.java2d.ps;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.IOException;
import java.util.List;
import org.apache.xmlgraphics.java2d.AbstractGraphics2D;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.StrokingTextHandler;
import org.apache.xmlgraphics.java2d.TextHandler;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSImageUtils;

public class PSGraphics2D extends AbstractGraphics2D {
   private static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();
   private static final boolean DEBUG = false;
   protected PSGraphics2D rootG2D;
   protected PSGenerator gen;
   protected boolean clippingDisabled;
   protected TextHandler fallbackTextHandler;
   protected TextHandler customTextHandler;
   protected Color currentColour;
   private Graphics2D fmg;

   public PSGraphics2D(boolean textAsShapes) {
      super(textAsShapes);
      this.clippingDisabled = false;
      this.fallbackTextHandler = new StrokingTextHandler();
      this.currentColour = new Color(0, 0, 0);
      BufferedImage bi = new BufferedImage(1, 1, 2);
      this.fmg = bi.createGraphics();
   }

   public PSGraphics2D(boolean textAsShapes, PSGenerator gen) {
      this(textAsShapes);
      this.setPSGenerator(gen);
   }

   public PSGraphics2D(PSGraphics2D g) {
      super(g);
      this.clippingDisabled = false;
      this.fallbackTextHandler = new StrokingTextHandler();
      this.currentColour = new Color(0, 0, 0);
      BufferedImage bi = new BufferedImage(1, 1, 2);
      this.fmg = bi.createGraphics();
      this.rootG2D = g.rootG2D != null ? g.rootG2D : g;
      this.setPSGenerator(g.gen);
      this.clippingDisabled = g.clippingDisabled;
      this.customTextHandler = g.customTextHandler;
      this.currentColour = g.currentColour;
   }

   public void setPSGenerator(PSGenerator gen) {
      this.gen = gen;
   }

   public PSGenerator getPSGenerator() {
      return this.gen;
   }

   public void setGraphicContext(GraphicContext c) {
      this.gc = c;
   }

   public TextHandler getFallbackTextHandler() {
      return this.fallbackTextHandler;
   }

   public TextHandler getCustomTextHandler() {
      return this.customTextHandler;
   }

   public void setCustomTextHandler(TextHandler handler) {
      this.customTextHandler = handler;
   }

   public void disableClipping(boolean b) {
      this.clippingDisabled = b;
   }

   public Graphics create() {
      this.preparePainting();
      return new PSGraphics2D(this);
   }

   public void handleIOException(IOException ioe) {
      ioe.printStackTrace();
   }

   public void preparePainting() {
      if (this.rootG2D != null) {
         this.rootG2D.preparePainting();
      }

   }

   public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
      this.preparePainting();
      int width = img.getWidth(observer);
      int height = img.getHeight(observer);
      if (width != -1 && height != -1) {
         Dimension size = new Dimension(width, height);
         BufferedImage buf = this.buildBufferedImage(size);
         Graphics2D g = buf.createGraphics();
         g.setComposite(AlphaComposite.SrcOver);
         g.setBackground(new Color(1, 1, 1, 0));
         g.setPaint(new Color(1, 1, 1, 0));
         g.fillRect(0, 0, width, height);
         g.clip(new Rectangle(0, 0, buf.getWidth(), buf.getHeight()));
         if (!g.drawImage(img, 0, 0, observer)) {
            return false;
         } else {
            g.dispose();

            try {
               AffineTransform at = this.getTransform();
               this.gen.saveGraphicsState();
               this.gen.concatMatrix(at);
               Shape imclip = this.getClip();
               this.writeClip(imclip);
               PSImageUtils.renderBitmapImage(buf, (float)x, (float)y, (float)width, (float)height, this.gen);
               this.gen.restoreGraphicsState();
            } catch (IOException var12) {
               this.handleIOException(var12);
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public BufferedImage buildBufferedImage(Dimension size) {
      return new BufferedImage(size.width, size.height, 2);
   }

   public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
      this.preparePainting();
      System.err.println("NYI: drawImage");
      return true;
   }

   public void dispose() {
      this.gen = null;
      this.fallbackTextHandler = null;
      this.customTextHandler = null;
      this.currentColour = null;
   }

   public int processShape(Shape s) throws IOException {
      if (s instanceof Rectangle2D) {
         Rectangle2D r = (Rectangle2D)s;
         this.gen.defineRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
         return 1;
      } else {
         PathIterator iter = s.getPathIterator(IDENTITY_TRANSFORM);
         this.processPathIterator(iter);
         return iter.getWindingRule();
      }
   }

   public void processPathIterator(PathIterator iter) throws IOException {
      for(double[] vals = new double[6]; !iter.isDone(); iter.next()) {
         int type = iter.currentSegment(vals);
         switch (type) {
            case 0:
               this.gen.writeln(this.gen.formatDouble(vals[0]) + " " + this.gen.formatDouble(vals[1]) + " " + this.gen.mapCommand("moveto"));
               break;
            case 1:
               this.gen.writeln(this.gen.formatDouble(vals[0]) + " " + this.gen.formatDouble(vals[1]) + " " + this.gen.mapCommand("lineto"));
               break;
            case 2:
               this.gen.writeln(this.gen.formatDouble(vals[0]) + " " + this.gen.formatDouble(vals[1]) + " " + this.gen.formatDouble(vals[2]) + " " + this.gen.formatDouble(vals[3]) + " QT");
               break;
            case 3:
               this.gen.writeln(this.gen.formatDouble(vals[0]) + " " + this.gen.formatDouble(vals[1]) + " " + this.gen.formatDouble(vals[2]) + " " + this.gen.formatDouble(vals[3]) + " " + this.gen.formatDouble(vals[4]) + " " + this.gen.formatDouble(vals[5]) + " " + this.gen.mapCommand("curveto"));
               break;
            case 4:
               this.gen.writeln(this.gen.mapCommand("closepath"));
         }
      }

   }

   public void draw(Shape s) {
      this.preparePainting();

      try {
         this.gen.saveGraphicsState();
         AffineTransform trans = this.getTransform();
         boolean newTransform = this.gen.getCurrentState().checkTransform(trans) && !trans.isIdentity();
         if (newTransform) {
            this.gen.concatMatrix(trans);
         }

         Shape imclip = this.getClip();
         if (this.shouldBeClipped(imclip, s)) {
            this.writeClip(imclip);
         }

         this.establishColor(this.getColor());
         this.applyPaint(this.getPaint(), false);
         this.applyStroke(this.getStroke());
         this.gen.writeln(this.gen.mapCommand("newpath"));
         this.processShape(s);
         this.doDrawing(false, true, false);
         this.gen.restoreGraphicsState();
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

   }

   public boolean shouldBeClipped(Shape clip, Shape s) {
      if (clip != null && s != null) {
         Area as = new Area(s);
         Area imclip = new Area(clip);
         imclip.intersect(as);
         return !imclip.equals(as);
      } else {
         return false;
      }
   }

   public void writeClip(Shape s) {
      if (s != null) {
         if (!this.clippingDisabled) {
            this.preparePainting();

            try {
               this.gen.writeln(this.gen.mapCommand("newpath"));
               this.processShape(s);
               this.gen.writeln(this.gen.mapCommand("clip"));
            } catch (IOException var3) {
               this.handleIOException(var3);
            }
         }

      }
   }

   protected void applyPaint(Paint paint, boolean fill) {
      this.preparePainting();
      if (paint instanceof GradientPaint) {
         System.err.println("NYI: Gradient paint");
      } else if (paint instanceof TexturePaint && fill) {
         try {
            PSTilingPattern psTilingPattern = new PSTilingPattern("Pattern1", (TexturePaint)paint, 0.0, 0.0, 3, (List)null);
            this.gen.write(psTilingPattern.toString());
            this.gen.writeln("/Pattern " + this.gen.mapCommand("setcolorspace"));
            this.gen.writeln(psTilingPattern.getName() + " " + this.gen.mapCommand("setcolor"));
         } catch (IOException var4) {
            this.handleIOException(var4);
         }
      }

   }

   protected void applyStroke(Stroke stroke) {
      this.preparePainting();

      try {
         applyStroke(stroke, this.gen);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

   }

   public static void applyStroke(Stroke stroke, PSGenerator gen) throws IOException {
      if (stroke instanceof BasicStroke) {
         BasicStroke basicStroke = (BasicStroke)stroke;
         float[] da = basicStroke.getDashArray();
         int lj;
         if (da == null) {
            gen.useDash((String)null);
         } else {
            StringBuffer sb = new StringBuffer("[");

            for(lj = 0; lj < da.length; ++lj) {
               sb.append(gen.formatDouble((double)da[lj]));
               if (lj < da.length - 1) {
                  sb.append(" ");
               }
            }

            sb.append("] ");
            float offset = basicStroke.getDashPhase();
            sb.append(gen.formatDouble((double)offset));
            gen.useDash(sb.toString());
         }

         int ec = basicStroke.getEndCap();
         switch (ec) {
            case 0:
               gen.useLineCap(0);
               break;
            case 1:
               gen.useLineCap(1);
               break;
            case 2:
               gen.useLineCap(2);
               break;
            default:
               System.err.println("Unsupported line cap: " + ec);
         }

         lj = basicStroke.getLineJoin();
         float lw;
         switch (lj) {
            case 0:
               gen.useLineJoin(0);
               lw = basicStroke.getMiterLimit();
               gen.useMiterLimit(lw >= -1.0F ? lw : 1.0F);
               break;
            case 1:
               gen.useLineJoin(1);
               break;
            case 2:
               gen.useLineJoin(2);
               break;
            default:
               System.err.println("Unsupported line join: " + lj);
         }

         lw = basicStroke.getLineWidth();
         gen.useLineWidth((double)lw);
      } else {
         System.err.println("Stroke not supported: " + stroke.toString());
      }

   }

   public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
      this.preparePainting();

      try {
         AffineTransform at = this.getTransform();
         this.gen.saveGraphicsState();
         this.gen.concatMatrix(at);
         this.gen.concatMatrix(xform);
         Shape imclip = this.getClip();
         this.writeClip(imclip);
         PSImageUtils.renderBitmapImage(img, 0.0F, 0.0F, (float)img.getWidth(), (float)img.getHeight(), this.gen);
         this.gen.restoreGraphicsState();
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

   }

   public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
      this.preparePainting();
      System.err.println("NYI: drawRenderableImage");
   }

   public void establishColor(Color c) throws IOException {
      this.gen.useColor(c);
   }

   public void drawString(String s, float x, float y) {
      try {
         if (this.customTextHandler != null && !this.textAsShapes) {
            this.customTextHandler.drawString(this, s, x, y);
         } else {
            this.fallbackTextHandler.drawString(this, s, x, y);
         }
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

   }

   public void fill(Shape s) {
      this.preparePainting();

      try {
         this.gen.saveGraphicsState();
         AffineTransform trans = this.getTransform();
         boolean newTransform = this.gen.getCurrentState().checkTransform(trans) && !trans.isIdentity();
         if (newTransform) {
            this.gen.concatMatrix(trans);
         }

         Shape imclip = this.getClip();
         if (this.shouldBeClipped(imclip, s)) {
            this.writeClip(imclip);
         }

         this.establishColor(this.getColor());
         this.applyPaint(this.getPaint(), true);
         this.gen.writeln(this.gen.mapCommand("newpath"));
         int windingRule = this.processShape(s);
         this.doDrawing(true, false, windingRule == 0);
         this.gen.restoreGraphicsState();
      } catch (IOException var6) {
         this.handleIOException(var6);
      }

   }

   protected void doDrawing(boolean fill, boolean stroke, boolean nonzero) throws IOException {
      this.preparePainting();
      if (fill) {
         if (stroke) {
            if (!nonzero) {
               this.gen.writeln(this.gen.mapCommand("gsave") + " " + this.gen.mapCommand("fill") + " " + this.gen.mapCommand("grestore") + " " + this.gen.mapCommand("stroke"));
            } else {
               this.gen.writeln(this.gen.mapCommand("gsave") + " " + this.gen.mapCommand("eofill") + " " + this.gen.mapCommand("grestore") + " " + this.gen.mapCommand("stroke"));
            }
         } else if (!nonzero) {
            this.gen.writeln(this.gen.mapCommand("fill"));
         } else {
            this.gen.writeln(this.gen.mapCommand("eofill"));
         }
      } else {
         this.gen.writeln(this.gen.mapCommand("stroke"));
      }

   }

   public GraphicsConfiguration getDeviceConfiguration() {
      return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
   }

   public FontMetrics getFontMetrics(Font f) {
      return this.fmg.getFontMetrics(f);
   }

   public void setXORMode(Color c1) {
      System.err.println("NYI: setXORMode");
   }

   public void copyArea(int x, int y, int width, int height, int dx, int dy) {
      System.err.println("NYI: copyArea");
   }
}
