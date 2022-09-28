package org.apache.fop.render.intermediate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontEventAdapter;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.fonts.base14.Base14FontCollection;

public abstract class AbstractBinaryWritingIFDocumentHandler extends AbstractIFDocumentHandler {
   protected OutputStream outputStream;
   private boolean ownOutputStream;
   protected FontInfo fontInfo;

   public void setResult(Result result) throws IFException {
      if (result instanceof StreamResult) {
         StreamResult streamResult = (StreamResult)result;
         OutputStream out = streamResult.getOutputStream();
         if (out == null) {
            if (streamResult.getWriter() != null) {
               throw new IllegalArgumentException("FOP cannot use a Writer. Please supply an OutputStream!");
            }

            try {
               URL url = new URL(streamResult.getSystemId());
               File f = FileUtils.toFile(url);
               if (f != null) {
                  out = new FileOutputStream(f);
               } else {
                  out = url.openConnection().getOutputStream();
               }
            } catch (IOException var6) {
               throw new IFException("I/O error while opening output stream", var6);
            }

            out = new BufferedOutputStream((OutputStream)out);
            this.ownOutputStream = true;
         }

         if (out == null) {
            throw new IllegalArgumentException("Need a StreamResult with an OutputStream");
         } else {
            this.outputStream = (OutputStream)out;
         }
      } else {
         throw new UnsupportedOperationException("Unsupported Result subclass: " + result.getClass().getName());
      }
   }

   public FontInfo getFontInfo() {
      return this.fontInfo;
   }

   public void setFontInfo(FontInfo fontInfo) {
      this.fontInfo = fontInfo;
   }

   public void setDefaultFontInfo(FontInfo fontInfo) {
      FontManager fontManager = this.getUserAgent().getFactory().getFontManager();
      FontCollection[] fontCollections = new FontCollection[]{new Base14FontCollection(fontManager.isBase14KerningEnabled())};
      FontInfo fi = fontInfo != null ? fontInfo : new FontInfo();
      fi.setEventListener(new FontEventAdapter(this.getUserAgent().getEventBroadcaster()));
      fontManager.setup(fi, fontCollections);
      this.setFontInfo(fi);
   }

   public void startDocument() throws IFException {
      super.startDocument();
      if (this.outputStream == null) {
         throw new IllegalStateException("OutputStream hasn't been set through setResult()");
      }
   }

   public void endDocument() throws IFException {
      if (this.ownOutputStream) {
         IOUtils.closeQuietly(this.outputStream);
         this.outputStream = null;
      }

   }
}
