package org.apache.fop.render.afp;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.afp.AFPBorderPainter;
import org.apache.fop.afp.AFPDataObjectInfo;
import org.apache.fop.afp.AFPDitheredRectanglePainter;
import org.apache.fop.afp.AFPEventProducer;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPRectanglePainter;
import org.apache.fop.afp.AFPResourceLevelDefaults;
import org.apache.fop.afp.AFPResourceManager;
import org.apache.fop.afp.AFPTextDataInfo;
import org.apache.fop.afp.AFPUnitConverter;
import org.apache.fop.afp.AbstractAFPPainter;
import org.apache.fop.afp.BorderPaintingInfo;
import org.apache.fop.afp.DataStream;
import org.apache.fop.afp.RectanglePaintingInfo;
import org.apache.fop.afp.fonts.AFPFont;
import org.apache.fop.afp.fonts.AFPFontAttributes;
import org.apache.fop.afp.fonts.AFPFontCollection;
import org.apache.fop.afp.fonts.AFPPageFonts;
import org.apache.fop.afp.fonts.CharacterSet;
import org.apache.fop.afp.modca.PageObject;
import org.apache.fop.afp.util.DefaultFOPResourceAccessor;
import org.apache.fop.afp.util.ResourceAccessor;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.CTM;
import org.apache.fop.area.OffDocumentExtensionAttachment;
import org.apache.fop.area.OffDocumentItem;
import org.apache.fop.area.PageSequence;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.Leader;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.datatypes.URISpecification;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.render.AbstractPathOrientedRenderer;
import org.apache.fop.render.Graphics2DAdapter;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.afp.extensions.AFPExtensionAttachment;
import org.apache.fop.render.afp.extensions.AFPIncludeFormMap;
import org.apache.fop.render.afp.extensions.AFPInvokeMediumMap;
import org.apache.fop.render.afp.extensions.AFPPageOverlay;
import org.apache.fop.render.afp.extensions.AFPPageSetup;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.apache.xmlgraphics.ps.ImageEncodingHelper;
import org.xml.sax.Locator;

public class AFPRenderer extends AbstractPathOrientedRenderer implements AFPCustomizable {
   private static final int X = 0;
   private static final int Y = 1;
   private AFPResourceManager resourceManager;
   private final AFPPaintingState paintingState;
   private final AFPUnitConverter unitConv;
   private AFPBorderPainter borderPainter;
   private final Map pageSegmentMap = new HashMap();
   private final Map pages = new HashMap();
   private DataStream dataStream;
   private final AFPImageHandlerRegistry imageHandlerRegistry;
   private AbstractAFPPainter rectanglePainter;
   private AFPShadingMode shadingMode;
   private String lastMediumMap;
   private static final ImageFlavor[] NATIVE_FLAVORS;
   private static final ImageFlavor[] FLAVORS;

   public AFPRenderer() {
      this.shadingMode = AFPShadingMode.COLOR;
      this.imageHandlerRegistry = new AFPImageHandlerRegistry();
      this.resourceManager = new AFPResourceManager();
      this.paintingState = new AFPPaintingState();
      this.unitConv = this.paintingState.getUnitConverter();
   }

   public void setupFontInfo(FontInfo inFontInfo) {
      this.fontInfo = inFontInfo;
      FontManager fontManager = this.userAgent.getFactory().getFontManager();
      FontCollection[] fontCollections = new FontCollection[]{new AFPFontCollection(this.userAgent.getEventBroadcaster(), this.getFontList())};
      fontManager.setup(this.getFontInfo(), fontCollections);
   }

   public void setUserAgent(FOUserAgent agent) {
      super.setUserAgent(agent);
   }

   public void startRenderer(OutputStream outputStream) throws IOException {
      this.paintingState.setColor(Color.WHITE);
      this.dataStream = this.resourceManager.createDataStream(this.paintingState, outputStream);
      this.borderPainter = new AFPBorderPainter(this.paintingState, this.dataStream);
      this.rectanglePainter = this.createRectanglePainter();
      this.dataStream.startDocument();
   }

