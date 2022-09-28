package org.apache.fop.render.ps;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.Map;
import org.apache.fop.render.AbstractGraphics2DAdapter;
import org.apache.fop.render.ImageHandlerUtil;
import org.apache.fop.render.RendererContext;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.apache.xmlgraphics.java2d.ps.PSGraphics2D;
import org.apache.xmlgraphics.ps.PSGenerator;

public class PSGraphics2DAdapter extends AbstractGraphics2DAdapter {
   private PSGenerator gen;
   private boolean clip;

   public PSGraphics2DAdapter(PSRenderer renderer) {
      this(renderer.gen, true);
   }

   public PSGraphics2DAdapter(PSGenerator gen, boolean clip) {
      this.clip = true;
      this.gen = gen;
      this.clip = clip;
   }

   public void paintImage(Graphics2DImagePainter painter, RendererContext context, int x, int y, int width, int height) throws IOException {
      float fwidth = (float)width / 1000.0F;
      float fheight = (float)height / 1000.0F;
      float fx = (float)x / 1000.0F;
      float fy = (float)y / 1000.0F;
      Dimension dim = painter.getImageSize();
      float imw = (float)dim.getWidth() / 1000.0F;
      float imh = (float)dim.getHeight() / 1000.0F;
      boolean paintAsBitmap = false;
      if (context != null) {
         Map foreign = (Map)context.getProperty("foreign-attributes");
         paintAsBitmap = foreign != null && ImageHandlerUtil.isConversionModeBitmap(foreign);
      }

      float sx = paintAsBitmap ? 1.0F : fwidth / imw;
      float sy = paintAsBitmap ? 1.0F : fheight / imh;
      this.gen.commentln("%FOPBeginGraphics2D");
      this.gen.saveGraphicsState();
      if (this.clip) {
         this.gen.writeln("newpath");
         this.gen.defineRect((double)fx, (double)fy, (double)fwidth, (double)fheight);
         this.gen.writeln("clip");
      }

      this.gen.concatMatrix((double)sx, 0.0, 0.0, (double)sy, (double)fx, (double)fy);
      boolean textAsShapes = false;
      PSGraphics2D graphics = new PSGraphics2D(false, this.gen);
      graphics.setGraphicContext(new GraphicContext());
      AffineTransform transform = new AffineTransform();
      transform.translate((double)fx, (double)fy);
      this.gen.getCurrentState().concatMatrix(transform);
      if (paintAsBitmap) {
         int resolution = Math.round(context.getUserAgent().getTargetResolution());
         RendererContext.RendererContextWrapper ctx = RendererContext.wrapRendererContext(context);
         BufferedImage bi = this.paintToBufferedImage(painter, ctx, resolution, false, false);
         float scale = 72.0F / context.getUserAgent().getTargetResolution();
         graphics.drawImage(bi, new AffineTransform(scale, 0.0F, 0.0F, scale, 0.0F, 0.0F), (ImageObserver)null);
      } else {
         Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, (double)imw, (double)imh);
         painter.paint(graphics, area);
      }

      this.gen.restoreGraphicsState();
      this.gen.commentln("%FOPEndGraphics2D");
   }
}
