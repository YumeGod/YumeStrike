package org.apache.batik.swing.gvt;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.bridge.InterruptedBridgeException;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.util.EventDispatcher;
import org.apache.batik.util.HaltingThread;

public class GVTTreeRenderer extends HaltingThread {
   protected ImageRenderer renderer;
   protected Shape areaOfInterest;
   protected int width;
   protected int height;
   protected AffineTransform user2DeviceTransform;
   protected boolean doubleBuffering;
   protected List listeners = Collections.synchronizedList(new LinkedList());
   static EventDispatcher.Dispatcher prepareDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((GVTTreeRendererListener)var1).gvtRenderingPrepare((GVTTreeRendererEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher startedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((GVTTreeRendererListener)var1).gvtRenderingStarted((GVTTreeRendererEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher cancelledDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((GVTTreeRendererListener)var1).gvtRenderingCancelled((GVTTreeRendererEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher completedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((GVTTreeRendererListener)var1).gvtRenderingCompleted((GVTTreeRendererEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher failedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((GVTTreeRendererListener)var1).gvtRenderingFailed((GVTTreeRendererEvent)var2);
      }
   };

   public GVTTreeRenderer(ImageRenderer var1, AffineTransform var2, boolean var3, Shape var4, int var5, int var6) {
      this.renderer = var1;
      this.areaOfInterest = var4;
      this.user2DeviceTransform = var2;
      this.doubleBuffering = var3;
      this.width = var5;
      this.height = var6;
   }

   public void run() {
      GVTTreeRendererEvent var1 = new GVTTreeRendererEvent(this, (BufferedImage)null);

      try {
         this.fireEvent(prepareDispatcher, var1);
         this.renderer.setTransform(this.user2DeviceTransform);
         this.renderer.setDoubleBuffered(this.doubleBuffering);
         this.renderer.updateOffScreen(this.width, this.height);
         this.renderer.clearOffScreen();
         if (this.isHalted()) {
            this.fireEvent(cancelledDispatcher, var1);
            return;
         }

         var1 = new GVTTreeRendererEvent(this, this.renderer.getOffScreen());
         this.fireEvent(startedDispatcher, var1);
         if (this.isHalted()) {
            this.fireEvent(cancelledDispatcher, var1);
            return;
         }

         this.renderer.repaint(this.areaOfInterest);
         if (this.isHalted()) {
            this.fireEvent(cancelledDispatcher, var1);
            return;
         }

         var1 = new GVTTreeRendererEvent(this, this.renderer.getOffScreen());
         this.fireEvent(completedDispatcher, var1);
      } catch (NoClassDefFoundError var3) {
      } catch (InterruptedBridgeException var4) {
         this.fireEvent(cancelledDispatcher, var1);
      } catch (ThreadDeath var5) {
         this.fireEvent(failedDispatcher, var1);
         throw var5;
      } catch (Throwable var6) {
         var6.printStackTrace();
         this.fireEvent(failedDispatcher, var1);
      }

   }

   public void fireEvent(EventDispatcher.Dispatcher var1, Object var2) {
      EventDispatcher.fireEvent(var1, this.listeners, var2, true);
   }

   public void addGVTTreeRendererListener(GVTTreeRendererListener var1) {
      this.listeners.add(var1);
   }

   public void removeGVTTreeRendererListener(GVTTreeRendererListener var1) {
      this.listeners.remove(var1);
   }
}