   AbstractAFPPainter createRectanglePainter() {
      return (AbstractAFPPainter)(AFPShadingMode.DITHERED.equals(this.shadingMode) ? new AFPDitheredRectanglePainter(this.paintingState, this.dataStream, this.resourceManager) : new AFPRectanglePainter(this.paintingState, this.dataStream));
   }

   public void stopRenderer() throws IOException {
      this.dataStream.endDocument();
      this.resourceManager.writeToStream();
      this.resourceManager = null;
   }

   public void startPageSequence(PageSequence pageSequence) {
      super.startPageSequence(pageSequence);

      try {
         this.dataStream.startPageGroup();
      } catch (IOException var7) {
         log.error(var7.getMessage());
      }

      if (pageSequence.hasExtensionAttachments()) {
         Iterator iter = pageSequence.getExtensionAttachments().iterator();

         while(iter.hasNext()) {
            ExtensionAttachment attachment = (ExtensionAttachment)iter.next();
            String mediumMap;
            if (attachment instanceof AFPInvokeMediumMap) {
               AFPInvokeMediumMap imm = (AFPInvokeMediumMap)attachment;
               mediumMap = imm.getName();
               if (mediumMap != null) {
                  this.dataStream.createInvokeMediumMap(mediumMap);
               }
            } else if (attachment instanceof AFPPageSetup) {
               AFPPageSetup aps = (AFPPageSetup)attachment;
               mediumMap = aps.getName();
               String value = aps.getValue();
               this.dataStream.createTagLogicalElement(mediumMap, value);
            }
         }
      }

   }

   public boolean supportsOutOfOrder() {
      return false;
   }

   public void preparePage(PageViewport page) {
      int pageRotation = this.paintingState.getPageRotation();
      int pageWidth = this.paintingState.getPageWidth();
      int pageHeight = this.paintingState.getPageHeight();
      int resolution = this.paintingState.getResolution();
      this.dataStream.startPage(pageWidth, pageHeight, pageRotation, resolution, resolution);
      this.renderPageObjectExtensions(page);
      PageObject currentPage = this.dataStream.savePage();
      this.pages.put(page, currentPage);
   }

   public void processOffDocumentItem(OffDocumentItem odi) {
      if (odi instanceof OffDocumentExtensionAttachment) {
         ExtensionAttachment attachment = ((OffDocumentExtensionAttachment)odi).getAttachment();
         if (attachment != null && "apache:fop:extensions:afp".equals(attachment.getCategory()) && attachment instanceof AFPIncludeFormMap) {
            this.handleIncludeFormMap((AFPIncludeFormMap)attachment);
         }
      }

   }

   private void handleIncludeFormMap(AFPIncludeFormMap formMap) {
      ResourceAccessor accessor = new DefaultFOPResourceAccessor(this.getUserAgent(), (String)null, (URI)null);

      try {
         this.resourceManager.createIncludedResource(formMap.getName(), formMap.getSrc(), accessor, (byte)-2);
      } catch (IOException var5) {
         AFPEventProducer eventProducer = AFPEventProducer.Provider.get(this.userAgent.getEventBroadcaster());
         eventProducer.resourceEmbeddingError(this, formMap.getName(), var5);
      }

   }

   public Graphics2DAdapter getGraphics2DAdapter() {
      return new AFPGraphics2DAdapter(this.paintingState);
   }

   public void startVParea(CTM ctm, Rectangle2D clippingRect) {
      this.saveGraphicsState();
      if (ctm != null) {
         AffineTransform at = ctm.toAffineTransform();
         this.concatenateTransformationMatrix(at);
      }

      if (clippingRect != null) {
         this.clipRect((float)clippingRect.getX() / 1000.0F, (float)clippingRect.getY() / 1000.0F, (float)clippingRect.getWidth() / 1000.0F, (float)clippingRect.getHeight() / 1000.0F);
      }

   }

   public void endVParea() {
      this.restoreGraphicsState();
   }

