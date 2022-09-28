package org.apache.xmlgraphics.image.loader.impl;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.codec.tiff.TIFFDecodeParam;
import org.apache.xmlgraphics.image.codec.tiff.TIFFImage;
import org.apache.xmlgraphics.image.codec.util.ImageInputStreamSeekableStreamAdapter;
import org.apache.xmlgraphics.image.codec.util.SeekableStream;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;

public class ImageLoaderInternalTIFF extends AbstractImageLoader {
   protected static Log log;

   public ImageFlavor getTargetFlavor() {
      return ImageFlavor.RENDERED_IMAGE;
   }

   public Image loadImage(ImageInfo info, Map hints, ImageSessionContext session) throws ImageException, IOException {
      Source src = session.needSource(info.getOriginalURI());
      ImageInputStream imgStream = ImageUtil.needImageInputStream(src);
      SeekableStream seekStream = new ImageInputStreamSeekableStreamAdapter(imgStream);
      TIFFImage img = new TIFFImage(seekStream, (TIFFDecodeParam)null, 0);
      return new ImageRendered(info, img, (Color)null);
   }

   public int getUsagePenalty() {
      return 1000;
   }

   static {
      log = LogFactory.getLog(ImageLoaderInternalTIFF.class);
   }
}
