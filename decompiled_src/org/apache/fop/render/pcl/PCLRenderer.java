package org.apache.fop.render.pcl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.Area;
import org.apache.fop.area.Block;
import org.apache.fop.area.BlockViewport;
import org.apache.fop.area.CTM;
import org.apache.fop.area.NormalFlow;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.RegionViewport;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.AbstractTextArea;
import org.apache.fop.area.inline.ForeignObject;
import org.apache.fop.area.inline.Image;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.Leader;
import org.apache.fop.area.inline.SpaceArea;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.area.inline.Viewport;
import org.apache.fop.area.inline.WordArea;
import org.apache.fop.datatypes.URISpecification;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontMetrics;
import org.apache.fop.render.Graphics2DAdapter;
import org.apache.fop.render.ImageHandlerUtil;
import org.apache.fop.render.PrintRenderer;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.RendererEventProducer;
import org.apache.fop.render.java2d.Base14FontCollection;
import org.apache.fop.render.java2d.ConfiguredFontCollection;
import org.apache.fop.render.java2d.FontMetricsMapper;
import org.apache.fop.render.java2d.InstalledFontCollection;
import org.apache.fop.render.java2d.Java2DFontMetrics;
import org.apache.fop.render.java2d.Java2DRenderer;
import org.apache.fop.render.pcl.extensions.PCLElementMapping;
import org.apache.fop.traits.BorderProps;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.impl.ImageGraphics2D;
import org.apache.xmlgraphics.image.loader.impl.ImageRendered;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.apache.xmlgraphics.util.UnitConv;
import org.w3c.dom.Document;
import org.xml.sax.Locator;

public class PCLRenderer extends PrintRenderer implements PCLConstants {
   private static Log log;
   public static final String MIME_TYPE = "application/vnd.hp-PCL";
   protected OutputStream out;
   protected PCLGenerator gen;
   private boolean ioTrouble = false;
   private final Stack graphicContextStack = new Stack();
   private GraphicContext graphicContext = new GraphicContext();
   private PCLPageDefinition currentPageDefinition;
   private int currentPrintDirection = 0;
   private GeneralPath currentPath = null;
   private Color currentFillColor = null;
   private PCLRenderingUtil pclUtil;
   private long pageWidth = 0L;
   private long pageHeight = 0L;
   private static final ImageFlavor[] FLAVORS;

   public void setUserAgent(FOUserAgent agent) {
      super.setUserAgent(agent);
      this.pclUtil = new PCLRenderingUtil(this.getUserAgent());
   }

   PCLRenderingUtil getPCLUtil() {
      return this.pclUtil;
   }

   public void setQualityBeforeSpeed(boolean qualityBeforeSpeed) {
      this.pclUtil.setRenderingMode(qualityBeforeSpeed ? PCLRenderingMode.QUALITY : PCLRenderingMode.SPEED);
   }

   public void setPJLDisabled(boolean disable) {
      this.pclUtil.setPJLDisabled(disable);
   }

   public boolean isPJLDisabled() {
      return this.pclUtil.isPJLDisabled();
   }

   public void setAllTextAsBitmaps(boolean allTextAsBitmaps) {
      this.pclUtil.setAllTextAsBitmaps(allTextAsBitmaps);
   }

   public void setupFontInfo(FontInfo inFontInfo) {
      this.fontInfo = inFontInfo;
      Graphics2D graphics2D = Java2DFontMetrics.createFontMetricsGraphics2D();
      FontCollection[] fontCollections = new FontCollection[]{new Base14FontCollection(graphics2D), new InstalledFontCollection(graphics2D), new ConfiguredFontCollection(this.getFontResolver(), this.getFontList())};
      this.userAgent.getFactory().getFontManager().setup(this.getFontInfo(), fontCollections);
   }

   protected void handleIOTrouble(IOException ioe) {
      if (!this.ioTrouble) {
         RendererEventProducer eventProducer = RendererEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.ioError(this, ioe);
         this.ioTrouble = true;
      }

   }

   public Graphics2DAdapter getGraphics2DAdapter() {
      return new PCLGraphics2DAdapter();
   }

   public GraphicContext getGraphicContext() {
      return this.graphicContext;
   }

   protected int getResolution() {
      int resolution = Math.round(this.userAgent.getTargetResolution());
      return resolution <= 300 ? 300 : 600;
   }

   public void startRenderer(OutputStream outputStream) throws IOException {
      log.debug("Rendering areas to PCL...");
      this.out = outputStream;
      this.gen = new PCLGenerator(this.out, this.getResolution());
      if (!this.isPJLDisabled()) {
         this.gen.universalEndOfLanguage();
         this.gen.writeText("@PJL COMMENT Produced by " + this.userAgent.getProducer() + "\n");
         if (this.userAgent.getTitle() != null) {
            this.gen.writeText("@PJL JOB NAME = \"" + this.userAgent.getTitle() + "\"\n");
         }

         this.gen.writeText("@PJL SET RESOLUTION = " + this.getResolution() + "\n");
         this.gen.writeText("@PJL ENTER LANGUAGE = PCL\n");
      }

      this.gen.resetPrinter();
      this.gen.setUnitOfMeasure(this.getResolution());
      this.gen.setRasterGraphicsResolution(this.getResolution());
   }

   public void stopRenderer() throws IOException {
      this.gen.separateJobs();
      this.gen.resetPrinter();
      if (!this.isPJLDisabled()) {
         this.gen.universalEndOfLanguage();
      }

   }

   public String getMimeType() {
      return "application/vnd.hp-PCL";
   }

