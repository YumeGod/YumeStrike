package de.javasoft.util.java2d;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;

public class Synthetica2DUtils {
   public static BufferedImage createBlurredImage(BufferedImage var0, int var1) {
      ConvolveOp var2 = createBlurOp(var1);
      BufferedImage var3 = var2.createCompatibleDestImage(var0, var0.getColorModel());
      var2.filter(var0, var3);
      return var3;
   }

   public static ConvolveOp createBlurOp(int var0) {
      float[] var1 = new float[var0 * var0];
      float var2 = 1.0F / (float)(var0 * var0);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var1[var3] = var2;
      }

      return new ConvolveOp(new Kernel(var0, var0, var1));
   }

   public static BufferedImage createAlphaImage(BufferedImage var0, float var1) {
      BufferedImage var2 = new BufferedImage(var0.getWidth(), var0.getHeight(), 2);
      Graphics2D var3 = (Graphics2D)var2.getGraphics();
      var3.setComposite(AlphaComposite.getInstance(3, var1));
      var3.drawImage(var0, 0, 0, (ImageObserver)null);
      var3.dispose();
      return var2;
   }

   public static BufferedImage createColorizedImage(BufferedImage var0, Color var1, float var2) {
      BufferedImage var3 = new BufferedImage(var0.getWidth(), var0.getHeight(), 2);
      colorizeImage(var0, var3, var1, var2);
      return var3;
   }

   public static void colorizeImage(BufferedImage var0, BufferedImage var1, Color var2, float var3) {
      Graphics2D var4 = (Graphics2D)var1.getGraphics();
      var4.drawImage(var0, 0, 0, (ImageObserver)null);
      var4.setComposite(AlphaComposite.getInstance(10, var3));
      var4.setColor(var2);
      var4.fillRect(0, 0, var1.getWidth(), var1.getHeight());
      var4.dispose();
   }

   public static Image flipHorizontal(Image var0) {
      BufferedImage var1 = new BufferedImage(var0.getWidth((ImageObserver)null), var0.getHeight((ImageObserver)null), 2);
      Graphics2D var2 = (Graphics2D)var1.getGraphics();
      var2.scale(-1.0, 1.0);
      var2.translate(-var0.getWidth((ImageObserver)null), 0);
      var2.drawImage(var0, 0, 0, (ImageObserver)null);
      var2.dispose();
      return var1;
   }

   public static Image flipVertical(Image var0) {
      BufferedImage var1 = new BufferedImage(var0.getWidth((ImageObserver)null), var0.getHeight((ImageObserver)null), 2);
      Graphics2D var2 = (Graphics2D)var1.getGraphics();
      var2.scale(1.0, -1.0);
      var2.translate(0, -var0.getHeight((ImageObserver)null));
      var2.drawImage(var0, 0, 0, (ImageObserver)null);
      var2.dispose();
      return var1;
   }
}
