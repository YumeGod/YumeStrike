package org.apache.fop.image.loader.batik;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.parser.UnitProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.svg.SimpleSVGUserAgent;
import org.apache.fop.util.UnclosableInputStream;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.impl.AbstractImagePreloader;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public class PreloaderSVG extends AbstractImagePreloader {
   private static Log log;
   private boolean batikAvailable = true;

   public ImageInfo preloadImage(String uri, Source src, ImageContext context) throws IOException {
      ImageInfo info = null;
      if (this.batikAvailable) {
         try {
            Loader loader = new Loader();
            if (!loader.isSupportedSource(src)) {
               return null;
            }

            info = loader.getImage(uri, src, context);
         } catch (NoClassDefFoundError var6) {
            this.batikAvailable = false;
            log.warn("Batik not in class path", var6);
            return null;
         }
      }

      if (info != null) {
         ImageUtil.closeQuietly(src);
      }

      return info;
   }

   public static String getParserName() {
      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
         return factory.newSAXParser().getXMLReader().getClass().getName();
      } catch (Exception var1) {
         return null;
      }
   }

   private static double px2mpt(double px, double resolution) {
      return px * 1000.0 * 72.0 / resolution;
   }

   static {
      log = LogFactory.getLog(PreloaderSVG.class);
   }

   class Loader {
      private ImageInfo getImage(String uri, Source src, ImageContext context) {
         InputStream in = null;

         try {
            SVGDocument doc;
            if (src instanceof DOMSource) {
               DOMSource domSrc = (DOMSource)src;
               doc = (SVGDocument)domSrc.getNode();
            } else {
               in = new UnclosableInputStream(ImageUtil.needInputStream(src));
               int length = in.available();
               in.mark(length + 1);
               SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(PreloaderSVG.getParserName());
               doc = factory.createSVGDocument(src.getSystemId(), (InputStream)in);
            }

            ImageInfo info = this.createImageInfo(uri, context, doc);
            return info;
         } catch (NoClassDefFoundError var10) {
            if (in != null) {
               try {
                  in.reset();
               } catch (IOException var9) {
               }
            }

            PreloaderSVG.this.batikAvailable = false;
            PreloaderSVG.log.warn("Batik not in class path", var10);
            return null;
         } catch (IOException var11) {
            PreloaderSVG.log.debug("Error while trying to load stream as an SVG file: " + var11.getMessage());

            try {
               in.reset();
            } catch (IOException var8) {
            }

            return null;
         }
      }

      private ImageInfo createImageInfo(String uri, ImageContext context, SVGDocument doc) {
         Element e = doc.getRootElement();
         float pxUnitToMillimeter = 25.4F / context.getSourceResolution();
         UserAgent userAg = new SimpleSVGUserAgent(pxUnitToMillimeter, new AffineTransform()) {
            public void displayMessage(String message) {
               PreloaderSVG.log.debug(message);
            }
         };
         BridgeContext ctx = new BridgeContext(userAg);
         UnitProcessor.Context uctx = org.apache.batik.bridge.UnitProcessor.createContext(ctx, e);
         String s = e.getAttributeNS((String)null, "width");
         if (s.length() == 0) {
            s = "100%";
         }

         float width = org.apache.batik.bridge.UnitProcessor.svgHorizontalLengthToUserSpace(s, "width", uctx);
         s = e.getAttributeNS((String)null, "height");
         if (s.length() == 0) {
            s = "100%";
         }

         float height = org.apache.batik.bridge.UnitProcessor.svgVerticalLengthToUserSpace(s, "height", uctx);
         int widthMpt = (int)Math.round(PreloaderSVG.px2mpt((double)width, (double)context.getSourceResolution()));
         int heightMpt = (int)Math.round(PreloaderSVG.px2mpt((double)height, (double)context.getSourceResolution()));
         ImageInfo info = new ImageInfo(uri, "image/svg+xml");
         ImageSize size = new ImageSize();
         size.setSizeInMillipoints(widthMpt, heightMpt);
         size.setResolution((double)context.getSourceResolution());
         size.calcPixelsFromSize();
         info.setSize(size);
         ImageXMLDOM xmlImage = new ImageXMLDOM(info, doc, BatikImageFlavors.SVG_DOM);
         info.getCustomObjects().put(ImageInfo.ORIGINAL_IMAGE, xmlImage);
         return info;
      }

      private boolean isSupportedSource(Source src) {
         if (src instanceof DOMSource) {
            DOMSource domSrc = (DOMSource)src;
            return domSrc.getNode() instanceof SVGDocument;
         } else {
            return ImageUtil.hasInputStream(src);
         }
      }
   }
}
