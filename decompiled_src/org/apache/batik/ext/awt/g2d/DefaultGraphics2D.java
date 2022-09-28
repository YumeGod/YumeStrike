package org.apache.batik.ext.awt.g2d;

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

   public DefaultGraphics2D(boolean var1) {
      super(var1);
      BufferedImage var2 = new BufferedImage(1, 1, 2);
      this.fmg = var2.createGraphics();
   }

   public DefaultGraphics2D(DefaultGraphics2D var1) {
      super(var1);
      BufferedImage var2 = new BufferedImage(1, 1, 2);
      this.fmg = var2.createGraphics();
   }

   public Graphics create() {
      return new DefaultGraphics2D(this);
   }

   public boolean drawImage(Image var1, int var2, int var3, ImageObserver var4) {
      System.err.println("drawImage");
      return true;
   }

   public boolean drawImage(Image var1, int var2, int var3, int var4, int var5, ImageObserver var6) {
      System.out.println("drawImage");
      return true;
   }

   public void dispose() {
      System.out.println("dispose");
   }

   public void draw(Shape var1) {
      System.out.println("draw(Shape)");
   }

   public void drawRenderedImage(RenderedImage var1, AffineTransform var2) {
      System.out.println("drawRenderedImage");
   }

   public void drawRenderableImage(RenderableImage var1, AffineTransform var2) {
      System.out.println("drawRenderableImage");
   }

   public void drawString(String var1, float var2, float var3) {
      System.out.println("drawString(String)");
   }

   public void drawString(AttributedCharacterIterator var1, float var2, float var3) {
      System.err.println("drawString(AttributedCharacterIterator)");
   }

   public void fill(Shape var1) {
      System.err.println("fill");
   }

   public GraphicsConfiguration getDeviceConfiguration() {
      System.out.println("getDeviceConviguration");
      return null;
   }

   public FontMetrics getFontMetrics(Font var1) {
      return this.fmg.getFontMetrics(var1);
   }

   public void setXORMode(Color var1) {
      System.out.println("setXORMode");
   }

   public void copyArea(int var1, int var2, int var3, int var4, int var5, int var6) {
      System.out.println("copyArea");
   }
}
