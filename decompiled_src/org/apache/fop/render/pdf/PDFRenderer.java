package org.apache.fop.render.pdf;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.Area;
import org.apache.fop.area.Block;
import org.apache.fop.area.BookmarkData;
import org.apache.fop.area.CTM;
import org.apache.fop.area.DestinationData;
import org.apache.fop.area.LineArea;
import org.apache.fop.area.OffDocumentExtensionAttachment;
import org.apache.fop.area.OffDocumentItem;
import org.apache.fop.area.PageSequence;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.AbstractTextArea;
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
import org.apache.fop.fonts.LazyFont;
import org.apache.fop.fonts.SingleByteFont;
import org.apache.fop.fonts.Typeface;
import org.apache.fop.pdf.PDFAMode;
import org.apache.fop.pdf.PDFAction;
import org.apache.fop.pdf.PDFAnnotList;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFEncryptionParams;
import org.apache.fop.pdf.PDFFactory;
import org.apache.fop.pdf.PDFGoTo;
import org.apache.fop.pdf.PDFInfo;
import org.apache.fop.pdf.PDFLink;
import org.apache.fop.pdf.PDFNumber;
import org.apache.fop.pdf.PDFOutline;
import org.apache.fop.pdf.PDFPage;
import org.apache.fop.pdf.PDFPaintingState;
import org.apache.fop.pdf.PDFResourceContext;
import org.apache.fop.pdf.PDFResources;
import org.apache.fop.pdf.PDFTextUtil;
import org.apache.fop.pdf.PDFXMode;
import org.apache.fop.pdf.PDFXObject;
import org.apache.fop.render.AbstractPathOrientedRenderer;
import org.apache.fop.render.Graphics2DAdapter;
import org.apache.fop.render.RendererContext;
import org.apache.fop.traits.RuleStyle;
import org.apache.fop.util.AbstractPaintingState;
import org.apache.fop.util.CharUtilities;
import org.apache.fop.util.XMLUtil;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.Locator;

public class PDFRenderer extends AbstractPathOrientedRenderer implements PDFConfigurationConstants {
   public static final String MIME_TYPE = "application/pdf";
   public static final int NORMAL_PDF_RESOLUTION = 72;
   protected static final boolean WRITE_COMMENTS = true;
   protected PDFDocument pdfDoc;
   protected PDFRenderingUtil pdfUtil;
   private Map pages;
   protected Map pageReferences = new HashMap();
   protected Map idPositions = new HashMap();
   protected Map idGoTos = new HashMap();
   protected List unfinishedGoTos = new ArrayList();
   protected OutputStream ostream;
   protected PDFResources pdfResources;
   protected PDFContentGenerator generator;
   private PDFBorderPainter borderPainter;
   protected PDFResourceContext currentContext = null;
   protected PDFPage currentPage;
   protected String currentPageRef;
   protected int pageHeight;
   private final PDFImageHandlerRegistry imageHandlerRegistry = new PDFImageHandlerRegistry();
   private boolean accessEnabled;
   private PDFLogicalStructureHandler logicalStructureHandler;
   private int pageSequenceIndex;
   private String imageReference;

   public void setUserAgent(FOUserAgent agent) {
      super.setUserAgent(agent);
      this.pdfUtil = new PDFRenderingUtil(this.getUserAgent());
      this.accessEnabled = agent.isAccessibilityEnabled();
   }

   PDFRenderingUtil getPDFUtil() {
      return this.pdfUtil;
   }

   PDFContentGenerator getGenerator() {
      return this.generator;
   }

   PDFPaintingState getState() {
      return this.getGenerator().getState();
   }

   public void startRenderer(OutputStream stream) throws IOException {
      if (this.userAgent == null) {
         throw new IllegalStateException("UserAgent must be set before starting the renderer");
      } else {
         this.ostream = stream;
         this.pdfDoc = this.pdfUtil.setupPDFDocument(stream);
         if (this.accessEnabled) {
            this.pdfDoc.getRoot().makeTagged();
            this.logicalStructureHandler = new PDFLogicalStructureHandler(this.pdfDoc, this.userAgent.getEventBroadcaster());
         }

      }
   }

