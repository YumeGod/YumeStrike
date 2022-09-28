package org.apache.fop.render.ps;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.transform.Source;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.Area;
import org.apache.fop.area.BlockViewport;
import org.apache.fop.area.CTM;
import org.apache.fop.area.OffDocumentExtensionAttachment;
import org.apache.fop.area.OffDocumentItem;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.RegionViewport;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.AbstractTextArea;
import org.apache.fop.area.inline.InlineParent;
import org.apache.fop.area.inline.Leader;
import org.apache.fop.area.inline.SpaceArea;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.area.inline.WordArea;
import org.apache.fop.datatypes.URISpecification;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.LazyFont;
import org.apache.fop.fonts.SingleByteFont;
import org.apache.fop.fonts.Typeface;
import org.apache.fop.render.AbstractPathOrientedRenderer;
import org.apache.fop.render.Graphics2DAdapter;
import org.apache.fop.render.ImageAdapter;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.ImageHandlerRegistry;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.RendererEventProducer;
import org.apache.fop.render.ps.extensions.PSCommentAfter;
import org.apache.fop.render.ps.extensions.PSCommentBefore;
import org.apache.fop.render.ps.extensions.PSExtensionAttachment;
import org.apache.fop.render.ps.extensions.PSSetPageDevice;
import org.apache.fop.render.ps.extensions.PSSetupCode;
import org.apache.fop.traits.RuleStyle;
import org.apache.fop.util.CharUtilities;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.apache.xmlgraphics.ps.DSCConstants;
import org.apache.xmlgraphics.ps.PSDictionary;
import org.apache.xmlgraphics.ps.PSDictionaryFormatException;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSPageDeviceDictionary;
import org.apache.xmlgraphics.ps.PSProcSets;
import org.apache.xmlgraphics.ps.PSResource;
import org.apache.xmlgraphics.ps.PSState;
import org.apache.xmlgraphics.ps.dsc.DSCException;
import org.apache.xmlgraphics.ps.dsc.ResourceTracker;
import org.apache.xmlgraphics.ps.dsc.events.DSCCommentBoundingBox;
import org.apache.xmlgraphics.ps.dsc.events.DSCCommentHiResBoundingBox;
import org.xml.sax.Locator;

public class PSRenderer extends AbstractPathOrientedRenderer implements ImageAdapter, PSSupportedFlavors, PSConfigurationConstants {
   private static Log log;
   public static final String MIME_TYPE = "application/postscript";
   private int currentPageNumber = 0;
   private OutputStream outputStream;
   private File tempFile;
   protected PSGenerator gen;
   private boolean ioTrouble = false;
   private boolean inTextMode = false;
   private List setupCodeList;
   private Map fontResources;
   private Map formResources;
   private PSPageDeviceDictionary pageDeviceDictionary;
   protected PSRenderingUtil psUtil;
   private PSBorderPainter borderPainter;
   private Rectangle2D documentBoundingBox;
   private Collection headerComments;
   private Collection footerComments;

   public void setUserAgent(FOUserAgent agent) {
      super.setUserAgent(agent);
      this.psUtil = new PSRenderingUtil(this.getUserAgent());
   }

   PSRenderingUtil getPSUtil() {
      return this.psUtil;
   }

   public void setAutoRotateLandscape(boolean value) {
      this.getPSUtil().setAutoRotateLandscape(value);
   }

   public boolean isAutoRotateLandscape() {
      return this.getPSUtil().isAutoRotateLandscape();
   }

   public void setLanguageLevel(int level) {
      this.getPSUtil().setLanguageLevel(level);
   }

   public int getLanguageLevel() {
      return this.getPSUtil().getLanguageLevel();
   }

   public void setOptimizeResources(boolean value) {
      this.getPSUtil().setOptimizeResources(value);
   }

   public boolean isOptimizeResources() {
      return this.getPSUtil().isOptimizeResources();
   }

   public Graphics2DAdapter getGraphics2DAdapter() {
      return new PSGraphics2DAdapter(this);
   }

   public ImageAdapter getImageAdapter() {
      return this;
   }

   protected void writeln(String cmd) {
      try {
         this.gen.writeln(cmd);
      } catch (IOException var3) {
         this.handleIOTrouble(var3);
      }

   }

   protected void handleIOTrouble(IOException ioe) {
      if (!this.ioTrouble) {
         RendererEventProducer eventProducer = RendererEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.ioError(this, ioe);
         this.ioTrouble = true;
      }

   }

