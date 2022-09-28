package org.apache.fop.afp;

import java.awt.geom.Rectangle2D;
import org.apache.fop.afp.ioca.IDEStructureParameter;
import org.apache.fop.afp.ioca.ImageContent;
import org.apache.fop.afp.modca.AbstractDataObject;
import org.apache.fop.afp.modca.AbstractNamedAFPObject;
import org.apache.fop.afp.modca.Document;
import org.apache.fop.afp.modca.GraphicsObject;
import org.apache.fop.afp.modca.ImageObject;
import org.apache.fop.afp.modca.IncludeObject;
import org.apache.fop.afp.modca.ObjectContainer;
import org.apache.fop.afp.modca.Overlay;
import org.apache.fop.afp.modca.PageSegment;
import org.apache.fop.afp.modca.Registry;
import org.apache.fop.afp.modca.ResourceObject;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;

public class AFPDataObjectFactory {
   private final Factory factory;

   public AFPDataObjectFactory(Factory factory) {
      this.factory = factory;
   }

   public ObjectContainer createObjectContainer(AFPDataObjectInfo dataObjectInfo) {
      ObjectContainer objectContainer = this.factory.createObjectContainer();
      objectContainer.setViewport(dataObjectInfo);
      Registry.ObjectType objectType = dataObjectInfo.getObjectType();
      AFPResourceInfo resourceInfo = dataObjectInfo.getResourceInfo();
      AFPResourceLevel resourceLevel = resourceInfo.getLevel();
      boolean dataInContainer = true;
      boolean containerHasOEG = resourceLevel.isInline();
      boolean dataInOCD = true;
      objectContainer.setObjectClassification((byte)1, objectType, true, containerHasOEG, true);
      objectContainer.setData(dataObjectInfo.getData());
      return objectContainer;
   }

   public ImageObject createImage(AFPImageObjectInfo imageObjectInfo) {
      ImageObject imageObj = this.factory.createImageObject();
      imageObj.setViewport(imageObjectInfo);
      if (imageObjectInfo.hasCompression()) {
         int compression = imageObjectInfo.getCompression();
         switch (compression) {
            case 2:
               imageObj.setEncoding((byte)-128);
               break;
            case 3:
               imageObj.setEncoding((byte)-127);
               break;
            case 4:
               imageObj.setEncoding((byte)-126);
               break;
            default:
               throw new IllegalStateException("Invalid compression scheme: " + compression);
         }
      }

      ImageContent content = imageObj.getImageSegment().getImageContent();
      int bitsPerPixel = imageObjectInfo.getBitsPerPixel();
      imageObj.setIDESize((byte)bitsPerPixel);
      IDEStructureParameter ideStruct;
      switch (bitsPerPixel) {
         case 1:
            break;
         case 4:
         case 8:
            ideStruct = content.needIDEStructureParameter();
            ideStruct.setBitsPerComponent(new int[]{bitsPerPixel});
            break;
         case 24:
            ideStruct = content.needIDEStructureParameter();
            ideStruct.setDefaultRGBColorModel();
            break;
         case 32:
            ideStruct = content.needIDEStructureParameter();
            ideStruct.setDefaultCMYKColorModel();
            break;
         default:
            throw new IllegalArgumentException("Unsupported number of bits per pixel: " + bitsPerPixel);
      }

      if (imageObjectInfo.isSubtractive()) {
         ideStruct = content.needIDEStructureParameter();
         ideStruct.setSubtractive(imageObjectInfo.isSubtractive());
      }

      imageObj.setData(imageObjectInfo.getData());
      return imageObj;
   }

   public GraphicsObject createGraphic(AFPGraphicsObjectInfo graphicsObjectInfo) {
      GraphicsObject graphicsObj = this.factory.createGraphicsObject();
      graphicsObj.setViewport(graphicsObjectInfo);
      AFPGraphics2D g2d = graphicsObjectInfo.getGraphics2D();
      g2d.setGraphicsObject(graphicsObj);
      graphicsObj.setColorConverter(g2d.getPaintingState().getColorConverter());
      Graphics2DImagePainter painter = graphicsObjectInfo.getPainter();
      Rectangle2D area = graphicsObjectInfo.getArea();
      g2d.scale(1.0, -1.0);
      g2d.translate(0.0, -area.getHeight());
      painter.paint(g2d, area);
      graphicsObj.setComplete(true);
      return graphicsObj;
   }

   public IncludeObject createInclude(String includeName, AFPDataObjectInfo dataObjectInfo) {
      IncludeObject includeObj = this.factory.createInclude(includeName);
      if (dataObjectInfo instanceof AFPImageObjectInfo) {
         includeObj.setObjectType((byte)-5);
      } else if (dataObjectInfo instanceof AFPGraphicsObjectInfo) {
         includeObj.setObjectType((byte)-69);
      } else {
         includeObj.setObjectType((byte)-110);
         Registry.ObjectType objectType = dataObjectInfo.getObjectType();
         if (objectType == null) {
            throw new IllegalStateException("Failed to set Object Classification Triplet on Object Container.");
         }

         boolean dataInContainer = true;
         boolean containerHasOEG = false;
         boolean dataInOCD = true;
         includeObj.setObjectClassification((byte)16, objectType, true, false, true);
      }

      AFPObjectAreaInfo objectAreaInfo = dataObjectInfo.getObjectAreaInfo();
      int xOffset = objectAreaInfo.getX();
      int yOffset = objectAreaInfo.getY();
      includeObj.setObjectAreaOffset(xOffset, yOffset);
      int width = objectAreaInfo.getWidth();
      int height = objectAreaInfo.getHeight();
      includeObj.setObjectAreaSize(width, height);
      int rotation = objectAreaInfo.getRotation();
      includeObj.setObjectAreaOrientation(rotation);
      int widthRes = objectAreaInfo.getWidthRes();
      int heightRes = objectAreaInfo.getHeightRes();
      includeObj.setMeasurementUnits(widthRes, heightRes);
      includeObj.setMappingOption((byte)32);
      return includeObj;
   }

   public ResourceObject createResource(AbstractNamedAFPObject namedObj, AFPResourceInfo resourceInfo, Registry.ObjectType objectType) {
      ResourceObject resourceObj = null;
      String resourceName = resourceInfo.getName();
      if (resourceName != null) {
         resourceObj = this.factory.createResource(resourceName);
      } else {
         resourceObj = this.factory.createResource();
      }

      if (namedObj instanceof Document) {
         resourceObj.setType((byte)-88);
      } else if (namedObj instanceof PageSegment) {
         resourceObj.setType((byte)-5);
      } else if (namedObj instanceof Overlay) {
         resourceObj.setType((byte)-4);
      } else {
         if (!(namedObj instanceof AbstractDataObject)) {
            throw new UnsupportedOperationException("Unsupported resource object type " + namedObj);
         }

         AbstractDataObject dataObj = (AbstractDataObject)namedObj;
         if (namedObj instanceof ObjectContainer) {
            resourceObj.setType((byte)-110);
            boolean dataInContainer = true;
            boolean containerHasOEG = false;
            boolean dataInOCD = true;
            resourceObj.setObjectClassification((byte)1, objectType, true, false, true);
         } else if (namedObj instanceof ImageObject) {
            resourceObj.setType((byte)6);
         } else {
            if (!(namedObj instanceof GraphicsObject)) {
               throw new UnsupportedOperationException("Unsupported resource object for data object type " + dataObj);
            }

            resourceObj.setType((byte)3);
         }
      }

      resourceObj.setDataObject(namedObj);
      return resourceObj;
   }
}
