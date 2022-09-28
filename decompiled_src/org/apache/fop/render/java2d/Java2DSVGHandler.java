package org.apache.fop.render.java2d;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Map;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.image.loader.batik.BatikUtil;
import org.apache.fop.render.AbstractGenericSVGHandler;
import org.apache.fop.render.ImageHandlerUtil;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererContext;
import org.apache.fop.svg.SVGEventProducer;
import org.apache.fop.svg.SVGUserAgent;
import org.w3c.dom.Document;

public class Java2DSVGHandler extends AbstractGenericSVGHandler implements Java2DRendererContextConstants {
   private static Log log;

   public static Java2DInfo getJava2DInfo(RendererContext context) {
      Java2DInfo pdfi = new Java2DInfo();
      pdfi.state = (Java2DGraphicsState)context.getProperty("state");
      pdfi.width = (Integer)context.getProperty("width");
      pdfi.height = (Integer)context.getProperty("height");
      pdfi.currentXPosition = (Integer)context.getProperty("xpos");
      pdfi.currentYPosition = (Integer)context.getProperty("ypos");
      Map foreign = (Map)context.getProperty("foreign-attributes");
      pdfi.paintAsBitmap = ImageHandlerUtil.isConversionModeBitmap(foreign);
      return pdfi;
   }

   protected void renderSVGDocument(RendererContext context, Document doc) {
      Java2DInfo info = getJava2DInfo(context);
      if (log.isDebugEnabled()) {
         log.debug("renderSVGDocument(" + context + ", " + doc + ", " + info + ")");
      }

      if (info.paintAsBitmap) {
         try {
            super.renderSVGDocument(context, doc);
         } catch (IOException var18) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
            eventProducer.svgRenderingError(this, var18, this.getDocumentURI(doc));
         }

      } else {
         int x = info.currentXPosition;
         int y = info.currentYPosition;
         SVGUserAgent ua = new SVGUserAgent(context.getUserAgent(), new AffineTransform());
         BridgeContext ctx = new BridgeContext(ua);
         Document clonedDoc = BatikUtil.cloneSVGDocument(doc);

         GraphicsNode root;
         try {
            GVTBuilder builder = new GVTBuilder();
            root = builder.build(ctx, clonedDoc);
         } catch (Exception var20) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
            eventProducer.svgNotBuilt(this, var20, this.getDocumentURI(doc));
            return;
         }

         float iw = (float)ctx.getDocumentSize().getWidth() * 1000.0F;
         float ih = (float)ctx.getDocumentSize().getHeight() * 1000.0F;
         float w = (float)info.width;
         float h = (float)info.height;
         AffineTransform origTransform = info.state.getGraph().getTransform();
         info.state.getGraph().translate((double)((float)x / 1000.0F), (double)((float)y / 1000.0F));
         AffineTransform at = AffineTransform.getScaleInstance((double)(w / iw), (double)(h / ih));
         if (!at.isIdentity()) {
            info.state.getGraph().transform(at);
         }

         try {
            root.paint(info.state.getGraph());
         } catch (Exception var19) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
            eventProducer.svgRenderingError(this, var19, this.getDocumentURI(doc));
         }

         info.state.getGraph().setTransform(origTransform);
      }
   }

   public boolean supportsRenderer(Renderer renderer) {
      return renderer instanceof Java2DRenderer;
   }

   static {
      log = LogFactory.getLog(Java2DSVGHandler.class);
   }

   public static class Java2DInfo {
      public Java2DGraphicsState state;
      public int width;
      public int height;
      public int currentXPosition;
      public int currentYPosition;
      public boolean paintAsBitmap;

      public String toString() {
         return "Java2DInfo {state = " + this.state + ", " + "width = " + this.width + ", " + "height = " + this.height + ", " + "currentXPosition = " + this.currentXPosition + ", " + "currentYPosition = " + this.currentYPosition + ", " + "paintAsBitmap = " + this.paintAsBitmap + "}";
      }
   }
}
