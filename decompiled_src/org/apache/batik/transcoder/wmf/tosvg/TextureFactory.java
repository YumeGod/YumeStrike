package org.apache.batik.transcoder.wmf.tosvg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TextureFactory {
   private static TextureFactory fac = null;
   private Map textures = new HashMap(1);
   private static final int SIZE = 10;
   private float scale = 1.0F;

   private TextureFactory(float var1) {
   }

   public static TextureFactory getInstance() {
      if (fac == null) {
         fac = new TextureFactory(1.0F);
      }

      return fac;
   }

   public static TextureFactory getInstance(float var0) {
      if (fac == null) {
         fac = new TextureFactory(var0);
      }

      return fac;
   }

   public void reset() {
      this.textures.clear();
   }

   public Paint getTexture(int var1) {
      Integer var2 = new Integer(var1);
      Paint var3;
      if (this.textures.containsKey(var2)) {
         var3 = (Paint)this.textures.get(var2);
         return var3;
      } else {
         var3 = this.createTexture(var1, (Color)null, (Color)null);
         if (var3 != null) {
            this.textures.put(var2, var3);
         }

         return var3;
      }
   }

   public Paint getTexture(int var1, Color var2) {
      ColoredTexture var3 = new ColoredTexture(var1, var2, (Color)null);
      Paint var4;
      if (this.textures.containsKey(var3)) {
         var4 = (Paint)this.textures.get(var3);
         return var4;
      } else {
         var4 = this.createTexture(var1, var2, (Color)null);
         if (var4 != null) {
            this.textures.put(var3, var4);
         }

         return var4;
      }
   }

   public Paint getTexture(int var1, Color var2, Color var3) {
      ColoredTexture var4 = new ColoredTexture(var1, var2, var3);
      Paint var5;
      if (this.textures.containsKey(var4)) {
         var5 = (Paint)this.textures.get(var4);
         return var5;
      } else {
         var5 = this.createTexture(var1, var2, var3);
         if (var5 != null) {
            this.textures.put(var4, var5);
         }

         return var5;
      }
   }

   private Paint createTexture(int var1, Color var2, Color var3) {
      BufferedImage var4 = new BufferedImage(10, 10, 2);
      Graphics2D var5 = var4.createGraphics();
      Rectangle2D.Float var6 = new Rectangle2D.Float(0.0F, 0.0F, 10.0F, 10.0F);
      TexturePaint var7 = null;
      boolean var8 = false;
      if (var3 != null) {
         var5.setColor(var3);
         var5.fillRect(0, 0, 10, 10);
      }

      if (var2 == null) {
         var5.setColor(Color.black);
      } else {
         var5.setColor(var2);
      }

      int var9;
      if (var1 == 1) {
         for(var9 = 0; var9 < 5; ++var9) {
            var5.drawLine(var9 * 10, 0, var9 * 10, 10);
         }

         var8 = true;
      } else if (var1 == 0) {
         for(var9 = 0; var9 < 5; ++var9) {
            var5.drawLine(0, var9 * 10, 10, var9 * 10);
         }

         var8 = true;
      } else if (var1 == 3) {
         for(var9 = 0; var9 < 5; ++var9) {
            var5.drawLine(0, var9 * 10, var9 * 10, 0);
         }

         var8 = true;
      } else if (var1 == 2) {
         for(var9 = 0; var9 < 5; ++var9) {
            var5.drawLine(0, var9 * 10, 10 - var9 * 10, 10);
         }

         var8 = true;
      } else if (var1 == 5) {
         for(var9 = 0; var9 < 5; ++var9) {
            var5.drawLine(0, var9 * 10, var9 * 10, 0);
            var5.drawLine(0, var9 * 10, 10 - var9 * 10, 10);
         }

         var8 = true;
      } else if (var1 == 4) {
         for(var9 = 0; var9 < 5; ++var9) {
            var5.drawLine(var9 * 10, 0, var9 * 10, 10);
            var5.drawLine(0, var9 * 10, 10, var9 * 10);
         }

         var8 = true;
      }

      var4.flush();
      if (var8) {
         var7 = new TexturePaint(var4, var6);
      }

      return var7;
   }

   private class ColoredTexture {
      final int textureId;
      final Color foreground;
      final Color background;

      ColoredTexture(int var2, Color var3, Color var4) {
         this.textureId = var2;
         this.foreground = var3;
         this.background = var4;
      }
   }
}