   public void renderPage(PageViewport page) throws IOException, FOPException {
      this.saveGraphicsState();
      String paperSource = page.getForeignAttributeValue(PCLElementMapping.PCL_PAPER_SOURCE);
      if (paperSource != null) {
         this.gen.selectPaperSource(Integer.parseInt(paperSource));
      }

      String outputBin = page.getForeignAttributeValue(PCLElementMapping.PCL_OUTPUT_BIN);
      if (outputBin != null) {
         this.gen.selectOutputBin(Integer.parseInt(outputBin));
      }

      String pageDuplex = page.getForeignAttributeValue(PCLElementMapping.PCL_DUPLEX_MODE);
      if (pageDuplex != null) {
         this.gen.selectDuplexMode(Integer.parseInt(pageDuplex));
      }

      long pagewidth = Math.round(page.getViewArea().getWidth());
      long pageheight = Math.round(page.getViewArea().getHeight());
      this.selectPageFormat(pagewidth, pageheight);
      super.renderPage(page);
      this.gen.formFeed();
      this.restoreGraphicsState();
   }

   private void selectPageFormat(long pagewidth, long pageheight) throws IOException {
      if (pagewidth != this.pageWidth || pageheight != this.pageHeight) {
         this.pageWidth = pagewidth;
         this.pageHeight = pageheight;
         this.currentPageDefinition = PCLPageDefinition.getPageDefinition(pagewidth, pageheight, 1000);
         if (this.currentPageDefinition == null) {
            this.currentPageDefinition = PCLPageDefinition.getDefaultPageDefinition();
            log.warn("Paper type could not be determined. Falling back to: " + this.currentPageDefinition.getName());
         }

         if (log.isDebugEnabled()) {
            log.debug("page size: " + this.currentPageDefinition.getPhysicalPageSize());
            log.debug("logical page: " + this.currentPageDefinition.getLogicalPageRect());
         }

         if (this.currentPageDefinition.isLandscapeFormat()) {
            this.gen.writeCommand("&l1O");
         } else {
            this.gen.writeCommand("&l0O");
         }

         this.gen.selectPageSize(this.currentPageDefinition.getSelector());
         this.gen.clearHorizontalMargins();
         this.gen.setTopMargin(0);
      }

   }

   protected void saveGraphicsState() {
      this.graphicContextStack.push(this.graphicContext);
      this.graphicContext = (GraphicContext)this.graphicContext.clone();
   }

   protected void restoreGraphicsState() {
      this.graphicContext = (GraphicContext)this.graphicContextStack.pop();
   }

   protected void clipRect(float x, float y, float width, float height) {
   }

   private Point2D transformedPoint(float x, float y) {
      return this.transformedPoint(Math.round(x), Math.round(y));
   }

   private Point2D transformedPoint(int x, int y) {
      return PCLRenderingUtil.transformedPoint(x, y, this.graphicContext.getTransform(), this.currentPageDefinition, this.currentPrintDirection);
   }

   private void changePrintDirection() {
      AffineTransform at = this.graphicContext.getTransform();

      try {
         int newDir = PCLRenderingUtil.determinePrintDirection(at);
         if (newDir != this.currentPrintDirection) {
            this.currentPrintDirection = newDir;
            this.gen.changePrintDirection(this.currentPrintDirection);
         }
      } catch (IOException var4) {
         this.handleIOTrouble(var4);
      }

   }

   protected void startVParea(CTM ctm, Rectangle2D clippingRect) {
      this.saveGraphicsState();
      AffineTransform at = new AffineTransform(ctm.toArray());
      this.graphicContext.transform(at);
      this.changePrintDirection();
      if (log.isDebugEnabled()) {
         log.debug("startVPArea: " + at + " --> " + this.graphicContext.getTransform());
      }

   }

   protected void endVParea() {
      this.restoreGraphicsState();
      this.changePrintDirection();
      if (log.isDebugEnabled()) {
         log.debug("endVPArea() --> " + this.graphicContext.getTransform());
      }

   }

   protected void handleBlockTraits(Block block) {
      int borderPaddingStart = block.getBorderAndPaddingWidthStart();
      int borderPaddingBefore = block.getBorderAndPaddingWidthBefore();
      float startx = (float)this.currentIPPosition / 1000.0F;
      float starty = (float)this.currentBPPosition / 1000.0F;
      float width = (float)block.getIPD() / 1000.0F;
      float height = (float)block.getBPD() / 1000.0F;
      startx += (float)block.getStartIndent() / 1000.0F;
      startx -= (float)block.getBorderAndPaddingWidthStart() / 1000.0F;
      width += (float)borderPaddingStart / 1000.0F;
      width += (float)block.getBorderAndPaddingWidthEnd() / 1000.0F;
      height += (float)borderPaddingBefore / 1000.0F;
      height += (float)block.getBorderAndPaddingWidthAfter() / 1000.0F;
      this.drawBackAndBorders(block, startx, starty, width, height);
   }

   protected void handleRegionTraits(RegionViewport region) {
      Rectangle2D viewArea = region.getViewArea();
      float startx = (float)(viewArea.getX() / 1000.0);
      float starty = (float)(viewArea.getY() / 1000.0);
      float width = (float)(viewArea.getWidth() / 1000.0);
      float height = (float)(viewArea.getHeight() / 1000.0);
      if (region.getRegionReference().getRegionClass() == 58) {
         this.currentBPPosition = region.getBorderAndPaddingWidthBefore();
         this.currentIPPosition = region.getBorderAndPaddingWidthStart();
      }

      this.drawBackAndBorders(region, startx, starty, width, height);
   }

