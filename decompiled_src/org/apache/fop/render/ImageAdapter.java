package org.apache.fop.render;

import java.awt.image.RenderedImage;
import java.io.IOException;

public interface ImageAdapter {
   void paintImage(RenderedImage var1, RendererContext var2, int var3, int var4, int var5, int var6) throws IOException;
}
