package org.apache.batik.swing.svg;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.InterruptedBridgeException;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.EventDispatcher;
import org.apache.batik.util.HaltingThread;
import org.w3c.dom.svg.SVGDocument;

public class SVGLoadEventDispatcher extends HaltingThread {
   protected SVGDocument svgDocument;
   protected GraphicsNode root;
   protected BridgeContext bridgeContext;
   protected UpdateManager updateManager;
   protected List listeners = Collections.synchronizedList(new LinkedList());
   protected Exception exception;
   static EventDispatcher.Dispatcher startedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((SVGLoadEventDispatcherListener)var1).svgLoadEventDispatchStarted((SVGLoadEventDispatcherEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher completedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((SVGLoadEventDispatcherListener)var1).svgLoadEventDispatchCompleted((SVGLoadEventDispatcherEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher cancelledDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((SVGLoadEventDispatcherListener)var1).svgLoadEventDispatchCancelled((SVGLoadEventDispatcherEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher failedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((SVGLoadEventDispatcherListener)var1).svgLoadEventDispatchFailed((SVGLoadEventDispatcherEvent)var2);
      }
   };

   public SVGLoadEventDispatcher(GraphicsNode var1, SVGDocument var2, BridgeContext var3, UpdateManager var4) {
      this.svgDocument = var2;
      this.root = var1;
      this.bridgeContext = var3;
      this.updateManager = var4;
   }

   public void run() {
      SVGLoadEventDispatcherEvent var1 = new SVGLoadEventDispatcherEvent(this, this.root);

      try {
         this.fireEvent(startedDispatcher, var1);
         if (this.isHalted()) {
            this.fireEvent(cancelledDispatcher, var1);
            return;
         }

         this.updateManager.dispatchSVGLoadEvent();
         if (this.isHalted()) {
            this.fireEvent(cancelledDispatcher, var1);
            return;
         }

         this.fireEvent(completedDispatcher, var1);
      } catch (InterruptedException var3) {
         this.fireEvent(cancelledDispatcher, var1);
      } catch (InterruptedBridgeException var4) {
         this.fireEvent(cancelledDispatcher, var1);
      } catch (Exception var5) {
         this.exception = var5;
         this.fireEvent(failedDispatcher, var1);
      } catch (ThreadDeath var6) {
         this.exception = new Exception(var6.getMessage());
         this.fireEvent(failedDispatcher, var1);
         throw var6;
      } catch (Throwable var7) {
         var7.printStackTrace();
         this.exception = new Exception(var7.getMessage());
         this.fireEvent(failedDispatcher, var1);
      }

   }

   public UpdateManager getUpdateManager() {
      return this.updateManager;
   }

   public Exception getException() {
      return this.exception;
   }

   public void addSVGLoadEventDispatcherListener(SVGLoadEventDispatcherListener var1) {
      this.listeners.add(var1);
   }

   public void removeSVGLoadEventDispatcherListener(SVGLoadEventDispatcherListener var1) {
      this.listeners.remove(var1);
   }

   public void fireEvent(EventDispatcher.Dispatcher var1, Object var2) {
      EventDispatcher.fireEvent(var1, this.listeners, var2, true);
   }
}