   protected void concatenateTransformationMatrix(AffineTransform at) {
      if (!at.isIdentity()) {
         this.paintingState.concatenate(at);
      }

   }

   private AffineTransform getBaseTransform() {
      AffineTransform baseTransform = new AffineTransform();
      double scale = (double)this.unitConv.mpt2units(1.0F);
      baseTransform.scale(scale, scale);
      return baseTransform;
   }

   public void renderPage(PageViewport pageViewport) throws IOException, FOPException {
      this.paintingState.clear();
      Rectangle2D bounds = pageViewport.getViewArea();
      AffineTransform baseTransform = this.getBaseTransform();
      this.paintingState.concatenate(baseTransform);
      if (this.pages.containsKey(pageViewport)) {
         this.dataStream.restorePage((PageObject)this.pages.remove(pageViewport));
      } else {
         int pageWidth = Math.round(this.unitConv.mpt2units((float)bounds.getWidth()));
         this.paintingState.setPageWidth(pageWidth);
         int pageHeight = Math.round(this.unitConv.mpt2units((float)bounds.getHeight()));
         this.paintingState.setPageHeight(pageHeight);
         int pageRotation = this.paintingState.getPageRotation();
         int resolution = this.paintingState.getResolution();
         this.renderInvokeMediumMap(pageViewport);
         this.dataStream.startPage(pageWidth, pageHeight, pageRotation, resolution, resolution);
         this.renderPageObjectExtensions(pageViewport);
      }

      super.renderPage(pageViewport);
      AFPPageFonts pageFonts = this.paintingState.getPageFonts();
      if (pageFonts != null && !pageFonts.isEmpty()) {
         this.dataStream.addFontsToCurrentPage(pageFonts);
      }

      this.dataStream.endPage();
   }

   public void drawBorderLine(float x1, float y1, float x2, float y2, boolean horz, boolean startOrBefore, int style, Color col) {
      BorderPaintingInfo borderPaintInfo = new BorderPaintingInfo(x1, y1, x2, y2, horz, style, col);
      this.borderPainter.paint(borderPaintInfo);
   }

   public void fillRect(float x, float y, float width, float height) {
      RectanglePaintingInfo rectanglePaintInfo = new RectanglePaintingInfo(x, y, width, height);

      try {
         this.rectanglePainter.paint(rectanglePaintInfo);
      } catch (IOException var7) {
         throw new RuntimeException("I/O error while painting a filled rectangle", var7);
      }
   }

   protected RendererContext instantiateRendererContext() {
      return new AFPRendererContext(this, this.getMimeType());
   }

   protected RendererContext createRendererContext(int x, int y, int width, int height, Map foreignAttributes) {
      RendererContext context = super.createRendererContext(x, y, width, height, foreignAttributes);
      context.setProperty("afpFontInfo", this.fontInfo);
      context.setProperty("afpResourceManager", this.resourceManager);
      context.setProperty("afpPaintingState", this.paintingState);
      return context;
   }

