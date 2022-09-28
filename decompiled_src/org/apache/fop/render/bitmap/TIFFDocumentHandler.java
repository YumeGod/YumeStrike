package org.apache.fop.render.bitmap;

import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;

public class TIFFDocumentHandler extends AbstractBitmapDocumentHandler {
   public String getMimeType() {
      return "image/tiff";
   }

   public String getDefaultExtension() {
      return "tif";
   }

   public IFDocumentHandlerConfigurator getConfigurator() {
      return new TIFFRendererConfigurator(this.getUserAgent());
   }
}