   protected void comment(String comment) {
      try {
         if (comment.startsWith("%")) {
            this.gen.commentln(comment);
            this.writeln(comment);
         } else {
            this.gen.commentln("%" + comment);
         }
      } catch (IOException var3) {
         this.handleIOTrouble(var3);
      }

   }

   protected void movetoCurrPosition() {
      this.moveTo((float)this.currentIPPosition, (float)this.currentBPPosition);
   }

   protected void clip() {
      this.writeln("clip newpath");
   }

   protected void clipRect(float x, float y, float width, float height) {
      try {
         this.gen.defineRect((double)x, (double)y, (double)width, (double)height);
         this.clip();
      } catch (IOException var6) {
         this.handleIOTrouble(var6);
      }

   }

   protected void moveTo(float x, float y) {
      this.writeln(this.gen.formatDouble((double)x) + " " + this.gen.formatDouble((double)y) + " M");
   }

   protected void rmoveTo(float x, float y) {
      this.writeln(this.gen.formatDouble((double)x) + " " + this.gen.formatDouble((double)y) + " RM");
   }

   protected void lineTo(float x, float y) {
      this.writeln(this.gen.formatDouble((double)x) + " " + this.gen.formatDouble((double)y) + " lineto");
   }

   protected void closePath() {
      this.writeln("cp");
   }

   protected void fillRect(float x, float y, float width, float height) {
      if (width != 0.0F && height != 0.0F) {
         try {
            this.gen.defineRect((double)x, (double)y, (double)width, (double)height);
            this.gen.writeln("fill");
         } catch (IOException var6) {
            this.handleIOTrouble(var6);
         }
      }

   }

   protected void updateColor(Color col, boolean fill) {
      try {
         this.useColor(col);
      } catch (IOException var4) {
         this.handleIOTrouble(var4);
      }

   }

