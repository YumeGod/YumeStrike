package org.apache.batik.bridge;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.batik.ext.awt.geom.RectListManager;
import org.apache.batik.gvt.renderer.ImageRenderer;

public class RepaintManager {
   static final int COPY_OVERHEAD = 10000;
   static final int COPY_LINE_OVERHEAD = 10;
   protected ImageRenderer renderer;

   public RepaintManager(ImageRenderer var1) {
      this.renderer = var1;
   }

   public Collection updateRendering(Collection var1) throws InterruptedException {
      this.renderer.flush(var1);
      ArrayList var2 = new ArrayList(var1.size());
      AffineTransform var3 = this.renderer.getTransform();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Shape var5 = (Shape)var4.next();
         var5 = var3.createTransformedShape(var5);
         Rectangle2D var6 = var5.getBounds2D();
         int var7 = (int)Math.floor(var6.getX());
         int var8 = (int)Math.floor(var6.getY());
         int var9 = (int)Math.ceil(var6.getX() + var6.getWidth());
         int var10 = (int)Math.ceil(var6.getY() + var6.getHeight());
         Rectangle var11 = new Rectangle(var7 - 1, var8 - 1, var9 - var7 + 3, var10 - var8 + 3);
         var2.add(var11);
      }

      RectListManager var13 = null;

      try {
         var13 = new RectListManager(var2);
         var13.mergeRects(10000, 10);
      } catch (Exception var12) {
         var12.printStackTrace();
      }

      this.renderer.repaint(var13);
      return var13;
   }

   public void setupRenderer(AffineTransform var1, boolean var2, Shape var3, int var4, int var5) {
      this.renderer.setTransform(var1);
      this.renderer.setDoubleBuffered(var2);
      this.renderer.updateOffScreen(var4, var5);
      this.renderer.clearOffScreen();
   }

   public BufferedImage getOffScreen() {
      return this.renderer.getOffScreen();
   }
}
