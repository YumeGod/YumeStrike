package org.apache.fop.render.intermediate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.ImageHandlerRegistry;
import org.apache.fop.render.ImageHandlerUtil;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.RuleStyle;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.w3c.dom.Document;
import org.xml.sax.Locator;

public abstract class AbstractIFPainter implements IFPainter {
   private static Log log;
   protected static final String INSTREAM_OBJECT_URI = "(instream-object)";
   protected IFState state;

   protected abstract IFContext getContext();

   protected FOUserAgent getUserAgent() {
      return this.getContext().getUserAgent();
   }

   protected FopFactory getFopFactory() {
      return this.getUserAgent().getFactory();
   }

   private AffineTransform combine(AffineTransform[] transforms) {
      AffineTransform at = new AffineTransform();
      int i = 0;

      for(int c = transforms.length; i < c; ++i) {
         at.concatenate(transforms[i]);
      }

      return at;
   }

   public void startViewport(AffineTransform[] transforms, Dimension size, Rectangle clipRect) throws IFException {
      this.startViewport(this.combine(transforms), size, clipRect);
   }

   public void startGroup(AffineTransform[] transforms) throws IFException {
      this.startGroup(this.combine(transforms));
   }

   protected abstract RenderingContext createRenderingContext();

   protected void drawImageUsingImageHandler(ImageInfo info, Rectangle rect) throws ImageException, IOException {
      ImageManager manager = this.getFopFactory().getImageManager();
      ImageSessionContext sessionContext = this.getUserAgent().getImageSessionContext();
      ImageHandlerRegistry imageHandlerRegistry = this.getFopFactory().getImageHandlerRegistry();
      RenderingContext context = this.createRenderingContext();
      Map hints = this.createDefaultImageProcessingHints(sessionContext);
      context.putHints(hints);
      ImageFlavor[] flavors = imageHandlerRegistry.getSupportedFlavors(context);
      Image img = manager.getImage(info, flavors, hints, sessionContext);

      try {
         this.drawImage(img, rect, context);
      } catch (IOException var12) {
         ResourceEventProducer eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageWritingError(this, var12);
      }

   }

   protected Map createDefaultImageProcessingHints(ImageSessionContext sessionContext) {
      Map hints = ImageUtil.getDefaultHints(sessionContext);
      Object conversionMode = this.getContext().getForeignAttribute(ImageHandlerUtil.CONVERSION_MODE);
      if (conversionMode != null) {
         hints.put(ImageHandlerUtil.CONVERSION_MODE, conversionMode);
      }

      return hints;
   }

   protected void drawImage(Image image, Rectangle rect, RenderingContext context) throws IOException, ImageException {
      this.drawImage(image, rect, context, false, (Map)null);
   }

   protected void drawImage(Image image, Rectangle rect, RenderingContext context, boolean convert, Map additionalHints) throws IOException, ImageException {
      ImageManager manager = this.getFopFactory().getImageManager();
      ImageHandlerRegistry imageHandlerRegistry = this.getFopFactory().getImageHandlerRegistry();
      context.putHints(additionalHints);
      Image effImage;
      if (convert) {
         Map hints = this.createDefaultImageProcessingHints(this.getUserAgent().getImageSessionContext());
         if (additionalHints != null) {
            hints.putAll(additionalHints);
         }

         effImage = manager.convertImage(image, imageHandlerRegistry.getSupportedFlavors(context), hints);
      } else {
         effImage = image;
      }

      ImageHandler handler = imageHandlerRegistry.getHandler(context, effImage);
      if (handler == null) {
         throw new UnsupportedOperationException("No ImageHandler available for image: " + effImage.getInfo() + " (" + effImage.getClass().getName() + ")");
      } else {
         if (log.isTraceEnabled()) {
            log.trace("Using ImageHandler: " + handler.getClass().getName());
         }

         handler.handleImage(context, effImage, rect);
      }
   }