   protected void renderText(final TextArea text) {
      this.renderInlineAreaBackAndBorders(text);
      String fontname = this.getInternalFontNameForArea(text);
      final int fontsize = text.getTraitAsInteger(Trait.FONT_SIZE);
      int saveIP = this.currentIPPosition;
      int rx = this.currentIPPosition + text.getBorderAndPaddingWidthStart();
      int bl = this.currentBPPosition + text.getOffset() + text.getBaselineOffset();

      try {
         final Color col = (Color)text.getTrait(Trait.COLOR);
         boolean pclFont = this.pclUtil.isAllTextAsBitmaps() ? false : HardcodedFonts.setFont(this.gen, fontname, fontsize, text.getText());
         if (pclFont) {
            if (col != null) {
               this.gen.setTransparencyMode(true, false);
               this.gen.selectGrayscale(col);
            }

            this.saveGraphicsState();
            this.graphicContext.translate(rx, bl);
            this.setCursorPos(0.0F, 0.0F);
            this.gen.setTransparencyMode(true, true);
            if (text.hasUnderline()) {
               this.gen.writeCommand("&d0D");
            }

            super.renderText(text);
            if (text.hasUnderline()) {
               this.gen.writeCommand("&d@");
            }

            this.restoreGraphicsState();
         } else {
            final Font font = this.getFontFromArea(text);
            final int baseline = text.getBaselineOffset();
            int extraWidth = font.getFontSize() / 3;
            final FontMetricsMapper mapper = (FontMetricsMapper)this.fontInfo.getMetricsFor(font.getFontName());
            int maxAscent = mapper.getMaxAscent(font.getFontSize()) / 1000;
            final int additionalBPD = maxAscent - baseline;
            Graphics2DAdapter g2a = this.getGraphics2DAdapter();
            final Rectangle paintRect = new Rectangle(rx, this.currentBPPosition + text.getOffset() - additionalBPD, text.getIPD() + extraWidth, text.getBPD() + additionalBPD);
            RendererContext rc = this.createRendererContext(paintRect.x, paintRect.y, paintRect.width, paintRect.height, (Map)null);
            Map atts = new HashMap();
            atts.put(ImageHandlerUtil.CONVERSION_MODE, "bitmap");
            atts.put(SRC_TRANSPARENCY, "true");
            rc.setProperty("foreign-attributes", atts);
            Graphics2DImagePainter painter = new Graphics2DImagePainter() {
               public void paint(Graphics2D g2d, Rectangle2D area) {
                  g2d.setFont(mapper.getFont(font.getFontSize()));
                  g2d.translate(0, baseline + additionalBPD);
                  g2d.scale(1000.0, 1000.0);
                  g2d.setColor(col);
                  Java2DRenderer.renderText(text, g2d, font);
                  PCLRenderer.renderTextDecoration(g2d, mapper, fontsize, text, 0, 0);
               }

               public Dimension getImageSize() {
                  return paintRect.getSize();
               }
            };
            g2a.paintImage(painter, rc, paintRect.x, paintRect.y, paintRect.width, paintRect.height);
            this.currentIPPosition = saveIP + text.getAllocIPD();
         }
      } catch (IOException var20) {
         this.handleIOTrouble(var20);
      }

   }

   private static void renderTextDecoration(Graphics2D g2d, FontMetrics fm, int fontsize, InlineArea inline, int baseline, int startx) {
      boolean hasTextDeco = inline.hasUnderline() || inline.hasOverline() || inline.hasLineThrough();
      if (hasTextDeco) {
         float descender = (float)fm.getDescender(fontsize) / 1000.0F;
         float capHeight = (float)fm.getCapHeight(fontsize) / 1000.0F;
         float lineWidth = descender / -4.0F / 1000.0F;
         float endx = (float)(startx + inline.getIPD()) / 1000.0F;
         Color ct;
         float y;
         if (inline.hasUnderline()) {
            ct = (Color)inline.getTrait(Trait.UNDERLINE_COLOR);
            g2d.setColor(ct);
            y = (float)baseline - descender / 2.0F;
            g2d.setStroke(new BasicStroke(lineWidth));
            g2d.draw(new Line2D.Float((float)startx / 1000.0F, y / 1000.0F, endx, y / 1000.0F));
         }

         if (inline.hasOverline()) {
            ct = (Color)inline.getTrait(Trait.OVERLINE_COLOR);
            g2d.setColor(ct);
            y = (float)((double)baseline - 1.1 * (double)capHeight);
            g2d.setStroke(new BasicStroke(lineWidth));
            g2d.draw(new Line2D.Float((float)startx / 1000.0F, y / 1000.0F, endx, y / 1000.0F));
         }

         if (inline.hasLineThrough()) {
            ct = (Color)inline.getTrait(Trait.LINETHROUGH_COLOR);
            g2d.setColor(ct);
            y = (float)((double)baseline - 0.45 * (double)capHeight);
            g2d.setStroke(new BasicStroke(lineWidth));
            g2d.draw(new Line2D.Float((float)startx / 1000.0F, y / 1000.0F, endx, y / 1000.0F));
         }
      }

   }

   void setCursorPos(float x, float y) {
      try {
         Point2D transPoint = this.transformedPoint(x, y);
         this.gen.setCursorPos(transPoint.getX(), transPoint.getY());
      } catch (IOException var4) {
         this.handleIOTrouble(var4);
      }

   }

   protected void clip() {
      if (this.currentPath == null) {
         throw new IllegalStateException("No current path available!");
      } else {
         this.currentPath = null;
      }
   }

