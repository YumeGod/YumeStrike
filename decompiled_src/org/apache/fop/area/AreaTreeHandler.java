package org.apache.fop.area;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FormattingResults;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.FOEventHandler;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.fop.fo.extensions.ExternalDocument;
import org.apache.fop.fo.extensions.destination.Destination;
import org.apache.fop.fo.pagination.AbstractPageSequence;
import org.apache.fop.fo.pagination.Root;
import org.apache.fop.fo.pagination.bookmarks.BookmarkTree;
import org.apache.fop.layoutmgr.ExternalDocumentLayoutManager;
import org.apache.fop.layoutmgr.LayoutManagerMaker;
import org.apache.fop.layoutmgr.LayoutManagerMapping;
import org.apache.fop.layoutmgr.PageSequenceLayoutManager;
import org.apache.fop.layoutmgr.TopLevelLayoutManager;
import org.xml.sax.SAXException;

public class AreaTreeHandler extends FOEventHandler {
   private static Log log;
   private Statistics statistics = null;
   private LayoutManagerMaker lmMaker;
   protected AreaTreeModel model;
   private IDTracker idTracker;
   private Root rootFObj;
   private FormattingResults results = new FormattingResults();
   private TopLevelLayoutManager prevPageSeqLM;
   private int idGen = 0;

   public AreaTreeHandler(FOUserAgent userAgent, String outputFormat, OutputStream stream) throws FOPException {
      super(userAgent);
      this.setupModel(userAgent, outputFormat, stream);
      this.lmMaker = userAgent.getFactory().getLayoutManagerMakerOverride();
      if (this.lmMaker == null) {
         this.lmMaker = new LayoutManagerMapping();
      }

      this.idTracker = new IDTracker();
      if (log.isDebugEnabled()) {
         this.statistics = new Statistics();
      }

   }

   protected void setupModel(FOUserAgent userAgent, String outputFormat, OutputStream stream) throws FOPException {
      if (userAgent.isConserveMemoryPolicyEnabled()) {
         this.model = new CachedRenderPagesModel(userAgent, outputFormat, this.fontInfo, stream);
      } else {
         this.model = new RenderPagesModel(userAgent, outputFormat, this.fontInfo, stream);
      }

   }

   public AreaTreeModel getAreaTreeModel() {
      return this.model;
   }

   public LayoutManagerMaker getLayoutManagerMaker() {
      return this.lmMaker;
   }

   public IDTracker getIDTracker() {
      return this.idTracker;
   }

   public FormattingResults getResults() {
      return this.results;
   }

   public void startDocument() throws SAXException {
      if (this.statistics != null) {
         this.statistics.start();
      }

   }

   private void finishPrevPageSequence(Numeric initialPageNumber) {
      if (this.prevPageSeqLM != null) {
         this.prevPageSeqLM.doForcePageCount(initialPageNumber);
         this.prevPageSeqLM.finishPageSequence();
         this.prevPageSeqLM = null;
      }

   }

   public void startPageSequence(org.apache.fop.fo.pagination.PageSequence pageSequence) {
      this.startAbstractPageSequence(pageSequence);
   }

   private void startAbstractPageSequence(AbstractPageSequence pageSequence) {
      this.rootFObj = pageSequence.getRoot();
      this.finishPrevPageSequence(pageSequence.getInitialPageNumber());
      pageSequence.initPageNumber();
      this.wrapAndAddExtensionAttachments(this.rootFObj.getExtensionAttachments());
      if (this.rootFObj.getDeclarations() != null) {
         this.wrapAndAddExtensionAttachments(this.rootFObj.getDeclarations().getExtensionAttachments());
      }

   }

   private void wrapAndAddExtensionAttachments(List list) {
      Iterator it = list.iterator();

      while(it.hasNext()) {
         ExtensionAttachment attachment = (ExtensionAttachment)it.next();
         this.addOffDocumentItem(new OffDocumentExtensionAttachment(attachment));
      }

   }

   public void endPageSequence(org.apache.fop.fo.pagination.PageSequence pageSequence) {
      if (this.statistics != null) {
         this.statistics.end();
      }

      if (pageSequence.getMainFlow() != null) {
         PageSequenceLayoutManager pageSLM = this.getLayoutManagerMaker().makePageSequenceLayoutManager(this, pageSequence);
         pageSLM.activateLayout();
         this.prevPageSeqLM = pageSLM;
      }

   }

   public void startExternalDocument(ExternalDocument document) {
      this.startAbstractPageSequence(document);
   }

   public void endExternalDocument(ExternalDocument document) {
      if (this.statistics != null) {
         this.statistics.end();
      }

      ExternalDocumentLayoutManager edLM = this.getLayoutManagerMaker().makeExternalDocumentLayoutManager(this, document);
      edLM.activateLayout();
      this.prevPageSeqLM = edLM;
   }

