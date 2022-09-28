package org.apache.fop.afp;

import java.awt.geom.AffineTransform;

public class AFPRectanglePainter extends AbstractAFPPainter {
   public AFPRectanglePainter(AFPPaintingState paintingState, DataStream dataStream) {
      super(paintingState, dataStream);
   }

   public void paint(PaintingInfo paintInfo) {
      RectanglePaintingInfo rectanglePaintInfo = (RectanglePaintingInfo)paintInfo;
      int pageWidth = this.dataStream.getCurrentPage().getWidth();
      int pageHeight = this.dataStream.getCurrentPage().getHeight();
      AFPUnitConverter unitConv = this.paintingState.getUnitConverter();
      float width = unitConv.pt2units(rectanglePaintInfo.getWidth());
      float height = unitConv.pt2units(rectanglePaintInfo.getHeight());
      float x = unitConv.pt2units(rectanglePaintInfo.getX());
      float y = unitConv.pt2units(rectanglePaintInfo.getY());
      AffineTransform at = this.paintingState.getData().getTransform();
      AFPLineDataInfo lineDataInfo = new AFPLineDataInfo();
      lineDataInfo.color = this.paintingState.getColor();
      lineDataInfo.rotation = this.paintingState.getRotation();
      lineDataInfo.thickness = Math.round(height);
      switch (lineDataInfo.rotation) {
         case 0:
            lineDataInfo.x1 = Math.round((float)at.getTranslateX() + x);
            lineDataInfo.y1 = lineDataInfo.y2 = Math.round((float)at.getTranslateY() + y);
            lineDataInfo.x2 = Math.round((float)at.getTranslateX() + x + width);
            break;
         case 90:
            lineDataInfo.x1 = Math.round((float)at.getTranslateY() + x);
            lineDataInfo.y1 = lineDataInfo.y2 = pageWidth - Math.round((float)at.getTranslateX()) + Math.round(y);
            lineDataInfo.x2 = Math.round(width + (float)at.getTranslateY() + x);
            break;
         case 180:
            lineDataInfo.x1 = pageWidth - Math.round((float)at.getTranslateX() - x);
            lineDataInfo.y1 = lineDataInfo.y2 = pageHeight - Math.round((float)at.getTranslateY() - y);
            lineDataInfo.x2 = pageWidth - Math.round((float)at.getTranslateX() - x - width);
            break;
         case 270:
            lineDataInfo.x1 = pageHeight - Math.round((float)at.getTranslateY() - x);
            lineDataInfo.y1 = lineDataInfo.y2 = Math.round((float)at.getTranslateX() + y);
            lineDataInfo.x2 = pageHeight - Math.round((float)at.getTranslateY() - x - width);
      }

      this.dataStream.createLine(lineDataInfo);
   }
}
