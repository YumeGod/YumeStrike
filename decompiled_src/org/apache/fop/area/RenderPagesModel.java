package org.apache.fop.area;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererEventProducer;
import org.xml.sax.SAXException;

public class RenderPagesModel extends AreaTreeModel {
   protected Renderer renderer;
   protected List prepared = new ArrayList();
   private List pendingODI = new ArrayList();
   private List endDocODI = new ArrayList();

   public RenderPagesModel(FOUserAgent userAgent, String outputFormat, FontInfo fontInfo, OutputStream stream) throws FOPException {
      this.renderer = userAgent.getRendererFactory().createRenderer(userAgent, outputFormat);

      try {
         this.renderer.setupFontInfo(fontInfo);
         if (!fontInfo.isSetupValid()) {
            throw new FOPException("No default font defined by OutputConverter");
         } else {
            this.renderer.startRenderer(stream);
         }
      } catch (IOException var6) {
         throw new FOPException(var6);
      }
   }

   public void startPageSequence(PageSequence pageSequence) {
      super.startPageSequence(pageSequence);
      if (this.renderer.supportsOutOfOrder()) {
         this.renderer.startPageSequence(this.getCurrentPageSequence());
      }

   }

   public void addPage(PageViewport page) {
      super.addPage(page);
      boolean ready = this.renderer.supportsOutOfOrder() && page.isResolved();
      if (ready) {
         if (!this.renderer.supportsOutOfOrder() && page.getPageSequence().isFirstPage(page)) {
            this.renderer.startPageSequence(this.getCurrentPageSequence());
         }

         String err;
         try {
            this.renderer.renderPage(page);
         } catch (RuntimeException var5) {
            err = "Error while rendering page " + page.getPageNumberString();
            log.error(err, var5);
            throw var5;
         } catch (IOException var6) {
            RendererEventProducer eventProducer = RendererEventProducer.Provider.get(this.renderer.getUserAgent().getEventBroadcaster());
            eventProducer.ioError(this, var6);
         } catch (FOPException var7) {
            err = "Error while rendering page " + page.getPageNumberString();
            log.error(err, var7);
            throw new IllegalStateException("Fatal error occurred. Cannot continue. " + var7.getClass().getName() + ": " + err);
         }

         page.clear();
      } else {
         this.preparePage(page);
      }

      boolean cont = this.checkPreparedPages(page, false);
      if (cont) {
         this.processOffDocumentItems(this.pendingODI);
         this.pendingODI.clear();
      }

   }

   protected boolean checkPreparedPages(PageViewport newPageViewport, boolean renderUnresolved) {
      Iterator iter = this.prepared.iterator();

      while(iter.hasNext()) {
         PageViewport pageViewport = (PageViewport)iter.next();
         if (!pageViewport.isResolved() && !renderUnresolved) {
            if (!this.renderer.supportsOutOfOrder()) {
               break;
            }
         } else {
            if (!this.renderer.supportsOutOfOrder() && pageViewport.getPageSequence().isFirstPage(pageViewport)) {
               this.renderer.startPageSequence(this.getCurrentPageSequence());
            }

            this.renderPage(pageViewport);
            pageViewport.clear();
            iter.remove();
         }
      }

      return this.renderer.supportsOutOfOrder() || this.prepared.isEmpty();
   }

   protected void renderPage(PageViewport pageViewport) {
      try {
         this.renderer.renderPage(pageViewport);
         if (!pageViewport.isResolved()) {
            String[] idrefs = pageViewport.getIDRefs();

            for(int count = 0; count < idrefs.length; ++count) {
               AreaEventProducer eventProducer = AreaEventProducer.Provider.get(this.renderer.getUserAgent().getEventBroadcaster());
               eventProducer.unresolvedIDReferenceOnPage(this, pageViewport.getPageNumberString(), idrefs[count]);
            }
         }
      } catch (Exception var5) {
         AreaEventProducer eventProducer = AreaEventProducer.Provider.get(this.renderer.getUserAgent().getEventBroadcaster());
         eventProducer.pageRenderingError(this, pageViewport.getPageNumberString(), var5);
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         }
      }

   }

   protected void preparePage(PageViewport page) {
      if (this.renderer.supportsOutOfOrder()) {
         this.renderer.preparePage(page);
      }

      this.prepared.add(page);
   }

   public void handleOffDocumentItem(OffDocumentItem oDI) {
      switch (oDI.getWhenToProcess()) {
         case 0:
            this.renderer.processOffDocumentItem(oDI);
            break;
         case 1:
            this.pendingODI.add(oDI);
            break;
         case 2:
            this.endDocODI.add(oDI);
            break;
         default:
            throw new RuntimeException();
      }

   }

   private void processOffDocumentItems(List list) {
      for(int count = 0; count < list.size(); ++count) {
         OffDocumentItem oDI = (OffDocumentItem)list.get(count);
         this.renderer.processOffDocumentItem(oDI);
      }

   }

   public void endDocument() throws SAXException {
      this.checkPreparedPages((PageViewport)null, true);
      this.processOffDocumentItems(this.pendingODI);
      this.pendingODI.clear();
      this.processOffDocumentItems(this.endDocODI);

      try {
         this.renderer.stopRenderer();
      } catch (IOException var2) {
         throw new SAXException(var2);
      }
   }
}
