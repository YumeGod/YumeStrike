package org.apache.fop.render.pdf;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.image.loader.batik.BatikImageFlavors;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.svg.PDFAElementBridge;
import org.apache.fop.svg.PDFBridgeContext;
import org.apache.fop.svg.PDFGraphics2D;
import org.apache.fop.svg.SVGEventProducer;
import org.apache.fop.svg.SVGUserAgent;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.w3c.dom.Document;

public class PDFImageHandlerSVG implements ImageHandler {
   private static Log log;

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      PDFRenderingContext pdfContext = (PDFRenderingContext)context;
      PDFContentGenerator generator = pdfContext.getGenerator();
      ImageXMLDOM imageSVG = (ImageXMLDOM)image;
      FOUserAgent userAgent = context.getUserAgent();
      float deviceResolution = userAgent.getTargetResolution();
      if (log.isDebugEnabled()) {
         log.debug("Generating SVG at " + deviceResolution + "dpi.");
      }

      float uaResolution = userAgent.getSourceResolution();
      SVGUserAgent ua = new SVGUserAgent(userAgent, new AffineTransform());
      double s = (double)(uaResolution / deviceResolution);
      AffineTransform resolutionScaling = new AffineTransform();
      resolutionScaling.scale(s, s);
      GVTBuilder builder = new GVTBuilder();
      boolean strokeText = false;
      BridgeContext ctx = new PDFBridgeContext(ua, strokeText ? null : pdfContext.getFontInfo(), userAgent.getFactory().getImageManager(), userAgent.getImageSessionContext(), new AffineTransform());

      GraphicsNode root;
      try {
         root = builder.build(ctx, (Document)imageSVG.getDocument());
         builder = null;
      } catch (Exception var29) {
         SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
         eventProducer.svgNotBuilt(this, var29, image.getInfo().getOriginalURI());
         return;
      }

      float w = (float)ctx.getDocumentSize().getWidth() * 1000.0F;
      float h = (float)ctx.getDocumentSize().getHeight() * 1000.0F;
      float sx = (float)pos.width / w;
      float sy = (float)pos.height / h;
      AffineTransform scaling = new AffineTransform(sx, 0.0F, 0.0F, sy, (float)pos.x / 1000.0F, (float)pos.y / 1000.0F);
      AffineTransform imageTransform = new AffineTransform();
      imageTransform.concatenate(scaling);
      imageTransform.concatenate(resolutionScaling);
      generator.comment("SVG setup");
      generator.saveGraphicsState();
      if (context.getUserAgent().isAccessibilityEnabled()) {
         PDFLogicalStructureHandler.MarkedContentInfo mci = pdfContext.getMarkedContentInfo();
         generator.beginMarkedContentSequence(mci.tag, mci.mcid);
      }

      generator.setColor(Color.black, false);
      generator.setColor(Color.black, true);
      if (!scaling.isIdentity()) {
         generator.comment("viewbox");
         generator.add(CTMHelper.toPDFString(scaling, false) + " cm\n");
      }

      PDFGraphics2D graphics = new PDFGraphics2D(true, pdfContext.getFontInfo(), generator.getDocument(), generator.getResourceContext(), pdfContext.getPage().referencePDF(), "", 0.0F);
      graphics.setGraphicContext(new GraphicContext());
      if (!resolutionScaling.isIdentity()) {
         generator.comment("resolution scaling for " + uaResolution + " -> " + deviceResolution + "\n");
         generator.add(CTMHelper.toPDFString(resolutionScaling, false) + " cm\n");
         graphics.scale(1.0 / s, 1.0 / s);
      }

      generator.comment("SVG start");
      generator.getState().save();
      generator.getState().concatenate(imageTransform);
      PDFAElementBridge aBridge = (PDFAElementBridge)ctx.getBridge("http://www.w3.org/2000/svg", "a");
      aBridge.getCurrentTransform().setTransform(generator.getState().getTransform());
      graphics.setPaintingState(generator.getState());
      graphics.setOutputStream(generator.getOutputStream());

      try {
         root.paint(graphics);
         generator.add(graphics.getString());
      } catch (Exception var28) {
         SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
         eventProducer.svgRenderingError(this, var28, image.getInfo().getOriginalURI());
      }

      generator.getState().restore();
      if (context.getUserAgent().isAccessibilityEnabled()) {
         generator.restoreGraphicsStateAccess();
      } else {
         generator.restoreGraphicsState();
      }

      generator.comment("SVG end");
   }

   public int getPriority() {
      return 400;
   }

   public Class getSupportedImageClass() {
      return ImageXMLDOM.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return new ImageFlavor[]{BatikImageFlavors.SVG_DOM};
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      return (image == null || image instanceof ImageXMLDOM && image.getFlavor().isCompatible(BatikImageFlavors.SVG_DOM)) && targetContext instanceof PDFRenderingContext;
   }

   static {
      log = LogFactory.getLog(PDFImageHandlerSVG.class);
   }
}
