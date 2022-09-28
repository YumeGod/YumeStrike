package org.apache.fop.render;

import java.awt.Rectangle;
import java.io.IOException;
import org.apache.xmlgraphics.image.loader.Image;

public interface ImageHandler extends ImageHandlerBase {
   boolean isCompatible(RenderingContext var1, Image var2);

   void handleImage(RenderingContext var1, Image var2, Rectangle var3) throws IOException;
}
