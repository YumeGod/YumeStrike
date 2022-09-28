package org.apache.fop.render.pdf;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.image.loader.batik.BatikUtil;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFPage;
import org.apache.fop.pdf.PDFResourceContext;
import org.apache.fop.render.AbstractGenericSVGHandler;
import org.apache.fop.render.ImageHandlerUtil;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererContext;
import org.apache.fop.svg.PDFAElementBridge;
import org.apache.fop.svg.PDFBridgeContext;
import org.apache.fop.svg.PDFGraphics2D;
import org.apache.fop.svg.SVGEventProducer;
import org.apache.fop.svg.SVGUserAgent;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.w3c.dom.Document;

public class PDFSVGHandler extends AbstractGenericSVGHandler implements PDFRendererContextConstants {
   private static Log log;

   public static PDFInfo getPDFInfo(RendererContext context) {
      PDFInfo pdfi = new PDFInfo();
      pdfi.pdfDoc = (PDFDocument)context.getProperty("pdfDoc");
      pdfi.outputStream = (OutputStream)context.getProperty("outputStream");
      pdfi.pdfPage = (PDFPage)context.getProperty("pdfPage");
      pdfi.pdfContext = (PDFResourceContext)context.getProperty("pdfContext");
      pdfi.width = (Integer)context.getProperty("width");
      pdfi.height = (Integer)context.getProperty("height");
      pdfi.fi = (FontInfo)context.getProperty("fontInfo");
      pdfi.currentFontName = (String)context.getProperty("fontName");
      pdfi.currentFontSize = (Integer)context.getProperty("fontSize");
      pdfi.currentXPosition = (Integer)context.getProperty("xpos");
      pdfi.currentYPosition = (Integer)context.getProperty("ypos");
      pdfi.cfg = (Configuration)context.getProperty("cfg");
      Map foreign = (Map)context.getProperty("foreign-attributes");
      pdfi.paintAsBitmap = ImageHandlerUtil.isConversionModeBitmap(foreign);
      return pdfi;
   }

   protected void renderSVGDocument(RendererContext context, Document doc) {
      PDFRenderer renderer = (PDFRenderer)context.getRenderer();
      PDFInfo pdfInfo = getPDFInfo(context);
      if (pdfInfo.paintAsBitmap) {
         try {
            super.renderSVGDocument(context, doc);
         } catch (IOException var30) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
            eventProducer.svgRenderingError(this, var30, this.getDocumentURI(doc));
         }

      } else {
         int xOffset = pdfInfo.currentXPosition;
         int yOffset = pdfInfo.currentYPosition;
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
         boolean strokeText = false;
         Configuration cfg = pdfInfo.cfg;
         if (cfg != null) {
            strokeText = cfg.getChild("stroke-text", true).getValueAsBoolean(strokeText);
         }

         BridgeContext ctx = new PDFBridgeContext(ua, strokeText ? null : pdfInfo.fi, userAgent.getFactory().getImageManager(), userAgent.getImageSessionContext(), new AffineTransform());
         Document clonedDoc = BatikUtil.cloneSVGDocument(doc);

         GraphicsNode root;
         try {
            GVTBuilder builder = new GVTBuilder();
            root = builder.build(ctx, (Document)clonedDoc);
         } catch (Exception var32) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
            eventProducer.svgNotBuilt(this, var32, this.getDocumentURI(doc));
            return;
         }

         float w = (float)ctx.getDocumentSize().getWidth() * 1000.0F;
         float h = (float)ctx.getDocumentSize().getHeight() * 1000.0F;
         float sx = (float)pdfInfo.width / w;
         float sy = (float)pdfInfo.height / h;
         AffineTransform scaling = new AffineTransform(sx, 0.0F, 0.0F, sy, (float)xOffset / 1000.0F, (float)yOffset / 1000.0F);
         AffineTransform imageTransform = new AffineTransform();
         imageTransform.concatenate(scaling);
         imageTransform.concatenate(resolutionScaling);
         PDFContentGenerator generator = renderer.getGenerator();
         generator.comment("SVG setup");
         generator.saveGraphicsState();
         generator.setColor(Color.black, false);
         generator.setColor(Color.black, true);
         if (!scaling.isIdentity()) {
            generator.comment("viewbox");
            generator.add(CTMHelper.toPDFString(scaling, false) + " cm\n");
         }

         if (pdfInfo.pdfContext == null) {
            pdfInfo.pdfContext = pdfInfo.pdfPage;
         }

         PDFGraphics2D graphics = new PDFGraphics2D(true, pdfInfo.fi, pdfInfo.pdfDoc, pdfInfo.pdfContext, pdfInfo.pdfPage.referencePDF(), pdfInfo.currentFontName, (float)pdfInfo.currentFontSize);
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
         graphics.setOutputStream(pdfInfo.outputStream);

         try {
            root.paint(graphics);
            generator.add(graphics.getString());
         } catch (Exception var31) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
            eventProducer.svgRenderingError(this, var31, this.getDocumentURI(doc));
         }

         generator.getState().restore();
         generator.restoreGraphicsState();
         generator.comment("SVG end");
      }
   }

   public boolean supportsRenderer(Renderer renderer) {
      return renderer instanceof PDFRenderer;
   }

   static {
      log = LogFactory.getLog(PDFSVGHandler.class);
   }

   public static class PDFInfo {
      public PDFDocument pdfDoc;
      public OutputStream outputStream;
      public PDFPage pdfPage;
      public PDFResourceContext pdfContext;
      public int width;
      public int height;
      public FontInfo fi;
      public String currentFontName;
      public int currentFontSize;
      public int currentXPosition;
      public int currentYPosition;
      public Configuration cfg;
      public boolean paintAsBitmap;
   }
}