   protected void finishOpenGoTos() {
      int count = this.unfinishedGoTos.size();
      if (count > 0) {
         Point2D.Float defaultPos = new Point2D.Float(0.0F, (float)this.pageHeight / 1000.0F);

         while(!this.unfinishedGoTos.isEmpty()) {
            PDFGoTo gt = (PDFGoTo)this.unfinishedGoTos.get(0);
            this.finishIDGoTo(gt, defaultPos);
         }

         PDFEventProducer eventProducer = PDFEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.nonFullyResolvedLinkTargets(this, count);
      }

   }

   public void stopRenderer() throws IOException {
      this.finishOpenGoTos();
      this.pdfDoc.getResources().addFonts(this.pdfDoc, this.fontInfo);
      this.pdfDoc.outputTrailer(this.ostream);
      this.pdfDoc = null;
      this.ostream = null;
      this.pages = null;
      this.pageReferences.clear();
      this.pdfResources = null;
      this.generator = null;
      this.currentContext = null;
      this.currentPage = null;
      this.idPositions.clear();
      this.idGoTos.clear();
   }

   public boolean supportsOutOfOrder() {
      return !this.accessEnabled;
   }

   public void processOffDocumentItem(OffDocumentItem odi) {
      if (odi instanceof DestinationData) {
         this.renderDestination((DestinationData)odi);
      } else if (odi instanceof BookmarkData) {
         this.renderBookmarkTree((BookmarkData)odi);
      } else if (odi instanceof OffDocumentExtensionAttachment) {
         ExtensionAttachment attachment = ((OffDocumentExtensionAttachment)odi).getAttachment();
         if ("adobe:ns:meta/".equals(attachment.getCategory())) {
            this.pdfUtil.renderXMPMetadata((XMPMetadata)attachment);
         }
      }

   }

   private void renderDestination(DestinationData dd) {
      String targetID = dd.getIDRef();
      if (targetID != null && targetID.length() != 0) {
         PageViewport pv = dd.getPageViewport();
         if (pv != null) {
            PDFGoTo gt = this.getPDFGoToForID(targetID, pv.getKey());
            this.pdfDoc.getFactory().makeDestination(dd.getIDRef(), gt.makeReference());
         } else {
            log.debug("Unresolved destination item received: " + dd.getIDRef());
         }

      } else {
         throw new IllegalArgumentException("DestinationData must contain a ID reference");
      }
   }

   protected void renderBookmarkTree(BookmarkData bookmarks) {
      for(int i = 0; i < bookmarks.getCount(); ++i) {
         BookmarkData ext = bookmarks.getSubData(i);
         this.renderBookmarkItem(ext, (PDFOutline)null);
      }

   }

   private void renderBookmarkItem(BookmarkData bookmarkItem, PDFOutline parentBookmarkItem) {
      PDFOutline pdfOutline = null;
      String targetID = bookmarkItem.getIDRef();
      if (targetID != null && targetID.length() != 0) {
         PageViewport pv = bookmarkItem.getPageViewport();
         if (pv != null) {
            String pvKey = pv.getKey();
            PDFGoTo gt = this.getPDFGoToForID(targetID, pvKey);
            PDFOutline parent = parentBookmarkItem != null ? parentBookmarkItem : this.pdfDoc.getOutlineRoot();
            pdfOutline = this.pdfDoc.getFactory().makeOutline(parent, bookmarkItem.getBookmarkTitle(), (PDFAction)gt, bookmarkItem.showChildItems());
         } else {
            log.debug("Bookmark with IDRef \"" + targetID + "\" has a null PageViewport.");
         }

         for(int i = 0; i < bookmarkItem.getCount(); ++i) {
            this.renderBookmarkItem(bookmarkItem.getSubData(i), pdfOutline);
         }

      } else {
         throw new IllegalArgumentException("DestinationData must contain a ID reference");
      }
   }

