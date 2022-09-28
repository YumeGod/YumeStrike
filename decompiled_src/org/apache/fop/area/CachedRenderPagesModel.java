package org.apache.fop.area;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fonts.FontInfo;
import org.xml.sax.SAXException;

public class CachedRenderPagesModel extends RenderPagesModel {
   private Map pageMap = new HashMap();
   protected File baseDir = new File(System.getProperty("java.io.tmpdir"));

   public CachedRenderPagesModel(FOUserAgent userAgent, String outputFormat, FontInfo fontInfo, OutputStream stream) throws FOPException {
      super(userAgent, outputFormat, fontInfo, stream);
   }

   protected boolean checkPreparedPages(PageViewport newpage, boolean renderUnresolved) {
      Iterator iter = this.prepared.iterator();

      while(iter.hasNext()) {
         PageViewport pageViewport = (PageViewport)iter.next();
         if (!pageViewport.isResolved() && !renderUnresolved) {
            if (!this.renderer.supportsOutOfOrder()) {
               break;
            }
         } else {
            if (pageViewport != newpage) {
               try {
                  String name = (String)this.pageMap.get(pageViewport);
                  File tempFile = new File(this.baseDir, name);
                  log.debug("Loading page from: " + tempFile);
                  ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(tempFile)));

                  try {
                     pageViewport.loadPage(in);
                  } finally {
                     IOUtils.closeQuietly((InputStream)in);
                  }

                  if (!tempFile.delete()) {
                     ResourceEventProducer eventProducer = ResourceEventProducer.Provider.get(this.renderer.getUserAgent().getEventBroadcaster());
                     eventProducer.cannotDeleteTempFile(this, tempFile);
                  }

                  this.pageMap.remove(pageViewport);
               } catch (Exception var12) {
                  AreaEventProducer eventProducer = AreaEventProducer.Provider.get(this.renderer.getUserAgent().getEventBroadcaster());
                  eventProducer.pageLoadError(this, pageViewport.getPageNumberString(), var12);
               }
            }

            this.renderPage(pageViewport);
            pageViewport.clear();
            iter.remove();
         }
      }

      if (newpage != null && newpage.getPage() != null) {
         this.savePage(newpage);
         newpage.clear();
      }

      return this.renderer.supportsOutOfOrder() || this.prepared.isEmpty();
   }

   protected void savePage(PageViewport page) {
      try {
         String fname = "fop-page-" + page.getPageIndex() + ".ser";
         File tempFile = new File(this.baseDir, fname);
         tempFile.deleteOnExit();
         ObjectOutputStream tempstream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile)));

         try {
            page.savePage(tempstream);
         } finally {
            IOUtils.closeQuietly((OutputStream)tempstream);
         }

         this.pageMap.put(page, fname);
         if (log.isDebugEnabled()) {
            log.debug("Page saved to temporary file: " + tempFile);
         }
      } catch (IOException var9) {
         AreaEventProducer eventProducer = AreaEventProducer.Provider.get(this.renderer.getUserAgent().getEventBroadcaster());
         eventProducer.pageSaveError(this, page.getPageNumberString(), var9);
      }

   }

   public void endDocument() throws SAXException {
      super.endDocument();
   }
}
