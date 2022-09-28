package org.apache.batik.gvt.renderer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.apache.batik.ext.awt.geom.RectListManager;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.HaltingThread;

public class MacRenderer implements ImageRenderer {
   static final int COPY_OVERHEAD = 1000;
   static final int COPY_LINE_OVERHEAD = 10;
   static final AffineTransform IDENTITY = new AffineTransform();
   protected RenderingHints renderingHints = new RenderingHints((Map)null);
   protected AffineTransform usr2dev;
   protected GraphicsNode rootGN;
   protected int offScreenWidth;
   protected int offScreenHeight;
   protected boolean isDoubleBuffered;
   protected BufferedImage currImg;
   protected BufferedImage workImg;
   protected RectListManager damagedAreas;
   public static int IMAGE_TYPE = 3;
   public static Color TRANSPARENT_WHITE = new Color(255, 255, 255, 0);
   protected static RenderingHints defaultRenderingHints = new RenderingHints((Map)null);

   public MacRenderer() {
      this.renderingHints.add(defaultRenderingHints);
      this.usr2dev = new AffineTransform();
   }

   public MacRenderer(RenderingHints var1, AffineTransform var2) {
      this.renderingHints.add(var1);
      if (var2 == null) {
         new AffineTransform();
      } else {
         new AffineTransform(var2);
      }

   }

   public void dispose() {
      this.rootGN = null;
      this.currImg = null;
      this.workImg = null;
      this.renderingHints = null;
      this.usr2dev = null;
      if (this.damagedAreas != null) {
         this.damagedAreas.clear();
      }

      this.damagedAreas = null;
   }

   public void setTree(GraphicsNode var1) {
      this.rootGN = var1;
   }

   public GraphicsNode getTree() {
      return this.rootGN;
   }

   public void setTransform(AffineTransform var1) {
      if (var1 == null) {
         this.usr2dev = new AffineTransform();
      } else {
         this.usr2dev = new AffineTransform(var1);
      }

      if (this.workImg != null) {
         synchronized(this.workImg) {
            Graphics2D var3 = this.workImg.createGraphics();
            var3.setComposite(AlphaComposite.Clear);
            var3.fillRect(0, 0, this.workImg.getWidth(), this.workImg.getHeight());
            var3.dispose();
         }

         this.damagedAreas = null;
      }
   }

   public AffineTransform getTransform() {
      return this.usr2dev;
   }

   public void setRenderingHints(RenderingHints var1) {
      this.renderingHints = new RenderingHints((Map)null);
      this.renderingHints.add(var1);
      this.damagedAreas = null;
   }

   public RenderingHints getRenderingHints() {
      return this.renderingHints;
   }

   public boolean isDoubleBuffered() {
      return this.isDoubleBuffered;
   }

   public void setDoubleBuffered(boolean var1) {
      if (this.isDoubleBuffered != var1) {
         this.isDoubleBuffered = var1;
         if (var1) {
            this.workImg = null;
         } else {
            this.workImg = this.currImg;
            this.damagedAreas = null;
         }

      }
   }

   public void updateOffScreen(int var1, int var2) {
      this.offScreenWidth = var1;
      this.offScreenHeight = var2;
   }

   public BufferedImage getOffScreen() {
      return this.rootGN == null ? null : this.currImg;
   }

   public void clearOffScreen() {
      if (!this.isDoubleBuffered) {
         this.updateWorkingBuffers();
         if (this.workImg != null) {
            synchronized(this.workImg) {
               Graphics2D var2 = this.workImg.createGraphics();
               var2.setComposite(AlphaComposite.Clear);
               var2.fillRect(0, 0, this.workImg.getWidth(), this.workImg.getHeight());
               var2.dispose();
            }

            this.damagedAreas = null;
         }
      }
   }

   public void flush() {
   }

   public void flush(Rectangle var1) {
   }

   public void flush(Collection var1) {
   }

   protected void updateWorkingBuffers() {
      if (this.rootGN == null) {
         this.currImg = null;
         this.workImg = null;
      } else {
         int var1 = this.offScreenWidth;
         int var2 = this.offScreenHeight;
         if (this.workImg == null || this.workImg.getWidth() < var1 || this.workImg.getHeight() < var2) {
            this.workImg = new BufferedImage(var1, var2, IMAGE_TYPE);
         }

         if (!this.isDoubleBuffered) {
            this.currImg = this.workImg;
         }

      }
   }

   public void repaint(Shape var1) {
      if (var1 != null) {
         RectListManager var2 = new RectListManager();
         var2.add(this.usr2dev.createTransformedShape(var1).getBounds());
         this.repaint(var2);
      }
   }

   public void repaint(RectListManager var1) {
      if (var1 != null) {
         this.updateWorkingBuffers();
         if (this.rootGN != null && this.workImg != null) {
            try {
               synchronized(this.workImg) {
                  Graphics2D var3 = GraphicsUtil.createGraphics(this.workImg, this.renderingHints);
                  Rectangle var4 = new Rectangle(0, 0, this.offScreenWidth, this.offScreenHeight);
                  Iterator var5;
                  Rectangle var6;
                  if (this.isDoubleBuffered && this.currImg != null && this.damagedAreas != null) {
                     this.damagedAreas.subtract(var1, 1000, 10);
                     this.damagedAreas.mergeRects(1000, 10);
                     var5 = this.damagedAreas.iterator();
                     var3.setComposite(AlphaComposite.Src);

                     while(var5.hasNext()) {
                        var6 = (Rectangle)var5.next();
                        if (var4.intersects(var6)) {
                           var6 = var4.intersection(var6);
                           var3.setClip(var6.x, var6.y, var6.width, var6.height);
                           var3.setComposite(AlphaComposite.Clear);
                           var3.fillRect(var6.x, var6.y, var6.width, var6.height);
                           var3.setComposite(AlphaComposite.SrcOver);
                           var3.drawImage(this.currImg, 0, 0, (ImageObserver)null);
                        }
                     }
                  }

                  var5 = var1.iterator();

                  while(var5.hasNext()) {
                     var6 = (Rectangle)var5.next();
                     if (var4.intersects(var6)) {
                        var6 = var4.intersection(var6);
                        var3.setTransform(IDENTITY);
                        var3.setClip(var6.x, var6.y, var6.width, var6.height);
                        var3.setComposite(AlphaComposite.Clear);
                        var3.fillRect(var6.x, var6.y, var6.width, var6.height);
                        var3.setComposite(AlphaComposite.SrcOver);
                        var3.transform(this.usr2dev);
                        this.rootGN.paint(var3);
                     }
                  }

                  var3.dispose();
               }
            } catch (Throwable var9) {
               var9.printStackTrace();
            }

            if (!HaltingThread.hasBeenHalted()) {
               if (this.isDoubleBuffered) {
                  BufferedImage var2 = this.workImg;
                  this.workImg = this.currImg;
                  this.currImg = var2;
                  this.damagedAreas = var1;
               }

            }
         }
      }
   }

   static {
      defaultRenderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      defaultRenderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
   }
}
