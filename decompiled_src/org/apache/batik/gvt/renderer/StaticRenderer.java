package org.apache.batik.gvt.renderer;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.RenderContext;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import org.apache.batik.ext.awt.geom.RectListManager;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.PadRed;
import org.apache.batik.ext.awt.image.rendered.TileCacheRed;
import org.apache.batik.ext.awt.image.rendered.TranslateRed;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.HaltingThread;

public class StaticRenderer implements ImageRenderer {
   protected GraphicsNode rootGN;
   protected Filter rootFilter;
   protected CachableRed rootCR;
   protected SoftReference lastCR;
   protected SoftReference lastCache;
   protected boolean isDoubleBuffered = false;
   protected WritableRaster currentBaseRaster;
   protected WritableRaster currentRaster;
   protected BufferedImage currentOffScreen;
   protected WritableRaster workingBaseRaster;
   protected WritableRaster workingRaster;
   protected BufferedImage workingOffScreen;
   protected int offScreenWidth;
   protected int offScreenHeight;
   protected RenderingHints renderingHints = new RenderingHints((Map)null);
   protected AffineTransform usr2dev;
   protected static RenderingHints defaultRenderingHints = new RenderingHints((Map)null);

   public StaticRenderer(RenderingHints var1, AffineTransform var2) {
      this.renderingHints.add(var1);
      this.usr2dev = new AffineTransform(var2);
   }

   public StaticRenderer() {
      this.renderingHints.add(defaultRenderingHints);
      this.usr2dev = new AffineTransform();
   }

   public void dispose() {
      this.rootGN = null;
      this.rootFilter = null;
      this.rootCR = null;
      this.workingOffScreen = null;
      this.workingBaseRaster = null;
      this.workingRaster = null;
      this.currentOffScreen = null;
      this.currentBaseRaster = null;
      this.currentRaster = null;
      this.renderingHints = null;
      this.lastCache = null;
      this.lastCR = null;
   }

   public void setTree(GraphicsNode var1) {
      this.rootGN = var1;
      this.rootFilter = null;
      this.rootCR = null;
      this.workingOffScreen = null;
      this.workingRaster = null;
      this.currentOffScreen = null;
      this.currentRaster = null;
   }

   public GraphicsNode getTree() {
      return this.rootGN;
   }

   public void setRenderingHints(RenderingHints var1) {
      this.renderingHints = new RenderingHints((Map)null);
      this.renderingHints.add(var1);
      this.rootFilter = null;
      this.rootCR = null;
      this.workingOffScreen = null;
      this.workingRaster = null;
      this.currentOffScreen = null;
      this.currentRaster = null;
   }

   public RenderingHints getRenderingHints() {
      return this.renderingHints;
   }

   public void setTransform(AffineTransform var1) {
      if (!this.usr2dev.equals(var1)) {
         if (var1 == null) {
            this.usr2dev = new AffineTransform();
         } else {
            this.usr2dev = new AffineTransform(var1);
         }

         this.rootCR = null;
      }
   }

   public AffineTransform getTransform() {
      return this.usr2dev;
   }

   public boolean isDoubleBuffered() {
      return this.isDoubleBuffered;
   }

   public void setDoubleBuffered(boolean var1) {
      if (this.isDoubleBuffered != var1) {
         this.isDoubleBuffered = var1;
         if (var1) {
            this.currentOffScreen = null;
            this.currentBaseRaster = null;
            this.currentRaster = null;
         } else {
            this.currentOffScreen = this.workingOffScreen;
            this.currentBaseRaster = this.workingBaseRaster;
            this.currentRaster = this.workingRaster;
         }

      }
   }

   public void updateOffScreen(int var1, int var2) {
      this.offScreenWidth = var1;
      this.offScreenHeight = var2;
   }

   public BufferedImage getOffScreen() {
      return this.rootGN == null ? null : this.currentOffScreen;
   }

   public void clearOffScreen() {
      if (!this.isDoubleBuffered) {
         this.updateWorkingBuffers();
         if (this.rootCR != null && this.workingBaseRaster != null) {
            ColorModel var1 = this.rootCR.getColorModel();
            WritableRaster var2 = this.workingBaseRaster;
            synchronized(var2) {
               BufferedImage var4 = new BufferedImage(var1, this.workingBaseRaster, var1.isAlphaPremultiplied(), (Hashtable)null);
               Graphics2D var5 = var4.createGraphics();
               var5.setComposite(AlphaComposite.Clear);
               var5.fillRect(0, 0, var4.getWidth(), var4.getHeight());
               var5.dispose();
            }
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
         if (this.rootCR != null && this.workingBaseRaster != null) {
            Object var2 = this.rootCR;
            WritableRaster var3 = this.workingBaseRaster;
            WritableRaster var4 = this.workingRaster;
            Rectangle var5 = this.rootCR.getBounds();
            Rectangle var6 = this.workingRaster.getBounds();
            if (var6.x < var5.x || var6.y < var5.y || var6.x + var6.width > var5.x + var5.width || var6.y + var6.height > var5.y + var5.height) {
               var2 = new PadRed((CachableRed)var2, var6, PadMode.ZERO_PAD, (RenderingHints)null);
            }

            synchronized(var3) {
               ((CachableRed)var2).copyData(var4);
            }

            if (!HaltingThread.hasBeenHalted()) {
               BufferedImage var7 = this.workingOffScreen;
               this.workingBaseRaster = this.currentBaseRaster;
               this.workingRaster = this.currentRaster;
               this.workingOffScreen = this.currentOffScreen;
               this.currentRaster = var4;
               this.currentBaseRaster = var3;
               this.currentOffScreen = var7;
            }

         }
      }
   }

   public void flush() {
      if (this.lastCache != null) {
         Object var1 = this.lastCache.get();
         if (var1 != null) {
            TileCacheRed var2 = (TileCacheRed)var1;
            var2.flushCache(var2.getBounds());
         }
      }
   }

   public void flush(Collection var1) {
      AffineTransform var2 = this.getTransform();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Shape var4 = (Shape)var3.next();
         Rectangle var5 = var2.createTransformedShape(var4).getBounds();
         this.flush(var5);
      }

   }

