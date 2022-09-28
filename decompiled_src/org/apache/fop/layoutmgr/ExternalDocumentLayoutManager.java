package org.apache.fop.layoutmgr;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.AreaTreeHandler;
import org.apache.fop.area.Block;
import org.apache.fop.area.BodyRegion;
import org.apache.fop.area.CTM;
import org.apache.fop.area.LineArea;
import org.apache.fop.area.PageSequence;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.RegionViewport;
import org.apache.fop.area.inline.Image;
import org.apache.fop.area.inline.Viewport;
import org.apache.fop.datatypes.FODimension;
import org.apache.fop.datatypes.URISpecification;
import org.apache.fop.fo.extensions.ExternalDocument;
import org.apache.fop.layoutmgr.inline.ImageLayout;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;

public class ExternalDocumentLayoutManager extends AbstractPageSequenceLayoutManager {
   private static Log log;
   private ImageLayout imageLayout;

   public ExternalDocumentLayoutManager(AreaTreeHandler ath, ExternalDocument document) {
      super(ath, document);
   }

   protected ExternalDocument getExternalDocument() {
      return (ExternalDocument)this.pageSeq;
   }

   public PageSequenceLayoutManager getPSLM() {
      throw new IllegalStateException("getPSLM() is illegal for " + this.getClass().getName());
   }

   public void activateLayout() {
      this.initialize();
      FOUserAgent userAgent = this.pageSeq.getUserAgent();
      ImageManager imageManager = userAgent.getFactory().getImageManager();
      String uri = this.getExternalDocument().getSrc();
      Integer firstPageIndex = ImageUtil.getPageIndexFromURI(uri);
      boolean hasPageIndex = firstPageIndex != null;

      try {
         ImageInfo info = imageManager.getImageInfo(uri, userAgent.getImageSessionContext());
         Object moreImages = info.getCustomObjects().get(ImageInfo.HAS_MORE_IMAGES);
         boolean hasMoreImages = moreImages != null && !Boolean.FALSE.equals(moreImages);
         Dimension intrinsicSize = info.getSize().getDimensionMpt();
         ImageLayout layout = new ImageLayout(this.getExternalDocument(), this, intrinsicSize);
         PageSequence pageSequence = new PageSequence((LineArea)null);
         this.transferExtensions(pageSequence);
         this.areaTreeHandler.getAreaTreeModel().startPageSequence(pageSequence);
         if (log.isDebugEnabled()) {
            log.debug("Starting layout");
         }

         this.makePageForImage(info, layout);
         if (!hasPageIndex && hasMoreImages) {
            if (log.isTraceEnabled()) {
               log.trace("Starting multi-page processing...");
            }

            try {
               URI originalURI = new URI(URISpecification.escapeURI(uri));

               for(int pageIndex = 1; hasMoreImages; ++pageIndex) {
                  URI tempURI = new URI(originalURI.getScheme(), originalURI.getSchemeSpecificPart(), "page=" + Integer.toString(pageIndex + 1));
                  if (log.isTraceEnabled()) {
                     log.trace("Subimage: " + tempURI.toASCIIString());
                  }

                  ImageInfo subinfo = imageManager.getImageInfo(tempURI.toASCIIString(), userAgent.getImageSessionContext());
                  moreImages = subinfo.getCustomObjects().get(ImageInfo.HAS_MORE_IMAGES);
                  hasMoreImages = moreImages != null && !Boolean.FALSE.equals(moreImages);
                  intrinsicSize = subinfo.getSize().getDimensionMpt();
                  layout = new ImageLayout(this.getExternalDocument(), this, intrinsicSize);
                  this.makePageForImage(subinfo, layout);
               }
            } catch (URISyntaxException var16) {
               this.getResourceEventProducer().uriError(this, uri, var16, this.getExternalDocument().getLocator());
               return;
            }
         }
      } catch (FileNotFoundException var17) {
         this.getResourceEventProducer().imageNotFound(this, uri, var17, this.getExternalDocument().getLocator());
      } catch (IOException var18) {
         this.getResourceEventProducer().imageIOError(this, uri, var18, this.getExternalDocument().getLocator());
      } catch (ImageException var19) {
         this.getResourceEventProducer().imageError(this, uri, var19, this.getExternalDocument().getLocator());
      }

   }

