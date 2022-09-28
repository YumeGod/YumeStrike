package org.apache.fop.render.bitmap;

import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;

public class PNGDocumentHandler extends AbstractBitmapDocumentHandler {
   public String getMimeType() {
      return "image/png";
   }

   public String getDefaultExtension() {
      return "png";
   }

   public IFDocumentHandlerConfigurator getConfigurator() {
      return new BitmapRendererConfigurator(this.getUserAgent());
   }
}
