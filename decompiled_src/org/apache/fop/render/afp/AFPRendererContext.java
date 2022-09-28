package org.apache.fop.render.afp;

import java.util.Map;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPResourceInfo;
import org.apache.fop.afp.AFPResourceManager;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.render.AbstractRenderer;
import org.apache.fop.render.ImageHandlerUtil;
import org.apache.fop.render.RendererContext;

public class AFPRendererContext extends RendererContext {
   public AFPRendererContext(AbstractRenderer renderer, String mime) {
      super(renderer, mime);
   }

   public AFPInfo getInfo() {
      AFPInfo info = new AFPInfo();
      info.setWidth((Integer)this.getProperty("width"));
      info.setHeight((Integer)this.getProperty("height"));
      info.setX((Integer)this.getProperty("xpos"));
      info.setY((Integer)this.getProperty("ypos"));
      info.setHandlerConfiguration((Configuration)this.getProperty("cfg"));
      info.setFontInfo((FontInfo)this.getProperty("afpFontInfo"));
      info.setPaintingState((AFPPaintingState)this.getProperty("afpPaintingState"));
      info.setResourceManager((AFPResourceManager)this.getProperty("afpResourceManager"));
      Map foreignAttributes = (Map)this.getProperty("foreign-attributes");
      if (foreignAttributes != null) {
         boolean paintAsBitmap = ImageHandlerUtil.isConversionModeBitmap(foreignAttributes);
         info.setPaintAsBitmap(paintAsBitmap);
         AFPForeignAttributeReader foreignAttributeReader = new AFPForeignAttributeReader();
         AFPResourceInfo resourceInfo = foreignAttributeReader.getResourceInfo(foreignAttributes);
         if (!resourceInfo.levelChanged()) {
            byte resourceType = paintAsBitmap ? 6 : 3;
            resourceInfo.setLevel(info.getResourceManager().getResourceLevelDefaults().getDefaultResourceLevel((byte)resourceType));
         }

         info.setResourceInfo(resourceInfo);
      }

      return info;
   }
}
