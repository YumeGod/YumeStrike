package org.apache.fop.render.afp;

import org.apache.fop.render.AbstractImageHandlerRegistry;

public class AFPImageHandlerRegistry extends AbstractImageHandlerRegistry {
   public Class getHandlerClass() {
      return AFPImageHandler.class;
   }
}
