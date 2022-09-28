package de.javasoft.util.java2d;

import de.javasoft.util.JavaVersion;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.ImagingOpException;

public class DropShadow {
   private boolean highQuality;
   private BufferedImage shadow;
   private BufferedImage originalImage;
   private float angle;
   private int distance;
   private int shadowSize;
   private float shadowOpacity;
   private Color shadowColor;
   private int distance_x;
   private int distance_y;

   protected DropShadow() {
      this.highQuality = true;
      this.shadow = null;
      this.originalImage = null;
      this.angle = 45.0F;
      this.distance = -5;
      this.shadowSize = 5;
      this.shadowOpacity = 0.8F;
      this.shadowColor = new Color(0);
      this.distance_x = 0;
      this.distance_y = 0;
      this.computeShadowPosition();
   }

   public DropShadow(BufferedImage var1) {
      this();
      this.setImage(var1);
   }

   public void setImage(BufferedImage var1) {
      this.originalImage = var1;
      this.refreshShadow();
   }

   public BufferedImage getImage() {
      return this.originalImage;
   }

   public boolean getHighQuality() {
      return this.highQuality;
   }

   public void setQuality(boolean var1) {
      this.highQuality = var1;
      this.refreshShadow();
   }

   public float getAngle() {
      return this.angle;
   }

   public void setAngle(float var1) {
      this.angle = var1;
      this.computeShadowPosition();
   }

   public int getDistance() {
      return this.distance;
   }

   public void setDistance(int var1) {
      this.distance = var1;
      this.computeShadowPosition();
   }

   public Color getShadowColor() {
      return this.shadowColor;
   }

   public void setShadowColor(Color var1) {
      this.shadowColor = var1;
      this.refreshShadow();
   }

   public float getShadowOpacity() {
      return this.shadowOpacity;
   }

   public void setShadowOpacity(float var1) {
      this.shadowOpacity = var1;
      this.refreshShadow();
   }

   public int getShadowSize() {
      return this.shadowSize;
   }

   public void setShadowSize(int var1) {
      this.shadowSize = var1;
      this.refreshShadow();
   }

   private void refreshShadow() {
      if (this.originalImage != null) {
         this.shadow = this.createDropShadow(this.originalImage);
      }

   }

   private void computeShadowPosition() {
      double var1 = Math.toRadians((double)this.angle);
      this.distance_x = (int)(Math.cos(var1) * (double)this.distance);
      this.distance_y = (int)(Math.sin(var1) * (double)this.distance);
   }

   private BufferedImage createDropShadow(BufferedImage var1) {
      BufferedImage var2 = new BufferedImage(var1.getWidth() + this.shadowSize * 2, var1.getHeight() + this.shadowSize * 2, 2);
      Graphics2D var3 = var2.createGraphics();
      var3.drawImage(var1, (BufferedImageOp)null, this.shadowSize, this.shadowSize);
      var3.dispose();
      if (this.highQuality) {
         BufferedImage var4 = this.createShadowMask(var2);

         try {
            Synthetica2DUtils.createBlurOp(this.shadowSize).filter(var4, var2);
         } catch (ImagingOpException var6) {
            if (!JavaVersion.JAVA7) {
               throw new RuntimeException(var6);
            }
         }
      } else {
         this.applyShadow(var2);
      }

      return var2;
   }

   private void applyShadow(BufferedImage var1) {
      int var2 = var1.getWidth();
      int var3 = var1.getHeight();
      int var4 = this.shadowSize - 1 >> 1;
      int var5 = this.shadowSize - var4;
      int var6 = var4;
      int var7 = var2 - var5;
      int var8 = var4;
      int var9 = var3 - var5;
      int var10 = this.shadowColor.getRGB() & 16777215;
      int[] var11 = new int[this.shadowSize];
      boolean var12 = false;
      int[] var14 = ((DataBufferInt)var1.getRaster().getDataBuffer()).getData();
      int var15 = var5 * var2;
      float var16 = this.shadowOpacity / (float)this.shadowSize;
      int var17 = 0;

      int var13;
      int var18;
      int var19;
      int var20;
      int var21;
      for(var18 = 0; var17 < var3; var18 = var17 * var2) {
         var13 = 0;
         var21 = 0;

         for(var19 = 0; var19 < this.shadowSize; ++var18) {
            var20 = var14[var18] >>> 24;
            var11[var19] = var20;
            var13 += var20;
            ++var19;
         }

         var18 -= var5;

         for(var19 = var6; var19 < var7; ++var18) {
            var20 = (int)((float)var13 * var16);
            var14[var18] = var20 << 24 | var10;
            var13 -= var11[var21];
            var20 = var14[var18 + var5] >>> 24;
            var11[var21] = var20;
            var13 += var20;
            ++var21;
            if (var21 >= this.shadowSize) {
               var21 -= this.shadowSize;
            }

            ++var19;
         }

         ++var17;
      }

      var17 = 0;

      for(var18 = 0; var17 < var2; var18 = var17) {
         var13 = 0;
         var21 = 0;

         for(var19 = 0; var19 < this.shadowSize; var18 += var2) {
            var20 = var14[var18] >>> 24;
            var11[var19] = var20;
            var13 += var20;
            ++var19;
         }

         var18 -= var15;

         for(var19 = var8; var19 < var9; var18 += var2) {
            var20 = (int)((float)var13 * var16);
            var14[var18] = var20 << 24 | var10;
            var13 -= var11[var21];
            var20 = var14[var18 + var15] >>> 24;
            var11[var21] = var20;
            var13 += var20;
            ++var21;
            if (var21 >= this.shadowSize) {
               var21 -= this.shadowSize;
            }

            ++var19;
         }

         ++var17;
      }

   }

   private BufferedImage createShadowMask(BufferedImage var1) {
      BufferedImage var2 = new BufferedImage(var1.getWidth(), var1.getHeight(), 2);
      Graphics2D var3 = var2.createGraphics();
      var3.drawImage(var1, 0, 0, (ImageObserver)null);
      var3.setComposite(AlphaComposite.getInstance(5, this.shadowOpacity));
      var3.setColor(this.shadowColor);
      var3.fillRect(0, 0, var1.getWidth(), var1.getHeight());
      var3.dispose();
      return var2;
   }

   public void paintShadow(Graphics var1, int var2, int var3) {
      if (this.shadow != null) {
         var1.drawImage(this.shadow, var2 + this.distance_x, var3 + this.distance_y, (ImageObserver)null);
      }

   }

   public void paint(Graphics var1, int var2, int var3) {
      this.paint(var1, var2, var3, true);
   }

   public void paint(Graphics var1, int var2, int var3, boolean var4) {
      if (var4) {
         this.paintShadow(var1, var2, var3);
      }

      if (this.originalImage != null) {
         var1.drawImage(this.originalImage, var2, var3, (ImageObserver)null);
      }

   }
}
