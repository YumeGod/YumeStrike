package org.apache.batik.ext.awt.image.codec.imageio;

import java.awt.image.RenderedImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;

public class ImageIOJPEGImageWriter extends ImageIOImageWriter {
   private static final String JPEG_NATIVE_FORMAT = "javax_imageio_jpeg_image_1.0";

   public ImageIOJPEGImageWriter() {
      super("image/jpeg");
   }

   protected IIOMetadata updateMetadata(IIOMetadata var1, ImageWriterParams var2) {
      if ("javax_imageio_jpeg_image_1.0".equals(var1.getNativeMetadataFormatName())) {
         var1 = addAdobeTransform(var1);
         IIOMetadataNode var3 = (IIOMetadataNode)var1.getAsTree("javax_imageio_jpeg_image_1.0");
         IIOMetadataNode var4 = getChildNode(var3, "JPEGvariety");
         if (var4 == null) {
            var4 = new IIOMetadataNode("JPEGvariety");
            var3.appendChild(var4);
         }

         if (var2.getResolution() != null) {
            IIOMetadataNode var5 = getChildNode(var4, "app0JFIF");
            if (var5 == null) {
               var5 = new IIOMetadataNode("app0JFIF");
               var4.appendChild(var5);
            }

            var5.setAttribute("majorVersion", (String)null);
            var5.setAttribute("minorVersion", (String)null);
            var5.setAttribute("resUnits", "1");
            var5.setAttribute("Xdensity", var2.getResolution().toString());
            var5.setAttribute("Ydensity", var2.getResolution().toString());
            var5.setAttribute("thumbWidth", (String)null);
            var5.setAttribute("thumbHeight", (String)null);
         }

         try {
            var1.setFromTree("javax_imageio_jpeg_image_1.0", var3);
         } catch (IIOInvalidTreeException var7) {
            throw new RuntimeException("Cannot update image metadata: " + var7.getMessage(), var7);
         }
      }

      return var1;
   }

   private static IIOMetadata addAdobeTransform(IIOMetadata var0) {
      IIOMetadataNode var1 = (IIOMetadataNode)var0.getAsTree("javax_imageio_jpeg_image_1.0");
      IIOMetadataNode var2 = getChildNode(var1, "markerSequence");
      if (var2 == null) {
         throw new RuntimeException("Invalid metadata!");
      } else {
         IIOMetadataNode var3 = getChildNode(var2, "app14Adobe");
         if (var3 == null) {
            var3 = new IIOMetadataNode("app14Adobe");
            var3.setAttribute("transform", "1");
            var3.setAttribute("version", "101");
            var3.setAttribute("flags0", "0");
            var3.setAttribute("flags1", "0");
            var2.appendChild(var3);
         } else {
            var3.setAttribute("transform", "1");
         }

         try {
            var0.setFromTree("javax_imageio_jpeg_image_1.0", var1);
            return var0;
         } catch (IIOInvalidTreeException var5) {
            throw new RuntimeException("Cannot update image metadata: " + var5.getMessage(), var5);
         }
      }
   }

   protected ImageWriteParam getDefaultWriteParam(ImageWriter var1, RenderedImage var2, ImageWriterParams var3) {
      JPEGImageWriteParam var4 = new JPEGImageWriteParam(var1.getLocale());
      return var4;
   }
}