   protected void closePath() {
      this.currentPath.closePath();
   }

   protected void lineTo(float x, float y) {
      if (this.currentPath == null) {
         this.currentPath = new GeneralPath();
      }

      this.currentPath.lineTo(x, y);
   }

   protected void moveTo(float x, float y) {
      if (this.currentPath == null) {
         this.currentPath = new GeneralPath();
      }

      this.currentPath.moveTo(x, y);
   }

   protected void fillRect(float x, float y, float width, float height) {
      try {
         this.setCursorPos(x * 1000.0F, y * 1000.0F);
         this.gen.fillRect((int)(width * 1000.0F), (int)(height * 1000.0F), this.currentFillColor);
      } catch (IOException var6) {
         this.handleIOTrouble(var6);
      }

   }

   protected void updateFillColor(Color color) {
      this.currentFillColor = color;
   }

   protected void renderWord(WordArea word) {
      String s = word.getWord();

      try {
         this.gen.writeText(s);
      } catch (IOException var4) {
         this.handleIOTrouble(var4);
      }

      super.renderWord(word);
   }

   protected void renderSpace(SpaceArea space) {
      AbstractTextArea textArea = (AbstractTextArea)space.getParentArea();
      String s = space.getSpace();
      char sp = s.charAt(0);
      Font font = this.getFontFromArea(textArea);
      int tws = space.isAdjustable() ? textArea.getTextWordSpaceAdjust() + 2 * textArea.getTextLetterSpaceAdjust() : 0;
      double dx = (double)((float)(font.getCharWidth(sp) + tws) / 100.0F);

      try {
         this.gen.writeCommand("&a+" + this.gen.formatDouble2(dx) + "H");
      } catch (IOException var10) {
         this.handleIOTrouble(var10);
      }

      super.renderSpace(space);
   }

   public void renderViewport(Viewport viewport) {
      float x = (float)this.currentIPPosition / 1000.0F;
      float y = (float)(this.currentBPPosition + viewport.getOffset()) / 1000.0F;
      float width = (float)viewport.getIPD() / 1000.0F;
      float height = (float)viewport.getBPD() / 1000.0F;
      float borderPaddingStart = (float)viewport.getBorderAndPaddingWidthStart() / 1000.0F;
      float borderPaddingBefore = (float)viewport.getBorderAndPaddingWidthBefore() / 1000.0F;
      float bpwidth = borderPaddingStart + (float)viewport.getBorderAndPaddingWidthEnd() / 1000.0F;
      float bpheight = borderPaddingBefore + (float)viewport.getBorderAndPaddingWidthAfter() / 1000.0F;
      this.drawBackAndBorders(viewport, x, y, width + bpwidth, height + bpheight);
      if (viewport.getClip()) {
         this.saveGraphicsState();
         this.clipRect(x + borderPaddingStart, y + borderPaddingBefore, width, height);
      }

      super.renderViewport(viewport);
      if (viewport.getClip()) {
         this.restoreGraphicsState();
      }

   }