   protected ImageInfo getImageInfo(String uri) {
      ImageManager manager = this.getFopFactory().getImageManager();

      ResourceEventProducer eventProducer;
      try {
         ImageSessionContext sessionContext = this.getUserAgent().getImageSessionContext();
         return manager.getImageInfo(uri, sessionContext);
      } catch (ImageException var5) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageError(this, uri, var5, (Locator)null);
      } catch (FileNotFoundException var6) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageNotFound(this, uri, var6, (Locator)null);
      } catch (IOException var7) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageIOError(this, uri, var7, (Locator)null);
      }

      return null;
   }

   protected void drawImageUsingURI(String uri, Rectangle rect) {
      ImageManager manager = this.getFopFactory().getImageManager();
      ImageInfo info = null;

      ResourceEventProducer eventProducer;
      try {
         ImageSessionContext sessionContext = this.getUserAgent().getImageSessionContext();
         info = manager.getImageInfo(uri, sessionContext);
         this.drawImageUsingImageHandler(info, rect);
      } catch (ImageException var7) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageError(this, info != null ? info.toString() : uri, var7, (Locator)null);
      } catch (FileNotFoundException var8) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageNotFound(this, info != null ? info.toString() : uri, var8, (Locator)null);
      } catch (IOException var9) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageIOError(this, info != null ? info.toString() : uri, var9, (Locator)null);
      }

   }

   protected void drawImageUsingDocument(Document doc, Rectangle rect) {
      ImageManager manager = this.getFopFactory().getImageManager();
      ImageInfo info = null;

      ResourceEventProducer eventProducer;
      try {
         info = manager.preloadImage((String)null, (Source)(new DOMSource(doc)));
         this.drawImageUsingImageHandler(info, rect);
      } catch (ImageException var7) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageError(this, info != null ? info.toString() : "(instream-object)", var7, (Locator)null);
      } catch (FileNotFoundException var8) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageNotFound(this, info != null ? info.toString() : "(instream-object)", var8, (Locator)null);
      } catch (IOException var9) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageIOError(this, info != null ? info.toString() : "(instream-object)", var9, (Locator)null);
      }

   }

   public void drawBorderRect(Rectangle rect, BorderProps before, BorderProps after, BorderProps start, BorderProps end) throws IFException {
      Rectangle b;
      if (before != null) {
         b = new Rectangle(rect.x, rect.y, rect.width, before.width);
         this.fillRect(b, before.color);
      }

      if (end != null) {
         b = new Rectangle(rect.x + rect.width - end.width, rect.y, end.width, rect.height);
         this.fillRect(b, end.color);
      }

      if (after != null) {
         b = new Rectangle(rect.x, rect.y + rect.height - after.width, rect.width, after.width);
         this.fillRect(b, after.color);
      }

      if (start != null) {
         b = new Rectangle(rect.x, rect.y, start.width, rect.height);
         this.fillRect(b, start.color);
      }

   }

   public void drawLine(Point start, Point end, int width, Color color, RuleStyle style) throws IFException {
      Rectangle rect = this.getLineBoundingBox(start, end, width);
      this.fillRect(rect, color);
   }

   protected Rectangle getLineBoundingBox(Point start, Point end, int width) {
      int leftx;
      if (start.y == end.y) {
         leftx = start.y - width / 2;
         return new Rectangle(start.x, leftx, end.x - start.x, width);
      } else if (start.x == end.y) {
         leftx = start.x - width / 2;
         return new Rectangle(leftx, start.x, width, end.y - start.y);
      } else {
         throw new IllegalArgumentException("Only horizontal or vertical lines are supported at the moment.");
      }
   }

   public void setFont(String family, String style, Integer weight, String variant, Integer size, Color color) throws IFException {
      if (family != null) {
         this.state.setFontFamily(family);
      }

      if (style != null) {
         this.state.setFontStyle(style);
      }

      if (weight != null) {
         this.state.setFontWeight(weight);
      }

      if (variant != null) {
         this.state.setFontVariant(variant);
      }

      if (size != null) {
         this.state.setFontSize(size);
      }

      if (color != null) {
         this.state.setTextColor(color);
      }

   }

   public static AffineTransform toPoints(AffineTransform transform) {
      double[] matrix = new double[6];
      transform.getMatrix(matrix);
      matrix[4] /= 1000.0;
      matrix[5] /= 1000.0;
      return new AffineTransform(matrix);
   }

   static {
      log = LogFactory.getLog(AbstractIFPainter.class);
   }
}