   public Graphics2DAdapter getGraphics2DAdapter() {
      return new PDFGraphics2DAdapter(this);
   }

   protected void saveGraphicsState() {
      this.generator.saveGraphicsState();
   }

   protected void restoreGraphicsState() {
      this.generator.restoreGraphicsState();
   }

   protected void beginTextObject() {
      this.generator.beginTextObject();
   }

   protected void endTextObject() {
      this.generator.endTextObject();
   }

   public void startPageSequence(PageSequence pageSequence) {
      super.startPageSequence(pageSequence);
      LineArea seqTitle = pageSequence.getTitle();
      if (seqTitle != null) {
         String str = this.convertTitleToString(seqTitle);
         PDFInfo info = this.pdfDoc.getInfo();
         if (info.getTitle() == null) {
            info.setTitle(str);
         }
      }

      Locale language = null;
      if (pageSequence.getLanguage() != null) {
         String lang = pageSequence.getLanguage();
         String country = pageSequence.getCountry();
         if (lang != null) {
            language = country == null ? new Locale(lang) : new Locale(lang, country);
         }

         if (this.pdfDoc.getRoot().getLanguage() == null) {
            this.pdfDoc.getRoot().setLanguage(XMLUtil.toRFC3066(language));
         }
      }

      this.pdfUtil.generateDefaultXMPMetadata();
      if (this.accessEnabled) {
         NodeList nodes = this.getUserAgent().getStructureTree().getPageSequence(this.pageSequenceIndex++);
         this.logicalStructureHandler.processStructureTree(nodes, language);
      }

   }

   public void preparePage(PageViewport page) {
      this.setupPage(page);
      if (this.pages == null) {
         this.pages = new HashMap();
      }

      this.pages.put(page, this.currentPage);
   }

   private void setupPage(PageViewport page) {
      this.pdfResources = this.pdfDoc.getResources();
      Rectangle2D bounds = page.getViewArea();
      double w = bounds.getWidth();
      double h = bounds.getHeight();
      this.currentPage = this.pdfDoc.getFactory().makePage(this.pdfResources, (int)Math.round(w / 1000.0), (int)Math.round(h / 1000.0), page.getPageIndex());
      this.pageReferences.put(page.getKey(), this.currentPage.referencePDF());
      this.pdfUtil.generatePageLabel(page.getPageIndex(), page.getPageNumberString());
   }

   public void renderPage(PageViewport page) throws IOException, FOPException {
      if (this.pages != null && (this.currentPage = (PDFPage)this.pages.get(page)) != null) {
         this.pages.remove(page);
      } else {
         this.setupPage(page);
      }

      this.currentPageRef = this.currentPage.referencePDF();
      if (this.accessEnabled) {
         this.logicalStructureHandler.startPage(this.currentPage);
      }

      Rectangle bounds = page.getViewArea();
      this.pageHeight = bounds.height;
      this.generator = new PDFContentGenerator(this.pdfDoc, this.ostream, this.currentPage);
      this.borderPainter = new PDFBorderPainter(this.generator);
      this.saveGraphicsState();
      AffineTransform basicPageTransform = new AffineTransform(1.0F, 0.0F, 0.0F, -1.0F, 0.0F, (float)this.pageHeight / 1000.0F);
      this.generator.concatenate(basicPageTransform);
      super.renderPage(page);
      this.restoreGraphicsState();
      if (this.accessEnabled) {
         this.logicalStructureHandler.endPage();
      }

      this.pdfDoc.registerObject(this.generator.getStream());
      this.currentPage.setContents(this.generator.getStream());
      PDFAnnotList annots = this.currentPage.getAnnotations();
      if (annots != null) {
         this.pdfDoc.addObject(annots);
      }

      this.pdfDoc.addObject(this.currentPage);
      this.borderPainter = null;
      this.generator.flushPDFDoc();
      this.generator = null;
   }

