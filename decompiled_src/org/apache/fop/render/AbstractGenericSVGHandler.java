package org.apache.fop.render;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.image.loader.batik.BatikUtil;
import org.apache.fop.image.loader.batik.Graphics2DImagePainterImpl;
import org.apache.fop.svg.SVGEventProducer;
import org.apache.fop.svg.SVGUserAgent;
import org.w3c.dom.Document;

public abstract class AbstractGenericSVGHandler implements XMLHandler, RendererContextConstants {
   public void handleXML(RendererContext context, Document doc, String ns) throws Exception {
      if ("http://www.w3.org/2000/svg".equals(ns)) {
         this.renderSVGDocument(context, doc);
      }

   }

   protected org.apache.xmlgraphics.java2d.Graphics2DImagePainter createGraphics2DImagePainter(GraphicsNode root, BridgeContext ctx, Dimension imageSize) {
      return new Graphics2DImagePainterImpl(root, ctx, imageSize);
   }

   protected GraphicsNode buildGraphicsNode(FOUserAgent userAgent, BridgeContext ctx, Document doc) {
      GVTBuilder builder = new GVTBuilder();

      try {
         GraphicsNode root = builder.build(ctx, doc);
         return root;
      } catch (Exception var10) {
         EventBroadcaster eventBroadcaster = userAgent.getEventBroadcaster();
         SVGEventProducer eventProducer = SVGEventProducer.Provider.get(eventBroadcaster);
         String uri = this.getDocumentURI(doc);
         eventProducer.svgNotBuilt(this, var10, uri);
         return null;
      }
   }

   protected Dimension getImageSize(RendererContext.RendererContextWrapper wrappedContext) {
      int width = wrappedContext.getWidth();
      int height = wrappedContext.getHeight();
      return new Dimension(width, height);
   }

   protected void renderSVGDocument(RendererContext rendererContext, Document doc) throws IOException {
      this.updateRendererContext(rendererContext);
      FOUserAgent userAgent = rendererContext.getUserAgent();
      SVGUserAgent svgUserAgent = new SVGUserAgent(userAgent, new AffineTransform());
      BridgeContext bridgeContext = new BridgeContext(svgUserAgent);
      Document clonedDoc = BatikUtil.cloneSVGDocument(doc);
      GraphicsNode root = this.buildGraphicsNode(userAgent, bridgeContext, clonedDoc);
      RendererContext.RendererContextWrapper wrappedContext = RendererContext.wrapRendererContext(rendererContext);
      Dimension imageSize = this.getImageSize(wrappedContext);
      org.apache.xmlgraphics.java2d.Graphics2DImagePainter painter = this.createGraphics2DImagePainter(root, bridgeContext, imageSize);
      Graphics2DAdapter g2dAdapter = rendererContext.getRenderer().getGraphics2DAdapter();
      int x = wrappedContext.getCurrentXPosition();
      int y = wrappedContext.getCurrentYPosition();
      int width = wrappedContext.getWidth();
      int height = wrappedContext.getHeight();
      g2dAdapter.paintImage(painter, rendererContext, x, y, width, height);
   }

   protected String getDocumentURI(Document doc) {
      String docURI = null;
      if (doc instanceof AbstractDocument) {
         AbstractDocument level3Doc = (AbstractDocument)doc;
         docURI = level3Doc.getDocumentURI();
      }

      return docURI;
   }

   protected void updateRendererContext(RendererContext context) {
   }

   public String getNamespace() {
      return "http://www.w3.org/2000/svg";
   }
}