   public void drawImage(String uri, Rectangle2D pos, Map foreignAttributes) {
      uri = URISpecification.getURL(uri);
      this.paintingState.setImageUri(uri);
      Point origin = new Point(this.currentIPPosition, this.currentBPPosition);
      Rectangle posInt = new Rectangle((int)Math.round(pos.getX()), (int)Math.round(pos.getY()), (int)Math.round(pos.getWidth()), (int)Math.round(pos.getHeight()));
      int x = origin.x + posInt.x;
      int y = origin.y + posInt.y;
      String name = (String)this.pageSegmentMap.get(uri);
      if (name != null) {
         float[] srcPts = new float[]{(float)x, (float)y, (float)posInt.width, (float)posInt.height};
         int[] coords = this.unitConv.mpts2units(srcPts);
         int width = Math.round(this.unitConv.mpt2units((float)posInt.width));
         int height = Math.round(this.unitConv.mpt2units((float)posInt.height));
         this.dataStream.createIncludePageSegment(name, coords[0], coords[1], width, height);
      } else {
         ImageManager manager = this.userAgent.getFactory().getImageManager();
         ImageInfo info = null;

         ResourceEventProducer eventProducer;
         try {
            ImageSessionContext sessionContext = this.userAgent.getImageSessionContext();
            info = manager.getImageInfo(uri, sessionContext);
            Map hints = ImageUtil.getDefaultHints(sessionContext);
            boolean nativeImagesSupported = this.paintingState.isNativeImagesSupported();
            ImageFlavor[] flavors = nativeImagesSupported ? NATIVE_FLAVORS : FLAVORS;
            Image img = manager.getImage(info, flavors, hints, sessionContext);
            AFPImageHandler imageHandler = (AFPImageHandler)this.imageHandlerRegistry.getHandler(img);
            if (imageHandler == null) {
               throw new UnsupportedOperationException("No AFPImageHandler available for image: " + info + " (" + img.getClass().getName() + ")");
            }

            RendererContext rendererContext = this.createRendererContext(x, y, posInt.width, posInt.height, foreignAttributes);
            AFPRendererImageInfo rendererImageInfo = new AFPRendererImageInfo(uri, pos, origin, info, img, rendererContext, foreignAttributes);
            AFPDataObjectInfo dataObjectInfo = null;

            try {
               dataObjectInfo = imageHandler.generateDataObjectInfo(rendererImageInfo);
               if (dataObjectInfo != null) {
                  this.resourceManager.createObject(dataObjectInfo);
               }
            } catch (IOException var22) {
               ResourceEventProducer eventProducer = ResourceEventProducer.Provider.get(this.userAgent.getEventBroadcaster());
               eventProducer.imageWritingError(this, var22);
               throw var22;
            }
         } catch (ImageException var23) {
            eventProducer = ResourceEventProducer.Provider.get(this.userAgent.getEventBroadcaster());
            eventProducer.imageError(this, info != null ? info.toString() : uri, var23, (Locator)null);
         } catch (FileNotFoundException var24) {
            eventProducer = ResourceEventProducer.Provider.get(this.userAgent.getEventBroadcaster());
            eventProducer.imageNotFound(this, info != null ? info.toString() : uri, var24, (Locator)null);
         } catch (IOException var25) {
            eventProducer = ResourceEventProducer.Provider.get(this.userAgent.getEventBroadcaster());
            eventProducer.imageIOError(this, info != null ? info.toString() : uri, var25, (Locator)null);
         }
      }

   }

   /** @deprecated */
   public static void writeImage(RenderedImage image, OutputStream out) throws IOException {
      ImageEncodingHelper.encodeRenderedImageAsRGB(image, out);
   }

   public void updateColor(Color col, boolean fill) {
      if (fill) {
         this.paintingState.setColor(col);
      }

   }

   public void restoreStateStackAfterBreakOut(List breakOutList) {
      log.debug("Block.FIXED --> restoring context after break-out");
      this.paintingState.saveAll(breakOutList);
   }

   protected List breakOutOfStateStack() {
      log.debug("Block.FIXED --> break out");
      return this.paintingState.restoreAll();
   }

   public void saveGraphicsState() {
      this.paintingState.save();
   }

   public void restoreGraphicsState() {
      this.paintingState.restore();
   }

   public void renderImage(org.apache.fop.area.inline.Image image, Rectangle2D pos) {
      this.drawImage(image.getURL(), pos, image.getForeignAttributes());
   }