   public void notifyPageSequenceFinished(AbstractPageSequence pageSequence, int pageCount) {
      this.results.haveFormattedPageSequence(pageSequence, pageCount);
      if (log.isDebugEnabled()) {
         log.debug("Last page-sequence produced " + pageCount + " pages.");
      }

   }

   public void endDocument() throws SAXException {
      this.finishPrevPageSequence((Numeric)null);
      if (this.rootFObj != null) {
         List destinationList = this.rootFObj.getDestinationList();
         if (destinationList != null) {
            while(destinationList.size() > 0) {
               Destination destination = (Destination)destinationList.remove(0);
               DestinationData destinationData = new DestinationData(destination);
               this.addOffDocumentItem(destinationData);
            }
         }

         BookmarkTree bookmarkTree = this.rootFObj.getBookmarkTree();
         if (bookmarkTree != null) {
            BookmarkData data = new BookmarkData(bookmarkTree);
            this.addOffDocumentItem(data);
            if (!data.isResolved()) {
               this.model.handleOffDocumentItem(data);
            }
         }
      }

      this.model.endDocument();
      if (this.statistics != null) {
         this.statistics.logResults();
      }

   }

   private void addOffDocumentItem(OffDocumentItem odi) {
      if (odi instanceof Resolvable) {
         Resolvable res = (Resolvable)odi;
         String[] ids = res.getIDRefs();

         for(int count = 0; count < ids.length; ++count) {
            List pageVPList = this.idTracker.getPageViewportsContainingID(ids[count]);
            if (pageVPList != null) {
               res.resolveIDRef(ids[count], pageVPList);
            } else {
               AreaEventProducer eventProducer = AreaEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
               eventProducer.unresolvedIDReference(this, odi.getName(), ids[count]);
               this.idTracker.addUnresolvedIDRef(ids[count], res);
            }
         }

         if (res.isResolved()) {
            this.model.handleOffDocumentItem(odi);
         }
      } else {
         this.model.handleOffDocumentItem(odi);
      }

   }

   public String generatePageViewportKey() {
      ++this.idGen;
      return "P" + this.idGen;
   }

   /** @deprecated */
   public void associateIDWithPageViewport(String id, PageViewport pv) {
      this.idTracker.associateIDWithPageViewport(id, pv);
   }

   /** @deprecated */
   public void signalPendingID(String id) {
      this.idTracker.signalPendingID(id);
   }

   /** @deprecated */
   public void signalIDProcessed(String id) {
      this.idTracker.signalIDProcessed(id);
   }

   /** @deprecated */
   public boolean alreadyResolvedID(String id) {
      return this.idTracker.alreadyResolvedID(id);
   }

   /** @deprecated */
   public void tryIDResolution(PageViewport pv) {
      this.idTracker.tryIDResolution(pv);
   }

   /** @deprecated */
   public List getPageViewportsContainingID(String id) {
      return this.idTracker.getPageViewportsContainingID(id);
   }

   /** @deprecated */
   public void addUnresolvedIDRef(String idref, Resolvable res) {
      this.idTracker.addUnresolvedIDRef(idref, res);
   }

   static {
      log = LogFactory.getLog(AreaTreeHandler.class);
   }

   private class Statistics {
      private Runtime runtime = Runtime.getRuntime();
      private long initialMemory;
      private long startTime;

      protected Statistics() {
      }

      protected void start() {
         this.initialMemory = this.runtime.totalMemory() - this.runtime.freeMemory();
         this.startTime = System.currentTimeMillis();
      }

      protected void end() {
         long memoryNow = this.runtime.totalMemory() - this.runtime.freeMemory();
         AreaTreeHandler.log.debug("Current heap size: " + memoryNow / 1024L + "KB");
      }

      protected void logResults() {
         long memoryNow = this.runtime.totalMemory() - this.runtime.freeMemory();
         long memoryUsed = (memoryNow - this.initialMemory) / 1024L;
         long timeUsed = System.currentTimeMillis() - this.startTime;
         int pageCount = AreaTreeHandler.this.rootFObj.getTotalPagesGenerated();
         AreaTreeHandler.log.debug("Initial heap size: " + this.initialMemory / 1024L + "KB");
         AreaTreeHandler.log.debug("Current heap size: " + memoryNow / 1024L + "KB");
         AreaTreeHandler.log.debug("Total memory used: " + memoryUsed + "KB");
         AreaTreeHandler.log.debug("Total time used: " + timeUsed + "ms");
         AreaTreeHandler.log.debug("Pages rendered: " + pageCount);
         if (pageCount > 0) {
            long perPage = timeUsed / (long)pageCount;
            long ppm = timeUsed != 0L ? Math.round((double)('\uea60' * pageCount) / (double)timeUsed) : -1L;
            AreaTreeHandler.log.debug("Avg render time: " + perPage + "ms/page (" + ppm + "pages/min)");
         }

      }
   }
}
