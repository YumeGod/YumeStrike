package org.apache.fop.render.bitmap;

import java.awt.image.BufferedImage;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.java2d.Java2DRenderer;
import org.apache.xmlgraphics.image.GraphicsUtil;
import org.apache.xmlgraphics.image.rendered.FormatRed;
import org.apache.xmlgraphics.image.writer.ImageWriter;
import org.apache.xmlgraphics.image.writer.ImageWriterParams;
import org.apache.xmlgraphics.image.writer.ImageWriterRegistry;
import org.apache.xmlgraphics.image.writer.MultiImageWriter;

public class TIFFRenderer extends Java2DRenderer implements TIFFConstants {
   private ImageWriterParams writerParams = new ImageWriterParams();
   private int bufferedImageType = 2;
   private OutputStream outputStream;

   public String getMimeType() {
      return "image/tiff";
   }

   public TIFFRenderer() {
      this.writerParams.setCompressionMethod("PackBits");
   }

   public void setUserAgent(FOUserAgent foUserAgent) {
      super.setUserAgent(foUserAgent);
      int dpi = Math.round(this.userAgent.getTargetResolution());
      this.writerParams.setResolution(dpi);
   }

   public void startRenderer(OutputStream outputStream) throws IOException {
      this.outputStream = outputStream;
      super.startRenderer(outputStream);
   }

   public void stopRenderer() throws IOException {
      super.stopRenderer();
      log.debug("Starting TIFF encoding ...");
      Iterator pageImagesItr = new LazyPageImagesIterator(this.getNumberOfPages(), log);
      ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor(this.getMimeType());
      BitmapRendererEventProducer eventProducer;
      if (writer == null) {
         eventProducer = BitmapRendererEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.noImageWriterFound(this, this.getMimeType());
      }

      if (writer.supportsMultiImageWriter()) {
         MultiImageWriter multiWriter = writer.createMultiImageWriter(this.outputStream);

         try {
            while(pageImagesItr.hasNext()) {
               RenderedImage img = (RenderedImage)pageImagesItr.next();
               multiWriter.writeImage(img, this.writerParams);
            }
         } finally {
            multiWriter.close();
         }
      } else {
         writer.writeImage((RenderedImage)pageImagesItr.next(), this.outputStream, this.writerParams);
         if (pageImagesItr.hasNext()) {
            eventProducer = BitmapRendererEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.stoppingAfterFirstPageNoFilename(this);
         }
      }

      this.outputStream.flush();
      this.clearViewportList();
      log.debug("TIFF encoding done.");
   }

   protected BufferedImage getBufferedImage(int bitmapWidth, int bitmapHeight) {
      return new BufferedImage(bitmapWidth, bitmapHeight, this.bufferedImageType);
   }

   public void setBufferedImageType(int bufferedImageType) {
      this.bufferedImageType = bufferedImageType;
   }

   public ImageWriterParams getWriterParams() {
      return this.writerParams;
   }

   private class LazyPageImagesIterator implements Iterator {
      private Log log;
      private int count;
      private int current = 0;

      public LazyPageImagesIterator(int c, Log log) {
         this.count = c;
         this.log = log;
      }

      public boolean hasNext() {
         return this.current < this.count;
      }

      public Object next() {
         if (this.log.isDebugEnabled()) {
            this.log.debug("[" + (this.current + 1) + "]");
         }

         BufferedImage pageImage = null;

         try {
            pageImage = TIFFRenderer.this.getPageImage(this.current++);
         } catch (FOPException var9) {
            this.log.error(var9);
            return null;
         }

         if (!"CCITT T.4".equalsIgnoreCase(TIFFRenderer.this.writerParams.getCompressionMethod()) && !"CCITT T.6".equalsIgnoreCase(TIFFRenderer.this.writerParams.getCompressionMethod())) {
            SinglePixelPackedSampleModel sppsm = (SinglePixelPackedSampleModel)pageImage.getSampleModel();
            int bands = sppsm.getNumBands();
            int[] off = new int[bands];
            int w = pageImage.getWidth();
            int h = pageImage.getHeight();

            for(int i = 0; i < bands; off[i] = i++) {
            }

            SampleModel sm = new PixelInterleavedSampleModel(0, w, h, bands, w * bands, off);
            RenderedImage rimg = new FormatRed(GraphicsUtil.wrap(pageImage), sm);
            return rimg;
         } else {
            return pageImage;
         }
      }

      public void remove() {
         throw new UnsupportedOperationException("Method 'remove' is not supported.");
      }
   }
}
