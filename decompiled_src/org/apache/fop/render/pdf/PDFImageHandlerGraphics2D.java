package org.apache.fop.render.pdf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import org.apache.fop.pdf.PDFXObject;
import org.apache.fop.render.AbstractImageHandlerGraphics2D;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.svg.PDFGraphics2D;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.ImageGraphics2D;
import org.apache.xmlgraphics.java2d.GraphicContext;

public class PDFImageHandlerGraphics2D extends AbstractImageHandlerGraphics2D implements PDFImageHandler {
   private static final ImageFlavor[] FLAVORS;

   public PDFXObject generateImage(RendererContext context, Image image, Point origin, Rectangle pos) throws IOException {
      PDFRenderer renderer = (PDFRenderer)context.getRenderer();
      PDFRenderingContext pdfContext = new PDFRenderingContext(context.getUserAgent(), renderer.getGenerator(), renderer.currentPage, renderer.getFontInfo());
      Rectangle effPos = new Rectangle(origin.x + pos.x, origin.y + pos.y, pos.width, pos.height);
      if (context.getUserAgent().isAccessibilityEnabled()) {
         pdfContext.setMarkedContentInfo(renderer.addCurrentImageToStructureTree());
      }

      this.handleImage(pdfContext, image, effPos);
      return null;
   }

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      PDFRenderingContext pdfContext = (PDFRenderingContext)context;
      PDFContentGenerator generator = pdfContext.getGenerator();
      ImageGraphics2D imageG2D = (ImageGraphics2D)image;
      float fwidth = (float)pos.width / 1000.0F;
      float fheight = (float)pos.height / 1000.0F;
      float fx = (float)pos.x / 1000.0F;
      float fy = (float)pos.y / 1000.0F;
      Dimension dim = image.getInfo().getSize().getDimensionMpt();
      float imw = (float)dim.getWidth() / 1000.0F;
      float imh = (float)dim.getHeight() / 1000.0F;
      float sx = fwidth / imw;
      float sy = fheight / imh;
      generator.comment("G2D start");
      boolean accessibilityEnabled = context.getUserAgent().isAccessibilityEnabled();
      if (accessibilityEnabled) {
         PDFLogicalStructureHandler.MarkedContentInfo mci = pdfContext.getMarkedContentInfo();
         generator.saveGraphicsState(mci.tag, mci.mcid);
      } else {
         generator.saveGraphicsState();
      }

      generator.updateColor(Color.black, false, (StringBuffer)null);
      generator.updateColor(Color.black, true, (StringBuffer)null);
      generator.add(sx + " 0 0 " + sy + " " + fx + " " + fy + " cm\n");
      boolean textAsShapes = false;
      PDFGraphics2D graphics = new PDFGraphics2D(false, pdfContext.getFontInfo(), generator.getDocument(), generator.getResourceContext(), pdfContext.getPage().referencePDF(), "", 0.0F);
      graphics.setGraphicContext(new GraphicContext());
      AffineTransform transform = new AffineTransform();
      transform.translate((double)fx, (double)fy);
      generator.getState().concatenate(transform);
      graphics.setPaintingState(generator.getState());
      graphics.setOutputStream(generator.getOutputStream());
      Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, (double)imw, (double)imh);
      imageG2D.getGraphics2DImagePainter().paint(graphics, area);
      generator.add(graphics.getString());
      if (accessibilityEnabled) {
         generator.restoreGraphicsStateAccess();
      } else {
         generator.restoreGraphicsState();
      }

      generator.comment("G2D end");
   }

   public int getPriority() {
      return 200;
   }

   public Class getSupportedImageClass() {
      return ImageGraphics2D.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return FLAVORS;
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      return (image == null || image instanceof ImageGraphics2D) && targetContext instanceof PDFRenderingContext;
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.GRAPHICS2D};
   }
}
