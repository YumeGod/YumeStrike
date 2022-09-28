package org.apache.xmlgraphics.java2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;

public class DefaultGraphics2D extends AbstractGraphics2D {
   private Graphics2D fmg;

   public DefaultGraphics2D(boolean textAsShapes) {
      super(textAsShapes);
      BufferedImage bi = new BufferedImage(1, 1, 2);
      this.fmg = bi.createGraphics();
   }

   public DefaultGraphics2D(DefaultGraphics2D g) {
      super(g);
      BufferedImage bi = new BufferedImage(1, 1, 2);
      this.fmg = bi.createGraphics();
   }

   public Graphics create() {
      return new DefaultGraphics2D(this);
   }

   public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
      System.err.println("drawImage");
      return true;
   }

   public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
      System.out.println("drawImage");
      return true;
   }

   public void dispose() {
      System.out.println("dispose");
   }

   public void draw(Shape s) {
      System.out.println("draw(Shape)");
   }

   public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
      System.out.println("drawRenderedImage");
   }

   public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
      System.out.println("drawRenderableImage");
   }

   public void drawString(String s, float x, float y) {
      System.out.println("drawString(String)");
   }

   public void drawString(AttributedCharacterIterator iterator, float x, float y) {
      System.err.println("drawString(AttributedCharacterIterator)");
   }

   public void fill(Shape s) {
      System.err.println("fill");
   }

   public GraphicsConfiguration getDeviceConfiguration() {
      System.out.println("getDeviceConviguration");
      return null;
   }

   public FontMetrics getFontMetrics(Font f) {
      return this.fmg.getFontMetrics(f);
   }

   public void setXORMode(Color c1) {
      System.out.println("setXORMode");
   }

   public void copyArea(int x, int y, int width, int height, int dx, int dy) {
      System.out.println("copyArea");
   }
}
