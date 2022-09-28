package org.apache.fop.render.pdf;

import org.apache.fop.render.AbstractImageHandlerRegistry;

public class PDFImageHandlerRegistry extends AbstractImageHandlerRegistry {
   public Class getHandlerClass() {
      return PDFImageHandler.class;
   }
}
