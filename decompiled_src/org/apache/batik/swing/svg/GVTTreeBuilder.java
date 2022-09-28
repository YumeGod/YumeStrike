package org.apache.batik.swing.svg;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.DynamicGVTBuilder;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.InterruptedBridgeException;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.EventDispatcher;
import org.apache.batik.util.HaltingThread;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;

public class GVTTreeBuilder extends HaltingThread {
   protected SVGDocument svgDocument;
   protected BridgeContext bridgeContext;
   protected List listeners = Collections.synchronizedList(new LinkedList());
   protected Exception exception;
   static EventDispatcher.Dispatcher startedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((GVTTreeBuilderListener)var1).gvtBuildStarted((GVTTreeBuilderEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher completedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((GVTTreeBuilderListener)var1).gvtBuildCompleted((GVTTreeBuilderEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher cancelledDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((GVTTreeBuilderListener)var1).gvtBuildCancelled((GVTTreeBuilderEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher failedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((GVTTreeBuilderListener)var1).gvtBuildFailed((GVTTreeBuilderEvent)var2);
      }
   };

   public GVTTreeBuilder(SVGDocument var1, BridgeContext var2) {
      this.svgDocument = var1;
      this.bridgeContext = var2;
   }

   public void run() {
      GVTTreeBuilderEvent var1 = new GVTTreeBuilderEvent(this, (GraphicsNode)null);

      try {
         try {
            this.fireEvent(startedDispatcher, var1);
            if (this.isHalted()) {
               this.fireEvent(cancelledDispatcher, var1);
               return;
            }

            Object var2 = null;
            if (this.bridgeContext.isDynamic()) {
               var2 = new DynamicGVTBuilder();
            } else {
               var2 = new GVTBuilder();
            }

            GraphicsNode var3 = ((GVTBuilder)var2).build(this.bridgeContext, (Document)this.svgDocument);
            if (this.isHalted()) {
               this.fireEvent(cancelledDispatcher, var1);
               return;
            }

            var1 = new GVTTreeBuilderEvent(this, var3);
            this.fireEvent(completedDispatcher, var1);
         } catch (InterruptedBridgeException var11) {
            this.fireEvent(cancelledDispatcher, var1);
         } catch (BridgeException var12) {
            this.exception = var12;
            var1 = new GVTTreeBuilderEvent(this, var12.getGraphicsNode());
            this.fireEvent(failedDispatcher, var1);
         } catch (Exception var13) {
            this.exception = var13;
            this.fireEvent(failedDispatcher, var1);
         } catch (ThreadDeath var14) {
            this.exception = new Exception(var14.getMessage());
            this.fireEvent(failedDispatcher, var1);
            throw var14;
         } catch (Throwable var15) {
            var15.printStackTrace();
            this.exception = new Exception(var15.getMessage());
            this.fireEvent(failedDispatcher, var1);
         }

      } finally {
         ;
      }
   }

   public Exception getException() {
      return this.exception;
   }

   public void addGVTTreeBuilderListener(GVTTreeBuilderListener var1) {
      this.listeners.add(var1);
   }

   public void removeGVTTreeBuilderListener(GVTTreeBuilderListener var1) {
      this.listeners.remove(var1);
   }

   public void fireEvent(EventDispatcher.Dispatcher var1, Object var2) {
      EventDispatcher.fireEvent(var1, this.listeners, var2, true);
   }
}