   protected void startVParea(CTM ctm, Rectangle2D clippingRect) {
      this.saveGraphicsState();
      if (clippingRect != null) {
         this.clipRect((float)clippingRect.getX() / 1000.0F, (float)clippingRect.getY() / 1000.0F, (float)clippingRect.getWidth() / 1000.0F, (float)clippingRect.getHeight() / 1000.0F);
      }

      this.generator.concatenate(new AffineTransform(CTMHelper.toPDFArray(ctm)));
   }

   protected void endVParea() {
      this.restoreGraphicsState();
   }

   protected void concatenateTransformationMatrix(AffineTransform at) {
      this.generator.concatenate(at);
   }

   protected static String format(float value) {
      return PDFNumber.doubleOut((double)value);
   }

   protected void drawBorderLine(float x1, float y1, float x2, float y2, boolean horz, boolean startOrBefore, int style, Color col) {
      PDFBorderPainter.drawBorderLine(this.generator, x1, y1, x2, y2, horz, startOrBefore, style, col);
   }

   protected void clipRect(float x, float y, float width, float height) {
      this.generator.add(format(x) + " " + format(y) + " " + format(width) + " " + format(height) + " re ");
      this.clip();
   }

   protected void clip() {
      this.generator.add("W\nn\n");
   }

   protected void moveTo(float x, float y) {
      this.generator.add(format(x) + " " + format(y) + " m ");
   }

   protected void lineTo(float x, float y) {
      this.generator.add(format(x) + " " + format(y) + " l ");
   }

   protected void closePath() {
      this.generator.add("h ");
   }

   protected void fillRect(float x, float y, float width, float height) {
      if (width > 0.0F && height > 0.0F) {
         this.generator.add(format(x) + " " + format(y) + " " + format(width) + " " + format(height) + " re f\n");
      }

   }

   private void drawLine(float startx, float starty, float endx, float endy) {
      this.generator.add(format(startx) + " " + format(starty) + " m ");
      this.generator.add(format(endx) + " " + format(endy) + " l S\n");
   }

   protected List breakOutOfStateStack() {
      PDFPaintingState paintingState = this.getState();
      List breakOutList = new ArrayList();

      while(true) {
         AbstractPaintingState.AbstractData data = paintingState.getData();
         if (paintingState.restore() == null) {
            return breakOutList;
         }

         if (breakOutList.size() == 0) {
            this.generator.comment("------ break out!");
         }

         breakOutList.add(0, data);
         this.generator.restoreGraphicsState(false);
      }
   }

   protected void restoreStateStackAfterBreakOut(List breakOutList) {
      this.generator.comment("------ restoring context after break-out...");
      Iterator i = breakOutList.iterator();

      while(i.hasNext()) {
         AbstractPaintingState.AbstractData data = (AbstractPaintingState.AbstractData)i.next();
         this.saveGraphicsState();
         AffineTransform at = data.getTransform();
         this.concatenateTransformationMatrix(at);
      }

      this.generator.comment("------ done.");
   }

   protected String getTargetableID(Area area) {
      String id = (String)area.getTrait(Trait.PROD_ID);
      return id != null && id.length() != 0 && this.currentPageViewport.isFirstWithID(id) && !this.idPositions.containsKey(id) ? id : null;
   }

   protected void finishIDGoTo(PDFGoTo gt, Point2D.Float position) {
      gt.setPosition(position);
      this.pdfDoc.addTrailerObject(gt);
      this.unfinishedGoTos.remove(gt);
   }

   protected void finishIDGoTo(PDFGoTo gt, String pdfPageRef, Point2D.Float position) {
      gt.setPageReference(pdfPageRef);
      this.finishIDGoTo(gt, position);
   }