   protected void renderBlockViewport(BlockViewport bv, List children) {
      int saveIP = this.currentIPPosition;
      int saveBP = this.currentBPPosition;
      CTM ctm = bv.getCTM();
      int borderPaddingStart = bv.getBorderAndPaddingWidthStart();
      int borderPaddingBefore = bv.getBorderAndPaddingWidthBefore();
      float width = (float)bv.getIPD() / 1000.0F;
      float height = (float)bv.getBPD() / 1000.0F;
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
         this.saveGraphicsState();
         this.concatenateTransformationMatrix(UnitConv.mptToPt(positionTransform));
         float bpwidth = (float)(borderPaddingStart + bv.getBorderAndPaddingWidthEnd()) / 1000.0F;
         float bpheight = (float)(borderPaddingBefore + bv.getBorderAndPaddingWidthAfter()) / 1000.0F;
         this.drawBackAndBorders(bv, 0.0F, 0.0F, width + bpwidth, height + bpheight);
         AffineTransform contentRectTransform = new AffineTransform();
         contentRectTransform.translate((double)borderPaddingStart, (double)borderPaddingBefore);
         this.concatenateTransformationMatrix(UnitConv.mptToPt(contentRectTransform));
         if (bv.getClip()) {
            this.clipRect(0.0F, 0.0F, width, height);
         }

         this.saveGraphicsState();
         AffineTransform contentTransform = ctm.toAffineTransform();
         this.concatenateTransformationMatrix(UnitConv.mptToPt(contentTransform));
         this.currentIPPosition = 0;
         this.currentBPPosition = 0;
         this.renderBlocks(bv, children);
         this.restoreGraphicsState();
         this.restoreGraphicsState();
         if (breakOutList != null) {
            this.restoreStateStackAfterBreakOut(breakOutList);
         }

         this.currentIPPosition = saveIP;
         this.currentBPPosition = saveBP;
      }

   }

   protected void renderReferenceArea(Block block) {
      int saveIP = this.currentIPPosition;
      int saveBP = this.currentBPPosition;
      AffineTransform at = new AffineTransform();
      at.translate((double)this.currentIPPosition, (double)this.currentBPPosition);
      at.translate((double)block.getXOffset(), (double)block.getYOffset());
      at.translate(0.0, (double)block.getSpaceBefore());
      if (!at.isIdentity()) {
         this.saveGraphicsState();
         this.concatenateTransformationMatrix(UnitConv.mptToPt(at));
      }

      this.currentIPPosition = 0;
      this.currentBPPosition = 0;
      this.handleBlockTraits(block);
      List children = block.getChildAreas();
      if (children != null) {
         this.renderBlocks(block, children);
      }

      if (!at.isIdentity()) {
         this.restoreGraphicsState();
      }

      this.currentIPPosition = saveIP;
      this.currentBPPosition = saveBP;
   }

   protected void renderFlow(NormalFlow flow) {
      int saveIP = this.currentIPPosition;
      int saveBP = this.currentBPPosition;
      AffineTransform at = new AffineTransform();
      at.translate((double)this.currentIPPosition, (double)this.currentBPPosition);
      if (!at.isIdentity()) {
         this.saveGraphicsState();
         this.concatenateTransformationMatrix(UnitConv.mptToPt(at));
      }

      this.currentIPPosition = 0;
      this.currentBPPosition = 0;
      super.renderFlow(flow);
      if (!at.isIdentity()) {
         this.restoreGraphicsState();
      }

      this.currentIPPosition = saveIP;
      this.currentBPPosition = saveBP;
   }

   protected void concatenateTransformationMatrix(AffineTransform at) {
      if (!at.isIdentity()) {
         this.graphicContext.transform(UnitConv.ptToMpt(at));
         this.changePrintDirection();
      }

   }

   private List breakOutOfStateStack() {
      log.debug("Block.FIXED --> break out");
      List breakOutList = new ArrayList();

      while(!this.graphicContextStack.empty()) {
         breakOutList.add(0, this.graphicContext);
         this.restoreGraphicsState();
      }

      return breakOutList;
   }

   private void restoreStateStackAfterBreakOut(List breakOutList) {
      log.debug("Block.FIXED --> restoring context after break-out");
      int i = 0;

      for(int c = breakOutList.size(); i < c; ++i) {
         this.saveGraphicsState();
         this.graphicContext = (GraphicContext)breakOutList.get(i);
      }

   }

   protected RendererContext createRendererContext(int x, int y, int width, int height, Map foreignAttributes) {
      RendererContext context = super.createRendererContext(x, y, width, height, foreignAttributes);
      context.setProperty("color-canvas", this.pclUtil.isColorCanvasEnabled());
      return context;
   }

   public void renderImage(Image image, Rectangle2D pos) {
      this.drawImage(image.getURL(), pos, image.getForeignAttributes());
   }

   protected void drawImage(String uri, Rectangle2D pos, Map foreignAttributes) {
      uri = URISpecification.getURL(uri);
      Rectangle posInt = new Rectangle((int)pos.getX(), (int)pos.getY(), (int)pos.getWidth(), (int)pos.getHeight());
      Point origin = new Point(this.currentIPPosition, this.currentBPPosition);
      int x = origin.x + posInt.x;
      int y = origin.y + posInt.y;
      ImageManager manager = this.getUserAgent().getFactory().getImageManager();
      ImageInfo info = null;

      ResourceEventProducer eventProducer;
      try {
         ImageSessionContext sessionContext = this.getUserAgent().getImageSessionContext();
         info = manager.getImageInfo(uri, sessionContext);
         Map hints = ImageUtil.getDefaultHints(sessionContext);
         org.apache.xmlgraphics.image.loader.Image img = manager.getImage(info, FLAVORS, hints, sessionContext);
         if (img instanceof ImageGraphics2D) {
            ImageGraphics2D imageG2D = (ImageGraphics2D)img;
            RendererContext context = this.createRendererContext(posInt.x, posInt.y, posInt.width, posInt.height, foreignAttributes);
            this.getGraphics2DAdapter().paintImage(imageG2D.getGraphics2DImagePainter(), context, x, y, posInt.width, posInt.height);
         } else if (img instanceof ImageRendered) {
            ImageRendered imgRend = (ImageRendered)img;
            RenderedImage ri = imgRend.getRenderedImage();
            this.setCursorPos((float)x, (float)y);
            this.gen.paintBitmap(ri, new Dimension(posInt.width, posInt.height), false);
         } else {
            if (!(img instanceof ImageXMLDOM)) {
               throw new UnsupportedOperationException("Unsupported image type: " + img);
            }

            ImageXMLDOM imgXML = (ImageXMLDOM)img;
            this.renderDocument(imgXML.getDocument(), imgXML.getRootNamespace(), pos, foreignAttributes);
         }
      } catch (ImageException var15) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageError(this, info != null ? info.toString() : uri, var15, (Locator)null);
      } catch (FileNotFoundException var16) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageNotFound(this, info != null ? info.toString() : uri, var16, (Locator)null);
      } catch (IOException var17) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageIOError(this, info != null ? info.toString() : uri, var17, (Locator)null);
      }

   }

   public void renderForeignObject(ForeignObject fo, Rectangle2D pos) {
      Document doc = fo.getDocument();
      String ns = fo.getNameSpace();
      this.renderDocument(doc, ns, pos, fo.getForeignAttributes());
   }

   protected void renderInlineAreaBackAndBorders(InlineArea area) {
      float x = (float)this.currentIPPosition / 1000.0F;
      float y = (float)(this.currentBPPosition + area.getOffset()) / 1000.0F;
      float width = (float)area.getIPD() / 1000.0F;
      float height = (float)area.getBPD() / 1000.0F;
      float borderPaddingStart = (float)area.getBorderAndPaddingWidthStart() / 1000.0F;
      float borderPaddingBefore = (float)area.getBorderAndPaddingWidthBefore() / 1000.0F;
      float bpwidth = borderPaddingStart + (float)area.getBorderAndPaddingWidthEnd() / 1000.0F;
      float bpheight = borderPaddingBefore + (float)area.getBorderAndPaddingWidthAfter() / 1000.0F;
      if (height != 0.0F || bpheight != 0.0F && bpwidth != 0.0F) {
         this.drawBackAndBorders(area, x, y - borderPaddingBefore, width + bpwidth, height + bpheight);
      }

   }

   protected void drawBackAndBorders(Area area, float startx, float starty, float width, float height) {
      BorderProps bpsBefore = (BorderProps)area.getTrait(Trait.BORDER_BEFORE);
      BorderProps bpsAfter = (BorderProps)area.getTrait(Trait.BORDER_AFTER);
      BorderProps bpsStart = (BorderProps)area.getTrait(Trait.BORDER_START);
      BorderProps bpsEnd = (BorderProps)area.getTrait(Trait.BORDER_END);
      Trait.Background back = (Trait.Background)area.getTrait(Trait.BACKGROUND);
      if (back != null) {
         float sx = startx;
         float sy = starty;
         float paddRectWidth = width;
         float paddRectHeight = height;
         if (bpsStart != null) {
            sx = startx + (float)bpsStart.width / 1000.0F;
            paddRectWidth = width - (float)bpsStart.width / 1000.0F;
         }

         if (bpsBefore != null) {
            sy = starty + (float)bpsBefore.width / 1000.0F;
            paddRectHeight = height - (float)bpsBefore.width / 1000.0F;
         }

         if (bpsEnd != null) {
            paddRectWidth -= (float)bpsEnd.width / 1000.0F;
         }

         if (bpsAfter != null) {
            paddRectHeight -= (float)bpsAfter.width / 1000.0F;
         }

         if (back.getColor() != null) {
            this.updateFillColor(back.getColor());
            this.fillRect(sx, sy, paddRectWidth, paddRectHeight);
         }

         if (back.getImageInfo() != null) {
            ImageSize imageSize = back.getImageInfo().getSize();
            this.saveGraphicsState();
            this.clipRect(sx, sy, paddRectWidth, paddRectHeight);
            int horzCount = (int)(paddRectWidth * 1000.0F / (float)imageSize.getWidthMpt() + 1.0F);
            int vertCount = (int)(paddRectHeight * 1000.0F / (float)imageSize.getHeightMpt() + 1.0F);
            if (back.getRepeat() == 96) {
               horzCount = 1;
               vertCount = 1;
            } else if (back.getRepeat() == 113) {
               vertCount = 1;
            } else if (back.getRepeat() == 114) {
               horzCount = 1;
            }

            sx *= 1000.0F;
            sy *= 1000.0F;
            if (horzCount == 1) {
               sx += (float)back.getHoriz();
            }

            if (vertCount == 1) {
               sy += (float)back.getVertical();
            }

            for(int x = 0; x < horzCount; ++x) {
               for(int y = 0; y < vertCount; ++y) {
                  Rectangle2D pos = new Rectangle2D.Float(sx - (float)this.currentIPPosition + (float)(x * imageSize.getWidthMpt()), sy - (float)this.currentBPPosition + (float)(y * imageSize.getHeightMpt()), (float)imageSize.getWidthMpt(), (float)imageSize.getHeightMpt());
                  this.drawImage(back.getURL(), pos, (Map)null);
               }
            }

            this.restoreGraphicsState();
         }
      }

      Rectangle2D.Float borderRect = new Rectangle2D.Float(startx, starty, width, height);
      this.drawBorders(borderRect, bpsBefore, bpsAfter, bpsStart, bpsEnd);
   }

   protected void drawBorders(Rectangle2D.Float borderRect, BorderProps bpsBefore, BorderProps bpsAfter, BorderProps bpsStart, BorderProps bpsEnd) {
      if (bpsBefore != null || bpsAfter != null || bpsStart != null || bpsEnd != null) {
         if (PCLRenderingMode.SPEED == this.pclUtil.getRenderingMode()) {
            this.drawFastBorders(borderRect, bpsBefore, bpsAfter, bpsStart, bpsEnd);
         } else {
            this.drawQualityBorders(borderRect, bpsBefore, bpsAfter, bpsStart, bpsEnd);
         }

      }
   }

   protected void drawFastBorders(Rectangle2D.Float borderRect, BorderProps bpsBefore, BorderProps bpsAfter, BorderProps bpsStart, BorderProps bpsEnd) {
      float startx = borderRect.x;
      float starty = borderRect.y;
      float width = borderRect.width;
      float height = borderRect.height;
      float borderWidth;
      if (bpsBefore != null) {
         borderWidth = (float)bpsBefore.width / 1000.0F;
         this.updateFillColor(bpsBefore.color);
         this.fillRect(startx, starty, width, borderWidth);
      }

      if (bpsAfter != null) {
         borderWidth = (float)bpsAfter.width / 1000.0F;
         this.updateFillColor(bpsAfter.color);
         this.fillRect(startx, starty + height - borderWidth, width, borderWidth);
      }

      if (bpsStart != null) {
         borderWidth = (float)bpsStart.width / 1000.0F;
         this.updateFillColor(bpsStart.color);
         this.fillRect(startx, starty, borderWidth, height);
      }

      if (bpsEnd != null) {
         borderWidth = (float)bpsEnd.width / 1000.0F;
         this.updateFillColor(bpsEnd.color);
         this.fillRect(startx + width - borderWidth, starty, borderWidth, height);
      }

   }

   protected void drawQualityBorders(Rectangle2D.Float borderRect, final BorderProps bpsBefore, final BorderProps bpsAfter, final BorderProps bpsStart, final BorderProps bpsEnd) {
      Graphics2DAdapter g2a = this.getGraphics2DAdapter();
      final Rectangle2D.Float effBorderRect = new Rectangle2D.Float(0.0F, 0.0F, borderRect.width, borderRect.height);
      final Rectangle paintRect = new Rectangle(Math.round(borderRect.x * 1000.0F), Math.round(borderRect.y * 1000.0F), (int)Math.floor((double)(borderRect.width * 1000.0F)) + 1, (int)Math.floor((double)(borderRect.height * 1000.0F)) + 1);
      final int pixelWidth = (int)Math.round(UnitConv.in2mpt(1.0) / (double)this.userAgent.getTargetResolution());
      final int xoffset = Math.round(-effBorderRect.x * 1000.0F) + pixelWidth;
      paintRect.x += xoffset;
      paintRect.y += pixelWidth;
      paintRect.width += 2 * pixelWidth;
      paintRect.height += 2 * pixelWidth;
      RendererContext rc = this.createRendererContext(paintRect.x, paintRect.y, paintRect.width, paintRect.height, (Map)null);
      Map atts = new HashMap();
      atts.put(ImageHandlerUtil.CONVERSION_MODE, "bitmap");
      atts.put(SRC_TRANSPARENCY, "true");
      rc.setProperty("foreign-attributes", atts);
      Graphics2DImagePainter painter = new Graphics2DImagePainter() {
         public void paint(Graphics2D g2d, Rectangle2D area) {
            g2d.translate(xoffset, pixelWidth);
            g2d.scale(1000.0, 1000.0);
            float startx = effBorderRect.x;
            float starty = effBorderRect.y;
            float width = effBorderRect.width;
            float height = effBorderRect.height;
            boolean[] b = new boolean[]{bpsBefore != null, bpsEnd != null, bpsAfter != null, bpsStart != null};
            if (b[0] || b[1] || b[2] || b[3]) {
               float[] bw = new float[]{b[0] ? (float)bpsBefore.width / 1000.0F : 0.0F, b[1] ? (float)bpsEnd.width / 1000.0F : 0.0F, b[2] ? (float)bpsAfter.width / 1000.0F : 0.0F, b[3] ? (float)bpsStart.width / 1000.0F : 0.0F};
               float[] clipw = new float[]{(float)BorderProps.getClippedWidth(bpsBefore) / 1000.0F, (float)BorderProps.getClippedWidth(bpsEnd) / 1000.0F, (float)BorderProps.getClippedWidth(bpsAfter) / 1000.0F, (float)BorderProps.getClippedWidth(bpsStart) / 1000.0F};
               starty += clipw[0];
               height -= clipw[0];
               height -= clipw[2];
               startx += clipw[3];
               width -= clipw[3];
               width -= clipw[1];
               boolean[] slant = new boolean[]{b[3] && b[0], b[0] && b[1], b[1] && b[2], b[2] && b[3]};
               float sy2;
               float ey1;
               float ey2;
               float outerx;
               float clipx;
               float innerx;
               Graphics2D g;
               float sy1a;
               float ey1a;
               Rectangle2D.Float lineRect;
               if (bpsBefore != null) {
                  sy2 = slant[0] ? startx + bw[3] - clipw[3] : startx;
                  ey1 = startx + width;
                  ey2 = slant[1] ? ey1 - bw[1] + clipw[1] : ey1;
                  outerx = starty - clipw[0];
                  clipx = outerx + clipw[0];
                  innerx = outerx + bw[0];
                  g = (Graphics2D)g2d.create();
                  PCLRenderer.this.moveTo(startx, clipx);
                  sy1a = startx;
                  ey1a = ey1;
                  if (bpsBefore.mode == 2) {
                     if (bpsStart != null && bpsStart.mode == 2) {
                        sy1a = startx - clipw[3];
                     }

                     if (bpsEnd != null && bpsEnd.mode == 2) {
                        ey1a = ey1 + clipw[1];
                     }

                     PCLRenderer.this.lineTo(sy1a, outerx);
                     PCLRenderer.this.lineTo(ey1a, outerx);
                  }

                  PCLRenderer.this.lineTo(ey1, clipx);
                  PCLRenderer.this.lineTo(ey2, innerx);
                  PCLRenderer.this.lineTo(sy2, innerx);
                  PCLRenderer.this.closePath();
                  g.clip(PCLRenderer.this.currentPath);
                  PCLRenderer.this.currentPath = null;
                  lineRect = new Rectangle2D.Float(sy1a, outerx, ey1a - sy1a, innerx - outerx);
                  Java2DRenderer.drawBorderLine(lineRect, true, true, bpsBefore.style, bpsBefore.color, g);
               }

               if (bpsEnd != null) {
                  sy2 = slant[1] ? starty + bw[0] - clipw[0] : starty;
                  ey1 = starty + height;
                  ey2 = slant[2] ? ey1 - bw[2] + clipw[2] : ey1;
                  outerx = startx + width + clipw[1];
                  clipx = outerx - clipw[1];
                  innerx = outerx - bw[1];
                  g = (Graphics2D)g2d.create();
                  PCLRenderer.this.moveTo(clipx, starty);
                  sy1a = starty;
                  ey1a = ey1;
                  if (bpsEnd.mode == 2) {
                     if (bpsBefore != null && bpsBefore.mode == 2) {
                        sy1a = starty - clipw[0];
                     }

                     if (bpsAfter != null && bpsAfter.mode == 2) {
                        ey1a = ey1 + clipw[2];
                     }

                     PCLRenderer.this.lineTo(outerx, sy1a);
                     PCLRenderer.this.lineTo(outerx, ey1a);
                  }

                  PCLRenderer.this.lineTo(clipx, ey1);
                  PCLRenderer.this.lineTo(innerx, ey2);
                  PCLRenderer.this.lineTo(innerx, sy2);
                  PCLRenderer.this.closePath();
                  g.setClip(PCLRenderer.this.currentPath);
                  PCLRenderer.this.currentPath = null;
                  lineRect = new Rectangle2D.Float(innerx, sy1a, outerx - innerx, ey1a - sy1a);
                  Java2DRenderer.drawBorderLine(lineRect, false, false, bpsEnd.style, bpsEnd.color, g);
               }

               if (bpsAfter != null) {
                  sy2 = slant[3] ? startx + bw[3] - clipw[3] : startx;
                  ey1 = startx + width;
                  ey2 = slant[2] ? ey1 - bw[1] + clipw[1] : ey1;
                  outerx = starty + height + clipw[2];
                  clipx = outerx - clipw[2];
                  innerx = outerx - bw[2];
                  g = (Graphics2D)g2d.create();
                  PCLRenderer.this.moveTo(ey1, clipx);
                  sy1a = startx;
                  ey1a = ey1;
                  if (bpsAfter.mode == 2) {
                     if (bpsStart != null && bpsStart.mode == 2) {
                        sy1a = startx - clipw[3];
                     }

                     if (bpsEnd != null && bpsEnd.mode == 2) {
                        ey1a = ey1 + clipw[1];
                     }

                     PCLRenderer.this.lineTo(ey1a, outerx);
                     PCLRenderer.this.lineTo(sy1a, outerx);
                  }

                  PCLRenderer.this.lineTo(startx, clipx);
                  PCLRenderer.this.lineTo(sy2, innerx);
                  PCLRenderer.this.lineTo(ey2, innerx);
                  PCLRenderer.this.closePath();
                  g.setClip(PCLRenderer.this.currentPath);
                  PCLRenderer.this.currentPath = null;
                  lineRect = new Rectangle2D.Float(sy1a, innerx, ey1a - sy1a, outerx - innerx);
                  Java2DRenderer.drawBorderLine(lineRect, true, false, bpsAfter.style, bpsAfter.color, g);
               }

               if (bpsStart != null) {
                  sy2 = slant[0] ? starty + bw[0] - clipw[0] : starty;
                  ey1 = starty + height;
                  ey2 = slant[3] ? ey1 - bw[2] + clipw[2] : ey1;
                  outerx = startx - clipw[3];
                  clipx = outerx + clipw[3];
                  innerx = outerx + bw[3];
                  g = (Graphics2D)g2d.create();
                  PCLRenderer.this.moveTo(clipx, ey1);
                  sy1a = starty;
                  ey1a = ey1;
                  if (bpsStart.mode == 2) {
                     if (bpsBefore != null && bpsBefore.mode == 2) {
                        sy1a = starty - clipw[0];
                     }

                     if (bpsAfter != null && bpsAfter.mode == 2) {
                        ey1a = ey1 + clipw[2];
                     }

                     PCLRenderer.this.lineTo(outerx, ey1a);
                     PCLRenderer.this.lineTo(outerx, sy1a);
                  }

                  PCLRenderer.this.lineTo(clipx, starty);
                  PCLRenderer.this.lineTo(innerx, sy2);
                  PCLRenderer.this.lineTo(innerx, ey2);
                  PCLRenderer.this.closePath();
                  g.setClip(PCLRenderer.this.currentPath);
                  PCLRenderer.this.currentPath = null;
                  lineRect = new Rectangle2D.Float(outerx, sy1a, innerx - outerx, ey1a - sy1a);
                  Java2DRenderer.drawBorderLine(lineRect, false, false, bpsStart.style, bpsStart.color, g);
               }

            }
         }

         public Dimension getImageSize() {
            return paintRect.getSize();
         }
      };

      try {
         g2a.paintImage(painter, rc, paintRect.x - xoffset, paintRect.y, paintRect.width, paintRect.height);
      } catch (IOException var16) {
         this.handleIOTrouble(var16);
      }

   }

   public void renderLeader(Leader area) {
      this.renderInlineAreaBackAndBorders(area);
      this.saveGraphicsState();
      int style = area.getRuleStyle();
      float startx = (float)(this.currentIPPosition + area.getBorderAndPaddingWidthStart()) / 1000.0F;
      float starty = (float)(this.currentBPPosition + area.getOffset()) / 1000.0F;
      float endx = (float)(this.currentIPPosition + area.getBorderAndPaddingWidthStart() + area.getIPD()) / 1000.0F;
      float ruleThickness = (float)area.getRuleThickness() / 1000.0F;
      Color col = (Color)area.getTrait(Trait.COLOR);
      switch (style) {
         case 31:
         case 36:
         case 37:
         case 55:
         case 119:
         case 133:
            this.updateFillColor(col);
            this.fillRect(startx, starty, endx - startx, ruleThickness);
            this.restoreGraphicsState();
            super.renderLeader(area);
            return;
         default:
            throw new UnsupportedOperationException("rule style not supported");
      }
   }

   static {
      log = LogFactory.getLog(PCLRenderer.class);
      FLAVORS = new ImageFlavor[]{ImageFlavor.GRAPHICS2D, ImageFlavor.BUFFERED_IMAGE, ImageFlavor.RENDERED_IMAGE, ImageFlavor.XML_DOM};
   }
}