   private ResourceEventProducer getResourceEventProducer() {
      return ResourceEventProducer.Provider.get(this.getExternalDocument().getUserAgent().getEventBroadcaster());
   }

   private void makePageForImage(ImageInfo info, ImageLayout layout) {
      this.imageLayout = layout;
      this.curPage = this.makeNewPage(false, false);
      this.fillPage(info.getOriginalURI());
      this.finishPage();
   }

   private void fillPage(String uri) {
      Dimension imageSize = this.imageLayout.getViewportSize();
      Block blockArea = new Block();
      blockArea.setIPD(imageSize.width);
      LineArea lineArea = new LineArea();
      Image imageArea = new Image(uri);
      TraitSetter.setProducerID(imageArea, this.fobj.getId());
      this.transferForeignAttributes(imageArea);
      Viewport vp = new Viewport(imageArea);
      TraitSetter.setProducerID(vp, this.fobj.getId());
      vp.setIPD(imageSize.width);
      vp.setBPD(imageSize.height);
      vp.setContentPosition(this.imageLayout.getPlacement());
      vp.setOffset(0);
      lineArea.addInlineArea(vp);
      lineArea.updateExtentsFromChildren();
      blockArea.addLineArea(lineArea);
      this.curPage.getPageViewport().getCurrentFlow().addBlock(blockArea);
      this.curPage.getPageViewport().getCurrentSpan().notifyFlowsFinished();
   }

   public void finishPageSequence() {
      if (this.pageSeq.hasId()) {
         this.idTracker.signalIDProcessed(this.pageSeq.getId());
      }

      this.pageSeq.getRoot().notifyPageSequenceFinished(this.currentPageNum, this.currentPageNum - this.startPageNum + 1);
      this.areaTreeHandler.notifyPageSequenceFinished(this.pageSeq, this.currentPageNum - this.startPageNum + 1);
      if (log.isDebugEnabled()) {
         log.debug("Ending layout");
      }

   }

   protected Page createPage(int pageNumber, boolean isBlank) {
      String pageNumberString = this.pageSeq.makeFormattedPageNumber(pageNumber);
      Dimension imageSize = this.imageLayout.getViewportSize();
      Rectangle referenceRect;
      if (this.pageSeq.getReferenceOrientation() % 180 == 0) {
         referenceRect = new Rectangle(0, 0, imageSize.width, imageSize.height);
      } else {
         referenceRect = new Rectangle(0, 0, imageSize.height, imageSize.width);
      }

      FODimension reldims = new FODimension(0, 0);
      CTM pageCTM = CTM.getCTMandRelDims(this.pageSeq.getReferenceOrientation(), 79, referenceRect, reldims);
      Page page = new Page(referenceRect, pageNumber, pageNumberString, isBlank);
      PageViewport pv = page.getPageViewport();
      org.apache.fop.area.Page pageArea = new org.apache.fop.area.Page();
      pv.setPage(pageArea);
      RegionViewport rv = new RegionViewport(referenceRect);
      rv.setIPD(referenceRect.width);
      rv.setBPD(referenceRect.height);
      rv.setClip(true);
      BodyRegion body = new BodyRegion(58, "fop-image-region", rv, 1, 0);
      body.setIPD(imageSize.width);
      body.setBPD(imageSize.height);
      body.setCTM(pageCTM);
      rv.setRegionReference(body);
      pageArea.setRegionViewport(58, rv);
      pv.setKey(this.areaTreeHandler.generatePageViewportKey());
      pv.createSpan(false);
      return page;
   }

   static {
      log = LogFactory.getLog(ExternalDocumentLayoutManager.class);
   }
}