   protected PDFGoTo getPDFGoToForID(String targetID, String pvKey) {
      PDFGoTo gt = (PDFGoTo)this.idGoTos.get(targetID);
      if (gt == null) {
         String pdfPageRef = (String)this.pageReferences.get(pvKey);
         Point2D.Float position = (Point2D.Float)this.idPositions.get(targetID);
         if (pdfPageRef != null && position != null) {
            gt = this.pdfDoc.getFactory().getPDFGoTo(pdfPageRef, position);
         } else {
            gt = new PDFGoTo(pdfPageRef);
            this.pdfDoc.assignObjectNumber(gt);
            this.unfinishedGoTos.add(gt);
         }

         this.idGoTos.put(targetID, gt);
      }

      return gt;
   }

   protected void saveAbsolutePosition(String id, String pdfPageRef, int relativeIPP, int relativeBPP, AffineTransform tf) {
      Point2D.Float position = new Point2D.Float((float)relativeIPP / 1000.0F, (float)relativeBPP / 1000.0F);
      tf.transform(position, position);
      this.idPositions.put(id, position);
      PDFGoTo gt = (PDFGoTo)this.idGoTos.get(id);
      if (gt != null) {
         this.finishIDGoTo(gt, pdfPageRef, position);
      }

   }

   protected void saveAbsolutePosition(String id, int relativeIPP, int relativeBPP) {
      this.saveAbsolutePosition(id, this.currentPageRef, relativeIPP, relativeBPP, this.getState().getTransform());
   }

   protected void saveBlockPosIfTargetable(Block block) {
      String id = this.getTargetableID(block);
      if (id != null) {
         int ipp = block.getXOffset();
         int bpp = block.getYOffset() + block.getSpaceBefore();
         int positioning = block.getPositioning();
         if (positioning != 3 && positioning != 2) {
            ipp += this.currentIPPosition;
            bpp += this.currentBPPosition;
         }

         AffineTransform tf = positioning == 3 ? this.getState().getBaseTransform() : this.getState().getTransform();
         this.saveAbsolutePosition(id, this.currentPageRef, ipp, bpp, tf);
      }

   }

   protected void saveInlinePosIfTargetable(InlineArea inlineArea) {
      String id = this.getTargetableID(inlineArea);
      if (id != null) {
         int extraMarginBefore = 5000;
         int ipp = this.currentIPPosition;
         int bpp = this.currentBPPosition + inlineArea.getOffset() - extraMarginBefore;
         this.saveAbsolutePosition(id, ipp, bpp);
      }

   }

   protected void renderBlock(Block block) {
      this.saveBlockPosIfTargetable(block);
      super.renderBlock(block);
   }

   protected void renderLineArea(LineArea line) {
      super.renderLineArea(line);
   }

   protected void renderInlineArea(InlineArea inlineArea) {
      this.saveInlinePosIfTargetable(inlineArea);
      super.renderInlineArea(inlineArea);
   }

   public void renderInlineParent(InlineParent ip) {
      boolean annotsAllowed = this.pdfDoc.getProfile().isAnnotationAllowed();
      Rectangle2D ipRect = null;
      PDFFactory factory = null;
      PDFAction action = null;
      if (annotsAllowed) {
         int ipp = this.currentIPPosition;
         int bpp = this.currentBPPosition + ip.getOffset();
         Rectangle2D ipRect = new Rectangle2D.Float((float)ipp / 1000.0F, (float)bpp / 1000.0F, (float)ip.getIPD() / 1000.0F, (float)ip.getBPD() / 1000.0F);
         AffineTransform transform = this.getState().getTransform();
         ipRect = transform.createTransformedShape(ipRect).getBounds2D();
         factory = this.pdfDoc.getFactory();
      }

      super.renderInlineParent(ip);
      boolean linkTraitFound = false;
      Trait.InternalLink intLink = (Trait.InternalLink)ip.getTrait(Trait.INTERNAL_LINK);
      String ptr;
      if (intLink != null) {
         linkTraitFound = true;
         String pvKey = intLink.getPVKey();
         ptr = intLink.getIDRef();
         boolean pvKeyOK = pvKey != null && pvKey.length() > 0;
         boolean idRefOK = ptr != null && ptr.length() > 0;
         if (pvKeyOK && idRefOK && annotsAllowed) {
            action = this.getPDFGoToForID(ptr, pvKey);
         }
      }

      if (!linkTraitFound) {
         Trait.ExternalLink extLink = (Trait.ExternalLink)ip.getTrait(Trait.EXTERNAL_LINK);
         if (extLink != null) {
            ptr = extLink.getDestination();
            if (ptr != null && ptr.length() > 0) {
               linkTraitFound = true;
               if (annotsAllowed) {
                  action = factory.getExternalAction(ptr, extLink.newWindow());
               }
            }
         }
      }

      if (linkTraitFound) {
         if (!annotsAllowed) {
            log.warn("Skipping annotation for a link due to PDF profile: " + this.pdfDoc.getProfile());
         } else if (action != null) {
            PDFLink pdfLink = factory.makeLink(ipRect, (PDFAction)action);
            if (this.accessEnabled) {
               ptr = (String)ip.getTrait(Trait.PTR);
               this.logicalStructureHandler.addLinkContentItem(pdfLink, ptr);
            }

            this.currentPage.addAnnotation(pdfLink);
         }
      }

   }

