package org.apache.fop.render;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.Area;
import org.apache.fop.area.BeforeFloat;
import org.apache.fop.area.Block;
import org.apache.fop.area.BlockViewport;
import org.apache.fop.area.BodyRegion;
import org.apache.fop.area.CTM;
import org.apache.fop.area.Footnote;
import org.apache.fop.area.LineArea;
import org.apache.fop.area.MainReference;
import org.apache.fop.area.NormalFlow;
import org.apache.fop.area.OffDocumentItem;
import org.apache.fop.area.Page;
import org.apache.fop.area.PageSequence;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.RegionReference;
import org.apache.fop.area.RegionViewport;
import org.apache.fop.area.Span;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.Container;
import org.apache.fop.area.inline.ForeignObject;
import org.apache.fop.area.inline.Image;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.InlineBlockParent;
import org.apache.fop.area.inline.InlineParent;
import org.apache.fop.area.inline.Leader;
import org.apache.fop.area.inline.Space;
import org.apache.fop.area.inline.SpaceArea;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.area.inline.Viewport;
import org.apache.fop.area.inline.WordArea;
import org.apache.fop.fo.Constants;
import org.apache.fop.fonts.FontInfo;
import org.w3c.dom.Document;

public abstract class AbstractRenderer implements Renderer, Constants {
   protected static Log log = LogFactory.getLog("org.apache.fop.render");
   protected FOUserAgent userAgent = null;
   protected int currentBPPosition = 0;
   protected int currentIPPosition = 0;
   protected int containingBPPosition = 0;
   protected int containingIPPosition = 0;
   protected PageViewport currentPageViewport;
   private Set warnedXMLHandlers;

   public abstract void setupFontInfo(FontInfo var1) throws FOPException;

   public void setUserAgent(FOUserAgent agent) {
      this.userAgent = agent;
   }

   public FOUserAgent getUserAgent() {
      if (this.userAgent == null) {
         throw new IllegalStateException("FOUserAgent has not been set on Renderer");
      } else {
         return this.userAgent;
      }
   }

   public void startRenderer(OutputStream outputStream) throws IOException {
      if (this.userAgent == null) {
         throw new IllegalStateException("FOUserAgent has not been set on Renderer");
      }
   }

   public void stopRenderer() throws IOException {
   }

   public boolean supportsOutOfOrder() {
      return false;
   }

   public void processOffDocumentItem(OffDocumentItem odi) {
   }

   public Graphics2DAdapter getGraphics2DAdapter() {
      return null;
   }

   public ImageAdapter getImageAdapter() {
      return null;
   }

   protected PageViewport getCurrentPageViewport() {
      return this.currentPageViewport;
   }

   public void preparePage(PageViewport page) {
   }

   protected String convertTitleToString(LineArea title) {
      List children = title.getInlineAreas();
      String str = this.convertToString(children);
      return str.trim();
   }

   private String convertToString(List children) {
      StringBuffer sb = new StringBuffer();

      for(int count = 0; count < children.size(); ++count) {
         InlineArea inline = (InlineArea)children.get(count);
         if (inline instanceof TextArea) {
            sb.append(((TextArea)inline).getText());
         } else if (inline instanceof InlineParent) {
            sb.append(this.convertToString(((InlineParent)inline).getChildAreas()));
         } else {
            sb.append(" ");
         }
      }

      return sb.toString();
   }

   public void startPageSequence(LineArea seqTitle) {
   }

   public void startPageSequence(PageSequence pageSequence) {
      this.startPageSequence(pageSequence.getTitle());
   }

   public void renderPage(PageViewport page) throws IOException, FOPException {
      this.currentPageViewport = page;

      try {
         Page p = page.getPage();
         this.renderPageAreas(p);
      } finally {
         this.currentPageViewport = null;
      }

   }

   protected void renderPageAreas(Page page) {
      RegionViewport viewport = page.getRegionViewport(57);
      if (viewport != null) {
         this.renderRegionViewport(viewport);
      }

      viewport = page.getRegionViewport(61);
      if (viewport != null) {
         this.renderRegionViewport(viewport);
      }

      viewport = page.getRegionViewport(59);
      if (viewport != null) {
         this.renderRegionViewport(viewport);
      }

      viewport = page.getRegionViewport(56);
      if (viewport != null) {
         this.renderRegionViewport(viewport);
      }

      viewport = page.getRegionViewport(58);
      if (viewport != null) {
         this.renderRegionViewport(viewport);
      }

   }

