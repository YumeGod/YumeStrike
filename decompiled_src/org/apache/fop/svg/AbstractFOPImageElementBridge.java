package org.apache.fop.svg;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.SVGImageElementBridge;
import org.apache.batik.gvt.AbstractGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.impl.ImageGraphics2D;
import org.apache.xmlgraphics.image.loader.impl.ImageRawCCITTFax;
import org.apache.xmlgraphics.image.loader.impl.ImageRawJPEG;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public abstract class AbstractFOPImageElementBridge extends SVGImageElementBridge {
   protected GraphicsNode createImageGraphicsNode(BridgeContext ctx, Element imageElement, ParsedURL purl) {
      AbstractFOPBridgeContext bridgeCtx = (AbstractFOPBridgeContext)ctx;
      ImageManager manager = bridgeCtx.getImageManager();
      ImageSessionContext sessionContext = bridgeCtx.getImageSessionContext();

      try {
         ImageInfo info = manager.getImageInfo(purl.toString(), sessionContext);
         ImageFlavor[] supportedFlavors = this.getSupportedFlavours();
         Image image = manager.getImage(info, supportedFlavors, sessionContext);
         AbstractGraphicsNode specializedNode = null;
         if (image instanceof ImageXMLDOM) {
            ImageXMLDOM xmlImage = (ImageXMLDOM)image;
            if (xmlImage.getDocument() instanceof SVGDocument) {
               return this.createSVGImageNode(ctx, imageElement, (SVGDocument)xmlImage.getDocument());
            }

            image = manager.convertImage(xmlImage, new ImageFlavor[]{ImageFlavor.GRAPHICS2D});
         }

         if (image instanceof ImageRawJPEG) {
            specializedNode = this.createLoaderImageNode(image, ctx, imageElement, purl);
         } else if (image instanceof ImageRawCCITTFax) {
            specializedNode = this.createLoaderImageNode(image, ctx, imageElement, purl);
         } else if (image instanceof ImageGraphics2D) {
            ImageGraphics2D g2dImage = (ImageGraphics2D)image;
            specializedNode = new Graphics2DNode(g2dImage);
         } else {
            ctx.getUserAgent().displayError(new ImageException("Cannot convert an image to a usable format: " + purl));
         }

         Rectangle2D imgBounds = getImageBounds(ctx, imageElement);
         Rectangle2D bounds = ((AbstractGraphicsNode)specializedNode).getPrimitiveBounds();
         float[] vb = new float[]{0.0F, 0.0F, (float)bounds.getWidth(), (float)bounds.getHeight()};
         initializeViewport(ctx, imageElement, (GraphicsNode)specializedNode, vb, imgBounds);
         return (GraphicsNode)specializedNode;
      } catch (Exception var14) {
         ctx.getUserAgent().displayError(var14);
         return this.superCreateGraphicsNode(ctx, imageElement, purl);
      }
   }

   protected GraphicsNode superCreateGraphicsNode(BridgeContext ctx, Element imageElement, ParsedURL purl) {
      return super.createImageGraphicsNode(ctx, imageElement, purl);
   }

   protected abstract ImageFlavor[] getSupportedFlavours();

   protected LoaderImageNode createLoaderImageNode(Image image, BridgeContext ctx, Element imageElement, ParsedURL purl) {
      return new LoaderImageNode(image, ctx, imageElement, purl);
   }

   public class Graphics2DNode extends AbstractGraphicsNode {
      private final ImageGraphics2D image;

      public Graphics2DNode(ImageGraphics2D g2d) {
         this.image = g2d;
      }

      public Shape getOutline() {
         return this.getPrimitiveBounds();
      }

      public void primitivePaint(Graphics2D g2d) {
         int width = this.image.getSize().getWidthPx();
         int height = this.image.getSize().getHeightPx();
         Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, (double)width, (double)height);
         Graphics2DImagePainter painter = this.image.getGraphics2DImagePainter();
         painter.paint(g2d, area);
      }

      public Rectangle2D getGeometryBounds() {
         return this.getPrimitiveBounds();
      }

      public Rectangle2D getPrimitiveBounds() {
         return new Rectangle2D.Double(0.0, 0.0, (double)this.image.getSize().getWidthPx(), (double)this.image.getSize().getHeightPx());
      }

      public Rectangle2D getSensitiveBounds() {
         return this.getPrimitiveBounds();
      }
   }

   public class LoaderImageNode extends AbstractGraphicsNode {
      protected final Image image;
      protected final BridgeContext ctx;
      protected final Element imageElement;
      protected final ParsedURL purl;
      protected GraphicsNode origGraphicsNode = null;

      public LoaderImageNode(Image image, BridgeContext ctx, Element imageElement, ParsedURL purl) {
         this.image = image;
         this.ctx = ctx;
         this.imageElement = imageElement;
         this.purl = purl;
      }

      public Shape getOutline() {
         return this.getPrimitiveBounds();
      }

      public void primitivePaint(Graphics2D g2d) {
         if (g2d instanceof NativeImageHandler) {
            NativeImageHandler nativeImageHandler = (NativeImageHandler)g2d;
            float x = 0.0F;
            float y = 0.0F;

            try {
               float width = (float)this.image.getSize().getWidthPx();
               float height = (float)this.image.getSize().getHeightPx();
               nativeImageHandler.addNativeImage(this.image, x, y, width, height);
            } catch (Exception var7) {
               this.ctx.getUserAgent().displayError(var7);
            }
         } else {
            if (this.origGraphicsNode == null) {
               this.origGraphicsNode = AbstractFOPImageElementBridge.this.superCreateGraphicsNode(this.ctx, this.imageElement, this.purl);
            }

            this.origGraphicsNode.primitivePaint(g2d);
         }

      }

      public Rectangle2D getGeometryBounds() {
         return this.getPrimitiveBounds();
      }

      public Rectangle2D getPrimitiveBounds() {
         return new Rectangle2D.Double(0.0, 0.0, (double)this.image.getSize().getWidthPx(), (double)this.image.getSize().getHeightPx());
      }

      public Rectangle2D getSensitiveBounds() {
         return this.getPrimitiveBounds();
      }
   }
}
