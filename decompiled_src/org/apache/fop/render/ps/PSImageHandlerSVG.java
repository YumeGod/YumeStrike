package org.apache.fop.render.ps;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.fop.image.loader.batik.BatikImageFlavors;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.svg.SVGEventProducer;
import org.apache.fop.svg.SVGUserAgent;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.ps.PSGraphics2D;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.w3c.dom.Document;

public class PSImageHandlerSVG implements ImageHandler {
   private static final ImageFlavor[] FLAVORS;

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      PSRenderingContext psContext = (PSRenderingContext)context;
      PSGenerator gen = psContext.getGenerator();
      ImageXMLDOM imageSVG = (ImageXMLDOM)image;
      boolean strokeText = false;
      SVGUserAgent ua = new SVGUserAgent(context.getUserAgent(), new AffineTransform());
      PSGraphics2D graphics = new PSGraphics2D(strokeText, gen);
      graphics.setGraphicContext(new GraphicContext());
      BridgeContext ctx = new PSBridgeContext(ua, strokeText ? null : psContext.getFontInfo(), context.getUserAgent().getFactory().getImageManager(), context.getUserAgent().getImageSessionContext());

      GraphicsNode root;
      try {
         GVTBuilder builder = new GVTBuilder();
         root = builder.build(ctx, (Document)imageSVG.getDocument());
      } catch (Exception var21) {
         SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
         eventProducer.svgNotBuilt(this, var21, image.getInfo().getOriginalURI());
         return;
      }

      float w = (float)ctx.getDocumentSize().getWidth() * 1000.0F;
      float h = (float)ctx.getDocumentSize().getHeight() * 1000.0F;
      float sx = (float)pos.width / w;
      float sy = (float)pos.height / h;
      ctx = null;
      gen.commentln("%FOPBeginSVG");
      gen.saveGraphicsState();
      boolean clip = false;
      gen.concatMatrix((double)sx, 0.0, 0.0, (double)sy, pos.getMinX() / 1000.0, pos.getMinY() / 1000.0);
      AffineTransform transform = new AffineTransform();
      transform.translate(pos.getMinX(), pos.getMinY());
      gen.getCurrentState().concatMatrix(transform);

      try {
         root.paint(graphics);
      } catch (Exception var20) {
         SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
         eventProducer.svgRenderingError(this, var20, image.getInfo().getOriginalURI());
      }

      gen.restoreGraphicsState();
      gen.commentln("%FOPEndSVG");
   }

   public int getPriority() {
      return 400;
   }

   public Class getSupportedImageClass() {
      return ImageXMLDOM.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return FLAVORS;
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      if (!(targetContext instanceof PSRenderingContext)) {
         return false;
      } else {
         PSRenderingContext psContext = (PSRenderingContext)targetContext;
         return !psContext.isCreateForms() && (image == null || image instanceof ImageXMLDOM && image.getFlavor().isCompatible(BatikImageFlavors.SVG_DOM));
      }
   }

   static {
      FLAVORS = new ImageFlavor[]{BatikImageFlavors.SVG_DOM};
   }
}
