package org.apache.fop.afp.svg;

import java.awt.geom.AffineTransform;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.gvt.TextPainter;
import org.apache.fop.afp.AFPGraphics2D;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.svg.AbstractFOPBridgeContext;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;

public class AFPBridgeContext extends AbstractFOPBridgeContext {
   private final AFPGraphics2D g2d;

   public AFPBridgeContext(UserAgent userAgent, FontInfo fontInfo, ImageManager imageManager, ImageSessionContext imageSessionContext, AffineTransform linkTransform, AFPGraphics2D g2d) {
      super(userAgent, fontInfo, imageManager, imageSessionContext, linkTransform);
      this.g2d = g2d;
   }

   public AFPBridgeContext(UserAgent userAgent, DocumentLoader documentLoader, FontInfo fontInfo, ImageManager imageManager, ImageSessionContext imageSessionContext, AffineTransform linkTransform, AFPGraphics2D g2d) {
      super(userAgent, documentLoader, fontInfo, imageManager, imageSessionContext, linkTransform);
      this.g2d = g2d;
   }

   public void registerSVGBridges() {
      super.registerSVGBridges();
      if (this.fontInfo != null) {
         AFPTextHandler textHandler = new AFPTextHandler(this.fontInfo);
         this.g2d.setCustomTextHandler(textHandler);
         TextPainter textPainter = new AFPTextPainter(textHandler);
         this.setTextPainter(textPainter);
         this.putBridge(new AFPTextElementBridge(textPainter));
      }

      this.putBridge(new AFPImageElementBridge());
   }

   public BridgeContext createBridgeContext() {
      return new AFPBridgeContext(this.getUserAgent(), this.getDocumentLoader(), this.fontInfo, this.getImageManager(), this.getImageSessionContext(), this.linkTransform, this.g2d);
   }
}
