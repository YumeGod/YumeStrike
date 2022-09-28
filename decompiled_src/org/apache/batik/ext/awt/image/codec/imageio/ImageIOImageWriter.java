package org.apache.batik.ext.awt.image.codec.imageio;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ImageIOImageWriter implements ImageWriter, IIOWriteWarningListener {
   private String targetMIME;

   public ImageIOImageWriter(String var1) {
      this.targetMIME = var1;
   }

   public void writeImage(RenderedImage var1, OutputStream var2) throws IOException {
      this.writeImage(var1, var2, (ImageWriterParams)null);
   }

   public void writeImage(RenderedImage var1, OutputStream var2, ImageWriterParams var3) throws IOException {
      Iterator var4 = ImageIO.getImageWritersByMIMEType(this.getMIMEType());
      javax.imageio.ImageWriter var5 = null;

      try {
         var5 = (javax.imageio.ImageWriter)var4.next();
         if (var5 == null) {
            throw new UnsupportedOperationException("No ImageIO codec for writing " + this.getMIMEType() + " is available!");
         }

         var5.addIIOWriteWarningListener(this);
         ImageOutputStream var6 = null;

         try {
            var6 = ImageIO.createImageOutputStream(var2);
            ImageWriteParam var7 = this.getDefaultWriteParam(var5, var1, var3);
            ImageTypeSpecifier var8;
            if (var7.getDestinationType() != null) {
               var8 = var7.getDestinationType();
            } else {
               var8 = ImageTypeSpecifier.createFromRenderedImage(var1);
            }

            IIOMetadata var9 = var5.getDefaultImageMetadata(var8, var7);
            if (var3 != null && var9 != null) {
               var9 = this.updateMetadata(var9, var3);
            }

            var5.setOutput(var6);
            IIOImage var10 = new IIOImage(var1, (List)null, var9);
            var5.write((IIOMetadata)null, var10, var7);
         } finally {
            if (var6 != null) {
               System.err.println("closing");
               var6.close();
            }

         }
      } finally {
         if (var5 != null) {
            System.err.println("disposing");
            var5.dispose();
         }

      }

   }

   protected ImageWriteParam getDefaultWriteParam(javax.imageio.ImageWriter var1, RenderedImage var2, ImageWriterParams var3) {
      ImageWriteParam var4 = var1.getDefaultWriteParam();
      System.err.println("Param: " + var3);
      if (var3 != null && var3.getCompressionMethod() != null) {
         var4.setCompressionMode(2);
         var4.setCompressionType(var3.getCompressionMethod());
      }

      return var4;
   }

   protected IIOMetadata updateMetadata(IIOMetadata var1, ImageWriterParams var2) {
      if (var1.isStandardMetadataFormatSupported()) {
         IIOMetadataNode var4 = (IIOMetadataNode)var1.getAsTree("javax_imageio_1.0");
         IIOMetadataNode var5 = getChildNode(var4, "Dimension");
         if (var2.getResolution() != null) {
            IIOMetadataNode var6 = getChildNode(var5, "HorizontalPixelSize");
            if (var6 == null) {
               var6 = new IIOMetadataNode("HorizontalPixelSize");
               var5.appendChild(var6);
            }

            var6.setAttribute("value", Double.toString(var2.getResolution().doubleValue() / 25.4));
            var6 = getChildNode(var5, "VerticalPixelSize");
            if (var6 == null) {
               var6 = new IIOMetadataNode("VerticalPixelSize");
               var5.appendChild(var6);
            }

            var6.setAttribute("value", Double.toString(var2.getResolution().doubleValue() / 25.4));
         }

         try {
            var1.mergeTree("javax_imageio_1.0", var4);
         } catch (IIOInvalidTreeException var8) {
            throw new RuntimeException("Cannot update image metadata: " + var8.getMessage());
         }
      }

      return var1;
   }

   protected static IIOMetadataNode getChildNode(Node var0, String var1) {
      NodeList var2 = var0.getChildNodes();

      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         Node var4 = var2.item(var3);
         if (var1.equals(var4.getNodeName())) {
            return (IIOMetadataNode)var4;
         }
      }

      return null;
   }

   public String getMIMEType() {
      return this.targetMIME;
   }

   public void warningOccurred(javax.imageio.ImageWriter var1, int var2, String var3) {
      System.err.println("Problem while writing image using ImageI/O: " + var3);
   }
}
