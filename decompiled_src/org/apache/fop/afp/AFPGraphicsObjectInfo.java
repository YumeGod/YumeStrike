package org.apache.fop.afp;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;

public class AFPGraphicsObjectInfo extends AFPDataObjectInfo {
   private Graphics2DImagePainter painter;
   private Rectangle2D area;
   private AFPGraphics2D g2d;

   public Graphics2DImagePainter getPainter() {
      return this.painter;
   }

   public void setPainter(Graphics2DImagePainter graphicsPainter) {
      this.painter = graphicsPainter;
   }

   public Rectangle2D getArea() {
      AFPObjectAreaInfo objectAreaInfo = this.getObjectAreaInfo();
      int width = objectAreaInfo.getWidth();
      int height = objectAreaInfo.getHeight();
      return new Rectangle(width, height);
   }

   public void setArea(Rectangle2D area) {
      this.area = area;
   }

   public void setGraphics2D(AFPGraphics2D g2d) {
      this.g2d = g2d;
   }

   public AFPGraphics2D getGraphics2D() {
      return this.g2d;
   }

   public String toString() {
      return "GraphicsObjectInfo{" + super.toString() + "}";
   }

   public String getMimeType() {
      return "image/svg+xml";
   }
}
