package org.apache.xmlgraphics.image.writer.imageio;

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
import org.apache.xmlgraphics.image.writer.ImageWriter;
import org.apache.xmlgraphics.image.writer.ImageWriterParams;
import org.apache.xmlgraphics.image.writer.MultiImageWriter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ImageIOImageWriter implements ImageWriter, IIOWriteWarningListener {
   private static final String STANDARD_METADATA_FORMAT = "javax_imageio_1.0";
   private String targetMIME;

   public ImageIOImageWriter(String mime) {
      this.targetMIME = mime;
   }

   public void writeImage(RenderedImage image, OutputStream out) throws IOException {
      this.writeImage(image, out, (ImageWriterParams)null);
   }

   public void writeImage(RenderedImage image, OutputStream out, ImageWriterParams params) throws IOException {
      javax.imageio.ImageWriter iiowriter = this.getIIOImageWriter();
      iiowriter.addIIOWriteWarningListener(this);
      ImageOutputStream imgout = ImageIO.createImageOutputStream(out);

      try {
         ImageWriteParam iwParam = this.getDefaultWriteParam(iiowriter, image, params);
         ImageTypeSpecifier type;
         if (iwParam.getDestinationType() != null) {
            type = iwParam.getDestinationType();
         } else {
            type = ImageTypeSpecifier.createFromRenderedImage(image);
         }

         IIOMetadata meta = iiowriter.getDefaultImageMetadata(type, iwParam);
         if (params != null && meta != null) {
            meta = this.updateMetadata(meta, params);
         }

         iiowriter.setOutput(imgout);
         IIOImage iioimg = new IIOImage(image, (List)null, meta);
         iiowriter.write((IIOMetadata)null, iioimg, iwParam);
      } finally {
         imgout.close();
         iiowriter.dispose();
      }

   }

   private javax.imageio.ImageWriter getIIOImageWriter() {
      Iterator iter = ImageIO.getImageWritersByMIMEType(this.getMIMEType());
      javax.imageio.ImageWriter iiowriter = null;
      if (iter.hasNext()) {
         iiowriter = (javax.imageio.ImageWriter)iter.next();
      }

      if (iiowriter == null) {
         throw new UnsupportedOperationException("No ImageIO codec for writing " + this.getMIMEType() + " is available!");
      } else {
         return iiowriter;
      }
   }

   protected ImageWriteParam getDefaultWriteParam(javax.imageio.ImageWriter iiowriter, RenderedImage image, ImageWriterParams params) {
      ImageWriteParam param = iiowriter.getDefaultWriteParam();
      if (params != null && params.getCompressionMethod() != null) {
         param.setCompressionMode(2);
         param.setCompressionType(params.getCompressionMethod());
      }

      return param;
   }

   protected IIOMetadata updateMetadata(IIOMetadata meta, ImageWriterParams params) {
      if (meta.isStandardMetadataFormatSupported()) {
         IIOMetadataNode root = (IIOMetadataNode)meta.getAsTree("javax_imageio_1.0");
         IIOMetadataNode dim = getChildNode(root, "Dimension");
         if (params.getResolution() != null) {
            IIOMetadataNode child = getChildNode(dim, "HorizontalPixelSize");
            if (child == null) {
               child = new IIOMetadataNode("HorizontalPixelSize");
               dim.appendChild(child);
            }

            child.setAttribute("value", Double.toString(params.getResolution().doubleValue() / 25.4));
            child = getChildNode(dim, "VerticalPixelSize");
            if (child == null) {
               child = new IIOMetadataNode("VerticalPixelSize");
               dim.appendChild(child);
            }

            child.setAttribute("value", Double.toString(params.getResolution().doubleValue() / 25.4));
         }

         try {
            meta.mergeTree("javax_imageio_1.0", root);
         } catch (IIOInvalidTreeException var7) {
            throw new RuntimeException("Cannot update image metadata: " + var7.getMessage());
         }
      }

      return meta;
   }

   protected static IIOMetadataNode getChildNode(Node n, String name) {
      NodeList nodes = n.getChildNodes();

      for(int i = 0; i < nodes.getLength(); ++i) {
         Node child = nodes.item(i);
         if (name.equals(child.getNodeName())) {
            return (IIOMetadataNode)child;
         }
      }

      return null;
   }

   public String getMIMEType() {
      return this.targetMIME;
   }

   public boolean isFunctional() {
      Iterator iter = ImageIO.getImageWritersByMIMEType(this.getMIMEType());
      return iter.hasNext();
   }

   public void warningOccurred(javax.imageio.ImageWriter source, int imageIndex, String warning) {
      System.err.println("Problem while writing image using ImageI/O: " + warning);
   }

   public MultiImageWriter createMultiImageWriter(OutputStream out) throws IOException {
      return new IIOMultiImageWriter(out);
   }

   public boolean supportsMultiImageWriter() {
      javax.imageio.ImageWriter iiowriter = this.getIIOImageWriter();

      boolean var2;
      try {
         var2 = iiowriter.canWriteSequence();
      } finally {
         iiowriter.dispose();
      }

      return var2;
   }

   private class IIOMultiImageWriter implements MultiImageWriter {
      private javax.imageio.ImageWriter iiowriter = ImageIOImageWriter.this.getIIOImageWriter();
      private ImageOutputStream imageStream;

      public IIOMultiImageWriter(OutputStream out) throws IOException {
         if (!this.iiowriter.canWriteSequence()) {
            throw new UnsupportedOperationException("This ImageWriter does not support writing multiple images to a single image file.");
         } else {
            this.iiowriter.addIIOWriteWarningListener(ImageIOImageWriter.this);
            this.imageStream = ImageIO.createImageOutputStream(out);
            this.iiowriter.setOutput(this.imageStream);
            this.iiowriter.prepareWriteSequence((IIOMetadata)null);
         }
      }

      public void writeImage(RenderedImage image, ImageWriterParams params) throws IOException {
         if (this.iiowriter == null) {
            throw new IllegalStateException("MultiImageWriter already closed!");
         } else {
            ImageWriteParam iwParam = ImageIOImageWriter.this.getDefaultWriteParam(this.iiowriter, image, params);
            ImageTypeSpecifier type;
            if (iwParam.getDestinationType() != null) {
               type = iwParam.getDestinationType();
            } else {
               type = ImageTypeSpecifier.createFromRenderedImage(image);
            }

            IIOMetadata meta = this.iiowriter.getDefaultImageMetadata(type, iwParam);
            if (params != null && meta != null) {
               meta = ImageIOImageWriter.this.updateMetadata(meta, params);
            }

            IIOImage iioimg = new IIOImage(image, (List)null, meta);
            this.iiowriter.writeToSequence(iioimg, iwParam);
         }
      }

      public void close() throws IOException {
         this.imageStream.close();
         this.imageStream = null;
         this.iiowriter.dispose();
         this.iiowriter = null;
      }
   }
}
