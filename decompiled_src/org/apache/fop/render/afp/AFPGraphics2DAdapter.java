package org.apache.fop.render.afp;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import org.apache.fop.afp.AFPGraphics2D;
import org.apache.fop.afp.AFPGraphicsObjectInfo;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPResourceManager;
import org.apache.fop.render.AbstractGraphics2DAdapter;
import org.apache.fop.render.RendererContext;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;

public class AFPGraphics2DAdapter extends AbstractGraphics2DAdapter {
   private final AFPPaintingState paintingState;

   public AFPGraphics2DAdapter(AFPPaintingState paintingState) {
      this.paintingState = paintingState;
   }

   public void paintImage(Graphics2DImagePainter painter, RendererContext rendererContext, int x, int y, int width, int height) throws IOException {
      AFPRendererContext afpRendererContext = (AFPRendererContext)rendererContext;
      AFPInfo afpInfo = afpRendererContext.getInfo();
      boolean textAsShapes = false;
      AFPGraphics2D g2d = afpInfo.createGraphics2D(false);
      this.paintingState.save();
      if (afpInfo.paintAsBitmap()) {
         RendererContext.RendererContextWrapper rendererContextWrapper = RendererContext.wrapRendererContext(rendererContext);
         float targetResolution = rendererContext.getUserAgent().getTargetResolution();
         int resolution = Math.round(targetResolution);
         boolean colorImages = afpInfo.isColorSupported();
         BufferedImage bufferedImage = this.paintToBufferedImage(painter, rendererContextWrapper, resolution, !colorImages, false);
         AffineTransform at = this.paintingState.getData().getTransform();
         at.translate((double)x, (double)y);
         g2d.drawImage(bufferedImage, at, (ImageObserver)null);
      } else {
         AFPGraphicsObjectInfo graphicsObjectInfo = new AFPGraphicsObjectInfo();
         graphicsObjectInfo.setPainter(painter);
         graphicsObjectInfo.setGraphics2D(g2d);
         Dimension imageSize = painter.getImageSize();
         float imw = (float)imageSize.getWidth() / 1000.0F;
         float imh = (float)imageSize.getHeight() / 1000.0F;
         Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, (double)imw, (double)imh);
         graphicsObjectInfo.setArea(area);
         AFPResourceManager resourceManager = afpInfo.getResourceManager();
         resourceManager.createObject(graphicsObjectInfo);
      }

      this.paintingState.restore();
   }

   protected int mpt2px(int unit, int resolution) {
      return Math.round(this.paintingState.getUnitConverter().mpt2units((float)unit));
   }
}
