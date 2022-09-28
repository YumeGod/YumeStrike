package org.apache.batik.gvt.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import org.apache.batik.ext.awt.geom.RectListManager;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.PadRed;
import org.apache.batik.util.HaltingThread;

public class DynamicRenderer extends StaticRenderer {
   static final int COPY_OVERHEAD = 1000;
   static final int COPY_LINE_OVERHEAD = 10;
   RectListManager damagedAreas;

   public DynamicRenderer() {
   }

   public DynamicRenderer(RenderingHints var1, AffineTransform var2) {
      super(var1, var2);
   }

   protected CachableRed setupCache(CachableRed var1) {
      return var1;
   }

   public void flush(Rectangle var1) {
   }

   public void flush(Collection var1) {
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
         if (this.workingBaseRaster == null || this.workingBaseRaster.getWidth() < var2 || this.workingBaseRaster.getHeight() < var3) {
            var1 = var1.createCompatibleSampleModel(var2, var3);
            this.workingBaseRaster = Raster.createWritableRaster(var1, new Point(0, 0));
            this.workingRaster = this.workingBaseRaster.createWritableChild(0, 0, var2, var3, 0, 0, (int[])null);
            this.workingOffScreen = new BufferedImage(this.rootCR.getColorModel(), this.workingRaster, this.rootCR.getColorModel().isAlphaPremultiplied(), (Hashtable)null);
         }

         if (!this.isDoubleBuffered) {
            this.currentOffScreen = this.workingOffScreen;
            this.currentBaseRaster = this.workingBaseRaster;
            this.currentRaster = this.workingRaster;
         }

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

            boolean var7 = false;
            Rectangle var8 = var4.getBounds();
            synchronized(var3) {
               if (var7) {
                  ((CachableRed)var2).copyData(var4);
               } else {
                  Object var10 = null;
                  Color var11;
                  Color var12;
                  Iterator var13;
                  Rectangle var14;
                  WritableRaster var15;
                  if (this.isDoubleBuffered && this.currentRaster != null && this.damagedAreas != null) {
                     this.damagedAreas.subtract(var1, 1000, 10);
                     this.damagedAreas.mergeRects(1000, 10);
                     var11 = new Color(0, 0, 255, 50);
                     var12 = new Color(0, 0, 0, 50);
                     var13 = this.damagedAreas.iterator();

                     while(var13.hasNext()) {
                        var14 = (Rectangle)var13.next();
                        if (var8.intersects(var14)) {
                           var14 = var8.intersection(var14);
                           var15 = this.currentRaster.createWritableChild(var14.x, var14.y, var14.width, var14.height, var14.x, var14.y, (int[])null);
                           GraphicsUtil.copyData((Raster)var15, (WritableRaster)var4);
                           if (var10 != null) {
                              ((Graphics2D)var10).setPaint(var11);
                              ((Graphics2D)var10).fill(var14);
                              ((Graphics2D)var10).setPaint(var12);
                              ((Graphics2D)var10).draw(var14);
                           }
                        }
                     }
                  }

                  var11 = new Color(255, 0, 0, 50);
                  var12 = new Color(0, 0, 0, 50);
                  var13 = var1.iterator();

                  while(var13.hasNext()) {
                     var14 = (Rectangle)var13.next();
                     if (var8.intersects(var14)) {
                        var14 = var8.intersection(var14);
                        var15 = var4.createWritableChild(var14.x, var14.y, var14.width, var14.height, var14.x, var14.y, (int[])null);
                        ((CachableRed)var2).copyData(var15);
                        if (var10 != null) {
                           ((Graphics2D)var10).setPaint(var11);
                           ((Graphics2D)var10).fill(var14);
                           ((Graphics2D)var10).setPaint(var12);
                           ((Graphics2D)var10).draw(var14);
                        }
                     }
                  }
               }
            }

            if (!HaltingThread.hasBeenHalted()) {
               BufferedImage var9 = this.workingOffScreen;
               this.workingBaseRaster = this.currentBaseRaster;
               this.workingRaster = this.currentRaster;
               this.workingOffScreen = this.currentOffScreen;
               this.currentRaster = var4;
               this.currentBaseRaster = var3;
               this.currentOffScreen = var9;
               this.damagedAreas = var1;
            }
         }
      }
   }
}
