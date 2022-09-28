package org.apache.fop.image.loader.batik;

import java.io.IOException;
import java.util.Map;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.XMLNamespaceEnabledImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoader;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;

public class ImageLoaderSVG extends AbstractImageLoader {
   private ImageFlavor targetFlavor;

   public ImageLoaderSVG(ImageFlavor targetFlavor) {
      if (!XMLNamespaceEnabledImageFlavor.SVG_DOM.isCompatible(targetFlavor)) {
         throw new IllegalArgumentException("Incompatible target ImageFlavor: " + targetFlavor);
      } else {
         this.targetFlavor = targetFlavor;
      }
   }

   public ImageFlavor getTargetFlavor() {
      return this.targetFlavor;
   }

   public Image loadImage(ImageInfo info, Map hints, ImageSessionContext session) throws ImageException, IOException {
      if (!"image/svg+xml".equals(info.getMimeType())) {
         throw new IllegalArgumentException("ImageInfo must be from an SVG image");
      } else {
         Image img = info.getOriginalImage();
         if (!(img instanceof ImageXMLDOM)) {
            throw new IllegalArgumentException("ImageInfo was expected to contain the SVG document as DOM");
         } else {
            ImageXMLDOM svgImage = (ImageXMLDOM)img;
            if (!"http://www.w3.org/2000/svg".equals(svgImage.getRootNamespace())) {
               throw new IllegalArgumentException("The Image is not in the SVG namespace: " + svgImage.getRootNamespace());
            } else {
               return svgImage;
            }
         }
      }
   }
}
