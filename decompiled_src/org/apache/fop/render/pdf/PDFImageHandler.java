package org.apache.fop.render.pdf;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import org.apache.fop.pdf.PDFXObject;
import org.apache.fop.render.ImageHandlerBase;
import org.apache.fop.render.RendererContext;
import org.apache.xmlgraphics.image.loader.Image;

public interface PDFImageHandler extends ImageHandlerBase {
   PDFXObject generateImage(RendererContext var1, Image var2, Point var3, Rectangle var4) throws IOException;
}