   public void flush(Rectangle var1) {
      if (this.lastCache != null) {
         Object var2 = this.lastCache.get();
         if (var2 != null) {
            TileCacheRed var3 = (TileCacheRed)var2;
            var1 = (Rectangle)var1.clone();
            var1.x -= Math.round((float)this.usr2dev.getTranslateX());
            var1.y -= Math.round((float)this.usr2dev.getTranslateY());
            var3.flushCache(var1);
         }
      }
   }

   protected CachableRed setupCache(CachableRed var1) {
      if (this.lastCR == null || var1 != this.lastCR.get()) {
         this.lastCR = new SoftReference(var1);
         this.lastCache = null;
      }

      Object var2 = null;
      if (this.lastCache != null) {
         var2 = this.lastCache.get();
      }

      if (var2 != null) {
         return (CachableRed)var2;
      } else {
         TileCacheRed var3 = new TileCacheRed(var1);
         this.lastCache = new SoftReference(var3);
         return var3;
      }
   }

   protected CachableRed renderGNR() {
      AffineTransform var1 = this.usr2dev;
      AffineTransform var2 = new AffineTransform(var1.getScaleX(), var1.getShearY(), var1.getShearX(), var1.getScaleY(), 0.0, 0.0);
      RenderContext var3 = new RenderContext(var2, (Shape)null, this.renderingHints);
      RenderedImage var4 = this.rootFilter.createRendering(var3);
      if (var4 == null) {
         return null;
      } else {
         CachableRed var5 = GraphicsUtil.wrap(var4);
         var5 = this.setupCache(var5);
         int var6 = Math.round((float)var1.getTranslateX());
         int var7 = Math.round((float)var1.getTranslateY());
         TranslateRed var8 = new TranslateRed(var5, var5.getMinX() + var6, var5.getMinY() + var7);
         var5 = GraphicsUtil.convertTosRGB(var8);
         return var5;
      }
   }

   protected void updateWorkingBuffers() {
      if (this.rootFilter == null) {
         this.rootFilter = this.rootGN.getGraphicsNodeRable(true);
         this.rootCR = null;
      }

      this.rootCR = this.renderGNR();
      if (this.rootCR == null) {
         this.workingRaster = null;
         this.workingOffScreen = null;
         this.workingBaseRaster = null;
         this.currentOffScreen = null;
         this.currentBaseRaster = null;
         this.currentRaster = null;
      } else {
         SampleModel var1 = this.rootCR.getSampleModel();
         int var2 = this.offScreenWidth;
         int var3 = this.offScreenHeight;
         int var4 = var1.getWidth();
         int var5 = var1.getHeight();
         var2 = ((var2 + var4 - 1) / var4 + 1) * var4;
         var3 = ((var3 + var5 - 1) / var5 + 1) * var5;
         if (this.workingBaseRaster == null || this.workingBaseRaster.getWidth() < var2 || this.workingBaseRaster.getHeight() < var3) {
            var1 = var1.createCompatibleSampleModel(var2, var3);
            this.workingBaseRaster = Raster.createWritableRaster(var1, new Point(0, 0));
         }

         int var6 = -this.rootCR.getTileGridXOffset();
         int var7 = -this.rootCR.getTileGridYOffset();
         int var8;
         if (var6 >= 0) {
            var8 = var6 / var4;
         } else {
            var8 = (var6 - var4 + 1) / var4;
         }

         int var9;
         if (var7 >= 0) {
            var9 = var7 / var5;
         } else {
            var9 = (var7 - var5 + 1) / var5;
         }

         int var10 = var8 * var4 - var6;
         int var11 = var9 * var5 - var7;
         this.workingRaster = this.workingBaseRaster.createWritableChild(0, 0, var2, var3, var10, var11, (int[])null);
         this.workingOffScreen = new BufferedImage(this.rootCR.getColorModel(), this.workingRaster.createWritableChild(0, 0, this.offScreenWidth, this.offScreenHeight, 0, 0, (int[])null), this.rootCR.getColorModel().isAlphaPremultiplied(), (Hashtable)null);
         if (!this.isDoubleBuffered) {
            this.currentOffScreen = this.workingOffScreen;
            this.currentBaseRaster = this.workingBaseRaster;
            this.currentRaster = this.workingRaster;
         }

      }
   }

   static {
      defaultRenderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      defaultRenderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
   }
}
