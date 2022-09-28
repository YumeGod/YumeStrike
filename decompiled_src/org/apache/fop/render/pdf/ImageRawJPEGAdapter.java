package org.apache.fop.render.pdf;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.fop.pdf.DCTFilter;
import org.apache.fop.pdf.PDFDeviceColorSpace;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFFilter;
import org.apache.xmlgraphics.image.loader.impl.ImageRawJPEG;
import org.apache.xmlgraphics.image.loader.impl.JPEGFile;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;

public class ImageRawJPEGAdapter extends AbstractImageAdapter {
   private PDFFilter pdfFilter = null;

   public ImageRawJPEGAdapter(ImageRawJPEG image, String key) {
      super(image, key);
   }

   public ImageRawJPEG getImage() {
      return (ImageRawJPEG)this.image;
   }

   public void setup(PDFDocument doc) {
      this.pdfFilter = new DCTFilter();
      this.pdfFilter.setApplied(true);
      super.setup(doc);
   }

   public PDFDeviceColorSpace getColorSpace() {
      return toPDFColorSpace(this.getImageColorSpace());
   }

   public int getBitsPerComponent() {
      return 8;
   }

   public boolean isInverted() {
      return this.getImage().isInverted();
   }

   public PDFFilter getPDFFilter() {
      return this.pdfFilter;
   }

   public void outputContents(OutputStream out) throws IOException {
      InputStream in = this.getImage().createInputStream();
      in = ImageUtil.decorateMarkSupported(in);

      try {
         JPEGFile jpeg = new JPEGFile(in);
         DataInput din = jpeg.getDataInput();

         while(true) {
            while(true) {
               int segID = jpeg.readMarkerSegment();
               int reclen;
               switch (segID) {
                  case 216:
                     out.write(255);
                     out.write(segID);
                     break;
                  case 217:
                  case 218:
                     out.write(255);
                     out.write(segID);
                     IOUtils.copy(in, out);
                     return;
                  case 226:
                     boolean skipICCProfile = false;
                     in.mark(16);

                     try {
                        reclen = jpeg.readSegmentLength();
                        byte[] iccString = new byte[11];
                        din.readFully(iccString);
                        din.skipBytes(1);
                        if ("ICC_PROFILE".equals(new String(iccString, "US-ASCII"))) {
                           skipICCProfile = this.image.getICCProfile() != null;
                        }
                     } finally {
                        in.reset();
                     }

                     if (skipICCProfile) {
                        jpeg.skipCurrentMarkerSegment();
                        break;
                     }
                  default:
                     out.write(255);
                     out.write(segID);
                     reclen = jpeg.readSegmentLength();
                     out.write(reclen >>> 8 & 255);
                     out.write(reclen >>> 0 & 255);
                     int left = reclen - 2;

                     int part;
                     for(byte[] buf = new byte[2048]; left > 0; left -= part) {
                        part = Math.min(buf.length, left);
                        din.readFully(buf, 0, part);
                        out.write(buf, 0, part);
                     }
               }
            }
         }
      } finally {
         IOUtils.closeQuietly(in);
      }
   }

   public String getFilterHint() {
      return "jpeg";
   }
}
