package org.apache.fop.svg;

import java.awt.geom.AffineTransform;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.SVGTextElementBridge;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.gvt.TextPainter;
import org.apache.fop.fonts.FontInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;

public class PDFBridgeContext extends AbstractFOPBridgeContext {
   public PDFBridgeContext(UserAgent userAgent, DocumentLoader documentLoader, FontInfo fontInfo, ImageManager imageManager, ImageSessionContext imageSessionContext, AffineTransform linkTransform) {
      super(userAgent, documentLoader, fontInfo, imageManager, imageSessionContext, linkTransform);
   }

   public PDFBridgeContext(UserAgent userAgent, FontInfo fontInfo, ImageManager imageManager, ImageSessionContext imageSessionContext) {
      super(userAgent, fontInfo, imageManager, imageSessionContext);
   }

   public PDFBridgeContext(SVGUserAgent userAgent, FontInfo fontInfo, ImageManager imageManager, ImageSessionContext imageSessionContext, AffineTransform linkTransform) {
      super(userAgent, fontInfo, imageManager, imageSessionContext, linkTransform);
   }

   public void registerSVGBridges() {
      super.registerSVGBridges();
      if (this.fontInfo != null) {
         TextPainter textPainter = new PDFTextPainter(this.fontInfo);
         SVGTextElementBridge textElementBridge = new PDFTextElementBridge(textPainter);
         this.putBridge(textElementBridge);
         this.putElementBridgeConditional("org.apache.fop.svg.PDFBatikFlowTextElementBridge", "org.apache.batik.extension.svg.BatikFlowTextElementBridge");
         this.putElementBridgeConditional("org.apache.fop.svg.PDFSVG12TextElementBridge", "org.apache.batik.bridge.svg12.SVG12TextElementBridge");
         this.putElementBridgeConditional("org.apache.fop.svg.PDFSVGFlowRootElementBridge", "org.apache.batik.bridge.svg12.SVGFlowRootElementBridge");
      }

      PDFAElementBridge pdfAElementBridge = new PDFAElementBridge();
      if (this.linkTransform != null) {
         pdfAElementBridge.setCurrentTransform(this.linkTransform);
      } else {
         pdfAElementBridge.setCurrentTransform(new AffineTransform());
      }

      this.putBridge(pdfAElementBridge);
      this.putBridge(new PDFImageElementBridge());
   }

   public BridgeContext createBridgeContext() {
      return new PDFBridgeContext(this.getUserAgent(), this.getDocumentLoader(), this.fontInfo, this.getImageManager(), this.getImageSessionContext(), this.linkTransform);
   }
}