   public void renderText(TextArea text) {
      this.renderInlineAreaBackAndBorders(text);
      int fontSize = (Integer)text.getTrait(Trait.FONT_SIZE);
      this.paintingState.setFontSize(fontSize);
      String internalFontName = this.getInternalFontNameForArea(text);
      Map fontMetricMap = this.fontInfo.getFonts();
      AFPFont font = (AFPFont)fontMetricMap.get(internalFontName);
      AFPPageFonts pageFonts = this.paintingState.getPageFonts();
      AFPFontAttributes fontAttributes = pageFonts.registerFont(internalFontName, font, fontSize);
      Font fnt = this.getFontFromArea(text);
      if (font.isEmbeddable()) {
         CharacterSet charSet = font.getCharacterSet(fontSize);

         try {
            this.resourceManager.embedFont(font, charSet);
         } catch (IOException var26) {
            AFPEventProducer eventProducer = AFPEventProducer.Provider.get(this.userAgent.getEventBroadcaster());
            eventProducer.resourceEmbeddingError(this, charSet.getName(), var26);
         }
      }

      AFPTextDataInfo textDataInfo = new AFPTextDataInfo();
      int fontReference = fontAttributes.getFontReference();
      textDataInfo.setFontReference(fontReference);
      int x = this.currentIPPosition + text.getBorderAndPaddingWidthStart();
      int y = this.currentBPPosition + text.getOffset() + text.getBaselineOffset();
      int[] coords = this.unitConv.mpts2units(new float[]{(float)x, (float)y});
      textDataInfo.setX(coords[0]);
      textDataInfo.setY(coords[1]);
      Color color = (Color)text.getTrait(Trait.COLOR);
      textDataInfo.setColor(color);
      int textWordSpaceAdjust = text.getTextWordSpaceAdjust();
      int textLetterSpaceAdjust = text.getTextLetterSpaceAdjust();
      int textWidth = font.getWidth(32, fontSize) / 1000;
      int textWidth = 0;
      int variableSpaceCharacterIncrement = textWidth + textWordSpaceAdjust + textLetterSpaceAdjust;
      variableSpaceCharacterIncrement = Math.round(this.unitConv.mpt2units((float)variableSpaceCharacterIncrement));
      textDataInfo.setVariableSpaceCharacterIncrement(variableSpaceCharacterIncrement);
      int interCharacterAdjustment = Math.round(this.unitConv.mpt2units((float)textLetterSpaceAdjust));
      textDataInfo.setInterCharacterAdjustment(interCharacterAdjustment);
      CharacterSet charSet = font.getCharacterSet(fontSize);
      String encoding = charSet.getEncoding();
      textDataInfo.setEncoding(encoding);
      String textString = text.getText();
      textDataInfo.setString(textString);

      try {
         this.dataStream.createText(textDataInfo, textLetterSpaceAdjust, textWordSpaceAdjust, fnt, charSet);
      } catch (UnsupportedEncodingException var25) {
         AFPEventProducer eventProducer = AFPEventProducer.Provider.get(this.userAgent.getEventBroadcaster());
         eventProducer.characterSetEncodingError(this, charSet.getName(), encoding);
      }

      super.renderText(text);
      this.renderTextDecoration(font, fontSize, text, y, x);
   }

