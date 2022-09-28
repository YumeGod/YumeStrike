package org.apache.fop.render;

import java.io.IOException;

public interface Graphics2DAdapter {
   void paintImage(org.apache.xmlgraphics.java2d.Graphics2DImagePainter var1, RendererContext var2, int var3, int var4, int var5, int var6) throws IOException;

   /** @deprecated */
   void paintImage(Graphics2DImagePainter var1, RendererContext var2, int var3, int var4, int var5, int var6) throws IOException;
}
