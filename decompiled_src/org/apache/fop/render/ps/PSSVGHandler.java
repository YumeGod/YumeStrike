package org.apache.fop.render.ps;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Map;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.image.loader.batik.BatikUtil;
import org.apache.fop.render.AbstractGenericSVGHandler;
import org.apache.fop.render.ImageHandlerUtil;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererContext;
import org.apache.fop.svg.SVGEventProducer;
import org.apache.fop.svg.SVGUserAgent;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.ps.PSGraphics2D;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.w3c.dom.Document;

public class PSSVGHandler extends AbstractGenericSVGHandler implements PSRendererContextConstants {
   private static Log log;

   public static PSInfo getPSInfo(RendererContext context) {
      PSInfo psi = new PSInfo();
      psi.psGenerator = (PSGenerator)context.getProperty("psGenerator");
      psi.fontInfo = (FontInfo)context.getProperty("psFontInfo");
      psi.width = (Integer)context.getProperty("width");
      psi.height = (Integer)context.getProperty("height");
      psi.currentXPosition = (Integer)context.getProperty("xpos");
      psi.currentYPosition = (Integer)context.getProperty("ypos");
      psi.cfg = (Configuration)context.getProperty("cfg");
      return psi;
   }

   protected void renderSVGDocument(RendererContext context, Document doc) {
      PSInfo psInfo = getPSInfo(context);
      int xOffset = psInfo.currentXPosition;
      int yOffset = psInfo.currentYPosition;
      PSGenerator gen = psInfo.psGenerator;
      boolean paintAsBitmap = false;
      if (context != null) {
         Map foreign = (Map)context.getProperty("foreign-attributes");
         paintAsBitmap = ImageHandlerUtil.isConversionModeBitmap(foreign);
      }

      if (paintAsBitmap) {
         try {
            super.renderSVGDocument(context, doc);
         } catch (IOException var22) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
            eventProducer.svgRenderingError(this, var22, this.getDocumentURI(doc));
         }

      } else {
         boolean strokeText = false;
         Configuration cfg = psInfo.getHandlerConfiguration();
         if (cfg != null) {
            strokeText = cfg.getChild("stroke-text", true).getValueAsBoolean(strokeText);
         }

         SVGUserAgent ua = new SVGUserAgent(context.getUserAgent(), new AffineTransform());
         PSGraphics2D graphics = new PSGraphics2D(strokeText, gen);
         graphics.setGraphicContext(new GraphicContext());
         BridgeContext ctx = new PSBridgeContext(ua, strokeText ? null : psInfo.fontInfo, context.getUserAgent().getFactory().getImageManager(), context.getUserAgent().getImageSessionContext());
         Document clonedDoc = BatikUtil.cloneSVGDocument(doc);

         GraphicsNode root;
         try {
            GVTBuilder builder = new GVTBuilder();
            root = builder.build(ctx, (Document)clonedDoc);
         } catch (Exception var25) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
            eventProducer.svgNotBuilt(this, var25, this.getDocumentURI(doc));
            return;
         }

         float w = (float)ctx.getDocumentSize().getWidth() * 1000.0F;
         float h = (float)ctx.getDocumentSize().getHeight() * 1000.0F;
         float sx = (float)psInfo.getWidth() / w;
         float sy = (float)psInfo.getHeight() / h;
         ctx = null;

         try {
            gen.commentln("%FOPBeginSVG");
            gen.saveGraphicsState();
            gen.writeln("newpath");
            gen.defineRect((double)((float)xOffset / 1000.0F), (double)((float)yOffset / 1000.0F), (double)((float)psInfo.getWidth() / 1000.0F), (double)((float)psInfo.getHeight() / 1000.0F));
            gen.writeln("clip");
            gen.concatMatrix((double)sx, 0.0, 0.0, (double)sy, (double)((float)xOffset / 1000.0F), (double)((float)yOffset / 1000.0F));
            AffineTransform transform = new AffineTransform();
            transform.translate((double)xOffset, (double)yOffset);
            gen.getCurrentState().concatMatrix(transform);

            try {
               root.paint(graphics);
            } catch (Exception var23) {
               SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
               eventProducer.svgRenderingError(this, var23, this.getDocumentURI(doc));
            }

            gen.restoreGraphicsState();
            gen.commentln("%FOPEndSVG");
         } catch (IOException var24) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
            eventProducer.svgRenderingError(this, var24, this.getDocumentURI(doc));
         }

      }
   }

   public boolean supportsRenderer(Renderer renderer) {
      return renderer instanceof PSRenderer;
   }

   static {
      log = LogFactory.getLog(PSSVGHandler.class);
   }

   public static class PSInfo {
      private PSGenerator psGenerator;
      private FontInfo fontInfo;
      private int width;
      private int height;
      private int currentXPosition;
      private int currentYPosition;
      private Configuration cfg;

      public PSGenerator getPSGenerator() {
         return this.psGenerator;
      }

      public void setPsGenerator(PSGenerator psGenerator) {
         this.psGenerator = psGenerator;
      }

      public FontInfo getFontInfo() {
         return this.fontInfo;
      }

      public void setFontInfo(FontInfo fontInfo) {
         this.fontInfo = fontInfo;
      }

      public int getCurrentXPosition() {
         return this.currentXPosition;
      }

      public void setCurrentXPosition(int currentXPosition) {
         this.currentXPosition = currentXPosition;
      }

      public int getCurrentYPosition() {
         return this.currentYPosition;
      }

      public void setCurrentYPosition(int currentYPosition) {
         this.currentYPosition = currentYPosition;
      }

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
         return this.cfg;
      }

      public void setHandlerConfiguration(Configuration cfg) {
         this.cfg = cfg;
      }
   }
}
