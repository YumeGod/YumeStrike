package org.apache.xmlgraphics.image.writer.imageio;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import org.apache.xmlgraphics.image.writer.ImageWriterParams;

public class ImageIOTIFFImageWriter extends ImageIOImageWriter {
   private static final String SUN_TIFF_NATIVE_FORMAT = "com_sun_media_imageio_plugins_tiff_image_1.0";

   public ImageIOTIFFImageWriter() {
      super("image/tiff");
   }

   protected IIOMetadata updateMetadata(IIOMetadata meta, ImageWriterParams params) {
      IIOMetadata ret = super.updateMetadata(meta, params);
      if (params.getResolution() != null && "com_sun_media_imageio_plugins_tiff_image_1.0".equals(meta.getNativeMetadataFormatName())) {
         IIOMetadataNode root = new IIOMetadataNode("com_sun_media_imageio_plugins_tiff_image_1.0");
         IIOMetadataNode ifd = getChildNode(root, "TIFFIFD");
         if (ifd == null) {
            ifd = new IIOMetadataNode("TIFFIFD");
            ifd.setAttribute("tagSets", "com.sun.media.imageio.plugins.tiff.BaselineTIFFTagSet");
            root.appendChild(ifd);
         }

         ifd.appendChild(this.createResolutionField(282, "XResolution", params));
         ifd.appendChild(this.createResolutionField(283, "YResolution", params));
         IIOMetadataNode field = new IIOMetadataNode("TIFFField");
         field.setAttribute("number", Integer.toString(296));
         field.setAttribute("name", "ResolutionUnit");
         IIOMetadataNode arrayNode = new IIOMetadataNode("TIFFShorts");
         field.appendChild(arrayNode);
         IIOMetadataNode valueNode = new IIOMetadataNode("TIFFShort");
         valueNode.setAttribute("value", Integer.toString(3));
         valueNode.setAttribute("description", "Centimeter");
         arrayNode.appendChild(valueNode);

         try {
            meta.mergeTree("com_sun_media_imageio_plugins_tiff_image_1.0", root);
         } catch (IIOInvalidTreeException var10) {
            throw new RuntimeException("Cannot update image metadata: " + var10.getMessage(), var10);
         }
      }

      return ret;
   }

   private IIOMetadataNode createResolutionField(int number, String name, ImageWriterParams params) {
      IIOMetadataNode field = new IIOMetadataNode("TIFFField");
      field.setAttribute("number", Integer.toString(number));
      field.setAttribute("name", name);
      IIOMetadataNode arrayNode = new IIOMetadataNode("TIFFRationals");
      field.appendChild(arrayNode);
      IIOMetadataNode valueNode = new IIOMetadataNode("TIFFRational");
      arrayNode.appendChild(valueNode);
      float pixSzMM = 25.4F / params.getResolution().floatValue();
      int numPix = (int)((double)(100000.0F / pixSzMM) + 0.5);
      int denom = 10000;
      valueNode.setAttribute("value", numPix + "/" + denom);
      return field;
   }
}
