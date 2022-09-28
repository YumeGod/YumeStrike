package org.apache.fop.render.ps;

import java.awt.geom.AffineTransform;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.SVGTextElementBridge;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.gvt.TextPainter;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.svg.AbstractFOPBridgeContext;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;

public class PSBridgeContext extends AbstractFOPBridgeContext {
   public PSBridgeContext(UserAgent userAgent, DocumentLoader documentLoader, FontInfo fontInfo, ImageManager imageManager, ImageSessionContext imageSessionContext, AffineTransform linkTransform) {
      super(userAgent, documentLoader, fontInfo, imageManager, imageSessionContext, linkTransform);
   }

   public PSBridgeContext(UserAgent userAgent, FontInfo fontInfo, ImageManager imageManager, ImageSessionContext imageSessionContext) {
      super(userAgent, fontInfo, imageManager, imageSessionContext);
   }

   public void registerSVGBridges() {
      super.registerSVGBridges();
      if (this.fontInfo != null) {
         TextPainter textPainter = new PSTextPainter(this.fontInfo);
         SVGTextElementBridge textElementBridge = new PSTextElementBridge(textPainter);
         this.putBridge(textElementBridge);
         this.putElementBridgeConditional("org.apache.fop.render.ps.PSBatikFlowTextElementBridge", "org.apache.batik.extension.svg.BatikFlowTextElementBridge");
         this.putElementBridgeConditional("org.apache.fop.render.ps.PSSVG12TextElementBridge", "org.apache.batik.bridge.svg12.SVG12TextElementBridge");
         this.putElementBridgeConditional("org.apache.fop.render.ps.PSSVGFlowRootElementBridge", "org.apache.batik.bridge.svg12.SVGFlowRootElementBridge");
      }

   }

   public BridgeContext createBridgeContext() {
      return new PSBridgeContext(this.getUserAgent(), this.getDocumentLoader(), this.fontInfo, this.getImageManager(), this.getImageSessionContext(), this.linkTransform);
   }
}
