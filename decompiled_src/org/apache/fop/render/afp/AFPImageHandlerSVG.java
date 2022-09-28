package org.apache.fop.render.afp;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.fop.afp.AFPDataObjectInfo;
import org.apache.fop.afp.AFPGraphics2D;
import org.apache.fop.afp.AFPGraphicsObjectInfo;
import org.apache.fop.afp.AFPObjectAreaInfo;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPResourceInfo;
import org.apache.fop.afp.AFPResourceLevel;
import org.apache.fop.afp.AFPResourceManager;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.image.loader.batik.BatikImageFlavors;
import org.apache.fop.image.loader.batik.BatikUtil;
import org.apache.fop.image.loader.batik.Graphics2DImagePainterImpl;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.ImageHandlerUtil;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.svg.SVGEventProducer;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.w3c.dom.Document;

public class AFPImageHandlerSVG implements ImageHandler {
   private static final ImageFlavor[] FLAVORS;

   protected AFPDataObjectInfo createDataObjectInfo() {
      return new AFPGraphicsObjectInfo();
   }

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      AFPRenderingContext afpContext = (AFPRenderingContext)context;
      ImageXMLDOM imageSVG = (ImageXMLDOM)image;
      FOUserAgent userAgent = afpContext.getUserAgent();
      AFPGraphicsObjectInfo graphicsObjectInfo = (AFPGraphicsObjectInfo)this.createDataObjectInfo();
      AFPResourceInfo resourceInfo = graphicsObjectInfo.getResourceInfo();
      this.setDefaultToInlineResourceLevel(graphicsObjectInfo);
      boolean textAsShapes = false;
      AFPGraphics2D g2d = new AFPGraphics2D(false, afpContext.getPaintingState(), afpContext.getResourceManager(), resourceInfo, afpContext.getFontInfo());
      g2d.setGraphicContext(new GraphicContext());
      AFPPaintingState paintingState = g2d.getPaintingState();
      paintingState.setImageUri(image.getInfo().getOriginalURI());
      BridgeContext bridgeContext = AFPSVGHandler.createBridgeContext(userAgent, g2d);
      Document clonedDoc = BatikUtil.cloneSVGDocument(imageSVG.getDocument());

      GraphicsNode root;
      try {
         GVTBuilder builder = new GVTBuilder();
         root = builder.build(bridgeContext, clonedDoc);
      } catch (Exception var20) {
         SVGEventProducer eventProducer = SVGEventProducer.Provider.get(context.getUserAgent().getEventBroadcaster());
         eventProducer.svgNotBuilt(this, var20, image.getInfo().getOriginalURI());
         return;
      }

      AFPObjectAreaInfo objectAreaInfo = AFPImageHandler.createObjectAreaInfo(paintingState, pos);
      graphicsObjectInfo.setObjectAreaInfo(objectAreaInfo);
      paintingState.save();
      AffineTransform placement = new AffineTransform();
      placement.translate((double)pos.x, (double)pos.y);
      paintingState.concatenate(placement);
      graphicsObjectInfo.setGraphics2D(g2d);
      Dimension imageSize = image.getSize().getDimensionMpt();
      Graphics2DImagePainter painter = new Graphics2DImagePainterImpl(root, bridgeContext, imageSize);
      graphicsObjectInfo.setPainter(painter);
      AFPResourceManager resourceManager = afpContext.getResourceManager();
      resourceManager.createObject(graphicsObjectInfo);
      paintingState.restore();
   }

   private void setDefaultToInlineResourceLevel(AFPGraphicsObjectInfo graphicsObjectInfo) {
      AFPResourceInfo resourceInfo = graphicsObjectInfo.getResourceInfo();
      if (!resourceInfo.levelChanged()) {
         resourceInfo.setLevel(new AFPResourceLevel(0));
      }

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
      boolean supported = (image == null || image instanceof ImageXMLDOM && image.getFlavor().isCompatible(BatikImageFlavors.SVG_DOM)) && targetContext instanceof AFPRenderingContext;
      if (supported) {
         String mode = (String)targetContext.getHint(ImageHandlerUtil.CONVERSION_MODE);
         if (ImageHandlerUtil.isConversionModeBitmap(mode)) {
            return false;
         }
      }

      return supported;
   }

   static {
      FLAVORS = new ImageFlavor[]{BatikImageFlavors.SVG_DOM};
   }
}
