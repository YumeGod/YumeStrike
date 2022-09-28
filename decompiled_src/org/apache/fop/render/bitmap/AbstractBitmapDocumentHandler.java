package org.apache.fop.render.bitmap;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.commons.io.IOUtils;
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
import org.apache.xmlgraphics.image.writer.ImageWriter;
import org.apache.xmlgraphics.image.writer.ImageWriterRegistry;
import org.apache.xmlgraphics.image.writer.MultiImageWriter;

public abstract class AbstractBitmapDocumentHandler extends AbstractBinaryWritingIFDocumentHandler {
   private static Log log;
   public static final String TARGET_BITMAP_SIZE = "target-bitmap-size";
   private ImageWriter imageWriter;
   private MultiImageWriter multiImageWriter;
   private MultiFileRenderingUtil multiFileUtil;
   private int pageCount;
   private Dimension currentPageDimensions;
   private BufferedImage currentImage;
   private BitmapRenderingSettings bitmapSettings = new BitmapRenderingSettings();
   private double scaleFactor = 1.0;
   private Dimension targetBitmapSize;

   public boolean supportsPagesOutOfOrder() {
      return false;
   }

   public abstract String getMimeType();

   public abstract String getDefaultExtension();

   public void setContext(IFContext context) {
      super.setContext(context);
      int dpi = Math.round(context.getUserAgent().getTargetResolution());
      this.getSettings().getWriterParams().setResolution(dpi);
      Map renderingOptions = this.getUserAgent().getRendererOptions();
      this.setTargetBitmapSize((Dimension)renderingOptions.get("target-bitmap-size"));
   }

   public abstract IFDocumentHandlerConfigurator getConfigurator();

   public BitmapRenderingSettings getSettings() {
      return this.bitmapSettings;
   }

   public void setDefaultFontInfo(FontInfo fontInfo) {
      FontInfo fi = Java2DUtil.buildDefaultJava2DBasedFontInfo(fontInfo, this.getUserAgent());
      this.setFontInfo(fi);
   }

   public void setTargetBitmapSize(Dimension size) {
      this.targetBitmapSize = size;
   }

   public void startDocument() throws IFException {
      super.startDocument();

      try {
         this.imageWriter = ImageWriterRegistry.getInstance().getWriterFor(this.getMimeType());
         if (this.imageWriter == null) {
            BitmapRendererEventProducer eventProducer = BitmapRendererEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.noImageWriterFound(this, this.getMimeType());
         }

         if (this.imageWriter.supportsMultiImageWriter()) {
            this.multiImageWriter = this.imageWriter.createMultiImageWriter(this.outputStream);
         } else {
            this.multiFileUtil = new MultiFileRenderingUtil(this.getDefaultExtension(), this.getUserAgent().getOutputFile());
         }

         this.pageCount = 0;
      } catch (IOException var2) {
         throw new IFException("I/O error in startDocument()", var2);
      }
   }

   public void endDocumentHeader() throws IFException {
   }

   public void endDocument() throws IFException {
      try {
         if (this.multiImageWriter != null) {
            this.multiImageWriter.close();
         }

         this.multiImageWriter = null;
         this.imageWriter = null;
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
      ++this.pageCount;
      this.currentPageDimensions = new Dimension(size);
   }

   public IFPainter startPageContent() throws IFException {
      Point2D offset = null;
      int bitmapWidth;
      int bitmapHeight;
      double scale;
      if (this.targetBitmapSize != null) {
         double scale2w = (double)(1000 * this.targetBitmapSize.width) / this.currentPageDimensions.getWidth();
         double scale2h = (double)(1000 * this.targetBitmapSize.height) / this.currentPageDimensions.getHeight();
         bitmapWidth = this.targetBitmapSize.width;
         bitmapHeight = this.targetBitmapSize.height;
         offset = new Point2D.Double();
         double h;
         if (scale2w < scale2h) {
            scale = scale2w;
            h = (double)this.currentPageDimensions.height * scale2w / 1000.0;
            offset.setLocation(0.0, ((double)bitmapHeight - h) / 2.0);
         } else {
            scale = scale2h;
            h = (double)this.currentPageDimensions.width * scale2h / 1000.0;
            offset.setLocation(((double)bitmapWidth - h) / 2.0, 0.0);
         }
      } else {
         scale = this.scaleFactor * (double)this.getUserAgent().getTargetResolution() / 72.0;
         bitmapWidth = (int)((double)this.currentPageDimensions.width * scale / 1000.0 + 0.5);
         bitmapHeight = (int)((double)this.currentPageDimensions.height * scale / 1000.0 + 0.5);
      }

      this.currentImage = this.createBufferedImage(bitmapWidth, bitmapHeight);
      Graphics2D graphics2D = this.currentImage.createGraphics();
      if (!this.getSettings().hasTransparentPageBackground()) {
         graphics2D.setBackground(this.getSettings().getPageBackgroundColor());
         graphics2D.setPaint(this.getSettings().getPageBackgroundColor());
         graphics2D.fillRect(0, 0, bitmapWidth, bitmapHeight);
      }

      graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      if (this.getSettings().isAntiAliasingEnabled() && this.currentImage.getColorModel().getPixelSize() > 1) {
         graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      } else {
         graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
         graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      }

      if (this.getSettings().isQualityRenderingEnabled()) {
         graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      } else {
         graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
      }

      graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      if (offset != null) {
         graphics2D.translate(offset.getX(), offset.getY());
      }

      graphics2D.scale(scale / 1000.0, scale / 1000.0);
      return new Java2DPainter(graphics2D, this.getContext(), this.getFontInfo());
   }

   protected BufferedImage createBufferedImage(int bitmapWidth, int bitmapHeight) {
      return new BufferedImage(bitmapWidth, bitmapHeight, this.getSettings().getBufferedImageType());
   }

   public void endPageContent() throws IFException {
      try {
         if (this.multiImageWriter == null) {
            switch (this.pageCount) {
               case 1:
                  this.imageWriter.writeImage(this.currentImage, this.outputStream, this.getSettings().getWriterParams());
                  IOUtils.closeQuietly(this.outputStream);
                  this.outputStream = null;
                  break;
               default:
                  OutputStream out = this.multiFileUtil.createOutputStream(this.pageCount - 1);
                  if (out == null) {
                     BitmapRendererEventProducer eventProducer = BitmapRendererEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
                     eventProducer.stoppingAfterFirstPageNoFilename(this);
                  } else {
                     try {
                        this.imageWriter.writeImage(this.currentImage, out, this.getSettings().getWriterParams());
                     } finally {
                        IOUtils.closeQuietly(out);
                     }
                  }
            }
         } else {
            this.multiImageWriter.writeImage(this.currentImage, this.getSettings().getWriterParams());
         }

         this.currentImage = null;
      } catch (IOException var7) {
         throw new IFException("I/O error while encoding BufferedImage", var7);
      }
   }

   public void endPage() throws IFException {
      this.currentPageDimensions = null;
   }

   public void handleExtensionObject(Object extension) throws IFException {
      log.debug("Don't know how to handle extension object. Ignoring: " + extension + " (" + extension.getClass().getName() + ")");
   }

   static {
      log = LogFactory.getLog(AbstractBitmapDocumentHandler.class);
   }
}