   protected void drawImage(String uri, Rectangle2D pos, Map foreignAttributes) {
      this.endTextObject();
      int x = this.currentIPPosition + (int)Math.round(pos.getX());
      int y = this.currentBPPosition + (int)Math.round(pos.getY());
      uri = URISpecification.getURL(uri);
      if (log.isDebugEnabled()) {
         log.debug("Handling image: " + uri);
      }

      int width = (int)pos.getWidth();
      int height = (int)pos.getHeight();
      Rectangle targetRect = new Rectangle(x, y, width, height);
      ImageManager manager = this.getUserAgent().getFactory().getImageManager();
      ImageInfo info = null;

      ResourceEventProducer eventProducer;
      try {
         ImageSessionContext sessionContext = this.getUserAgent().getImageSessionContext();
         info = manager.getImageInfo(uri, sessionContext);
         PSRenderingContext renderingContext = new PSRenderingContext(this.getUserAgent(), this.gen, this.getFontInfo());
         if (this.isOptimizeResources() && !PSImageUtils.isImageInlined(info, renderingContext)) {
            if (log.isDebugEnabled()) {
               log.debug("Image " + info + " is embedded as a form later");
            }

            PSResource form = this.getFormForImage(info.getOriginalURI());
            PSImageUtils.drawForm(form, info, targetRect, this.gen);
         } else {
            if (log.isDebugEnabled()) {
               log.debug("Image " + info + " is inlined");
            }

            ImageHandlerRegistry imageHandlerRegistry = this.userAgent.getFactory().getImageHandlerRegistry();
            ImageFlavor[] flavors = imageHandlerRegistry.getSupportedFlavors(renderingContext);
            Map hints = ImageUtil.getDefaultHints(sessionContext);
            Image img = manager.getImage(info, flavors, hints, sessionContext);
            ImageHandler basicHandler = imageHandlerRegistry.getHandler(renderingContext, img);
            basicHandler.handleImage(renderingContext, img, targetRect);
         }
      } catch (ImageException var18) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageError(this, info != null ? info.toString() : uri, var18, (Locator)null);
      } catch (FileNotFoundException var19) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageNotFound(this, info != null ? info.toString() : uri, var19, (Locator)null);
      } catch (IOException var20) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageIOError(this, info != null ? info.toString() : uri, var20, (Locator)null);
      }

   }

   protected PSResource getFormForImage(String uri) {
      if (uri != null && !"".equals(uri)) {
         if (this.formResources == null) {
            this.formResources = new HashMap();
         }

         PSResource form = (PSResource)this.formResources.get(uri);
         if (form == null) {
            form = new PSImageFormResource(this.formResources.size() + 1, uri);
            this.formResources.put(uri, form);
         }

         return (PSResource)form;
      } else {
         throw new IllegalArgumentException("uri must not be empty or null");
      }
   }

   public void paintImage(RenderedImage image, RendererContext context, int x, int y, int width, int height) throws IOException {
      float fx = (float)x / 1000.0F;
      x = (int)((float)x + (float)this.currentIPPosition / 1000.0F);
      float fy = (float)y / 1000.0F;
      y = (int)((float)y + (float)this.currentBPPosition / 1000.0F);
      float fw = (float)width / 1000.0F;
      float fh = (float)height / 1000.0F;
      PSImageUtils.renderBitmapImage(image, fx, fy, fw, fh, this.gen);
   }

   private void drawLine(float startx, float starty, float endx, float endy) {
      this.writeln(this.gen.formatDouble((double)startx) + " " + this.gen.formatDouble((double)starty) + " M " + this.gen.formatDouble((double)endx) + " " + this.gen.formatDouble((double)endy) + " lineto stroke newpath");
   }

   public void saveGraphicsState() {
      this.endTextObject();

      try {
         this.gen.saveGraphicsState();
      } catch (IOException var2) {
         this.handleIOTrouble(var2);
      }

   }

   public void restoreGraphicsState() {
      try {
         this.endTextObject();
         this.gen.restoreGraphicsState();
      } catch (IOException var2) {
         this.handleIOTrouble(var2);
      }

   }

   protected void concatMatrix(double a, double b, double c, double d, double e, double f) {
      try {
         this.gen.concatMatrix(a, b, c, d, e, f);
      } catch (IOException var14) {
         this.handleIOTrouble(var14);
      }

   }

   protected void concatMatrix(double[] matrix) {
      try {
         this.gen.concatMatrix(matrix);
      } catch (IOException var3) {
         this.handleIOTrouble(var3);
      }

   }

   protected void concatenateTransformationMatrix(AffineTransform at) {
      try {
         this.gen.concatMatrix(at);
      } catch (IOException var3) {
         this.handleIOTrouble(var3);
      }

   }

   private String getPostScriptNameForFontKey(String key) {
      int pos = key.indexOf(95);
      String postFix = null;
      if (pos > 0) {
         postFix = key.substring(pos);
         key = key.substring(0, pos);
      }

      Map fonts = this.fontInfo.getFonts();
      Typeface tf = (Typeface)fonts.get(key);
      if (tf instanceof LazyFont) {
         tf = ((LazyFont)tf).getRealFont();
      }

      if (tf == null) {
         throw new IllegalStateException("Font not available: " + key);
      } else {
         return postFix == null ? tf.getFontName() : tf.getFontName() + postFix;
      }
   }

   protected PSResource getPSResourceForFontKey(String key) {
      PSResource res = null;
      if (this.fontResources != null) {
         res = (PSResource)this.fontResources.get(key);
      } else {
         this.fontResources = new HashMap();
      }

      if (res == null) {
         res = new PSResource("font", this.getPostScriptNameForFontKey(key));
         this.fontResources.put(key, res);
      }

      return res;
   }

   protected void useFont(String key, int size) {
      try {
         PSResource res = this.getPSResourceForFontKey(key);
         this.gen.useFont("/" + res.getName(), (float)size / 1000.0F);
         this.gen.getResourceTracker().notifyResourceUsageOnPage(res);
      } catch (IOException var4) {
         this.handleIOTrouble(var4);
      }

   }

   private void useColor(Color col) throws IOException {
      this.gen.useColor(col);
   }

   protected void drawBackAndBorders(Area area, float startx, float starty, float width, float height) {
      if (area.hasTrait(Trait.BACKGROUND) || area.hasTrait(Trait.BORDER_BEFORE) || area.hasTrait(Trait.BORDER_AFTER) || area.hasTrait(Trait.BORDER_START) || area.hasTrait(Trait.BORDER_END)) {
         this.comment("%FOPBeginBackgroundAndBorder: " + startx + " " + starty + " " + width + " " + height);
         super.drawBackAndBorders(area, startx, starty, width, height);
         this.comment("%FOPEndBackgroundAndBorder");
      }

   }

   protected void drawBorderLine(float x1, float y1, float x2, float y2, boolean horz, boolean startOrBefore, int style, Color col) {
      try {
         PSBorderPainter.drawBorderLine(this.gen, x1, y1, x2, y2, horz, startOrBefore, style, col);
      } catch (IOException var10) {
         this.handleIOTrouble(var10);
      }

   }

   public void startRenderer(OutputStream outputStream) throws IOException {
      log.debug("Rendering areas to PostScript...");
      this.outputStream = outputStream;
      Object out;
      if (this.isOptimizeResources()) {
         this.tempFile = File.createTempFile("fop", (String)null);
         OutputStream out = new FileOutputStream(this.tempFile);
         out = new BufferedOutputStream(out);
      } else {
         out = this.outputStream;
      }

      this.gen = new PSGenerator((OutputStream)out) {
         public Source resolveURI(String uri) {
            return PSRenderer.this.userAgent.resolveURI(uri);
         }
      };
      this.gen.setPSLevel(this.getLanguageLevel());
      this.borderPainter = new PSBorderPainter(this.gen);
      this.currentPageNumber = 0;
      this.pageDeviceDictionary = new PSPageDeviceDictionary();
      this.pageDeviceDictionary.setFlushOnRetrieval(!this.getPSUtil().isDSCComplianceEnabled());
      this.pageDeviceDictionary.put("/ImagingBBox", "null");
   }

   private void writeHeader() throws IOException {
      this.writeln("%!PS-Adobe-3.0");
      this.gen.writeDSCComment("Creator", (Object[])(new String[]{this.userAgent.getProducer()}));
      this.gen.writeDSCComment("CreationDate", new Object[]{new Date()});
      this.gen.writeDSCComment("LanguageLevel", (Object)(new Integer(this.gen.getPSLevel())));
      this.gen.writeDSCComment("Pages", new Object[]{DSCConstants.ATEND});
      this.gen.writeDSCComment("BoundingBox", DSCConstants.ATEND);
      this.gen.writeDSCComment("HiResBoundingBox", DSCConstants.ATEND);
      this.documentBoundingBox = new Rectangle2D.Double();
      this.gen.writeDSCComment("DocumentSuppliedResources", new Object[]{DSCConstants.ATEND});
      if (this.headerComments != null) {
         Iterator iter = this.headerComments.iterator();

         while(iter.hasNext()) {
            PSExtensionAttachment comment = (PSExtensionAttachment)iter.next();
            this.gen.writeln("%" + comment.getContent());
         }
      }

      this.gen.writeDSCComment("EndComments");
      this.gen.writeDSCComment("BeginDefaults");
      this.gen.writeDSCComment("EndDefaults");
      this.gen.writeDSCComment("BeginProlog");
      PSProcSets.writeStdProcSet(this.gen);
      PSProcSets.writeEPSProcSet(this.gen);
      this.gen.writeDSCComment("EndProlog");
      this.gen.writeDSCComment("BeginSetup");
      PSRenderingUtil.writeSetupCodeList(this.gen, this.setupCodeList, "SetupCode");
      if (!this.isOptimizeResources()) {
         this.fontResources = PSFontUtils.writeFontDict(this.gen, this.fontInfo);
      } else {
         this.gen.commentln("%FOPFontSetup");
      }

      this.gen.writeDSCComment("EndSetup");
   }

   public void stopRenderer() throws IOException {
      this.gen.writeDSCComment("Trailer");
      if (this.footerComments != null) {
         Iterator iter = this.footerComments.iterator();

         while(iter.hasNext()) {
            PSExtensionAttachment comment = (PSExtensionAttachment)iter.next();
            this.gen.commentln("%" + comment.getContent());
         }

         this.footerComments.clear();
      }

      this.gen.writeDSCComment("Pages", (Object)(new Integer(this.currentPageNumber)));
      (new DSCCommentBoundingBox(this.documentBoundingBox)).generate(this.gen);
      (new DSCCommentHiResBoundingBox(this.documentBoundingBox)).generate(this.gen);
      this.gen.getResourceTracker().writeResources(false, this.gen);
      this.gen.writeDSCComment("EOF");
      this.gen.flush();
      log.debug("Rendering to PostScript complete.");
      if (this.isOptimizeResources()) {
         IOUtils.closeQuietly(this.gen.getOutputStream());
         this.rewritePostScriptFile();
      }

      if (this.footerComments != null) {
         this.headerComments.clear();
      }

      if (this.pageDeviceDictionary != null) {
         this.pageDeviceDictionary.clear();
      }

      this.borderPainter = null;
      this.gen = null;
   }

   private void rewritePostScriptFile() throws IOException {
      log.debug("Processing PostScript resources...");
      long startTime = System.currentTimeMillis();
      ResourceTracker resTracker = this.gen.getResourceTracker();
      InputStream in = new FileInputStream(this.tempFile);
      InputStream in = new BufferedInputStream(in);

      try {
         ResourceHandler handler = new ResourceHandler(this.userAgent, this.fontInfo, resTracker, this.formResources);
         handler.process(in, this.outputStream, this.currentPageNumber, this.documentBoundingBox);
         this.outputStream.flush();
      } catch (DSCException var10) {
         throw new RuntimeException(var10.getMessage());
      } finally {
         IOUtils.closeQuietly((InputStream)in);
         if (!this.tempFile.delete()) {
            this.tempFile.deleteOnExit();
            log.warn("Could not delete temporary file: " + this.tempFile);
         }

      }

      if (log.isDebugEnabled()) {
         long duration = System.currentTimeMillis() - startTime;
         log.debug("Resource Processing complete in " + duration + " ms.");
      }

   }

   public void processOffDocumentItem(OffDocumentItem oDI) {
      if (log.isDebugEnabled()) {
         log.debug("Handling OffDocumentItem: " + oDI.getName());
      }

      if (oDI instanceof OffDocumentExtensionAttachment) {
         ExtensionAttachment attachment = ((OffDocumentExtensionAttachment)oDI).getAttachment();
         if (attachment != null && "apache:fop:extensions:postscript".equals(attachment.getCategory())) {
            if (attachment instanceof PSSetupCode) {
               if (this.setupCodeList == null) {
                  this.setupCodeList = new ArrayList();
               }

               if (!this.setupCodeList.contains(attachment)) {
                  this.setupCodeList.add(attachment);
               }
            } else if (attachment instanceof PSSetPageDevice) {
               PSSetPageDevice setPageDevice = (PSSetPageDevice)attachment;
               String content = setPageDevice.getContent();
               if (content != null) {
                  try {
                     this.pageDeviceDictionary.putAll(PSDictionary.valueOf(content));
                  } catch (PSDictionaryFormatException var7) {
                     PSEventProducer eventProducer = PSEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
                     eventProducer.postscriptDictionaryParseError(this, content, var7);
                  }
               }
            } else if (attachment instanceof PSCommentBefore) {
               if (this.headerComments == null) {
                  this.headerComments = new ArrayList();
               }

               this.headerComments.add(attachment);
            } else if (attachment instanceof PSCommentAfter) {
               if (this.footerComments == null) {
                  this.footerComments = new ArrayList();
               }

               this.footerComments.add(attachment);
            }
         }
      }

      super.processOffDocumentItem(oDI);
   }

   public void renderPage(PageViewport page) throws IOException, FOPException {
      log.debug("renderPage(): " + page);
      if (this.currentPageNumber == 0) {
         this.writeHeader();
      }

      ++this.currentPageNumber;
      this.gen.getResourceTracker().notifyStartNewPage();
      this.gen.getResourceTracker().notifyResourceUsageOnPage(PSProcSets.STD_PROCSET);
      this.gen.writeDSCComment("Page", new Object[]{page.getPageNumberString(), new Integer(this.currentPageNumber)});
      double pageWidth = (double)((float)page.getViewArea().width / 1000.0F);
      double pageHeight = (double)((float)page.getViewArea().height / 1000.0F);
      boolean rotate = false;
      List pageSizes = new ArrayList();
      if (this.getPSUtil().isAutoRotateLandscape() && pageHeight < pageWidth) {
         rotate = true;
         pageSizes.add(new Long(Math.round(pageHeight)));
         pageSizes.add(new Long(Math.round(pageWidth)));
      } else {
         pageSizes.add(new Long(Math.round(pageWidth)));
         pageSizes.add(new Long(Math.round(pageHeight)));
      }

      this.pageDeviceDictionary.put("/PageSize", pageSizes);
      if (page.hasExtensionAttachments()) {
         Iterator iter = page.getExtensionAttachments().iterator();

         while(iter.hasNext()) {
            ExtensionAttachment attachment = (ExtensionAttachment)iter.next();
            if (attachment instanceof PSSetPageDevice) {
               PSSetPageDevice setPageDevice = (PSSetPageDevice)attachment;
               String content = setPageDevice.getContent();
               if (content != null) {
                  try {
                     this.pageDeviceDictionary.putAll(PSDictionary.valueOf(content));
                  } catch (PSDictionaryFormatException var15) {
                     PSEventProducer eventProducer = PSEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
                     eventProducer.postscriptDictionaryParseError(this, content, var15);
                  }
               }
            }
         }
      }

      try {
         if (this.setupCodeList != null) {
            PSRenderingUtil.writeEnclosedExtensionAttachments(this.gen, this.setupCodeList);
            this.setupCodeList.clear();
         }
      } catch (IOException var14) {
         log.error(var14.getMessage());
      }

      Integer zero = new Integer(0);
      Rectangle2D pageBoundingBox = new Rectangle2D.Double();
      if (rotate) {
         pageBoundingBox.setRect(0.0, 0.0, pageHeight, pageWidth);
         this.gen.writeDSCComment("PageBoundingBox", new Object[]{zero, zero, new Long(Math.round(pageHeight)), new Long(Math.round(pageWidth))});
         this.gen.writeDSCComment("PageHiResBoundingBox", new Object[]{zero, zero, new Double(pageHeight), new Double(pageWidth)});
         this.gen.writeDSCComment("PageOrientation", (Object)"Landscape");
      } else {
         pageBoundingBox.setRect(0.0, 0.0, pageWidth, pageHeight);
         this.gen.writeDSCComment("PageBoundingBox", new Object[]{zero, zero, new Long(Math.round(pageWidth)), new Long(Math.round(pageHeight))});
         this.gen.writeDSCComment("PageHiResBoundingBox", new Object[]{zero, zero, new Double(pageWidth), new Double(pageHeight)});
         if (this.getPSUtil().isAutoRotateLandscape()) {
            this.gen.writeDSCComment("PageOrientation", (Object)"Portrait");
         }
      }

      this.documentBoundingBox.add(pageBoundingBox);
      this.gen.writeDSCComment("PageResources", new Object[]{DSCConstants.ATEND});
      this.gen.commentln("%FOPSimplePageMaster: " + page.getSimplePageMasterName());
      this.gen.writeDSCComment("BeginPageSetup");
      Object attObj;
      List extensionAttachments;
      int i;
      PSExtensionAttachment attachment;
      if (page.hasExtensionAttachments()) {
         extensionAttachments = page.getExtensionAttachments();

         for(i = 0; i < extensionAttachments.size(); ++i) {
            attObj = extensionAttachments.get(i);
            if (attObj instanceof PSExtensionAttachment) {
               attachment = (PSExtensionAttachment)attObj;
               if (attachment instanceof PSCommentBefore) {
                  this.gen.commentln("%" + attachment.getContent());
               } else if (attachment instanceof PSSetupCode) {
                  this.gen.writeln(attachment.getContent());
               }
            }
         }
      }

      if (!this.pageDeviceDictionary.isEmpty()) {
         String content = this.pageDeviceDictionary.getContent();
         if (this.getPSUtil().isSafeSetPageDevice()) {
            content = content + " SSPD";
         } else {
            content = content + " setpagedevice";
         }

         PSRenderingUtil.writeEnclosedExtensionAttachment(this.gen, new PSSetPageDevice(content));
      }

      if (rotate) {
         this.gen.writeln(Math.round(pageHeight) + " 0 translate");
         this.gen.writeln("90 rotate");
      }

      this.concatMatrix(1.0, 0.0, 0.0, -1.0, 0.0, pageHeight);
      this.gen.writeDSCComment("EndPageSetup");
      super.renderPage(page);
      this.gen.showPage();
      this.gen.writeDSCComment("PageTrailer");
      if (page.hasExtensionAttachments()) {
         extensionAttachments = page.getExtensionAttachments();

         for(i = 0; i < extensionAttachments.size(); ++i) {
            attObj = extensionAttachments.get(i);
            if (attObj instanceof PSExtensionAttachment) {
               attachment = (PSExtensionAttachment)attObj;
               if (attachment instanceof PSCommentAfter) {
                  this.gen.commentln("%" + attachment.getContent());
               }
            }
         }
      }

      this.gen.getResourceTracker().writeResources(true, this.gen);
   }

   protected void renderRegionViewport(RegionViewport port) {
      if (port != null) {
         this.comment("%FOPBeginRegionViewport: " + port.getRegionReference().getRegionName());
         super.renderRegionViewport(port);
         this.comment("%FOPEndRegionViewport");
      }

   }

   protected void beginTextObject() {
      if (!this.inTextMode) {
         this.saveGraphicsState();
         this.writeln("BT");
         this.inTextMode = true;
      }

   }

   protected void endTextObject() {
      if (this.inTextMode) {
         this.inTextMode = false;
         this.writeln("ET");
         this.restoreGraphicsState();
      }

   }

   public void renderText(TextArea area) {
      this.renderInlineAreaBackAndBorders(area);
      String fontkey = this.getInternalFontNameForArea(area);
      int fontsize = area.getTraitAsInteger(Trait.FONT_SIZE);
      Typeface tf = (Typeface)this.fontInfo.getFonts().get(fontkey);
      int rx = this.currentIPPosition + area.getBorderAndPaddingWidthStart();
      int bl = this.currentBPPosition + area.getOffset() + area.getBaselineOffset();
      Color ct = (Color)area.getTrait(Trait.COLOR);
      if (ct != null) {
         try {
            this.useColor(ct);
         } catch (IOException var9) {
            this.handleIOTrouble(var9);
         }
      }

      this.beginTextObject();
      this.writeln("1 0 0 -1 " + this.gen.formatDouble((double)((float)rx / 1000.0F)) + " " + this.gen.formatDouble((double)((float)bl / 1000.0F)) + " Tm");
      super.renderText(area);
      this.renderTextDecoration(tf, fontsize, area, bl, rx);
   }

   protected void renderWord(WordArea word) {
      this.renderText((TextArea)word.getParentArea(), word.getWord(), word.getLetterAdjustArray());
      super.renderWord(word);
   }

   protected void renderSpace(SpaceArea space) {
      AbstractTextArea textArea = (AbstractTextArea)space.getParentArea();
      String s = space.getSpace();
      char sp = s.charAt(0);
      Font font = this.getFontFromArea(textArea);
      int tws = space.isAdjustable() ? ((TextArea)space.getParentArea()).getTextWordSpaceAdjust() + 2 * textArea.getTextLetterSpaceAdjust() : 0;
      this.rmoveTo((float)(font.getCharWidth(sp) + tws) / 1000.0F, 0.0F);
      super.renderSpace(space);
   }

   private Typeface getTypeface(String fontName) {
      Typeface tf = (Typeface)this.fontInfo.getFonts().get(fontName);
      if (tf instanceof LazyFont) {
         tf = ((LazyFont)tf).getRealFont();
      }

      return tf;
   }

   private void renderText(AbstractTextArea area, String text, int[] letterAdjust) {
      String fontkey = this.getInternalFontNameForArea(area);
      int fontSize = area.getTraitAsInteger(Trait.FONT_SIZE);
      Font font = this.getFontFromArea(area);
      Typeface tf = this.getTypeface(font.getFontName());
      SingleByteFont singleByteFont = null;
      if (tf instanceof SingleByteFont) {
         singleByteFont = (SingleByteFont)tf;
      }

      int textLen = text.length();
      if (singleByteFont != null && singleByteFont.hasAdditionalEncodings()) {
         int start = 0;
         int currentEncoding = -1;

         for(int i = 0; i < textLen; ++i) {
            char c = text.charAt(i);
            char mapped = tf.mapChar(c);
            int encoding = mapped / 256;
            if (currentEncoding != encoding) {
               if (i > 0) {
                  this.writeText(area, text, start, i - start, letterAdjust, fontSize, tf);
               }

               if (encoding == 0) {
                  this.useFont(fontkey, fontSize);
               } else {
                  this.useFont(fontkey + "_" + Integer.toString(encoding), fontSize);
               }

               currentEncoding = encoding;
               start = i;
            }
         }

         this.writeText(area, text, start, textLen - start, letterAdjust, fontSize, tf);
      } else {
         this.useFont(fontkey, fontSize);
         this.writeText(area, text, 0, textLen, letterAdjust, fontSize, tf);
      }

   }

   private void writeText(AbstractTextArea area, String text, int start, int len, int[] letterAdjust, int fontsize, Typeface tf) {
      int end = start + len;
      int initialSize = text.length();
      initialSize += initialSize / 2;
      StringBuffer sb = new StringBuffer(initialSize);
      char c;
      if (letterAdjust == null && area.getTextLetterSpaceAdjust() == 0 && area.getTextWordSpaceAdjust() == 0) {
         sb.append("(");

         for(int i = start; i < end; ++i) {
            char c = text.charAt(i);
            c = (char)(tf.mapChar(c) % 256);
            PSGenerator.escapeChar(c, sb);
         }

         sb.append(") t");
      } else {
         sb.append("(");
         int[] offsets = new int[len];

         int i;
         for(i = start; i < end; ++i) {
            c = text.charAt(i);
            char mapped = tf.mapChar(c);
            char codepoint = (char)(mapped % 256);
            int wordSpace;
            if (CharUtilities.isAdjustableSpace(mapped)) {
               wordSpace = area.getTextWordSpaceAdjust();
            } else {
               wordSpace = 0;
            }

            int cw = tf.getWidth(mapped, fontsize) / 1000;
            int ladj = letterAdjust != null && i < end - 1 ? letterAdjust[i + 1] : 0;
            int tls = i < end - 1 ? area.getTextLetterSpaceAdjust() : 0;
            offsets[i - start] = cw + ladj + tls + wordSpace;
            PSGenerator.escapeChar(codepoint, sb);
         }

         sb.append(")\n[");

         for(i = 0; i < len; ++i) {
            if (i > 0) {
               if (i % 8 == 0) {
                  sb.append('\n');
               } else {
                  sb.append(" ");
               }
            }

            sb.append(this.gen.formatDouble((double)((float)offsets[i] / 1000.0F)));
         }

         sb.append("]\nxshow");
      }

      this.writeln(sb.toString());
   }

   protected List breakOutOfStateStack() {
      try {
         List breakOutList = new ArrayList();

         while(true) {
            if (breakOutList.size() == 0) {
               this.endTextObject();
               this.comment("------ break out!");
            }

            PSState state = this.gen.getCurrentState();
            if (!this.gen.restoreGraphicsState()) {
               return breakOutList;
            }

            breakOutList.add(0, state);
         }
      } catch (IOException var3) {
         this.handleIOTrouble(var3);
         return null;
      }
   }

   protected void restoreStateStackAfterBreakOut(List breakOutList) {
      try {
         this.comment("------ restoring context after break-out...");
         Iterator i = breakOutList.iterator();

         while(i.hasNext()) {
            PSState state = (PSState)i.next();
            this.saveGraphicsState();
            state.reestablish(this.gen);
         }

         this.comment("------ done.");
      } catch (IOException var4) {
         this.handleIOTrouble(var4);
      }

   }

   protected void startVParea(CTM ctm, Rectangle2D clippingRect) {
      this.saveGraphicsState();
      if (clippingRect != null) {
         this.clipRect((float)clippingRect.getX() / 1000.0F, (float)clippingRect.getY() / 1000.0F, (float)clippingRect.getWidth() / 1000.0F, (float)clippingRect.getHeight() / 1000.0F);
      }

      double[] matrix = ctm.toArray();
      matrix[4] /= 1000.0;
      matrix[5] /= 1000.0;
      this.concatMatrix(matrix);
   }

   protected void endVParea() {
      this.restoreGraphicsState();
   }

   protected void renderBlockViewport(BlockViewport bv, List children) {
      this.comment("%FOPBeginBlockViewport: " + bv.toString());
      super.renderBlockViewport(bv, children);
      this.comment("%FOPEndBlockViewport");
   }

   protected void renderInlineParent(InlineParent ip) {
      super.renderInlineParent(ip);
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

      try {
         this.borderPainter.drawLine(new Point(startx, starty), new Point(endx, starty), ruleThickness, col, RuleStyle.valueOf(style));
      } catch (IOException var9) {
         this.handleIOTrouble(var9);
      }

      super.renderLeader(area);
   }

   public void renderImage(org.apache.fop.area.inline.Image image, Rectangle2D pos) {
      this.drawImage(image.getURL(), pos, image.getForeignAttributes());
   }

   protected RendererContext createRendererContext(int x, int y, int width, int height, Map foreignAttributes) {
      RendererContext context = super.createRendererContext(x, y, width, height, foreignAttributes);
      context.setProperty("psGenerator", this.gen);
      context.setProperty("psFontInfo", this.fontInfo);
      return context;
   }

   public String getMimeType() {
      return "application/postscript";
   }

   public void setSafeSetPageDevice(boolean safeSetPageDevice) {
      this.getPSUtil().setSafeSetPageDevice(safeSetPageDevice);
   }

   public void setDSCCompliant(boolean dscCompliant) {
      this.getPSUtil().setDSCComplianceEnabled(dscCompliant);
   }

   static {
      log = LogFactory.getLog(PSRenderer.class);
   }
}