   protected void renderRegionViewport(RegionViewport port) {
      Rectangle2D view = port.getViewArea();
      this.currentBPPosition = 0;
      this.currentIPPosition = 0;
      RegionReference regionReference = port.getRegionReference();
      this.handleRegionTraits(port);
      this.startVParea(regionReference.getCTM(), port.isClip() ? view : null);
      if (regionReference.getRegionClass() == 58) {
         this.renderBodyRegion((BodyRegion)regionReference);
      } else {
         this.renderRegion(regionReference);
      }

      this.endVParea();
   }

   protected abstract void startVParea(CTM var1, Rectangle2D var2);

   protected abstract void endVParea();

   protected void handleRegionTraits(RegionViewport rv) {
   }

   protected void renderRegion(RegionReference region) {
      this.renderBlocks((Block)null, region.getBlocks());
   }

   protected void renderBodyRegion(BodyRegion region) {
      BeforeFloat bf = region.getBeforeFloat();
      if (bf != null) {
         this.renderBeforeFloat(bf);
      }

      MainReference mr = region.getMainReference();
      if (mr != null) {
         this.renderMainReference(mr);
      }

      Footnote foot = region.getFootnote();
      if (foot != null) {
         this.renderFootnote(foot);
      }

   }

   protected void renderBeforeFloat(BeforeFloat bf) {
      List blocks = bf.getChildAreas();
      if (blocks != null) {
         this.renderBlocks((Block)null, blocks);
         Block sep = bf.getSeparator();
         if (sep != null) {
            this.renderBlock(sep);
         }
      }

   }

   protected void renderFootnote(Footnote footnote) {
      this.currentBPPosition += footnote.getTop();
      List blocks = footnote.getChildAreas();
      if (blocks != null) {
         Block sep = footnote.getSeparator();
         if (sep != null) {
            this.renderBlock(sep);
         }

         this.renderBlocks((Block)null, blocks);
      }

   }

   protected void renderMainReference(MainReference mr) {
      int saveIPPos = this.currentIPPosition;
      Span span = null;
      List spans = mr.getSpans();
      int saveBPPos = this.currentBPPosition;
      int saveSpanBPPos = saveBPPos;

      for(int count = 0; count < spans.size(); ++count) {
         span = (Span)spans.get(count);

         for(int c = 0; c < span.getColumnCount(); ++c) {
            NormalFlow flow = span.getNormalFlow(c);
            if (flow != null) {
               this.currentBPPosition = saveSpanBPPos;
               this.renderFlow(flow);
               this.currentIPPosition += flow.getIPD();
               this.currentIPPosition += mr.getColumnGap();
            }
         }

         this.currentIPPosition = saveIPPos;
         this.currentBPPosition = saveSpanBPPos + span.getHeight();
         saveSpanBPPos = this.currentBPPosition;
      }

      this.currentBPPosition = saveBPPos;
   }

   protected void renderFlow(NormalFlow flow) {
      List blocks = flow.getChildAreas();
      if (blocks != null) {
         this.renderBlocks((Block)null, blocks);
      }

   }

   protected void handleBlockTraits(Block block) {
   }

   protected void renderBlockViewport(BlockViewport bv, List children) {
      int saveIP;
      int saveBP;
      if (bv.getPositioning() == 2) {
         saveIP = this.currentIPPosition;
         saveBP = this.currentBPPosition;
         Rectangle2D clippingRect = null;
         if (bv.getClip()) {
            clippingRect = new Rectangle(saveIP, saveBP, bv.getIPD(), bv.getBPD());
         }

         CTM ctm = bv.getCTM();
         this.currentIPPosition = 0;
         this.currentBPPosition = 0;
         this.startVParea(ctm, clippingRect);
         this.handleBlockTraits(bv);
         this.renderBlocks(bv, children);
         this.endVParea();
         this.currentIPPosition = saveIP;
         this.currentBPPosition = saveBP;
      } else {
         saveIP = this.currentIPPosition;
         saveBP = this.currentBPPosition;
         this.handleBlockTraits(bv);
         this.renderBlocks(bv, children);
         this.currentIPPosition = saveIP;
         this.currentBPPosition = saveBP + bv.getAllocBPD();
      }

   }

