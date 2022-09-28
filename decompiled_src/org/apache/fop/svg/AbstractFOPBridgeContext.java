package org.apache.fop.svg;

import java.awt.geom.AffineTransform;
import java.lang.reflect.Constructor;
import org.apache.batik.bridge.Bridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.UserAgent;
import org.apache.fop.fonts.FontInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;

public abstract class AbstractFOPBridgeContext extends BridgeContext {
   protected final FontInfo fontInfo;
   protected final ImageManager imageManager;
   protected final ImageSessionContext imageSessionContext;
   protected final AffineTransform linkTransform;

   public AbstractFOPBridgeContext(UserAgent userAgent, DocumentLoader loader, FontInfo fontInfo, ImageManager imageManager, ImageSessionContext imageSessionContext, AffineTransform linkTransform) {
      super(userAgent, loader);
      this.fontInfo = fontInfo;
      this.imageManager = imageManager;
      this.imageSessionContext = imageSessionContext;
      this.linkTransform = linkTransform;
   }

   public AbstractFOPBridgeContext(UserAgent userAgent, FontInfo fontInfo, ImageManager imageManager, ImageSessionContext imageSessionContext, AffineTransform linkTransform) {
      super(userAgent);
      this.fontInfo = fontInfo;
      this.imageManager = imageManager;
      this.imageSessionContext = imageSessionContext;
      this.linkTransform = linkTransform;
   }

   public AbstractFOPBridgeContext(UserAgent userAgent, FontInfo fontInfo, ImageManager imageManager, ImageSessionContext imageSessionContext) {
      this(userAgent, fontInfo, imageManager, imageSessionContext, (AffineTransform)null);
   }

   public ImageManager getImageManager() {
      return this.imageManager;
   }

   public ImageSessionContext getImageSessionContext() {
      return this.imageSessionContext;
   }

   protected void putElementBridgeConditional(String className, String testFor) {
      try {
         Class.forName(testFor);
         Class clazz = Class.forName(className);
         Constructor constructor = clazz.getConstructor(FontInfo.class);
         this.putBridge((Bridge)constructor.newInstance(this.fontInfo));
      } catch (Throwable var5) {
      }

   }

   public abstract BridgeContext createBridgeContext();
}
