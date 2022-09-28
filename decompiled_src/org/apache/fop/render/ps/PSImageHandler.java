package org.apache.fop.render.ps;

import java.io.IOException;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.Image;

public interface PSImageHandler extends ImageHandler {
   void generateForm(RenderingContext var1, Image var2, PSImageFormResource var3) throws IOException;
}