   public void renderViewport(Viewport viewport) {
      this.imageReference = (String)viewport.getTrait(Trait.PTR);
      super.renderViewport(viewport);
      this.imageReference = null;
   }

   private Typeface getTypeface(String fontName) {
      Typeface tf = (Typeface)this.fontInfo.getFonts().get(fontName);
      if (tf instanceof LazyFont) {
         tf = ((LazyFont)tf).getRealFont();
      }

      return tf;
   }

   public void renderText(TextArea text) {
      this.renderInlineAreaBackAndBorders(text);
      Color ct = (Color)text.getTrait(Trait.COLOR);
      this.updateColor(ct, true);
      String fontName;
      if (this.accessEnabled) {
         fontName = (String)text.getTrait(Trait.PTR);
         PDFLogicalStructureHandler.MarkedContentInfo mci = this.logicalStructureHandler.addTextContentItem(fontName);
         if (this.generator.getTextUtil().isInTextObject()) {
            this.generator.separateTextElements(mci.tag, mci.mcid);
         }

         this.generator.beginTextObject(mci.tag, mci.mcid);
      } else {
         this.beginTextObject();
      }

      fontName = this.getInternalFontNameForArea(text);
      int size = (Integer)text.getTrait(Trait.FONT_SIZE);
      Typeface tf = this.getTypeface(fontName);
      PDFTextUtil textutil = this.generator.getTextUtil();
      textutil.updateTf(fontName, (double)((float)size / 1000.0F), tf.isMultiByte());
      int rx = this.currentIPPosition + text.getBorderAndPaddingWidthStart();
      int bl = this.currentBPPosition + text.getOffset() + text.getBaselineOffset();
      textutil.writeTextMatrix(new AffineTransform(1.0F, 0.0F, 0.0F, -1.0F, (float)rx / 1000.0F, (float)bl / 1000.0F));
      super.renderText(text);
      textutil.writeTJ();
      this.renderTextDecoration(tf, size, text, bl, rx);
   }

   public void renderWord(WordArea word) {
      Font font = this.getFontFromArea(word.getParentArea());
      String s = word.getWord();
      this.escapeText(s, word.getLetterAdjustArray(), font, (AbstractTextArea)word.getParentArea());
      super.renderWord(word);
   }

   public void renderSpace(SpaceArea space) {
      Font font = this.getFontFromArea(space.getParentArea());
      String s = space.getSpace();
      AbstractTextArea textArea = (AbstractTextArea)space.getParentArea();
      this.escapeText(s, (int[])null, font, textArea);
      if (space.isAdjustable()) {
         int tws = -((TextArea)space.getParentArea()).getTextWordSpaceAdjust() - 2 * textArea.getTextLetterSpaceAdjust();
         if (tws != 0) {
            float adjust = (float)tws / ((float)font.getFontSize() / 1000.0F);
            this.generator.getTextUtil().adjustGlyphTJ((double)adjust);
         }
      }

      super.renderSpace(space);
   }

