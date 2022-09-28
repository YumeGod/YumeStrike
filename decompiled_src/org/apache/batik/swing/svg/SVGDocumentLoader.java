package org.apache.batik.swing.svg;

import java.io.InterruptedIOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.util.EventDispatcher;
import org.apache.batik.util.HaltingThread;
import org.w3c.dom.svg.SVGDocument;

public class SVGDocumentLoader extends HaltingThread {
   protected String url;
   protected DocumentLoader loader;
   protected Exception exception;
   protected List listeners = Collections.synchronizedList(new LinkedList());
   static EventDispatcher.Dispatcher startedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((SVGDocumentLoaderListener)var1).documentLoadingStarted((SVGDocumentLoaderEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher completedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((SVGDocumentLoaderListener)var1).documentLoadingCompleted((SVGDocumentLoaderEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher cancelledDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((SVGDocumentLoaderListener)var1).documentLoadingCancelled((SVGDocumentLoaderEvent)var2);
      }
   };
   static EventDispatcher.Dispatcher failedDispatcher = new EventDispatcher.Dispatcher() {
      public void dispatch(Object var1, Object var2) {
         ((SVGDocumentLoaderListener)var1).documentLoadingFailed((SVGDocumentLoaderEvent)var2);
      }
   };

   public SVGDocumentLoader(String var1, DocumentLoader var2) {
      this.url = var1;
      this.loader = var2;
   }

   public void run() {
      SVGDocumentLoaderEvent var1 = new SVGDocumentLoaderEvent(this, (SVGDocument)null);

      try {
         this.fireEvent(startedDispatcher, var1);
         if (this.isHalted()) {
            this.fireEvent(cancelledDispatcher, var1);
            return;
         }

         SVGDocument var2 = (SVGDocument)this.loader.loadDocument(this.url);
         if (this.isHalted()) {
            this.fireEvent(cancelledDispatcher, var1);
            return;
         }

         var1 = new SVGDocumentLoaderEvent(this, var2);
         this.fireEvent(completedDispatcher, var1);
      } catch (InterruptedIOException var3) {
         this.fireEvent(cancelledDispatcher, var1);
      } catch (Exception var4) {
         this.exception = var4;
         this.fireEvent(failedDispatcher, var1);
      } catch (ThreadDeath var5) {
         this.exception = new Exception(var5.getMessage());
         this.fireEvent(failedDispatcher, var1);
         throw var5;
      } catch (Throwable var6) {
         var6.printStackTrace();
         this.exception = new Exception(var6.getMessage());
         this.fireEvent(failedDispatcher, var1);
      }

   }

   public Exception getException() {
      return this.exception;
   }

   public void addSVGDocumentLoaderListener(SVGDocumentLoaderListener var1) {
      this.listeners.add(var1);
   }

   public void removeSVGDocumentLoaderListener(SVGDocumentLoaderListener var1) {
      this.listeners.remove(var1);
   }

   public void fireEvent(EventDispatcher.Dispatcher var1, Object var2) {
      EventDispatcher.fireEvent(var1, this.listeners, var2, true);
   }
}
