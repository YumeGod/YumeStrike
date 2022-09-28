package org.apache.fop.afp;

import java.awt.geom.AffineTransform;
import org.apache.fop.util.ColorUtil;

public class AFPBorderPainter extends AbstractAFPPainter {
   public AFPBorderPainter(AFPPaintingState paintingState, DataStream dataStream) {
      super(paintingState, dataStream);
   }

   public void paint(PaintingInfo paintInfo) {
      BorderPaintingInfo borderPaintInfo = (BorderPaintingInfo)paintInfo;
      float w = borderPaintInfo.getX2() - borderPaintInfo.getX1();
      float h = borderPaintInfo.getY2() - borderPaintInfo.getY1();
      if (!(w < 0.0F) && !(h < 0.0F)) {
         int pageWidth = this.dataStream.getCurrentPage().getWidth();
         int pageHeight = this.dataStream.getCurrentPage().getHeight();
         AFPUnitConverter unitConv = this.paintingState.getUnitConverter();
         AffineTransform at = this.paintingState.getData().getTransform();
         float x1 = unitConv.pt2units(borderPaintInfo.getX1());
         float y1 = unitConv.pt2units(borderPaintInfo.getY1());
         float x2 = unitConv.pt2units(borderPaintInfo.getX2());
         float y2 = unitConv.pt2units(borderPaintInfo.getY2());
         switch (this.paintingState.getRotation()) {
            case 0:
               x1 = (float)((double)x1 + at.getTranslateX());
               y1 = (float)((double)y1 + at.getTranslateY());
               x2 = (float)((double)x2 + at.getTranslateX());
               y2 = (float)((double)y2 + at.getTranslateY());
               break;
            case 90:
               x1 = (float)((double)x1 + at.getTranslateY());
               y1 += (float)((double)pageWidth - at.getTranslateX());
               x2 = (float)((double)x2 + at.getTranslateY());
               y2 += (float)((double)pageWidth - at.getTranslateX());
               break;
            case 180:
               x1 += (float)((double)pageWidth - at.getTranslateX());
               y1 += (float)((double)pageHeight - at.getTranslateY());
               x2 += (float)((double)pageWidth - at.getTranslateX());
               y2 += (float)((double)pageHeight - at.getTranslateY());
               break;
            case 270:
               x1 = (float)((double)pageHeight - at.getTranslateY());
               y1 += (float)at.getTranslateX();
               x2 += x1;
               y2 += (float)at.getTranslateX();
         }

         AFPLineDataInfo lineDataInfo = new AFPLineDataInfo();
         lineDataInfo.setColor(borderPaintInfo.getColor());
         lineDataInfo.setRotation(this.paintingState.getRotation());
         lineDataInfo.x1 = Math.round(x1);
         lineDataInfo.y1 = Math.round(y1);
         float thickness;
         if (borderPaintInfo.isHorizontal()) {
            thickness = y2 - y1;
         } else {
            thickness = x2 - x1;
         }

         lineDataInfo.setThickness(Math.round(thickness));
         int thick;
         int ex2;
         switch (borderPaintInfo.getStyle()) {
            case 31:
               thick = lineDataInfo.thickness * 3;
               if (borderPaintInfo.isHorizontal()) {
                  lineDataInfo.x2 = lineDataInfo.x1 + thick;
                  lineDataInfo.y2 = lineDataInfo.y1;

                  for(ex2 = Math.round(x2); lineDataInfo.x1 + thick < ex2; lineDataInfo.x2 = lineDataInfo.x1 + thick) {
                     this.dataStream.createLine(lineDataInfo);
                     lineDataInfo.x1 += 2 * thick;
                  }

                  return;
               } else {
                  lineDataInfo.x2 = lineDataInfo.x1;
                  lineDataInfo.y2 = lineDataInfo.y1 + thick;

                  for(ex2 = Math.round(y2); lineDataInfo.y1 + thick < ex2; lineDataInfo.y2 = lineDataInfo.y1 + thick) {
                     this.dataStream.createLine(lineDataInfo);
                     lineDataInfo.y1 += 2 * thick;
                  }

                  return;
               }
            case 36:
               if (borderPaintInfo.isHorizontal()) {
                  lineDataInfo.x2 = lineDataInfo.x1 + lineDataInfo.thickness;
                  lineDataInfo.y2 = lineDataInfo.y1;

                  for(ex2 = Math.round(x2); lineDataInfo.x1 + lineDataInfo.thickness < ex2; lineDataInfo.x2 = lineDataInfo.x1 + lineDataInfo.thickness) {
                     this.dataStream.createLine(lineDataInfo);
                     lineDataInfo.x1 += 3 * lineDataInfo.thickness;
                  }

                  return;
               } else {
                  lineDataInfo.x2 = lineDataInfo.x1;
                  lineDataInfo.y2 = lineDataInfo.y1 + lineDataInfo.thickness;

                  for(ex2 = Math.round(y2); lineDataInfo.y1 + lineDataInfo.thickness < ex2; lineDataInfo.y2 = lineDataInfo.y1 + lineDataInfo.thickness) {
                     this.dataStream.createLine(lineDataInfo);
                     lineDataInfo.y1 += 3 * lineDataInfo.thickness;
                  }

                  return;
               }
            case 37:
               int thickness3 = (int)Math.floor((double)(thickness / 3.0F));
               lineDataInfo.setThickness(thickness3);
               if (borderPaintInfo.isHorizontal()) {
                  lineDataInfo.x2 = Math.round(x2);
                  lineDataInfo.y2 = lineDataInfo.y1;
                  this.dataStream.createLine(lineDataInfo);
                  thick = thickness3 * 2;
                  lineDataInfo = new AFPLineDataInfo(lineDataInfo);
                  lineDataInfo.y1 += thick;
                  lineDataInfo.y2 += thick;
                  this.dataStream.createLine(lineDataInfo);
               } else {
                  lineDataInfo.x2 = lineDataInfo.x1;
                  lineDataInfo.y2 = Math.round(y2);
                  this.dataStream.createLine(lineDataInfo);
                  thick = thickness3 * 2;
                  lineDataInfo = new AFPLineDataInfo(lineDataInfo);
                  lineDataInfo.x1 += thick;
                  lineDataInfo.x2 += thick;
                  this.dataStream.createLine(lineDataInfo);
               }
               break;
            case 55:
            case 119:
               lineDataInfo.x2 = Math.round(x2);
               float colFactor = borderPaintInfo.getStyle() == 55 ? 0.4F : -0.4F;
               float h3 = (y2 - y1) / 3.0F;
               lineDataInfo.color = ColorUtil.lightenColor(borderPaintInfo.getColor(), -colFactor);
               lineDataInfo.thickness = Math.round(h3);
               lineDataInfo.y1 = lineDataInfo.y2 = Math.round(y1);
               this.dataStream.createLine(lineDataInfo);
               lineDataInfo.color = borderPaintInfo.getColor();
               lineDataInfo.y1 = lineDataInfo.y2 = Math.round(y1 + h3);
               this.dataStream.createLine(lineDataInfo);
               lineDataInfo.color = ColorUtil.lightenColor(borderPaintInfo.getColor(), colFactor);
               lineDataInfo.y1 = lineDataInfo.y2 = Math.round(y1 + h3 + h3);
               this.dataStream.createLine(lineDataInfo);
            case 57:
               break;
            case 67:
            case 101:
            case 133:
            default:
               if (borderPaintInfo.isHorizontal()) {
                  lineDataInfo.x2 = Math.round(x2);
                  lineDataInfo.y2 = lineDataInfo.y1;
               } else {
                  lineDataInfo.x2 = lineDataInfo.x1;
                  lineDataInfo.y2 = Math.round(y2);
               }

               this.dataStream.createLine(lineDataInfo);
         }

      } else {
         log.error("Negative extent received. Border won't be painted.");
      }
   }
}