   protected abstract void renderReferenceArea(Block var1);

   protected void renderBlocks(Block parent, List blocks) {
      int saveIP = this.currentIPPosition;
      if (parent != null && !parent.getTraitAsBoolean(Trait.IS_VIEWPORT_AREA)) {
         this.currentBPPosition += parent.getBorderAndPaddingWidthBefore();
      }

      int contBP = this.currentBPPosition;
      int contIP = this.currentIPPosition;
      this.containingBPPosition = this.currentBPPosition;
      this.containingIPPosition = this.currentIPPosition;

      for(int count = 0; count < blocks.size(); ++count) {
         Object obj = blocks.get(count);
         if (obj instanceof Block) {
            this.currentIPPosition = contIP;
            this.containingBPPosition = contBP;
            this.containingIPPosition = contIP;
            this.renderBlock((Block)obj);
            this.containingBPPosition = contBP;
            this.containingIPPosition = contIP;
         } else {
            LineArea line = (LineArea)obj;
            this.currentIPPosition = contIP + parent.getStartIndent() + line.getStartIndent();
            this.renderLineArea(line);
            this.currentBPPosition += line.getAllocBPD();
         }

         this.currentIPPosition = saveIP;
      }

   }

   protected void renderBlock(Block block) {
      List children = block.getChildAreas();
      if (block instanceof BlockViewport) {
         if (children != null) {
            this.renderBlockViewport((BlockViewport)block, children);
         } else {
            this.handleBlockTraits(block);
            this.currentBPPosition += block.getAllocBPD();
         }
      } else if (block.getTraitAsBoolean(Trait.IS_REFERENCE_AREA)) {
         this.renderReferenceArea(block);
      } else {
         int saveIP = this.currentIPPosition;
         int saveBP = this.currentBPPosition;
         this.currentIPPosition += block.getXOffset();
         this.currentBPPosition += block.getYOffset();
         this.currentBPPosition += block.getSpaceBefore();
         this.handleBlockTraits(block);
         if (children != null) {
            this.renderBlocks(block, children);
         }

         if (block.getPositioning() == 2) {
            this.currentBPPosition = saveBP;
         } else {
            this.currentIPPosition = saveIP;
            this.currentBPPosition = saveBP + block.getAllocBPD();
         }
      }

   }

   protected void renderLineArea(LineArea line) {
      List children = line.getInlineAreas();
      int saveBP = this.currentBPPosition;
      this.currentBPPosition += line.getSpaceBefore();

      for(int count = 0; count < children.size(); ++count) {
         InlineArea inline = (InlineArea)children.get(count);
         this.renderInlineArea(inline);
      }

      this.currentBPPosition = saveBP;
   }

   protected void renderInlineArea(InlineArea inlineArea) {
      if (inlineArea instanceof TextArea) {
         this.renderText((TextArea)inlineArea);
      } else if (inlineArea instanceof WordArea) {
         this.renderWord((WordArea)inlineArea);
      } else if (inlineArea instanceof SpaceArea) {
         this.renderSpace((SpaceArea)inlineArea);
      } else if (inlineArea instanceof InlineParent) {
         this.renderInlineParent((InlineParent)inlineArea);
      } else if (inlineArea instanceof InlineBlockParent) {
         this.renderInlineBlockParent((InlineBlockParent)inlineArea);
      } else if (inlineArea instanceof Space) {
         this.renderInlineSpace((Space)inlineArea);
      } else if (inlineArea instanceof Viewport) {
         this.renderViewport((Viewport)inlineArea);
      } else if (inlineArea instanceof Leader) {
         this.renderLeader((Leader)inlineArea);
      }

   }

   protected abstract void renderInlineAreaBackAndBorders(InlineArea var1);

   protected void renderInlineSpace(Space space) {
      this.renderInlineAreaBackAndBorders(space);
      this.currentIPPosition += space.getAllocIPD();
   }

   protected void renderLeader(Leader area) {
      this.currentIPPosition += area.getAllocIPD();
   }

   protected void renderText(TextArea text) {
      int saveIP = this.currentIPPosition;
      int saveBP = this.currentBPPosition;
      Iterator iter = text.getChildAreas().iterator();

      while(iter.hasNext()) {
         this.renderInlineArea((InlineArea)iter.next());
      }

      this.currentIPPosition = saveIP + text.getAllocIPD();
   }

