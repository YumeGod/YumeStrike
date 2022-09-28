package org.apache.fop.render.afp;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.fop.afp.AFPGraphics2D;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPResourceInfo;
import org.apache.fop.afp.AFPResourceManager;
import org.apache.fop.fonts.FontInfo;
import org.apache.xmlgraphics.java2d.GraphicContext;

public final class AFPInfo {
   private int width;
   private int height;
   private int x;
   private int y;
   private Configuration handlerConfiguration;
   private FontInfo fontInfo;
   private AFPPaintingState paintingState;
   private AFPResourceManager resourceManager;
   private AFPResourceInfo resourceInfo;
   private boolean paintAsBitmap;

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public Configuration getHandlerConfiguration() {
      return this.handlerConfiguration;
   }

   public void setHandlerConfiguration(Configuration cfg) {
      this.handlerConfiguration = cfg;
   }

   public FontInfo getFontInfo() {
      return this.fontInfo;
   }

   public AFPPaintingState getPaintingState() {
      return this.paintingState;
   }

   public AFPResourceManager getResourceManager() {
      return this.resourceManager;
   }

   public boolean isColorSupported() {
      return this.getPaintingState().isColorImages();
   }

   protected int getX() {
      return this.x;
   }

   protected int getY() {
      return this.y;
   }

   protected int getResolution() {
      return this.getPaintingState().getResolution();
   }

   protected int getBitsPerPixel() {
      return this.getPaintingState().getBitsPerPixel();
   }

   protected void setX(int x) {
      this.x = x;
   }

   protected void setY(int y) {
      this.y = y;
   }

   protected void setFontInfo(FontInfo fontInfo) {
      this.fontInfo = fontInfo;
   }

   public void setPaintingState(AFPPaintingState paintingState) {
      this.paintingState = paintingState;
   }

   public void setResourceManager(AFPResourceManager resourceManager) {
      this.resourceManager = resourceManager;
   }

   public void setPaintAsBitmap(boolean b) {
      this.paintAsBitmap = b;
   }

   public boolean paintAsBitmap() {
      return this.paintAsBitmap;
   }

   public boolean strokeText() {
      boolean strokeText = false;
      if (this.handlerConfiguration != null) {
         strokeText = this.handlerConfiguration.getChild("stroke-text", true).getValueAsBoolean(strokeText);
      }

      return strokeText;
   }

   public void setResourceInfo(AFPResourceInfo resourceInfo) {
      this.resourceInfo = resourceInfo;
   }

   public AFPResourceInfo getResourceInfo() {
      return this.resourceInfo;
   }

   public AFPGraphics2D createGraphics2D(boolean textAsShapes) {
      AFPGraphics2D g2d = new AFPGraphics2D(textAsShapes, this.paintingState, this.resourceManager, this.resourceInfo, this.fontInfo);
      g2d.setGraphicContext(new GraphicContext());
      return g2d;
   }

   public String toString() {
      return "AFPInfo{width=" + this.width + ", height=" + this.height + ", x=" + this.x + ", y=" + this.y + ", cfg=" + this.handlerConfiguration + ", fontInfo=" + this.fontInfo + ", resourceManager=" + this.resourceManager + ", paintingState=" + this.paintingState + ", paintAsBitmap=" + this.paintAsBitmap + ", resourceInfo=" + this.resourceInfo + "}";
   }
}
