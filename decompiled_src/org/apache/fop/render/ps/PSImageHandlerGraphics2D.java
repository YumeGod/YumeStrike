package org.apache.fop.render.ps;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.impl.ImageGraphics2D;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.apache.xmlgraphics.java2d.ps.PSGraphics2D;
import org.apache.xmlgraphics.ps.FormGenerator;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSProcSets;

public class PSImageHandlerGraphics2D implements PSImageHandler {
   private static final ImageFlavor[] FLAVORS;

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      PSRenderingContext psContext = (PSRenderingContext)context;
      PSGenerator gen = psContext.getGenerator();
      ImageGraphics2D imageG2D = (ImageGraphics2D)image;
      Graphics2DImagePainter painter = imageG2D.getGraphics2DImagePainter();
      float fx = (float)pos.getX() / 1000.0F;
      float fy = (float)pos.getY() / 1000.0F;
      float fwidth = (float)pos.getWidth() / 1000.0F;
      float fheight = (float)pos.getHeight() / 1000.0F;
      Dimension dim = painter.getImageSize();
      float imw = (float)dim.getWidth() / 1000.0F;
      float imh = (float)dim.getHeight() / 1000.0F;
      float sx = fwidth / imw;
      float sy = fheight / imh;
      gen.commentln("%FOPBeginGraphics2D");
      gen.saveGraphicsState();
      boolean clip = false;
      gen.concatMatrix((double)sx, 0.0, 0.0, (double)sy, (double)fx, (double)fy);
      boolean textAsShapes = false;
      PSGraphics2D graphics = new PSGraphics2D(false, gen);
      graphics.setGraphicContext(new GraphicContext());
      AffineTransform transform = new AffineTransform();
      transform.translate((double)fx, (double)fy);
      gen.getCurrentState().concatMatrix(transform);
      Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, (double)imw, (double)imh);
      painter.paint(graphics, area);
      gen.restoreGraphicsState();
      gen.commentln("%FOPEndGraphics2D");
   }

   public void generateForm(RenderingContext context, Image image, PSImageFormResource form) throws IOException {
      PSRenderingContext psContext = (PSRenderingContext)context;
      PSGenerator gen = psContext.getGenerator();
      final ImageGraphics2D imageG2D = (ImageGraphics2D)image;
      ImageInfo info = image.getInfo();
      String imageDescription = info.getMimeType() + " " + info.getOriginalURI();
      Dimension2D dimensionsPt = info.getSize().getDimensionPt();
      final Dimension2D dimensionsMpt = info.getSize().getDimensionMpt();
      FormGenerator formGen = new FormGenerator(form.getName(), imageDescription, dimensionsPt) {
         protected void generatePaintProc(PSGenerator gen) throws IOException {
            gen.getResourceTracker().notifyResourceUsageOnPage(PSProcSets.EPS_PROCSET);
            gen.writeln("BeginEPSF");
            PSGraphics2DAdapter adapter = new PSGraphics2DAdapter(gen, false);
            adapter.paintImage(imageG2D.getGraphics2DImagePainter(), (RendererContext)null, 0, 0, (int)Math.round(dimensionsMpt.getWidth()), (int)Math.round(dimensionsMpt.getHeight()));
            gen.writeln("EndEPSF");
         }
      };
      formGen.generate(gen);
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
      if (!(targetContext instanceof PSRenderingContext)) {
         return false;
      } else {
         return image == null || image instanceof ImageGraphics2D;
      }
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.GRAPHICS2D};
   }
}
