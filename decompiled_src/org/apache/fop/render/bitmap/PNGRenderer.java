package org.apache.fop.render.bitmap;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.fop.area.PageViewport;
import org.apache.fop.render.java2d.Java2DRenderer;
import org.apache.xmlgraphics.image.writer.ImageWriter;
import org.apache.xmlgraphics.image.writer.ImageWriterParams;
import org.apache.xmlgraphics.image.writer.ImageWriterRegistry;

public class PNGRenderer extends Java2DRenderer {
   public static final String MIME_TYPE = "image/png";
   private static final String PNG_FILE_EXTENSION = "png";
   private OutputStream firstOutputStream;
   private MultiFileRenderingUtil multiFileUtil;

   public String getMimeType() {
      return "image/png";
   }

   public void startRenderer(OutputStream outputStream) throws IOException {
      log.info("rendering areas to PNG");
      this.multiFileUtil = new MultiFileRenderingUtil("png", this.getUserAgent().getOutputFile());
      this.firstOutputStream = outputStream;
   }

   public void stopRenderer() throws IOException {
      super.stopRenderer();

      for(int i = 0; i < this.pageViewportList.size(); ++i) {
         OutputStream os = this.getCurrentOutputStream(i);
         if (os == null) {
            BitmapRendererEventProducer eventProducer = BitmapRendererEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.stoppingAfterFirstPageNoFilename(this);
            break;
         }

         try {
            PageViewport pv = (PageViewport)this.pageViewportList.get(i);
            RenderedImage image = this.getPageImage(pv);
            if (log.isDebugEnabled()) {
               log.debug("Encoding page " + (i + 1));
            }

            this.writeImage(os, image);
         } finally {
            if (os != this.firstOutputStream) {
               IOUtils.closeQuietly(os);
            }

         }
      }

   }

   private void writeImage(OutputStream os, RenderedImage image) throws IOException {
      ImageWriterParams params = new ImageWriterParams();
      params.setResolution(Math.round(this.userAgent.getTargetResolution()));
      ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor(this.getMimeType());
      if (writer == null) {
         BitmapRendererEventProducer eventProducer = BitmapRendererEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.noImageWriterFound(this, this.getMimeType());
      }

      if (log.isDebugEnabled()) {
         log.debug("Writing image using " + writer.getClass().getName());
      }

      writer.writeImage(image, os, params);
   }

   protected OutputStream getCurrentOutputStream(int pageNumber) throws IOException {
      return pageNumber == 0 ? this.firstOutputStream : this.multiFileUtil.createOutputStream(pageNumber);
   }
}
