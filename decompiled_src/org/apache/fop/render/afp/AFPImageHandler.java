package org.apache.fop.render.afp;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Map;
import org.apache.fop.afp.AFPDataObjectInfo;
import org.apache.fop.afp.AFPObjectAreaInfo;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPResourceInfo;
import org.apache.fop.afp.AFPUnitConverter;
import org.apache.fop.render.ImageHandlerBase;

public abstract class AFPImageHandler implements ImageHandlerBase {
   private static final int X = 0;
   private static final int Y = 1;
   private final AFPForeignAttributeReader foreignAttributeReader = new AFPForeignAttributeReader();

   public AFPDataObjectInfo generateDataObjectInfo(AFPRendererImageInfo rendererImageInfo) throws IOException {
      AFPDataObjectInfo dataObjectInfo = this.createDataObjectInfo();
      this.setResourceInformation(dataObjectInfo, rendererImageInfo.getURI(), rendererImageInfo.getForeignAttributes());
      Point origin = rendererImageInfo.getOrigin();
      Rectangle2D position = rendererImageInfo.getPosition();
      int srcX = Math.round((float)origin.x + (float)position.getX());
      int srcY = Math.round((float)origin.y + (float)position.getY());
      Rectangle targetRect = new Rectangle(srcX, srcY, (int)Math.round(position.getWidth()), (int)Math.round(position.getHeight()));
      AFPRendererContext rendererContext = (AFPRendererContext)rendererImageInfo.getRendererContext();
      AFPInfo afpInfo = rendererContext.getInfo();
      AFPPaintingState paintingState = afpInfo.getPaintingState();
      dataObjectInfo.setObjectAreaInfo(createObjectAreaInfo(paintingState, targetRect));
      return dataObjectInfo;
   }

   protected void setResourceInformation(AFPDataObjectInfo dataObjectInfo, String uri, Map foreignAttributes) {
      AFPResourceInfo resourceInfo = this.foreignAttributeReader.getResourceInfo(foreignAttributes);
      resourceInfo.setUri(uri);
      dataObjectInfo.setResourceInfo(resourceInfo);
   }

   public static AFPObjectAreaInfo createObjectAreaInfo(AFPPaintingState paintingState, Rectangle targetRect) {
      AFPObjectAreaInfo objectAreaInfo = new AFPObjectAreaInfo();
      AFPUnitConverter unitConv = paintingState.getUnitConverter();
      int[] coords = unitConv.mpts2units(new float[]{(float)targetRect.x, (float)targetRect.y});
      objectAreaInfo.setX(coords[0]);
      objectAreaInfo.setY(coords[1]);
      int width = Math.round(unitConv.mpt2units((float)targetRect.width));
      objectAreaInfo.setWidth(width);
      int height = Math.round(unitConv.mpt2units((float)targetRect.height));
      objectAreaInfo.setHeight(height);
      int resolution = paintingState.getResolution();
      objectAreaInfo.setHeightRes(resolution);
      objectAreaInfo.setWidthRes(resolution);
      objectAreaInfo.setRotation(paintingState.getRotation());
      return objectAreaInfo;
   }

   protected abstract AFPDataObjectInfo createDataObjectInfo();
}
