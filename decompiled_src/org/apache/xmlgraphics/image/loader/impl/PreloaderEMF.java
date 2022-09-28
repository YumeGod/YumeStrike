package org.apache.xmlgraphics.image.loader.impl;

import java.io.IOException;
import java.nio.ByteOrder;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.apache.xmlgraphics.util.UnitConv;

public class PreloaderEMF extends AbstractImagePreloader {
   protected static final int EMF_SIG_LENGTH = 88;
   private static final int SIGNATURE_OFFSET = 40;
   private static final int WIDTH_OFFSET = 32;
   private static final int HEIGHT_OFFSET = 36;
   private static final int HRES_PIXEL_OFFSET = 72;
   private static final int VRES_PIXEL_OFFSET = 76;
   private static final int HRES_MM_OFFSET = 80;
   private static final int VRES_MM_OFFSET = 84;

   public ImageInfo preloadImage(String uri, Source src, ImageContext context) throws IOException, ImageException {
      if (!ImageUtil.hasImageInputStream(src)) {
         return null;
      } else {
         ImageInputStream in = ImageUtil.needImageInputStream(src);
         byte[] header = this.getHeader(in, 88);
         boolean supported = header[40] == 32 && header[41] == 69 && header[42] == 77 && header[43] == 70;
         if (supported) {
            ImageInfo info = new ImageInfo(uri, "image/emf");
            info.setSize(this.determineSize(in, context));
            return info;
         } else {
            return null;
         }
      }
   }

   private ImageSize determineSize(ImageInputStream in, ImageContext context) throws IOException, ImageException {
      in.mark();
      ByteOrder oldByteOrder = in.getByteOrder();

      ImageSize var19;
      try {
         ImageSize size = new ImageSize();
         in.setByteOrder(ByteOrder.LITTLE_ENDIAN);
         in.skipBytes(32);
         int width = (int)in.readUnsignedInt();
         int height = (int)in.readUnsignedInt();
         in.skipBytes(32);
         long hresPixel = in.readUnsignedInt();
         long vresPixel = in.readUnsignedInt();
         long hresMM = in.readUnsignedInt();
         long vresMM = in.readUnsignedInt();
         double resHorz = (double)hresPixel / UnitConv.mm2in((double)hresMM);
         double resVert = (double)vresPixel / UnitConv.mm2in((double)vresMM);
         size.setResolution(resHorz, resVert);
         width = (int)Math.round(UnitConv.mm2mpt((double)((float)width / 100.0F)));
         height = (int)Math.round(UnitConv.mm2mpt((double)((float)height / 100.0F)));
         size.setSizeInMillipoints(width, height);
         size.calcPixelsFromSize();
         var19 = size;
      } finally {
         in.setByteOrder(oldByteOrder);
         in.reset();
      }

      return var19;
   }
}
