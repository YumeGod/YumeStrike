package org.apache.fop.render.pcl;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.render.AbstractGraphics2DAdapter;
import org.apache.fop.render.RendererContext;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.apache.xmlgraphics.util.UnitConv;

public class PCLGraphics2DAdapter extends AbstractGraphics2DAdapter {
   private static Log log;

   public void paintImage(Graphics2DImagePainter painter, RendererContext context, int x, int y, int width, int height) throws IOException {
      PCLRendererContext pclContext = PCLRendererContext.wrapRendererContext(context);
      PCLRenderer pcl = (PCLRenderer)context.getRenderer();
      PCLGenerator gen = pcl.gen;
      Dimension dim = painter.getImageSize();
      float imw = (float)dim.getWidth();
      float imh = (float)dim.getHeight();
      boolean painted = false;
      boolean paintAsBitmap = pclContext.paintAsBitmap();
      if (!paintAsBitmap) {
         ByteArrayOutputStream baout = new ByteArrayOutputStream();
         PCLGenerator tempGen = new PCLGenerator(baout, gen.getMaximumBitmapResolution());

         try {
            GraphicContext ctx = (GraphicContext)pcl.getGraphicContext().clone();
            AffineTransform prepareHPGL2 = new AffineTransform();
            prepareHPGL2.scale(0.001, 0.001);
            ctx.setTransform(prepareHPGL2);
            PCLGraphics2D graphics = new PCLGraphics2D(tempGen);
            graphics.setGraphicContext(ctx);
            graphics.setClippingDisabled(pclContext.isClippingDisabled());
            Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, (double)imw, (double)imh);
            painter.paint(graphics, area);
            pcl.saveGraphicsState();
            pcl.setCursorPos((float)x, (float)y);
            gen.writeCommand("*c" + gen.formatDouble4((double)((float)width / 100.0F)) + "x" + gen.formatDouble4((double)((float)height / 100.0F)) + "Y");
            gen.writeCommand("*c0T");
            gen.enterHPGL2Mode(false);
            gen.writeText("\nIN;");
            gen.writeText("SP1;");
            double scale = (double)imw / UnitConv.mm2pt((double)imw * 0.025);
            gen.writeText("SC0," + gen.formatDouble4(scale) + ",0,-" + gen.formatDouble4(scale) + ",2;");
            gen.writeText("IR0,100,0,100;");
            gen.writeText("PU;PA0,0;\n");
            baout.writeTo(gen.getOutputStream());
            gen.writeText("\n");
            gen.enterPCLMode(false);
            pcl.restoreGraphicsState();
            painted = true;
         } catch (UnsupportedOperationException var23) {
            log.debug("Cannot paint graphic natively. Falling back to bitmap painting. Reason: " + var23.getMessage());
         }
      }

      if (!painted) {
         int resolution = Math.round(context.getUserAgent().getTargetResolution());
         BufferedImage bi = this.paintToBufferedImage(painter, pclContext, resolution, !pclContext.isColorCanvas(), false);
         pcl.setCursorPos((float)x, (float)y);
         gen.paintBitmap(bi, new Dimension(width, height), pclContext.isSourceTransparency());
      }

   }

   static {
      log = LogFactory.getLog(PCLGraphics2DAdapter.class);
   }
}
