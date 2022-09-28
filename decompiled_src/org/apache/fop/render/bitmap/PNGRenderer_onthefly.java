package org.apache.fop.render.bitmap;

import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.area.PageViewport;
import org.apache.fop.render.java2d.Java2DRenderer;
import org.apache.xmlgraphics.image.codec.png.PNGEncodeParam;
import org.apache.xmlgraphics.image.codec.png.PNGImageEncoder;

public class PNGRenderer_onthefly extends Java2DRenderer {
   public static final String MIME_TYPE = "image/png";
   private String fileSyntax;
   private File outputDir;
   private PNGEncodeParam renderParams;
   private OutputStream firstOutputStream;

   public String getMimeType() {
      return "image/png";
   }

   public boolean supportsOutOfOrder() {
      return true;
   }

   public void startRenderer(OutputStream outputStream) throws IOException {
      log.info("rendering areas to PNG");
      this.setOutputDirectory();
      this.firstOutputStream = outputStream;
   }

   private void setOutputDirectory() {
      File f = this.getUserAgent().getOutputFile();
      this.outputDir = f.getParentFile();
      String s = f.getName();
      int i = s.lastIndexOf(".");
      if (s.charAt(i - 1) == '1') {
         --i;
      }

      this.fileSyntax = s.substring(0, i);
   }

   public void renderPage(PageViewport pageViewport) throws IOException {
      RenderedImage image = this.getPageImage(pageViewport);
      log.debug("Encoding page" + (this.getCurrentPageNumber() + 1));
      this.renderParams = PNGEncodeParam.getDefaultEncodeParam(image);
      OutputStream os = this.getCurrentOutputStream(this.getCurrentPageNumber());
      if (os != null) {
         try {
            PNGImageEncoder encoder = new PNGImageEncoder(os, this.renderParams);
            encoder.encode(image);
         } finally {
            if (os != this.firstOutputStream) {
               IOUtils.closeQuietly(os);
            }

         }
      }

      this.setCurrentPageNumber(this.getCurrentPageNumber() + 1);
   }

   private OutputStream getCurrentOutputStream(int pageNumber) {
      if (pageNumber == 0) {
         return this.firstOutputStream;
      } else {
         File f = new File(this.outputDir + File.separator + this.fileSyntax + (pageNumber + 1) + ".png");

         try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
            return os;
         } catch (FileNotFoundException var4) {
            new FOPException("Can't build the OutputStream\n" + var4);
            return null;
         }
      }
   }
}