   public void renderLeader(Leader area) {
      this.renderInlineAreaBackAndBorders(area);
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
            this.drawBorderLine(startx, starty, endx, starty + ruleThickness, true, true, style, col);
            super.renderLeader(area);
            return;
         default:
            throw new UnsupportedOperationException("rule style not supported");
      }
   }

   public String getMimeType() {
      return "application/x-afp";
   }

   private void renderInvokeMediumMap(PageViewport pageViewport) {
      if (pageViewport.getExtensionAttachments() != null && pageViewport.getExtensionAttachments().size() > 0) {
         Iterator it = pageViewport.getExtensionAttachments().iterator();

         while(it.hasNext()) {
            ExtensionAttachment attachment = (ExtensionAttachment)it.next();
            if ("apache:fop:extensions:afp".equals(attachment.getCategory())) {
               AFPExtensionAttachment aea = (AFPExtensionAttachment)attachment;
               if ("invoke-medium-map".equals(aea.getElementName())) {
                  AFPInvokeMediumMap imm = (AFPInvokeMediumMap)attachment;
                  String mediumMap = imm.getName();
                  if (mediumMap != null && !mediumMap.equals(this.lastMediumMap)) {
                     this.dataStream.createInvokeMediumMap(mediumMap);
                     this.lastMediumMap = mediumMap;
                  }
               }
            }
         }
      }

   }

   private void renderPageObjectExtensions(PageViewport pageViewport) {
      this.pageSegmentMap.clear();
      if (pageViewport.getExtensionAttachments() != null && pageViewport.getExtensionAttachments().size() > 0) {
         Iterator it = pageViewport.getExtensionAttachments().iterator();

         while(it.hasNext()) {
            ExtensionAttachment attachment = (ExtensionAttachment)it.next();
            if ("apache:fop:extensions:afp".equals(attachment.getCategory())) {
               String element;
               String content;
               if (attachment instanceof AFPPageSetup) {
                  AFPPageSetup aps = (AFPPageSetup)attachment;
                  element = aps.getElementName();
                  String value;
                  if ("include-page-segment".equals(element)) {
                     content = aps.getName();
                     value = aps.getValue();
                     this.pageSegmentMap.put(value, content);
                  } else if ("tag-logical-element".equals(element)) {
                     content = aps.getName();
                     value = aps.getValue();
                     this.dataStream.createTagLogicalElement(content, value);
                  } else if ("no-operation".equals(element)) {
                     content = aps.getContent();
                     if (content != null) {
                        this.dataStream.createNoOperation(content);
                     }
                  }
               } else if (attachment instanceof AFPPageOverlay) {
                  AFPPageOverlay ipo = (AFPPageOverlay)attachment;
                  element = ipo.getElementName();
                  if ("include-page-overlay".equals(element)) {
                     content = ipo.getName();
                     if (content != null) {
                        this.dataStream.createIncludePageOverlay(content, ipo.getX(), ipo.getY());
                     }
                  }
               }
            }
         }
      }

   }

   public void setPortraitRotation(int rotation) {
      this.paintingState.setPortraitRotation(rotation);
   }

   public void setLandscapeRotation(int rotation) {
      this.paintingState.setLandscapeRotation(rotation);
   }

   public void setBitsPerPixel(int bitsPerPixel) {
      this.paintingState.setBitsPerPixel(bitsPerPixel);
   }

   public void setColorImages(boolean colorImages) {
      this.paintingState.setColorImages(colorImages);
   }

   public void setNativeImagesSupported(boolean nativeImages) {
      this.paintingState.setNativeImagesSupported(nativeImages);
   }

   public void setCMYKImagesSupported(boolean value) {
      this.paintingState.setCMYKImagesSupported(value);
   }

   public void setDitheringQuality(float quality) {
      this.paintingState.setDitheringQuality(quality);
   }

   public void setShadingMode(AFPShadingMode shadingMode) {
      this.shadingMode = shadingMode;
   }

   public void setResolution(int resolution) {
      this.paintingState.setResolution(resolution);
   }

   public int getResolution() {
      return this.paintingState.getResolution();
   }

   public void setDefaultResourceGroupFilePath(String filePath) {
      this.resourceManager.setDefaultResourceGroupFilePath(filePath);
   }

   public void setResourceLevelDefaults(AFPResourceLevelDefaults defaults) {
      this.resourceManager.setResourceLevelDefaults(defaults);
   }

   protected void establishTransformationMatrix(AffineTransform at) {
      this.saveGraphicsState();
      this.concatenateTransformationMatrix(at);
   }

   public void clip() {
   }

   public void clipRect(float x, float y, float width, float height) {
   }

   public void moveTo(float x, float y) {
   }

   public void lineTo(float x, float y) {
   }

   public void closePath() {
   }

   public void beginTextObject() {
   }

   public void endTextObject() {
   }

   static {
      NATIVE_FLAVORS = new ImageFlavor[]{ImageFlavor.XML_DOM, ImageFlavor.RAW_JPEG, ImageFlavor.RAW_CCITTFAX, ImageFlavor.RAW_EPS, ImageFlavor.RAW_TIFF, ImageFlavor.GRAPHICS2D, ImageFlavor.BUFFERED_IMAGE, ImageFlavor.RENDERED_IMAGE};
      FLAVORS = new ImageFlavor[]{ImageFlavor.XML_DOM, ImageFlavor.GRAPHICS2D, ImageFlavor.BUFFERED_IMAGE, ImageFlavor.RENDERED_IMAGE};
   }
}
