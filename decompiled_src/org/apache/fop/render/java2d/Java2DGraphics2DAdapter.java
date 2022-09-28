package org.apache.fop.render.java2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import org.apache.fop.render.AbstractGraphics2DAdapter;
import org.apache.fop.render.RendererContext;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;

public class Java2DGraphics2DAdapter extends AbstractGraphics2DAdapter {
   public void paintImage(Graphics2DImagePainter painter, RendererContext context, int x, int y, int width, int height) throws IOException {
      float fwidth = (float)width / 1000.0F;
      float fheight = (float)height / 1000.0F;
      float fx = (float)x / 1000.0F;
      float fy = (float)y / 1000.0F;
      Dimension dim = painter.getImageSize();
      float imw = (float)dim.getWidth() / 1000.0F;
      float imh = (float)dim.getHeight() / 1000.0F;
      float sx = fwidth / imw;
      float sy = fheight / imh;
      Java2DRenderer renderer = (Java2DRenderer)context.getRenderer();
      Java2DGraphicsState state = renderer.state;
      Graphics2D g2d = (Graphics2D)state.getGraph().create();
      g2d.setColor(Color.black);
      g2d.setBackground(Color.black);
      g2d.translate((double)fx, (double)fy);
      AffineTransform at = AffineTransform.getScaleInstance((double)sx, (double)sy);
      if (!at.isIdentity()) {
         g2d.transform(at);
      }

      Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, (double)imw, (double)imh);
      painter.paint(g2d, area);
      g2d.dispose();
   }
}
