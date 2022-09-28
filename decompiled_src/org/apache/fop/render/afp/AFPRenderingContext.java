package org.apache.fop.render.afp;

import java.util.Map;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPResourceManager;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.render.AbstractRenderingContext;

public class AFPRenderingContext extends AbstractRenderingContext {
   private AFPResourceManager resourceManager;
   private AFPPaintingState paintingState;
   private FontInfo fontInfo;
   private Map foreignAttributes;

   public AFPRenderingContext(FOUserAgent userAgent, AFPResourceManager resourceManager, AFPPaintingState paintingState, FontInfo fontInfo, Map foreignAttributes) {
      super(userAgent);
      this.resourceManager = resourceManager;
      this.paintingState = paintingState;
      this.fontInfo = fontInfo;
      this.foreignAttributes = foreignAttributes;
   }

   public String getMimeType() {
      return "application/x-afp";
   }

   public AFPResourceManager getResourceManager() {
      return this.resourceManager;
   }

   public AFPPaintingState getPaintingState() {
      return this.paintingState;
   }

   public FontInfo getFontInfo() {
      return this.fontInfo;
   }

   public Map getForeignAttributes() {
      return this.foreignAttributes;
   }
}
