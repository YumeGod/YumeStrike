package org.apache.xmlgraphics.image.writer.imageio;

import java.awt.image.RenderedImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import org.apache.xmlgraphics.image.writer.ImageWriterParams;

public class ImageIOJPEGImageWriter extends ImageIOImageWriter {
   private static final String JPEG_NATIVE_FORMAT = "javax_imageio_jpeg_image_1.0";

   public ImageIOJPEGImageWriter() {
      super("image/jpeg");
   }

   protected IIOMetadata updateMetadata(IIOMetadata meta, ImageWriterParams params) {
      if ("javax_imageio_jpeg_image_1.0".equals(meta.getNativeMetadataFormatName())) {
         meta = addAdobeTransform(meta);
         IIOMetadataNode root = (IIOMetadataNode)meta.getAsTree("javax_imageio_jpeg_image_1.0");
         IIOMetadataNode jv = getChildNode(root, "JPEGvariety");
         if (jv == null) {
            jv = new IIOMetadataNode("JPEGvariety");
            root.appendChild(jv);
         }

         if (params.getResolution() != null) {
            IIOMetadataNode child = getChildNode(jv, "app0JFIF");
            if (child == null) {
               child = new IIOMetadataNode("app0JFIF");
               jv.appendChild(child);
            }

            child.setAttribute("majorVersion", (String)null);
            child.setAttribute("minorVersion", (String)null);
            child.setAttribute("resUnits", "1");
            child.setAttribute("Xdensity", params.getResolution().toString());
            child.setAttribute("Ydensity", params.getResolution().toString());
            child.setAttribute("thumbWidth", (String)null);
            child.setAttribute("thumbHeight", (String)null);
         }

         try {
            meta.setFromTree("javax_imageio_jpeg_image_1.0", root);
         } catch (IIOInvalidTreeException var7) {
            throw new RuntimeException("Cannot update image metadata: " + var7.getMessage(), var7);
         }
      }

      return meta;
   }

   private static IIOMetadata addAdobeTransform(IIOMetadata meta) {
      IIOMetadataNode root = (IIOMetadataNode)meta.getAsTree("javax_imageio_jpeg_image_1.0");
      IIOMetadataNode markerSequence = getChildNode(root, "markerSequence");
      if (markerSequence == null) {
         throw new RuntimeException("Invalid metadata!");
      } else {
         IIOMetadataNode adobeTransform = getChildNode(markerSequence, "app14Adobe");
         if (adobeTransform == null) {
            adobeTransform = new IIOMetadataNode("app14Adobe");
            adobeTransform.setAttribute("transform", "1");
            adobeTransform.setAttribute("version", "101");
            adobeTransform.setAttribute("flags0", "0");
            adobeTransform.setAttribute("flags1", "0");
            markerSequence.appendChild(adobeTransform);
         } else {
            adobeTransform.setAttribute("transform", "1");
         }

         try {
            meta.setFromTree("javax_imageio_jpeg_image_1.0", root);
            return meta;
         } catch (IIOInvalidTreeException var5) {
            throw new RuntimeException("Cannot update image metadata: " + var5.getMessage(), var5);
         }
      }
   }

   protected ImageWriteParam getDefaultWriteParam(ImageWriter iiowriter, RenderedImage image, ImageWriterParams params) {
      JPEGImageWriteParam param = new JPEGImageWriteParam(iiowriter.getLocale());
      return param;
   }
}