   protected void escapeText(String s, int[] letterAdjust, Font font, AbstractTextArea parentArea) {
      this.escapeText(s, 0, s.length(), letterAdjust, font, parentArea);
   }

   protected void escapeText(String s, int start, int end, int[] letterAdjust, Font font, AbstractTextArea parentArea) {
      String fontName = font.getFontName();
      float fontSize = (float)font.getFontSize() / 1000.0F;
      Typeface tf = this.getTypeface(fontName);
      SingleByteFont singleByteFont = null;
      if (tf instanceof SingleByteFont) {
         singleByteFont = (SingleByteFont)tf;
      }

      PDFTextUtil textutil = this.generator.getTextUtil();
      int l = s.length();

      for(int i = start; i < end; ++i) {
         char orgChar = s.charAt(i);
         float glyphAdjust = 0.0F;
         char ch;
         if (font.hasChar(orgChar)) {
            ch = font.mapChar(orgChar);
            int encoding;
            if (singleByteFont != null && singleByteFont.hasAdditionalEncodings()) {
               encoding = ch / 256;
               if (encoding == 0) {
                  textutil.updateTf(fontName, (double)fontSize, tf.isMultiByte());
               } else {
                  textutil.updateTf(fontName + "_" + Integer.toString(encoding), (double)fontSize, tf.isMultiByte());
                  ch = (char)(ch % 256);
               }
            }

            encoding = i < l - 1 ? parentArea.getTextLetterSpaceAdjust() : 0;
            glyphAdjust -= (float)encoding;
         } else if (CharUtilities.isFixedWidthSpace(orgChar)) {
            ch = font.mapChar(' ');
            glyphAdjust = (float)(font.getCharWidth(ch) - font.getCharWidth(orgChar));
         } else {
            ch = font.mapChar(orgChar);
         }

         if (letterAdjust != null && i < l - 1) {
            glyphAdjust -= (float)letterAdjust[i + 1];
         }

         textutil.writeTJMappedChar(ch);
         float adjust = glyphAdjust / fontSize;
         if (adjust != 0.0F) {
            textutil.adjustGlyphTJ((double)adjust);
         }
      }

   }

   protected void updateColor(Color col, boolean fill) {
      this.generator.updateColor(col, fill, (StringBuffer)null);
   }

   public void renderImage(Image image, Rectangle2D pos) {
      this.endTextObject();
      String url = image.getURL();
      this.putImage(url, pos, image.getForeignAttributes());
   }

   protected void drawImage(String url, Rectangle2D pos, Map foreignAttributes) {
      this.endTextObject();
      this.putImage(url, pos, foreignAttributes);
   }

   /** @deprecated */
   protected void putImage(String uri, Rectangle2D pos) {
      this.putImage(uri, pos, (Map)null);
   }

