package org.apache.fop.render.pdf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import org.apache.fop.render.AbstractGraphics2DAdapter;
import org.apache.fop.render.RendererContext;
import org.apache.fop.svg.PDFGraphics2D;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;

public class PDFGraphics2DAdapter extends AbstractGraphics2DAdapter {
   private PDFRenderer renderer;

   public PDFGraphics2DAdapter(PDFRenderer renderer) {
      this.renderer = renderer;
   }

   public void paintImage(Graphics2DImagePainter painter, RendererContext context, int x, int y, int width, int height) throws IOException {
      PDFContentGenerator generator = this.renderer.getGenerator();
      PDFSVGHandler.PDFInfo pdfInfo = PDFSVGHandler.getPDFInfo(context);
      float fwidth = (float)width / 1000.0F;
      float fheight = (float)height / 1000.0F;
      float fx = (float)x / 1000.0F;
      float fy = (float)y / 1000.0F;
      Dimension dim = painter.getImageSize();
      float imw = (float)dim.getWidth() / 1000.0F;
      float imh = (float)dim.getHeight() / 1000.0F;
      float sx = pdfInfo.paintAsBitmap ? 1.0F : fwidth / imw;
      float sy = pdfInfo.paintAsBitmap ? 1.0F : fheight / imh;
      generator.comment("G2D start");
      generator.saveGraphicsState();
      generator.updateColor(Color.black, false, (StringBuffer)null);
      generator.updateColor(Color.black, true, (StringBuffer)null);
      generator.add(sx + " 0 0 " + sy + " " + fx + " " + fy + " cm\n");
      boolean textAsShapes = false;
      if (pdfInfo.pdfContext == null) {
         pdfInfo.pdfContext = pdfInfo.pdfPage;
      }

      PDFGraphics2D graphics = new PDFGraphics2D(false, pdfInfo.fi, pdfInfo.pdfDoc, pdfInfo.pdfContext, pdfInfo.pdfPage.referencePDF(), pdfInfo.currentFontName, (float)pdfInfo.currentFontSize);
      graphics.setGraphicContext(new GraphicContext());
      AffineTransform transform = new AffineTransform();
      transform.translate((double)fx, (double)fy);
      generator.getState().concatenate(transform);
      graphics.setPaintingState(generator.getState());
      graphics.setOutputStream(pdfInfo.outputStream);
      if (pdfInfo.paintAsBitmap) {
         int resolution = Math.round(context.getUserAgent().getTargetResolution());
         RendererContext.RendererContextWrapper ctx = RendererContext.wrapRendererContext(context);
         BufferedImage bi = this.paintToBufferedImage(painter, ctx, resolution, false, false);
         float scale = 72.0F / context.getUserAgent().getTargetResolution();
         graphics.drawImage(bi, new AffineTransform(scale, 0.0F, 0.0F, scale, 0.0F, 0.0F), (ImageObserver)null);
      } else {
         Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, (double)imw, (double)imh);
         painter.paint(graphics, area);
      }

      generator.add(graphics.getString());
      generator.restoreGraphicsState();
      generator.comment("G2D end");
   }

   protected void setRenderingHintsForBufferedImage(Graphics2D g2d) {
      super.setRenderingHintsForBufferedImage(g2d);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
   }
}
