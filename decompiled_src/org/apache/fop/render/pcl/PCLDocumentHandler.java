package org.apache.fop.render.pcl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.render.intermediate.AbstractBinaryWritingIFDocumentHandler;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFPainter;
import org.apache.fop.render.java2d.Java2DPainter;
import org.apache.fop.render.java2d.Java2DUtil;
import org.apache.fop.render.pcl.extensions.PCLElementMapping;
import org.apache.xmlgraphics.util.UnitConv;

public class PCLDocumentHandler extends AbstractBinaryWritingIFDocumentHandler implements PCLConstants {
   private static Log log;
   protected PCLRenderingUtil pclUtil;
   private PCLGenerator gen;
   private PCLPageDefinition currentPageDefinition;
   private long pageWidth = 0L;
   private long pageHeight = 0L;
   private BufferedImage currentImage;

   public boolean supportsPagesOutOfOrder() {
      return false;
   }

   public String getMimeType() {
      return "application/x-pcl";
   }

   public void setContext(IFContext context) {
      super.setContext(context);
      this.pclUtil = new PCLRenderingUtil(context.getUserAgent());
   }

   public IFDocumentHandlerConfigurator getConfigurator() {
      return new PCLRendererConfigurator(this.getUserAgent());
   }

   public void setDefaultFontInfo(FontInfo fontInfo) {
      FontInfo fi = Java2DUtil.buildDefaultJava2DBasedFontInfo(fontInfo, this.getUserAgent());
      this.setFontInfo(fi);
   }

   PCLRenderingUtil getPCLUtil() {
      return this.pclUtil;
   }

   PCLGenerator getPCLGenerator() {
      return this.gen;
   }

   protected int getResolution() {
      int resolution = Math.round(this.getUserAgent().getTargetResolution());
      return resolution <= 300 ? 300 : 600;
   }

   public void startDocument() throws IFException {
      super.startDocument();

      try {
         this.gen = new PCLGenerator(this.outputStream, this.getResolution());
         if (!this.pclUtil.isPJLDisabled()) {
            this.gen.universalEndOfLanguage();
            this.gen.writeText("@PJL COMMENT Produced by " + this.getUserAgent().getProducer() + "\n");
            if (this.getUserAgent().getTitle() != null) {
               this.gen.writeText("@PJL JOB NAME = \"" + this.getUserAgent().getTitle() + "\"\n");
            }

            this.gen.writeText("@PJL SET RESOLUTION = " + this.getResolution() + "\n");
            this.gen.writeText("@PJL ENTER LANGUAGE = PCL\n");
         }

         this.gen.resetPrinter();
         this.gen.setUnitOfMeasure(this.getResolution());
         this.gen.setRasterGraphicsResolution(this.getResolution());
      } catch (IOException var2) {
         throw new IFException("I/O error in startDocument()", var2);
      }
   }

   public void endDocumentHeader() throws IFException {
   }

   public void endDocument() throws IFException {
      try {
         this.gen.separateJobs();
         this.gen.resetPrinter();
         if (!this.pclUtil.isPJLDisabled()) {
            this.gen.universalEndOfLanguage();
         }
      } catch (IOException var2) {
         throw new IFException("I/O error in endDocument()", var2);
      }

      super.endDocument();
   }

   public void startPageSequence(String id) throws IFException {
   }

   public void endPageSequence() throws IFException {
   }

   public void startPage(int index, String name, String pageMasterName, Dimension size) throws IFException {
      try {
         Object paperSource = this.getContext().getForeignAttribute(PCLElementMapping.PCL_PAPER_SOURCE);
         if (paperSource != null) {
            this.gen.selectPaperSource(Integer.parseInt(paperSource.toString()));
         }

         Object outputBin = this.getContext().getForeignAttribute(PCLElementMapping.PCL_OUTPUT_BIN);
         if (outputBin != null) {
            this.gen.selectOutputBin(Integer.parseInt(outputBin.toString()));
         }

         Object pageDuplex = this.getContext().getForeignAttribute(PCLElementMapping.PCL_DUPLEX_MODE);
         if (pageDuplex != null) {
            this.gen.selectDuplexMode(Integer.parseInt(pageDuplex.toString()));
         }

         long pagewidth = (long)size.width;
         long pageheight = (long)size.height;
         this.selectPageFormat(pagewidth, pageheight);
      } catch (IOException var12) {
         throw new IFException("I/O error in startPage()", var12);
      }
   }

   public IFPainter startPageContent() throws IFException {
      return (IFPainter)(this.pclUtil.getRenderingMode() == PCLRenderingMode.BITMAP ? this.createAllBitmapPainter() : new PCLPainter(this, this.currentPageDefinition));
   }

   private IFPainter createAllBitmapPainter() {
      double scale = (double)((float)this.gen.getMaximumBitmapResolution() / 72.0F);
      Rectangle printArea = this.currentPageDefinition.getLogicalPageRect();
      int bitmapWidth = (int)Math.ceil(UnitConv.mpt2px((double)printArea.width, this.gen.getMaximumBitmapResolution()));
      int bitmapHeight = (int)Math.ceil(UnitConv.mpt2px((double)printArea.height, this.gen.getMaximumBitmapResolution()));
      this.currentImage = this.createBufferedImage(bitmapWidth, bitmapHeight);
      Graphics2D graphics2D = this.currentImage.createGraphics();
      if (!PCLGenerator.isJAIAvailable()) {
         RenderingHints hints = new RenderingHints((Map)null);
         hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
         graphics2D.addRenderingHints(hints);
      }

      graphics2D.setBackground(Color.WHITE);
      graphics2D.clearRect(0, 0, bitmapWidth, bitmapHeight);
      graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      graphics2D.scale(scale / 1000.0, scale / 1000.0);
      graphics2D.translate(-printArea.x, -printArea.y);
      return new Java2DPainter(graphics2D, this.getContext(), this.getFontInfo());
   }

   private BufferedImage createBufferedImage(int bitmapWidth, int bitmapHeight) {
      byte bitmapType;
      if (PCLGenerator.isJAIAvailable()) {
         bitmapType = 10;
      } else {
         bitmapType = 12;
      }

      return new BufferedImage(bitmapWidth, bitmapHeight, bitmapType);
   }

   public void endPageContent() throws IFException {
      if (this.currentImage != null) {
         try {
            Rectangle printArea = this.currentPageDefinition.getLogicalPageRect();
            this.gen.setCursorPos(0.0, 0.0);
            this.gen.paintBitmap(this.currentImage, printArea.getSize(), true);
         } catch (IOException var5) {
            throw new IFException("I/O error while encoding page image", var5);
         } finally {
            this.currentImage = null;
         }
      }

   }

   public void endPage() throws IFException {
      try {
         this.gen.formFeed();
      } catch (IOException var2) {
         throw new IFException("I/O error in endPage()", var2);
      }
   }

   public void handleExtensionObject(Object extension) throws IFException {
      log.debug("Don't know how to handle extension object. Ignoring: " + extension + " (" + extension.getClass().getName() + ")");
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

   static {
      log = LogFactory.getLog(PCLDocumentHandler.class);
   }
}