   protected void renderWord(WordArea word) {
      this.currentIPPosition += word.getAllocIPD();
   }

   protected void renderSpace(SpaceArea space) {
      this.currentIPPosition += space.getAllocIPD();
   }

   protected void renderInlineParent(InlineParent ip) {
      this.renderInlineAreaBackAndBorders(ip);
      int saveIP = this.currentIPPosition;
      int saveBP = this.currentBPPosition;
      this.currentIPPosition += ip.getBorderAndPaddingWidthStart();
      this.currentBPPosition += ip.getOffset();
      Iterator iter = ip.getChildAreas().iterator();

      while(iter.hasNext()) {
         this.renderInlineArea((InlineArea)iter.next());
      }

      this.currentIPPosition = saveIP + ip.getAllocIPD();
      this.currentBPPosition = saveBP;
   }

   protected void renderInlineBlockParent(InlineBlockParent ibp) {
      this.renderInlineAreaBackAndBorders(ibp);
      this.currentIPPosition += ibp.getBorderAndPaddingWidthStart();
      int saveBP = this.currentBPPosition;
      this.currentBPPosition += ibp.getOffset();
      this.renderBlock(ibp.getChildArea());
      this.currentBPPosition = saveBP;
   }

   protected void renderViewport(Viewport viewport) {
      Area content = viewport.getContent();
      int saveBP = this.currentBPPosition;
      this.currentBPPosition += viewport.getOffset();
      Rectangle2D contpos = viewport.getContentPosition();
      if (content instanceof Image) {
         this.renderImage((Image)content, contpos);
      } else if (content instanceof Container) {
         this.renderContainer((Container)content);
      } else if (content instanceof ForeignObject) {
         this.renderForeignObject((ForeignObject)content, contpos);
      } else if (content instanceof InlineBlockParent) {
         this.renderInlineBlockParent((InlineBlockParent)content);
      }

      this.currentIPPosition += viewport.getAllocIPD();
      this.currentBPPosition = saveBP;
   }

   public void renderImage(Image image, Rectangle2D pos) {
   }

   protected void renderContainer(Container cont) {
      int saveIP = this.currentIPPosition;
      int saveBP = this.currentBPPosition;
      List blocks = cont.getBlocks();
      this.renderBlocks((Block)null, blocks);
      this.currentIPPosition = saveIP;
      this.currentBPPosition = saveBP;
   }

   protected void renderForeignObject(ForeignObject fo, Rectangle2D pos) {
   }

   public void renderXML(RendererContext ctx, Document doc, String namespace) {
      XMLHandler handler = this.userAgent.getXMLHandlerRegistry().getXMLHandler(this, (String)namespace);
      if (handler != null) {
         try {
            XMLHandlerConfigurator configurator = new XMLHandlerConfigurator(this.userAgent);
            configurator.configure(ctx, namespace);
            handler.handleXML(ctx, doc, namespace);
         } catch (Exception var7) {
            ResourceEventProducer eventProducer = ResourceEventProducer.Provider.get(ctx.getUserAgent().getEventBroadcaster());
            eventProducer.foreignXMLProcessingError(this, doc, namespace, var7);
         }
      } else {
         if (this.warnedXMLHandlers == null) {
            this.warnedXMLHandlers = new HashSet();
         }

         if (!this.warnedXMLHandlers.contains(namespace)) {
            this.warnedXMLHandlers.add(namespace);
            ResourceEventProducer eventProducer = ResourceEventProducer.Provider.get(ctx.getUserAgent().getEventBroadcaster());
            eventProducer.foreignXMLNoHandler(this, doc, namespace);
         }
      }

   }

   protected AffineTransform mptToPt(AffineTransform at) {
      double[] matrix = new double[6];
      at.getMatrix(matrix);
      matrix[4] /= 1000.0;
      matrix[5] /= 1000.0;
      return new AffineTransform(matrix);
   }

   protected AffineTransform ptToMpt(AffineTransform at) {
      double[] matrix = new double[6];
      at.getMatrix(matrix);
      matrix[4] = (double)Math.round(matrix[4] * 1000.0);
      matrix[5] = (double)Math.round(matrix[5] * 1000.0);
      return new AffineTransform(matrix);
   }
}
