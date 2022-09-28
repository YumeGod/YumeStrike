package org.apache.fop.render.intermediate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import javax.xml.transform.stream.StreamResult;
import org.apache.batik.parser.AWTTransformProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.Version;
import org.apache.fop.apps.FOPException;
import org.apache.fop.area.Area;
import org.apache.fop.area.AreaTreeObject;
import org.apache.fop.area.Block;
import org.apache.fop.area.BlockViewport;
import org.apache.fop.area.BookmarkData;
import org.apache.fop.area.CTM;
import org.apache.fop.area.DestinationData;
import org.apache.fop.area.OffDocumentExtensionAttachment;
import org.apache.fop.area.OffDocumentItem;
import org.apache.fop.area.PageSequence;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.RegionViewport;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.AbstractTextArea;
import org.apache.fop.area.inline.ForeignObject;
import org.apache.fop.area.inline.Image;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.InlineParent;
import org.apache.fop.area.inline.Leader;
import org.apache.fop.area.inline.SpaceArea;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.area.inline.Viewport;
import org.apache.fop.area.inline.WordArea;
import org.apache.fop.datatypes.URISpecification;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.fop.fo.extensions.xmp.XMPMetadata;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.fonts.LazyFont;
import org.apache.fop.fonts.Typeface;
import org.apache.fop.render.AbstractPathOrientedRenderer;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.intermediate.extensions.AbstractAction;
import org.apache.fop.render.intermediate.extensions.ActionSet;
import org.apache.fop.render.intermediate.extensions.Bookmark;
import org.apache.fop.render.intermediate.extensions.BookmarkTree;
import org.apache.fop.render.intermediate.extensions.GoToXYAction;
import org.apache.fop.render.intermediate.extensions.Link;
import org.apache.fop.render.intermediate.extensions.NamedDestination;
import org.apache.fop.render.intermediate.extensions.URIAction;
import org.apache.fop.render.pdf.PDFEventProducer;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.RuleStyle;
import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.schemas.DublinCoreAdapter;
import org.apache.xmlgraphics.xmp.schemas.DublinCoreSchema;
import org.apache.xmlgraphics.xmp.schemas.XMPBasicAdapter;
import org.apache.xmlgraphics.xmp.schemas.XMPBasicSchema;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class IFRenderer extends AbstractPathOrientedRenderer {
   protected static Log log;
   public static final String IF_MIME_TYPE = "application/X-fop-intermediate-format";
   private IFDocumentHandler documentHandler;
   private IFPainter painter;
   protected Renderer mimic;
   private boolean inPageSequence = false;
   private Stack graphicContextStack = new Stack();
   private Stack viewportDimensionStack = new Stack();
   private IFGraphicContext graphicContext = new IFGraphicContext();
   private Metadata documentMetadata;
   private Map idPositions = new HashMap();
   private List unfinishedGoTos = new ArrayList();
   protected Map pageIndices = new HashMap();
   private BookmarkTree bookmarkTree;
   private List deferredDestinations = new ArrayList();
   private List deferredLinks = new ArrayList();
   private ActionSet actionSet = new ActionSet();
   private TextUtil textUtil = new TextUtil();
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public String getMimeType() {
      return "application/X-fop-intermediate-format";
   }

   public void setDocumentHandler(IFDocumentHandler documentHandler) {
      this.documentHandler = documentHandler;
   }

   public void setupFontInfo(FontInfo inFontInfo) throws FOPException {
      if (this.documentHandler == null) {
         this.documentHandler = this.createDefaultDocumentHandler();
      }

      IFUtil.setupFonts(this.documentHandler, inFontInfo);
      this.fontInfo = inFontInfo;
   }

   private void handleIFException(IFException ife) {
      if (ife.getCause() instanceof SAXException) {
         throw new RuntimeException(ife.getCause());
      } else {
         throw new RuntimeException(ife);
      }
   }

   private void handleIFExceptionWithIOException(IFException ife) throws IOException {
      if (ife.getCause() instanceof IOException) {
         throw (IOException)ife.getCause();
      } else {
         this.handleIFException(ife);
      }
   }

   public boolean supportsOutOfOrder() {
      return this.documentHandler != null ? this.documentHandler.supportsPagesOutOfOrder() : false;
   }

   protected IFDocumentNavigationHandler getDocumentNavigationHandler() {
      return this.documentHandler.getDocumentNavigationHandler();
   }

   protected boolean hasDocumentNavigation() {
      return this.getDocumentNavigationHandler() != null;
   }

   protected IFDocumentHandler createDefaultDocumentHandler() {
      IFSerializer serializer = new IFSerializer();
      serializer.setContext(new IFContext(this.getUserAgent()));
      return serializer;
   }

   public void startRenderer(OutputStream outputStream) throws IOException {
      try {
         if (outputStream != null) {
            StreamResult result = new StreamResult(outputStream);
            if (this.getUserAgent().getOutputFile() != null) {
               result.setSystemId(this.getUserAgent().getOutputFile().toURI().toURL().toExternalForm());
            }

            if (this.documentHandler == null) {
               this.documentHandler = this.createDefaultDocumentHandler();
            }

            this.documentHandler.setResult(result);
         }

         super.startRenderer((OutputStream)null);
         if (log.isDebugEnabled()) {
            log.debug("Rendering areas via IF document handler (" + this.documentHandler.getClass().getName() + ")...");
         }

         this.documentHandler.startDocument();
         this.documentHandler.startDocumentHeader();
      } catch (IFException var3) {
         this.handleIFExceptionWithIOException(var3);
      }

   }

   public void stopRenderer() throws IOException {
      try {
         if (this.inPageSequence) {
            this.documentHandler.endPageSequence();
            this.inPageSequence = false;
         }

         this.documentHandler.startDocumentTrailer();
         if (this.hasDocumentNavigation()) {
            this.finishOpenGoTos();
            Iterator iter = this.deferredDestinations.iterator();

            while(iter.hasNext()) {
               NamedDestination dest = (NamedDestination)iter.next();
               iter.remove();
               this.getDocumentNavigationHandler().renderNamedDestination(dest);
            }

            if (this.bookmarkTree != null) {
               this.getDocumentNavigationHandler().renderBookmarkTree(this.bookmarkTree);
            }
         }

         this.documentHandler.endDocumentTrailer();
         this.documentHandler.endDocument();
      } catch (IFException var3) {
         this.handleIFExceptionWithIOException(var3);
      }

      this.pageIndices.clear();
      this.idPositions.clear();
      this.actionSet.clear();
      super.stopRenderer();
      log.debug("Rendering finished.");
   }

   public void processOffDocumentItem(OffDocumentItem odi) {
      if (odi instanceof DestinationData) {
         this.renderDestination((DestinationData)odi);
      } else if (odi instanceof BookmarkData) {
         this.renderBookmarkTree((BookmarkData)odi);
      } else if (odi instanceof OffDocumentExtensionAttachment) {
         ExtensionAttachment attachment = ((OffDocumentExtensionAttachment)odi).getAttachment();
         if ("adobe:ns:meta/".equals(attachment.getCategory())) {
            this.renderXMPMetadata((XMPMetadata)attachment);
         } else {
            try {
               this.documentHandler.handleExtensionObject(attachment);
            } catch (IFException var4) {
               this.handleIFException(var4);
            }
         }
      }

   }

   private void renderDestination(DestinationData dd) {
      if (this.hasDocumentNavigation()) {
         String targetID = dd.getIDRef();
         if (targetID != null && targetID.length() != 0) {
            PageViewport pv = dd.getPageViewport();
            if (pv != null) {
               GoToXYAction action = this.getGoToActionForID(targetID, pv.getPageIndex());
               NamedDestination namedDestination = new NamedDestination(targetID, action);
               this.deferredDestinations.add(namedDestination);
            } else {
               log.debug("Unresolved destination item received: " + dd.getIDRef());
            }

         } else {
            throw new IllegalArgumentException("DestinationData must contain a ID reference");
         }
      }
   }

   protected void renderBookmarkTree(BookmarkData bookmarks) {
      if (!$assertionsDisabled && this.bookmarkTree != null) {
         throw new AssertionError();
      } else if (this.hasDocumentNavigation()) {
         this.bookmarkTree = new BookmarkTree();

         for(int i = 0; i < bookmarks.getCount(); ++i) {
            BookmarkData ext = bookmarks.getSubData(i);
            Bookmark b = this.renderBookmarkItem(ext);
            this.bookmarkTree.addBookmark(b);
         }

      }
   }

   private Bookmark renderBookmarkItem(BookmarkData bookmarkItem) {
      String targetID = bookmarkItem.getIDRef();
      if (targetID != null && targetID.length() != 0) {
         GoToXYAction action = null;
         PageViewport pv = bookmarkItem.getPageViewport();
         if (pv != null) {
            action = this.getGoToActionForID(targetID, pv.getPageIndex());
         } else {
            log.debug("Bookmark with IDRef \"" + targetID + "\" has a null PageViewport.");
         }

         Bookmark b = new Bookmark(bookmarkItem.getBookmarkTitle(), bookmarkItem.showChildItems(), action);

         for(int i = 0; i < bookmarkItem.getCount(); ++i) {
            b.addChildBookmark(this.renderBookmarkItem(bookmarkItem.getSubData(i)));
         }

         return b;
      } else {
         throw new IllegalArgumentException("DestinationData must contain a ID reference");
      }
   }

   private void renderXMPMetadata(XMPMetadata metadata) {
      this.documentMetadata = metadata.getMetadata();
   }

   private GoToXYAction getGoToActionForID(String targetID, int pageIndex) {
      GoToXYAction action = (GoToXYAction)this.actionSet.get(targetID);
      if (action == null) {
         if (pageIndex < 0) {
         }

         Point position = (Point)this.idPositions.get(targetID);
         if (pageIndex >= 0 && position != null) {
            action = new GoToXYAction(targetID, pageIndex, position);
         } else {
            action = new GoToXYAction(targetID, pageIndex, (Point)null);
            this.unfinishedGoTos.add(action);
         }

         action = (GoToXYAction)this.actionSet.put(action);
      }

      return action;
   }

   private void finishOpenGoTos() {
      int count = this.unfinishedGoTos.size();
      if (count > 0) {
         Point defaultPos = new Point(0, 0);

         while(!this.unfinishedGoTos.isEmpty()) {
            GoToXYAction action = (GoToXYAction)this.unfinishedGoTos.get(0);
            this.noteGoToPosition(action, defaultPos);
         }

         PDFEventProducer eventProducer = PDFEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.nonFullyResolvedLinkTargets(this, count);
      }

   }

   private void noteGoToPosition(GoToXYAction action, Point position) {
      action.setTargetLocation(position);

      try {
         this.getDocumentNavigationHandler().addResolvedAction(action);
      } catch (IFException var4) {
         this.handleIFException(var4);
      }

      this.unfinishedGoTos.remove(action);
   }

   private void noteGoToPosition(GoToXYAction action, PageViewport pv, Point position) {
      action.setPageIndex(pv.getPageIndex());
      this.noteGoToPosition(action, position);
   }

   private void saveAbsolutePosition(String id, PageViewport pv, int relativeIPP, int relativeBPP, AffineTransform tf) {
      Point position = new Point(relativeIPP, relativeBPP);
      tf.transform(position, position);
      this.idPositions.put(id, position);
      GoToXYAction action = (GoToXYAction)this.actionSet.get(id);
      if (action != null) {
         this.noteGoToPosition(action, pv, position);
      }

   }

   private void saveAbsolutePosition(String id, int relativeIPP, int relativeBPP) {
      this.saveAbsolutePosition(id, this.currentPageViewport, relativeIPP, relativeBPP, this.graphicContext.getTransform());
   }

   private void saveBlockPosIfTargetable(Block block) {
      String id = this.getTargetableID(block);
      if (this.hasDocumentNavigation() && id != null) {
         int ipp = block.getXOffset();
         int bpp = block.getYOffset() + block.getSpaceBefore();
         int positioning = block.getPositioning();
         if (positioning != 3 && positioning != 2) {
            ipp += this.currentIPPosition;
            bpp += this.currentBPPosition;
         }

         this.saveAbsolutePosition(id, this.currentPageViewport, ipp, bpp, this.graphicContext.getTransform());
      }

   }

   private void saveInlinePosIfTargetable(InlineArea inlineArea) {
      String id = this.getTargetableID(inlineArea);
      if (this.hasDocumentNavigation() && id != null) {
         int extraMarginBefore = 5000;
         int ipp = this.currentIPPosition;
         int bpp = this.currentBPPosition + inlineArea.getOffset() - extraMarginBefore;
         this.saveAbsolutePosition(id, ipp, bpp);
      }

   }

   private String getTargetableID(Area area) {
      String id = (String)area.getTrait(Trait.PROD_ID);
      return id != null && id.length() != 0 && this.currentPageViewport.isFirstWithID(id) && !this.idPositions.containsKey(id) ? id : null;
   }

   public void startPageSequence(PageSequence pageSequence) {
      try {
         if (this.inPageSequence) {
            this.documentHandler.endPageSequence();
            this.documentHandler.getContext().setLanguage((Locale)null);
         } else {
            if (this.documentMetadata == null) {
               this.documentMetadata = this.createDefaultDocumentMetadata();
            }

            this.documentHandler.handleExtensionObject(this.documentMetadata);
            this.documentHandler.endDocumentHeader();
            this.inPageSequence = true;
         }

         this.establishForeignAttributes(pageSequence.getForeignAttributes());
         this.documentHandler.getContext().setLanguage(this.toLocale(pageSequence));
         this.documentHandler.startPageSequence((String)null);
         this.resetForeignAttributes();
         this.processExtensionAttachments(pageSequence);
      } catch (IFException var3) {
         this.handleIFException(var3);
      }

   }

   private Locale toLocale(PageSequence pageSequence) {
      if (pageSequence.getLanguage() != null) {
         return pageSequence.getCountry() != null ? new Locale(pageSequence.getLanguage(), pageSequence.getCountry()) : new Locale(pageSequence.getLanguage());
      } else {
         return null;
      }
   }

   private Metadata createDefaultDocumentMetadata() {
      Metadata xmp = new Metadata();
      DublinCoreAdapter dc = DublinCoreSchema.getAdapter(xmp);
      if (this.getUserAgent().getTitle() != null) {
         dc.setTitle(this.getUserAgent().getTitle());
      }

      if (this.getUserAgent().getAuthor() != null) {
         dc.addCreator(this.getUserAgent().getAuthor());
      }

      if (this.getUserAgent().getKeywords() != null) {
         dc.addSubject(this.getUserAgent().getKeywords());
      }

      XMPBasicAdapter xmpBasic = XMPBasicSchema.getAdapter(xmp);
      if (this.getUserAgent().getProducer() != null) {
         xmpBasic.setCreatorTool(this.getUserAgent().getProducer());
      } else {
         xmpBasic.setCreatorTool(Version.getVersion());
      }

      xmpBasic.setMetadataDate(new Date());
      if (this.getUserAgent().getCreationDate() != null) {
         xmpBasic.setCreateDate(this.getUserAgent().getCreationDate());
      } else {
         xmpBasic.setCreateDate(xmpBasic.getMetadataDate());
      }

      return xmp;
   }

   public void preparePage(PageViewport page) {
      super.preparePage(page);
   }

   public void renderPage(PageViewport page) throws IOException, FOPException {
      if (log.isTraceEnabled()) {
         log.trace("renderPage() " + page);
      }

      try {
         this.pageIndices.put(page.getKey(), new Integer(page.getPageIndex()));
         Rectangle viewArea = page.getViewArea();
         Dimension dim = new Dimension(viewArea.width, viewArea.height);
         this.establishForeignAttributes(page.getForeignAttributes());
         this.documentHandler.startPage(page.getPageIndex(), page.getPageNumberString(), page.getSimplePageMasterName(), dim);
         this.resetForeignAttributes();
         this.documentHandler.startPageHeader();
         this.processExtensionAttachments(page);
         this.documentHandler.endPageHeader();
         this.painter = this.documentHandler.startPageContent();
         super.renderPage(page);
         this.painter = null;
         this.documentHandler.endPageContent();
         this.documentHandler.startPageTrailer();
         if (this.hasDocumentNavigation()) {
            Iterator iter = this.deferredLinks.iterator();

            while(iter.hasNext()) {
               Link link = (Link)iter.next();
               iter.remove();
               this.getDocumentNavigationHandler().renderLink(link);
            }
         }

         this.documentHandler.endPageTrailer();
         this.establishForeignAttributes(page.getForeignAttributes());
         this.documentHandler.endPage();
         this.resetForeignAttributes();
      } catch (IFException var6) {
         this.handleIFException(var6);
      }

   }

   private void processExtensionAttachments(AreaTreeObject area) throws IFException {
      if (area.hasExtensionAttachments()) {
         Iterator iter = area.getExtensionAttachments().iterator();

         while(iter.hasNext()) {
            ExtensionAttachment attachment = (ExtensionAttachment)iter.next();
            this.documentHandler.handleExtensionObject(attachment);
         }
      }

   }

   private void establishForeignAttributes(Map foreignAttributes) {
      this.documentHandler.getContext().setForeignAttributes(foreignAttributes);
   }

   private void resetForeignAttributes() {
      this.documentHandler.getContext().resetForeignAttributes();
   }

   private void establishStructurePointer(String ptr) {
      this.documentHandler.getContext().setStructurePointer(ptr);
   }

   private void resetStructurePointer() {
      this.documentHandler.getContext().resetStructurePointer();
   }

   protected void saveGraphicsState() {
      this.graphicContextStack.push(this.graphicContext);
      this.graphicContext = (IFGraphicContext)this.graphicContext.clone();
   }

   protected void restoreGraphicsState() {
      label23:
      while(true) {
         if (this.graphicContext.getGroupStackSize() > 0) {
            IFGraphicContext.Group[] groups = this.graphicContext.dropGroups();
            int i = groups.length - 1;

            while(true) {
               if (i < 0) {
                  continue label23;
               }

               try {
                  groups[i].end(this.painter);
               } catch (IFException var4) {
                  this.handleIFException(var4);
               }

               --i;
            }
         }

         this.graphicContext = (IFGraphicContext)this.graphicContextStack.pop();
         return;
      }
   }

   private void pushGroup(IFGraphicContext.Group group) {
      this.graphicContext.pushGroup(group);

      try {
         group.start(this.painter);
      } catch (IFException var3) {
         this.handleIFException(var3);
      }

   }

   protected List breakOutOfStateStack() {
      log.debug("Block.FIXED --> break out");

      ArrayList breakOutList;
      for(breakOutList = new ArrayList(); !this.graphicContextStack.empty(); this.graphicContext = (IFGraphicContext)this.graphicContextStack.pop()) {
         IFGraphicContext.Group[] groups = this.graphicContext.getGroups();

         for(int j = groups.length - 1; j >= 0; --j) {
            try {
               groups[j].end(this.painter);
            } catch (IFException var5) {
               this.handleIFException(var5);
            }
         }

         breakOutList.add(0, this.graphicContext);
      }

      return breakOutList;
   }

   protected void restoreStateStackAfterBreakOut(List breakOutList) {
      log.debug("Block.FIXED --> restoring context after break-out");
      int i = 0;

      for(int c = breakOutList.size(); i < c; ++i) {
         this.graphicContextStack.push(this.graphicContext);
         this.graphicContext = (IFGraphicContext)breakOutList.get(i);
         IFGraphicContext.Group[] groups = this.graphicContext.getGroups();
         int j = 0;

         for(int jc = groups.length; j < jc; ++j) {
            try {
               groups[j].start(this.painter);
            } catch (IFException var8) {
               this.handleIFException(var8);
            }
         }
      }

      log.debug("restored.");
   }

   protected void concatenateTransformationMatrix(AffineTransform at) {
      if (!at.isIdentity()) {
         this.concatenateTransformationMatrixMpt(this.ptToMpt(at), false);
      }

   }

   private void concatenateTransformationMatrixMpt(AffineTransform at, boolean force) {
      if (force || !at.isIdentity()) {
         if (log.isTraceEnabled()) {
            log.trace("-----concatenateTransformationMatrix: " + at);
         }

         IFGraphicContext.Group group = new IFGraphicContext.Group(at);
         this.pushGroup(group);
      }

   }

   protected void beginTextObject() {
   }

   protected void endTextObject() {
   }

   protected void renderRegionViewport(RegionViewport viewport) {
      Dimension dim = new Dimension(viewport.getIPD(), viewport.getBPD());
      this.viewportDimensionStack.push(dim);
      super.renderRegionViewport(viewport);
      this.viewportDimensionStack.pop();
   }

   protected void renderBlockViewport(BlockViewport bv, List children) {
      Dimension dim = new Dimension(bv.getIPD(), bv.getBPD());
      this.viewportDimensionStack.push(dim);
      int saveIP = this.currentIPPosition;
      int saveBP = this.currentBPPosition;
      CTM ctm = bv.getCTM();
      int borderPaddingStart = bv.getBorderAndPaddingWidthStart();
      int borderPaddingBefore = bv.getBorderAndPaddingWidthBefore();
      if (bv.getPositioning() != 2 && bv.getPositioning() != 3) {
         this.currentBPPosition += bv.getSpaceBefore();
         this.handleBlockTraits(bv);
         this.currentIPPosition += bv.getStartIndent();
         CTM tempctm = new CTM((double)this.containingIPPosition, (double)this.currentBPPosition);
         ctm = tempctm.multiply(ctm);
         this.currentBPPosition += borderPaddingBefore;
         Rectangle2D clippingRect = null;
         if (bv.getClip()) {
            clippingRect = new Rectangle(this.currentIPPosition, this.currentBPPosition, bv.getIPD(), bv.getBPD());
         }

         this.startVParea(ctm, clippingRect);
         this.currentIPPosition = 0;
         this.currentBPPosition = 0;
         this.renderBlocks(bv, children);
         this.endVParea();
         this.currentIPPosition = saveIP;
         this.currentBPPosition = saveBP;
         this.currentBPPosition += bv.getAllocBPD();
      } else {
         List breakOutList = null;
         if (bv.getPositioning() == 3) {
            breakOutList = this.breakOutOfStateStack();
         }

         AffineTransform positionTransform = new AffineTransform();
         positionTransform.translate((double)bv.getXOffset(), (double)bv.getYOffset());
         positionTransform.translate((double)(-borderPaddingStart), (double)(-borderPaddingBefore));
         String transf = bv.getForeignAttributeValue(FOX_TRANSFORM);
         if (transf != null) {
            AffineTransform freeTransform = AWTTransformProducer.createAffineTransform(transf);
            positionTransform.concatenate(freeTransform);
         }

         this.saveGraphicsState();
         this.concatenateTransformationMatrixMpt(positionTransform, false);
         float bpwidth = (float)(borderPaddingStart + bv.getBorderAndPaddingWidthEnd());
         float bpheight = (float)(borderPaddingBefore + bv.getBorderAndPaddingWidthAfter());
         this.drawBackAndBorders(bv, 0.0F, 0.0F, ((float)dim.width + bpwidth) / 1000.0F, ((float)dim.height + bpheight) / 1000.0F);
         AffineTransform contentRectTransform = new AffineTransform();
         contentRectTransform.translate((double)borderPaddingStart, (double)borderPaddingBefore);
         this.concatenateTransformationMatrixMpt(contentRectTransform, false);
         Rectangle clipRect = null;
         if (bv.getClip()) {
            clipRect = new Rectangle(0, 0, dim.width, dim.height);
         }

         AffineTransform contentTransform = ctm.toAffineTransform();
         this.startViewport(contentTransform, clipRect);
         this.currentIPPosition = 0;
         this.currentBPPosition = 0;
         this.renderBlocks(bv, children);
         this.endViewport();
         this.restoreGraphicsState();
         if (breakOutList != null) {
            this.restoreStateStackAfterBreakOut(breakOutList);
         }

         this.currentIPPosition = saveIP;
         this.currentBPPosition = saveBP;
      }

      this.viewportDimensionStack.pop();
   }

   public void renderViewport(Viewport viewport) {
      String ptr = (String)viewport.getTrait(Trait.PTR);
      this.establishStructurePointer(ptr);
      Dimension dim = new Dimension(viewport.getIPD(), viewport.getBPD());
      this.viewportDimensionStack.push(dim);
      super.renderViewport(viewport);
      this.viewportDimensionStack.pop();
      this.resetStructurePointer();
   }

   protected void startVParea(CTM ctm, Rectangle2D clippingRect) {
      if (log.isTraceEnabled()) {
         log.trace("startVParea() ctm=" + ctm + ", clippingRect=" + clippingRect);
      }

      AffineTransform at = new AffineTransform(ctm.toArray());
      Rectangle clipRect = null;
      if (clippingRect != null) {
         clipRect = new Rectangle((int)clippingRect.getMinX() - this.currentIPPosition, (int)clippingRect.getMinY() - this.currentBPPosition, (int)clippingRect.getWidth(), (int)clippingRect.getHeight());
      }

      this.startViewport(at, clipRect);
      if (log.isTraceEnabled()) {
         log.trace("startVPArea: " + at + " --> " + this.graphicContext.getTransform());
      }

   }

   private void startViewport(AffineTransform at, Rectangle clipRect) {
      this.saveGraphicsState();

      try {
         IFGraphicContext.Viewport viewport = new IFGraphicContext.Viewport(at, (Dimension)this.viewportDimensionStack.peek(), clipRect);
         this.graphicContext.pushGroup(viewport);
         viewport.start(this.painter);
      } catch (IFException var4) {
         this.handleIFException(var4);
      }

   }

   protected void endVParea() {
      log.trace("endVParea()");
      this.endViewport();
      if (log.isTraceEnabled()) {
         log.trace("endVPArea() --> " + this.graphicContext.getTransform());
      }

   }

   private void endViewport() {
      this.restoreGraphicsState();
   }

   protected void renderInlineArea(InlineArea inlineArea) {
      this.saveInlinePosIfTargetable(inlineArea);
      super.renderInlineArea(inlineArea);
   }

   public void renderInlineParent(InlineParent ip) {
      Rectangle ipRect = null;
      AbstractAction action = null;
      String ptr = (String)ip.getTrait(Trait.PTR);
      int ipp = this.currentIPPosition;
      int bpp = this.currentBPPosition + ip.getOffset();
      ipRect = new Rectangle(ipp, bpp, ip.getIPD(), ip.getBPD());
      AffineTransform transform = this.graphicContext.getTransform();
      ipRect = transform.createTransformedShape(ipRect).getBounds();
      super.renderInlineParent(ip);
      boolean linkTraitFound = false;
      Trait.InternalLink intLink = (Trait.InternalLink)ip.getTrait(Trait.INTERNAL_LINK);
      String extDest;
      if (intLink != null) {
         linkTraitFound = true;
         String pvKey = intLink.getPVKey();
         extDest = intLink.getIDRef();
         boolean pvKeyOK = pvKey != null && pvKey.length() > 0;
         boolean idRefOK = extDest != null && extDest.length() > 0;
         if (pvKeyOK && idRefOK) {
            Integer pageIndex = (Integer)this.pageIndices.get(pvKey);
            action = this.getGoToActionForID(extDest, pageIndex != null ? pageIndex : -1);
         }
      }

      if (!linkTraitFound) {
         Trait.ExternalLink extLink = (Trait.ExternalLink)ip.getTrait(Trait.EXTERNAL_LINK);
         if (extLink != null) {
            extDest = extLink.getDestination();
            if (extDest != null && extDest.length() > 0) {
               linkTraitFound = true;
               AbstractAction action = new URIAction(extDest, extLink.newWindow());
               action = this.actionSet.put(action);
            }
         }
      }

      if (linkTraitFound) {
         ((AbstractAction)action).setStructurePointer(ptr);
         Link link = new Link((AbstractAction)action, ipRect);
         this.deferredLinks.add(link);
      }

   }

   protected void renderBlock(Block block) {
      if (log.isTraceEnabled()) {
         log.trace("renderBlock() " + block);
      }

      this.saveBlockPosIfTargetable(block);
      super.renderBlock(block);
   }

   private Typeface getTypeface(String fontName) {
      Typeface tf = (Typeface)this.fontInfo.getFonts().get(fontName);
      if (tf instanceof LazyFont) {
         tf = ((LazyFont)tf).getRealFont();
      }

      return tf;
   }

   protected void renderText(TextArea text) {
      if (log.isTraceEnabled()) {
         log.trace("renderText() " + text);
      }

      this.renderInlineAreaBackAndBorders(text);
      Color ct = (Color)text.getTrait(Trait.COLOR);
      this.beginTextObject();
      String fontName = this.getInternalFontNameForArea(text);
      int size = (Integer)text.getTrait(Trait.FONT_SIZE);
      String ptr = (String)text.getTrait(Trait.PTR);
      this.establishStructurePointer(ptr);
      Typeface tf = this.getTypeface(fontName);
      FontTriplet triplet = (FontTriplet)text.getTrait(Trait.FONT);

      try {
         this.painter.setFont(triplet.getName(), triplet.getStyle(), new Integer(triplet.getWeight()), "normal", new Integer(size), ct);
      } catch (IFException var10) {
         this.handleIFException(var10);
      }

      int rx = this.currentIPPosition + text.getBorderAndPaddingWidthStart();
      int bl = this.currentBPPosition + text.getOffset() + text.getBaselineOffset();
      this.textUtil.flush();
      this.textUtil.setStartPosition(rx, bl);
      this.textUtil.setSpacing(text.getTextLetterSpaceAdjust(), text.getTextWordSpaceAdjust());
      super.renderText(text);
      this.textUtil.flush();
      this.renderTextDecoration(tf, size, text, bl, rx);
      this.resetStructurePointer();
   }

   protected void renderWord(WordArea word) {
      Font font = this.getFontFromArea(word.getParentArea());
      String s = word.getWord();
      this.renderText(s, word.getLetterAdjustArray(), font, (AbstractTextArea)word.getParentArea());
      super.renderWord(word);
   }

   protected void renderSpace(SpaceArea space) {
      Font font = this.getFontFromArea(space.getParentArea());
      String s = space.getSpace();
      AbstractTextArea textArea = (AbstractTextArea)space.getParentArea();
      this.renderText(s, (int[])null, font, textArea);
      super.renderSpace(space);
   }

   protected void renderText(String s, int[] letterAdjust, Font font, AbstractTextArea parentArea) {
      int l = s.length();
      if (l != 0) {
         if (letterAdjust != null) {
            this.textUtil.adjust(letterAdjust[0]);
         }

         for(int i = 0; i < l; ++i) {
            char ch = s.charAt(i);
            this.textUtil.addChar(ch);
            int glyphAdjust = 0;
            if (letterAdjust != null && i < l - 1) {
               glyphAdjust += letterAdjust[i + 1];
            }

            this.textUtil.adjust(glyphAdjust);
         }

      }
   }

   public void renderImage(Image image, Rectangle2D pos) {
      this.drawImage(image.getURL(), pos, image.getForeignAttributes());
   }

   protected void drawImage(String uri, Rectangle2D pos, Map foreignAttributes) {
      Rectangle posInt = new Rectangle(this.currentIPPosition + (int)pos.getX(), this.currentBPPosition + (int)pos.getY(), (int)pos.getWidth(), (int)pos.getHeight());
      uri = URISpecification.getURL(uri);

      try {
         this.establishForeignAttributes(foreignAttributes);
         this.painter.drawImage(uri, posInt);
         this.resetForeignAttributes();
      } catch (IFException var6) {
         this.handleIFException(var6);
      }

   }

   public void renderForeignObject(ForeignObject fo, Rectangle2D pos) {
      this.endTextObject();
      Rectangle posInt = new Rectangle(this.currentIPPosition + (int)pos.getX(), this.currentBPPosition + (int)pos.getY(), (int)pos.getWidth(), (int)pos.getHeight());
      Document doc = fo.getDocument();

      try {
         this.establishForeignAttributes(fo.getForeignAttributes());
         this.painter.drawImage(doc, posInt);
         this.resetForeignAttributes();
      } catch (IFException var6) {
         this.handleIFException(var6);
      }

   }

   public void renderLeader(Leader area) {
      this.renderInlineAreaBackAndBorders(area);
      int style = area.getRuleStyle();
      int ruleThickness = area.getRuleThickness();
      int startx = this.currentIPPosition + area.getBorderAndPaddingWidthStart();
      int starty = this.currentBPPosition + area.getOffset() + ruleThickness / 2;
      int endx = this.currentIPPosition + area.getBorderAndPaddingWidthStart() + area.getIPD();
      Color col = (Color)area.getTrait(Trait.COLOR);
      Point start = new Point(startx, starty);
      Point end = new Point(endx, starty);

      try {
         this.painter.drawLine(start, end, ruleThickness, col, RuleStyle.valueOf(style));
      } catch (IFException var11) {
         this.handleIFException(var11);
      }

      super.renderLeader(area);
   }

   protected void clip() {
      throw new IllegalStateException("Not used");
   }

   protected void clipRect(float x, float y, float width, float height) {
      this.pushGroup(new IFGraphicContext.Group());

      try {
         this.painter.clipRect(this.toMillipointRectangle(x, y, width, height));
      } catch (IFException var6) {
         this.handleIFException(var6);
      }

   }

   protected void closePath() {
      throw new IllegalStateException("Not used");
   }

   protected void drawBorders(float startx, float starty, float width, float height, BorderProps bpsBefore, BorderProps bpsAfter, BorderProps bpsStart, BorderProps bpsEnd) {
      Rectangle rect = this.toMillipointRectangle(startx, starty, width, height);

      try {
         this.painter.drawBorderRect(rect, bpsBefore, bpsAfter, bpsStart, bpsEnd);
      } catch (IFException var11) {
         this.handleIFException(var11);
      }

   }

   protected void drawBorderLine(float x1, float y1, float x2, float y2, boolean horz, boolean startOrBefore, int style, Color col) {
      this.updateColor(col, true);
      this.fillRect(x1, y1, x2 - x1, y2 - y1);
   }

   private int toMillipoints(float coordinate) {
      return Math.round(coordinate * 1000.0F);
   }

   private Rectangle toMillipointRectangle(float x, float y, float width, float height) {
      return new Rectangle(this.toMillipoints(x), this.toMillipoints(y), this.toMillipoints(width), this.toMillipoints(height));
   }

   protected void fillRect(float x, float y, float width, float height) {
      try {
         this.painter.fillRect(this.toMillipointRectangle(x, y, width, height), this.graphicContext.getPaint());
      } catch (IFException var6) {
         this.handleIFException(var6);
      }

   }

   protected void moveTo(float x, float y) {
      throw new IllegalStateException("Not used");
   }

   protected void lineTo(float x, float y) {
      throw new IllegalStateException("Not used");
   }

   protected void updateColor(Color col, boolean fill) {
      if (fill) {
         this.graphicContext.setPaint(col);
      } else {
         this.graphicContext.setColor(col);
      }

   }

   static {
      $assertionsDisabled = !IFRenderer.class.desiredAssertionStatus();
      log = LogFactory.getLog(IFRenderer.class);
   }

   private class TextUtil {
      private static final int INITIAL_BUFFER_SIZE = 16;
      private int[] dx;
      private int lastDXPos;
      private final StringBuffer text;
      private int startx;
      private int starty;
      private int tls;
      private int tws;
      private final boolean combined;

      private TextUtil() {
         this.dx = new int[16];
         this.lastDXPos = 0;
         this.text = new StringBuffer();
         this.combined = false;
      }

      void addChar(char ch) {
         this.text.append(ch);
      }

      void adjust(int adjust) {
         if (adjust != 0) {
            int idx = this.text.length();
            if (idx > this.dx.length - 1) {
               int newSize = Math.max(this.dx.length, idx + 1) + 16;
               int[] newDX = new int[newSize];
               System.arraycopy(this.dx, 0, newDX, 0, this.dx.length);
               this.dx = newDX;
            }

            int[] var10000 = this.dx;
            var10000[idx] += adjust;
            this.lastDXPos = idx;
         }

      }

      void reset() {
         if (this.text.length() > 0) {
            this.text.setLength(0);
            Arrays.fill(this.dx, 0);
            this.lastDXPos = 0;
         }

      }

      void setStartPosition(int x, int y) {
         this.startx = x;
         this.starty = y;
      }

      void setSpacing(int tls, int tws) {
         this.tls = tls;
         this.tws = tws;
      }

      void flush() {
         if (this.text.length() > 0) {
            try {
               int[] effDX = null;
               if (this.lastDXPos > 0) {
                  int size = this.lastDXPos + 1;
                  effDX = new int[size];
                  System.arraycopy(this.dx, 0, effDX, 0, size);
               }

               IFRenderer.this.painter.drawText(this.startx, this.starty, this.tls, this.tws, effDX, this.text.toString());
            } catch (IFException var3) {
               IFRenderer.this.handleIFException(var3);
            }

            this.reset();
         }

      }

      // $FF: synthetic method
      TextUtil(Object x1) {
         this();
      }
   }
}