   protected void putImage(String uri, Rectangle2D pos, Map foreignAttributes) {
      Rectangle posInt = new Rectangle((int)pos.getX(), (int)pos.getY(), (int)pos.getWidth(), (int)pos.getHeight());
      uri = URISpecification.getURL(uri);
      PDFXObject xobject = this.pdfDoc.getXObject(uri);
      if (xobject != null) {
         float w = (float)pos.getWidth() / 1000.0F;
         float h = (float)pos.getHeight() / 1000.0F;
         this.placeImage((float)pos.getX() / 1000.0F, (float)pos.getY() / 1000.0F, w, h, xobject);
      } else {
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
            ImageFlavor[] supportedFlavors = this.imageHandlerRegistry.getSupportedFlavors();
            org.apache.xmlgraphics.image.loader.Image img = manager.getImage(info, supportedFlavors, hints, sessionContext);
            PDFImageHandler handler = (PDFImageHandler)this.imageHandlerRegistry.getHandler(img.getClass());
            if (handler == null) {
               throw new UnsupportedOperationException("No PDFImageHandler available for image: " + info + " (" + img.getClass().getName() + ")");
            }

            if (log.isDebugEnabled()) {
               log.debug("Using PDFImageHandler: " + handler.getClass().getName());
            }

            try {
               RendererContext context = this.createRendererContext(x, y, posInt.width, posInt.height, foreignAttributes);
               handler.generateImage(context, img, origin, posInt);
            } catch (IOException var19) {
               ResourceEventProducer eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
               eventProducer.imageWritingError(this, var19);
               return;
            }
         } catch (ImageException var20) {
            eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.imageError(this, info != null ? info.toString() : uri, var20, (Locator)null);
         } catch (FileNotFoundException var21) {
            eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.imageNotFound(this, info != null ? info.toString() : uri, var21, (Locator)null);
         } catch (IOException var22) {
            eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.imageIOError(this, info != null ? info.toString() : uri, var22, (Locator)null);
         }

         try {
            this.generator.flushPDFDoc();
         } catch (IOException var18) {
            log.error(var18.getMessage());
         }

      }
   }

   public void placeImage(float x, float y, float w, float h, PDFXObject xobj) {
      if (this.accessEnabled) {
         PDFLogicalStructureHandler.MarkedContentInfo mci = this.logicalStructureHandler.addImageContentItem(this.imageReference);
         this.generator.saveGraphicsState(mci.tag, mci.mcid);
      } else {
         this.saveGraphicsState();
      }

      this.generator.add(format(w) + " 0 0 " + format(-h) + " " + format((float)this.currentIPPosition / 1000.0F + x) + " " + format((float)this.currentBPPosition / 1000.0F + h + y) + " cm\n" + xobj.getName() + " Do\n");
      if (this.accessEnabled) {
         this.generator.restoreGraphicsStateAccess();
      } else {
         this.restoreGraphicsState();
      }

   }

   protected RendererContext createRendererContext(int x, int y, int width, int height, Map foreignAttributes) {
      RendererContext context = super.createRendererContext(x, y, width, height, foreignAttributes);
      context.setProperty("pdfDoc", this.pdfDoc);
      context.setProperty("outputStream", this.ostream);
      context.setProperty("pdfPage", this.currentPage);
      context.setProperty("pdfContext", this.currentContext);
      context.setProperty("pdfStream", this.generator.getStream());
      context.setProperty("fontInfo", this.fontInfo);
      context.setProperty("fontName", "");
      context.setProperty("fontSize", new Integer(0));
      return context;
   }

   public void renderDocument(Document doc, String ns, Rectangle2D pos, Map foreignAttributes) {
      if (this.accessEnabled) {
         PDFLogicalStructureHandler.MarkedContentInfo mci = this.logicalStructureHandler.addImageContentItem(this.imageReference);
         this.generator.beginMarkedContentSequence(mci.tag, mci.mcid);
      }

      super.renderDocument(doc, ns, pos, foreignAttributes);
      if (this.accessEnabled) {
         this.generator.endMarkedContentSequence();
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
      this.endTextObject();
      this.borderPainter.drawLine(new Point(startx, starty), new Point(endx, starty), ruleThickness, col, RuleStyle.valueOf(style));
      super.renderLeader(area);
   }

   public String getMimeType() {
      return "application/pdf";
   }

   public void setAMode(PDFAMode mode) {
      this.pdfUtil.setAMode(mode);
   }

   public void setXMode(PDFXMode mode) {
      this.pdfUtil.setXMode(mode);
   }

   public void setOutputProfileURI(String outputProfileURI) {
      this.pdfUtil.setOutputProfileURI(outputProfileURI);
   }

   public void setFilterMap(Map filterMap) {
      this.pdfUtil.setFilterMap(filterMap);
   }

   public void setEncryptionParams(PDFEncryptionParams encryptionParams) {
      this.pdfUtil.setEncryptionParams(encryptionParams);
   }

   PDFLogicalStructureHandler.MarkedContentInfo addCurrentImageToStructureTree() {
      return this.logicalStructureHandler.addImageContentItem(this.imageReference);
   }
}
