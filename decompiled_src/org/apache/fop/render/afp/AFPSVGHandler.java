package org.apache.fop.render.afp;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.fop.afp.AFPGraphics2D;
import org.apache.fop.afp.AFPGraphicsObjectInfo;
import org.apache.fop.afp.AFPObjectAreaInfo;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPResourceInfo;
import org.apache.fop.afp.AFPResourceManager;
import org.apache.fop.afp.AFPUnitConverter;
import org.apache.fop.afp.svg.AFPBridgeContext;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.image.loader.batik.BatikUtil;
import org.apache.fop.image.loader.batik.Graphics2DImagePainterImpl;
import org.apache.fop.render.AbstractGenericSVGHandler;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererContext;
import org.apache.fop.svg.SVGEventProducer;
import org.apache.fop.svg.SVGUserAgent;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.w3c.dom.Document;

public class AFPSVGHandler extends AbstractGenericSVGHandler {
   private boolean paintAsBitmap = false;

   public void handleXML(RendererContext context, Document doc, String ns) throws Exception {
      if ("http://www.w3.org/2000/svg".equals(ns)) {
         this.renderSVGDocument(context, doc);
      }

   }

   protected void renderSVGDocument(RendererContext rendererContext, Document doc) throws IOException {
      AFPRendererContext afpRendererContext = (AFPRendererContext)rendererContext;
      AFPInfo afpInfo = afpRendererContext.getInfo();
      this.paintAsBitmap = afpInfo.paintAsBitmap();
      FOUserAgent userAgent = rendererContext.getUserAgent();
      String uri = this.getDocumentURI(doc);
      if (this.paintAsBitmap) {
         try {
            super.renderSVGDocument(rendererContext, doc);
         } catch (IOException var26) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(userAgent.getEventBroadcaster());
            eventProducer.svgRenderingError(this, var26, uri);
         }

      } else {
         boolean textAsShapes = afpInfo.strokeText();
         AFPGraphics2D g2d = afpInfo.createGraphics2D(textAsShapes);
         AFPPaintingState paintingState = g2d.getPaintingState();
         paintingState.setImageUri(uri);
         BridgeContext bridgeContext = createBridgeContext(userAgent, g2d);
         Document clonedDoc = BatikUtil.cloneSVGDocument(doc);
         GraphicsNode root = this.buildGraphicsNode(userAgent, bridgeContext, clonedDoc);
         RendererContext.RendererContextWrapper wrappedContext = RendererContext.wrapRendererContext(rendererContext);
         Dimension imageSize = this.getImageSize(wrappedContext);
         Graphics2DImagePainter painter = this.createGraphics2DImagePainter(bridgeContext, root, imageSize);
         RendererContext.RendererContextWrapper rctx = RendererContext.wrapRendererContext(rendererContext);
         int x = rctx.getCurrentXPosition();
         int y = rctx.getCurrentYPosition();
         int width = afpInfo.getWidth();
         int height = afpInfo.getHeight();
         int resolution = afpInfo.getResolution();
         paintingState.save();
         AFPObjectAreaInfo objectAreaInfo = this.createObjectAreaInfo(paintingState, x, y, width, height, resolution);
         AFPResourceInfo resourceInfo = afpInfo.getResourceInfo();
         AFPGraphicsObjectInfo graphicsObjectInfo = this.createGraphicsObjectInfo(paintingState, painter, userAgent, resourceInfo, g2d);
         graphicsObjectInfo.setObjectAreaInfo(objectAreaInfo);
         AFPResourceManager resourceManager = afpInfo.getResourceManager();
         resourceManager.createObject(graphicsObjectInfo);
         paintingState.restore();
      }
   }

   private AFPObjectAreaInfo createObjectAreaInfo(AFPPaintingState paintingState, int x, int y, int width, int height, int resolution) {
      AFPObjectAreaInfo objectAreaInfo = new AFPObjectAreaInfo();
      AffineTransform at = paintingState.getData().getTransform();
      at.translate((double)x, (double)y);
      objectAreaInfo.setX((int)Math.round(at.getTranslateX()));
      objectAreaInfo.setY((int)Math.round(at.getTranslateY()));
      objectAreaInfo.setWidthRes(resolution);
      objectAreaInfo.setHeightRes(resolution);
      AFPUnitConverter unitConv = paintingState.getUnitConverter();
      objectAreaInfo.setWidth(Math.round(unitConv.mpt2units((float)width)));
      objectAreaInfo.setHeight(Math.round(unitConv.mpt2units((float)height)));
      int rotation = paintingState.getRotation();
      objectAreaInfo.setRotation(rotation);
      return objectAreaInfo;
   }

   private AFPGraphicsObjectInfo createGraphicsObjectInfo(AFPPaintingState paintingState, Graphics2DImagePainter painter, FOUserAgent userAgent, AFPResourceInfo resourceInfo, AFPGraphics2D g2d) {
      AFPGraphicsObjectInfo graphicsObjectInfo = new AFPGraphicsObjectInfo();
      String uri = paintingState.getImageUri();
      graphicsObjectInfo.setUri(uri);
      graphicsObjectInfo.setMimeType("image/x-afp+goca");
      graphicsObjectInfo.setResourceInfo(resourceInfo);
      graphicsObjectInfo.setPainter(painter);
      graphicsObjectInfo.setGraphics2D(g2d);
      return graphicsObjectInfo;
   }

   public static BridgeContext createBridgeContext(FOUserAgent userAgent, AFPGraphics2D g2d) {
      ImageManager imageManager = userAgent.getFactory().getImageManager();
      SVGUserAgent svgUserAgent = new SVGUserAgent(userAgent, new AffineTransform());
      ImageSessionContext imageSessionContext = userAgent.getImageSessionContext();
      FontInfo fontInfo = g2d.getFontInfo();
      return new AFPBridgeContext(svgUserAgent, fontInfo, imageManager, imageSessionContext, new AffineTransform(), g2d);
   }

   public boolean supportsRenderer(Renderer renderer) {
      return renderer instanceof AFPRenderer;
   }

   protected void updateRendererContext(RendererContext context) {
      context.setProperty("afpGrayscale", Boolean.FALSE);
   }

   private Graphics2DImagePainter createGraphics2DImagePainter(BridgeContext ctx, GraphicsNode root, Dimension imageSize) {
      Graphics2DImagePainter painter = null;
      if (this.paintAsBitmap()) {
         painter = super.createGraphics2DImagePainter(root, ctx, imageSize);
      } else {
         painter = new Graphics2DImagePainterImpl(root, ctx, imageSize);
      }

      return (Graphics2DImagePainter)painter;
   }

   private boolean paintAsBitmap() {
      return this.paintAsBitmap;
   }
}
